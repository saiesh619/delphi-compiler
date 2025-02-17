import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
 
public class DelphiInterpreter extends delphiBaseVisitor <Object> {
    private Map<String, Integer> variables = new HashMap<>();
    private Scanner scanner = new Scanner(System.in);
    private Map<String, Map<String, Object>> objects = new HashMap<>();
    private Map<String, Map<String, Object>> classDefinitions = new HashMap<>();

    
    
    @Override
    public Object visitMethodImplementation(delphiParser.MethodImplementationContext ctx) {
    String methodName = ctx.qualifiedIdent().getText();
    System.out.println("Defining method: " + methodName);

    // 🔹 Extract class name
    String[] parts = methodName.split("\\.");
    if (parts.length != 2) {
        throw new RuntimeException("Invalid method definition: " + methodName);
    }
    
    String className = parts[0];  // e.g., `TCalculator`
    String method = parts[1];     // e.g., `ReadNumber`
   
    // 🔹 Ensure class exists before storing the method
    if (!classDefinitions.containsKey(className)) {
        throw new RuntimeException("Class not defined: " + className);
    }

    // 🔹 Store method implementation
    classDefinitions.get(className).put(method, ctx.compoundStatement());
    System.out.println("Method " + method + " added to class " + className);

    return null;
}

public Object visitClassType(delphiParser.ClassTypeContext ctx, String className) {
    // Retrieve stored class definition
    Map<String, Object> classInfo = classDefinitions.get(className);
    if (classInfo == null) {
        throw new RuntimeException("Class " + className + " was not registered before visiting classType.");
    }

    System.out.println("Processing members of class: " + className);

    // Iterate over class members
    if (ctx.classMemberList() != null) {
        for (ParseTree member : ctx.classMemberList().children) {
            if (member instanceof delphiParser.FieldDeclarationContext) {
                delphiParser.FieldDeclarationContext fieldCtx = (delphiParser.FieldDeclarationContext) member;
                String fieldName = fieldCtx.getChild(0).getText();
                classInfo.put(fieldName, 0);  // Default field value
                System.out.println(" -> Field: " + fieldName);
            } else if (member instanceof delphiParser.MethodDeclarationContext) {
                delphiParser.MethodDeclarationContext methodCtx = (delphiParser.MethodDeclarationContext) member;
                String methodName = methodCtx.getChild(1).getText();
                classInfo.put(methodName, null);  // Placeholder for method implementation
                System.out.println(" -> Method: " + methodName);
            }
        }
    }

    return null;
}


@Override
public Object visitTypeDefinition(delphiParser.TypeDefinitionContext ctx) {
    // Extract class name
    String className = ctx.identifier().getText();
    
    // Ensure it's a class type
    if (ctx.type_().classType() == null) {
        throw new RuntimeException("Type definition is not a class: " + className);
    }

    System.out.println("Defining class: " + className);

    // Store an empty map for fields & methods
    Map<String, Object> classInfo = new HashMap<>();
    classDefinitions.put(className, classInfo);

    System.out.println("Class " + className + " registered in classDefinitions");

    // 🔹 Call visitClassType with the extracted class name
    return visitClassType(ctx.type_().classType(), className);
}


@Override
public Object visitObjectInstantiation(delphiParser.ObjectInstantiationContext ctx) {
    String className = ctx.IDENT().getText();

    // 🔹 Ensure the class is defined
    if (!classDefinitions.containsKey(className)) {
        throw new RuntimeException("Undefined class: " + className);
    }

    // 🔹 Create a new instance and initialize fields
    Map<String, Object> instance = new HashMap<>();
    instance.put("__class__", className); // Store reference to class name

    // Copy default field values from `classDefinitions`
    Map<String, Object> classInfo = classDefinitions.get(className);
    for (String key : classInfo.keySet()) {
        instance.put(key, classInfo.get(key)); // Initialize fields
    }

    // 🔹 Generate an instance name and store it
    String instanceName = "Instance_" + className + "_" + System.currentTimeMillis();
    objects.put(instanceName, instance);

    System.out.println("Created instance of " + className + " with fields: " + instance);
    return instanceName; // Return reference
}


    
@Override
public Object visitMethodCall(delphiParser.MethodCallContext ctx) {
    System.out.println("inside visit methos call");
    String methodCall = ctx.qualifiedIdent().getText();

    // Extract instance name and method name
    String[] parts = methodCall.split("\\.");
    if (parts.length != 2) {
        throw new RuntimeException("Invalid method call: " + methodCall);
    }

    String instanceName = parts[0];  // e.g., `Calc`
    String methodName = parts[1];    // e.g., `ReadNumber`

    // 🔹 Resolve instance name if stored in `variables`
    if (variables.containsKey(instanceName) && objects.containsKey(variables.get(instanceName))) {
        instanceName = variables.get(instanceName).toString();
    }

    // Check if instance exists
    if (!objects.containsKey(instanceName)) {
        throw new RuntimeException("Undefined object: " + instanceName);
    }

    Map<String, Object> instance = objects.get(instanceName);
    System.out.println("Instance name isssss: "+ instance);

    // 🔹 Hardcoded handling for built-in methods
    switch (methodName) {
        case "ReadNumber":
            System.out.print("Enter a number: ");
            int value = scanner.nextInt();
            instance.put("Number", value);
            return null;

        case "ShowNumber":
            int num = (int) instance.get("Number");
            System.out.println("Your number is: " + num);
            return null;

        case "Destroy":
            objects.remove(instanceName);
            System.out.println("Object discarded");
            return null;
    }

    // 🔹 Handle User-Defined Methods
    String className = instance.get("__class__").toString();
    Map<String, Object> classInfo = classDefinitions.get(className);

    if (!classInfo.containsKey(methodName)) {
        throw new RuntimeException("Unknown method: " + methodName + " in class " + className);
    }

    // Retrieve and execute method
    Object methodImpl = classInfo.get(methodName);
    if (methodImpl instanceof delphiParser.CompoundStatementContext) {
        return visit((delphiParser.CompoundStatementContext) methodImpl);
    } else {
        throw new RuntimeException("Method " + methodName + " is not implemented.");
    }
}

/**
 * Executes a dynamically registered user-defined method.
 */
private Object executeMethod(Map<String, Object> instance, String methodName, List<Object> params) {
    Object methodImpl = classDefinitions.get(instance.get("__class__")).get(methodName);

    if (methodImpl instanceof delphiParser.CompoundStatementContext) {
        return visit((delphiParser.CompoundStatementContext) methodImpl);
    } else {
        throw new RuntimeException("Method " + methodName + " is not implemented.");
    }
}


 
    @Override
    public Object visitExpression(delphiParser.ExpressionContext ctx) {
        System.out.println("Visit Expression: " + ctx.getText());
        if (ctx.NUMBER() != null) {
            return Integer.parseInt(ctx.NUMBER().getText());  // Return integer value
        }
        
        else if (ctx.stringLiteral() != null) {
            return ctx.stringLiteral().getText();  // Return string value
        } else if (ctx.qualifiedIdent() != null) {
            String varName = ctx.qualifiedIdent().getText();            
            if (!variables.containsKey(varName)) {
                throw new RuntimeException("Undefined variable: " + varName);
            }
            return variables.get(varName);
        } else if (ctx.methodCall() != null) {
            return visit(ctx.methodCall());  // Visit method calls
        }
        else if (ctx.objectInstantiation() != null) {
            return visit(ctx.objectInstantiation());  // Visit method calls
        }
    
        return 0; // Default case
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println("Starting Delphi Interpreter...");

        // 🔹 Use a hardcoded string instead of System.in
        String filePath = args[0];
        CharStream input = CharStreams.fromPath(Paths.get(filePath));

        System.out.println("Creating Lexer...");
        delphiLexer lexer = new delphiLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);


        System.out.println("Creating Parser...");
        delphiParser parser = new delphiParser(tokens);

        // 🔹 Enable error reporting
        parser.removeErrorListeners();
        parser.addErrorListener(new org.antlr.v4.runtime.ConsoleErrorListener());

        System.out.println("Parsing Program...");
        ParseTree tree = parser.program();  // 🔸 Ensure "program" is the start rule!

        // 🔹 Print the parse tree
       // System.out.println("Parse Tree:");
       // System.out.println(tree.toStringTree(parser));

        System.out.println("Starting Interpretation...");
        DelphiInterpreter interpreter = new DelphiInterpreter();
        interpreter.visit(tree);

        System.out.println("Execution Completed.");
    }
    @Override
public Object visitAssignmentStatement(delphiParser.AssignmentStatementContext ctx) {
    String varName = ctx.qualifiedIdent().getText();
    
    Object value = visit(ctx.expression()); // Evaluate right-hand side

    // If the value is an instance, store it in the variable map
    if (value instanceof String && ((String) value).startsWith("Instance_")) {
        objects.put(varName, objects.get(value)); // Store instance reference
        System.out.println("Variable " + varName + " now references " + value);
    } else {
        variables.put(varName, (Integer) value); // Regular integer assignment
    }


    return null;
}
@Override
public Object visitStatement(delphiParser.StatementContext ctx) {
    if (ctx.assignmentStatement() != null) {
        return visit(ctx.assignmentStatement());
    } else if (ctx.procedureCall() != null) {
        return visit(ctx.procedureCall());
    } else if (ctx.getChild(0) instanceof delphiParser.MethodCallContext) {
        return visit(ctx.getChild(0));  // ✅ Ensure method calls are visited
    } else {
        System.out.println("Unknown statement type: " + ctx.getText());
    }
    return null;
}


@Override
public Object visitStatementList(delphiParser.StatementListContext ctx) {
    for (delphiParser.StatementContext statementCtx : ctx.statement()) {
        visit(statementCtx); // 🔹 Process each statement
    }
    return null;
}

private String extractStringContent(String rawString) {
    if (rawString.startsWith("'") && rawString.endsWith("'")) {
        return rawString.substring(1, rawString.length() - 1); // Remove quotes
    }
    return rawString; // Return unchanged if not a string literal
}

@Override
public Object visitProcedureCall(delphiParser.ProcedureCallContext ctx) {
    System.out.println("Inside visitProcedureCall: " + ctx.getText());

    String procedureName = ctx.getChild(0).getText();

    if (procedureName.equalsIgnoreCase("Write") || procedureName.equalsIgnoreCase("WriteLn")) {
        if (ctx.parameterList() != null) {
            for (delphiParser.ExpressionContext exprCtx : ctx.parameterList().expression()) {
                String valueToPrint = resolveVariableValue(exprCtx.getText());
                System.out.println("Output: " + valueToPrint);
            }
        }
        return null;
    }

    if (procedureName.equalsIgnoreCase("ReadLn")) {
        if (ctx.parameterList() != null && ctx.parameterList().expression().size() == 1) {
            String varName = ctx.parameterList().expression(0).getText();

            // 🔹 Check if it's an instance variable (`Calc.Number`)
            if (varName.contains(".")) {
                String[] parts = varName.split("\\.");
                if (parts.length != 2) {
                    throw new RuntimeException("Invalid ReadLn variable: " + varName);
                }

                String instanceName = parts[0]; // e.g., `Calc`
                String fieldName = parts[1];   // e.g., `Number`

                if (!objects.containsKey(instanceName)) {
                    throw new RuntimeException("Undefined object: " + instanceName);
                }

                Map<String, Object> instance = objects.get(instanceName);
                if (!instance.containsKey(fieldName)) {
                    throw new RuntimeException("Undefined field: " + fieldName + " in object " + instanceName);
                }

                // Read input dynamically
                System.out.print("Enter value for " + varName + ": ");
                int value = scanner.nextInt();
                instance.put(fieldName, value); // Store in instance field

                System.out.println("Stored " + value + " in " + instanceName + "." + fieldName);
                return null;
            }

            // 🔹 Otherwise, it's a standalone variable
            if (!variables.containsKey(varName)) {
                System.out.println("Here");
                throw new RuntimeException("Undefined variable: " + varName);
            }

            System.out.print("Enter value for " + varName + ": ");
            int value = scanner.nextInt();
            variables.put(varName, value); // Store in `variables` map

            System.out.println("Stored " + value + " in " + varName);
            return null;
        } else {
            throw new RuntimeException("ReadLn requires exactly one variable.");
        }
    }
    if (ctx.qualifiedIdent() != null) {
        String methodCall = ctx.qualifiedIdent().getText();

        // Extract instance name and method name
        String[] parts = methodCall.split("\\.");
        if (parts.length != 2) {
            throw new RuntimeException("Invalid method call: " + methodCall);
        }

        String instanceName = parts[0];  // e.g., `Calc`
        String methodName = parts[1];    // e.g., `ReadNumber`

        System.out.println("Procedure call detected - Instance: " + instanceName + ", Method: " + methodName);

        // 🔹 Resolve instance name if stored in `variables`
        if (variables.containsKey(instanceName) && objects.containsKey(variables.get(instanceName))) {
            instanceName = variables.get(instanceName).toString();
        }

        // Check if instance exists
        if (!objects.containsKey(instanceName)) {
            throw new RuntimeException("Undefined object: " + instanceName);
        }

        Map<String, Object> instance = objects.get(instanceName);

        // 🔹 Hardcoded handling for built-in methods
        

        // 🔹 Handle User-Defined Methods
        String className = instance.get("__class__").toString();
        Map<String, Object> classInfo = classDefinitions.get(className);

        if (!classInfo.containsKey(methodName)) {
            throw new RuntimeException("Unknown method: " + methodName + " in class " + className);
        }

        // Retrieve and execute method
        Object methodImpl = classInfo.get(methodName);
        if (methodImpl instanceof delphiParser.CompoundStatementContext) {
            return visit((delphiParser.CompoundStatementContext) methodImpl);
        } else {
            throw new RuntimeException("Method " + methodName + " is not implemented.");
        }
    }

    throw new RuntimeException("Unknown procedure call: " + ctx.getText());
}


@Override
public Object visitFieldDeclaration(delphiParser.FieldDeclarationContext ctx) {
    // 🔹 Get the class name from the parent type definition
    String className = ctx.getParent().getParent().getChild(0).getText();

    // 🔹 Get the field name and type
    String fieldName = ctx.identifier().getText();
    String fieldType = ctx.typeIdentifier().getText();

    System.out.println("Defining field: " + fieldName + " of type " + fieldType + " in class " + className);

    // 🔹 Ensure the class is defined
    if (!classDefinitions.containsKey(className)) {
        throw new RuntimeException("Class " + className + " is not defined.");
    }

    // 🔹 Store field with a default value (0 for integers)
    classDefinitions.get(className).put(fieldName, 0);

    return null;
}
private String resolveVariableValue(String varName) {
    // 🔹 Check if it's an instance variable (`Calc.Number`)
    if (varName.contains(".")) {
        String[] parts = varName.split("\\.");
        if (parts.length != 2) {
            return varName;
        }

        String instanceName = parts[0]; // e.g., `Calc`
        String fieldName = parts[1];   // e.g., `Number`

        if (objects.containsKey(instanceName) && objects.get(instanceName).containsKey(fieldName)) {
            return objects.get(instanceName).get(fieldName).toString();
        }
        return "Undefined field: " + fieldName + " in " + instanceName;
    }

    // 🔹 Otherwise, check standalone variables
    return variables.containsKey(varName)
        ? variables.get(varName).toString() 
        : "Undefined variable: " + varName;
}

}
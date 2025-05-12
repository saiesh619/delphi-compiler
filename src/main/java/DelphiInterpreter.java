import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
 
public class DelphiInterpreter extends delphiBaseVisitor <Object> {
    private Map<String, Integer> variables = new HashMap<>();
    private Map<String, Integer> localVariablesMap = new HashMap<>();
    private final Deque<Map<String, Object>> scopes = new ArrayDeque<>();
    private Scanner scanner = new Scanner(System.in);
    private Map<String, Map<String, Object>> objects = new HashMap<>();
    private Map<String, Map<String, Object>> classDefinitions = new HashMap<>();
    private String currentClassName = null;  // Stores the class currently being executed

    
    @Override
    public Object visitMethodImplementation(delphiParser.MethodImplementationContext ctx) {
    String methodName = ctx.qualifiedIdent().getText();
    
    //println("Defining method: " + methodName);
    
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
    //System.out.println("Method " + method + " added to class " + className);
    //System.out.println(classDefinitions);

    return null;
}
@Override
public Object visitCompoundStatement(delphiParser.CompoundStatementContext ctx) {
    scopes.push(new HashMap<>());
    System.out.println("NEW SCOPE CREATED");
    Object result = visit(ctx.statementList());
    //scopes.pop(); 
    return result;
}

@Override
public Object visitClassMemberList(delphiParser.ClassMemberListContext ctx) {
    //System.out.println("Visiting Class Member List");

    for (ParseTree member : ctx.children) {
        //: " + member.getClass().getSimpleName() + " -> " + member.getText());

        // 🔹 If it's a visibility section, visit it
        if (member instanceof delphiParser.VisibilitySectionContext) {
           // System.out.println("Found visibility section, visiting...");
            visit(member);  // 🔹 This ensures `visitVisibilitySection` is triggered
        } 
        // 🔹 If it's a constructor or method declaration, visit it separately
        else if (member instanceof delphiParser.ConstructorDeclarationContext ||
                 member instanceof delphiParser.MethodDeclarationContext) {
            //System.out.println("Found constructor/method, visiting...");
            visit(member);
        }
    }

    return null;
}

public Object visitClassType(delphiParser.ClassTypeContext ctx, String className) {
    // Retrieve stored class definition
    Map<String, Object> classInfo = classDefinitions.get(className);
    if (classInfo == null) {
        throw new RuntimeException("Class " + className + " was not registered before visiting classType.");
    }

    //System.out.println("Processing members of class: " + className);

    // Iterate over class members
    if (ctx.classMemberList() != null) {
        for (ParseTree member : ctx.classMemberList().children) {
            if (member instanceof delphiParser.FieldDeclarationContext) {
                delphiParser.FieldDeclarationContext fieldCtx = (delphiParser.FieldDeclarationContext) member;
                String fieldName = fieldCtx.getChild(0).getText();
                classInfo.put(fieldName, 0);  // Default field value
                //System.out.println(" -> Field: " + fieldName);
            } else if (member instanceof delphiParser.MethodDeclarationContext) {
                delphiParser.MethodDeclarationContext methodCtx = (delphiParser.MethodDeclarationContext) member;
                String methodName = methodCtx.getChild(1).getText();
                classInfo.put(methodName, null);  // Placeholder for method implementation
                ///System.out.println(" -> Method: " + methodName);
            }
        }
        
            visit(ctx.classMemberList());  // This should now visit visibility sections
        
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

    //System.out.println("Defining class: " + className);

    // Store an empty map for fields & methods
    Map<String, Object> classInfo = new HashMap<>();
    classDefinitions.put(className, classInfo);

    //System.out.println("Class " + className + " registered in classDefinitions");

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

    //System.out.println("Created instance of " + className + " with fields: " + instance);
    return instanceName; // Return reference
}


    
@Override
public Object visitMethodCall(delphiParser.MethodCallContext ctx) {
    String methodCall = ctx.qualifiedIdent().getText();

    String[] parts = methodCall.split("\\.");
    if (parts.length != 2) {
        throw new RuntimeException("Invalid method call: " + methodCall);
    }

    String instanceName = parts[0];
    String methodName = parts[1];

    if (variables.containsKey(instanceName) && objects.containsKey(variables.get(instanceName))) {
        instanceName = variables.get(instanceName).toString();
    }

    if (!objects.containsKey(instanceName)) {
        throw new RuntimeException("Undefined object: " + instanceName);
    }

    Map<String, Object> instance = objects.get(instanceName);
    String className = instance.get("__class__").toString();
    Map<String, Object> classInfo = classDefinitions.get(className);

    if (!classInfo.containsKey(methodName)) {
        throw new RuntimeException("Unknown method: " + methodName + " in class " + className);
    }
   //.println("Method call");
    Object methodImpl = classInfo.get(methodName);
    if (methodImpl instanceof delphiParser.CompoundStatementContext methodBody) {
        //System.out.println(classInfo);
        //System.out.println("Method call");
        // Create a new scope
       //System.out.println("scopes");
        //System.out.println(objects);
        Map<String, Object> localScope = new HashMap<>();

        localScope.put("result", 0); // support 'Result := ...'
        localScope.put(methodName, 0); // support 'funcName := ...'
        scopes.push(localScope);

        visit(methodBody);

        Object result = localScope.get("Result");
        if (result == null) {
            result = localScope.get(methodName);
        }

        scopes.pop();
        return result;
    }

    throw new RuntimeException("Method " + methodName + " is not implemented.");
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
        
         if (ctx.relationalExpression() != null) {
            return visitRelationalExpression(ctx.relationalExpression());
        }
       // System.out.println("Visit Expression: " + ctx.getText());
        if (ctx.NUMBER() != null) {
            
            return Integer.parseInt(ctx.NUMBER().getText());  // Return integer value
        }        
        else if (ctx.stringLiteral() != null) {
            return ctx.stringLiteral().getText();  // Return string value
        }  
        else if (ctx.qualifiedIdent() != null) {
            
            String varName = ctx.qualifiedIdent().getText();  
            
            
            for (Map<String, Object> scope : scopes) {                
                
                if (scope.containsKey(varName)) {
                    return scope.get(varName);
                } 
            }
            return variables.get(varName);
        } else if (ctx.methodCall() != null) {
            return visit(ctx.methodCall());  // Visit method calls
        }
        
        else if (ctx.objectInstantiation() != null) {
            return visit(ctx.objectInstantiation());  // Visit method calls
        } else if (ctx.expression().size() == 2 && ctx.op != null) {
                   
            int left = (int) visit(ctx.expression(0));
            int right = (int) visit(ctx.expression(1));
            
            return ctx.op.getText().equals("+") ? left + right : left - right;

            
        }
    
        return 0; // Default case
    } 
    @Override
public Object visitAssignmentStatement(delphiParser.AssignmentStatementContext ctx) {
    String varName = ctx.qualifiedIdent().getText();
    
    Object value = visit(ctx.expression()); // Evaluate right-hand side
   // System.out.println("varname inside assn stmt:"+ varName);
   // System.out.println("value inside assn stmt:"+ value);
   // System.out.println(scopes);

    scopes.peek().put("result", value);
   // System.out.println(objects);
   // System.out.println(scopes);


    
    if (value instanceof String && ((String) value).startsWith("Instance_")) {   
        //System.out.println("nside assn stmt instnc____:");     
        objects.put(varName, objects.get(value));
    } if (currentClassName != null && objects.containsKey(currentClassName)) {
        //System.out.println("nside assn stmt currclas:");     

        Map<String, Object> instance = objects.get(currentClassName);
        if (instance.containsKey(varName)) {
            
            instance.put(varName, value);
            return null;
        }
    }
// 🔹 Case 2: Local variable
if (!scopes.isEmpty()) {
    boolean updated = false;

    Iterator<Map<String, Object>> descending = ((Deque<Map<String, Object>>) scopes).descendingIterator();
while (descending.hasNext()) {
    Map<String, Object> scope = descending.next();
    if (scope.containsKey(varName)) {
        
        scope.put(varName, value);
        updated = true;
        break;
    }
}


    if (!updated) {
        
        scopes.peek().put(varName, value);
    }
}
    
    return null;
}


    
@Override
public Object visitLocalvariableDeclarationStatement(delphiParser.LocalvariableDeclarationStatementContext ctx) {
    // Assuming the grammar: 'var' IDENT ':' typeIdentifier (':=' expression)? ';'
    String varName = ctx.getChild(1).getText();  // IDENT is child #1
    Object value = null;

    // Check for ':=' assignment (child #4) and expression (child #5)
    if (ctx.getChildCount() > 5 && ctx.getChild(4).getText().equals(":=")) {
        value = visit(ctx.getChild(5));  // Expression
    }

    if (!scopes.isEmpty()) {
        scopes.peek().put(varName, value);
        
    } else {
        throw new RuntimeException("No scope available for local variable declaration.");
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
    }
    else if (ctx.forStatement()!=null){
        return visit(ctx.getChild(0));
    }
    else if (ctx.ifStatement() != null) {
        return visit(ctx.getChild(0));
    }
    else if(ctx.whileStatement()!= null){
        return visit(ctx.getChild(0));
    }
    else if(ctx.breakStatement()!=null){
        return visit(ctx.getChild(0));
    }
    else if(ctx.continueStatement()!=null){
        return visit(ctx.getChild(0));
    }
    else if (ctx.localvariableDeclarationStatement()!=null){
        return visit(ctx.getChild(0));
    } 
    else {
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
    //System.out.println("Inside visitProcedureCall: " + ctx.getText());

    String procedureName = ctx.getChild(0).getText();

    if (procedureName.equalsIgnoreCase("Write") || procedureName.equalsIgnoreCase("WriteLn")) {
        //System.out.println("writeLn");
        if (ctx.parameterList() != null && ctx.parameterList().expression().size()>=1) {
            //System.out.println("inside here sizgeater than 1  writeln");

            if(ctx.parameterList().expression().size()==2){ 

                //System.out.println("inside here size 2 writeln");
                Object valueToPrint;
                //System.out.println(ctx.parameterList().expression(1).getText());
                delphiParser.ExpressionContext expr = ctx.parameterList().expression(1);
                if (expr.methodCall() != null) {
                    valueToPrint = visit(expr.methodCall()); // Call and get return           
                    //System.out.println("scopescorrect: "+scopes);  
                    scopes.pop();       
                    //System.out.println("HEREEeeeeeeeee" + scopes.peek().get("result"));
                    //System.out.println(objects);
                    valueToPrint = scopes.peek().get("result");
                }
                else if(expr.qualifiedIdent()!=null){
                    valueToPrint = resolveVariableValue(expr.qualifiedIdent().getText());
                    //System.out.println("inside function result qualified where we want it: "+objects);
                
                } else {
                    valueToPrint = resolveVariableValue(expr.getText());
                }         
    
                //.println("valuetoprint:"+ valueToPrint);             

                    System.out.println(
                        ctx.parameterList().expression(0).getText().substring(1, 
                        ctx.parameterList().expression(0).getText().length()-1) + valueToPrint);

            }
            else if(ctx.parameterList().expression().size()==1){
                //System.out.println("inside here size 1 writeln");
                System.out.println(ctx.parameterList().expression(0).getText().substring(1, ctx.parameterList().expression(0).getText().length()-1));

            }
        }
        return null;
    }

    if (procedureName.equalsIgnoreCase("ReadLn")) {
        //System.out.println("readln");
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
                //System.out.print("Enter value for " + varName + ": ");
                String value = scanner.next();
                instance.put(fieldName, value); // Store in instance field

                //System.out.println("Stored " + value + " in " + instanceName + "." + fieldName);
                return null;
            }
           else if(!varName.contains(".")) {
                
                String instanceName = currentClassName; // e.g., `Calc`
                String fieldName = varName;   // e.g., `Number`

                if (!objects.containsKey(instanceName)) {
                    throw new RuntimeException("Undefined object: " + instanceName);
                }

                Map<String, Object> instance = objects.get(instanceName);
                if (!instance.containsKey(fieldName)) {
                    throw new RuntimeException("Undefined field: " + fieldName + " in object " + instanceName);
                }

                // Read input dynamically
                //System.out.print("Enter value for " + varName + ": ");
                String value = scanner.next();
                instance.put(fieldName, value); // Store in instance field

                //System.out.println("Stored " + value + " in " + instanceName + "." + fieldName);
                return null;
            }

            // 🔹 Otherwise, it's a standalone variable
            else if (!variables.containsKey(varName)) {
                throw new RuntimeException("Undefined variable: " + varName);
            }

            //System.out.print("Enter value for " + varName + ": ");
            int value = scanner.nextInt();
            variables.put(varName, value); // Store in `variables` map

            //System.out.println("Stored " + value + " in " + varName);
            return null;
        } else {
            throw new RuntimeException("ReadLn requires exactly one variable.");
        }
    }
    if (ctx.qualifiedIdent() != null) {
        //System.out.println("qualident");
        String methodCall = ctx.qualifiedIdent().getText();

        // Extract instance name and method name
        String[] parts = methodCall.split("\\.");
        if (parts.length != 2) {
            throw new RuntimeException("Invalid method call: " + methodCall);
        }

        String instanceName = parts[0];  // e.g., `Calc`
        String methodName = parts[1];    // e.g., `ReadNumber`
    
        // 🔹 Set the current class name
        //System.out.println("instANCE"+instanceName);
        
        currentClassName = instanceName;
        //System.out.println("currentClassName: "+currentClassName);
        //System.out.println("Executing method in class: " + currentClassName);
       //System.out.println("Procedure call detected - Instance: " + instanceName + ", Method: " + methodName);

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
public Object visitVisibilitySection(delphiParser.VisibilitySectionContext ctx) {
    //System.out.println("Inside visitVisibilitySection");

    // 🔹 Extract and print visibility modifier
    if (ctx.visibilityModifier() != null) {
        String visibility = ctx.visibilityModifier().getText();
        //System.out.println("Visibility: " + visibility);
    }

    // 🔹 Ensure field declarations are visited
    for (ParseTree child : ctx.children) {
        if (child instanceof delphiParser.FieldDeclarationContext) {
            //System.out.println("Found field declaration, visiting...");
            visit(child);  // 🔹 Ensures `visitFieldDeclaration` is triggered
        }
    }

    return null;
}


@Override
public Object visitFieldDeclaration(delphiParser.FieldDeclarationContext ctx) {
    //System.out.println("Inside visitFieldDeclaration");

    // 🔹 Traverse up the tree to get class name
    ParseTree parent = ctx.getParent(); // This is VisibilitySectionContext
    while (parent != null && !(parent instanceof delphiParser.ClassTypeContext)) {
        parent = parent.getParent();
    }

    if (!(parent instanceof delphiParser.ClassTypeContext)) {
        throw new RuntimeException("Could not determine class name for field");
    }

    String className = parent.getParent().getParent().getChild(0).getText(); // Extract class name
    //System.out.println("Class detected: " + className);


    // 🔹 Get field name and type
    String fieldName = ctx.identifier().getText();
    String fieldType = ctx.typeIdentifier().getText();

    //System.out.println("Defining field: " + fieldName + " of type " + fieldType + " in class " + className);

    // 🔹 Ensure class exists
    if (!classDefinitions.containsKey(className)) {
        throw new RuntimeException("Class " + className + " is not defined.");
    }

    // 🔹 Store field
    classDefinitions.get(className).put(fieldName, 0);

    return null;
}

private Object resolveVariableValue(String varName) {
    //System.out.println(currentClassName);
    //System.out.println(objects.get(currentClassName));
    //System.out.println("varName " + varName);
   // System.out.println(currentClassName);
    //System.out.println(objects.get(currentClassName));
    //System.out.println("varName " + varName);
    //System.out.println(scopes);
    //System.out.println(variables);
    Object value =  objects.get(currentClassName).get(varName);
    
    if(value == null){
    for (Map<String, Object> scope : scopes) {        
        if (scope.containsKey(varName)) {                
            return (int) scope.get(varName);
        }
        
    }
    }
    return value;
}

public class ContinueException extends RuntimeException {
    public ContinueException() {
        super("Continue statement encountered");
    }
}
@Override
public Object visitContinueStatement(delphiParser.ContinueStatementContext ctx) {
    throw new ContinueException();
}
public boolean asBoolean(Object value) {
    if (value instanceof Boolean) return (Boolean) value;
    if (value instanceof Integer) return (Integer) value != 0;
    if (value instanceof String) return !((String) value).isEmpty();
    return false;
}
private boolean breakFlag = false;
private boolean continueFlag = false;
 
    private void exitScope() {
        scopes.pop();
    }
    static class ReturnValue {
        Object value;
        ReturnValue(Object value) { this.value = value; }
    }
   
@Override
    public Object visitWhileStatement(delphiParser.WhileStatementContext ctx) {
        while (asBoolean(visit(ctx.expression()))) {
            try {
                visit(ctx.compoundStatement()); // Execute the loop body
            } catch (BreakException be) {
                System.out.println("Break encountered, exiting loop");
                break;
            }
         catch (ContinueException ce) {
            continue; // Skip to next loop iteration
        }
    }
                 return null;
    }
// Add to your visitor class (e.g., DelphiInterpreter)
@Override
public Void visitForStatement(delphiParser.ForStatementContext ctx) {
    
    String loopVar = ctx.identifier().getText();
    int start = Integer.parseInt(ctx.expression(0).getText());    
    int end = Integer.parseInt(ctx.expression(1).getText());
    boolean isTo = ctx.TO() != null;

    if (isTo) {
        for (int i = start; i <= end; i++) {
            scopes.peek().put(loopVar,i);            
            visit(ctx.compoundStatement());
        }
    } else {
        for (int i = start; i >= end; i--) {
            System.out.println("Setting " + loopVar + " to " + i);
            // Same here
            
        }
    }
    return null;
}
public class BreakException extends RuntimeException {
    public BreakException() {
        super("Break statement encountered");
    }
}
@Override
public Object visitBreakStatement(delphiParser.BreakStatementContext ctx) {
    throw new BreakException();
}

public Void visitIfStatement(delphiParser.IfStatementContext ctx) {
    // Evaluate the condition expression
    Object condition = visit(ctx.expression());    
    
    if (condition instanceof Boolean && (Boolean) condition) {    
        // If true, execute the THEN block
        visit(ctx.compoundStatement(0));
    } 

    return null;
}


    public Object visitRelationalExpression(delphiParser.RelationalExpressionContext ctx) {
        // There's always at least one additiveExpression child
        int left = (int) visitAdditiveExpression(ctx.additiveExpression(0));
    
        // If there's no second additiveExpression or no relOp, just return `left`
        // or treat it as no-op. Modify if you want a default or error in that scenario.
        if (ctx.additiveExpression().size() == 1) {
            return left;  // No operator => just pass back the single value
        }
    
        // Otherwise, we do have 2 additiveExpressions plus an operator
        int right = (int) visitAdditiveExpression(ctx.additiveExpression(1));
        String op = ctx.relOp().getText(); // or ctx.getChild(1).getText()
     
        switch (op) {
            case ">" -> {
                return left > right;
            }
            case "<" -> {
                return left < right;
            }
            case "=" -> {
                return left == right;
            }
            default -> throw new RuntimeException("Unsupported relational operator: " + op);
        }
    }
    
public Object visitAdditiveExpression(delphiParser.AdditiveExpressionContext ctx) {
    
    return visitTerm(ctx.term());
}

public Object visitTerm(delphiParser.TermContext ctx) {
    
    return visitFactor(ctx.factor());
}

@Override
public Object visitFactor(delphiParser.FactorContext ctx) {
    if (ctx.NUMBER() != null) {
        return Integer.parseInt(ctx.NUMBER().getText());
    }

    if (ctx.qualifiedIdent() != null) {
        String name = ctx.qualifiedIdent().getText();
        // Search from innermost to outermost scope
        Iterator<Map<String, Object>> descending = ((Deque<Map<String, Object>>) scopes).descendingIterator();
        while (descending.hasNext()) {
            Map<String, Object> scope = descending.next();
            if (scope.containsKey(name)) {
                Object value = scope.get(name);
                if (value instanceof Integer) {
                    return value;
                } else {
                    System.err.println("Invalid or null value for variable '" + name + "': " + value);
                    return 0;
                }
            }
        }
        System.err.println("Variable not found: " + name);
    }
    if(ctx.IDENT()!=null){
        String name = ctx.IDENT().getText();
        // Search from innermost to outermost scope
        Iterator<Map<String, Object>> descending = ((Deque<Map<String, Object>>) scopes).descendingIterator();
        while (descending.hasNext()) {
            Map<String, Object> scope = descending.next();
            if (scope.containsKey(name)) {
                Object value = scope.get(name);
                if (value instanceof Integer) {
                    return value;
                } else {
                    System.err.println("Invalid or null value for variable '" + name + "': " + value);
                    return 0;
                }
            }
        }
        System.err.println("Variable not found: " + name);
    }
    
    return 0;
}

    public static void main(String[] args) throws Exception {
        //System.out.println("Starting Delphi Interpreter...");

        // 🔹 Use a hardcoded string instead of System.in
        String filePath = args[0];
        CharStream input = CharStreams.fromPath(Paths.get(filePath));

        //System.out.println("Creating Lexer...");
        delphiLexer lexer = new delphiLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);


        //System.out.println("Creating Parser...");
        delphiParser parser = new delphiParser(tokens);

        // 🔹 Enable error reporting
        parser.removeErrorListeners();
        parser.addErrorListener(new org.antlr.v4.runtime.ConsoleErrorListener());

        //System.out.println("Parsing Program...");
        ParseTree tree = parser.program();  // 🔸 Ensure "program" is the start rule!

        // 🔹 Print the parse tree
       // System.out.println("Parse Tree:");
       // System.out.println(tree.toStringTree(parser));

        //System.out.println("Starting Interpretation...");
        DelphiInterpreter interpreter = new DelphiInterpreter();
        interpreter.visit(tree);

        //System.out.println("Execution Completed.");
    }
       
}
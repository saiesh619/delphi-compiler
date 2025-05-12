import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.antlr.v4.runtime.tree.ParseTree;

public class LLVMGenerator extends delphiBaseVisitor<String> {
private StringBuilder ir = new StringBuilder();
private StringBuilder globalIr = new StringBuilder();
private int tempCount = 0;
private int labelCount = 0;
private Map<String, Map<String, Object>> objects = new HashMap<>();
private Map<String, String> variables = new HashMap<>();
private Map<String, List<String>> classFields = new HashMap<>();
private Map<String, String> objectTypes = new HashMap<>();
private Set<String> definedStructs = new HashSet<>();
private Scanner scanner = new Scanner(System.in);
private String currentClassName = null;
private Map<String, Map<String, Object>> classDefinitions = new HashMap<>();
private final Deque<Map<String, Object>> scopes = new ArrayDeque<>();
private final Deque<String> breakLabels = new ArrayDeque<>();
private final Deque<String> continueLabels = new ArrayDeque<>();

public String generate(ParseTree tree) {
ir.append("; ModuleID = 'main'\n");
ir.append("declare i32 @printf(i8*, ...)\n");
ir.append("declare i32 @read_int()\n");
ir.append("@.str = constant [4 x i8] c\"%d\\0A\\00\"\n\n");
ir.append("define i32 @main() {\n");
visit(tree);
ir.append(" ret i32 0\n");
ir.append("}\n");
return globalIr.toString() + "\n" + ir.toString();
}

public String compoundStatement(delphiParser.CompoundStatementContext ctx) {
if (ctx.statementList() != null) {
return visit(ctx.statementList());
}
return "";
}

@Override
public String visitDeclarationPart(delphiParser.DeclarationPartContext ctx) {
if (ctx.typeDefinitionPart() != null) {
//System.out.println("OK");
for (delphiParser.TypeDefinitionPartContext typeDefinition : ctx.typeDefinitionPart()) {
visit(typeDefinition);
}
}
if (ctx.methodImplementation() != null) {
for (delphiParser.MethodImplementationContext methodCtx : ctx.methodImplementation()) {
visit(methodCtx);
}
}
return "";
}

// Push a new scope (new block)
private void pushScope() {
scopes.push(new HashMap<>());
}

// Pop the current scope
private void popScope() {
scopes.pop();
}

@Override
public String visitMethodCall(delphiParser.MethodCallContext ctx) {
String fullName = ctx.qualifiedIdent().getText(); // "Calc.ReadNumber"
if (fullName.contains(".")) {
String[] parts = fullName.split("\\.");
String objectName = parts[0];
String methodName = parts[1];
String objectPtr = lookupVariable(objectName);
if (objectPtr == null) {
throw new RuntimeException("Undefined object: " + objectName);
}
String className = objectTypes.get(objectName);
if (className == null) {
throw new RuntimeException("Unknown object type for: " + objectName);
}
// ✅ Correct: emit call to class method passing the object (self)
emit("call i32 @" + className + "_" + methodName + "(%class." + className + "* " + objectPtr + ")");
} else {
String functionName = fullName;
emit("call i32 @" + functionName + "()");
}
return null;
}



@Override
public String visitMethodImplementation(delphiParser.MethodImplementationContext ctx) {
String fullName = ctx.qualifiedIdent().getText(); // like TCalculator.ReadNumber
String[] parts = fullName.split("\\.");
boolean isFunction = isFunction(ctx); // ✅ fix here
if (parts.length == 2) {
String className = parts[0];
currentClassName = className;
String methodName = parts[1];
globalIr.append("define ").append(isFunction ? "i32" : "void")
.append(" @").append(className).append("_").append(methodName)
.append("(%class.").append(className).append("* %self) {\n");
} else {
String functionName = parts[0];
globalIr.append("define ").append(isFunction ? "i32" : "void")
.append(" @").append(functionName).append("() {\n");
}
StringBuilder oldIr = ir;
ir = globalIr;
pushScope();
if (isFunction) {
String resultPtr = freshTemp();
emit(resultPtr + " = alloca i32");
declareVariable("result", resultPtr);
}
visit(ctx.compoundStatement());
if (isFunction) {
String resultValue = freshTemp();
String resultPtr = lookupVariable("result");
emit(resultValue + " = load i32, i32* " + resultPtr);
emit("ret i32 " + resultValue);
} else {
emit("ret void");
}
popScope();
ir = oldIr;
globalIr.append("}\n\n");
return null;
}

private boolean isFunction(delphiParser.MethodImplementationContext ctx) {
// function Add : Integer;
return ctx.getChild(0).getText().equalsIgnoreCase("function");
}

private String freshTemp() {
return "%t" + (tempCount++);
}

private String freshLabel() {
return "label" + (labelCount++);
}

private void emit(String code) {
ir.append(" ").append(code).append("\n");
}

// --- Modify resolveVariableValue to use scopes ---
private String resolveVariableValue(String varName) {
String ptr = lookupVariable(varName);
if (ptr == null) {
// 🔥 Check if this var is a field of the class (fetching from null map)
List<String> fields = classFields.getOrDefault(null, List.of());
if (fields.contains(varName)) {
int fieldIndex = fields.indexOf(varName);
String gep = freshTemp();
emit(gep + " = getelementptr %class." + currentClassName + ", %class." + currentClassName + "* %self, i32 0, i32 " + fieldIndex);
ptr = gep;
} else {
throw new RuntimeException("Undefined variable: " + varName);
}
}
String temp = freshTemp();
emit(temp + " = load i32, i32* " + ptr);
return temp;
}
// --- Now modify visitBlock to push/pop scope ---
@Override
public String visitBlock(delphiParser.BlockContext ctx) {
pushScope(); // <== enter new block

if (ctx.declarationPart() != null) {
visit(ctx.declarationPart());
}
if (ctx.compoundStatement() != null) {
visit(ctx.compoundStatement());
}

popScope(); // <== leave block
return "";
}

@Override
public String visitCompoundStatement(delphiParser.CompoundStatementContext ctx) {
pushScope(); // 🔥 Push new scope when entering a begin..end block
String result = visit(ctx.statementList());
popScope(); // 🔥 Pop scope after finishing block
return result;
}

@Override
public String visitVariableDeclarationPart(delphiParser.VariableDeclarationPartContext ctx) {
for (delphiParser.VariableDeclarationContext varCtx : ctx.variableDeclaration()) {
visit(varCtx);
}
return null;
}

@Override
public String visitVariableDeclaration(delphiParser.VariableDeclarationContext ctx) {
String varName = ctx.identifier().getText();
String ptr = freshTemp();
emit(ptr + " = alloca i32");
declareVariable(varName, ptr);

// Optional: Initialize to 0 to avoid garbage
emit("store i32 0, i32* " + ptr);

return null;
}

@Override
public String visitStatement(delphiParser.StatementContext ctx) {
if (ctx.procedureCall() != null) {
return visit(ctx.procedureCall());
}
if (ctx.assignmentStatement() != null) {
return visit(ctx.assignmentStatement());
}
if (ctx.ifStatement() != null) {
return (visit(ctx.ifStatement()));
}
if (ctx.whileStatement() != null) {
return (visit(ctx.whileStatement()));
}
if (ctx.breakStatement() != null) {
return visit(ctx.breakStatement());
}
if (ctx.continueStatement() != null) {
return visit(ctx.continueStatement());
}
if (ctx.forStatement() != null) {
return visit(ctx.forStatement());
}
if (ctx.localvariableDeclarationStatement() != null) { // 🔥 ADD THIS
return visit(ctx.localvariableDeclarationStatement());
}
return null;
}

@Override
public String visitStatementList(delphiParser.StatementListContext ctx) {
for (delphiParser.StatementContext stmt : ctx.statement()) {
visit(stmt);
}
return "null";
}

@Override
public String visitProcedureCall(delphiParser.ProcedureCallContext ctx) {
String procedureName = ctx.getChild(0).getText();

if (procedureName.equalsIgnoreCase("Write") || procedureName.equalsIgnoreCase("Writeln")) {
// Handle Write / Writeln specially
if (ctx.parameterList() != null && ctx.parameterList().expression().size() >= 1) {
if (ctx.parameterList().expression().size() == 1) {
delphiParser.ExpressionContext expr = ctx.parameterList().expression(0);
String valueToPrint = visit(expr);

if (valueToPrint == null || valueToPrint.isEmpty()) {
// Might be a string literal
String raw = expr.getText();
if (raw.startsWith("'")) {
String cleaned = raw.substring(1, raw.length() - 1);
String globalName = "@.str" + tempCount;
int strLen = cleaned.length() + 2;
String llvmFormatted = cleaned.replace("\"", "\\22") + "\\0A\\00";
ir.insert(0, globalName + " = constant [" + strLen + " x i8] c\"" + llvmFormatted + "\"\n");
emit("call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([" + strLen + " x i8], [" + strLen + " x i8]* " +
globalName + ", i32 0, i32 0))");
tempCount++;
}
} else {
// Computed value
emit("call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str, i32 0, i32 0), i32 " +
valueToPrint + ")");
}
} else {
// Two expressions
delphiParser.ExpressionContext firstExp = ctx.parameterList().expression(0);
delphiParser.ExpressionContext secondExp = ctx.parameterList().expression(1);

String raw = firstExp.getText();
if (raw.startsWith("'")) {
String cleaned = raw.substring(1, raw.length() - 1);
String globalName = "@.str" + tempCount;
int strLen = cleaned.length() + 1;
String llvmFormatted = cleaned.replace("\"", "\\22") + "\\00";
ir.insert(0, globalName + " = constant [" + strLen + " x i8] c\"" + llvmFormatted + "\"\n");
emit("call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([" + strLen + " x i8], [" + strLen + " x i8]* " +
globalName + ", i32 0, i32 0))");
tempCount++;
} else {
throw new RuntimeException("First argument to Writeln must be a string literal");
}

String valueToPrint = visit(secondExp);
if (valueToPrint == null || valueToPrint.isEmpty()) {
throw new RuntimeException("Second argument must be an expression returning a value");
}

emit("call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str, i32 0, i32 0), i32 " +
valueToPrint + ")");
}
}
return null; // ✅ VERY IMPORTANT: STOP after handling Write/Writeln
}

if (procedureName.contains(".")) {
// Object method call
String[] parts = procedureName.split("\\.");
String objectName = parts[0];
String methodName = parts[1];

String objectPtr = lookupVariable(objectName);
String className = objectTypes.get(objectName);

emit("call i32 @" + className + "_" + methodName + "(%class." + className + "* " + objectPtr + ")");
} else {
// Normal global procedure
emit("call i32 @" + procedureName + "()");
}

return null;
}



@Override
public String visitRelationalExpression(delphiParser.RelationalExpressionContext ctx) {
// Always visit the left side
String left = ctx.additiveExpression(0).getText();
// If no right side, just return the left
if (ctx.additiveExpression().size() == 1) {
return left;
}

// Otherwise, get operator and right side
String right = ctx.additiveExpression(1).getText();

String op = ctx.relOp().getText();

left = resolveVariableValue(left);
String llvmOp;
switch (op) {
case ">":
llvmOp = "sgt";
break;
case "<":
llvmOp = "slt";
break;
case "=":
llvmOp = "eq";
break;
case "<=":
llvmOp = "sle";
break;
case ">=":
llvmOp = "sge";
break;
case "<>":
llvmOp = "ne";
break; // Pascal not-equal
default:
throw new RuntimeException("Unsupported relOp: " + op);
}

String result = freshTemp();
emit(result + " = icmp " + llvmOp + " i32 " + left + ", " + right);
return result;
}

@Override
public String visitExpression(delphiParser.ExpressionContext ctx) {
// Handling '+' and '-' binary operations
if (ctx.op != null && (ctx.op.getType() == delphiParser.PLUS || ctx.op.getType() == delphiParser.MINUS)) {
String left = visit(ctx.expression(0));
String right = visit(ctx.expression(1));
String temp = freshTemp();
String op = ctx.op.getType() == delphiParser.PLUS ? "add" : "sub";
emit(temp + " = " + op + " i32 " + left + ", " + right);
return temp;
}
// Handle method calls
if (ctx.methodCall() != null) {
if (ctx.methodCall() != null) {
// Instead of visiting normally, manually handle method call returning value
String fullName = ctx.methodCall().qualifiedIdent().getText();
if (fullName.contains(".")) {
String[] parts = fullName.split("\\.");
String objectName = parts[0];
String methodName = parts[1];
String objectPtr = lookupVariable(objectName);
String className = objectTypes.get(objectName);
String temp = freshTemp();
emit(temp + " = call i32 @" + className + "_" + methodName + "(%class." + className + "* " + objectPtr + ")");
return temp;
} else {
String functionName = fullName;
String temp = freshTemp();
emit(temp + " = call i32 @" + functionName + "()");
return temp;
}
}
}
// Handle qualified identifiers (variables)
if (ctx.qualifiedIdent() != null) {
return resolveVariableValue(ctx.qualifiedIdent().getText());
}
// Handle number literals
if (ctx.NUMBER() != null) {
return createImmediateInt(ctx.NUMBER().getText());
}
// Handle relational expressions (>, <, =)
if (ctx.relationalExpression() != null) {
return visit(ctx.relationalExpression());
}
// Object Instantiation Detection
if (ctx.objectInstantiation() != null) {
//System.out.println("hello hello");
String fullText = ctx.objectInstantiation().getText(); // e.g., TCalculator.Create
String className = fullText.split("\\.")[0]; // "TCalculator"
String temp = freshTemp();
emit(temp + " = alloca %class." + className); 
// 🔥 Immediately call the constructor
emit("call i32 @" + className + "_Create(%class." + className + "* " + temp + ")");
return temp; // return pointer to newly created and initialized object
}

// 🔥 If it's an object instantiation or something complex, you may extend here.

return "";
}


private String createImmediateInt(String val) {
String temp = freshTemp();
emit(temp + " = add i32 0, " + val);
return temp;
}

@Override
public String visitAssignmentStatement(delphiParser.AssignmentStatementContext ctx) {
String varName = ctx.qualifiedIdent().getText();
// Object creation
if (ctx.expression().objectInstantiation() != null) {
String fullText = ctx.expression().objectInstantiation().getText();
String className = fullText.split("\\.")[0];
String temp = freshTemp();
emit(temp + " = alloca %class." + className);
declareVariable(varName, temp);
objectTypes.put(varName, className);
return null;
}
// Normal assignment
String value = visit(ctx.expression());
String ptr = lookupVariable(varName);
if (ptr == null) {
// Check if it's a class field
//System.out.println(currentClassName);
//System.out.println(varName);
//System.out.println(classFields);
if (classFields.getOrDefault(null, List.of()).contains(varName)) 
{
// Generate GEP from self
ptr = freshTemp();
int index = classFields.get(null).indexOf(varName);
emit(ptr + " = getelementptr %class." + currentClassName + ", %class." + currentClassName + "* %self, i32 0, i32 " + index);
} else {
throw new RuntimeException("Undefined variable: " + varName);
}
}
emit("store i32 " + value + ", i32* " + ptr);
return null;
}

@Override
public String visitIfStatement(delphiParser.IfStatementContext ctx) {
String condReg = visit(ctx.expression()); // This generates icmp and returns result (i1)

String thenLabel = freshLabel();
String endLabel = freshLabel();

emit("br i1 " + condReg + ", label %" + thenLabel + ", label %" + endLabel);
emit(thenLabel + ":");

visit(ctx.compoundStatement(0)); // The `then` block

emit("br label %" + endLabel);
emit(endLabel + ":");

return null;
}

@Override
public String visitLocalvariableDeclarationStatement(delphiParser.LocalvariableDeclarationStatementContext ctx) {
visit(ctx.variableDeclaration()); // ❗ just visit once, not a loop
return null;
}

@Override
public String visitWhileStatement(delphiParser.WhileStatementContext ctx) {
String condLabel = freshLabel(); // Check condition here
String bodyLabel = freshLabel(); // Execute loop body here
String endLabel = freshLabel(); // Exit loop here

// Push break/continue labels
breakLabels.push(endLabel);
continueLabels.push(condLabel);

emit("br label %" + condLabel); // Initial jump to condition

// Condition block
emit(condLabel + ":");
String condReg = visit(ctx.expression()); // condition returns i1
emit("br i1 " + condReg + ", label %" + bodyLabel + ", label %" + endLabel);

// Body block
emit(bodyLabel + ":");
visit(ctx.compoundStatement()); // loop body
emit("br label %" + condLabel); // loop back

// End block
emit(endLabel + ":");

// Pop break/continue after loop
breakLabels.pop();
continueLabels.pop();

return null;
}

// Look up a variable from innermost to outer scopes
private String lookupVariable(String name) {
for (Map<String, Object> scope : scopes) {
if (scope.containsKey(name)) {
return (String) scope.get(name);
}
}
return null;
}

// Declare a new variable into current scope
private void declareVariable(String name, String ptr) {
scopes.peek().put(name, ptr);
}

@Override
public String visitForStatement(delphiParser.ForStatementContext ctx) {
String loopVar = ctx.identifier().getText();
String startReg = visit(ctx.expression(0)); // initial value
String endReg = visit(ctx.expression(1)); // end value

// Lookup or allocate loop variable
String loopPtr = lookupVariable(loopVar);
if (loopPtr == null) {
loopPtr = freshTemp();
emit(loopPtr + " = alloca i32");
declareVariable(loopVar, loopPtr);
}

emit("store i32 " + startReg + ", i32* " + loopPtr);

// Labels
String condLabel = freshLabel();
String bodyLabel = freshLabel();
String updateLabel = freshLabel();
String endLabel = freshLabel();

breakLabels.push(endLabel);
continueLabels.push(updateLabel);

emit("br label %" + condLabel);

emit(condLabel + ":");
String loopVal = freshTemp();
emit(loopVal + " = load i32, i32* " + loopPtr);
String condResult = freshTemp();
String direction = ctx.TO() != null ? "sle" : "sge";
emit(condResult + " = icmp " + direction + " i32 " + loopVal + ", " + endReg);
emit("br i1 " + condResult + ", label %" + bodyLabel + ", label %" + endLabel);

emit(bodyLabel + ":");
visit(ctx.compoundStatement());

emit("br label %" + updateLabel);

emit(updateLabel + ":");
String loaded = freshTemp();
emit(loaded + " = load i32, i32* " + loopPtr);
String updated = freshTemp();
String op = ctx.TO() != null ? "add" : "sub";
emit(updated + " = " + op + " i32 " + loaded + ", 1");
emit("store i32 " + updated + ", i32* " + loopPtr);
emit("br label %" + condLabel);

emit(endLabel + ":");

breakLabels.pop();
continueLabels.pop();

return null;
}

@Override
public String visitBreakStatement(delphiParser.BreakStatementContext ctx) {
if (breakLabels.isEmpty()) {
throw new RuntimeException("Break used outside of a loop");
}
emit("br label %" + breakLabels.peek());
return null;
}

@Override
public String visitContinueStatement(delphiParser.ContinueStatementContext ctx) {
if (continueLabels.isEmpty()) {
throw new RuntimeException("Continue used outside of a loop");
}
emit("br label %" + continueLabels.peek());
return null;
}
@Override
public String visitTypeDefinition(delphiParser.TypeDefinitionContext ctx) {
String className = ctx.identifier().getText();
//System.out.println("Visited class: " + className);

if (!definedStructs.contains(className)) {
definedStructs.add(className);
//System.out.println(" HEY");
List<String> fieldTypes = new ArrayList<>();

// 🔥 Go inside classType → classMemberList → visibilitySection → fieldDeclaration
if (ctx.type_().classType() != null && ctx.type_().classType().classMemberList() != null) {
for (delphiParser.VisibilitySectionContext visCtx : ctx.type_().classType().classMemberList().visibilitySection()) {
for (delphiParser.FieldDeclarationContext fieldCtx : visCtx.fieldDeclaration()) {
visit(fieldCtx); // 🔥 This will call visitFieldDeclaration, save field names
String pascalType = fieldCtx.typeIdentifier().getText();
fieldTypes.add(convertToLLVMType(pascalType));
}
}
}

// Build the LLVM struct
globalIr.append("%class.").append(className).append(" = type { ");
globalIr.append(String.join(", ", fieldTypes));
globalIr.append(" }\n");
}

return className;
}

private String convertToLLVMType(String pascalType) {
switch (pascalType.toLowerCase()) {
case "integer":
return "i32";
default:
throw new RuntimeException("Unknown type: " + pascalType);
}
}
@Override
public String visitFieldDeclaration(delphiParser.FieldDeclarationContext ctx) {
String fieldName = ctx.identifier().getText();
String pascalType = ctx.typeIdentifier().getText();
// Optional: print for debugging
//System.out.println("Field Found: " + fieldName + " of type " + pascalType);
// Only allow Integer fields for now
if (!pascalType.equalsIgnoreCase("Integer")) {
throw new RuntimeException("Only Integer fields are supported: found " + pascalType);
}

// Add field to the current class' field list
classFields
.computeIfAbsent(currentClassName, k -> new ArrayList<>())
.add(fieldName);
//System.out.println(classFields);
return null;
}


}

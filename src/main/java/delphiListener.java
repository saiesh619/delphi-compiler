// Generated from delphi.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link delphiParser}.
 */
public interface delphiListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link delphiParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(delphiParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(delphiParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#programHeading}.
	 * @param ctx the parse tree
	 */
	void enterProgramHeading(delphiParser.ProgramHeadingContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#programHeading}.
	 * @param ctx the parse tree
	 */
	void exitProgramHeading(delphiParser.ProgramHeadingContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(delphiParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(delphiParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void enterCompoundStatement(delphiParser.CompoundStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void exitCompoundStatement(delphiParser.CompoundStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#statementList}.
	 * @param ctx the parse tree
	 */
	void enterStatementList(delphiParser.StatementListContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#statementList}.
	 * @param ctx the parse tree
	 */
	void exitStatementList(delphiParser.StatementListContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#declarationPart}.
	 * @param ctx the parse tree
	 */
	void enterDeclarationPart(delphiParser.DeclarationPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#declarationPart}.
	 * @param ctx the parse tree
	 */
	void exitDeclarationPart(delphiParser.DeclarationPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#typeDefinitionPart}.
	 * @param ctx the parse tree
	 */
	void enterTypeDefinitionPart(delphiParser.TypeDefinitionPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#typeDefinitionPart}.
	 * @param ctx the parse tree
	 */
	void exitTypeDefinitionPart(delphiParser.TypeDefinitionPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#typeDefinition}.
	 * @param ctx the parse tree
	 */
	void enterTypeDefinition(delphiParser.TypeDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#typeDefinition}.
	 * @param ctx the parse tree
	 */
	void exitTypeDefinition(delphiParser.TypeDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#type_}.
	 * @param ctx the parse tree
	 */
	void enterType_(delphiParser.Type_Context ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#type_}.
	 * @param ctx the parse tree
	 */
	void exitType_(delphiParser.Type_Context ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#classType}.
	 * @param ctx the parse tree
	 */
	void enterClassType(delphiParser.ClassTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#classType}.
	 * @param ctx the parse tree
	 */
	void exitClassType(delphiParser.ClassTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#classMemberList}.
	 * @param ctx the parse tree
	 */
	void enterClassMemberList(delphiParser.ClassMemberListContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#classMemberList}.
	 * @param ctx the parse tree
	 */
	void exitClassMemberList(delphiParser.ClassMemberListContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#constructorDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterConstructorDeclaration(delphiParser.ConstructorDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#constructorDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitConstructorDeclaration(delphiParser.ConstructorDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#constructorImplementation}.
	 * @param ctx the parse tree
	 */
	void enterConstructorImplementation(delphiParser.ConstructorImplementationContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#constructorImplementation}.
	 * @param ctx the parse tree
	 */
	void exitConstructorImplementation(delphiParser.ConstructorImplementationContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#destructorDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterDestructorDeclaration(delphiParser.DestructorDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#destructorDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitDestructorDeclaration(delphiParser.DestructorDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#destructorImplementation}.
	 * @param ctx the parse tree
	 */
	void enterDestructorImplementation(delphiParser.DestructorImplementationContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#destructorImplementation}.
	 * @param ctx the parse tree
	 */
	void exitDestructorImplementation(delphiParser.DestructorImplementationContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#fieldDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterFieldDeclaration(delphiParser.FieldDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#fieldDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitFieldDeclaration(delphiParser.FieldDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMethodDeclaration(delphiParser.MethodDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMethodDeclaration(delphiParser.MethodDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#variableDeclarationPart}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclarationPart(delphiParser.VariableDeclarationPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#variableDeclarationPart}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclarationPart(delphiParser.VariableDeclarationPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#qualifiedIdent}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedIdent(delphiParser.QualifiedIdentContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#qualifiedIdent}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedIdent(delphiParser.QualifiedIdentContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaration(delphiParser.VariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaration(delphiParser.VariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(delphiParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(delphiParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#assignmentStatement}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentStatement(delphiParser.AssignmentStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#assignmentStatement}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentStatement(delphiParser.AssignmentStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#procedureCall}.
	 * @param ctx the parse tree
	 */
	void enterProcedureCall(delphiParser.ProcedureCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#procedureCall}.
	 * @param ctx the parse tree
	 */
	void exitProcedureCall(delphiParser.ProcedureCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void enterParameterList(delphiParser.ParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void exitParameterList(delphiParser.ParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(delphiParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(delphiParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#methodCall}.
	 * @param ctx the parse tree
	 */
	void enterMethodCall(delphiParser.MethodCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#methodCall}.
	 * @param ctx the parse tree
	 */
	void exitMethodCall(delphiParser.MethodCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#visibilitySection}.
	 * @param ctx the parse tree
	 */
	void enterVisibilitySection(delphiParser.VisibilitySectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#visibilitySection}.
	 * @param ctx the parse tree
	 */
	void exitVisibilitySection(delphiParser.VisibilitySectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#visibilityModifier}.
	 * @param ctx the parse tree
	 */
	void enterVisibilityModifier(delphiParser.VisibilityModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#visibilityModifier}.
	 * @param ctx the parse tree
	 */
	void exitVisibilityModifier(delphiParser.VisibilityModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#typeIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterTypeIdentifier(delphiParser.TypeIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#typeIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitTypeIdentifier(delphiParser.TypeIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(delphiParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(delphiParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#stringLiteral}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteral(delphiParser.StringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#stringLiteral}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteral(delphiParser.StringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#simpleType}.
	 * @param ctx the parse tree
	 */
	void enterSimpleType(delphiParser.SimpleTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#simpleType}.
	 * @param ctx the parse tree
	 */
	void exitSimpleType(delphiParser.SimpleTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#methodImplementation}.
	 * @param ctx the parse tree
	 */
	void enterMethodImplementation(delphiParser.MethodImplementationContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#methodImplementation}.
	 * @param ctx the parse tree
	 */
	void exitMethodImplementation(delphiParser.MethodImplementationContext ctx);
}
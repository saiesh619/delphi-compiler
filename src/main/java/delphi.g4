
grammar delphi;

options {
    caseInsensitive = true;
}

program
    : programHeading block DOT
    ;

programHeading
    : PROGRAM identifier SEMI
    ;

block
    : declarationPart compoundStatement
    ;

compoundStatement
    : BEGIN statementList END SEMI?
    ;

statementList
    : statement (SEMI statement)* SEMI?
    ;

declarationPart
    : (typeDefinitionPart
    | variableDeclarationPart
    | assignmentStatement
    | methodImplementation
    | destructorDeclaration
    | constructorImplementation
    | destructorImplementation
    )*
    ;

typeDefinitionPart
    : TYPE typeDefinition (SEMI typeDefinition)* SEMI
    ;

typeDefinition
    : identifier EQUAL type_
    | identifier COLON typeIdentifier
    ;

type_
    : simpleType
    | classType
    ;

classType
    : CLASS classMemberList END
    ;

classMemberList
    : (visibilitySection | fieldDeclaration | methodDeclaration | constructorDeclaration | destructorDeclaration)*
    ;

constructorDeclaration
    : CONSTRUCTOR CREATE SEMI
    ;

constructorImplementation
    : CONSTRUCTOR IDENT DOT CREATE SEMI compoundStatement
    ;



destructorDeclaration
    : DESTRUCTOR identifier (OVERRIDE)? SEMI
    ;

destructorImplementation
    : DESTRUCTOR qualifiedIdent SEMI 
    ;

fieldDeclaration
    : identifier COLON typeIdentifier SEMI
    ;

methodDeclaration
    : PROCEDURE identifier SEMI
    ;

variableDeclarationPart
    : VAR variableDeclaration+
    ;

variableDeclaration
    : identifier COLON typeIdentifier SEMI?
    ;


variableDeclarationStatement
    : VAR variableDeclaration+
    ;

localvariableDeclarationStatement
    : VAR variableDeclaration
    ;

statement
    : assignmentStatement
    | procedureCall     
    | localvariableDeclarationStatement
    | forStatement
    | ifStatement
    | whileStatement
    | breakStatement
    | continueStatement

    ;

forStatement
    : FOR identifier ASSIGN expression (TO | DOWNTO) expression DO compoundStatement  SEMI?
    ;

ifStatement
    : IF expression THEN compoundStatement (ELSE compoundStatement)?
    ;

whileStatement
    : WHILE expression DO compoundStatement
    ;

breakStatement
    : BREAK
    ;

continueStatement
    : CONTINUE
    ;






scope:
    assignmentStatement
    | procedureCall     
    | localvariableDeclarationStatement
    | forStatement
    ;

assignmentStatement
    : qualifiedIdent ASSIGN expression
    ;

procedureCall
    : ( 'WRITE' | 'WRITELN' | 'READLN') LPAREN parameterList? RPAREN
    | qualifiedIdent (LPAREN parameterList? RPAREN)?
    ;


parameterList
    : expression (COMMA expression)*
    ;
expression
    : objectInstantiation
    | methodCall
    | qualifiedIdent
    | stringLiteral
    | relationalExpression
    |additionExpression     
    | NUMBER
    ;

additionExpression
    : factor ('+' factor)*    // Only support +
    ;

factor
    : NUMBER
    | IDENT
    |qualifiedIdent
    ;
relationalExpression
    : additiveExpression (relOp additiveExpression)?
    ;
additiveExpression
    : term
    ;

term
    : factor
    ;

relOp
    : GT
    | LT
    | EQUALTO
    ;
GT : '>';
LT : '<';    
objectInstantiation
    : IDENT '.' CREATE   // Allow `Create` to be an IDENT
    ;

methodCall
    : qualifiedIdent '(' parameterList? ')'       // Match obj.Method()
    ;

qualifiedIdent
    : IDENT ('.' IDENT)* 
    |'Create'  // Supports obj.field or obj.Method
    | COLON EQUAL
    ;


visibilitySection
    : visibilityModifier 
      (fieldDeclaration | methodDeclaration | constructorDeclaration | destructorDeclaration)*
    ;

visibilityModifier
    : PUBLIC
    | PRIVATE
    | PROTECTED
    ;

// Add tokens
PUBLIC: 'public';
PRIVATE: 'private';
PROTECTED: 'protected';

// Tokens
PROGRAM: 'program';
CREATE: 'Create';
TYPE: 'type';
IF: 'if';
THEN: 'then';
ELSE: 'else';
WHILE: 'while';
DO: 'do';
BREAK: 'break';
CONTINUE: 'continue';
VAR: 'var';
CLASS: 'class';
PROCEDURE: 'procedure';
BEGIN: 'begin';
END: 'end';
SEMI: ';';
COLON: ':';
DOT:'.';
CONSTRUCTOR: 'constructor';
DESTRUCTOR: 'destructor';
OVERRIDE: 'override';
INHERITED: 'inherited';
EQUAL: '=';
EQUALTO:'==';
PLUS   : '+';
MINUS  : '-';
STAR   : '*';
SLASH  : '/';
ASSIGN: ':=';
LPAREN: '(';
RPAREN: ')';
FOR:'for';
TO: 'to';
DOWNTO: 'downto';

// Rules for types and identifiers
typeIdentifier
    : identifier
    | STRING
    | INTEGER
    ;

identifier
    : IDENT
    ;

stringLiteral
    : STRING_LITERAL
    ;

// Token definitions

WS
    : [ \t\r\n]+ -> skip
    ;

NUMBER: [0-9]+;

IDENT
    : ('A' .. 'Z') ('A' .. 'Z' | '0' .. '9' | '_')*
    ;
STRING: 'String';
INTEGER: 'Integer';

simpleType
    : typeIdentifier
    ;

STRING_LITERAL
    : '\'' (~['\r\n] | '\'\'')* '\''
    ;

COMMA: ',';

methodImplementation
    : PROCEDURE qualifiedIdent SEMI compoundStatement 
    ;



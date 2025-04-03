/*
BSD License

Copyright (c) 2013, Tom Everett
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

1. Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.
3. Neither the name of Tom Everett nor the names of its contributors
   may be used to endorse or promote products derived from this software
   without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
/*
Adapted from pascal.g by  Hakki Dogusan, Piet Schoutteten and Marton Papp
*/

// $antlr-format alignTrailingComments true, columnLimit 150, minEmptyLines 1, maxEmptyLinesToKeep 1, reflowComments false, useTab false
// $antlr-format allowShortRulesOnASingleLine false, allowShortBlocksOnASingleLine true, alignSemicolons hanging, alignColons hanging

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
    : identifier COLON typeIdentifier SEMI
    ;



statement
    : assignmentStatement
    | procedureCall
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
    | NUMBER
    ;


objectInstantiation
    : IDENT '.' CREATE   // Allow `Create` to be an IDENT
    ;

methodCall
    : qualifiedIdent '(' parameterList? ')'       // Match obj.Method()
    ;

qualifiedIdent
    : IDENT ('.' IDENT)* 
    |'Create'  // Supports obj.field or obj.Method
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
ASSIGN: ':=';
LPAREN: '(';
RPAREN: ')';
// Rules for types and identifiers
typeIdentifier
    : identifier
    | STRING
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



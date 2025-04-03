#!/bin/bash

# Check if input file exists
if [ ! -f "test1.pas" ]; then
    echo "Error: test1.pas not found"
    exit 1
fi

# Generate parser from grammar
antlr4 -visitor -no-listener delphi.g4
if [ $? -ne 0 ]; then
    echo "Error: ANTLR4 grammar generation failed"
    exit 1
fi

# Compile Java files
javac -cp "lib/antlr4.jar:src/main/java" -d target src/main/java/*.java
if [ $? -ne 0 ]; then
    echo "Error: Java compilation failed"
    exit 1
fi

# Run TestRig
java -cp "lib/antlr4.jar:target" DelphiInterpreter test1.pas
# java -cp "lib/antlr4.jar:target" DelphiInterpreter < test1.pas 
if [ $? -ne 0 ]; then
    echo "Error: TestRig execution failed"
    exit 1
fi

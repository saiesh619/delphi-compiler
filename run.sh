#!/bin/bash

set -e

# Define paths
ANTLR_JAR="lib/antlr4.jar"
TARGET_DIR="target"
INPUT_FILE="testFor.pas"
LLVM_FILE="testFor.ll"
OUTPUT_BINARY="output"

javac -cp "lib/antlr4.jar" -d target src/main/java/*.java

# Compile using ANTLR + your Main class
java -cp "$ANTLR_JAR:$TARGET_DIR" Main "$INPUT_FILE"

# Compile LLVM IR to native binary using clang
clang "$LLVM_FILE" -o "$OUTPUT_BINARY"

# Run the output binary
./"$OUTPUT_BINARY"

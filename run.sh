#!/bin/bash

# Exit immediately if a command exits with a non-zero status
set -e

# Define paths
ANTLR_JAR="lib/antlr4.jar"
TARGET_DIR="target"
INPUT_FILE="test.pas"
LLVM_FILE="test.ll"
OUTPUT_BINARY="output"

javac -cp "lib/antlr4.jar" -d target src/main/java/*.java

# Compile using ANTLR + your Main class
echo "Running Java compiler with ANTLR..."
java -cp "$ANTLR_JAR:$TARGET_DIR" Main "$INPUT_FILE"

# Compile LLVM IR to native binary using clang
echo "Compiling LLVM IR with Clang..."
clang "$LLVM_FILE" -o "$OUTPUT_BINARY"

# Run the output binary
echo "Running output binary..."
./"$OUTPUT_BINARY"

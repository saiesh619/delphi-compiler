#!/bin/bash

ANTLR_JAR="lib/antlr4.jar"
TARGET_DIR="target"
OUTPUT_BINARY="output"

for INPUT_FILE in *.pas; do
    BASENAME=$(basename "$INPUT_FILE" .pas)
    echo "Compiling $INPUT_FILE to ${BASENAME}.ll"
    java -cp "$ANTLR_JAR:$TARGET_DIR" Main "$INPUT_FILE" > "${BASENAME}.ll"
    # Compile LLVM IR to native binary using clang
    clang "${BASENAME}.ll" -o "$OUTPUT_BINARY"

    # Run the output binary
    ./"$OUTPUT_BINARY"  
done


🔧 Delphi-to-LLVM IR Compiler

* Converts Pascal-style code into LLVM IR and WebAssembly (WASM)
* Evolved from an interpreter into a full compiler using ANTLR4, LLVM, and Emscripten

📁 Project Overview

* Parses `.pas` files using custom ANTLR4 grammar
* Generates LLVM IR via a Java-based visitor
* Optionally compiles IR to WebAssembly for browser execution

🗂️ Directory Structure

* Main.java – Entry point to parse `.pas` and emit LLVM IR
* LLVMGenerator.java – Walks AST and generates LLVM IR
* delphi.g4 – ANTLR4 grammar for Pascal/Delphi subset
* \*.pas – Sample input files
* \*.ll – LLVM IR output
* output.js / output.wasm / output.html – WebAssembly output and loader
* compile\_all.sh – Batch compile all `.pas` files
* run.sh – Run/verify individual tests

🛠️ How to Use

1. Generate parser & build:

   * `antlr4 -visitor -no-listener delphi.g4`
   * `javac -cp lib/antlr4.jar *.java`

2. Compile a `.pas` file:

   * `java -cp lib/antlr4.jar:. Main test1.pas`

3. Compile all files:

   * `chmod +x compile_all.sh && ./compile_all.sh`

🌐 WebAssembly Compilation

* Convert LLVM IR to WASM: `emcc test.ll -o output.js -s WASM=1`
* Serve locally: `python3 -m http.server 8000`
* Open in browser: `http://localhost:8000/output.html`

✅ Features

* Classes & Objects – testOops.pas
* Encapsulation – testEncap.pas
* Static Scoping – testscope.pas
* While Loops – testWhile.pas
* For Loops – testFor.pas
* Procedures – testProc.pas
* Functions – testFunc.pas

🤖 Tech Stack

* ANTLR4 – Grammar and parser generation
* Java – AST walking and IR generation
* LLVM – Target backend
* Emscripten – IR to WebAssembly conversion

📌 Notes

* Modular and extensible for new features
* Focus on correctness, clarity, and static scoping

💡 Future Additions

* Type checking and semantic validation
* Optimized IR generation
* Interactive web-based IDE for live execution


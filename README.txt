--------------------------------------
Delphi to LLVM IR Compiler (Project 3)
--------------------------------------

This project compiles our Delphi/Pascal language to LLVM Intermediate Representation (IR).
As an extension of Project 1 and 2, this project replaces interpretation with code generation. For extra credit,
it also supports compiling LLVM IR to WebAssembly (WASM) using Emscripten and running it in a browser.

-------------------------------------------------------
Project Directory Contents (delphi-compiler_final/)
-------------------------------------------------------

Main.java               - Main class that parses .pas files and generates LLVM IR
LLVMGenerator.java      - Visitor that emits LLVM IR instead of evaluating
delphi.g4               - ANTLR grammar 
*                       - Sample Delphi input files
*.ll                    - Corresponding generated LLVM IR files
output.wasm             - WebAssembly file compiled from LLVM IR (via Emscripten)
output.html             - Web page to load and execute the .wasm module
output.js               - JavaScript to load and call exported WASM functions
run.sh                  - Run and verify individual testfiles (execute ./output after this)
compile_all.sh          - Script to batch compile all .pas files to .ll files

---------------------
How to Build and Run
---------------------

1. Generate ANTLR parser and compile Java files:
   antlr4 -visitor -no-listener delphi.g4
   javac -cp lib/antlr4.jar *.java

2. Compile a single .pas file to LLVM IR:
   java -cp lib/antlr4.jar:target Main test1.pas

3. Compile all .pas files to LLVM IR .ll at once in the directory:
   chmod +x compileAll.sh
   ./compileAll.sh

-----------------------------
LLVM IR to WebAssembly (Extra Credit)
-----------------------------

We use Emscripten to compile LLVM IR to WebAssembly:

1. Compile .ll to .wasm using emcc:
   emcc test.ll -o output.js -s WASM=1 

2. This will generate:
   - output.js      (glue code to load the wasm)
   - output.wasm    (compiled WebAssembly)

3. Run in browser:
   In another terminal, execute python3 -m http.server 8000 
   Visit http://localhost:8000/output.html on the browser to see the output for that specific test case/
   It uses output.html to render, output.js to load and instantiate output.wasm and call the exported function.

-----------------------------
Language Features Implemented
-----------------------------

testOops.pas      - Class and Objects
testEncap.pas     - Encapsulation
testscope.pas     - Static scoping
testWhile.pas     - While-do loop and continue keyword
testFor.pas       - for-do loop ,break and continue keywords
testProc.pas      - User defined procedures
testFunc.pas      - User defined functions

----------------
Demo Video Link
----------------

Watch our demo video here:
https://drive.google.com/file/d/1t-Fen5IweA7EiID8iTq2_ykl9r5e09Zz/view


-------------------
Credits / Team Info
-------------------

- Vaishnavi Kalva (51796466)
- Saiesh Prabhu (57751975)

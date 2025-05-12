
 🔧 Delphi-to-LLVM IR Compiler

Transform Pascal-style code into **LLVM Intermediate Representation (IR)** — and even run it in the browser via **WebAssembly**!  
This project evolves from a basic interpreter into a full compiler pipeline using **ANTLR4**, **LLVM**, and **Emscripten**.


 📁 Project Overview

This compiler supports a Delphi-like language and performs the following:

- Parses `.pas` files using a custom ANTLR4 grammar
- Generates LLVM IR via a Java-based visitor
- Optionally compiles IR to WebAssembly for browser execution

---

## 🗂️ Directory Structure

```

delphi-compiler/
├── Main.java             # Entry point: parses .pas files and emits LLVM IR
├── LLVMGenerator.java    # Visitor: walks AST and emits LLVM IR
├── delphi.g4             # ANTLR4 grammar for Pascal/Delphi subset
├── \*.pas                 # Sample source files
├── \*.ll                  # Generated LLVM IR output
├── output.js             # JavaScript glue for WebAssembly
├── output.wasm           # WebAssembly binary
├── output.html           # Web interface to run WASM
├── compile\_all.sh        # Compile all .pas → .ll
├── run.sh                # Run/verify a specific test





 🛠️ Step 1: Generate Parser & Build Compiler

```bash
antlr4 -visitor -no-listener delphi.g4
javac -cp lib/antlr4.jar *.java
````

 🔄 Step 2: Compile a `.pas` file to LLVM IR

```bash
java -cp lib/antlr4.jar:. Main test1.pas
```

 📦 Step 3: Compile All `.pas` Files

```bash
chmod +x compile_all.sh
./compile_all.sh
```

---

🌐 WebAssembly Integration (Optional)

Use **Emscripten** to convert LLVM IR into WebAssembly and run it in a browser.

### ⚙️ Compile to WASM

```bash
emcc test.ll -o output.js -s WASM=1
```

 🌍 Serve Locally

```bash
python3 -m http.server 8000
# Open: http://localhost:8000/output.html
```

---

 ✅ Supported Features

| Feature               | File            | Description                                 |
| --------------------- | --------------- | ------------------------------------------- |
| **Classes & Objects** | `testOops.pas`  | Object instantiation, constructors, methods |
| **Encapsulation**     | `testEncap.pas` | Access control for object members           |
| **Static Scoping**    | `testscope.pas` | Nested scope resolution                     |
| **While Loops**       | `testWhile.pas` | `while-do` loop with `continue`             |
| **For Loops**         | `testFor.pas`   | `for-do` loop with `break`/`continue`       |
| **Procedures**        | `testProc.pas`  | Custom procedures with parameters           |
| **Functions**         | `testFunc.pas`  | Return-type functions with full support     |

---

## 🤖 Tech Stack

ANTLR4 – Grammar and parser generation
Java – AST walking & LLVM IR code generation
LLVM – Target backend for IR representation
Emscripten** – IR → WebAssembly conversion

---

 📌 Notes

 The project is modular and extensible for new language features
 Focus is on correctness, clarity, and scoping for a Pascal-like syntax

---

 💡 Future Additions

 Type checking and semantic validation
 Optimized IR generation
 Interactive Web IDE for live code execution

---


Here’s a polished and beginner-friendly version of your `README.md` for the `delphi-compiler` project:

---

```markdown
# 🛠️ Delphi to LLVM IR Compiler

Welcome to our **Delphi-to-LLVM IR compiler**, a full-featured project that transforms Pascal-style source code into low-level **LLVM Intermediate Representation (IR)** — and even compiles it into **WebAssembly (WASM)** for execution in the browser!

This project is a continuation of our interpreter efforts, evolving into a full compiler pipeline using ANTLR, LLVM, and Emscripten.

---

## 📁 Project Structure

```

delphi-compiler/
├── Main.java             # Entry point: parses .pas files and emits LLVM IR
├── LLVMGenerator.java    # Visitor: walks the AST and generates LLVM IR code
├── delphi.g4             # ANTLR grammar for our Delphi subset
├── \*.pas                 # Sample Delphi input programs
├── \*.ll                  # Output LLVM IR files
├── output.js             # JavaScript loader for WebAssembly
├── output.wasm          # WebAssembly binary compiled from LLVM IR
├── output.html          # Webpage that loads & runs the WebAssembly module
├── compile\_all.sh        # Batch-compile all .pas → .ll
├── run.sh                # Run/verify a specific compiled test

````

---

## 🚀 Getting Started

### Step 1: Build the Parser & Compiler

Generate the ANTLR parser and compile all Java files:
```bash
antlr4 -visitor -no-listener delphi.g4
javac -cp lib/antlr4.jar *.java
````

### Step 2: Compile a Delphi `.pas` file to LLVM IR

```bash
java -cp lib/antlr4.jar:. Main test1.pas
```

### Step 3: Compile All `.pas` Files at Once

```bash
chmod +x compile_all.sh
./compile_all.sh
```

---

## 🌐 WebAssembly Output (Extra Credit)

You can take the generated `.ll` LLVM IR file and compile it into WebAssembly using Emscripten:

### Step 1: Compile `.ll` to WebAssembly

```bash
emcc test.ll -o output.js -s WASM=1
```

### Step 2: Run in Your Browser

```bash
python3 -m http.server 8000
# Now open http://localhost:8000/output.html
```

This setup uses:

* `output.js`: glue code to load WebAssembly
* `output.wasm`: compiled binary
* `output.html`: interactive HTML page to run the test

---

## ✅ Features Implemented

| Feature           | Example File    | Description                                    |
| ----------------- | --------------- | ---------------------------------------------- |
| Classes & Objects | `testOops.pas`  | Supports object creation and method calls      |
| Encapsulation     | `testEncap.pas` | Variable/method access controls                |
| Static Scoping    | `testscope.pas` | Nested blocks with correct scope resolution    |
| While Loops       | `testWhile.pas` | `while-do` and `continue` keyword              |
| For Loops         | `testFor.pas`   | `for-do`, `break`, and `continue` support      |
| Procedures        | `testProc.pas`  | User-defined procedures with parameter passing |
| Functions         | `testFunc.pas`  | Full function support including return types   |

---

## 📽️ Demo Video

Watch our live demo here:
🎥 [Demo on Google Drive](https://drive.google.com/file/d/1t-Fen5IweA7EiID8iTq2_ykl9r5e09Zz/view)

---


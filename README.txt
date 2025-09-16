



````markdown
# 🔧 Delphi-to-LLVM IR Compiler

A **Pascal/Delphi-to-LLVM IR and WebAssembly compiler**.  
Originally built as an **interpreter**, now evolved into a **full compiler** using **ANTLR4, LLVM, and Emscripten**.

---

## 📁 Project Overview
- Parses `.pas` files with a custom **ANTLR4 grammar**  
- Walks the **AST** with a **Java-based visitor** to generate LLVM IR  
- Optionally compiles IR into **WebAssembly** for browser execution  

---

## 🗂️ Directory Structure

```plaintext
.
├── Main.java           # Entry point – parse `.pas` and emit LLVM IR
├── LLVMGenerator.java  # AST visitor – generates LLVM IR
├── delphi.g4           # ANTLR4 grammar for Pascal/Delphi subset
├── *.pas               # Sample input files
├── *.ll                # LLVM IR outputs
├── output.js           # WASM + JS loader
├── output.wasm         # Compiled WebAssembly binary
├── output.html         # Test harness for running in browser
├── compile_all.sh      # Batch-compile all `.pas` files
└── run.sh              # Run/verify individual tests
````

---

## 🚀 Getting Started

### 🔨 1. Generate Parser & Build

```bash
antlr4 -visitor -no-listener delphi.g4
javac -cp lib/antlr4.jar *.java
```

### 📦 2. Compile a `.pas` File

```bash
java -cp lib/antlr4.jar:. Main test1.pas
```

### ⚡ 3. Compile All Files

```bash
chmod +x compile_all.sh
./compile_all.sh
```

---

## 🌐 WebAssembly Compilation

Convert **LLVM IR → WebAssembly**:

```bash
emcc test.ll -o output.js -s WASM=1
python3 -m http.server 8000
```

Open in browser:

```
http://localhost:8000/output.html
```

---

## ✅ Supported Features

### 🔹 Core Language

* Classes & Objects – [`testOops.pas`](./testOops.pas)
* Encapsulation – [`testEncap.pas`](./testEncap.pas)
* Static Scoping – [`testScope.pas`](./testScope.pas)

### 🔹 Control Flow

* While Loops – [`testWhile.pas`](./testWhile.pas)
* For Loops – [`testFor.pas`](./testFor.pas)

### 🔹 Procedures & Functions

* Procedures – [`testProc.pas`](./testProc.pas)
* Functions – [`testFunc.pas`](./testFunc.pas)

---

## 🤖 Tech Stack

* **ANTLR4** → Grammar & parser generation
* **Java** → AST traversal & IR generation
* **LLVM** → Backend IR target
* **Emscripten** → LLVM IR → WebAssembly

---

## 📌 Notes

✔️ Designed for **clarity, modularity, and correctness**
✔️ Implements **static scoping**
✔️ Easy to extend with new language features

---

## 💡 Future Roadmap

* [ ] Type checking & semantic validation
* [ ] Optimized IR generation
* [ ] Interactive **web-based IDE** for live execution

```



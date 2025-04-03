Here’s an updated and detailed **README.md** file that clearly explains how to run the project, what’s implemented, and what’s planned for future additions. This should be suitable for submission and helpful for the TAs reviewing your work.

---

# Delphi Interpreter (ANTLR4 + Java)

## 📌 Introduction
Pascal is a procedural programming language developed by Niklaus Wirth in 1970. Delphi is its object-oriented successor, much like how C++ is to C. This project extends Pascal by adding key object-oriented features using ANTLR4 and Java, building a functioning interpreter that processes `.pas` (Delphi) files.

---

## 🧠 What’s Implemented (Project Part 1)

### ✅ Language Features Implemented:
- **Classes and Objects**
- **Encapsulation (class variables and methods)**
- **Constructors**
- **Input/Output operations using `ReadLn` and `WriteLn`**

### 📦 Grammar
- `delphi.g4` is a modified version of the Pascal grammar, extended to support Delphi-like constructs.

### 🖥️ Interpreter
- `DelphiInterpreter.java` uses the ANTLR4 visitor pattern to walk through the generated AST and interpret the language behavior.

---

## 🚀 How to Run the Code

### 📁 Project Structure
```
delphi-compiler/
├── lib/
│   └── antlr4.jar
├── src/
│   └── main/
│       └── java/
│           ├── delphi.g4
│           └── DelphiInterpreter.java
├── target/
├── test1.pas
├── test2.pas
├── test3.pas
├── test4.pas
└── pom.xml
```

### 1. Generate Lexer & Parser
**Run from `src/main/java/` directory:**
```bash
antlr4 -visitor -no-listener delphi.g4
```

### 2. Compile Java Files
**Run from the project root (`delphi-compiler/`):**
```bash
javac -cp "lib/antlr4.jar:src/main/java" -d target src/main/java/*.java
```

### 3. Execute Test Files
**Run each test case from the project root (`delphi-compiler/`):**
```bash
java -cp "lib/antlr4.jar:target" DelphiInterpreter test1.pas
java -cp "lib/antlr4.jar:target" DelphiInterpreter test2.pas
java -cp "lib/antlr4.jar:target" DelphiInterpreter test3.pas
java -cp "lib/antlr4.jar:target" DelphiInterpreter test4.pas
```

---

## 🧪 Test Case Breakdown

| Test Case | Description |
|-----------|-------------|
| **test1.pas** | Demonstrates classes, object instantiation, class methods, and class variables with integer input/output. |
| **test2.pas** | Same as test1, but with string input/output showcasing OOP features. |
| **test3.pas** | Adds constructor functionality and shows variable initialization through the constructor. |
| **test4.pas** | Basic Hello World-style program using `WriteLn` to print messages to the terminal. |

---

## 🔨 Tools & Dependencies
- **ANTLR4** (used to define grammar and generate parser)
- **Java 8+**
- **Maven** (for managing dependencies via `pom.xml`)

---

## 🏗️ What We Will Add (Project Part 2)

### 🧩 Language Extensions
- [ ] `while-do` and `for-do` loops
- [ ] `break` and `continue` support
- [ ] User-defined **procedures** and **functions**
- [ ] Proper **static scoping**

### 🧠 Scope Management
- New scope created for:
  - Each block (e.g., `for`, `while`)
  - Each procedure/function
- Global and local variable resolution with chained scopes

---

## ✨ Bonuses (Planned for Part 2)

| Bonus | Description |
|-------|-------------|
| ✅ **Constant Propagation** | Compile-time evaluation of constant expressions like `v = 2*(10+11)` → `v = 42`. |
| ✅ **Formal Parameter Passing** | Pass parameters to procedures/functions with correct scope handling. |

---

## 📦 Submission Instructions
Turn in the following:
- `delphi.g4` grammar
- All Java source files (`*.java`)
- Test files (`*.pas`)
- `README.md`
- `pom.xml`
- Bundle everything in a `.zip` file

---

If you'd like, I can also generate this as a markdown file (`README.md`) or help you create a polished GitHub repository. Want that?
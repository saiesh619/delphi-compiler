HOW TO RUN THE CODE:

delphi.g4 - is in src/main/java
DelphiInterpreter.java - is in src/main/java
test files - in delphi-compiler directory (main folder)

1. To generate parser and lexers and helpers (run from src/main/java directory)
    antlr4 -visitor -no-listener delphi.g4

    antlr4 -visitor -no-listener src/main/java/delphi.g4

2. Compile all the Java files (run from delphi-compiler directory(main folder))

    javac -cp "lib/antlr4.jar:src/main/java" -d target src/main/java/*.java

3. Run Java Interpreter for Delphi for each test file (run from delphi-compiler directory(main folder))
    Test case 1 : java -cp "lib/antlr4.jar:target" DelphiInterpreter test1.pas 
    Test case 2 : java -cp "lib/antlr4.jar:target" DelphiInterpreter test2.pas 
    Test case 3 : java -cp "lib/antlr4.jar:target" DelphiInterpreter test3.pas 
    Test case 4 : java -cp "lib/antlr4.jar:target" Main test.pas 

    Similary for other tests 

export PATH="/opt/homebrew/opt/llvm/bin:$PATH"
 
llc -march=wasm32 -filetype=obj output.ll -o output.o
 
wasm-ld --no-entry --export-all --allow-undefined -o output.wasm output.o

Test case 1 : There are four test cases. This test case creates a class and an object of that classs. This class has member funtions and class variables.
             We take a numberical input from user and echo's it back on the terminal. The numerical input is the class variable being assigned the value. 
             This way we exhibit Object Oriented functionality of accessing member functions and variables with this test case

Test case 2 : Same as above but take string input name and print it out to the terminal also using Classes,Objects, Class Methods,Variables

Test case 3 : Constructor functionality added in this test case. Display of variable done through constructor call during object instantiation with  FOR LOOP

Test case 4 : Simple hello world program to demonstrate functionality of displaying custom messages on terminal, using WriteLn

Test case 5 : User defined procedures,while loop and continue keyword functionality

Test case 6 : Displaying Local varibale (Scoping)

Test case 7  : Displaying Global varibale (Scoping)

Test case 8 : User defined procedures,while loop and break keyword functionality

Test case 9 : User defined functions which can return values. Exihibited this functionality by displaying the returned value in Writeln functionality


Things to do in Project 2;
For loop : Test case 3
While loop : Test 5,8
Continue : Test case 5
Break : Test case 8
Functions : Test 9
Procedures is in all test cases except 9

Static scoping is done in all test cases. Explicitly tested in test case 6 & 7
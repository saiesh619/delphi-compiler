HOW TO RUN THE CODE:

delphi.g4 - is in src/main/java
DelphiInterpreter.java - is in src/main/java
test files - in delphi-compiler directory (main folder)


1. To generate parser and lexers and helpers (run from src/main/java directory)
    antlr4 -visitor -no-listener delphi.g4

2. Compile all the Java files (run from delphi-compiler directory(main folder))

    javac -cp "lib/antlr4.jar:src/main/java" -d target src/main/java/*.java

3. Run Java Interpreter for Delphi for each test file (run from delphi-compiler directory(main folder))
    Test case 1 : java -cp "lib/antlr4.jar:target" DelphiInterpreter test1.pas 
    Test case 2 : java -cp "lib/antlr4.jar:target" DelphiInterpreter test2.pas 
    Test case 3 : java -cp "lib/antlr4.jar:target" DelphiInterpreter test3.pas 
    Test case 4 : java -cp "lib/antlr4.jar:target" DelphiInterpreter test4.pas 



Test case 1 : There are four test cases. This test case creates a class and an object of that classs. This class has member funtions and class variables.
             We take a numberical input from user and echo's it back on the terminal. The numerical input is the class variable being assigned the value. 
             This way we exhibit Object Oriented functionality of accessing member functions and variables with this test case

Test case 2 : Same as above but take string input name and print it out to the terminal also using Classes,Objects, Class Methods,Variables

Test case 3 : Constructor functionality added in this test case. Display of variable done through constructor call during object instantiation

Test case 4 : Simple hello world program to demonstrate functionality of displaying custom messages on terminal, using WriteLn


------We have added a pom.xml file that is needed to add the antlr dependency to the java maven project.
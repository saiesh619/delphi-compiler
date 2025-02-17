# delphi-compiler
This is a very simple grammar and interpreter writen for Delphi(Object Pascal)
Commands to run : antlr4 -visitor -no-listener delphi.g4(Generates Parser using ANTLR)
javac -cp "lib/antlr4.jar:src/main/java" -d target src/main/java/*.java ( Compilers Java files)
java -cp "lib/antlr4.jar:target" DelphiInterpreter test1.pas   (Runs Test1.pas code)
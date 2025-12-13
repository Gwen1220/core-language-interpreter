Project5
Name: Jiaqing Guan

1. Files Submitted

Main.java — Entry point. Initializes scanners, parses the .code, attaches .data, runs executor and optional semantic checks.

project2/Core.java — Token/keyword/symbol definitions.

project2/CoreScanner.java — Scanner for .code and used to read ints from .data if desired.

project2/Parser/Parser.java — Recursive-descent parser updated from Project 3 to handle procedure declarations (<function>) and procedure calls (<call>).

project2/Node/ — AST node classes with execute/evaluate:

 - Existing: ProcedureNode, DeclSeqNode, StmtSeqNode, DeclNode, DeclIntegerNode, DeclObjNode, StmtNode, AssignNode, IfNode, LoopNode, PrintNode, ReadNode, ExprNode, TermNode, FactorNode, CmprNode, CondNode, VariableTable, CoreObject, FunctionNode, CallNode, ParameterNode, FunctionMap.

 - New: GcManager.java — A global reference, which tracks reachable objects and prints gc:n whenever the count changes.

project2/SemanticChecker.java, SemanticException.java —  Semantic error and check handling classes.(Not used in project5)

project2/ParserException.java — Error handling classes.

Correct — Test programs (.code), input (.data), expected outputs (.expected).

tester.sh — Script to compile/run/diff.


2. Overall Design

Project 5 extends the previous interpreter by adding a simple reference-counting garbage collector. Each time a new object is created, GcManager prints “gc:n”. When an object loses its final reference—either because a variable is reassigned, overwritten, or a scope ends—the reference count decreases and “gc:n” is printed again. The design reuses Project 4’s call-stack model: each procedure call creates a new scope, and objects follow call-by-sharing. Garbage-collection logic is embedded inside VariableTable, so GC updates automatically occur during object creation, assignment, and scope exit.


3. Testing

Before each run, I switch to the src directory, delete all existing .class files using "find . -name "*.class" -delete", and then run the interpreter through "bash tester.sh" to validate the outputs.

I verified: 

Correct GC behavior during: object creation (gc:+1)， local object loss after procedure return (gc:-1)， overwriting object variables， exiting nested scopes

Correct numeric output for all print statements.

No known bugs. This project passed all tests.

4. Special Features

GC implementation is lightweight and fully integrated into existing logic.

No additional data structures beyond GcManager were added.

Reference counting is precise: only objects losing their final reference trigger gc:n decrement events.
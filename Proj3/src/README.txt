Project3
Name: Jiaqing Guan

1. Files Submitted

Main.java — Entry point. Initializes scanners, parses the .code, attaches .data, runs executor and optional semantic checks.

project2/Core.java — Token/keyword/symbol definitions.

project2/CoreScanner.java — Scanner for .code and used to read ints from .data if desired.

project2/Parser/ Parser.java — Recursive-descent parser from Project 2, builds AST.

project2/Node/ — AST node classes with execute/evaluate:

ProcedureNode, DeclNode, StmtNode, AssignNode, IfNode, LoopNode, PrintNode, ReadNode, ExprNode, TermNode, FactorNode, CmprNode, CondNode, VariableTable, etc.

project2/SemanticChecker.java, SemanticException.java —  Semantic error and check handling classes.

project2/ParserException.java — Error handling classes.

Correct/, Error/ — Test programs (.code), input (.data), expected outputs (.expected).

tester.sh — Script to compile/run/diff.


2. Overall Design

Project3 design from Scanner to Parser to Executor. AST is executed with a VariableTable that keeps a scope stack (Deque<Map<String,Object>>). Integers use the value model, which is Integer. Objects use the reference model, which is Map<String,Integer>. Declarations go to the current scope, lookups search from top scope down. Assignments overwrite the nearest existing binding, which can avoid accidental shadowing, entering a block calls enterNewScope() so new declarations may shadow outer ones. Object ops: a["k"]=v updates the map, a = new object("k", e) binds a fresh map with default key, a1 : a2 shares the same map reference. Runtime errors like input exhausted, divide-by-zero and missing key print to stdout and exit.


3. Testing

In Project 3, I test the interpreter by first changing to the `src` folder, deleting all old `.class` files using `find . -name "*.class" -delete`, and then running `bash tester.sh` to compare my program’s output with the `.expected` files. All results, including error messages, print to stdout. I also test three specific runtime-error tests: 
(1) read input exhausted, which prints `ERROR: input exhausted` and stops.
(2) division by zero, which prints `ERROR: division by 0` and stops.
(3) missing object key in expressions like `id["key"]`, which prints `ERROR: key "key" not found in object "id"` and stops. 
All regular and error tests pass successfully.

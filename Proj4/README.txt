Project4
Name: Jiaqing Guan

1. Files Submitted

Main.java — Entry point. Initializes scanners, parses the .code, attaches .data, runs executor and optional semantic checks.

project2/Core.java — Token/keyword/symbol definitions.

project2/CoreScanner.java — Scanner for .code and used to read ints from .data if desired.

project2/Parser/Parser.java — Recursive-descent parser updated from Project 3 to handle procedure declarations (<function>) and procedure calls (<call>).

project2/Node/ — AST node classes with execute/evaluate:

 - Existing: ProcedureNode, DeclSeqNode, StmtSeqNode, DeclNode, DeclIntegerNode, DeclObjNode, StmtNode, AssignNode, IfNode, LoopNode, PrintNode, ReadNode, ExprNode, TermNode, FactorNode, CmprNode, CondNode, VariableTable.

 - New: FunctionNode, ParameterNode, CallNode, FunctionMap (for global function registration).

project2/SemanticChecker.java, SemanticException.java —  Semantic error and check handling classes.(Not used in project4)

project2/ParserException.java — Error handling classes.

Correct/, Error/ — Test programs (.code), input (.data), expected outputs (.expected).

tester.sh — Script to compile/run/diff.


2. Overall Design

The Project 4 interpreter extends the Project 3 Core language to support procedure declarations and procedure calls using recursion and a call stack.

 - Parser layer: Adds parseFunction() and parseCall() for the grammar rules. Each declared function is stored in FunctionMap, ensuring unique names.

 - Executor layer: A call stack is implemented using Deque<VariableTable>. This structure supports recursion and nested calls. Variables in inner frames shadow outer scopes but do not overwrite them.


3. Testing

Before each run, all .class files are deleted with "find . -name "*.class" -delete", and the interpreter is tested via "bash tester.sh".

The test set includes both correct cases and error cases.

For the correct cases, I verified that normal procedure definitions, parameter passing correctly. Examples include procedures that print integer values, call other procedures, and modify shared object parameters. All valid test programs produce the expected printed output exactly as in the .expected files.

For the error cases, I tested semantic and syntax errors such as:
 - missing procedure body (00.error),
 - calling an undefined function (01.error),
 - duplicate parameter names (02.error), 
 - duplicate procedure names (03.error).

This project passed all tests.
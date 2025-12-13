Project2
Name: Jiaqing Guan

Please read through the Testing section first. Before running tester.sh, you must compile the files; otherwise, Main will not be recognized.

1. Files Submitted

Main.java: Entry point. Runs scanner, parser, semantic checks, and printer.

Core.java: Defines keywords, symbols, and special.

Scanner.java: Converts input characters into tokens.

Node classes: Recursive descent parse tree nodes.

SemanticChecker.java: Performs semantic checks, including declarations, scope and type checking.

ParseException.java: Parser error handling classes

SemanticException.java: Semantic error handling classes.

2. Design

I built my parser using recursive descent. Each grammar rule has a matching Node class and a parse function. For example, <procedure> is represented by ProcedureNode, <stmt> by StmtNode, and <expr> by ExprNode. Each node stores its children and has a toPrint() method that can print the code again to help check if the parse tree is correct.

Error handling is done during parsing. If the current token is not what program expect, the parser throws a ParserException. This way invalid programs are rejected cleanly.

Semantic checks are done separately after parsing. I check that all IDs are declared, that the right types (integer or object) are used, and that there are no duplicate declarations in the same scope.

Overall, the parser builds the tree, the semantic checker ensures correctness, and the printer helps me verify the structure. This makes the design clean and easier to debug.

3. Testing

To test my parser, I used the tester.sh script provided. On stdlinux, I first changed into the src directory: cd src
Then I compiled my code: javac project2/**/*.java
javac Main.java
After compilation, I ran the tester script: bash tester.sh
The tester checks both the Correct cases (26 programs) and the Error cases (10 programs). For the correct cases, it compares my pretty-print output with the original input. For the error cases, it asks me to confirm whether the error messages match the actual problem.

This way, I was able to confirm that my parser builds a valid parse tree and rejects invalid input with meaningful error messages.

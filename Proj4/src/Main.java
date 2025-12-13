import project2.CoreScanner;
import project2.ParserException;
import project2.SemanticChecker;
import project2.SemanticException;
import project2.Node.ProcedureNode;
import project2.Parser.Parser;

public class Main {
    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                System.out.println("Usage: java -jar CoreInterpreter.jar <source-file>");
                return;
            }
            String filePath = args[0];
            String inputPath = args[1];
           
        //    String filePath = "src/Correct/2.code";
        //    String inputPath = "src/Correct/2.data";

            CoreScanner scanner = new CoreScanner(filePath);

            Parser parser = new Parser(scanner);

            try {
                ProcedureNode root = parser.parseProcedure();
                // SemanticChecker checker = new SemanticChecker();
                // checker.check(root);
                //System.out.println(root.toPrint(""));
                root.execute(inputPath);
            } catch (ParserException e) {
                System.out.println(e.getMessage());
                return;
            } 
            // catch (SemanticException e) {
            //     System.out.println(e.getMessage());
            //     return;
            // }

        } catch (Exception e) {
            
            System.out.println("ERROR: Unexpected error - " + e.getMessage());
            System.exit(1);
        
        }
    }
}


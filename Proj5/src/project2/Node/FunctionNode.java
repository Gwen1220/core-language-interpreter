package project2.Node;

import java.util.ArrayList;
import java.util.List;

public class FunctionNode extends DeclNode {
    public String identifier = null;       // procedure ID
    public ParameterNode parameters = null; // <parameters>
    public StmtSeqNode stmtSeq = null;     // <stmt-seq>

    @Override
    public String toPrint(String tap) {
        String result = tap + "procedure " + this.identifier + " (object ";
        if (this.parameters != null) {
            result += this.parameters.toPrint("");
        }
        result += ") is\n";
        if (this.stmtSeq != null) {
            result += this.stmtSeq.toPrint(tap + "  ");
        }
        result += tap + "end\n";
        return result;
    }

    @Override
    public void Node() {}

    @Override
    public List<Node> get_children() {
        List<Node> children = new ArrayList<>();
        if (parameters != null) children.add(parameters);
        if (stmtSeq != null) children.add(stmtSeq);
        return children;
    }

    public void execute(VariableTable variableTable) {
        // Enter new scope for the function
        variableTable.enterNewScope();

        // Declare parameters as objects
        if (parameters != null) {
            for (String param : parameters.parameterList) {
                variableTable.declareObject(param);
            }
        }

        // Execute function body
        if (stmtSeq != null) {
            stmtSeq.execute(variableTable);
        }

        // Exit scope
        variableTable.exitScope();
    }
}

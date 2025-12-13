package project2.Node;
import java.util.List;
import java.util.ArrayList;

public class ProcedureNode extends Node { // DFS
    public String identifier = null;
    public DeclSeqNode declSeq = null;
    public StmtSeqNode stmtSeq = null;

    @Override
    public String toPrint(String tap) {
        if (this.declSeq != null) {
            return "procedure " + this.identifier + " is\n" + this.declSeq.toPrint(tap + "\t") + "begin\n" + this.stmtSeq.toPrint(tap + "\t") + "end";
        } else {
            return "procedure " + this.identifier + " is\n" + "begin\n" + this.stmtSeq.toPrint(tap + "\t") + "end";
        }
    }

    @Override
    public List<Node> get_children() {
        List<Node> children = new ArrayList<>();

        if (this.declSeq != null) {
            children.add(this.declSeq);
        }
        if (this.stmtSeq != null) {
            children.add(this.stmtSeq);
        }
        return children;
    }

    @Override
    public void Node() {
    }
}

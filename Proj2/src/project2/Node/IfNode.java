package project2.Node;

import java.util.ArrayList;
import java.util.List;

public class IfNode extends Node {
    public CondNode cond = null;
    public StmtSeqNode thenPart = null;
    public StmtSeqNode elsePart = null;

    @Override
    public String toPrint(String tap) {
        String result = tap + "if " + cond.toPrint("") + " then\n";
        result += thenPart.toPrint(tap + "\t");
        if (elsePart != null) {
            result += tap + "else\n";
            result += elsePart.toPrint(tap + "\t");
        }
        result += tap + "end\n";
        return result;
    }

    @Override
    public void Node() {
    }

    @Override
    public List<Node> get_children() {
        List<Node> children = new ArrayList<>();
        if (this.cond != null) {
            children.add(this.cond);
        }
        if (this.thenPart != null) {
            children.add(this.thenPart);
        }
        if (this.elsePart != null) {
            children.add(this.elsePart);
        }
        return children;   
    }
}

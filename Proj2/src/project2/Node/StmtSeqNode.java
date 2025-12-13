package project2.Node;

import java.util.ArrayList;
import java.util.List;

public class StmtSeqNode extends Node {
    public List<StmtNode> statements = new ArrayList<>();

    @Override
    public String toPrint(String tap) {
        String result = "";
        for (StmtNode stmt : this.statements) {
            result += stmt.toPrint(tap);
        }
        return result;
    }

    @Override
    public void Node() {
    }

    @Override
    public List<Node> get_children() {
        List<Node> children = new ArrayList<>();
        for (StmtNode stmt : this.statements) {
            children.add(stmt);
        }
        return children;
    }
}

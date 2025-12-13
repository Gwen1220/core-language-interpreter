package project2.Node;

import java.util.ArrayList;
import java.util.List;

public class PrintNode extends Node {

    public ExprNode expr = null;

    @Override
    public String toPrint(String tap) {
        return tap + "print(" + this.expr.toPrint("") + ");\n";
    }

    @Override
    public void Node() {
    }

    @Override
    public List<Node> get_children() {
        List<Node> children = new ArrayList<>();
        children.add(this.expr);
        return children;
    }
}

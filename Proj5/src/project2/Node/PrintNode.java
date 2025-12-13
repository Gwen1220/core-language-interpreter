package project2.Node;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

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

    public void execute(VariableTable variableTable) {
        Object value = this.expr.evaluate(variableTable);
        System.out.println(value);
        return;
    }

}

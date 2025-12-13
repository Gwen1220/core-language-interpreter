package project2.Node;

import java.util.ArrayList;
import java.util.List;

public class ExprNode extends Node {
    public List<TermNode> term = new ArrayList<>();
    public List<String> ops = new ArrayList<>(); // + or -

    @Override
    public String toPrint(String tap) {
        String result = "";
        for (int i = 0; i < this.term.size(); i++) {
            result += this.term.get(i).toPrint("");
            if (i < this.ops.size()) {
                if (this.ops.get(i) == "ADD") {
                    result += " " + "+" + " ";
                } else {
                    result += " " + "-" + " ";
                }

            }
        }
        return result;
    }

    @Override
    public void Node() {

    }

    @Override
    public List<Node> get_children() {
        List<Node> children = new ArrayList<>();
        for (TermNode t : this.term) {
            children.add(t);
        }
        return children;
    }

    // expr = 1 - 2 - 3
    public Object evaluate(VariableTable variableTable) {
        int result = (Integer) this.term.get(this.term.size() - 1)
                .evaluate(variableTable);
        for (int i = this.ops.size() - 1; i >= 0; i--) {
            int leftOperand = (Integer) this.term.get(i).evaluate(variableTable);
            String op = this.ops.get(i);

            switch (op) {
                case "ADD":
                    result = leftOperand + result;
                    break;
                case "SUBTRACT":
                    result = leftOperand - result;
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported operators: " + op);
            }
        }
        return result;
    }

}

package project2.Node;

import java.util.ArrayList;
import java.util.List;

import project2.Core;

public class TermNode extends Node {

    public List<FactorNode> factors = new ArrayList<>();
    public List<Core> ops = new ArrayList<>(); // * or /

    @Override
    public String toPrint(String tap) {
        String result = "";
        for (int i = 0; i < this.factors.size(); i++) {
            result += this.factors.get(i).toPrint("");
            if (i < this.ops.size()) {
                if (this.ops.get(i) == Core.MULTIPLY) {
                    result += " " + "*" + " ";
                } else if (this.ops.get(i) == Core.DIVIDE) {
                    result += " " + "/" + " ";
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
        for (FactorNode f : this.factors) {
            children.add(f);
        }
        return children;
    }

    public Object evaluate(VariableTable variableTable) {
        int result = (Integer) this.factors.get(this.factors.size() - 1)
                .evaluate(variableTable);

        // Traverse operator from right to left
        for (int i = this.ops.size() - 1; i >= 0; i--) {
            int leftOperand = (Integer) this.factors.get(i).evaluate(variableTable);
            Core op = this.ops.get(i);

            switch (op) {
                case MULTIPLY:
                    result = leftOperand * result;
                    break;
                case DIVIDE:
                    result = leftOperand / result;
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported operators: " + op);
            }
        }
        return result;
    }

}

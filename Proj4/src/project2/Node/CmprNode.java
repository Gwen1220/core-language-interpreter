package project2.Node;

import java.util.List;

import project2.Core;

public class CmprNode extends Node {
    public ExprNode firstExprNode;
    public ExprNode secondExprNode;
    public Core op;

    @Override
    public String toPrint(String tap) {
        if (this.op == Core.EQUAL) {
            return this.firstExprNode.toPrint("") + " == "
                    + this.secondExprNode.toPrint("");
        }
        return this.firstExprNode.toPrint("") + " < " + this.secondExprNode.toPrint("");
    }

    @Override
    public void Node() {

    }

    @Override
    public List<Node> get_children() {
        return null;
    }

    public boolean evaluate(VariableTable vars) {
        int left = (int) this.firstExprNode.evaluate(vars);
        int right = (int) this.secondExprNode.evaluate(vars);

        if (this.op == Core.EQUAL) {
            return left == right;
        }
        if (this.op == Core.LESS) {
            return left < right;
        }

        throw new IllegalStateException("Unsupported comparison op: " + this.op);
    }

}

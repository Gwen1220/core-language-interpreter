package project2.Node;

import java.util.List;

import project2.Core;

public class CmprNode extends Node {
    public ExprNode firstExprNode;
    public ExprNode secondExprNode;
    public Core op;

    @Override
    public String toPrint(String tap) {
        if (op == Core.EQUAL) {
            return firstExprNode.toPrint("") + " == " + secondExprNode.toPrint("");
        } 
        return firstExprNode.toPrint("") + " < " + secondExprNode.toPrint("");
    }

    @Override
    public void Node() {

    }

    @Override
    public List<Node> get_children() {
        return null;
    }
}

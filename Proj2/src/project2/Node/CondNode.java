package project2.Node;

import java.util.ArrayList;
import java.util.List;

import project2.Core;

public class CondNode extends Node {


    public CmprNode firstCmprNode = null;
    public CondNode secondCondNode = null;
    public Core op = null;
    public Boolean is_not = false;
    public Boolean is_bracket = false;
    public CondNode contained_cond = null;

    @Override
    public String toPrint(String tap) {
        if (is_not) {
            return "not " + this.contained_cond.toPrint("");
        }
        if (is_bracket) {
            return "[" + this.contained_cond.toPrint("") + "]";
        }
        String result = "";
        result += this.firstCmprNode.toPrint("");
        if (this.op != null) {
            if (this.op == Core.AND) {
                result += " and ";
            } else {
                result += " or ";
            }
            result += this.secondCondNode.toPrint("");
        }
        return result;
    }

    @Override
    public void Node() {

    }

    @Override
    public List<Node> get_children() {
        return null;
    }
}

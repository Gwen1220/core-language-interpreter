package project2.Node;

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
        if (this.is_not) {
            return "not " + this.contained_cond.toPrint("");
        }
        if (this.is_bracket) {
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

    public boolean evaluate(VariableTable vars) {
        // not <cond>
        if (Boolean.TRUE.equals(this.is_not)) {
            if (this.contained_cond == null) {
                throw new IllegalStateException("NOT missing contained condition");
            }
            return !this.contained_cond.evaluate(vars);
        }

        // [ <cond> ]
        if (Boolean.TRUE.equals(this.is_bracket)) {
            if (this.contained_cond == null) {
                throw new IllegalStateException("Bracketed condition missing inner cond");
            }
            return this.contained_cond.evaluate(vars);
        }

        // <cmpr> [and|or <cond>]
        if (this.firstCmprNode == null) {
            throw new IllegalStateException("Condition missing first comparison");
        }
        boolean left = this.firstCmprNode.evaluate(vars);

        if (this.op == null) {
            // only one <cmpr>
            return left;
        } else if (this.op == Core.AND) {
            // If the left is false, it is false.
            return left && (this.secondCondNode != null
                    && this.secondCondNode.evaluate(vars));
        } else if (this.op == Core.OR) {
            // If the left is true, it is true.
            return left || (this.secondCondNode != null
                    && this.secondCondNode.evaluate(vars));
        } else {
            throw new IllegalStateException("Unsupported cond op: " + this.op);
        }
    }

}

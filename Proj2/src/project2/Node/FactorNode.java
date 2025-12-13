package project2.Node;

import java.util.List;
import java.util.Arrays;
public class FactorNode extends Node {

    public String identifier = null;
    public ConstantNode constant = null;
    public ExprNode expr = null;
    public String str = null;

    @Override
    public String toPrint(String tap) {
        if (this.constant != null) {
            return this.constant.toPrint(tap);
        } else if (this.expr != null) {
            return "(" + this.expr.toPrint("") + ")";
        } else if (this.str != null) {
            return this.identifier + "[" + this.str + "]";
        } else if (this.identifier != null) {
            return this.identifier;
        } else {
            return "";
        }
    }

    @Override
    public void Node() {
        if (this.constant != null) {
            this.constant.Node();
        }
    }

    @Override
    public List<Node> get_children() {
        if (this.expr != null) {
            return Arrays.asList(this.expr);
        } 
        return null;
    }
}

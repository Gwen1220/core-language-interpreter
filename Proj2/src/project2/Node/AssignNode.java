package project2.Node;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class AssignNode extends Node {
    public String identifier = null;
    public ExprNode expr = null;
    public String index = null; // an array identifier
    public String newObjectString = null; // new object
    public ExprNode newObjectExpr = null; // expr inside new object

    public String alias = null;

    @Override
    public String toPrint(String tap) {
        if (this.expr != null && this.index == null) {
            return tap + this.identifier + "=" + this.expr.toPrint("") + ";\n";
        } else if (this.index != null) {
            return tap + this.identifier + "[" + this.index + "] = " + this.expr.toPrint("") + ";\n";
        } else if (this.newObjectString != null) {
            if (this.newObjectExpr != null) {
                return tap + this.identifier + "=new object(" + this.newObjectString + ", " + this.newObjectExpr.toPrint("") 
                        + ");\n";
            } else {
                return tap + this.identifier + " : " + this.newObjectString + "();\n";
            }
        } else if (this.alias != null) {
            return tap + this.identifier + " : " + this.alias + ";\n";
        } else {
            return "";
        }
    }

    @Override
    public void Node() {
    }

    @Override
    public List<Node> get_children() {
        if (this.expr != null) {
            return Arrays.asList(this.expr);
        } else if (this.newObjectExpr != null) {
            return Arrays.asList(this.newObjectExpr);
        } else {
            return null;
        }
    }
}

package project2.Node;

import java.util.Arrays;
import java.util.List;

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
            return tap + this.identifier + "[" + this.index + "] = "
                    + this.expr.toPrint("") + ";\n";
        } else if (this.newObjectString != null) {
            if (this.newObjectExpr != null) {
                return tap + this.identifier + "=new object(" + this.newObjectString
                        + ", " + this.newObjectExpr.toPrint("") + ");\n";
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

    public void execute(VariableTable vars) {
        //  id = <expr> ;
        //  Eg. x = 4;
        if (this.expr != null && this.index == null && this.newObjectString == null
                && this.alias == null) {
            int val = (int) this.expr.evaluate(vars);
            vars.setInteger(this.identifier, val);
            return;
        }

        //  id [ string ] = <expr> ;
        if (this.index != null && this.expr != null) {
            int val = (int) this.expr.evaluate(vars);
            vars.setObjectValue(this.identifier, this.index, val);
            return;
        }

        //  id = new object ( string , <expr> ) ;
        //  Eg. x = new object("a", 5)
        if (this.newObjectString != null && this.newObjectExpr != null) {
            int initVal = (int) this.newObjectExpr.evaluate(vars);
            vars.createNewObject(this.identifier, this.newObjectString, initVal);
            return;
        }

        // id1 : id2 ;
        if (this.alias != null) {
            vars.referenceAssign(this.identifier, this.alias);
            return;
        }

    }
}

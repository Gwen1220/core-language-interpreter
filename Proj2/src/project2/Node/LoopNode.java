package project2.Node;

import java.util.List;

public class LoopNode extends Node {
    public String loopId;
    public ExprNode initExpr;
    public CondNode cond;
    public ExprNode updateExpr;
    public StmtSeqNode body;

    @Override
    public String toPrint(String tap) {
        return tap + "for(" + loopId + "=" + initExpr.toPrint("") + "; " + cond.toPrint("") + "; "
                + updateExpr.toPrint("") + ") do \n" + body.toPrint(tap + "\t") + tap + "end\n";
    }

    @Override
    public void Node() {
    }

    @Override
    public List<Node> get_children() {
        return null;
    }
}

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
        return tap + "for(" + this.loopId + "=" + this.initExpr.toPrint("") + "; "
                + this.cond.toPrint("") + "; " + this.updateExpr.toPrint("") + ") do \n"
                + this.body.toPrint(tap + "\t") + tap + "end\n";
    }

    @Override
    public void Node() {
    }

    @Override
    public List<Node> get_children() {
        return null;
    }

    public void execute(VariableTable vars) {
        // Initialize loop variable
        int initVal = (int) this.initExpr.evaluate(vars);
        vars.setInteger(this.loopId, initVal);

        // Enter the loop
        while (this.cond.evaluate(vars)) {
            vars.enterNewScope();
            this.body.execute(vars);
            vars.exitScope();
            // Update the loop variable
            int updatedVal = (int) this.updateExpr.evaluate(vars);
            vars.setInteger(this.loopId, updatedVal);
        }
    }

}

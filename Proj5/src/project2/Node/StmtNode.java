package project2.Node;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

public class StmtNode extends Node {
    public AssignNode assign = null; // <assign>
    public IfNode ifStmt = null; // <if>
    public LoopNode loop = null; // <loop>
    public PrintNode print = null; // <print>
    public ReadNode read = null; // <read>
    public DeclNode decl = null; // <decl>
    public CallNode call = null; // <call>

    @Override
    public String toPrint(String tap) {
        String result = "";
        if (this.assign != null) {
            result += this.assign.toPrint(tap);
        }
        if (this.ifStmt != null) {
            result += this.ifStmt.toPrint(tap);
        }
        if (this.loop != null) {
            result += this.loop.toPrint(tap);
        }
        if (this.print != null) {
            result += this.print.toPrint(tap);
        }
        if (this.read != null) {
            result += this.read.toPrint(tap);
        }
        if (this.decl != null) {
            result += this.decl.toPrint(tap);
        }
        if( this.call != null) {
            result += this.call.toPrint(tap);
        }
        return result;
    }

    @Override
    public void Node() {

    }

    @Override
    public List<Node> get_children() {
        List<Node> children = new ArrayList<>();
        if (this.assign != null) {
            children.add(this.assign);
        }
        if (this.ifStmt != null) {
            children.add(this.ifStmt);
        }
        if (this.loop != null) {
            children.add(this.loop);
        }
        if (this.print != null) {
            children.add(this.print);
        }
        if (this.read != null) {
            children.add(this.read);
        }
        if (this.decl != null) {
            children.add(this.decl);
        }
        if(this.call != null) {
            children.add(this.call);
        }
        return children;
    }

    public void execute(VariableTable variableTable) {
        if (this.assign != null) {
            this.assign.execute(variableTable);
        }
        if (this.ifStmt != null) {
            variableTable.enterNewScope();
            this.ifStmt.execute(variableTable);
            variableTable.exitScope();
        }
        if (this.loop != null) {
            //enter
            this.loop.execute(variableTable);
            //exit
        }
        if (this.print != null) {
            this.print.execute(variableTable);
        }
        if (this.read != null) {
            this.read.execute(variableTable);
        }
        if (this.decl != null) {
            this.decl.execute(variableTable);
        }
        if (this.call != null) {
            this.call.execute(variableTable);
        }
    }

}

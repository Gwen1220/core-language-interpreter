package project2.Node;

import java.util.ArrayList;
import java.util.List;

public class StmtNode extends Node {
    public AssignNode assign = null; // <assign>
    public IfNode ifStmt = null; // <if>
    public LoopNode loop = null; // <loop>
    public PrintNode print = null; // <print>
    public ReadNode read = null; // <read>
    public DeclNode decl = null; // <decl>

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
        return children;
    }
}

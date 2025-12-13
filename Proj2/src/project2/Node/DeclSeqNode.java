package project2.Node;

import java.util.ArrayList;
import java.util.List;

public class DeclSeqNode extends Node {
    public List<DeclNode> declare = new ArrayList<>();

    @Override
    public String toPrint(String tap) {
        String result = "";
        for (DeclNode decl : declare) {
            result += decl.toPrint(tap);
        }
        return result;
    }

    @Override
    public List<Node> get_children() {
        return new ArrayList<Node>(this.declare);
    }
    
    @Override
    public void Node() {
    }
}

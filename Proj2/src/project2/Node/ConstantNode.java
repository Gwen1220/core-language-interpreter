package project2.Node;

import java.util.List;

public class ConstantNode extends Node {
    public Integer value;

    @Override
    public String toPrint(String tap) {
        return value.toString();
    }

    @Override
    public List<Node> get_children() {
        return null;
    }

    @Override
    public void Node() {

    }
    
}

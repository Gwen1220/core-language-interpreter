package project2.Node;

import java.util.List;

public class ReadNode extends Node {
    public String identifier = null;

    @Override
    public String toPrint(String tap) {
        return tap + "read(" + this.identifier + ");\n";
    }

    @Override
    public void Node() {
    }

    @Override
    public List<Node> get_children() {
        return null;
    }
}

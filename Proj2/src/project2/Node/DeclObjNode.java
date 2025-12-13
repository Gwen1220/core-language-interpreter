package project2.Node;

import java.util.List;

public class DeclObjNode extends DeclNode {
    public String id;

    @Override
    public String toPrint(String tap) {
        return tap + "object" + this.id + ";\n";
    }

    @Override
    public List<Node> get_children() {
        return null;
    }

    @Override
    public void Node() {
    }
}

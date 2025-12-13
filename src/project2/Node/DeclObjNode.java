package project2.Node;

import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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

    @Override
    public void execute(VariableTable variableTable) {
        variableTable.declareObject(this.id); 
    }
}

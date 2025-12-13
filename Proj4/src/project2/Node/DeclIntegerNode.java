package project2.Node;

import java.util.List;

public class DeclIntegerNode extends DeclNode {
    public String identifier;

    @Override
    public String toPrint(String tap) {
        return tap + "integer " + this.identifier + ";\n";
    }

    @Override
    public void Node() {
    }

    @Override
    public List<Node> get_children() {
        return null;
    }

    @Override
    public void execute(VariableTable variableTable) {
        variableTable.declareInteger(this.identifier, 0);
    }

}

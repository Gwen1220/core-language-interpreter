package project2.Node;

import java.util.ArrayList;
import java.util.List;

import java.util.Map;
import java.util.Deque;
import java.util.HashMap;
import java.util.ArrayDeque;

public class DeclSeqNode extends DeclNode {
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
        List<Node> children = new ArrayList<>();
        children.addAll(this.declare);
        return children;
    }
    
    @Override
    public void Node() {
    }



    public void execute(VariableTable variableTable) {
        for (DeclNode decl : declare) {
            if (decl instanceof FunctionNode) {
                FunctionNode func = (FunctionNode) decl;
            } else {
                // otherwise, execute the declaration
                decl.execute(variableTable);
            }
        }
    }

}

package project2.Node;

import java.util.ArrayList;
import java.util.List;

public class ExprNode extends Node {
    public List<TermNode> term = new ArrayList<>();
    public List<String> ops = new ArrayList<>(); // + or -

    @Override
    public String toPrint(String tap) {
        String result = "";
        for (int i = 0; i < term.size(); i++) {
            result += term.get(i).toPrint("");
            if (i < ops.size()) {
                if (ops.get(i) == "ADD") {
                    result += " " + "+" + " ";
                } else {
                    result += " " + "-" + " ";
                }
                
            }
        }
        return result;
    }

    @Override
    public void Node() {

    }

    @Override
    public List<Node> get_children() {
        List<Node> children = new ArrayList<>();
        for (TermNode t : this.term) {
            children.add(t);
        }
        return children;
    }
}

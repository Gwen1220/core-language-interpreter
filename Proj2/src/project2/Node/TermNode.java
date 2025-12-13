package project2.Node;

import java.util.ArrayList;
import java.util.List;

import project2.Core;

public class TermNode extends Node {

    public List<FactorNode> factors = new ArrayList<>();
    public List<Core> ops = new ArrayList<>(); // * or /

    @Override
    public String toPrint(String tap) {
        String result = "";
        for (int i = 0; i < factors.size(); i++) {
            result += factors.get(i).toPrint("");
            if (i < ops.size()) {
                if (ops.get(i) == Core.MULTIPLY) {
                    result += " " + "*" + " ";
                } else if (ops.get(i) == Core.DIVIDE) {
                    result += " " + "/" + " ";
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
        for (FactorNode f : this.factors) {
            children.add(f);
        }
        return children;
    }
}

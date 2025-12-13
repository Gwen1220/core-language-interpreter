package project2.Node;

import java.util.ArrayList;
import java.util.List;

public class ParameterNode extends Node {
    public List<String> parameterList = new ArrayList<>();

    @Override
    public String toPrint(String tap) {
        String result = "";
        for (int i = 0; i < this.parameterList.size(); i++) {
            result += this.parameterList.get(i);
            if (i < this.parameterList.size() - 1) {
                result += ", ";
            }
        }
        return result;
    }

    @Override
    public void Node() {
    }

    @Override
    public List<Node> get_children() {
        return null;
    }

    public List<String> execute(VariableTable variableTable) {
       
        return new ArrayList<>(this.parameterList);
    }
}

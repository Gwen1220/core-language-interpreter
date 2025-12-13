package project2.Node;

import java.util.List;

public class CallNode extends Node {
    public String identifier = null;      // function ID
    public ParameterNode parameters = null;  // <parameter>
    

    @Override
    public String toPrint(String tap) {
        String result = tap + "begin " + this.identifier + " (";
        if (this.parameters != null) {
            result += this.parameters.toPrint("");
        }
        result += ")\n";
        return result;
    }

    @Override
    public void Node() {
    }

    @Override
    public List<Node> get_children() {
        if (this.parameters != null) {
            return List.of(this.parameters);
        }
        return null;
    }

public void execute(VariableTable variableTable) {
        // find the function definition
        FunctionNode targetFunc = FunctionMap.getFunction(this.identifier);
        if (targetFunc == null) {
            throw new RuntimeException("Runtime Error: Function '" + this.identifier + "' not found!");
        }

        // enter new scope for the function call
        variableTable.enterNewScope();

        // bind parameters
        List<String> formalParams = targetFunc.parameters.parameterList; // parameters defined in function
        List<String> actualParams = this.parameters.parameterList;       // parameters passed in call
        // declare 
        if (targetFunc.parameters != null) {
            for (String param : targetFunc.parameters.parameterList) {
                variableTable.declareObject(param);
            }
        }

        if (formalParams.size() != actualParams.size()) {
            throw new RuntimeException("Runtime Error: Function '" + this.identifier +
                    "' expected " + formalParams.size() + " parameters but got " + actualParams.size());
        }

        // assign actual parameters to formal parameters
        for (int i = 0; i < formalParams.size(); i++) {
            variableTable.referenceAssignForcall(
                formalParams.get(i), 
                actualParams.get(i)
            );
        }

        // execute the function body
        if (targetFunc.stmtSeq != null) {
            targetFunc.stmtSeq.execute(variableTable);
        }

        // exit scope
        variableTable.exitScope();
    }

}

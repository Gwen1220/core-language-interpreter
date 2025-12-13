package project2.Node;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProcedureNode extends Node { // DFS
    public String identifier = null;
    public DeclSeqNode declSeq = null;
    public StmtSeqNode stmtSeq = null;

    @Override
    public String toPrint(String tap) {
        if (this.declSeq != null) {
            return "procedure " + this.identifier + " is\n"
                    + this.declSeq.toPrint(tap + "\t") + "begin\n"
                    + this.stmtSeq.toPrint(tap + "\t") + "end";
        } else {
            return "procedure " + this.identifier + " is\n" + "begin\n"
                    + this.stmtSeq.toPrint(tap + "\t") + "end";
        }
    }

    @Override
    public List<Node> get_children() {
        List<Node> children = new ArrayList<>();

        if (this.declSeq != null) {
            children.add(this.declSeq);
        }
        if (this.stmtSeq != null) {
            children.add(this.stmtSeq);
        }
        return children;
    }

    @Override
    public void Node() {
    }

    public void execute(String filePath) {
        VariableTable variableTable = new VariableTable();
        java.util.List<Integer> intList = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] numStrs = line.split("\\s+");
                for (String str : numStrs) {
                    if (!str.isEmpty()) {
                        int num = Integer.parseInt(str);
                        intList.add(num);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("read error:" + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("not integer:" + e.getMessage());
        }
        variableTable.loadInput(intList);
        if (this.declSeq != null) {
            this.declSeq.execute(variableTable);
            variableTable.enterNewScope();
        }
        this.stmtSeq.execute(variableTable);
    }

}

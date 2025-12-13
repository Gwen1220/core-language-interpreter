package project2.Node;
import java.util.Map;
import java.util.Deque;
import java.util.HashMap;
import java.util.ArrayDeque;

public abstract class DeclNode extends Node {
    // Subclasses (DeclIntegerNode / DeclObjectNode)
    // will implement this functionality.
    public abstract void execute(VariableTable variableTable);
}

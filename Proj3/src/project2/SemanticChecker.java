package project2;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// <key, value>

import project2.Node.AssignNode;
import project2.Node.DeclIntegerNode;
import project2.Node.DeclObjNode;
import project2.Node.FactorNode;
import project2.Node.IfNode;
import project2.Node.LoopNode;
import project2.Node.Node;
import project2.Node.ProcedureNode;
import project2.Node.ReadNode;
import project2.Node.StmtSeqNode;

public class SemanticChecker {
    private Deque<Map<String, Core>> scopeStack;

    public SemanticChecker() {
        this.scopeStack = new ArrayDeque<>();
        this.scopeStack.push(new HashMap<>());
    }

    // 1. Check the root node (triggering semantic analysis of the entire syntax tree)
    public void check(ProcedureNode root) throws SemanticException {
        this.dfs(root);
    }

    public void dfs(Node node) throws SemanticException {
        List<Node> children = node.get_children();
        if (node instanceof IfNode || node instanceof LoopNode) {
            this.enterScope();
        }
        this.addVariableToCurrentScope_andCheck(node);
        this.check_used_before_declared(node);
        this.check_assign_with_right_type(node);
        if (children == null) {
            return;
        }
        if (children != null) {
            for (Node child : children) {
                if (node instanceof ProcedureNode) {
                    if (child instanceof StmtSeqNode) {
                        this.enterScope();
                    }
                }
                this.dfs(child);
                if (node instanceof ProcedureNode) {
                    if (child instanceof StmtSeqNode) {
                        this.exitScope();
                    }
                }
            }
        }
        if (node instanceof IfNode || node instanceof LoopNode) {
            this.exitScope();
        }
    }

    private void addVariableToCurrentScope_andCheck(Node node) throws SemanticException {
        if (node instanceof DeclIntegerNode) {
            String id = ((DeclIntegerNode) node).identifier;
            Core core = Core.INTEGER;
            if (this.findInCurrentScope(id) != null) {
                throw new SemanticException(
                        "Variable '" + id + "' already declared in this scope");
            }
            this.scopeStack.peek().put(id, core);
        }
        if (node instanceof DeclObjNode) {
            String id = ((DeclObjNode) node).id;
            Core core = Core.OBJECT;
            if (this.findInCurrentScope(id) != null) {
                throw new SemanticException(
                        "Variable '" + id + "' already declared in this scope");
            }
            this.scopeStack.peek().put(id, core);
        }
    }

    private void check_used_before_declared(Node node) throws SemanticException {
        if (node instanceof AssignNode) {
            String id = ((AssignNode) node).identifier;
            if (this.findVariable(id) == null) {
                throw new SemanticException(
                        "Variable '" + id + "' used before declaration");
            }
        }
        if (node instanceof ReadNode) {
            String id = ((ReadNode) node).identifier;
            if (this.findVariable(id) == null) {
                throw new SemanticException(
                        "Variable '" + id + "' used before declaration");
            }
        }
        if (node instanceof LoopNode) {
            String id = ((LoopNode) node).loopId;
            if (this.findVariable(id) == null) {
                throw new SemanticException(
                        "Variable '" + id + "' used before declaration");
            }
        }
        if (node instanceof FactorNode) {
            String id = ((FactorNode) node).identifier;
            if (id != null) {
                if (this.findVariable(id) == null) {
                    throw new SemanticException(
                            "Variable '" + id + "' used before declaration");
                }
            }
        }
    }

    private void check_assign_with_right_type(Node node) throws SemanticException {
        if (node instanceof AssignNode) {
            String id = ((AssignNode) node).identifier;
            if (((AssignNode) node).newObjectString != null) {
                if (this.findVariable(id) == Core.INTEGER) {
                    throw new SemanticException(
                            "Variable '" + id + "' declared with wrong type");
                }
            }
            if (((AssignNode) node).index != null) {
                if (this.findVariable(id) == Core.INTEGER) {
                    throw new SemanticException(
                            "Variable '" + id + "' declared with wrong type");
                }
            }
            if (((AssignNode) node).alias != null) {
                if (this.findVariable(id) == Core.INTEGER) {
                    throw new SemanticException(
                            "Variable '" + id + "' declared with wrong type");
                }
                if (this.findVariable(((AssignNode) node).alias) == Core.INTEGER) {
                    throw new SemanticException("Variable '" + ((AssignNode) node).alias
                            + "' used before declaration");
                }
            }
        }
        if (node instanceof FactorNode) {
            if (((FactorNode) node).str != null) {
                String id = ((FactorNode) node).identifier;
                if (this.findVariable(id) == Core.INTEGER) {
                    throw new SemanticException(
                            "Variable '" + id + "' used with wrong type");
                }
            }
        }
    }

    // Look up variables in the scope stack (trace upward from the current scope)
    private Core findVariable(String id) {
        for (Map<String, Core> scope : this.scopeStack) {
            if (scope.containsKey(id)) {
                return scope.get(id);
            }
        }
        return null;
    }

    private Core findInCurrentScope(String id) {
        Map<String, Core> currentScope = this.scopeStack.peek();
        if (!currentScope.containsKey(id)) {
            return null;
        }
        return currentScope.get(id);
    }

    private void enterScope() {
        this.scopeStack.push(new HashMap<>());
    }

    private void exitScope() {
        this.scopeStack.pop();
    }

}

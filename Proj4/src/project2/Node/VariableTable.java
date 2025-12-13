package project2.Node;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class VariableTable {
    private Deque<Map<String, Object>> scopeStack = new ArrayDeque<>();
    private Deque<Integer> input = new ArrayDeque<>();

    public VariableTable() {
        this.scopeStack.push(new HashMap<>()); // global variable table
    }

    public void enterNewScope() {
        this.scopeStack.push(new HashMap<>());
    }

    public void exitScope() {
        if (this.scopeStack.size() > 1) {
            this.scopeStack.pop();
        } else {
            throw new IllegalStateException("Cannot exit global scope");
        }
    }

    public void declareInteger(String name, Integer value) {
        Map<String, Object> currentScope = this.scopeStack.peek();
        currentScope.put(name, value);
    }

    public void setInteger(String name, Integer value) {
        for (Map<String, Object> scope : this.scopeStack) {
            if (scope.containsKey(name)) {
                Object cur = scope.get(name);
                if (cur instanceof Integer) {
                    scope.put(name, value);
                    return;
                } else if (cur instanceof CoreObject) {
                    if (((CoreObject) cur).objMap == null) {
                        throw new IllegalArgumentException(
                                "Error is assignment to null object variable.");
                    } else {
                        ((CoreObject) cur).defaultValue = value;
                        return;
                    }

                }
            }
        }
        throw new IllegalArgumentException("Variable " + name + " not found");
    }

    public Integer getInteger(String name) {
        for (Map<String, Object> scope : this.scopeStack) {
            if (scope.containsKey(name)) {
                Object value = scope.get(name);
                if (value instanceof Integer) {
                    return (Integer) value;
                } else if (value instanceof CoreObject) {
                    return ((CoreObject) value).defaultValue;
                } else {
                    throw new IllegalArgumentException(
                            "Variable " + name + " is not an integer");
                }
            }
        }
        throw new IllegalArgumentException("Variable " + name + " not found");
    }

    // object x;
    public void declareObject(String name) {
        this.scopeStack.peek().put(name, new CoreObject());
    }

    public void setObjectValue(String name, String key, Integer value) {
        for (Map<String, Object> scope : this.scopeStack) {
            if (scope.containsKey(name)) {
                Object obj = scope.get(name);
                if (obj instanceof CoreObject) {
                    Map<String, Integer> objMap = ((CoreObject) obj).objMap;
                    objMap.put(key, value);
                    return;
                } else {
                    throw new IllegalArgumentException(
                            "Variable " + name + " is not an object");
                }
            }
        }
        throw new IllegalArgumentException("Variable " + name + " not found");
    }

    public Integer getObjectValue(String name, String key) {
        for (Map<String, Object> scope : this.scopeStack) {
            if (scope.containsKey(name)) {
                Object obj = scope.get(name);
                if (obj instanceof CoreObject) {
                    CoreObject coreObj = (CoreObject) obj;
                    if (coreObj.objMap.containsKey(key)) {
                        return coreObj.objMap.get(key);
                    } else {
                        throw new IllegalArgumentException("Key \"" + key
                                + "\" not found in object \"" + name + "\"");
                    }
                } else {
                    throw new IllegalArgumentException(
                            "Variable \"" + name + "\" is not an object");
                }
            }
        }
        throw new IllegalArgumentException("Object \"" + name + "\" not found");
    }

    public void createNewObject(String name, String defaultKey, int initVal) {
        CoreObject coreObj = new CoreObject();
        coreObj.objMap = new HashMap<>();
        coreObj.objMap.put(defaultKey, initVal);
        coreObj.defaultValue = initVal;
        this.scopeStack.peek().put(name, coreObj);
    }

    public void referenceAssign(String left, String right) {
        // right var
        Object rightVal = null;
        for (Map<String, Object> scope : this.scopeStack) {
            if (scope.containsKey(right)) {
                rightVal = scope.get(right);
                break;
            }
        }
        if (rightVal == null) {
            throw new IllegalArgumentException("Variable \"" + right + "\" not found");
        }
        if (!(rightVal instanceof CoreObject)) {
            throw new IllegalArgumentException(
                    "Reference assignment requires object variable: " + right);
        }

        //  left var
        Map<String, Object> leftScope = null;
        Object leftVal = null;
        for (Map<String, Object> scope : this.scopeStack) {
            if (scope.containsKey(left)) {
                leftScope = scope;
                leftVal = scope.get(left);
                break;
            }
        }
        if (leftScope == null) {
            leftScope = this.scopeStack.peek();
        } else if (leftVal instanceof Integer) {
            throw new IllegalArgumentException(
                    "Variable \"" + left + "\" is not an object");
        }

        // Make left and right point to the same Map
        leftScope.put(left, rightVal);
    }

    // Load the integer sequence read from external sources into the system.
    public void loadInput(java.util.List<Integer> data) {
        this.input.clear();
        this.input.addAll(data);
    }

    public int readNextInt() {
        if (this.input.isEmpty()) {
            System.out.println("ERROR: input exhausted");
            System.exit(1);
        }
        return this.input.removeFirst();
    }

    // public void setIntegerValue(String name, int value) {
    //     for (Map<String, Object> scope : this.scopeStack) {
    //         if (scope.containsKey(name)) {
    //             Object cur = scope.get(name);
    //             if (cur instanceof Integer) {
    //                 scope.put(name, value);
    //                 return;
    //             } else {
    //                 throw new IllegalArgumentException(
    //                         "Variable \"" + name + "\" is not an integer");
    //             }
    //         }
    //     }
    //     throw new IllegalArgumentException("Variable \"" + name + "\" not found");
    // }

}

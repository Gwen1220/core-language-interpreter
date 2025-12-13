package project2.Node;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;

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
            Map<String, Object> oldScope = this.scopeStack.pop();
            Set<Object> unique_vals = new java.util.HashSet<>(oldScope.values());
            for (Object val : unique_vals) {
                    if (val instanceof CoreObject) {
                        if (((CoreObject) val).objMap == null) {
                            continue;
                        }
                        Integer isReferenced = 0;
                        for (Map<String, Object> scope : this.scopeStack) {
                            for (Map.Entry<String, Object> entry : scope.entrySet()) {
                                Object v = entry.getValue(); 
                                if (v instanceof CoreObject) {
                                    if (((CoreObject) v) == val) {
                                        isReferenced += 1;
                                    }
                                }
                            }
                        }
                        if (isReferenced == 0) {
                            GcManeger.decrement();
                        }
                    }
                }
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
                        ((CoreObject) cur).objMap.put("'default'", value);
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
                    return ((CoreObject) value).objMap.get("'default'");
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
                    ((CoreObject) obj).objMap.put(key, value);
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
        // modify here
        // CoreObject coreObj = new CoreObject();
        // coreObj.objMap = new HashMap<>();
        // coreObj.objMap.put(defaultKey, initVal);
        // coreObj.defaultValue = initVal;
        // // Gc ++
        // GcManeger.increment();
        // this.scopeStack.peek().put(name, coreObj);
        GcManeger.increment();
        for (Map<String, Object> scope : this.scopeStack) {
            if (scope.containsKey(name)) {
                Object obj = scope.get(name);
                if (obj instanceof CoreObject) {
                    CoreObject new_obj = new CoreObject();
                    new_obj.objMap = new HashMap<>();
                    new_obj.objMap.put(defaultKey, initVal);
                    scope.put(name, new_obj);
                    break;
                }
                
            }
        }

    }
    
    public void referenceAssignForcall(String left, String right) {
        // right var
        Object rightVal = null;
        Iterator<Map<String, Object>> iterator = this.scopeStack.iterator();
        if (iterator.hasNext()) {
            iterator.next(); // skip current scope
        }
        while (iterator.hasNext()) {
            Map<String, Object> scope = iterator.next();
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
    
    
    // a : a
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

        // old reference removed
        // modify here

        if (leftVal instanceof CoreObject) {
            if (((CoreObject) leftVal) == ((CoreObject) rightVal)) {
                // do nothing
            } else {
                Integer isReferenced = 0;
                if (((CoreObject) leftVal).objMap != null) {
                    for (Map<String, Object> scope : this.scopeStack) {
                        for (Map.Entry<String, Object> entry : scope.entrySet()) {
                            String key = entry.getKey();
                            Object val = entry.getValue();
                            if (val instanceof CoreObject) {
                                if (((CoreObject) val) == leftVal) {
                                    isReferenced += 1;
                                }
                            }
                        }
                    }
                    if (isReferenced == 1) {
                        GcManeger.decrement();
                    }
                }
            }

        }

        // Make left and right point to the same Map
        leftScope.put(left, rightVal);

        // new reference created
        // GcManeger.increment();
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


}

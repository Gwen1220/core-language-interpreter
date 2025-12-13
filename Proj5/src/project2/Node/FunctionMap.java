package project2.Node;
import java.util.Map;

public class FunctionMap {
        private static Map<String, FunctionNode> functionMap = new java.util.HashMap<>();

        public static void addFunction(String name, FunctionNode function) {
                if (functionMap.containsKey(name)) {
                        throw new RuntimeException("Function " + name + " is already defined.");
                }
                functionMap.put(name, function);
        }

        public static FunctionNode getFunction(String name) {
                return functionMap.get(name);
        }

    }
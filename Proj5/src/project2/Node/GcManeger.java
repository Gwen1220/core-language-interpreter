package project2.Node;

public class GcManeger {

    private static int count = 0; // reachable object count

    // called when a new object is created
    public static void increment() {
        count++;
        System.out.println("gc:" + count);
    }

    // called when an object loses last reference
    public static void decrement() {
        count--;
        System.out.println("gc:" + count);
    }

    
    public static int get() {
        return count;
    }

}

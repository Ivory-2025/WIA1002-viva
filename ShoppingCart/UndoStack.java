package ShoppingCart;
public class UndoStack {
    private static class StackNode {
        int productId;
        int quantity;
        StackNode next;

        StackNode (int productId, int quantity){
            this.productId = productId;
            this.quantity = quantity;
            this.next = null;
        }
    }

    private StackNode top;
    private int size;

    public UndoStack(){
        this.top = null;
        this.size = 0;
    }

    // push a cart action onto stack
    public void push (int productId, int quantity){
        StackNode newNode = new StackNode (productId, quantity);
        newNode.next = top;
        top = newNode;
        size++;
    }

    // return the most recent action (pop)
    public int [] pop(){
        if (top == null){
            return null;
        }
        int [] action = {top.productId, top.quantity};
        top = top.next;
        size--;
        return action;
    }

    // peek but no remove
    public int [] peek () {
        if (top == null){
            return null;
        }
        return new int[] {top.productId, top.quantity};
    }

    public void clear(){
        top = null;
        size = 0;
    }

    public boolean isEmpty(){
        return top == null;
    }

    public int getSize(){
        return size;
    }
}
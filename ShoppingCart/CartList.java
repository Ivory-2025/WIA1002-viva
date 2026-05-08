package ShoppingCart;

import InventoryManagement.InventoryManager;
import InventoryManagement.Product;
public class CartList {
    private CartNode head;
    private int size;

    public CartList(){
        this.head=null;
        this.size=0;
    }

    public void addItem(Product p, int qty){
        CartNode existing=findItem(p.getId());
        if(existing!=null){
            existing.quantity+=qty;
            return;
        }else{
            CartNode newNode=new CartNode(p,qty);
            if(head==null){
                head=newNode;
            }else{
                CartNode current=head;
                while(current.next!=null){
                    current=current.next;
                }
                current.next=newNode;
            }
            size++;
        }
    }

    public CartNode removeItem(int productId){
        if(head==null)
            return null;
        if(head.product.getId()==productId){
            CartNode temp=head;
            head=head.next;
            size--;
            return temp;
        }
        CartNode current=head;
        while(current.next!=null){
            if(current.next.product.getId()==productId){
                CartNode temp = current.next;
                current.next = current.next.next; // Bypass the node to remove
                size--;
                return temp;
            }
            current=current.next;
        }
        return null;
    }

    // Update in CartList.java
public void updateQuantity(int productId, int newQty, UndoStack stack) {
    CartNode current = head;
    while (current != null) {
        if (current.product.getId() == productId) {
            // 1. Store the original quantity for undo
            int oldQty = current.quantity;

            // 2. Update the current quantity
            current.quantity = newQty;

            // 3. Push to undo stack 
            // For updates: push the DIFFERENCE added
            // Positive if increasing, negative if decreasing
            int diff = newQty - oldQty;
            stack.push(productId, diff); 
            
            return; // Exit once found and updated
        }
        current = current.next;
    }
}

    public CartNode findItem(int productId){
        CartNode current=head;
        while(current!=null){
            if(current.product.getId()==productId){
                return current;
            }
            current=current.next;
        }
        return null;
    }

    public void displayCart(){
        CartNode current=head;
        if(current==null){
            System.out.println("Cart is empty");
            return;
        }
        while(current!=null){
            System.out.println("Product: "+current.product.getId()+"("+current.product.getName()+")");
            System.out.println("Quantity: "+current.quantity);
            System.out.printf("Unit Price: RM%.2f%n", current.product.getPrice());
            double subtotals=(current.product.getPrice())*(double)(current.quantity);
            System.out.printf("Product subtotals: RM%.2f%n", subtotals);
            current=current.next;
        }
    }

    public double calculateTotal(){
        double total=0;
        CartNode current=head;
        while(current!=null){
            total+=(current.product.getPrice())*(double)(current.quantity);
            current=current.next;
        }
        return total;
    }

    public void clear(){
        head=null;
        size=0;
    }

    public CartNode undo(){
        if(head==null){
            System.out.println("Cart is empty, nothing to remove");
            return null;
        }
        //LIFO
        CartNode removedNode;
        if(head.next==null){
            removedNode=head;
            head=null;
        }else{
            CartNode current=head;
            while(current.next.next!=null){
                current=current.next;
            }
            removedNode=current.next;
            current.next=null;
        }
        System.out.println("Removed product: "+removedNode.product.getName()+", Quantity: "+removedNode.quantity);
        size--;
        return removedNode;
    }

    public int getSize(){
        return this.size;
    }

    public boolean isEmpty(){
        return getSize()==0;
    }

    public CartNode getHead(){
        return head;
    }

}

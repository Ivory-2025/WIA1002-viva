package ShoppingCart;

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
        while(current!=null){
            if(current.next.product.getId()==productId){
                current.next=current.next.next;
                size--;
                return current;
            }
            current=current.next;
        }
        return null;
    }

    public void updateQuantity(int productId, int newQty){
        CartNode current=head;
        while(current!=null){
            if(current.product.getId()==productId){
                current.quantity=newQty;
            }
            current=current.next;
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

    public Product undo(){
        if(head==null){
            System.out.println("Cart is empty, nothing to remove");
            return null;
        }
        //LIFO
        Product removed;
        int removedQty;
        if(head.next==null){
            removed=head.product;
            removedQty=head.quantity;
            head=null;
        }else{
            CartNode current=head;
            while(current.next.next!=null){
                current=current.next;
            }
            removed=current.next.product;
            removedQty=current.next.quantity;
            current.next=null;
        }
        System.out.println("Removed product: "+removed.getName()+", Quantity: "+removedQty);
        size--;
        return removed;
    }

    public int getSize(){
        return this.size;
    }

    public boolean isEmpty(){
        return getSize()==0;
    }

}

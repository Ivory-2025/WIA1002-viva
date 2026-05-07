package ShoppingCart;

import InventoryManagement.Product;
public class CartNode {
    public Product product;
    public int quantity;
    public CartNode next;

    public CartNode(Product product, int quantity){
        this.product=product;
        this.quantity=quantity;
        this.next=null;
    }

}

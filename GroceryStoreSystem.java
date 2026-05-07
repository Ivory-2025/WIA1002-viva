import InventoryManagement.*;
import ShoppingCart.*;
import java.util.*;
public class GroceryStoreSystem {
    private static CartList cart=new CartList();
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        InventoryManager inventory=new InventoryManager();
        UndoStack undoStack = new UndoStack();

        try{
            inventory.loadFromFile("inventory.txt");
        }catch(Exception e){
            System.err.println("Error: Could not load inventory file. Starting with empty inventory.");
        }
        while (true) {
            try{
                System.out.println("\n--- Main Menu (Enter number to proceed) ---");
                System.out.println("1: Display Inventory");
                System.out.println("2: Search Product (ID/Name)");
                System.out.println("3: Add New Product to Store");
                System.out.println("4: Remove Product from Store");
                System.out.println("5: Update Inventory Stock");
                System.out.println("6: Add Item to Cart");
                System.out.println("7: Remove Item from Cart");
                System.out.println("8: View Cart");
                System.out.println("9: Update Item Quantity in Cart");
                System.out.println("10: Undo Last Addition / Clear Cart");
                System.out.println("11: Generate Bill / Checkout");
                System.out.println("12: Save and Exit");
                System.out.print("Choice: ");
                if (!sc.hasNextInt()) {
                    System.out.println("Invalid input! Please enter a number between 1 and 12.");
                    sc.nextLine(); 
                    continue;
                }
                int input=sc.nextInt();
                sc.nextLine();
                switch(input){
                    case 1:
                        System.out.println("Display All Products");
                        inventory.displayAll();
                        break;
                    case 2:
                        System.out.println("Search product by Id(1) or name (2): ");
                        int value=sc.nextInt();
                        sc.nextLine();
                        if(value==1){
                            System.out.println("Enter product id: ");
                            int id=sc.nextInt();
                            sc.nextLine();
                            Product p=inventory.searchById(id);
                            if (p==null) 
                                throw new NoSuchElementException("Product ID " + id + " not found.");
                            System.out.println("Found: " + p);
                        }else if(value==2){
                            System.out.println("Enter product name: ");
                            String name=sc.nextLine();
                            ArrayList<Product> p=inventory.searchByName(name);
                            if(p==null||p.isEmpty()) 
                                throw new NoSuchElementException("Product name " + name + " not found.");
                            System.out.println("Found: " + p);
                        }
                        break;
                    case 3:
                        boolean productAdded = false;
                        while (!productAdded) {
                        try {
                            System.out.println("Enter new product id: ");
                            int id = Integer.parseInt(sc.nextLine());
                            System.out.println("Enter new product name: ");
                            String name = sc.nextLine();
                            System.out.println("Enter new product price: ");
                            double price = Double.parseDouble(sc.nextLine());
                            System.out.println("Enter new product stock: ");
                            int stock = Integer.parseInt(sc.nextLine());
                            Product p = new Product(id, name, price, stock);
                            inventory.addProduct(p);
                            productAdded = true;
                        }catch (NumberFormatException e) {
                            System.out.println("Error: Please enter valid numbers for ID, Price, and Stock.");
                        } }
                        break;
                    case 4:
                        System.out.println("Enter product id to remove: ");
                        int removedId=sc.nextInt();
                        sc.nextLine();
                        inventory.removeProduct(removedId);
                        break;
                    case 5:
                        System.out.println("Enter product id to update its stock: ");
                        int stockId=sc.nextInt();
                        sc.nextLine();
                        System.out.println("Update quantity to update: ");
                        int quantity=sc.nextInt();
                        inventory.updateStock(stockId, quantity);
                        break;
                    case 6:
                        System.out.println("Enter product id: ");
                        int proId=sc.nextInt();
                        sc.nextLine();
                        System.out.println("Enter product quantity: ");
                        int proQty=sc.nextInt();
                        sc.nextLine();
                        Product pCart=inventory.getProductById(proId);
                        if(pCart==null){
                            throw new NoSuchElementException("Product ID " + proId + " not found.");
                        }else if(!inventory.isAvailable(proId, proQty)){
                            System.out.println("Error: Insufficient stock. Available: " + pCart.getStock());
                        }else{
                            pCart.setStock(pCart.getStock()-proQty);
                            cart.addItem(pCart,proQty);
                            undoStack.push(proId, proQty);
                            System.out.println("Item added successfully.");
                        }
                        break;
                    case 7:
                        System.out.println("Enter product id to remove: ");
                        int remId=sc.nextInt();
                        sc.nextLine();
                        CartNode productToRemove=cart.findItem(remId);
                        if(productToRemove!=null){
                            productToRemove.product.setStock(productToRemove.product.getStock()+productToRemove.quantity);
                            cart.removeItem(remId);
                            undoStack.push(remId, -productToRemove.quantity);
                        }else{
                            System.out.println("Product not found in cart");
                        }
                        break;
                    case 8:
                        System.out.println("Your Cart ("+cart.getSize()+" items):");
                        cart.displayCart();
                        break;
                    case 9:
                        System.out.println("Enter product id to update quantity: ");
                        int updateId=sc.nextInt();
                        sc.nextLine();
                        CartNode itemToUpdate = cart.findItem(updateId);
                        if(itemToUpdate != null){
                            System.out.println("Enter new quantity: ");
                            int newQty=sc.nextInt();
                            sc.nextLine();
                            if(newQty <= 0){
                                System.out.println("Quantity must be at least 1.");
                            } else {
                                int oldQty = itemToUpdate.quantity;
                                if(newQty > oldQty){
                                    int diff = newQty - oldQty;
                                    if(inventory.isAvailable(updateId, diff)){
                                        itemToUpdate.product.setStock(itemToUpdate.product.getStock() - diff);
                                        itemToUpdate.quantity = newQty;
                                        System.out.println("Quantity updated successfully");
                                    } else {
                                        System.out.println("Error: Insufficient stock. Available: " + itemToUpdate.product.getStock());
                                    }
                                } else if(newQty < oldQty){
                                    int diff = oldQty - newQty;
                                    itemToUpdate.product.setStock(itemToUpdate.product.getStock() + diff);
                                    itemToUpdate.quantity = newQty;
                                    System.out.println("Quantity updated successfully");
                                } else {
                                    System.out.println("Quantity is unchanged.");
                                }
                            }
                        }else{
                            System.out.println("Product not found in cart");
                        }
                        break;
                    case 10:
                        System.out.println("Undo Last Addition(1) or Clear Cart (2): ");
                        int choice=sc.nextInt();
                        sc.nextLine();
                        if(choice==1){
                            int [] action = undoStack.pop();
                            if(action != null){
                                cart.removeItem(action[0]);
                                if (action != null) {
                                    cart.removeItem(action[0]);
                                    Product prod = inventory.getProductById(action[0]);
                                    if (prod != null) {
                                        inventory.updateStock(action[0], prod.getStock() + action[1]);
                                    }
                                }
                            }
                        }else if(choice==2){
                            CartNode current = cart.gethead();
                            while(current != null){
                                inventory.updateStock(current.product.getId(), current.product.getStock() + current.quantity);
                                current = current.next;
                            }
                            cart.clear();
                            undoStack.clear();
                            System.out.println("Cart cleared successfully");
                        }
                        break;
                    case 11:
                        if(cart.isEmpty()){
                            System.out.println("Cart is empty, cannot checkout");
                            break;
                        }else{
                           System.out.println("\n--- Receipt ---");
                           cart.displayCart();
                           System.out.printf("TOTAL: RM%.2f\n", cart.calculateTotal());
                           clearAndRestoreStock();
                           undoStack.clear();
                           System.out.print("Save inventory? (y/n): ");
                           if (sc.nextLine().equalsIgnoreCase("y")){ 
                                inventory.saveToFile("inventory.txt");
                                 System.out.println("Inventory saved.");
                            }
                        }
                        break;
                    case 12:
                        inventory.saveToFile("inventory.txt");
                        System.out.println("Inventory saved. Exiting...");
                        sc.close();
                        return;
                    default:

                    System.out.println("Invalid choice, please try again.");
                    }   
            }catch(NumberFormatException e){
                System.out.println("Invalid input format! Please enter numeric values where required.");
            }catch(NoSuchElementException e){
                System.out.println(e.getMessage());
            }catch(Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                sc.nextLine(); 
            }
        }
    }

    public static void clearAndRestoreStock(){
            CartNode current=cart.gethead();
            while(current!=null){
                current.product.setStock(current.product.getStock()+current.quantity);
                current=current.next;
            }
            cart.clear();
    }
}

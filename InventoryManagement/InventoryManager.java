package InventoryManagement;

import java.io.*;
import java.util.*;
public class InventoryManager {
    private ArrayList<Product> products=new ArrayList<>();

    public void loadFromFile(String filename){
        try(Scanner sc=new Scanner(new File(filename))){
            while(sc.hasNextLine()){
                String line=sc.nextLine();
                String[]parts=line.split(",");
                int id=Integer.parseInt(parts[0]);
                String name=parts[1];
                double price=Double.parseDouble(parts[2]);
                int stock=Integer.parseInt(parts[3]);
                products.add(new Product(id, name, price, stock));
            }
        }catch(FileNotFoundException e){
                System.out.println(e.getMessage());
        }
    }

    public void saveToFile(String filename){
        try(PrintWriter pw=new PrintWriter(new File(filename))){
            for(Product p:products){
                pw.println(p.getId()+","+p.getName()+","+p.getPrice()+","+p.getStock());
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
    }
    }

    public void addProduct(Product p){
        for(Product prod:products){
            if(p.getId()==prod.getId()){
                System.out.println("Duplicate id detected");
                return;
            }
        }
        products.add(p);
        System.out.println("Product added successfully");
    }

    public void removeProduct(int id){
        for(Product prod:products){
            if(prod.getId()==id){
                products.remove(prod);
                System.out.println("Product removed successfully");
                return;
            }
        }
        System.out.println("Product doesn't exist");
    }

    public Product searchById(int id){
        for(Product prod:products){
            if(prod.getId()==id){
                return prod;
            }
        }return null;
    }

    public ArrayList<Product> searchByName(String name){
        ArrayList<Product> result=new ArrayList<>();
        String searchItem=name.toLowerCase();
        for(Product prod:products){
            if(prod.getName().toLowerCase().contains(searchItem)){
                result.add(prod);
            }
        }return result;
    }

    public int updateStock(int id, int newStock){
        for(Product prod:products){
            if(prod.getId()==id){
                prod.setStock(newStock);
                System.out.println("Stock updated successfully");
                return newStock;
            }
        }
        System.out.println("Product not found");
        return -1;
    }

    public void displayAll() {
        System.out.println("\n-------------------------------------------------------------------------");
        System.out.printf("| %-10s | %-20s | %-12s | %-10s |\n", "ID", "Name", "Price", "Stock");
        System.out.println("-------------------------------------------------------------------------");
    
        for(Product p:products){
        // Formatted rows matching the header widths
            System.out.printf("| %-10d | %-20s | $%-11.2f | %-10d |\n", 
                          p.getId(), p.getName(), p.getPrice(), p.getStock());
    }
    
    System.out.println("-------------------------------------------------------------------------");
}

    public Product getProductById(int id){
        for(Product prod:products){
            if(prod.getId()==id){
                return prod;
            }
        }
        return null;
    }

    public boolean isAvailable(int id, int requestedQty){
        for(Product prod:products){
            if(prod.getId()==id){
                if(prod.getStock()>=requestedQty){
                    return true;
                }
            }
        }
        return false;
    }
}


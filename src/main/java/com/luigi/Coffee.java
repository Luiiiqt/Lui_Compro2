package com.luigi;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Coffee {
    private String name;
    private String type;
    private String size;
    private double price;
    private String roastLevel;
    private String origin;
    private boolean isDecaf;
    private int stock;
    private List<String> flavorNotes;
    private String brewMethod;

    // Constructor
    public Coffee(String name, String type, String size, double price, String roastLevel, String origin, boolean isDecaf, int stock, String[] flavorNotes, String brewMethod) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.price = price;
        this.roastLevel = roastLevel;
        this.origin = origin;
        this.isDecaf = isDecaf;
        this.stock = Math.max(stock, 0); // Ensure stock is non-negative
        this.flavorNotes = new ArrayList<>(Arrays.asList(flavorNotes));
        this.brewMethod = brewMethod;
    }

    // Encapsulation - Getters
    public String getName() { return name; }
    public String getType() { return type; }
    public String getSize() { return size; }
    public double getPrice() { return price; }
    public String getRoastLevel() { return roastLevel; }
    public String getOrigin() { return origin; }
    public boolean isDecaf() { return isDecaf; }
    public int getStock() { return stock; }
    public List<String> getFlavorNotes() { return new ArrayList<>(flavorNotes); }
    public String getBrewMethod() { return brewMethod; }

    // Encapsulation - Setters
    public void setSize(String size) { this.size = size; }
    public void setRoastLevel(String roastLevel) { this.roastLevel = roastLevel; }
    public void setDecaf(boolean isDecaf) { this.isDecaf = isDecaf; }

    // Calculate adjusted price based on size
    public double calculatePrice() {
        switch (size) {
            case "Small": return price * 0.9;
            case "Medium": return price;
            case "Large": return price * 1.2;
            default: return price;
        }
    }

    // Check if coffee is in stock
    public boolean isAvailable() {
        return stock > 0;
    }

    // Add a new flavor note
    public void addFlavor(String note) {
        if (!flavorNotes.contains(note)) {
            flavorNotes.add(note);
        }
    }

    // Update stock, preventing negative values
    public void updateStock(int quantity) {
        if (stock + quantity >= 0) {
            stock += quantity;
        } else {
            System.out.println("Stock cannot be negative.");
        }
    }

    // Apply discount, ensuring price does not go negative
    public void applyDiscount(double percentage) {
        if (percentage > 0 && percentage < 100) {
            price *= (1 - (percentage / 100));
        }
    }

    // Describe the coffee
    public void describe() {
        System.out.println("A " + roastLevel.toLowerCase() + " roast " + name + " from " + origin +
                " with " + String.join(", ", flavorNotes) + " notes.");
    }
}
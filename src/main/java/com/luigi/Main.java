package com.luigi;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Creating Coffee Objects
        Coffee espresso = new Coffee("Espresso", "Black", "Medium", 50.0, "Dark", "Colombia", false, 10, new String[]{"Rich", "Bold"}, "Espresso Machine");
        Coffee latte = new Coffee("Latte", "Milk-based", "Large", 70.0, "Medium", "Ethiopia", false, 5, new String[]{"Creamy", "Smooth"}, "Espresso with Steamed Milk");

        // Calling Methods on the Objects
        System.out.println("---- Coffee Details ----");
        espresso.describe();
        latte.describe();

        System.out.println("\n---- Checking Stock ----");
        System.out.println(espresso.getName() + " in stock? " + espresso.isAvailable());
        System.out.println(latte.getName() + " in stock? " + latte.isAvailable());

        System.out.println("\n---- Updating Stock ----");
        espresso.updateStock(-3);
        latte.updateStock(5);
        System.out.println(espresso.getName() + " new stock: " + espresso.getStock());
        System.out.println(latte.getName() + " new stock: " + latte.getStock());

        System.out.println("\n---- Price Adjustments ----");
        System.out.println(espresso.getName() + " original price: " + espresso.getPrice() + " PHP");
        System.out.println(latte.getName() + " original price: " + latte.getPrice() + " PHP");

        espresso.applyDiscount(10);
        latte.applyDiscount(15);
        System.out.println(espresso.getName() + " discounted price: " + espresso.getPrice() + " PHP");
        System.out.println(latte.getName() + " discounted price: " + latte.getPrice() + " PHP");

        System.out.println("\n---- Adding Flavor Notes ----");
        espresso.addFlavor("Chocolatey");
        latte.addFlavor("Vanilla");
        espresso.describe();
        latte.describe();

        Scanner input = new Scanner(System.in);
        System.out.println("Welcome to Luigi's Coffee Shop!");

        String[][] coffeeMenu = {
                {"Espresso", "50.0"},
                {"Latte", "70.0"},
                {"Cappuccino", "65.0"},
                {"Mocha", "80.0"}
        };

        int[] quantities = new int[coffeeMenu.length];

        while (true) {
            System.out.println("\n---- Coffee Menu ----");
            for (int i = 0; i < coffeeMenu.length; i++) {
                System.out.printf("%d. %-10s - %s PHP\n", (i + 1), coffeeMenu[i][0], coffeeMenu[i][1]);
            }
            System.out.println("0. Finish Order");

            System.out.print("Choose your coffee (1-4, or 0 to finish): ");
            int coffeeChoice = input.nextInt();
            input.nextLine(); // Consume newline

            if (coffeeChoice == 0) {
                break;
            } else if (coffeeChoice < 1 || coffeeChoice > coffeeMenu.length) {
                System.out.println("Invalid choice. Please choose between 1 and " + coffeeMenu.length + ", or 0 to finish.");
                continue;
            }

            System.out.print("Enter quantity: ");
            int quantity = input.nextInt();
            input.nextLine(); // Consume newline
            quantities[coffeeChoice - 1] += quantity;

            String name = coffeeMenu[coffeeChoice - 1][0];
            double basePrice = Double.parseDouble(coffeeMenu[coffeeChoice - 1][1]);

            // User selects coffee size
            System.out.print("Choose a size (Small, Medium, Large): ");
            String size = input.nextLine();

            // User selects roast level
            System.out.print("Choose roast level (Light, Medium, Dark): ");
            String roastLevel = input.nextLine();

            // User adds a flavor note (optional)
            System.out.print("Would you like to add a flavor note? (yes/no): ");
            String addFlavor = input.nextLine();
            String[] flavorNotes = new String[0];
            if (addFlavor.equalsIgnoreCase("yes")) {
                System.out.print("Enter flavor note (Chocolate, Citrus, Nutty): ");
                flavorNotes = new String[]{input.nextLine()};
            }

            Coffee selectedCoffee = new Coffee(name, "Arabica", size, basePrice, roastLevel, "Colombia", false, 10, flavorNotes, "Espresso Machine");
            double updatedPrice = selectedCoffee.calculatePrice();

            // Display final coffee details
            System.out.println("\n---- Your Coffee Details ----");
            selectedCoffee.describe();
            System.out.println("Size: " + selectedCoffee.getSize() + ", Price: " + updatedPrice + " PHP, Roast Level: " + selectedCoffee.getRoastLevel());
            System.out.println("Brew Method: " + selectedCoffee.getBrewMethod() + ", Stock Available: " + selectedCoffee.getStock());
        }

        String receiptHeader = "\n---- Coffee Order Receipt ----\n";
        String receiptBody = "";
        String receiptFooter = "------------------------------\n";

        double totalAmount = 0.0;

        for (int i = 0; i < coffeeMenu.length; i++) {
            if (quantities[i] > 0) {
                double price = Double.parseDouble(coffeeMenu[i][1]);
                double subtotal = quantities[i] * price;
                totalAmount += subtotal;
                receiptBody += String.format("%-20s x %2d @ %6.2f PHP = %8.2f PHP\n",
                        coffeeMenu[i][0], quantities[i], price, subtotal);

                double vat = totalAmount * 0.12;
                double grandTotal = totalAmount + vat;

                String receiptSummary = "";
                receiptSummary += String.format("Subtotal: %30.2f PHP\n", totalAmount);
                receiptSummary += String.format("VAT (12%%): %28.2f PHP\n", vat);
                receiptSummary += String.format("Grand Total: %27.2f PHP\n", grandTotal);

                String completeReceipt = receiptHeader + receiptBody + receiptFooter + receiptSummary + receiptFooter;

                System.out.println(completeReceipt);

                try (FileWriter writer = new FileWriter("CoffeeReceipt.txt")) {
                    writer.write(completeReceipt);
                    System.out.println("Receipt saved to CoffeeReceipt.txt");
                } catch (IOException e) {
                    System.out.println("An error occurred while saving the receipt.");
                    e.printStackTrace();
                }

                input.close();
            }
        }
    }
}
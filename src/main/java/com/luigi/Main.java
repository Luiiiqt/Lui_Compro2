package com.luigi;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        String[][] coffeeMenu = {
                {"Espresso", "50.0"},
                {"Latte", "70.0"},
                {"Cappuccino", "65.0"},
                {"Mocha", "80.0"}
        };

        int[] quantities = new int[coffeeMenu.length];

        while (true) {
            System.out.println("---- Coffee Menu ----");
            for (int i = 0; i < coffeeMenu.length; i++) {
                System.out.printf("%d. %-10s - %s PHP\n", i + 1, coffeeMenu[i][0], coffeeMenu[i][1]);
            }
            System.out.println("0. Finish Order");

            System.out.print("Choose your coffee (1-4, or 0 to finish): ");
            int choose = input.nextInt();

            if (choose == 0) {
                break;
            } else if (choose < 1 || choose > coffeeMenu.length) {
                System.out.println("Invalid choice. Please choose between 1 and 4, or 0 to finish.");
                continue;
            }

            System.out.print("Enter quantity: ");
            int quantity = input.nextInt();
            quantities[choose - 1] += quantity;
            System.out.println(quantity + " x " + coffeeMenu[choose - 1][0] + " added to your order.\n");
        }

        String receiptHeader = "\n----Coffee Order Receipt----\n";
        String receiptBody = "";
        String receiptFooter = "------------------------------\n";

        double totalAmount = 0.0;
        for (int i = 0; i < coffeeMenu.length; i++) {
            if (quantities[i] > 0) {
                double price = Double.parseDouble(coffeeMenu[i][1]);  // Convert price from String to double
                double subtotal = quantities[i] * price;
                totalAmount += subtotal;
                receiptBody += String.format("%-20s x %2d @ %6.2f PHP = %8.2f PHP\n", coffeeMenu[i][0], quantities[i], price, subtotal);
            }
        }

        double vat = totalAmount * 0.12;
        double grandTotal = totalAmount + vat;

        String receiptSummary = String.format("Subtotal: %30.2f PHP\n", totalAmount) +
                String.format("VAT (12%%): %28.2f PHP\n", vat) +
                String.format("Grand Total: %27.2f PHP\n", grandTotal);

        String completeReceipt = receiptHeader + receiptBody + receiptFooter + receiptSummary + receiptFooter;
        System.out.println(completeReceipt);

        System.out.print("Enter payment: ");
        double payment = input.nextDouble();
        double change = payment - grandTotal;

        if (change >= 0) {
            System.out.printf("Change: %.2f PHP\n", change);
        } else {
            System.out.printf("Insufficient payment. You need %.2f PHP more.\n", Math.abs(change));
        }

        try (FileWriter writer = new FileWriter("CoffeeReceipt.txt")) {
            writer.write(completeReceipt);
            System.out.println("Receipt saved to CoffeeReceipt.txt");
        } catch (IOException e) {
            System.out.println("An error occurred while saving the receipt.");
            e.printStackTrace();
        }
    }
}

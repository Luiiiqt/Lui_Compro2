package com.luigi.demo;


import org.springframework.stereotype.Service;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CoffeeService {
    private List<Coffee> coffees;
    private final String FILE_NAME = "coffees.csv";

    public CoffeeService() {
        this.coffees = new ArrayList<>();
        readFromDisk(); // Load coffees from file on startup
    }


    // Return the list of all coffees
    public List<Coffee> getCoffees() {
        return coffees;
    }


    // Delete a coffee by its ID
    public void deleteCoffee(int id) {
        coffees.removeIf(c -> c.getId() == id);
        writeToDisk(); // Save after deletion
    }


    // Search coffees by keyword (in name, origin, or brew method)
    public List<Coffee> searchCoffee(String keyword) {
        if (keyword.trim().isEmpty()) {
            return coffees;
        }
        return coffees.stream().filter(c ->
                c.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                        c.getOrigin().toLowerCase().contains(keyword.toLowerCase()) ||
                        c.getBrewMethod().toLowerCase().contains(keyword.toLowerCase())
        ).collect(Collectors.toList());
    }


    // Get a specific coffee by ID
    public Coffee getCoffee(int id) {
        return coffees.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }


    // Update a coffee entry by its ID
    public void updateCoffee(int id, Coffee updatedCoffee) {
        for (int i = 0; i < coffees.size(); i++) {
            if (coffees.get(i).getId() == id) {
                coffees.set(i, updatedCoffee);
                writeToDisk(); // Save after update
                return;
            }
        }
    }


    // Add a new coffee entry
    public void addCoffee(Coffee coffee) {
        coffees.add(coffee);
        writeToDisk(); // Save after adding
    }


    // Get the ID of the last coffee in the list
    public int getLastId() {
        if (coffees.isEmpty()) {
            return 0;
        }
        return coffees.get(coffees.size() - 1).getId();
    }


    // Write the coffee list to the CSV file
    public void writeToDisk() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Coffee c : coffees) {
                bw.write(c.getId() + ","
                        + c.getName() + ","
                        + c.getType() + ","
                        + c.getSize() + ","
                        + c.getPrice() + ","
                        + c.getRoastLevel() + ","
                        + c.getOrigin() + ","
                        + c.isDecaf() + ","
                        + c.getStock() + ","
                        + String.join(";", c.getFlavorNotes()) + ","
                        + c.getBrewMethod());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();  // Log error if file writing fails
        }
    }


    // Read the coffee list from the CSV file
    public void readFromDisk() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("File not found, starting fresh.");
            return;
        }


        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (data.length < 11) {
                    System.out.println("Skipping malformed line: " + line);
                    continue;
                }


                Coffee c = new Coffee();
                c.setId(Integer.parseInt(data[0]));
                c.setName(data[1]);
                c.setType(data[2]);
                c.setSize(data[3]);
                c.setPrice(Double.parseDouble(data[4]));
                c.setRoastLevel(data[5]);
                c.setOrigin(data[6]);
                c.setDecaf(Boolean.parseBoolean(data[7]));
                c.setStock(Integer.parseInt(data[8]));
                c.setFlavorNotes(data[9].isEmpty() ? new ArrayList<>() : List.of(data[9].split(";")));
                c.setBrewMethod(data[10]);

                coffees.add(c);
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}

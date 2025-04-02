package com.luigi.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class HomeController {
    CoffeeService coffeeService;

    // Constructor injection for CoffeeService
    public HomeController(CoffeeService coffeeService) {
        this.coffeeService = coffeeService;
    }

    // Get list of coffees, optionally filtered by search term
    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "") String search, Model model) {
        model.addAttribute("coffees", coffeeService.searchCoffee(search));
        return "index";
    }

    // Delete a coffee by ID
    @GetMapping("/delete")
    public String delete(@RequestParam int id) {
        coffeeService.deleteCoffee(id);
        return "redirect:/";  // Redirect to home page after deletion
    }

    // Display the form for creating a new coffee
    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("coffee", new Coffee());  // Add an empty coffee to the model for form binding
        return "new";  // Return the view for creating a coffee
    }

    // Save a new coffee
    @PostMapping("/save")
    public String store(
            @RequestParam String name,
            @RequestParam String type,
            @RequestParam String size,
            @RequestParam double price,
            @RequestParam String roastLevel,
            @RequestParam String origin,
            @RequestParam(defaultValue = "false") boolean isDecaf,
            @RequestParam int stock,
            @RequestParam String flavorNotes,  // flavorNotes as a string input
            @RequestParam String brewMethod
    ) {
        // Split flavorNotes by semicolon (assuming the user enters flavors like "chocolate; nutty")
        List<String> flavorList = new ArrayList<>(Arrays.asList(flavorNotes.split(";")));


        // Create a new Coffee object
        Coffee c = new Coffee(coffeeService.getLastId() + 1,
                name, type, size, price, roastLevel, origin, isDecaf, stock, flavorList, brewMethod
        );


        coffeeService.addCoffee(c);  // Add the new coffee to the service
        return "redirect:/";  // Redirect to the home page after saving
    }


    // Display the form for editing an existing coffee
    @GetMapping("/edit")
    public String edit(@RequestParam int id, Model model) {
        Coffee c = coffeeService.getCoffee(id);
        if (c == null) {
            model.addAttribute("error", "Coffee not found.");  // Add error message if not found
            return "error";  // Render error page if coffee is not found
        }
        model.addAttribute("coffee", c);  // Add the coffee to the model for editing
        return "edit";  // Return the edit view for modifying the coffee
    }


    // Update an existing coffee entry
    @PostMapping("/update")
    public String update(
            @RequestParam int id,
            @RequestParam String name,
            @RequestParam String type,
            @RequestParam String size,
            @RequestParam double price,
            @RequestParam String roastLevel,
            @RequestParam String origin,
            @RequestParam(defaultValue = "false") boolean isDecaf,
            @RequestParam int stock,
            @RequestParam String flavorNotes,  // flavorNotes as a string input
            @RequestParam String brewMethod
    ) {
        Coffee c = coffeeService.getCoffee(id);  // Retrieve the coffee by ID
        if (c != null) {
            // Set new values from the form input
            c.setName(name);
            c.setType(type);
            c.setSize(size);
            c.setPrice(price);
            c.setRoastLevel(roastLevel);
            c.setOrigin(origin);
            c.setDecaf(isDecaf);
            c.setStock(stock);


            // Split flavorNotes by semicolon and update the list
            c.setFlavorNotes(new ArrayList<>(Arrays.asList(flavorNotes.split(";"))));


            c.setBrewMethod(brewMethod);  // Set brew method
            coffeeService.updateCoffee(id, c);  // Update the coffee in the service
        }
        return "redirect:/";  // Redirect to the home page after update
    }
}

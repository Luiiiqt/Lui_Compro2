package com.luigi.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class HomeController {
    private List<Coffee> coffeeList = new ArrayList<>();

    public HomeController() {
        coffeeList.add(new Coffee(1, "Espresso", "Arabica", "Small", 3.50, "Dark", "Ethiopia", false, 10, Arrays.asList("Chocolate", "Nutty"), "Espresso"));
        coffeeList.add(new Coffee(2, "Latte", "Arabica", "Medium", 4.50, "Medium", "Brazil", false, 8, Arrays.asList("Creamy", "Sweet"), "Drip"));
        coffeeList.add(new Coffee(3, "Cappuccino", "Robusta", "Large", 5.00, "Medium", "Colombia", false, 12, Arrays.asList("Fruity", "Bold"), "French Press"));
        coffeeList.add(new Coffee(4, "Mocha", "Arabica", "Medium", 4.75, "Dark", "Guatemala", false, 6, Arrays.asList("Chocolate", "Smooth"), "Espresso"));
        coffeeList.add(new Coffee(5, "Americano", "Robusta", "Large", 3.25, "Light", "Kenya", false, 15, Arrays.asList("Citrus", "Balanced"), "Drip"));
    }

    @GetMapping("/add")
    public String addCoffeeForm() {
        return "new";
    }

    @PostMapping("/save")
    public String saveCoffee(
            @RequestParam String name,
            @RequestParam String type,
            @RequestParam String size,
            @RequestParam double price,
            @RequestParam String roastLevel,
            @RequestParam String origin,
            @RequestParam(required = false, defaultValue = "false") boolean isDecaf,
            @RequestParam int stock,
            @RequestParam String brewMethod
    ) {
        int newId = coffeeList.get(coffeeList.size() - 1).getId() + 1;
        coffeeList.add(new Coffee(newId, name, type, size, price, roastLevel, origin, isDecaf, stock, new ArrayList<>(), brewMethod));
        return "redirect:/";
    }

    @GetMapping("/edit")
    public String editCoffee(@RequestParam int id, Model model) {
        for (Coffee coffee : coffeeList) {
            if (coffee.getId() == id) {
                model.addAttribute("coffee", coffee);
                return "edit";
            }
        }
        return "redirect:/";
    }

    @PostMapping("/update")
    public String updateCoffee(
            @RequestParam int id,
            @RequestParam String name,
            @RequestParam String type,
            @RequestParam String size,
            @RequestParam double price,
            @RequestParam String roastLevel,
            @RequestParam String origin,
            @RequestParam(required = false, defaultValue = "false") boolean isDecaf,
            @RequestParam int stock,
            @RequestParam String brewMethod
    ) {
        for (Coffee coffee : coffeeList) {
            if (coffee.getId() == id) {
                coffee.setName(name);
                coffee.setType(type);
                coffee.setSize(size);
                coffee.setPrice(price);
                coffee.setRoastLevel(roastLevel);
                coffee.setOrigin(origin);
                coffee.setDecaf(isDecaf);
                coffee.setStock(stock);
                coffee.setBrewMethod(brewMethod);
                break;
            }
        }
        return "redirect:/";
    }

    @GetMapping("/")
    public String getCoffees(Model model) {
        model.addAttribute("coffees", coffeeList);
        return "index";
    }

    @GetMapping("/delete")
    public String deleteCoffee(@RequestParam int id) {
        coffeeList.removeIf(coffee -> coffee.getId() == id);
        return "redirect:/";
    }
}
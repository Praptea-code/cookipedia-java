/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import java.util.List;
import model.AppModel;
import model.RecipeData;
import model.RecipeRequest;
/**
 *
 * @author Acer
 */
public class Validate {
    private final AppModel model;

    public Validate(AppModel model) {
        this.model = model;
    }

    public String validateLogin(String username, String password) {
        if (username == null || password == null) {
            return "Please enter username and password.";
        }

        username = username.trim();
        password = password.trim();

        if (username.isEmpty() && password.isEmpty()) {
            return "Please enter username and password.";
        }
        if (username.isEmpty()) {
            return "Please enter your username.";
        }
        if (password.isEmpty()) {
            return "Please enter your password.";
        }

        if (username.equals("admin") && password.equals("12345")) {
            return "admin";
        } else if (username.equals("user") && password.equals("67890")) {
            return "user";
        } else {
            return "Invalid username or password!";
        }
    }

    public String validateRecipeFields(String title, String cuisine, String difficulty,
                                       String prepTime, String rating) {

        if (title == null || title.trim().isEmpty()) {
            return "Recipe title cannot be empty!";
        }

        // duplicate check
        for (RecipeData existingRecipe : model.getAllRecipes()) {
            if (existingRecipe.getTitle().equalsIgnoreCase(title.trim()) &&
                existingRecipe.getCuisine().equalsIgnoreCase(cuisine.trim()) &&
                existingRecipe.getDifficulty().equalsIgnoreCase(difficulty.trim())) {
                return "A recipe with this title, cuisine, and difficulty already exists!";
            }
        }

        if (cuisine == null || cuisine.trim().isEmpty()) {
            return "Cuisine type cannot be empty!";
        }

        if (difficulty == null || difficulty.trim().isEmpty()) {
            return "Difficulty level cannot be empty!";
        }

        String difficultyLower = difficulty.trim().toLowerCase();
        if (!difficultyLower.equals("easy") &&
            !difficultyLower.equals("medium") &&
            !difficultyLower.equals("hard")) {
            return "Difficulty must be Easy, Medium, or Hard!";
        }

        if (prepTime == null || prepTime.trim().isEmpty()) {
            return "Preparation time cannot be empty!";
        }

        try {
            int time = Integer.parseInt(prepTime.trim());
            if (time <= 0) {
                return "Preparation time must be a positive number!";
            }
            if (time > 1440) {
                return "Preparation time seems unreasonably long (max 1440 minutes)!";
            }
        } catch (NumberFormatException e) {
            return "Preparation time must be a valid number! Please enter only digits.";
        }

        if (rating == null || rating.trim().isEmpty()) {
            return "Rating cannot be empty!";
        }

        try {
            double rate = Double.parseDouble(rating.trim());
            if (rate < 0.0 || rate > 5.0) {
                return "Rating must be between 0.0 and 5.0!";
            }
        } catch (NumberFormatException e) {
            return "Rating must be a valid decimal number (e.g., 4.5)!";
        }

        return "valid";
    }

    public boolean isValidStatus(String status) {
        if (status == null) return false;
        String s = status.trim().toLowerCase();
        return s.equals("pending") || s.equals("updated") || s.equals("cancelled");
    }

    public boolean validateDeleteRequestUserTitle(List<RecipeRequest> requests,
                                                  String username, String title) {
        if (username == null || title == null) return false;
        for (RecipeRequest req : requests) {
            if (req.getUsername().equals(username) && req.getTitle().equals(title)) {
                return true;
            }
        }
        return false;
    }
}

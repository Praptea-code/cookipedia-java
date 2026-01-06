/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.AppModel;
import model.RecipeData;
import model.RecipeRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Queue;
/**
 *
 * @author Acer
 */
public class AppController {
    private final AppModel model;
    
    public AppController() {
        this.model = new AppModel();
    }
    
    // ===== Authentication Logic =====
    
    /**
     * Validates user login credentials
     * @return "admin" if admin login, "user" if user login, error message otherwise
     */
    public String validateLogin(String username, String password) {
        // Input validation
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
        
        // Authentication logic
        if (username.equals("admin") && password.equals("12345")) {
            return "admin";
        } else if (username.equals("user") && password.equals("67890")) {
            return "user";
        } else {
            return "Invalid username or password!";
        }
    }
    
    // ===== Recipe Management Logic =====
    
    /**
     * Validates recipe form fields
     * @return "valid" if all fields are valid, error message otherwise
     */
    public String validateRecipeFields(String title, String cuisine, String difficulty,
                                      String prepTime, String rating) {
        // Check for empty fields
        if (title == null || title.trim().isEmpty() ||
            cuisine == null || cuisine.trim().isEmpty() ||
            difficulty == null || difficulty.trim().isEmpty() ||
            prepTime == null || prepTime.trim().isEmpty() ||
            rating == null || rating.trim().isEmpty()) {
            return "Please fill all required fields!";
        }
        
        // Validate numeric fields
        try {
            int time = Integer.parseInt(prepTime.trim());
            if (time <= 0) {
                return "Preparation time must be positive!";
            }
        } catch (NumberFormatException e) {
            return "Please enter a valid number for preparation time!";
        }
        
        try {
            double rate = Double.parseDouble(rating.trim());
            if (rate < 0 || rate > 5) {
                return "Rating must be between 0 and 5!";
            }
        } catch (NumberFormatException e) {
            return "Please enter a valid number for rating!";
        }
        
        return "valid";
    }
    
    /**
     * Adds a new recipe after validation
     */
    public String addRecipe(String title, String cuisine, String difficulty,
                           String prepTime, String rating, String imagePath,
                           String ingredients, String process) {
        // Validate first
        String validation = validateRecipeFields(title, cuisine, difficulty, prepTime, rating);
        if (!validation.equals("valid")) {
            return validation;
        }
        
        // Set default image if empty
        if (imagePath == null || imagePath.trim().isEmpty()) {
            imagePath = "/img/default.png";
        }
        
        // Create and add recipe
        RecipeData recipe = new RecipeData(
            title.trim(),
            cuisine.trim(),
            difficulty.trim(),
            Integer.parseInt(prepTime.trim()),
            Double.parseDouble(rating.trim()),
            imagePath.trim(),
            ingredients != null ? ingredients.trim() : "",
            process != null ? process.trim() : ""
        );
        
        model.addRecipe(recipe);
        return "success";
    }
    
    /**
     * Updates an existing recipe
     */
    public boolean updateRecipe(int id, String title, String cuisine, String difficulty,
                               int prepTime, double rating, String imagePath,
                               String ingredients, String process) {
        return model.updateRecipe(id, title, cuisine, difficulty, prepTime,
                                 rating, imagePath, ingredients, process);
    }
    
    /**
     * Deletes a recipe by ID
     */
    public boolean deleteRecipe(int id) {
        return model.deleteRecipe(id);
    }
    
    /**
     * Retrieves a recipe by ID
     */
    public RecipeData getRecipeById(int id) {
        return model.getRecipeById(id);
    }
    
    /**
     * Gets all recipes
     */
    public List<RecipeData> getAllRecipes() {
        return model.getAllRecipes();
    }
    
    /**
     * Gets recently added recipes
     */
    public List<RecipeData> getRecentlyAdded(int count) {
        return model.getRecentlyAdded(count);
    }
    
    /**
     * Sorts recipes by name
     */
    public List<RecipeData> sortRecipesByName() {
        List<RecipeData> recipes = model.getAllRecipes();
        recipes.sort((r1, r2) -> r1.getTitle().compareToIgnoreCase(r2.getTitle()));
        return recipes;
    }
    
    /**
     * Sorts recipes by difficulty
     */
    public List<RecipeData> sortRecipesByDifficulty() {
        List<RecipeData> recipes = model.getAllRecipes();
        recipes.sort((r1, r2) -> {
            int order1 = getDifficultyOrder(r1.getDifficulty());
            int order2 = getDifficultyOrder(r2.getDifficulty());
            return Integer.compare(order1, order2);
        });
        return recipes;
    }
    
    private int getDifficultyOrder(String difficulty) {
        switch (difficulty.toLowerCase()) {
            case "easy": return 1;
            case "medium": return 2;
            case "hard": return 3;
            default: return 4;
        }
    }
    
    /**
     * Searches recipes by title
     */
    public List<RecipeData> searchRecipes(String query) {
        if (query == null || query.trim().isEmpty()) {
            return model.getAllRecipes();
        }
        
        String lowerQuery = query.trim().toLowerCase();
        List<RecipeData> results = new java.util.ArrayList<>();
        
        for (RecipeData recipe : model.getAllRecipes()) {
            if (recipe.getTitle().toLowerCase().contains(lowerQuery)) {
                results.add(recipe);
            }
        }
        
        return results;
    }
    
    // ===== Recipe Request Logic =====
    
    /**
     * Validates and adds a recipe request
     */
    public String addRecipeRequest(String username, String title,
                                   String vegNonVeg, String notes) {
        // Validation
        if (username == null || username.trim().isEmpty()) {
            return "Username is required!";
        }
        if (title == null || title.trim().isEmpty()) {
            return "Recipe title is required!";
        }
        
        // Generate timestamp
        LocalDateTime now = LocalDateTime.now();
        String date = now.toLocalDate().toString();
        String time = now.toLocalTime().toString().substring(0, 8); // HH:MM:SS
        
        // Create and add request
        RecipeRequest request = new RecipeRequest(
            username.trim(),
            title.trim(),
            vegNonVeg != null ? vegNonVeg.trim() : "",
            notes != null ? notes.trim() : "",
            date,
            time
        );
        
        model.addRequest(request);
        model.incrementRequestCount();
        
        return "success";
    }
    
    /**
     * Gets all recipe requests
     */
    public Queue<RecipeRequest> getAllRequests() {
        return model.getAllRequests();
    }
    
    /**
     * Updates request status
     */
    public boolean updateRequestStatus(RecipeRequest request, String newStatus) {
        if (request == null || newStatus == null) {
            return false;
        }
        request.setStatus(newStatus);
        return true;
    }
    
    /**
     * Removes a recipe request
     */
    public boolean removeRequest(RecipeRequest request) {
        return model.removeRequest(request);
    }
    
    // ===== History Management Logic =====
    
    /**
     * Adds a recipe to viewing history
     */
    public void addToHistory(RecipeData recipe) {
        if (recipe != null) {
            model.addToHistory(recipe);
        }
    }
    
    /**
     * Gets recipe viewing history
     */
    public List<RecipeData> getHistory() {
        return model.getHistory();
    }
    
    /**
     * Clears viewing history
     */
    public void clearHistory() {
        model.clearHistory();
    }
    
    /**
     * Searches history by title
     */
    public List<RecipeData> searchHistory(String query) {
        if (query == null || query.trim().isEmpty()) {
            return model.getHistory();
        }
        
        String lowerQuery = query.trim().toLowerCase();
        List<RecipeData> results = new java.util.ArrayList<>();
        
        for (RecipeData recipe : model.getHistory()) {
            if (recipe.getTitle().toLowerCase().contains(lowerQuery)) {
                results.add(recipe);
            }
        }
        
        return results;
    }
    
    // ===== Statistics Logic =====
    
    /**
     * Marks a recipe as cooked
     */
    public void markRecipeAsCooked() {
        model.incrementCookedCount();
    }
    
    /**
     * Gets count of cooked recipes
     */
    public int getCookedCount() {
        return model.getCookedCount();
    }
    
    /**
     * Gets count of recipes yet to cook
     */
    public int getYetToCookCount() {
        return model.getYetToCookCount();
    }
    
    /**
     * Gets total recipe count
     */
    public int getTotalRecipes() {
        return model.getTotalRecipes();
    }
    
    /**
     * Gets total request count
     */
    public int getRequestCount() {
        return model.getRequestCount();
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.AppModel;
import model.AppModel.ValidationResult;
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
    
    public AppController(AppModel model) {
        this.model = model;
    }
    
    // Delegate to model - NO UI LOGIC HERE
    public int getCookedCount() {
        return model.getCookedCount();
    }
    
    public int getYetToCookCount() {
        return model.getYetToCookCount();
    }
    
    public int getTotalRecipes() {
        return model.getTotalRecipes();
    }
    
    public int getRequestCount() {
        return model.getRequestCount();
    }
    
    public List<RecipeData> getAllRecipes() {
        return model.getAllRecipes();
    }
    
    public List<RecipeData> getRecentlyAdded(int count) {
        return model.getRecentlyAdded(count);
    }
    
    public List<RecipeData> getHistory() {
        return model.getHistory();
    }
    
    public Queue<RecipeRequest> getAllRequests() {
        return model.getAllRequests();
    }
    
    public RecipeData getRecipeById(int id) {
        return model.getRecipeById(id);
    }
    
    // Business operations
    public void markRecipeAsCooked(RecipeData recipe) {
        model.incrementCookedCount();
        model.addToHistory(recipe);
    }
    
    public void addToHistory(RecipeData recipe) {
        model.addToHistory(recipe);
    }
    
    public void clearHistory() {
        model.clearHistory();
    }
    
    // Recipe CRUD
    public ValidationResult addRecipe(String title, String cuisine, String difficulty,
                                     String prepTime, String rating, String imagePath,
                                     String ingredients, String process) {
        ValidationResult validation = model.validateRecipeFields(
            title, cuisine, difficulty, prepTime, rating
        );
        
        if (!validation.isValid()) {
            return validation;
        }
        
        try {
            String finalImagePath = (imagePath == null || imagePath.trim().isEmpty()) 
                ? "/img/default.png" 
                : imagePath.trim();
            
            RecipeData recipe = new RecipeData(
                title.trim(),
                cuisine.trim(),
                difficulty.trim(),
                Integer.parseInt(prepTime.trim()),
                Double.parseDouble(rating.trim()),
                finalImagePath,
                ingredients != null ? ingredients.trim() : "",
                process != null ? process.trim() : ""
            );
            
            model.addRecipe(recipe);
            return new ValidationResult(true, "Recipe added successfully!");
            
        } catch (Exception e) {
            return new ValidationResult(false, "Error creating recipe: " + e.getMessage());
        }
    }
    
    public ValidationResult updateRecipe(int id, String title, String cuisine,
                                        String difficulty, String prepTime, 
                                        String rating, String imagePath,
                                        String ingredients, String process) {
        ValidationResult validation = model.validateRecipeFields(
            title, cuisine, difficulty, prepTime, rating
        );
        
        if (!validation.isValid()) {
            return validation;
        }
        
        try {
            String finalImagePath = (imagePath == null || imagePath.trim().isEmpty()) 
                ? "/img/default.png" 
                : imagePath.trim();
            
            boolean updated = model.updateRecipe(
                id,
                title.trim(),
                cuisine.trim(),
                difficulty.trim(),
                Integer.parseInt(prepTime.trim()),
                Double.parseDouble(rating.trim()),
                finalImagePath,
                ingredients != null ? ingredients.trim() : "",
                process != null ? process.trim() : ""
            );
            
            if (updated) {
                return new ValidationResult(true, "Recipe updated successfully!");
            } else {
                return new ValidationResult(false, "Recipe not found!");
            }
            
        } catch (Exception e) {
            return new ValidationResult(false, "Error updating recipe: " + e.getMessage());
        }
    }
    
    public ValidationResult deleteRecipe(int id) {
        boolean deleted = model.deleteRecipe(id);
        if (deleted) {
            return new ValidationResult(true, "Recipe deleted successfully!");
        } else {
            return new ValidationResult(false, "Recipe not found!");
        }
    }
    
    // Request operations
    public ValidationResult addRecipeRequest(String username, String title,
                                            String vegNonVeg, String notes) {
        ValidationResult validation = model.validateRequestFields(username, title);
        
        if (!validation.isValid()) {
            return validation;
        }
        
        try {
            LocalDateTime now = LocalDateTime.now();
            String date = now.toLocalDate().toString();
            String time = now.toLocalTime().toString();
            
            RecipeRequest req = new RecipeRequest(
                username.trim(),
                title.trim(),
                vegNonVeg != null ? vegNonVeg.trim() : "",
                notes != null ? notes.trim() : "",
                date,
                time
            );
            
            model.addRequest(req);
            model.incrementRequestCount();
            
            return new ValidationResult(true, "Request submitted successfully!");
            
        } catch (Exception e) {
            return new ValidationResult(false, "Error creating request: " + e.getMessage());
        }
    }
    
    // Login validation
    public LoginResult validateLogin(String username, String password) {
        if ((username == null || username.trim().isEmpty()) && 
            (password == null || password.trim().isEmpty())) {
            return new LoginResult(LoginResult.Status.ERROR, 
                "Please enter username and password.");
        }
        if (username == null || username.trim().isEmpty()) {
            return new LoginResult(LoginResult.Status.ERROR, 
                "Please enter your username.");
        }
        if (password == null || password.trim().isEmpty()) {
            return new LoginResult(LoginResult.Status.ERROR, 
                "Please enter your password.");
        }
        
        String cleanUsername = username.trim();
        
        if (cleanUsername.equals("admin") && password.equals("12345")) {
            return new LoginResult(LoginResult.Status.ADMIN, "Login successful");
        } else if (cleanUsername.equals("user") && password.equals("67890")) {
            return new LoginResult(LoginResult.Status.USER, "Login successful");
        } else {
            return new LoginResult(LoginResult.Status.ERROR, "User not found!");
        }
    }
    
    // Inner class for login results
    public static class LoginResult {
        public enum Status { ADMIN, USER, ERROR }
        
        private final Status status;
        private final String message;
        
        public LoginResult(Status status, String message) {
            this.status = status;
            this.message = message;
        }
        
        public Status getStatus() { return status; }
        public String getMessage() { return message; }
    }


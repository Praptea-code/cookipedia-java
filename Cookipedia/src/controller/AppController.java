/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import model.AppModel;
import model.RecipeData;
import model.RecipeRequest;
import java.util.List;
/**
 *
 * @author Acer
 */
public class AppController {
    private final AppModel model;
    
    public AppController() {
        this.model = new AppModel();
    }
    
    public int getCookedCount() {
        return model.getCookedCount();
    }
    
    public void markRecipeAsCooked() {
        model.incrementCookedCount();
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
    
    public void addRecipe(RecipeData recipe) {
        model.addRecipe(recipe);
    }
    
    public void addToHistory(RecipeData recipe) {
        model.addToHistory(recipe);
    }
    
    public java.util.List<RecipeData> getHistory() {
        return model.getHistory();
    }
    
    public void clearHistory() {
        model.clearHistory();
    }
    
    public void addRequest(RecipeRequest req) {
        model.addRequest(req);
    }
    
    public java.util.Queue<RecipeRequest> getAllRequests() {
        return model.getAllRequests();
    }
    
    public boolean updateRecipe(int id, String title, String cuisine,
                                String difficulty, int prepTime, double rating,
                                String imagePath, String ingredients, String process) {
        return model.updateRecipe(id, title, cuisine, difficulty, prepTime, 
                                 rating, imagePath, ingredients, process);
    }
    
    public boolean deleteRecipe(int id) {
        return model.deleteRecipe(id);
    }
    
    public RecipeData getRecipeById(int id) {
        for (RecipeData r : model.getAllRecipes()) {
            if (r.id == id) {
                return r;
            }
        }
        return null;
    }
    
    public String addRecipeRequest(String username, String title, 
                                   String vegNonVeg, String notes) {
        if (username.isEmpty() || title.isEmpty()) {
            return "Username and Recipe Title are required!";
        }
        
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        String date = now.toLocalDate().toString();
        String time = now.toLocalTime().toString();
        
        RecipeRequest req = new RecipeRequest(username, title, vegNonVeg, notes, date, time);
        model.addRequest(req);
        model.incrementRequestCount();
        
        return "success";
    }

    public String validateLogin(String username, String password) {
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
            return "User not found!";
        }
    }
    
    public String validateRecipeFields(String title, String cuisine, String difficulty,
                                      String prepTime, String rating) {
        if (title.isEmpty() || cuisine.isEmpty() || difficulty.isEmpty() ||
            prepTime.isEmpty() || rating.isEmpty()) {
            return "Please fill all required fields!";
        }
        
        try {
            Integer.parseInt(prepTime);
            Double.parseDouble(rating);
            return "valid";
        } catch (NumberFormatException e) {
            return "Please enter valid numbers for Time and Rating!";
        }
    }
    
}

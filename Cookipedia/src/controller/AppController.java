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
    

    public List<RecipeData> getAllRecipes() {
        return model.getAllRecipes();
    }
    
    public List<RecipeData> getRecentlyAdded(int count) {
        return model.getRecentlyAdded(count);
    }
    
    public void addRecipe(RecipeData recipe) {
        model.addRecipe(recipe);
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
    
    public void addRequest(RecipeRequest req) {
        model.addRequest(req);
    }
    
    public java.util.Queue<RecipeRequest> getAllRequests() {
        return model.getAllRequests();
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

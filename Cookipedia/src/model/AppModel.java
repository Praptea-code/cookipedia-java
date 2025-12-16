/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Acer
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppModel {

    // simple inner class for recipes
    public static class RecipeData {
        public int id;
        public String title;
        public String cuisine;
        public String difficulty;
        public int prepTime;
        public double rating;
        public String imagePath;

        public RecipeData(int id, String title, String cuisine,
                          String difficulty, int prepTime,
                          double rating, String imagePath) {
            this.id = id;
            this.title = title;
            this.cuisine = cuisine;
            this.difficulty = difficulty;
            this.prepTime = prepTime;
            this.rating = rating;
            this.imagePath = imagePath;
        }
    }

    private final List<RecipeData> recipes = new ArrayList<>();

    public AppModel() {
        seedDummyRecipes();
    }

    private void seedDummyRecipes() {
        recipes.add(new RecipeData(1, "Paneer Tikka", "Indian",
                "Medium", 30, 4.5, "/img/paneer.png"));
        recipes.add(new RecipeData(2, "Margherita Pizza", "Italian",
                "Easy", 20, 4.2, "/img/pizza.png"));
        recipes.add(new RecipeData(3, "Veg Fried Rice", "Chinese",
                "Easy", 15, 4.0, "/img/friedrice.png"));
        recipes.add(new RecipeData(4, "Brownie", "Dessert",
                "Medium", 25, 4.8, "/img/brownie.png"));
        recipes.add(new RecipeData(5, "Momo", "Nepali",
                "Medium", 40, 4.6, "/img/momo.png"));
    }

    public List<RecipeData> getAllRecipes() {
        return new ArrayList<>(recipes);
    }

    // for random recommended
    public java.util.List<RecipeData> getRecentlyAdded(int count) {
        java.util.List<RecipeData> all = getAllRecipes();   
        int size = all.size();
        int fromIndex = Math.max(0, size - count);
        return all.subList(fromIndex, size);                
    }
}

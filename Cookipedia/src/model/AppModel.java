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
        public String type;
        public String difficulty;
        public int prepTime;
        public double rating;
        public String imagePath;
        public String ingredients;  
        public String process; 

        private static int idCounter = 1;

        // Constructor WITH ID (for seeding initial data)
        public RecipeData(int id, String title, String cuisine, String difficulty, 
                         int prepTime, double rating, String imagePath, 
                         String ingredients, String process) {
            this.id = id;
            this.title = title;
            this.cuisine = cuisine;
            this.type = ""; // Set default empty for seeded data
            this.difficulty = difficulty;
            this.prepTime = prepTime;
            this.rating = rating;
            this.imagePath = imagePath;
            this.ingredients = ingredients;
            this.process = process;
       }
        
       public RecipeData(String title, String cuisine, String difficulty, 
                         int prepTime, double rating, String imagePath, 
                         String ingredients, String process) {
            this.id = idCounter++;
            this.title = title;
            this.cuisine = cuisine;
            this.type = ""; // Keep field but leave empty
            this.difficulty = difficulty;
            this.prepTime = prepTime;
            this.rating = rating;
            this.imagePath = imagePath;
            this.ingredients = ingredients;
            this.process = process;
        }
    }

    private final List<RecipeData> recipes = new ArrayList<>();

    public AppModel() {
        seedDummyRecipes();
    }

    private void seedDummyRecipes() {
        recipes.add(new RecipeData(
            1, "Chicken Chowmein", "Chinese",
            "Easy", 25, 4.3, "/img/chickenChowmein.jpg",
            "Noodles, chicken, onion, cabbage, carrot, garlic, soy sauce, oil",
            "Boil noodles in salted water until soft and drain. Heat oil in a pan, add garlic and sauté until fragrant. "
          + "Add sliced onion and cook until light brown. Add chicken and cook until fully done. "
          + "Add cabbage and carrot and stir-fry on high heat. Add boiled noodles, soy sauce, and toss well. "
          + "Cook for 2–3 minutes and serve hot."
        ));

        recipes.add(new RecipeData(
            2, "Veg Burger", "Fast Food",
            "Easy", 15, 4.1, "/img/vegBurger.jpg",
            "Burger bun, potato patty, lettuce, tomato, onion, mayo, ketchup",
            "Heat oil and fry the potato patty until golden brown on both sides. Slice tomatoes and onions. "
          + "Toast burger buns lightly. Spread mayonnaise and ketchup on the buns. "
          + "Place lettuce, patty, tomato, and onion on the bun. Cover with top bun and serve warm."
        ));

        recipes.add(new RecipeData(
            3, "Pasta Alfredo", "Italian",
            "Medium", 30, 4.4, "/img/alfredoPasta.jpg",
            "Pasta, cream, butter, garlic, cheese, salt, pepper",
            "Boil pasta in salted water until al dente and drain. Heat butter in a pan and sauté garlic gently. "
          + "Add cream and cook on low heat. Add cheese, salt, and pepper and stir until smooth. "
          + "Add cooked pasta and mix well. Cook for 2–3 minutes and serve hot."
        ));

        recipes.add(new RecipeData(
            4, "Chicken Biryani", "Indian",
            "Hard", 60, 4.9, "/img/chickenBiryani.jpg",
            "Rice, chicken, onion, yogurt, biryani masala, oil",
            "Wash and soak rice for 30 minutes, then cook halfway. Fry onions until golden brown. "
          + "Add chicken, yogurt, and biryani masala and cook until chicken is tender. "
          + "Layer rice and chicken in a pot. Cover and cook on low heat (dum) for 20 minutes. "
          + "Serve hot with raita."
        ));

        recipes.add(new RecipeData(
            5, "Sel Roti", "Nepali",
            "Medium", 45, 4.7, "/img/selRoti.jpg",
            "Rice flour, sugar, milk, ghee, oil",
            "Prepare a smooth batter by mixing rice flour, sugar, and milk. Rest the batter for 30 minutes. "
          + "Heat oil in a deep pan. Pour batter in a circular motion to form a ring. "
          + "Fry on medium heat until golden brown on both sides. Remove and serve."
        ));

        recipes.add(new RecipeData(
            6, "Spring Rolls", "Chinese",
            "Easy", 20, 4.2, "/img/springRolls.jpg",
            "Spring roll wrapper, cabbage, carrot, garlic, oil",
            "Heat oil in a pan and sauté garlic. Add cabbage and carrot and cook briefly. "
          + "Let the mixture cool. Place filling on wrapper and roll tightly. "
          + "Heat oil and deep fry rolls until crispy and golden. Serve with sauce."
        ));

        recipes.add(new RecipeData(
            7, "French Fries", "Fast Food",
            "Easy", 10, 4.0, "/img/frenchFries.jpg",
            "Potatoes, oil, salt",
            "Peel and cut potatoes into thin strips. Wash and dry them completely. "
          + "Fry once on medium heat until soft, then remove and cool. "
          + "Fry again on high heat until golden and crispy. Sprinkle salt and serve hot."
        ));

        recipes.add(new RecipeData(
            8, "Lasagna", "Italian",
            "Hard", 50, 4.6, "/img/lasagna.jpg",
            "Lasagna sheets, tomato sauce, cheese, vegetables/meat",
            "Boil lasagna sheets and drain. Prepare tomato sauce separately. "
          + "In a baking dish, layer sauce, sheets, and cheese repeatedly. "
          + "Top with cheese and bake in preheated oven until cheese melts and turns golden. Serve hot."
        ));

        recipes.add(new RecipeData(
            9, "Chicken Curry", "Indian",
            "Medium", 35, 4.5, "/img/chickenCurry.jpg",
            "Chicken, onion, tomato, ginger garlic paste, spices, oil",
            "Heat oil and fry onions until golden. Add ginger garlic paste and sauté well. "
          + "Add chicken and spices and cook until chicken changes color. "
          + "Add tomatoes and simmer until gravy thickens. Serve with rice or roti."
        ));

        recipes.add(new RecipeData(
            10, "Yomari", "Nepali",
            "Hard", 90, 4.8, "/img/yomari.jpg",
            "Rice flour, chaku (jaggery), sesame seeds",
            "Prepare rice flour dough using warm water. Make filling by mixing chaku and sesame seeds. "
          + "Shape dough into fish-like form and fill with mixture. "
          + "Steam Yomari for about 15 minutes until cooked. Serve warm."
        ));

        recipes.add(new RecipeData(
            11, "Veg Sandwich", "Fast Food",
            "Easy", 10, 3.9, "/img/vegSandwich.jpg",
            "Bread, cucumber, tomato, butter, salt",
            "Apply butter evenly on bread slices. Slice cucumber and tomato thinly. "
          + "Arrange vegetables on bread, sprinkle salt. "
          + "Cover with another slice, cut into halves, and serve fresh."
        ));

        recipes.add(new RecipeData(
            12, "Hakka Noodles", "Chinese",
            "Easy", 20, 4.1, "/img/hakkaNoodles.jpg",
            "Noodles, onion, cabbage, carrot, garlic, soy sauce, oil",
            "Boil noodles and drain. Heat oil in a wok and sauté garlic. "
          + "Add onion and vegetables and stir-fry on high heat. "
          + "Add noodles and soy sauce and toss well. Cook for 2 minutes and serve hot."
        ));

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
    
    public void addRecipe(RecipeData recipe) {
        recipes.add(recipe);
    }
    
    public boolean updateRecipe(int id, String title, String cuisine, String type,
                               String difficulty, int prepTime, double rating, String imagePath) {
        for (RecipeData r : recipes) {
            if (r.id == id) {
                r.title = title;
                r.cuisine = cuisine;
                r.type = type;
                r.difficulty = difficulty;
                r.prepTime = prepTime;
                r.rating = rating;
                r.imagePath = imagePath;
                return true;
            }
        }
        return false;
    }
    
    public boolean deleteRecipe(int id) {
        return recipes.removeIf(r -> r.id == id);
    }
    
    public static class RecipeRequest {
        public String username;
        public String title;
        public String vegNonVeg;
        public String notes;
        public String date;
        public String time;
        public String status;

        public RecipeRequest(String username, String title,
                             String vegNonVeg, String notes,
                             String date, String time) {
            this.username = username;
            this.title = title;
            this.vegNonVeg = vegNonVeg;
            this.notes = notes;
            this.date = date;
            this.time = time;
            this.status = "Pending";
        }
    }

    private final java.util.Queue<RecipeRequest> requestQueue =
            new java.util.LinkedList<>();

    public void addRequest(RecipeRequest req) {
        requestQueue.offer(req);   // enqueue FIFO
    }

    public java.util.Queue<RecipeRequest> getAllRequests() {
        return requestQueue;       // user/admin read
    }

    public RecipeRequest pollNextRequest() {
        return requestQueue.poll(); // admin handles next
    }
}

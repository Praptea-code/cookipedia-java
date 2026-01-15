/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Acer
 */
import controller.HistoryQueue;
import java.util.ArrayList;
import java.util.List;

public class AppModel {

    private final List<RecipeData> recipes;
    private final HistoryQueue historyQueue;
    private int cookedCount;
    private int requestCount;
    private static final int HISTORY_LIMIT = 8;

    /*
    this constructor creates a fresh model instance with empty data structures
    it sets up the recipe list and history queue and calls seedDummyRecipes so ui has data to show
    */
    public AppModel() {
        this.recipes = new ArrayList<RecipeData>();
        this.historyQueue = new HistoryQueue(HISTORY_LIMIT);
        this.cookedCount = 0;
        this.requestCount = 0;
        seedDummyRecipes();
    }

    //recipe crud operations

    /*
    this method adds a new recipe into the main recipe list
    it takes a recipedata object created by the controller and appends it to the list
    */
    public void addRecipe(RecipeData recipe) {
        recipes.add(recipe);
    }

    /*
    this method returns a copy of all recipes stored in the model
    it creates and returns a new arraylist so outside code cannot change the internal list directly
    */
    public List<RecipeData> getAllRecipes() {
        return new ArrayList<RecipeData>(recipes);
    }

    /*
    this method searches for a recipe by its unique id
    it loops through the recipe list and returns the first recipedata whose id matches or null when not found
    */
    public RecipeData getRecipeById(int id) {
        int i = 0;
        while (i < recipes.size()) {
            RecipeData recipe = recipes.get(i);
            if (recipe.getId() == id) {
                return recipe;
            }
            i = i + 1;
        }
        return null;
    }

    /*
    this method updates an existing recipe with new field values
    it takes recipe id and new title cuisine difficulty prepTime rating imagePath ingredients and process
    it finds the matching recipe and sets all these fields and returns true or returns false if id does not exist
    */
    public boolean updateRecipe(int id, String title, String cuisine, String difficulty,int prepTime, double rating, String imagePath,String ingredients, String process) {
        RecipeData recipe = getRecipeById(id);
        if (recipe != null) {
            recipe.setTitle(title);
            recipe.setCuisine(cuisine);
            recipe.setDifficulty(difficulty);
            recipe.setPrepTime(prepTime);
            recipe.setRating(rating);
            recipe.setImagePath(imagePath);
            recipe.setIngredients(ingredients);
            recipe.setProcess(process);
            return true;
        }
        return false;
    }

    /*
    this method removes a recipe from the main list using its id
    it loops through the list manually and removes the first recipe whose id matches and returns true or false when not found
    */
    public boolean deleteRecipe(int id) {
        int i = 0;
        while (i < recipes.size()) {
            RecipeData r = recipes.get(i);
            if (r.getId() == id) {
                recipes.remove(i);
                return true;
            }
            i = i + 1;
        }
        return false;
    }

    /*
    this method returns a list of most recently added recipes
    it takes count integer and builds a new list from the end of recipes without using math helpers
    */
    public List<RecipeData> getRecentlyAdded(int count) {
        int size = recipes.size();
        int fromIndex = 0;
        if (size - count > 0) {
            fromIndex = size - count;
        }

        List<RecipeData> result = new ArrayList<RecipeData>();
        int i = fromIndex;
        while (i < size) {
            result.add(recipes.get(i));
            i = i + 1;
        }
        return result;
    }

    //history operations

    /*
    this method records that user viewed a recipe into the history queue
    it first checks if this recipe already exists in current history array and skips duplicate insertion
    if it is not already present it tries to add at front and when full it removes the oldest recipe and then adds at front again
    */
    public void addToHistory(RecipeData recipe) {
        RecipeData[] current = historyQueue.toArray();
        int i = 0;
        while (i < current.length) {
            if (current[i].getId() == recipe.getId()) {
                return;
            }
            i = i + 1;
        }

        boolean ok = historyQueue.enqueue(recipe);
        if (!ok) {
            historyQueue.dequeue();
            historyQueue.enqueue(recipe);
        }
    }

    /*
    this method returns viewing history as a list from most recent to oldest
    it creates and returns a new arraylist built from the internal historyqueue array
    */
    public List<RecipeData> getHistory() {
        RecipeData[] arr = historyQueue.toArray();
        List<RecipeData> list = new ArrayList<RecipeData>();
        int i = 0;
        while (i < arr.length) {
            list.add(arr[i]);
            i = i + 1;
        }
        return list;
    }

    /*
    this method clears all elements from the viewing history
    it repeatedly calls removeLast until the history queue becomes empty
    */
    public void clearHistory() {
        while (historyQueue.dequeue() != null) {
        }
    }

    //statistics getters and setters

    /*
    this method returns how many recipes user has cooked
    it simply returns the cookedCount field
    */
    public int getCookedCount() {
        return cookedCount;
    }

    /*
    this method increases cookedCount by one
    it is expected to be called when user marks a recipe as cooked
    */
    public void incrementCookedCount() {
        cookedCount++;
    }

    /*
    this method returns how many recipe requests have been made so far
    controller updates this when a request is successfully added to its request queue
    */
    public int getRequestCount() {
        return requestCount;
    }

    /*
    this method increases requestCount by one
    it is expected to be called from controller when a new request is added
    */
    public void incrementRequestCount() {
        requestCount++;
    }

    /*
    this method returns total number of recipes stored in the model
    it uses the size of recipes list
    */
    public int getTotalRecipes() {
        return recipes.size();
    }

    /*
    this method computes how many recipes are still left to cook
    it subtracts cookedCount from total recipes and returns the remaining number
    */
    public int getYetToCookCount() {
        return getTotalRecipes() - cookedCount;
    }

    //data seeding

    /*
    this method fills the model with some initial example recipes
    it is called from the constructor so that the ui has sample data to display at start
    */
    private void seedDummyRecipes() {
        recipes.add(new RecipeData(
            "Chicken Chowmein", "Chinese", "Easy", 25, 4.3, "/img/chickenChowmein.jpg",
            "Noodles, chicken, onion, cabbage, carrot, garlic, soy sauce, oil",
            "Boil noodles in salted water until soft and drain. Heat oil in a pan, add garlic and sauté until fragrant. " +
            "Add sliced onion and cook until light brown. Add chicken and cook until fully done. " +
            "Add cabbage and carrot and stir-fry on high heat. Add boiled noodles, soy sauce, and toss well. " +
            "Cook for 2–3 minutes and serve hot."
        ));

        recipes.add(new RecipeData(
            "Veg Burger", "Fast Food", "Easy", 15, 4.1, "/img/vegBurger.jpg",
            "Burger bun, potato patty, lettuce, tomato, onion, mayo, ketchup",
            "Heat oil and fry the potato patty until golden brown on both sides. Slice tomatoes and onions. " +
            "Toast burger buns lightly. Spread mayonnaise and ketchup on the buns. " +
            "Place lettuce, patty, tomato, and onion on the bun. Cover with top bun and serve warm."
        ));

        recipes.add(new RecipeData(
            "Pasta Alfredo", "Italian", "Medium", 30, 4.4, "/img/alfredoPasta.jpg",
            "Pasta, cream, butter, garlic, cheese, salt, pepper",
            "Boil pasta in salted water until al dente and drain. Heat butter in a pan and sauté garlic gently. " +
            "Add cream and cook on low heat. Add cheese, salt, and pepper and stir until smooth. " +
            "Add cooked pasta and mix well. Cook for 2–3 minutes and serve hot."
        ));

        recipes.add(new RecipeData(
            "Chicken Biryani", "Indian", "Hard", 60, 4.9, "/img/chickenBiryani.jpg",
            "Rice, chicken, onion, yogurt, biryani masala, oil",
            "Wash and soak rice for 30 minutes, then cook halfway. Fry onions until golden brown. " +
            "Add chicken, yogurt, and biryani masala and cook until chicken is tender. " +
            "Layer rice and chicken in a pot. Cover and cook on low heat (dum) for 20 minutes. " +
            "Serve hot with raita."
        ));

        recipes.add(new RecipeData(
            "Sel Roti", "Nepali", "Medium", 45, 4.7, "/img/selRoti.jpg",
            "Rice flour, sugar, milk, ghee, oil",
            "Prepare a smooth batter by mixing rice flour, sugar, and milk. Rest the batter for 30 minutes. " +
            "Heat oil in a deep pan. Pour batter in a circular motion to form a ring. " +
            "Fry on medium heat until golden brown on both sides. Remove and serve."
        ));

        recipes.add(new RecipeData(
            "Spring Rolls", "Chinese", "Easy", 20, 4.2, "/img/springRolls.jpg",
            "Spring roll wrapper, cabbage, carrot, garlic, oil",
            "Heat oil in a pan and sauté garlic. Add cabbage and carrot and cook briefly. " +
            "Let the mixture cool. Place filling on wrapper and roll tightly. " +
            "Heat oil and deep fry rolls until crispy and golden. Serve with sauce."
        ));

        recipes.add(new RecipeData(
            "French Fries", "Fast Food", "Easy", 10, 4.0, "/img/frenchFries.jpg",
            "Potatoes, oil, salt",
            "Peel and cut potatoes into thin strips. Wash and dry them completely. " +
            "Fry once on medium heat until soft, then remove and cool. " +
            "Fry again on high heat until golden and crispy. Sprinkle salt and serve hot."
        ));

        recipes.add(new RecipeData(
            "Lasagna", "Italian", "Hard", 50, 4.6, "/img/lasagna.jpg",
            "Lasagna sheets, tomato sauce, cheese, vegetables/meat",
            "Boil lasagna sheets and drain. Prepare tomato sauce separately. " +
            "In a baking dish, layer sauce, sheets, and cheese repeatedly. " +
            "Top with cheese and bake in preheated oven until cheese melts and turns golden. Serve hot."
        ));

        recipes.add(new RecipeData(
            "Chicken Curry", "Indian", "Medium", 35, 4.5, "/img/chickenCurry.jpg",
            "Chicken, onion, tomato, ginger garlic paste, spices, oil",
            "Heat oil and fry onions until golden. Add ginger garlic paste and sauté well. " +
            "Add chicken and spices and cook until chicken changes color. " +
            "Add tomatoes and simmer until gravy thickens. Serve with rice or roti."
        ));

        recipes.add(new RecipeData(
            "Yomari", "Nepali", "Hard", 90, 4.8, "/img/yomari.jpg",
            "Rice flour, chaku (jaggery), sesame seeds",
            "Prepare rice flour dough using warm water. Make filling by mixing chaku and sesame seeds. " +
            "Shape dough into fish-like form and fill with mixture. " +
            "Steam Yomari for about 15 minutes until cooked. Serve warm."
        ));

        recipes.add(new RecipeData(
            "Veg Sandwich", "Fast Food", "Easy", 10, 3.9, "/img/vegSandwich.jpg",
            "Bread, cucumber, tomato, butter, salt",
            "Apply butter evenly on bread slices. Slice cucumber and tomato thinly. " +
            "Arrange vegetables on bread, sprinkle salt. " +
            "Cover with another slice, cut into halves, and serve fresh."
        ));

        recipes.add(new RecipeData(
            "Hakka Noodles", "Chinese", "Easy", 20, 4.1, "/img/hakkaNoodles.jpg",
            "Noodles, onion, cabbage, carrot, garlic, soy sauce, oil",
            "Boil noodles and drain. Heat oil in a wok and sauté garlic. " +
            "Add onion and vegetables and stir-fry on high heat. " +
            "Add noodles and soy sauce and toss well. Cook for 2 minutes and serve hot."
        ));
    }
}

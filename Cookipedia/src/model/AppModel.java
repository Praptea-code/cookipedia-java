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
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class AppModel {
    // Data structures
    private final List<RecipeData> recipes;
    private static final int REQUEST_QUEUE_SIZE = 50;
    private RecipeRequest[] requestItems = new RecipeRequest[REQUEST_QUEUE_SIZE];
    private int front = -1;
    private int rear = -1;
    private final Deque<RecipeData> historyQueue;
    
    // Statistics
    private int cookedCount;
    private int requestCount;
    
    // Constants
    private static final int HISTORY_LIMIT = 8;

    // Constructor - Initialize data structures and seed data
    public AppModel() {
        this.recipes = new ArrayList<>();
        this.historyQueue = new LinkedList<>();
        this.cookedCount = 0;
        this.requestCount = 0;
        
        seedDummyRecipes();
    }

    // ===== Recipe CRUD Operations =====
    
    public void addRecipe(RecipeData recipe) {
        recipes.add(recipe);
    }

    public List<RecipeData> getAllRecipes() {
        return new ArrayList<>(recipes);
    }

    public RecipeData getRecipeById(int id) {
        for (RecipeData recipe : recipes) {
            if (recipe.getId() == id) {
                return recipe;
            }
        }
        return null;
    }

    public boolean updateRecipe(int id, String title, String cuisine, String difficulty,
                               int prepTime, double rating, String imagePath,
                               String ingredients, String process) {
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

    public boolean deleteRecipe(int id) {
        return recipes.removeIf(r -> r.getId() == id);
    }

    public List<RecipeData> getRecentlyAdded(int count) {
        int size = recipes.size();
        int fromIndex = Math.max(0, size - count);
        return new ArrayList<>(recipes.subList(fromIndex, size));
    }

    // ===== Manual Request Queue (Array-based) =====

    private boolean isRequestQueueFull() {
        return front == 0 && rear == REQUEST_QUEUE_SIZE - 1;
    }

    private boolean isRequestQueueEmpty() {
        return front == -1;
    }

    // enqueue
    private boolean enQueueRequest(RecipeRequest req) {
        if (isRequestQueueFull()) {
            System.out.println("Request queue is full");
            return false;
        }
        if (front == -1) {
            front = 0;
        }
        rear++;
        requestItems[rear] = req;
        return true;
    }

    // dequeue (front)
    public RecipeRequest pollNextRequest() {
        if (isRequestQueueEmpty()) {
            System.out.println("Request queue is empty");
            return null;
        }

        RecipeRequest element = requestItems[front];

        if (front >= rear) {
            // only one element, reset
            front = -1;
            rear = -1;
        } else {
            front++;
        }
        return element;
    }

    // remove specific request (by reference)
    public boolean removeRequest(RecipeRequest target) {
        if (isRequestQueueEmpty()) return false;

        int index = -1;
        for (int i = front; i <= rear; i++) {
            if (requestItems[i] == target) { 
                index = i;
                break;
            }
        }
        if (index == -1) return false;

        // shift left
        for (int i = index; i < rear; i++) {
            requestItems[i] = requestItems[i + 1];
        }
        requestItems[rear] = null;
        rear--;

        if (rear < front) {
            front = -1;
            rear = -1;
        }
        return true;
    }

    // read-only snapshot for controller/view
    public RecipeRequest[] getAllRequestsArray() {
        if (isRequestQueueEmpty()) return new RecipeRequest[0];
        int size = rear - front + 1;
        RecipeRequest[] arr = new RecipeRequest[size];
        for (int i = 0; i < size; i++) {
            arr[i] = requestItems[front + i];
        }
        return arr;
    }

    public boolean addRequest(RecipeRequest request) {
        return enQueueRequest(request);
    }
    // ===== Request Queue Operations =====
    

    // ===== History Operations =====
    
    public void addToHistory(RecipeData recipe) {
        // Remove if already exists
        historyQueue.removeIf(item -> item.getId() == recipe.getId());
        
        // Add to front
        historyQueue.addFirst(recipe);
        
        // Maintain limit
        if (historyQueue.size() > HISTORY_LIMIT) {
            historyQueue.removeLast();
        }
    }

    public List<RecipeData> getHistory() {
        return new ArrayList<>(historyQueue);
    }

    public void clearHistory() {
        historyQueue.clear();
    }

    // ===== Statistics Getters/Setters =====
    
    public int getCookedCount() {
        return cookedCount;
    }

    public void incrementCookedCount() {
        cookedCount++;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void incrementRequestCount() {
        requestCount++;
    }

    public int getTotalRecipes() {
        return recipes.size();
    }

    public int getYetToCookCount() {
        return getTotalRecipes() - cookedCount;
    }

    // ===== Data Seeding =====
    
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

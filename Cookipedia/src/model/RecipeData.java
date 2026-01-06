/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Acer
 */
public class RecipeData {
    private int id;
    private String title;
    private String cuisine;
    private String type;
    private String difficulty;
    private int prepTime;
    private double rating;
    private String imagePath;
    private String ingredients;
    private String process;

    private static int idCounter = 1;

    // Constructor
    public RecipeData(String title, String cuisine, String difficulty, 
                     int prepTime, double rating, String imagePath, 
                     String ingredients, String process) {
        this.id = idCounter++;
        this.title = title;
        this.cuisine = cuisine;
        this.type = "";
        this.difficulty = difficulty;
        this.prepTime = prepTime;
        this.rating = rating;
        this.imagePath = imagePath;
        this.ingredients = ingredients;
        this.process = process;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    @Override
    public String toString() {
        return "RecipeData{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", cuisine='" + cuisine + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", prepTime=" + prepTime +
                ", rating=" + rating +
                '}';
    }
}


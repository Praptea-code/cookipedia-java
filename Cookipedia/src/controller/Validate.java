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

    /*
    this constructor links validation to the app model so it can see existing recipes
    it takes an appmodel instance used later to check for duplicate recipes
    it simply stores this reference in a field
    */
    public Validate(AppModel model) {
        this.model = model;
    }

    /*
    this method validates login inputs and decides user type or error
    it takes username and password strings provided by the login form
    it trims them then checks for empty values and compares against fixed admin and user credentials
    it returns "admin" or "user" for correct login or a detailed error message for different invalid cases
    */
    public String login(String username, String password) {
        if (username == null || password == null) {
            return "Please enter username and password.";
        }

        String u = username.trim();
        String p = password.trim();

        if (u.length() == 0 && p.length() == 0) {
            return "Please enter username and password.";
        }
        if (u.length() == 0) {
            return "Please enter your username.";
        }
        if (p.length() == 0) {
            return "Please enter your password.";
        }

        if (u.equals("admin") && p.equals("12345")) {
            return "admin";
        } else if (u.equals("user") && p.equals("67890")) {
            return "user";
        } else {
            return "Invalid username or password!";
        }
    }

    /*
    this method checks all recipe fields carefully before saving a new recipe
    it takes title cuisine difficulty prepTime string and rating string from the form
    it verifies each field is not empty checks difficulty matches allowed words checks duplicates in model and ensures prepTime and rating follow numeric rules
    it returns "valid" when all conditions pass or an error message string describing the first problem it finds
    */
    public String recipeFields(String title, String cuisine, String difficulty,String prepTime, String rating) {

        if (title == null || title.trim().length() == 0) {
            return "Recipe title cannot be empty!";
        }

        if (cuisine == null || cuisine.trim().length() == 0) {
            return "Cuisine type cannot be empty!";
        }

        if (difficulty == null || difficulty.trim().length() == 0) {
            return "Difficulty level cannot be empty!";
        }

        String t = title.trim();
        String c = cuisine.trim();
        String d = difficulty.trim();

        String dLower = d.toLowerCase();
        if (!dLower.equals("easy") && !dLower.equals("medium") && !dLower.equals("hard")) {
            return "Difficulty must be Easy, Medium, or Hard!";
        }

        List<RecipeData> all = model.getAllRecipes();
        int i = 0;
        while (i < all.size()) {
            RecipeData r = all.get(i);
            String rt = r.getTitle();
            String rc = r.getCuisine();
            String rd = r.getDifficulty();
            if (rt == null) {
                rt = "";
            }
            if (rc == null) {
                rc = "";
            }
            if (rd == null) {
                rd = "";
            }
            if (rt.equalsIgnoreCase(t) &&
                rc.equalsIgnoreCase(c) &&
                rd.equalsIgnoreCase(d)) {
                return "A recipe with this title, cuisine, and difficulty already exists!";
            }
            i = i + 1;
        }

        if (prepTime == null || prepTime.trim().length() == 0) {
            return "Preparation time cannot be empty!";
        }

        String pt = prepTime.trim();
        int index = 0;
        while (index < pt.length()) {
            char ch = pt.charAt(index);
            if (ch < '0' || ch > '9') {
                return "Preparation time must be a valid number! Please enter only digits.";
            }
            index = index + 1;
        }

        int time;
        try {
            time = Integer.parseInt(pt);
        } catch (NumberFormatException e) {
            return "Preparation time must be a valid number! Please enter only digits.";
        }

        if (time <= 0) {
            return "Preparation time must be a positive number!";
        }
        if (time > 1440) {
            return "Preparation time seems unreasonably long (max 1440 minutes)!";
        }

        if (rating == null || rating.trim().length() == 0) {
            return "Rating cannot be empty!";
        }

        String rstr = rating.trim();
        int dotCount = 0;
        int pos = 0;
        while (pos < rstr.length()) {
            char ch = rstr.charAt(pos);
            if (ch == '.') {
                dotCount = dotCount + 1;
                if (dotCount > 1) {
                    return "Rating must be a valid decimal number (e.g., 4.5)!";
                }
            } else {
                if (ch < '0' || ch > '9') {
                    return "Rating must be a valid decimal number (e.g., 4.5)!";
                }
            }
            pos = pos + 1;
        }

        double rate;
        try {
            rate = Double.parseDouble(rstr);
        } catch (NumberFormatException e) {
            return "Rating must be a valid decimal number (e.g., 4.5)!";
        }

        if (rate < 0.0 || rate > 5.0) {
            return "Rating must be between 0.0 and 5.0!";
        }

        return "valid";
    }

    /*
    this method confirms if a given status word is acceptable for a request
    it takes status string which is expected to be pending updated or cancelled
    it trims and lowers it and compares it against the three allowed options
    it returns true only when it matches one of the allowed values and false otherwise
    */
    public boolean status(String status) {
        if (status == null) {
            return false;
        }
        String s = status.trim().toLowerCase();
        if (s.equals("pending")) {
            return true;
        }
        if (s.equals("updated")) {
            return true;
        }
        if (s.equals("cancelled")) {
            return true;
        }
        return false;
    }

    /*
    this method locates a request object from a list using username and title
    it takes list of requests and two strings username and title that identify the record
    it loops through the list and returns the first request whose username and title both match or null when there is no such entry
    */
    public RecipeRequest findRequest(List<RecipeRequest> list,String username, String title) {
        if (username == null || title == null) {
            return null;
        }
        int i = 0;
        while (i < list.size()) {
            RecipeRequest r = list.get(i);
            if (r.getUsername().equals(username) && r.getTitle().equals(title)) {
                return r;
            }
            i = i + 1;
        }
        return null;
    }

    /*
    this method checks if a bounded array queue is currently full
    it takes current size and capacity of the queue as integers usually from model
    it compares them and returns true when they are equal meaning no more elements can be added
    */
    public boolean isQueueFull(int size, int capacity) {
        if (size >= capacity) {
            return true;
        }
        return false;
    }

    /*
    this method checks if a bounded array queue is currently empty
    it takes current size of the queue as integer usually from model
    it returns true when size is zero meaning there are no elements to remove
    */
    public boolean isQueueEmpty(int size) {
        if (size == 0) {
            return true;
        }
        return false;
    }
}

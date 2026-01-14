/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import java.util.ArrayList;
import java.util.List;
import model.RecipeData;
/**
 *
 * @author Acer
 */
public class LinearSearchTitle {
    /*
    this method finds recipes whose title contains a given piece of text
    it takes full recipe list and a query string typed by the user in search bar
    it converts both title and query to lowercase and uses indexOf to check partial matches in a linear scan
    it returns all recipes that contain that query anywhere inside their title
    */
    public List<RecipeData> byTitle(List<RecipeData> all, String query) {
        if (query == null) {
            return all;
        }

        String trimmed = query.trim();
        if (trimmed.length() == 0) {
            return all;
        }

        String lowerQuery = trimmed.toLowerCase();
        List<RecipeData> results = new ArrayList<RecipeData>();

        int i = 0;
        while (i < all.size()) {
            RecipeData recipe = all.get(i);
            String title = recipe.getTitle();
            if (title == null) {
                title = "";
            }
            String lowerTitle = title.toLowerCase();

            if (lowerTitle.indexOf(lowerQuery) >= 0) {
                results.add(recipe);
            }
            i = i + 1;
        }
        return results;
    }
}

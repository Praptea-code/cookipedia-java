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
public class BinarySearchCuisine {
    /*
    this method finds recipes that have exactly the same cuisine text
    it takes a list of recipes already sorted by cuisine and a cuisine string to search for
    it runs a binary search to find one matching index then expands left and right to collect all recipes with that cuisine
    it returns a list of all recipes whose cuisine string equals the given target ignoring case
    */
    public List<RecipeData> byCuisine(List<RecipeData> sortedByCuisine, String cuisine) {
        if (cuisine == null) {
            return sortedByCuisine;
        }

        String trimmed = cuisine.trim();
        if (trimmed.length() == 0) {
            return sortedByCuisine;
        }

        String target = trimmed.toLowerCase();
        List<RecipeData> results = new ArrayList<RecipeData>();

        int index = binarySearchCuisine(sortedByCuisine, target);
        if (index == -1) {
            return results;
        }

        int left = index;
        while (left >= 0) {
            RecipeData r = sortedByCuisine.get(left);
            String c = r.getCuisine();
            if (c == null) {
                c = "";
            }
            String lowerC = c.toLowerCase();
            if (lowerC.equals(target)) {
                results.add(0, r);
                left = left - 1;
            } else {
                break;
            }
        }

        int right = index + 1;
        while (right < sortedByCuisine.size()) {
            RecipeData r = sortedByCuisine.get(right);
            String c = r.getCuisine();
            if (c == null) {
                c = "";
            }
            String lowerC = c.toLowerCase();
            if (lowerC.equals(target)) {
                results.add(r);
                right = right + 1;
            } else {
                break;
            }
        }

        return results;
    }

    /*
    this method is a basic binary search for cuisine names
    it takes a list sorted by cuisine in ascending order and a targetCuisine in lowercase
    it repeatedly checks the middle element and moves the search range left or right until it finds a match or the range is empty and returns index or minus one
    */
    private int binarySearchCuisine(List<RecipeData> recipes, String targetCuisine) {
        int left = 0;
        int right = recipes.size() - 1;

        while (left <= right) {
            int mid = (left + right) / 2;
            RecipeData midRecipe = recipes.get(mid);
            String midCuisine = midRecipe.getCuisine();
            if (midCuisine == null) {
                midCuisine = "";
            }
            String lowerMid = midCuisine.toLowerCase();

            int cmp = lowerMid.compareTo(targetCuisine);

            if (cmp == 0) {
                return mid;
            } else if (cmp < 0) {
                left = mid + 1;
            } else {
                right = right - 1;
            }
        }
        return -1;
    }
}

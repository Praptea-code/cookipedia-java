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
public class Search {
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
    this method performs a flexible search using title cuisine and difficulty at the same time
    it takes full recipe list and three filter strings which may be null or empty
    it checks each recipe in a single loop and only keeps those that match every filter the user actually provided
    it returns a list of recipes that satisfy the chosen combinations like only difficulty hard or title and cuisine together
    */
    public List<RecipeData> multi(List<RecipeData> all,String title, String cuisine, String difficulty) {

        List<RecipeData> results = new ArrayList<RecipeData>();

        boolean useTitle = false;
        boolean useCuisine = false;
        boolean useDifficulty = false;

        String lt = "";
        String lc = "";
        String ld = "";

        if (title != null) {
            String t = title.trim();
            if (t.length() > 0) {
                useTitle = true;
                lt = t.toLowerCase();
            }
        }

        if (cuisine != null) {
            String c = cuisine.trim();
            if (c.length() > 0) {
                useCuisine = true;
                lc = c.toLowerCase();
            }
        }

        if (difficulty != null) {
            String d = difficulty.trim();
            if (d.length() > 0) {
                useDifficulty = true;
                ld = d.toLowerCase();
            }
        }

        int i = 0;
        while (i < all.size()) {
            RecipeData r = all.get(i);
            boolean ok = true;

            if (useTitle) {
                String t = r.getTitle();
                if (t == null) {
                    t = "";
                }
                String ltRecipe = t.toLowerCase();
                if (ltRecipe.indexOf(lt) < 0) {
                    ok = false;
                }
            }

            if (ok && useCuisine) {
                String c = r.getCuisine();
                if (c == null) {
                    c = "";
                }
                String lcRecipe = c.toLowerCase();
                if (lcRecipe.indexOf(lc) < 0) {
                    ok = false;
                }
            }

            if (ok && useDifficulty) {
                String d = r.getDifficulty();
                if (d == null) {
                    d = "";
                }
                String ldRecipe = d.toLowerCase();
                if (!ldRecipe.equals(ld)) {
                    ok = false;
                }
            }

            if (ok) {
                results.add(r);
            }

            i = i + 1;
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

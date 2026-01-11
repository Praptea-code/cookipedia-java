/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import java.util.ArrayList;
import java.util.List;
import model.RecipeData;
import model.RecipeRequest;
/**
 *
 * @author Acer
 */
public class Sort {


    /*
    this method sorts recipes by title from a to z using merge sort
    it takes a list of recipedata objects to sort by their title field
    it copies that list into a new list then runs merge sort by comparing titles ignoring case
    it returns a new list where recipes are arranged in ascending alphabetical order
    */
    public List<RecipeData> sortByNameAsc(List<RecipeData> recipes) {
        List<RecipeData> list = new ArrayList<RecipeData>();
        int i = 0;
        while (i < recipes.size()) {
            list.add(recipes.get(i));
            i = i + 1;
        }

        if (list.size() > 1) {
            mergeSortByName(list, 0, list.size() - 1);
        }
        return list;
    }

    /*
    this method sorts recipes by title from z to a
    it takes a list of recipedata objects to sort by title
    it first calls sortByNameAsc to get a to z then manually reverses that list
    it returns a new list where the first recipe has the last title alphabetically
    */
    public List<RecipeData> sortByNameDesc(List<RecipeData> recipes) {
        List<RecipeData> asc = sortByNameAsc(recipes);
        List<RecipeData> reversed = new ArrayList<RecipeData>();
        int i = asc.size() - 1;
        while (i >= 0) {
            reversed.add(asc.get(i));
            i = i - 1;
        }
        return reversed;
    }

    /*
    this method sorts recipes by cuisine name from a to z using selection sort
    it takes a list of recipedata objects that have a cuisine field
    it repeatedly finds the smallest cuisine string in the unsorted part and swaps it to the front
    it returns a new list where recipes are ordered from earliest cuisine text to latest cuisine text
    */
    public List<RecipeData> sortByCuisineName(List<RecipeData> recipes) {
        List<RecipeData> list = new ArrayList<RecipeData>();
        int i = 0;
        while (i < recipes.size()) {
            list.add(recipes.get(i));
            i = i + 1;
        }

        int n = list.size();
        int pos = 0;
        while (pos < n - 1) {
            int minIndex = pos;
            int j = pos + 1;
            while (j < n) {
                RecipeData rMin = list.get(minIndex);
                RecipeData rJ = list.get(j);
                String cMin = rMin.getCuisine();
                String cJ = rJ.getCuisine();

                if (cMin == null) {
                    cMin = "";
                }
                if (cJ == null) {
                    cJ = "";
                }

                int cmp = cJ.compareToIgnoreCase(cMin);
                if (cmp < 0) {
                    minIndex = j;
                }
                j = j + 1;
            }
            if (minIndex != pos) {
                RecipeData temp = list.get(pos);
                list.set(pos, list.get(minIndex));
                list.set(minIndex, temp);
            }
            pos = pos + 1;
        }

        return list;
    }

    /*
    this method sorts recipes by preparation time from lowest to highest using selection sort
    it takes a list of recipedata objects that have prepTime integer
    it searches for the recipe with smallest prepTime in the unsorted part and swaps it to the front until the list is sorted
    it returns a new list where recipes appear in order of increasing cooking time
    */
    public List<RecipeData> sortByTimeAsc(List<RecipeData> recipes) {
        List<RecipeData> list = new ArrayList<RecipeData>();
        int i = 0;
        while (i < recipes.size()) {
            list.add(recipes.get(i));
            i = i + 1;
        }

        int n = list.size();
        int pos = 0;
        while (pos < n - 1) {
            int minIndex = pos;
            int j = pos + 1;
            while (j < n) {
                int timeJ = list.get(j).getPrepTime();
                int timeMin = list.get(minIndex).getPrepTime();
                if (timeJ < timeMin) {
                    minIndex = j;
                }
                j = j + 1;
            }
            if (minIndex != pos) {
                RecipeData temp = list.get(pos);
                list.set(pos, list.get(minIndex));
                list.set(minIndex, temp);
            }
            pos = pos + 1;
        }
        return list;
    }

    /*
    this method sorts recipes by rating from highest to lowest using merge sort
    it takes a list of recipedata objects with rating double field
    it copies that list and runs mergeSortByRating which always keeps larger ratings earlier during merge
    it returns a new list where first recipe has highest rating and last one has lowest rating
    */
    public List<RecipeData> sortByRatingDesc(List<RecipeData> recipes) {
        List<RecipeData> list = new ArrayList<RecipeData>();
        int i = 0;
        while (i < recipes.size()) {
            list.add(recipes.get(i));
            i = i + 1;
        }

        if (list.size() > 1) {
            mergeSortByRating(list, 0, list.size() - 1);
        }
        return list;
    }

    /*
    this method sorts recipe requests by title from a to z using insertion sort
    it takes a list of reciperequest objects that have a title
    it walks from left to right and inserts each request into the correct place among already sorted part on the left
    it returns a new list where earlier elements have titles that come first alphabetically
    */
    public List<RecipeRequest> sortRequestsByNameAsc(List<RecipeRequest> requests) {
        List<RecipeRequest> list = new ArrayList<RecipeRequest>();
        int i = 0;
        while (i < requests.size()) {
            list.add(requests.get(i));
            i = i + 1;
        }

        int n = list.size();
        int index = 1;
        while (index < n) {
            RecipeRequest key = list.get(index);
            int j = index - 1;

            while (j >= 0) {
                RecipeRequest rJ = list.get(j);
                String titleJ = rJ.getTitle();
                String titleKey = key.getTitle();

                if (titleJ == null) {
                    titleJ = "";
                }
                if (titleKey == null) {
                    titleKey = "";
                }

                int cmp = titleJ.compareToIgnoreCase(titleKey);
                if (cmp > 0) {
                    list.set(j + 1, rJ);
                    j = j - 1;
                } else {
                    break;
                }
            }
            list.set(j + 1, key);
            index = index + 1;
        }
        return list;
    }

    /*
    this method sorts recipe requests by title from z to a using insertion sort
    it takes a list of reciperequest objects that have a title
    it walks through the list and inserts each request in reverse alphabetical order in the sorted portion
    it returns a new list where first elements have titles later in the alphabet
    */
    public List<RecipeRequest> sortRequestsByNameDesc(List<RecipeRequest> requests) {
        List<RecipeRequest> list = new ArrayList<RecipeRequest>();
        int i = 0;
        while (i < requests.size()) {
            list.add(requests.get(i));
            i = i + 1;
        }

        int n = list.size();
        int index = 1;
        while (index < n) {
            RecipeRequest key = list.get(index);
            int j = index - 1;

            while (j >= 0) {
                RecipeRequest rJ = list.get(j);
                String titleJ = rJ.getTitle();
                String titleKey = key.getTitle();

                if (titleJ == null) {
                    titleJ = "";
                }
                if (titleKey == null) {
                    titleKey = "";
                }

                int cmp = titleJ.compareToIgnoreCase(titleKey);
                if (cmp < 0) {
                    list.set(j + 1, rJ);
                    j = j - 1;
                } else {
                    break;
                }
            }
            list.set(j + 1, key);
            index = index + 1;
        }
        return list;
    }

    /*
    this method is the recursive part of merge sort for titles
    it takes a list and two indexes left and right that define which portion to sort
    it splits that range into two halves sorts them separately and then merges them back in order
    */
    private void mergeSortByName(List<RecipeData> recipes, int left, int right) {
        if (left >= right) {
            return;
        }
        int mid = (left + right) / 2;
        mergeSortByName(recipes, left, mid);
        mergeSortByName(recipes, mid + 1, right);
        mergeByName(recipes, left, mid, right);
    }

    /*
    this method merges two sorted sublists by title that sit next to each other
    it takes the list and boundaries left mid and right so left..mid and mid+1..right are already sorted
    it creates temporary left and right lists and then writes back the smaller title each time until merged
    */
    private void mergeByName(List<RecipeData> recipes, int left, int mid, int right) {
        List<RecipeData> leftList = new ArrayList<RecipeData>();
        List<RecipeData> rightList = new ArrayList<RecipeData>();

        int i = left;
        while (i <= mid) {
            leftList.add(recipes.get(i));
            i = i + 1;
        }
        int j = mid + 1;
        while (j <= right) {
            rightList.add(recipes.get(j));
            j = j + 1;
        }

        int indexLeft = 0;
        int indexRight = 0;
        int indexMain = left;

        while (indexLeft < leftList.size() && indexRight < rightList.size()) {
            RecipeData leftRecipe = leftList.get(indexLeft);
            RecipeData rightRecipe = rightList.get(indexRight);

            String leftTitle = leftRecipe.getTitle();
            String rightTitle = rightRecipe.getTitle();

            if (leftTitle == null) {
                leftTitle = "";
            }
            if (rightTitle == null) {
                rightTitle = "";
            }

            int cmp = leftTitle.compareToIgnoreCase(rightTitle);
            if (cmp <= 0) {
                recipes.set(indexMain, leftRecipe);
                indexLeft = indexLeft + 1;
            } else {
                recipes.set(indexMain, rightRecipe);
                indexRight = indexRight + 1;
            }
            indexMain = indexMain + 1;
        }

        while (indexLeft < leftList.size()) {
            recipes.set(indexMain, leftList.get(indexLeft));
            indexLeft = indexLeft + 1;
            indexMain = indexMain + 1;
        }

        while (indexRight < rightList.size()) {
            recipes.set(indexMain, rightList.get(indexRight));
            indexRight = indexRight + 1;
            indexMain = indexMain + 1;
        }
    }

    /*
    this method is the recursive part of merge sort for ratings
    it takes list and two indexes left and right that describe the range to sort
    it halves that range and calls itself then merges the halves so that larger ratings come first
    */
    private void mergeSortByRating(List<RecipeData> recipes, int left, int right) {
        if (left >= right) {
            return;
        }
        int mid = (left + right) / 2;
        mergeSortByRating(recipes, left, mid);
        mergeSortByRating(recipes, mid + 1, right);
        mergeByRating(recipes, left, mid, right);
    }

    /*
    this method merges two sorted sublists by rating in descending order
    it takes list and three indexes left mid and right where left..mid and mid+1..right are already sorted by rating
    it always picks the larger rating from left or right list and writes it into the main list first
    */
    private void mergeByRating(List<RecipeData> recipes, int left, int mid, int right) {
        List<RecipeData> leftList = new ArrayList<RecipeData>();
        List<RecipeData> rightList = new ArrayList<RecipeData>();

        int i = left;
        while (i <= mid) {
            leftList.add(recipes.get(i));
            i = i + 1;
        }
        int j = mid + 1;
        while (j <= right) {
            rightList.add(recipes.get(j));
            j = j + 1;
        }

        int indexLeft = 0;
        int indexRight = 0;
        int indexMain = left;

        while (indexLeft < leftList.size() && indexRight < rightList.size()) {
            double leftRating = leftList.get(indexLeft).getRating();
            double rightRating = rightList.get(indexRight).getRating();

            if (leftRating >= rightRating) {
                recipes.set(indexMain, leftList.get(indexLeft));
                indexLeft = indexLeft + 1;
            } else {
                recipes.set(indexMain, rightList.get(indexRight));
                indexRight = indexRight + 1;
            }
            indexMain = indexMain + 1;
        }

        while (indexLeft < leftList.size()) {
            recipes.set(indexMain, leftList.get(indexLeft));
            indexLeft = indexLeft + 1;
            indexMain = indexMain + 1;
        }

        while (indexRight < rightList.size()) {
            recipes.set(indexMain, rightList.get(indexRight));
            indexRight = indexRight + 1;
            indexMain = indexMain + 1;
        }
    }
}

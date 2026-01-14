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
public class MergeSort {
    /*
    this method sorts recipes by title from a to z using merge sort
    it takes full recipe list from the model and sorts by the title field
    it is used in appcontroller to show recipes and history in alphabetical order
    */
    public List<RecipeData> mergeSortByNameAsc(List<RecipeData> recipes) {
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
    this method sorts recipes by title from z to a using merge sort
    it first calls mergeSortByNameAsc to get a to z order then reverses the list
    it is used in appcontroller when admin wants recipes sorted from z to a
    */
    public List<RecipeData> mergeSortByNameDesc(List<RecipeData> recipes) {
        List<RecipeData> asc = mergeSortByNameAsc(recipes);
        List<RecipeData> reversed = new ArrayList<RecipeData>();

        int i = asc.size() - 1;
        while (i >= 0) {
            reversed.add(asc.get(i));
            i = i - 1;
        }
        return reversed;
    }

    /*
    this method sorts recipes by rating from highest to lowest using merge sort
    it takes full recipe list from the model and compares the rating field
    it is used in appcontroller to show best rated recipes or history first
    */
    public List<RecipeData> mergeSortByRatingDesc(List<RecipeData> recipes) {
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
    this method is the recursive part of merge sort for recipe titles
    it divides the current range into two halves sorts them and then merges them back
    it is only used internally by mergeSortByNameAsc and mergeSortByNameDesc
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
    it assumes left..mid and mid+1..right are already sorted alphabetically by title
    it is only used internally to complete merge sort on recipe titles
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
    it splits the range by middle index and sorts both halves then merges them
    it is only used internally by mergeSortByRatingDesc
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
    it assumes left..mid and mid+1..right are already sorted from highest rating to lowest
    it is only used internally to complete merge sort on recipe ratings
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

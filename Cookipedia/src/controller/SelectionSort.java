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
public class SelectionSort {
    /*
    this method sorts recipes by cuisine name from a to z using selection sort
    it makes a copy of the recipe list and repeatedly selects the smallest cuisine text
    it is used in appcontroller before running binary search on cuisines for search
    */
    public List<RecipeData> selectionSortByCuisineName(List<RecipeData> recipes) {
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
    it makes a copy of the recipe list and selects the smallest prep time into front step by step
    it is used in appcontroller to show recipes and history from quickest to slowest
    */
    public List<RecipeData> selectionSortByTimeAsc(List<RecipeData> recipes) {
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
}

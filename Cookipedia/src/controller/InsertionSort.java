/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import java.util.ArrayList;
import java.util.List;
import model.RecipeRequest;
/**
 *
 * @author Acer
 */
public class InsertionSort {
    /*
    this method sorts recipe requests by title from a to z using insertion sort
    it walks from left to right and inserts each request into correct position in sorted part
    it is used in appcontroller to show admin request table sorted a to z by title
    */
    public List<RecipeRequest> insertionSortByNameAsc(List<RecipeRequest> requests) {
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
    it scans requests and inserts each one into the sorted part in reverse alphabetical order
    it is used in appcontroller to show admin request table sorted z to a by title
    */
    public List<RecipeRequest> insertionSortByNameDesc(List<RecipeRequest> requests) {
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
}

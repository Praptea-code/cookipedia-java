/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.RecipeData;

/**
 *
 * @author Acer
 */
public class RecentlyAddedQueue {
    private final RecipeData[] items; 
    private final int capacity;       
    private int size;                

    /*
     * this constructor creates a new empty queue with given capacity
     * it allocates the array and sets size to zero
     * it is useful for storing a small recent list for home panel
     */
    public RecentlyAddedQueue(int capacity) {
        this.capacity = capacity;
        this.items = new RecipeData[capacity];
        this.size = 0;
    }

    /*
     * this method adds a recipe at the rear end of the queue
     * it takes a recipedata object and appends it at the end
     * when the queue is full it removes the oldest recipe at index 0 by shifting
     * it returns true when the recipe is stored successfully or false when recipe is null
     */
    public boolean enqueue(RecipeData recipe) {
        if (recipe == null) {
            return false;
        }

        // when full, drop the oldest by shifting everything one step to left
        if (isFull()) {
            // shift items
            int i = 1;
            while (i < size) {
                items[i - 1] = items[i];
                i = i + 1;
            }
            // now size stays same but last position will be overwritten below
            size = size - 1;
        }

        items[size] = recipe;
        size = size + 1;
        return true;
    }

    /*
     * this method removes and returns the recipe at the front of the queue
     * it follows fifo rule by always deleting the oldest recipe at index 0
     * it returns the removed recipe or null when the queue is empty
     */
    public RecipeData dequeue() {
        if (isEmpty()) {
            return null;
        }

        RecipeData removed = items[0];

        // shift remaining elements one position to the left
        int i = 1;
        while (i < size) {
            items[i - 1] = items[i];
            i = i + 1;
        }

        items[size - 1] = null;
        size = size - 1;
        return removed;
    }

    /*
     * this method removes a recipe with a given id from the queue
     * it scans the array and when id matches it shifts later items to fill the gap
     * it returns true when a recipe is found and removed or false when id is not present
     */
    public boolean removeById(int recipeId) {
        if (isEmpty()) {
            return false;
        }

        int index = 0;
        boolean found = false;

        //finding the index of the recipe to delete
        while (index < size) {
            RecipeData current = items[index];
            if (current != null && current.getId() == recipeId) {
                found = true;
                break;
            }
            index = index + 1;
        }

        if (!found) {
            return false;
        }

        // shifting elements after index to the left
        int i = index + 1;
        while (i < size) {
            items[i - 1] = items[i];
            i = i + 1;
        }

        items[size - 1] = null;
        size = size - 1;
        return true;
    }

    /*
     * this method checks if the queue is empty
     * it returns true when size is zero
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /*
     * this method checks if the queue is full
     * it returns true when size equals capacity
     */
    public boolean isFull() {
        return size == capacity;
    }

    /*
     * this method returns the current number of recipes stored
     * it is useful for stats or debugging
     */
    public int getSize() {
        return size;
    }

    /*
     * this method returns the fixed capacity of this queue
     * it is useful for ui or validation
     */
    public int getCapacity() {
        return capacity;
    }

    /*
     * this method returns a snapshot array of all recipes in correct order
     * it creates a new array from index 0 to size minus one
     * it lets ui display recent recipes without modifying internal storage
     */
    public RecipeData[] toArray() {
        RecipeData[] result = new RecipeData[size];
        int i = 0;
        while (i < size) {
            result[i] = items[i];
            i = i + 1;
        }
        return result;
    }

    /*
     * this method clears all recipes from the queue
     * it sets all slots to null and resets size back to zero
     * it is useful when resetting recently added section
     */
    public void clear() {
        int i = 0;
        while (i < size) {
            items[i] = null;
            i = i + 1;
        }
        size = 0;
    }
}

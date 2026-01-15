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
    private final int capacity;
    private final RecipeData[] items;
    private int front;  //index of most recent recipe
    private int rear;   //index of oldest recipe

    /*
     * this constructor creates an empty recently added queue with fixed capacity
     * it sets front and rear to -1 to show that the queue is empty at the start
     */
    public RecentlyAddedQueue(int capacity) {
        this.capacity = capacity;
        this.items = new RecipeData[capacity];
        this.front = -1;
        this.rear = -1;
    }

    /*
     * this method checks whether the queue is empty
     * it returns true when front is -1
     */
    public boolean isEmpty() {
        return front == -1;
    }

    /*
     * this method checks whether the queue is full
     * it returns true when rear is at last index and front is 0
     */
    public boolean isFull() {
        return front == 0 && rear == capacity - 1;
    }

    /*
     * this method adds a recipe at the front side (index 0) of recently added queue
     * when empty it sets front and rear to 0 and inserts element
     * when not empty it shifts all existing elements one step to the right,
     * writes new element at index 0 and updates rear
     * when full it first removes the oldest element at rear by shifting elements left,
     * then inserts new recipe at front
     * it returns true when insert succeeds or false when recipe is null
     */
    public boolean enqueue(RecipeData recipe) {
        if (recipe == null) {
            return false;
        }

        // if full, drop the oldest (rear) by shifting left once
        if (isFull()) {
            int i = rear - 1;
            while (i >= front) {
                items[i + 1] = items[i];
                i = i - 1;
            }
            // after this, front stays 0 and rear stays capacity-1,
            // but the oldest at rear is overwritten by shift
        }

        if (isEmpty()) {
            front = 0;
            rear = 0;
            items[0] = recipe;
            return true;
        }

        // shift all elements one step to the right
        int i = rear;
        while (i >= front) {
            items[i + 1] = items[i];
            i = i - 1;
        }
        items[front] = recipe;
        rear = rear + 1;
        return true;
    }

    /*
     * this method removes and returns the recipe at the back side (rear) of the queue
     * it always deletes the oldest recipe
     * it returns null when queue is empty
     */
    public RecipeData dequeue() {
        if (isEmpty()) {
            return null;
        }

        RecipeData element = items[rear];
        items[rear] = null;

        if (front >= rear) {
            front = -1;
            rear = -1;
        } else {
            rear = rear - 1;
        }
        return element;
    }

    /*
     * this method removes a recipe with a given id from the queue
     * it scans from front to rear, when id matches it shifts later items left
     * it returns true when a recipe is found and removed or false when id is not present
     */
    public boolean removeById(int recipeId) {
        if (isEmpty()) {
            return false;
        }

        int index = front;
        boolean found = false;

        while (index <= rear) {
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

        // shift elements after index to the left
        int i = index + 1;
        while (i <= rear) {
            items[i - 1] = items[i];
            i = i + 1;
        }

        items[rear] = null;
        rear = rear - 1;

        if (rear < front) {
            front = -1;
            rear = -1;
        }

        return true;
    }

    /*
     * this method returns the current number of recipes stored
     */
    public int getSize() {
        if (isEmpty()) {
            return 0;
        }
        return rear - front + 1;
    }

    /*
     * this method returns the fixed capacity of this queue
     */
    public int getCapacity() {
        return capacity;
    }

    /*
     * this method returns a snapshot array of all recipes from front to rear
     * index 0 of result is the most recent recipe
     */
    public RecipeData[] toArray() {
        if (isEmpty()) {
            return new RecipeData[0];
        }
        int size = rear - front + 1;
        RecipeData[] arr = new RecipeData[size];
        int i = 0;
        while (i < size) {
            arr[i] = items[front + i];
            i = i + 1;
        }
        return arr;
    }

    /*
     * this method clears all recipes from the queue
     * it sets all active slots to null and resets front and rear
     */
    public void clear() {
        if (isEmpty()) {
            return;
        }
        int i = front;
        while (i <= rear) {
            items[i] = null;
            i = i + 1;
        }
        front = -1;
        rear = -1;
    }
}

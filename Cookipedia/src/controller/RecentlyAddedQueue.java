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
    this method checks whether the queue is empty
    it returns true when front is -1
    */
    public boolean isEmpty() {
        return front == -1;
    }

    /*
    this method checks whether the queue is full
    it returns true when front is 0 and rear is at last index
    */
    public boolean isFull() {
        return front == 0 && rear == capacity - 1;
    }

    /*
    this method inserts a new recipe at the rear end standard enqueue
    it takes recipedata object recipe that should be added
    it first checks overflow using isfull then sets front on first insert and moves rear and stores the recipe
    when full it drops oldest by moving front forward then adds new at rear
    it returns true when insert succeeds or false when recipe is null
    */
    public boolean enqueue(RecipeData recipe) {
        if (recipe == null) {
            return false;
        }

        if (isFull()) {
            // drop oldest: move front forward
            items[front] = null;
            front = front + 1;
        }

        if (isEmpty()) {
            front = 0;
            rear = 0;
            items[0] = recipe;
        } else {
            rear = rear + 1;
            items[rear] = recipe;
        }
        return true;
    }

    /*
    this method removes and returns the recipe at the front of the queue standard dequeue
    it always deletes the oldest recipe
    it returns null when queue is empty
    */
    public RecipeData dequeue() {
        if (isEmpty()) {
            return null;
        }

        RecipeData element = items[front];
        items[front] = null;

        if (front >= rear) {
            front = -1;
            rear = -1;
        } else {
            front = front + 1;
        }
        return element;
    }

    /*
    this method returns the current number of recipes stored
    */
    public int getSize() {
        if (isEmpty()) {
            return 0;
        }
        return rear - front + 1;
    }

    /*
    this method returns the fixed capacity of this queue
    */
    public int getCapacity() {
        return capacity;
    }

    /*
    this method returns a snapshot array of all recipes newest first for home panel display
    index 0 of result is the most recent recipe
    */
    public RecipeData[] toArray() {
        if (isEmpty()) {
            return new RecipeData[0];
        }
        int size = rear - front + 1;
        RecipeData[] arr = new RecipeData[size];
        int j = 0;
        int i = rear;
        while (i >= front) {
            arr[j] = items[i];
            j = j + 1;
            i = i - 1;
        }
        return arr;
    }

    /*
    this method clears all recipes from the queue
    it sets all active slots to null and resets front and rear
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

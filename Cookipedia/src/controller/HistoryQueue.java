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
public class HistoryQueue {
    private final int capacity;
    private final RecipeData[] items;
    private int front;
    private int rear;

    /*
    this constructor creates an empty history queue with fixed capacity
    it takes capacity as integer which tells maximum number of history items allowed
    it sets front and rear to -1 to show that the queue is empty at the start
    */
    public HistoryQueue(int capacity) {
        this.capacity = capacity;
        this.items = new RecipeData[capacity];
        this.front = -1;
        this.rear = -1;
    }

    /*
    this method checks whether the history queue is full
    it returns true when front is 0 and rear is at last index meaning there is no free space left
    */
    public boolean isFull() {
        return front == 0 && rear == capacity - 1;
    }

    /*
    this method checks whether the history queue is empty
    it returns true when front is -1 which is the empty flag
    */
    public boolean isEmpty() {
        return front == -1;
    }

    /*
    this method adds a recipe at the front side of the history queue
    it first checks overflow then when queue is empty it sets front and rear to 0 and inserts element
    when not empty it shifts all existing elements one step to the right and writes new element at index 0 and updates rear
    it returns true when insert succeeds or false when the queue is full
    */
    public boolean addFirst(RecipeData element) {
        if (isFull()) {
            System.out.println("history queue is full");
            return false;
        }
        if (isEmpty()) {
            front = 0;
            rear = 0;
            items[0] = element;
            return true;
        }

        int i = rear;
        while (i >= front) {
            items[i + 1] = items[i];
            i = i - 1;
        } 
        items[front] = element;
        rear = rear + 1;
        return true;
    }

    /*
    this method removes and returns the recipe at the back side of the history queue
    it always deletes from rear where the oldest recipe is stored
    it returns null when queue is empty or the last recipe when not empty and resets front and rear for last element
    */
    public RecipeData removeLast() {
        if (isEmpty()) {
            System.out.println("history queue is empty");
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
    this method copies all active recipes from front to rear into a new array
    it returns an empty array when queue is empty or an array with exact number of elements otherwise
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
}


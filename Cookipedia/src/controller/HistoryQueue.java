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
    this method inserts a new recipe at the rear end of the history queue standard enqueue
    it takes recipedata object element that should be added
    it first checks overflow using isfull then sets front on first insert and moves rear and stores the recipe and returns true
    */
    public boolean enqueue(RecipeData element) {
        if (isFull()) {
            System.out.println("history queue is full");
            return false;
        }
        if (isEmpty()) {
            front = 0;
            rear = 0;
            items[0] = element;
        } else {
            rear = rear + 1;
            items[rear] = element;
        }
        return true;
    }

    /*
    this method removes and returns the recipe at the front of the history queue standard dequeue
    it returns recipedata at front or null when queue is empty and resets front and rear back to -1 when last element is removed
    */
    public RecipeData dequeue() {
        if (isEmpty()) {
            System.out.println("history queue is empty");
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
    this method copies all active recipes from front to rear into a new array but reversed newest first
    it returns an empty array when queue is empty or an array of exact active size newest first when it has elements
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
}


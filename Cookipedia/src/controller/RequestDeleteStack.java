/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.RecipeRequest;

/**
 *
 * @author Acer
 */

public class RequestDeleteStack {
    private final int capacity;          //maximum number of deleted requests to store
    private final RecipeRequest[] items; //array to hold deleted requests
    private int top;                     //index of last pushed request -1 means empty

    /*
    this constructor creates a new request delete stack with given capacity
    it allocates the array and sets top to minus one to indicate empty stack
     */
    public RequestDeleteStack(int capacity) {
        this.capacity = capacity;
        this.items = new RecipeRequest[capacity];
        this.top = -1;
    }

    /*
    this method pushes a deleted request onto the stack
    it takes a reciperequest object to remember as deleted
    it returns true when push succeeds or false when the stack is already full
     */
    public boolean push(RecipeRequest req) {
        if (isFull() || req == null) {
            return false;
        }
        top = top + 1;
        items[top] = req;
        return true;
    }

    /*
    this method pops the most recently deleted request from the stack
     it returns the request at the top or null when the stack is empty
     */
    public RecipeRequest pop() {
        if (isEmpty()) {
            return null;
        }
        RecipeRequest req = items[top];
        items[top] = null;
        top = top - 1;
        return req;
    }

    /*
    this method checks if the stack is empty
    it returns true when top is minus one meaning no deleted requests are stored
     */
    public boolean isEmpty() {
        return top == -1;
    }

    /*
    this method checks if the stack is full
    it returns true when top has reached capacity minus one
     */
    public boolean isFull() {
        return top == capacity - 1;
    }

    /*
    this method returns the current number of deleted requests stored
    it adds one to top since top starts at minus one for empty
     */
    public int size() {
        return top + 1;
    }

    /*
    this method clears all deleted requests from the stack
    it loops from zero up to top and sets each position to null then resets top to minus one
     */
    public void clear() {
        int i = 0;
        while (i <= top) {
            items[i] = null;
            i = i + 1;
        }
        top = -1;
    }

    /*
    this method returns a snapshot array of deleted requests in stack order
    it creates and returns a new array from index zero to top so ui can display it safely
     */
    public RecipeRequest[] toArray() {
        int size = size();
        RecipeRequest[] result = new RecipeRequest[size];
        int i = 0;
        while (i < size) {
            result[i] = items[i];
            i = i + 1;
        }
        return result;
    }
}


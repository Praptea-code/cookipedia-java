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
public class RecipeDeleteStack {
    private final int capacity;
    private final RecipeData[] items;
    private int top;

    /*
    this constructor creates a new stack with given capacity
    it allocates an array to hold recipe items and sets top to minus one to indicate empty stack
    */
    public RecipeDeleteStack(int capacity) {
        this.capacity = capacity;
        this.items = new RecipeData[capacity];
        this.top = -1;
    }

    /*
    this method pushes a recipe onto the stack
    it takes a recipedata object to save before deletion
    it returns true when push succeeds or false when stack is already full
    */
    public boolean push(RecipeData recipe) {
        if (isFull()) {
            return false;
        }
        top = top + 1;
        items[top] = recipe;
        return true;
    }

    /*
    this method pops the most recently deleted recipe from the stack
    it returns the recipe at the top or null when stack is empty
    */
    public RecipeData pop() {
        if (isEmpty()) {
            return null;
        }
        RecipeData recipe = items[top];
        items[top] = null;
        top = top - 1;
        return recipe;
    }

    /*
    this method looks at the top recipe without removing it
    it returns the recipe at the top or null when stack is empty
    */
    public RecipeData peek() {
        if (isEmpty()) {
            return null;
        }
        return items[top];
    }

    /*
    this method checks if the stack is empty
    it returns true when top is minus one meaning no items are stored
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
    this method returns the current number of items in the stack
    it adds one to top since top starts at zero when one item exists
    */
    public int size() {
        return top + 1;
    }

    /*
    this method clears all items from the stack
    it loops from current top down to zero and sets each position to null then resets top to minus one
    */
    public void clear() {
        int i = 0;
        while (i <= top) {
            items[i] = null;
            i = i + 1;
        }
        top = -1;
    }
}
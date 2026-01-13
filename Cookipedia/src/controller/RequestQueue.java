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
public class RequestQueue {
    private final int capacity;
    private final RecipeRequest[] items;
    private int front;
    private int rear;

    /*
    this constructor prepares an empty queue with fixed size
    it takes capacity as integer which tells maximum number of requests allowed
    it sets front and rear to -1 to show that queue is empty
    */
    public RequestQueue(int capacity) {
        this.capacity = capacity;
        this.items = new RecipeRequest[capacity];
        this.front = -1;
        this.rear = -1;
    }

    /*
    this method checks whether the queue is full
    it returns true when front is 0 and rear is at last index so no more items can be added
    */
    public boolean isFull() {
        return front == 0 && rear == capacity - 1;
    }

    /*
    this method checks whether the queue is empty
    it returns true when front is -1 which is the empty flag
    */
    public boolean isEmpty() {
        return front == -1;
    }

    /*
    this method inserts a new request at the rear end of the queue
    it takes reciperequest object req that should be added
    it first checks overflow using isfull then sets front on first insert and moves rear and stores the request and returns true
    */
    public boolean enqueue(RecipeRequest req) {
        if (isFull()) {
            System.out.println("queue is full");
            return false;
        }
        if (front == -1) {
            front = 0;
        }
        rear = rear + 1;
        items[rear] = req;
        return true;
    }

    /*
    this method removes and returns the request at the front of the queue
    it returns reciperequest at front or null when queue is empty and resets front and rear back to -1 when last element is removed
    */
    public RecipeRequest dequeue() {
        if (isEmpty()) {
            System.out.println("queue is empty");
            return null;
        }

        RecipeRequest element = items[front];

        if (front >= rear) {
            front = -1;
            rear = -1;
        } else {
            front = front + 1;
        }
        return element;
    }

    /*
    this method copies all active requests from front to rear into a new array
    it returns an empty array when queue is empty or an array of exact active size when it has elements
    */
    public RecipeRequest[] toArray() {
        if (isEmpty()) {
            return new RecipeRequest[0];
        }
        int size = rear - front + 1;
        RecipeRequest[] arr = new RecipeRequest[size];
        int i = 0;
        while (i < size) {
            arr[i] = items[front + i];
            i = i + 1;
        }
        return arr;
    }
}

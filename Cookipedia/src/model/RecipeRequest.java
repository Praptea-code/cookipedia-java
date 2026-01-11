/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Acer
 */
public class RecipeRequest {
    private String username;
    private String title;
    private String vegNonVeg;
    private String notes;
    private String date;
    private String time;
    private String status;

    /*
    this constructor builds a new reciperequest with basic information
    it takes username title vegNonVeg notes date and time and sets initial status to pending
    */
    public RecipeRequest(String username, String title, String vegNonVeg, 
                        String notes, String date, String time) {
        this.username = username;
        this.title = title;
        this.vegNonVeg = vegNonVeg;
        this.notes = notes;
        this.date = date;
        this.time = time;
        this.status = "Pending";
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVegNonVeg() {
        return vegNonVeg;
    }

    public void setVegNonVeg(String vegNonVeg) {
        this.vegNonVeg = vegNonVeg;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /*
    this method returns a short text version of the request
    it includes username title vegNonVeg and status which is useful for debugging or logs
    */
    @Override
    public String toString() {
        return "RecipeRequest{" +
                "username='" + username + '\'' +
                ", title='" + title + '\'' +
                ", vegNonVeg='" + vegNonVeg + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.AppModel;
import model.RecipeData;
import model.RecipeRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import controller.RecipeDeleteStack;
import java.util.LinkedList;
import controller.RecentlyAddedQueue;


/**
 *
 * @author Acer
 */
public class AppController {
private final AppModel model;
    private final Sort sorter;
    private final Search searcher;
    private final Validate validator;
    private final RequestQueue requestQueue;
    private final RecipeDeleteStack deletedRecipesStack;
    private final LinkedList<RecipeRequest> pendingRequests; 
    private final List<RecipeRequest> deletedRequestsList;
    private final RecentlyAddedQueue recentlyAddedQueue;
    
    /*
    this constructor prepares the controller with its own model and helper classes
    it creates a new appmodel object and passes it to validate
    it also creates sort search and queue objects so later methods can reuse them
    */
    public AppController() {
        this.model = new AppModel();
        this.sorter = new Sort();
        this.searcher = new Search();
        this.validator = new Validate(model);
        this.requestQueue = new RequestQueue(50);
        this.deletedRecipesStack = new RecipeDeleteStack(20); 
        this.pendingRequests = new LinkedList<>();
        this.deletedRequestsList = new ArrayList<>(); 
        this.recentlyAddedQueue = new RecentlyAddedQueue(4);
    }

    /*
    this method stores a new recipe request into the request queue
    it takes username and title as required strings and vegNonVeg and notes as optional strings
    it first checks that username and title are not empty then builds a request with current date and time
    it returns "success" if the queue accepts the request or an error message when queue is full or inputs are missing
    */
    public String addRecipeRequest(String username, String title,
                                   String vegNonVeg, String notes) {
        if (username == null || username.trim().length() == 0) {
            return "Username is required!";
        }
        if (title == null || title.trim().length() == 0) {
            return "Recipe title is required!";
        }

        LocalDateTime now = LocalDateTime.now();
        String date = now.toLocalDate().toString();
        String time = now.toLocalTime().toString().substring(0, 8);

        String u = username.trim();
        String t = title.trim();
        String v = "";
        String n = "";

        if (vegNonVeg != null) {
            v = vegNonVeg.trim();
        }
        if (notes != null) {
            n = notes.trim();
        }

        RecipeRequest req = new RecipeRequest(u, t, v, n, date, time);

        boolean ok = requestQueue.enqueue(req);
        if (!ok) {
            return "Request queue is full!";
        }
        pendingRequests.addFirst(req); 
        model.incrementRequestCount();
        return "success";
    }

    /*
    this method gives all stored recipe requests as a list
    it reads the snapshot array from the queue and copies it into an arraylist
    it returns that list so ui can loop and display each request easily
    */
    public List<RecipeRequest> getAllRequests() {
        RecipeRequest[] arr = requestQueue.toArray();
        List<RecipeRequest> list = new ArrayList<RecipeRequest>();
        int i = 0;
        while (i < arr.length) {
            list.add(arr[i]);
            i = i + 1;
        }
        return list;
    }

    /*
    this method permanently removes a cancelled request from the queue using dequeue
    it takes username and title to identify the request
    it first checks if request status is cancelled before allowing deletion
    it dequeues all requests rebuilds queue without target and saves deleted request to list
    it returns "success" when deleted or error message when not cancelled or not found
    */
    public String deleteRequest(String username, String title) {
        RecipeRequest target = validator.findRequest(getAllRequests(), username, title);
        if (target == null) {
            return "Request not found!";
        }

        // CHECK IF STATUS IS CANCELLED ✅
        if (!target.getStatus().equals("Cancelled")) {
            return "Cannot delete! Request must be cancelled first.";
        }

        // Temporarily store all requests
        List<RecipeRequest> temp = new ArrayList<>();
        boolean found = false;

        // Dequeue all requests ✅
        while (!requestQueue.isEmpty()) {
            RecipeRequest req = requestQueue.dequeue(); // DEQUEUE operation ✅

            // Check if this is the one to delete
            if (req.getUsername().equals(username) && req.getTitle().equals(title)) {
                deletedRequestsList.add(req); // Save to deleted list ✅
                pendingRequests.remove(req); // Remove from LinkedList
                found = true;
                // Don't add to temp (this deletes it)
            } else {
                temp.add(req); // Keep this one
            }
        }

        // Re-enqueue all kept requests ✅
        int i = 0;
        while (i < temp.size()) {
            requestQueue.enqueue(temp.get(i));
            i = i + 1;
        }

        return found ? "success" : "Request not found!";
    }


    /*
    this method returns physically deleted requests that were removed using dequeue
    it returns the list of requests that were permanently deleted from the queue
    */
    public List<RecipeRequest> getDeletedRequests() {
        return new ArrayList<>(deletedRequestsList);
    }
    
    /*
    this method changes the status of a stored request to pending updated or cancelled
    it takes username and title to find the request and newStatus as the new state text
    it first checks that the status word is allowed then finds the request and updates its status field
    it returns true when update is successful or false if status is invalid or request is not found
    */
    public boolean updateRequestStatus(String username, String title, String newStatus) {
        if (!validator.status(newStatus)) {
            return false;
        }
        RecipeRequest target = validator.findRequest(getAllRequests(), username, title);
        if (target == null) {
            return false;
        }

        String s = newStatus.trim().toLowerCase();
        if (s.equals("pending")) {
            target.setStatus("Pending");
        } else if (s.equals("updated")) {
            target.setStatus("Updated");
            pendingRequests.remove(target);
        } else if (s.equals("cancelled")) {
            target.setStatus("Cancelled");
            pendingRequests.remove(target);
        }
        return true;
    }

    /*
    this method checks login credentials and decides what role to give
    it takes username and password strings entered in the login form
    it delegates all detailed checks to validate class and just returns whatever message it gets
    */
    public String validateLogin(String username, String password) {
        return validator.login(username, password);
    }

    /*
    this method adds a new recipe after checking all input fields carefully
    it takes title cuisine difficulty prepTime rating imagePath ingredients and process as strings from ui form
    it validates everything using validate class then parses prepTime and rating and finally builds a recipedata object and stores it
    it returns "success" when recipe is added or an error message string when validation fails
    */
    public String addRecipe(String title, String cuisine, String difficulty,String prepTime, String rating, String imagePath,String ingredients, String process) {

        String msg = validator.recipeFields(title, cuisine, difficulty, prepTime, rating);
        if (!msg.equals("valid")) {
            return msg;
        }

        String img = imagePath;
        if (img == null || img.trim().length() == 0) {
            img = "/img/default.png";
        }

        String t = title.trim();
        String c = cuisine.trim();
        String d = difficulty.trim();
        String pt = prepTime.trim();
        String rt = rating.trim();

        int prep = Integer.parseInt(pt);
        double rate = Double.parseDouble(rt);

        String ing = "";
        String pro = "";

        if (ingredients != null) {
            ing = ingredients.trim();
        }
        if (process != null) {
            pro = process.trim();
        }

        RecipeData r = new RecipeData(t, c, d, prep, rate, img.trim(), ing, pro);
        recentlyAddedQueue.enqueue(r);
        return "success";
    }

    /*
    this method updates an existing recipe with new details while preventing duplicates
    it takes recipe id and new title cuisine difficulty prepTime rating imagePath ingredients and process
    it first scans all recipes to make sure no other record has exactly same title cuisine and difficulty then forwards the update to the model
    it returns true when model updates successfully or throws an exception if a duplicate combination is found
    */
    public boolean updateRecipe(int id, String title, String cuisine, String difficulty,int prepTime, double rating, String imagePath,String ingredients, String process) {

        List<RecipeData> all = model.getAllRecipes();
        int i = 0;
        String t = title.trim();
        String c = cuisine.trim();
        String d = difficulty.trim();

        while (i < all.size()) {
            RecipeData r = all.get(i);
            if (r.getId() != id &&
                r.getTitle().equalsIgnoreCase(t) &&
                r.getCuisine().equalsIgnoreCase(c) &&
                r.getDifficulty().equalsIgnoreCase(d)) {
                throw new IllegalArgumentException(
                    "A recipe with this title, cuisine, and difficulty already exists!");
            }
            i = i + 1;
        }

        return model.updateRecipe(id, title, cuisine, difficulty,
                                  prepTime, rating, imagePath, ingredients, process);
    }

    /*
    this method removes a recipe from storage using its id
    it takes the integer id of the recipe row in your model
    it returns true when model finds and deletes it or false when nothing matched that id
    */
    public boolean deleteRecipe(int id) {
        RecipeData recipe = model.getRecipeById(id);
        if (recipe != null) {
            deletedRecipesStack.push(recipe);
            recentlyAddedQueue.removeById(id);
        }
        return model.deleteRecipe(id);
    }
    
    /*
    this method restores the most recently deleted recipe
    it pops from the delete stack and adds the recipe back to the model
    it returns the restored recipe or null when no deleted recipes exist
    */
    public RecipeData undoLastDelete() {
        RecipeData restored = deletedRecipesStack.pop();
        if (restored != null) {
            model.addRecipe(restored);
        }
        return restored;
    }

    /*
    this method checks if undo is available
    it returns true when there are deleted recipes that can be restored
    */
    public boolean canUndo() {
        return !deletedRecipesStack.isEmpty();
    }

    
    /*
    this method fetches a single recipe for viewing or editing
    it takes the recipe id as integer
    it returns the recipedata object from the model or null when id is not present
    */
    public RecipeData getRecipeById(int id) {
        return model.getRecipeById(id);
    }

    /*
    this method gives full list of recipes stored in the app
    it does not change the data and simply returns what model keeps
    it returns list of recipedata so ui can show cards or table directly
    */
    public List<RecipeData> getAllRecipes() {
        return model.getAllRecipes();
    }

    /*
    this method gives the latest few recipes that were added
    it takes count as integer which tells how many recent items to fetch
    it returns a list of those recipes according to internal rules in the model
    */
    public List<RecipeData> getRecentlyAdded(int count) {
        return model.getRecentlyAdded(count);
    }

    /*
    * this method returns recipes from the recently added queue
    * it fetches the queue array and converts it to a list for ui display
    * it returns list of most recently added recipes in fifo order
    */
   public List<RecipeData> getRecentlyAddedFromQueue() {
       RecipeData[] arr = recentlyAddedQueue.toArray();
       List<RecipeData> list = new ArrayList<>();
       int i = 0;
       while (i < arr.length) {
           if (arr[i] != null) {
               list.add(arr[i]);
           }
           i = i + 1;
       }
       return list;
   }

   /*
    * this method removes the oldest recipe from recently added queue
    * it uses dequeue operation to remove from front following fifo principle
    * it returns the removed recipe or null if queue is empty
    */
   public RecipeData removeOldestFromRecentlyAdded() {
       return recentlyAddedQueue.dequeue();
   }

    
    /*
    this method returns recipes sorted by title from a to z
    it simply forwards the full recipe list to sort helper and gets a new sorted copy
    it returns that sorted list so ui can display alphabetically
    */
    public List<RecipeData> sortRecipesByName() {
        return sorter.sortByNameAsc(model.getAllRecipes());
    }

    /*
    this method returns recipes sorted by title from z to a
    it uses sort helper to reverse the order of name sort
    it returns a new list in descending alphabetical order
    */
    public List<RecipeData> getRecipesSortedByNameDesc() {
        return sorter.sortByNameDesc(model.getAllRecipes());
    }

    /*
    this method returns recipes sorted by preparation time
    it calls sort helper which uses selection sort based on prepTime field
    it returns a new list ordered from shortest time to longest time
    */
    public List<RecipeData> sortRecipesByTime() {
        return sorter.sortByTimeAsc(model.getAllRecipes());
    }

    /*
    this method returns recipes sorted by rating from best to worst
    it calls sort helper that uses merge sort based on rating field
    it returns a new list ordered from highest rating to lowest rating
    */
    public List<RecipeData> sortRecipesByRating() {
        return sorter.sortByRatingDesc(model.getAllRecipes());
    }

    /*
    this method returns requests sorted by title from a to z
    it collects all requests then calls sort helper for insertion sort by title
    it returns a new list where first items are earliest letters like a or b
    */
    public List<RecipeRequest> sortRequestsByNameAsc() {
        return sorter.sortRequestsByNameAsc(getAllRequests());
    }

    /*
    this method returns requests sorted by title from z to a
    it collects all requests and lets sort helper order them descending
    it returns a new list where first items start from last letters like z or y
    */
    public List<RecipeRequest> sortRequestsByNameDesc() {
        return sorter.sortRequestsByNameDesc(getAllRequests());
    }

    /*
    this method finds recipes whose titles contain some text
    it takes search query string typed by user in search box
    it calls search helper which runs a manual linear scan on titles and returns matching recipes
    */
    public List<RecipeData> searchRecipesByTitle(String query) {
        return searcher.byTitle(model.getAllRecipes(), query);
    }

    /*
    this method finds recipes that match a given cuisine name exactly
    it takes cuisine text like nepali indian or italian
    it first sorts recipes by cuisine with selection sort then calls binary search helper to find all matches
    it returns list of all recipes whose cuisine equals the given text ignoring case
    */
    public List<RecipeData> searchRecipesByCuisine(String cuisine) {
        List<RecipeData> all = sorter.sortByCuisineName(model.getAllRecipes());
        return searcher.byCuisine(all, cuisine);
    }

    /*
    this method searches using title cuisine and difficulty at once
    it takes three filter strings which may be empty or null
    it returns only those recipes that satisfy every filter that user actually filled
    */
    public List<RecipeData> searchRecipes(String title, String cuisine, String difficulty) {
        return searcher.multi(model.getAllRecipes(), title, cuisine, difficulty);
    }

    /*
    this method records that user viewed a recipe so it appears in history
    it takes a recipedata object that user just clicked or opened
    it simply passes this recipe to model history list
    */
    public void addToHistory(RecipeData recipe) {
        if (recipe != null) {
            model.addToHistory(recipe);
        }
    }

    /*
    this method gives the whole viewing history
    it returns whatever list of recipes the model remembers as viewed
    ui can use it to build a history panel or recent views section
    */
    public List<RecipeData> getHistory() {
        return model.getHistory();
    }

    /*
    this method clears all previously viewed history records
    it tells the model to discard its history list
    this is useful for a clear history button in settings
    */
    public void clearHistory() {
        model.clearHistory();
    }

    /*
    this method returns viewing history sorted by recipe name
    it calls sort helper on the history list instead of full recipe list
    it returns a new list where history entries are ordered from a to z
    */
    public List<RecipeData> getHistorySortedByName() {
        return sorter.sortByNameAsc(model.getHistory());
    }

    /*
    this method returns viewing history sorted by prep time
    it uses sort helper to reorder history from quickest recipes to slowest ones
    it returns a new sorted list so ui can show which viewed recipes are fastest to cook
    */
    public List<RecipeData> getHistorySortedByTime() {
        return sorter.sortByTimeAsc(model.getHistory());
    }

    /*
    this method returns viewing history sorted by rating
    it uses sort helper to arrange viewed recipes from highest rating to lowest rating
    it returns a new list so ui can show the best rated viewed items first
    */
    public List<RecipeData> getHistorySortedByRating() {
        return sorter.sortByRatingDesc(model.getHistory());
    }

    /*
    this method increases the count of cooked recipes
    it is intended to be called when user marks a recipe as cooked
    it simply tells model to increment its cooked counter
    */
    public void markRecipeAsCooked() {
        model.incrementCookedCount();
    }

    /*
    this method reports how many recipes user has cooked in total
    it returns the cooked count integer stored in the model
    ui can use this number in stats cards or progress bars
    */
    public int getCookedCount() {
        return model.getCookedCount();
    }

    /*
    this method reports how many recipes are still left to cook
    it returns yet to cook count integer from the model
    this helps to show remaining recipes user has not tried
    */
    public int getYetToCookCount() {
        return model.getYetToCookCount();
    }

    /*
    this method tells how many recipes are stored in total
    it returns integer total recipe count from the model
    it is useful for summary numbers in dashboard or header
    */
    public int getTotalRecipes() {
        return model.getTotalRecipes();
    }

    /*
    this method tells how many recipe requests have been made
    it returns integer total request count from the model
    it can be used to show how active users are in requesting recipes
    */
    public int getRequestCount() {
        return model.getRequestCount();
    }
    
    /*
    this method returns only pending requests from the linked list
    it converts the linked list to an arraylist for the view layer
    */
    public List<RecipeRequest> getPendingRequests() {
        return new ArrayList<>(pendingRequests);
    }
}

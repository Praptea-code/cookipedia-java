# Cookipedia

Cookipedia is a desktop recipe manager built with Java Swing where users can browse, cook and request recipes, while admins manage the full recipe library and user requests.

## Features

- User login with separate **user** and **admin** dashboards.  
- Browse recipes as cards with image, cuisine, difficulty, time and rating.  
- View full recipe details with large image, ingredients and step‑by‑step process.  
- Mark recipes as cooked and see **My Stats** (total recipes, cooked, yet to cook, requests).  
- View **History** of viewed/cooked recipes (limited recent items using a queue).  
- Search recipes by title (linear search) and by cuisine (selection sort + binary search).  
- Sort recipes by name, time and rating using merge sort, selection sort and insertion sort.  
- Request recipes as a user; admin can see all requests, update status and delete.  
- Admin recipe management (add, update, delete, undo delete via stack).  
- Separate deleted requests stack and pending requests list to demonstrate data structures.

## Tech Stack

- Language: Java (JDK 8+ recommended)  
- UI: Java Swing (NetBeans GUI Builder used for layouts)  
- Architecture: MVC (Model–View–Controller)  
- Data structures:
  - Arrays and `ArrayList` for primary recipe storage  
  - Circular queue for viewing history  
  - Queue for recipe requests  
  - Queue for recently added recipes  
  - Stack for deleted recipes (undo delete)  
  - Stack for deleted requests  
  - `LinkedList` for pending requests  
- Algorithms:
  - Merge sort for recipes by name and rating  
  - Selection sort for recipes by time and cuisine  
  - Insertion sort for recipe requests by title  
  - Linear search by title  
  - Binary search by cuisine (on a sorted list)

## Project Structure

- `model.AppModel` – Holds main recipe list, history queue and statistics like cookedCount and requestCount.  
- `model.RecipeData` – One recipe (id, title, cuisine, difficulty, time, rating, image, ingredients, process).  
- `model.RecipeRequest` – One recipe request (username, title, veg/non‑veg, notes, date, time, status).  
- `controller.AppController` – Connects UI with model, handles validation, CRUD, requests, history, sorting and searching.  
- Sorting helpers – `MergeSort`, `SelectionSort`, `InsertionSort`.  
- Searching helpers – `LinearSearchTitle`, `BinarySearchCuisine`.  
- Queues – `HistoryQueue`, `RequestQueue`, `RecentlyAddedQueue`.  
- Stacks – `RecipeDeleteStack`, `RequestDeleteStack`.  
- `controller.Validate` – Central validation and login helper.  
- `view.AppViewFrame` – Main application window: login, user home, browse, history, recipe view and admin panels.

## Screens and Flows

### Login

- Admin login:  
  - Username: `admin`  
  - Password: `12345`  

- User login:  
  - Username: `user`  
  - Password: `67890`  

If the credentials are valid, the app opens the correct dashboard for that role. If not, it shows an error message.

### User Dashboard

- Top navigation: **Home**, **Recipes**, **History**, **Recipe Request**, **Logout**.  
- **Home**:
  - Stats cards: Total Recipes, Cooked, Yet to Cook, Requests.  
  - “Recently Added” recipe cards loaded from the recently added queue.  
- **Recipes**:
  - Shows all recipes as cards with image, title, cuisine, difficulty, time and rating.  
  - Sort options (e.g. Name A–Z, Name Z–A, Time, Rating).  
  - Search by title and cuisine.  
- **History**:
  - Shows recent viewed/cooked recipes from the history queue.  
  - Sort by name, time or rating.  
  - Option to clear history (depending on implementation).  
- **Recipe Request**:
  - User fills username, recipe title, veg/non‑veg and notes.  
  - System records date, time and status and stores the request in the request queue.

### Recipe View Screen

- Shows full recipe details:
  - Large title and big image  
  - Cuisine, difficulty, preparation time and rating  
  - Ingredients list  
  - Cooking process / steps  
- “Mark Cooked” button:
  - Increases cooked recipe count  
  - Updates stats on the Home screen  
  - Adds recipe to viewing history

### Admin Dashboard

- **Manage Recipes**:
  - Recipes table with all recipes.  
  - Form to add a new recipe (title, cuisine, difficulty, time, rating, image path, ingredients, process).  
  - Edit existing recipes by selecting from the table.  
  - Delete recipes and undo last delete using the deleted‑recipes stack.  
  - Sort recipes by name, time and rating.  
  - Search recipes using the search field.  

- **Manage Requests**:
  - Table of all recipe requests from the queue.  
  - Filter buttons to show only pending or show all.  
  - Update status to `Pending`, `Updated` or `Cancelled`.  
  - Delete requests so they move into the deleted‑requests stack.  
  - Table for deleted requests to view what was removed.

## How to Run

### Requirements

- JDK 8 or later  
- NetBeans (or any Java IDE that supports Swing)  
- Image files (for example `imgchickenChowmein.jpg`, `imgdefault.png`, and other `img*.jpg` files) placed on the classpath under an `img` folder

### Setup

1. Clone or download the project folder.  
2. Open the project in NetBeans as an existing Java project.  
3. Ensure the packages are correct (for example `view`, `model`, `controller`).  
4. Make sure the `img` folder is included in the project so `getResource("img/…")` can find the images.  
5. Set `view.AppViewFrame` as the main class in the project properties (or main form).

### Run

- In NetBeans, right‑click the project and choose **Run** to start the application.  
- After building a runnable JAR, you can also run from command line:
  ```bash
  java -jar Cookipedia.jar 

## Usage Summary

- Log in as **admin** (`admin` / `12345`) to manage recipes and requests.  
- Log in as **user** (`user` / `67890`) to browse, cook and request recipes.  
- Use the navigation tabs to move between **Home**, **Recipes**, **History** and **Recipe Request**.  
- Use sorting and searching options to quickly find recipes.  
- Use the **mark-cooked**, **delete**, **undo**, **update-status** and **request** features to interact with the data structures and algorithms behind the scenes.

## Screenshots
<img width="922" height="704" alt="image" src="https://github.com/user-attachments/assets/4265e0ba-a168-4c07-bbc4-38984594fb51" />
<img width="923" height="704" alt="image" src="https://github.com/user-attachments/assets/9a735c4a-c723-4499-a35a-2e443fa73aa1" />
<img width="923" height="703" alt="image" src="https://github.com/user-attachments/assets/2ac1127b-9520-4abb-a506-b1f3fd4b1af5" />
<img width="924" height="708" alt="image" src="https://github.com/user-attachments/assets/52d1afe9-8b97-4279-bcc4-374347c4d8cd" />
<img width="924" height="706" alt="image" src="https://github.com/user-attachments/assets/af097034-6e2c-4e10-aecd-46f40124c605" />
<img width="924" height="706" alt="image" src="https://github.com/user-attachments/assets/5c84343a-726e-489d-828a-f675e54f5b64" />
<img width="924" height="706" alt="image" src="https://github.com/user-attachments/assets/83b74542-7b69-435a-82a1-0fc910b84ce7" />
<img width="925" height="706" alt="image" src="https://github.com/user-attachments/assets/7af22c58-9182-4ab6-bec3-244aa24899e2" />

## Author

**Developer:** Prapti Bhattarai

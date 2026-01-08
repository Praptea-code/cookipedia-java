/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import java.awt.CardLayout;
import java.awt.Color;
import java.util.Scanner;
import model.AppModel;
import java.util.Queue;
import java.util.LinkedList;
import model.RecipeData;      
import model.RecipeRequest;
import controller.AppController;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Acer
 */
public class AppViewFrame extends javax.swing.JFrame {
    private javax.swing.JTextField adminIdField;
    private javax.swing.JTextField adminTitleField;
    private javax.swing.JTextField adminCuisineField;
    private javax.swing.JTextField adminImagePathField;
    private javax.swing.JTextField adminTypeField;
    private javax.swing.JTextField adminPrepTimeField;
    private javax.swing.JTextField adminRatingField;
    private javax.swing.JTextField adminDifficultyField;
    private javax.swing.JTextArea adminProcessArea;
    private javax.swing.JTextArea adminIngredientsArea;
    private javax.swing.JTable adminRecipeTable;
    private javax.swing.JTable adminReqTable;
    private final java.awt.Color loginNormalBg  = new java.awt.Color(0, 0, 0);
    private final java.awt.Color loginNormalFg  = new java.awt.Color(239, 196, 4);
    private final java.awt.Color loginHoverBg   = new java.awt.Color(239, 196, 4);  
    private final java.awt.Color loginHoverFg   = java.awt.Color.BLACK; 
    private final Color viewNormalBg = Color.BLACK; // F1C40F
    private final Color viewNormalFg = new Color(0xF1, 0xC4, 0x0F);
    private final Color viewHoverBg  = new Color(0xF1, 0xC4, 0x0F);
    private final Color viewHoverFg  = Color.BLACK;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AppViewFrame.class.getName());
    private final AppController controller = new AppController();
    private RecipeData currentViewingRecipe = null;

    
    public AppViewFrame() {
        initComponents();
        setupHomeCardLayouts();
        loadHomeCards();
        setupHistoryLayout();
        setupAdminComponents();
        setupSearchFieldListeners(); 
        logoutButton.setFocusPainted(false);
        logoutButton.setContentAreaFilled(true);
        logoutButton.setBorderPainted(false);

        logoutButton.setBorder(
            new javax.swing.border.LineBorder(
                new java.awt.Color(0, 0, 0), 
                2,                            
                true                          
            )
        );
    }
    
    private void setupAdminComponents() {
        adminTitleField = titleAdmin;
        adminCuisineField = cuisineAdmin;
        adminImagePathField = imagePathAdmin;
        adminDifficultyField = difficultyAdmin;
        adminPrepTimeField = timeAdmin;
        adminRatingField = ratingAdmin;
        adminProcessArea = processAdmin;
        adminRecipeTable = recipesTable;
        adminIngredientsArea = ingredientsAdmin;  
        adminRecipeTable.setDefaultEditor(Object.class, null);
        
        adminReqTable = reqTableAdmin;
        
        adminRecipeTable.setDefaultEditor(Object.class, null);
        
        adminReqTable.setDefaultEditor(Object.class, null);


        adminRecipeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = adminRecipeTable.getSelectedRow();
                if (selectedRow >= 0) {
                    populateRecipeFormFromTable(selectedRow);
                }
            }
        });
    }

    
    private void populateRecipeFormFromTable(int row) {
        try {
            javax.swing.table.TableModel tableModel = adminRecipeTable.getModel();

            // Validate row
            if (row < 0 || row >= tableModel.getRowCount()) {
                return;
            }

            // Get recipe ID safely
            Object idValue = tableModel.getValueAt(row, 0);
            if (idValue == null) {
                System.err.println("Recipe ID is null at row " + row);
                return;
            }

            int recipeId = Integer.parseInt(idValue.toString());
            RecipeData recipe = controller.getRecipeById(recipeId);

            if (recipe != null) {
                adminRecipeTable.putClientProperty("selectedRecipeId", recipe.getId());
                adminTitleField.setText(recipe.getTitle() != null ? recipe.getTitle() : "");
                adminCuisineField.setText(recipe.getCuisine() != null ? recipe.getCuisine() : "");
                adminDifficultyField.setText(recipe.getDifficulty() != null ? recipe.getDifficulty() : "");
                adminPrepTimeField.setText(String.valueOf(recipe.getPrepTime()));
                adminRatingField.setText(String.valueOf(recipe.getRating()));
                adminImagePathField.setText(recipe.getImagePath() != null ? recipe.getImagePath() : "");
                adminIngredientsArea.setText(recipe.getIngredients() != null ? recipe.getIngredients() : "");
                adminProcessArea.setText(recipe.getProcess() != null ? recipe.getProcess() : "");
            } else {
                System.err.println("Recipe not found for ID: " + recipeId);
            }
        } catch (Exception e) {
            System.err.println("Error populating form: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void clearRecipeForm() {
        adminTitleField.setText("");
        adminCuisineField.setText("");
        adminImagePathField.setText("");
        adminPrepTimeField.setText("");
        adminRatingField.setText("");
        adminDifficultyField.setText("");
        adminIngredientsArea.setText("");
        adminProcessArea.setText("");
        adminRecipeTable.clearSelection();
        adminRecipeTable.putClientProperty("selectedRecipeId", null);
    }
    
    private void setupHomeCardLayouts() {
        recentlyAddedPanel.setLayout(new java.awt.GridLayout(1, 3, 60, 0));
        recentlyAddedPanel.setBackground(java.awt.Color.WHITE);
    }
    
    private void loadHomeCards() {
        recentlyAddedPanel.removeAll();
        browseCardsPanel.removeAll(); 
        browseCardsPanel.setLayout(new java.awt.GridLayout(0, 4, 20, 20));
        browseCardsPanel.setBorder(
            javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );
        
        List<RecipeData> recent = controller.getRecentlyAdded(4);  
            for (RecipeData r : recent) {
                recentlyAddedPanel.add(createRecipeCard(r));
            }
        List<RecipeData> allRecipes = controller.getAllRecipes();  
            for (RecipeData r : allRecipes) {
                browseCardsPanel.add(createRecipeCard(r));
            }

            
        updateHomeStats();
        
        recentlyAddedPanel.revalidate();
        recentlyAddedPanel.repaint();
        browseCardsPanel.revalidate();
        browseCardsPanel.repaint();
    }
    
    private void updateHomeStats() {
        totalRecipesNumber.setText(String.valueOf(controller.getTotalRecipes()));
        requestedNumber1.setText(String.valueOf(controller.getCookedCount()));
        yetToCookNumber.setText(String.valueOf(controller.getYetToCookCount()));
        requestedNumber.setText(String.valueOf(controller.getRequestCount()));
    }
    
    private void loadHistoryCards() {
        browseHistoryPanel.removeAll();

        for (RecipeData r :controller.getHistory()) {
            browseHistoryPanel.add(createRecipeCard(r));
        }

        browseHistoryPanel.revalidate();
        browseHistoryPanel.repaint();
    }


    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        String result = controller.validateLogin(username, password);
        CardLayout cl = (CardLayout) getContentPane().getLayout();

        if (result.equals("admin")) {
            cl.show(getContentPane(), "card4");
            loadAdminRecipesTable();
        } else if (result.equals("user")) {
            cl.show(getContentPane(), "card2");
            
            CardLayout baseCL = (CardLayout) basePanel.getLayout();
            baseCL.show(basePanel, "card6"); 
            
            
            updateHomeStats(); 
            loadHomeCards();
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, result,
                "Login Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void setupHistoryLayout() {
        browseHistoryPanel.setLayout(new java.awt.GridLayout(0, 4, 20, 20));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        loginPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        subTitleLabel = new javax.swing.JLabel();
        usernameField = new javax.swing.JTextField();
        titleLabel = new javax.swing.JLabel();
        usernameLabel = new javax.swing.JLabel();
        passwordLabel = new javax.swing.JLabel();
        loginButton = new javax.swing.JButton();
        passwordField = new javax.swing.JPasswordField();
        jPanel8 = new javax.swing.JPanel();
        loginPageImage = new javax.swing.JLabel();
        adminPanel = new javax.swing.JPanel();
        navMainPanelAdmin = new javax.swing.JPanel();
        navigationPanel1 = new javax.swing.JPanel();
        manageRecipesBtn = new javax.swing.JButton();
        manageRequestBtn = new javax.swing.JButton();
        jSeparator9 = new javax.swing.JSeparator();
        logoutButtonAdmin = new javax.swing.JButton();
        logoAdmin = new javax.swing.JLabel();
        baseAdminPAnel = new javax.swing.JPanel();
        manageRecipesPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        titleLabelAdmin = new javax.swing.JLabel();
        cuisineLabelAdmin = new javax.swing.JLabel();
        imagePathLevelAdmin = new javax.swing.JLabel();
        ingredientsLabelAmin = new javax.swing.JLabel();
        timeLabelAdmin = new javax.swing.JLabel();
        ratingLabelAdmin = new javax.swing.JLabel();
        processLabelAdmin = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        processAdmin = new javax.swing.JTextArea();
        difficultyAdmin = new javax.swing.JTextField();
        timeAdmin = new javax.swing.JTextField();
        ratingAdmin = new javax.swing.JTextField();
        titleAdmin = new javax.swing.JTextField();
        imagePathAdmin = new javax.swing.JTextField();
        cuisineAdmin = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        difficultyLabelAdmin = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        ingredientsAdmin = new javax.swing.JTextArea();
        jButton11 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        recipesTable = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jTextField3 = new javax.swing.JTextField();
        manageRequestsPanel = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        reqTableAdmin = new javax.swing.JTable();
        updateStatusBtn = new javax.swing.JButton();
        deleteRequestBtn = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        userPanel = new javax.swing.JPanel();
        navMainPanel = new javax.swing.JPanel();
        navigationPanel = new javax.swing.JPanel();
        homeButton = new javax.swing.JButton();
        myHistoryButton = new javax.swing.JButton();
        browseRecipeButton = new javax.swing.JButton();
        myRecipeRequestButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        logoutButton = new javax.swing.JButton();
        logo = new javax.swing.JLabel();
        basePanel = new javax.swing.JPanel();
        homePanelUser = new javax.swing.JPanel();
        myStatsLabel = new javax.swing.JLabel();
        recentlyAddedPanel = new javax.swing.JPanel();
        totalRecipes = new javax.swing.JLabel();
        totalRecipesNumber = new javax.swing.JLabel();
        recipesCookedNumber = new javax.swing.JLabel();
        requestedNumber = new javax.swing.JLabel();
        bannerPanel = new javax.swing.JPanel();
        bannerHomePage = new javax.swing.JLabel();
        requestedLabel1 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        requestedNumber1 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        yetToCookNumber = new javax.swing.JLabel();
        yetToCook = new javax.swing.JLabel();
        totalRecipesLogo = new javax.swing.JLabel();
        recipesCookedLogo = new javax.swing.JLabel();
        requestedLogo = new javax.swing.JLabel();
        yetToCookLogo = new javax.swing.JLabel();
        myStatsLabel1 = new javax.swing.JLabel();
        browseRecipesPanel = new javax.swing.JPanel();
        browsePanel = new javax.swing.JPanel();
        browseTopPanel = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        browseScrollPane = new javax.swing.JScrollPane();
        browseCardsPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        myHistoryPanel = new javax.swing.JPanel();
        historyPanel = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        historyScrollPane = new java.awt.ScrollPane();
        browseHistoryPanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        recipeRequestPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        reqUsernameLabel = new javax.swing.JLabel();
        recipeTitleLabel = new javax.swing.JLabel();
        vegNonvegLabel = new javax.swing.JLabel();
        noteLabel = new javax.swing.JLabel();
        requestBtn = new javax.swing.JButton();
        reqUsernameTextField = new javax.swing.JTextField();
        recipeTitleTextfield = new javax.swing.JTextField();
        vegNonvegTextField = new javax.swing.JTextField();
        noteTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        reqHistoryTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        banner = new javax.swing.JLabel();
        requestRecipe = new javax.swing.JLabel();
        reqHistoryLabel = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        nameAsc = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        viewPanel = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        backButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.CardLayout());

        loginPanel.setBackground(new java.awt.Color(248, 237, 236));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        subTitleLabel.setFont(new java.awt.Font("SansSerif", 0, 20)); // NOI18N
        subTitleLabel.setText("Let's get started!");

        titleLabel.setFont(new java.awt.Font("SansSerif", 1, 28)); // NOI18N
        titleLabel.setText("Welcome to Cookipedia");

        usernameLabel.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        usernameLabel.setText("Username");

        passwordLabel.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        passwordLabel.setText("Password");

        loginButton.setBackground(new java.awt.Color(0, 0, 0));
        loginButton.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        loginButton.setForeground(new java.awt.Color(239, 196, 4));
        loginButton.setText("Login");
        loginButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButtonMouseExited(evt);
            }
        });
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        passwordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(passwordLabel)
                            .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(usernameLabel)
                            .addComponent(titleLabel)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(164, 164, 164)
                        .addComponent(subTitleLabel))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(187, 187, 187)
                        .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(89, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(259, 259, 259)
                .addComponent(titleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subTitleLabel)
                .addGap(43, 43, 43)
                .addComponent(usernameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(passwordLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(260, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(239, 196, 4));
        jPanel8.setLayout(new java.awt.GridBagLayout());

        loginPageImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/loginpage.png"))); // NOI18N
        jPanel8.add(loginPageImage, new java.awt.GridBagConstraints());

        javax.swing.GroupLayout loginPanelLayout = new javax.swing.GroupLayout(loginPanel);
        loginPanel.setLayout(loginPanelLayout);
        loginPanelLayout.setHorizontalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 724, Short.MAX_VALUE))
        );
        loginPanelLayout.setVerticalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        getContentPane().add(loginPanel, "card3");

        navMainPanelAdmin.setBackground(new java.awt.Color(244, 196, 4));
        navMainPanelAdmin.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        navMainPanelAdmin.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        navMainPanelAdmin.setLayout(null);

        navigationPanel1.setBackground(new java.awt.Color(96, 26, 26));

        javax.swing.GroupLayout navigationPanel1Layout = new javax.swing.GroupLayout(navigationPanel1);
        navigationPanel1.setLayout(navigationPanel1Layout);
        navigationPanel1Layout.setHorizontalGroup(
            navigationPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 190, Short.MAX_VALUE)
        );
        navigationPanel1Layout.setVerticalGroup(
            navigationPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 550, Short.MAX_VALUE)
        );

        navMainPanelAdmin.add(navigationPanel1);
        navigationPanel1.setBounds(0, 130, 190, 550);

        manageRecipesBtn.setBackground(new java.awt.Color(96, 26, 26));
        manageRecipesBtn.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        manageRecipesBtn.setText("Manage Recipes");
        manageRecipesBtn.setBorder(null);
        manageRecipesBtn.setContentAreaFilled(false);
        manageRecipesBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        manageRecipesBtn.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        manageRecipesBtn.setFocusPainted(false);
        manageRecipesBtn.setRequestFocusEnabled(false);
        manageRecipesBtn.setRolloverEnabled(false);
        manageRecipesBtn.setVerifyInputWhenFocusTarget(false);
        manageRecipesBtn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                manageRecipesBtnFocusGained(evt);
            }
        });
        manageRecipesBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageRecipesBtnActionPerformed(evt);
            }
        });
        navMainPanelAdmin.add(manageRecipesBtn);
        manageRecipesBtn.setBounds(440, 0, 180, 60);

        manageRequestBtn.setBackground(new java.awt.Color(96, 26, 26));
        manageRequestBtn.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        manageRequestBtn.setText("Manage Requests");
        manageRequestBtn.setBorder(null);
        manageRequestBtn.setBorderPainted(false);
        manageRequestBtn.setContentAreaFilled(false);
        manageRequestBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        manageRequestBtn.setFocusPainted(false);
        manageRequestBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageRequestBtnActionPerformed(evt);
            }
        });
        navMainPanelAdmin.add(manageRequestBtn);
        manageRequestBtn.setBounds(640, 0, 170, 60);

        jSeparator9.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator9.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator9.setOrientation(javax.swing.SwingConstants.VERTICAL);
        navMainPanelAdmin.add(jSeparator9);
        jSeparator9.setBounds(630, 20, 10, 30);

        logoutButtonAdmin.setBackground(new java.awt.Color(0, 0, 0));
        logoutButtonAdmin.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        logoutButtonAdmin.setForeground(new java.awt.Color(255, 204, 0));
        logoutButtonAdmin.setText("Logout");
        logoutButtonAdmin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        logoutButtonAdmin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutButtonAdminMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutButtonAdminMouseExited(evt);
            }
        });
        logoutButtonAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonAdminActionPerformed(evt);
            }
        });
        navMainPanelAdmin.add(logoutButtonAdmin);
        logoutButtonAdmin.setBounds(1110, 10, 100, 42);

        logoAdmin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logo.png"))); // NOI18N
        navMainPanelAdmin.add(logoAdmin);
        logoAdmin.setBounds(20, 0, 60, 58);

        baseAdminPAnel.setLayout(new java.awt.CardLayout());

        manageRecipesPanel.setBackground(new java.awt.Color(255, 255, 255));

        titleLabelAdmin.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        titleLabelAdmin.setText("Title:");

        cuisineLabelAdmin.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cuisineLabelAdmin.setText("Cuisine:");

        imagePathLevelAdmin.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        imagePathLevelAdmin.setText("Image:");

        ingredientsLabelAmin.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        ingredientsLabelAmin.setText("Ingredients:");

        timeLabelAdmin.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        timeLabelAdmin.setText("Time:");

        ratingLabelAdmin.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        ratingLabelAdmin.setText("Rating:");

        processLabelAdmin.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        processLabelAdmin.setText("Process:");

        processAdmin.setColumns(20);
        processAdmin.setRows(5);
        processAdmin.setWrapStyleWord(true);
        jScrollPane3.setViewportView(processAdmin);
        processAdmin.setLineWrap(true);
        processAdmin.setWrapStyleWord(true);
        jScrollPane3.setHorizontalScrollBarPolicy(
            javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jButton6.setBackground(new java.awt.Color(0, 102, 0));
        jButton6.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Add");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(0, 51, 153));
        jButton7.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("Update");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(153, 0, 0));
        jButton8.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("Delete");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        difficultyLabelAdmin.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        difficultyLabelAdmin.setText("Difficulty:");

        ingredientsAdmin.setColumns(6);
        ingredientsAdmin.setRows(5);
        ingredientsAdmin.setWrapStyleWord(true);
        jScrollPane4.setViewportView(ingredientsAdmin);
        ingredientsAdmin.setLineWrap(true);
        ingredientsAdmin.setWrapStyleWord(true);
        jScrollPane2.setHorizontalScrollBarPolicy(
            javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jButton11.setBackground(new java.awt.Color(0, 0, 0));
        jButton11.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setText("Clear");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(imagePathLevelAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(difficultyLabelAdmin)
                                    .addComponent(timeLabelAdmin)
                                    .addComponent(ratingLabelAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jButton6)
                                        .addGap(109, 109, 109)
                                        .addComponent(jButton8)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(90, 90, 90)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton11))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(titleLabelAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(titleAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(cuisineLabelAdmin)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cuisineAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(imagePathAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(difficultyAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(timeAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ratingAdmin))))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(ingredientsLabelAmin, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(processLabelAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(16, 16, 16))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ingredientsLabelAmin)
                            .addComponent(titleLabelAdmin)
                            .addComponent(titleAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cuisineLabelAdmin)
                            .addComponent(cuisineAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(imagePathLevelAdmin)
                            .addComponent(imagePathAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(difficultyLabelAdmin)
                            .addComponent(difficultyAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(processLabelAdmin))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(timeLabelAdmin)
                            .addComponent(timeAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ratingLabelAdmin)
                            .addComponent(ratingAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6)
                    .addComponent(jButton8)
                    .addComponent(jButton7)
                    .addComponent(jButton11))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        recipesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Title ", "Cuisine", "Difficulty", "Time", "Rating"
            }
        ));
        jScrollPane2.setViewportView(recipesTable);

        jLabel11.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel11.setText("Recipes Form");

        jLabel12.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel12.setText("Recipes Table");

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cookNow.png"))); // NOI18N

        jLabel13.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel13.setText("Sort By:");

        jButton14.setBackground(new java.awt.Color(0, 0, 0));
        jButton14.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jButton14.setForeground(new java.awt.Color(255, 204, 0));
        jButton14.setText("Name ↑");
        jButton14.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setBackground(new java.awt.Color(0, 0, 0));
        jButton15.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jButton15.setForeground(new java.awt.Color(255, 204, 0));
        jButton15.setText(" Time ↑");
        jButton15.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton16.setBackground(new java.awt.Color(0, 0, 0));
        jButton16.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jButton16.setForeground(new java.awt.Color(255, 204, 0));
        jButton16.setText("Rating ↓");
        jButton16.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jTextField3.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jTextField3.setForeground(new java.awt.Color(102, 102, 102));
        jTextField3.setText("Search any recipe");
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout manageRecipesPanelLayout = new javax.swing.GroupLayout(manageRecipesPanel);
        manageRecipesPanel.setLayout(manageRecipesPanelLayout);
        manageRecipesPanelLayout.setHorizontalGroup(
            manageRecipesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(manageRecipesPanelLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(manageRecipesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addGroup(manageRecipesPanelLayout.createSequentialGroup()
                        .addGroup(manageRecipesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, manageRecipesPanelLayout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addComponent(jLabel5))
                    .addGroup(manageRecipesPanelLayout.createSequentialGroup()
                        .addGroup(manageRecipesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(manageRecipesPanelLayout.createSequentialGroup()
                                .addGap(587, 587, 587)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton14)
                                .addGap(18, 18, 18)
                                .addComponent(jButton15)
                                .addGap(18, 18, 18)
                                .addComponent(jButton16))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1161, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addComponent(jLabel2)))
                .addContainerGap())
        );
        manageRecipesPanelLayout.setVerticalGroup(
            manageRecipesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(manageRecipesPanelLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(manageRecipesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(manageRecipesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(manageRecipesPanelLayout.createSequentialGroup()
                        .addGap(168, 168, 168)
                        .addComponent(jLabel2)
                        .addContainerGap(266, Short.MAX_VALUE))
                    .addGroup(manageRecipesPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addGap(10, 10, 10)
                        .addGroup(manageRecipesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jButton14)
                            .addComponent(jButton15)
                            .addComponent(jButton16)
                            .addComponent(jTextField3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39))))
        );

        baseAdminPAnel.add(manageRecipesPanel, "card2");

        manageRequestsPanel.setBackground(new java.awt.Color(255, 255, 255));

        reqTableAdmin.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Username", "Recipe Title", "Veg/Non-Veg", "Dietary Notes", "Date", "Time", "Status"
            }
        ));
        jScrollPane6.setViewportView(reqTableAdmin);

        updateStatusBtn.setBackground(new java.awt.Color(0, 0, 0));
        updateStatusBtn.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        updateStatusBtn.setForeground(new java.awt.Color(255, 204, 0));
        updateStatusBtn.setText("Update Status");
        updateStatusBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateStatusBtnActionPerformed(evt);
            }
        });

        deleteRequestBtn.setBackground(new java.awt.Color(0, 0, 0));
        deleteRequestBtn.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        deleteRequestBtn.setForeground(new java.awt.Color(255, 204, 0));
        deleteRequestBtn.setText("Delete Request");
        deleteRequestBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteRequestBtnActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel7.setText("Requests");

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/bannerRequestPage.png"))); // NOI18N

        javax.swing.GroupLayout manageRequestsPanelLayout = new javax.swing.GroupLayout(manageRequestsPanel);
        manageRequestsPanel.setLayout(manageRequestsPanelLayout);
        manageRequestsPanelLayout.setHorizontalGroup(
            manageRequestsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(manageRequestsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(manageRequestsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(manageRequestsPanelLayout.createSequentialGroup()
                        .addComponent(updateStatusBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(deleteRequestBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(manageRequestsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel7)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 1142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9)))
                .addGap(44, 44, 44))
        );
        manageRequestsPanelLayout.setVerticalGroup(
            manageRequestsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(manageRequestsPanelLayout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addGroup(manageRequestsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updateStatusBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteRequestBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(37, 37, 37))
        );

        baseAdminPAnel.add(manageRequestsPanel, "card3");

        javax.swing.GroupLayout adminPanelLayout = new javax.swing.GroupLayout(adminPanel);
        adminPanel.setLayout(adminPanelLayout);
        adminPanelLayout.setHorizontalGroup(
            adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(navMainPanelAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(baseAdminPAnel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        adminPanelLayout.setVerticalGroup(
            adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adminPanelLayout.createSequentialGroup()
                .addComponent(navMainPanelAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(baseAdminPAnel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(adminPanel, "card4");

        userPanel.setBackground(new java.awt.Color(255, 255, 255));

        navMainPanel.setBackground(new java.awt.Color(244, 196, 4));
        navMainPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        navMainPanel.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        navMainPanel.setLayout(null);

        navigationPanel.setBackground(new java.awt.Color(96, 26, 26));

        javax.swing.GroupLayout navigationPanelLayout = new javax.swing.GroupLayout(navigationPanel);
        navigationPanel.setLayout(navigationPanelLayout);
        navigationPanelLayout.setHorizontalGroup(
            navigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 190, Short.MAX_VALUE)
        );
        navigationPanelLayout.setVerticalGroup(
            navigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 550, Short.MAX_VALUE)
        );

        navMainPanel.add(navigationPanel);
        navigationPanel.setBounds(0, 130, 190, 550);

        homeButton.setBackground(new java.awt.Color(96, 26, 26));
        homeButton.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        homeButton.setText("Home");
        homeButton.setBorder(null);
        homeButton.setContentAreaFilled(false);
        homeButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        homeButton.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        homeButton.setFocusPainted(false);
        homeButton.setRequestFocusEnabled(false);
        homeButton.setRolloverEnabled(false);
        homeButton.setVerifyInputWhenFocusTarget(false);
        homeButton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                homeButtonFocusGained(evt);
            }
        });
        homeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeButtonActionPerformed(evt);
            }
        });
        navMainPanel.add(homeButton);
        homeButton.setBounds(300, 0, 120, 60);

        myHistoryButton.setBackground(new java.awt.Color(96, 26, 26));
        myHistoryButton.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        myHistoryButton.setText("History");
        myHistoryButton.setBorder(null);
        myHistoryButton.setBorderPainted(false);
        myHistoryButton.setContentAreaFilled(false);
        myHistoryButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        myHistoryButton.setFocusPainted(false);
        myHistoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myHistoryButtonActionPerformed(evt);
            }
        });
        navMainPanel.add(myHistoryButton);
        myHistoryButton.setBounds(580, 0, 120, 60);

        browseRecipeButton.setBackground(new java.awt.Color(96, 26, 26));
        browseRecipeButton.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        browseRecipeButton.setText("Recipes");
        browseRecipeButton.setBorder(null);
        browseRecipeButton.setBorderPainted(false);
        browseRecipeButton.setContentAreaFilled(false);
        browseRecipeButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        browseRecipeButton.setDefaultCapable(false);
        browseRecipeButton.setFocusPainted(false);
        browseRecipeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseRecipeButtonActionPerformed(evt);
            }
        });
        navMainPanel.add(browseRecipeButton);
        browseRecipeButton.setBounds(440, 0, 120, 60);

        myRecipeRequestButton.setBackground(new java.awt.Color(96, 26, 26));
        myRecipeRequestButton.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        myRecipeRequestButton.setText("Recipe Request");
        myRecipeRequestButton.setBorder(null);
        myRecipeRequestButton.setBorderPainted(false);
        myRecipeRequestButton.setContentAreaFilled(false);
        myRecipeRequestButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        myRecipeRequestButton.setFocusPainted(false);
        myRecipeRequestButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myRecipeRequestButtonActionPerformed(evt);
            }
        });
        navMainPanel.add(myRecipeRequestButton);
        myRecipeRequestButton.setBounds(720, 0, 140, 60);

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        navMainPanel.add(jSeparator1);
        jSeparator1.setBounds(710, 20, 10, 30);

        jSeparator2.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        navMainPanel.add(jSeparator2);
        jSeparator2.setBounds(430, 20, 10, 30);

        jSeparator3.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator3.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        navMainPanel.add(jSeparator3);
        jSeparator3.setBounds(570, 20, 10, 30);

        logoutButton.setBackground(new java.awt.Color(0, 0, 0));
        logoutButton.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        logoutButton.setForeground(new java.awt.Color(255, 204, 0));
        logoutButton.setText("Logout");
        logoutButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutButtonMouseExited(evt);
            }
        });
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });
        navMainPanel.add(logoutButton);
        logoutButton.setBounds(1110, 10, 100, 42);

        logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logo.png"))); // NOI18N
        navMainPanel.add(logo);
        logo.setBounds(20, 0, 60, 58);

        basePanel.setBackground(new java.awt.Color(255, 255, 255));
        basePanel.setLayout(new java.awt.CardLayout());

        homePanelUser.setBackground(new java.awt.Color(255, 255, 255));

        myStatsLabel.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        myStatsLabel.setText("Recently Added");

        recentlyAddedPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 10));

        totalRecipes.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        totalRecipes.setText("Total Recipes");

        totalRecipesNumber.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        totalRecipesNumber.setText("0");

        recipesCookedNumber.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        recipesCookedNumber.setText("Recipes Cooked");

        requestedNumber.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        requestedNumber.setText("0");

        bannerPanel.setBackground(new java.awt.Color(0, 0, 0));

        bannerHomePage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/banner.png"))); // NOI18N

        javax.swing.GroupLayout bannerPanelLayout = new javax.swing.GroupLayout(bannerPanel);
        bannerPanel.setLayout(bannerPanelLayout);
        bannerPanelLayout.setHorizontalGroup(
            bannerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bannerHomePage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        bannerPanelLayout.setVerticalGroup(
            bannerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bannerPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(bannerHomePage))
        );

        requestedLabel1.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        requestedLabel1.setText("Requested");

        jSeparator5.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);

        requestedNumber1.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        requestedNumber1.setText("0");

        jSeparator6.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator7.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator7.setOrientation(javax.swing.SwingConstants.VERTICAL);

        yetToCookNumber.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        yetToCookNumber.setText("0");

        yetToCook.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        yetToCook.setText("Yet to Cook");

        totalRecipesLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/symbol1.png"))); // NOI18N

        recipesCookedLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/symbol2.png"))); // NOI18N

        requestedLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/symbol3.png"))); // NOI18N

        yetToCookLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/symbol4.png"))); // NOI18N

        myStatsLabel1.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        myStatsLabel1.setText("Stats");

        javax.swing.GroupLayout homePanelUserLayout = new javax.swing.GroupLayout(homePanelUser);
        homePanelUser.setLayout(homePanelUserLayout);
        homePanelUserLayout.setHorizontalGroup(
            homePanelUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bannerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(homePanelUserLayout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addGroup(homePanelUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(homePanelUserLayout.createSequentialGroup()
                        .addComponent(myStatsLabel)
                        .addContainerGap())
                    .addGroup(homePanelUserLayout.createSequentialGroup()
                        .addGroup(homePanelUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(homePanelUserLayout.createSequentialGroup()
                                .addGap(73, 73, 73)
                                .addComponent(totalRecipes))
                            .addGroup(homePanelUserLayout.createSequentialGroup()
                                .addGap(120, 120, 120)
                                .addComponent(totalRecipesNumber))
                            .addGroup(homePanelUserLayout.createSequentialGroup()
                                .addGap(107, 107, 107)
                                .addComponent(totalRecipesLogo)))
                        .addGap(80, 80, 80)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(homePanelUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(homePanelUserLayout.createSequentialGroup()
                                .addGap(83, 83, 83)
                                .addComponent(recipesCookedNumber))
                            .addGroup(homePanelUserLayout.createSequentialGroup()
                                .addGap(131, 131, 131)
                                .addComponent(requestedNumber1))
                            .addGroup(homePanelUserLayout.createSequentialGroup()
                                .addGap(120, 120, 120)
                                .addComponent(recipesCookedLogo)))
                        .addGap(84, 84, 84)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(homePanelUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(homePanelUserLayout.createSequentialGroup()
                                .addGroup(homePanelUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(homePanelUserLayout.createSequentialGroup()
                                        .addGap(106, 106, 106)
                                        .addComponent(yetToCook))
                                    .addGroup(homePanelUserLayout.createSequentialGroup()
                                        .addGap(139, 139, 139)
                                        .addComponent(yetToCookNumber)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, homePanelUserLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(yetToCookLogo)
                                .addGap(121, 121, 121)))
                        .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(homePanelUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(homePanelUserLayout.createSequentialGroup()
                                .addGap(87, 87, 87)
                                .addGroup(homePanelUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, homePanelUserLayout.createSequentialGroup()
                                        .addComponent(requestedNumber)
                                        .addGap(180, 180, 180))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, homePanelUserLayout.createSequentialGroup()
                                        .addComponent(requestedLabel1)
                                        .addGap(147, 147, 147))))
                            .addGroup(homePanelUserLayout.createSequentialGroup()
                                .addGap(106, 106, 106)
                                .addComponent(requestedLogo))))
                    .addGroup(homePanelUserLayout.createSequentialGroup()
                        .addGroup(homePanelUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(recentlyAddedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1074, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(myStatsLabel1))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        homePanelUserLayout.setVerticalGroup(
            homePanelUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homePanelUserLayout.createSequentialGroup()
                .addComponent(bannerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addComponent(myStatsLabel1)
                .addGap(18, 18, 18)
                .addGroup(homePanelUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, homePanelUserLayout.createSequentialGroup()
                        .addGroup(homePanelUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(recipesCookedLogo)
                            .addComponent(yetToCookLogo)
                            .addComponent(totalRecipesLogo)
                            .addComponent(requestedLogo))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(homePanelUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(totalRecipes, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(recipesCookedNumber)
                            .addComponent(yetToCook, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(requestedLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(homePanelUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(totalRecipesNumber)
                            .addComponent(requestedNumber1)
                            .addComponent(yetToCookNumber)
                            .addComponent(requestedNumber))))
                .addGap(86, 86, 86)
                .addComponent(myStatsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(recentlyAddedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(318, 318, 318))
        );

        basePanel.add(homePanelUser, "card6");

        browseRecipesPanel.setBackground(new java.awt.Color(255, 255, 255));

        browsePanel.setBackground(new java.awt.Color(255, 255, 255));

        browseTopPanel.setBackground(new java.awt.Color(255, 255, 255));

        jTextField1.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(102, 102, 102));
        jTextField1.setText("Search any recipe");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel3.setText("Sort By:");

        jButton1.setBackground(new java.awt.Color(0, 0, 0));
        jButton1.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 204, 0));
        jButton1.setText("Name ↑");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(0, 0, 0));
        jButton3.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 204, 0));
        jButton3.setText("Time ↑");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(0, 0, 0));
        jButton9.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jButton9.setForeground(new java.awt.Color(255, 204, 0));
        jButton9.setText("Rating ↓");
        jButton9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout browseTopPanelLayout = new javax.swing.GroupLayout(browseTopPanel);
        browseTopPanel.setLayout(browseTopPanelLayout);
        browseTopPanelLayout.setHorizontalGroup(
            browseTopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(browseTopPanelLayout.createSequentialGroup()
                .addGap(331, 331, 331)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(jLabel3)
                .addGap(30, 30, 30)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        browseTopPanelLayout.setVerticalGroup(
            browseTopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(browseTopPanelLayout.createSequentialGroup()
                .addContainerGap(56, Short.MAX_VALUE)
                .addGroup(browseTopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, browseTopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jButton1)
                        .addComponent(jButton3)
                        .addComponent(jButton9))
                    .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(23, 23, 23))
        );

        browseScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        browseScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        browseCardsPanel.setBackground(new java.awt.Color(255, 255, 255));
        browseCardsPanel.setAutoscrolls(true);
        browseCardsPanel.setLayout(new java.awt.GridLayout(1, 0));
        browseScrollPane.setViewportView(browseCardsPanel);

        jLabel6.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel6.setText("All Recipes");

        javax.swing.GroupLayout browsePanelLayout = new javax.swing.GroupLayout(browsePanel);
        browsePanel.setLayout(browsePanelLayout);
        browsePanelLayout.setHorizontalGroup(
            browsePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(browseTopPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, browsePanelLayout.createSequentialGroup()
                .addContainerGap(64, Short.MAX_VALUE)
                .addGroup(browsePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(browseScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 1101, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60))
        );
        browsePanelLayout.setVerticalGroup(
            browsePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(browsePanelLayout.createSequentialGroup()
                .addComponent(browseTopPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(browseScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 584, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(71, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout browseRecipesPanelLayout = new javax.swing.GroupLayout(browseRecipesPanel);
        browseRecipesPanel.setLayout(browseRecipesPanelLayout);
        browseRecipesPanelLayout.setHorizontalGroup(
            browseRecipesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(browsePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        browseRecipesPanelLayout.setVerticalGroup(
            browseRecipesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(browsePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        basePanel.add(browseRecipesPanel, "card4");

        myHistoryPanel.setBackground(new java.awt.Color(255, 255, 255));

        historyPanel.setBackground(new java.awt.Color(255, 255, 255));

        jTextField2.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jTextField2.setForeground(new java.awt.Color(102, 102, 102));
        jTextField2.setText("Search recipe in your history");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel4.setText("Sort By:");

        jButton2.setBackground(new java.awt.Color(0, 0, 0));
        jButton2.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 204, 0));
        jButton2.setText(" Name ↑");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(0, 0, 0));
        jButton4.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 204, 0));
        jButton4.setText("Time ↑");
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(102, 0, 51));
        jButton5.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Clear History");
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton10.setBackground(new java.awt.Color(0, 0, 0));
        jButton10.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jButton10.setForeground(new java.awt.Color(255, 204, 0));
        jButton10.setText("Rating ↓");
        jButton10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout historyPanelLayout = new javax.swing.GroupLayout(historyPanel);
        historyPanel.setLayout(historyPanelLayout);
        historyPanelLayout.setHorizontalGroup(
            historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(historyPanelLayout.createSequentialGroup()
                .addGap(170, 170, 170)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(109, 109, 109)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addGap(156, 156, 156))
        );
        historyPanelLayout.setVerticalGroup(
            historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(historyPanelLayout.createSequentialGroup()
                .addContainerGap(56, Short.MAX_VALUE)
                .addGroup(historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jButton2)
                    .addComponent(jButton4)
                    .addComponent(jTextField2)
                    .addComponent(jButton5)
                    .addComponent(jButton10))
                .addGap(23, 23, 23))
        );

        browseHistoryPanel.setBackground(new java.awt.Color(255, 255, 255));
        browseHistoryPanel.setLayout(new java.awt.GridLayout(1, 0));
        historyScrollPane.add(browseHistoryPanel);

        jLabel8.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel8.setText("History");

        javax.swing.GroupLayout myHistoryPanelLayout = new javax.swing.GroupLayout(myHistoryPanel);
        myHistoryPanel.setLayout(myHistoryPanelLayout);
        myHistoryPanelLayout.setHorizontalGroup(
            myHistoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(myHistoryPanelLayout.createSequentialGroup()
                .addGroup(myHistoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(myHistoryPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(historyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(myHistoryPanelLayout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addGroup(myHistoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(historyScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 1101, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 55, Short.MAX_VALUE)))
                .addContainerGap())
        );
        myHistoryPanelLayout.setVerticalGroup(
            myHistoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(myHistoryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(historyPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addGap(12, 12, 12)
                .addComponent(historyScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 582, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(78, Short.MAX_VALUE))
        );

        basePanel.add(myHistoryPanel, "card3");

        recipeRequestPanel.setBackground(new java.awt.Color(255, 255, 255));

        reqUsernameLabel.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        reqUsernameLabel.setText("Username:");

        recipeTitleLabel.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        recipeTitleLabel.setText("Recipe Title:");

        vegNonvegLabel.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        vegNonvegLabel.setText("Veg/Non-veg:");

        noteLabel.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        noteLabel.setText("Dietary Notes:");

        requestBtn.setBackground(new java.awt.Color(0, 0, 0));
        requestBtn.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        requestBtn.setForeground(new java.awt.Color(255, 204, 0));
        requestBtn.setText("Request");
        requestBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                requestBtnActionPerformed(evt);
            }
        });

        reqUsernameTextField.setForeground(new java.awt.Color(51, 51, 51));
        reqUsernameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reqUsernameTextFieldActionPerformed(evt);
            }
        });

        recipeTitleTextfield.setForeground(new java.awt.Color(51, 51, 51));

        vegNonvegTextField.setForeground(new java.awt.Color(51, 51, 51));
        vegNonvegTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vegNonvegTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(reqUsernameLabel)
                    .addComponent(recipeTitleLabel)
                    .addComponent(noteLabel)
                    .addComponent(vegNonvegLabel))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(requestBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(vegNonvegTextField)
                        .addComponent(recipeTitleTextfield)
                        .addComponent(reqUsernameTextField)
                        .addComponent(noteTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)))
                .addContainerGap(113, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(reqUsernameLabel)
                    .addComponent(reqUsernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(recipeTitleLabel)
                    .addComponent(recipeTitleTextfield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(vegNonvegLabel)
                    .addComponent(vegNonvegTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(noteLabel)
                    .addComponent(noteTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addComponent(requestBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80))
        );

        reqHistoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Username", "Recipe Title", "Veg/Non-Veg", "Dietary Notes", "Date", "Time", "Status"
            }
        ));
        jScrollPane1.setViewportView(reqHistoryTable);

        banner.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/requestBanner.png"))); // NOI18N

        requestRecipe.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        requestRecipe.setText("Request Recipe");

        reqHistoryLabel.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        reqHistoryLabel.setText("Request History");

        jLabel10.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel10.setText("Sort By:");

        nameAsc.setBackground(new java.awt.Color(0, 0, 0));
        nameAsc.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        nameAsc.setForeground(new java.awt.Color(255, 204, 0));
        nameAsc.setText("Name (A-Z)");
        nameAsc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        nameAsc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameAscActionPerformed(evt);
            }
        });

        jButton13.setBackground(new java.awt.Color(0, 0, 0));
        jButton13.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jButton13.setForeground(new java.awt.Color(255, 204, 0));
        jButton13.setText("Name (Z-A)");
        jButton13.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout recipeRequestPanelLayout = new javax.swing.GroupLayout(recipeRequestPanel);
        recipeRequestPanel.setLayout(recipeRequestPanelLayout);
        recipeRequestPanelLayout.setHorizontalGroup(
            recipeRequestPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(recipeRequestPanelLayout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(recipeRequestPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(recipeRequestPanelLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(banner)
                        .addContainerGap(27, Short.MAX_VALUE))
                    .addGroup(recipeRequestPanelLayout.createSequentialGroup()
                        .addGroup(recipeRequestPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(recipeRequestPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1099, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(requestRecipe))
                            .addGroup(recipeRequestPanelLayout.createSequentialGroup()
                                .addComponent(reqHistoryLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(nameAsc)
                                .addGap(18, 18, 18)
                                .addComponent(jButton13)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        recipeRequestPanelLayout.setVerticalGroup(
            recipeRequestPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(recipeRequestPanelLayout.createSequentialGroup()
                .addGroup(recipeRequestPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(recipeRequestPanelLayout.createSequentialGroup()
                        .addGap(229, 229, 229)
                        .addComponent(jLabel1))
                    .addGroup(recipeRequestPanelLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(requestRecipe)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(recipeRequestPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(banner)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(33, 33, 33)
                .addComponent(reqHistoryLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(recipeRequestPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameAsc)
                    .addComponent(jButton13)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );

        basePanel.add(recipeRequestPanel, "card2");

        viewPanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1157, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 685, Short.MAX_VALUE)
        );

        jScrollPane5.setViewportView(jPanel4);

        backButton.setBackground(new java.awt.Color(102, 0, 51));
        backButton.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        backButton.setForeground(new java.awt.Color(255, 255, 255));
        backButton.setText("Back");
        backButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout viewPanelLayout = new javax.swing.GroupLayout(viewPanel);
        viewPanel.setLayout(viewPanelLayout);
        viewPanelLayout.setHorizontalGroup(
            viewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(viewPanelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(viewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(backButton)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 1134, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        viewPanelLayout.setVerticalGroup(
            viewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(viewPanelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(backButton)
                .addGap(48, 48, 48)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 660, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        basePanel.add(viewPanel, "card5");

        javax.swing.GroupLayout userPanelLayout = new javax.swing.GroupLayout(userPanel);
        userPanel.setLayout(userPanelLayout);
        userPanelLayout.setHorizontalGroup(
            userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(navMainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(basePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        userPanelLayout.setVerticalGroup(
            userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, userPanelLayout.createSequentialGroup()
                .addComponent(navMainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(basePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(userPanel, "card2");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void setupSearchFieldListeners() {
        setupBrowseSearchField();
        setupHistorySearchField();
        setupAdminSearchField();
    }
    
    private javax.swing.JPanel createRecipeCard(RecipeData r) {
        if (r == null) {
            System.err.println("Error: RecipeData is null!");
            return new javax.swing.JPanel(); // Return empty panel
        }

        javax.swing.JPanel card = new javax.swing.JPanel();
        card.setPreferredSize(new java.awt.Dimension(220, 260));
        card.setBackground(java.awt.Color.WHITE);
        card.setBorder(javax.swing.BorderFactory.createLineBorder(
            new java.awt.Color(230, 230, 230), 1, true));
        card.setLayout(new java.awt.BorderLayout(0, 0)); 

        javax.swing.JLabel imgLabel = new javax.swing.JLabel();
        imgLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imgLabel.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        imgLabel.setOpaque(true);
        imgLabel.setBackground(new Color(245, 245, 245));

        String imgPath = r.getImagePath();
        if (imgPath != null) {
            java.net.URL imgUrl = getClass().getResource(imgPath);
            if (imgUrl != null) {
                javax.swing.ImageIcon icon = new javax.swing.ImageIcon(imgUrl);
                java.awt.Image scaled = icon.getImage().getScaledInstance(
                    218, 150, java.awt.Image.SCALE_SMOOTH);
                imgLabel.setIcon(new javax.swing.ImageIcon(scaled));
            } else {
                imgLabel.setText("No image");
            }
        } else {
            imgLabel.setText("No image");
        }

        javax.swing.JLabel lblTitle = new javax.swing.JLabel(
            r.getTitle() != null ? r.getTitle() : "Untitled");
        lblTitle.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 15));

        String infoText = String.format(
            "%s • %s • %d min • %.1f ★",
            r.getCuisine() != null ? r.getCuisine() : "Cuisine",
            r.getDifficulty() != null ? r.getDifficulty() : "Difficulty",
            r.getPrepTime(),
            r.getRating()
        );
        javax.swing.JLabel lblInfo = new javax.swing.JLabel(infoText);
        lblInfo.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12)); 
        lblInfo.setForeground(new java.awt.Color(120, 120, 120));

        javax.swing.JPanel center = new javax.swing.JPanel();
        center.setBackground(java.awt.Color.WHITE);
        center.setLayout(new javax.swing.BoxLayout(center, javax.swing.BoxLayout.Y_AXIS));
        center.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 5, 10)); 
        center.add(lblTitle);
        center.add(javax.swing.Box.createVerticalStrut(3));
        center.add(lblInfo);

        javax.swing.JButton btnView = new javax.swing.JButton("View");
        btnView.setBackground(viewNormalBg);
        btnView.setForeground(viewNormalFg);
        btnView.setFocusPainted(false);
        btnView.setBorderPainted(false);
        btnView.setContentAreaFilled(true);
        btnView.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 14));
        btnView.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnView.setPreferredSize(new java.awt.Dimension(80, 30));

        btnView.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnView.setBackground(viewHoverBg);
                btnView.setForeground(viewHoverFg);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btnView.setBackground(viewNormalBg);
                btnView.setForeground(viewNormalFg);
            }
        });

        javax.swing.JPanel bottom = new javax.swing.JPanel(
            new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 5));
        bottom.setBackground(java.awt.Color.WHITE);
        bottom.add(btnView);

        card.add(imgLabel, java.awt.BorderLayout.NORTH);
        card.add(center, java.awt.BorderLayout.CENTER);
        card.add(bottom, java.awt.BorderLayout.SOUTH);

        btnView.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                try {
                    System.out.println("View button clicked for recipe: " + r.getTitle());
                    controller.addToHistory(r);  // use controller, not direct method
                    openRecipeView(r);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    javax.swing.JOptionPane.showMessageDialog(
                        AppViewFrame.this,
                        "Error opening recipe: " + ex.getMessage(),
                        "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        return card;
    }


    private void openRecipeView(RecipeData recipe) {
        if (recipe == null) {
            System.err.println("Cannot open view: recipe is null");
            return;
        }

        try {
            currentViewingRecipe = recipe;
            addToHistory(recipe);   

            CardLayout baseCL = (CardLayout) basePanel.getLayout();
            baseCL.show(basePanel, "card5");           

            basePanel.revalidate();
            basePanel.repaint();

            javax.swing.SwingUtilities.invokeLater(() -> {
                baseCL.show(basePanel, "card5");
                basePanel.revalidate();
                basePanel.repaint();
            });

            populateViewPanel(recipe);

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                "Error opening recipe view:\n" + e.getMessage(),
                "View Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void populateViewPanel(RecipeData recipe) {
        if (recipe == null) {
            System.err.println("Recipe is null");
            return;
        }

        jScrollPane5.setBorder(null);
        jScrollPane5.setBackground(viewPanel.getBackground());
        jScrollPane5.getViewport().setBackground(viewPanel.getBackground());
        viewPanel.setBorder(null);

        jPanel4.removeAll();
        jPanel4.setLayout(new java.awt.GridBagLayout());
        jPanel4.setBackground(viewPanel.getBackground());

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(12, 24, 20, 24);
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;

        javax.swing.JLabel titleLabel = new javax.swing.JLabel(
            recipe.getTitle() != null ? recipe.getTitle().trim() : "Recipe");
        titleLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 30));
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel4.add(titleLabel, gbc);
        
        gbc.gridy = 2;
        gbc.gridx = 1; // under right column
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        gbc.fill = java.awt.GridBagConstraints.NONE; 

        javax.swing.JButton markCookedBtn = new javax.swing.JButton("Mark Cooked");
        markCookedBtn.setBackground(new java.awt.Color(0, 0, 0));
        markCookedBtn.setForeground(new java.awt.Color(255, 204, 0));
        markCookedBtn.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 14));
        markCookedBtn.setFocusPainted(false);
        markCookedBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        markCookedBtn.setPreferredSize(new java.awt.Dimension(130, 36));

        markCookedBtn.addActionListener(e -> {
           controller.markRecipeAsCooked();
           updateHomeStats();  
           javax.swing.JOptionPane.showMessageDialog(
            this,
            "Recipe marked as cooked successfully!",
            "Success",
            javax.swing.JOptionPane.INFORMATION_MESSAGE
        );
        });


        jPanel4.add(markCookedBtn, gbc);

        // ========== vertical glue ==========
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gbc.anchor = java.awt.GridBagConstraints.NORTH;
        jPanel4.add(new javax.swing.JLabel(), gbc);

        // Reset for columns
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;

        // ───────────────────────────────
        // LEFT COLUMN (Image + Info)
        // ───────────────────────────────
        gbc.gridx = 0;
        gbc.weightx = 0.55;           // left side gets more space
        gbc.fill = java.awt.GridBagConstraints.BOTH;

        javax.swing.JPanel leftPanel = new javax.swing.JPanel();
        leftPanel.setLayout(new javax.swing.BoxLayout(leftPanel, javax.swing.BoxLayout.Y_AXIS));
        leftPanel.setBackground(jPanel4.getBackground());
        leftPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 10, 20));

            // Image
            javax.swing.JLabel imgLabel = new javax.swing.JLabel();
            imgLabel.setAlignmentX(0.0f);
            imgLabel.setPreferredSize(new java.awt.Dimension(520, 380));
            imgLabel.setMaximumSize(new java.awt.Dimension(520, 380));
            imgLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            imgLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220,220,220), 1));

            // ← put your image loading code here (same as before)
            if (recipe.getImagePath() != null && !recipe.getImagePath().isBlank()) {
                try {
                    java.net.URL url = getClass().getResource(recipe.getImagePath());
                    if (url != null) {
                        javax.swing.ImageIcon icon = new javax.swing.ImageIcon(url);
                        java.awt.Image scaled = icon.getImage().getScaledInstance(520, 380, java.awt.Image.SCALE_SMOOTH);
                        imgLabel.setIcon(new javax.swing.ImageIcon(scaled));
                    } else {
                        imgLabel.setText("Image not found");
                    }
                } catch (Exception e) {
                    imgLabel.setText("Image error");
                }
            } else {
                imgLabel.setText("No image");
            }
            leftPanel.add(imgLabel);
            leftPanel.add(javax.swing.Box.createVerticalStrut(18));

            // Info line
            String info = String.format("%s  •  %s  •  %d min  •  %.1f ★",
                recipe.getCuisine() != null ? recipe.getCuisine() : "—",
                recipe.getDifficulty() != null ? recipe.getDifficulty() : "—",
                recipe.getPrepTime(),
                recipe.getRating());
            javax.swing.JLabel infoLabel = new javax.swing.JLabel(info);
            infoLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 19));
            infoLabel.setForeground(new java.awt.Color(100, 100, 100));
            infoLabel.setAlignmentX(0.0f);
            leftPanel.add(infoLabel);

        jPanel4.add(leftPanel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.45;
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.anchor = java.awt.GridBagConstraints.NORTH;

        javax.swing.JPanel rightPanel = new javax.swing.JPanel();
        rightPanel.setLayout(new javax.swing.BoxLayout(rightPanel, javax.swing.BoxLayout.Y_AXIS));
        rightPanel.setBackground(jPanel4.getBackground());
        rightPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 10, 0));

            // Ingredients
            rightPanel.add(createSectionLabel("Ingredients"));
            rightPanel.add(createContentTextArea(recipe.getIngredients() != null ? recipe.getIngredients() : "No ingredients listed."));

            rightPanel.add(javax.swing.Box.createVerticalStrut(40));

            // Process
            rightPanel.add(createSectionLabel("Process"));
            rightPanel.add(createContentTextArea(recipe.getProcess() != null ? recipe.getProcess() : "No instructions available."));

        jPanel4.add(rightPanel, gbc);

        jPanel4.revalidate();
        jPanel4.repaint();
        jScrollPane5.revalidate();
        jScrollPane5.repaint();
    }

    // Helpers
    
    private void incrementLabelNumber(javax.swing.JLabel label, int delta) {
        if (label == null) return;
        String text = label.getText().trim();
        int current = 0;
        try {
            current = Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            current = 0;
        }
        int updated = current + delta;
        if (updated < 0) {
            updated = 0;
        }
        label.setText(String.valueOf(updated));
    }
    
    private void handleRecipeCooked(RecipeData recipe) {
        try {
        // Update model through controller
            controller.markRecipeAsCooked();
            controller.addToHistory(recipe);

            // Update UI
            updateHomeStats();
            loadHistoryCards();

            javax.swing.JOptionPane.showMessageDialog(
                this,
                "Marked as cooked!",
                "Success",
                javax.swing.JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(
                this,
                "Could not mark as cooked: " + ex.getMessage(),
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private javax.swing.JLabel createSectionLabel(String text) {
        javax.swing.JLabel lbl = new javax.swing.JLabel(text);
        lbl.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 22));
        lbl.setAlignmentX(0.0f);
        lbl.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 10, 0));
        return lbl;
    }

    private javax.swing.JTextArea createContentTextArea(String content) {
        javax.swing.JTextArea ta = new javax.swing.JTextArea(content);
        ta.setEditable(false);
        ta.setFocusable(false);
        ta.setOpaque(false);
        ta.setBorder(null);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 15));
        ta.setMargin(new java.awt.Insets(6, 0, 12, 0));
        ta.setAlignmentX(0.0f);
        return ta;
    }
   
    private void addToHistory(RecipeData r) {
        controller.addToHistory(r);
        loadHistoryCards();
    }

    private void loadAdminRequestsTable() {
        javax.swing.table.DefaultTableModel dtm =
            (javax.swing.table.DefaultTableModel) adminReqTable.getModel();
        dtm.setRowCount(0);

        for (RecipeRequest req : controller.getAllRequests()) {
            dtm.addRow(new Object[] {
                req.getUsername(),
                req.getTitle(),
                req.getVegNonVeg(),
                req.getNotes(),
                req.getDate(),
                req.getTime(),
                req.getStatus()
            });
        }
    }
    
    private void loadAdminRecipesTable() {
        javax.swing.table.DefaultTableModel dtm =
            (javax.swing.table.DefaultTableModel) adminRecipeTable.getModel();
        dtm.setRowCount(0);

        for (RecipeData r : controller.getAllRecipes()) {
            dtm.addRow(new Object[] {
                r.getId(),
                r.getTitle(),
                r.getCuisine(),
                r.getDifficulty(),
                r.getPrepTime(),
                r.getRating()
            });
        }
    }
    
    private void loadUserRequestHistoryTable() {
        javax.swing.table.DefaultTableModel dtm =
                (javax.swing.table.DefaultTableModel) reqHistoryTable.getModel();
        dtm.setRowCount(0);

        for (RecipeRequest req : controller.getAllRequests()) {
            dtm.addRow(new Object[]{
                    req.getUsername(),
                    req.getTitle(),
                    req.getVegNonVeg(),
                    req.getNotes(),
                    req.getDate(),
                    req.getTime(),
                    req.getStatus()
            });
        }
    }
    
    private void displayRecipesInBrowse(List<RecipeData> recipes) {
        browseCardsPanel.removeAll();
        for (RecipeData recipe : recipes) {
            browseCardsPanel.add(createRecipeCard(recipe));
        }
        browseCardsPanel.revalidate();
        browseCardsPanel.repaint();
    }
    
        private void setupBrowseSearchField() {
        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (jTextField1.getText().equals("Search any recipe")) {
                    jTextField1.setText("");
                    jTextField1.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (jTextField1.getText().trim().isEmpty()) {
                    jTextField1.setText("Search any recipe");
                    jTextField1.setForeground(new Color(102, 102, 102));
                }
            }
        });
    }
        
    private void setupHistorySearchField() {
        jTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (jTextField2.getText().equals("Search recipe in your history")) {
                    jTextField2.setText("");
                    jTextField2.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (jTextField2.getText().trim().isEmpty()) {
                    jTextField2.setText("Search recipe in your history");
                    jTextField2.setForeground(new Color(102, 102, 102));
                }
            }
        });
    }
    
    private void setupAdminSearchField() {
        jTextField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (jTextField3.getText().equals("Search any recipe")) {
                    jTextField3.setText("");
                    jTextField3.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (jTextField3.getText().trim().isEmpty()) {
                    jTextField3.setText("Search any recipe");
                    jTextField3.setForeground(new Color(102, 102, 102));
                }
            }
        });
    }
    
    // Helper method to sort request table
    private void sortRequestTable(List<RecipeData> sortedRecipes) {
        // Get all requests
        Queue<RecipeRequest> allRequests = controller.getAllRequests();
        List<RecipeRequest> requestList = new ArrayList<>(allRequests);

        // Sort requests based on sorted recipes order
        requestList.sort((r1, r2) -> {
            int index1 = -1, index2 = -1;
            for (int i = 0; i < sortedRecipes.size(); i++) {
                if (sortedRecipes.get(i).getTitle().equalsIgnoreCase(r1.getTitle())) {
                    index1 = i;
                }
                if (sortedRecipes.get(i).getTitle().equalsIgnoreCase(r2.getTitle())) {
                    index2 = i;
                }
            }
            return Integer.compare(index1, index2);
        });

        // Update table
        javax.swing.table.DefaultTableModel dtm = 
            (javax.swing.table.DefaultTableModel) reqHistoryTable.getModel();
        dtm.setRowCount(0);

        for (RecipeRequest req : requestList) {
            dtm.addRow(new Object[] {
                req.getUsername(),
                req.getTitle(),
                req.getVegNonVeg(),
                req.getNotes(),
                req.getDate(),
                req.getTime(),
                req.getStatus()
            });
        }
    }
    
    private void sortRequestHistoryByNameDesc() {
        java.util.List<RecipeRequest> list =
                new java.util.ArrayList<>(controller.getAllRequests());

        list.sort((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle())); // A-Z

        // Manual reverse
        java.util.List<RecipeRequest> reversed = new java.util.ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            reversed.add(list.get(i));
        }

        DefaultTableModel dtm = (DefaultTableModel) reqHistoryTable.getModel();
        dtm.setRowCount(0);
        for (RecipeRequest req : reversed) {
            dtm.addRow(new Object[]{
                req.getUsername(),
                req.getTitle(),
                req.getVegNonVeg(),
                req.getNotes(),
                req.getDate(),
                req.getTime(),
                req.getStatus()
            });
        }
    }
       
    private void passwordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passwordFieldActionPerformed

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        handleLogin();
    }//GEN-LAST:event_loginButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Call controller method
        List<RecipeData> sorted = controller.sortRecipesByName();

        // Update view
        browseCardsPanel.removeAll();
        for (RecipeData r : sorted) {
            browseCardsPanel.add(createRecipeCard(r));
        }
        browseCardsPanel.revalidate();
        browseCardsPanel.repaint();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Call controller method
        List<RecipeData> sorted = controller.sortRecipesByTime();

        // Update view
        browseCardsPanel.removeAll();
        for (RecipeData r : sorted) {
            browseCardsPanel.add(createRecipeCard(r));
        }
        browseCardsPanel.revalidate();
        browseCardsPanel.repaint();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void loginButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loginButtonMouseEntered
        loginButton.setBackground(loginHoverBg);
        loginButton.setForeground(loginHoverFg);
    }//GEN-LAST:event_loginButtonMouseEntered

    private void loginButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loginButtonMouseExited
        loginButton.setBackground(loginNormalBg);
        loginButton.setForeground(loginNormalFg);
    }//GEN-LAST:event_loginButtonMouseExited

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        int choice = javax.swing.JOptionPane.showConfirmDialog(this,"Are you sure you want to log out?","Confirm Logout",javax.swing.JOptionPane.YES_NO_OPTION,javax.swing.JOptionPane.QUESTION_MESSAGE);
        if (choice == javax.swing.JOptionPane.YES_OPTION) {
            CardLayout cl = (CardLayout) getContentPane().getLayout();
            cl.show(getContentPane(), "card3");
            usernameField.setText("");
            passwordField.setText("");
        }
    }//GEN-LAST:event_logoutButtonActionPerformed

    private void myHistoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myHistoryButtonActionPerformed

        CardLayout cl = (CardLayout) basePanel.getLayout();
        cl.show(basePanel, "card3");
    }//GEN-LAST:event_myHistoryButtonActionPerformed

    private void browseRecipeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseRecipeButtonActionPerformed
  
        CardLayout cl = (CardLayout) basePanel.getLayout();
        cl.show(basePanel, "card4");   
        loadHomeCards();
    }//GEN-LAST:event_browseRecipeButtonActionPerformed

    private void homeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeButtonActionPerformed
     
        CardLayout cl = (CardLayout) basePanel.getLayout();
        cl.show(basePanel, "card6");   
        loadHomeCards();
    }//GEN-LAST:event_homeButtonActionPerformed

    private void homeButtonFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_homeButtonFocusGained
        CardLayout cl = (CardLayout) basePanel.getLayout();
        cl.show(basePanel, "card2");
    }//GEN-LAST:event_homeButtonFocusGained

    private void logoutButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutButtonMouseEntered
        logoutButton.setBackground(loginHoverBg);
        logoutButton.setForeground(loginHoverFg);
    }//GEN-LAST:event_logoutButtonMouseEntered

    private void logoutButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutButtonMouseExited
        logoutButton.setBackground(loginNormalBg);
        logoutButton.setForeground(loginNormalFg);
    }//GEN-LAST:event_logoutButtonMouseExited

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        String query = jTextField1.getText().trim();
    
        if (query.equals("Search any recipe")) {
            query = "";
        }

        // Call controller method
        List<RecipeData> results = controller.searchRecipesByTitle(query);

        // Update view
        browseCardsPanel.removeAll();
        for (RecipeData r : results) {
            browseCardsPanel.add(createRecipeCard(r));
        }
        browseCardsPanel.revalidate();
        browseCardsPanel.repaint();
    }//GEN-LAST:event_jTextField1ActionPerformed
    
    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        String query = jTextField2.getText().trim();
    
        if (query.equals("Search recipe in your history")) {
            query = "";
        }

        // Call controller method
        List<RecipeData> results = controller.searchHistory(query);

        // Update view
        browseHistoryPanel.removeAll();
        for (RecipeData r : results) {
            browseHistoryPanel.add(createRecipeCard(r));
        }
        browseHistoryPanel.revalidate();
        browseHistoryPanel.repaint();
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Call controller method
        List<RecipeData> sorted = controller.sortRecipesByName();

        // Filter only history items
        List<RecipeData> historyItems = controller.getHistory();
        List<RecipeData> sortedHistory = new ArrayList<>();
        for (RecipeData r : sorted) {
            if (historyItems.stream().anyMatch(h -> h.getId() == r.getId())) {
                sortedHistory.add(r);
            }
        }

        // Update view
        browseHistoryPanel.removeAll();
        for (RecipeData r : sortedHistory) {
            browseHistoryPanel.add(createRecipeCard(r));
        }
        browseHistoryPanel.revalidate();
        browseHistoryPanel.repaint();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // Call controller method
        List<RecipeData> sorted = controller.sortRecipesByTime();

        // Filter only history items
        List<RecipeData> historyItems = controller.getHistory();
        List<RecipeData> sortedHistory = new ArrayList<>();
        for (RecipeData r : sorted) {
            if (historyItems.stream().anyMatch(h -> h.getId() == r.getId())) {
                sortedHistory.add(r);
            }
        }

        // Update view
        browseHistoryPanel.removeAll();
        for (RecipeData r : sortedHistory) {
            browseHistoryPanel.add(createRecipeCard(r));
        }
        browseHistoryPanel.revalidate();
        browseHistoryPanel.repaint();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        controller.clearHistory();
        loadHistoryCards();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void requestBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_requestBtnActionPerformed
        String username = reqUsernameTextField.getText().trim();
        String title    = recipeTitleTextfield.getText().trim();
        String vegNon   = vegNonvegTextField.getText().trim();
        String notes    = noteTextField.getText().trim();

        String result = controller.addRecipeRequest(username, title, vegNon, notes);

        if ("success".equals(result)) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Request submitted successfully!",
                    "Success",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
            );

            // reload history from controller for both MVC correctness and sync
            loadUserRequestHistoryTable();

            // Clear text fields
            reqUsernameTextField.setText("");
            recipeTitleTextfield.setText("");
            vegNonvegTextField.setText("");
            noteTextField.setText("");

            // Update statistics labels properly from model
            updateHomeStats();

        } else {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    result,
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_requestBtnActionPerformed

    private void myRecipeRequestButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myRecipeRequestButtonActionPerformed
     
        CardLayout cl = (CardLayout) basePanel.getLayout();
        cl.show(basePanel, "card2");  

    }//GEN-LAST:event_myRecipeRequestButtonActionPerformed

    private void vegNonvegTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vegNonvegTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_vegNonvegTextFieldActionPerformed

    private void reqUsernameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reqUsernameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_reqUsernameTextFieldActionPerformed

    private void manageRecipesBtnFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_manageRecipesBtnFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_manageRecipesBtnFocusGained

    private void manageRecipesBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageRecipesBtnActionPerformed
        CardLayout cl = (CardLayout) baseAdminPAnel.getLayout();
        cl.show(baseAdminPAnel, "card2");      
        loadAdminRecipesTable();
    }//GEN-LAST:event_manageRecipesBtnActionPerformed

    private void manageRequestBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageRequestBtnActionPerformed
        CardLayout cl = (CardLayout) baseAdminPAnel.getLayout();
        cl.show(baseAdminPAnel, "card3");
        loadAdminRequestsTable(); 
    }//GEN-LAST:event_manageRequestBtnActionPerformed

    private void logoutButtonAdminMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutButtonAdminMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_logoutButtonAdminMouseEntered

    private void logoutButtonAdminMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutButtonAdminMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_logoutButtonAdminMouseExited

    private void logoutButtonAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonAdminActionPerformed
        int choice = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to log out?",
            "Confirm Logout",
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.QUESTION_MESSAGE);

        if (choice == javax.swing.JOptionPane.YES_OPTION) {
            CardLayout cl = (CardLayout) getContentPane().getLayout();
            cl.show(getContentPane(), "card3");   
            usernameField.setText("");
            passwordField.setText("");
        }
    }//GEN-LAST:event_logoutButtonAdminActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        String title = adminTitleField.getText().trim();
        String cuisine = adminCuisineField.getText().trim();
        String difficulty = adminDifficultyField.getText().trim();
        String prepTime = adminPrepTimeField.getText().trim();
        String rating = adminRatingField.getText().trim();
        String imagePath = adminImagePathField.getText().trim();
        String ingredients = adminIngredientsArea.getText().trim();
        String process = adminProcessArea.getText().trim();

        String result = controller.addRecipe(
                title, cuisine, difficulty,
                prepTime, rating, imagePath,
                ingredients, process
        );

        if ("success".equals(result)) {
            loadAdminRecipesTable();
            loadHomeCards();
            clearRecipeForm();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Recipe added successfully!",
                    "Success",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } else {
            javax.swing.JOptionPane.showMessageDialog(this,
                    result,
                    "Validation Error",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        try {
            Integer recipeId = (Integer) adminRecipeTable.getClientProperty("selectedRecipeId");

            if (recipeId == null) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Please select a recipe from the table to update!",
                    "No Selection",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (adminTitleField.getText().trim().isEmpty() ||
                adminCuisineField.getText().trim().isEmpty() ||
                adminDifficultyField.getText().trim().isEmpty() ||
                adminPrepTimeField.getText().trim().isEmpty() ||
                adminRatingField.getText().trim().isEmpty()) {

                javax.swing.JOptionPane.showMessageDialog(this,
                    "Please fill all required fields!",
                    "Validation Error",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            String title = adminTitleField.getText().trim();
            String cuisine = adminCuisineField.getText().trim();
            String difficulty = adminDifficultyField.getText().trim();
            int prepTime = Integer.parseInt(adminPrepTimeField.getText().trim());
            double rating = Double.parseDouble(adminRatingField.getText().trim());
            String imagePath = adminImagePathField.getText().trim();
            String ingredients = adminIngredientsArea.getText().trim();
            String process = adminProcessArea.getText().trim();

            if (imagePath.isEmpty()) {
                imagePath = "/img/default.png";
            }

            boolean updated = controller.updateRecipe(recipeId, title, cuisine, 
                                                difficulty, prepTime, rating, imagePath,
                                                ingredients, process);

            if (updated) {
                loadAdminRecipesTable();
                loadHomeCards();
                clearRecipeForm();

                javax.swing.JOptionPane.showMessageDialog(this,
                    "Recipe updated successfully!",
                    "Success",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            } else {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Recipe not found!",
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Please enter valid numbers for Time and Rating!",
                "Input Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        try {
            Integer recipeId = (Integer) adminRecipeTable.getClientProperty("selectedRecipeId");

            if (recipeId == null) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Please select a recipe from the table to delete!",
                    "No Selection",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Confirm deletion
            int choice = javax.swing.JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this recipe?",
                "Confirm Delete",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE);

            if (choice == javax.swing.JOptionPane.YES_OPTION) {
                boolean deleted = controller.deleteRecipe(recipeId);

                if (deleted) {
                    loadAdminRecipesTable();
                    loadHomeCards();
                    clearRecipeForm();

                    javax.swing.JOptionPane.showMessageDialog(this,
                        "Recipe deleted successfully!",
                        "Success",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this,
                        "Recipe not found!",
                        "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (NumberFormatException ex) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Invalid Recipe ID!",
                "Input Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void updateStatusBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateStatusBtnActionPerformed
        int row = adminReqTable.getSelectedRow();
        if (row < 0) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Please select a request from the table.",
                    "No Selection",
                    javax.swing.JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Identify request using username + title from selected row
        String username = (String) adminReqTable.getValueAt(row, 0);
        String title    = (String) adminReqTable.getValueAt(row, 1);

        // Dialog is View responsibility
        String[] options = {"Pending", "Updated", "Cancelled"};
        String currentStatus = (String) adminReqTable.getValueAt(row, 6);
        String newStatus = (String) javax.swing.JOptionPane.showInputDialog(
                this,
                "Select new status:",
                "Update Status",
                javax.swing.JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                currentStatus
        );

        if (newStatus == null) {
            // dialog cancelled
            return;
        }

        // Find matching request object via controller
        RecipeRequest target = null;
        for (RecipeRequest req : controller.getAllRequests()) {
            if (req.getUsername().equals(username) && req.getTitle().equals(title)) {
                target = req;
                break;
            }
        }

        if (target == null) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Request not found.",
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        boolean ok = controller.updateRequestStatus(target, newStatus);
        if (!ok) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Status must be Pending, Updated, or Cancelled only.",
                    "Invalid Status",
                    javax.swing.JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Sync admin + user views from same controller data
        loadAdminRequestsTable();
        loadUserRequestHistoryTable();

        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Status updated successfully.",
                "Success",
                javax.swing.JOptionPane.INFORMATION_MESSAGE
        );
    }//GEN-LAST:event_updateStatusBtnActionPerformed

    private void deleteRequestBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteRequestBtnActionPerformed
        int row = adminReqTable.getSelectedRow();
        if (row < 0) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Please select a request from the table.",
                    "No Selection",
                    javax.swing.JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String username = (String) adminReqTable.getValueAt(row, 0);
        String title    = (String) adminReqTable.getValueAt(row, 1);

        int confirm = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this request?",
                "Confirm Delete",
                javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (confirm != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }

        RecipeRequest target = null;
        for (RecipeRequest req : controller.getAllRequests()) {
            if (req.getUsername().equals(username) && req.getTitle().equals(title)) {
                target = req;
                break;
            }
        }

        if (target == null) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Request not found.",
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        boolean removed = controller.removeRequest(target);
        if (!removed) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Failed to delete request.",
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Sync admin + user views after delete
        loadAdminRequestsTable();
        loadUserRequestHistoryTable();
        updateHomeStats();
    }//GEN-LAST:event_deleteRequestBtnActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        CardLayout baseCL = (CardLayout) basePanel.getLayout();
        baseCL.show(basePanel, "card4");
        loadHomeCards();
    }//GEN-LAST:event_backButtonActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // Call controller method
        List<RecipeData> sorted = controller.sortRecipesByRating();

        // Update view
        browseCardsPanel.removeAll();
        for (RecipeData r : sorted) {
            browseCardsPanel.add(createRecipeCard(r));
        }
        browseCardsPanel.revalidate();
        browseCardsPanel.repaint();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // Call controller method
        List<RecipeData> sorted = controller.sortRecipesByRating();

        // Filter only history items
        List<RecipeData> historyItems = controller.getHistory();
        List<RecipeData> sortedHistory = new ArrayList<>();
        for (RecipeData r : sorted) {
            if (historyItems.stream().anyMatch(h -> h.getId() == r.getId())) {
                sortedHistory.add(r);
            }
        }

        // Update view
        browseHistoryPanel.removeAll();
        for (RecipeData r : sortedHistory) {
            browseHistoryPanel.add(createRecipeCard(r));
        }
        browseHistoryPanel.revalidate();
        browseHistoryPanel.repaint();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void nameAscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameAscActionPerformed
        List<RecipeData> sorted = controller.sortRecipesByName();
        sortRequestTable(sorted);
    }//GEN-LAST:event_nameAscActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // Call controller method
        List<RecipeData> sorted = controller.sortRecipesByName();

        // Update view
        javax.swing.table.DefaultTableModel dtm = 
            (javax.swing.table.DefaultTableModel) adminRecipeTable.getModel();
        dtm.setRowCount(0);

        for (RecipeData r : sorted) {
            dtm.addRow(new Object[] {
                r.getId(),
                r.getTitle(),
                r.getCuisine(),
                r.getDifficulty(),
                r.getPrepTime(),
                r.getRating()
            });
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        String query = jTextField3.getText().trim();
    
        if (query.equals("Search any recipe")) {
            query = "";
        }

        // Call controller method
        List<RecipeData> results = controller.searchRecipesByTitle(query);

        // Update view
        javax.swing.table.DefaultTableModel dtm = 
            (javax.swing.table.DefaultTableModel) adminRecipeTable.getModel();
        dtm.setRowCount(0);

        for (RecipeData r : results) {
            dtm.addRow(new Object[] {
                r.getId(),
                r.getTitle(),
                r.getCuisine(),
                r.getDifficulty(),
                r.getPrepTime(),
                r.getRating()
            });
        }
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // Call controller method
        List<RecipeData> sorted = controller.sortRecipesByTime();

        // Update view
        javax.swing.table.DefaultTableModel dtm = 
            (javax.swing.table.DefaultTableModel) adminRecipeTable.getModel();
        dtm.setRowCount(0);

        for (RecipeData r : sorted) {
            dtm.addRow(new Object[] {
                r.getId(),
                r.getTitle(),
                r.getCuisine(),
                r.getDifficulty(),
                r.getPrepTime(),
                r.getRating()
            });
        }
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // Call controller method
        List<RecipeData> sorted = controller.sortRecipesByRating();

        // Update view
        javax.swing.table.DefaultTableModel dtm = 
            (javax.swing.table.DefaultTableModel) adminRecipeTable.getModel();
        dtm.setRowCount(0);

        for (RecipeData r : sorted) {
            dtm.addRow(new Object[] {
                r.getId(),
                r.getTitle(),
                r.getCuisine(),
                r.getDifficulty(),
                r.getPrepTime(),
                r.getRating()
            });
        }
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        List<RecipeData> sorted = controller.getRecipesSortedByNameDesc();
        DefaultTableModel dtm = (DefaultTableModel) adminRecipeTable.getModel();
        dtm.setRowCount(0);
        for (RecipeData r : sorted) {
            dtm.addRow(new Object[]{
                r.getId(),
                r.getTitle(),
                r.getCuisine(),
                r.getDifficulty(),
                r.getPrepTime(),
                r.getRating()
            });
        }
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        clearRecipeForm();
    }//GEN-LAST:event_jButton11ActionPerformed

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new AppViewFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel adminPanel;
    private javax.swing.JButton backButton;
    private javax.swing.JLabel banner;
    private javax.swing.JLabel bannerHomePage;
    private javax.swing.JPanel bannerPanel;
    private javax.swing.JPanel baseAdminPAnel;
    private javax.swing.JPanel basePanel;
    private javax.swing.JPanel browseCardsPanel;
    private javax.swing.JPanel browseHistoryPanel;
    private javax.swing.JPanel browsePanel;
    private javax.swing.JButton browseRecipeButton;
    private javax.swing.JPanel browseRecipesPanel;
    private javax.swing.JScrollPane browseScrollPane;
    private javax.swing.JPanel browseTopPanel;
    private javax.swing.JTextField cuisineAdmin;
    private javax.swing.JLabel cuisineLabelAdmin;
    private javax.swing.JButton deleteRequestBtn;
    private javax.swing.JTextField difficultyAdmin;
    private javax.swing.JLabel difficultyLabelAdmin;
    private javax.swing.JPanel historyPanel;
    private java.awt.ScrollPane historyScrollPane;
    private javax.swing.JButton homeButton;
    private javax.swing.JPanel homePanelUser;
    private javax.swing.JTextField imagePathAdmin;
    private javax.swing.JLabel imagePathLevelAdmin;
    private javax.swing.JTextArea ingredientsAdmin;
    private javax.swing.JLabel ingredientsLabelAmin;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JButton loginButton;
    private javax.swing.JLabel loginPageImage;
    private javax.swing.JPanel loginPanel;
    private javax.swing.JLabel logo;
    private javax.swing.JLabel logoAdmin;
    private javax.swing.JButton logoutButton;
    private javax.swing.JButton logoutButtonAdmin;
    private javax.swing.JButton manageRecipesBtn;
    private javax.swing.JPanel manageRecipesPanel;
    private javax.swing.JButton manageRequestBtn;
    private javax.swing.JPanel manageRequestsPanel;
    private javax.swing.JButton myHistoryButton;
    private javax.swing.JPanel myHistoryPanel;
    private javax.swing.JButton myRecipeRequestButton;
    private javax.swing.JLabel myStatsLabel;
    private javax.swing.JLabel myStatsLabel1;
    private javax.swing.JButton nameAsc;
    private javax.swing.JPanel navMainPanel;
    private javax.swing.JPanel navMainPanelAdmin;
    private javax.swing.JPanel navigationPanel;
    private javax.swing.JPanel navigationPanel1;
    private javax.swing.JLabel noteLabel;
    private javax.swing.JTextField noteTextField;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JTextArea processAdmin;
    private javax.swing.JLabel processLabelAdmin;
    private javax.swing.JTextField ratingAdmin;
    private javax.swing.JLabel ratingLabelAdmin;
    private javax.swing.JPanel recentlyAddedPanel;
    private javax.swing.JPanel recipeRequestPanel;
    private javax.swing.JLabel recipeTitleLabel;
    private javax.swing.JTextField recipeTitleTextfield;
    private javax.swing.JLabel recipesCookedLogo;
    private javax.swing.JLabel recipesCookedNumber;
    private javax.swing.JTable recipesTable;
    private javax.swing.JLabel reqHistoryLabel;
    private javax.swing.JTable reqHistoryTable;
    private javax.swing.JTable reqTableAdmin;
    private javax.swing.JLabel reqUsernameLabel;
    private javax.swing.JTextField reqUsernameTextField;
    private javax.swing.JButton requestBtn;
    private javax.swing.JLabel requestRecipe;
    private javax.swing.JLabel requestedLabel1;
    private javax.swing.JLabel requestedLogo;
    private javax.swing.JLabel requestedNumber;
    private javax.swing.JLabel requestedNumber1;
    private javax.swing.JLabel subTitleLabel;
    private javax.swing.JTextField timeAdmin;
    private javax.swing.JLabel timeLabelAdmin;
    private javax.swing.JTextField titleAdmin;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JLabel titleLabelAdmin;
    private javax.swing.JLabel totalRecipes;
    private javax.swing.JLabel totalRecipesLogo;
    private javax.swing.JLabel totalRecipesNumber;
    private javax.swing.JButton updateStatusBtn;
    private javax.swing.JPanel userPanel;
    private javax.swing.JTextField usernameField;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JLabel vegNonvegLabel;
    private javax.swing.JTextField vegNonvegTextField;
    private javax.swing.JPanel viewPanel;
    private javax.swing.JLabel yetToCook;
    private javax.swing.JLabel yetToCookLogo;
    private javax.swing.JLabel yetToCookNumber;
    // End of variables declaration//GEN-END:variables
}

package com.expensesplitter.views;

import com.expensesplitter.dao.UserDAO;
import com.expensesplitter.models.User;
import com.expensesplitter.utils.InputValidator;
import com.expensesplitter.utils.PasswordHasher;
import com.expensesplitter.utils.SessionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Login screen for the AI-Powered Expense Splitter application.
 * Built entirely with pure JavaFX code (no FXML).
 * Extends BorderPane to provide a centered card layout.
 *
 * Features:
 *   - Email and password input fields
 *   - Login button with validation
 *   - Register link for new users
 *   - Error label for feedback
 *
 * Student : Muhammad Arham | L1F23BSSE0372
 * Package : com.expensesplitter.views
 */
public class LoginScreen extends BorderPane {

    /** UI Components */
    private TextField emailField;
    private PasswordField passwordField;
    private Label errorLabel;

    /** Data Access Object for user operations */
    private UserDAO userDAO;

    /**
     * Constructor - builds the entire login UI programmatically.
     * Creates a centered card with title, input fields, buttons, and links.
     */
    public LoginScreen() {
        // Initialize UserDAO for database operations
        userDAO = new UserDAO();

        // Create the main center card container
        VBox centerCard = createCenterCard();

        // Style the main background
        this.setStyle("-fx-background-color: #f0f2f5;");

        // Center the card in the BorderPane
        this.setCenter(centerCard);
    }

    /**
     * Creates the center card VBox containing all login UI elements.
     *
     * @return VBox with styled login form elements
     */
    private VBox createCenterCard() {
        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40));
        card.setMaxWidth(400);
        card.setStyle("-fx-background-color: white; " +
                      "-fx-background-radius: 10; " +
                      "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        // Create title label
        Label titleLabel = new Label("AI Expense Splitter");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#2c3e50"));

        // Create subtitle label
        Label subtitleLabel = new Label("Sign in to your account");
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setTextFill(Color.web("#7f8c8d"));

        // Create email field
        Label emailLabel = new Label("Email:");
        emailLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        emailLabel.setTextFill(Color.web("#34495e"));

        emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.setStyle("-fx-background-radius: 5; " +
                           "-fx-border-color: #bdc3c7; " +
                           "-fx-border-radius: 5; " +
                           "-fx-padding: 10;");
        emailField.setPrefWidth(300);

        // Create password field
        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        passwordLabel.setTextFill(Color.web("#34495e"));

        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setStyle("-fx-background-radius: 5; " +
                              "-fx-border-color: #bdc3c7; " +
                              "-fx-border-radius: 5; " +
                              "-fx-padding: 10;");
        passwordField.setPrefWidth(300);

        // Create error label (initially hidden)
        errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("Arial", 11));
        errorLabel.setWrapText(true);

        // Create login button
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #3498db; " +
                            "-fx-text-fill: white; " +
                            "-fx-background-radius: 5; " +
                            "-fx-font-weight: bold; " +
                            "-fx-font-size: 14; " +
                            "-fx-padding: 12 30;");
        loginButton.setPrefWidth(300);
        loginButton.setOnAction(e -> handleLogin());

        // Create register link
        Hyperlink registerLink = new Hyperlink("Don't have an account? Register");
        registerLink.setStyle("-fx-text-fill: #3498db; " +
                             "-fx-font-size: 12;");
        registerLink.setBorder(null);
        registerLink.setOnAction(e -> handleGoToRegister());

        // Add all elements to the card
        card.getChildren().addAll(
            titleLabel,
            subtitleLabel,
            new Label(""), // spacer
            emailLabel,
            emailField,
            passwordLabel,
            passwordField,
            errorLabel,
            loginButton,
            registerLink
        );

        return card;
    }

    /**
     * Handles the login button click event.
     * Validates inputs, checks credentials, and navigates to dashboard on success.
     */
    private void handleLogin() {
        // Get input values
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        // Clear previous error
        errorLabel.setText("");

        // Validate email
        if (!InputValidator.validateEmail(email)) {
            errorLabel.setText("Please enter a valid email address.");
            return;
        }

        // Validate password
        if (!InputValidator.validatePassword(password)) {
            errorLabel.setText("Password must be at least 6 characters.");
            return;
        }

        try {
            // Retrieve user from database by email
            User user = userDAO.getUserByEmail(email);

            // Check if user exists
            if (user == null) {
                errorLabel.setText("No account found with this email.");
                return;
            }

            // Verify password
            if (!PasswordHasher.verifyPassword(password, user.getPasswordHash())) {
                errorLabel.setText("Incorrect password. Please try again.");
                return;
            }

            // Password is correct - set session
            SessionManager.setCurrentUser(user);

            // Navigate to dashboard screen
            // Note: DashboardScreen should be created separately
            // For now, we'll navigate to a placeholder or existing DashboardView
            try {
                DashboardView dashboard = new DashboardView();
                ScreenNavigator.navigateTo(dashboard, "Dashboard - AI Expense Splitter");
            } catch (Exception ex) {
                errorLabel.setText("Error loading dashboard: " + ex.getMessage());
                ex.printStackTrace();
            }

        } catch (Exception e) {
            errorLabel.setText("Login failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles navigation to the registration screen.
     * Replaces the current scene with RegisterScreen.
     */
    private void handleGoToRegister() {
        try {
            RegisterScreen registerScreen = new RegisterScreen();
            ScreenNavigator.navigateTo(registerScreen, "Register - AI Expense Splitter");
        } catch (Exception e) {
            errorLabel.setText("Error loading registration screen: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

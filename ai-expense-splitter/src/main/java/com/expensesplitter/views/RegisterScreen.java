package com.expensesplitter.views;

import com.expensesplitter.dao.UserDAO;
import com.expensesplitter.models.User;
import com.expensesplitter.utils.InputValidator;
import com.expensesplitter.utils.PasswordHasher;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Registration screen for the AI-Powered Expense Splitter application.
 * Built entirely with pure JavaFX code (no FXML).
 * Extends VBox to provide a centered card layout.
 *
 * Features:
 *   - Name, email, password, and confirm password input fields
 *   - Register button with validation
 *   - Back to login link
 *   - Error label for feedback
 *
 * Student : Muhammad Arham | L1F23BSSE0372
 * Package : com.expensesplitter.views
 */
public class RegisterScreen extends VBox {

    /** UI Components */
    private TextField nameField;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Label errorLabel;

    /** Data Access Object for user operations */
    private UserDAO userDAO;

    /**
     * Constructor - builds the entire registration UI programmatically.
     * Creates a centered card with title, input fields, buttons, and links.
     */
    public RegisterScreen() {
        // Initialize UserDAO for database operations
        userDAO = new UserDAO();

        // Create the main center card container
        VBox centerCard = createCenterCard();

        // Style the main background
        this.setStyle("-fx-background-color: #f0f2f5;");
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(20));

        // Add the card to this VBox
        this.getChildren().add(centerCard);
    }

    /**
     * Creates the center card VBox containing all registration UI elements.
     *
     * @return VBox with styled registration form elements
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
        Label titleLabel = new Label("Create Account");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#2c3e50"));

        // Create subtitle label
        Label subtitleLabel = new Label("Join AI Expense Splitter");
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setTextFill(Color.web("#7f8c8d"));

        // Create name field
        Label nameLabel = new Label("Full Name:");
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        nameLabel.setTextFill(Color.web("#34495e"));

        nameField = new TextField();
        nameField.setPromptText("Enter your full name");
        nameField.setStyle("-fx-background-radius: 5; " +
                          "-fx-border-color: #bdc3c7; " +
                          "-fx-border-radius: 5; " +
                          "-fx-padding: 10;");
        nameField.setPrefWidth(300);

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
        passwordField.setPromptText("Create a password (min 6 characters)");
        passwordField.setStyle("-fx-background-radius: 5; " +
                              "-fx-border-color: #bdc3c7; " +
                              "-fx-border-radius: 5; " +
                              "-fx-padding: 10;");
        passwordField.setPrefWidth(300);

        // Create confirm password field
        Label confirmPasswordLabel = new Label("Confirm Password:");
        confirmPasswordLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        confirmPasswordLabel.setTextFill(Color.web("#34495e"));

        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Re-enter your password");
        confirmPasswordField.setStyle("-fx-background-radius: 5; " +
                                     "-fx-border-color: #bdc3c7; " +
                                     "-fx-border-radius: 5; " +
                                     "-fx-padding: 10;");
        confirmPasswordField.setPrefWidth(300);

        // Create error label (initially hidden)
        errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("Arial", 11));
        errorLabel.setWrapText(true);

        // Create register button
        Button registerButton = new Button("Register");
        registerButton.setStyle("-fx-background-color: #27ae60; " +
                               "-fx-text-fill: white; " +
                               "-fx-background-radius: 5; " +
                               "-fx-font-weight: bold; " +
                               "-fx-font-size: 14; " +
                               "-fx-padding: 12 30;");
        registerButton.setPrefWidth(300);
        registerButton.setOnAction(e -> handleRegister());

        // Create back to login link
        Hyperlink backToLoginLink = new Hyperlink("Already have an account? Login");
        backToLoginLink.setStyle("-fx-text-fill: #3498db; " +
                                 "-fx-font-size: 12;");
        backToLoginLink.setBorder(null);
        backToLoginLink.setOnAction(e -> handleBackToLogin());

        // Add all elements to the card
        card.getChildren().addAll(
            titleLabel,
            subtitleLabel,
            new Label(""), // spacer
            nameLabel,
            nameField,
            emailLabel,
            emailField,
            passwordLabel,
            passwordField,
            confirmPasswordLabel,
            confirmPasswordField,
            errorLabel,
            registerButton,
            backToLoginLink
        );

        return card;
    }

    /**
     * Handles the register button click event.
     * Validates all inputs, checks password match, hashes password, creates user,
     * and navigates to login screen on success.
     */
    private void handleRegister() {
        // Get input values
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Clear previous error
        errorLabel.setText("");

        // Validate name
        if (!InputValidator.validateName(name)) {
            errorLabel.setText("Please enter your full name.");
            return;
        }

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

        // Validate confirm password
        if (!InputValidator.validatePassword(confirmPassword)) {
            errorLabel.setText("Please confirm your password.");
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match. Please try again.");
            return;
        }

        try {
            // Check if email already exists
            User existingUser = userDAO.getUserByEmail(email);
            if (existingUser != null) {
                errorLabel.setText("An account with this email already exists.");
                return;
            }

            // Hash the password
            String passwordHash = PasswordHasher.hashPassword(password);
            if (passwordHash == null) {
                errorLabel.setText("Error hashing password. Please try again.");
                return;
            }

            // Create new User object
            User newUser = new User();
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setPasswordHash(passwordHash);

            // Insert user into database
            boolean success = userDAO.createUser(newUser);

            if (success) {
                // Registration successful - navigate to login screen
                LoginScreen loginScreen = new LoginScreen();
                ScreenNavigator.navigateTo(loginScreen, "Login - AI Expense Splitter");
            } else {
                errorLabel.setText("Registration failed. Please try again.");
            }

        } catch (Exception e) {
            errorLabel.setText("Registration error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles navigation back to the login screen.
     * Replaces the current scene with LoginScreen.
     */
    private void handleBackToLogin() {
        try {
            LoginScreen loginScreen = new LoginScreen();
            ScreenNavigator.navigateTo(loginScreen, "Login - AI Expense Splitter");
        } catch (Exception e) {
            errorLabel.setText("Error loading login screen: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

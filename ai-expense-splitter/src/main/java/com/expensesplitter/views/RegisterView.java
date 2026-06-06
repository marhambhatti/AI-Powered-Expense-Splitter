package com.expensesplitter.views;

import com.expensesplitter.models.User;
import com.expensesplitter.dao.UserDAO;
import com.expensesplitter.utils.InputValidator;
import com.expensesplitter.utils.PasswordHasher;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.SQLException;

/**
 * Programmatic JavaFX view for the Registration screen.
 * Replaces the FXML-based Register layout and controller.
 *
 * Student : Muhammad Arham | L1F23BSSE0372
 * Package : com.expensesplitter.views
 */
public class RegisterView extends StackPane {

    private TextField nameField;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Label errorLabel;
    private Button registerButton;
    private Hyperlink loginLink;

    private final UserDAO userDAO = new UserDAO();

    /**
     * Constructs the RegisterView and initializes its layout programmatically.
     */
    public RegisterView() {
        initUI();
    }

    /**
     * Initializes the user interface components and layout of the registration card.
     */
    private void initUI() {
        // Set background styling matching the global stylesheet
        setStyle("-fx-background-color: #F5F7FA;");

        // Main Card layout container
        VBox card = new VBox(20);
        card.getStyleClass().add("card");
        card.setMaxWidth(450);
        card.setAlignment(Pos.CENTER);

        // Header section (Title & Subtitle)
        VBox header = new VBox(5);
        header.setAlignment(Pos.CENTER);
        Label titleLabel = new Label("AI Expense Splitter");
        titleLabel.getStyleClass().add("heading");
        Label subtitleLabel = new Label("Create a new account");
        subtitleLabel.setStyle("-fx-text-fill: #7F8C8D; -fx-font-size: 14px;");
        header.getChildren().addAll(titleLabel, subtitleLabel);

        // Form Fields (Name, Email, Password, Confirm Password)
        VBox form = new VBox(12);
        form.setAlignment(Pos.CENTER_LEFT);

        VBox nameBox = new VBox(5);
        Label nameLabel = new Label("Full Name");
        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #34495E;");
        nameField = new TextField();
        nameField.setPromptText("Enter your name");
        nameField.getStyleClass().add("text-field");
        nameField.setMaxWidth(Double.MAX_VALUE);
        nameBox.getChildren().addAll(nameLabel, nameField);

        VBox emailBox = new VBox(5);
        Label emailLabel = new Label("Email Address");
        emailLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #34495E;");
        emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.getStyleClass().add("text-field");
        emailField.setMaxWidth(Double.MAX_VALUE);
        emailBox.getChildren().addAll(emailLabel, emailField);

        VBox passwordBox = new VBox(5);
        Label passwordLabel = new Label("Password");
        passwordLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #34495E;");
        passwordField = new PasswordField();
        passwordField.setPromptText("Choose a password (min 6 chars)");
        passwordField.getStyleClass().add("password-field");
        passwordField.setMaxWidth(Double.MAX_VALUE);
        passwordBox.getChildren().addAll(passwordLabel, passwordField);

        VBox confirmPasswordBox = new VBox(5);
        Label confirmPasswordLabel = new Label("Confirm Password");
        confirmPasswordLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #34495E;");
        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm your password");
        confirmPasswordField.getStyleClass().add("password-field");
        confirmPasswordField.setMaxWidth(Double.MAX_VALUE);
        confirmPasswordBox.getChildren().addAll(confirmPasswordLabel, confirmPasswordField);

        form.getChildren().addAll(nameBox, emailBox, passwordBox, confirmPasswordBox);

        // Error message label
        errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setWrapText(true);
        errorLabel.setAlignment(Pos.CENTER);

        // Actions section (Register Button & Return to Login Link)
        VBox actions = new VBox(15);
        actions.setAlignment(Pos.CENTER);
        
        registerButton = new Button("Register");
        registerButton.getStyleClass().add("btn-primary");
        registerButton.setMaxWidth(Double.MAX_VALUE);
        registerButton.setOnAction(e -> handleRegister());
        confirmPasswordField.setOnAction(e -> handleRegister()); // Submit on Enter key

        HBox linkBox = new HBox(5);
        linkBox.setAlignment(Pos.CENTER);
        Label linkText = new Label("Already have an account?");
        linkText.setStyle("-fx-text-fill: #7F8C8D;");
        loginLink = new Hyperlink("Back to Login");
        loginLink.setStyle("-fx-text-fill: #4A6FA5; -fx-font-weight: bold; -fx-underline: false;");
        loginLink.setOnAction(e -> handleGoToLogin());
        linkBox.getChildren().addAll(linkText, loginLink);

        actions.getChildren().addAll(registerButton, linkBox);

        // Assemble components
        card.getChildren().addAll(header, form, errorLabel, actions);

        // Add card to StackPane
        getChildren().add(card);
    }

    /**
     * Handles account creation registration logic.
     */
    private void handleRegister() {
        errorLabel.setText("");
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // 1. Input Validation
        if (InputValidator.isEmpty(name) || InputValidator.isEmpty(email) ||
            InputValidator.isEmpty(password) || InputValidator.isEmpty(confirmPassword)) {
            errorLabel.setText("All fields are required.");
            return;
        }

        if (!InputValidator.validateName(name)) {
            errorLabel.setText("Please enter a valid full name.");
            return;
        }

        if (!InputValidator.validateEmail(email)) {
            errorLabel.setText("Please enter a valid email address.");
            return;
        }

        if (!InputValidator.validatePassword(password)) {
            errorLabel.setText("Password must be at least 6 characters long.");
            return;
        }

        // 2. Check Passwords Match
        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match.");
            return;
        }

        try {
            // 3. Prevent Duplicate Emails
            if (userDAO.getUserByEmail(email.trim()) != null) {
                errorLabel.setText("This email address is already registered.");
                return;
            }

            // 4. Hash password
            String hashedPassword = PasswordHasher.hashPassword(password);
            
            // 5. Create new User
            User newUser = new User(0, name.trim(), email.trim(), hashedPassword);
            
            if (userDAO.createUser(newUser)) {
                System.out.println("Registration successful for: " + newUser.getName());
                
                // 6. Navigate back to LoginView
                navigateTo(new LoginView(), "Login - " + MainApp.APP_TITLE);
            } else {
                errorLabel.setText("Failed to create account. Please try again.");
            }
            
        } catch (SQLException e) {
            errorLabel.setText("Database error: " + e.getMessage());
            System.err.println("SQLException on register: " + e.getMessage());
        }
    }

    /**
     * Handles navigating back to the Login screen.
     */
    private void handleGoToLogin() {
        navigateTo(new LoginView(), "Login - " + MainApp.APP_TITLE);
    }

    /**
     * Helper method to switch root layout on the current Scene.
     *
     * @param newRoot The Parent layout of the view to display.
     * @param title    The title of the window stage.
     */
    private void navigateTo(Parent newRoot, String title) {
        Scene scene = getScene();
        if (scene != null) {
            scene.setRoot(newRoot);
            Stage stage = (Stage) scene.getWindow();
            if (stage != null) {
                stage.setTitle(title);
            }
        }
    }
}

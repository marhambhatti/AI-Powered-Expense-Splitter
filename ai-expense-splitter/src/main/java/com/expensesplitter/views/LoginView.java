package com.expensesplitter.views;

import com.expensesplitter.models.User;
import com.expensesplitter.dao.UserDAO;
import com.expensesplitter.utils.InputValidator;
import com.expensesplitter.utils.PasswordHasher;
import com.expensesplitter.utils.SessionManager;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.SQLException;

/**
 * Programmatic JavaFX view for the Login screen.
 * Replaces the FXML-based Login layout and controller.
 *
 * Student : Muhammad Arham | L1F23BSSE0372
 * Package : com.expensesplitter.views
 */
public class LoginView extends StackPane {

    private TextField emailField;
    private PasswordField passwordField;
    private Label errorLabel;
    private Button loginButton;
    private Hyperlink registerLink;

    private final UserDAO userDAO = new UserDAO();

    /**
     * Constructs the LoginView and initializes its layout programmatically.
     */
    public LoginView() {
        initUI();
    }

    /**
     * Initializes the user interface components and layout of the login card.
     */
    private void initUI() {
        // Set background styling matching the global stylesheet
        setStyle("-fx-background-color: #F5F7FA;");

        // Main Card layout container
        VBox card = new VBox(20);
        card.getStyleClass().add("card");
        card.setMaxWidth(400);
        card.setAlignment(Pos.CENTER);

        // Header section (Title & Subtitle)
        VBox header = new VBox(5);
        header.setAlignment(Pos.CENTER);
        Label titleLabel = new Label("AI Expense Splitter");
        titleLabel.getStyleClass().add("heading");
        Label subtitleLabel = new Label("Sign in to your account");
        subtitleLabel.setStyle("-fx-text-fill: #7F8C8D; -fx-font-size: 14px;");
        header.getChildren().addAll(titleLabel, subtitleLabel);

        // Form Fields (Email & Password)
        VBox form = new VBox(15);
        form.setAlignment(Pos.CENTER_LEFT);

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
        passwordField.setPromptText("Enter your password");
        passwordField.getStyleClass().add("password-field");
        passwordField.setMaxWidth(Double.MAX_VALUE);
        passwordBox.getChildren().addAll(passwordLabel, passwordField);

        form.getChildren().addAll(emailBox, passwordBox);

        // Error message label
        errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setWrapText(true);
        errorLabel.setAlignment(Pos.CENTER);

        // Actions section (Login Button & Registration Link)
        VBox actions = new VBox(15);
        actions.setAlignment(Pos.CENTER);
        
        loginButton = new Button("Login");
        loginButton.getStyleClass().add("btn-primary");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setOnAction(e -> handleLogin());
        passwordField.setOnAction(e -> handleLogin()); // Submit on Enter key

        HBox linkBox = new HBox(5);
        linkBox.setAlignment(Pos.CENTER);
        Label linkText = new Label("Don't have an account?");
        linkText.setStyle("-fx-text-fill: #7F8C8D;");
        registerLink = new Hyperlink("Register");
        registerLink.setStyle("-fx-text-fill: #4A6FA5; -fx-font-weight: bold; -fx-underline: false;");
        registerLink.setOnAction(e -> handleGoToRegister());
        linkBox.getChildren().addAll(linkText, registerLink);

        actions.getChildren().addAll(loginButton, linkBox);

        // Assemble all components inside the card
        card.getChildren().addAll(header, form, errorLabel, actions);

        // Add card to StackPane (centers automatically)
        getChildren().add(card);
    }

    /**
     * Handles authentication when the user clicks Login or presses Enter.
     */
    private void handleLogin() {
        errorLabel.setText("");
        String email = emailField.getText();
        String password = passwordField.getText();

        // 1. Input Validation
        if (InputValidator.isEmpty(email) || InputValidator.isEmpty(password)) {
            errorLabel.setText("Please enter both email and password.");
            return;
        }

        if (!InputValidator.validateEmail(email)) {
            errorLabel.setText("Please enter a valid email address.");
            return;
        }

        try {
            // 2. Fetch User by email
            User user = userDAO.getUserByEmail(email.trim());
            
            if (user == null) {
                errorLabel.setText("Invalid email or password.");
                return;
            }

            // 3. Verify Password
            if (PasswordHasher.verifyPassword(password, user.getPasswordHash())) {
                // Set Session
                SessionManager.setCurrentUser(user);
                System.out.println("Login successful for: " + user.getName());

                // Navigate to DashboardView
                navigateTo(new DashboardView(), "Dashboard - " + MainApp.APP_TITLE);
            } else {
                errorLabel.setText("Invalid email or password.");
            }
            
        } catch (SQLException e) {
            errorLabel.setText("Database error: " + e.getMessage());
            System.err.println("SQLException on login: " + e.getMessage());
        }
    }

    /**
     * Handles switching to the Register View.
     */
    private void handleGoToRegister() {
        navigateTo(new RegisterView(), "Register - " + MainApp.APP_TITLE);
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
            // Swap the root panel of the scene
            scene.setRoot(newRoot);
            Stage stage = (Stage) scene.getWindow();
            if (stage != null) {
                stage.setTitle(title);
            }
        }
    }
}

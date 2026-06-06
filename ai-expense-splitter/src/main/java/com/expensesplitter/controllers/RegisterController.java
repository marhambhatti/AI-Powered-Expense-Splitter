package com.expensesplitter.controllers;

import com.expensesplitter.models.User;
import com.expensesplitter.dao.UserDAO;
import com.expensesplitter.utils.InputValidator;
import com.expensesplitter.utils.PasswordHasher;
import com.expensesplitter.views.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * JavaFX Controller for the Register screen.
 * Linked to Register.fxml via fx:controller attribute.
 *
 * Student : Muhammad Arham | L1F23BSSE0372
 * Package : com.expensesplitter.controllers
 */
public class RegisterController implements Initializable {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label errorLabel;

    private final UserDAO userDAO = new UserDAO();

    /**
     * Called automatically after FXML is loaded.
     * Initializes UI components and clears any initial error messages.
     *
     * @param url            The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorLabel.setText("");
    }

    /**
     * Handles the user click on the Register button.
     * Validates input fields, checks for email duplicates, hashes the password, and creates the new user record in the DB.
     *
     * @param event The ActionEvent triggered by the button click or form submit.
     */
    @FXML
    public void handleRegister(ActionEvent event) {
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

        // 2. Check password matches confirm password field
        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match.");
            return;
        }

        try {
            // 3. Check if email is already registered in the system
            if (userDAO.getUserByEmail(email.trim()) != null) {
                errorLabel.setText("This email address is already registered.");
                return;
            }

            // 4. Hash password using SHA-256 via PasswordHasher utility
            String hashedPassword = PasswordHasher.hashPassword(password);
            
            // 5. Create new User object and save to DB
            User newUser = new User(0, name.trim(), email.trim(), hashedPassword);
            
            if (userDAO.createUser(newUser)) {
                System.out.println("Registration successful for user: " + newUser.getName());
                
                // 6. Navigate back to Login screen on successful creation
                navigateTo("/com/expensesplitter/fxml/Login.fxml", "Login - " + MainApp.APP_TITLE);
            } else {
                errorLabel.setText("Failed to create account. Please try again.");
            }
            
        } catch (SQLException e) {
            errorLabel.setText("Database error: " + e.getMessage());
            System.err.println("SQLException during user registration: " + e.getMessage());
        }
    }

    /**
     * Handles navigation back to the Login screen when the user clicks the "Back to Login" link.
     *
     * @param event The ActionEvent triggered by the hyperlink click.
     */
    @FXML
    public void handleGoToLogin(ActionEvent event) {
        navigateTo("/com/expensesplitter/fxml/Login.fxml", "Login - " + MainApp.APP_TITLE);
    }

    /**
     * Helper method to switch scenes/screens within the application.
     *
     * @param fxmlPath The path to the FXML file resource.
     * @param title    The text shown in the window title bar.
     */
    private void navigateTo(String fxmlPath, String title) {
        try {
            // Retrieve current stage from the injected nameField
            Stage stage = (Stage) nameField.getScene().getWindow();
            
            // Load the target FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load(), 900, 600);
            
            // Apply global CSS stylesheet
            scene.getStylesheets().add(
                getClass().getResource("/com/expensesplitter/css/styles.css").toExternalForm()
            );
            
            // Update stage properties and show new scene
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
            
        } catch (Exception e) {
            errorLabel.setText("Error loading screen: " + e.getMessage());
            System.err.println("Navigation error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

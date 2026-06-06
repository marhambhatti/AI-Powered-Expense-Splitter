package com.expensesplitter.controllers;

import com.expensesplitter.models.User;
import com.expensesplitter.dao.UserDAO;
import com.expensesplitter.utils.InputValidator;
import com.expensesplitter.utils.PasswordHasher;
import com.expensesplitter.utils.SessionManager;
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
 * JavaFX Controller for the Login screen.
 * Linked to Login.fxml via fx:controller attribute.
 *
 * Student : Muhammad Arham | L1F23BSSE0372
 * Package : com.expensesplitter.controllers
 */
public class LoginController implements Initializable {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

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
     * Handles the user click on the Login button or Enter key press.
     * Validates input fields, verifies credentials against database, sets session and navigates to Dashboard.
     *
     * @param event The ActionEvent triggered by the button click or enter key.
     */
    @FXML
    public void handleLogin(ActionEvent event) {
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
            // 2. Retrieve user from database by email
            User user = userDAO.getUserByEmail(email.trim());
            
            if (user == null) {
                errorLabel.setText("Invalid email or password.");
                return;
            }

            // 3. Verify password hash using PasswordHasher utility
            if (PasswordHasher.verifyPassword(password, user.getPasswordHash())) {
                // 4. Store authenticated user inside SessionManager singleton
                SessionManager.setCurrentUser(user);
                System.out.println("Login successful for user: " + user.getName());
                
                // 5. Navigate to Dashboard screen
                navigateTo("/com/expensesplitter/fxml/Dashboard.fxml", "Dashboard - " + MainApp.APP_TITLE);
            } else {
                errorLabel.setText("Invalid email or password.");
            }
            
        } catch (SQLException e) {
            errorLabel.setText("Database error: " + e.getMessage());
            System.err.println("SQLException during login attempt: " + e.getMessage());
        }
    }

    /**
     * Handles navigation to the Register screen when the user clicks the "Register" link.
     *
     * @param event The ActionEvent triggered by the hyperlink click.
     */
    @FXML
    public void handleGoToRegister(ActionEvent event) {
        navigateTo("/com/expensesplitter/fxml/Register.fxml", "Register - " + MainApp.APP_TITLE);
    }

    /**
     * Helper method to switch scenes/screens within the application.
     *
     * @param fxmlPath The path to the FXML file resource.
     * @param title    The text shown in the window title bar.
     */
    private void navigateTo(String fxmlPath, String title) {
        try {
            // Retrieve current stage from the injected emailField
            Stage stage = (Stage) emailField.getScene().getWindow();
            
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

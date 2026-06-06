package com.expensesplitter.views;

import com.expensesplitter.models.User;
import com.expensesplitter.utils.SessionManager;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Programmatic JavaFX view for the Dashboard screen.
 * Replaces the FXML-based Dashboard layout and controller.
 *
 * Student : Muhammad Arham | L1F23BSSE0372
 * Package : com.expensesplitter.views
 */
public class DashboardView extends VBox {

    /**
     * Constructs the DashboardView and initializes its layout programmatically.
     */
    public DashboardView() {
        initUI();
    }

    /**
     * Initializes the user interface components and layout of the dashboard view.
     */
    private void initUI() {
        // Alignment and padding matching styles
        setAlignment(Pos.CENTER);
        setSpacing(25);
        setStyle("-fx-background-color: #F5F7FA; -fx-padding: 40;");

        // Main Card container for dashboard content
        VBox card = new VBox(20);
        card.getStyleClass().add("card");
        card.setMaxWidth(500);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-padding: 30;");

        // Welcome text
        Label welcomeTitle = new Label("Dashboard Screen");
        welcomeTitle.getStyleClass().add("heading");

        Label welcomeUser = new Label();
        welcomeUser.setStyle("-fx-font-size: 16px; -fx-text-fill: #34495E;");
        
        // Fetch the currently logged-in user from the session singleton
        User currentUser = SessionManager.getCurrentUser();
        if (currentUser != null) {
            welcomeUser.setText("Welcome, " + currentUser.getName() + " (" + currentUser.getEmail() + ")!");
        } else {
            welcomeUser.setText("Welcome! (No session active)");
        }

        Label placeholderLabel = new Label("TODO: Build Full Layout programmatically");
        placeholderLabel.setStyle("-fx-text-fill: #7F8C8D; -fx-font-style: italic;");

        // Logout Button
        Button logoutButton = new Button("Logout");
        logoutButton.getStyleClass().add("btn-secondary");
        logoutButton.setOnAction(e -> handleLogout());

        card.getChildren().addAll(welcomeTitle, welcomeUser, placeholderLabel, logoutButton);
        getChildren().add(card);
    }

    /**
     * Handles user logout. Clears the session and navigates back to the Login screen.
     */
    private void handleLogout() {
        // Clear active session in SessionManager
        SessionManager.clearSession();

        // Navigate back to LoginView
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

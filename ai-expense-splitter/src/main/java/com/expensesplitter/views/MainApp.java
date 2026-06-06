package com.expensesplitter.views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main entry point of the AI-Powered Expense Splitter application.
 * Extends JavaFX Application — launches the Login screen first.
 *
 * Flow:
 *   MainApp.start() → loads Login.fxml → user logs in
 *   → navigates to Dashboard → all other screens
 *
 * Student : Muhammad Arham | L1F23BSSE0372
 * GitHub  : https://github.com/marhambhatti
 * Package : com.expensesplitter.views
 */
public class MainApp extends Application {

    /** Application title shown in the window title bar */
    public static final String APP_TITLE = "AI-Powered Expense Splitter";

    /**
     * JavaFX lifecycle method — called when the application starts.
     * Loads the Login screen as the initial view.
     *
     * @param primaryStage  the main application window
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the Login FXML file
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/expensesplitter/fxml/Login.fxml")
        );

        // Create scene with loaded FXML
        Scene scene = new Scene(loader.load(), 900, 600);

        // Apply stylesheet
        scene.getStylesheets().add(
            getClass().getResource("/com/expensesplitter/css/styles.css").toExternalForm()
        );

        // Configure the main window
        primaryStage.setTitle(APP_TITLE);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    /** Application entry point */
    public static void main(String[] args) {
        launch(args);
    }
}

package com.expensesplitter.controllers;

import com.expensesplitter.dao.GroupDAO;
import com.expensesplitter.dao.UserDAO;
import com.expensesplitter.models.Group;
import com.expensesplitter.models.User;
import com.expensesplitter.utils.InputValidator;
import com.expensesplitter.utils.SessionManager;
import com.expensesplitter.views.DashboardView;
import com.expensesplitter.views.GroupView;
import com.expensesplitter.views.MainApp;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Controller class that manages GroupView UI events, coordinates database logic via GroupDAO,
 * and maintains Group management session data.
 *
 * Student : Muhammad Arham | L1F23BSSE0372
 * Package : com.expensesplitter.controllers
 */
public class GroupController {

    private final GroupView view;
    private final GroupDAO groupDAO;
    private final UserDAO userDAO;

    /**
     * Constructs the GroupController, initializes the view and binds event listeners.
     */
    public GroupController() {
        this.view = new GroupView();
        this.groupDAO = new GroupDAO();
        this.userDAO = new UserDAO();

        bindEvents();
        loadUserGroups();
    }

    /**
     * Retrieves the managed GroupView pane.
     *
     * @return the programmatic GroupView node
     */
    public GroupView getView() {
        return view;
    }

    /**
     * Binds action event listeners to View buttons and selection properties.
     */
    private void bindEvents() {
        // Selection listener for Groups list view
        view.getGroupsListView().getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            view.setSelectedGroup(newVal);
        });

        // Top bar Back button
        view.getBackButton().setOnAction(e -> handleBack());

        // Left Panel Group Actions
        view.getCreateGroupButton().setOnAction(e -> handleCreateGroup());
        view.getDeleteGroupButton().setOnAction(e -> handleDeleteGroup());

        // Right Panel Member Actions
        view.getAddMemberButton().setOnAction(e -> handleAddMember());
        view.getRemoveMemberButton().setOnAction(e -> handleRemoveMember());
    }

    /**
     * Loads the groups associated with the currently logged-in user and populates the list view.
     */
    private void loadUserGroups() {
        User currentUser = SessionManager.getCurrentUser();
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Session Error", "No active user session found.");
            return;
        }

        try {
            // Fetch groups where the logged-in user is a member
            List<Group> userGroups = groupDAO.getGroupsByUser(currentUser.getId());
            
            // Clear current items and reload
            view.getGroupsListView().getItems().clear();
            view.getGroupsListView().getItems().addAll(userGroups);
            
            // Clear detail panel selection
            view.setSelectedGroup(null);
            
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to retrieve groups: " + e.getMessage());
        }
    }

    /**
     * Navigates back to the Dashboard screen.
     */
    private void handleBack() {
        navigateTo(new DashboardView(), "Dashboard - " + MainApp.APP_TITLE);
    }

    /**
     * Displays a dialog prompting the user to create a new expense group.
     */
    private void handleCreateGroup() {
        User currentUser = SessionManager.getCurrentUser();
        if (currentUser == null) return;

        // Create a custom dialog layout
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create New Group");
        dialog.setHeaderText("Enter details for the new expense group.");
        
        ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Group Name (e.g. Roommates)");
        TextArea descArea = new TextArea();
        descArea.setPromptText("Enter a brief description...");
        descArea.setPrefRowCount(3);

        grid.add(new Label("Group Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descArea, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Request focus on the name field by default
        Platform.runLater(nameField::requestFocus);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == createButtonType) {
            String name = nameField.getText().trim();
            String description = descArea.getText().trim();

            if (name.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validation Warning", "Group name cannot be empty.");
                return;
            }

            try {
                Group newGroup = new Group(0, name, description, currentUser.getId());
                if (groupDAO.createGroup(newGroup)) {
                    // Reload list and select the newly created group
                    loadUserGroups();
                    selectGroupInList(newGroup.getId());
                } else {
                    showAlert(Alert.AlertType.ERROR, "Creation Failed", "Could not create the group.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error saving group: " + e.getMessage());
            }
        }
    }

    /**
     * Deletes the currently selected group after confirmation.
     */
    private void handleDeleteGroup() {
        Group selectedGroup = view.getGroupsListView().getSelectionModel().getSelectedItem();
        if (selectedGroup == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a group to delete.");
            return;
        }

        // Show confirmation alert
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Group: " + selectedGroup.getName());
        confirmAlert.setContentText("Are you sure you want to delete this group? All shared expenses, settlements, and member lists will be permanently removed.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if (groupDAO.deleteGroup(selectedGroup.getId())) {
                    loadUserGroups();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Deletion Failed", "Could not delete the group.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error deleting group: " + e.getMessage());
            }
        }
    }

    /**
     * Prompts the user to enter an email address to add a user to the selected group.
     */
    private void handleAddMember() {
        Group selectedGroup = view.getGroupsListView().getSelectionModel().getSelectedItem();
        if (selectedGroup == null) return;

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Member");
        dialog.setHeaderText("Add user to group: " + selectedGroup.getName());
        dialog.setContentText("Enter member's email address:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String email = result.get().trim();

            if (email.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validation Warning", "Email address cannot be empty.");
                return;
            }

            if (!InputValidator.validateEmail(email)) {
                showAlert(Alert.AlertType.WARNING, "Validation Warning", "Please enter a valid email format.");
                return;
            }

            try {
                // Find user by email
                User userToAdd = userDAO.getUserByEmail(email);
                if (userToAdd == null) {
                    showAlert(Alert.AlertType.ERROR, "User Not Found", "No registered user found with email: " + email);
                    return;
                }

                // Check if user is already a member
                boolean isAlreadyMember = selectedGroup.getMembers().stream()
                        .anyMatch(member -> member.getId() == userToAdd.getId());
                
                if (isAlreadyMember) {
                    showAlert(Alert.AlertType.WARNING, "Already Member", userToAdd.getName() + " is already a member of this group.");
                    return;
                }

                // Add member in database
                if (groupDAO.addMember(selectedGroup.getId(), userToAdd.getId())) {
                    // Refresh current group selection details
                    refreshSelectedGroupDetails(selectedGroup.getId());
                } else {
                    showAlert(Alert.AlertType.ERROR, "Operation Failed", "Could not add user to group.");
                }

            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error adding member: " + e.getMessage());
            }
        }
    }

    /**
     * Removes the currently selected member from the group member list.
     */
    private void handleRemoveMember() {
        Group selectedGroup = view.getGroupsListView().getSelectionModel().getSelectedItem();
        User selectedMember = view.getMembersListView().getSelectionModel().getSelectedItem();

        if (selectedGroup == null) return;
        
        if (selectedMember == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a member to remove.");
            return;
        }

        // Prevent removing the group creator
        if (selectedMember.getId() == selectedGroup.getCreatedBy()) {
            showAlert(Alert.AlertType.WARNING, "Operation Restricted", "Cannot remove the group creator from the group.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Removal");
        confirmAlert.setHeaderText("Remove Member");
        confirmAlert.setContentText("Are you sure you want to remove " + selectedMember.getName() + " from the group " + selectedGroup.getName() + "?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if (groupDAO.removeMember(selectedGroup.getId(), selectedMember.getId())) {
                    // Refresh details
                    refreshSelectedGroupDetails(selectedGroup.getId());
                } else {
                    showAlert(Alert.AlertType.ERROR, "Operation Failed", "Could not remove member.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error removing member: " + e.getMessage());
            }
        }
    }

    /**
     * Refreshes the details of the selected group by pulling fresh data from the database.
     *
     * @param groupId The ID of the group to refresh.
     */
    private void refreshSelectedGroupDetails(int groupId) throws SQLException {
        Group updatedGroup = groupDAO.getGroupById(groupId);
        if (updatedGroup != null) {
            // Update details panel
            view.setSelectedGroup(updatedGroup);

            // Update in the main list view item without resetting list scroll
            ListView<Group> listView = view.getGroupsListView();
            int index = listView.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                listView.getItems().set(index, updatedGroup);
                listView.getSelectionModel().select(index);
            }
        }
    }

    /**
     * Automatically selects a group in the main ListView by its ID.
     *
     * @param groupId the ID of the group to select
     */
    private void selectGroupInList(int groupId) {
        ListView<Group> listView = view.getGroupsListView();
        for (int i = 0; i < listView.getItems().size(); i++) {
            if (listView.getItems().get(i).getId() == groupId) {
                listView.getSelectionModel().select(i);
                break;
            }
        }
    }

    /**
     * Displays a JavaFX Alert popup.
     *
     * @param type    The AlertType of the dialog.
     * @param title   Title of the dialog box.
     * @param content Body message of the dialog box.
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Helper method to navigate to another scene.
     *
     * @param newRoot The Parent layout panel of the new scene.
     * @param title    The title of the window.
     */
    private void navigateTo(Parent newRoot, String title) {
        Scene scene = view.getScene();
        if (scene != null) {
            scene.setRoot(newRoot);
            Stage stage = (Stage) scene.getWindow();
            if (stage != null) {
                stage.setTitle(title);
            }
        }
    }
}

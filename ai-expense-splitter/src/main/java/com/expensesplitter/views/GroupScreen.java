package com.expensesplitter.views;

import com.expensesplitter.dao.GroupDAO;
import com.expensesplitter.dao.UserDAO;
import com.expensesplitter.models.Group;
import com.expensesplitter.models.User;
import com.expensesplitter.utils.SessionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

/**
 * Group management screen for the AI-Powered Expense Splitter application.
 * Built entirely with pure JavaFX code (no FXML).
 * Extends BorderPane to provide a split-pane layout with group list and details.
 *
 * Features:
 *   - Left panel: ListView showing user's groups
 *   - Right panel: Selected group details, members list, and action buttons
 *   - Create Group, Add Member, Remove Member, Delete Group buttons
 *   - Refresh functionality after every action
 *
 * Student : Muhammad Arham | L1F23BSSE0372
 * Package : com.expensesplitter.views
 */
public class GroupScreen extends BorderPane {

    /** UI Components */
    private ListView<String> groupListView;
    private ListView<String> memberListView;
    private Label selectedGroupLabel;
    private Label errorLabel;

    /** Data Access Objects */
    private GroupDAO groupDAO;
    private UserDAO userDAO;

    /** Currently selected group */
    private Group selectedGroup;

    /** Currently logged-in user */
    private User currentUser;

    /**
     * Constructor - builds the entire group management UI programmatically.
     * Creates a split layout with group list on left and details on right.
     */
    public GroupScreen() {
        // Initialize DAOs
        groupDAO = new GroupDAO();
        userDAO = new UserDAO();

        // Get current logged-in user
        currentUser = SessionManager.getCurrentUser();

        // Create the main layout
        this.setStyle("-fx-background-color: #f0f2f5;");

        // Create left panel (group list)
        VBox leftPanel = createLeftPanel();

        // Create right panel (group details)
        VBox rightPanel = createRightPanel();

        // Set panels in BorderPane
        this.setLeft(leftPanel);
        this.setRight(rightPanel);

        // Load initial groups
        refreshGroupList();
    }

    /**
     * Creates the left panel containing the group list.
     *
     * @return VBox with group ListView
     */
    private VBox createLeftPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: white; " +
                      "-fx-border-color: #bdc3c7; " +
                      "-fx-border-width: 0 1 0 0;");
        panel.setPrefWidth(300);

        // Title label
        Label titleLabel = new Label("My Groups");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.web("#2c3e50"));

        // Create group ListView
        groupListView = new ListView<>();
        groupListView.setPrefHeight(400);
        groupListView.setStyle("-fx-background-color: #ecf0f1; " +
                               "-fx-background-radius: 5;");
        groupListView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> handleGroupSelection(newValue)
        );

        // Create Group button
        Button createGroupButton = new Button("Create Group");
        createGroupButton.setStyle("-fx-background-color: #27ae60; " +
                                   "-fx-text-fill: white; " +
                                   "-fx-background-radius: 5; " +
                                   "-fx-font-weight: bold; " +
                                   "-fx-padding: 10 20;");
        createGroupButton.setMaxWidth(Double.MAX_VALUE);
        createGroupButton.setOnAction(e -> handleCreateGroup());

        // Add elements to panel
        panel.getChildren().addAll(titleLabel, groupListView, createGroupButton);

        return panel;
    }

    /**
     * Creates the right panel containing selected group details and actions.
     *
     * @return VBox with group details and action buttons
     */
    private VBox createRightPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: white;");
        panel.setAlignment(Pos.TOP_CENTER);

        // Selected group label
        selectedGroupLabel = new Label("Select a group to view details");
        selectedGroupLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        selectedGroupLabel.setTextFill(Color.web("#2c3e50"));
        selectedGroupLabel.setWrapText(true);

        // Members label
        Label membersLabel = new Label("Members:");
        membersLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        membersLabel.setTextFill(Color.web("#34495e"));
        membersLabel.setVisible(false);

        // Create members ListView
        memberListView = new ListView<>();
        memberListView.setPrefHeight(200);
        memberListView.setStyle("-fx-background-color: #ecf0f1; " +
                                "-fx-background-radius: 5;");
        memberListView.setVisible(false);

        // Action buttons container
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setVisible(false);

        // Add Member button
        Button addMemberButton = new Button("Add Member");
        addMemberButton.setStyle("-fx-background-color: #3498db; " +
                                "-fx-text-fill: white; " +
                                "-fx-background-radius: 5; " +
                                "-fx-font-weight: bold; " +
                                "-fx-padding: 10 15;");
        addMemberButton.setOnAction(e -> handleAddMember());

        // Remove Member button
        Button removeMemberButton = new Button("Remove Member");
        removeMemberButton.setStyle("-fx-background-color: #e67e22; " +
                                   "-fx-text-fill: white; " +
                                   "-fx-background-radius: 5; " +
                                   "-fx-font-weight: bold; " +
                                   "-fx-padding: 10 15;");
        removeMemberButton.setOnAction(e -> handleRemoveMember());

        // Delete Group button
        Button deleteGroupButton = new Button("Delete Group");
        deleteGroupButton.setStyle("-fx-background-color: #e74c3c; " +
                                   "-fx-text-fill: white; " +
                                   "-fx-background-radius: 5; " +
                                   "-fx-font-weight: bold; " +
                                   "-fx-padding: 10 15;");
        deleteGroupButton.setOnAction(e -> handleDeleteGroup());

        buttonBox.getChildren().addAll(addMemberButton, removeMemberButton, deleteGroupButton);

        // Error label
        errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("Arial", 11));
        errorLabel.setWrapText(true);

        // Add elements to panel
        panel.getChildren().addAll(
            selectedGroupLabel,
            membersLabel,
            memberListView,
            buttonBox,
            errorLabel
        );

        return panel;
    }

    /**
     * Handles group selection from the ListView.
     * Loads group details and populates the right panel.
     *
     * @param selectedGroupName the name of the selected group
     */
    private void handleGroupSelection(String selectedGroupName) {
        if (selectedGroupName == null) {
            return;
        }

        // Clear previous error
        errorLabel.setText("");

        try {
            // Find the selected group from user's groups
            List<Group> userGroups = groupDAO.getGroupsByUser(currentUser.getId());
            for (Group group : userGroups) {
                if (group.getName().equals(selectedGroupName)) {
                    selectedGroup = group;
                    break;
                }
            }

            if (selectedGroup != null) {
                // Update UI with selected group details
                selectedGroupLabel.setText(selectedGroup.getName());
                if (selectedGroup.getDescription() != null && !selectedGroup.getDescription().isEmpty()) {
                    selectedGroupLabel.setText(selectedGroup.getName() + "\n" + selectedGroup.getDescription());
                }

                // Populate members list
                memberListView.getItems().clear();
                for (User member : selectedGroup.getMembers()) {
                    memberListView.getItems().add(member.getName() + " (" + member.getEmail() + ")");
                }

                // Show members section and buttons
                memberListView.setVisible(true);
                memberListView.setManaged(true);

                // Find and show the members label and button box
                VBox rightPanel = (VBox) this.getRight();
                for (javafx.scene.Node node : rightPanel.getChildren()) {
                    if (node instanceof Label && ((Label) node).getText().equals("Members:")) {
                        node.setVisible(true);
                        node.setManaged(true);
                    }
                    if (node instanceof HBox) {
                        node.setVisible(true);
                        node.setManaged(true);
                    }
                }
            }

        } catch (Exception e) {
            errorLabel.setText("Error loading group details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the Create Group button click event.
     * Shows a TextInputDialog to get group name and description,
     * then creates the group in the database.
     */
    private void handleCreateGroup() {
        // Clear previous error
        errorLabel.setText("");

        // Dialog for group name
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Create Group");
        nameDialog.setHeaderText("Enter Group Name");
        nameDialog.setContentText("Group Name:");

        nameDialog.showAndWait().ifPresent(groupName -> {
            if (groupName == null || groupName.trim().isEmpty()) {
                errorLabel.setText("Group name cannot be empty.");
                return;
            }

            // Dialog for group description
            TextInputDialog descDialog = new TextInputDialog();
            descDialog.setTitle("Create Group");
            descDialog.setHeaderText("Enter Group Description");
            descDialog.setContentText("Description (optional):");

            descDialog.showAndWait().ifPresent(description -> {
                try {
                    // Create new Group object
                    Group newGroup = new Group();
                    newGroup.setName(groupName.trim());
                    newGroup.setDescription(description.trim());
                    newGroup.setCreatedBy(currentUser.getId());

                    // Insert into database
                    boolean success = groupDAO.createGroup(newGroup);

                    if (success) {
                        // Refresh group list
                        refreshGroupList();
                        errorLabel.setText(""); // Clear error
                    } else {
                        errorLabel.setText("Failed to create group. Please try again.");
                    }

                } catch (Exception e) {
                    errorLabel.setText("Error creating group: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        });
    }

    /**
     * Handles the Add Member button click event.
     * Shows a TextInputDialog to get user email,
     * finds the user by email, and adds them to the group.
     */
    private void handleAddMember() {
        if (selectedGroup == null) {
            errorLabel.setText("Please select a group first.");
            return;
        }

        // Clear previous error
        errorLabel.setText("");

        // Dialog for user email
        TextInputDialog emailDialog = new TextInputDialog();
        emailDialog.setTitle("Add Member");
        emailDialog.setHeaderText("Enter User Email");
        emailDialog.setContentText("Email:");

        emailDialog.showAndWait().ifPresent(email -> {
            if (email == null || email.trim().isEmpty()) {
                errorLabel.setText("Email cannot be empty.");
                return;
            }

            try {
                // Find user by email
                User userToAdd = userDAO.getUserByEmail(email.trim());

                if (userToAdd == null) {
                    errorLabel.setText("No user found with this email.");
                    return;
                }

                // Check if user is already a member
                for (User member : selectedGroup.getMembers()) {
                    if (member.getId() == userToAdd.getId()) {
                        errorLabel.setText("User is already a member of this group.");
                        return;
                    }
                }

                // Add member to group
                boolean success = groupDAO.addMember(selectedGroup.getId(), userToAdd.getId());

                if (success) {
                    // Refresh group details
                    refreshGroupList();
                    // Re-select the group to refresh details
                    handleGroupSelection(selectedGroup.getName());
                    errorLabel.setText(""); // Clear error
                } else {
                    errorLabel.setText("Failed to add member. Please try again.");
                }

            } catch (Exception e) {
                errorLabel.setText("Error adding member: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * Handles the Remove Member button click event.
     * Removes the selected member from the group.
     */
    private void handleRemoveMember() {
        if (selectedGroup == null) {
            errorLabel.setText("Please select a group first.");
            return;
        }

        String selectedMember = memberListView.getSelectionModel().getSelectedItem();
        if (selectedMember == null) {
            errorLabel.setText("Please select a member to remove.");
            return;
        }

        // Clear previous error
        errorLabel.setText("");

        // Extract email from selected member string
        String email = selectedMember.substring(selectedMember.indexOf("(") + 1, selectedMember.indexOf(")"));

        try {
            // Find user by email
            User userToRemove = userDAO.getUserByEmail(email);

            if (userToRemove == null) {
                errorLabel.setText("User not found.");
                return;
            }

            // Prevent removing the group creator
            if (userToRemove.getId() == selectedGroup.getCreatedBy()) {
                errorLabel.setText("Cannot remove the group creator.");
                return;
            }

            // Remove member from group
            boolean success = groupDAO.removeMember(selectedGroup.getId(), userToRemove.getId());

            if (success) {
                // Refresh group details
                refreshGroupList();
                // Re-select the group to refresh details
                handleGroupSelection(selectedGroup.getName());
                errorLabel.setText(""); // Clear error
            } else {
                errorLabel.setText("Failed to remove member. Please try again.");
            }

        } catch (Exception e) {
            errorLabel.setText("Error removing member: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the Delete Group button click event.
     * Shows a confirmation dialog and deletes the group if confirmed.
     */
    private void handleDeleteGroup() {
        if (selectedGroup == null) {
            errorLabel.setText("Please select a group first.");
            return;
        }

        // Clear previous error
        errorLabel.setText("");

        // Show confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Group");
        alert.setHeaderText("Delete Group: " + selectedGroup.getName());
        alert.setContentText("Are you sure you want to delete this group? This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Delete group from database
                    boolean success = groupDAO.deleteGroup(selectedGroup.getId());

                    if (success) {
                        // Clear selection
                        selectedGroup = null;
                        selectedGroupLabel.setText("Select a group to view details");
                        memberListView.getItems().clear();
                        memberListView.setVisible(false);
                        memberListView.setManaged(false);

                        // Hide members label and button box
                        VBox rightPanel = (VBox) this.getRight();
                        for (javafx.scene.Node node : rightPanel.getChildren()) {
                            if (node instanceof Label && ((Label) node).getText().equals("Members:")) {
                                node.setVisible(false);
                                node.setManaged(false);
                            }
                            if (node instanceof HBox) {
                                node.setVisible(false);
                                node.setManaged(false);
                            }
                        }

                        // Refresh group list
                        refreshGroupList();
                        errorLabel.setText(""); // Clear error
                    } else {
                        errorLabel.setText("Failed to delete group. Please try again.");
                    }

                } catch (Exception e) {
                    errorLabel.setText("Error deleting group: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Refreshes the group ListView with the current user's groups.
     * Called after every group-related action to keep the UI in sync.
     */
    private void refreshGroupList() {
        try {
            // Clear current list
            groupListView.getItems().clear();

            // Load user's groups from database
            List<Group> userGroups = groupDAO.getGroupsByUser(currentUser.getId());

            // Add group names to ListView
            for (Group group : userGroups) {
                groupListView.getItems().add(group.getName());
            }

        } catch (Exception e) {
            errorLabel.setText("Error loading groups: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

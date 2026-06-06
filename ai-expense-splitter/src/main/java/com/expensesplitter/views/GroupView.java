package com.expensesplitter.views;

import com.expensesplitter.models.Group;
import com.expensesplitter.models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Programmatic JavaFX view for the Group Management screen.
 * Consists of a split-pane layout: groups list on the left and group details on the right.
 *
 * Student : Muhammad Arham | L1F23BSSE0372
 * Package : com.expensesplitter.views
 */
public class GroupView extends BorderPane {

    private ListView<Group> groupsListView;
    private ListView<User> membersListView;

    private Button backButton;
    private Button createGroupButton;
    private Button deleteGroupButton;
    private Button addMemberButton;
    private Button removeMemberButton;

    private Label detailsNameLabel;
    private Label detailsDescLabel;
    private VBox detailsContentBox;
    private StackPane placeholderBox;

    /**
     * Constructs the GroupView and initializes its layout programmatically.
     */
    public GroupView() {
        initUI();
    }

    /**
     * Initializes the user interface components and layout.
     */
    private void initUI() {
        // Apply global root styling
        setStyle("-fx-background-color: #F5F7FA;");

        // ── TOP HEADER BAR ──────────────────────────────────────
        HBox headerBar = new HBox(20);
        headerBar.setAlignment(Pos.CENTER_LEFT);
        headerBar.setPadding(new Insets(15, 25, 15, 25));
        headerBar.setStyle("-fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 1);");

        backButton = new Button("← Back");
        backButton.getStyleClass().add("btn-secondary");
        backButton.setStyle("-fx-padding: 6 12;");

        Label screenTitle = new Label("Group Management");
        screenTitle.getStyleClass().add("heading");

        headerBar.getChildren().addAll(backButton, screenTitle);
        setTop(headerBar);

        // ── SPLIT PANE (CENTER) ──────────────────────────────────
        SplitPane splitPane = new SplitPane();
        splitPane.setStyle("-fx-background-color: transparent; -fx-padding: 20;");

        // ── LEFT PANEL: GROUPS LIST ➔ VBox ──
        VBox leftPanel = new VBox(15);
        leftPanel.setMinWidth(280);
        leftPanel.setMaxWidth(350);
        leftPanel.getStyleClass().add("card");

        Label listHeader = new Label("My Groups");
        listHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");

        groupsListView = new ListView<>();
        VBox.setVgrow(groupsListView, Priority.ALWAYS);
        groupsListView.getStyleClass().add("text-field"); // Use text-field style for list borders

        // Group Action Buttons
        HBox groupActions = new HBox(10);
        groupActions.setAlignment(Pos.CENTER);
        
        createGroupButton = new Button("Create Group");
        createGroupButton.getStyleClass().add("btn-primary");
        createGroupButton.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(createGroupButton, Priority.ALWAYS);

        deleteGroupButton = new Button("Delete");
        deleteGroupButton.getStyleClass().add("btn-secondary");
        deleteGroupButton.setStyle("-fx-text-fill: #E74C3C; -fx-border-color: #FADBD8;");

        groupActions.getChildren().addAll(createGroupButton, deleteGroupButton);
        leftPanel.getChildren().addAll(listHeader, groupsListView, groupActions);

        // ── RIGHT PANEL: DETAILS CARD ➔ StackPane ──
        StackPane rightPanel = new StackPane();
        rightPanel.getStyleClass().add("card");
        rightPanel.setStyle("-fx-background-color: white;");

        // Content placeholder when no group is selected
        placeholderBox = new StackPane();
        Label placeholderLabel = new Label("Select a group from the list to view details.");
        placeholderLabel.setStyle("-fx-text-fill: #7F8C8D; -fx-font-style: italic; -fx-font-size: 15px;");
        placeholderBox.getChildren().add(placeholderLabel);

        // Actual details container
        detailsContentBox = new VBox(20);
        detailsContentBox.setAlignment(Pos.TOP_LEFT);
        detailsContentBox.setVisible(false); // Hidden initially

        // Group info display
        detailsNameLabel = new Label();
        detailsNameLabel.getStyleClass().add("heading");
        detailsNameLabel.setStyle("-fx-font-size: 20px;");

        detailsDescLabel = new Label();
        detailsDescLabel.setStyle("-fx-text-fill: #7F8C8D; -fx-font-size: 14px;");
        detailsDescLabel.setWrapText(true);

        Separator separator = new Separator();

        // Members list section
        VBox membersSection = new VBox(10);
        VBox.setVgrow(membersSection, Priority.ALWAYS);
        
        Label membersHeader = new Label("Group Members");
        membersHeader.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #34495E;");
        
        membersListView = new ListView<>();
        VBox.setVgrow(membersListView, Priority.ALWAYS);
        membersListView.getStyleClass().add("text-field");

        // Customize membersListView cell formatting
        membersListView.setCellFactory(lv -> new ListCell<User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + " (" + item.getEmail() + ")");
                }
            }
        });

        membersSection.getChildren().addAll(membersHeader, membersListView);

        // Members action buttons
        HBox memberActions = new HBox(15);
        memberActions.setAlignment(Pos.CENTER_RIGHT);

        addMemberButton = new Button("Add Member");
        addMemberButton.getStyleClass().add("btn-primary");
        addMemberButton.setStyle("-fx-padding: 8 16;");

        removeMemberButton = new Button("Remove Member");
        removeMemberButton.getStyleClass().add("btn-secondary");
        removeMemberButton.setStyle("-fx-padding: 8 16; -fx-text-fill: #E74C3C; -fx-border-color: #FADBD8;");

        memberActions.getChildren().addAll(removeMemberButton, addMemberButton);

        detailsContentBox.getChildren().addAll(detailsNameLabel, detailsDescLabel, separator, membersSection, memberActions);

        // Add both layers to StackPane
        rightPanel.getChildren().addAll(placeholderBox, detailsContentBox);

        // Add panels to SplitPane
        splitPane.getItems().addAll(leftPanel, rightPanel);
        splitPane.setDividerPositions(0.35); // 35% left, 65% right
        
        setCenter(splitPane);
    }

    /**
     * Updates the right details panel based on the selected Group.
     *
     * @param group The selected Group model, or null if nothing is selected.
     */
    public void setSelectedGroup(Group group) {
        if (group == null) {
            detailsContentBox.setVisible(false);
            placeholderBox.setVisible(true);
        } else {
            placeholderBox.setVisible(false);
            detailsContentBox.setVisible(true);

            // Populate labels
            detailsNameLabel.setText(group.getName());
            detailsDescLabel.setText(group.getDescription() != null && !group.getDescription().isEmpty()
                    ? group.getDescription()
                    : "No description provided.");

            // Populate members list
            membersListView.getItems().clear();
            membersListView.getItems().addAll(group.getMembers());
        }
    }

    // ── Getters for UI elements ──────────────────────────────────────────

    public ListView<Group> getGroupsListView() {
        return groupsListView;
    }

    public ListView<User> getMembersListView() {
        return membersListView;
    }

    public Button getBackButton() {
        return backButton;
    }

    public Button getCreateGroupButton() {
        return createGroupButton;
    }

    public Button getDeleteGroupButton() {
        return deleteGroupButton;
    }

    public Button getAddMemberButton() {
        return addMemberButton;
    }

    public Button getRemoveMemberButton() {
        return removeMemberButton;
    }
}

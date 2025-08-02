/*Class for accepting or rejecting project request With UI and backend */

package Admin;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Connection.ConnectionManager;

public class changeStatus extends Application {
    private TableView<Project> projectTable;
    private Scene previousScene;
    
    public changeStatus() {}
    
    public changeStatus(Scene passed)
    {
    	this.previousScene = passed;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Change Project Status");
        
        Label backArrow = new Label("\u2190 Back");
        backArrow.setStyle("-fx-font-size: 18px; -fx-text-fill: #0078D7; -fx-cursor: hand;");

        backArrow.setOnMouseClicked(event -> {
            primaryStage.setTitle("Student Page");
            primaryStage.setScene(previousScene);
        });

        projectTable = new TableView<>();
        setupTableColumns();

        // Set a fixed width and apply a CSS class for additional styling
        projectTable.setPrefWidth(500);
        projectTable.getStyleClass().add("custom-table");

        loadProjects();

        VBox vbox = new VBox(10, backArrow, projectTable);
        vbox.setAlignment(Pos.CENTER);

        // Centering the VBox within a BorderPane and adding padding around it
        BorderPane root = new BorderPane();
        root.setCenter(vbox);
        root.setPadding(new Insets(20, 50, 20, 50)); // Add padding around the table

        projectTable.setMaxWidth(700); // Set a maximum width for the table


        Scene scene = new Scene(root, 600, 500);
        scene.getStylesheets().add(getClass().getResource("/resources/status.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupTableColumns() {
        TableColumn<Project, Integer> idColumn = new TableColumn<>("Project ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("projectId"));
        idColumn.setPrefWidth(100);

        TableColumn<Project, String> nameColumn = new TableColumn<>("Project Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        nameColumn.setPrefWidth(200);

        TableColumn<Project, String> statusColumn = new TableColumn<>("Current Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setPrefWidth(100);

        TableColumn<Project, Button> actionColumn = new TableColumn<>("Action");
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("acceptButton"));

        actionColumn.setCellFactory(col -> new TableCell<Project, Button>() {
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(5, item, projectTable.getItems().get(getIndex()).getRejectButton());
                    setGraphic(hbox);
                }
            }
        });

        projectTable.getColumns().addAll(idColumn, nameColumn, statusColumn, actionColumn);
    }

    private void loadProjects() {
        try {
            Connection con = ConnectionManager.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT projectId, projectName, status FROM project WHERE status = 'requested'");

            while (rs.next()) {
                int projectId = rs.getInt("projectId");
                String projectName = rs.getString("projectName");
                String currentStatus = rs.getString("status");

                Button acceptButton = new Button("Accept");
                acceptButton.setOnAction(e -> changeStatus(projectId, 1));

                Button rejectButton = new Button("Reject");
                rejectButton.setOnAction(e -> changeStatus(projectId, 2));

                projectTable.getItems().add(new Project(projectId, projectName, currentStatus, acceptButton, rejectButton));
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void changeStatus(int projectId, int statusChoice) {
        String newStatus = (statusChoice == 1) ? "accepted" : "rejected";
        try {
            Connection con = ConnectionManager.getConnection();
            Statement stmt = con.createStatement();
            String updateQuery = "UPDATE project SET status = '" + newStatus + "' WHERE projectId = " + projectId;
            int rowsUpdated = stmt.executeUpdate(updateQuery);

            if (rowsUpdated > 0) {
                System.out.println("Project ID " + projectId + " status successfully updated to '" + newStatus + "'.");
                projectTable.getItems().removeIf(project -> project.getProjectId() == projectId);
             
            }

            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
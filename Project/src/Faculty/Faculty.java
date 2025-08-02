/*UI class of Faculty after Login which contains buttons for all Faculty Functions with their function calls */

package Faculty;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import Connection.ConnectionManager;
import UI.AlertBox;
import UI.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
	
public class Faculty  extends Application
{
	public String Username,Password;
	public int id;
    public Scene previousScene;
    
    public Faculty() {}
    
    public Faculty(int id)
    {
    	this.id = id;
    }
    
    public Faculty(String usr,String pwd,int id)
    {
    	this.Username = usr;
    	this.Password = pwd;
    	this.id = id;
    }

    @Override
    public void start(Stage facultyStage) 
    {	
    	Label optionLabel = new Label("Choose the Option");
        optionLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #4A90E2; -fx-padding: 20px;");

       
        Button displayProgressButton = new Button("Check Progress ");
        Button viewProjectButton = new Button("View All Projects");
        Button backButton = new Button("Logout");
        
        double buttonWidth = 220;
        displayProgressButton.setPrefWidth(buttonWidth);
        viewProjectButton.setPrefWidth(buttonWidth);
        backButton.setPrefWidth(buttonWidth);

        
        VBox layout;
        layout = new VBox(10, optionLabel, viewProjectButton, displayProgressButton, backButton);
        
        layout.setSpacing(10);
        layout.setAlignment(Pos.CENTER);

        
        Scene facultyScene = new Scene(layout, 300, 200);
        facultyScene.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());
        
        backButton.setOnAction(event -> handleBack(facultyStage));
        displayProgressButton.setOnAction(event -> displayProgress(facultyScene,facultyStage,id));
        viewProjectButton.setOnAction(event -> viewProjects(facultyScene,facultyStage,id));
        
        facultyStage.setTitle("Faculty Page");
        facultyStage.setScene(facultyScene);
        
        
        facultyStage.show();
    }

   
    private void displayProgress(Scene facultyScene, Stage facultyStage, int facultyId) 
    {
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Select Project ID");
        dialog.getDialogPane().getStyleClass().add("dialog-pane");

        // Grid for layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // TextField for Project ID input
        TextField projectIdField = new TextField();
        projectIdField.setPromptText("Enter Project ID");
        projectIdField.getStyleClass().add("input-field");

        // Validating that only digits are entered
        projectIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                projectIdField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // Adding components to the grid
        grid.add(new Label("Project ID:"), 0, 0);
        grid.add(projectIdField, 1, 0);

        // Setting content for the dialog
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Setting result converter for the dialog to handle OK/Cancel actions
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try 
                {
                    int projectId = Integer.parseInt(projectIdField.getText());
                    viewTaskUI taskUI = new viewTaskUI(facultyId, projectId,facultyScene);
                    System.out.println(facultyId+" "+projectId);
                    taskUI.start(facultyStage);  // Assume show() initializes and displays the UI for task view
                    return projectId;
                } 
                
                catch (NumberFormatException e) 
                {
                    new AlertBox("Error in Project ID! Please enter a valid number.", true);
                }
            }
            return null;
        });

        // Show the dialog and wait for a response
        dialog.showAndWait();
        System.out.println("Display Progress Button clicked!");
    }

    private void viewProjects(Scene facultyScene,Stage facultyStage,int id)
    {
    	disProjects pro = new disProjects();
    	
    	try
    	{
    		ResultSet r = pro.viewProject(id);
    		projectsPage page = new projectsPage(r,facultyScene);
    		page.start(facultyStage);
    	}
    	
    	catch(Exception e)
    	{
    		new AlertBox(e.getMessage(),true);
    	}
    	
    	System.out.println("View Projects Button clicked !");
    }

    private void handleBack(Stage facultyStage) 
    {
        if (previousScene != null) 
        {
            facultyStage.setScene(previousScene); 
        }
        
        else
        {
        	new Main().start(facultyStage);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
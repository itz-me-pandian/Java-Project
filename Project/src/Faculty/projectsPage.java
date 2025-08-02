/*Class for Displaying all projects associated with a particular Faculty by their ID */

package Faculty;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Connection.ConnectionManager;
import UI.AlertBox;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// class for returning RedultSet of Projects for a Given Faculty ID
class disProjects
{

	public ResultSet viewProject(int fid) 
	{
	    try 
	    {
	        // Establishing the connection to the database
	        Connection con = ConnectionManager.getConnection();
	        Statement st = con.createStatement();
	    
	        String projectQuery = "SELECT * FROM project WHERE facultyId = " + fid;
	        ResultSet projectResultSet = st.executeQuery(projectQuery);
	        
	        if (projectResultSet.next())
	        {
	            return projectResultSet;
	        }
	        
	        else
	        {
	        	throw new NullPointerException("No projects found for the faculty with ID: "  + fid);
	        }
	           
	    }
	    
	    catch (SQLException e)
	    {
	        throw new NullPointerException("Database error");
	    }
	
	}
}

// UI class for retrieving ResultSet and displaying Projects of a Particular Faculty By ID
public class projectsPage extends Application 
{
    private ResultSet r;
    public Scene previousScene;

    public projectsPage(ResultSet r, Scene passed) 
    {
        this.r = r;
        this.previousScene = passed;
    }

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage projectStage) 
    {
        System.out.println("Entered here");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getStyleClass().add("main-layout");
        
        layout.getStylesheets().add(getClass().getResource("/resources/fac_proj.css").toExternalForm());

        try 
        {
            Button backArrow = new Button("Back");
            backArrow.setStyle("-fx-font-size: 18px; -fx-text-fill: #0078D7; -fx-cursor: hand;");

            backArrow.setOnAction(event -> {
                projectStage.setTitle("Faculty Page");
                projectStage.setScene(previousScene);
            });

            layout.getChildren().add(backArrow);

            // Ensuring that the cursor is on the first row
            do 
            {
                int projectId = r.getInt("projectId");
                String projectName = r.getString("projectname");
                String projectStatus = r.getString("status");
                int completionStatus = r.getInt("completionstatus");

                // Creating Labels with formatted text
                Label projIdLabel = new Label("Project ID: " + projectId);
                projIdLabel.getStyleClass().add("label-header");

                Label projNameLabel = new Label("Project Name: " + projectName);
                projNameLabel.getStyleClass().add("label-header");

                Label projStatusLabel = new Label("Project Status: " + projectStatus);
                projStatusLabel.getStyleClass().add("label-header");

                Label projCompStatusLabel = new Label("Completion Status: " + completionStatus);
                projCompStatusLabel.getStyleClass().add("label-header");

                // Creating  VBox for each project's details
                VBox projectBox = new VBox(5); 
                projectBox.getChildren().addAll(projIdLabel, projNameLabel, projStatusLabel, projCompStatusLabel);
                projectBox.getStyleClass().add("project-box");

                // Adding VBox to main layout
                layout.getChildren().add(projectBox);
                layout.setAlignment(Pos.BASELINE_CENTER);

            } while (r.next());

        } 
        
        catch (SQLException e) 
        {
            e.printStackTrace();
        }

        // Setting the scene and showing the stage
        Scene scene = new Scene(layout, 600, 400);
        projectStage.setScene(scene);
        projectStage.setTitle("Project Details");
        projectStage.show();
    }
}


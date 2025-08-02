/*UI class for Displaying Particular Student Task
  It is used in ViewProject.java */

package Student;

import java.sql.ResultSet;

import UI.AlertBox;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// class for Wrapping the Task details of a particular student
class TaskDetails 
{
    private String projectName;
    private String taskName;
    private String taskProgress;

    public TaskDetails(String projectName, String taskName, String taskProgress) 
    {
        this.projectName = projectName;
        this.taskName = taskName;
        this.taskProgress = taskProgress;
    }

    public String getProjectName() throws NullPointerException
    {
    	if(projectName!=null)
    		return projectName;
    	
    	else
    		throw new NullPointerException("Not Involved in any Project!\nSubmit your Project Idea");
    }

    public String getTaskName() throws NullPointerException
    {
    	if(taskName!=null)
    		return taskName;
    	
    	else
    		throw new NullPointerException("Not Involved in any Task");
    }

    public String getTaskProgress() throws NullPointerException
    {
    	if(taskProgress!=null)
    		return taskProgress;
    	
    	else
    		throw new NullPointerException("Error in getting Progress");
    }
}

// Printing the Student Task
public class StudentTask extends Application 
{

    private String username;
    private String password;
    private int digitalId;
    private Scene previousScene;
    
    public StudentTask() {}

    public StudentTask(String username, String password, int digitalId,Scene passed) {
        this.username = username;
        this.password = password;
        this.digitalId = digitalId;
        this.previousScene = passed;
    }

    @Override
    public void start(Stage primaryStage) 
    {
        // Create the UI layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getStyleClass().add("main-layout");

        // Fetch the task details
        ViewProject viewProject = new ViewProject();
        TaskDetails taskDetails;
        try
        {
        	ResultSet r = ViewProject.CheckStatus(digitalId);
        }
        
        catch(Exception e)
        {
        	new AlertBox("Error in Getting Student Result Set !",true);
        	return ;
        }
        
        try 
        {
        	System.out.println(username+" "+password+" "+digitalId);
            taskDetails = viewProject.viewTask(username, password, digitalId);
        } 
        
        catch (Exception e) 
        {
        	new AlertBox("Failed to fetch task details: "+e.getMessage() ,true);
            return;
        }
        
        Button backArrow = new Button("Back");
        backArrow.setStyle("-fx-font-size: 18px; -fx-cursor: hand;");

        backArrow.setOnAction(event -> {
            primaryStage.setTitle("Student Page");
            primaryStage.setScene(previousScene);
        });
        
        try
        {	
        	
        	Label header = new Label("Your Current Status of your Task in the Project !");
        	header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #4A90E2; -fx-padding: 20px;");

	        // Display task details
	        Label projectNameLabel = new Label("Project Name : " + taskDetails.getProjectName());
	        projectNameLabel.getStyleClass().add("label-header");
	        
	
	        Label taskNameLabel = new Label("Task Name : " + taskDetails.getTaskName());
	        taskNameLabel.getStyleClass().add("label-header");
	
	        Label taskProgressLabel = new Label("Progress : " + taskDetails.getTaskProgress());
	        taskProgressLabel.getStyleClass().add("label-header");
	
	        //layout.getChildren().addAll(backArrow, studName,studId,studEmail,facultyId,facultyName,projectID,projectNameLabel, taskNameLabel, taskProgressLabel);
	        layout.getChildren().addAll(backArrow, header, projectNameLabel, taskNameLabel, taskProgressLabel);
	        
	        // Set up the scene
	        Scene scene = new Scene(layout, 400, 300);
	        scene.getStylesheets().add(getClass().getResource("/resources/taskdetails.css").toExternalForm());
	
	        primaryStage.setTitle("Task Details");
	        primaryStage.setScene(scene);
	        primaryStage.show();
        }
        
        catch(NullPointerException e)
        {
        	new AlertBox(e.getMessage(),true);
        }
    }
    

    public static void main(String[] args) {
        launch(args);
    }
}
/*UI class for Displaying project of a Student when display project button is clicked
  It uses displayStud class for retrieving Student ResultSet  */

package Student;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.ResultSet;
import java.sql.SQLException;

import Exceptions.displayStudException;
import UI.AlertBox;

public class projectPage extends Application 
{
    private displayStud studentDetails;
    private String Username, Password;
    private int id;
    private String projname;
    private Scene previousScene;
    
    public projectPage(Scene passed, String Username, String Password, int id) 
    {
        this.Username = Username;
        this.Password = Password;
        this.id = id;
        this.previousScene = passed;
    }
    
    @Override
    public void start(Stage projectStage) 
    {
        studentDetails = new displayStud(Username, Password, id);
        
        try
        {
	        studentDetails.retrieveStudentInfo();
	        
	        Button backArrow = new Button("Back");
            backArrow.setStyle("-fx-font-size: 18px; -fx-cursor: hand;");

            backArrow.setOnAction(event -> {
                projectStage.setTitle("Student Page");
                Student temp = new Student(Username,Password,id);
                temp.start(projectStage);
                
                projectStage.setScene(previousScene);
            });
	        
	        Label studentLabel = new Label("Student Information        :- ");
	        Label facultyLabel = new Label("Faculty Mentor Information :- ");
	
	        Label facultyIdLabel = new Label("Faculty ID       :");
	        TextField facultyIdField = new TextField(studentDetails.getFacultyId());
	        facultyIdField.setMaxSize(100,50);
	        facultyIdField.setEditable(false);
	
	        Label facultyNameLabel = new Label("Faculty Name :");
	        TextField facultyNameField = new TextField(studentDetails.getFacultyName());
	        facultyNameField.setEditable(false);
	        facultyNameField.setMaxSize(100, 50);
	        facultyNameField.getStyleClass().add("label-header");
	
	        VBox facultyInfoBox = new VBox(10, facultyIdLabel, facultyIdField, facultyNameLabel, facultyNameField,backArrow);
	        facultyInfoBox.setPadding(new Insets(10));
	        facultyInfoBox.setAlignment(Pos.CENTER);

	
	        TableView<ProjectTask> studentTable = new TableView<>();
	        studentTable.setMaxWidth(450);
	        
	        TableColumn<ProjectTask, String> studentColumn = new TableColumn<>("Student Name");
	        studentColumn.setPrefWidth(150);
	        studentColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStudentName()));
	
	        TableColumn<ProjectTask, String> progressColumn = new TableColumn<>("Task Progress");
	        progressColumn.setPrefWidth(150);
	        progressColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTaskProgress()));
	     
	        TableColumn<ProjectTask, String> taskColumn = new TableColumn<>("Task Name");
	        taskColumn.setPrefWidth(150);
	        taskColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTaskName()));
	
	        
	        studentTable.getColumns().addAll(studentColumn, progressColumn, taskColumn);
	
	        studentDetails.retrieveStudentInfo();                                         //method in Student.displayStud.java
	        populateStudentTable(studentDetails.getStudentResultSet(), studentTable);     //method here and method in Student.displayStud.java
	
	        Label projectNameLabel = new Label("Project Name         : " + projname);	  //assigned to projname when populateStudentTable is called
	        
	        VBox layout = new VBox(15, projectNameLabel, studentLabel, studentTable, facultyLabel, facultyInfoBox);
	        layout.setAlignment(Pos.CENTER);
	        layout.setPrefWidth(200);
	        layout.setPadding(new Insets(15));
	
	        Scene scene = new Scene(layout, 600, 500);
	        scene.getStylesheets().add(getClass().getResource("/resources/projectPage.css").toExternalForm());
	        projectStage.setScene(scene);
	        projectStage.setTitle("Project and Faculty Details");
	        projectStage.show();
        }
        
        catch(displayStudException e)
        {
        	new AlertBox(e.toString() ,true);
        	return ;
        }
        
        catch(NullPointerException e)
        {
        	new AlertBox(e.getMessage() ,true);
        	//new AlertBox( "No faculty mentors this project",true);
        	return ;
        }
    }
    
    private void populateStudentTable(ResultSet studentResultSet, TableView<ProjectTask> studentTable) {
        try 
        {
            while (studentResultSet != null && studentResultSet.next()) 
            {
                String studentName = studentResultSet.getString("name");
                String taskName = studentResultSet.getString("taskname");
                String taskProgress = studentResultSet.getString("taskprogress");
                projname = studentResultSet.getString("projectname");
                studentTable.getItems().add(new ProjectTask(taskName, taskProgress, studentName));
             }
            
        } 
        
        catch (SQLException e) 
        {
            new AlertBox("Error loading student data: " + e.getMessage(),true);
        }
    }

    public static void main(String[] args) 
    {
        launch(args);
    }
    
    
    public static class ProjectTask 
    {
        private String taskName;
        private String taskProgress;
        private String studentName;

        public ProjectTask(String taskName, String taskProgress, String studentName) 
        {
            this.taskName = taskName;
            this.taskProgress = taskProgress;
            this.studentName = studentName;
        }

        public String getTaskName() { return taskName; }
        public String getTaskProgress() { return taskProgress; }
        public String getStudentName() { return studentName; }
    }
}
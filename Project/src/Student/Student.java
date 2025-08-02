/*Student class for displaying UI for login */

package Student;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.scene.control.Label;
import Connection.ConnectionManager;
import UI.AlertBox;
import UI.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Student extends Application 
{
	public String Username,Password;
	public int id;
    
    public Student() {}
    
    public Student(int id)
    {
    	this.id = id;
    }
    
    public Student(String usr,String pwd,int id)
    {
    	this.Username = usr;
    	this.Password = pwd;
    	this.id = id;
    }

    @Override
    public void start(Stage studentStage) 
    {
    	
    	Label optionLabel = new Label("Choose the Option");
        optionLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #4A90E2; -fx-padding: 20px;");

       
        Button submitProposalButton = new Button("Submit Project ");
        Button displayProjectButton = new Button("Display Project");
        Button updateTaskButton = new Button("Update Task");
        Button viewTaskButton   = new Button("View Task");
        Button projectStatusButton = new Button("Project Status");
        Button backButton = new Button("Logout");
        
        double buttonWidth = 220;
        submitProposalButton.setPrefWidth(buttonWidth);
        displayProjectButton.setPrefWidth(buttonWidth);
        updateTaskButton.setPrefWidth(buttonWidth);
        viewTaskButton.setPrefWidth(buttonWidth);
        projectStatusButton.setPrefWidth(buttonWidth);
        backButton.setPrefWidth(buttonWidth);

        
        VBox layout;
        System.out.println(isStudentNotAssignedToProject(id)+" "+id);
        
        if(isStudentNotAssignedToProject(id))
        	layout = new VBox(10, optionLabel, submitProposalButton, displayProjectButton, viewTaskButton, backButton);
        else
        	layout = new VBox(10, optionLabel, displayProjectButton, viewTaskButton, updateTaskButton, projectStatusButton, backButton);
        
        layout.setSpacing(10);
        layout.setAlignment(Pos.CENTER);

        
        Scene studentScene = new Scene(layout, 300, 200);
        studentScene.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());
        
        submitProposalButton.setOnAction(event -> handleSubmitProposal(studentScene,studentStage,Username,Password,id));
        displayProjectButton.setOnAction(event -> handleDisplayProject(studentScene,studentStage,Username,Password,id));
        viewTaskButton.setOnAction(event -> handleViewTask(studentScene,studentStage,Username,Password,id));
        updateTaskButton.setOnAction(event -> handleUpdateTask(studentStage,studentScene,id));
        projectStatusButton.setOnAction(event -> handleCheckStatus(Username,Password,id));
        backButton.setOnAction(event -> new Main().start(studentStage));
        
        studentStage.setTitle("Student Page");
        studentStage.setScene(studentScene);
        
        
        studentStage.show();
    }

   
    private void handleSubmitProposal(Scene studentScene,Stage studentStage,String Username,String Password,int id) 
    {
        System.out.println("Submit Project Proposal clicked.");
        projectSubmit ps = new projectSubmit(studentScene,Username,Password,id);
        ps.start(studentStage);
    }

    private void handleDisplayProject(Scene studentScene,Stage studentStage,String Username,String Password,int id) 
    {
        System.out.println("Display Project clicked.");
        projectPage page = new projectPage(studentScene,Username,Password,id);
        page.start(studentStage);
    }
    
    private void handleViewTask(Scene studentScene,Stage studentStage,String Username,String Password,int id)
    {
    	System.out.println("View Project clicked.");
    	StudentTask s = new StudentTask(Username,Password,id,studentScene);
    	s.start(studentStage);
    }

    private void handleUpdateTask(Stage studentStage,Scene studentScene,int id) 
    {
        System.out.println("Update Task clicked.");
        updateTaskUI temp = new updateTaskUI(studentScene,id);
        temp.start(studentStage);
    }
    
    private void handleCheckStatus(String Username,String password,int id)
    {
    	try
    	{
    		ResultSet r = ViewProject.CheckStatus(id);
    		String status = r.getString("status");
    		int cstatus  = r.getInt("completionstatus");
    		new AlertBox("Your Project Proposal is in the Status   : '"+status+"'\nYour Project Overall Completion Status: '"+cstatus+"'",false);
    	}
    	
    	catch(Exception e)
    	{
    		new AlertBox(e.getMessage(),true);
    	}
    	
    	System.out.println("Check Status Button Clicked");
    }
    
    public boolean isStudentNotAssignedToProject(int digitalId) 
    {
        String query = "SELECT projectID FROM student WHERE digital_id = ?";
        
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            
            pstmt.setInt(1, digitalId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) 
            {
                return rs.getObject("projectID") == null;
            }
            
        }
        
        catch (SQLException e) 
        {
            System.out.println("Database Error: " + e.getMessage());
        }
       
        return true;
    }

    public static void main(String[] args) 
    {
        launch(args);
    }
}



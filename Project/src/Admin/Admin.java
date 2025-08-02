/*UI class of Admin after Login which contains buttons for all Admin Functionalities with their function calls */

package Admin;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.scene.control.Label;
import Connection.ConnectionManager;
import UI.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
	
public class Admin  extends Application
{
	public String Username,Password;
	public int id;
    public Scene previousScene;
    
    public Admin() {}
    
    public Admin(int id)
    {
    	this.id = id;
    	//this.previousScene = passed;
    }
    
    public Admin(String usr,String pwd,int id)
    {
    	this.Username = usr;
    	this.Password = pwd;
    	this.id = id;
    }

    @Override
    public void start(Stage adminStage) {
    	
    	Label optionLabel = new Label("Choose the Option");
        optionLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #4A90E2; -fx-padding: 20px;");

       
        Button displayRequestButton = new Button("Display Request ");
        Button viewProjectButton = new Button("View Students");
        Button backButton = new Button("Logout");
        
        double buttonWidth = 220;
        displayRequestButton.setPrefWidth(buttonWidth);
        viewProjectButton.setPrefWidth(buttonWidth);
        backButton.setPrefWidth(buttonWidth);

        
        VBox layout;
        layout = new VBox(10, optionLabel, displayRequestButton, viewProjectButton, backButton);
        
        layout.setSpacing(10);
        layout.setAlignment(Pos.CENTER);

        
        Scene adminScene = new Scene(layout, 300, 200);
        adminScene.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());
        
        backButton.setOnAction(event -> handleBack(adminStage));
        displayRequestButton.setOnAction(event -> displayRequest(adminScene,adminStage));
        viewProjectButton.setOnAction(event -> viewProjects(adminScene,adminStage));
        
        adminStage.setTitle("Admin Page");
        adminStage.setScene(adminScene);
        
        
        adminStage.show();
    }

   
    private void displayRequest(Scene adminScene,Stage adminStage)
    {
    	changeStatus temp = new changeStatus(adminScene);
    	temp.start(adminStage);
    	
    	System.out.println("Display Request Button clicked !");
    }
    
    private void viewProjects(Scene adminScene,Stage adminStage)
    {
    	dispProject temp = new dispProject();
    	temp.DisplayProj();
    	
    	System.out.println("View Projects Button clicked !");
    }

    private void handleBack(Stage adminStage) 
    {
        if (previousScene != null) 
        {
            adminStage.setScene(previousScene); 
        }
        
        else{
        	new Main().start(adminStage);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}




	
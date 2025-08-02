/*Main UI class for Displaying Home Page of the Software
  Entry point of Execution */

package UI;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.stage.Screen;
import javafx.scene.layout.VBox;

public class Main extends Application 
{
    @Override
    public void start(Stage primaryStage) 
    {
        Label label = new Label("Welcome to Student Project Management Software !!");
      
    Button b1 = new Button("Student Login");
    Button b2 = new Button("Staff Login");
    Button b3 = new Button("Admin Login");
    
    b1.setPrefSize(200, 40);
    b2.setPrefSize(200, 40);
    b3.setPrefSize(200, 40);
   
    VBox root = new VBox(10);
    root.setAlignment(Pos.CENTER);
    root.getChildren().add(label);
    root.getChildren().add(b1);
    root.getChildren().add(b2);
    root.getChildren().add(b3);
   
    Scene scene = new Scene(root,1600,1200);
    scene.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());
    
    b1.setOnAction(event ->{
    	Login student = new StudentLogin(scene);
    	student.start(primaryStage);
    });
    
    b2.setOnAction(event ->{
    	Login faculty = new FacultyLogin(scene);
    	faculty.start(primaryStage);
    });
    
    b3.setOnAction(event ->{
    	Login admin = new AdminLogin(scene);
    	admin.start(primaryStage);
    });
       
        primaryStage.setScene(scene);
        primaryStage.setTitle("Home");
        primaryStage.show();
       
    }

    public static void main(String[] args) 
    {
        launch(args);
    }
}

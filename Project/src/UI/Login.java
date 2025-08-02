/*Base class for Login UI of Student,Faculty and Admin */

package UI;

import java.sql.SQLException;

import Exceptions.LoginException;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

public abstract class Login extends Application {

    public String Username, Password, UserType;
    public int digitalID;
    private Scene previousScene;
    private Stage sel; // stage for displaying Separate window for getting usertype(Student/Faculty) for Registration

    Login() {}

    Login(Scene passed) 
    {
        this.previousScene = passed;
    }

    public void start(Stage loginStage)
    {
        loginStage.setTitle("Login Page");

        Button backArrow = new Button("Back");
        backArrow.setStyle("-fx-font-size: 18px; -fx-cursor: hand;");

        backArrow.setOnAction(event -> {
            loginStage.setTitle("Home");
            loginStage.setScene(previousScene);
        });

        TextField tf = new TextField();
        TextField nf = new TextField();
        PasswordField pf = new PasswordField();
        
        nf.textProperty().addListener((observable, oldValue, newValue) -> 
        {
            if (!newValue.matches("\\d*")) //regular expression for matching only digits while Entering DigitalId
            { 
                nf.setText(oldValue); 
            }
        });

        Label text = new Label("Username ");
        Label id   = new Label("Digital ID");
        Label pass = new Label("Password  ");

        Button login = new Button("Login");
        Button reg = new Button("Register");

        login.setPrefSize(100, 20);
        reg.setPrefSize(120, 20); 

        GridPane loginPage = new GridPane();
        loginPage.add(backArrow, 0, 0, 2, 1);
        loginPage.addRow(1, text, tf);
        loginPage.addRow(2, id,nf);
        loginPage.addRow(3, pass, pf);

        // Create an HBox for buttons and add it to the GridPane
        HBox buttonBox = new HBox(10); 
        buttonBox.getChildren().addAll(login, reg); 
        buttonBox.setAlignment(Pos.CENTER); 

        loginPage.add(buttonBox, 0, 4, 2, 1);

        loginPage.setAlignment(Pos.CENTER);
        loginPage.setHgap(10);
        loginPage.setVgap(10);

        // Login button action
        login.setOnAction(new EventHandler<ActionEvent>() 
        {
            public void handle(ActionEvent event) {
                Username = tf.getText();
                Password = pf.getText();
                String input = nf.getText().trim(); 
                
                if(input.equals(null))
                {
                	new AlertBox("Enter correct Digital ID !",true);
                	return ;
                }

				if (input.matches("\\d+")) 
				{ 
				    digitalID = Integer.parseInt(input);
				    System.out.println("Converted to integer: " + digitalID);  
				} 
				
				else 
				{
				    System.out.println("Invalid input: " + input + " is not a number.");
				}

			    try
			    {
	                if (validate(Username, digitalID, Password)) 
	                {
	                    redirect(loginStage,Username,Password,digitalID); 
	                } 
	                
	                else
	                	new AlertBox("Invalid Username or Password !",true);
                
                }
			    
			    catch(LoginException e)
			    {
			    	new AlertBox(e.message,true);
			    }
            }
        });

        reg.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) 
            {  
            	
            	if(sel!=null && sel.isShowing())
            		sel.close();
            	
                if (sel == null || !sel.isShowing()) {
                    sel = new Stage(); // Creating new stage
                    sel.setTitle("Select User Type");

                    // Creating RadioButtons
                    RadioButton studentRadioButton = new RadioButton("Student");
                    RadioButton facultyRadioButton = new RadioButton("Faculty");
                    
                    studentRadioButton.setStyle("-fx-font-weight: bold;");
                    facultyRadioButton.setStyle("-fx-font-weight: bold;");

                    // Grouping the RadioButtons so only one can be selected at a time
                    ToggleGroup userTypeGroup = new ToggleGroup();
                    studentRadioButton.setToggleGroup(userTypeGroup);
                    facultyRadioButton.setToggleGroup(userTypeGroup);

                    GridPane sele = new GridPane();
                    sele.addColumn(1, studentRadioButton, facultyRadioButton);
                    sele.setAlignment(Pos.CENTER);
                    sele.setVgap(20);

                    // Creating and setting the new scene for user type selection
                    Scene newScene = new Scene(sele, 400, 300);
                    newScene.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());
                    sel.setScene(newScene);
                    sel.setResizable(false);
                    sel.show();

                    // Define action listeners for the radio buttons using EventHandler<ActionEvent>
                    studentRadioButton.setOnAction(new EventHandler<ActionEvent>() 
                    {
                        @Override
                        public void handle(ActionEvent studentEvent) {
                            UserType = "Student"; 
                            sel.close(); 
                            openRegisterPage(loginStage);  //method call for Student registration
                        }
                    });

                    facultyRadioButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent facultyEvent) {
                            UserType = "Faculty";  
                            sel.close();  
                            openRegisterPage(loginStage);  //method call for Student registration
                        }
                    });
                }
            }
        });

        Scene scene = new Scene(loginPage, 400, 300);
        scene.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());

        loginStage.setScene(scene);
        loginStage.show();
    }

    private void openRegisterPage(Stage loginStage) 
    {
        if (UserType.equals("Student")) 
        {
            StudentRegister studentRegister = new StudentRegister(previousScene);
            studentRegister.start(loginStage); 
        } 
        
        else if (UserType.equals("Faculty")) 
        {
            FacultyRegister facultyRegister = new FacultyRegister(previousScene); 
            facultyRegister.start(loginStage);
        }
    }
    
    // Abstract Methods which will be implemented by the Subclass extending this class
    
    abstract boolean validate(String username, int digitalID, String password) throws LoginException;

    abstract void redirect(Stage loginStage,String Username,String Password,int id);

}

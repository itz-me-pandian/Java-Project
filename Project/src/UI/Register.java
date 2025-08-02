/*Base class For Registration UI for Student,Faculty 
  It throws RegistrationFailedException when Registration error occurs */

package UI;

import Exceptions.RegistrationFailedException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.*;

public abstract class Register extends Application
{
	String Username,Password,Email;
	int digitalID;
	Scene previousScene;
	
	Register(){}
	
	Register(Scene passed)
	{
		this.previousScene = passed;
	}
	
	public void start(Stage RegisterStage)
	{
		RegisterStage.setTitle("Register");
		
		Button backArrow = new Button("Back");
        backArrow.setStyle("-fx-font-size: 18px; -fx-cursor: hand;");

        backArrow.setOnAction(event -> {
        	RegisterStage.setTitle("Home");
        	RegisterStage.setScene(previousScene);
        });
        
        TextField tf = new TextField();
        tf.setPromptText("Enter your Name");
        
        TextField nf = new TextField();
        nf.setPromptText("Enter the Digital ID");
        
        PasswordField pf = new PasswordField();
        pf.setPromptText("Enter the Password");
        
        PasswordField pf1 = new PasswordField();
        pf1.setPromptText("Confirm the Password");
        
        TextField ef = new TextField();
        ef.setPromptText("Enter the Email ID");
        
        Button register = new Button("Register");
        
        nf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { 
                nf.setText(oldValue); 
            }
        });
        
        Label text = new Label("Username");
        Label id   = new Label("Digital ID");
        Label mail = new Label("Mail ID");
        Label pass = new Label("Password");
        Label pass1 = new Label("Confirm Password");
        
        GridPane registerPage = new GridPane();
        registerPage.add(backArrow, 0, 0, 2, 1);
        registerPage.addRow(1,text,tf);
        registerPage.addRow(2, id,nf);
        registerPage.addRow(3, mail,ef);
        registerPage.addRow(4, pass,pf);
        registerPage.addRow(5, pass1,pf1);
        registerPage.addRow(6,register);
        
        registerPage.setAlignment(Pos.CENTER);
        registerPage.setVgap(10);
        registerPage.setHgap(10);
        
        register.setOnAction(new EventHandler<ActionEvent>()
        {
        	public void handle(ActionEvent event)
			{
				Username = tf.getText();
				Email    = ef.getText();
				Password = pf.getText();
				String pass = pf1.getText();
				String input = nf.getText().trim(); 
				int flag = 0;
				
				if(input == null)
				{
					new AlertBox("Enter all Details",true);
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
				
				System.out.println(Username+" "+Password+" "+Email+" "+digitalID);
				
				if(Username.equals(null) || Email.equals(null) || Password.equals(null) || digitalID==0)
				{
					new AlertBox("Enter all details !",true);
					return ;
				}
				
				if(!isValidEmail(Email) && flag!=1)
				{
					new AlertBox("Invalid Email Format !",true);
					System.out.println("Invalid Email Format !");
					flag=1;
				}
				
				if(!reCheckPass(Password,pass) && flag!=1)
				{
					System.out.println("Entered Password and Confirmation Password are Different !");
					new AlertBox("Entered Password and Confirmation Password are Different !",true);
					flag=1;
				}
				
				if(flag!=1)
				{
					try
					{
						if(validate(Username,Password,digitalID,Email))
						{
							new AlertBox("Registration Successful!\nLogin with your Credentials !!",false);
							redirect(RegisterStage);
						}
					}
					
					catch(RegistrationFailedException e)
					{
						new AlertBox(e.message,true);
					}
				}
			}
        });
        
        Scene scene = new Scene(registerPage,400,300);
        scene.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());

        RegisterStage.setScene(scene);
        RegisterStage.show();
     
	}
	
	public boolean isValidEmail(String email)
	{
		String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
		return email.matches(emailRegex);
	}
	
	public boolean reCheckPass(String pass,String pass1)
	{
	    return pass1.equals(pass);
	}
	
	abstract boolean validate(String username,String pass,int digitalID,String Email) throws RegistrationFailedException;
    abstract void redirect(Stage RegisterStage);
}

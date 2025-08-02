/*Child class for Validating and Redirecting Faculty after Faculty Login */

package UI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Connection.ConnectionManager;
import Exceptions.*;
import Faculty.Faculty;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class FacultyLogin extends Login
{
	FacultyLogin(){}
	
	FacultyLogin(Scene passed)
	{
		super(passed);
	}
	
	public boolean validate(String username,int digitalID,String password) throws LoginException
	{
	        try
	        {
	        	Connection con = ConnectionManager.getConnection();

		        // Use a prepared statement to prevent SQL injection
		        String query = "SELECT * FROM faculty WHERE name = ? AND password = ? AND facultyid = ?";

		        // Prepare the statement
		        PreparedStatement pstmt = con.prepareStatement(query);

		        // Set the values for the placeholder (?)
		        
		        pstmt.setString(1, username);
		        pstmt.setString(2, password);
		        pstmt.setInt(3, digitalID);
		        
		        // Execute the query
		        ResultSet r1 = pstmt.executeQuery();

		        // Check if a record was found
		        boolean isValidUser = r1.next();

		        // Close resources
		        r1.close();
		        pstmt.close();
		        con.close();
		        
		        return isValidUser;
	        }
	        
	        catch(SQLException e)
	        {
	        	throw new LoginException("Error occured while Connecting with database");
	        }
	}
	
	public void redirect(Stage loginStage,String Username,String Password,int id)
	{
		new AlertBox("Faculty Login Successful",false);
		Faculty faculty = new Faculty(Username,Password,id);
		faculty.start(loginStage);
	}

}

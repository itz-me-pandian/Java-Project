/*Child class for Validating and Redirecting Faculty after Faculty Registration*/

package UI;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Connection.ConnectionManager;
import Exceptions.RegistrationFailedException;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class FacultyRegister extends Register {

    public FacultyRegister() {}

    public FacultyRegister(Scene passed) {
        this.previousScene = passed;
    }

    @Override
    protected boolean validate(String uname, String pwd, int dig, String email) throws RegistrationFailedException
    {
    	 Connection con = null;
         Statement st = null;
         ResultSet rs = null;

         try 
         {
             con = ConnectionManager.getConnection();
             st = con.createStatement();

             // Query to check if the digital_id already exists
             String checkQuery = "SELECT * FROM faculty WHERE facultyid = " + dig;
             rs = st.executeQuery(checkQuery);

             // If a result exists, the digital_id is already in use
             if (rs.next()) 
             {
            	 if (rs != null) rs.close();
                 if (st != null) st.close();
                 if (con != null) con.close();
             	throw new RegistrationFailedException("Registration Failed: Digital ID already exists");
                //System.out.println("Registration failed: Digital ID already exists.");
             } 
             
             else 
             {
                 // If no result, proceed with inserting the new student
            	 String query = "INSERT INTO faculty (name, password, facultyid, email) VALUES ('" + uname + "', '" + pwd + "', " + dig + ", '" + email + "')";
            	 st.executeUpdate(query);
            	 String dispQuery = "INSERT INTO facultydisp (name, facultyid) VALUES ('" + uname + "', " + dig + ")";
             	 st.executeUpdate(dispQuery);
            	 if (rs != null) rs.close();
                 if (st != null) st.close();
                 if (con != null) con.close();
                 System.out.println("Faculty registered successfully.");
                 return true;
             }
             
         } 
         
         catch(SQLException e)
         {
         	new AlertBox("Registration Failed",true);
         	e.printStackTrace();
         	return false;
         }
         
    }

    @Override
    protected void redirect(Stage registerStage) 
    {
        System.out.println("Faculty Registered Successfully!");
        new Main().start(registerStage);
    }
    

    public static void main(String[] args) 
    {
        launch(args); 
    }
}
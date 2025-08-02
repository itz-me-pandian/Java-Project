/*Child class for Validating and Redirecting Student after Student Registration*/

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

public class StudentRegister extends Register 
{

    public StudentRegister() {}

    public StudentRegister(Scene passed) {
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
             String checkQuery = "SELECT * FROM student WHERE digital_id = " + dig;
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
            	 String query = "INSERT INTO student (name, password, digital_id, email) VALUES ('" + uname + "', '" + pwd + "', " + dig + ", '" + email + "')";
            	 st.executeUpdate(query);
            	 if (rs != null) rs.close();
                 if (st != null) st.close();
                 if (con != null) con.close();
                 //new AlertBox("Student registered successfully !",false);
                 System.out.println("Student registered successfully.");
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
        System.out.println("Student Registered Successfully!");
        System.out.println(Username+" "+Password+" "+digitalID+" "+Email);
        new Main().start(registerStage);
    }
    
    public static void NewRegistration(String uname, String pwd, int dig)
    {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try 
        {
            con = ConnectionManager.getConnection();
            st = con.createStatement();

            // Query to check if the digital_id already exists
            String checkQuery = "SELECT * FROM student WHERE digital_id = " + dig;
            rs = st.executeQuery(checkQuery);

            // If a result exists, the digital_id is already in use
            if (rs.next()) 
            {
            	new AlertBox("Registration Failed: Digital ID already exists.",true);
                System.out.println("Registration failed: Digital ID already exists.");
            } 
            
            else 
            {
                // If no result, proceed with inserting the new student
                String query = "INSERT INTO student (name, password, digital_id) VALUES ('" + uname + "', '" + pwd + "', " + dig + ")";
                st.executeUpdate(query);
                new AlertBox("Student registered successfully !",false);
                System.out.println("Student registered successfully.");
            }
            
            if (rs != null) rs.close();
            if (st != null) st.close();
            if (con != null) con.close();
        } 
        
        catch(SQLException e)
        {
        	new AlertBox("Registration Failed",true);
        	e.printStackTrace();
        }
        
    }

    public static void main(String[] args) 
    {
        launch(args); 
    }
}


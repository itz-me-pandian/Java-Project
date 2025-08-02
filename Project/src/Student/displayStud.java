/*Java Class for fetching details of faculty (backend) by student DigitalId. This class is used by projectPage to display Student Details in the frontend */

package Student;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Connection.ConnectionManager;
import Exceptions.displayStudException;
import UI.AlertBox;
import javafx.scene.control.Alert;

public class displayStud 
{
    private String uname;
    private String pwd;
    private int dig;

    // Individual fields to store results
    private String facultyName;
    private String facultyId;
    private ResultSet studentResultSet;
    private ResultSet facultyResultSet;

    // Constructor
    public displayStud(String uname, String pwd, int dig) 
    {
        this.uname = uname;
        this.pwd = pwd;
        this.dig = dig;
    }

    // Method to execute the logic and populate fields
    public void retrieveStudentInfo()  throws NullPointerException, displayStudException
    {
        Connection con = null;
        Statement st = null;
        ResultSet r1 = null;

        try 
        {
            con = ConnectionManager.getConnection();
            st = con.createStatement();

            // Query to find the student based on credentials
            String query = "SELECT * FROM student WHERE name = '" + uname + "' AND password = '" + pwd + "' AND digital_id = " + dig;
            r1 = st.executeQuery(query);

            // Check if any student record was retrieved
            if (r1.next()) 
            {
                int projectId = r1.getInt("projectId");

                // Check if projectId is null
                if (r1.wasNull()) 
                {
                    //new AlertBox("Student has not been assigned a project." ,true);
                    //throw new NullPointerException();
                    throw new displayStudException("Student has not been assigned a project.");
                }

                // Query for faculty information based on the projectId
                String fquery = "SELECT * FROM faculty WHERE projectId = " + projectId;
                facultyResultSet = st.executeQuery(fquery);

                if (facultyResultSet.next()) 
                {
                    facultyId = facultyResultSet.getString("facultyId");
                    facultyName = facultyResultSet.getString("name");
                } 
                
                else 
                {
                    throw new NullPointerException("No faculty mentors this project.");
                }

                query = "SELECT * FROM student WHERE projectId = " + projectId;
                studentResultSet = st.executeQuery(query);

            } 
            
            else 
            {
                throw new NullPointerException("No user found with the provided credentials !");
            }

        } 
        
        catch (SQLException e) 
        {
            new AlertBox("Database error: " + e.getMessage(),true);
        }
    }

    // Get methods to return individual variables and ResultSets
    public String getFacultyName() 
    {
        return facultyName;
    }

    public String getFacultyId() 
    {
        return facultyId;
    }

    public ResultSet getStudentResultSet() 
    {
        return studentResultSet;
    }

    public ResultSet getFacultyResultSet() 
    {
        return facultyResultSet;
    }
}

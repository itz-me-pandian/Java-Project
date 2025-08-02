/*Class containing method for displaying Project details(Students involved in the project) 
  It uses StudentInfo.java and TaskDetails of StudentTask.java for wrapping the details of Students and Task */

package Student;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Connection.*;
import Exceptions.*;
import UI.AlertBox;

public class ViewProject 
{
    public StudentInfo displayStud(String uname, String pwd, int dig) throws NoUserFoundException, displayStudException {
        Connection con = null;
        Statement st = null;
        ResultSet r1 = null;
        String facultyName = null;
        String facultyId = null;

        try 
        {
            con = ConnectionManager.getConnection();
            st = con.createStatement();

            // Query to find the student based on credentials
            String query = "SELECT * FROM student WHERE name = '" + uname + "' AND password = '" + pwd + "' AND digital_id = " + dig;
            r1 = st.executeQuery(query);

            // Check if any student record was retrieved
            if (r1.next()) {
                int projectId = r1.getInt("projectId");

                // Check if projectId is null
                if (r1.wasNull()) {
                    throw new displayStudException("Student has not been assigned a project.");
                }

                // Query for faculty information based on the projectId
                String fquery = "SELECT * FROM faculty WHERE projectId = " + projectId;
                ResultSet r3 = st.executeQuery(fquery);

                if (r3.next()) {
                    facultyId = r3.getString("projectId");
                    facultyName = r3.getString("name");
                } 
                
                else 
                {
                	facultyId = null; 
                    facultyName = null;
                }
                r3.close();

                // Returning a StudentInfo object containing faculty details and the student ResultSet
                return new StudentInfo(facultyName, facultyId, r1);

            } 
            
            else 
            {
                throw new NoUserFoundException("No user found with the provided credentials.");
            }
        } 
        
        catch (SQLException e) 
        {
            throw new displayStudException("Database error: " + e.getMessage());
        } 
}
    
    
public TaskDetails viewTask(String uname, String pwd, int dig) 
{
        Connection con = null;
        Statement st = null;
        ResultSet r = null;

        try 
        {
            con = ConnectionManager.getConnection();
            st = con.createStatement();

            // Fetch task details
            String query = "SELECT projectname, taskname, taskprogress FROM student WHERE digital_id = " + dig;
            r = st.executeQuery(query);

            if (r.next()) 
            {
                // Retrieve task details
                String projectName = r.getString("projectname");
                String taskName = r.getString("taskname");

                // Check if taskProgress is null
                Integer taskProgress = r.getInt("taskprogress");
                String progressDisplay;

                // Use if-else to check for null
                if (r.wasNull()) 
                {
                    progressDisplay = "Not set"; // Task progress is null
                } 
                
                else 
                {
                    progressDisplay = taskProgress + "%"; // Task progress is available
                }

                // Returning the task details wrapped in the TaskDetails class
                return new TaskDetails(projectName, taskName, progressDisplay);
            } 
            
            else 
            {
                // Display an alert for no tasks found
                new AlertBox("No tasks found for the student with digital ID: " + dig, true);
                
                // Return a default or empty TaskDetails object
                return new TaskDetails("No Project", "No Task", "No Progress");
            }
        } 
        
        catch (SQLException e) 
        {
            // Wrap and throw a custom exception
            throw new RuntimeException("Database error occurred while fetching task details: " + e.getMessage(), e);
        } 
        
        finally 
        {
            // Clean up resources
            try {
                if (r != null) r.close();
                if (st != null) st.close();
                if (con != null) con.close();
            } 
            
            catch (SQLException e) 
            {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
}

public static ResultSet CheckStatus(int dig) throws SQLException,CheckStatusException 
{
        Connection con = ConnectionManager.getConnection();
        Statement st = con.createStatement();

        // Fetch task details
        int pid;
        String query = "SELECT projectId from student where digital_id = " + dig;
        ResultSet r1 = st.executeQuery(query);
        ResultSet r2;
        if (r1.next())
        {

            pid = r1.getInt("projectId");
            if (!r1.wasNull())
            {
                query = "select * from project where projectId = "+pid;
                r2 = st.executeQuery(query);
                r2.next();
                System.out.println("The status of your project is: "+r2.getString("status"));
                return r2;
            }
            else
            {
                throw new CheckStatusException("No project proposed");
                //System.out.println("No project proposed");
            }


        }
        else
        {
            throw new CheckStatusException("No record exists");
        }
    }
}
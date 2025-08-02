/*Class for Wrapping Student info 
  This class is used by ViewProject.java to wrap the Student details by creating object of this class */

package Student;

import java.sql.ResultSet;
import java.sql.SQLException;

import UI.AlertBox;

public class StudentInfo 
{
    private String facultyName;
    private String facultyId;
    private ResultSet studentResultSet;

    public StudentInfo(String facultyName, String facultyId, ResultSet studentResultSet) 
    {
        this.facultyName = facultyName;
        this.facultyId = facultyId;
        this.studentResultSet = studentResultSet;
    }

    // Getters for each field
    public String getFacultyName() {
        return facultyName;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public ResultSet getStudentResultSet() {
        return studentResultSet;
    }
    
    public int getProjectId() 
    {
        try 
        {
            return studentResultSet.getInt("projectId");
        } 
        
        catch (SQLException e) 
        {
            new AlertBox("Error in Database", true);
            return -1;
        }
    }

    // Method to get Name
    public String getName() 
    {
        try 
        {
            return studentResultSet.getString("name");
        } 
        
        catch (SQLException e) 
        {
            new AlertBox("Error in Database", true);
            return null;
        }
    }

    // Method to get Email
    public String getEmail() 
    {
        try 
        {
            return studentResultSet.getString("email");
        } 
        
        catch (SQLException e) 
        {
            new AlertBox("Error in Database", true);
            return null;
        }
    }

    // Method to get Password
    public String getPassword() 
    {
        try 
        {
            return studentResultSet.getString("password");
        } 
        
        catch (SQLException e) 
        {
            new AlertBox("Error in Database", true);
            return null;
        }
    }

    // Method to get Project Name
    public String getProjectName()
    {
    	try
    	{
    		return studentResultSet.getString("projectname");
    	}
    	
    	catch(SQLException e)
    	{
    		new AlertBox("Error in Database",true);
    		return null;
    	}
    }

    // Method to get Digital ID
    public String getDigitalId() 
    {
        try 
        {
            return studentResultSet.getString("digital_id");
        } 
        
        catch (SQLException e) 
        {
            new AlertBox("Error in Database", true);
            return null;
        }
    }

    // Method to get Task Name
    public String getTaskName() 
    {
        try 
        {
            return studentResultSet.getString("taskname");
        } 
        
        catch (SQLException e) 
        {
            new AlertBox("Error in Database", true);
            return null;
        }
    }

    // Method to get Task Progress
    public String getTaskProgress() 
    {
        try 
        {
            return studentResultSet.getString("taskprogress");
        } 
        
        catch (SQLException e) 
        {
            new AlertBox("Error in Database", true);
            return null;
        }
    }
}

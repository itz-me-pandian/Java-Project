/*Class for Writting Details of All projects (requested and accepted) details in a file */

package Admin;

import java.sql.Connection;
import java.io.*;
import java.sql.*;

import Connection.ConnectionManager;
import UI.AlertBox;

public class dispProject 
{

    public static void DisplayProj() 
    {
        
        String filePath = "allprojects.txt";
        boolean projectsFound = false;

        try (Connection con = ConnectionManager.getConnection();
             Statement st = con.createStatement();
             ResultSet rs2 = st.executeQuery("select * from student order by projectId");
             BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            // Write column headers
            writer.write("Project ID\tName\tEmail\tProject Name");
            writer.newLine();
           
            // Write data rows
            while (rs2.next()) 
            {
                projectsFound = true;
                int pid;
                
                pid = rs2.getInt("projectId");
                
                if(pid == 0)
                {
	                writer.write("null" + "\t" +
	                        rs2.getString("name") + "\t" +
	                        rs2.getString("email") + "\t" +
	                        rs2.getString("projectname"));
                }
                
                else
                {
                	writer.write(pid + "\t" +
	                        rs2.getString("name") + "\t" +
	                        rs2.getString("email") + "\t" +
	                        rs2.getString("projectname"));
                }
                writer.newLine();
            }

            if (projectsFound) 
            {
                new AlertBox( "Data has been written to " + filePath,false);
            } 
            
            else 
            {
                new AlertBox("No projects were found in the database.",false);
            }

        } 
        
        catch (SQLException e) 
        {
            new AlertBox( "Error checking student assignment: " + e.getMessage(),true);
        } 
        
        catch (IOException e) 
        {
            new AlertBox( "Error writing to file: " + e.getMessage(),true);
        }
    }
}

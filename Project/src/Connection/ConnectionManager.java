package Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager 
{
	 private static Connection connection = null;

	    // Method to get or create a database connection
	    public static Connection getConnection() throws SQLException 
	    {
	        if (connection == null || connection.isClosed()) 
	        {
	            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "pandi12345678");
	        }
	        return connection; 
	    }
}
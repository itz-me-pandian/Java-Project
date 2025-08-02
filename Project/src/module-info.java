module Project
 
{
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.base;
	requires javafx.graphics;
	requires transitive java.sql;
	
	exports UI;
	exports Connection;
	exports Exceptions;
	exports Student;
	exports Admin;
	exports Faculty;
}

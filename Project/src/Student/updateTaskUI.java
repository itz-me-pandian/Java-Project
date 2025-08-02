/*UI class for Updating task for Student Progress when Involved in a Project (frontend/backend) */

package Student;

import Connection.ConnectionManager;
import Exceptions.ProjectRejectedException;
import Exceptions.ViewTaskException;
import UI.AlertBox;
import UI.Main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class updateTaskUI extends Application 
{
    private int id;
    private Scene previousScene;

    public updateTaskUI() {}

    public updateTaskUI(Scene passed, int id)
    {
        this.id = id;
        this.previousScene = passed;
    }

    public static void main(String[] args) 
    {
        launch(args);
    }

    // UI method for Update Task Progress
    public void start(Stage primaryStage) 
    {
        primaryStage.setTitle("Update Task Progress");
        
        Stage dialog = new Stage();
        dialog.setTitle("Update Task Progress");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        
        Label prompt = new Label("Enter the new Progress Percentage (0 to 100) ");
        prompt.getStyleClass().add("title-label");
        
        TextField newProgressField = new TextField();
        newProgressField.setMaxSize(100.0, 200.0);
        newProgressField.setPromptText("New Progress (0-100)");
        
        Button backButton = new Button("Cancel");
        backButton.setMaxSize(150.0, 100.0);
        backButton.setOnMouseClicked((event) -> {
            System.out.println("Back button clicked");
            dialog.close();
        });
        
        Button updateButton = new Button("Update Progress");
        updateButton.setMaxSize(150.0, 100.0);
        
	        updateButton.setOnAction((e) -> {
	        	
	        	try
	        	{
	        		this.updateTaskProgress(this.id, primaryStage, this.previousScene, newProgressField);
	        		dialog.close();
	        	}
	        	
	        	catch(ProjectRejectedException error)
	        	{
	        		new AlertBox(error.toString(),true);
	        		dialog.close();
	        		new Main().start(primaryStage);
	        	}
	        	
	        	 catch(ViewTaskException error)
	             {
	        		System.out.println("Came here !");
	            	new AlertBox(error.toString(),true);
	            	dialog.close();
	             }
	        	
	        });
        
        VBox vbox = new VBox(10.0, new Node[]{prompt, newProgressField, updateButton, backButton});
        vbox.setAlignment(Pos.CENTER);
        Scene dialogScene = new Scene(vbox, 400.0, 300.0);
        dialogScene.getStylesheets().add(this.getClass().getResource("/resources/styles.css").toExternalForm());
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    // Backend Method for Updating Task Progress
    private void updateTaskProgress(int id, Stage primaryStage, Scene previousScene, TextField newProgressField) throws ViewTaskException,ProjectRejectedException
    {
        String newProgress = newProgressField.getText();
        if (newProgress.isEmpty())
        {
            new AlertBox("Please New Progress !", true);
        } 
        
        else 
        {
            int newProgressValue;
            try 
            {
                newProgressValue = Integer.parseInt(newProgress);
                if (newProgressValue < 0 || newProgressValue > 100) 
                {
                    new AlertBox("Invalid progress value!\nPlease enter a value between 0 and 100 !!", true);
                    return;
                }
            } 
            
            catch (NumberFormatException var19) 
            {
                new AlertBox("Invalid progress value!\nPlease enter a valid number!!", true);
                return;
            }

            try 
            {
                Connection con = ConnectionManager.getConnection();
                Statement st = con.createStatement();
                String query = "SELECT projectId, taskprogress, projectname FROM student WHERE digital_id = " + id;
                ResultSet r = st.executeQuery(query);
                 
                int totp = 0;
                int c = 0;
                if (!r.next()) 
                {
                    new AlertBox("No student found with the given digital ID!", true);
                } 
                
                else 
                {
                	System.out.println("here");
                    int pid = r.getInt("projectId");
                    int currentProgress = r.getInt("taskprogress");
                    String projectName  = r.getString("projectname");
                    String status ;
                   
                    Connection con1 = ConnectionManager.getConnection();
                    Statement st1 = con1.createStatement();
                    String query2 ="SELECT status from project WHERE projectId = " + pid;
                    ResultSet r2  = st1.executeQuery(query2);
                    
                    System.out.println(query2);
                    
                    if (r2.next()) 
                    { 
                        status = r2.getString("status");
                        System.out.println(status);
                        
                        if(status.equals("requested"))
                        {
                        	System.out.println("Your project is in requested status ");
                        	throw new ViewTaskException("Your Project is not Accepted by the Admin !\nYou cannot Update your Progress now !!");
                        	//return ;
                        }
                        
                        else if(status.equals("completed"))
                        { 
                        	System.out.println("Your project is in completed status ");
                        	throw new ViewTaskException("Project Status is Already Completed !\nYou can't Update Task Now !!");
                        }
                        
                        else if (status.equals("rejected")) 
                        {
                            System.out.println("Project status is rejected. Updating database records...");

                            try 
                            {
                                // Updating student records related to the rejected project
                                Connection con3 = ConnectionManager.getConnection();
                                Statement st3 = con3.createStatement();
                                String updateStudentQuery = "UPDATE student SET projectId = NULL, projectname = NULL, taskname = NULL, taskprogress = 0 WHERE projectId = " + pid;
                                st3.executeUpdate(updateStudentQuery);
                                System.out.println("Student records updated for rejected project.");

                                //Deleting faculty records related to the rejected project
                                String deleteFacultyQuery = "DELETE FROM faculty WHERE projectId = " + pid;
                                st3.executeUpdate(deleteFacultyQuery);
                                System.out.println("Faculty records deleted for rejected project.");

                                // Deleting project record from the project table
                                String deleteProjectQuery = "DELETE FROM project WHERE projectId = " + pid;
                                st3.executeUpdate(deleteProjectQuery);
                                System.out.println("Project record deleted for rejected project.");

                                st3.close();
                                con3.close();
                                
                                //new AlertBox("Project with ID " + pid + " has been rejected. Associated records have been updated.", false);
                                throw new ProjectRejectedException("Project with ID " + pid + " has been rejected!\nAssociated records have been updated !\nSubmit new Project !!");
                                
                            } 
                            
                            catch (SQLException e) 
                            {
                                new AlertBox("Error updating database for rejected project: " + e.getMessage(), true);
                                e.printStackTrace();
                            }
                        }

                    }
                    
                    else 
                    {
                        System.out.println("No results found for projectId = " + pid);
                        throw new ViewTaskException("No results found for projectId = \" + pid !");
                    }   
                    
                    System.out.println("1 - Your project is in requested status ");
                    Connection con2 = ConnectionManager.getConnection();
                    Statement st2 = con2.createStatement();
                    
                    query = "UPDATE student SET taskprogress = " + newProgressValue + " WHERE digital_id = " + id;
                    st2.executeUpdate(query);
                    query = "SELECT * FROM student WHERE projectId = " + pid;
                    r = st2.executeQuery(query);
                    
                    if(r.next())
                    {
                    	c    = 0;
                    	totp = 0;

	                    do 
	                    {
	                        totp += r.getInt("taskprogress");
	                        c++ ;
	                    } while(r.next());
	
	                    int avgp = totp/c;
	                    
	                    System.out.println("Completion status : "+avgp);
	                    
	                    if (avgp!=100)
	                    {
	                        query = "UPDATE project SET completionstatus = " + avgp + " WHERE projectId = " + pid;
	                    }
	                    
	                    else
	                    {
	                        query = "UPDATE project SET completionstatus = " + avgp + ", status = 'completed' WHERE projectId = " + pid;
	
	                    }
	                    
	                    System.out.println("Executing : "+query);
	                    st.executeUpdate(query);
	                    new AlertBox("Current Progress for project \"" + projectName + "\": " + currentProgress + "%\nTask progress updated successfully to " + newProgressValue + "%.", false);
	                    primaryStage.setScene(previousScene);
                    }
                    
                    else
                    {
                    	throw new ViewTaskException("Error in Database !\nCan't find Student details !");
                    }
                }

                r.close();
                st.close();
                con.close();
            } 
            
            catch (SQLException var18) 
            {
                Exception e = var18;
                new AlertBox("Error here : " + e.getMessage(), true);
            }

        }
    }
}
/*Class for Task details of all students involved in a particular project associated with the Faculty */

package Faculty;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import UI.AlertBox;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Connection.ConnectionManager;
import Exceptions.ViewTaskException;

class viewTask 
{

	public static ResultSet viewTask(int pid, int facultyId) throws ViewTaskException
	{
		try
		{
			// Establish the connection to the database
			Connection con = ConnectionManager.getConnection();
			Statement st = con.createStatement();

			// Query to check if the project ID is associated with the given faculty ID
			System.out.println(pid + " " + facultyId);
			String query = "SELECT * FROM faculty WHERE projectId = " + pid + " AND facultyid = " + facultyId;
			ResultSet facultyResultSet = st.executeQuery(query);

			if (facultyResultSet.next()) {
				// Project ID belongs to the given faculty ID, proceed to retrieve tasks
		
				String taskQuery = "SELECT * FROM student WHERE projectId = " + pid;
				ResultSet taskResultSet = st.executeQuery(taskQuery);
				ResultSet r = taskResultSet;

				if (taskResultSet.next()) {
				    return r; 	
				}

				else
				{
					throw new ViewTaskException("No Projects associated with this Faculty !");
				}
			}

			else
			{
				throw new ViewTaskException("This Project ID is not associated with this Faculty !!");
			}

		}

		catch (SQLException e)
		{
			new AlertBox("Database error: " + e.getMessage(),true);
			return null;
		}
		
	}
}


public class viewTaskUI extends Application {

	private TableView<Task> taskTable;
	private int projectId;
	private int facultyId;
	private Scene previousScene;

	public static void main(String[] args) {
		launch(args);
	}

	public viewTaskUI(int facultyId, int projectId, Scene passed) {
		this.projectId = projectId;
		this.facultyId = facultyId;
		this.previousScene = passed;
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Task View");
		
		Button backArrow = new Button("\u2190 Back");
        backArrow.setStyle("-fx-font-size: 18px; -fx-text-fill: #0078D7; -fx-cursor: hand;");

        backArrow.setOnAction(event -> {
            primaryStage.setTitle("Faculty Page");
            primaryStage.setScene(previousScene);
        });

		// TableView setup
		taskTable = new TableView<>();
		setupTableColumns();
		taskTable.setMaxWidth(450);

		VBox root = new VBox(50,backArrow, taskTable);
		root.setAlignment(Pos.CENTER);
		Scene scene = new Scene(root, 600, 400);

		try 
		{
			List<Task> tasks = displayTasks(projectId, facultyId);
			if (tasks.isEmpty()) 
			{
				new AlertBox("No tasks to display for this project and faculty ID !", true);
				return ;
			} 
			
			else 
			{
				taskTable.getItems().addAll(tasks);
			}
			
			primaryStage.setScene(scene);
			primaryStage.show();
		} 
		
		catch (Exception e) 
		{
			new AlertBox(e.getMessage(), true);
		}
	}

	private void setupTableColumns() 
	{
		// Column for student name
		TableColumn<Task, String> studentNameCol = new TableColumn<>("Student Name");
		studentNameCol.setCellValueFactory(new PropertyValueFactory<>("studentName"));
		studentNameCol.setPrefWidth(150);

		// Column for task name
		TableColumn<Task, String> taskNameCol = new TableColumn<>("Task Name");
		taskNameCol.setCellValueFactory(new PropertyValueFactory<>("taskName"));
		taskNameCol.setPrefWidth(150);
		
		// Column for task progress
		TableColumn<Task, Integer> taskProgressCol = new TableColumn<>("Task Progress (%)");
		taskProgressCol.setCellValueFactory(new PropertyValueFactory<>("taskProgress"));
		taskProgressCol.setPrefWidth(150);

		// Add columns to the table
		taskTable.getColumns().addAll(studentNameCol, taskNameCol, taskProgressCol);
	}

	private List<Task> displayTasks(int pid, int facultyId) throws NullPointerException
	{
		List<Task> tasks = new ArrayList<>();
		
		try 
		{
			ResultSet taskResultSet = viewTask.viewTask(pid, facultyId);
			
			do{
				String studentName = taskResultSet.getString("name");
				String taskName = taskResultSet.getString("taskname");
				int taskProgress = taskResultSet.getInt("taskprogress");
				System.out.println(1+" "+studentName+" "+taskName+" "+taskProgress);
				tasks.add(new Task(studentName, taskName, taskProgress));
			} while (taskResultSet.next()) ;
		} 
		
		catch(ViewTaskException e)
		{
			throw new NullPointerException(e.toString());
		}
		
		catch (SQLException e) {
			throw new NullPointerException("There is no record relevant to the Faculty with this ProjectID !");
		}
		
		return tasks;
	}

	// Task class to hold data for each task row
	public static class Task 
	{
		private String studentName;
		private String taskName;
		private int taskProgress;

		public Task(String studentName, String taskName, int taskProgress) 
		{
			this.studentName = studentName;
			this.taskName = taskName;
			this.taskProgress = taskProgress;
		}
		
		 public String getStudentName() {
	            return studentName;
	        }

	        public String getTaskName() {
	            return taskName;
	        }

	        public int getTaskProgress() {
	            return taskProgress;
	        }
	}
}

/*UI class for displaying project Submission form for newly registered Students*/

package Student;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;
import java.util.*;
import Connection.ConnectionManager;
import UI.*;

public class projectSubmit extends Application
{

	private static int projId ;
	private int stur = 0;
	private ArrayList<StudentTask> selectedStudents = new ArrayList<>();
	private int facultyid;
	private String facultyname;
	public Scene previousScene;
	private String projectName;
	private String Username;
	private String Password;
	private int id;

	// Initializing projId with the greatest value of projectId from the database + 1
	static
	{

		Connection con;
		try
		{
			con = ConnectionManager.getConnection();

			Statement st = con.createStatement();
			ResultSet r1 = st.executeQuery("SELECT MAX(projectId) AS max_projectId FROM project");
			if (r1.next() && r1.getInt("max_projectId") != 0 )
			{
				projId = r1.getInt("max_projectId") + 1;
			}
			else
			{
				projId= 1;
			}

		}

		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	// No-argument constructor
	public projectSubmit() {}

	// Constructor with previous scene
	public projectSubmit(Scene passed,String Username,String Password,int id) 
	{
		this.previousScene = passed;
		this.Username      = Username;
		this.Password      = Password;
		this.id            = id;
	}

	@Override
	public void start(Stage primaryStage)
	{
		primaryStage.setTitle("Project Proposal Submission");

		Button backArrow = new Button("Back");
        backArrow.setStyle("-fx-font-size: 18px; -fx-cursor: hand;");

        backArrow.setOnAction(event -> {
			primaryStage.setTitle("Student Page");
			primaryStage.setScene(previousScene);
		});

		// Main layout with style class
		VBox mainLayout = new VBox(15);
		mainLayout.setPadding(new Insets(20));
		mainLayout.getStyleClass().add("main-layout");

		Label titleLabel = new Label("Submit Project Proposal");
		titleLabel.getStyleClass().add("title-label");

		TextField projectNameField = new TextField();
		projectNameField.setPromptText("Enter Project Name");
		projectNameField.getStyleClass().add("input-field");

		Button selectStudentsButton = new Button("Select Students");
		selectStudentsButton.getStyleClass().add("action-button");

		Button selectFacultyButton = new Button("Select Faculty");
		selectFacultyButton.getStyleClass().add("action-button");

		Button submitButton = new Button("Submit Project");
		submitButton.getStyleClass().add("submit-button");

		mainLayout.getChildren().addAll(
		    backArrow,
		    titleLabel,
		    new Label("Project Name:"), projectNameField,
		    selectStudentsButton, selectFacultyButton, submitButton
		);

		Scene mainScene = new Scene(mainLayout, 400, 400);
		mainScene.getStylesheets().add(getClass().getResource("/resources/style_proj.css").toExternalForm());
		primaryStage.setScene(mainScene);
		primaryStage.show();

		// Event handlers
		selectStudentsButton.setOnAction(e -> selectStudent());
		submitButton.setOnAction(e -> submitProject(primaryStage, projectNameField.getText()));
		selectFacultyButton.setOnAction(e -> selectFaculty(projectNameField.getText()));
	}

	private void selectStudent()
	{
		if (stur >= 5) {
			new AlertBox( "Selection Limit\nOnly a maximum of 5 members allowed ",false);
			return;
		}

		Dialog<StudentTask> dialog = new Dialog<>();
		dialog.setTitle("Select Student");
		dialog.getDialogPane().getStyleClass().add("dialog-pane");

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField digitalIdField = new TextField();
		digitalIdField.setPromptText("Digital ID");
		digitalIdField.getStyleClass().add("input-field");

		digitalIdField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				digitalIdField.setText(newValue.replaceAll("[^\\d]", ""));
			}
		});

		TextField taskField = new TextField();
		taskField.setPromptText("Task");
		taskField.getStyleClass().add("input-field");

		grid.add(new Label("Digital ID:"), 0, 0);
		grid.add(digitalIdField, 1, 0);
		grid.add(new Label("Task:"), 0, 1);
		grid.add(taskField, 1, 1);

		dialog.getDialogPane().setContent(grid);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == ButtonType.OK)
			{
				try
				{
					int digitalId = Integer.parseInt(digitalIdField.getText());
					String task = taskField.getText();

					if (isStudentAlreadyInvolved(digitalId))
					{
						new AlertBox("Student is already assigned to a project!",true);
						return null;
					}

					return new StudentTask(digitalId, task);
				}

				catch (NumberFormatException e) {
					new AlertBox("Please enter a valid digital ID!",true);
				}
			}
			return null;
		});

		dialog.showAndWait().ifPresent(studentTask -> {
			if (studentTask != null)
			{
				try
				{
					Connection con = ConnectionManager.getConnection();
					Statement st = con.createStatement();
					int di = studentTask.digitalId;
					String q = "select * from student where digital_id = "+di;
					ResultSet r = st.executeQuery(q);
					if (r.next())
					{
						if(CheckStudent(studentTask))
						{
							selectedStudents.add(studentTask);
							stur++;
							new AlertBox("Student added for the project ",false);
						}
						
						else
						{
							new AlertBox("Student Already Selected now !\nTry with other Student !!",true);
							return ;
						}
					}
					
					else
					{
						new AlertBox("Invalid Digital ID !\nStudent for this Digital ID doesn't exist !\nMake sure student is registered!!",true);
						return ;
					}
				}

				catch(SQLException e)
				{
					new AlertBox("Error in Databse !",true);
					return ;
				}


			}

		});
	}

	private boolean isStudentAlreadyInvolved(int digitalId)
	{
		try (Connection con = ConnectionManager.getConnection();
			        Statement st = con.createStatement()) {

			String queryCheck = "SELECT projectname FROM student WHERE digital_id = " + digitalId;
			ResultSet rs = st.executeQuery(queryCheck);
			return rs.next() && rs.getString("projectname") != null;

		}

		catch (SQLException e)
		{
			new AlertBox("Database Error\nError checking student assignment: " + e.getMessage(),true);
			return true;
		}
	}
	
	public boolean CheckStudent(StudentTask studentTask)
	{
		Iterator itr = selectedStudents.iterator();
		
		while(itr.hasNext())
		{
			StudentTask temp = (StudentTask) itr.next();
			
			if(temp.digitalId == studentTask.digitalId)
				return false;
			
		}
		
	    if (selectedStudents.contains(studentTask))
	    {
	        return false;
	    }
	    
	    return true;
	}

	public static boolean CheckProject(String projectName) 
	{
	    String query = "SELECT * FROM project WHERE projectname = ?";
	    
	    try (Connection con = ConnectionManager.getConnection();
	            PreparedStatement pst = con.prepareStatement(query)) {
	        pst.setString(1, projectName);
	        ResultSet r = pst.executeQuery();
	        boolean projectExists = r.next();
	        return !projectExists;
	    } 
	    
	    catch (SQLException e) 
	    {
	        new AlertBox("Error in Database!", true);
	        return true;
	    }
	}
    
    public ResultSet facultyDetails() {
        try {
            Connection con = ConnectionManager.getConnection();
            Statement st = con.createStatement();
            String fetchFacultyDetails = "SELECT * FROM faculty WHERE facultyid = " + facultyid;
            System.out.println("Executing query: " + fetchFacultyDetails); // Debug line

            ResultSet rs = st.executeQuery(fetchFacultyDetails);
            return rs;

        } catch (SQLException e) {
            new AlertBox("Database error", true);
            e.printStackTrace(); // Add for debugging purposes
            return null; // Return null to indicate an error
        }
    }
    
    private void submitProject(Stage primaryStage, String projectName) 
    {
        if (selectedStudents.isEmpty()) {
            new AlertBox("No students selected for the project!", true);
            return;
        }

      try 
      {
        Connection con = ConnectionManager.getConnection();
         Statement st = con.createStatement();

        if (CheckProject(projectName)) {
            ResultSet rs = facultyDetails();

            if (rs.next()) 
            {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");

                // Check if the faculty already has a project assigned
                if (rs.getString("projectId") == null) 
                {
                    // Update existing faculty record
                    String updateFaculty = "UPDATE faculty SET projectId = " + projId + 
                                           " WHERE facultyid = " + facultyid + " AND projectId is NULL";
                    System.out.println("Executing update: " + updateFaculty); // Debug line
                    Connection con1 = ConnectionManager.getConnection();
                    Statement st1 = con1.createStatement();
                    st1.executeUpdate(updateFaculty);
                } 
                
                else 
                {
                    // Insert new faculty record for a new project
                    String insertNewFaculty = "INSERT INTO faculty (name, email, password, facultyid, projectId) " +
                                              "VALUES ('" + name + "', '" + email + "', '" + password + "', " + facultyid + ", " + projId + ")";
                    System.out.println("Executing insert: " + insertNewFaculty); // Debug line
                    Connection con1 = ConnectionManager.getConnection();
                    Statement st1 = con1.createStatement();
                    st1.executeUpdate(insertNewFaculty);
                }

                new AlertBox("Faculty assigned to new project successfully", false);
            } 
            
            else 
            {
                new AlertBox("No faculty found with ID: " + facultyid, true);
                return;
            }

            // Insert new project details
            String insertProject = "INSERT INTO project (projectId, projectname, status, completionstatus, facultyName, facultyId) " +
                                   "VALUES (" + projId + ", '" + projectName + "', 'requested', 0, '" + facultyname + "', " + facultyid + ")";
            System.out.println("Executing project insert: " + insertProject); // Debug line
            Connection con2 = ConnectionManager.getConnection();
            Statement st2 = con2.createStatement();
            st2.executeUpdate(insertProject);

            // Update student details
            for (StudentTask studentTask : selectedStudents) 
            {
                String updateStudent = "UPDATE student SET projectId = " + projId +
                                       ", projectname = '" + projectName +
                                       "', taskname = '" + studentTask.task +
                                       "', taskprogress = 0 WHERE digital_id = " + studentTask.digitalId;
                System.out.println("Updating student: " + updateStudent); // Debug line
                Connection con3 = ConnectionManager.getConnection();
                Statement st3 = con3.createStatement();
                st3.executeUpdate(updateStudent);
            }

            projId++;
            stur = 0;
            selectedStudents.clear();
            new AlertBox("Project and tasks submitted successfully!", false);
            Student temp = new Student(Username,Password,id);
			temp.start(primaryStage);
        } 
        
        else 
        {
            new AlertBox("Project Name " + projectName + " already selected", true);
        }

    } 
      
    catch (SQLException e) 
      {
        new AlertBox("Database Error!\nAn error occurred sub: "  , true);
    }
  }

	private void selectFaculty(String projectName)
	{
		try {
			
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("Select Faculty");
			dialog.setHeaderText("Enter Faculty ID:");

			dialog.showAndWait().ifPresent(facultyIdStr -> {
				int facultyId = Integer.parseInt(facultyIdStr);
				facultyid = facultyId;
				facultyname = getFacultyNameById(facultyid);

				System.out.println("Retrieved Faculty Details: " + facultyid + " " + facultyname);
			});
			// Establish the database connection
			Connection connection = ConnectionManager.getConnection();

			// Check if the faculty ID exists in the facultydisp table
			String checkFacultySql = "SELECT * FROM facultydisp WHERE facultyid = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(checkFacultySql);
			preparedStatement.setInt(1, facultyid);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (!resultSet.next()) {
				new AlertBox("Faculty ID " + facultyid + " does not exist .",true);
			}

		}

		catch (SQLException e)
		{
			new AlertBox("Database error .",true);
		}
	}

	public String getFacultyNameById(int facultyId)
	{
		String facultyName = null;
		String query = "SELECT name FROM faculty WHERE facultyid = ?";

		Connection connection = null;
		try (PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(query)) {
			preparedStatement.setInt(1, facultyId); // Setting the facultyid parameter
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				facultyName = resultSet.getString("name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return facultyName;
	}

	public static void main(String[] args) 
	{
		launch(args);
	}

	static class StudentTask {
		int digitalId;
		String task;

		StudentTask(int digitalId, String task) {
			this.digitalId = digitalId;
			this.task = task;
		}
	}
}
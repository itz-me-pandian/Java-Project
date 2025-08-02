/*UI class for displaying Alertbox of both Information and Error */

package UI;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox 
{
	private Alert alert;

	public AlertBox(String message, boolean isError) 
	{
        
        if (isError) 
        {
            alert = new Alert(AlertType.ERROR); 
            alert.setTitle("Error");
        } 
        
        else 
        {
            alert = new Alert(AlertType.INFORMATION); 
            alert.setTitle("Information");
        }
        
        alert.setHeaderText(null); 
        alert.setContentText(message); 
        
        // Show the alert and wait for user response
        alert.showAndWait();
    }
}
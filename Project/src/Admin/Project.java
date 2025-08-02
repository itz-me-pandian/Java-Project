/*Wrapper class */

package Admin;

import javafx.scene.control.Button;

public class Project 
{
    private int projectId;
    private String projectName;
    private String status;
    private Button acceptButton;
    private Button rejectButton;

    public Project(int projectId, String projectName, String status, Button acceptButton, Button rejectButton) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.status = status;
        this.acceptButton = acceptButton;
        this.rejectButton = rejectButton;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getStatus() {
        return status;
    }

    public Button getAcceptButton() {
        return acceptButton;
    }

    public Button getRejectButton() {
        return rejectButton;
    }
}


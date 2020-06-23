package hk.org.ha.eclaim.model.project;

import java.util.List;

public class ProjectBaseVo {

	private String projectId;
	private String projectVerId;
	private String projectStep;
	private String projectStatus;
	private String projectType;
	private String recType;
	
	private String updateSuccess;
	private String displayMessage;
	private List<String> displayMessages;
	
	
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProjectVerId() {
		return projectVerId;
	}
	public void setProjectVerId(String projectVerId) {
		this.projectVerId = projectVerId;
	}
	public String getProjectStep() {
		return projectStep;
	}
	public void setProjectStep(String projectStep) {
		this.projectStep = projectStep;
	}
	public String getProjectStatus() {
		return projectStatus;
	}
	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	public String getRecType() {
		return recType;
	}
	public void setRecType(String recType) {
		this.recType = recType;
	}
	public String getUpdateSuccess() {
		return updateSuccess;
	}
	public void setUpdateSuccess(String updateSuccess) {
		this.updateSuccess = updateSuccess;
	}
	public String getDisplayMessage() {
		return displayMessage;
	}
	public void setDisplayMessage(String displayMessage) {
		this.displayMessage = displayMessage;
	}
	public List<String> getDisplayMessages() {
		return displayMessages;
	}
	public void setDisplayMessages(List<String> displayMessages) {
		this.displayMessages = displayMessages;
	}
	
	
}

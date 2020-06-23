package hk.org.ha.eclaim.model.project;

import java.util.List;

public class ProjectQuantifiableVo {
	
	private String projectId;
	private String projectVerId;
	private String projectStep;
	private List<ProjectQuantifiableDetailVo> detailsList;
	private String qDeliver;
	private String updateSuccess;
	private String displayMessage;
	
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
	public List<ProjectQuantifiableDetailVo> getDetailsList() {
		return detailsList;
	}
	public void setDetailsList(List<ProjectQuantifiableDetailVo> detailsList) {
		this.detailsList = detailsList;
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
	public String getqDeliver() {
		return qDeliver;
	}
	public void setqDeliver(String qDeliver) {
		this.qDeliver = qDeliver;
	}
	
	
}

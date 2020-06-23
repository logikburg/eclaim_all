package hk.org.ha.eclaim.model.project;

import java.util.ArrayList;
import java.util.List;

public class ProjectInvitationWebVo {
	private int projectId;
	private String projectName;

	private String publishDate;
	private String startDate;
	private String endDate;
	private String othInfo;
	private String targetApp;
	
	private List<ProjectIncludedInvitationVo> includedInvitations = new ArrayList<ProjectIncludedInvitationVo>();

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getOthInfo() {
		return othInfo;
	}

	public void setOthInfo(String othInfo) {
		this.othInfo = othInfo;
	}

	public String getTargetApp() {
		return targetApp;
	}

	public void setTargetApp(String targetApp) {
		this.targetApp = targetApp;
	}

	public List<ProjectIncludedInvitationVo> getIncludedInvitations() {
		return includedInvitations;
	}

	public void setIncludedInvitations(List<ProjectIncludedInvitationVo> includedInvitations) {
		this.includedInvitations = includedInvitations;
	}
}

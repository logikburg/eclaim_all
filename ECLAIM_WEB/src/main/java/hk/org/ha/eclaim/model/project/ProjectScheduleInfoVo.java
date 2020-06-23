package hk.org.ha.eclaim.model.project;

import java.util.Date;
import java.util.List;

public class ProjectScheduleInfoVo {
	
	private String projectId;
	private String projectVerId;
	private String projectStep;
	private List<ProjectScheduleVo> scheduleList;
	private Date startDate;
	private Date endDate;
	private Boolean is7x24;
	private String attendance;
	private String other;
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
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Boolean getIs7x24() {
		return is7x24;
	}
	public void setIs7x24(Boolean is7x24) {
		this.is7x24 = is7x24;
	}
	public String getAttendance() {
		return attendance;
	}
	public void setAttendance(String attendance) {
		this.attendance = attendance;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public String getProjectStep() {
		return projectStep;
	}
	public void setProjectStep(String projectStep) {
		this.projectStep = projectStep;
	}
	public List<ProjectScheduleVo> getScheduleList() {
		return scheduleList;
	}
	public void setScheduleList(List<ProjectScheduleVo> scheduleList) {
		this.scheduleList = scheduleList;
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
	
	
}

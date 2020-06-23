package hk.org.ha.eclaim.model.project;

import java.util.Date;

public class ProjectScheduleVo {
	
	private String projectId;
	private String projectVerId;
	private String scheduleId;
	private String[] patternCode;
	private String patternCodes; // for Proeject Review
	private String scheduleDate;
	private String patternDesc;
	private String startTime;
	private String endTime;
	private String status;
	
	
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
	public String getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}
	public String[] getPatternCode() {
		return patternCode;
	}
	public void setPatternCode(String[] patternCode) {
		this.patternCode = patternCode;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPatternDesc() {
		return patternDesc;
	}
	public void setPatternDesc(String patternDesc) {
		this.patternDesc = patternDesc;
	}
	public String getPatternCodes() {
		return patternCodes;
	}
	public void setPatternCodes(String patternCodes) {
		this.patternCodes = patternCodes;
	}
	public String getScheduleDate() {
		return scheduleDate;
	}
	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}
	
	
}

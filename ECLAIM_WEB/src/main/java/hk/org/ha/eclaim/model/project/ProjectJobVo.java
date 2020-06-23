package hk.org.ha.eclaim.model.project;

import java.util.Map;

public class ProjectJobVo {

	private String projectId;
	
	private String projectVerId;
	
	private String projectStatus;

	private String jobGroupId;
	
	private String staffGrp;
	
	private Map<String, String> jobRankList; 
	
	private String[] jobRank;
	
	private String jobRanks; // for Project Review
	
	private String coOrdinatorId;
	
	private String coOrdinator;
	
	private String description;
	
	private String quota;
	
	private String duration;
	
	private String sessionDay;
	
	private String sessionHour;
	
	private String totalHour;
	
	private String otherInfo;
	
	private String targerApp;
	
	private String maxGradeVal;
	
	private String estImpact; // for Project Review
	
	private String status;

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getJobGroupId() {
		return jobGroupId;
	}

	public void setJobGroupId(String jobGroupId) {
		this.jobGroupId = jobGroupId;
	}

	public String getProjectVerId() {
		return projectVerId;
	}

	public void setProjectVerId(String projectVerId) {
		this.projectVerId = projectVerId;
	}

	public String[] getJobRank() {
		return jobRank;
	}

	public void setJobRank(String[] jobRank) {
		this.jobRank = jobRank;
	}

	public String getCoOrdinator() {
		return coOrdinator;
	}

	public void setCoOrdinator(String coOrdinator) {
		this.coOrdinator = coOrdinator;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getQuota() {
		return quota;
	}

	public void setQuota(String quota) {
		this.quota = quota;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getSessionDay() {
		return sessionDay;
	}

	public void setSessionDay(String sessionDay) {
		this.sessionDay = sessionDay;
	}

	public String getSessionHour() {
		return sessionHour;
	}

	public void setSessionHour(String sessionHour) {
		this.sessionHour = sessionHour;
	}

	public String getTotalHour() {
		return totalHour;
	}

	public void setTotalHour(String totalHour) {
		this.totalHour = totalHour;
	}
	
	public String getOtherInfo() {
		return otherInfo;
	}

	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
	}

	public String getTargerApp() {
		return targerApp;
	}

	public void setTargerApp(String targerApp) {
		this.targerApp = targerApp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStaffGrp() {
		return staffGrp;
	}

	public void setStaffGrp(String staffGrp) {
		this.staffGrp = staffGrp;
	}

	public Map<String, String> getJobRankList() {
		return jobRankList;
	}

	public void setJobRankList(Map<String, String> jobRankList) {
		this.jobRankList = jobRankList;
	}

	public String getMaxGradeVal() {
		return maxGradeVal;
	}

	public void setMaxGradeVal(String maxGradeVal) {
		this.maxGradeVal = maxGradeVal;
	}

	public String getJobRanks() {
		return jobRanks;
	}

	public void setJobRanks(String jobRanks) {
		this.jobRanks = jobRanks;
	}

	public String getEstImpact() {
		return estImpact;
	}

	public void setEstImpact(String estImpact) {
		this.estImpact = estImpact;
	}

	public String getCoOrdinatorId() {
		return coOrdinatorId;
	}

	public void setCoOrdinatorId(String coOrdinatorId) {
		this.coOrdinatorId = coOrdinatorId;
	}

	public String getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}
	
	
}

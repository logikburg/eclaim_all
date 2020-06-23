package hk.org.ha.eclaim.model.project;

public class ProjectIncludedInvitationVo {

	// Multiple
	private String included;
	private String jobs;
	private Integer jobGroupId;
	private String appDeadline;
	private String hospitalName;
	private String hospitalCode;
	private String departmentName;
	private String departmentId;
	private String workingSchedule;
	private String jobDesc;

	public String getIncluded() {
		return included;
	}

	public void setIncluded(String included) {
		this.included = included;
	}

	public String getJobs() {
		return jobs;
	}

	public void setJobs(String jobs) {
		this.jobs = jobs;
	}

	public String getAppDeadline() {
		return appDeadline;
	}

	public void setAppDeadline(String appDeadline) {
		this.appDeadline = appDeadline;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getWorkingSchedule() {
		return workingSchedule;
	}

	public void setWorkingSchedule(String workingSchedule) {
		this.workingSchedule = workingSchedule;
	}

	public String getJobDesc() {
		return jobDesc;
	}

	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public Integer getJobGroupId() {
		return jobGroupId;
	}

	public void setJobGroupId(Integer jobGroupId) {
		this.jobGroupId = jobGroupId;
	}

}

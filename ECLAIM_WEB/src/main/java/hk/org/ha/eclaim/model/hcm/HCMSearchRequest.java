package hk.org.ha.eclaim.model.hcm;

public class HCMSearchRequest {
	private String hcmJob;
	private String hcmPostTitle;
	private String hcmOrganization;
	private String hcmPostOrganization;
	private String hcmUnitTeam;
	private String hcmEffectiveDate;
	private String postId;
	private String staffGroup;
	
	// Added for UT29984
	private String hcmPositionName;
	
	//HCM Position Information
	private String hcmCluster;
	private String hcmInst;
	private String hcmDept;
	private String hcmRank;
	private String hcmstaffGroup;
	
	private String programType;
	private String duration;
	
	private String allowSingleIncumbent;
	
	// Added for UT30034 
	private String mprsPostId;
	
	// Added for ST08732
	private String requestType;
	
	public String getHcmJob() {
		return hcmJob;
	}

	public void setHcmJob(String hcmJob) {
		this.hcmJob = hcmJob;
	}
	
	public String getHcmPostTitle() {
		return hcmPostTitle;
	}
	
	public void setHcmPostTitle(String hcmPostTitle) {
		this.hcmPostTitle = hcmPostTitle;
	}
	
	public String getHcmOrganization() {
		return hcmOrganization;
	}
	
	public void setHcmOrganization(String hcmOrganization) {
		this.hcmOrganization = hcmOrganization;
	}
	
	public String getHcmPostOrganization() {
		return hcmPostOrganization;
	}
	
	public void setHcmPostOrganization(String hcmPostOrganization) {
		this.hcmPostOrganization = hcmPostOrganization;
	}
	
	public String getHcmUnitTeam() {
		return hcmUnitTeam;
	}
	
	public void setHcmUnitTeam(String hcmUnitTeam) {
		this.hcmUnitTeam = hcmUnitTeam;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getHcmRank() {
		return hcmRank;
	}

	public void setHcmRank(String hcmRank) {
		this.hcmRank = hcmRank;
	}

	public String getHcmDept() {
		return hcmDept;
	}

	public void setHcmDept(String hcmDept) {
		this.hcmDept = hcmDept;
	}

	public String getHcmInst() {
		return hcmInst;
	}

	public void setHcmInst(String hcmInst) {
		this.hcmInst = hcmInst;
	}

	public String getHcmCluster() {
		return hcmCluster;
	}

	public void setHcmCluster(String hcmCluster) {
		this.hcmCluster = hcmCluster;
	}

	public String getHcmstaffGroup() {
		return hcmstaffGroup;
	}

	public void setHcmstaffGroup(String hcmstaffGroup) {
		this.hcmstaffGroup = hcmstaffGroup;
	}

	public String getProgramType() {
		return programType;
	}

	public void setProgramType(String programType) {
		this.programType = programType;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getHcmEffectiveDate() {
		return hcmEffectiveDate;
	}

	public void setHcmEffectiveDate(String hcmEffectiveDate) {
		this.hcmEffectiveDate = hcmEffectiveDate;
	}

	public String getAllowSingleIncumbent() {
		return allowSingleIncumbent;
	}

	public void setAllowSingleIncumbent(String allowSingleIncumbent) {
		this.allowSingleIncumbent = allowSingleIncumbent;
	}

	public String getMprsPostId() {
		return mprsPostId;
	}

	public void setMprsPostId(String mprsPostId) {
		this.mprsPostId = mprsPostId;
	}

	public String getHcmPositionName() {
		return hcmPositionName;
	}

	public void setHcmPositionName(String hcmPositionName) {
		this.hcmPositionName = hcmPositionName;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getStaffGroup() {
		return staffGroup;
	}

	public void setStaffGroup(String staffGroup) {
		this.staffGroup = staffGroup;
	}
}

package hk.org.ha.eclaim.model.request;

public class PostResponse {

	private int postUid;

	private String postId;

	private String clusterCode;

	private String instCode;

	private String deptCode;

	private String staffGroupCode;

	private String rankCode;
	
	private String rankDesc;

	private String postTitle;

	private String postDuration;

	private String postDurationDesc;

	private Double postFte;
	
	private String postStartDateStr;
	
	private String postEndDateStr;
	
	private String limitDurationEndDateStr;

	// Fields for table POST_FUNDING
	private String annualPlan;

	private String annualPlanDesc;

	private String programYear;

	private String programCode;

	private String programName;

	//private String programRemark;

	private String fundSrc1st;

	private String fundSrc1stStartDateStr;

	private String fundSrc1stEndDateStr;

	//private String fundSrc2nd;

	//private String fundSrc2ndStartDateStr;

	//private String fundSrc2ndEndDateStr;

	//private String fundSrc3rd;

	//private String fundSrc3rdStartDateStr;

	//private String fundSrc3rdEndDateStr;

	//private String costCode;

	private String programTypeCode;

	private String fundSrc1stFteStr;

	//private String fundSrc2ndFteStr;

	//private String fundSrc3rdFteStr;

	private String fundSrc1stRemark;

	//private String fundSrc2ndRemark;

	//private String fundSrc3rdRemark;
	
	private String hcmPositionId;
	
	private String hcmPositionName;

	public int getPostUid() {
		return postUid;
	}

	public void setPostUid(int postUid) {
		this.postUid = postUid;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getClusterCode() {
		return clusterCode;
	}

	public void setClusterCode(String clusterCode) {
		this.clusterCode = clusterCode;
	}

	public String getInstCode() {
		return instCode;
	}

	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getStaffGroupCode() {
		return staffGroupCode;
	}

	public void setStaffGroupCode(String staffGroupCode) {
		this.staffGroupCode = staffGroupCode;
	}

	public String getRankCode() {
		return rankCode;
	}

	public void setRankCode(String rankCode) {
		this.rankCode = rankCode;
	}

	public String getRankDesc() {
		return rankDesc;
	}

	public void setRankDesc(String rankDesc) {
		this.rankDesc = rankDesc;
	}

	public String getPostTitle() {
		return postTitle;
	}

	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}

	public String getPostDuration() {
		return postDuration;
	}

	public void setPostDuration(String postDuration) {
		this.postDuration = postDuration;
	}

	public String getPostDurationDesc() {
		return postDurationDesc;
	}

	public void setPostDurationDesc(String postDurationDesc) {
		this.postDurationDesc = postDurationDesc;
	}

	public Double getPostFte() {
		return postFte;
	}

	public void setPostFte(Double postFte) {
		this.postFte = postFte;
	}

	public String getPostStartDateStr() {
		return postStartDateStr;
	}

	public void setPostStartDateStr(String postStartDateStr) {
		this.postStartDateStr = postStartDateStr;
	}

	public String getPostEndDateStr() {
		return postEndDateStr;
	}

	public void setPostEndDateStr(String postEndDateStr) {
		this.postEndDateStr = postEndDateStr;
	}

	public String getLimitDurationEndDateStr() {
		return limitDurationEndDateStr;
	}

	public void setLimitDurationEndDateStr(String limitDurationEndDateStr) {
		this.limitDurationEndDateStr = limitDurationEndDateStr;
	}

	public String getAnnualPlan() {
		return annualPlan;
	}

	public void setAnnualPlan(String annualPlan) {
		this.annualPlan = annualPlan;
	}

	public String getAnnualPlanDesc() {
		return annualPlanDesc;
	}

	public void setAnnualPlanDesc(String annualPlanDesc) {
		this.annualPlanDesc = annualPlanDesc;
	}

	public String getProgramYear() {
		return programYear;
	}

	public void setProgramYear(String programYear) {
		this.programYear = programYear;
	}

	public String getProgramCode() {
		return programCode;
	}

	public void setProgramCode(String programCode) {
		this.programCode = programCode;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	/*public String getProgramRemark() {
		return programRemark;
	}

	public void setProgramRemark(String programRemark) {
		this.programRemark = programRemark;
	}*/

	public String getFundSrc1st() {
		return fundSrc1st;
	}

	public void setFundSrc1st(String fundSrc1st) {
		this.fundSrc1st = fundSrc1st;
	}

	public String getFundSrc1stStartDateStr() {
		return fundSrc1stStartDateStr;
	}

	public void setFundSrc1stStartDateStr(String fundSrc1stStartDateStr) {
		this.fundSrc1stStartDateStr = fundSrc1stStartDateStr;
	}

	public String getFundSrc1stEndDateStr() {
		return fundSrc1stEndDateStr;
	}

	public void setFundSrc1stEndDateStr(String fundSrc1stEndDateStr) {
		this.fundSrc1stEndDateStr = fundSrc1stEndDateStr;
	}

	/*public String getFundSrc2nd() {
		return fundSrc2nd;
	}

	public void setFundSrc2nd(String fundSrc2nd) {
		this.fundSrc2nd = fundSrc2nd;
	}

	public String getFundSrc2ndStartDateStr() {
		return fundSrc2ndStartDateStr;
	}

	public void setFundSrc2ndStartDateStr(String fundSrc2ndStartDateStr) {
		this.fundSrc2ndStartDateStr = fundSrc2ndStartDateStr;
	}

	public String getFundSrc2ndEndDateStr() {
		return fundSrc2ndEndDateStr;
	}

	public void setFundSrc2ndEndDateStr(String fundSrc2ndEndDateStr) {
		this.fundSrc2ndEndDateStr = fundSrc2ndEndDateStr;
	}

	public String getFundSrc3rd() {
		return fundSrc3rd;
	}

	public void setFundSrc3rd(String fundSrc3rd) {
		this.fundSrc3rd = fundSrc3rd;
	}

	public String getFundSrc3rdStartDateStr() {
		return fundSrc3rdStartDateStr;
	}

	public void setFundSrc3rdStartDateStr(String fundSrc3rdStartDateStr) {
		this.fundSrc3rdStartDateStr = fundSrc3rdStartDateStr;
	}

	public String getFundSrc3rdEndDateStr() {
		return fundSrc3rdEndDateStr;
	}

	public void setFundSrc3rdEndDateStr(String fundSrc3rdEndDateStr) {
		this.fundSrc3rdEndDateStr = fundSrc3rdEndDateStr;
	}

	public String getCostCode() {
		return costCode;
	}

	public void setCostCode(String costCode) {
		this.costCode = costCode;
	}*/

	public String getProgramTypeCode() {
		return programTypeCode;
	}

	public void setProgramTypeCode(String programTypeCode) {
		this.programTypeCode = programTypeCode;
	}

	public String getFundSrc1stFteStr() {
		return fundSrc1stFteStr;
	}

	public void setFundSrc1stFteStr(String fundSrc1stFteStr) {
		this.fundSrc1stFteStr = fundSrc1stFteStr;
	}
	
	public String getFundSrc1stRemark() {
		return fundSrc1stRemark;
	}

	public void setFundSrc1stRemark(String fundSrc1stRemark) {
		this.fundSrc1stRemark = fundSrc1stRemark;
	}
	
	/*
	public String getFundSrc2ndFteStr() {
		return fundSrc2ndFteStr;
	}

	public void setFundSrc2ndFteStr(String fundSrc2ndFteStr) {
		this.fundSrc2ndFteStr = fundSrc2ndFteStr;
	}

	public String getFundSrc3rdFteStr() {
		return fundSrc3rdFteStr;
	}

	public void setFundSrc3rdFteStr(String fundSrc3rdFteStr) {
		this.fundSrc3rdFteStr = fundSrc3rdFteStr;
	}

	public String getFundSrc2ndRemark() {
		return fundSrc2ndRemark;
	}

	public void setFundSrc2ndRemark(String fundSrc2ndRemark) {
		this.fundSrc2ndRemark = fundSrc2ndRemark;
	}

	public String getFundSrc3rdRemark() {
		return fundSrc3rdRemark;
	}

	public void setFundSrc3rdRemark(String fundSrc3rdRemark) {
		this.fundSrc3rdRemark = fundSrc3rdRemark;
	}*/

	public String getHcmPositionId() {
		return hcmPositionId;
	}

	public void setHcmPositionId(String hcmPositionId) {
		this.hcmPositionId = hcmPositionId;
	}

	public String getHcmPositionName() {
		return hcmPositionName;
	}

	public void setHcmPositionName(String hcmPositionName) {
		this.hcmPositionName = hcmPositionName;
	}
	
}

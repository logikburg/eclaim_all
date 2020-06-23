package hk.org.ha.eclaim.model.report;

public class ReportingWebVo {
	private String asAtDate;
	private String clusterCode;
	private String instCode;
	private String department;
	private String rankCode;
	private String staffGroupCode;
	private String userName;
	private String password;
	private String downloadToken;
	private String remark;
	private String outputFormat = "Excel";
	private String postId;
	private String groupBy = "I";
	
	public String getAsAtDate() {
		return asAtDate;
	}
	
	public void setAsAtDate(String asAtDate) {
		this.asAtDate = asAtDate;
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

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getRankCode() {
		return rankCode;
	}

	public void setRankCode(String rankCode) {
		this.rankCode = rankCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getStaffGroupCode() {
		return staffGroupCode;
	}

	public void setStaffGroupCode(String staffGroupCode) {
		this.staffGroupCode = staffGroupCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDownloadToken() {
		return downloadToken;
	}

	public void setDownloadToken(String downloadToken) {
		this.downloadToken = downloadToken;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOutputFormat() {
		return outputFormat;
	}

	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}
}

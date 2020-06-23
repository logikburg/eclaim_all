package hk.org.ha.eclaim.bs.request.po;

import java.util.List;

import hk.org.ha.eclaim.bs.request.po.PostMasterPo;

public class EnquiryWebPo {
	private String userName;
	
	private String clusterCode;
	private String instCode;
	private String deptCode;
	private String staffGroupCode;
	private String rankCode;
	private String postId;
	private String effectiveDate;
	private String employeeId;
	private String approvalRef;		// Added for UT30064
	
	private String haveResult;
	private List<PostMasterPo> searchResultList;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public List<PostMasterPo> getSearchResultList() {
		return searchResultList;
	}

	public void setSearchResultList(List<PostMasterPo> searchResultList) {
		this.searchResultList = searchResultList;
	}

	public String getHaveResult() {
		return haveResult;
	}

	public void setHaveResult(String haveResult) {
		this.haveResult = haveResult;
	}

	public String getApprovalRef() {
		return approvalRef;
	}

	public void setApprovalRef(String approvalRef) {
		this.approvalRef = approvalRef;
	}
}

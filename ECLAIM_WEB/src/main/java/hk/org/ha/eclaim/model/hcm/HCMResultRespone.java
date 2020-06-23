package hk.org.ha.eclaim.model.hcm;

import java.util.List;
import java.util.Map;

import hk.org.ha.eclaim.bs.hcm.po.HCMRecordGradePo;
import hk.org.ha.eclaim.bs.hcm.po.HCMRecordPo;

public class HCMResultRespone {
	private Map<String, String> errorMessage;
	private String searchResult;
	private String postId;
	private HCMRecordPo hcmRecord;
	private List<HCMRecordGradePo> hcmGradeList;
	
	private String deptCode;
	private String staffGroupCode;
	private String rankCode;
	private String lastRecord;
	
	private String employeeId;		// Added for UT30034 
	private String employeeName;	// Added for UT30034
	
	private String mprsPostCount;

	public Map<String, String> getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(Map<String, String> errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getSearchResult() {
		return searchResult;
	}

	public void setSearchResult(String searchResult) {
		this.searchResult = searchResult;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public HCMRecordPo getHcmRecord() {
		return hcmRecord;
	}

	public void setHcmRecord(HCMRecordPo hcmRecord) {
		this.hcmRecord = hcmRecord;
	}

	public List<HCMRecordGradePo> getHcmGradeList() {
		return hcmGradeList;
	}

	public void setHcmGradeList(List<HCMRecordGradePo> hcmGradeList) {
		this.hcmGradeList = hcmGradeList;
	}
	
	public int getHcmGradeListSize() {
		if (hcmGradeList == null)
			return 0;
		
		return hcmGradeList.size();
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

	public String getLastRecord() {
		return lastRecord;
	}

	public void setLastRecord(String lastRecord) {
		this.lastRecord = lastRecord;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getMprsPostCount() {
		return mprsPostCount;
	}

	public void setMprsPostCount(String mprsPostCount) {
		this.mprsPostCount = mprsPostCount;
	}
}

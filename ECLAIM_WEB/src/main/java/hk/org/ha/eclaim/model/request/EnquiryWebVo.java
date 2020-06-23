package hk.org.ha.eclaim.model.request;

import java.util.List;

import hk.org.ha.eclaim.bs.request.po.PostMasterPo;

public class EnquiryWebVo {
	private String userName;
	private String clusterCode;
	private String instCode;
	private String deptCode;
	private String staffGroupCode;
	private String rankCode;
	private String postId;
	private String effectiveDate;
	private String employeeId;
	
	//Position
	private String unit;
	private String postTitle;	
	private String postStartDate;		
	private String postActualStartDate;	
	private String postDuration; 		
	private String limitDurationNo;	
	private String limitDurationUnit;
	private String limitDurationType;		
	private String limitDurationEndDate;
	private String postRemark;			
	private String postFTE;				
	private String postFTEValue;		
	private String positionStatus;		
	private String positionStartDate;
	private String positionEndDate;		
	private String clusterRefNo;		
	private String clusterRemark;	
	
	//Funding
	private String viewAnnualPlanInd;
	private String viewProgramYear;
	private String viewProgramTypeCode;
	private String viewProgramName;
	private String viewFundSrcId;
	private String viewFundSrcSubCatId;
	private String viewFundSrcStartDate;
	private String viewFundSrcEndDate;
	private String viewFundSrcFte;
	private String viewFundSrcRemark;
	private String viewInst;
	private String viewSection;
	private String viewAnalytical;
	private String viewProgramCode;
	
	private String subSpecialty;
	
	//Resource
	private String res_sup_fr_ext;
	private String res_sup_remark;
		
	private String haveResult;
	private List<PostMasterPo> searchResultList;
	
	private String totalRecordCount;
	private String showRecordTrimmedMsg;
	
	private String approvalReference;	// Added for UT30064
	
	private String clusterReferenceNo;  // Added for CC177608
	private String additionRemark;      // Added for CC177608
	
	private String requestApprovalReference; // Added for UT30064
	private String requestApprovalDate; // Added for UT30064
	private String requestApprovalRemark; // Added for UT30064
	
	private String isHR;				// Added for CC177608
	
	private String hoBuyServiceInd;
	
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getRes_sup_fr_ext() {
		return res_sup_fr_ext;
	}

	public void setRes_sup_fr_ext(String res_sup_fr_ext) {
		this.res_sup_fr_ext = res_sup_fr_ext;
	}

	public String getRes_sup_remark() {
		return res_sup_remark;
	}

	public void setRes_sup_remark(String res_sup_remark) {
		this.res_sup_remark = res_sup_remark;
	}

	public String getPostTitle() {
		return postTitle;
	}

	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}

	public String getPostStartDate() {
		return postStartDate;
	}

	public void setPostStartDate(String postStartDate) {
		this.postStartDate = postStartDate;
	}

	public String getPostActualStartDate() {
		return postActualStartDate;
	}

	public void setPostActualStartDate(String postActualStartDate) {
		this.postActualStartDate = postActualStartDate;
	}

	public String getPostDuration() {
		return postDuration;
	}

	public void setPostDuration(String postDuration) {
		this.postDuration = postDuration;
	}

	public String getLimitDurationNo() {
		return limitDurationNo;
	}

	public void setLimitDurationNo(String limitDurationNo) {
		this.limitDurationNo = limitDurationNo;
	}

	public String getLimitDurationUnit() {
		return limitDurationUnit;
	}

	public void setLimitDurationUnit(String limitDurationUnit) {
		this.limitDurationUnit = limitDurationUnit;
	}

	public String getLimitDurationType() {
		return limitDurationType;
	}

	public void setLimitDurationType(String limitDurationType) {
		this.limitDurationType = limitDurationType;
	}

	public String getLimitDurationEndDate() {
		return limitDurationEndDate;
	}

	public void setLimitDurationEndDate(String limitDurationEndDate) {
		this.limitDurationEndDate = limitDurationEndDate;
	}

	public String getPostRemark() {
		return postRemark;
	}

	public void setPostRemark(String postRemark) {
		this.postRemark = postRemark;
	}

	public String getPostFTE() {
		return postFTE;
	}

	public void setPostFTE(String postFTE) {
		this.postFTE = postFTE;
	}

	public String getPostFTEValue() {
		return postFTEValue;
	}

	public void setPostFTEValue(String postFTEValue) {
		this.postFTEValue = postFTEValue;
	}

	public String getPositionStatus() {
		return positionStatus;
	}

	public void setPositionStatus(String positionStatus) {
		this.positionStatus = positionStatus;
	}

	public String getPositionStartDate() {
		return positionStartDate;
	}

	public void setPositionStartDate(String positionStartDate) {
		this.positionStartDate = positionStartDate;
	}

	public String getPositionEndDate() {
		return positionEndDate;
	}

	public void setPositionEndDate(String positionEndDate) {
		this.positionEndDate = positionEndDate;
	}

	public String getClusterRefNo() {
		return clusterRefNo;
	}

	public void setClusterRefNo(String clusterRefNo) {
		this.clusterRefNo = clusterRefNo;
	}

	public String getClusterRemark() {
		return clusterRemark;
	}

	public void setClusterRemark(String clusterRemark) {
		this.clusterRemark = clusterRemark;
	}

	public String getSubSpecialty() {
		return subSpecialty;
	}

	public void setSubSpecialty(String subSpecialty) {
		this.subSpecialty = subSpecialty;
	}

	public String getTotalRecordCount() {
		return totalRecordCount;
	}

	public void setTotalRecordCount(String totalRecordCount) {
		this.totalRecordCount = totalRecordCount;
	}

	public String getShowRecordTrimmedMsg() {
		return showRecordTrimmedMsg;
	}

	public void setShowRecordTrimmedMsg(String showRecordTrimmedMsg) {
		this.showRecordTrimmedMsg = showRecordTrimmedMsg;
	}

	public String getApprovalReference() {
		return approvalReference;
	}

	public void setApprovalReference(String approvalReference) {
		this.approvalReference = approvalReference;
	}

	public String getClusterReferenceNo() {
		return clusterReferenceNo;
	}

	public void setClusterReferenceNo(String clusterReferenceNo) {
		this.clusterReferenceNo = clusterReferenceNo;
	}

	public String getAdditionRemark() {
		return additionRemark;
	}

	public void setAdditionRemark(String additionRemark) {
		this.additionRemark = additionRemark;
	}

	public String getRequestApprovalReference() {
		return requestApprovalReference;
	}

	public void setRequestApprovalReference(String requestApprovalReference) {
		this.requestApprovalReference = requestApprovalReference;
	}

	public String getRequestApprovalDate() {
		return requestApprovalDate;
	}

	public void setRequestApprovalDate(String requestApprovalDate) {
		this.requestApprovalDate = requestApprovalDate;
	}

	public String getRequestApprovalRemark() {
		return requestApprovalRemark;
	}

	public void setRequestApprovalRemark(String requestApprovalRemark) {
		this.requestApprovalRemark = requestApprovalRemark;
	}

	public String getIsHR() {
		return isHR;
	}

	public void setIsHR(String isHR) {
		this.isHR = isHR;
	}

	public String getHoBuyServiceInd() {
		return hoBuyServiceInd;
	}

	public void setHoBuyServiceInd(String hoBuyServiceInd) {
		this.hoBuyServiceInd = hoBuyServiceInd;
	}

	public String getViewAnnualPlanInd() {
		return viewAnnualPlanInd;
	}

	public void setViewAnnualPlanInd(String viewAnnualPlanInd) {
		this.viewAnnualPlanInd = viewAnnualPlanInd;
	}

	public String getViewProgramYear() {
		return viewProgramYear;
	}

	public void setViewProgramYear(String viewProgramYear) {
		this.viewProgramYear = viewProgramYear;
	}

	public String getViewProgramTypeCode() {
		return viewProgramTypeCode;
	}

	public void setViewProgramTypeCode(String viewProgramTypeCode) {
		this.viewProgramTypeCode = viewProgramTypeCode;
	}

	public String getViewProgramName() {
		return viewProgramName;
	}

	public void setViewProgramName(String viewProgramName) {
		this.viewProgramName = viewProgramName;
	}

	public String getViewFundSrcId() {
		return viewFundSrcId;
	}

	public void setViewFundSrcId(String viewFundSrcId) {
		this.viewFundSrcId = viewFundSrcId;
	}

	public String getViewFundSrcSubCatId() {
		return viewFundSrcSubCatId;
	}

	public void setViewFundSrcSubCatId(String viewFundSrcSubCatId) {
		this.viewFundSrcSubCatId = viewFundSrcSubCatId;
	}

	public String getViewFundSrcStartDate() {
		return viewFundSrcStartDate;
	}

	public void setViewFundSrcStartDate(String viewFundSrcStartDate) {
		this.viewFundSrcStartDate = viewFundSrcStartDate;
	}

	public String getViewFundSrcEndDate() {
		return viewFundSrcEndDate;
	}

	public void setViewFundSrcEndDate(String viewFundSrcEndDate) {
		this.viewFundSrcEndDate = viewFundSrcEndDate;
	}

	public String getViewFundSrcFte() {
		return viewFundSrcFte;
	}

	public void setViewFundSrcFte(String viewFundSrcFte) {
		this.viewFundSrcFte = viewFundSrcFte;
	}

	public String getViewInst() {
		return viewInst;
	}

	public void setViewInst(String viewInst) {
		this.viewInst = viewInst;
	}

	public String getViewSection() {
		return viewSection;
	}

	public void setViewSection(String viewSection) {
		this.viewSection = viewSection;
	}

	public String getViewAnalytical() {
		return viewAnalytical;
	}

	public void setViewAnalytical(String viewAnalytical) {
		this.viewAnalytical = viewAnalytical;
	}

	public String getViewProgramCode() {
		return viewProgramCode;
	}

	public void setViewProgramCode(String viewProgramCode) {
		this.viewProgramCode = viewProgramCode;
	}

	public String getViewFundSrcRemark() {
		return viewFundSrcRemark;
	}

	public void setViewFundSrcRemark(String viewFundSrcRemark) {
		this.viewFundSrcRemark = viewFundSrcRemark;
	}
}

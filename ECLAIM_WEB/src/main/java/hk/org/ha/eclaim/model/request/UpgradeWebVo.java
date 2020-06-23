package hk.org.ha.eclaim.model.request;

import java.util.ArrayList;
import java.util.List;

import hk.org.ha.eclaim.bs.request.po.RequestPostPo;

public class UpgradeWebVo extends CommonWebVo {
	private String withMassSave = "N";
	
	private String requestNo;
	private String requestId;
	private String requester;
	private String requestStatus;
	private String requestReason;
	private String effectiveDate;
	private String cluster;
	private String institution;
	private String department;
	private String staffGroup;
	private String rank;
	
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
	private String annualPlanInd;
	private String programYear;
	private String programCode;
	private String programName;
	private String fundSrcId;
	private String fundSrcSubCatId;
	private String fundSrcStartDate;
	private String fundSrcEndDate;
	private String fundSrcFte;
	private String fundSrcRemark;
	private String inst;
	private String section;
	private String analytical;
	private String programTypeCode;
		
	//Resource
	private String res_sup_fr_ext;
	private String res_sup_remark;
	
	//Post ID Assignment
	private String proposed_post_id;
	private String post_id_just;
	
	private String updateSuccess;
	private String displayMessage;
	private String formAction;
	
	private List<String> requestPostNo = new ArrayList<String>();
	private List<String> requestPostNoTo = new ArrayList<String>();
	
	private List<String> requestCluster = new ArrayList<String>();
	private List<String> requestInst = new ArrayList<String>();
	private List<String> requestDept = new ArrayList<String>();
	private List<String> requestStaffGroup = new ArrayList<String>();
	private List<String> requestRank = new ArrayList<String>();
	private List<String> requestUnit = new ArrayList<String>();
	private List<String> requestPostTitle = new ArrayList<String>();
	private List<String> requestPostDuration = new ArrayList<String>();
	private List<String> requestPostStartDate = new ArrayList<String>();
	private List<String> requestRdDuration = new ArrayList<String>();
	private List<Integer> requestDurationValue = new ArrayList<Integer>();
	private List<String> requestDurationUnit = new ArrayList<String>();
	private List<String> requestPostActualStartDate = new ArrayList<String>();
	private List<String> requestRdDuration2 = new ArrayList<String>();
	private List<String> requestPostActualEndDate = new ArrayList<String>();
	private List<String> requestPostRemark = new ArrayList<String>();
	private List<String> requestPostFTE = new ArrayList<String>();
	private List<String> requestPostFTEValue = new ArrayList<String>();
	private List<String> requestPositionStatus = new ArrayList<String>();
	private List<String> requestPositionStartDate = new ArrayList<String>();
	private List<String> requestPositionEndDate = new ArrayList<String>();
	private List<String> requestClusterRefNo = new ArrayList<String>();
	private List<String> requestClusterRemark = new ArrayList<String>();
	private List<String> requestResSupFrExt = new ArrayList<String>();
	private List<String> requestResSupRemark = new ArrayList<String>();
	private List<String> requestProposedPostId = new ArrayList<String>();
	private List<String> requestPostIdJust = new ArrayList<String>();
	private List<String> requestHoBuyServiceInd = new ArrayList<String>();
	
	private List<String> requestAnnualPlanInd = new ArrayList<String>();
	private List<String> requestProgramYear = new ArrayList<String>();
	private List<String> requestProgramCode = new ArrayList<String>();
	private List<String> requestProgramName = new ArrayList<String>();
	private List<String> requestProgramRemark = new ArrayList<String>();
	private List<String> requestFundSrc1st = new ArrayList<String>();
	private List<String> requestFundSrc1stStartDate = new ArrayList<String>();
	private List<String> requestFundSrc1stEndDate = new ArrayList<String>();
	private List<String> requestFund1stFTE = new ArrayList<String>();
	private List<String> requestFundRemark = new ArrayList<String>();
	
	private List<String> requestProgramType = new ArrayList<String>();
	private List<String> requestSubSpecialty = new ArrayList<String>();
	
	private List<RequestPostPo> requestPositionFromList = new ArrayList<RequestPostPo>();
	private List<RequestPostPo> requestPositionToList = new ArrayList<RequestPostPo>();
	
	private String relatedHcmJob;
	private String relatedHcmPostTitle;
	private String relatedHcmPostOrganization;
	private String relatedHcmOrganization;
	private String relatedHcmUnitTeam;
	private String relatedHcmEffectiveStartDate;
	private String relatedHcmEffectiveEndDate;
	private String relatedHcmDateEffective;
	private String relatedHcmFTE;
	private String relatedHcmHeadCount;
	private String relatedHcmPositionName;
	private String relatedHcmHiringStatus;
	private String relatedHcmType;
	
	private String hcmJob;
	private String hcmPostTitle;
	private String hcmPostOrganization;
	private String hcmOrganization;
	private String hcmUnitTeam;
	
	private String ddHcmJob;
	private String ddHcmPostTitle;
	private String ddHcmPostOrganization;
	private String ddHcmOrganization;
	private String ddHcmUnitTeam;
	
	private String subSpecialty;
	private String programType;
	private String fund_src_1st_fte;
	
	private String canEditDetailInfo;
	private String canEditFinancialInfo;
	
	private List<String> hcmPositionId = new ArrayList<String>();
	
	private String hcmPositionName; // Added for UT29984
	
	private String hoBuyServiceInd;
	
	private String tmpStaffGroup;
	
	public String getRequestId() {
		return requestId;
	}
	
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	public String getRequester() {
		return requester;
	}
	
	public void setRequester(String requester) {
		this.requester = requester;
	}

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getUpdateSuccess() {
		return updateSuccess;
	}

	public void setUpdateSuccess(String updateSuccess) {
		this.updateSuccess = updateSuccess;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public String getDisplayMessage() {
		return displayMessage;
	}

	public void setDisplayMessage(String displayMessage) {
		this.displayMessage = displayMessage;
	}

	public String getFormAction() {
		return formAction;
	}

	public void setFormAction(String formAction) {
		this.formAction = formAction;
	}

	public String getRequestReason() {
		return requestReason;
	}

	public void setRequestReason(String requestReason) {
		this.requestReason = requestReason;
	}

	public List<String> getRequestPostNo() {
		return requestPostNo;
	}

	public void setRequestPostNo(List<String> requestPostNo) {
		this.requestPostNo = requestPostNo;
	}

	public String getWithMassSave() {
		return withMassSave;
	}

	public void setWithMassSave(String withMassSave) {
		this.withMassSave = withMassSave;
	}

	public String getCluster() {
		return cluster;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getStaffGroup() {
		return staffGroup;
	}

	public void setStaffGroup(String staffGroup) {
		this.staffGroup = staffGroup;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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

	public String getClusterRemark() {
		return clusterRemark;
	}

	public void setClusterRemark(String clusterRemark) {
		this.clusterRemark = clusterRemark;
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

	public String getProposed_post_id() {
		return proposed_post_id;
	}

	public void setProposed_post_id(String proposed_post_id) {
		this.proposed_post_id = proposed_post_id;
	}

	public String getPost_id_just() {
		return post_id_just;
	}

	public void setPost_id_just(String post_id_just) {
		this.post_id_just = post_id_just;
	}

	public String getClusterRefNo() {
		return clusterRefNo;
	}

	public void setClusterRefNo(String clusterRefNo) {
		this.clusterRefNo = clusterRefNo;
	}

	public List<String> getRequestPostNoTo() {
		return requestPostNoTo;
	}

	public void setRequestPostNoTo(List<String> requestPostNoTo) {
		this.requestPostNoTo = requestPostNoTo;
	}

	public List<String> getRequestCluster() {
		return requestCluster;
	}

	public void setRequestCluster(List<String> requestCluster) {
		this.requestCluster = requestCluster;
	}

	public List<String> getRequestInst() {
		return requestInst;
	}

	public void setRequestInst(List<String> requestInst) {
		this.requestInst = requestInst;
	}

	public List<String> getRequestDept() {
		return requestDept;
	}

	public void setRequestDept(List<String> requestDept) {
		this.requestDept = requestDept;
	}

	public List<String> getRequestStaffGroup() {
		return requestStaffGroup;
	}

	public void setRequestStaffGroup(List<String> requestStaffGroup) {
		this.requestStaffGroup = requestStaffGroup;
	}

	public List<String> getRequestRank() {
		return requestRank;
	}

	public void setRequestRank(List<String> requestRank) {
		this.requestRank = requestRank;
	}

	public List<String> getRequestUnit() {
		return requestUnit;
	}

	public void setRequestUnit(List<String> requestUnit) {
		this.requestUnit = requestUnit;
	}

	public List<String> getRequestPostTitle() {
		return requestPostTitle;
	}

	public void setRequestPostTitle(List<String> requestPostTitle) {
		this.requestPostTitle = requestPostTitle;
	}

	public List<String> getRequestPostDuration() {
		return requestPostDuration;
	}

	public void setRequestPostDuration(List<String> requestPostDuration) {
		this.requestPostDuration = requestPostDuration;
	}

	public List<String> getRequestPostStartDate() {
		return requestPostStartDate;
	}

	public void setRequestPostStartDate(List<String> requestPostStartDate) {
		this.requestPostStartDate = requestPostStartDate;
	}

	public List<String> getRequestRdDuration() {
		return requestRdDuration;
	}

	public void setRequestRdDuration(List<String> requestRdDuration) {
		this.requestRdDuration = requestRdDuration;
	}

	public List<Integer> getRequestDurationValue() {
		return requestDurationValue;
	}

	public void setRequestDurationValue(List<Integer> requestDurationValue) {
		this.requestDurationValue = requestDurationValue;
	}

	public List<String> getRequestDurationUnit() {
		return requestDurationUnit;
	}

	public void setRequestDurationUnit(List<String> requestDurationUnit) {
		this.requestDurationUnit = requestDurationUnit;
	}

	public List<String> getRequestPostActualStartDate() {
		return requestPostActualStartDate;
	}

	public void setRequestPostActualStartDate(List<String> requestPostActualStartDate) {
		this.requestPostActualStartDate = requestPostActualStartDate;
	}

	public List<String> getRequestRdDuration2() {
		return requestRdDuration2;
	}

	public void setRequestRdDuration2(List<String> requestRdDuration2) {
		this.requestRdDuration2 = requestRdDuration2;
	}

	public List<String> getRequestPostActualEndDate() {
		return requestPostActualEndDate;
	}

	public void setRequestPostActualEndDate(List<String> requestPostActualEndDate) {
		this.requestPostActualEndDate = requestPostActualEndDate;
	}

	public List<String> getRequestPostRemark() {
		return requestPostRemark;
	}

	public void setRequestPostRemark(List<String> requestPostRemark) {
		this.requestPostRemark = requestPostRemark;
	}

	public List<String> getRequestPostFTE() {
		return requestPostFTE;
	}

	public void setRequestPostFTE(List<String> requestPostFTE) {
		this.requestPostFTE = requestPostFTE;
	}

	public List<String> getRequestPostFTEValue() {
		return requestPostFTEValue;
	}

	public void setRequestPostFTEValue(List<String> requestPostFTEValue) {
		this.requestPostFTEValue = requestPostFTEValue;
	}

	public List<String> getRequestPositionStatus() {
		return requestPositionStatus;
	}

	public void setRequestPositionStatus(List<String> requestPositionStatus) {
		this.requestPositionStatus = requestPositionStatus;
	}

	public List<String> getRequestPositionStartDate() {
		return requestPositionStartDate;
	}

	public void setRequestPositionStartDate(List<String> requestPositionStartDate) {
		this.requestPositionStartDate = requestPositionStartDate;
	}

	public List<String> getRequestPositionEndDate() {
		return requestPositionEndDate;
	}

	public void setRequestPositionEndDate(List<String> requestPositionEndDate) {
		this.requestPositionEndDate = requestPositionEndDate;
	}

	public List<String> getRequestClusterRefNo() {
		return requestClusterRefNo;
	}

	public void setRequestClusterRefNo(List<String> requestClusterRefNo) {
		this.requestClusterRefNo = requestClusterRefNo;
	}

	public List<String> getRequestClusterRemark() {
		return requestClusterRemark;
	}

	public void setRequestClusterRemark(List<String> requestClusterRemark) {
		this.requestClusterRemark = requestClusterRemark;
	}

	public List<String> getRequestResSupFrExt() {
		return requestResSupFrExt;
	}

	public void setRequestResSupFrExt(List<String> requestResSupFrExt) {
		this.requestResSupFrExt = requestResSupFrExt;
	}

	public List<String> getRequestResSupRemark() {
		return requestResSupRemark;
	}

	public void setRequestResSupRemark(List<String> requestResSupRemark) {
		this.requestResSupRemark = requestResSupRemark;
	}

	public List<String> getRequestProposedPostId() {
		return requestProposedPostId;
	}

	public void setRequestProposedPostId(List<String> requestProposedPostId) {
		this.requestProposedPostId = requestProposedPostId;
	}

	public List<String> getRequestPostIdJust() {
		return requestPostIdJust;
	}

	public void setRequestPostIdJust(List<String> requestPostIdJust) {
		this.requestPostIdJust = requestPostIdJust;
	}

	public List<String> getRequestAnnualPlanInd() {
		return requestAnnualPlanInd;
	}

	public void setRequestAnnualPlanInd(List<String> requestAnnualPlanInd) {
		this.requestAnnualPlanInd = requestAnnualPlanInd;
	}

	public List<String> getRequestProgramYear() {
		return requestProgramYear;
	}

	public void setRequestProgramYear(List<String> requestProgramYear) {
		this.requestProgramYear = requestProgramYear;
	}

	public List<String> getRequestProgramCode() {
		return requestProgramCode;
	}

	public void setRequestProgramCode(List<String> requestProgramCode) {
		this.requestProgramCode = requestProgramCode;
	}

	public List<String> getRequestProgramName() {
		return requestProgramName;
	}

	public void setRequestProgramName(List<String> requestProgramName) {
		this.requestProgramName = requestProgramName;
	}

	public List<String> getRequestProgramRemark() {
		return requestProgramRemark;
	}

	public void setRequestProgramRemark(List<String> requestProgramRemark) {
		this.requestProgramRemark = requestProgramRemark;
	}

	public List<String> getRequestFundSrc1st() {
		return requestFundSrc1st;
	}

	public void setRequestFundSrc1st(List<String> requestFundSrc1st) {
		this.requestFundSrc1st = requestFundSrc1st;
	}
	
	public List<String> getRequestFundSrc1stStartDate() {
		return requestFundSrc1stStartDate;
	}

	public void setRequestFundSrc1stStartDate(List<String> requestFundSrc1stStartDate) {
		this.requestFundSrc1stStartDate = requestFundSrc1stStartDate;
	}

	public List<String> getRequestFundSrc1stEndDate() {
		return requestFundSrc1stEndDate;
	}

	public void setRequestFundSrc1stEndDate(List<String> requestFundSrc1stEndDate) {
		this.requestFundSrc1stEndDate = requestFundSrc1stEndDate;
	}

	public List<String> getRequestFundRemark() {
		return requestFundRemark;
	}

	public void setRequestFundRemark(List<String> requestFundRemark) {
		this.requestFundRemark = requestFundRemark;
	}
	
	public List<RequestPostPo> getRequestPositionFromList() {
		return requestPositionFromList;
	}

	public void setRequestPositionFromList(List<RequestPostPo> requestPositionFromList) {
		this.requestPositionFromList = requestPositionFromList;
	}

	public List<RequestPostPo> getRequestPositionToList() {
		return requestPositionToList;
	}

	public void setRequestPositionToList(List<RequestPostPo> requestPositionToList) {
		this.requestPositionToList = requestPositionToList;
	}

	public String getRelatedHcmJob() {
		return relatedHcmJob;
	}

	public void setRelatedHcmJob(String relatedHcmJob) {
		this.relatedHcmJob = relatedHcmJob;
	}

	public String getRelatedHcmPostTitle() {
		return relatedHcmPostTitle;
	}

	public void setRelatedHcmPostTitle(String relatedHcmPostTitle) {
		this.relatedHcmPostTitle = relatedHcmPostTitle;
	}

	public String getRelatedHcmPostOrganization() {
		return relatedHcmPostOrganization;
	}

	public void setRelatedHcmPostOrganization(String relatedHcmPostOrganization) {
		this.relatedHcmPostOrganization = relatedHcmPostOrganization;
	}

	public String getRelatedHcmOrganization() {
		return relatedHcmOrganization;
	}

	public void setRelatedHcmOrganization(String relatedHcmOrganization) {
		this.relatedHcmOrganization = relatedHcmOrganization;
	}

	public String getRelatedHcmUnitTeam() {
		return relatedHcmUnitTeam;
	}

	public void setRelatedHcmUnitTeam(String relatedHcmUnitTeam) {
		this.relatedHcmUnitTeam = relatedHcmUnitTeam;
	}

	public String getRelatedHcmEffectiveStartDate() {
		return relatedHcmEffectiveStartDate;
	}

	public void setRelatedHcmEffectiveStartDate(String relatedHcmEffectiveStartDate) {
		this.relatedHcmEffectiveStartDate = relatedHcmEffectiveStartDate;
	}

	public String getRelatedHcmEffectiveEndDate() {
		return relatedHcmEffectiveEndDate;
	}

	public void setRelatedHcmEffectiveEndDate(String relatedHcmEffectiveEndDate) {
		this.relatedHcmEffectiveEndDate = relatedHcmEffectiveEndDate;
	}

	public String getRelatedHcmDateEffective() {
		return relatedHcmDateEffective;
	}

	public void setRelatedHcmDateEffective(String relatedHcmDateEffective) {
		this.relatedHcmDateEffective = relatedHcmDateEffective;
	}

	public String getRelatedHcmFTE() {
		return relatedHcmFTE;
	}

	public void setRelatedHcmFTE(String relatedHcmFTE) {
		this.relatedHcmFTE = relatedHcmFTE;
	}

	public String getRelatedHcmHeadCount() {
		return relatedHcmHeadCount;
	}

	public void setRelatedHcmHeadCount(String relatedHcmHeadCount) {
		this.relatedHcmHeadCount = relatedHcmHeadCount;
	}

	public String getRelatedHcmPositionName() {
		return relatedHcmPositionName;
	}

	public void setRelatedHcmPositionName(String relatedHcmPositionName) {
		this.relatedHcmPositionName = relatedHcmPositionName;
	}

	public String getRelatedHcmHiringStatus() {
		return relatedHcmHiringStatus;
	}

	public void setRelatedHcmHiringStatus(String relatedHcmHiringStatus) {
		this.relatedHcmHiringStatus = relatedHcmHiringStatus;
	}

	public String getRelatedHcmType() {
		return relatedHcmType;
	}

	public void setRelatedHcmType(String relatedHcmType) {
		this.relatedHcmType = relatedHcmType;
	}

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

	public String getHcmPostOrganization() {
		return hcmPostOrganization;
	}

	public void setHcmPostOrganization(String hcmPostOrganization) {
		this.hcmPostOrganization = hcmPostOrganization;
	}

	public String getHcmOrganization() {
		return hcmOrganization;
	}

	public void setHcmOrganization(String hcmOrganization) {
		this.hcmOrganization = hcmOrganization;
	}

	public String getHcmUnitTeam() {
		return hcmUnitTeam;
	}

	public void setHcmUnitTeam(String hcmUnitTeam) {
		this.hcmUnitTeam = hcmUnitTeam;
	}

	public String getDdHcmJob() {
		return ddHcmJob;
	}

	public void setDdHcmJob(String ddHcmJob) {
		this.ddHcmJob = ddHcmJob;
	}

	public String getDdHcmPostTitle() {
		return ddHcmPostTitle;
	}

	public void setDdHcmPostTitle(String ddHcmPostTitle) {
		this.ddHcmPostTitle = ddHcmPostTitle;
	}

	public String getDdHcmPostOrganization() {
		return ddHcmPostOrganization;
	}

	public void setDdHcmPostOrganization(String ddHcmPostOrganization) {
		this.ddHcmPostOrganization = ddHcmPostOrganization;
	}

	public String getDdHcmOrganization() {
		return ddHcmOrganization;
	}

	public void setDdHcmOrganization(String ddHcmOrganization) {
		this.ddHcmOrganization = ddHcmOrganization;
	}

	public String getDdHcmUnitTeam() {
		return ddHcmUnitTeam;
	}

	public void setDdHcmUnitTeam(String ddHcmUnitTeam) {
		this.ddHcmUnitTeam = ddHcmUnitTeam;
	}

	public String getSubSpecialty() {
		return subSpecialty;
	}

	public void setSubSpecialty(String subSpecialty) {
		this.subSpecialty = subSpecialty;
	}

	public String getProgramType() {
		return programType;
	}

	public void setProgramType(String programType) {
		this.programType = programType;
	}

	public String getFund_src_1st_fte() {
		return fund_src_1st_fte;
	}

	public void setFund_src_1st_fte(String fund_src_1st_fte) {
		this.fund_src_1st_fte = fund_src_1st_fte;
	}

	public List<String> getRequestFund1stFTE() {
		return requestFund1stFTE;
	}

	public void setRequestFund1stFTE(List<String> requestFund1stFTE) {
		this.requestFund1stFTE = requestFund1stFTE;
	}

	public List<String> getHcmPositionId() {
		return hcmPositionId;
	}

	public void setHcmPositionId(List<String> hcmPositionId) {
		this.hcmPositionId = hcmPositionId;
	}

	public String getCanEditDetailInfo() {
		return canEditDetailInfo;
	}

	public void setCanEditDetailInfo(String canEditDetailInfo) {
		this.canEditDetailInfo = canEditDetailInfo;
	}

	public String getCanEditFinancialInfo() {
		return canEditFinancialInfo;
	}

	public void setCanEditFinancialInfo(String canEditFinancialInfo) {
		this.canEditFinancialInfo = canEditFinancialInfo;
	}

	public List<String> getRequestProgramType() {
		return requestProgramType;
	}

	public void setRequestProgramType(List<String> requestProgramType) {
		this.requestProgramType = requestProgramType;
	}

	public List<String> getRequestSubSpecialty() {
		return requestSubSpecialty;
	}

	public void setRequestSubSpecialty(List<String> requestSubSpecialty) {
		this.requestSubSpecialty = requestSubSpecialty;
	}

	public String getHcmPositionName() {
		return hcmPositionName;
	}

	public void setHcmPositionName(String hcmPositionName) {
		this.hcmPositionName = hcmPositionName;
	}

	public String getHoBuyServiceInd() {
		return hoBuyServiceInd;
	}

	public void setHoBuyServiceInd(String hoBuyServiceInd) {
		this.hoBuyServiceInd = hoBuyServiceInd;
	}

	public String getTmpStaffGroup() {
		return tmpStaffGroup;
	}

	public void setTmpStaffGroup(String tmpStaffGroup) {
		this.tmpStaffGroup = tmpStaffGroup;
	}

	public String getAnnualPlanInd() {
		return annualPlanInd;
	}

	public void setAnnualPlanInd(String annualPlanInd) {
		this.annualPlanInd = annualPlanInd;
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
	
	public String getFundSrcId() {
		return fundSrcId;
	}

	public void setFundSrcId(String fundSrcId) {
		this.fundSrcId = fundSrcId;
	}

	public String getFundSrcSubCatId() {
		return fundSrcSubCatId;
	}

	public void setFundSrcSubCatId(String fundSrcSubCatId) {
		this.fundSrcSubCatId = fundSrcSubCatId;
	}

	public String getFundSrcStartDate() {
		return fundSrcStartDate;
	}

	public void setFundSrcStartDate(String fundSrcStartDate) {
		this.fundSrcStartDate = fundSrcStartDate;
	}

	public String getFundSrcEndDate() {
		return fundSrcEndDate;
	}

	public void setFundSrcEndDate(String fundSrcEndDate) {
		this.fundSrcEndDate = fundSrcEndDate;
	}

	public String getFundSrcFte() {
		return fundSrcFte;
	}

	public void setFundSrcFte(String fundSrcFte) {
		this.fundSrcFte = fundSrcFte;
	}

	public String getFundSrcRemark() {
		return fundSrcRemark;
	}

	public void setFundSrcRemark(String fundSrcRemark) {
		this.fundSrcRemark = fundSrcRemark;
	}

	public String getInst() {
		return inst;
	}

	public void setInst(String inst) {
		this.inst = inst;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getAnalytical() {
		return analytical;
	}

	public void setAnalytical(String analytical) {
		this.analytical = analytical;
	}

	public String getProgramTypeCode() {
		return programTypeCode;
	}

	public void setProgramTypeCode(String programTypeCode) {
		this.programTypeCode = programTypeCode;
	}

	public List<String> getRequestHoBuyServiceInd() {
		return requestHoBuyServiceInd;
	}

	public void setRequestHoBuyServiceInd(List<String> requestHoBuyServiceInd) {
		this.requestHoBuyServiceInd = requestHoBuyServiceInd;
	}
}
package hk.org.ha.eclaim.model.request;

import java.util.ArrayList;
import java.util.List;

import hk.org.ha.eclaim.bs.request.po.RequestPostPo;

public class NewPostWebVo extends CommonWebVo {
	private String withMassSave = "Y";
	
	private String requestNo;
	private String requestId;
	private String requestStatus;
	private String postID;
	private String cluster;
	private String institution;
	private String department;
	private String staffGroup;
	private String staffGroupDisplay;
	private String rank;
	private String requester;
	private String effectiveDate;
	
	//HCM Position
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
	
	private String currentRequestNo;
	
	private String hcmPositionId;
	
	//Position
	private String unit;
	private String postTitle;	
	private String postStartDate;		
	private String postActualStartDate;	
	private String postDuration; 		
	private Integer limitDurationNo;	
	private String limitDurationUnit;
	private String limitDurationType;		
	private String limitDurationEndDate;
	private String postRemark;			
	private String postFTE;				
	private String postFTEValue;		
	private String positionStatus;		
	private String positionStartDate;
	private String positionEndDate;			
	private String clusterRemark;	
	
	//Resource
	private String res_sup_fr_ext;
	private String res_sup_remark;
	
	//Post ID Assignment
	private String proposed_post_id;
	
	private String createBy;
	private String updateBy;
	
	private String updateSuccess;
	private String displayMessage;
	private String formAction;
	
	private String subSpecialty;
	private String programType;
	
	private String clusterRefNo;
	
	private String noOfCopySave;
	
	private List<String> requestPostNo = new ArrayList<String>();
	
	private List<RequestPostPo> requestPositionList = new ArrayList<RequestPostPo>();
	
	private List<RequestFundingWebVo> requestFundingList = new ArrayList<RequestFundingWebVo>();
		
	private String userName;
	
	private String showDetail;
	
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
	
	private String canEditDetailInfo;
	private String canEditFinancialInfo;
	
	// For Detail Enquiry use
	private String dtlUnitN;
	private String dtlPostTitleN;	
	private String dtlPostStartDateN;		
	private String dtlPostActualStartDateN;	
	private String dtlPostDurationN; 		
	private String dtlLimitDurationNoN;	
	private String dtlLimitDurationUnitN;
	private String dtlLimitDurationTypeN;		
	private String dtlLimitDurationEndDateN;
	private String dtlPostRemarkN;			
	private String dtlPostFTEN;				
	private String dtlPostFTEValueN;		
	private String dtlPositionStatusN;		
	private String dtlPositionStartDateN;
	private String dtlPositionEndDateN;		
	private String dtlClusterRefNoN;		
	private String dtlClusterRemarkN;	
	
	//Funding
	private String dtlAnnual_plan_indN;
	private String dtlProgram_yearN;
	private String dtlProgram_codeN;
	private String dtlProgram_nameN;
	private String dtlProgram_remarkN;
	private String dtlFund_src_1stN;	
	private String dtlFund_src_1st_start_dateN;
	private String dtlFund_src_1st_end_dateN;
	private String dtlFund_src_1st_fteN;
	private String dtlFund_remarkN;
	
	private String dtlSubSpecialtyN;
	private String dtlProgramTypeN;
	
	//Resource
	private String dtlRes_sup_fr_extN;
	private String dtlRes_sup_remarkN;
	
	private String currentRole;
	
	private String hcmPositionName; // Added for UT29984
	
	private String shortFallPostId;
	
	private String canEditShortFall;
	private String showEditShortFall;
	
	private String postEndDate;
	private String hoBuyServiceInd;
	
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}

	public String getPostID() {
		return postID;
	}

	public void setPostID(String postID) {
		this.postID = postID;
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

	public Integer getLimitDurationNo() {
		return limitDurationNo;
	}

	public void setLimitDurationNo(Integer limitDurationNo) {
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

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getUpdateSuccess() {
		return updateSuccess;
	}

	public void setUpdateSuccess(String updateSuccess) {
		this.updateSuccess = updateSuccess;
	}

	public String getFormAction() {
		return formAction;
	}
	
	public void setFormAction(String formAction) {
		this.formAction = formAction;
	}

	public String getDisplayMessage() {
		return displayMessage;
	}

	public void setDisplayMessage(String displayMessage) {
		this.displayMessage = displayMessage;
	}

	public List<String> getRequestPostNo() {
		return requestPostNo;
	}

	public void setRequestPostNo(List<String> requestPostNo) {
		this.requestPostNo = requestPostNo;
	}

	public List<RequestPostPo> getRequestPositionList() {
		return requestPositionList;
	}

	public void setRequestPositionList(List<RequestPostPo> requestPositionList) {
		this.requestPositionList = requestPositionList;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public String getRequester() {
		return requester;
	}

	public void setRequester(String requester) {
		this.requester = requester;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getShowDetail() {
		return showDetail;
	}

	public void setShowDetail(String showDetail) {
		this.showDetail = showDetail;
	}

	public String getNoOfCopySave() {
		return noOfCopySave;
	}

	public void setNoOfCopySave(String noOfCopySave) {
		this.noOfCopySave = noOfCopySave;
	}

	public String getClusterRefNo() {
		return clusterRefNo;
	}

	public void setClusterRefNo(String clusterRefNo) {
		this.clusterRefNo = clusterRefNo;
	}
	
	public String getCurrentRequestNo() {
		return currentRequestNo;
	}

	public void setCurrentRequestNo(String currentRequestNo) {
		this.currentRequestNo = currentRequestNo;
	}

	public String getWithMassSave() {
		return withMassSave;
	}

	public void setWithMassSave(String withMassSave) {
		this.withMassSave = withMassSave;
	}

	public String getHcmPositionId() {
		return hcmPositionId;
	}

	public void setHcmPositionId(String hcmPositionId) {
		this.hcmPositionId = hcmPositionId;
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

	public String getRelatedHcmOrganization() {
		return relatedHcmOrganization;
	}

	public void setRelatedHcmOrganization(String relatedHcmOrganization) {
		this.relatedHcmOrganization = relatedHcmOrganization;
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
	
	public String getHaveMassSaveRight() {
		System.out.println("haveMassSaveRight: " + this.withMassSave + " / " + this.getUserHaveSaveRight());
		// return withMassSave;
		if ("Y".equals(this.withMassSave) && "Y".equals(this.getUserHaveSaveRight())) {
			return "Y";
		}
		
		return "N";
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

	public String getDtlUnitN() {
		return dtlUnitN;
	}

	public void setDtlUnitN(String dtlUnitN) {
		this.dtlUnitN = dtlUnitN;
	}

	public String getDtlPostTitleN() {
		return dtlPostTitleN;
	}

	public void setDtlPostTitleN(String dtlPostTitleN) {
		this.dtlPostTitleN = dtlPostTitleN;
	}

	public String getDtlPostStartDateN() {
		return dtlPostStartDateN;
	}

	public void setDtlPostStartDateN(String dtlPostStartDateN) {
		this.dtlPostStartDateN = dtlPostStartDateN;
	}

	public String getDtlPostActualStartDateN() {
		return dtlPostActualStartDateN;
	}

	public void setDtlPostActualStartDateN(String dtlPostActualStartDateN) {
		this.dtlPostActualStartDateN = dtlPostActualStartDateN;
	}

	public String getDtlPostDurationN() {
		return dtlPostDurationN;
	}

	public void setDtlPostDurationN(String dtlPostDurationN) {
		this.dtlPostDurationN = dtlPostDurationN;
	}

	public String getDtlLimitDurationNoN() {
		return dtlLimitDurationNoN;
	}

	public void setDtlLimitDurationNoN(String dtlLimitDurationNoN) {
		this.dtlLimitDurationNoN = dtlLimitDurationNoN;
	}

	public String getDtlLimitDurationUnitN() {
		return dtlLimitDurationUnitN;
	}

	public void setDtlLimitDurationUnitN(String dtlLimitDurationUnitN) {
		this.dtlLimitDurationUnitN = dtlLimitDurationUnitN;
	}

	public String getDtlLimitDurationTypeN() {
		return dtlLimitDurationTypeN;
	}

	public void setDtlLimitDurationTypeN(String dtlLimitDurationTypeN) {
		this.dtlLimitDurationTypeN = dtlLimitDurationTypeN;
	}

	public String getDtlLimitDurationEndDateN() {
		return dtlLimitDurationEndDateN;
	}

	public void setDtlLimitDurationEndDateN(String dtlLimitDurationEndDateN) {
		this.dtlLimitDurationEndDateN = dtlLimitDurationEndDateN;
	}

	public String getDtlPostRemarkN() {
		return dtlPostRemarkN;
	}

	public void setDtlPostRemarkN(String dtlPostRemarkN) {
		this.dtlPostRemarkN = dtlPostRemarkN;
	}

	public String getDtlPostFTEN() {
		return dtlPostFTEN;
	}

	public void setDtlPostFTEN(String dtlPostFTEN) {
		this.dtlPostFTEN = dtlPostFTEN;
	}

	public String getDtlPostFTEValueN() {
		return dtlPostFTEValueN;
	}

	public void setDtlPostFTEValueN(String dtlPostFTEValueN) {
		this.dtlPostFTEValueN = dtlPostFTEValueN;
	}

	public String getDtlPositionStatusN() {
		return dtlPositionStatusN;
	}

	public void setDtlPositionStatusN(String dtlPositionStatusN) {
		this.dtlPositionStatusN = dtlPositionStatusN;
	}

	public String getDtlPositionStartDateN() {
		return dtlPositionStartDateN;
	}

	public void setDtlPositionStartDateN(String dtlPositionStartDateN) {
		this.dtlPositionStartDateN = dtlPositionStartDateN;
	}

	public String getDtlPositionEndDateN() {
		return dtlPositionEndDateN;
	}

	public void setDtlPositionEndDateN(String dtlPositionEndDateN) {
		this.dtlPositionEndDateN = dtlPositionEndDateN;
	}

	public String getDtlClusterRefNoN() {
		return dtlClusterRefNoN;
	}

	public void setDtlClusterRefNoN(String dtlClusterRefNoN) {
		this.dtlClusterRefNoN = dtlClusterRefNoN;
	}

	public String getDtlClusterRemarkN() {
		return dtlClusterRemarkN;
	}

	public void setDtlClusterRemarkN(String dtlClusterRemarkN) {
		this.dtlClusterRemarkN = dtlClusterRemarkN;
	}

	public String getDtlAnnual_plan_indN() {
		return dtlAnnual_plan_indN;
	}

	public void setDtlAnnual_plan_indN(String dtlAnnual_plan_indN) {
		this.dtlAnnual_plan_indN = dtlAnnual_plan_indN;
	}

	public String getDtlProgram_yearN() {
		return dtlProgram_yearN;
	}

	public void setDtlProgram_yearN(String dtlProgram_yearN) {
		this.dtlProgram_yearN = dtlProgram_yearN;
	}

	public String getDtlProgram_codeN() {
		return dtlProgram_codeN;
	}

	public void setDtlProgram_codeN(String dtlProgram_codeN) {
		this.dtlProgram_codeN = dtlProgram_codeN;
	}

	public String getDtlProgram_nameN() {
		return dtlProgram_nameN;
	}

	public void setDtlProgram_nameN(String dtlProgram_nameN) {
		this.dtlProgram_nameN = dtlProgram_nameN;
	}

	public String getDtlProgram_remarkN() {
		return dtlProgram_remarkN;
	}

	public void setDtlProgram_remarkN(String dtlProgram_remarkN) {
		this.dtlProgram_remarkN = dtlProgram_remarkN;
	}

	public String getDtlFund_src_1stN() {
		return dtlFund_src_1stN;
	}

	public void setDtlFund_src_1stN(String dtlFund_src_1stN) {
		this.dtlFund_src_1stN = dtlFund_src_1stN;
	}

	public String getDtlFund_src_1st_start_dateN() {
		return dtlFund_src_1st_start_dateN;
	}

	public void setDtlFund_src_1st_start_dateN(String dtlFund_src_1st_start_dateN) {
		this.dtlFund_src_1st_start_dateN = dtlFund_src_1st_start_dateN;
	}

	public String getDtlFund_src_1st_end_dateN() {
		return dtlFund_src_1st_end_dateN;
	}

	public void setDtlFund_src_1st_end_dateN(String dtlFund_src_1st_end_dateN) {
		this.dtlFund_src_1st_end_dateN = dtlFund_src_1st_end_dateN;
	}
	
	public String getDtlFund_remarkN() {
		return dtlFund_remarkN;
	}

	public void setDtlFund_remarkN(String dtlFund_remarkN) {
		this.dtlFund_remarkN = dtlFund_remarkN;
	}

	public String getDtlFund_src_1st_fteN() {
		return dtlFund_src_1st_fteN;
	}

	public void setDtlFund_src_1st_fteN(String dtlFund_src_1st_fteN) {
		this.dtlFund_src_1st_fteN = dtlFund_src_1st_fteN;
	}

	public String getDtlSubSpecialtyN() {
		return dtlSubSpecialtyN;
	}

	public void setDtlSubSpecialtyN(String dtlSubSpecialtyN) {
		this.dtlSubSpecialtyN = dtlSubSpecialtyN;
	}

	public String getDtlProgramTypeN() {
		return dtlProgramTypeN;
	}

	public void setDtlProgramTypeN(String dtlProgramTypeN) {
		this.dtlProgramTypeN = dtlProgramTypeN;
	}

	public String getDtlRes_sup_fr_extN() {
		return dtlRes_sup_fr_extN;
	}

	public void setDtlRes_sup_fr_extN(String dtlRes_sup_fr_extN) {
		this.dtlRes_sup_fr_extN = dtlRes_sup_fr_extN;
	}

	public String getDtlRes_sup_remarkN() {
		return dtlRes_sup_remarkN;
	}

	public void setDtlRes_sup_remarkN(String dtlRes_sup_remarkN) {
		this.dtlRes_sup_remarkN = dtlRes_sup_remarkN;
	}

	public String getCurrentRole() {
		return currentRole;
	}

	public void setCurrentRole(String currentRole) {
		this.currentRole = currentRole;
	}

	public String getHcmPositionName() {
		return hcmPositionName;
	}

	public void setHcmPositionName(String hcmPositionName) {
		this.hcmPositionName = hcmPositionName;
	}

	public List<RequestFundingWebVo> getRequestFundingList() {
		return requestFundingList;
	}

	public void setRequestFundingList(List<RequestFundingWebVo> requestFundingList) {
		this.requestFundingList = requestFundingList;
	}

	public String getShortFallPostId() {
		return shortFallPostId;
	}

	public void setShortFallPostId(String shortFallPostId) {
		this.shortFallPostId = shortFallPostId;
	}

	public String getCanEditShortFall() {
		return canEditShortFall;
	}

	public void setCanEditShortFall(String canEditShortFall) {
		this.canEditShortFall = canEditShortFall;
	}

	public String getShowEditShortFall() {
		return showEditShortFall;
	}

	public void setShowEditShortFall(String showEditShortFall) {
		this.showEditShortFall = showEditShortFall;
	}

	public String getPostEndDate() {
		return postEndDate;
	}

	public void setPostEndDate(String postEndDate) {
		this.postEndDate = postEndDate;
	}

	public String getStaffGroupDisplay() {
		return staffGroupDisplay;
	}

	public void setStaffGroupDisplay(String staffGroupDisplay) {
		this.staffGroupDisplay = staffGroupDisplay;
	}

	public String getHoBuyServiceInd() {
		return hoBuyServiceInd;
	}

	public void setHoBuyServiceInd(String hoBuyServiceInd) {
		this.hoBuyServiceInd = hoBuyServiceInd;
	}
}

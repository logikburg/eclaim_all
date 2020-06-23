package hk.org.ha.eclaim.model.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class CommonWebVo {
	// For approval user
	private String approvalRemark;
	private String approvalReference;
	private String approvalDate;
	
	private String currentWFUser;
	private String currentWFGroup;
	private String nextWFUser;
	private String nextWFGroup;
	
	private String submitWithModifiedEmail;
	private String submitWithEmail;
	private String submitWithoutEmail;
	private String emailTo;
	private String emailCC;
	private String emailTitle;
	private String emailContent;
	private String emailSuppInfo;

	private MultipartFile approvalFile;
	
	private String userHaveApprovalRight;
	private String userHaveSubmitRight;
	private String userHaveSaveRight = "Y";
	private String userHaveWithdrawRight;
	private String withMassSave = "N";
	private String userHaveCreationRight = "Y";
	
	private String currentLoginUser;
	
	private String lastUpdateDate;
	
	private String dtlUnit;
	private String dtlPostTitle;	
	private String dtlPostStartDate;		
	private String dtlPostActualStartDate;	
	private String dtlPostDuration; 		
	private String dtlLimitDurationNo;	
	private String dtlLimitDurationUnit;
	private String dtlLimitDurationType;		
	private String dtlLimitDurationEndDate;
	private String dtlPostRemark;			
	private String dtlPostFTE;				
	private String dtlPostFTEValue;		
	private String dtlPositionStatus;		
	private String dtlPositionStartDate;
	private String dtlPositionEndDate;		
	private String dtlClusterRefNo;		
	private String dtlClusterRemark;	
	
	//Funding
	private String dtlAnnual_plan_ind;
	private String dtlProgram_year;
	private String dtlProgram_code;
	private String dtlProgram_name;
	private String dtlProgram_remark;
	private String dtlFund_src_1st;	
	private String dtlFund_src_1st_start_date;
	private String dtlFund_src_1st_end_date;
	private String dtlFund_src_1st_fte;
	private String dtlFund_remark;
	
	private String dtlSubSpecialty;
	private String dtlProgramType;
	
	//Resource
	private String dtlRes_sup_fr_ext;
	private String dtlRes_sup_remark;
	
	private String requestType;
	private String fromRankName;
	
	private List<String> approvalAttachmentFilename;
	private List<String> approvalAttachmentUid;
	private List<String> removeAttachmentUid;

	private String submitButtonLabel;
	
	private List<String> uploadFileId;
	
	private String returnCase;
	
	private String mgTeamInd;
	
	// For searching use (common_MPRSPost_search.jsp)
	private String searchClusterId;
	private String searchInstId;
	private String searchDeptId;
	private String searchStaffGroupId;
	private String searchRankId;
	private String searchPostId;
	private String searchEffectiveDate;
	
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
	
	public String getApprovalRemark() {
		return approvalRemark;
	}

	public void setApprovalRemark(String approvalRemark) {
		this.approvalRemark = approvalRemark;
	}

	public String getApprovalReference() {
		return approvalReference;
	}

	public void setApprovalReference(String approvalReference) {
		this.approvalReference = approvalReference;
	}

	public String getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(String approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getCurrentWFUser() {
		return currentWFUser;
	}

	public void setCurrentWFUser(String currentWFUser) {
		this.currentWFUser = currentWFUser;
	}

	public String getCurrentWFGroup() {
		return currentWFGroup;
	}

	public void setCurrentWFGroup(String currentWFGroup) {
		this.currentWFGroup = currentWFGroup;
	}

	public String getNextWFUser() {
		return nextWFUser;
	}

	public void setNextWFUser(String nextWFUser) {
		this.nextWFUser = nextWFUser;
	}

	public String getNextWFGroup() {
		return nextWFGroup;
	}

	public void setNextWFGroup(String nextWFGroup) {
		this.nextWFGroup = nextWFGroup;
	}

	public String getSubmitWithModifiedEmail() {
		return submitWithModifiedEmail;
	}

	public void setSubmitWithModifiedEmail(String submitWithModifiedEmail) {
		this.submitWithModifiedEmail = submitWithModifiedEmail;
	}

	public String getSubmitWithEmail() {
		return submitWithEmail;
	}

	public void setSubmitWithEmail(String submitWithEmail) {
		this.submitWithEmail = submitWithEmail;
	}

	public String getSubmitWithoutEmail() {
		return submitWithoutEmail;
	}

	public void setSubmitWithoutEmail(String submitWithoutEmail) {
		this.submitWithoutEmail = submitWithoutEmail;
	}

	public String getEmailTo() {
		return emailTo;
	}

	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}

	public String getEmailCC() {
		return emailCC;
	}

	public void setEmailCC(String emailCC) {
		this.emailCC = emailCC;
	}

	public String getEmailTitle() {
		return emailTitle;
	}

	public void setEmailTitle(String emailTitle) {
		this.emailTitle = emailTitle;
	}

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}

	public String getEmailSuppInfo() {
		return emailSuppInfo;
	}

	public void setEmailSuppInfo(String emailSuppInfo) {
		this.emailSuppInfo = emailSuppInfo;
	}

	public MultipartFile getApprovalFile() {
		return approvalFile;
	}

	public void setApprovalFile(MultipartFile approvalFile) {
		this.approvalFile = approvalFile;
	}

	public String getUserHaveApprovalRight() {
		return userHaveApprovalRight;
	}

	public void setUserHaveApprovalRight(String userHaveApprovalRight) {
		this.userHaveApprovalRight = userHaveApprovalRight;
	}

	public String getCurrentLoginUser() {
		return currentLoginUser;
	}

	public void setCurrentLoginUser(String currentLoginUser) {
		this.currentLoginUser = currentLoginUser;
	}

	public String getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getUserHaveSubmitRight() {
		return userHaveSubmitRight;
	}

	public void setUserHaveSubmitRight(String userHaveSubmitRight) {
		this.userHaveSubmitRight = userHaveSubmitRight;
	}

	public String getUserHaveSaveRight() {
		return userHaveSaveRight;
	}

	public void setUserHaveSaveRight(String userHaveSaveRight) {
		this.userHaveSaveRight = userHaveSaveRight;
	}

	public String getUserHaveWithdrawRight() {
		return userHaveWithdrawRight;
	}

	public void setUserHaveWithdrawRight(String userHaveWithdrawRight) {
		this.userHaveWithdrawRight = userHaveWithdrawRight;
	}

	public String getHaveMassSaveRight() {
		System.out.println("haveMassSaveRight: " + this.withMassSave + " / " + userHaveSaveRight);
		// return withMassSave;
		if ("Y".equals(this.withMassSave) && "Y".equals(this.userHaveSaveRight)) {
			return "Y";
		}
		
		return "N";
	}

	public String getDtlPostTitle() {
		return dtlPostTitle;
	}

	public void setDtlPostTitle(String dtlPostTitle) {
		this.dtlPostTitle = dtlPostTitle;
	}

	public String getDtlUnit() {
		return dtlUnit;
	}

	public void setDtlUnit(String dtlUnit) {
		this.dtlUnit = dtlUnit;
	}

	public String getDtlPostStartDate() {
		return dtlPostStartDate;
	}

	public void setDtlPostStartDate(String dtlPostStartDate) {
		this.dtlPostStartDate = dtlPostStartDate;
	}

	public String getDtlPostActualStartDate() {
		return dtlPostActualStartDate;
	}

	public void setDtlPostActualStartDate(String dtlPostActualStartDate) {
		this.dtlPostActualStartDate = dtlPostActualStartDate;
	}

	public String getDtlPostDuration() {
		return dtlPostDuration;
	}

	public void setDtlPostDuration(String dtlPostDuration) {
		this.dtlPostDuration = dtlPostDuration;
	}

	public String getDtlLimitDurationNo() {
		return dtlLimitDurationNo;
	}

	public void setDtlLimitDurationNo(String dtlLimitDurationNo) {
		this.dtlLimitDurationNo = dtlLimitDurationNo;
	}

	public String getDtlLimitDurationUnit() {
		return dtlLimitDurationUnit;
	}

	public void setDtlLimitDurationUnit(String dtlLimitDurationUnit) {
		this.dtlLimitDurationUnit = dtlLimitDurationUnit;
	}

	public String getDtlLimitDurationType() {
		return dtlLimitDurationType;
	}

	public void setDtlLimitDurationType(String dtlLimitDurationType) {
		this.dtlLimitDurationType = dtlLimitDurationType;
	}

	public String getDtlLimitDurationEndDate() {
		return dtlLimitDurationEndDate;
	}

	public void setDtlLimitDurationEndDate(String dtlLimitDurationEndDate) {
		this.dtlLimitDurationEndDate = dtlLimitDurationEndDate;
	}

	public String getDtlPostRemark() {
		return dtlPostRemark;
	}

	public void setDtlPostRemark(String dtlPostRemark) {
		this.dtlPostRemark = dtlPostRemark;
	}

	public String getDtlPostFTE() {
		return dtlPostFTE;
	}

	public void setDtlPostFTE(String dtlPostFTE) {
		this.dtlPostFTE = dtlPostFTE;
	}

	public String getDtlPostFTEValue() {
		return dtlPostFTEValue;
	}

	public void setDtlPostFTEValue(String dtlPostFTEValue) {
		this.dtlPostFTEValue = dtlPostFTEValue;
	}

	public String getDtlPositionStatus() {
		return dtlPositionStatus;
	}

	public void setDtlPositionStatus(String dtlPositionStatus) {
		this.dtlPositionStatus = dtlPositionStatus;
	}

	public String getDtlPositionStartDate() {
		return dtlPositionStartDate;
	}

	public void setDtlPositionStartDate(String dtlPositionStartDate) {
		this.dtlPositionStartDate = dtlPositionStartDate;
	}

	public String getDtlPositionEndDate() {
		return dtlPositionEndDate;
	}

	public void setDtlPositionEndDate(String dtlPositionEndDate) {
		this.dtlPositionEndDate = dtlPositionEndDate;
	}

	public String getDtlClusterRefNo() {
		return dtlClusterRefNo;
	}

	public void setDtlClusterRefNo(String dtlClusterRefNo) {
		this.dtlClusterRefNo = dtlClusterRefNo;
	}

	public String getDtlClusterRemark() {
		return dtlClusterRemark;
	}

	public void setDtlClusterRemark(String dtlClusterRemark) {
		this.dtlClusterRemark = dtlClusterRemark;
	}

	public String getDtlAnnual_plan_ind() {
		return dtlAnnual_plan_ind;
	}

	public void setDtlAnnual_plan_ind(String dtlAnnual_plan_ind) {
		this.dtlAnnual_plan_ind = dtlAnnual_plan_ind;
	}

	public String getDtlProgram_year() {
		return dtlProgram_year;
	}

	public void setDtlProgram_year(String dtlProgram_year) {
		this.dtlProgram_year = dtlProgram_year;
	}

	public String getDtlProgram_code() {
		return dtlProgram_code;
	}

	public void setDtlProgram_code(String dtlProgram_code) {
		this.dtlProgram_code = dtlProgram_code;
	}

	public String getDtlProgram_name() {
		return dtlProgram_name;
	}

	public void setDtlProgram_name(String dtlProgram_name) {
		this.dtlProgram_name = dtlProgram_name;
	}

	public String getDtlProgram_remark() {
		return dtlProgram_remark;
	}

	public void setDtlProgram_remark(String dtlProgram_remark) {
		this.dtlProgram_remark = dtlProgram_remark;
	}

	public String getDtlFund_src_1st() {
		return dtlFund_src_1st;
	}

	public void setDtlFund_src_1st(String dtlFund_src_1st) {
		this.dtlFund_src_1st = dtlFund_src_1st;
	}

	public String getDtlFund_src_1st_start_date() {
		return dtlFund_src_1st_start_date;
	}

	public void setDtlFund_src_1st_start_date(String dtlFund_src_1st_start_date) {
		this.dtlFund_src_1st_start_date = dtlFund_src_1st_start_date;
	}

	public String getDtlFund_src_1st_end_date() {
		return dtlFund_src_1st_end_date;
	}

	public void setDtlFund_src_1st_end_date(String dtlFund_src_1st_end_date) {
		this.dtlFund_src_1st_end_date = dtlFund_src_1st_end_date;
	}
	
	public String getDtlFund_remark() {
		return dtlFund_remark;
	}

	public void setDtlFund_remark(String dtlFund_remark) {
		this.dtlFund_remark = dtlFund_remark;
	}

	public String getDtlRes_sup_remark() {
		return dtlRes_sup_remark;
	}

	public void setDtlRes_sup_remark(String dtlRes_sup_remark) {
		this.dtlRes_sup_remark = dtlRes_sup_remark;
	}

	public String getDtlRes_sup_fr_ext() {
		return dtlRes_sup_fr_ext;
	}

	public void setDtlRes_sup_fr_ext(String dtlRes_sup_fr_ext) {
		this.dtlRes_sup_fr_ext = dtlRes_sup_fr_ext;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getFromRankName() {
		return fromRankName;
	}

	public void setFromRankName(String fromRankName) {
		this.fromRankName = fromRankName;
	}

	public List<String> getApprovalAttachmentFilename() {
		return approvalAttachmentFilename;
	}

	public void setApprovalAttachmentFilename(List<String> approvalAttachmentFilename) {
		this.approvalAttachmentFilename = approvalAttachmentFilename;
	}

	public List<String> getApprovalAttachmentUid() {
		return approvalAttachmentUid;
	}

	public void setApprovalAttachmentUid(List<String> approvalAttachmentUid) {
		this.approvalAttachmentUid = approvalAttachmentUid;
	}

	public List<String> getRemoveAttachmentUid() {
		return removeAttachmentUid;
	}

	public void setRemoveAttachmentUid(List<String> removeAttachmentUid) {
		this.removeAttachmentUid = removeAttachmentUid;
	}

	public String getSubmitButtonLabel() {
		return submitButtonLabel;
	}

	public void setSubmitButtonLabel(String submitButtonLabel) {
		this.submitButtonLabel = submitButtonLabel;
	}

	public String getReturnCase() {
		return returnCase;
	}

	public void setReturnCase(String returnCase) {
		this.returnCase = returnCase;
	}

	public String getUserHaveCreationRight() {
		return userHaveCreationRight;
	}

	public void setUserHaveCreationRight(String userHaveCreationRight) {
		this.userHaveCreationRight = userHaveCreationRight;
	}

	public String getMgTeamInd() {
		return mgTeamInd;
	}

	public void setMgTeamInd(String mgTeamInd) {
		this.mgTeamInd = mgTeamInd;
	}

	public List<String> getUploadFileId() {
		return uploadFileId;
	}

	public void setUploadFileId(List<String> uploadFileId) {
		this.uploadFileId = uploadFileId;
	}

	public String getDtlFund_src_1st_fte() {
		return dtlFund_src_1st_fte;
	}

	public void setDtlFund_src_1st_fte(String dtlFund_src_1st_fte) {
		this.dtlFund_src_1st_fte = dtlFund_src_1st_fte;
	}

	public String getDtlSubSpecialty() {
		return dtlSubSpecialty;
	}

	public void setDtlSubSpecialty(String dtlSubSpecialty) {
		this.dtlSubSpecialty = dtlSubSpecialty;
	}

	public String getDtlProgramType() {
		return dtlProgramType;
	}

	public void setDtlProgramType(String dtlProgramType) {
		this.dtlProgramType = dtlProgramType;
	}

	public String getSearchClusterId() {
		return searchClusterId;
	}

	public void setSearchClusterId(String searchClusterId) {
		this.searchClusterId = searchClusterId;
	}

	public String getSearchInstId() {
		return searchInstId;
	}

	public void setSearchInstId(String searchInstId) {
		this.searchInstId = searchInstId;
	}

	public String getSearchDeptId() {
		return searchDeptId;
	}

	public void setSearchDeptId(String searchDeptId) {
		this.searchDeptId = searchDeptId;
	}

	public String getSearchStaffGroupId() {
		return searchStaffGroupId;
	}

	public void setSearchStaffGroupId(String searchStaffGroupId) {
		this.searchStaffGroupId = searchStaffGroupId;
	}

	public String getSearchRankId() {
		return searchRankId;
	}

	public void setSearchRankId(String searchRankId) {
		this.searchRankId = searchRankId;
	}

	public String getSearchPostId() {
		return searchPostId;
	}

	public void setSearchPostId(String searchPostId) {
		this.searchPostId = searchPostId;
	}

	public String getSearchEffectiveDate() {
		return searchEffectiveDate;
	}

	public void setSearchEffectiveDate(String searchEffectiveDate) {
		this.searchEffectiveDate = searchEffectiveDate;
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

	public String getViewFundSrcSubCatId() {
		return viewFundSrcSubCatId;
	}

	public void setViewFundSrcSubCatId(String viewFundSrcSubCatId) {
		this.viewFundSrcSubCatId = viewFundSrcSubCatId;
	}

	public String getViewFundSrcRemark() {
		return viewFundSrcRemark;
	}

	public void setViewFundSrcRemark(String viewFundSrcRemark) {
		this.viewFundSrcRemark = viewFundSrcRemark;
	}
	
}

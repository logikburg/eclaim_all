package hk.org.ha.eclaim.model.request;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hk.org.ha.eclaim.bs.cs.po.ClusterPo;
import hk.org.ha.eclaim.bs.cs.po.DepartmentPo;
import hk.org.ha.eclaim.bs.cs.po.InstitutionPo;
import hk.org.ha.eclaim.bs.cs.po.ProgramTypePo;
import hk.org.ha.eclaim.bs.cs.po.StaffGroupPo;
import hk.org.ha.eclaim.bs.cs.po.SubSpecialtyPo;
import hk.org.ha.eclaim.bs.cs.vo.EmailTemplateVo;
import hk.org.ha.eclaim.bs.report.po.ReportingPo;
import hk.org.ha.eclaim.bs.request.po.PostMasterPo;
import hk.org.ha.eclaim.bs.request.po.RankVo;
import hk.org.ha.eclaim.core.helper.StrHelper;
import hk.org.ha.eclaim.model.maintenance.RoleVo;

public class MPRSResultResponse {
	private Map<String, String> errorMessage;
	private String searchResult;
	private PostMasterPo mprsPost;
	
	private String clusterCode;
	private String instCode;
	private String staffGroupCode;
	private String rankCode;
	private String deptCode;
	private String hcmPositionId;
	
	private String postId;
	
	// For Enquiry use
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
	private String clusterRefNo;
	private String clusterRemark;
	
	//Resource
	private String res_sup_fr_ext;
	private String res_sup_remark;
	
	//Post ID Assignment
	private String proposed_post_id;
	private String post_id_just;
		
	private int upgradeListSize = 0;
	private int extensionListSize = 0;
	private int deletionListSize = 0;
	private int changeFundingListSize = 0;
	private int suppPromotionListSize = 0;
	private int frozenListSize = 0;
	private int changeStaffMixListSize = 0;
	private int assignmentListSize = 0;
	private int attachmentListSize = 0;
	private int fteAdjustmentListSize = 0;
	
	private String relatedHcmEffectiveStartDate;
	private String relatedHcmFTE;
	private String relatedHcmHeadCount;
	private String relatedHcmPositionName;
	private String relatedHcmHiringStatus;
	private String relatedHcmType;
	
	private String subSpecialty;
	private String token;
	
	//history
	//01, upgrade
	private List<String> upgradeHistRequestList_RequestID = new ArrayList<String>();
	private List<String> upgradeHistRequestList_RequestEffDate = new ArrayList<String>();
	private List<String> upgradeHistRequestList_RequestFromToInd = new ArrayList<String>();
	private List<String> upgradeHistRequestList_RequestPostID = new ArrayList<String>();
	private List<String> upgradeHistRequestList_RequestDept = new ArrayList<String>();
	private List<String> upgradeHistRequestList_RequestRank = new ArrayList<String>();
	private List<String> upgradeHistRequestList_RequestPositiontDurationType = new ArrayList<String>();
	private List<String> upgradeHistRequestList_PositionType = new ArrayList<String>();
	private List<String> upgradeHistRequestList_RequestUid = new ArrayList<String>();
	private List<String> upgradeHistRequestList_Reason = new ArrayList<String>();
	
	//02, extension
	private List<String> extensionHistRequestList_RequestID = new ArrayList<String>();
	private List<String> extensionHistRequestList_RequestTransDate = new ArrayList<String>();
	private List<String> extensionHistRequestList_RequestOrgStartDate = new ArrayList<String>();
	private List<String> extensionHistRequestList_RequestRevisedStartDate = new ArrayList<String>();
	private List<String> extensionHistRequestList_RequestOrgEndDate = new ArrayList<String>();
	private List<String> extensionHistRequestList_RequestRevisedEndDate = new ArrayList<String>();
	private List<String> extensionHistRequestList_RequestUid = new ArrayList<String>();
	
	//03, Deletion
	private List<String> deletionHistRequestList_RequestID = new ArrayList<String>();
	private List<String> deletionHistRequestList_RequestTransDate = new ArrayList<String>();
	private List<String> deletionHistRequestList_RequestEffDate = new ArrayList<String>();
	private List<String> deletionHistRequestList_RequestReason = new ArrayList<String>();
	private List<String> deletionHistRequestList_RequestUid = new ArrayList<String>();
	
	//04, ChangeFunding
	private List<String> changeFundHistRequestList_RequestID = new ArrayList<String>();
	private List<String> changeFundHistRequestList_RequestTransDate = new ArrayList<String>();
	private List<String> changeFundHistRequestList_RequestReason = new ArrayList<String>();
	private List<String> changeFundHistRequestList_RequestUid = new ArrayList<String>();
	
	//05, Supp Promo
	private List<String> suppPromoHistRequestList_RequestID = new ArrayList<String>();
	private List<String> suppPromoHistRequestList_RequestTransDate = new ArrayList<String>();
	private List<String> suppPromoHistRequestList_RequestEffDate = new ArrayList<String>();
	private List<String> suppPromoHistRequestList_RequestRemark = new ArrayList<String>();
	private List<String> suppPromoHistRequestList_RequestUid = new ArrayList<String>();
	
	//06, Frozen
	private List<String> frozenHistRequestList_RequestID = new ArrayList<String>();
	private List<String> frozenHistRequestList_RequestTransDate = new ArrayList<String>();
	private List<String> frozenHistRequestList_RequestEffDate = new ArrayList<String>();
	private List<String> frozenHistRequestList_RequestFrozenEndDate = new ArrayList<String>();
	private List<String> frozenHistRequestList_RequestReason = new ArrayList<String>();
	private List<String> frozenHistRequestList_RequestUid = new ArrayList<String>();
	
	//07, ChangeStaffMix
	private List<String> staffMixHistRequestList_RequestID = new ArrayList<String>();
	private List<String> staffMixHistRequestList_RequestEffDate = new ArrayList<String>();
	private List<String> staffMixHistRequestList_RequestFromToInd = new ArrayList<String>();
	private List<String> staffMixHistRequestList_RequestPostID = new ArrayList<String>();
	private List<String> staffMixHistRequestList_RequestPostID2 = new ArrayList<String>();
	private List<String> staffMixHistRequestList_RequestDept = new ArrayList<String>();
	private List<String> staffMixHistRequestList_RequestRank = new ArrayList<String>();
	private List<String> staffMixHistRequestList_RequestPositiontDurationType = new ArrayList<String>();
	private List<String> staffMixHistRequestList_PositionType = new ArrayList<String>();
	private List<String> staffMixHistRequestList_RequestUid = new ArrayList<String>();
	private List<String> staffMixHistRequestList_Reason = new ArrayList<String>();
	
	// FTE Adjustment
	private List<String> fteAdjustmentHistRequestList_RequestUid = new ArrayList<String>();
	private List<String> fteAdjustmentHistRequestList_RequestID = new ArrayList<String>();
	private List<String> fteAdjustmentHistRequestList_RequestTransDate = new ArrayList<String>();
	private List<String> fteAdjustmentHistRequestList_RequestReason = new ArrayList<String>();
	
	//09, Assignment
	private List<String> assignmentHistList_PositionID = new ArrayList<String>();
	private List<String> assignmentHistList_EmpName = new ArrayList<String>();
	private List<String> assignmentHistList_Rank = new ArrayList<String>();
	private List<String> assignmentHistList_EmpType = new ArrayList<String>();
	private List<String> assignmentHistList_EmpPositionTitle = new ArrayList<String>();
	private List<String> assignmentHistList_FTE = new ArrayList<String>();
	private List<String> assignmentHistList_StartDate = new ArrayList<String>();
	private List<String> assignmentHistList_EndDate = new ArrayList<String>();
	private List<String> assignmentHistList_LeaveReason = new ArrayList<String>();
	private List<String> assignmentHistList_AssignmentNumber = new ArrayList<String>();
	
		
	private List<ReportingPo> reportList;
	
	private List<String> userName;
	private List<String> userId;
	private List<RoleVo> roleList;
	private List<ClusterPo> clusterList = new ArrayList<ClusterPo>();
	private List<InstitutionPo> instList;
	private List<StaffGroupPo> staffGroupList;
	private List<DepartmentPo> deptList;
	private List<RankVo> rankList;
	private List<SubSpecialtyPo> subSpecailtyList;
	
	private EmailTemplateVo emailTemplate = new EmailTemplateVo();
	private String toEmailList;
	private String ccEmailList;
	
	private String requestUid;
	private String requestId;
	
	private String defaultRole;
	private String nextActionName;
	
	// For attachment section
	private List<String> attachmentNameList = new ArrayList<String>();
	private List<String> attachmentUidList = new ArrayList<String>();
	private List<String> attachmentCreatedByList = new ArrayList<String>();
	private List<String> attachmentCreatedDateList = new ArrayList<String>();
	
	private String clusterReferenceNo;  // Added for CC177608
	private String additionRemark;      // Added for CC177608
	
	private String requestApprovalReference; // Added for UT30064
	private String requestApprovalDate; // Added for UT30064
	private String requestApprovalRemark; // Added for UT30064
	
	private List<ProgramTypePo> programTypeList;	// Added for CC176525
	
	private List<PostFundingVo> fundingList;
	
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

	public PostMasterPo getMprsPost() {
		return mprsPost;
	}

	public void setMprsPost(PostMasterPo mprsPost) {
		this.mprsPost = mprsPost;
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

	public List<String> getUpgradeHistRequestList_RequestID() {
		return upgradeHistRequestList_RequestID;
	}

	public void setUpgradeHistRequestList_RequestID(List<String> upgradeHistRequestList_RequestID) {
		this.upgradeHistRequestList_RequestID = upgradeHistRequestList_RequestID;
	}

	public List<String> getUpgradeHistRequestList_RequestEffDate() {
		return upgradeHistRequestList_RequestEffDate;
	}

	public void setUpgradeHistRequestList_RequestEffDate(List<String> upgradeHistRequestList_RequestEffDate) {
		this.upgradeHistRequestList_RequestEffDate = upgradeHistRequestList_RequestEffDate;
	}

	public List<String> getUpgradeHistRequestList_RequestFromToInd() {
		return upgradeHistRequestList_RequestFromToInd;
	}

	public void setUpgradeHistRequestList_RequestFromToInd(List<String> upgradeHistRequestList_RequestFromToInd) {
		this.upgradeHistRequestList_RequestFromToInd = upgradeHistRequestList_RequestFromToInd;
	}

	public List<String> getUpgradeHistRequestList_RequestPostID() {
		return upgradeHistRequestList_RequestPostID;
	}

	public void setUpgradeHistRequestList_RequestPostID(List<String> upgradeHistRequestList_RequestPostID) {
		this.upgradeHistRequestList_RequestPostID = upgradeHistRequestList_RequestPostID;
	}

	public List<String> getUpgradeHistRequestList_RequestDept() {
		return upgradeHistRequestList_RequestDept;
	}

	public void setUpgradeHistRequestList_RequestDept(List<String> upgradeHistRequestList_RequestDept) {
		this.upgradeHistRequestList_RequestDept = upgradeHistRequestList_RequestDept;
	}

	public List<String> getUpgradeHistRequestList_RequestRank() {
		return upgradeHistRequestList_RequestRank;
	}

	public void setUpgradeHistRequestList_RequestRank(List<String> upgradeHistRequestList_RequestRank) {
		this.upgradeHistRequestList_RequestRank = upgradeHistRequestList_RequestRank;
	}

	public List<String> getUpgradeHistRequestList_RequestPositiontDurationType() {
		return upgradeHistRequestList_RequestPositiontDurationType;
	}

	public void setUpgradeHistRequestList_RequestPositiontDurationType(
			List<String> upgradeHistRequestList_RequestPositiontDurationType) {
		this.upgradeHistRequestList_RequestPositiontDurationType = upgradeHistRequestList_RequestPositiontDurationType;
	}

	public List<String> getUpgradeHistRequestList_PositionType() {
		return upgradeHistRequestList_PositionType;
	}

	public void setUpgradeHistRequestList_PositionType(List<String> upgradeHistRequestList_PositionType) {
		this.upgradeHistRequestList_PositionType = upgradeHistRequestList_PositionType;
	}

	public List<String> getExtensionHistRequestList_RequestID() {
		return extensionHistRequestList_RequestID;
	}

	public void setExtensionHistRequestList_RequestID(List<String> extensionHistRequestList_RequestID) {
		this.extensionHistRequestList_RequestID = extensionHistRequestList_RequestID;
	}

	public List<String> getExtensionHistRequestList_RequestTransDate() {
		return extensionHistRequestList_RequestTransDate;
	}

	public void setExtensionHistRequestList_RequestTransDate(List<String> extensionHistRequestList_RequestTransDate) {
		this.extensionHistRequestList_RequestTransDate = extensionHistRequestList_RequestTransDate;
	}

	public List<String> getExtensionHistRequestList_RequestOrgEndDate() {
		return extensionHistRequestList_RequestOrgEndDate;
	}

	public void setExtensionHistRequestList_RequestOrgEndDate(List<String> extensionHistRequestList_RequestOrgEndDate) {
		this.extensionHistRequestList_RequestOrgEndDate = extensionHistRequestList_RequestOrgEndDate;
	}

	public List<String> getExtensionHistRequestList_RequestRevisedEndDate() {
		return extensionHistRequestList_RequestRevisedEndDate;
	}

	public void setExtensionHistRequestList_RequestRevisedEndDate(
			List<String> extensionHistRequestList_RequestRevisedEndDate) {
		this.extensionHistRequestList_RequestRevisedEndDate = extensionHistRequestList_RequestRevisedEndDate;
	}

	public int getUpgradeListSize() {
		return upgradeListSize;
	}

	public void setUpgradeListSize(int upgradeListSize) {
		this.upgradeListSize = upgradeListSize;
	}

	public int getExtensionListSize() {
		return extensionListSize;
	}

	public void setExtensionListSize(int extensionListSize) {
		this.extensionListSize = extensionListSize;
	}

	public int getDeletionListSize() {
		return deletionListSize;
	}

	public void setDeletionListSize(int deletionListSize) {
		this.deletionListSize = deletionListSize;
	}

	public int getChangeFundingListSize() {
		return changeFundingListSize;
	}

	public void setChangeFundingListSize(int changeFundingListSize) {
		this.changeFundingListSize = changeFundingListSize;
	}

	public int getSuppPromotionListSize() {
		return suppPromotionListSize;
	}

	public void setSuppPromotionListSize(int suppPromotionListSize) {
		this.suppPromotionListSize = suppPromotionListSize;
	}

	public int getFrozenListSize() {
		return frozenListSize;
	}

	public void setFrozenListSize(int frozenListSize) {
		this.frozenListSize = frozenListSize;
	}

	public int getChangeStaffMixListSize() {
		return changeStaffMixListSize;
	}

	public void setChangeStaffMixListSize(int changeStaffMixListSize) {
		this.changeStaffMixListSize = changeStaffMixListSize;
	}

	public List<String> getDeletionHistRequestList_RequestID() {
		return deletionHistRequestList_RequestID;
	}

	public void setDeletionHistRequestList_RequestID(List<String> deletionHistRequestList_RequestID) {
		this.deletionHistRequestList_RequestID = deletionHistRequestList_RequestID;
	}

	public List<String> getDeletionHistRequestList_RequestTransDate() {
		return deletionHistRequestList_RequestTransDate;
	}

	public void setDeletionHistRequestList_RequestTransDate(List<String> deletionHistRequestList_RequestTransDate) {
		this.deletionHistRequestList_RequestTransDate = deletionHistRequestList_RequestTransDate;
	}

	public List<String> getDeletionHistRequestList_RequestEffDate() {
		return deletionHistRequestList_RequestEffDate;
	}

	public void setDeletionHistRequestList_RequestEffDate(List<String> deletionHistRequestList_RequestEffDate) {
		this.deletionHistRequestList_RequestEffDate = deletionHistRequestList_RequestEffDate;
	}

	public List<String> getDeletionHistRequestList_RequestReason() {
		return deletionHistRequestList_RequestReason;
	}

	public void setDeletionHistRequestList_RequestReason(List<String> deletionHistRequestList_RequestReason) {
		this.deletionHistRequestList_RequestReason = deletionHistRequestList_RequestReason;
	}

	public List<String> getChangeFundHistRequestList_RequestID() {
		return changeFundHistRequestList_RequestID;
	}

	public void setChangeFundHistRequestList_RequestID(List<String> changeFundHistRequestList_RequestID) {
		this.changeFundHistRequestList_RequestID = changeFundHistRequestList_RequestID;
	}

	public List<String> getChangeFundHistRequestList_RequestTransDate() {
		return changeFundHistRequestList_RequestTransDate;
	}

	public void setChangeFundHistRequestList_RequestTransDate(List<String> changeFundHistRequestList_RequestTransDate) {
		this.changeFundHistRequestList_RequestTransDate = changeFundHistRequestList_RequestTransDate;
	}

	public List<String> getChangeFundHistRequestList_RequestReason() {
		return changeFundHistRequestList_RequestReason;
	}

	public void setChangeFundHistRequestList_RequestReason(List<String> changeFundHistRequestList_RequestReason) {
		this.changeFundHistRequestList_RequestReason = changeFundHistRequestList_RequestReason;
	}

	public List<String> getSuppPromoHistRequestList_RequestID() {
		return suppPromoHistRequestList_RequestID;
	}

	public void setSuppPromoHistRequestList_RequestID(List<String> suppPromoHistRequestList_RequestID) {
		this.suppPromoHistRequestList_RequestID = suppPromoHistRequestList_RequestID;
	}

	public List<String> getSuppPromoHistRequestList_RequestTransDate() {
		return suppPromoHistRequestList_RequestTransDate;
	}

	public void setSuppPromoHistRequestList_RequestTransDate(List<String> suppPromoHistRequestList_RequestTransDate) {
		this.suppPromoHistRequestList_RequestTransDate = suppPromoHistRequestList_RequestTransDate;
	}

	public List<String> getSuppPromoHistRequestList_RequestEffDate() {
		return suppPromoHistRequestList_RequestEffDate;
	}

	public void setSuppPromoHistRequestList_RequestEffDate(List<String> suppPromoHistRequestList_RequestEffDate) {
		this.suppPromoHistRequestList_RequestEffDate = suppPromoHistRequestList_RequestEffDate;
	}

	public List<String> getSuppPromoHistRequestList_RequestRemark() {
		return suppPromoHistRequestList_RequestRemark;
	}

	public void setSuppPromoHistRequestList_RequestRemark(List<String> suppPromoHistRequestList_RequestRemark) {
		this.suppPromoHistRequestList_RequestRemark = suppPromoHistRequestList_RequestRemark;
	}

	public List<String> getFrozenHistRequestList_RequestID() {
		return frozenHistRequestList_RequestID;
	}

	public void setFrozenHistRequestList_RequestID(List<String> frozenHistRequestList_RequestID) {
		this.frozenHistRequestList_RequestID = frozenHistRequestList_RequestID;
	}

	public List<String> getFrozenHistRequestList_RequestTransDate() {
		return frozenHistRequestList_RequestTransDate;
	}

	public void setFrozenHistRequestList_RequestTransDate(List<String> frozenHistRequestList_RequestTransDate) {
		this.frozenHistRequestList_RequestTransDate = frozenHistRequestList_RequestTransDate;
	}

	public List<String> getFrozenHistRequestList_RequestEffDate() {
		return frozenHistRequestList_RequestEffDate;
	}

	public void setFrozenHistRequestList_RequestEffDate(List<String> frozenHistRequestList_RequestEffDate) {
		this.frozenHistRequestList_RequestEffDate = frozenHistRequestList_RequestEffDate;
	}

	public List<String> getFrozenHistRequestList_RequestFrozenEndDate() {
		return frozenHistRequestList_RequestFrozenEndDate;
	}

	public void setFrozenHistRequestList_RequestFrozenEndDate(List<String> frozenHistRequestList_RequestFrozenEndDate) {
		this.frozenHistRequestList_RequestFrozenEndDate = frozenHistRequestList_RequestFrozenEndDate;
	}

	public List<String> getFrozenHistRequestList_RequestReason() {
		return frozenHistRequestList_RequestReason;
	}

	public void setFrozenHistRequestList_RequestReason(List<String> frozenHistRequestList_RequestReason) {
		this.frozenHistRequestList_RequestReason = frozenHistRequestList_RequestReason;
	}

	public List<String> getStaffMixHistRequestList_RequestID() {
		return staffMixHistRequestList_RequestID;
	}

	public void setStaffMixHistRequestList_RequestID(List<String> staffMixHistRequestList_RequestID) {
		this.staffMixHistRequestList_RequestID = staffMixHistRequestList_RequestID;
	}

	public List<String> getStaffMixHistRequestList_RequestEffDate() {
		return staffMixHistRequestList_RequestEffDate;
	}

	public void setStaffMixHistRequestList_RequestEffDate(List<String> staffMixHistRequestList_RequestEffDate) {
		this.staffMixHistRequestList_RequestEffDate = staffMixHistRequestList_RequestEffDate;
	}

	public List<String> getStaffMixHistRequestList_RequestFromToInd() {
		return staffMixHistRequestList_RequestFromToInd;
	}

	public void setStaffMixHistRequestList_RequestFromToInd(List<String> staffMixHistRequestList_RequestFromToInd) {
		this.staffMixHistRequestList_RequestFromToInd = staffMixHistRequestList_RequestFromToInd;
	}

	public List<String> getStaffMixHistRequestList_RequestPostID() {
		return staffMixHistRequestList_RequestPostID;
	}

	public void setStaffMixHistRequestList_RequestPostID(List<String> staffMixHistRequestList_RequestPostID) {
		this.staffMixHistRequestList_RequestPostID = staffMixHistRequestList_RequestPostID;
	}

	public List<String> getStaffMixHistRequestList_RequestDept() {
		return staffMixHistRequestList_RequestDept;
	}

	public void setStaffMixHistRequestList_RequestDept(List<String> staffMixHistRequestList_RequestDept) {
		this.staffMixHistRequestList_RequestDept = staffMixHistRequestList_RequestDept;
	}

	public List<String> getStaffMixHistRequestList_RequestRank() {
		return staffMixHistRequestList_RequestRank;
	}

	public void setStaffMixHistRequestList_RequestRank(List<String> staffMixHistRequestList_RequestRank) {
		this.staffMixHistRequestList_RequestRank = staffMixHistRequestList_RequestRank;
	}

	public List<String> getStaffMixHistRequestList_RequestPositiontDurationType() {
		return staffMixHistRequestList_RequestPositiontDurationType;
	}

	public void setStaffMixHistRequestList_RequestPositiontDurationType(
			List<String> staffMixHistRequestList_RequestPositiontDurationType) {
		this.staffMixHistRequestList_RequestPositiontDurationType = staffMixHistRequestList_RequestPositiontDurationType;
	}

	public List<String> getStaffMixHistRequestList_PositionType() {
		return staffMixHistRequestList_PositionType;
	}

	public void setStaffMixHistRequestList_PositionType(List<String> staffMixHistRequestList_PositionType) {
		this.staffMixHistRequestList_PositionType = staffMixHistRequestList_PositionType;
	}

	

	public List<ReportingPo> getReportList() {
		return reportList;
	}

	public void setReportList(List<ReportingPo> reportList) {
		this.reportList = reportList;
	}

	public int getAssignmentListSize() {
		return assignmentListSize;
	}

	public void setAssignmentListSize(int assignmentListSize) {
		this.assignmentListSize = assignmentListSize;
	}

	public List<String> getAssignmentHistList_EmpName() {
		return assignmentHistList_EmpName;
	}

	public void setAssignmentHistList_EmpName(List<String> assignmentHistList_EmpName) {
		this.assignmentHistList_EmpName = assignmentHistList_EmpName;
	}

	public List<String> getAssignmentHistList_Rank() {
		return assignmentHistList_Rank;
	}

	public void setAssignmentHistList_Rank(List<String> assignmentHistList_Rank) {
		this.assignmentHistList_Rank = assignmentHistList_Rank;
	}

	public List<String> getAssignmentHistList_EmpType() {
		return assignmentHistList_EmpType;
	}

	public void setAssignmentHistList_EmpType(List<String> assignmentHistList_EmpType) {
		this.assignmentHistList_EmpType = assignmentHistList_EmpType;
	}

	public List<String> getAssignmentHistList_EmpPositionTitle() {
		return assignmentHistList_EmpPositionTitle;
	}

	public void setAssignmentHistList_EmpPositionTitle(List<String> assignmentHistList_EmpPositionTitle) {
		this.assignmentHistList_EmpPositionTitle = assignmentHistList_EmpPositionTitle;
	}

	public List<String> getAssignmentHistList_FTE() {
		return assignmentHistList_FTE;
	}

	public void setAssignmentHistList_FTE(List<String> assignmentHistList_FTE) {
		this.assignmentHistList_FTE = assignmentHistList_FTE;
	}

	public List<String> getAssignmentHistList_StartDate() {
		return assignmentHistList_StartDate;
	}

	public void setAssignmentHistList_StartDate(List<String> assignmentHistList_StartDate) {
		this.assignmentHistList_StartDate = assignmentHistList_StartDate;
	}

	public List<String> getAssignmentHistList_EndDate() {
		return assignmentHistList_EndDate;
	}

	public void setAssignmentHistList_EndDate(List<String> assignmentHistList_EndDate) {
		this.assignmentHistList_EndDate = assignmentHistList_EndDate;
	}

	public List<String> getAssignmentHistList_LeaveReason() {
		return assignmentHistList_LeaveReason;
	}

	public void setAssignmentHistList_LeaveReason(List<String> assignmentHistList_LeaveReason) {
		this.assignmentHistList_LeaveReason = assignmentHistList_LeaveReason;
	}

	public List<String> getAssignmentHistList_PositionID() {
		return assignmentHistList_PositionID;
	}

	public void setAssignmentHistList_PositionID(List<String> assignmentHistList_PositionID) {
		this.assignmentHistList_PositionID = assignmentHistList_PositionID;
	}

	public List<InstitutionPo> getInstList() {
		return instList;
	}

	public void setInstList(List<InstitutionPo> instList) {
		this.instList = instList;
	}

	public List<String> getUserName() {
		return userName;
	}

	public void setUserName(List<String> userName) {
		this.userName = userName;
	}

	public List<String> getUserId() {
		return userId;
	}

	public void setUserId(List<String> userId) {
		this.userId = userId;
	}

	public EmailTemplateVo getEmailTemplate() {
		return emailTemplate;
	}

	public void setEmailTemplate(EmailTemplateVo emailTemplate) {
		this.emailTemplate = emailTemplate;
	}

	public String getToEmailList() {
		return toEmailList;
	}

	public void setToEmailList(String toEmailList) {
		this.toEmailList = toEmailList;
	}

	public String getRequestUid() {
		return requestUid;
	}

	public void setRequestUid(String requestUid) {
		this.requestUid = requestUid;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getDefaultRole() {
		return defaultRole;
	}

	public void setDefaultRole(String defaultRole) {
		this.defaultRole = defaultRole;
	}

	public String getNextActionName() {
		return nextActionName;
	}

	public void setNextActionName(String nextActionName) {
		this.nextActionName = nextActionName;
	}

	public List<ClusterPo> getClusterList() {
		return clusterList;
	}

	public void setClusterList(List<ClusterPo> clusterList) {
		this.clusterList = clusterList;
	}

	public List<StaffGroupPo> getStaffGroupList() {
		return staffGroupList;
	}

	public void setStaffGroupList(List<StaffGroupPo> staffGroupList) {
		this.staffGroupList = staffGroupList;
	}

	public List<RankVo> getRankList() {
		return rankList;
	}

	public void setRankList(List<RankVo> rankList) {
		this.rankList = rankList;
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

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getHcmPositionId() {
		return hcmPositionId;
	}

	public void setHcmPositionId(String hcmPositionId) {
		this.hcmPositionId = hcmPositionId;
	}

	public String getCcEmailList() {
		return ccEmailList;
	}

	public void setCcEmailList(String ccEmailList) {
		this.ccEmailList = ccEmailList;
	}

	public List<DepartmentPo> getDeptList() {
		return deptList;
	}

	public void setDeptList(List<DepartmentPo> deptList) {
		List<DepartmentPo> tmpList = new ArrayList<DepartmentPo>();
		
		if (deptList != null) {
			for (int i=0; i<deptList.size(); i++) {
				DepartmentPo tmp = new DepartmentPo();
				tmp.setDeptCode(StrHelper.format(deptList.get(i).getDeptCode()));
				tmp.setDeptName(StrHelper.format(deptList.get(i).getDeptName()));
				
				tmpList.add(tmp);
			}
		}
		
		this.deptList = tmpList;
	}

	public List<String> getAssignmentHistList_AssignmentNumber() {
		return assignmentHistList_AssignmentNumber;
	}

	public void setAssignmentHistList_AssignmentNumber(List<String> assignmentHistList_AssignmentNumber) {
		this.assignmentHistList_AssignmentNumber = assignmentHistList_AssignmentNumber;
	}
	
	public String getRelatedHcmEffectiveStartDate() {
		return relatedHcmEffectiveStartDate;
	}

	public void setRelatedHcmEffectiveStartDate(String relatedHcmEffectiveStartDate) {
		this.relatedHcmEffectiveStartDate = relatedHcmEffectiveStartDate;
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

	public String getSubSpecialty() {
		return subSpecialty;
	}

	public void setSubSpecialty(String subSpecialty) {
		this.subSpecialty = subSpecialty;
	}

	public List<String> getUpgradeHistRequestList_RequestUid() {
		return upgradeHistRequestList_RequestUid;
	}

	public void setUpgradeHistRequestList_RequestUid(List<String> upgradeHistRequestList_RequestUid) {
		this.upgradeHistRequestList_RequestUid = upgradeHistRequestList_RequestUid;
	}

	public List<String> getExtensionHistRequestList_RequestUid() {
		return extensionHistRequestList_RequestUid;
	}

	public void setExtensionHistRequestList_RequestUid(List<String> extensionHistRequestList_RequestUid) {
		this.extensionHistRequestList_RequestUid = extensionHistRequestList_RequestUid;
	}

	public List<String> getDeletionHistRequestList_RequestUid() {
		return deletionHistRequestList_RequestUid;
	}

	public void setDeletionHistRequestList_RequestUid(List<String> deletionHistRequestList_RequestUid) {
		this.deletionHistRequestList_RequestUid = deletionHistRequestList_RequestUid;
	}

	public List<String> getChangeFundHistRequestList_RequestUid() {
		return changeFundHistRequestList_RequestUid;
	}

	public void setChangeFundHistRequestList_RequestUid(List<String> changeFundHistRequestList_RequestUid) {
		this.changeFundHistRequestList_RequestUid = changeFundHistRequestList_RequestUid;
	}

	public List<String> getSuppPromoHistRequestList_RequestUid() {
		return suppPromoHistRequestList_RequestUid;
	}

	public void setSuppPromoHistRequestList_RequestUid(List<String> suppPromoHistRequestList_RequestUid) {
		this.suppPromoHistRequestList_RequestUid = suppPromoHistRequestList_RequestUid;
	}

	public List<String> getFrozenHistRequestList_RequestUid() {
		return frozenHistRequestList_RequestUid;
	}

	public void setFrozenHistRequestList_RequestUid(List<String> frozenHistRequestList_RequestUid) {
		this.frozenHistRequestList_RequestUid = frozenHistRequestList_RequestUid;
	}

	public List<String> getStaffMixHistRequestList_RequestUid() {
		return staffMixHistRequestList_RequestUid;
	}

	public void setStaffMixHistRequestList_RequestUid(List<String> staffMixHistRequestList_RequestUid) {
		this.staffMixHistRequestList_RequestUid = staffMixHistRequestList_RequestUid;
	}

	public List<String> getAttachmentNameList() {
		return attachmentNameList;
	}

	public void setAttachmentNameList(List<String> attachmentNameList) {
		this.attachmentNameList = attachmentNameList;
	}

	public List<String> getAttachmentUidList() {
		return attachmentUidList;
	}

	public void setAttachmentUidList(List<String> attachmentUidList) {
		this.attachmentUidList = attachmentUidList;
	}

	public List<String> getAttachmentCreatedByList() {
		return attachmentCreatedByList;
	}

	public void setAttachmentCreatedByList(List<String> attachmentCreatedByList) {
		this.attachmentCreatedByList = attachmentCreatedByList;
	}

	public List<String> getAttachmentCreatedDateList() {
		return attachmentCreatedDateList;
	}

	public void setAttachmentCreatedDateList(List<String> attachmentCreatedDateList) {
		this.attachmentCreatedDateList = attachmentCreatedDateList;
	}

	public int getAttachmentListSize() {
		return attachmentListSize;
	}

	public void setAttachmentListSize(int attachmentListSize) {
		this.attachmentListSize = attachmentListSize;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public List<String> getExtensionHistRequestList_RequestOrgStartDate() {
		return extensionHistRequestList_RequestOrgStartDate;
	}

	public void setExtensionHistRequestList_RequestOrgStartDate(
			List<String> extensionHistRequestList_RequestOrgStartDate) {
		this.extensionHistRequestList_RequestOrgStartDate = extensionHistRequestList_RequestOrgStartDate;
	}

	public List<String> getExtensionHistRequestList_RequestRevisedStartDate() {
		return extensionHistRequestList_RequestRevisedStartDate;
	}

	public void setExtensionHistRequestList_RequestRevisedStartDate(
			List<String> extensionHistRequestList_RequestRevisedStartDate) {
		this.extensionHistRequestList_RequestRevisedStartDate = extensionHistRequestList_RequestRevisedStartDate;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<String> getStaffMixHistRequestList_RequestPostID2() {
		return staffMixHistRequestList_RequestPostID2;
	}

	public void setStaffMixHistRequestList_RequestPostID2(List<String> staffMixHistRequestList_RequestPostID2) {
		this.staffMixHistRequestList_RequestPostID2 = staffMixHistRequestList_RequestPostID2;
	}

	public List<RoleVo> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<RoleVo> roleList) {
		this.roleList = roleList;
	}
	
	public List<String> getStaffMixHistRequestList_Reason() {
		return staffMixHistRequestList_Reason;
	}

	public void setStaffMixHistRequestList_Reason(List<String> staffMixHistRequestList_Reason) {
		this.staffMixHistRequestList_Reason = staffMixHistRequestList_Reason;
	}

	public List<String> getUpgradeHistRequestList_Reason() {
		return upgradeHistRequestList_Reason;
	}

	public void setUpgradeHistRequestList_Reason(List<String> upgradeHistRequestList_Reason) {
		this.upgradeHistRequestList_Reason = upgradeHistRequestList_Reason;
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

	public List<ProgramTypePo> getProgramTypeList() {
		return programTypeList;
	}

	public void setProgramTypeList(List<ProgramTypePo> programTypeList) {
		this.programTypeList = programTypeList;
	}

	public List<PostFundingVo> getFundingList() {
		return fundingList;
	}

	public void setFundingList(List<PostFundingVo> fundingList) {
		this.fundingList = fundingList;
	}

	public List<SubSpecialtyPo> getSubSpecailtyList() {
		return subSpecailtyList;
	}

	public void setSubSpecailtyList(List<SubSpecialtyPo> subSpecailtyList) {
		this.subSpecailtyList = subSpecailtyList;
	}

	public List<String> getFteAdjustmentHistRequestList_RequestID() {
		return fteAdjustmentHistRequestList_RequestID;
	}

	public void setFteAdjustmentHistRequestList_RequestID(List<String> fteAdjustmentHistRequestList_RequestID) {
		this.fteAdjustmentHistRequestList_RequestID = fteAdjustmentHistRequestList_RequestID;
	}

	public List<String> getFteAdjustmentHistRequestList_RequestTransDate() {
		return fteAdjustmentHistRequestList_RequestTransDate;
	}

	public void setFteAdjustmentHistRequestList_RequestTransDate(
			List<String> fteAdjustmentHistRequestList_RequestTransDate) {
		this.fteAdjustmentHistRequestList_RequestTransDate = fteAdjustmentHistRequestList_RequestTransDate;
	}

	public List<String> getFteAdjustmentHistRequestList_RequestReason() {
		return fteAdjustmentHistRequestList_RequestReason;
	}

	public void setFteAdjustmentHistRequestList_RequestReason(List<String> fteAdjustmentHistRequestList_RequestReason) {
		this.fteAdjustmentHistRequestList_RequestReason = fteAdjustmentHistRequestList_RequestReason;
	}

	public int getFteAdjustmentListSize() {
		return fteAdjustmentListSize;
	}

	public void setFteAdjustmentListSize(int fteAdjustmentListSize) {
		this.fteAdjustmentListSize = fteAdjustmentListSize;
	}

	public List<String> getFteAdjustmentHistRequestList_RequestUid() {
		return fteAdjustmentHistRequestList_RequestUid;
	}

	public void setFteAdjustmentHistRequestList_RequestUid(List<String> fteAdjustmentHistRequestList_RequestUid) {
		this.fteAdjustmentHistRequestList_RequestUid = fteAdjustmentHistRequestList_RequestUid;
	}


}

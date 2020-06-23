package hk.org.ha.eclaim.model.hcm;

import java.util.ArrayList;
import java.util.List;

import hk.org.ha.eclaim.bs.hcm.po.HCMGradePo;

public class CreateHCMApproverWebVo {
	// For searching use
	private String searchClusterId;
	private String searchInstId;
	private String searchDeptId;
	private String searchStaffGroupId;
	private String searchRankId;
	private String searchPostId;
	private String searchEmployeeId;
	
	private String hcmEffectiveDate;
	
	private String ddGrade;
	private String ddHcmJob;
	private String ddHcmPostTitle;
	private String ddHcmPostOrganization;
	private String ddHcmOrganization;
	
	private String effectiveFromDate;
	private String startDate;
	private String type;
	private String postName;
	
	private String job;
	private String postTitle;
	private String postOrganization;
	private String organization;
	private String unitTeam;
	
	private String ddJob;
	private String ddPostTitle;
	private String ddPostOrganization;
	private String ddOrganization;
	private String ddLocation;
	
	private String hiringStatus;
	private String location;
	private String fte;
	private String headCount;
	private String probationDuration;
	private String probationDurationUnit;
	
	private String updateSuccess;
	private String displayMessage;
	private String fromCreateHCMPage;
	
	private String userName;
	private String proposedEndDate;
	
	private String formAction;
	private String positionId;
	
	private String positionGroup;
	private String srcFunding;
	
	private String versionNo;
	
	private List<String> grade = new ArrayList<String>();
	private List<String> gradeDateFrom = new ArrayList<String>();
	private List<String> gradeDateTo = new ArrayList<String>();
	
	private List<HCMGradePo> gradeListAll = new ArrayList<HCMGradePo>(); 
	
	private List<HCMRecordGradeVo> gradeList = new ArrayList<HCMRecordGradeVo>();
	
	private String updatedFTE;
	private String updatedFTEReason;
	
	private String repsonsibilityId;
	private String userId;
	
	private String selectedPostId;
	private String selectedFTE;
	private String selectedMPRSPostId;
	
	private String effectiveFromDisplay;
	private String effectiveToDisplay;
	private String hiringStatusStartDate;
	
	private String hiddenPostName;
	
	public String getEffectiveFromDate() {
		return effectiveFromDate;
	}

	public void setEffectiveFromDate(String effectiveFromDate) {
		this.effectiveFromDate = effectiveFromDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHiringStatus() {
		return hiringStatus;
	}

	public void setHiringStatus(String hiringStatus) {
		this.hiringStatus = hiringStatus;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getFte() {
		return fte;
	}

	public void setFte(String fte) {
		this.fte = fte;
	}

	public String getHeadCount() {
		return headCount;
	}

	public void setHeadCount(String headCount) {
		this.headCount = headCount;
	}

	public String getProbationDuration() {
		return probationDuration;
	}

	public void setProbationDuration(String probationDuration) {
		this.probationDuration = probationDuration;
	}

	public String getProbationDurationUnit() {
		return probationDurationUnit;
	}

	public void setProbationDurationUnit(String probationDurationUnit) {
		this.probationDurationUnit = probationDurationUnit;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getUnitTeam() {
		return unitTeam;
	}

	public void setUnitTeam(String unitTeam) {
		this.unitTeam = unitTeam;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getPostOrganization() {
		return postOrganization;
	}

	public void setPostOrganization(String postOrganization) {
		this.postOrganization = postOrganization;
	}

	public String getPostTitle() {
		return postTitle;
	}

	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}

	public String getUpdateSuccess() {
		return updateSuccess;
	}

	public void setUpdateSuccess(String updateSuccess) {
		this.updateSuccess = updateSuccess;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFormAction() {
		return formAction;
	}

	public void setFormAction(String formAction) {
		this.formAction = formAction;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public String getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

	public String getDisplayMessage() {
		return displayMessage;
	}

	public void setDisplayMessage(String displayMessage) {
		this.displayMessage = displayMessage;
	}

	public String getFromCreateHCMPage() {
		return fromCreateHCMPage;
	}

	public void setFromCreateHCMPage(String fromCreateHCMPage) {
		this.fromCreateHCMPage = fromCreateHCMPage;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public String getProposedEndDate() {
		return proposedEndDate;
	}

	public void setProposedEndDate(String proposedEndDate) {
		this.proposedEndDate = proposedEndDate;
	}

	public List<HCMRecordGradeVo> getGradeList() {
		return gradeList;
	}

	public void setGradeList(List<HCMRecordGradeVo> gradeList) {
		this.gradeList = gradeList;
	}
	
	public int getGradeListSize() {
		if (this.gradeList == null)
			return 0;
		
		return gradeList.size();
	}

	public List<String> getGrade() {
		return grade;
	}

	public void setGrade(List<String> grade) {
		this.grade = grade;
	}

	public List<String> getGradeDateFrom() {
		return gradeDateFrom;
	}

	public void setGradeDateFrom(List<String> gradeDateFrom) {
		this.gradeDateFrom = gradeDateFrom;
	}

	public List<String> getGradeDateTo() {
		return gradeDateTo;
	}

	public void setGradeDateTo(List<String> gradeDateTo) {
		this.gradeDateTo = gradeDateTo;
	}

	public String getPositionGroup() {
		return positionGroup;
	}

	public void setPositionGroup(String positionGroup) {
		this.positionGroup = positionGroup;
	}

	public String getSrcFunding() {
		return srcFunding;
	}

	public void setSrcFunding(String srcFunding) {
		this.srcFunding = srcFunding;
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

	public String getDdJob() {
		return ddJob;
	}

	public void setDdJob(String ddJob) {
		this.ddJob = ddJob;
	}

	public String getDdPostTitle() {
		return ddPostTitle;
	}

	public void setDdPostTitle(String ddPostTitle) {
		this.ddPostTitle = ddPostTitle;
	}

	public String getDdPostOrganization() {
		return ddPostOrganization;
	}

	public void setDdPostOrganization(String ddPostOrganization) {
		this.ddPostOrganization = ddPostOrganization;
	}

	public String getDdOrganization() {
		return ddOrganization;
	}

	public void setDdOrganization(String ddOrganization) {
		this.ddOrganization = ddOrganization;
	}

	public String getHcmEffectiveDate() {
		return hcmEffectiveDate;
	}

	public void setHcmEffectiveDate(String hcmEffectiveDate) {
		this.hcmEffectiveDate = hcmEffectiveDate;
	}

	public String getDdLocation() {
		return ddLocation;
	}

	public void setDdLocation(String ddLocation) {
		this.ddLocation = ddLocation;
	}

	public List<HCMGradePo> getGradeListAll() {
		return gradeListAll;
	}

	public void setGradeListAll(List<HCMGradePo> gradeListAll) {
		this.gradeListAll = gradeListAll;
	}

	public String getUpdatedFTE() {
		return updatedFTE;
	}

	public void setUpdatedFTE(String updatedFTE) {
		this.updatedFTE = updatedFTE;
	}

	public String getUpdatedFTEReason() {
		return updatedFTEReason;
	}

	public void setUpdatedFTEReason(String updatedFTEReason) {
		this.updatedFTEReason = updatedFTEReason;
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

	public String getSearchEmployeeId() {
		return searchEmployeeId;
	}

	public void setSearchEmployeeId(String searchEmployeeId) {
		this.searchEmployeeId = searchEmployeeId;
	}

	public String getRepsonsibilityId() {
		return repsonsibilityId;
	}

	public void setRepsonsibilityId(String repsonsibilityId) {
		this.repsonsibilityId = repsonsibilityId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDdGrade() {
		return ddGrade;
	}

	public void setDdGrade(String ddGrade) {
		this.ddGrade = ddGrade;
	}

	public String getSelectedPostId() {
		return selectedPostId;
	}

	public void setSelectedPostId(String selectedPostId) {
		this.selectedPostId = selectedPostId;
	}

	public String getSelectedFTE() {
		return selectedFTE;
	}

	public void setSelectedFTE(String selectedFTE) {
		this.selectedFTE = selectedFTE;
	}

	public String getSelectedMPRSPostId() {
		return selectedMPRSPostId;
	}

	public void setSelectedMPRSPostId(String selectedMPRSPostId) {
		this.selectedMPRSPostId = selectedMPRSPostId;
	}

	public String getEffectiveFromDisplay() {
		return effectiveFromDisplay;
	}

	public void setEffectiveFromDisplay(String effectiveFromDisplay) {
		this.effectiveFromDisplay = effectiveFromDisplay;
	}

	public String getEffectiveToDisplay() {
		return effectiveToDisplay;
	}

	public void setEffectiveToDisplay(String effectiveToDisplay) {
		this.effectiveToDisplay = effectiveToDisplay;
	}

	public String getHiringStatusStartDate() {
		return hiringStatusStartDate;
	}

	public void setHiringStatusStartDate(String hiringStatusStartDate) {
		this.hiringStatusStartDate = hiringStatusStartDate;
	}

	public String getHiddenPostName() {
		return hiddenPostName;
	}

	public void setHiddenPostName(String hiddenPostName) {
		this.hiddenPostName = hiddenPostName;
	}
}

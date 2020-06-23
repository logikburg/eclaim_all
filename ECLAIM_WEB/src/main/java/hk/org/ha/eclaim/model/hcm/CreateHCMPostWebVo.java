package hk.org.ha.eclaim.model.hcm;

import java.util.ArrayList;
import java.util.List;

public class CreateHCMPostWebVo {
	private String effectiveFromDate;
	private String startDate;
	private String type = "SHARED";
	
	private String hcmJob;
	private String hcmPostTitle;
	private String hcmPostOrganization;
	private String hcmOrganization;
	private String hcmUnitTeam;
	
	private String ddHcmJob;
	private String ddHcmPostTitle;
	private String ddHcmPostOrganization;
	private String ddHcmOrganization;
	private String ddLocation;
	private String ddGrade;
	
	private String hiringStatus = "Active";
	private String location;
	private String fte = "0";
	private String headCount = "1";
	private String probationDuration;
	private String probationDurationUnit;
	private String permanentFlag;
	
	private String proposedEndDate;
	
	private String updateSuccess;
	private String userName;
	
	private List<String> grade = new ArrayList<String>();
	private List<String> gradeDateFrom = new ArrayList<String>();
	private List<String> gradeDateTo = new ArrayList<String>();
	
	private String displayMessage;
	private String positionId;
	private String postName;
	
	private String positionGroup;
	private String srcFunding;
	private String repsonsibilityId;
	private String userId;
	
	private String hiddenPostName;
	
	private List<HCMRecordGradeVo> gradeList = new ArrayList<HCMRecordGradeVo>();
	
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

	public String getPermanentFlag() {
		return permanentFlag;
	}

	public void setPermanentFlag(String permanentFlag) {
		this.permanentFlag = permanentFlag;
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

	public String getProposedEndDate() {
		return proposedEndDate;
	}

	public void setProposedEndDate(String proposedEndDate) {
		this.proposedEndDate = proposedEndDate;
	}

	public String getDisplayMessage() {
		return displayMessage;
	}

	public void setDisplayMessage(String displayMessage) {
		this.displayMessage = displayMessage;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
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

	public String getDdLocation() {
		return ddLocation;
	}

	public void setDdLocation(String ddLocation) {
		this.ddLocation = ddLocation;
	}

	public String getDdGrade() {
		return ddGrade;
	}

	public void setDdGrade(String ddGrade) {
		this.ddGrade = ddGrade;
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

	public String getHiddenPostName() {
		return hiddenPostName;
	}

	public void setHiddenPostName(String hiddenPostName) {
		this.hiddenPostName = hiddenPostName;
	}
}

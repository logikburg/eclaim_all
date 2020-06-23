package hk.org.ha.eclaim.model.request;

import java.util.Date;

import hk.org.ha.eclaim.core.helper.DateTimeHelper;

public class RequestPostVo {
	private String requestPostId;
	private String postNo;
	private String postId;
	private String clusterCode;
	private String instCode;
	private String deptCode;
	private String staffGroupCode;
	private String rankCode;
	private String rankDesc;
	private String proposedPostId;
	private String fromPostInd;
	private String toPostInd;
	private String changeFundingInd;
	private String unit;
	private String postTitle;
	private String postDuration;
	private String limitDurationType;
	private Integer limitDurationNo;
	private String limitDurationUnit;
	private Date limitDurationEndDate;
	private String postRemark;
	private Date postStartDate;
	private String postFTEType;
	private String postFTE;
	private String postStatus;
	private Date postStatusStartDate;
	private Date postStatusEndDate;
	private String clusterRef;		
	private String clusterRemark;
	private String suppPromoInd;
	private String juniorPostId;
	private String hcmPositionId;
	private String subSpecialtyCode;
	
	private String annualPlanInd;
	private String programYear;
	private String programCode;
	private String programName;
	//private String programRemark;
	private String fundSrc1st;
	private Date funSrc1stStartDate ;
	private Date fundSrc1stEndDate;
	private Double fundSrc1stFte;
	private String fundRemark;
	private String programTypeCode;
	
//
//	@OneToOne(fetch = FetchType.EAGER, mappedBy="requestPosition", cascade = CascadeType.ALL)
//	private RequestFundingResourcePo requestFundingResource;
	
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
	
	private String resourcesSupportFrExt;
	private String resourcesSupportRemark;
	
	private String hoBuyServiceInd;

	public String getRequestPostId() {
		return requestPostId;
	}
	
	public void setRequestPostId(String requestPostId) {
		this.requestPostId = requestPostId;
	}
	
	public String getPostNo() {
		return postNo;
	}
	
	public void setPostNo(String postNo) {
		this.postNo = postNo;
	}
	
	public String getPostId() {
		return postId;
	}
	
	public void setPostId(String postId) {
		this.postId = postId;
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
	
	public String getRankDesc() {
		return rankDesc;
	}
	
	public void setRankDesc(String rankDesc) {
		this.rankDesc = rankDesc;
	}
	
	public String getProposedPostId() {
		return proposedPostId;
	}
	
	public void setProposedPostId(String proposedPostId) {
		this.proposedPostId = proposedPostId;
	}
	
	public String getFromPostInd() {
		return fromPostInd;
	}
	
	public void setFromPostInd(String fromPostInd) {
		this.fromPostInd = fromPostInd;
	}
	
	public String getToPostInd() {
		return toPostInd;
	}
	
	public void setToPostInd(String toPostInd) {
		this.toPostInd = toPostInd;
	}
	
	public String getChangeFundingInd() {
		return changeFundingInd;
	}
	
	public void setChangeFundingInd(String changeFundingInd) {
		this.changeFundingInd = changeFundingInd;
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
	
	public String getPostDuration() {
		return postDuration;
	}
	
	public void setPostDuration(String postDuration) {
		this.postDuration = postDuration;
	}
	
	public String getLimitDurationType() {
		return limitDurationType;
	}
	
	public void setLimitDurationType(String limitDurationType) {
		this.limitDurationType = limitDurationType;
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
	
	public Date getLimitDurationEndDate() {
		return limitDurationEndDate;
	}
	
	public String getLimitDurationEndDateStr() {
		if (limitDurationEndDate != null) {
			return DateTimeHelper.formatDateToString(limitDurationEndDate);
		}
		
		return "";
	}
	
	public void setLimitDurationEndDate(Date limitDurationEndDate) {
		this.limitDurationEndDate = limitDurationEndDate;
	}
	
	public String getPostRemark() {
		return postRemark;
	}
	
	public void setPostRemark(String postRemark) {
		this.postRemark = postRemark;
	}
	
	public Date getPostStartDate() {
		return postStartDate;
	}
	
	public String getPostStartDateStr() {
		if (postStartDate != null) {
			return DateTimeHelper.formatDateToString(postStartDate);
		}
		
		return "";
	}
	
	public void setPostStartDate(Date postStartDate) {
		this.postStartDate = postStartDate;
	}
	
	public String getPostFTEType() {
		return postFTEType;
	}
	
	public void setPostFTEType(String postFTEType) {
		this.postFTEType = postFTEType;
	}
	
	public String getPostFTE() {
		return postFTE;
	}
	
	public void setPostFTE(String postFTE) {
		this.postFTE = postFTE;
	}
	
	public String getPostStatus() {
		return postStatus;
	}
	
	public void setPostStatus(String postStatus) {
		this.postStatus = postStatus;
	}
	
	public Date getPostStatusStartDate() {
		return postStatusStartDate;
	}
	
	public String getPostStatusStartDateStr() {
		if (postStatusStartDate != null) {
			return DateTimeHelper.formatDateToString(postStatusStartDate);
		}
		
		return "";
	}
	
	public void setPostStatusStartDate(Date postStatusStartDate) {
		this.postStatusStartDate = postStatusStartDate;
	}
	
	public Date getPostStatusEndDate() {
		return postStatusEndDate;
	}
	
	public String getPostStatusEndDateStr() {
		if (postStatusEndDate != null) {
			return DateTimeHelper.formatDateToString(postStatusEndDate);
		}
		
		return "";
	}
	
	public void setPostStatusEndDate(Date postStatusEndDate) {
		this.postStatusEndDate = postStatusEndDate;
	}
	
	public String getClusterRef() {
		return clusterRef;
	}
	
	public void setClusterRef(String clusterRef) {
		this.clusterRef = clusterRef;
	}
	
	public String getClusterRemark() {
		return clusterRemark;
	}
	
	public void setClusterRemark(String clusterRemark) {
		this.clusterRemark = clusterRemark;
	}
	
	public String getSuppPromoInd() {
		return suppPromoInd;
	}
	
	public void setSuppPromoInd(String suppPromoInd) {
		this.suppPromoInd = suppPromoInd;
	}
	
	public String getJuniorPostId() {
		return juniorPostId;
	}
	
	public void setJuniorPostId(String juniorPostId) {
		this.juniorPostId = juniorPostId;
	}
	
	public String getHcmPositionId() {
		return hcmPositionId;
	}
	
	public void setHcmPositionId(String hcmPositionId) {
		this.hcmPositionId = hcmPositionId;
	}
	
	public String getSubSpecialtyCode() {
		return subSpecialtyCode;
	}
	
	public void setSubSpecialtyCode(String subSpecialtyCode) {
		this.subSpecialtyCode = subSpecialtyCode;
	}

	public String getRankCode() {
		return rankCode;
	}

	public void setRankCode(String rankCode) {
		this.rankCode = rankCode;
	}

	public String getResourcesSupportFrExt() {
		return resourcesSupportFrExt;
	}

	public void setResourcesSupportFrExt(String resourcesSupportFrExt) {
		this.resourcesSupportFrExt = resourcesSupportFrExt;
	}

	public String getResourcesSupportRemark() {
		return resourcesSupportRemark;
	}

	public void setResourcesSupportRemark(String resourcesSupportRemark) {
		this.resourcesSupportRemark = resourcesSupportRemark;
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

	/*public String getProgramRemark() {
		return programRemark;
	}

	public void setProgramRemark(String programRemark) {
		this.programRemark = programRemark;
	}
*/
	public String getFundSrc1st() {
		return fundSrc1st;
	}

	public void setFundSrc1st(String fundSrc1st) {
		this.fundSrc1st = fundSrc1st;
	}

	public Date getFunSrc1stStartDate() {
		return funSrc1stStartDate;
	}
	
	public String getFunSrc1stStartDateStr() {
		if (funSrc1stStartDate != null) {
			return DateTimeHelper.formatDateToString(funSrc1stStartDate);
		}
		
		return "";
	}

	public void setFunSrc1stStartDate(Date funSrc1stStartDate) {
		this.funSrc1stStartDate = funSrc1stStartDate;
	}

	public Date getFundSrc1stEndDate() {
		return fundSrc1stEndDate;
	}
	
	public String getFundSrc1stEndDateStr() {
		if (fundSrc1stEndDate != null) {
			return DateTimeHelper.formatDateToString(fundSrc1stEndDate);
		}
		
		return "";
	}

	public void setFundSrc1stEndDate(Date fundSrc1stEndDate) {
		this.fundSrc1stEndDate = fundSrc1stEndDate;
	}
	
	public Double getFundSrc1stFte() {
		return fundSrc1stFte;
	}

	public void setFundSrc1stFte(Double fundSrc1stFte) {
		this.fundSrc1stFte = fundSrc1stFte;
	}
	
	public String getFundRemark() {
		return fundRemark;
	}

	public void setFundRemark(String fundRemark) {
		this.fundRemark = fundRemark;
	}
	
	public String getProgramTypeCode() {
		return programTypeCode;
	}

	public void setProgramTypeCode(String programTypeCode) {
		this.programTypeCode = programTypeCode;
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

	public String getHoBuyServiceInd() {
		return hoBuyServiceInd;
	}

	public void setHoBuyServiceInd(String hoBuyServiceInd) {
		this.hoBuyServiceInd = hoBuyServiceInd;
	}

}

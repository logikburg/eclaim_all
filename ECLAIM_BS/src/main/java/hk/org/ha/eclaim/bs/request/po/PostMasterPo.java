package hk.org.ha.eclaim.bs.request.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import hk.org.ha.eclaim.core.po.AbstractBasePo;
import hk.org.ha.eclaim.bs.cs.po.MPRSPostStatusPo;

@Entity
@Table(name="POST_MASTER")
public class PostMasterPo extends AbstractBasePo {
	
	private static final long serialVersionUID = -7509553790129659785L;

	@Id
	@Column(name="POST_UID")
	private int postUid;
	
	@Column(name="POST_ID")
	private String postId;
	
	@Column(name="CLUSTER_CODE")
	private String clusterCode;
	
	@Column(name="INST_CODE")
	private String instCode;
	
	@Column(name="DEPT_CODE")
	private String deptCode;
	
	@Column(name="STAFF_GROUP_CODE")
	private String staffGroupCode;
	
	@Column(name="RANK_CODE")
	private String rankCode;

	@Column(name="UNIT")
	private String unit;

	@Column(name="POST_TITLE")
	private String postTitle;

	@Column(name="POST_DURATION")
	private String postDuration;

	@Column(name="LIMIT_DURATION_TYPE")
	private String limitDurationType;

	@Column(name="LIMIT_DURATION_NO")
	private Integer limitDurationNo;

	@Column(name="LIMIT_DURATION_UNIT")
	private String limitDurationUnit;

	@Temporal(TemporalType.DATE)
	@Column(name="LIMIT_DURATION_END_DATE")
	private Date limitDurationEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name="ACTUAL_START_DATE")
	private Date actualStartDate;
	
	@Column(name="POST_REMARK")
	private String postRemark;
	
	@Column(name="POST_START_DATE")
	private Date postStartDate;
	
	@Column(name="POST_FTE_TYPE")
	private String postFTEType;
	
	@Column(name="POST_FTE")
	private Double postFTE;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="POST_STATUS")
	private MPRSPostStatusPo postStatus;

	@Temporal(TemporalType.DATE)
	@Column(name="POST_STATUS_START_DATE")
	private Date postStatusStartDate;

	@Temporal(TemporalType.DATE)
	@Column(name="POST_STATUS_END_DATE")
	private Date postStatusEndDate;
	
	@Column(name="CLUSTER_REF")
	private String clusterRef;
	
	@Column(name="CLUSTER_REMARK")
	private String clusterRemark;
	
	@Column(name="SUPP_PROMO_IND")
	private String suppPromoInd;
	
	@Column(name="JUNIOR_POST_ID")
	private String juniorPostId;
	
	@Column(name="HCM_POSITION_ID")
	private String hcmPositionId;
	
	@Column(name="SUB_SPECIALTY_CODE")
	private String subSpecialtyCode;
	
	@Column(name="SUPP_PROMO_OCCUPY_IND")
	private String suppPromotionOccupyInd;

	@Column(name="ORI_HCM_POSITION_ID")
	private String oriHcmPositionId;
	
	@Column(name="SHORTFALL_IND")
	private String shortFallInd;
	
	@Column(name="SHORTFALL_POST_ID")
	private String shortFallPostId;
	
	@Column(name="HO_BUY_SERVICE_IND")
	private String hoBuyServiceInd;
	
	// @OneToOne(mappedBy="post", cascade=CascadeType.ALL)
	@Transient
	private MPRSPostFunding postFunding;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy="post", cascade = CascadeType.ALL)
	private List<MPRSPostFunding> postFundingList = new ArrayList<MPRSPostFunding>();
	
	@OneToOne(mappedBy="post", cascade=CascadeType.ALL)
	private MPRSPostFundingResource postFundingResource;
	
	@Transient
	private String employeeID;
	
	@Transient
	private String annualPlan;
	
	@Transient
	private String postStatusDesc;
	
	@Transient
	private String postSnapUid;
	
	@Transient
	private String extraInfo;
	
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

	public String getRankCode() {
		return rankCode;
	}

	public void setRankCode(String rankCode) {
		this.rankCode = rankCode;
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

	public void setLimitDurationEndDate(Date limitDurationEndDate) {
		this.limitDurationEndDate = limitDurationEndDate;
	}
	
	public Date getActualStartDate() {
		return actualStartDate;
	}

	public void setActualStartDate(Date actualStartDate) {
		this.actualStartDate = actualStartDate;
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

	public void setPostStartDate(Date postStartDate) {
		this.postStartDate = postStartDate;
	}

	public String getPostFTEType() {
		return postFTEType;
	}

	public void setPostFTEType(String postFTEType) {
		this.postFTEType = postFTEType;
	}

	public Double getPostFTE() {
		return postFTE;
	}

	public void setPostFTE(Double postFTE) {
		this.postFTE = postFTE;
	}

	public Date getPostStatusStartDate() {
		return postStatusStartDate;
	}

	public void setPostStatusStartDate(Date postStatusStartDate) {
		this.postStatusStartDate = postStatusStartDate;
	}

	public Date getPostStatusEndDate() {
		return postStatusEndDate;
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

	public void setPostStatus(MPRSPostStatusPo postStatus) {
		this.postStatus = postStatus;
	}
	
	public MPRSPostStatusPo getPostStatus() {
		return postStatus;
	}

	public MPRSPostFunding getPostFunding() {
		return postFunding;
	}

	public void setPostFunding(MPRSPostFunding postFunding) {
		this.postFunding = postFunding;
	}

	public MPRSPostFundingResource getPostFundingResource() {
		return postFundingResource;
	}

	public void setPostFundingResource(MPRSPostFundingResource postFundingResource) {
		this.postFundingResource = postFundingResource;
	}

	public int getPostUid() {
		return postUid;
	}

	public void setPostUid(int postUid) {
		this.postUid = postUid;
	}

	public String getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}

	public String getAnnualPlan() {
		return annualPlan;
	}

	public void setAnnualPlan(String annualPlan) {
		this.annualPlan = annualPlan;
	}

	public String getPostStatusDesc() {
		return postStatusDesc;
	}

	public void setPostStatusDesc(String postStatusDesc) {
		this.postStatusDesc = postStatusDesc;
	}

	public String getPostSnapUid() {
		return postSnapUid;
	}

	public void setPostSnapUid(String postSnapUid) {
		this.postSnapUid = postSnapUid;
	}

	public String getSuppPromotionOccupyInd() {
		return suppPromotionOccupyInd;
	}

	public void setSuppPromotionOccupyInd(String suppPromotionOccupyInd) {
		this.suppPromotionOccupyInd = suppPromotionOccupyInd;
	}

	public String getOriHcmPositionId() {
		return oriHcmPositionId;
	}

	public void setOriHcmPositionId(String oriHcmPositionId) {
		this.oriHcmPositionId = oriHcmPositionId;
	}
	public String getShortFallInd() {
		return shortFallInd;
	}

	public void setShortFallInd(String shortFallInd) {
		this.shortFallInd = shortFallInd;
	}

	public String getShortFallPostId() {
		return shortFallPostId;
	}

	public void setShortFallPostId(String shortFallPostId) {
		this.shortFallPostId = shortFallPostId;
	}

	public String getHoBuyServiceInd() {
		return hoBuyServiceInd;
	}

	public void setHoBuyServiceInd(String hoBuyServiceInd) {
		this.hoBuyServiceInd = hoBuyServiceInd;
	}

	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}

	public List<MPRSPostFunding> getPostFundingList() {
		return postFundingList;
	}

	public void setPostFundingList(List<MPRSPostFunding> postFundingList) {
		this.postFundingList = postFundingList;
	}
	
}

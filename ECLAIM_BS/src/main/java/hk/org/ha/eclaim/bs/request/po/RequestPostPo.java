package hk.org.ha.eclaim.bs.request.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import hk.org.ha.eclaim.core.po.AbstractBasePo;
import hk.org.ha.eclaim.bs.cs.po.RankPo;
import hk.org.ha.eclaim.core.helper.DateTimeHelper;

@Entity
@Table(name="RQ_POST")
public class RequestPostPo extends AbstractBasePo {
	
	private static final long serialVersionUID = -3211650087421481195L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RQ_POST_UID_SEQ")
	@SequenceGenerator(name="RQ_POST_UID_SEQ", sequenceName="RQ_POST_UID_SEQ", allocationSize=1)
	@Column(name="REQUEST_POST_UID")
	private int requestPostId;

	@ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="REQUEST_UID")
	private RequestPo request;

	@Column(name="POST_UID")
	private String postNo;

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

//	@Column(name="RANK_CODE")
//	private String rankCode;
	@ManyToOne
	@JoinColumn(name="RANK_CODE")
	private RankPo rank;

	@Column(name="PROPOSED_POST_ID")
	private String proposedPostId;
	
	@Column(name="FROM_POST_IND")
	private String fromPostInd;

	@Column(name="TO_POST_IND")
	private String toPostInd;

	@Column(name="CHG_FUNDING_IND")
	private String changeFundingInd;

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

	@Column(name="POST_REMARK")
	private String postRemark;

	@Temporal(TemporalType.DATE)
	@Column(name="POST_START_DATE")
	private Date postStartDate;

	@Column(name="POST_FTE_TYPE")
	private String postFTEType;

	@Column(name="POST_FTE")
	private String postFTE;

	@Column(name="POST_STATUS")
	private String postStatus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="POST_STATUS_START_DATE")
	private Date postStatusStartDate;

	@Temporal(TemporalType.TIMESTAMP)
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

	@Column(name="ORI_HCM_POSITION_ID")
	private String oriHcmPositionId;
	
	@Column(name="SHORTFALL_IND")
	private String shortFallInd;
	
	@Column(name="SHORTFALL_POST_ID")
	private String shortFallPostId;
	
	@Column(name="HO_BUY_SERVICE_IND")
	private String hoBuyServiceInd;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy="requestPosition", cascade = CascadeType.ALL)
	private List<RequestFundingPo> requestFundingList = new ArrayList<RequestFundingPo>();

	@OneToOne(fetch = FetchType.EAGER, mappedBy="requestPosition", cascade = CascadeType.ALL)
	private RequestFundingResourcePo requestFundingResource;
	
	@Transient
	private Date originalEndDate;
	
	@Transient
	private Double originalPostFTE;
	
	@Transient
	private String hcmPositionName;
	
	@Transient
	private String oriHcmPositionName;
	
	// Added for ST08733
	@Transient
	private String isExistingPost;
	
	@Transient
	private String annualPlanInd;

	public int getRequestPostId() {
		return requestPostId;
	}

	public void setRequestPostId(int requestPostId) {
		this.requestPostId = requestPostId;
	}

	public RequestPo getRequest() {
		return request;
	}

	public void setRequest(RequestPo request) {
		this.request = request;
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

//	public String getRankCode() {
//		return rankCode;
//	}
//
//	public void setRankCode(String rankCode) {
//		this.rankCode = rankCode;
//	}

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
		if (limitDurationEndDate == null) {
			return "";
		}
		
		return DateTimeHelper.formatDateToString(limitDurationEndDate);
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
		if (postStartDate == null) {
			return "";
		}
		
		return DateTimeHelper.formatDateToString(postStartDate);
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
		if (postStatusStartDate == null) {
			return "";
		}
		
		return DateTimeHelper.formatDateToString(postStatusStartDate);
	}

	public void setPostStatusStartDate(Date postStatusStartDate) {
		this.postStatusStartDate = postStatusStartDate;
	}

	public Date getPostStatusEndDate() {
		return postStatusEndDate;
	}
	
	public String getPostStatusEndDateStr() {
		if (postStatusEndDate == null) {
			return "";
		}
		
		return DateTimeHelper.formatDateToString(postStatusEndDate);
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

	public RequestFundingResourcePo getRequestFundingResource() {
		return requestFundingResource;
	}

	public void setRequestFundingResource(RequestFundingResourcePo requestFundingResource) {
		if (requestFundingResource != null) {
			requestFundingResource.setRequestPosition(this);
		}
		this.requestFundingResource = requestFundingResource;
	}

	public RankPo getRank() {
		return rank;
	}

	public void setRank(RankPo rank) {
		this.rank = rank;
	}

	public Date getOriginalEndDate() {
		return originalEndDate;
	}

	public void setOriginalEndDate(Date originalEndDate) {
		this.originalEndDate = originalEndDate;
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

	public String getHcmPositionName() {
		return hcmPositionName;
	}

	public void setHcmPositionName(String hcmPositionName) {
		this.hcmPositionName = hcmPositionName;
	}
	
	public String getOriHcmPositionName() {
		return oriHcmPositionName;
	}

	public void setOriHcmPositionName(String oriHcmPositionName) {
		this.oriHcmPositionName = oriHcmPositionName;
	}

	public String getIsExistingPost() {
		return isExistingPost;
	}

	public void setIsExistingPost(String isExistingPost) {
		this.isExistingPost = isExistingPost;
	}

	public List<RequestFundingPo> getRequestFundingList() {
		return requestFundingList;
	}

	public void setRequestFundingList(List<RequestFundingPo> requestFundingList) {
		this.requestFundingList = requestFundingList;
	}
	
	public void addRequestFundingList(RequestFundingPo requestFundingList) {
		if (this.requestFundingList == null) {
			this.requestFundingList = new ArrayList<RequestFundingPo>();
		}
		
		requestFundingList.setRequestPosition(this);
		this.requestFundingList.add(requestFundingList);
	}

	public String getAnnualPlanInd() {
		return annualPlanInd;
	}

	public void setAnnualPlanInd(String annualPlanInd) {
		this.annualPlanInd = annualPlanInd;
	}

	public Double getOriginalPostFTE() {
		return originalPostFTE;
	}

	public void setOriginalPostFTE(Double originalPostFTE) {
		this.originalPostFTE = originalPostFTE;
	}
}

package hk.org.ha.eclaim.bs.report.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "V_POST_EXPORT")
public class PostExportPo {

	@Column(name = "POST_EXPORT_UID")
	private Integer postExportUid;

	@Column(name = "POST_SNAP_UID")
	private Integer postSnapUid;

	@Column(name = "POST_UID")
	private Integer postUid;

	@Column(name = "REQUEST_POST_UID")
	private Integer requestPostUid;

	@Temporal(TemporalType.DATE)
	@Column(name = "EFFECTIVE_START_DATE")
	private Date effectiveStartDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "EFFECTIVE_END_DATE")
	private Date effectiveEndDate;

	@Id
	@Column(name = "POST_ID")
	private String postId;

	@Column(name = "HCM_POSITION_ID")
	private Integer hcmPositionId;

	@Column(name = "HCM_POSITION_NAME")
	private String hcmPositionName;

	@Column(name = "STAFF_GROUP_CODE")
	private String staffGroupCode;

	@Column(name = "STAFF_GROUP_NAME")
	private String staffGroupName;

	@Column(name = "CLUSTER_CODE")
	private String clusterCode;

	@Column(name = "CLUSTER_NAME")
	private String clusterName;

	@Column(name = "INST_CODE")
	private String instCode;

	@Column(name = "INST_NAME")
	private String instName;

	@Column(name = "DEPT_CODE")
	private String deptCode;
	
	@Column(name = "SUB_SPECIALTY_CODE")
	private String subSpecialtyCode;
	
	@Column(name = "SUB_SPECIALTY_DESC")
	private String subSpecialtyDesc;

	@Column(name = "UNIT")
	private String unit;

	@Column(name = "RANK_CODE")
	private String rankCode;

	@Column(name = "RANK_NAME")
	private String rankName;

	@Column(name = "POST_TITLE")
	private String postTitle;

	@Temporal(TemporalType.DATE)
	@Column(name = "APPROVAL_DATE")
	private Date approvalDate;

	@Column(name = "APPROVAL_REF")
	private String approvalRef;

	@Column(name = "APPROVAL_REMARK")
	private String approvalRemark;

	@Column(name = "POST_DURATION")
	private String postDuration;

	@Column(name = "POST_START_DATE")
	private Date postStartDate;

	@Column(name = "LIMIT_DURATION_NO")
	private Integer limitDurationNo;

	@Column(name = "LIMIT_DURATION_UNIT")
	private String limitDurationUnit;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "ACTUAL_START_DATE")
	private Date actualStartDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "ACTUAL_END_DATE")
	private Date actualEndDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "FIXED_END_DATE")
	private Date fixedEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "LIMIT_DURATION_END_DATE")
	private Date limitDurationEndDate;

	@Column(name = "EMPLOYEE_NUMBER")
	private String employeeNumber;

	@Column(name = "EMPLOYEE_NAME")
	private String employeeName;

	@Column(name = "EMPLOYEE_RANK")
	private String employeeRank;
	
	@Column(name = "EMPLOYEE_CATEGORY")
	private String employeeCategory;

	@Column(name = "POST_FTE_TYPE")
	private String postFTEType;

	@Column(name = "POST_FTE")
	private Double postFTE;

	@Column(name = "STRENGTH_FTE")
	private Double strengthFTE;

	@Column(name = "VACANCY_FTE")
	private Double vacancyFTE;

	@Column(name = "CLUSTER_REF")
	private String clusterRef;

	@Column(name = "CLUSTER_REMARK")
	private String clusterRemark;

	@Column(name = "POST_REMARK")
	private String postRemark;

	@Column(name = "POST_STATUS_DESC")
	private String postStatusDesc;

	@Temporal(TemporalType.DATE)
	@Column(name = "POST_STATUS_START_DATE")
	private Date postStatusStartDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "POST_STATUS_END_DATE")
	private Date postStatusEndDate;

	@Column(name = "SUPP_PROMO_IND")
	private String suppPromoInd;

	@Column(name = "JUNIOR_POST_ID")
	private String juniorPostId;

	@Column(name = "RESOURCES_SUPPORT_FR_EXT")
	private String resourcesSupportFrExt;

	@Column(name = "RESOURCES_SUPPORT_REMARK")
	private String resourcesSupportRemark;
	
	// Added for UT30067
	@Column(name = "REQUEST_TYPE")
	private String requestType;
	
	@Transient
	private String inst;

	@Transient
	private String section;
	
	@Transient
	private String analytical;
	
	@Transient
	private String payzone;

	public Integer getPostExportUid() {
		return postExportUid;
	}

	public void setPostExportUid(Integer postExportUid) {
		this.postExportUid = postExportUid;
	}

	public Integer getPostSnapUid() {
		return postSnapUid;
	}

	public void setPostSnapUid(Integer postSnapUid) {
		this.postSnapUid = postSnapUid;
	}

	public Integer getPostUid() {
		return postUid;
	}

	public void setPostUid(Integer postUid) {
		this.postUid = postUid;
	}

	public Integer getRequestPostUid() {
		return requestPostUid;
	}

	public void setRequestPostUid(Integer requestPostUid) {
		this.requestPostUid = requestPostUid;
	}

	public Date getEffectiveStartDate() {
		return effectiveStartDate;
	}

	public void setEffectiveStartDate(Date effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}

	public Date getEffectiveEndDate() {
		return effectiveEndDate;
	}

	public void setEffectiveEndDate(Date effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public Integer getHcmPositionId() {
		return hcmPositionId;
	}

	public void setHcmPositionId(Integer hcmPositionId) {
		this.hcmPositionId = hcmPositionId;
	}

	public String getHcmPositionName() {
		return hcmPositionName;
	}

	public void setHcmPositionName(String hcmPositionName) {
		this.hcmPositionName = hcmPositionName;
	}

	public String getStaffGroupCode() {
		return staffGroupCode;
	}

	public void setStaffGroupCode(String staffGroupCode) {
		this.staffGroupCode = staffGroupCode;
	}

	public String getStaffGroupName() {
		return staffGroupName;
	}

	public void setStaffGroupName(String staffGroupName) {
		this.staffGroupName = staffGroupName;
	}

	public String getClusterCode() {
		return clusterCode;
	}

	public void setClusterCode(String clusterCode) {
		this.clusterCode = clusterCode;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public String getInstCode() {
		return instCode;
	}

	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}

	public String getInstName() {
		return instName;
	}

	public void setInstName(String instName) {
		this.instName = instName;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getSubSpecialtyCode() {
		return subSpecialtyCode;
	}

	public void setSubSpecialtyCode(String subSpecialtyCode) {
		this.subSpecialtyCode = subSpecialtyCode;
	}

	public String getSubSpecialtyDesc() {
		return subSpecialtyDesc;
	}

	public void setSubSpecialtyDesc(String subSpecialtyDesc) {
		this.subSpecialtyDesc = subSpecialtyDesc;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getRankCode() {
		return rankCode;
	}

	public void setRankCode(String rankCode) {
		this.rankCode = rankCode;
	}

	public String getRankName() {
		return rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}

	public String getPostTitle() {
		return postTitle;
	}

	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getApprovalRef() {
		return approvalRef;
	}

	public void setApprovalRef(String approvalRef) {
		this.approvalRef = approvalRef;
	}

	public String getApprovalRemark() {
		return approvalRemark;
	}

	public void setApprovalRemark(String approvalRemark) {
		this.approvalRemark = approvalRemark;
	}

	public String getPostDuration() {
		return postDuration;
	}

	public void setPostDuration(String postDuration) {
		this.postDuration = postDuration;
	}

	public Date getPostStartDate() {
		return postStartDate;
	}

	public void setPostStartDate(Date postStartDate) {
		this.postStartDate = postStartDate;
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

	public Date getActualStartDate() {
		return actualStartDate;
	}

	public void setActualStartDate(Date actualStartDate) {
		this.actualStartDate = actualStartDate;
	}

	public Date getActualEndDate() {
		return actualEndDate;
	}

	public void setActualEndDate(Date actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	public Date getFixedEndDate() {
		return fixedEndDate;
	}

	public void setFixedEndDate(Date fixedEndDate) {
		this.fixedEndDate = fixedEndDate;
	}

	public Date getLimitDurationEndDate() {
		return limitDurationEndDate;
	}

	public void setLimitDurationEndDate(Date limitDurationEndDate) {
		this.limitDurationEndDate = limitDurationEndDate;
	}

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeRank() {
		return employeeRank;
	}

	public void setEmployeeRank(String employeeRank) {
		this.employeeRank = employeeRank;
	}
	
	public String getEmployeeCategory() {
		return employeeCategory;
	}

	public void setEmployeeCategory(String employeeCategory) {
		this.employeeCategory = employeeCategory;
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

	public Double getStrengthFTE() {
		return strengthFTE;
	}

	public void setStrengthFTE(Double strengthFTE) {
		this.strengthFTE = strengthFTE;
	}

	public Double getVacancyFTE() {
		return vacancyFTE;
	}

	public void setVacancyFTE(Double vacancyFTE) {
		this.vacancyFTE = vacancyFTE;
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

	public String getPostRemark() {
		return postRemark;
	}

	public void setPostRemark(String postRemark) {
		this.postRemark = postRemark;
	}

	public String getPostStatusDesc() {
		return postStatusDesc;
	}

	public void setPostStatusDesc(String postStatusDesc) {
		this.postStatusDesc = postStatusDesc;
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

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getPayzone() {
		return payzone;
	}

	public void setPayzone(String payzone) {
		this.payzone = payzone;
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

}

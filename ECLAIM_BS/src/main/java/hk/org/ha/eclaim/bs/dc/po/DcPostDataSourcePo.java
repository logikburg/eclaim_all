package hk.org.ha.eclaim.bs.dc.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "DC_POST_DATA_SOURCE")
public class DcPostDataSourcePo {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DC_POST_DATA_SOURCE_UID_SEQ")
	@SequenceGenerator(name = "DC_POST_DATA_SOURCE_UID_SEQ", sequenceName = "DC_POST_DATA_SOURCE_UID_SEQ", allocationSize = 1)
	@Column(name = "DC_POST_DATA_SOURCE_UID")
	private Integer dcPostDataSourceUID;

	@Temporal(TemporalType.DATE)
	@Column(name = "DC_DATE")
	private Date dcDate;

	@Column(name = "SOURCE_POST_ID")
	private String sourcePostId;

	@Column(name = "HCM_POSITION_NAME")
	private String hcmPositionName;

	@Column(name = "CLUSTER_CODE")
	private String clusterCode;

	@Column(name = "INST_CODE")
	private String instCode;

	@Column(name = "DEPT_NAME")
	private String deptName;

	@Column(name = "SUB_SPECIALTY_DESC")
	private String subSpecialtyDesc;

	@Column(name = "UNIT")
	private String unit;

	@Column(name = "RANK_CODE")
	private String rankCode;

	@Column(name = "POST_TITLE")
	private String postTitle;

	@Temporal(TemporalType.DATE)
	@Column(name = "APPROVAL_DATE")
	private Date approvalDate;

	@Column(name = "APPROVAL_REF")
	private String approvalRef;

	@Column(name = "APPROVAL_REMARK")
	private String approvalRemark;

	@Column(name = "POST_DURATION_DESC")
	private String postDurationDesc;

	@Temporal(TemporalType.DATE)
	@Column(name = "POST_START_DATE")
	private Date postStartDate;

	@Column(name = "LIMIT_DURATION_NO")
	private String limitDurationNo;

	@Column(name = "LIMIT_DURATION_UNIT_DESC")
	private String limitDurationUnitDesc;

	@Temporal(TemporalType.DATE)
	@Column(name = "LIMIT_DURATION_END_DATE")
	private Date limitDurationEndDate;

	@Column(name = "EMPLOYEE_NUMBER")
	private String employeeNumber;

	@Column(name = "EMPLOYEE_NAME")
	private String employeeName;

	@Column(name = "EMPLOYEE_RANK")
	private String employeeRank;

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

	@Column(name = "HELD_AGAINST_IND")
	private String heldAgainstInd;

	@Column(name = "HELD_AGAINST_POST_ID")
	private String heldAgainstPostId;

	@Column(name = "UPGRADE_TL_IND")
	private String upgradeTLInd;

	@Column(name = "JUNIOR_POST_ID")
	private String juniorPostId;

	@Column(name = "HO_BUY_SERVICE_IND")
	private String hoBuyServiceInd;

	@Column(name = "RESOURCES_SUPPORT_FR_EXT_DESC")
	private String resourcesSupportFrExtDesc;

	@Column(name = "RESOURCES_SUPPORT_REMARK")
	private String resourcesSupportRemark;

	// Funding Source 1

	@Column(name = "FUND_SRC_1ST_ANNUAL_PLAN_IND")
	private String fundSrc1stAnnualPlanInd;

	@Column(name = "FUND_SRC_1ST_PROGRAM_YEAR")
	private String fundSrc1stProgramYear;

	@Column(name = "FUND_SRC_1ST_PROGRAM_CODE")
	private String fundSrc1stProgramCode;

	@Column(name = "FUND_SRC_1ST_PROGRAM_NAME")
	private String fundSrc1stProgramName;

	@Column(name = "FUND_SRC_1ST_PROGRAM_TYPE")
	private String fundSrc1stProgramType;

	@Column(name = "FUND_SRC_1ST_DESC")
	private String fundSrc1stDesc;

	@Column(name = "FUND_SRC_1ST_SUB_CAT_DESC")
	private String fundSrc1stSubCatDesc;

	@Column(name = "FUND_SRC_1ST_START_DATE")
	private Date fundSrc1stStartDate;

	@Column(name = "FUND_SRC_1ST_END_DATE")
	private Date fundSrc1stEndDate;

	@Column(name = "FUND_SRC_1ST_FTE")
	private Double fundSrc1stFte;

	@Column(name = "FUND_SRC_1ST_INST")
	private String fundSrc1stInst;

	@Column(name = "FUND_SRC_1ST_SECTION")
	private String fundSrc1stSection;

	@Column(name = "FUND_SRC_1ST_ANALYTICAL")
	private String fundSrc1stAnalytical;

	@Column(name = "FUND_SRC_1ST_REMARK")
	private String fundSrc1stRemark;

	// Funding Source 2

	@Column(name = "FUND_SRC_2ND_ANNUAL_PLAN_IND")
	private String fundSrc2ndAnnualPlanInd;

	@Column(name = "FUND_SRC_2ND_PROGRAM_YEAR")
	private String fundSrc2ndProgramYear;

	@Column(name = "FUND_SRC_2ND_PROGRAM_CODE")
	private String fundSrc2ndProgramCode;

	@Column(name = "FUND_SRC_2ND_PROGRAM_NAME")
	private String fundSrc2ndProgramName;

	@Column(name = "FUND_SRC_2ND_PROGRAM_TYPE")
	private String fundSrc2ndProgramType;

	@Column(name = "FUND_SRC_2ND_DESC")
	private String fundSrc2ndDesc;

	@Column(name = "FUND_SRC_2ND_SUB_CAT_DESC")
	private String fundSrc2ndSubCatDesc;

	@Column(name = "FUND_SRC_2ND_START_DATE")
	private Date fundSrc2ndStartDate;

	@Column(name = "FUND_SRC_2ND_END_DATE")
	private Date fundSrc2ndEndDate;

	@Column(name = "FUND_SRC_2ND_FTE")
	private Double fundSrc2ndFte;

	@Column(name = "FUND_SRC_2ND_INST")
	private String fundSrc2ndInst;

	@Column(name = "FUND_SRC_2ND_SECTION")
	private String fundSrc2ndSection;

	@Column(name = "FUND_SRC_2ND_ANALYTICAL")
	private String fundSrc2ndAnalytical;

	@Column(name = "FUND_SRC_2ND_REMARK")
	private String fundSrc2ndRemark;

	// Funding Source 3rd

	@Column(name = "FUND_SRC_3RD_ANNUAL_PLAN_IND")
	private String fundSrc3rdAnnualPlanInd;

	@Column(name = "FUND_SRC_3RD_PROGRAM_YEAR")
	private String fundSrc3rdProgramYear;

	@Column(name = "FUND_SRC_3RD_PROGRAM_CODE")
	private String fundSrc3rdProgramCode;

	@Column(name = "FUND_SRC_3RD_PROGRAM_NAME")
	private String fundSrc3rdProgramName;

	@Column(name = "FUND_SRC_3RD_PROGRAM_TYPE")
	private String fundSrc3rdProgramType;

	@Column(name = "FUND_SRC_3RD_DESC")
	private String fundSrc3rdDesc;

	@Column(name = "FUND_SRC_3RD_SUB_CAT_DESC")
	private String fundSrc3rdSubCatDesc;

	@Column(name = "FUND_SRC_3RD_START_DATE")
	private Date fundSrc3rdStartDate;

	@Column(name = "FUND_SRC_3RD_END_DATE")
	private Date fundSrc3rdEndDate;

	@Column(name = "FUND_SRC_3RD_FTE")
	private Double fundSrc3rdFte;

	@Column(name = "FUND_SRC_3RD_INST")
	private String fundSrc3rdInst;

	@Column(name = "FUND_SRC_3RD_SECTION")
	private String fundSrc3rdSection;

	@Column(name = "FUND_SRC_3RD_ANALYTICAL")
	private String fundSrc3rdAnalytical;

	@Column(name = "FUND_SRC_3RD_REMARK")
	private String fundSrc3rdRemark;

	// MPRS

	@Column(name = "POST_ID")
	private String postId;

	@Column(name = "HCM_POSITION_ID")
	private String hcmPositionId;

	@Column(name = "STAFF_GROUP_CODE")
	private String staffGroupCode;

	@Column(name = "DEPT_CODE")
	private String deptCode;

	@Column(name = "SUB_SPECIALTY_CODE")
	private String subSpecialtyCode;

	@Column(name = "POST_DURATION_CODE")
	private String postDurationCode;

	@Column(name = "LIMIT_DURATION_TYPE")
	private String limitDurationType;

	@Column(name = "LIMIT_DURATION_UNIT")
	private String limitDurationUnit;

	@Temporal(TemporalType.DATE)
	@Column(name = "ACTUAL_START_DATE")
	private Date actualStartDate;

	@Column(name = "POST_FTE_TYPE")
	private String postFTEType;

	@Column(name = "POST_STATUS_CODE")
	private String postStatusCode;

	@Column(name = "RESOURCES_SUPPORT_FR_EXT")
	private String resourcesSupportFrExt;

	// Funding Source 1

	@Column(name = "FUND_SRC_1ST_PROGRAM_TYPE_CODE")
	private String fundSrc1stProgramTypeCode;

	@Column(name = "FUND_SRC_1ST_ID")
	private String fundSrc1stId;

	@Column(name = "FUND_SRC_1ST_SUB_CAT_ID")
	private String fundSrc1stSubCatId;

	// Funding Source 2

	@Column(name = "FUND_SRC_2ND_PROGRAM_TYPE_CODE")
	private String fundSrc2ndProgramTypeCode;

	@Column(name = "FUND_SRC_2ND_ID")
	private String fundSrc2ndId;

	@Column(name = "FUND_SRC_2ND_SUB_CAT_ID")
	private String fundSrc2ndSubCatId;

	// Funding Source 3

	@Column(name = "FUND_SRC_3RD_PROGRAM_TYPE_CODE")
	private String fundSrc3rdProgramTypeCode;

	@Column(name = "FUND_SRC_3RD_ID")
	private String fundSrc3rdId;

	@Column(name = "FUND_SRC_3RD_SUB_CAT_ID")
	private String fundSrc3rdSubCatId;

	// DC specific

	@Column(name = "FUND_SRC_CNT")
	private Integer fundSrcCnt;

	@Column(name = "HCM_PROCESS_IND")
	private String hcmProcessInd;

	@Column(name = "VALID_UPLOAD_IND")
	private String validUploadInd;

	@Column(name = "UPLOAD_TYPE")
	private String UploadType;

	@Column(name = "FAIL_MSG")
	private String failMsg;

	public Integer getDcPostDataSourceUID() {
		return dcPostDataSourceUID;
	}

	public void setDcPostDataSourceUID(Integer dcPostDataSourceUID) {
		this.dcPostDataSourceUID = dcPostDataSourceUID;
	}

	public Date getDcDate() {
		return dcDate;
	}

	public void setDcDate(Date dcDate) {
		this.dcDate = dcDate;
	}

	public String getSourcePostId() {
		return sourcePostId;
	}

	public void setSourcePostId(String sourcePostId) {
		this.sourcePostId = sourcePostId;
	}

	public String getHcmPositionName() {
		return hcmPositionName;
	}

	public void setHcmPositionName(String hcmPositionName) {
		this.hcmPositionName = hcmPositionName;
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

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
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

	public String getPostDurationDesc() {
		return postDurationDesc;
	}

	public void setPostDurationDesc(String postDurationDesc) {
		this.postDurationDesc = postDurationDesc;
	}

	public Date getPostStartDate() {
		return postStartDate;
	}

	public void setPostStartDate(Date postStartDate) {
		this.postStartDate = postStartDate;
	}

	public String getLimitDurationNo() {
		return limitDurationNo;
	}

	public void setLimitDurationNo(String limitDurationNo) {
		this.limitDurationNo = limitDurationNo;
	}

	public String getLimitDurationUnitDesc() {
		return limitDurationUnitDesc;
	}

	public void setLimitDurationUnitDesc(String limitDurationUnitDesc) {
		this.limitDurationUnitDesc = limitDurationUnitDesc;
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

	public String getHeldAgainstInd() {
		return heldAgainstInd;
	}

	public void setHeldAgainstInd(String heldAgainstInd) {
		this.heldAgainstInd = heldAgainstInd;
	}

	public String getHeldAgainstPostId() {
		return heldAgainstPostId;
	}

	public void setHeldAgainstPostId(String heldAgainstPostId) {
		this.heldAgainstPostId = heldAgainstPostId;
	}

	public String getUpgradeTLInd() {
		return upgradeTLInd;
	}

	public void setUpgradeTLInd(String upgradeTLInd) {
		this.upgradeTLInd = upgradeTLInd;
	}

	public String getJuniorPostId() {
		return juniorPostId;
	}

	public void setJuniorPostId(String juniorPostId) {
		this.juniorPostId = juniorPostId;
	}

	public String getHoBuyServiceInd() {
		return hoBuyServiceInd;
	}

	public void setHoBuyServiceInd(String hoBuyServiceInd) {
		this.hoBuyServiceInd = hoBuyServiceInd;
	}

	public String getResourcesSupportFrExtDesc() {
		return resourcesSupportFrExtDesc;
	}

	public void setResourcesSupportFrExtDesc(String resourcesSupportFrExtDesc) {
		this.resourcesSupportFrExtDesc = resourcesSupportFrExtDesc;
	}

	public String getResourcesSupportRemark() {
		return resourcesSupportRemark;
	}

	public void setResourcesSupportRemark(String resourcesSupportRemark) {
		this.resourcesSupportRemark = resourcesSupportRemark;
	}

	public String getFundSrc1stAnnualPlanInd() {
		return fundSrc1stAnnualPlanInd;
	}

	public void setFundSrc1stAnnualPlanInd(String fundSrc1stAnnualPlanInd) {
		this.fundSrc1stAnnualPlanInd = fundSrc1stAnnualPlanInd;
	}

	public String getFundSrc1stProgramYear() {
		return fundSrc1stProgramYear;
	}

	public void setFundSrc1stProgramYear(String fundSrc1stProgramYear) {
		this.fundSrc1stProgramYear = fundSrc1stProgramYear;
	}

	public String getFundSrc1stProgramCode() {
		return fundSrc1stProgramCode;
	}

	public void setFundSrc1stProgramCode(String fundSrc1stProgramCode) {
		this.fundSrc1stProgramCode = fundSrc1stProgramCode;
	}

	public String getFundSrc1stProgramName() {
		return fundSrc1stProgramName;
	}

	public void setFundSrc1stProgramName(String fundSrc1stProgramName) {
		this.fundSrc1stProgramName = fundSrc1stProgramName;
	}

	public String getFundSrc1stProgramType() {
		return fundSrc1stProgramType;
	}

	public void setFundSrc1stProgramType(String fundSrc1stProgramType) {
		this.fundSrc1stProgramType = fundSrc1stProgramType;
	}

	public String getFundSrc1stDesc() {
		return fundSrc1stDesc;
	}

	public void setFundSrc1stDesc(String fundSrc1stDesc) {
		this.fundSrc1stDesc = fundSrc1stDesc;
	}

	public String getFundSrc1stSubCatDesc() {
		return fundSrc1stSubCatDesc;
	}

	public void setFundSrc1stSubCatDesc(String fundSrc1stSubCatDesc) {
		this.fundSrc1stSubCatDesc = fundSrc1stSubCatDesc;
	}

	public Date getFundSrc1stStartDate() {
		return fundSrc1stStartDate;
	}

	public void setFundSrc1stStartDate(Date fundSrc1stStartDate) {
		this.fundSrc1stStartDate = fundSrc1stStartDate;
	}

	public Date getFundSrc1stEndDate() {
		return fundSrc1stEndDate;
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

	public String getFundSrc1stInst() {
		return fundSrc1stInst;
	}

	public void setFundSrc1stInst(String fundSrc1stInst) {
		this.fundSrc1stInst = fundSrc1stInst;
	}

	public String getFundSrc1stSection() {
		return fundSrc1stSection;
	}

	public void setFundSrc1stSection(String fundSrc1stSection) {
		this.fundSrc1stSection = fundSrc1stSection;
	}

	public String getFundSrc1stAnalytical() {
		return fundSrc1stAnalytical;
	}

	public void setFundSrc1stAnalytical(String fundSrc1stAnalytical) {
		this.fundSrc1stAnalytical = fundSrc1stAnalytical;
	}

	public String getFundSrc1stRemark() {
		return fundSrc1stRemark;
	}

	public void setFundSrc1stRemark(String fundSrc1stRemark) {
		this.fundSrc1stRemark = fundSrc1stRemark;
	}

	public String getFundSrc2ndAnnualPlanInd() {
		return fundSrc2ndAnnualPlanInd;
	}

	public void setFundSrc2ndAnnualPlanInd(String fundSrc2ndAnnualPlanInd) {
		this.fundSrc2ndAnnualPlanInd = fundSrc2ndAnnualPlanInd;
	}

	public String getFundSrc2ndProgramYear() {
		return fundSrc2ndProgramYear;
	}

	public void setFundSrc2ndProgramYear(String fundSrc2ndProgramYear) {
		this.fundSrc2ndProgramYear = fundSrc2ndProgramYear;
	}

	public String getFundSrc2ndProgramCode() {
		return fundSrc2ndProgramCode;
	}

	public void setFundSrc2ndProgramCode(String fundSrc2ndProgramCode) {
		this.fundSrc2ndProgramCode = fundSrc2ndProgramCode;
	}

	public String getFundSrc2ndProgramName() {
		return fundSrc2ndProgramName;
	}

	public void setFundSrc2ndProgramName(String fundSrc2ndProgramName) {
		this.fundSrc2ndProgramName = fundSrc2ndProgramName;
	}

	public String getFundSrc2ndProgramType() {
		return fundSrc2ndProgramType;
	}

	public void setFundSrc2ndProgramType(String fundSrc2ndProgramType) {
		this.fundSrc2ndProgramType = fundSrc2ndProgramType;
	}

	public String getFundSrc2ndDesc() {
		return fundSrc2ndDesc;
	}

	public void setFundSrc2ndDesc(String fundSrc2ndDesc) {
		this.fundSrc2ndDesc = fundSrc2ndDesc;
	}

	public String getFundSrc2ndSubCatDesc() {
		return fundSrc2ndSubCatDesc;
	}

	public void setFundSrc2ndSubCatDesc(String fundSrc2ndSubCatDesc) {
		this.fundSrc2ndSubCatDesc = fundSrc2ndSubCatDesc;
	}

	public Date getFundSrc2ndStartDate() {
		return fundSrc2ndStartDate;
	}

	public void setFundSrc2ndStartDate(Date fundSrc2ndStartDate) {
		this.fundSrc2ndStartDate = fundSrc2ndStartDate;
	}

	public Date getFundSrc2ndEndDate() {
		return fundSrc2ndEndDate;
	}

	public void setFundSrc2ndEndDate(Date fundSrc2ndEndDate) {
		this.fundSrc2ndEndDate = fundSrc2ndEndDate;
	}

	public Double getFundSrc2ndFte() {
		return fundSrc2ndFte;
	}

	public void setFundSrc2ndFte(Double fundSrc2ndFte) {
		this.fundSrc2ndFte = fundSrc2ndFte;
	}

	public String getFundSrc2ndInst() {
		return fundSrc2ndInst;
	}

	public void setFundSrc2ndInst(String fundSrc2ndInst) {
		this.fundSrc2ndInst = fundSrc2ndInst;
	}

	public String getFundSrc2ndSection() {
		return fundSrc2ndSection;
	}

	public void setFundSrc2ndSection(String fundSrc2ndSection) {
		this.fundSrc2ndSection = fundSrc2ndSection;
	}

	public String getFundSrc2ndAnalytical() {
		return fundSrc2ndAnalytical;
	}

	public void setFundSrc2ndAnalytical(String fundSrc2ndAnalytical) {
		this.fundSrc2ndAnalytical = fundSrc2ndAnalytical;
	}

	public String getFundSrc2ndRemark() {
		return fundSrc2ndRemark;
	}

	public void setFundSrc2ndRemark(String fundSrc2ndRemark) {
		this.fundSrc2ndRemark = fundSrc2ndRemark;
	}

	public String getFundSrc3rdAnnualPlanInd() {
		return fundSrc3rdAnnualPlanInd;
	}

	public void setFundSrc3rdAnnualPlanInd(String fundSrc3rdAnnualPlanInd) {
		this.fundSrc3rdAnnualPlanInd = fundSrc3rdAnnualPlanInd;
	}

	public String getFundSrc3rdProgramYear() {
		return fundSrc3rdProgramYear;
	}

	public void setFundSrc3rdProgramYear(String fundSrc3rdProgramYear) {
		this.fundSrc3rdProgramYear = fundSrc3rdProgramYear;
	}

	public String getFundSrc3rdProgramCode() {
		return fundSrc3rdProgramCode;
	}

	public void setFundSrc3rdProgramCode(String fundSrc3rdProgramCode) {
		this.fundSrc3rdProgramCode = fundSrc3rdProgramCode;
	}

	public String getFundSrc3rdProgramName() {
		return fundSrc3rdProgramName;
	}

	public void setFundSrc3rdProgramName(String fundSrc3rdProgramName) {
		this.fundSrc3rdProgramName = fundSrc3rdProgramName;
	}

	public String getFundSrc3rdProgramType() {
		return fundSrc3rdProgramType;
	}

	public void setFundSrc3rdProgramType(String fundSrc3rdProgramType) {
		this.fundSrc3rdProgramType = fundSrc3rdProgramType;
	}

	public String getFundSrc3rdDesc() {
		return fundSrc3rdDesc;
	}

	public void setFundSrc3rdDesc(String fundSrc3rdDesc) {
		this.fundSrc3rdDesc = fundSrc3rdDesc;
	}

	public String getFundSrc3rdSubCatDesc() {
		return fundSrc3rdSubCatDesc;
	}

	public void setFundSrc3rdSubCatDesc(String fundSrc3rdSubCatDesc) {
		this.fundSrc3rdSubCatDesc = fundSrc3rdSubCatDesc;
	}

	public Date getFundSrc3rdStartDate() {
		return fundSrc3rdStartDate;
	}

	public void setFundSrc3rdStartDate(Date fundSrc3rdStartDate) {
		this.fundSrc3rdStartDate = fundSrc3rdStartDate;
	}

	public Date getFundSrc3rdEndDate() {
		return fundSrc3rdEndDate;
	}

	public void setFundSrc3rdEndDate(Date fundSrc3rdEndDate) {
		this.fundSrc3rdEndDate = fundSrc3rdEndDate;
	}

	public Double getFundSrc3rdFte() {
		return fundSrc3rdFte;
	}

	public void setFundSrc3rdFte(Double fundSrc3rdFte) {
		this.fundSrc3rdFte = fundSrc3rdFte;
	}

	public String getFundSrc3rdInst() {
		return fundSrc3rdInst;
	}

	public void setFundSrc3rdInst(String fundSrc3rdInst) {
		this.fundSrc3rdInst = fundSrc3rdInst;
	}

	public String getFundSrc3rdSection() {
		return fundSrc3rdSection;
	}

	public void setFundSrc3rdSection(String fundSrc3rdSection) {
		this.fundSrc3rdSection = fundSrc3rdSection;
	}

	public String getFundSrc3rdAnalytical() {
		return fundSrc3rdAnalytical;
	}

	public void setFundSrc3rdAnalytical(String fundSrc3rdAnalytical) {
		this.fundSrc3rdAnalytical = fundSrc3rdAnalytical;
	}

	public String getFundSrc3rdRemark() {
		return fundSrc3rdRemark;
	}

	public void setFundSrc3rdRemark(String fundSrc3rdRemark) {
		this.fundSrc3rdRemark = fundSrc3rdRemark;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getHcmPositionId() {
		return hcmPositionId;
	}

	public void setHcmPositionId(String hcmPositionId) {
		this.hcmPositionId = hcmPositionId;
	}

	public String getStaffGroupCode() {
		return staffGroupCode;
	}

	public void setStaffGroupCode(String staffGroupCode) {
		this.staffGroupCode = staffGroupCode;
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

	public String getPostDurationCode() {
		return postDurationCode;
	}

	public void setPostDurationCode(String postDurationCode) {
		this.postDurationCode = postDurationCode;
	}

	public String getLimitDurationType() {
		return limitDurationType;
	}

	public void setLimitDurationType(String limitDurationType) {
		this.limitDurationType = limitDurationType;
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

	public String getPostFTEType() {
		return postFTEType;
	}

	public void setPostFTEType(String postFTEType) {
		this.postFTEType = postFTEType;
	}

	public String getPostStatusCode() {
		return postStatusCode;
	}

	public void setPostStatusCode(String postStatusCode) {
		this.postStatusCode = postStatusCode;
	}

	public String getResourcesSupportFrExt() {
		return resourcesSupportFrExt;
	}

	public void setResourcesSupportFrExt(String resourcesSupportFrExt) {
		this.resourcesSupportFrExt = resourcesSupportFrExt;
	}

	public String getFundSrc1stProgramTypeCode() {
		return fundSrc1stProgramTypeCode;
	}

	public void setFundSrc1stProgramTypeCode(String fundSrc1stProgramTypeCode) {
		this.fundSrc1stProgramTypeCode = fundSrc1stProgramTypeCode;
	}

	public String getFundSrc1stId() {
		return fundSrc1stId;
	}

	public void setFundSrc1stId(String fundSrc1stId) {
		this.fundSrc1stId = fundSrc1stId;
	}

	public String getFundSrc1stSubCatId() {
		return fundSrc1stSubCatId;
	}

	public void setFundSrc1stSubCatId(String fundSrc1stSubCatId) {
		this.fundSrc1stSubCatId = fundSrc1stSubCatId;
	}

	public String getFundSrc2ndProgramTypeCode() {
		return fundSrc2ndProgramTypeCode;
	}

	public void setFundSrc2ndProgramTypeCode(String fundSrc2ndProgramTypeCode) {
		this.fundSrc2ndProgramTypeCode = fundSrc2ndProgramTypeCode;
	}

	public String getFundSrc2ndId() {
		return fundSrc2ndId;
	}

	public void setFundSrc2ndId(String fundSrc2ndId) {
		this.fundSrc2ndId = fundSrc2ndId;
	}

	public String getFundSrc2ndSubCatId() {
		return fundSrc2ndSubCatId;
	}

	public void setFundSrc2ndSubCatId(String fundSrc2ndSubCatId) {
		this.fundSrc2ndSubCatId = fundSrc2ndSubCatId;
	}

	public String getFundSrc3rdProgramTypeCode() {
		return fundSrc3rdProgramTypeCode;
	}

	public void setFundSrc3rdProgramTypeCode(String fundSrc3rdProgramTypeCode) {
		this.fundSrc3rdProgramTypeCode = fundSrc3rdProgramTypeCode;
	}

	public String getFundSrc3rdId() {
		return fundSrc3rdId;
	}

	public void setFundSrc3rdId(String fundSrc3rdId) {
		this.fundSrc3rdId = fundSrc3rdId;
	}

	public String getFundSrc3rdSubCatId() {
		return fundSrc3rdSubCatId;
	}

	public void setFundSrc3rdSubCatId(String fundSrc3rdSubCatId) {
		this.fundSrc3rdSubCatId = fundSrc3rdSubCatId;
	}

	public Integer getFundSrcCnt() {
		return fundSrcCnt;
	}

	public void setFundSrcCnt(Integer fundSrcCnt) {
		this.fundSrcCnt = fundSrcCnt;
	}

	public String getHcmProcessInd() {
		return hcmProcessInd;
	}

	public void setHcmProcessInd(String hcmProcessInd) {
		this.hcmProcessInd = hcmProcessInd;
	}

	public String getValidUploadInd() {
		return validUploadInd;
	}

	public void setValidUploadInd(String validUploadInd) {
		this.validUploadInd = validUploadInd;
	}

	public String getUploadType() {
		return UploadType;
	}

	public void setUploadType(String uploadType) {
		UploadType = uploadType;
	}

	public String getFailMsg() {
		return failMsg;
	}

	public void setFailMsg(String failMsg) {
		this.failMsg = failMsg;
	}

}

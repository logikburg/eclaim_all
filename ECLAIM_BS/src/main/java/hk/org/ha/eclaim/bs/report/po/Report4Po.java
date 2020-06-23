package hk.org.ha.eclaim.bs.report.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@IdClass(PK_Report4.class)
@Entity
public class Report4Po {
	@Id
	@Column(name="postTitle")
	private String postTitle;
	
	@Column(name="clusterCode")
	private String clusterCode;
	
	@Column(name="instCode")
	private String instCode;
	
	@Column(name="employeeId")
	private String employeeId;
	
	@Column(name="hcm_position_id")
	private String hcmPositionId;

	@Column(name="postDurationType")
	private String postDurationType;
	
	@Temporal(TemporalType.DATE)
	@Column(name="effective_start_date")
	private Date effDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="effective_end_date")
	private Date effEndDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="postEndDate")
	private Date postEndDate;
	
	@Id
	@Column(name="postId")
	private String postId;
	
	@Id
	@Column(name="employeeName")
	private String employeeName;
	
	@Column(name="generic_rank_name")
	private String rank;
	
	@Column(name="postRemark")
	private String postRemark;
	
	@Column(name="totalFTE")
	private Double totalFTE;
	
	@Column(name="strengthFTE")
	private Double strengthFTE;
	
	@Column(name="vacanciesFTE")
	private Double vacanciesFTE;
	
	@Column(name="employmentCategory")
	private String employmentCategory;
	
	@Column(name="contract_type")
	private String contractType;
	
	@Temporal(TemporalType.DATE)
	@Column(name="contract_active_start_date")
	private Date contractStartDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="contract_end_date")
	private Date contractEndDate;
	
	public String getPostTitle() {
		return postTitle;
	}

	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getPostRemark() {
		return postRemark;
	}

	public void setPostRemark(String postRemark) {
		this.postRemark = postRemark;
	}

	public Double getTotalFTE() {
		return totalFTE;
	}

	public void setTotalFTE(Double totalFTE) {
		this.totalFTE = totalFTE;
	}

	public Double getStrengthFTE() {
		return strengthFTE;
	}

	public void setStrengthFTE(Double strengthFTE) {
		this.strengthFTE = strengthFTE;
	}

	public Double getVacanciesFTE() {
		return vacanciesFTE;
	}

	public void setVacanciesFTE(Double vacanciesFTE) {
		this.vacanciesFTE = vacanciesFTE;
	}

	public String getEmploymentCategory() {
		return employmentCategory;
	}

	public void setEmploymentCategory(String employmentCategory) {
		this.employmentCategory = employmentCategory;
	}
	
	public Date getPostEndDate() {
		return postEndDate;
	}

	public void setPostEndDate(Date postEndDate) {
		this.postEndDate = postEndDate;
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

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getPostDurationType() {
		return postDurationType;
	}

	public void setPostDurationType(String postDurationType) {
		this.postDurationType = postDurationType;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public Date getEffDate() {
		return effDate;
	}

	public void setEffDate(Date effDate) {
		this.effDate = effDate;
	}

	public Date getEffEndDate() {
		return effEndDate;
	}

	public void setEffEndDate(Date effEndDate) {
		this.effEndDate = effEndDate;
	}

	public String getHcmPositionId() {
		return hcmPositionId;
	}

	public void setHcmPositionId(String hcmPositionId) {
		this.hcmPositionId = hcmPositionId;
	}

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public Date getContractStartDate() {
		return contractStartDate;
	}

	public void setContractStartDate(Date contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	public Date getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(Date contractEndDate) {
		this.contractEndDate = contractEndDate;
	}
}

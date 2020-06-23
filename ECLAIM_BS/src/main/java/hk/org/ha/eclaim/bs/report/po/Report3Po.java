package hk.org.ha.eclaim.bs.report.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Report3Po {
	@Id
	@Column(name="postSnapId")
	private String postSnapId;
	
	//@Id
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
	@Column(name="effDate")
	private Date effDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="effective_end_date")
	private Date effEndDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="postEndDate")
	private Date postEndDate;
	
	@Column(name="postId")
	private String postId;
	
	@Column(name="employeeName")
	private String employeeName;
	
	@Column(name="generic_rank_name")
	private String rank;
	
	@Column(name="postRemark")
	private String postRemark;
	
	public String getHcmPositionId() {
		return hcmPositionId;
	}

	public void setHcmPositionId(String hcmPositionId) {
		this.hcmPositionId = hcmPositionId;
	}

	public Date getEffEndDate() {
		return effEndDate;
	}

	public void setEffEndDate(Date effEndDate) {
		this.effEndDate = effEndDate;
	}

	@Column(name="totalFTE")
	private Double totalFTE;
	
	@Column(name="strengthFTE")
	private Double strengthFTE;
	
	@Column(name="vacanciesFTE")
	private Double vacanciesFTE;
	
	@Column(name="employmentType")
	private String employmentType;
	
	@Column(name="employeeCategory")
	private String employeeCategory;

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

	public Date getEffDate() {
		return effDate;
	}

	public void setEffDate(Date effDate) {
		this.effDate = effDate;
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

	public String getEmploymentType() {
		return employmentType;
	}

	public void setEmploymentType(String employmentType) {
		this.employmentType = employmentType;
	}

	public String getEmployeeCategory() {
		return employeeCategory;
	}

	public void setEmployeeCategory(String employeeCategory) {
		this.employeeCategory = employeeCategory;
	}
}

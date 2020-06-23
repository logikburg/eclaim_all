package hk.org.ha.eclaim.bs.report.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ReportHeldAgainstListPo {
	@Id
	@Column(name="postSnapUID")
	private String postSnapUID;
	
	@Column(name="postId")
	private String postId;
	
	@Column(name="clusterCode")
	private String clusterCode;
	
	@Column(name="instCode")
	private String instCode;
	
	@Column(name="deptName")
	private String deptName;
	
	@Column(name="employeeId")
	private String employeeId;
	
	@Column(name="employeeName")
	private String employeeName;
	
	@Column(name="generic_rank_name")
	private String rank;
		
	@Column(name="strengthFTE")
	private Double strengthFTE;
	
	@Column(name="employment_type")
	private String employmentType;
	
	@Column(name="employee_category")
	private String employeeCategory;
	
	public String getPostSnapUID() {
		return postSnapUID;
	}

	public void setPostSnapUID(String postSnapUID) {
		this.postSnapUID = postSnapUID;
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

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
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

	public Double getStrengthFTE() {
		return strengthFTE;
	}

	public void setStrengthFTE(Double strengthFTE) {
		this.strengthFTE = strengthFTE;
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
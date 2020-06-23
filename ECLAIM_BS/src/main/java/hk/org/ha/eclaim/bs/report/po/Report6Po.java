package hk.org.ha.eclaim.bs.report.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Report6Po {
	@Id
	@Column(name="postSnapId")
	private String postSnapId;
	
	@Column(name="postId")
	private String postId;
	
	@Column(name="post_status_desc")
	private String postStatusDesc;
	
	@Column(name="generic_rank_name")
	private String rank;
	
	@Temporal(TemporalType.DATE)
	@Column(name="postEndDate")
	private Date postEndDate;
	
	@Column(name="employeeId")
	private String employeeId;
	
	@Column(name="employeeName")
	private String employeeName;
	
	@Column(name="employmentCategory")
	private String employmentCategory;

	public String getPostSnapId() {
		return postSnapId;
	}

	public void setPostSnapId(String postSnapId) {
		this.postSnapId = postSnapId;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public Date getPostEndDate() {
		return postEndDate;
	}

	public void setPostEndDate(Date postEndDate) {
		this.postEndDate = postEndDate;
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

	public String getEmploymentCategory() {
		return employmentCategory;
	}

	public void setEmploymentCategory(String employmentCategory) {
		this.employmentCategory = employmentCategory;
	}

	public String getPostStatusDesc() {
		return postStatusDesc;
	}

	public void setPostStatusDesc(String postStatusDesc) {
		this.postStatusDesc = postStatusDesc;
	}
}

package hk.org.ha.eclaim.bs.report.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Report7Po {
	@Id
	@Column(name="postSnapId")
	private String postSnapId;
	
	@Column(name="postId")
	private String postId;
	
	@Column(name="mpr_hcm_position_id")
	private String postHcmPosition;
	
	@Column(name="hcm_hcm_position_id")
	private String hcmHcmPosition;
	
	@Column(name="mpr_hcm_position_name")
	private String postHcmPositionName;
	
	@Column(name="hcm_hcm_position_name")
	private String hcmHcmPositionName;
	
	@Column(name="generic_rank_name")
	private String rank;
		
	@Column(name="employeeId")
	private String employeeId;
	
	@Column(name="employeeName")
	private String employeeName;
	
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

	public String getPostHcmPosition() {
		return postHcmPosition;
	}

	public void setPostHcmPosition(String postHcmPosition) {
		this.postHcmPosition = postHcmPosition;
	}

	public String getHcmHcmPosition() {
		return hcmHcmPosition;
	}

	public void setHcmHcmPosition(String hcmHcmPosition) {
		this.hcmHcmPosition = hcmHcmPosition;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
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

	public String getPostHcmPositionName() {
		return postHcmPositionName;
	}

	public void setPostHcmPositionName(String postHcmPositionName) {
		this.postHcmPositionName = postHcmPositionName;
	}

	public String getHcmHcmPositionName() {
		return hcmHcmPositionName;
	}

	public void setHcmHcmPositionName(String hcmHcmPositionName) {
		this.hcmHcmPositionName = hcmHcmPositionName;
	}




}

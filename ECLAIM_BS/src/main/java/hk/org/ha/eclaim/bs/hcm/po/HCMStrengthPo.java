package hk.org.ha.eclaim.bs.hcm.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "V_HCM_STRENGTH")
public class HCMStrengthPo {

	@Id
	@Column(name = "PERSON_ID")
	private String personId;

	@Column(name = "EMPLOYEE_NUMBER")
	private String employeeNumber;

	@Column(name = "POST_ID")
	private String postId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECTIVE_START_DATE")
	private Date effectiveStartDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECTIVE_END_DATE")
	private Date effectiveEndDate;

	@Column(name = "STAFF_GROUP")
	private String staffGroup;

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
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

	public String getStaffGroup() {
		return staffGroup;
	}

	public void setStaffGroup(String staffGroup) {
		this.staffGroup = staffGroup;
	}

}

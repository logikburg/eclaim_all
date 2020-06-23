package hk.org.ha.eclaim.bs.hcm.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import hk.org.ha.eclaim.core.helper.DateTimeHelper;

@Entity
@IdClass(PK_HCMAssignmentPo.class)
@Table(name = "V_HCM_ASSIGNMENT")
public class HCMAssignmentPo {
	@Id
	@Column(name = "ASSIGNMENT_NUMBER")
	private String assignmentNumber;

	@Column(name = "PERSON_ID")
	private Integer personId;

	@Column(name = "FULL_NAME")
	private String fullName;

	@Column(name = "RANK")
	private String rank;

	@Column(name = "EMPLOYMENT_TYPE")
	private String employmentType;

	@Column(name = "POSITION_TITLE")
	private String positionTitle;

	@Column(name = "FTE")
	private Double fte;

	@Column(name = "HEAD")
	private Double head;

	@Id
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECTIVE_START_DATE")
	private Date effectiveStartDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECTIVE_END_DATE")
	private Date effectiveEndDate;

	@Column(name = "REASON_OF_LEAVING")
	private String reasonOfLeaving;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ACTUAL_TERMINATION_DATE")
	private Date actualTerminationDate;

	@Column(name = "POSITION_ID")
	private Integer positionId;

	@Column(name = "POSITION_NAME")
	private String positionName;

	@Column(name = "CHANGE_REASON_MEANING")
	private String changeReasonMeaning;

	@Column(name = "PRIMARY_FLAG")
	private String primaryFlag;

	@Column(name = "EMPLOYEE_CATEGORY")
	private String employeeCategory;

	@Column(name = "STAFF_GROUP")
	private String staffGroup;
	
	@Column(name = "ASSIGNMENT_STATUS")
	private String assignmentStatus;
	
	@Transient
	private String employeeNumber;	// Added for ST08674
	
	// Added for MPR0004
	@Column(name = "PAYZONE")
	private String payzone;
	
	@Transient
	public String getEffectiveStartDateStr() {
		if (effectiveStartDate == null) {
			return "";
		}
		return DateTimeHelper.formatDateToString(effectiveStartDate);
	}
	
	@Transient
	public String getEffectiveEndDateStr() {
		if (effectiveEndDate == null) {
			return "";
		}
		return DateTimeHelper.formatDateToString(effectiveEndDate);
	}

	@Transient
	public String getActualTerminationDateStr() {
		if (actualTerminationDate == null) {
			return "";
		}
		return DateTimeHelper.formatDateToString(actualTerminationDate);
	}

	public String getAssignmentNumber() {
		return assignmentNumber;
	}

	public void setAssignmentNumber(String assignmentNumber) {
		this.assignmentNumber = assignmentNumber;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getEmploymentType() {
		return employmentType;
	}

	public void setEmploymentType(String employmentType) {
		this.employmentType = employmentType;
	}

	public String getPositionTitle() {
		return positionTitle;
	}

	public void setPositionTitle(String positionTitle) {
		this.positionTitle = positionTitle;
	}

	public Double getFte() {
		return fte;
	}

	public void setFte(Double fte) {
		this.fte = fte;
	}

	public Double getHead() {
		return head;
	}

	public void setHead(Double head) {
		this.head = head;
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

	public String getReasonOfLeaving() {
		return reasonOfLeaving;
	}

	public void setReasonOfLeaving(String reasonOfLeaving) {
		this.reasonOfLeaving = reasonOfLeaving;
	}

	public Date getActualTerminationDate() {
		return actualTerminationDate;
	}

	public void setActualTerminationDate(Date actualTerminationDate) {
		this.actualTerminationDate = actualTerminationDate;
	}

	public Integer getPositionId() {
		return positionId;
	}

	public void setPositionId(Integer positionId) {
		this.positionId = positionId;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public String getChangeReasonMeaning() {
		return changeReasonMeaning;
	}

	public void setChangeReasonMeaning(String changeReasonMeaning) {
		this.changeReasonMeaning = changeReasonMeaning;
	}

	public String getPrimaryFlag() {
		return primaryFlag;
	}

	public void setPrimaryFlag(String primaryFlag) {
		this.primaryFlag = primaryFlag;
	}

	public String getEmployeeCategory() {
		return employeeCategory;
	}

	public void setEmployeeCategory(String employeeCategory) {
		this.employeeCategory = employeeCategory;
	}

	public String getStaffGroup() {
		return staffGroup;
	}

	public void setStaffGroup(String staffGroup) {
		this.staffGroup = staffGroup;
	}

	public String getAssignmentStatus() {
		return assignmentStatus;
	}

	public void setAssignmentStatus(String assignmentStatus) {
		this.assignmentStatus = assignmentStatus;
	}

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getPayzone() {
		return payzone;
	}

	public void setPayzone(String payzone) {
		this.payzone = payzone;
	}
}
package hk.org.ha.eclaim.bs.claim.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="XXEC_CLAIM_REQUEST")
public class ClaimRequestPo extends AbstractBasePo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6542499715324686340L;

	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="XXEC_CLAIM_ID_SEQ")
	@SequenceGenerator(name="XXEC_CLAIM_ID_SEQ", sequenceName="XXEC_CLAIM_ID_SEQ", allocationSize=1)
	@Id
	@Column(name="CLAIM_ID")
	private Integer claimId;
	
	@Column(name="CLAIM_TYPE")
	private String type;
	
	@Column(name="PROJECT_ID")
	private Integer projectId;
	
	@Column(name="DEPARTMENT_ID")
	private Integer departmentId;
	
	@Column(name="JOB_GROUP_ID")
	private Integer jobGroupId;
	
	@Column(name="LOCATION_ID")
	private Integer locationId;
	
	@Column(name="OS_CLAIM_AS_OF_DATE")
	private Date osClaimAsOfDate;
	
	@Column(name="LAST_UPDATE_DATE")
	private Date lastUpdateDate;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="TOTAL_TXN")
	private Integer totalTxn;
	
	@Column(name="NO_OF_APPROVED_TXN")
	private Integer noOfApprovedTxn;
	
	@Column(name="NO_OF_ON_HOLD_TXN")
	private Integer noOfOnHoldTxn;
	
	@Column(name="NO_OF_HA_CS_PAYROLL_TXN")
	private Integer noOfHaCsPayrollTxn;
	
	@Column(name="NO_OF_REVIEWED_TXN")
	private Integer noOfReviewedTxn;
	
	@Column(name="NO_OF_PENDING_TRANSFER_TXN")
	private Integer noOfPendingTransferTxn;
	
	@Column(name="TRANSFERRED")
	private String transferred;

	public Integer getClaimId() {
		return claimId;
	}

	public void setClaimId(Integer claimId) {
		this.claimId = claimId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getJobGroupId() {
		return jobGroupId;
	}

	public void setJobGroupId(Integer jobGroupId) {
		this.jobGroupId = jobGroupId;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public Date getOsClaimAsOfDate() {
		return osClaimAsOfDate;
	}

	public void setOsClaimAsOfDate(Date osClaimAsOfDate) {
		this.osClaimAsOfDate = osClaimAsOfDate;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getTotalTxn() {
		return totalTxn;
	}

	public void setTotalTxn(Integer totalTxn) {
		this.totalTxn = totalTxn;
	}

	public Integer getNoOfApprovedTxn() {
		return noOfApprovedTxn;
	}

	public void setNoOfApprovedTxn(Integer noOfApprovedTxn) {
		this.noOfApprovedTxn = noOfApprovedTxn;
	}

	public Integer getNoOfOnHoldTxn() {
		return noOfOnHoldTxn;
	}

	public void setNoOfOnHoldTxn(Integer noOfOnHoldTxn) {
		this.noOfOnHoldTxn = noOfOnHoldTxn;
	}

	public Integer getNoOfHaCsPayrollTxn() {
		return noOfHaCsPayrollTxn;
	}

	public void setNoOfHaCsPayrollTxn(Integer noOfHaCsPayrollTxn) {
		this.noOfHaCsPayrollTxn = noOfHaCsPayrollTxn;
	}

	public Integer getNoOfReviewedTxn() {
		return noOfReviewedTxn;
	}

	public void setNoOfReviewedTxn(Integer noOfReviewedTxn) {
		this.noOfReviewedTxn = noOfReviewedTxn;
	}

	public Integer getNoOfPendingTransferTxn() {
		return noOfPendingTransferTxn;
	}

	public void setNoOfPendingTransferTxn(Integer noOfPendingTransferTxn) {
		this.noOfPendingTransferTxn = noOfPendingTransferTxn;
	}

	public String getTransferred() {
		return transferred;
	}

	public void setTransferred(String transferred) {
		this.transferred = transferred;
	}
}

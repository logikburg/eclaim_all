package hk.org.ha.eclaim.bs.payment.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name = "XXEAL_PAYMENT_BATCH")
public class PaymentBatchPo extends AbstractBasePo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6542499715324686340L;

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XXEAL_BATCH_ID_SEQ")
	@SequenceGenerator(name = "XXEAL_BATCH_ID_SEQ", sequenceName = "XXEAL_BATCH_ID_SEQ", allocationSize = 1)
	@Id
	@Column(name = "BATCH_ID")
	private Integer batchId;
	
//	@Column(name = "DETAIL_GROUP_ID")
//	private Integer detailGroupId;

	@Column(name = "PAYMENT_TYPE")
	private String paymentType;

	@Column(name = "PROJECT_ID")
	private Integer projectId;

	@Transient
	private String projectName;

	@Column(name = "DEPARTMENT_ID")
	private Integer departmentId;

	@Transient
	private String departmentName;

	@Column(name = "LOCATION_ID")
	private Integer locationId;

	@Column(name = "PAY_MONTH")
	private Date payMonth;

	@Column(name = "LAST_UPDATE_DATE")
	private Date lastUpdateDate;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "TOTAL_TXN")
	private Integer totalTxn;

	@Column(name = "NO_OF_APPROVED_TXN")
	private Integer noOfApprovedTxn;

	@Column(name = "NO_OF_ON_HOLD_TXN")
	private Integer noOfOnHoldTxn;

	@Column(name = "NO_OF_HA_CS_PAYROLL_TXN")
	private Integer noOfHaCsPayrollTxn;

	@Column(name = "NO_OF_REVIEWED_TXN")
	private Integer noOfReviewedTxn;

	@Column(name = "NO_OF_PENDING_TRANSFER_TXN")
	private Integer noOfPendingTransferTxn;

	@Column(name = "TRANSFERRED")
	private Integer transferred;

	@Column(name = "PARENT_BATCH_ID")
	private Integer parentBatchId;

//	@Column(name = "REASON")
//	private String reason;

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
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

	/*
	 * public Integer getJobGroupId() { return jobGroupId; }
	 * 
	 * public void setJobGroupId(Integer jobGroupId) { this.jobGroupId = jobGroupId;
	 * }
	 */
	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public Date getPayMonth() {
		return payMonth;
	}

	public void setPayMonth(Date payMonth) {
		this.payMonth = payMonth;
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

	public Integer getTransferred() {
		return transferred;
	}

	public void setTransferred(Integer transferred) {
		this.transferred = transferred;
	}

	public Integer getParentBatchId() {
		return parentBatchId;
	}

	public void setParentBatchId(Integer parentBatchId) {
		this.parentBatchId = parentBatchId;
	}

//	public String getReason() {
//		return reason;
//	}
//
//	public void setReason(String reason) {
//		this.reason = reason;
//	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
}

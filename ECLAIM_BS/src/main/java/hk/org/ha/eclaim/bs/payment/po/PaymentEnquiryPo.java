package hk.org.ha.eclaim.bs.payment.po;

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

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
public class PaymentEnquiryPo extends AbstractBasePo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6542499715324686340L;

	@Id
	@Column(name="BATCH_ID")
	private Integer batchId;
	
	@Column(name="PROJECT_NAME")
	private String projectName;
	
	@Column(name="DEPARTMENT_NAME")
	private String departmentName;
	
	@Column(name="PAY_MONTH")
	private Date payMonth;
	
	@Column(name="LAST_UPDATE_DATE")
	private Date lastUpdateDate;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="TRANSFERRED")
	private Integer transferredTxn;
	
	@Column(name="TOTAL_TXN")
	private Integer totalTxn;
	
	@Column(name="NO_OF_ON_HOLD_TXN")
	private Integer onHoldTxn;
	
	@Column(name="JOBS")
	private String jobs;
	
	@Column(name="PARENT_BATCH_ID")
	private Integer parentBatchID;

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}

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

	public Integer getTransferredTxn() {
		return transferredTxn;
	}

	public void setTransferredTxn(Integer transferredTxn) {
		this.transferredTxn = transferredTxn;
	}

	public Integer getTotalTxn() {
		return totalTxn;
	}

	public void setTotalTxn(Integer totalTxn) {
		this.totalTxn = totalTxn;
	}

	public Integer getOnHoldTxn() {
		return onHoldTxn;
	}

	public void setOnHoldTxn(Integer onHoldTxn) {
		this.onHoldTxn = onHoldTxn;
	}

	public String getJobs() {
		return jobs;
	}

	public void setJobs(String jobs) {
		this.jobs = jobs;
	}

	public Integer getParentBatchID() {
		return parentBatchID;
	}

	public void setParentBatchID(Integer parentBatchID) {
		this.parentBatchID = parentBatchID;
	}

}

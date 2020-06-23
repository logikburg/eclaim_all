package hk.org.ha.eclaim.model.payment;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hk.org.ha.eclaim.bs.payment.po.PaymentBatchPo;
import hk.org.ha.eclaim.bs.payment.po.PaymentBatchTo;
import hk.org.ha.eclaim.bs.payment.po.PaymentEnquiryPo;
import hk.org.ha.eclaim.bs.payment.po.PaymentJobHoursVo;

public class PaymentBatchVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");

	public PaymentBatchVo() {
		super();
	}

	public PaymentBatchVo(PaymentBatchPo paymentBatchPo) {
		super();
		if (paymentBatchPo.getParentBatchId() == null) {
			this.batchId = paymentBatchPo.getBatchId().toString();
		} else {
			this.batchId = paymentBatchPo.getParentBatchId().toString();
			this.subBatchId = paymentBatchPo.getBatchId();
		}
		this.projectNmNo = "@Troy";
		this.hospDept = "@Troy";
		this.jobs = "@Troy";
		this.payMonth = sdf.format(paymentBatchPo.getPayMonth());
		this.status = paymentBatchPo.getStatus();
		this.lastUpdateDate = paymentBatchPo.getLastUpdateDate();
		this.totalSum = paymentBatchPo.getTotalTxn();
		this.transferredSum = paymentBatchPo.getTransferred();
		this.onHoldSum = paymentBatchPo.getNoOfOnHoldTxn();
		this.remark = "@Troy";
	}

	public PaymentBatchVo(PaymentEnquiryPo paymentEnquiryPo) {
		super();

		if (paymentEnquiryPo.getParentBatchID() != null) {
			this.batchId = paymentEnquiryPo.getParentBatchID().toString() + "(Deck 2)";
			this.parentBatchId = paymentEnquiryPo.getBatchId();
		} else {
			this.batchId = paymentEnquiryPo.getBatchId().toString();
		}
		this.projectNmNo = paymentEnquiryPo.getProjectName();
		this.hospDept = paymentEnquiryPo.getDepartmentName();
		this.jobs = paymentEnquiryPo.getJobs();
		this.payMonth = sdf.format(paymentEnquiryPo.getPayMonth());
		this.status = paymentEnquiryPo.getStatus();
		this.lastUpdateDate = paymentEnquiryPo.getLastUpdateDate();
		this.totalSum = paymentEnquiryPo.getTotalTxn();
		this.transferredSum = paymentEnquiryPo.getTransferredTxn();
		this.onHoldSum = paymentEnquiryPo.getOnHoldTxn();
		this.subBatchButton = false;
		if (onHoldSum > 0) {
			this.subBatchButton = true;
		}
		this.remark = "";
	}

	List<PaymentBatchTo> listDetailBatch = new ArrayList<PaymentBatchTo>();
	
	List<PaymentJobHoursVo> paymentJobHours = new ArrayList<PaymentJobHoursVo>();

	private String batchId;

	private Integer subBatchId;

	private String projectNmNo;

	private String hospDept;

	private String jobs;

	private String payMonth;

	private String status;

	private Date lastUpdateDate;

	private Integer totalSum;

	private Integer transferredSum;

	private Integer onHoldSum;

	private String remark;

	private Boolean subBatchButton;

	private Integer parentBatchId;

	private String projectId;

	private String projectName;

	private List<String> jobGroupId;

	private String departmentId;

	private String departmentName;

	private String paymentType;
	
	private String projectDuration;
	
	private String projectType;
	
	private Integer validatedSum;
	
	private Integer outstandingSum;

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public Integer getSubBatchId() {
		return subBatchId;
	}

	public void setSubBatchId(Integer subBatchId) {
		this.subBatchId = subBatchId;
	}

	public String getProjectNmNo() {
		return projectNmNo;
	}

	public void setProjectNmNo(String projectNmNo) {
		this.projectNmNo = projectNmNo;
	}

	public String getHospDept() {
		return hospDept;
	}

	public void setHospDept(String hospDept) {
		this.hospDept = hospDept;
	}

	public String getJobs() {
		return jobs;
	}

	public void setJobs(String jobs) {
		this.jobs = jobs;
	}

	public String getPayMonth() {
		return payMonth;
	}

	public void setPayMonth(String payMonth) {
		this.payMonth = payMonth;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(Integer totalSum) {
		this.totalSum = totalSum;
	}

	public Integer getTransferredSum() {
		return transferredSum;
	}

	public void setTransferredSum(Integer transferredSum) {
		this.transferredSum = transferredSum;
	}

	public Integer getOnHoldSum() {
		return onHoldSum;
	}

	public void setOnHoldSum(Integer onHoldSum) {
		this.onHoldSum = onHoldSum;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Boolean getSubBatchButton() {
		return subBatchButton;
	}

	public void setSubBatchButton(Boolean subBatchButton) {
		this.subBatchButton = subBatchButton;
	}

	public Integer getParentBatchId() {
		return parentBatchId;
	}

	public void setParentBatchId(Integer parentBatchId) {
		this.parentBatchId = parentBatchId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public List<String> getJobGroupId() {
		return jobGroupId;
	}

	public void setJobGroupId(List<String> jobGroupId) {
		this.jobGroupId = jobGroupId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public List<PaymentBatchTo> getListDetailBatch() {
		return listDetailBatch;
	}

	public void setListDetailBatch(List<PaymentBatchTo> lstDetailBatch) {
		this.listDetailBatch = lstDetailBatch;
	}

	public String getProjectDuration() {
		return projectDuration;
	}

	public void setProjectDuration(String fromDate, String toDate ) {
		//this.projectDuration = sdf.format(fromDate) + " - " + sdf.format(toDate);
		this.projectDuration = "09/2018 - 10/2019";
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public Integer getValidatedSum() {
		return validatedSum;
	}

	public void setValidatedSum(Integer validatedSum) {
		this.validatedSum = validatedSum;
	}

	public Integer getOutstandingSum() {
		return outstandingSum;
	}

	public void setOutstandingSum(Integer outstandingSum) {
		this.outstandingSum = outstandingSum;
	}

	public List<PaymentJobHoursVo> getPaymentJobHours() {
		return paymentJobHours;
	}

	public void setPaymentJobHours(List<PaymentJobHoursVo> paymentJobHoursVo) {
		this.paymentJobHours = paymentJobHoursVo;
	}

}

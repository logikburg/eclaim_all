package hk.org.ha.eclaim.model.payment;

import java.io.Serializable;

public class PaymentEnquirySearchVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String departmentName;
	private Integer departmentId;
	private Integer deptId;
	private String projectName;
	private Integer projectId;
	private Integer empNo;
	private String payMonth;
	private String earnedMonth;
	private Boolean unProcess;
	private Boolean validated;
	private Boolean partiallyValidated;
	private Boolean pendingRollback;
	private Boolean pendingTransfer;
	private Boolean transferred;
	private Boolean partiallyTransferred;

	public PaymentEnquirySearchVo() {
		super();
	}

	public PaymentEnquirySearchVo(Integer deptId, String projectNm, Integer projectId, Integer empNo, String payMonth,
			String earnedMonth, Boolean unProcess, Boolean validated, Boolean partiallyValidated,
			Boolean pendingRollback, Boolean pendingTransfer, Boolean transferred, Boolean partiallyTransferred) {
		super();
		this.deptId = deptId;
		this.projectName = projectNm;
		this.projectId = projectId;
		this.empNo = empNo;
		this.payMonth = payMonth;
		this.earnedMonth = earnedMonth;
		this.unProcess = unProcess;
		this.validated = validated;
		this.partiallyValidated = partiallyValidated;
		this.pendingRollback = pendingRollback;
		this.pendingTransfer = pendingTransfer;
		this.transferred = transferred;
		this.partiallyTransferred = partiallyTransferred;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setgetProjectName(String projectNm) {
		this.projectName = projectNm;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getEmpNo() {
		return empNo;
	}

	public void setEmpNo(Integer empNo) {
		this.empNo = empNo;
	}

	public String getPayMonth() {
		return payMonth;
	}

	public void setPayMonth(String payMonth) {
		this.payMonth = payMonth;
	}

	public String getEarnedMonth() {
		return earnedMonth;
	}

	public void setEarnedMonth(String earnedMonth) {
		this.earnedMonth = earnedMonth;
	}

	public Boolean getUnProcess() {
		return unProcess;
	}

	public void setUnProcess(Boolean unProcess) {
		this.unProcess = unProcess;
	}

	public Boolean getValidated() {
		return validated;
	}

	public void setValidated(Boolean validated) {
		this.validated = validated;
	}

	public Boolean getPartiallyValidated() {
		return partiallyValidated;
	}

	public void setPartiallyValidated(Boolean partiallyValidated) {
		this.partiallyValidated = partiallyValidated;
	}

	public Boolean getPendingRollback() {
		return pendingRollback;
	}

	public void setPendingRollback(Boolean pendingRollback) {
		this.pendingRollback = pendingRollback;
	}

	public Boolean getPendingTransfer() {
		return pendingTransfer;
	}

	public void setPendingTransfer(Boolean pendingTransfer) {
		this.pendingTransfer = pendingTransfer;
	}

	public Boolean getTransferred() {
		return transferred;
	}

	public void setTransferred(Boolean transferred) {
		this.transferred = transferred;
	}

	public Boolean getPartiallyTransferred() {
		return partiallyTransferred;
	}

	public void setPartiallyTransferred(Boolean partiallyTransferred) {
		this.partiallyTransferred = partiallyTransferred;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer deptId) {
		this.departmentId = deptId;
	}
	
	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}
}

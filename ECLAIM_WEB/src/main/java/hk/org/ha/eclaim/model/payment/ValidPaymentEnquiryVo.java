package hk.org.ha.eclaim.model.payment;

import java.util.Date;
import java.util.List;

import hk.org.ha.eclaim.bs.payment.po.PaymentBatchTo;
import hk.org.ha.eclaim.bs.security.po.UserLogonPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

public class ValidPaymentEnquiryVo {
	
	private String userName;
	private UserPo user;
	private String currentRole;
	private UserLogonPo userLogonPo;
	private String currentCluster;
	private String targetRole;
	private String paymentType;
	private String departmentName;
	private String projectName;
	private String projectId;
	private Date earnedMonth;
	private Date payMonth;
	private String empNo;
	private Boolean unProcess;
	private Boolean validated;
	private Boolean partiallyValidated;
	private Boolean pendingRollback;
	private Boolean pendingTransfer;
	private Boolean transferred;
	private Boolean partiallyTransferred;
	private List<PaymentBatchVo> paymentBatchList;
	private List<PaymentBatchTo> listDetailBatch;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public UserPo getUser() {
		return user;
	}
	public void setUser(UserPo user) {
		this.user = user;
	}
	public String getCurrentRole() {
		return currentRole;
	}
	public void setCurrentRole(String currentRole) {
		this.currentRole = currentRole;
	}
	public UserLogonPo getUserLogonPo() {
		return userLogonPo;
	}
	public void setUserLogonPo(UserLogonPo userLogonPo) {
		this.userLogonPo = userLogonPo;
	}
	public String getCurrentCluster() {
		return currentCluster;
	}
	public void setCurrentCluster(String currentCluster) {
		this.currentCluster = currentCluster;
	}
	public String getTargetRole() {
		return targetRole;
	}
	public void setTargetRole(String targetRole) {
		this.targetRole = targetRole;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public Date getEarnedMonth() {
		return earnedMonth;
	}
	public void setEarnedMonth(Date earnedMonth) {
		this.earnedMonth = earnedMonth;
	}
	public Date getPayMonth() {
		return payMonth;
	}
	public void setPayMonth(Date payMonth) {
		this.payMonth = payMonth;
	}
	public String getEmpNo() {
		return empNo;
	}
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
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
	public List<PaymentBatchVo> getPaymentBatchList() {
		return paymentBatchList;
	}
	public void setPaymentBatchList(List<PaymentBatchVo> paymentBatchList) {
		this.paymentBatchList = paymentBatchList;
	}
	public List<PaymentBatchTo> getListDetailBatch() {
		return listDetailBatch;
	}
	public void setListDetailBatch(List<PaymentBatchTo> listDetailBatch) {
		this.listDetailBatch = listDetailBatch;
	}

}

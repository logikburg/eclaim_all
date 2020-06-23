package hk.org.ha.eclaim.bs.request.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="XXEAL_WORKFLOW_ROUTE")
public class RequestWorkflowRoutePo extends AbstractBasePo {
	
	private static final long serialVersionUID = -6927250404679650655L;

	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="XXEAL_WORKFLOW_ROUTE_SEQ")
	@SequenceGenerator(name="XXEAL_WORKFLOW_ROUTE_SEQ", sequenceName="XXEAL_WORKFLOW_ROUTE_SEQ", allocationSize=1)
	@Id
	@Column(name="WF_ROUTE_UID")
	private Integer wfRouteUid;
	
	@Column(name="MG_REVIEW_IND")
	private String mgReviewInd;
	
	@Column(name="REQUEST_TYPE")
	private String requestType;
	
	@Column(name="CURRENT_STATUS")
	private String currentStatus;
	
	@Column(name="FROM_ROLE")
	private String fromRole;
	
	@Column(name="SUBMIT_TO_ROLE")
	private String submitToRole;
	
	@Column(name="SUBMIT_CC_ROLE")
	private String submitCcRole;
	
	@Column(name="RETURN_TO_ROLE")
	private String returnToRole; // "ALL" - for all user 
	
	@Column(name="RETURN_CC_ROLE")
	private String returnCcRole;
	
	@Column(name="ACTION_NAME")
	private String actionName;
	
	@Column(name="ACTION_TAKEN")
	private String actionTaken;
	
	@Column(name="STAFF_GROUP_CODE")
	private String staffGroupCode;
	
	@Column(name="TARGET_STATUS")
	private String targetStatus;
	
	@Column(name="RETURN_STATUS") 
	private String returnStatus;
	
	@Column(name="TEMPLATE_ID")
	private String templateId;
	
	@Column(name="RETURN_TEMPLATE_ID")
	private String returnTemplateId;
	
	public Integer getRouteUid() {
		return wfRouteUid;
	}

	public void setRouteUid(Integer routeUid) {
		this.wfRouteUid = routeUid;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getSubmitToRole() {
		return submitToRole;
	}

	public void setSubmitToRole(String submitToRole) {
		this.submitToRole = submitToRole;
	}

	public String getReturnToRole() {
		return returnToRole;
	}

	public void setReturnToRole(String returnToRole) {
		this.returnToRole = returnToRole;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getMgReviewInd() {
		return mgReviewInd;
	}

	public void setMgReviewInd(String mgReviewInd) {
		this.mgReviewInd = mgReviewInd;
	}

	public String getStaffGroupCode() {
		return staffGroupCode;
	}

	public void setStaffGroupCode(String staffGroupCode) {
		this.staffGroupCode = staffGroupCode;
	}

	public String getTargetStatus() {
		return targetStatus;
	}

	public void setTargetStatus(String targetStatus) {
		this.targetStatus = targetStatus;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(String returnStatus) {
		this.returnStatus = returnStatus;
	}

	public String getReturnTemplateId() {
		return returnTemplateId;
	}

	public void setReturnTemplateId(String returnTemplateId) {
		this.returnTemplateId = returnTemplateId;
	}

	public String getSubmitCcRole() {
		return submitCcRole;
	}

	public void setSubmitCcRole(String submitCcRole) {
		this.submitCcRole = submitCcRole;
	}

	public String getReturnCcRole() {
		return returnCcRole;
	}

	public void setReturnCcRole(String returnCcRole) {
		this.returnCcRole = returnCcRole;
	}

	public String getFromRole() {
		return fromRole;
	}

	public void setFromRole(String fromRole) {
		this.fromRole = fromRole;
	}

	public String getActionTaken() {
		return actionTaken;
	}

	public void setActionTaken(String actionTaken) {
		this.actionTaken = actionTaken;
	}
}

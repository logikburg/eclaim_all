package hk.org.ha.eclaim.model.project;

import java.util.List;

import hk.org.ha.eclaim.bs.project.po.ProjectDocumentPo;
import hk.org.ha.eclaim.bs.request.po.RequestWorkflowRoutePo;

public class ProjectReviewVo extends ProjectBaseVo{
	
	private String formAction;
	
	//General Info
	private String departmentName;
	private String srsHospital;
	private String srsDepartment;
	private String projectName;
	private String projectNameC;
	private String projectPurpose;
	private String projectOwner;
	private String projectPreparer;
	private String fundingSource;
	private String programType;
	private String finIcName;
	private String finIcId;
	
	//Circum
	private List<String> circumList;
	private String justifications;
	private String triggerPoint;
	private String other;
	private String manPowerShortage;
	
	//Job Details
	private List<ProjectJobVo> jobList;
	private String usingOTA;
	private String otaJustifications;
	private String totHour;
	private String totEstImp;
	private List<ProjectFinImplVo> finImplList;
	//Schedule
	private String is7x24;
	private String startDate;
	private String endDate;
	private String attendance;
	private String attenOther;
	private List<ProjectScheduleVo> scheduleList;
	private List<ProjectScheduleVo> scheduleDateList;

	//Quantifiable
	private String qDeliver;
//	private List<ProjectQuantifiableDetailVo> qDetailsList;
	
	//Approval History List
	private List<ApprHistVo> apprHistList;
	
	//Document List
	private List<String> uploadFileId;
	private List<ProjectDocumentVo> docList;
	
	private String remark;
	private String toTemplateId;
	private String returnTemplateId;
	private String toRole;
	private String returnRole;
	private String actionButton;
	private String returnButton;
	private String returnCase;
	private String emailTo;
	private String emailCC;
	private String emailTitle;
	private String emailContent;
	private String emailSuppInfo;
	
	public void setRouteFields(RequestWorkflowRoutePo route ) {
		this.setFormAction(route.getTargetStatus());
		this.setActionButton(route.getActionName());
		this.setToTemplateId(route.getTemplateId());
		this.setReturnTemplateId(route.getReturnTemplateId());
		this.setToRole(route.getSubmitToRole());
		this.setReturnRole(route.getReturnToRole());
		
		if (route.getReturnToRole() != null && !"".equals(route.getReturnToRole())) {
			this.setReturnButton("Return");
		}
	}
	
	public String getSrsHospital() {
		return srsHospital;
	}

	public void setSrsHospital(String srsHospital) {
		this.srsHospital = srsHospital;
	}

	public String getSrsDepartment() {
		return srsDepartment;
	}

	public void setSrsDepartment(String srsDepartment) {
		this.srsDepartment = srsDepartment;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectNameC() {
		return projectNameC;
	}

	public void setProjectNameC(String projectNameC) {
		this.projectNameC = projectNameC;
	}

	public String getProjectPurpose() {
		return projectPurpose;
	}

	public void setProjectPurpose(String projectPurpose) {
		this.projectPurpose = projectPurpose;
	}

	public String getProjectOwner() {
		return projectOwner;
	}

	public void setProjectOwner(String projectOwner) {
		this.projectOwner = projectOwner;
	}

	public String getProjectPreparer() {
		return projectPreparer;
	}

	public void setProjectPreparer(String projectPreparer) {
		this.projectPreparer = projectPreparer;
	}

	public String getFundingSource() {
		return fundingSource;
	}

	public void setFundingSource(String fundingSource) {
		this.fundingSource = fundingSource;
	}

	public List<String> getCircumList() {
		return circumList;
	}

	public void setCircumList(List<String> circumList) {
		this.circumList = circumList;
	}

	public String getJustifications() {
		return justifications;
	}

	public void setJustifications(String justifications) {
		this.justifications = justifications;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getManPowerShortage() {
		return manPowerShortage;
	}

	public void setManPowerShortage(String manPowerShortage) {
		this.manPowerShortage = manPowerShortage;
	}

	public List<ProjectJobVo> getJobList() {
		return jobList;
	}

	public void setJobList(List<ProjectJobVo> jobList) {
		this.jobList = jobList;
	}

	public String getUsingOTA() {
		return usingOTA;
	}

	public void setUsingOTA(String usingOTA) {
		this.usingOTA = usingOTA;
	}

	public String getOtaJustifications() {
		return otaJustifications;
	}

	public void setOtaJustifications(String otaJustifications) {
		this.otaJustifications = otaJustifications;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getAttendance() {
		return attendance;
	}

	public void setAttendance(String attendance) {
		this.attendance = attendance;
	}

	public String getAttenOther() {
		return attenOther;
	}

	public void setAttenOther(String attenOther) {
		this.attenOther = attenOther;
	}

	public List<ProjectScheduleVo> getScheduleList() {
		return scheduleList;
	}

	public void setScheduleList(List<ProjectScheduleVo> scheduleList) {
		this.scheduleList = scheduleList;
	}

//	public List<ProjectQuantifiableDetailVo> getqDetailsList() {
//		return qDetailsList;
//	}
//
//	public void setqDetailsList(List<ProjectQuantifiableDetailVo> qDetailsList) {
//		this.qDetailsList = qDetailsList;
//	}

	public String getTriggerPoint() {
		return triggerPoint;
	}

	public void setTriggerPoint(String triggerPoint) {
		this.triggerPoint = triggerPoint;
	}

	public String getqDeliver() {
		return qDeliver;
	}

	public void setqDeliver(String qDeliver) {
		this.qDeliver = qDeliver;
	}

	public String getReturnCase() {
		return returnCase;
	}

	public void setReturnCase(String returnCase) {
		this.returnCase = returnCase;
	}

	public String getEmailTo() {
		return emailTo;
	}

	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}

	public String getEmailCC() {
		return emailCC;
	}

	public void setEmailCC(String emailCC) {
		this.emailCC = emailCC;
	}

	public String getEmailTitle() {
		return emailTitle;
	}

	public void setEmailTitle(String emailTitle) {
		this.emailTitle = emailTitle;
	}

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}

	public String getEmailSuppInfo() {
		return emailSuppInfo;
	}

	public void setEmailSuppInfo(String emailSuppInfo) {
		this.emailSuppInfo = emailSuppInfo;
	}

	public String getFormAction() {
		return formAction;
	}

	public void setFormAction(String formAction) {
		this.formAction = formAction;
	}

	public String getToTemplateId() {
		return toTemplateId;
	}

	public void setToTemplateId(String toTemplateId) {
		this.toTemplateId = toTemplateId;
	}

	public String getReturnTemplateId() {
		return returnTemplateId;
	}

	public void setReturnTemplateId(String returnTemplateId) {
		this.returnTemplateId = returnTemplateId;
	}

	public String getToRole() {
		return toRole;
	}

	public void setToRole(String toRole) {
		this.toRole = toRole;
	}

	public String getReturnRole() {
		return returnRole;
	}

	public void setReturnRole(String returnRole) {
		this.returnRole = returnRole;
	}

	public String getActionButton() {
		return actionButton;
	}

	public void setActionButton(String actionButton) {
		this.actionButton = actionButton;
	}

	public String getReturnButton() {
		return returnButton;
	}

	public void setReturnButton(String returnButton) {
		this.returnButton = returnButton;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<ApprHistVo> getApprHistList() {
		return apprHistList;
	}

	public void setApprHistList(List<ApprHistVo> apprHistList) {
		this.apprHistList = apprHistList;
	}

	public String getTotHour() {
		return totHour;
	}

	public void setTotHour(String totHour) {
		this.totHour = totHour;
	}

	public String getTotEstImp() {
		return totEstImp;
	}

	public void setTotEstImp(String totEstImp) {
		this.totEstImp = totEstImp;
	}

	public String getIs7x24() {
		return is7x24;
	}

	public void setIs7x24(String is7x24) {
		this.is7x24 = is7x24;
	}

	public List<ProjectDocumentVo> getDocList() {
		return docList;
	}

	public void setDocList(List<ProjectDocumentVo> docList) {
		this.docList = docList;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getProgramType() {
		return programType;
	}

	public void setProgramType(String programType) {
		this.programType = programType;
	}

	public List<ProjectScheduleVo> getScheduleDateList() {
		return scheduleDateList;
	}

	public void setScheduleDateList(List<ProjectScheduleVo> scheduleDateList) {
		this.scheduleDateList = scheduleDateList;
	}

	public List<String> getUploadFileId() {
		return uploadFileId;
	}

	public void setUploadFileId(List<String> uploadFileId) {
		this.uploadFileId = uploadFileId;
	}

	public String getFinIcName() {
		return finIcName;
	}

	public void setFinIcName(String finIcName) {
		this.finIcName = finIcName;
	}

	public String getFinIcId() {
		return finIcId;
	}

	public void setFinIcId(String finIcId) {
		this.finIcId = finIcId;
	}

	public List<ProjectFinImplVo> getFinImplList() {
		return finImplList;
	}

	public void setFinImplList(List<ProjectFinImplVo> finImplList) {
		this.finImplList = finImplList;
	}
	
}

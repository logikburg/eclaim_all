package hk.org.ha.eclaim.model.project;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ProjectGeneralInfoVo extends ProjectBaseVo{
	
	private String departmentId;
	private String departmentName;
	private String srsHospital;
	private String srsDepartment;
	private String projectName;
	private String projectNameC;
	private String purpose;
	private String ownerId;
	private String owner;
	private String preparerId;
	private String preparer;
	private String fundingSource;
	private String programType;
	private String commencementDate;
	private String orgiEndDate;
	private List<ProjectScheduleVo> scheduleList;
	private List<ProjectScheduleVo> scheduleDateList;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	private String startDate;
	@JsonFormat(pattern="dd/MM/yyyy")
	private String endDate;
	
	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
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
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getPreparer() {
		return preparer;
	}
	public void setPreparer(String preparer) {
		this.preparer = preparer;
	}
	public String getFundingSource() {
		return fundingSource;
	}
	public void setFundingSource(String fundingSource) {
		this.fundingSource = fundingSource;
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
	public List<ProjectScheduleVo> getScheduleList() {
		return scheduleList;
	}
	public void setScheduleList(List<ProjectScheduleVo> scheduleList) {
		this.scheduleList = scheduleList;
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
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getPreparerId() {
		return preparerId;
	}
	public void setPreparerId(String preparerId) {
		this.preparerId = preparerId;
	}
	public List<ProjectScheduleVo> getScheduleDateList() {
		return scheduleDateList;
	}
	public void setScheduleDateList(List<ProjectScheduleVo> scheduleDateList) {
		this.scheduleDateList = scheduleDateList;
	}
	public String getCommencementDate() {
		return commencementDate;
	}
	public void setCommencementDate(String commencementDate) {
		this.commencementDate = commencementDate;
	}
	public String getOrgiEndDate() {
		return orgiEndDate;
	}
	public void setOrgiEndDate(String orgiEndDate) {
		this.orgiEndDate = orgiEndDate;
	}
}

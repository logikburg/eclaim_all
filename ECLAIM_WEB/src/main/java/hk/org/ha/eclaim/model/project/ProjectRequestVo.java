package hk.org.ha.eclaim.model.project;

import java.text.SimpleDateFormat;
import java.util.Date;

import hk.org.ha.eclaim.bs.project.po.ProjectRequestPo;

public class ProjectRequestVo {
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");

	public ProjectRequestVo() {
		super();
	}
	
	public ProjectRequestVo(ProjectRequestPo projPo) {
		super();
		this.projectId = projPo.getProjectId().toString();
		this.projectVerId = projPo.getProjectVerId().toString();
		this.projectStatus = projPo.getProjectStatus();
		this.departmentId = projPo.getDepartmentId().toString();
		this.departmentName = projPo.getDepartmentName();
		this.projectName = projPo.getProjectName();
		this.projectNameC = projPo.getProjectNameC();
		this.projectOwner = projPo.getProjectOwner();
		this.startDate = projPo.getFromDate();
		this.endDate = projPo.getToDate();
		this.extension = projPo.getExtension().toString();
		this.invitation = projPo.getInvitation().toString();
		this.totAppl = projPo.getTotAppl().toString();
		this.osAppl = projPo.getOsAppl().toString();
		this.apprWorkHour = projPo.getApprWorkHour().toString();
		this.usedWorkHour = projPo.getUsedWorkHour().toString();
		this.avalWorkHour = projPo.getAvalWorkHour().toString();
		this.updatedDate = projPo.getUpdatedDate();
	}

	private String projectId;
	
	private String projectVerId;
	
	private String projectStatus;
	
	private String departmentId;
	
	private String departmentName;
	
	private String projectName;
	
	private String projectNameC;
	
	private String projectOwner;
	
	private Date startDate;
	
	private Date endDate;
	
	private String extension;
	
	private String invitation;
	
	private String totAppl;
	
	private String osAppl;
	
	private String apprWorkHour;
	
	private String usedWorkHour;
	
	private String avalWorkHour;

	private Date updatedDate;
	
	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectVerId() {
		return projectVerId;
	}

	public void setProjectVerId(String projectVerId) {
		this.projectVerId = projectVerId;
	}

	public String getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

	public String getDepartmentId() {
		return departmentId;
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

	public String getProjectOwner() {
		return projectOwner;
	}

	public void setProjectOwner(String projectOwner) {
		this.projectOwner = projectOwner;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getInvitation() {
		return invitation;
	}

	public void setInvitation(String invitation) {
		this.invitation = invitation;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public String getTotAppl() {
		return totAppl;
	}

	public void setTotAppl(String totAppl) {
		this.totAppl = totAppl;
	}

	public String getOsAppl() {
		return osAppl;
	}

	public void setOsAppl(String osAppl) {
		this.osAppl = osAppl;
	}

	public String getApprWorkHour() {
		return apprWorkHour;
	}

	public void setApprWorkHour(String apprWorkHour) {
		this.apprWorkHour = apprWorkHour;
	}

	public String getUsedWorkHour() {
		return usedWorkHour;
	}

	public void setUsedWorkHour(String usedWorkHour) {
		this.usedWorkHour = usedWorkHour;
	}

	public String getAvalWorkHour() {
		return avalWorkHour;
	}

	public void setAvalWorkHour(String avalWorkHour) {
		this.avalWorkHour = avalWorkHour;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

}

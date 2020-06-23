package hk.org.ha.eclaim.bs.project.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnTransformer;


@Entity
@IdClass(ProjectId.class)
@Table(name="XXEAL_PROJ_REQ_V")
public class ProjectRequestPo {

	@Column(name="PROJECT_UID")
	private String projectUid;
	
	@Id
	@Column(name="PROJECT_ID")
	private Integer projectId;
	
	@Id
	@Column(name="PROJECT_VER_ID")
	private Integer projectVerId;
	
	@Column(name="PROJECT_STATUS_CODE")
	private String projectStatusCode;
	
	@Column(name="PROJECT_STATUS")
	private String projectStatus;
	
	@Column(name="DEPARTMENT_ID")
	private Integer departmentId;
	
//	@ColumnTransformer(read="(select name from HR_ALL_ORGANIZATION_UNITS where organization_Id = DEPARTMENT_ID)")
//	@Transient
	@Column(name="DEPARTMENT_NAME")
	private String departmentName;
	
	@Column(name="PROJECT_NAME")
	private String projectName;
	
	@Column(name="PROJECT_NAME_C")
	private String projectNameC;
	
//	@Column(name="PROJECT_OWNER_ID")
//	private String projectOwnerId;
	
	@Column(name="PROJECT_OWNER")
	private String projectOwner;
	
	//@Column(name="PROJECT_PREPARER_ID")
	@Transient
	private String projectPreparerId;
	
	@Column(name="PROJECT_PREPARER")
	private String projectPreparer;
	
	@Column(name="FIN_HOSP_IC")
	private String finHospIc;

	@Column(name="FIN_HOSP_IC_ID")
	private String finHospIcId;
	
	@Temporal(TemporalType.DATE)
	@Column(name="FROM_DATE")
	private Date fromDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="TO_DATE")
	private Date toDate;
	
	@Column(name="extension")
	private Integer extension;
	
	@Column(name="invitation")
	private Integer invitation;
	
	@Column(name="tot_appl")
	private Integer totAppl;
	
	@Column(name="os_appl")
	private Integer osAppl;
	
	@Column(name="appr_work_hour")
	private Integer apprWorkHour;
	
	@Column(name="used_work_hour")
	private Integer usedWorkHour;
	
	@Column(name="avai_work_hour")
	private Integer avalWorkHour;

	@Temporal(TemporalType.DATE)
	@Column(name="UPDATED_DATE")
	private Date updatedDate;
	
	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getProjectVerId() {
		return projectVerId;
	}

	public void setProjectVerId(Integer projectVerId) {
		this.projectVerId = projectVerId;
	}

	public String getProjectStatusCode() {
		return projectStatusCode;
	}

	public void setProjectStatusCode(String projectStatusCode) {
		this.projectStatusCode = projectStatusCode;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
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

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Integer getExtension() {
		return extension;
	}

	public void setExtension(Integer extension) {
		this.extension = extension;
	}

	public Integer getInvitation() {
		return invitation;
	}

	public void setInvitation(Integer invitation) {
		this.invitation = invitation;
	}

	public Integer getTotAppl() {
		return totAppl;
	}

	public void setTotAppl(Integer totAppl) {
		this.totAppl = totAppl;
	}

	public Integer getOsAppl() {
		return osAppl;
	}

	public void setOsAppl(Integer osAppl) {
		this.osAppl = osAppl;
	}

	public Integer getApprWorkHour() {
		return apprWorkHour;
	}

	public void setApprWorkHour(Integer apprWorkHour) {
		this.apprWorkHour = apprWorkHour;
	}

	public Integer getUsedWorkHour() {
		return usedWorkHour;
	}

	public void setUsedWorkHour(Integer usedWorkHour) {
		this.usedWorkHour = usedWorkHour;
	}

	public Integer getAvalWorkHour() {
		return avalWorkHour;
	}

	public void setAvalWorkHour(Integer avalWorkHour) {
		this.avalWorkHour = avalWorkHour;
	}

	public String getProjectPreparer() {
		return projectPreparer;
	}

	public void setProjectPreparer(String projectPreparer) {
		this.projectPreparer = projectPreparer;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getProjectUid() {
		return projectUid;
	}

	public void setProjectUid(String projectUid) {
		this.projectUid = projectUid;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

}

package hk.org.ha.eclaim.bs.project.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnTransformer;
import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@IdClass(ProjectId.class)
@Table(name="XXEAL_PROJECT")
public class ProjectPo extends AbstractBasePo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6542499715324686340L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="XXEAL_PROJECT_ID_SEQ")
	@SequenceGenerator(name="XXEAL_PROJECT_ID_SEQ", sequenceName="XXEAL_PROJECT_ID_SEQ", allocationSize=1)
	@Column(name="PROJECT_ID")
	private Integer projectId;

	@Id
	@Column(name="PROJECT_VER_ID")
	private Integer projectVerId;
	
	@Column(name="PROJECT_NUMBER")
	private String projectNumber;
	
	@Column(name="PROJECT_STEP")
	private Integer projectStep;
	
	@Column(name="PROJECT_STATUS_CODE")
	private String projectStatusCode;
	
	@Column(name="DEPARTMENT_ID")
	private Integer departmentId;
	
	@Column(name="CLUSTER_ID")
	private String clusterId;
	
	@Column(name="SRS_HOSPITAL")
	private String srsHospital;
	
	@Column(name="SRS_DEPARTMENT")
	private String srsDepartment;
	
	@Column(name="PROJECT_NAME")
	private String projectName;
	
	@Column(name="PROJECT_NAME_C")
	private String projectNameC;
	
	@Column(name="PROJECT_PURPOSE")
	private String projectPurpose;
	
	@Column(name="PROJECT_OWNER_ID")
	@ColumnTransformer(write="UPPER(?)")
	private String projectOwnerId;
	
	@Column(name="PROJECT_OWNER")
	private String projectOwner;

	@Column(name="PROJECT_PREPARER_ID")
	@ColumnTransformer(write="UPPER(?)")
	private String projectPreparerId;
	
	@Column(name="PROJECT_PREPARER")
	private String projectPreparer;
	
	@Column(name="FIN_HOSP_IC")
	private String finHospIc;

	@Column(name="FIN_HOSP_IC_ID")
	@ColumnTransformer(write="UPPER(?)")
	private String finHospIcId;

	@Column(name="FUNDING_SOURCE")
	private String fundingSource;

	@Column(name="JUSTIFICATIONS")
	private String justifications;

	@Column(name="TRIGGER_POINT")
	private String triggerPoint;

	@Column(name="MANPOWER_SITUATION")
	private String manpowerSituation;

	@Column(name="USE_OTA_FLAG")
	private String useOtaFlag;
	
	@Column(name="OTA_JUSTIFICATIONS")
	private String otaJustifications;
	
	@Temporal(TemporalType.DATE)
	@Column(name="FROM_DATE")
	private Date fromDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="TO_DATE")
	private Date toDate;

	@Column(name="NO_SCHEDULE_FLAG")
	private String noScheduleFlag;
	
	@Column(name="Q_DELIVERABLES")
	private String qDeliverables;

	@Column(name="PROJECT_TYPE")
	private String projectType;

	@Column(name="PROGRAM_TYPE")
	private String programType;
	
//	@Temporal(TemporalType.DATE)
//	@Column(name="ORGI_START_DATE")
//	private Date orgiStartDate;
//	
//	@Temporal(TemporalType.DATE)
//	@Column(name="ORGI_END_DATE")
//	private Date orgiEndDate;
	
//	@Column(name="ORGI_PROJECT_ID")
//	private Integer orgiProjectId;
	
//	@Column(name="ORGI_VER_ID")
//	private Integer orgiVerId;
	
//	@Column(name="EXTENSION")
//	private int extension;
	
	@Column(name="REC_TYPE")
	private String recType;
	
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

	public Integer getProjectStep() {
		return projectStep;
	}

	public void setProjectStep(Integer projectStep) {
		this.projectStep = projectStep;
	}

	public String getProjectNumber() {
		return projectNumber;
	}

	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
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

	public String getJustifications() {
		return justifications;
	}

	public void setJustifications(String justifications) {
		this.justifications = justifications;
	}

	public String getTriggerPoint() {
		return triggerPoint;
	}

	public void setTriggerPoint(String triggerPoint) {
		this.triggerPoint = triggerPoint;
	}

	public String getManpowerSituation() {
		return manpowerSituation;
	}

	public void setManpowerSituation(String manpowerSituation) {
		this.manpowerSituation = manpowerSituation;
	}

	public String getOtaJustifications() {
		return otaJustifications;
	}

	public void setOtaJustifications(String otaJustifications) {
		this.otaJustifications = otaJustifications;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getNoScheduleFlag() {
		return noScheduleFlag;
	}

	public void setNoScheduleFlag(String noScheduleFlag) {
		this.noScheduleFlag = noScheduleFlag;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getUseOtaFlag() {
		return useOtaFlag;
	}

	public void setUseOtaFlag(String useOtaFlag) {
		this.useOtaFlag = useOtaFlag;
	}
	
	public String getqDeliverables() {
		return qDeliverables;
	}

	public void setqDeliverables(String qDeliverables) {
		this.qDeliverables = qDeliverables;
	}

	public String getProgramType() {
		return programType;
	}

	public void setProgramType(String programType) {
		this.programType = programType;
	}

	public String getProjectPreparerId() {
		return projectPreparerId;
	}

	public void setProjectPreparerId(String projectPreparerId) {
		this.projectPreparerId = projectPreparerId;
	}

	public String getProjectOwnerId() {
		return projectOwnerId;
	}

	public void setProjectOwnerId(String projectOwnerId) {
		this.projectOwnerId = projectOwnerId;
	}

	public String getRecType() {
		return recType;
	}

	public void setRecType(String recType) {
		this.recType = recType;
	}

//	public Date getOrgiStartDate() {
//		return orgiStartDate;
//	}
//
//	public void setOrgiStartDate(Date orgiStartDate) {
//		this.orgiStartDate = orgiStartDate;
//	}
//
//	public Date getOrgiEndDate() {
//		return orgiEndDate;
//	}
//
//	public void setOrgiEndDate(Date orgiEndDate) {
//		this.orgiEndDate = orgiEndDate;
//	}

//	public Integer getOrgiProjectId() {
//		return orgiProjectId;
//	}

//	public void setOrgiProjectId(Integer orgiProjectId) {
//		this.orgiProjectId = orgiProjectId;
//	}
//
//	public Integer getOrgiVerId() {
//		return orgiVerId;
//	}
//
//	public void setOrgiVerId(Integer orgiVerId) {
//		this.orgiVerId = orgiVerId;
//	}
//
//	public int getExtension() {
//		return extension;
//	}
//
//	public void setExtension(int extension) {
//		this.extension = extension;
//	}

	public String getFinHospIcId() {
		return finHospIcId;
	}

	public void setFinHospIcId(String finHospIcId) {
		this.finHospIcId = finHospIcId;
	}

	public String getFinHospIc() {
		return finHospIc;
	}

	public void setFinHospIc(String finHospIc) {
		this.finHospIc = finHospIc;
	}

	public String getClusterId() {
		return clusterId;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}

}

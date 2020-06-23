package hk.org.ha.eclaim.bs.project.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="XXEAL_PROJECT_JOB")
public class ProjectJobPo extends AbstractBasePo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9101125664338011690L;
	
	@Column(name="PROJECT_ID")
	private Integer projectId;
	
	@Column(name="PROJECT_VER_ID")
	private Integer projectVerId;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="XXEAL_PRJ_JOB_GROUP_ID_SEQ")
	@SequenceGenerator(name="XXEAL_PRJ_JOB_GROUP_ID_SEQ", sequenceName="XXEAL_PRJ_JOB_GROUP_ID_SEQ", allocationSize=1)
	@Column(name="JOB_GROUP_ID")
	private Integer jobGroupId;
	
	@Column(name="STAFF_GROUP")
	private String  staffGroup;
	
	@Column(name="PROJECT_COORDINATOR_ID")
	private String projectCoordinatorId;
	
	@Column(name="PROJECT_COORDINATOR")
	private String projectCoordinator;
	
	@Column(name="JOB_DESCRIPTION")
	private String jobDescription;
	
	@Column(name="QUOTA")
	private Integer quota;
	
	@Column(name="DURATION")
	private Integer duration;
	
	@Column(name="SESSION_PER_DAY")
	private Integer sessionPerDay;
	
	@Column(name="HOURS_PER_SESSION")
	private Integer hoursPerSession;
	
	@Column(name="TOTAL_HOUR")
	private Integer totalHour;
	
	@Column(name="OTHER_INFORMATION")
	private String otherInformation;
	
	@Column(name="TARGER_APPLICANT")
	private String targerApplicant;
	
	@Column(name="COA_INST")
	private String coaInst;
	
	@Column(name="COA_FUND")
	private String coaFund;
	
	@Column(name="COA_SECTION")
	private String coaSection;
	
	@Column(name="COA_ANALYSTIC")
	private String coaAnalytic;
	
	@Column(name="COA_TYPE")
	private String coaType;
	
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
	public Integer getJobGroupId() {
		return jobGroupId;
	}
	public void setJobGroupId(Integer jobGroupId) {
		this.jobGroupId = jobGroupId;
	}
	public String getStaffGroup() {
		return staffGroup;
	}
	public void setStaffGroup(String staffGroup) {
		this.staffGroup = staffGroup;
	}
	public String getProjectCoordinator() {
		return projectCoordinator;
	}
	public void setProjectCoordinator(String projectCoordinator) {
		this.projectCoordinator = projectCoordinator;
	}
	public String getJobDescription() {
		return jobDescription;
	}
	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}
	public Integer getQuota() {
		return quota;
	}
	public void setQuota(Integer quota) {
		this.quota = quota;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public Integer getSessionPerDay() {
		return sessionPerDay;
	}
	public void setSessionPerDay(Integer sessionPerDay) {
		this.sessionPerDay = sessionPerDay;
	}
	public Integer getHoursPerSession() {
		return hoursPerSession;
	}
	public void setHoursPerSession(Integer hoursPerSession) {
		this.hoursPerSession = hoursPerSession;
	}
	public Integer getTotalHour() {
		return totalHour;
	}
	public void setTotalHour(Integer totalHour) {
		this.totalHour = totalHour;
	}
	public String getOtherInformation() {
		return otherInformation;
	}
	public void setOtherInformation(String otherInformation) {
		this.otherInformation = otherInformation;
	}
	public String getTargerApplicant() {
		return targerApplicant;
	}
	public void setTargerApplicant(String targerApplicant) {
		this.targerApplicant = targerApplicant;
	}
	public String getProjectCoordinatorId() {
		return projectCoordinatorId;
	}
	public void setProjectCoordinatorId(String projectCoordinatorId) {
		this.projectCoordinatorId = projectCoordinatorId;
	}
	public String getCoaInst() {
		return coaInst;
	}
	public void setCoaInst(String coaInst) {
		this.coaInst = coaInst;
	}
	public String getCoaFund() {
		return coaFund;
	}
	public void setCoaFund(String coaFund) {
		this.coaFund = coaFund;
	}
	public String getCoaSection() {
		return coaSection;
	}
	public void setCoaSection(String coaSection) {
		this.coaSection = coaSection;
	}
	public String getCoaAnalytic() {
		return coaAnalytic;
	}
	public void setCoaAnalytic(String coaAnalytic) {
		this.coaAnalytic = coaAnalytic;
	}
	public String getCoaType() {
		return coaType;
	}
	public void setCoaType(String coaType) {
		this.coaType = coaType;
	}
	
}

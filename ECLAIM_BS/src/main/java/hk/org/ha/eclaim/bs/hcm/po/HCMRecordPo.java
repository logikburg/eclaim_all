package hk.org.ha.eclaim.bs.hcm.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import hk.org.ha.eclaim.core.helper.DateTimeHelper;

@Entity
@Table(name="V_HCM_REC")
public class HCMRecordPo {
	@Id
	@Column(name="POSITION_ID")
	private Integer positionId;
	
	@Column(name="POSITION_TITLE")
	private String positionTitle;
	
	@Column(name="POSITION_ORGANIZATION")
	private String positionOrganization;
	
	@Column(name="UNIT_TEAM")
	private String unitTeam;
	
	@Column(name="JOB_NAME")
	private String jobName;
	
	@Column(name="ORGANIZATION_NAME")
	private String organizationName;
	
	@Temporal(TemporalType.DATE)
	@Column(name="EFFECTIVE_START_DATE")
	private Date effectiveStartDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="EFFECTIVE_END_DATE")
	private Date effectiveEndDate;
	
	@Column(name="OBJECT_VERSION_NUMBER")
	private Integer versionNumber;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="POSITION_DEFINITION_ID")
	private Integer positionDefinitionId;
	
	@Column(name="JOB_ID")
	private Integer jobId;
	
	@Column(name="ORGANIZATION_ID")
	private Integer organizationId;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DATE_EFFECTIVE")
	private Date dateEffective;
	
	@Column(name="AVAILABILITY_STATUS_ID")
	private Integer availabilityStatusId;
	
	@Column(name="AVAIL_STATUS")
	private String availabilityStatus;
	
	@Column(name="ENTRY_GRADE_ID")
	private Integer entryGradeId;
	
	@Column(name="FTE")
	private Double fte;
	
	@Column(name="MAX_PERSONS")
	private Integer maxPerson; 
	
	@Column(name="PERMANENT_TEMPORARY_FLAG")
	private String permanentTemporaryFlag;
	
	@Column(name="PROBATION_PERIOD")
	private Integer porbationPeriod;
	
	@Column(name="PROBATION_PERIOD_UNIT_CD")
	private String probationPeriodUnitCd;
	
	@Column(name="POSITION_TYPE")
	private String positionType;
	
	@Column(name="SEGMENT2")
	private String postOrganizationId;
	
	@Column(name="LOCATION_ID")
	private String locationId;
	
	@Column(name="POSITION_GROUPING")
	private String positionGroup;
	
	@Column(name="SOURCE_OF_FUNDING")
	private String srcFunding;
	
	@Column(name="LOCATION_DESC")
	private String locationDesc;
	
	@Temporal(TemporalType.DATE)
	@Column(name="POSITION_START_DATE")
	private Date positionStartDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="HIRING_STATUS_START_DATE")
	private Date hiringStatusStartDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="AVAIL_STATUS_PROP_END_DATE")
	private Date hiringStatusPropEndDate;

	public Integer getPositionId() {
		return positionId;
	}

	public void setPositionId(Integer positionId) {
		this.positionId = positionId;
	}

	public String getPositionTitle() {
		return positionTitle;
	}

	public void setPositionTitle(String positionTitle) {
		this.positionTitle = positionTitle;
	}

	public String getPositionOrganization() {
		return positionOrganization;
	}

	public void setPositionOrganization(String positionOrganization) {
		this.positionOrganization = positionOrganization;
	}

	public String getUnitTeam() {
		return unitTeam;
	}

	public void setUnitTeam(String unitTeam) {
		this.unitTeam = unitTeam;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public Date getEffectiveStartDate() {
		return effectiveStartDate;
	}

	public void setEffectiveStartDate(Date effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}
	
	public String getEffectiveStartDateStr() {
		if (effectiveStartDate == null) {
			return "";
		}
		return DateTimeHelper.formatDateToString(effectiveStartDate);
	}

	public Date getEffectiveEndDate() {
		return effectiveEndDate;
	}

	public void setEffectiveEndDate(Date effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}
	
	public String getEffectiveEndDateStr() {
		if (effectiveEndDate == null) {
			return "";
		}
		return DateTimeHelper.formatDateToString(effectiveEndDate);
	}

	public Integer getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPositionDefinitionId() {
		return positionDefinitionId;
	}

	public void setPositionDefinitionId(Integer positionDefinitionId) {
		this.positionDefinitionId = positionDefinitionId;
	}

	public Integer getJobId() {
		return jobId;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Date getDateEffective() {
		return dateEffective;
	}
	
	public String getDateEffectiveStr() {
		if (dateEffective == null) {
			return "";
		}
		return DateTimeHelper.formatDateToString(dateEffective);
	}

	public void setDateEffective(Date dateEffective) {
		this.dateEffective = dateEffective;
	}

	public Integer getAvailabilityStatusId() {
		return availabilityStatusId;
	}

	public void setAvailabilityStatusId(Integer availabilityStatusId) {
		this.availabilityStatusId = availabilityStatusId;
	}

	public Integer getEntryGradeId() {
		return entryGradeId;
	}

	public void setEntryGradeId(Integer entryGradeId) {
		this.entryGradeId = entryGradeId;
	}

	public Double getFte() {
		return fte;
	}

	public void setFte(Double fte) {
		this.fte = fte;
	}

	public Integer getMaxPerson() {
		return maxPerson;
	}

	public void setMaxPerson(Integer maxPerson) {
		this.maxPerson = maxPerson;
	}

	public String getPermanentTemporaryFlag() {
		return permanentTemporaryFlag;
	}

	public void setPermanentTemporaryFlag(String permanentTemporaryFlag) {
		this.permanentTemporaryFlag = permanentTemporaryFlag;
	}

	public Integer getPorbationPeriod() {
		return porbationPeriod;
	}

	public void setPorbationPeriod(Integer porbationPeriod) {
		this.porbationPeriod = porbationPeriod;
	}

	public String getProbationPeriodUnitCd() {
		return probationPeriodUnitCd;
	}

	public void setProbationPeriodUnitCd(String probationPeriodUnitCd) {
		this.probationPeriodUnitCd = probationPeriodUnitCd;
	}

	public String getPositionType() {
		return positionType;
	}

	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}

	public String getPostOrganizationId() {
		return postOrganizationId;
	}

	public void setPostOrganizationId(String postOrganizationId) {
		this.postOrganizationId = postOrganizationId;
	}

	public String getAvailabilityStatus() {
		return availabilityStatus;
	}

	public void setAvailabilityStatus(String availabilityStatus) {
		this.availabilityStatus = availabilityStatus;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getPositionGroup() {
		return positionGroup;
	}

	public void setPositionGroup(String positionGroup) {
		this.positionGroup = positionGroup;
	}

	public String getSrcFunding() {
		return srcFunding;
	}

	public void setSrcFunding(String srcFunding) {
		this.srcFunding = srcFunding;
	}

	public String getLocationDesc() {
		return locationDesc;
	}

	public void setLocationDesc(String locationDesc) {
		this.locationDesc = locationDesc;
	}

	public Date getPositionStartDate() {
		return positionStartDate;
	}

	public void setPositionStartDate(Date positionStartDate) {
		this.positionStartDate = positionStartDate;
	}
	
	public String getPositionStartDateStr() {
		if (positionStartDate == null) {
			return "";
		}
		return DateTimeHelper.formatDateToString(positionStartDate);
	}

	public Date getHiringStatusStartDate() {
		return hiringStatusStartDate;
	}

	public void setHiringStatusStartDate(Date hiringStatusStartDate) {
		this.hiringStatusStartDate = hiringStatusStartDate;
	}
	
	public String getHiringStatusStartDateStr() {
		if (hiringStatusStartDate == null) {
			return "";
		}
		return DateTimeHelper.formatDateToString(hiringStatusStartDate);
	}

	public Date getHiringStatusPropEndDate() {
		return hiringStatusPropEndDate;
	}
	
	public String getHiringStatusPropEndDateStr() {
		if (hiringStatusPropEndDate == null) {
			return "";
		}
		return DateTimeHelper.formatDateToString(hiringStatusPropEndDate);
	}

	public void setHiringStatusPropEndDate(Date hiringStatusPropEndDate) {
		this.hiringStatusPropEndDate = hiringStatusPropEndDate;
	}
}

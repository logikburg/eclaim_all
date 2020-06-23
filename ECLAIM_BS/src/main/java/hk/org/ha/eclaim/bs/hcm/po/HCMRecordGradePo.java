package hk.org.ha.eclaim.bs.hcm.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import hk.org.ha.eclaim.core.helper.DateTimeHelper;

@Entity
@Table(name="V_HCM_REC_GRADE")
public class HCMRecordGradePo {
	@Id
	@Column(name="VALID_GRADE_ID")
	private Integer gradeUid;
	
	@Column(name="GRADE_ID")
	private Integer gradeId;
	
	@Column(name="POSITION_ID")
	private Integer positionId;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DATE_FROM")
	private Date dateFrom;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DATE_TO")
	private Date dateTo;
	
	@Column(name="OBJECT_VERSION_NUMBER")
	private Integer versionNumber;
	
	@Transient
	private String gradeDesc;

	public Integer getGradeUid() {
		return gradeUid;
	}

	public void setGradeUid(Integer gradeUid) {
		this.gradeUid = gradeUid;
	}

	public Integer getGradeId() {
		return gradeId;
	}

	public void setGradeId(Integer gradeId) {
		this.gradeId = gradeId;
	}

	public Integer getPositionId() {
		return positionId;
	}

	public void setPositionId(Integer positionId) {
		this.positionId = positionId;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}
	
	public String getDateFromStr() {
		if (dateFrom != null) {
			return DateTimeHelper.formatDateToString(dateFrom);
		}
		return "";
	}

	public Date getDateTo() {
		return dateTo;
	}
	
	public String getDateToStr() {
		if (dateTo != null) {
			return DateTimeHelper.formatDateToString(dateTo);
		}
		return "";
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public Integer getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getGradeDesc() {
		return gradeDesc;
	}

	public void setGradeDesc(String gradeDesc) {
		this.gradeDesc = gradeDesc;
	}
}

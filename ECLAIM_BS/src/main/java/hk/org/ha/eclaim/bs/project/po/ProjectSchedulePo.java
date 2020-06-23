package hk.org.ha.eclaim.bs.project.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="XXEAL_PROJECT_SCHEDULE")
public class ProjectSchedulePo extends AbstractBasePo{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4364542069707542444L;

	@Column(name="PROJECT_ID")
	private Integer projectId;
	
	@Column(name="PROJECT_VER_ID")
	private Integer projectVerId;
	
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="XXEAL_PROJECT_SCHEDULE_ID_SEQ")
	@SequenceGenerator(name="XXEAL_PROJECT_SCHEDULE_ID_SEQ", sequenceName="XXEAL_PROJECT_SCHEDULE_ID_SEQ", allocationSize=1)
	@Id
	@Column(name="PROJECT_SCHEDULE_ID")
	private Integer projectScheduleId;
	
	@Column(name="PATTERN_CODE")
	private String patternCode;
	
	@Temporal(TemporalType.DATE)
	@Column(name="SCHEDULE_DATE")
	private Date scheduleDate;
	
//	@Temporal(TemporalType.TIME)
	@Column(name="START_TIME")
	private String startTime;
	
//	@Temporal(TemporalType.TIME)
	@Column(name="END_TIME")
	private String endTime;

//	@OneToOne
//	@JoinColumn(name = "PATTERN_CODE", insertable=false, updatable=false)
	@Transient
	private SessionPatternPo patternPo;
	
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

	public Integer getProjectScheduleId() {
		return projectScheduleId;
	}

	public void setProjectScheduleId(Integer projectScheduleId) {
		this.projectScheduleId = projectScheduleId;
	}

	public String getPatternCode() {
		return patternCode;
	}

	public void setPatternCode(String patternCode) {
		this.patternCode = patternCode;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public SessionPatternPo getPatternPo() {
		return patternPo;
	}

	public void setPatternPo(SessionPatternPo patternPo) {
		this.patternPo = patternPo;
	}

	public Date getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(Date scheduleDate) {
		this.scheduleDate = scheduleDate;
	}
	
	
	
}

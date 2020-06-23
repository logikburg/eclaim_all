package hk.org.ha.eclaim.bs.batch.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="CS_BATCH_LOG")
public class BatchLogPo extends AbstractBasePo {
	
	private static final long serialVersionUID = -5995634077461679845L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CS_BATCH_UID_SEQ")
	@SequenceGenerator(name="CS_BATCH_UID_SEQ", sequenceName="CS_BATCH_UID_SEQ", allocationSize=1)
	@Column(name="BATCH_UID")
	private Integer batchUid;
	
	@Column(name="BATCH_NAME")
	private String batchName;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="START_TIME")
	private Date startTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="END_TIME")
	private Date endTime;
	
	@Column(name="BATCH_RESULT")
	private String batchResult;

	public Integer getBatchUid() {
		return batchUid;
	}

	public void setBatchUid(Integer batchUid) {
		this.batchUid = batchUid;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getBatchResult() {
		return batchResult;
	}

	public void setBatchResult(String batchResult) {
		this.batchResult = batchResult;
	}
}

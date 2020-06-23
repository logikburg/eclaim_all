package hk.org.ha.eclaim.bs.batch.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="CS_BATCH_LOG_DTL")
public class BatchLogDtlPo extends AbstractBasePo {

	private static final long serialVersionUID = -4702029567565424031L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CS_BATCH_DTL_UID_SEQ")
	@SequenceGenerator(name="CS_BATCH_DTL_UID_SEQ", sequenceName="CS_BATCH_DTL_UID_SEQ", allocationSize=1)
	@Column(name="BATCH_DTL_UID")
	private Integer batchDtlUid;
	
	@Column(name="BATCH_UID")
	private Integer batchUid;
	
	@Column(name="POST_UID")
	private String postId;
	
	@Column(name="BATCH_DTL_RESULT")
	private String batchDtlResult;

	public Integer getBatchDtlUid() {
		return batchDtlUid;
	}

	public void setBatchDtlUid(Integer batchDtlUid) {
		this.batchDtlUid = batchDtlUid;
	}

	public Integer getBatchUid() {
		return batchUid;
	}

	public void setBatchUid(Integer batchUid) {
		this.batchUid = batchUid;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getBatchDtlResult() {
		return batchDtlResult;
	}

	public void setBatchDtlResult(String batchDtlResult) {
		this.batchDtlResult = batchDtlResult;
	}
}

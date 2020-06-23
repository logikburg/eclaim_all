package hk.org.ha.eclaim.bs.payment.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name = "XXEAL_PAYMENT_JOB")
public class PaymentJobPo extends AbstractBasePo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6542499715324686340L;

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XXEAL_BATCH_JOB_ID_SEQ")
	@SequenceGenerator(name = "XXEAL_BATCH_JOB_ID_SEQ", sequenceName = "XXEAL_BATCH_JOB_ID_SEQ", allocationSize = 1)
	@Id
	@Column(name = "BATCH_JOB_ID")
	private Integer batchJobId;

	@Column(name = "BATCH_ID")
	private Integer batchId;

	@Column(name = "RANK")
	private String rank;

	public Integer getBatchJobId() {
		return batchJobId;
	}

	public void setBatchJobId(Integer batchJobId) {
		this.batchJobId = batchJobId;
	}

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}
}

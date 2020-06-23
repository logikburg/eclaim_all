package hk.org.ha.eclaim.bs.payment.po;

import java.io.Serializable;

// VO to render Job Hours for ranks
public class PaymentJobHoursVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6542499715324686340L;

	private Integer batchId;
	private Integer apprHour;
	private String ranks;
	private Integer prjJobGroupId;
	private Integer usedHour;
	private Integer batchHour;
	private Integer availHour;

	public String getRanks() {
		return ranks;
	}

	public void setRanks(String ranks) {
		this.ranks = ranks;
	}

	public Integer getPrjJobGroupId() {
		return prjJobGroupId;
	}

	public void setPrjJobGroupId(Integer prjJobGroupId) {
		this.prjJobGroupId = prjJobGroupId;
	}

	public Integer getUsedHour() {
		return usedHour;
	}

	public void setUsedHour(Integer usedHour) {
		this.usedHour = usedHour;
	}

	public Integer getBatchHour() {
		return batchHour;
	}

	public void setBatchHour(Integer batchHour) {
		this.batchHour = batchHour;
	}

	public Integer getAvailHour() {
		return availHour;
	}

	public void setAvailHour(Integer availHour) {
		this.availHour = availHour;
	}

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getApprHour() {
		return apprHour;
	}

	public void setApprHour(Integer apprHour) {
		this.apprHour = apprHour;
	}
}

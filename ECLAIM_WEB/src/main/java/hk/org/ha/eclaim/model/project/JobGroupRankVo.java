package hk.org.ha.eclaim.model.project;

import java.io.Serializable;

import javax.persistence.Column;

public class JobGroupRankVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -137505123467247523L;

	@Column(name = "JOB_GROUP_ID")
	private Integer jobGroupId;

	@Column(name = "RANK")
	private String rank;

	public Integer getJobGroupId() {
		return jobGroupId;
	}

	public void setJobGroupId(Integer jobGroupId) {
		this.jobGroupId = jobGroupId;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

}

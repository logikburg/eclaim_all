package hk.org.ha.eclaim.bs.project.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="XXEAL_PRJ_JOB_DETAIL")
public class ProjectJobRankPo {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="XXEAL_PRJ_JOB_DETAIL_ID_SEQ")
	@SequenceGenerator(name="XXEAL_PRJ_JOB_DETAIL_ID_SEQ", sequenceName="XXEAL_PRJ_JOB_DETAIL_ID_SEQ", allocationSize=1)
	@Column(name="PRJ_JOB_DETAIL_ID")
	private Integer prjJobDetailId;
	
	@Column(name="JOB_GROUP_ID")
	private Integer jobGroupId;
	
	@Column(name="RANK")
	private String rank;

	public Integer getPrjJobDetailId() {
		return prjJobDetailId;
	}

	public void setPrjJobDetailId(Integer prjJobDetailId) {
		this.prjJobDetailId = prjJobDetailId;
	}

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

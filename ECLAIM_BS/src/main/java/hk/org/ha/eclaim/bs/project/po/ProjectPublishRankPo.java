package hk.org.ha.eclaim.bs.project.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name = "XXEAL_PUBLISH_PROJ_RANK")
public class ProjectPublishRankPo extends AbstractBasePo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6542499715324686340L;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "PUBLISH_ID", referencedColumnName = "PUBLISH_ID"),
			@JoinColumn(name = "JOB_GROUP_ID", referencedColumnName = "JOB_GROUP_ID") })
	private ProjectPublishJobPo publishJobs;

	@Column(name = "RANK")
	private String rank;

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public ProjectPublishJobPo getPublishJobs() {
		return publishJobs;
	}

	public void setPublishJobs(ProjectPublishJobPo publishJobs) {
		this.publishJobs = publishJobs;
	}
}

package hk.org.ha.eclaim.bs.project.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
//@IdClass(PublishJobId.class)
@Table(name = "XXEAL_PUBLISH_PROJ_JOB")
public class ProjectPublishJobPo extends AbstractBasePo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6542499715324686340L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XXEAL_PUBLISH_JOB_ID_SEQ")
	@SequenceGenerator(name = "XXEAL_PUBLISH_JOB_ID_SEQ", sequenceName = "XXEAL_PUBLISH_JOB_ID_SEQ", allocationSize = 1)
	@Column(name = "PUBLISH_JOB_ID")
	private Integer publishJobId;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "PUBLISH_ID", referencedColumnName = "PUBLISH_ID")
//	private ProjectPublishPo projectPublishPo;

//	@OneToOne(fetch = FetchType.LAZY)
//	@JoinColumns({ @JoinColumn(name = "PUBLISH_ID", referencedColumnName = "PUBLISH_ID"), 
//				   @JoinColumn(name = "PROJECT_VER_ID", referencedColumnName = "PROJECT_VER_ID", insertable = false, updatable = false),
//				   @JoinColumn(name = "PROJECT_ID", referencedColumnName = "PROJECT_ID", insertable = false, updatable = false) })

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PUBLISH_ID")
	private ProjectPublishPo projectPublishPo;

	@Column(name = "JOB_GROUP_ID")
	private Integer jobGroupId;
	
	@OneToMany(mappedBy = "publishJobs")
	private List<ProjectPublishRankPo> projectPublishRank;

	@Column(name = "STAFF_GROUP")
	private String staffGroup;

	@Column(name = "INST_CODE")
	private String instCode;

	@Column(name = "DEPT_CODE")
	private String deptCode;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DEADLINE")
	private Date deadline;

	@Column(name = "JOB_DESCRIPTION")
	private String jobDescription;

	public ProjectPublishJobPo() {
		projectPublishRank = new ArrayList<ProjectPublishRankPo>();
	}

	public Integer getJobGroupId() {
		return jobGroupId;
	}

	public void setJobGroupId(Integer jobGroupId) {
		this.jobGroupId = jobGroupId;
	}

	public String getStaffGroup() {
		return staffGroup;
	}

	public void setStaffGroup(String staffGroup) {
		this.staffGroup = staffGroup;
	}

	public String getInstCode() {
		return instCode;
	}

	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public List<ProjectPublishRankPo> getProjectPublishRank() {
		return projectPublishRank;
	}

	public void setProjectPublishRank(List<ProjectPublishRankPo> projectPublishRank) {
		this.projectPublishRank = projectPublishRank;
	}

	public ProjectPublishPo getProjectPublishPo() {
		return projectPublishPo;
	}

	public void setProjectPublishPo(ProjectPublishPo projectPublishPo) {
		this.projectPublishPo = projectPublishPo;
	}
}

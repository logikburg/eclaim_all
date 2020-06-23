package hk.org.ha.eclaim.bs.project.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
//@IdClass(ProjectId.class)
//@IdClass(PublishId.class)
@Table(name = "XXEAL_PUBLISH_PROJ")
public class ProjectPublishPo extends AbstractBasePo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6542499715324686340L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XXEAL_PUBLISH_PROJ_ID_SEQ")
	@SequenceGenerator(name = "XXEAL_PUBLISH_PROJ_ID_SEQ", sequenceName = "XXEAL_PUBLISH_PROJ_ID_SEQ", allocationSize = 1)
	@Column(name = "PUBLISH_ID")
	private Integer publishId;

	@Column(name = "PROJECT_ID")
	private Integer projectId;

	@Column(name = "PROJECT_VER_ID")
	private Integer projectVerId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PUBLISH_DATE")
	private Date publishDate;

	@Column(name = "OTHER_INFO")
	private String otherInfo;

	@Column(name = "TARGET_APPLICANT")
	private String targetApplicant;

	// @OneToOne(mappedBy="prjPublish", cascade=CascadeType.ALL)
	// private ProjectPublishJobPo projectPublishJob;

	// @OneToMany(mappedBy="prjPublish", cascade=CascadeType.ALL)
	// private List<ProjectPublishJobPo> projectPublishJob = new
	// ArrayList<ProjectPublishJobPo>();

	// 5:20pm
	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "PUBLISH_ID", insertable = false, updatable = false)
	//private ProjectPublishJobPo projectPublishJob;

	@OneToMany(mappedBy = "projectPublishPo")
	private List<ProjectPublishJobPo> projectPublishJobList;

	public ProjectPublishPo() {
		projectPublishJobList = new ArrayList<ProjectPublishJobPo>();
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getOtherInfo() {
		return otherInfo;
	}

	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
	}

	public String getTargetApplicant() {
		return targetApplicant;
	}

	public void setTargetApplicant(String targetApplicant) {
		this.targetApplicant = targetApplicant;
	}

	public Integer getPublishId() {
		return publishId;
	}

	public void setPublishId(Integer publishId) {
		this.publishId = publishId;
	}

	public List<ProjectPublishJobPo> getProjectPublishJobList() {
		return projectPublishJobList;
	}

	public void setProjectPublishJobList(List<ProjectPublishJobPo> projectPublishJobList) {
		this.projectPublishJobList = projectPublishJobList;
	}

	public Integer getProjectVerId() {
		return projectVerId;
	}

	public void setProjectVerId(Integer projectVerId) {
		this.projectVerId = projectVerId;
	}
}

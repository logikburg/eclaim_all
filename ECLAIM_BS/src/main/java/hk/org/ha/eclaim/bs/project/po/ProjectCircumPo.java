package hk.org.ha.eclaim.bs.project.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="XXEAL_SHS_PROJECT_CIRCUM")
public class ProjectCircumPo extends AbstractBasePo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6648779809973516083L;
	
	
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="XXEAL_SHS_PRJ_CIRCUM_ID_SEQ")
	@SequenceGenerator(name="XXEAL_SHS_PRJ_CIRCUM_ID_SEQ", sequenceName="XXEAL_SHS_PRJ_CIRCUM_ID_SEQ", allocationSize=1)
	@Id
	@Column(name="PROJECT_CIRCUM_UID")
	private Integer projectCircumUid;
	
	@Column(name="PROJECT_ID")
	private Integer projectId;
	
	@Column(name="PROJECT_VER_ID")
	private Integer projectVerId;
	
	@Column(name="CIRCUMSTANCE_ID")
	private Integer circumstanceId;
	
	@Column(name="Q_DELIVERABLES")
	private String qDeliverables;
	
	
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
	public Integer getCircumstanceId() {
		return circumstanceId;
	}
	public void setCircumstanceId(Integer circumstanceId) {
		this.circumstanceId = circumstanceId;
	}
	public String getqDeliverables() {
		return qDeliverables;
	}
	public void setqDeliverables(String qDeliverables) {
		this.qDeliverables = qDeliverables;
	}
	public Integer getProjectCircumUid() {
		return projectCircumUid;
	}
	public void setProjectCircumUid(Integer projectCircumUid) {
		this.projectCircumUid = projectCircumUid;
	}
	
	
}

package hk.org.ha.eclaim.bs.project.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import hk.org.ha.eclaim.bs.security.po.RolePo;
import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="XXEAL_APPROVAL_HISTORY")
public class ApprovalHistoryPo extends AbstractBasePo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5190213085418560437L;

	@Column(name="PROJECT_ID")
	private Integer projectId;
	
	@Column(name="PROJECT_VER_ID")
	private Integer projectVerId;
	
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="XXEAL_APPROVAL_HISTORY_ID_SEQ")
	@SequenceGenerator(name="XXEAL_APPROVAL_HISTORY_ID_SEQ", sequenceName="XXEAL_APPROVAL_HISTORY_ID_SEQ", allocationSize=1)
	@Id
	@Column(name="APPR_HIST_UID")
	private Integer apprHistUid;
	
	@Column(name="ACTION_BY")
	private String actionBy;
	
	@Column(name="ROLE_CODE")
	private String roleCode;

	@Column(name="ACTION_CODE")
	private String actionCode;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ACTION_DATE")
	private Date actionDate;

	@Column(name="REMARK")
	private String remark;

	@OneToOne
	@JoinColumn(name = "ACTION_CODE", insertable=false, updatable=false)
	private ApprovalActionPo action;
	
	@OneToOne
	@JoinColumn(name = "ROLE_CODE", referencedColumnName="ROLE_ID", insertable=false, updatable=false)
	private RolePo role;
	
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

	public Integer getApprHistUid() {
		return apprHistUid;
	}

	public void setApprHistUid(Integer apprHistUid) {
		this.apprHistUid = apprHistUid;
	}

	public String getActionBy() {
		return actionBy;
	}

	public void setActionBy(String actionBy) {
		this.actionBy = actionBy;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getActionCode() {
		return actionCode;
	}

	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public ApprovalActionPo getAction() {
		return action;
	}

	public void setAction(ApprovalActionPo action) {
		this.action = action;
	}

	public RolePo getRole() {
		return role;
	}

	public void setRole(RolePo role) {
		this.role = role;
	}
}

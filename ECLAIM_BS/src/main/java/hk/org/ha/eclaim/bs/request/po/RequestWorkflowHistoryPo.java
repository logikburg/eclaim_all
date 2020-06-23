package hk.org.ha.eclaim.bs.request.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import hk.org.ha.eclaim.core.helper.DateTimeHelper;
import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="XXEAL_WORKFLOW_HIST")
public class RequestWorkflowHistoryPo extends AbstractBasePo{
	private static final long serialVersionUID = -8837970217822665542L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="XXEAL_WORKFLOW_HIST_SEQ")
	@SequenceGenerator(name="XXEAL_WORKFLOW_HIST_SEQ", sequenceName="XXEAL_WORKFLOW_HIST_SEQ", allocationSize=1)
	@Column(name="WF_HIST_UID")
	private int wfHistoryUid;
	
	@Column(name="PROJECT_ID")
	private int projectId;
	
	@Column(name="PROJECT_VER_ID")
	private int projectVersionId;

	@Column(name="ACTION_BY")
	private String actionBy;
	
	@Column(name="ACTION_ROLE_ID")
	private String actionRoleId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ACTION_DATE")
	private Date actionDate;
	
	@Column(name="ACTION_TAKEN")
	private String actionTaken;
	
	@Column(name="SENT_TO_USER_ID")
	private String sentToUserId;
	
	@Column(name="SENT_TO_ROLE_ID")
	private String sentToRoleId;
	
	@Transient
	private String actionByName;

	public int getWfHistoryUid() {
		return wfHistoryUid;
	}

	public void setWfHistoryUid(int wfHistoryUid) {
		this.wfHistoryUid = wfHistoryUid;
	}

	public String getActionBy() {
		return actionBy;
	}

	public void setActionBy(String actionBy) {
		this.actionBy = actionBy;
	}

	public String getActionRoleId() {
		return actionRoleId;
	}

	public void setActionRoleId(String actionRoleId) {
		this.actionRoleId = actionRoleId;
	}

	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}
	
	public String getActionDateStr() {
		return DateTimeHelper.formatDateTimeToString(this.actionDate);
	}

	public String getActionTaken() {
		return actionTaken;
	}

	public void setActionTaken(String actionTaken) {
		this.actionTaken = actionTaken;
	}

	public String getSentToUserId() {
		return sentToUserId;
	}

	public void setSentToUserId(String sentToUserId) {
		this.sentToUserId = sentToUserId;
	}

	public String getSentToRoleId() {
		return sentToRoleId;
	}

	public void setSentToRoleId(String sentToRoleId) {
		this.sentToRoleId = sentToRoleId;
	}

	public String getActionByName() {
		return actionByName;
	}

	public void setActionByName(String actionByName) {
		this.actionByName = actionByName;
	}

	public int getProjectVersionId() {
		return projectVersionId;
	}

	public void setProjectVersionId(int projectVersionId) {
		this.projectVersionId = projectVersionId;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
}

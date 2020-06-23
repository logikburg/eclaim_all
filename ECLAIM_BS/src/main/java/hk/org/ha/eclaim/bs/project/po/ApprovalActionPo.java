package hk.org.ha.eclaim.bs.project.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="XXEAL_APPROVAL_ACTION")
public class ApprovalActionPo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 624641682369627222L;

	@Id
	@Column(name="ACTION_CODE")
	private String actionCode;
	
	@Column(name="ACTION_NAME")
	private String actionName;

	public String getActionCode() {
		return actionCode;
	}

	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}

package hk.org.ha.eclaim.bs.hcm.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(PK_HCMResponsibilityPo.class)
@Table(name="V_HCM_RESPONSIBILITY")
public class HCMResponsibilityPo {
	@Id
	@Column(name="USER_NAME")
	private String userId;
	
	@Id
	@Column(name="RESPONSIBILITY_ID")
	private Integer responsibilityId;
	
	@Column(name="RESPONSIBILITY_NAME")
	private String responsibilityName;

	public Integer getResponsibilityId() {
		return responsibilityId;
	}

	public void setResponsibilityId(Integer responsibilityId) {
		this.responsibilityId = responsibilityId;
	}

	public String getResponsibilityName() {
		return responsibilityName;
	}

	public void setResponsibilityName(String responsibilityName) {
		this.responsibilityName = responsibilityName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}

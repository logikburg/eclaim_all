package hk.org.ha.eclaim.bs.hcm.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="V_HCM_ORGANIZATION")
public class HCMOrganizationPo {
	@Id
	@Column(name="ORGANIZATION_ID")
	private String organizatonId;
	
	@Column(name="NAME")
	private String organizatonName;

	public String getOrganizatonId() {
		return organizatonId;
	}

	public void setOrganizatonId(String organizatonId) {
		this.organizatonId = organizatonId;
	}

	public String getOrganizatonName() {
		return organizatonName;
	}

	public void setOrganizatonName(String organizatonName) {
		this.organizatonName = organizatonName;
	}
}

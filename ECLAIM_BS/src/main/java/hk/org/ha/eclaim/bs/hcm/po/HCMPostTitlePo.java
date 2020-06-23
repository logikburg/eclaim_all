package hk.org.ha.eclaim.bs.hcm.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="V_HCM_POSITION_TITLE")
public class HCMPostTitlePo {
	@Id
	@Column(name="flex_value")
	private String titleId;
	
	@Column(name="description")
	private String titleDescription;
	
	@Column(name="STAFF_GROUP")
	private String staffGroup;

	public String getTitleId() {
		return titleId;
	}

	public void setTitleId(String titleId) {
		this.titleId = titleId;
	}

	public String getTitleDescription() {
		return titleDescription;
	}

	public void setTitleDescription(String titleDescription) {
		this.titleDescription = titleDescription;
	}

	public String getStaffGroup() {
		return staffGroup;
	}

	public void setStaffGroup(String staffGroup) {
		this.staffGroup = staffGroup;
	}
}

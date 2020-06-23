package hk.org.ha.eclaim.bs.cs.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="CS_STAFF_GROUP")
public class StaffGroupPo  extends AbstractBasePo {
	
	private static final long serialVersionUID = 4289292977006267100L;

	@Id
	@Column(name="STAFF_GROUP_CODE")
	private String staffGroupCode;

	@Column(name="STAFF_GROUP_NAME")
	private String staffGroupName;
	
	@Column(name="STAFF_GROUP_ABBR")
	private String staffGroupAbbr;

	public String getStaffGroupCode() {
		return staffGroupCode;
	}

	public void setStaffGroupCode(String staffGroupCode) {
		this.staffGroupCode = staffGroupCode;
	}

	public String getStaffGroupName() {
		return staffGroupName;
	}

	public void setStaffGroupName(String staffGroupName) {
		this.staffGroupName = staffGroupName;
	}

	public String getStaffGroupAbbr() {
		return staffGroupAbbr;
	}

	public void setStaffGroupAbbr(String staffGroupAbbr) {
		this.staffGroupAbbr = staffGroupAbbr;
	}
}

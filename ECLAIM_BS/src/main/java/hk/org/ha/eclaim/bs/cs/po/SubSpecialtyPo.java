package hk.org.ha.eclaim.bs.cs.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="CS_SUB_SPECIALTY")
public class SubSpecialtyPo extends AbstractBasePo {
	
	private static final long serialVersionUID = 8580049002287161379L;

	@Id
	@Column(name="SUB_SPECIALTY_CODE")
	private String subSpecialtyCode;
	
	@Column(name="SUB_SPECIALTY_DESC")
	private String subSpecialtyDesc;
	
	@Column(name="STAFF_GROUP_CODE")
	private String staffGroupCode;
	
	@Column(name="DEPT_CODE")
	private String deptCode;

	public String getSubSpecialtyCode() {
		return subSpecialtyCode;
	}

	public void setSubSpecialtyCode(String subSpecialtyCode) {
		this.subSpecialtyCode = subSpecialtyCode;
	}

	public String getSubSpecialtyDesc() {
		return subSpecialtyDesc;
	}

	public void setSubSpecialtyDesc(String subSpecialtyDesc) {
		this.subSpecialtyDesc = subSpecialtyDesc;
	}

	public String getStaffGroupCode() {
		return staffGroupCode;
	}

	public void setStaffGroupCode(String staffGroupCode) {
		this.staffGroupCode = staffGroupCode;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
}
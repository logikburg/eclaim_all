package hk.org.ha.eclaim.bs.cs.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="CS_DEPT")
public class DepartmentPo extends AbstractBasePo {
	
	private static final long serialVersionUID = 8455932862687389279L;

	@Id
	@Column(name="DEPT_CODE")
	private String deptCode;
	
	@Column(name="DEPT_NAME")
	private String deptName;
	
	@Column(name="STAFF_GROUP_CODE")
	private String staffGroupCode;
	
	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getStaffGroupCode() {
		return staffGroupCode;
	}

	public void setStaffGroupCode(String staffGroupCode) {
		this.staffGroupCode = staffGroupCode;
	}
	
}

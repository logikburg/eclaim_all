package hk.org.ha.eclaim.bs.hcm.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="V_HCM_EMPLOYEE")
public class HCMEmployeePo {
	@Id
	@Column(name="employee_id")
	private String employeeId;

	@Column(name="employee_name")
	private String employeeName;

	@Column(name="rank")
	private String rank;

	@Column(name="emp_term")
	private String empTerm;
	
	@Column(name="emp_type")
	private String empType;

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getEmpTerm() {
		return empTerm;
	}

	public void setEmpTerm(String empTerm) {
		this.empTerm = empTerm;
	}

	public String getEmpType() {
		return empType;
	}

	public void setEmpType(String empType) {
		this.empType = empType;
	}
}

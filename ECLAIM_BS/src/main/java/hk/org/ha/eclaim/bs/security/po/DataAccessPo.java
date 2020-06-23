package hk.org.ha.eclaim.bs.security.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="SS_USER_ROLE_DATA_ACCESS")
public class DataAccessPo extends AbstractBasePo {
	
	private static final long serialVersionUID = -4710703675411167679L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SS_USER_ROLE_DATA_ACC_UID_SEQ")
	@SequenceGenerator(name="SS_USER_ROLE_DATA_ACC_UID_SEQ", sequenceName="SS_USER_ROLE_DATA_ACC_UID_SEQ", allocationSize=1)
	@Column(name="USER_ROLE_DATA_ACCESS_UID")
	private Integer dataAccessUid;
	
	@Column(name="USER_ROLE_UID")
	private Integer roleId;
	
	@Column(name="CLUSTER_CODE")
	private String clusterCode;
	
	@Column(name="INST_CODE")
	private String instCode;
	
	@Column(name="DEPT_CODE")
	private String deptCode;
	
	@Column(name="STAFF_GROUP_CODE")
	private String staffGroupCode;
	
	public Integer getDataAccessUid() {
		return dataAccessUid;
	}

	public void setDataAccessUid(Integer dataAccessUid) {
		this.dataAccessUid = dataAccessUid;
	}

	public String getClusterCode() {
		return clusterCode;
	}

	public void setClusterCode(String clusterCode) {
		this.clusterCode = clusterCode;
	}

	public String getInstCode() {
		return instCode;
	}

	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getStaffGroupCode() {
		return staffGroupCode;
	}

	public void setStaffGroupCode(String staffGroupCode) {
		this.staffGroupCode = staffGroupCode;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
}

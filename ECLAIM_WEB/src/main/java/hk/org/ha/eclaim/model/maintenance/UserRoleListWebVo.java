package hk.org.ha.eclaim.model.maintenance;

import java.util.List;

public class UserRoleListWebVo {
	private List<String> roleIdList;
	private List<String> roleNameList;
	private List<String> clusterCodeList;
	private List<String> instCodeList;
	private List<String> deptCodeList;
	private List<String> staffGroupCodeList;
	private int responsibilityCount;
	
	public List<String> getRoleNameList() {
		return roleNameList;
	}
	
	public void setRoleNameList(List<String> roleNameList) {
		this.roleNameList = roleNameList;
	}
	
	public int getRoleCount() {
		return this.getRoleIdList().size();
	}

	public List<String> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<String> roleIdList) {
		this.roleIdList = roleIdList;
	}

	public List<String> getClusterCodeList() {
		return clusterCodeList;
	}

	public void setClusterCodeList(List<String> clusterCodeList) {
		this.clusterCodeList = clusterCodeList;
	}

	public List<String> getInstCodeList() {
		return instCodeList;
	}

	public void setInstCodeList(List<String> instCodeList) {
		this.instCodeList = instCodeList;
	}

	public List<String> getDeptCodeList() {
		return deptCodeList;
	}

	public void setDeptCodeList(List<String> deptCodeList) {
		this.deptCodeList = deptCodeList;
	}

	public List<String> getStaffGroupCodeList() {
		return staffGroupCodeList;
	}

	public void setStaffGroupCodeList(List<String> staffGroupCodeList) {
		this.staffGroupCodeList = staffGroupCodeList;
	}

	public int getResponsibilityCount() {
		return responsibilityCount;
	}

	public void setResponsibilityCount(int responsibilityCount) {
		this.responsibilityCount = responsibilityCount;
	}
}

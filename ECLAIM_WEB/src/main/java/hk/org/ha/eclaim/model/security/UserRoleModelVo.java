package hk.org.ha.eclaim.model.security;

public class UserRoleModelVo {

	private int userRoleUid;
	private String roleId;
	private String roleName;

	public int getUserRoleUid() {
		return userRoleUid;
	}

	public void setUserRoleUid(int userRoleUid) {
		this.userRoleUid = userRoleUid;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}

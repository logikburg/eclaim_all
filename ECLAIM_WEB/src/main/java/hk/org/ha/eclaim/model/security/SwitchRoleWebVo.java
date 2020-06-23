package hk.org.ha.eclaim.model.security;

import java.util.ArrayList;
import java.util.List;

import hk.org.ha.eclaim.bs.security.po.RolePo;

public class SwitchRoleWebVo {
	private List<RolePo> roleList = new ArrayList<RolePo>();
	
	private String userName;
	private String updateSuccess;
	private String displayMessage;
	private String selectedRoleId;
	private String currentRole;
	private String formAction;

	public void addRoleList(RolePo role) {
		roleList.add(role);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUpdateSuccess() {
		return updateSuccess;
	}

	public void setUpdateSuccess(String updateSuccess) {
		this.updateSuccess = updateSuccess;
	}

	public String getDisplayMessage() {
		return displayMessage;
	}

	public void setDisplayMessage(String displayMessage) {
		this.displayMessage = displayMessage;
	}

	public List<RolePo> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<RolePo> roleList) {
		this.roleList = roleList;
	}

	public String getSelectedRoleId() {
		return selectedRoleId;
	}

	public void setSelectedRoleId(String selectedRoleId) {
		this.selectedRoleId = selectedRoleId;
	}

	public String getCurrentRole() {
		return currentRole;
	}

	public void setCurrentRole(String currentRole) {
		this.currentRole = currentRole;
	}

	public String getFormAction() {
		return formAction;
	}

	public void setFormAction(String formAction) {
		this.formAction = formAction;
	}
}

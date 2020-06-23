package hk.org.ha.eclaim.model.maintenance;

import java.util.List;

public class UserMaintenanceWebVo {
	private String userId;
	private String userName;
	private String searchUserId;
	private String searchUserName;
	
	private String updateUserId;
	
	private List<String> ddRoleId;
	private List<String> ddCluster;
	private List<String> ddInstitute;
	private List<String> ddDept;
	private List<String> ddStaffGroup;
	
	private String editUserId;
	private String editUserName;
	private String editPhone;
	private String editEmail;
	private String editState;
	
	private String formAction;
	private String haveResult;
	private List<UserVo> searchResultList;
	
	private String updateSuccess;
	private String displayMessage;
	
	private String cidLoginUrl;
	
	private String domainUserId;
	private String returnCode;
	
	private String hcmResponsibilityCount;
	private String isExistingUser;
	private String isExistingUserInOtherCluster;
	private String currentRole;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<UserVo> getSearchResultList() {
		return searchResultList;
	}

	public void setSearchResultList(List<UserVo> searchResultList) {
		this.searchResultList = searchResultList;
	}

	public String getHaveResult() {
		return haveResult;
	}

	public void setHaveResult(String haveResult) {
		this.haveResult = haveResult;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFormAction() {
		return formAction;
	}

	public void setFormAction(String formAction) {
		this.formAction = formAction;
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

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getEditUserId() {
		return editUserId;
	}

	public void setEditUserId(String editUserId) {
		this.editUserId = editUserId;
	}

	public String getEditUserName() {
		return editUserName;
	}

	public void setEditUserName(String editUserName) {
		this.editUserName = editUserName;
	}

	public String getEditPhone() {
		return editPhone;
	}

	public void setEditPhone(String editPhone) {
		this.editPhone = editPhone;
	}

	public String getEditEmail() {
		return editEmail;
	}

	public void setEditEmail(String editEmail) {
		this.editEmail = editEmail;
	}

	public String getEditState() {
		return editState;
	}

	public void setEditState(String editState) {
		this.editState = editState;
	}

	public String getSearchResultListSize() {
		return String.valueOf(searchResultList.size());
	}

	public List<String> getDdCluster() {
		return ddCluster;
	}

	public void setDdCluster(List<String> ddCluster) {
		this.ddCluster = ddCluster;
	}

	public List<String> getDdInstitute() {
		return ddInstitute;
	}

	public void setDdInstitute(List<String> ddInstitute) {
		this.ddInstitute = ddInstitute;
	}

	public List<String> getDdDept() {
		return ddDept;
	}

	public void setDdDept(List<String> ddDept) {
		this.ddDept = ddDept;
	}

	public List<String> getDdStaffGroup() {
		return ddStaffGroup;
	}

	public void setDdStaffGroup(List<String> ddStaffGroup) {
		this.ddStaffGroup = ddStaffGroup;
	}

	public String getCidLoginUrl() {
		return cidLoginUrl;
	}

	public void setCidLoginUrl(String cidLoginUrl) {
		this.cidLoginUrl = cidLoginUrl;
	}

	public String getSearchUserId() {
		return searchUserId;
	}

	public void setSearchUserId(String searchUserId) {
		this.searchUserId = searchUserId;
	}

	public String getSearchUserName() {
		return searchUserName;
	}

	public void setSearchUserName(String searchUserName) {
		this.searchUserName = searchUserName;
	}

	public String getDomainUserId() {
		return domainUserId;
	}

	public void setDomainUserId(String domainUserId) {
		this.domainUserId = domainUserId;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public List<String> getDdRoleId() {
		return ddRoleId;
	}

	public void setDdRoleId(List<String> ddRoleId) {
		this.ddRoleId = ddRoleId;
	}

	public String getHcmResponsibilityCount() {
		return hcmResponsibilityCount;
	}

	public void setHcmResponsibilityCount(String hcmResponsibilityCount) {
		this.hcmResponsibilityCount = hcmResponsibilityCount;
	}

	public String getIsExistingUser() {
		return isExistingUser;
	}

	public void setIsExistingUser(String isExistingUser) {
		this.isExistingUser = isExistingUser;
	}

	public String getIsExistingUserInOtherCluster() {
		return isExistingUserInOtherCluster;
	}

	public void setIsExistingUserInOtherCluster(String isExistingUserInOtherCluster) {
		this.isExistingUserInOtherCluster = isExistingUserInOtherCluster;
	}

	public String getCurrentRole() {
		return currentRole;
	}

	public void setCurrentRole(String currentRole) {
		this.currentRole = currentRole;
	}

}

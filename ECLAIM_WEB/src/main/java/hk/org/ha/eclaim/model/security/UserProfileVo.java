package hk.org.ha.eclaim.model.security;

import java.util.List;

public class UserProfileVo {

	private String userId;
	private String domain;
	private String userName;
	private String lastLogonDate;
	private String lastLogonResult;
	private String environment;
	private String swVersion;
	private String url;
	private String bsdLoginUrl;

	private List<UserRoleModelVo> userRoles;
	private List<String> functionList;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLastLogonDate() {
		return lastLogonDate;
	}

	public void setLastLogonDate(String lastLogonDate) {
		this.lastLogonDate = lastLogonDate;
	}

	public String getLastLogonResult() {
		return lastLogonResult;
	}

	public void setLastLogonResult(String lastLogonResult) {
		this.lastLogonResult = lastLogonResult;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getSwVersion() {
		return swVersion;
	}

	public void setSwVersion(String swVersion) {
		this.swVersion = swVersion;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBsdLoginUrl() {
		return bsdLoginUrl;
	}

	public void setBsdLoginUrl(String bsdLoginUrl) {
		this.bsdLoginUrl = bsdLoginUrl;
	}

	public List<UserRoleModelVo> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<UserRoleModelVo> userRoles) {
		this.userRoles = userRoles;
	}

	public List<String> getFunctionList() {
		return functionList;
	}

	public void setFunctionList(List<String> functionList) {
		this.functionList = functionList;
	}

}

package hk.org.ha.eclaim.model.login;

public class LoginPageWebVo {

	private String userId;
	private String password;
	private String domain;
	private String environment;
	private String swVersion;
	private String url;
	private String bsdLoginUrl;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
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

}
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
@Table(name="SS_USER_LOGON")
public class UserLogonPo extends AbstractBasePo {
	
	private static final long serialVersionUID = 6783761927034256491L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SS_USER_LOGON_UID_SEQ")
	@SequenceGenerator(name="SS_USER_LOGON_UID_SEQ", sequenceName="SS_USER_LOGON_UID_SEQ", allocationSize=1)
	@Column(name="USER_LOGON_UID")
	private Integer userLogonUid;
	
	@Column(name="USER_ID")
	private String userId;
	
	@Column(name="DOMAIN")
	private String domain;
	
	@Column(name="LOGIN_STATUS")
	private String loginStatus;
	
	@Column(name="SESSION_TOKEN")
	private String sessionToken;
	
	@Column(name="HOST_NAME")
	private String hostName;
	
	@Column(name="BROWSER")
	private String browser;
	
	@Column(name="FAIL_REASON")
	private String failReason;
	
	public Integer getUserLogonUid() {
		return userLogonUid;
	}

	public void setUserLogonUid(Integer userLogonUid) {
		this.userLogonUid = userLogonUid;
	}

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

	public String getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

}

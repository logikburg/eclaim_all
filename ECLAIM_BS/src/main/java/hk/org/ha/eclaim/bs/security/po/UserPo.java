package hk.org.ha.eclaim.bs.security.po;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name = "SS_USER")
public class UserPo extends AbstractBasePo {
	
	private static final long serialVersionUID = -1566071368191957859L;

	@Id
	@Column(name = "USER_ID")
	private String userId;
	
	@Column(name = "USER_NAME")
	private String userName;
	
	@Column(name = "PHONE_NO")
	private String phoneNo;
	
	@Column(name = "EMAIL_ADDR")
	private String email;
	
	@Column(name = "ACCOUNT_STATUS")
	private String accountStatus;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy="user", cascade = CascadeType.ALL)
	private List<UserRolePo> userRole = new ArrayList<UserRolePo>();

	//@Transient
	//private List<String> functionList = new ArrayList<String>();
	
	@Transient
	private String currentRole;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<UserRolePo> getUserRole() {
		return userRole;
	}

	public void setUserRole(List<UserRolePo> userRole) {
		this.userRole = userRole;
	}
	
	/*public List<String> getFunctionList() {
		return functionList;
	}

	public void setFunctionList(List<String> functionList) {
		this.functionList = functionList;
	}*/

	public String getCurrentRole() {
		return currentRole;
	}

	public void setCurrentRole(String currentRole) {
		this.currentRole = currentRole;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	

	@Transient
	public String getUserRoleList() {
		String result = "";
		
		for (int i=0; i<this.userRole.size(); i++) {
			if (i != 0) 
				result += ", ";
			
			result += userRole.get(i).getRole().getRoleName();
		}
		return result;
	}
}

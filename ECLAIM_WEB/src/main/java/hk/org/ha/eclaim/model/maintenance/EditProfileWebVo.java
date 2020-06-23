package hk.org.ha.eclaim.model.maintenance;

public class EditProfileWebVo {
	private String userId;
	private String userName;
	private String phone;
	private String email;
	private String updateSuccess;
	private String displayMessage;
	
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
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
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
}

package hk.org.ha.eclaim.model.maintenance;

public class NewsMaintenanceDetailWebVo {
	
	private String newsUid;
	private String newsDesc;
	private String status;
	private String effectiveFrom;
	private String effectiveTo;
	private String lastUpdatedDate;
	private String userName;
	private String hiddenNewsUid;
	private String updateSuccess;
	private String displayMessage;
	
	public String getNewsUid() {
		return newsUid;
	}
	
	public void setNewsUid(String newsUid) {
		this.newsUid = newsUid;
	}

	public String getNewsDesc() {
		return newsDesc;
	}

	public void setNewsDesc(String newsDesc) {
		this.newsDesc = newsDesc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEffectiveFrom() {
		return effectiveFrom;
	}

	public void setEffectiveFrom(String effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public String getEffectiveTo() {
		return effectiveTo;
	}

	public void setEffectiveTo(String effectiveTo) {
		this.effectiveTo = effectiveTo;
	}

	public String getUpdateSuccess() {
		return updateSuccess;
	}

	public void setUpdateSuccess(String updateSuccess) {
		this.updateSuccess = updateSuccess;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getDisplayMessage() {
		return displayMessage;
	}

	public void setDisplayMessage(String displayMessage) {
		this.displayMessage = displayMessage;
	}

	public String getHiddenNewsUid() {
		return hiddenNewsUid;
	}

	public void setHiddenNewsUid(String hiddenNewsUid) {
		this.hiddenNewsUid = hiddenNewsUid;
	}
}

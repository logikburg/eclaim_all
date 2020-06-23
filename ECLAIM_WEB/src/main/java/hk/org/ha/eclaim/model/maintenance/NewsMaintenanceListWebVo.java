package hk.org.ha.eclaim.model.maintenance;

import java.util.List;


public class NewsMaintenanceListWebVo {
	private List<NewsWebVo> allNews;
	private String deleteNewsUid;
	private String updateSuccess;
	private String errorMessage;
	private String displayMessage;
	private String userName;

	public List<NewsWebVo> getAllNews() {
		return allNews;
	}

	public void setAllNews(List<NewsWebVo> allNews) {
		this.allNews = allNews;
	}

	public String getDeleteNewsUid() {
		return deleteNewsUid;
	}

	public void setDeleteNewsUid(String deleteNewsUid) {
		this.deleteNewsUid = deleteNewsUid;
	}

	public String getUpdateSuccess() {
		return updateSuccess;
	}

	public void setUpdateSuccess(String updateSuccess) {
		this.updateSuccess = updateSuccess;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDisplayMessage() {
		return displayMessage;
	}

	public void setDisplayMessage(String displayMessage) {
		this.displayMessage = displayMessage;
	}
}
package hk.org.ha.eclaim.model.home;

import java.util.List;

import hk.org.ha.eclaim.bs.project.po.ProjectRequestPo;
import hk.org.ha.eclaim.bs.security.po.UserLogonPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.model.project.ProjectRequestVo;

public class HomeWebVo {
//	private List<RequestPo> myRequest;
//	private List<RequestPo> myTeamRequest;
//	private List<RequestPo> myRecentRequest;
	private String userName;
	private UserPo user;
//	private List<NewsPo> currentNews;
//	private List<DocumentPo> currentDocument;
	private String currentRole;
	private UserLogonPo userLogonPo;
	private String currentCluster;
	private String targetRole;
	private List<ProjectRequestVo> myProjectList;
	
	private String projectId;
	private String projectName;
	private String ownerId;
	private String preparerId;
	private String statusCode;
	
	
//	public List<RequestPo> getMyRequest() {
//		return myRequest;
//	}
//
//	public void setMyRequest(List<RequestPo> myRequest) {
//		this.myRequest = myRequest;
//	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public UserPo getUser() {
		return user;
	}

	public void setUser(UserPo user) {
		this.user = user;
	}

//	public List<NewsPo> getCurrentNews() {
//		return currentNews;
//	}
//
//	public void setCurrentNews(List<NewsPo> currentNews) {
//		this.currentNews = currentNews;
//	}

	public String getCurrentRole() {
		return currentRole;
	}

	public void setCurrentRole(String currentRole) {
		this.currentRole = currentRole;
	}

//	public List<RequestPo> getMyTeamRequest() {
//		return myTeamRequest;
//	}
//
//	public void setMyTeamRequest(List<RequestPo> myTeamRequest) {
//		this.myTeamRequest = myTeamRequest;
//	}
//
//	public List<RequestPo> getMyRecentRequest() {
//		return myRecentRequest;
//	}
//
//	public void setMyRecentRequest(List<RequestPo> myRecentRequest) {
//		this.myRecentRequest = myRecentRequest;
//	}
//
//	public List<DocumentPo> getCurrentDocument() {
//		return currentDocument;
//	}
//
//	public void setCurrentDocument(List<DocumentPo> currentDocument) {
//		this.currentDocument = currentDocument;
//	}

	public UserLogonPo getUserLogonPo() {
		return userLogonPo;
	}

	public void setUserLogonPo(UserLogonPo userLogonPo) {
		this.userLogonPo = userLogonPo;
	}

	public String getCurrentCluster() {
		return currentCluster;
	}

	public void setCurrentCluster(String currentCluster) {
		this.currentCluster = currentCluster;
	}

	public String getTargetRole() {
		return targetRole;
	}

	public void setTargetRole(String targetRole) {
		this.targetRole = targetRole;
	}

	public List<ProjectRequestVo> getMyProjectList() {
		return myProjectList;
	}

	public void setMyProjectList(List<ProjectRequestVo> myProjectList) {
		this.myProjectList = myProjectList;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getPreparerId() {
		return preparerId;
	}

	public void setPreparerId(String preparerId) {
		this.preparerId = preparerId;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
}

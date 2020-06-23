package hk.org.ha.eclaim.model.maintenance;

import org.springframework.web.multipart.MultipartFile;

public class DocumentMaintenanceDetailWebVo {
	private String id;
	private String documentUid;
	private String documentDesc;
	private String documentUrl;
	private String documentType;
	private MultipartFile documentFile;
	private String documentFileName;
	private String recState;
	private String lastUpdatedDate;
	private String userName;
	private String updateSuccess;
	private String displayMessage;
	
	public String getDocumentDesc() {
		return documentDesc;
	}

	public void setDocumentDesc(String documentDesc) {
		this.documentDesc = documentDesc;
	}

	public String getUpdateSuccess() {
		return updateSuccess;
	}

	public void setUpdateSuccess(String updateSuccess) {
		this.updateSuccess = updateSuccess;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getDocumentUrl() {
		return documentUrl;
	}

	public void setDocumentUrl(String documentUrl) {
		this.documentUrl = documentUrl;
	}

	public MultipartFile getDocumentFile() {
		return documentFile;
	}

	public void setDocumentFile(MultipartFile documentFile) {
		this.documentFile = documentFile;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentFileName() {
		return documentFileName;
	}

	public void setDocumentFileName(String documentFileName) {
		this.documentFileName = documentFileName;
	}

	public String getDocumentUid() {
		return documentUid;
	}

	public void setDocumentUid(String documentUid) {
		this.documentUid = documentUid;
	}

	public String getRecState() {
		return recState;
	}

	public void setRecState(String recState) {
		this.recState = recState;
	}
}

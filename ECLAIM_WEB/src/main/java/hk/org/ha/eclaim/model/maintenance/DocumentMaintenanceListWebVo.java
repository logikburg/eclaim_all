package hk.org.ha.eclaim.model.maintenance;

import java.util.List;


public class DocumentMaintenanceListWebVo {
	private List<DocumentWebVo> allDocument;
	private String deleteDocumentUid;
	private String updateSuccess;
	private String errorMessage;
	private String displayMessage;
	private String userName;

	public List<DocumentWebVo> getAllDocument() {
		return allDocument;
	}

	public void setAllDocument(List<DocumentWebVo> allDocument) {
		this.allDocument = allDocument;
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

	public String getDeleteDocumentUid() {
		return deleteDocumentUid;
	}

	public void setDeleteDocumentUid(String deleteDocumentUid) {
		this.deleteDocumentUid = deleteDocumentUid;
	}
}
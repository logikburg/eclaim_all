package hk.org.ha.eclaim.model.request;

import java.util.ArrayList;
import java.util.List;

import hk.org.ha.eclaim.bs.request.po.RequestPostPo;

public class DeletionWebVo extends CommonWebVo {
	private String withMassSave = "N";
	
	private String userName;
	
	private String requestNo;
	private String requestId;
	private String requester;
	private String requestStatus = "";
	private String requestReason;
	private String effectiveDate;
	
	private String updateSuccess;
	private String displayMessage;
	private String formAction;
	
	private String hoBuyServiceInd;
	
	private List<String> requestPostNo = new ArrayList<String>();
	
	private List<RequestPostPo> requestPositionList = new ArrayList<RequestPostPo>();
	
	public String getRequestId() {
		return requestId;
	}
	
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	public String getRequester() {
		return requester;
	}
	
	public void setRequester(String requester) {
		this.requester = requester;
	}

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getUpdateSuccess() {
		return updateSuccess;
	}

	public void setUpdateSuccess(String updateSuccess) {
		this.updateSuccess = updateSuccess;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public String getDisplayMessage() {
		return displayMessage;
	}

	public void setDisplayMessage(String displayMessage) {
		this.displayMessage = displayMessage;
	}

	public String getFormAction() {
		return formAction;
	}

	public void setFormAction(String formAction) {
		this.formAction = formAction;
	}

	public String getRequestReason() {
		return requestReason;
	}

	public void setRequestReason(String requestReason) {
		this.requestReason = requestReason;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<String> getRequestPostNo() {
		return requestPostNo;
	}

	public void setRequestPostNo(List<String> requestPostNo) {
		this.requestPostNo = requestPostNo;
	}

	public List<RequestPostPo> getRequestPositionList() {
		return requestPositionList;
	}

	public void setRequestPositionList(List<RequestPostPo> requestPositionList) {
		this.requestPositionList = requestPositionList;
	}

	public String getWithMassSave() {
		return withMassSave;
	}

	public void setWithMassSave(String withMassSave) {
		this.withMassSave = withMassSave;
	}

	public String getHoBuyServiceInd() {
		return hoBuyServiceInd;
	}

	public void setHoBuyServiceInd(String hoBuyServiceInd) {
		this.hoBuyServiceInd = hoBuyServiceInd;
	}
}

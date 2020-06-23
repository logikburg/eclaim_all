package hk.org.ha.eclaim.bs.request.po;

import java.util.List;

public class RequestEnquiryWebPo {
	private String userName;
	
	private String requestId;
	
	private String haveResult;
	private List<RequestWorkflowHistoryPo> historyList;
	private String createdDateFrom;
	private String createdDateTo;
	private String updatedDateFrom;
	private String updatedDateTo;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getHaveResult() {
		return haveResult;
	}

	public void setHaveResult(String haveResult) {
		this.haveResult = haveResult;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public List<RequestWorkflowHistoryPo> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<RequestWorkflowHistoryPo> historyList) {
		this.historyList = historyList;
	}

	public String getCreatedDateFrom() {
		return createdDateFrom;
	}

	public void setCreatedDateFrom(String createdDateFrom) {
		this.createdDateFrom = createdDateFrom;
	}

	public String getCreatedDateTo() {
		return createdDateTo;
	}

	public void setCreatedDateTo(String createdDateTo) {
		this.createdDateTo = createdDateTo;
	}

	public String getUpdatedDateFrom() {
		return updatedDateFrom;
	}

	public void setUpdatedDateFrom(String updatedDateFrom) {
		this.updatedDateFrom = updatedDateFrom;
	}

	public String getUpdatedDateTo() {
		return updatedDateTo;
	}

	public void setUpdatedDateTo(String updatedDateTo) {
		this.updatedDateTo = updatedDateTo;
	}
}

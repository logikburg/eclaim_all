package hk.org.ha.eclaim.bs.request.po;

import java.util.Map;

public class MPRResultResponse {
	private Map<String, String> errorMessage;
	private String searchResult;

	public Map<String, String> getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(Map<String, String> errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getSearchResult() {
		return searchResult;
	}

	public void setSearchResult(String searchResult) {
		this.searchResult = searchResult;
	}
}

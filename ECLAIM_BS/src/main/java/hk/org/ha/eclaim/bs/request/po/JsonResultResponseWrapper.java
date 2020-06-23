package hk.org.ha.eclaim.bs.request.po;

import java.util.List;

public class JsonResultResponseWrapper {

	List<JsonResultResponse> jsonResultResponse;
	private String error;
	private String errorMsg;

	public List<JsonResultResponse> getJsonResultResponse() {
		return jsonResultResponse;
	}

	public void setJsonResultResponse(List<JsonResultResponse> jsonResultResponse) {
		this.jsonResultResponse = jsonResultResponse;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}

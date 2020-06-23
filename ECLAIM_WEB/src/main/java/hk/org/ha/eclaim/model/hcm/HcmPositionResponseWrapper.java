package hk.org.ha.eclaim.model.hcm;

import java.util.List;

public class HcmPositionResponseWrapper {
	private List<HcmPositionResponse> hcmPositionList;
	private String error;
	private String errorMsg;

	public List<HcmPositionResponse> getHcmPositionResponse() {
		return hcmPositionList;
	}

	public void setHcmPositionResponse(List<HcmPositionResponse> hcmPositionList) {
		this.hcmPositionList = hcmPositionList;
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

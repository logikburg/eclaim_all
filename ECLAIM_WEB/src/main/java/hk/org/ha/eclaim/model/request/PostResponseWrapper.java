package hk.org.ha.eclaim.model.request;

import java.util.List;

public class PostResponseWrapper {
	private List<PostResponse> postResponse;
	private String error;
	private String errorMsg;

	public List<PostResponse> getPostResponse() {
		return postResponse;
	}

	public void setPostResponse(List<PostResponse> postResponse) {
		this.postResponse = postResponse;
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

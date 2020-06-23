package hk.org.ha.eclaim.model.common;

public class EmailTemplateRequest {
	private String requestId;
	private String verId;
	private String templateId;
	private String param;
	private String param2;
	private String param3;
	private String param4;
	private String param5;
	private String param6;
	private String param7;
	private String param8;
	private String param9;
	private String wfGroup;
	private String wfUser;
	private String isReturn;
	
	private String toRole;
	private String ccRole;
	

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getParam2() {
		return param2;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}

	public String getParam3() {
		return param3;
	}

	public void setParam3(String param3) {
		this.param3 = param3;
	}

	public String getParam4() {
		return param4;
	}

	public void setParam4(String param4) {
		this.param4 = param4;
	}

	public String getParam5() {
		return param5;
	}

	public void setParam5(String param5) {
		this.param5 = param5;
	}

	public String getParam6() {
		return param6;
	}

	public void setParam6(String param6) {
		this.param6 = param6;
	}

	public String getParam7() {
		return param7;
	}

	public void setParam7(String param7) {
		this.param7 = param7;
	}

	public String getParam8() {
		return param8;
	}

	public void setParam8(String param8) {
		this.param8 = param8;
	}

	public String getParam9() {
		return param9;
	}

	public void setParam9(String param9) {
		this.param9 = param9;
	}

	public String getWfGroup() {
		return wfGroup;
	}

	public void setWfGroup(String wfGroup) {
		this.wfGroup = wfGroup;
	}

	public String getWfUser() {
		return wfUser;
	}

	public void setWfUser(String wfUser) {
		this.wfUser = wfUser;
	}

	public String getIsReturn() {
		return isReturn;
	}

	public void setIsReturn(String isReturn) {
		this.isReturn = isReturn;
	}
	
	public String toString() {
		String str = "requestId=" + requestId + ", templateId=" + templateId + 
				", param=" + param + ", param2=" + param2+ ", param3=" + param3 +
				", param4=" + param4 + ", param5=" + param5+ ", param6=" + param6 +
				", param7=" + param7 + ", param8=" + param8+ ", param9=" + param9 +
				", wfGroup=" + wfGroup + ", wfUser=" + wfUser+ ", isReturn=" + isReturn;
		return str;
	}

	public String getToRole() {
		return toRole;
	}

	public void setToRole(String toRole) {
		this.toRole = toRole;
	}

	public String getCcRole() {
		return ccRole;
	}

	public void setCcRole(String ccRole) {
		this.ccRole = ccRole;
	}

	public String getVerId() {
		return verId;
	}

	public void setVerId(String verId) {
		this.verId = verId;
	}
}

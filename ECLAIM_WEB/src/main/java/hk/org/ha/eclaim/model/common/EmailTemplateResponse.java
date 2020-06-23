package hk.org.ha.eclaim.model.common;

import java.util.List;

import hk.org.ha.eclaim.model.project.EmailVo;

public class EmailTemplateResponse {

	private String templateId;

	private String templateTitle;

	private String templateContent;

	private List<EmailVo> emailToList;
	private List<EmailVo> emailCCList;
	
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getTemplateTitle() {
		return templateTitle;
	}
	public void setTemplateTitle(String templateTitle) {
		this.templateTitle = templateTitle;
	}
	public String getTemplateContent() {
		return templateContent;
	}
	public void setTemplateContent(String templateContent) {
		this.templateContent = templateContent;
	}
	public List<EmailVo> getEmailToList() {
		return emailToList;
	}
	public void setEmailToList(List<EmailVo> emailToList) {
		this.emailToList = emailToList;
	}
	public List<EmailVo> getEmailCCList() {
		return emailCCList;
	}
	public void setEmailCCList(List<EmailVo> emailCCList) {
		this.emailCCList = emailCCList;
	}
	
}

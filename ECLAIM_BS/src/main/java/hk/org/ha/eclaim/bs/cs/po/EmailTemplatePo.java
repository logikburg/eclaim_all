package hk.org.ha.eclaim.bs.cs.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="XXEAL_EMAIL_TEMPLATE")
public class EmailTemplatePo extends AbstractBasePo {
	
	private static final long serialVersionUID = -4016345855871249716L;

	@Id
	@Column(name="TEMPLATE_ID")
	private String templateId;
	
	@Column(name="TEMPLATE_TITLE")
	private String templateTitle;
	
	@Column(name="TEMPLATE_CONTENT")
	private String templateContent;

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
}

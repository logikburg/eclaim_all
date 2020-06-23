package hk.org.ha.eclaim.bs.request.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="XXEAL_WORKFLOW_EMAIL")
public class RequestEmailPo extends AbstractBasePo {
	
	private static final long serialVersionUID = 8894478767646403434L;

	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="XXEAL_WORKFLOW_EMAIL_SEQ")
	@SequenceGenerator(name="XXEAL_WORKFLOW_EMAIL_SEQ", sequenceName="XXEAL_WORKFLOW_EMAIL_SEQ", allocationSize=1)
	@Id
	@Column(name="WF_EMAIL_ID")
	private Integer emailUid;
	
	@Column(name="PROJECT_ID")
	private Integer projectId;
	
	@Column(name="PROJECT_VER_ID")
	private Integer projectVersionId;
	
	@Column(name="EMAIL_TO")
	private String emailTo;
	
	@Column(name="EMAIL_CC")
	private String emailCc;
	
	@Column(name="TITLE")
	private String title;
	
	@Column(name="CONTENT")
	private String content;
	
	@Column(name="SUPP_INFO")
	private String suppInfo;
	
	@Column(name="SENT_IND")
	private String sentInd;

	public Integer getEmailUid() {
		return emailUid;
	}

	public void setEmailUid(Integer emailUid) {
		this.emailUid = emailUid;
	}

	public String getEmailTo() {
		return emailTo;
	}

	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}

	public String getEmailCc() {
		return emailCc;
	}

	public void setEmailCc(String emailCc) {
		this.emailCc = emailCc;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSentInd() {
		return sentInd;
	}

	public void setSentInd(String sentInd) {
		this.sentInd = sentInd;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getProjectVersionId() {
		return projectVersionId;
	}

	public void setProjectVersionId(Integer projectVersionId) {
		this.projectVersionId = projectVersionId;
	}

	public String getSuppInfo() {
		return suppInfo;
	}

	public void setSuppInfo(String suppInfo) {
		this.suppInfo = suppInfo;
	}
}

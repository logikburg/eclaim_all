package hk.org.ha.eclaim.bs.maintenance.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="CS_DOCUMENT")
public class DocumentPo extends AbstractBasePo {
	
	private static final long serialVersionUID = -7861383283866245780L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CS_DOCUMENT_UID_SEQ")
	@SequenceGenerator(name="CS_DOCUMENT_UID_SEQ", sequenceName="CS_DOCUMENT_UID_SEQ", allocationSize=1)
	@Column(name="DOCUMENT_UID")
	private Integer documentUid;
	
	@Column(name="DOCUMENT_NAME")
	private String documentName;
	
	@Column(name="DOCUMENT_TYPE")
	private String documentType;
	
	@Column(name="DOCUMENT_URL")
	private String documentUrl;

	@Column(name="FILE_NAME")
	private String documentFileName;
	
	public Integer getDocumentUid() {
		return documentUid;
	}

	public void setDocumentUid(Integer documentUid) {
		this.documentUid = documentUid;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentUrl() {
		return documentUrl;
	}

	public void setDocumentUrl(String documentUrl) {
		this.documentUrl = documentUrl;
	}

	public String getDocumentFileName() {
		return documentFileName;
	}

	public void setDocumentFileName(String documentFileName) {
		this.documentFileName = documentFileName;
	}
}

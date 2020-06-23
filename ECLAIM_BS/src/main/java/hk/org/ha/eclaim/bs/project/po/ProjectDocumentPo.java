package hk.org.ha.eclaim.bs.project.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="XXEAL_DOCUMENT")
public class ProjectDocumentPo extends AbstractBasePo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8462751935059811071L;

	@Id
	@Column(name="DOCUMENT_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="XXEAL_DOCUMENT_ID_SEQ")
	@SequenceGenerator(name="XXEAL_DOCUMENT_ID_SEQ", sequenceName="XXEAL_DOCUMENT_ID_SEQ", allocationSize=1)
	private Integer documentId;
	
	@Column(name="PROJECT_ID")
	private Integer projectId;
	
	@Column(name="PROJECT_VER_ID")
	private Integer projectVerId;
	
	@Column(name="PATH")
	private String path;
	
	@Column(name="FILENAME")
	private String fileName;
	
	@Column(name="DESCRIPTIONQ")
	private String description;
	
	public Integer getDocumentId() {
		return documentId;
	}
	public void setDocumentId(Integer documentId) {
		this.documentId = documentId;
	}
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	public Integer getProjectVerId() {
		return projectVerId;
	}
	public void setProjectVerId(Integer projectVerId) {
		this.projectVerId = projectVerId;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}

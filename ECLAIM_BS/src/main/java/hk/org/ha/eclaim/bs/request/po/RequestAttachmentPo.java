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
@Table(name="CS_POST_ATTACH")
public class RequestAttachmentPo extends AbstractBasePo {
	
	private static final long serialVersionUID = -4614548369457229960L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CS_POST_ATTACH_UID_SEQ")
	@SequenceGenerator(name="CS_POST_ATTACH_UID_SEQ", sequenceName="CS_POST_ATTACH_UID_SEQ", allocationSize=1)
	@Column(name="attach_uid")
	private Integer attachmentUid;

	@Column(name="request_uid")
	private Integer requestUid;
	
	@Column(name="post_uid")
	private Integer postUid;
	
	@Column(name="file_path")
	private String filePath;

	@Column(name="file_name")
	private String fileName;

	public Integer getAttachmentUid() {
		return attachmentUid;
	}

	public void setAttachmentUid(Integer attachmentUid) {
		this.attachmentUid = attachmentUid;
	}

	public Integer getRequestUid() {
		return requestUid;
	}

	public void setRequestUid(Integer requestUid) {
		this.requestUid = requestUid;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getPostUid() {
		return postUid;
	}

	public void setPostUid(Integer postUid) {
		this.postUid = postUid;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
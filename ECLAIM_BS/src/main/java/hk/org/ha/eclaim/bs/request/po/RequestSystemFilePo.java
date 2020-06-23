package hk.org.ha.eclaim.bs.request.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="XXEAL_SYS_ATTACH")
public class RequestSystemFilePo {

	@Id
	@Column(name="ATTACH_CODE")
	private String attachCode;
	
	@Column(name="FILE_NAME")
	private String filename;

	public String getAttachCode() {
		return attachCode;
	}

	public void setAttachCode(String attachCode) {
		this.attachCode = attachCode;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
}

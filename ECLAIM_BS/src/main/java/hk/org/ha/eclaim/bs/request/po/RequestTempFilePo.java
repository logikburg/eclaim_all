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
@Table(name="XXEAL_TMP_ATTACH")
public class RequestTempFilePo extends AbstractBasePo {
	
	private static final long serialVersionUID = 2085411533771823547L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="XXEAL_TMP_ATTACH_UID_SEQ")
	@SequenceGenerator(name="XXEAL_TMP_ATTACH_UID_SEQ", sequenceName="XXEAL_TMP_ATTACH_UID_SEQ", allocationSize=1)
	@Column(name="TMP_ATTACH_UID")
	private Integer tmpFileUid;
	
	@Column(name="FILE_NAME")
	private String filename;
	
	@Column(name="DESCRIPTIONQ")
	private String description;
	
	public Integer getTmpFileUid() {
		return tmpFileUid;
	}

	public void setTmpFileUid(Integer tmpFileUid) {
		this.tmpFileUid = tmpFileUid;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}

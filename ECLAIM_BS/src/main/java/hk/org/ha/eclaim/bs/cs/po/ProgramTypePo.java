package hk.org.ha.eclaim.bs.cs.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

public class ProgramTypePo extends AbstractBasePo {
	
	private static final long serialVersionUID = 4517310937362532154L;

	@Id
	@Column(name="PROGRAM_TYPE_CODE")
	private String programTypeCode;
	
	@Column(name="PROGRAM_TYPE_NAME")
	private String programTypeName;
	
	public String getProgramTypeCode() {
		return programTypeCode;
	}

	public void setProgramTypeCode(String programTypeCode) {
		this.programTypeCode = programTypeCode;
	}

	public String getProgramTypeName() {
		return programTypeName;
	}

	public void setProgramTypeName(String programTypeName) {
		this.programTypeName = programTypeName;
	}
	
}
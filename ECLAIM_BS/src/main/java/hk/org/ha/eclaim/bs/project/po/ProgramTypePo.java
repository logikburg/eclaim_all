package hk.org.ha.eclaim.bs.project.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="XXEAL_PROG_TYPE_M")
public class ProgramTypePo{
	
	@Id
	@Column(name="TYPE_CODE")
	private String typeCode;
	
	@Column(name="TYPE_DESC")
	private String typeDesc;

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
	
	
}

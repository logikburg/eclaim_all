package hk.org.ha.eclaim.bs.cs.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="CS_EXTERNAL_SUPPORT")
public class ExternalSupportPo extends AbstractBasePo {
	
	private static final long serialVersionUID = 2033430771782485196L;

	@Id
	@Column(name="SUPPORT_ID")
	private String supportId;
	
	@Column(name="SUPPORT_DESC")
	private String supportDesc;
	
	public String getSupportId() {
		return supportId;
	}

	public void setSupportId(String sourceId) {
		this.supportId = sourceId;
	}

	public String getSupportDesc() {
		return supportDesc;
	}

	public void setSupportDesc(String sourceDesc) {
		this.supportDesc = sourceDesc;
	}
}
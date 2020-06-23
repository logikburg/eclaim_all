package hk.org.ha.eclaim.bs.cs.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="CS_FUNDING_SOURCE")
public class FundingSourcePo extends AbstractBasePo {
	
	private static final long serialVersionUID = 1940329571567665702L;

	@Id
	@Column(name="SOURCE_ID")
	private String sourceId;
	
	@Column(name="SOURCE_DESC")
	private String sourceDesc;
	
	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceDesc() {
		return sourceDesc;
	}

	public void setSourceDesc(String sourceDesc) {
		this.sourceDesc = sourceDesc;
	}
}
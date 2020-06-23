package hk.org.ha.eclaim.bs.cs.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="CS_FUNDING_SOURCE_SUB_CAT")
public class FundingSourceSubCatPo extends AbstractBasePo {
	
	private static final long serialVersionUID = 1940329571567665702L;

	@Id
	@Column(name="SOURCE_SUB_CAT_ID")
	private String sourceSubCatId;
	
	@Column(name="SOURCE_SUB_CAT_DESC")
	private String sourceSubCatDesc;

	public String getSourceSubCatId() {
		return sourceSubCatId;
	}

	public void setSourceSubCatId(String sourceSubCatId) {
		this.sourceSubCatId = sourceSubCatId;
	}

	public String getSourceSubCatDesc() {
		return sourceSubCatDesc;
	}

	public void setSourceSubCatDesc(String sourceSubCatDesc) {
		this.sourceSubCatDesc = sourceSubCatDesc;
	}
	
}
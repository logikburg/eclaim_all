package hk.org.ha.eclaim.bs.hcm.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="V_HCM_SOURCE_FUNDING")
public class HCMSourceFundingPo {
	
	@Id
	@Column(name="flex_value")
	private String sourceFundingCode;
	
	@Column(name="description")
	private String sourceFundingDesc;

	public String getSourceFundingCode() {
		return sourceFundingCode;
	}

	public void setSourceFundingCode(String sourceFundingCode) {
		this.sourceFundingCode = sourceFundingCode;
	}

	public String getSourceFundingDesc() {
		return sourceFundingDesc;
	}

	public void setSourceFundingDesc(String sourceFundingDesc) {
		this.sourceFundingDesc = sourceFundingDesc;
	}
}
package hk.org.ha.eclaim.bs.project.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="XXEAL_SESS_PATTERN_M")
public class SessionPatternPo {
	
	@Id
	@Column(name="PATTERN_CODE")
	private String patternCode;
	
	@Column(name="PATTERN_DESC")
	private String patternDesc;

	public String getPatternCode() {
		return patternCode;
	}

	public void setPatternCode(String patternCode) {
		this.patternCode = patternCode;
	}

	public String getPatternDesc() {
		return patternDesc;
	}

	public void setPatternDesc(String patternDesc) {
		this.patternDesc = patternDesc;
	}
	
	
}

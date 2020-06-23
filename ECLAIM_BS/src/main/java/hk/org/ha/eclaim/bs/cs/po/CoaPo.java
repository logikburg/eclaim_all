package hk.org.ha.eclaim.bs.cs.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="XXEAL_COA_V")
public class CoaPo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4256650890019599294L;

	@Id
	@Column(name="COA_VAL")
	private String coaVal;
	
	@Column(name="COA_NAME")
	private String coaName;

	public String getCoaVal() {
		return coaVal;
	}

	public void setCoaVal(String coaVal) {
		this.coaVal = coaVal;
	}

	public String getCoaName() {
		return coaName;
	}

	public void setCoaName(String coaName) {
		this.coaName = coaName;
	}
	
}

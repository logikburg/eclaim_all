package hk.org.ha.eclaim.bs.security.po;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="SS_ROLE_ACCESS")
public class RoleAccessPo extends AbstractBasePo {
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private PK_RoleAccess pk = new PK_RoleAccess();
	
	@ManyToOne
	@MapsId("funcId")
	@JoinColumn(name="FUNC_ID")
	private MPRSFunctionPo func;

	public MPRSFunctionPo getFunc() {
		return func;
	}

	public void setFunc(MPRSFunctionPo func) {
		this.func = func;
	}
}

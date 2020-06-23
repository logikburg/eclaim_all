package hk.org.ha.eclaim.bs.security.po;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="SS_FUNC")
public class MPRSFunctionPo extends AbstractBasePo {
	
	private static final long serialVersionUID = -5700544564192700849L;

	@Id
	@Column(name="FUNC_ID")
	private String funcId;
	
	@Column(name="FUNC_NAME")
	private String funcName;
	
	@Column(name="PARENT_FUNC_ID")
	private String parentFuncId;
	
	@OneToMany(mappedBy = "func", cascade = CascadeType.ALL)
	private List<RoleAccessPo> roleAccessList;

	public String getFuncId() {
		return funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}

	public String getFuncName() {
		return funcName;
	}

	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	public String getParentFuncId() {
		return parentFuncId;
	}

	public void setParentFuncId(String parentFuncId) {
		this.parentFuncId = parentFuncId;
	}

	public List<RoleAccessPo> getRoleAccessList() {
		return roleAccessList;
	}

	public void setRoleAccessList(List<RoleAccessPo> roleAccessList) {
		this.roleAccessList = roleAccessList;
	}
}

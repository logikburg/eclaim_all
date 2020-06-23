package hk.org.ha.eclaim.bs.security.po;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PK_RoleAccess implements Serializable {
	private static final long serialVersionUID = -1726695281993821716L;
	
	@Column(name="ROLE_ID")
	private String roleId;
	
	@Column(name="FUNC_ID")
	private String funcId;
	
	public PK_RoleAccess() {
		
	}
	
	public PK_RoleAccess(String roleId, String funcId) {
		this.setRoleId(roleId);
		this.setFuncId(funcId);
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getFuncId() {
		return funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((roleId == null) ? 0 : roleId.hashCode());
		result = prime * result
				+ ((funcId == null) ? 0 : funcId.hashCode());
		return result;
	}
 
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null) {
			return false;
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		PK_RoleAccess other = (PK_RoleAccess) obj;
		if (roleId == null) {
			if (other.roleId != null) {
				return false;
			}
		} 
		else if (!roleId.equals(other.roleId)) {
			return false;
		}
		
		if (funcId == null) {
			if (other.funcId != null) {
				return false;
			}
		} 
		else if (!funcId.equals(other.funcId)) {
			return false;
		}

		return true;
	}
}

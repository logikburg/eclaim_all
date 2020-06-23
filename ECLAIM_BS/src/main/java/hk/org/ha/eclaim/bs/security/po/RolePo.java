package hk.org.ha.eclaim.bs.security.po;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="XXEAL_ROLE")
public class RolePo extends AbstractBasePo {
	
	private static final long serialVersionUID = -1931702203815822179L;

	@Id
	@Column(name="ROLE_ID")
	private String roleId;
	
	@Column(name="ROLE_NAME")
	private String roleName;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy="role", cascade = CascadeType.ALL)
	private List<UserRolePo> userRole = new ArrayList<UserRolePo>();

	@Transient
	private String defaultRole;
	
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<UserRolePo> getUserRole() {
		return userRole;
	}

	public void setUserRole(List<UserRolePo> userRole) {
		this.userRole = userRole;
	}

	public String getDefaultRole() {
		return defaultRole;
	}

	public void setDefaultRole(String defaultRole) {
		this.defaultRole = defaultRole;
	}
}

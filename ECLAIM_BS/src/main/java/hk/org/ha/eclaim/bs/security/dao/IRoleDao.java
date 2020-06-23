package hk.org.ha.eclaim.bs.security.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.security.po.RolePo;

public interface IRoleDao  {
	public List<RolePo> getAllRole();

	public List<RolePo> getAllRole(String term);

	public RolePo getRoleById(String string);

	public List<RolePo> getAllRoleByRoleId(String term, String currentRole);
}

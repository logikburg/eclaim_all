package hk.org.ha.eclaim.bs.security.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.security.po.RolePo;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.po.UserRolePo;

public interface IUserRoleDao {
	public int insertUserRole(UserPo newUser, RolePo role, String clusterCode, String instCode, String deptCode, String staffGroupCode, UserPo user);
	public void deleteUserRole(String userId, String roleId, UserPo user);
	public UserRolePo getUserRole(String userId, String roleId);
	public List<UserRolePo> findListUserRoleByUserId(String userId);
	public void updateDefaultRole(String userId, String selectedRoleId, UserPo user);
	public void resetDefaultRole(String userId, UserPo user);
}

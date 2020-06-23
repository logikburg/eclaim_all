package hk.org.ha.eclaim.bs.security.svc;

import java.util.List;

import hk.org.ha.eclaim.bs.security.po.DataAccessPo;
import hk.org.ha.eclaim.bs.security.po.MPRSFunctionPo;
import hk.org.ha.eclaim.bs.security.po.RolePo;
import hk.org.ha.eclaim.bs.security.po.UserLogonPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.po.UserRolePo;

public interface ISecuritySvc {

	public UserPo findUser(String userId) throws Exception;
	
	public List<UserPo> getAllUser();
	
	public List<UserRolePo> findUserRoleByUserId(String userId);

	public void insertUser(UserPo updatedUserPo);
	
	public void updateUser(UserPo updatedUser);
	
	public List<MPRSFunctionPo> getFunctionListByRole(String roleId) ;

	public List<RolePo> getAllRole();
	
	public List<RolePo> getAllRole(String term);

	public List<UserPo> getAllUserByGroup(String groupId);
	
	public List<UserPo> getAllUserByGroupAndCluster(String clusterCode, String instCode, String groupId);
	
	public List<UserPo> searchUser(String userId, String userName);
	
	public List<UserPo> searchUser(String userId, String userName, boolean activeOnly);
	
	public List<UserPo> searchUser(String userId, String userName, String loginUserId, String roleId);
	
	public List<UserPo> searchUser(String userId, String userName, String loginUserId, String roleId, boolean activeOnly);

	public void updateUserRole(String userId, List<String> roleIdList, 
										      List<String> cliusterList, 
										      List<String> instList, 
										      List<String> deptList, 
										      List<String> staffGroupList, UserPo updateUser) throws Exception;
	
	public List<UserPo> getRelatedUserFromWorkflowHistoryAll(String requestId);

	public List<DataAccessPo> getDataAccessByRoleId(int roleId);
	
	public int insertUserLogonInfo(UserLogonPo userLogon);
	
	public UserLogonPo getLastLogonInfo(String userId);

	public List<UserPo> getAllHROfficer(String userId, String currentRole);
	
	public boolean haveAccessRight(String clusterCode, String instCode, String userId, String roleId);

	public String generateCidsLogonToken(String userId, String domain);

	public List<RolePo> getAllRoleByRoleId(String term, String currentRole);
	
	public UserPo getCidsUserInformation(String userId);

	// Added for UT29849
	public List<UserPo> getReturnUserFromWorkflowHistoryAll(String requestNo, String nextWFGroup);

	// Added for UT30107
	public void updateDefaultRole(String userId, String selectedRoleId, UserPo user);
}

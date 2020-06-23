package hk.org.ha.eclaim.bs.security.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.core.dao.IBaseDao;

public interface IUserDao extends IBaseDao<UserPo> {
	
	public UserPo findUserById(String id);
	public List<UserPo> getAllUser();
	public void insertUser(UserPo updatedUserPo);
	public void updateUser(UserPo updatedUserPo);
	public List<UserPo> getAllUser(String groupId);
	public List<UserPo> getAllUserByGroupAndCluster(String clusterCode, String instCode, String groupId);
	public List<UserPo> searchUser(String userId, String userName, boolean activeOnly);
	public List<UserPo> searchUser(String userId, String userName, String loginUserId, String roleId, boolean activeOnly);
	public List<UserPo> getRelatedUserFromWorkflowHistoryAll(String requestId);
	public List<UserPo> getAllHROfficer(int userRoleId);
	public String generateCidsLogonToken(String userId, String domain);
	public UserPo getCidsUserInformation(String userId);
	
	// Added for UT29849
	public List<UserPo> getReturnUserFromWorkflowHistoryAll(String requestNo, String nextWFGroup);		
}

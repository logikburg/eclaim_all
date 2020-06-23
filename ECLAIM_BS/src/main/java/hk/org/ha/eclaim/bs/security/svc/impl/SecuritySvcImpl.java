package hk.org.ha.eclaim.bs.security.svc.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hk.org.ha.eclaim.bs.security.dao.IDataAccessDao;
import hk.org.ha.eclaim.bs.security.dao.IFuncAccessDao;
import hk.org.ha.eclaim.bs.security.dao.IRoleDao;
import hk.org.ha.eclaim.bs.security.dao.IUserDao;
import hk.org.ha.eclaim.bs.security.dao.IUserLogonDao;
import hk.org.ha.eclaim.bs.security.dao.IUserRoleDao;
import hk.org.ha.eclaim.bs.security.po.DataAccessPo;
import hk.org.ha.eclaim.bs.security.po.MPRSFunctionPo;
import hk.org.ha.eclaim.bs.security.po.RolePo;
import hk.org.ha.eclaim.bs.security.po.UserLogonPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.po.UserRolePo;
import hk.org.ha.eclaim.bs.security.svc.ISecuritySvc;
import hk.org.ha.eclaim.core.logger.EClaimLogger;

@Service("securitySvc")
public class SecuritySvcImpl implements ISecuritySvc {

	@Autowired(required=true)
	private IUserDao userDao;
	
	@Autowired
	private IRoleDao roleDao;
	
	@Autowired
	private IFuncAccessDao roleAccessDao;
	
	@Autowired
	private IDataAccessDao dataAccessDao;
	
	@Autowired
	private IUserRoleDao userRoleDao;
	
	@Autowired
	private IUserLogonDao userLogonDao;
	
	public UserPo findUser(String userId) throws Exception {
		UserPo user = userDao.findUserById(userId);
		if (user != null) {
			return user;
		}
		return null;
	}
		
	public List<UserRolePo> findUserRoleByUserId(String userId) {
		return userRoleDao.findListUserRoleByUserId(userId);
	}

	public List<UserPo> getAllUser() {
		return userDao.getAllUser();
	}

	@Transactional
	public void insertUser(UserPo updatedUserPo) {
		userDao.insertUser(updatedUserPo);
	}
	
	@Transactional
	public void updateUser(UserPo updatedUserPo) {
		userDao.updateUser(updatedUserPo);
	}

	public List<MPRSFunctionPo> getFunctionListByRole(String roleId) {
		return roleAccessDao.getFunctionListByRole(roleId);
	}

	public List<RolePo> getAllRole() {
		return roleDao.getAllRole();
	}
	
	public List<RolePo> getAllRole(String term) {
		return roleDao.getAllRole(term);
	}

	public List<UserPo> getAllUserByGroup(String groupId) {
		return userDao.getAllUser(groupId);
	}
	
	public List<UserPo> getAllUserByGroupAndCluster(String clusterCode, String instCode, String groupId) {
		EClaimLogger.info("getAllUserByGroupAndCluster - clusterCode=" + clusterCode + ", instCode=" + instCode + ", groupId=" + groupId);
		return userDao.getAllUserByGroupAndCluster(clusterCode, instCode, groupId);
	}

	// common not in use method
//	public List<DataAccessPo> getDataAccessByUserId(String userId) {
//		return dataAccessDao.getDataAccessByUserId(userId);
//	}

	public List<UserPo> searchUser(String userId, String userName) {
		return userDao.searchUser(userId, userName, false);
	}
	
	public List<UserPo> searchUser(String userId, String userName, boolean activeOnly) {
		return userDao.searchUser(userId, userName, activeOnly);
	}
	
	public List<UserPo> searchUser(String userId, String userName, String loginUserId, String roleId) {
		return userDao.searchUser(userId, userName, loginUserId, roleId, false);
	}
	
	public List<UserPo> searchUser(String userId, String userName, String loginUserId, String roleId, boolean activeOnly) {
		return userDao.searchUser(userId, userName, loginUserId, roleId,  activeOnly);
	}

	@Transactional
	public void updateUserRole(String userId, List<String> roleIdList, 
											  List<String> clusterList, 
											  List<String> instList, 
											  List<String> deptList, 
											  List<String> staffGroupList, UserPo updateUser) throws Exception {
		// Remove all existing user role mapping
		System.out.println("userId: " + userId);
		UserPo user = this.findUser(userId);
		if (user.getUserRole() != null) {
			for (int i=0; i<user.getUserRole().size(); i++) {
				userRoleDao.deleteUserRole(userId, user.getUserRole().get(i).getRole().getRoleId(), updateUser);
				dataAccessDao.deleteDataAccess(user.getUserRole().get(i).getUserRoleId(), updateUser);
			}
		}
		
		// Add new user role mapping
		// Get Role
		RolePo role = null;
		if (roleIdList != null) {
			for (int x=0; x<roleIdList.size(); x++) {
				role = roleDao.getRoleById(roleIdList.get(x));
				
				String cluster = "";
				if (clusterList != null) {
					if (clusterList.size() > 0) {
						cluster = clusterList.get(x);
					}
				}
				
				String inst = "";
				if (instList != null) {
					if (instList.size() > 0) {
						inst = instList.get(x);
					}
				}
				
				String dept = "";
				if (deptList != null) {
					if (deptList.size() > 0) {
						dept = deptList.get(x);
					}
				}
				
				String staffGroup = "";
				if (staffGroupList != null) {
					if (staffGroupList.size() > 0) {
						staffGroup = staffGroupList.get(x);
					}
				}
				
				
				int roleId = userRoleDao.insertUserRole(user, role, 
										   cluster,
										   inst,
										   dept,
										   staffGroup,
										   updateUser);
				
				dataAccessDao.insertDataAccess(roleId, 
						   cluster,
						   inst,
						   dept,
						   staffGroup,
						   updateUser);
			}
		}
	}
	
	public List<UserPo> getRelatedUserFromWorkflowHistoryAll(String requestId) {
		return userDao.getRelatedUserFromWorkflowHistoryAll(requestId);
	}

	public List<DataAccessPo> getDataAccessByRoleId(int roleId) {
		return dataAccessDao.getDataAccessByRoleId(roleId);
	}

	@Transactional
	public int insertUserLogonInfo(UserLogonPo userLogon) {
		return userLogonDao.insert(userLogon);
	}

	public UserLogonPo getLastLogonInfo(String userId) {
		return userLogonDao.getLastLogonInfo(userId);
	}

	public List<UserPo> getAllHROfficer(String userId, String currentRole) {
		UserRolePo userRole = userRoleDao.getUserRole(userId, currentRole);
		return userDao.getAllHROfficer(userRole.getUserRoleId());
	}

	public boolean haveAccessRight(String clusterCode, String instCode, String userId, String roleId) {
		return dataAccessDao.haveAccessRight(clusterCode, instCode, userId, roleId);
	}
	
	public String generateCidsLogonToken(String userId, String domain) {
		return userDao.generateCidsLogonToken(userId, domain);
	}

	public List<RolePo> getAllRoleByRoleId(String term, String currentRole) {
		return roleDao.getAllRoleByRoleId(term, currentRole);
	}
	
	public UserPo getCidsUserInformation(String userId) {
		return userDao.getCidsUserInformation(userId);	
	}
	
	// Added for UT29849
	public List<UserPo> getReturnUserFromWorkflowHistoryAll(String requestNo, String nextWFGroup) {
		return userDao.getReturnUserFromWorkflowHistoryAll(requestNo, nextWFGroup);
	}

	// Added for UT30107
	@Transactional
	public void updateDefaultRole(String userId, String selectedRoleId, UserPo user) {
		userRoleDao.resetDefaultRole(userId, user);
		
		userRoleDao.updateDefaultRole(userId, selectedRoleId, user); 
	}
}

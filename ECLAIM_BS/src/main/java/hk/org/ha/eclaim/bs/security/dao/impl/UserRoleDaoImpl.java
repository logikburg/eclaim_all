package hk.org.ha.eclaim.bs.security.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.bs.security.dao.IUserRoleDao;
import hk.org.ha.eclaim.bs.security.po.RolePo;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.po.UserRolePo;

@Repository
public class UserRoleDaoImpl implements IUserRoleDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	public int insertUserRole(UserPo newUser, RolePo role,
							   String clusterCode, 
							   String instCode, 
							   String deptCode, 
							   String staffGroupCode, 
							   UserPo user) {
		UserRolePo existingRole = getUserRole(newUser.getUserId(), role.getRoleId());
		int userRoleId = -1;
		if (existingRole != null) {
			existingRole.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
			existingRole.setUpdatedBy(user.getUserId());
			existingRole.setUpdatedRoleId(user.getCurrentRole());
			existingRole.setUpdatedDate(new Date());
			
			entityManager.merge(existingRole);
			
			userRoleId = existingRole.getUserRoleId();
		}
		else {
			UserRolePo newUserRole = new UserRolePo();
			newUserRole.setUser(newUser);
			newUserRole.setRole(role);
			newUserRole.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
			newUserRole.setCreatedBy(user.getUserId());
			newUserRole.setCreatedRoleId(user.getCurrentRole());
			newUserRole.setCreatedDate(new Date());
			newUserRole.setUpdatedBy(user.getUserId());
			newUserRole.setUpdatedRoleId(user.getCurrentRole());
			newUserRole.setUpdatedDate(new Date());
			
			entityManager.persist(newUserRole);
			
			userRoleId = newUserRole.getUserRoleId();
		}
		
		return userRoleId;
	}

	public void deleteUserRole(String userId, String roleId, UserPo user) {
		UserRolePo existingRole = getUserRole(userId, roleId);
		
		existingRole.setRecState(MPRSConstant.MPRS_STATE_INACTIVE);
		existingRole.setUpdatedBy(user.getUserId());
		existingRole.setUpdatedRoleId(user.getCurrentRole());
		existingRole.setUpdatedDate(new Date());
		
		entityManager.merge(existingRole);
	}
	
	public UserRolePo getUserRole(String userId, String roleId) {
		// Query q = entityManager.createNativeQuery("SELECT C FROM UserRolePo C where C.user.userId = :userId and C.role.roleId = :roleId ", UserRolePo.class);
		Query q = entityManager.createQuery("SELECT C FROM UserRolePo C where C.user.userId = :userId and C.role.roleId = :roleId ", UserRolePo.class);
		q.setParameter("userId", userId);
		q.setParameter("roleId", roleId);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		if (result.hasNext()) {
			return (UserRolePo)result.next();
		}
		
		return null;
	}
	
	public List<UserRolePo> findListUserRoleByUserId(String userId) {
		// Query q = entityManager.createNativeQuery("SELECT C FROM UserRolePo C where C.user.userId = :userId and C.role.roleId = :roleId ", UserRolePo.class);
		Query q = entityManager.createQuery("SELECT C FROM UserRolePo C where C.user.userId = :userId and C.recState = :recState ", UserRolePo.class);
		q.setParameter("userId", userId);
		q.setParameter("recState", MPRSConstant.MPRS_STATE_ACTIVE);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		List<UserRolePo> resultList = new ArrayList<UserRolePo>();
		UserRolePo c = null;
		while (result.hasNext()) {
			c = (UserRolePo)result.next();
			resultList.add(c);
		}
		
		return resultList;
	}
	
	public void updateDefaultRole(String userId, String selectedRoleId, UserPo user) {
		UserRolePo existingRole = getUserRole(userId, selectedRoleId);
		existingRole.setDefaultRole("Y");
		existingRole.setUpdatedBy(user.getUserId());
		existingRole.setUpdatedRoleId(user.getCurrentRole());
		existingRole.setUpdatedDate(new Date());

		entityManager.merge(existingRole);
	}
	
	public void resetDefaultRole(String userId, UserPo user) {
		// Get All User Role
		List<UserRolePo> userRoleList = findListUserRoleByUserId(userId);
		
		for (int i=0; i<userRoleList.size(); i++) {
			userRoleList.get(i).setDefaultRole("");
			userRoleList.get(i).setUpdatedBy(user.getUserId());
			userRoleList.get(i).setUpdatedRoleId(user.getCurrentRole());
			userRoleList.get(i).setUpdatedDate(new Date());
			
			entityManager.merge(userRoleList.get(i));
		}
	}
}

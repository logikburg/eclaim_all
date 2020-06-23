package hk.org.ha.eclaim.bs.security.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.bs.security.dao.IUserDao;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.po.UserRolePo;
import hk.org.ha.eclaim.core.dao.impl.AbstractBaseDaoImpl;
import hk.org.ha.eclaim.core.logger.EClaimLogger;

@Repository("userDao")
public class UserDaoImpl extends AbstractBaseDaoImpl<UserPo> implements IUserDao {
	
	@Value("${mail.appurl.prefix}")
	private String appUrl;
	
	//@PersistenceContext(unitName="cids")
	protected EntityManager cidsEntityManager;
	
	public UserPo findUserById(String id) {
		Query q = entityManager.createQuery("select C from UserPo C where recState = :recState and userId = :userId ", UserPo.class);
		q.setParameter("recState", MPRSConstant.MPRS_STATE_ACTIVE);
		q.setParameter("userId", id);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		UserPo c = null;
		if (result.hasNext()) {
			c = (UserPo)result.next();
		}

		if (c != null) {
			List<UserRolePo> roleList = new ArrayList<UserRolePo>();
			if (c.getUserRole() != null) {
				for (int i=0; i<c.getUserRole().size(); i++) {
					if (MPRSConstant.MPRS_STATE_ACTIVE.equals(c.getUserRole().get(i).getRecState())) {
						roleList.add(c.getUserRole().get(i));
					}
				}
				
				c.setUserRole(roleList);
			}
		}
		return c;
	}

	public List<UserPo> getAllUser() {
		List<UserPo> resultList = new ArrayList<UserPo>();
		Query q = entityManager.createQuery("select C from UserPo C where recState = :recState order by C.userName ", UserPo.class);
		q.setParameter("recState", MPRSConstant.MPRS_STATE_ACTIVE);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		UserPo c = null;
		while (result.hasNext()) {
			c = (UserPo)result.next();
			resultList.add(c);
		}
		
		return resultList;
		
	}

	public void insertUser(UserPo updatedUserPo) {
		entityManager.persist(updatedUserPo);
	}
	
	public void updateUser(UserPo updatedUserPo) {
		entityManager.merge(updatedUserPo);
	}

	public List<UserPo> getAllUser(String groupId) {
		List<UserPo> resultList = new ArrayList<UserPo>();
		Query q = entityManager.createQuery("select P from UserPo P INNER JOIN P.userRole r where r.role.roleId = :groupId and P.recState = :recState order by P.userName ", UserPo.class);
		q.setParameter("groupId", groupId);
		q.setParameter("recState", MPRSConstant.MPRS_STATE_ACTIVE);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		UserPo c = null;
		while (result.hasNext()) {
			c = (UserPo)result.next();
			resultList.add(c);
		}
		
		System.out.println("resultList.size(): " + resultList.size());
		
		return resultList;
	}

	public List<UserPo> getAllUserByGroupAndCluster(String clusterCode, String instCode, String groupId) {
		String sql = "select u.* ";
		sql += " from SS_USER u, SS_USER_ROLE ur ";
		sql += " LEFT JOIN SS_USER_ROLE_DATA_ACCESS d on ur.USER_ROLE_UID = d.USER_ROLE_UID and d.rec_state = :recState ";
		sql += " LEFT JOIN CS_INST i on d.CLUSTER_CODE = i.CLUSTER_CODE and (d.INST_CODE = i.INST_CODE or d.INST_CODE is null) or (d.CLUSTER_CODE is null and d.INST_CODE is null) and i.rec_state = :recState ";
		sql += " where u.USER_ID = ur.USER_ID ";
		sql += " and ur.ROLE_ID = :groupId ";
		sql += " and i.CLUSTER_CODE = :clusterCode ";
		sql += " and i.INST_CODE = :instCode ";
		sql += " and u.REC_STATE = :recState ";
		sql += " and u.ACCOUNT_STATUS = :recState ";
		sql += " and ur.REC_STATE = :recState ";
		sql += " order by u.user_Name ";
	
		List<UserPo> resultList = new ArrayList<UserPo>();
		Query q = entityManager.createNativeQuery(sql, UserPo.class);
		q.setParameter("groupId", groupId);
		q.setParameter("clusterCode", clusterCode);
		q.setParameter("instCode", instCode);
		q.setParameter("recState", MPRSConstant.MPRS_STATE_ACTIVE);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		UserPo c = null;
		while (result.hasNext()) {
			c = (UserPo)result.next();
			resultList.add(c);
		}
		
		System.out.println("resultList.size(): " + resultList.size());
		
		return resultList;
	}

	public List<UserPo> searchUser(String userId, String userName, boolean activeOnly) {
		System.out.println("userId:" + userId + "===");
		System.out.println("userName:" + userName + "===");
		String sql = "select C from UserPo C ";
		
		boolean haveCriteria = false;
		if (activeOnly) {
			sql += " WHERE C.accountStatus = 'A' ";
			haveCriteria = true;		
		}
		
		if (!"".equals(userId) && userId != null) {
			if (haveCriteria) 
				sql += " AND ";
			else 
				sql += " WHERE ";
			
			sql += " upper(userId) like upper(:userId) ";
			haveCriteria = true;		
		}
		
		if (!"".equals(userName) && userName != null) {
			if (haveCriteria) 
				sql += " AND ";
			else 
				sql += " WHERE ";
			
			sql += " upper(userName) like upper(:userName) ";
			haveCriteria = true;		
		}
		
		sql += " order by C.userId ";
		
		List<UserPo> resultList = new ArrayList<UserPo>();
		Query q = entityManager.createQuery(sql, UserPo.class);
		if (!"".equals(userId) && userId != null) {
			q.setParameter("userId", "%" + userId.toUpperCase() + "%");
		}
		
		if (!"".equals(userName) && userName != null) {
			q.setParameter("userName", "%" + userName.toUpperCase() + "%");
		}
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		UserPo c = null;
		while (result.hasNext()) {
			c = (UserPo)result.next();
			resultList.add(c);
		}
		
		return resultList;
	}
	
	public List<UserPo> searchUser(String userId, String userName, String loginUserId, String roleId, boolean activeOnly) {
		String sql = " select distinct u.* from SS_USER u ";
		sql += "left join SS_USER_ROLE ur on u.USER_ID = ur.USER_ID and ur.REC_STATE = 'A' ";
		sql += "left join SS_USER_ROLE_DATA_ACCESS da on ur.USER_ROLE_UID = da.USER_ROLE_UID  and da.REC_STATE = 'A' ";
		sql += " where u.REC_STATE = 'A' ";
		
		if (activeOnly) {
			sql += " AND u.ACCOUNT_STATUS = 'A' ";
		}

		boolean haveCriteria = true;
		if (!"HOSP_USER_ADM".equals(roleId)) {
			sql += " and ";
			if ("HO_ADM".equals(roleId)) {
				sql += " ( ";
			}

			sql += " (da.cluster_code)  in "; 
			sql += "     (select i.cluster_code from SS_USER_ROLE ur, SS_USER_ROLE_DATA_ACCESS d "; 
			sql += "   LEFT JOIN CS_INST i on (d.CLUSTER_CODE = i.CLUSTER_CODE and (d.INST_CODE = i.INST_CODE or d.INST_CODE is null)) or (d.CLUSTER_CODE is null and d.INST_CODE is null) ";
			sql += " where ur.USER_ROLE_UID = d.USER_ROLE_UID and ur.USER_ID = :loginUser and ur.ROLE_ID = :loginRole ) ";  

			if ("HO_ADM".equals(roleId)) {
				sql += " or da.cluster_code is null )";
			}
		}
		else {
			sql += " and (da.cluster_code) in "; 
			sql += " (select i.cluster_code from SS_USER_ROLE ur, SS_USER_ROLE_DATA_ACCESS d "; 
			sql += "  LEFT JOIN CS_INST i on d.CLUSTER_CODE = i.CLUSTER_CODE and d.INST_CODE = i.INST_CODE ";
			sql += " where ur.USER_ROLE_UID = d.USER_ROLE_UID and ur.USER_ID = :loginUser and ur.ROLE_ID = :loginRole ) ";
		}
		
		
		if (!"".equals(userId) && userId != null) {
			if (haveCriteria) 
				sql += " AND ";
			else 
				sql += " WHERE ";
			
			sql += " upper(u.USER_ID) like upper(:userId) ";
			haveCriteria = true;		
		}
		
		if (!"".equals(userName) && userName != null) {
			if (haveCriteria) 
				sql += " AND ";
			else 
				sql += " WHERE ";
			
			sql += " upper(u.USER_NAME) like upper(:userName) ";
			haveCriteria = true;		
		}
		
		sql += " order by u.USER_ID ";
		
		List<UserPo> resultList = new ArrayList<UserPo>();
		Query q = entityManager.createNativeQuery(sql, UserPo.class);
		q.setParameter("loginUser", loginUserId);
		q.setParameter("loginRole", roleId);
		if (!"".equals(userId) && userId != null) {
			q.setParameter("userId", "%" + userId.toUpperCase() + "%");
		}
		
		if (!"".equals(userName) && userName != null) {
			q.setParameter("userName", "%" + userName.toUpperCase() + "%");
		}
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		UserPo c = null;
		while (result.hasNext()) {
			c = (UserPo)result.next();
			resultList.add(c);
		}
		
		return resultList;
	}
	
	public List<UserPo> getRelatedUserFromWorkflowHistoryAll(String requestId) {
		List<UserPo> resultList = new ArrayList<UserPo>();
		Query q = entityManager.createNativeQuery("select s.* from SS_USER s, XXEC_WORKFLOW_HIST h where s.USER_ID = h.ACTION_BY and h.request_uid = :requestUid and h.rec_state = 'A'  order by h.created_date desc", UserPo.class);
		q.setParameter("requestUid", Integer.parseInt(requestId));
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		UserPo c = null;
		while (result.hasNext()) {
			c = (UserPo)result.next();
			resultList.add(c);
		}
		
		return resultList;
	}

	public List<UserPo> getAllHROfficer(int userRoleId) {
		String sql = "select distinct u.* from SS_USER u, SS_USER_ROLE r, " ;
		sql += " (select da.user_role_uid, ii.cluster_code from SS_USER_ROLE_DATA_ACCESS da " ; 
		sql += "  LEFT JOIN CS_INST ii on (da.CLUSTER_CODE = ii.CLUSTER_CODE and (da.INST_CODE = ii.INST_CODE or da.INST_CODE is null))) da " ; 
		sql += " where u.USER_ID = r.USER_ID   " ;
		sql += " and u.rec_state = :recState " ;
		sql += " and u.ACCOUNT_STATUS = :recState ";
		sql += " and r.USER_ROLE_UID = da.USER_ROLE_UID " ; 
		sql += " and (da.CLUSTER_CODE) in (select i.CLUSTER_CODE from SS_USER_ROLE ur, SS_USER_ROLE_DATA_ACCESS d " ;  
		sql += " LEFT JOIN CS_INST i on (d.CLUSTER_CODE = i.CLUSTER_CODE and (d.INST_CODE = i.INST_CODE or d.INST_CODE is null)) or (d.CLUSTER_CODE is null and d.INST_CODE is null) " ;   
		sql += " where ur.USER_ROLE_UID = d.USER_ROLE_UID and ur.USER_ROLE_UID = :userRoleUid and ur.REC_STATE = :recState and d.REC_STATE = :recState)  " ;
		sql += " order by u.USER_NAME " ;     
		
		List<UserPo> resultList = new ArrayList<UserPo>();
		Query q = entityManager.createNativeQuery(sql, UserPo.class);
		q.setParameter("userRoleUid", userRoleId);
		q.setParameter("recState", MPRSConstant.MPRS_STATE_ACTIVE);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		UserPo c = null;
		while (result.hasNext()) {
			c = (UserPo)result.next();
			resultList.add(c);
		}
		
		System.out.println("resultList.size(): " + resultList.size());
		
		return resultList;
	}
	
	public String generateCidsLogonToken(String userId, String domain) {
		String result = "";
		
		try {
			StoredProcedureQuery storedProcedure = cidsEntityManager.createStoredProcedureQuery("CID_FUNC_generate_uniqueid");
			storedProcedure.registerStoredProcedureParameter(0, String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
			storedProcedure.setParameter(0, domain);
			storedProcedure.setParameter(1, domain + "\\" + userId);
			storedProcedure.setParameter(2, "MPRS");
			storedProcedure.setParameter(3, domain + "\\" + userId);
			storedProcedure.setParameter(4, appUrl + "/maintenance/userMaintenance");
			storedProcedure.setParameter(5, "");
			storedProcedure.setParameter(6, "");
			EClaimLogger.debug("***** domain: " + domain +", userId: " + userId);
			@SuppressWarnings("rawtypes")
			List resultList = storedProcedure.getResultList();
			EClaimLogger.debug("***** resultList: " + resultList);
			if (resultList != null) {
				result = (String)resultList.get(0);
			}
		}
		catch (Exception e) {
			EClaimLogger.error("generateCidsLogonToken:" + e.getMessage(), e);
		}
		
		return result;
	}
	
	public UserPo getCidsUserInformation(String userId) {
		UserPo resultPo = null;
		
		try {
			Query q = cidsEntityManager.createNativeQuery("select usr_name, phone, perm_email from pub_userinfo_MPRS where domain_user = :userId");
			q.setParameter("userId", userId);
			
			@SuppressWarnings("rawtypes")
			Iterator result = q.getResultList().iterator();
			
			if (result.hasNext()) {
				resultPo = new UserPo();
				
				Object[] obj = (Object[])result.next();
				resultPo.setUserName(((String)obj[0]).trim());
				resultPo.setPhoneNo(((String)obj[1]).trim());
				resultPo.setEmail(((String)obj[2]).trim());
				EClaimLogger.debug("username: " + (String)obj[0]+ ";");
				EClaimLogger.debug("username(trim): " + ((String)obj[0]).trim() + ";");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			EClaimLogger.error("getCidsUserInformation:" + e.getMessage(), e);
		}
		
		return resultPo;
	}
	
	// Added for UT29849
	public List<UserPo> getReturnUserFromWorkflowHistoryAll(String requestNo, String nextWFGroup) {
		String sql = "select s.* from SS_USER s, XXEC_WORKFLOW_HIST h where s.USER_ID = h.ACTION_BY and h.request_uid = :requestUid ";
		
		if ("HR_MANAGER".equals(nextWFGroup) || "HR_OFFICER".equals(nextWFGroup)) {
			sql += " and ACTION_ROLE_ID in ('HR_MANAGER', 'HR_OFFICER') ";
		}
		
		if ("FIN_MANAGER".equals(nextWFGroup) || "FIN_OFFICER".equals(nextWFGroup)) {
			sql += " and ACTION_ROLE_ID in ('FIN_MANAGER', 'FIN_OFFICER') ";
		}
		
		// Added for ST08740
		if ("HO_MG_OFFICER".equals(nextWFGroup)) {
			sql += " and ACTION_ROLE_ID in ('HO_MG_OFFICER') ";
		}

		sql += " order by h.created_date desc";
		List<UserPo> resultList = new ArrayList<UserPo>();
		Query q = entityManager.createNativeQuery(sql, UserPo.class);
		q.setParameter("requestUid", Integer.parseInt(requestNo));
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		UserPo c = null;
		while (result.hasNext()) {
			c = (UserPo)result.next();
			resultList.add(c);
		}
		
		return resultList;
	
	}
}
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
import hk.org.ha.eclaim.bs.security.dao.IDataAccessDao;
import hk.org.ha.eclaim.bs.security.po.DataAccessPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

@Repository
public class DataAccessDaoImpl implements IDataAccessDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	public List<DataAccessPo> getDataAccessByUserId(String userId) {
		List<DataAccessPo> resultList = new ArrayList<DataAccessPo>();
		
		Query q = entityManager.createQuery("SELECT C FROM DataAccessPo C where C.userId = :userId where C.recState = :recState", DataAccessPo.class);
		q.setParameter("userId", userId);
		q.setParameter("recState", MPRSConstant.MPRS_STATE_ACTIVE);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			DataAccessPo c = (DataAccessPo)result.next();
			resultList.add(c);
		}
		
		return resultList;
	}

	public List<DataAccessPo> getDataAccessByRoleId(int roleId) {
		List<DataAccessPo> resultList = new ArrayList<DataAccessPo>();
		
		Query q = entityManager.createQuery("SELECT C FROM DataAccessPo C where C.roleId = :roleId and C.recState = :recState order by C.clusterCode, C.instCode, C.deptCode, C.staffGroupCode ", DataAccessPo.class);
		q.setParameter("roleId", roleId);
		q.setParameter("recState", MPRSConstant.MPRS_STATE_ACTIVE);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			DataAccessPo c = (DataAccessPo)result.next();
			resultList.add(c);
		}
		
		return resultList;
	}

	public void deleteDataAccess(int userRoleId, UserPo updateUser) {
		List<DataAccessPo> poList = this.getDataAccessByRoleId(userRoleId);

		for (int i=0; i<poList.size(); i++) {
			DataAccessPo po = poList.get(i);
			po.setRecState(MPRSConstant.MPRS_STATE_INACTIVE);
			po.setUpdatedBy(updateUser.getUserId());
			po.setUpdatedRoleId(updateUser.getCurrentRole());
			po.setUpdatedDate(new Date());
			
			entityManager.merge(po);
		}
	}

	public void insertDataAccess(int roleId, String clusterCode, String instCode, String deptCode, String staffGroupCode,
			UserPo user) {

		DataAccessPo access = new DataAccessPo();
		
		Query q = entityManager.createQuery("SELECT C FROM DataAccessPo C where C.roleId = :roleId and C.clusterCode =:clusterCode and C.instCode = :instCode and C.deptCode = :deptCode and C.staffGroupCode = :staffGroupCode", DataAccessPo.class);
		q.setParameter("roleId", roleId);
		q.setParameter("clusterCode", clusterCode);
		q.setParameter("instCode", instCode);
		q.setParameter("deptCode", deptCode);
		q.setParameter("staffGroupCode", staffGroupCode);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		if (result.hasNext()) {
			access = (DataAccessPo)result.next();
			
			access.setRoleId(roleId);
			access.setClusterCode(clusterCode);
			access.setInstCode(instCode);
			access.setDeptCode(deptCode); 
			access.setStaffGroupCode(staffGroupCode);
			access.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
			access.setUpdatedBy(user.getUserId());
			access.setUpdatedRoleId(user.getCurrentRole());
			access.setUpdatedDate(new Date());
			
			entityManager.merge(access);
		}
		else {
			access.setRoleId(roleId);
			access.setClusterCode(clusterCode);
			access.setInstCode(instCode);
			access.setDeptCode(deptCode); 
			access.setStaffGroupCode(staffGroupCode);
			access.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
			access.setCreatedBy(user.getUserId());
			access.setCreatedRoleId(user.getCurrentRole());
			access.setCreatedDate(new Date());
			access.setUpdatedBy(user.getUserId());
			access.setUpdatedRoleId(user.getCurrentRole());
			access.setUpdatedDate(new Date());
			
			entityManager.persist(access);
		}
	}

	public boolean haveAccessRight(String clusterCode, String instCode, String userId, String roleId) {
		String sql = "select i.CLUSTER_CODE, i.inst_code "; 
		sql += " from SS_USER_ROLE ur, SS_USER_ROLE_DATA_ACCESS d   ";
		sql += " LEFT JOIN CS_INST i on (d.CLUSTER_CODE = i.CLUSTER_CODE and (d.INST_CODE = i.INST_CODE or d.INST_CODE is null)) or (d.CLUSTER_CODE is null and d.INST_CODE is null) ";   
		sql += " where ur.USER_ROLE_UID = d.USER_ROLE_UID  ";
		sql += " and ur.user_id = :userId ";
		sql += " and ur.role_id = :roleId ";
		sql += " and ur.rec_state = :recState ";
		
		Query q = entityManager.createNativeQuery(sql);
		q.setParameter("userId", userId);
		q.setParameter("roleId", roleId);
		q.setParameter("recState", MPRSConstant.MPRS_STATE_ACTIVE);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			Object[] obj = (Object[]) result.next();
			String tmpClusterCode = (String)obj[0];
			String tmpInstCode = (String)obj[1];
			
			if (tmpClusterCode.equals(clusterCode) && tmpInstCode.equals(instCode)) {
				return true;
			}
		}
		
		return false;
	}
}

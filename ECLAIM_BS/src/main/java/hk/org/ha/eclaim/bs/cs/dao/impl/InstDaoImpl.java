package hk.org.ha.eclaim.bs.cs.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.bs.cs.dao.IInstDao;
import hk.org.ha.eclaim.bs.cs.po.InstitutionPo;;

@Repository
public class InstDaoImpl implements IInstDao{
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public List<InstitutionPo> getAllInst() {
		List<InstitutionPo> resultList = new ArrayList<InstitutionPo>();
		
		Query q = entityManager.createQuery("SELECT C FROM InstitutionPo C order by C.instCode", InstitutionPo.class);
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			InstitutionPo c = (InstitutionPo)result.next();
			resultList.add(c);
		}
		
		return resultList;
	}

	public List<InstitutionPo> getAllInstByCluster(String clusterCode) {
		List<InstitutionPo> resultList = new ArrayList<InstitutionPo>();
		
		Query q = entityManager.createQuery("SELECT C FROM InstitutionPo C where C.clusterCode = :clusterCode order by C.instCode", InstitutionPo.class);
		q.setParameter("clusterCode", clusterCode);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			InstitutionPo c = (InstitutionPo)result.next();
			resultList.add(c);
		}
		
		return resultList;
	}

	public List<InstitutionPo> getInstByUser(String userId, String roleId) {
		List<InstitutionPo> resultList = new ArrayList<InstitutionPo>();
		
		String sql = "select C.* from CS_INST C where C.inst_code in (select i.inst_code from SS_USER_ROLE ur, SS_USER_ROLE_DATA_ACCESS d  ";
        sql += " LEFT JOIN CS_INST i on (d.CLUSTER_CODE = i.CLUSTER_CODE and (d.INST_CODE = i.INST_CODE or d.INST_CODE is null)) or (d.CLUSTER_CODE is null and d.INST_CODE is null)   ";
        sql += " where ur.USER_ROLE_UID = d.USER_ROLE_UID and ur.USER_ID = :userId and ur.ROLE_ID = :roleId and ur.REC_STATE = :state and d.REC_STATE = :state) order by 1 ";
		
		Query q = entityManager.createNativeQuery(sql, InstitutionPo.class);
		q.setParameter("userId", userId);
		q.setParameter("roleId", roleId);
		q.setParameter("state", MPRSConstant.MPRS_STATE_ACTIVE);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			InstitutionPo c = (InstitutionPo)result.next();
			resultList.add(c);
		}
		
		return resultList;
	}

	public List<InstitutionPo> getInstByUserForHospitalAdmin(String userId) {
		List<InstitutionPo> resultList = new ArrayList<InstitutionPo>();
		
		String sql = "select C.* from CS_INST C where C.inst_code in (select i.inst_code from SS_USER_ROLE ur, SS_USER_ROLE_DATA_ACCESS d  ";
		sql += " LEFT JOIN CS_INST i on d.CLUSTER_CODE = i.CLUSTER_CODE and d.INST_CODE = i.INST_CODE   ";
	    sql += " where ur.USER_ROLE_UID = d.USER_ROLE_UID and ur.USER_ID = :userId and ur.ROLE_ID = :roleId and ur.REC_STATE = :state and d.REC_STATE = :state) order by 1 ";
		
		Query q = entityManager.createNativeQuery(sql, InstitutionPo.class);
		q.setParameter("userId", userId);
		q.setParameter("roleId", "HOSP_USER_ADM");
		q.setParameter("state", MPRSConstant.MPRS_STATE_ACTIVE);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			InstitutionPo c = (InstitutionPo)result.next();
			resultList.add(c);
		}
		
		return resultList;
	}

	public List<InstitutionPo> getInstByClusterAndUser(String clusterCode, String userId, String roleId) {
		List<InstitutionPo> resultList = new ArrayList<InstitutionPo>();
		
		String sql = "select C.* from CS_INST C where C.inst_code in (select i.inst_code from SS_USER_ROLE ur, SS_USER_ROLE_DATA_ACCESS d  ";
        sql += " LEFT JOIN CS_INST i on (d.CLUSTER_CODE = i.CLUSTER_CODE and (d.INST_CODE = i.INST_CODE or d.INST_CODE is null)) or (d.CLUSTER_CODE is null and d.INST_CODE is null)   ";
        sql += " where ur.USER_ROLE_UID = d.USER_ROLE_UID and ur.USER_ID = :userId and ur.ROLE_ID = :roleId and ur.REC_STATE = :state and d.REC_STATE = :state) ";
        
        if (clusterCode != null && !"".equals(clusterCode)) {
        	sql += " AND C.cluster_code = :clusterCode ";
        }
        sql += " order by 1 ";
		
		Query q = entityManager.createNativeQuery(sql, InstitutionPo.class);
		if (clusterCode != null && !"".equals(clusterCode)) {
			q.setParameter("clusterCode", clusterCode);
		}
		q.setParameter("userId", userId);
		q.setParameter("roleId", roleId);
		q.setParameter("state", MPRSConstant.MPRS_STATE_ACTIVE);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			InstitutionPo c = (InstitutionPo)result.next();
			resultList.add(c);
		}
		
		return resultList;
	}
	
	public List<InstitutionPo> getInstByClusterForHospitalAdmin(String clusterCode, String userId) {
		List<InstitutionPo> resultList = new ArrayList<InstitutionPo>();
		
		String sql = "select C.* from CS_INST C where C.cluster_code = :clusterCode and C.inst_code in (select i.inst_code from SS_USER_ROLE ur, SS_USER_ROLE_DATA_ACCESS d  ";
        sql += " LEFT JOIN CS_INST i on d.CLUSTER_CODE = i.CLUSTER_CODE and d.INST_CODE = i.INST_CODE   ";
        sql += " where ur.USER_ROLE_UID = d.USER_ROLE_UID and ur.USER_ID = :userId and ur.ROLE_ID = :roleId and ur.REC_STATE = :state and d.REC_STATE = :state) order by 1 ";
		
		System.out.println(sql);
		
		Query q = entityManager.createNativeQuery(sql, InstitutionPo.class);
		q.setParameter("clusterCode", clusterCode);
		q.setParameter("userId", userId);
		q.setParameter("roleId", "HOSP_USER_ADM");
		q.setParameter("state", MPRSConstant.MPRS_STATE_ACTIVE);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			InstitutionPo c = (InstitutionPo)result.next();
			resultList.add(c);
		}
		
		return resultList;
	}
	
	public InstitutionPo getInstNameByInstCode(String instCode) {
		InstitutionPo c = new InstitutionPo();
		Query q = entityManager.createQuery("SELECT C FROM InstitutionPo C where instCode = :instCode", InstitutionPo.class);
		q.setParameter("instCode", instCode);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		if (result.hasNext()) {
			c =(InstitutionPo)result.next();
		}
		return c;
	}
}

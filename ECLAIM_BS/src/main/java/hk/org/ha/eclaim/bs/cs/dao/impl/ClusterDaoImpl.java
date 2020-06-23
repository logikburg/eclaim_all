package hk.org.ha.eclaim.bs.cs.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.bs.cs.dao.IClusterDao;
import hk.org.ha.eclaim.bs.cs.po.ClusterPo;

@Repository
public class ClusterDaoImpl implements IClusterDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	public List<ClusterPo> getAllCluster() {
		List<ClusterPo> resultList = new ArrayList<ClusterPo>();
		
		Query q = entityManager.createQuery("SELECT C FROM ClusterPo C order by C.clusterCode", ClusterPo.class);
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			ClusterPo c = (ClusterPo)result.next();
			resultList.add(c);
		}
		
		return resultList;
	}
	
	public List<ClusterPo> getAllClusterForHospitalAdmin() {
		List<ClusterPo> resultList = new ArrayList<ClusterPo>();
		
		Query q = entityManager.createQuery("SELECT C FROM ClusterPo C where C.clusterCode <> 'HO' order by C.clusterCode", ClusterPo.class);
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			ClusterPo c = (ClusterPo)result.next();
			resultList.add(c);
		}
		
		return resultList;
	}

	public List<ClusterPo> getClusterByUser(String userId, String roleId) {
		List<ClusterPo> resultList = new ArrayList<ClusterPo>();
		
		String sql = "select C.* from CS_CLUSTER C where C.cluster_code in (select i.CLUSTER_CODE from SS_USER_ROLE ur, SS_USER_ROLE_DATA_ACCESS d  ";
        sql += " LEFT JOIN CS_INST i on (d.CLUSTER_CODE = i.CLUSTER_CODE and (d.INST_CODE = i.INST_CODE or d.INST_CODE is null)) or (d.CLUSTER_CODE is null and d.INST_CODE is null)   ";
        sql += " where ur.USER_ROLE_UID = d.USER_ROLE_UID and ur.USER_ID = :userId and ur.ROLE_ID = :roleId and ur.REC_STATE = :state and d.REC_STATE = :state) order by C.cluster_code ";
		
		Query q = entityManager.createNativeQuery(sql, ClusterPo.class);
		q.setParameter("userId", userId);
		q.setParameter("roleId", roleId);
		q.setParameter("state", MPRSConstant.MPRS_STATE_ACTIVE);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			ClusterPo c = (ClusterPo)result.next();
			resultList.add(c);
		}
		
		return resultList;
	}
}

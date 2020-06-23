package hk.org.ha.eclaim.bs.cs.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import hk.org.ha.eclaim.bs.cs.dao.IOrganizationDao;
import hk.org.ha.eclaim.bs.cs.po.OrganizationPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

@Repository
public class OrganizationDaoImpl implements IOrganizationDao{
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public List<OrganizationPo> getAllOrganization() {
		
		List<OrganizationPo> poList = new ArrayList<OrganizationPo>();
		Query query = entityManager.createQuery("SELECT C FROM OrganizationPo C",OrganizationPo.class);
		@SuppressWarnings("rawtypes")
		Iterator result = query.getResultList().iterator();
		
		while (result.hasNext()) {
			OrganizationPo c = (OrganizationPo)result.next();
			poList.add(c);
		}
		
		return poList;
	}

	public List<OrganizationPo> getAllActiveOrganization() {
		List<OrganizationPo> poList = new ArrayList<OrganizationPo>();
		Query query = entityManager.createQuery("SELECT C FROM OrganizationPo C WHERE sysdate >= C.dateFrom and sysdate <= nvl(C.dateTo,sysdate)",OrganizationPo.class);
		@SuppressWarnings("rawtypes")
		Iterator result = query.getResultList().iterator();
		
		while (result.hasNext()) {
			OrganizationPo c = (OrganizationPo)result.next();
			poList.add(c);
		}
		
		return poList;
	}

	public OrganizationPo getOrganizationByOrganizationId(int organizationId) {
		Query query = entityManager.createQuery("SELECT C FROM OrganizationPo C WHERE organizationId = :organizationId",OrganizationPo.class);
		query.setParameter("organizationId", organizationId);
		@SuppressWarnings("rawtypes")
		Iterator result = query.getResultList().iterator();
		
		while (result.hasNext()) {
			return (OrganizationPo)result.next();
		}
		
		return null;
	}
	
	public String getOrgNameByOrgId(int organizationId) {
		Query query = entityManager.createQuery("select name FROM OrganizationPo where organizationId = :organizationId");
		query.setParameter("organizationId", organizationId);
		@SuppressWarnings("rawtypes")
		Iterator result = query.getResultList().iterator();
		
		while (result.hasNext()) {
			return (String)result.next();
		}
		
		return null;
	}
	
	public String getClusIdByOrgId(int organizationId) {
		Query query = entityManager.createQuery("select clusId FROM OrganizationPo where organizationId = :organizationId");
		query.setParameter("organizationId", organizationId);
		@SuppressWarnings("rawtypes")
		Iterator result = query.getResultList().iterator();
		
		while (result.hasNext()) {
			return (String)result.next();
		}
		
		return null;
	}
	public List<OrganizationPo> searchOrganization(String name, String loginUserId, String roleId, boolean activeOnly) {
		String sql = "select u from OrganizationPo u ";
//		sql += "left join SS_USER_ROLE ur on u.USER_ID = ur.USER_ID and ur.REC_STATE = 'A' ";
//		sql += "left join SS_USER_ROLE_DATA_ACCESS da on ur.USER_ROLE_UID = da.USER_ROLE_UID  and da.REC_STATE = 'A' ";
		sql += " where 1=1 ";
		
//		if (activeOnly) {
//			sql += " AND u.ACCOUNT_STATUS = 'A' ";
//		}

		if (!"".equals(name)) {
			sql += " and upper(u.name) like upper(:name) ";
		}
		sql += " and rownum < 11 ";
		sql += " order by u.name ";
		
		List<OrganizationPo> resultList = new ArrayList<OrganizationPo>();
		Query q = entityManager.createQuery(sql, OrganizationPo.class);
//		q.setParameter("loginUser", loginUserId);
//		q.setParameter("loginRole", roleId);
//		if (!"".equals(userId) && userId != null) {
//			q.setParameter("userId", "%" + userId.toUpperCase() + "%");
//		}
//		
		if (!"".equals(name)) {
			q.setParameter("name", name + "%");
		}
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			resultList.add((OrganizationPo)result.next());
		}
		
		return resultList;
	}
}

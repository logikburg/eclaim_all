package hk.org.ha.eclaim.bs.security.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.security.dao.IRoleDao;
import hk.org.ha.eclaim.bs.security.po.RolePo;

@Repository
public class RoleDaoImpl implements IRoleDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	public List<RolePo> getAllRole() {
		List<RolePo> resultList = new ArrayList<RolePo>();
		Query q = entityManager.createQuery("select C from RolePo C order by C.roleName ", RolePo.class);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		RolePo c = null;
		while (result.hasNext()) {
			c = (RolePo)result.next();
			resultList.add(c);
		}
		
		return resultList;
	}
	
	public List<RolePo> getAllRole(String term) {
		List<RolePo> resultList = new ArrayList<RolePo>();
		Query q = entityManager.createQuery("select C from RolePo C where upper(C.roleName) like :roleName order by C.roleName ", RolePo.class);
		q.setParameter("roleName", "%" + term.toUpperCase() + "%");
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		RolePo c = null;
		while (result.hasNext()) {
			c = (RolePo)result.next();
			resultList.add(c);
		}
		
		return resultList;
	}

	public RolePo getRoleById(String roleId) {
		Query q = entityManager.createQuery("select C from RolePo C where C.roleId like :roleId ", RolePo.class);
		q.setParameter("roleId", roleId);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			return (RolePo)result.next();
		}
		
		return null;
	}

	public List<RolePo> getAllRoleByRoleId(String term, String currentRole) {
		List<RolePo> resultList = new ArrayList<RolePo>();
		Query q = entityManager.createQuery("select C from RolePo C where upper(C.roleName) like :roleName order by C.roleName ", RolePo.class);
		q.setParameter("roleName", "%" + term.toUpperCase() + "%");
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		RolePo c = null;
		while (result.hasNext()) {
			c = (RolePo)result.next();
			
			if ("HO_ADM".equals(currentRole)) {
				if (c.getRoleId().startsWith("HO_") || "CLUS_USER_ADM".equals(c.getRoleId())) {
					resultList.add(c);
				}
			}
			else if ("CLUS_USER_ADM".equals(currentRole)) {
				if (!c.getRoleId().startsWith("HO_")) {
					resultList.add(c);
				}
			}
			else if ("HOSP_USER_ADM".equals(currentRole)) {
				if (!c.getRoleId().startsWith("HO_") && !"CLUS_USER_ADM".equals(c.getRoleId())  && !"HOSP_USER_ADM".equals(c.getRoleId())) {
					resultList.add(c);
				}
			}
		}
		
		return resultList;
	}
}

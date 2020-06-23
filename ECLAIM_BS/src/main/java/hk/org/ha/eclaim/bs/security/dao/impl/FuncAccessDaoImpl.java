package hk.org.ha.eclaim.bs.security.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.security.dao.IFuncAccessDao;
import hk.org.ha.eclaim.bs.security.po.MPRSFunctionPo;
import hk.org.ha.eclaim.bs.security.po.RoleAccessPo;

@Repository
public class FuncAccessDaoImpl implements IFuncAccessDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	public List<MPRSFunctionPo> getFunctionListByRole(String roleId) {
		List<MPRSFunctionPo> resultList = new ArrayList<MPRSFunctionPo>();
		
		Query q = entityManager.createQuery("SELECT C FROM RoleAccessPo C where C.pk.roleId = :roleId", RoleAccessPo.class);
		q.setParameter("roleId", roleId);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			RoleAccessPo c = (RoleAccessPo)result.next();
			resultList.add(c.getFunc());
		}
		
		return resultList;
	}
}

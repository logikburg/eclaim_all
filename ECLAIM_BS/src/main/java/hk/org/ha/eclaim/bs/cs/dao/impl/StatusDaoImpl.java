package hk.org.ha.eclaim.bs.cs.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.cs.dao.IRequestStatusDao;
import hk.org.ha.eclaim.bs.cs.dao.IStatusDao;
import hk.org.ha.eclaim.bs.cs.po.RequestStatusPo;
import hk.org.ha.eclaim.bs.cs.po.StatusPo;

@Repository
public class StatusDaoImpl implements IStatusDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	public List<StatusPo> getAllStatus() {
		List<StatusPo> resultList = new ArrayList<StatusPo>();
		
		Query q = entityManager.createQuery("SELECT C FROM StatusPo C", StatusPo.class);
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			StatusPo c = (StatusPo)result.next();
			resultList.add(c);
		}
		
		return resultList;
	}
	public String getStatusDesc(String statusCode) {
		StatusPo po = entityManager.find(StatusPo.class, statusCode);
		return po != null ? po.getStatusDesc() : "";
	}
}

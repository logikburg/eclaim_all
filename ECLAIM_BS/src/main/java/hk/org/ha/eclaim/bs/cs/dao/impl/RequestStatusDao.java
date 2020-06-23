package hk.org.ha.eclaim.bs.cs.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.cs.dao.IRequestStatusDao;
import hk.org.ha.eclaim.bs.cs.po.RequestStatusPo;

@Repository
public class RequestStatusDao implements IRequestStatusDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	public List<RequestStatusPo> getAllRequestStatus() {
		List<RequestStatusPo> resultList = new ArrayList<RequestStatusPo>();
		
		Query q = entityManager.createQuery("SELECT C FROM RequestStatusPo C", RequestStatusPo.class);
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			RequestStatusPo c = (RequestStatusPo)result.next();
			resultList.add(c);
		}
		
		return resultList;
	}

}

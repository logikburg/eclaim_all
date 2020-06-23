package hk.org.ha.eclaim.bs.request.dao.impl;

import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.request.po.RequestPostPo;
import hk.org.ha.eclaim.bs.request.dao.IRequestPositionDao;

@Repository
public class RequestPositionDaoImpl implements IRequestPositionDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	public RequestPostPo getRequestPositionByUid(int requestPositionUid) {
		Query q = entityManager.createQuery("SELECT C FROM RequestPostPo C where C.requestPostId = :requestPostId", RequestPostPo.class);
		q.setParameter("requestPostId", requestPositionUid);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		if (result.hasNext()) {
			return (RequestPostPo)result.next();
		}
		
		return null;
	}
}

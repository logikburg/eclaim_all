package hk.org.ha.eclaim.bs.cs.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.cs.dao.ICircumstanceDao;
import hk.org.ha.eclaim.bs.cs.po.CircumstancePo;

@Repository
public class CircumstanceDaoImpl implements ICircumstanceDao{
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public List<CircumstancePo> getAllCircum() {
		
		List<CircumstancePo> poList = new ArrayList<CircumstancePo>();
		Query query = entityManager.createQuery("SELECT C FROM CIRCUMSTANCEPO C",CircumstancePo.class);
		@SuppressWarnings("rawtypes")
		Iterator result = query.getResultList().iterator();
		
		while (result.hasNext()) {
			CircumstancePo c = (CircumstancePo)result.next();
			poList.add(c);
		}
		
		return poList;
	}

	public List<CircumstancePo> getAllActiveCircum() {
		
		List<CircumstancePo> poList = new ArrayList<CircumstancePo>();
		Query query = entityManager.createQuery("SELECT C FROM CircumstancePo C WHERE SYSDATE BETWEEN C.startDate and C.endDate order by C.displaySeq",CircumstancePo.class);
		@SuppressWarnings("rawtypes")
		Iterator result = query.getResultList().iterator();
		
		while (result.hasNext()) {
			CircumstancePo c = (CircumstancePo)result.next();
			poList.add(c);
		}
		
		return poList;
	}
	
	public CircumstancePo getCircumPoByCircumId(Integer circumId) {
		CircumstancePo po = new CircumstancePo();
		Query query = entityManager.createQuery("SELECT C FROM CircumstancePo C WHERE circumstanceId = :circumId",CircumstancePo.class);
		query.setParameter("circumId", circumId);
		@SuppressWarnings("rawtypes")
		Iterator result = query.getResultList().iterator();
		
		if (result.hasNext()) {
			po =(CircumstancePo)result.next();
		}
		return po;
	}
	
	public CircumstancePo getCircumPoByCircumCode(String circumCode) {
		CircumstancePo po = new CircumstancePo();
		Query query = entityManager.createQuery("SELECT C FROM CircumstancePo C WHERE circumstanceCode = :circumCode",CircumstancePo.class);
		query.setParameter("circumCode", circumCode);
		@SuppressWarnings("rawtypes")
		Iterator result = query.getResultList().iterator();
		
		if (result.hasNext()) {
			po =(CircumstancePo)result.next();
		}
		return po;
	}

}

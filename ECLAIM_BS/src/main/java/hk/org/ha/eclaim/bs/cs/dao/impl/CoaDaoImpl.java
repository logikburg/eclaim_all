package hk.org.ha.eclaim.bs.cs.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.cs.dao.ICoaDao;
import hk.org.ha.eclaim.bs.cs.po.CoaPo;

@Repository
public class CoaDaoImpl implements ICoaDao{

	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<CoaPo> getAllCOA(String coaName) {
		List<CoaPo> resultList = new ArrayList<CoaPo>();
		
		Query q = entityManager.createQuery("SELECT p FROM CoaPo p where coaName = :coaName", CoaPo.class);
		q.setParameter("coaName",coaName);
		
		List<Object> result = q.getResultList();
		
		for(Object obj : result) {
			if(obj != null) {
				resultList.add((CoaPo)obj);
			}
		}
		
		return resultList;
	}

}

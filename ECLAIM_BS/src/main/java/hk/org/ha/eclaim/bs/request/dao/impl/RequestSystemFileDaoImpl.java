package hk.org.ha.eclaim.bs.request.dao.impl;

import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.request.po.RequestSystemFilePo;
import hk.org.ha.eclaim.bs.request.dao.IRequestSystemFileDao;

@Repository
public class RequestSystemFileDaoImpl implements IRequestSystemFileDao {

	@PersistenceContext
	private EntityManager entityManager;

	public RequestSystemFilePo getSystemFile(String code) {
		Query q = entityManager.createQuery("SELECT C FROM RequestSystemFilePo C where C.attachCode = :code ", RequestSystemFilePo.class);
		q.setParameter("code", code);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		if (result.hasNext()) {
			return (RequestSystemFilePo)result.next();
		}
		
		return null;
	}
	
}

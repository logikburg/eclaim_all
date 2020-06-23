package hk.org.ha.eclaim.bs.request.dao.impl;

import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.request.po.RequestTempFilePo;
import hk.org.ha.eclaim.bs.request.dao.IRequestTempFileDao;

@Repository
public class RequestTempFileDaoImpl implements IRequestTempFileDao {

	@PersistenceContext
	private EntityManager entityManager;

	public int insert(RequestTempFilePo file) {
		entityManager.persist(file);

		return file.getTmpFileUid();
	}

	public RequestTempFilePo getTempFile(int uid) {
		Query q = entityManager.createQuery("SELECT C FROM RequestTempFilePo C where C.tmpFileUid = :tmpFileUid ", RequestTempFilePo.class);
		q.setParameter("tmpFileUid", uid);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		if (result.hasNext()) {
			return (RequestTempFilePo)result.next();
		}
		
		return null;
	}
}

package hk.org.ha.eclaim.bs.cs.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.cs.dao.IExternalSupportDao;
import hk.org.ha.eclaim.bs.cs.po.ExternalSupportPo;

@Repository
public class ExternalSupportDaoImpl implements IExternalSupportDao{

	@PersistenceContext
	private EntityManager entityManager;

	public List<ExternalSupportPo> getAllExternalSupport() {
		List<ExternalSupportPo> resultList = new ArrayList<ExternalSupportPo>();

		Query q = entityManager.createQuery("SELECT D FROM ExternalSupportPo D", ExternalSupportPo.class);
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		while (result.hasNext()) {
			ExternalSupportPo c = (ExternalSupportPo)result.next();

			resultList.add(c);
		}

		return resultList;
	}

}

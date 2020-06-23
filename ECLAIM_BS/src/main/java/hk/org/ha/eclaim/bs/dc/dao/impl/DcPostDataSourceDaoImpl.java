package hk.org.ha.eclaim.bs.dc.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.dc.dao.IDcPostDataSourceDao;
import hk.org.ha.eclaim.bs.dc.po.DcPostDataSourcePo;

@Repository
public class DcPostDataSourceDaoImpl implements IDcPostDataSourceDao {

	@PersistenceContext
	private EntityManager entityManager;

	public Integer insert(DcPostDataSourcePo dcPostDataSource) {
		System.out.println("Perform DcPostDataSourceDaoImpl.insert");

		entityManager.persist(dcPostDataSource);
		return dcPostDataSource.getDcPostDataSourceUID();
	}

}

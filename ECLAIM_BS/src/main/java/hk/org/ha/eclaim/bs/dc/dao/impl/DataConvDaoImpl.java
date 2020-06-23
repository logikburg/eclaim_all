package hk.org.ha.eclaim.bs.dc.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.dc.dao.IDataConvDao;

@Repository
public class DataConvDaoImpl implements IDataConvDao {

	@PersistenceContext
	private EntityManager entityManager;

	private final static String DC_INIT_SP_NAME = "DC_PKG.INIT_DATA_SOURCE";
	private final static String DC_CONV_SP_NAME = "DC_PKG.PROCEED_DATA_CONV";

	public void initDataSource() {
		StoredProcedureQuery dcInitSp = entityManager.createStoredProcedureQuery(DC_INIT_SP_NAME);
		dcInitSp.execute();
	}
	
	public void proceedDataConv() {
		StoredProcedureQuery dcConvSp = entityManager.createStoredProcedureQuery(DC_CONV_SP_NAME);
		dcConvSp.execute();
	}

}

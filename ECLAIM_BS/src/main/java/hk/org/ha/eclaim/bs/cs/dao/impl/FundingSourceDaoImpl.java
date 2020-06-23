package hk.org.ha.eclaim.bs.cs.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.cs.dao.IFundingSourceDao;
import hk.org.ha.eclaim.bs.cs.po.FundingSourcePo;

@Repository
public class FundingSourceDaoImpl implements IFundingSourceDao{

	@PersistenceContext
	private EntityManager entityManager;
	
	public List<FundingSourcePo> getAllFundingSource() {
		List<FundingSourcePo> resultList = new ArrayList<FundingSourcePo>();
		
		Query q = entityManager.createQuery("SELECT D FROM FundingSourcePo D", FundingSourcePo.class);
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			FundingSourcePo c = (FundingSourcePo)result.next();
			
			resultList.add(c);
		}
		
		return resultList;
	}

}

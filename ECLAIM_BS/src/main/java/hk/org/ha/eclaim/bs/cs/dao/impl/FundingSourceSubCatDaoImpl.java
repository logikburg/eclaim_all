package hk.org.ha.eclaim.bs.cs.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.cs.dao.IFundingSourceSubCatDao;
import hk.org.ha.eclaim.bs.cs.po.FundingSourceSubCatPo;

@Repository
public class FundingSourceSubCatDaoImpl implements IFundingSourceSubCatDao{

	@PersistenceContext
	private EntityManager entityManager;
	
	public List<FundingSourceSubCatPo> getAllFundingSourceSubCat() {
		List<FundingSourceSubCatPo> resultList = new ArrayList<FundingSourceSubCatPo>();
		
		Query q = entityManager.createQuery("SELECT D FROM FundingSourceSubCatPo D", FundingSourceSubCatPo.class);
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			FundingSourceSubCatPo c = (FundingSourceSubCatPo)result.next();
			
			resultList.add(c);
		}
		
		return resultList;
	}

}

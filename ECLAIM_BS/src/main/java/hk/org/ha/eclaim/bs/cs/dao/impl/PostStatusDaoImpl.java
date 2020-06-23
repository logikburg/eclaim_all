package hk.org.ha.eclaim.bs.cs.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.cs.dao.IPostStatusDao;
import hk.org.ha.eclaim.bs.cs.po.MPRSPostStatusPo;

@Repository
public class PostStatusDaoImpl implements IPostStatusDao{

	@PersistenceContext
	private EntityManager entityManager;

	public List<MPRSPostStatusPo> getAllPostStatus() {
		List<MPRSPostStatusPo> resultList = new ArrayList<MPRSPostStatusPo>();

		Query q = entityManager.createQuery("SELECT C FROM MPRSPostStatusPo C", MPRSPostStatusPo.class);
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		while (result.hasNext()) {
			MPRSPostStatusPo c = (MPRSPostStatusPo)result.next();

			resultList.add(c);
		}

		return resultList;
	}
}

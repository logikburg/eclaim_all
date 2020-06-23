package hk.org.ha.eclaim.bs.cs.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.cs.dao.IPostDurationDao;
import hk.org.ha.eclaim.bs.cs.po.PostDurationPo;

@Repository
public class PostDurationDaoImpl implements IPostDurationDao{

	@PersistenceContext
	private EntityManager entityManager;

	public List<PostDurationPo> getAllPostDuration() {
		List<PostDurationPo> resultList = new ArrayList<PostDurationPo>();

		Query q = entityManager.createQuery("SELECT C FROM PostDurationPo C", PostDurationPo.class);
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		while (result.hasNext()) {
			PostDurationPo c = (PostDurationPo)result.next();

			resultList.add(c);
		}

		return resultList;
	}
	
	public PostDurationPo getDuartionDescByDurationCode(String durationCode) {
		PostDurationPo c = new PostDurationPo();

		Query q = entityManager.createQuery("SELECT C FROM PostDurationPo C where postDurationCode = :durationCode", PostDurationPo.class);
		q.setParameter("durationCode", durationCode);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		while (result.hasNext()) {
			c = (PostDurationPo)result.next();
		}

		return c;
	}

}

package hk.org.ha.eclaim.bs.maintenance.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.maintenance.dao.INewsDao;
import hk.org.ha.eclaim.bs.maintenance.po.NewsPo;
import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.bs.security.po.UserPo;


@Repository
public class NewsDaoImpl implements INewsDao {
	@PersistenceContext
	private EntityManager entityManager;

	public List<NewsPo> getAllNews() {
		List<NewsPo> resultList = new ArrayList<NewsPo>();
		
		Query q = entityManager.createQuery("SELECT C FROM NewsPo C where recState = :recState", NewsPo.class);
		q.setParameter("recState", MPRSConstant.MPRS_STATE_ACTIVE);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			NewsPo c = (NewsPo)result.next();
			
			resultList.add(c);
		}
		
		return resultList;
	}

	// To be implement, include the effective date 
	public List<NewsPo> getCurrentAllNews() {
		List<NewsPo> resultList = new ArrayList<NewsPo>();
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		Query q = entityManager.createQuery("SELECT C FROM NewsPo C where recState = :recState and C.effectiveFrom <= :effectFromDate and C.effectiveTo >= :effectiveToDate", NewsPo.class);
		q.setParameter("recState", MPRSConstant.MPRS_STATE_ACTIVE);
		q.setParameter("effectFromDate", new Date(cal.getTimeInMillis()));
		q.setParameter("effectiveToDate", new Date(cal.getTimeInMillis()));
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			NewsPo c = (NewsPo)result.next();
			
			resultList.add(c);
		}
		
		return resultList;
	}

	public NewsPo getNewsByNewsUid(int newsUid) {
		Query q = entityManager.createQuery("SELECT C FROM NewsPo C where newsUid = :newsUid", NewsPo.class);
		q.setParameter("newsUid", newsUid);
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		if (result.hasNext()) {
			return (NewsPo)result.next();
		}
		
		return null;
	}

	public void deleteNews(int newsUid, String updateBy, String roleId) {
		NewsPo news = entityManager.find(NewsPo.class, newsUid);
		
		System.out.println("new: " + news.getNewsDesc());
		
		news.setRecState(MPRSConstant.MPRS_STATE_INACTIVE);
		news.setUpdatedBy(updateBy);
		news.setUpdatedRoleId(roleId);
		news.setUpdatedDate(new Date());
		
		entityManager.merge(news);
	}

	public int insert(NewsPo news, UserPo updateUser) {
		news.setCreatedBy(updateUser.getUserId());
		news.setCreatedRoleId(updateUser.getCurrentRole());
		news.setCreatedDate(new Date());
		news.setUpdatedBy(updateUser.getUserId());
		news.setUpdatedRoleId(updateUser.getCurrentRole());
		news.setUpdatedDate(new Date());

		entityManager.persist(news);
		System.out.println("insert: " + news.getNewsUid()) ;
		
		return news.getNewsUid();
	}

	public void update(NewsPo news, UserPo updateUser) {
		news.setUpdatedBy(updateUser.getUserId());
		news.setUpdatedRoleId(updateUser.getCurrentRole());
		news.setUpdatedDate(new Date());

		entityManager.merge(news);
	}
}

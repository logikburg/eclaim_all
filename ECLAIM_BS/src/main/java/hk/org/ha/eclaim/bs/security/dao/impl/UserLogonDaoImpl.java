package hk.org.ha.eclaim.bs.security.dao.impl;

import java.util.Date;
import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.bs.security.dao.IUserLogonDao;
import hk.org.ha.eclaim.bs.security.po.UserLogonPo;

@Repository
public class UserLogonDaoImpl implements IUserLogonDao {
	
	@PersistenceContext
	private EntityManager entityManager;

	public int insert(UserLogonPo userLogon) {
		userLogon.setCreatedBy("SYSTEM");
		userLogon.setCreatedRoleId("SYSTEM");
		userLogon.setCreatedDate(new Date());
		userLogon.setUpdatedBy("SYSTEM");
		userLogon.setUpdatedRoleId("SYSTEM");
		userLogon.setUpdatedDate(new Date());
		userLogon.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
		entityManager.persist(userLogon);
		
		return userLogon.getUserLogonUid();
	}

	public UserLogonPo getLastLogonInfo(String userId) {
		Query q = entityManager.createQuery("SELECT C FROM UserLogonPo C where C.userId = :userId order by C.createdDate desc", UserLogonPo.class);
		q.setParameter("userId", userId);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		boolean skip = true;
		
		while (result.hasNext()) {
			if (skip) {
				skip = false;
				result.next();
				continue;
			}
			
			return (UserLogonPo)result.next();
		}
		
		return null;
	}
}

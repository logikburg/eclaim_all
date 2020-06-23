package hk.org.ha.eclaim.bs.request.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.request.po.RequestEmailPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.request.dao.IRequestEmailDao;

@Repository
public class RequestEmailDaoImpl implements IRequestEmailDao {
	@PersistenceContext
	private EntityManager entityManager;

	public List<RequestEmailPo> getUnsendEmail(int requestUid) {
		List<RequestEmailPo> resultList = new ArrayList<RequestEmailPo>();

		Query q = entityManager.createQuery("SELECT C FROM RequestEmailPo C where C.projectId = :requestUid and C.sentInd <> 'Y' order by created_date desc", RequestEmailPo.class);
		q.setParameter("requestUid", requestUid);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		while (result.hasNext()) {
			RequestEmailPo c = (RequestEmailPo)result.next();
			resultList.add(c);
		}

		return resultList;
	}

	public void performSend(RequestEmailPo email) {
		email.setSentInd("Y");
		email.setUpdatedBy("SYSTEM");
		email.setUpdatedRoleId("SYSTEM");
		email.setUpdatedDate(new Date());
		
		entityManager.merge(email);
	}
	
	public void insertEmail(RequestEmailPo email, UserPo updateUser) {
		email.setRecState("A");
		email.setCreatedBy(updateUser.getUserId());
		email.setCreatedRoleId(updateUser.getCurrentRole());
		email.setCreatedDate(new Date());
		email.setUpdatedBy(updateUser.getUserId());
		email.setUpdatedRoleId(updateUser.getCurrentRole());
		email.setUpdatedDate(new Date());

		entityManager.persist(email);
	}
}

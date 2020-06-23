package hk.org.ha.eclaim.bs.request.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.request.dao.IRequestWorkflowHistoryDao;
import hk.org.ha.eclaim.bs.request.po.RequestWorkflowHistoryPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

@Repository
public class RequestWorkflowHistoryDaoImpl implements IRequestWorkflowHistoryDao {

	@PersistenceContext
	private EntityManager entityManager;

	public List<RequestWorkflowHistoryPo> getRequestHistory(int requestUid) {
		String sql = "select p.* from RequestWorkflowHistoryPo p"; 
		sql += " where p.projectId = :requestUid and p.recState = 'A' ";
		sql += " order by p.actionDate ";
		
		List<RequestWorkflowHistoryPo> resultList = new ArrayList<RequestWorkflowHistoryPo>();

		Query q = entityManager.createQuery(sql, RequestWorkflowHistoryPo.class);
		q.setParameter("requestUid", requestUid);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
//			Object[] tmp = (Object[])result.next();
			RequestWorkflowHistoryPo c = new RequestWorkflowHistoryPo();
			c = (RequestWorkflowHistoryPo)result.next();
//			c.setActionDate((Date)tmp[1]);
//			c.setActionTaken((String)tmp[2]);
//			c.setActionBy((String)tmp[3]);
//			c.setActionRoleId((String)tmp[4]);
			resultList.add(c);
		}
		
		return resultList;
	}
	
	public void insert(RequestWorkflowHistoryPo history, UserPo updateUser) {
		history.setRecState("A");
		history.setCreatedBy(updateUser.getUserId());
		history.setCreatedRoleId(updateUser.getCurrentRole());
		history.setCreatedDate(new Date());
		history.setUpdatedBy(updateUser.getUserId());
		history.setUpdatedRoleId(updateUser.getCurrentRole());
		history.setUpdatedDate(new Date());

		entityManager.persist(history);
	}
}

package hk.org.ha.eclaim.bs.request.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.request.po.RequestAttachmentPo;
import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.bs.request.dao.IRequestAttachmentDao;
import hk.org.ha.eclaim.bs.security.po.UserPo;

@Repository
public class RequestAttachmentDaoImpl implements IRequestAttachmentDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	public int insertAttachment(RequestAttachmentPo attachment, UserPo user) {
		attachment.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
		attachment.setCreatedBy(user.getUserId());
		attachment.setCreatedRoleId(user.getCurrentRole());
		attachment.setCreatedDate(new Date());
		attachment.setUpdatedBy(user.getUserId());
		attachment.setUpdatedRoleId(user.getCurrentRole());
		attachment.setUpdatedDate(new Date());
		
		entityManager.persist(attachment);
		
		return attachment.getAttachmentUid();
	}

	public List<RequestAttachmentPo> getAttachmentByRequestUid(int requestUid) {
		List<RequestAttachmentPo> resultList = new ArrayList<RequestAttachmentPo>();
		Query q = entityManager.createQuery("SELECT C FROM RequestAttachmentPo C where C.requestUid = :requestUid  and C.recState = :recState ", RequestAttachmentPo.class);
		q.setParameter("requestUid", requestUid);
		q.setParameter("recState", MPRSConstant.MPRS_STATE_ACTIVE);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			RequestAttachmentPo c = (RequestAttachmentPo)result.next();
			resultList.add(c);
		}
		
		return resultList;
	}
	
	public List<RequestAttachmentPo> getAttachmentByPostUid(int postUid) {
		List<RequestAttachmentPo> resultList = new ArrayList<RequestAttachmentPo>();
		Query q = entityManager.createQuery("SELECT C FROM RequestAttachmentPo C where C.postUid = :postUid and C.recState = :recState", RequestAttachmentPo.class);
		q.setParameter("postUid", postUid);
		q.setParameter("recState", MPRSConstant.MPRS_STATE_ACTIVE);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			RequestAttachmentPo c = (RequestAttachmentPo)result.next();
			resultList.add(c);
		}
		
		return resultList;
	}

	public RequestAttachmentPo getAttachmentByAttachmentId(int attachmentUid) {
		Query q = entityManager.createQuery("SELECT C FROM RequestAttachmentPo C where C.attachmentUid = :attachmentUid ", RequestAttachmentPo.class);
		q.setParameter("attachmentUid", attachmentUid);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		if (result.hasNext()) {
			return (RequestAttachmentPo)result.next();
		}
		
		return null;
	}

	public void removeAttachment(RequestAttachmentPo attachment, UserPo user) {
		attachment.setRecState(MPRSConstant.MPRS_STATE_INACTIVE);
		attachment.setUpdatedBy(user.getUserId());
		attachment.setUpdatedRoleId(user.getCurrentRole());
		attachment.setUpdatedDate(new Date());
		
		entityManager.merge(attachment);
	}
}

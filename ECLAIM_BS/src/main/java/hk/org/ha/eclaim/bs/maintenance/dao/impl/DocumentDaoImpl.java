package hk.org.ha.eclaim.bs.maintenance.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.maintenance.dao.IDocumentDao;
import hk.org.ha.eclaim.bs.maintenance.po.DocumentPo;
import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.bs.security.po.UserPo;

@Repository
public class DocumentDaoImpl implements IDocumentDao {
	@PersistenceContext
	private EntityManager entityManager;

	public List<DocumentPo> getAllActiveDocuments() {
		List<DocumentPo> resultList = new ArrayList<DocumentPo>();
		
		Query q = entityManager.createQuery("SELECT C FROM DocumentPo C where recState = :recState order by documentName", DocumentPo.class);
		q.setParameter("recState", MPRSConstant.MPRS_STATE_ACTIVE);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			DocumentPo c = (DocumentPo)result.next();
			
			resultList.add(c);
		}
		
		return resultList;
	}

	public DocumentPo getDocumentByDocumentUid(int documentUid) {
		Query q = entityManager.createQuery("SELECT C FROM DocumentPo C where documentUid = :documentUid", DocumentPo.class);
		q.setParameter("documentUid", documentUid);
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		if (result.hasNext()) {
			return (DocumentPo)result.next();
		}
		
		return null;
	}

	public void deleteDocument(int documentUid, UserPo updateUser) {
		DocumentPo news = entityManager.find(DocumentPo.class, documentUid);
		
		news.setRecState(MPRSConstant.MPRS_STATE_INACTIVE);
		news.setUpdatedBy(updateUser.getUserId());
		news.setUpdatedRoleId(updateUser.getCurrentRole());
		news.setUpdatedDate(new Date());
		
		entityManager.merge(news);
	}

	public int insert(DocumentPo doc, UserPo updateUser) {
		doc.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
		doc.setCreatedBy(updateUser.getUserId());
		doc.setCreatedRoleId(updateUser.getCurrentRole());
		doc.setUpdatedBy(updateUser.getUserId());
		doc.setUpdatedRoleId(updateUser.getCurrentRole());
		doc.setCreatedDate(new Date());
		doc.setUpdatedDate(new Date());

		entityManager.persist(doc);
		System.out.println("insert: " + doc.getDocumentUid()) ;
		
		return doc.getDocumentUid();
	}

	public void update(DocumentPo news, UserPo updateUser) {
		news.setUpdatedBy(updateUser.getUserId());
		news.setUpdatedRoleId(updateUser.getCurrentRole());
		news.setUpdatedDate(new Date());

		entityManager.merge(news);
	}
}

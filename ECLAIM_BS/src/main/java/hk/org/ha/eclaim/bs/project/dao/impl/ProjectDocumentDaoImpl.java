package hk.org.ha.eclaim.bs.project.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.project.dao.IProjectDocumentDao;
import hk.org.ha.eclaim.bs.project.po.ProjectDocumentPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

@Repository
public class ProjectDocumentDaoImpl implements IProjectDocumentDao{

	@PersistenceContext
	private EntityManager entityManager;

	public ProjectDocumentPo getProjectDocumentPo(int uid){
		return entityManager.find(ProjectDocumentPo.class, uid);
	}
	
	public List<ProjectDocumentPo> getProjectDocumentListByProject(int projectId) {
		Query q = entityManager.createQuery("SELECT C FROM ProjectDocumentPo C where projectId = :projectId and projectVerId = function('xxeal_project_util_pkg.get_last_ver_id',projectId)", ProjectDocumentPo.class);
		q.setParameter("projectId", projectId);
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		List<ProjectDocumentPo> docList = new ArrayList<ProjectDocumentPo>();
		
		while (result.hasNext()) {
			docList.add((ProjectDocumentPo)result.next());
		}
		
		return docList;
	}
	
	public List<ProjectDocumentPo> getProjectDocumentListByProject(int projectId, int verId) {
		Query q = entityManager.createQuery("SELECT C FROM ProjectDocumentPo C where projectId = :projectId and projectVerId = :verId", ProjectDocumentPo.class);
		q.setParameter("projectId", projectId);
		q.setParameter("verId", verId);
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		List<ProjectDocumentPo> docList = new ArrayList<ProjectDocumentPo>();
		
		while (result.hasNext()) {
			docList.add((ProjectDocumentPo)result.next());
		}
		
		return docList;
	}

	public void deleteProject(int docId) {
		ProjectDocumentPo po = entityManager.find(ProjectDocumentPo.class, docId);
		entityManager.remove(po);
	}

	public int insert(ProjectDocumentPo docPo, UserPo updateUser) {
		docPo.setCreatedBy(updateUser.getUserId());
		docPo.setCreatedRoleId(updateUser.getCurrentRole());
		docPo.setCreatedDate(new Date());
		docPo.setUpdatedBy(updateUser.getUserId());
		docPo.setUpdatedRoleId(updateUser.getCurrentRole());
		docPo.setUpdatedDate(new Date());

		entityManager.persist(docPo);
		return docPo.getDocumentId();
	}

	public void update(ProjectDocumentPo docPo, UserPo updateUser) {
		docPo.setUpdatedBy(updateUser.getUserId());
		docPo.setUpdatedRoleId(updateUser.getCurrentRole());
		docPo.setUpdatedDate(new Date());

		entityManager.merge(docPo);
		
	}

	
}

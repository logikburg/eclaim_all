package hk.org.ha.eclaim.bs.project.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.project.dao.IProjectCircumDao;
import hk.org.ha.eclaim.bs.project.po.ProjectCircumPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.core.po.MPRSConstant;

@Repository
public class ProjectCircumDaoImpl implements IProjectCircumDao{

	@PersistenceContext
	private EntityManager entityManager;
	
	public ProjectCircumPo getProjectCircumPoByUid(int circumUid) {
		Query q = entityManager.createQuery("SELECT C FROM ProjectCircumPo C where projectCircumUid = :circumUid", ProjectCircumPo.class);
		q.setParameter("circumUid", circumUid);
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			return (ProjectCircumPo)result.next();
		}
		
		return null;
	}
	
	public List<ProjectCircumPo> getProjectCircumListByProjectId(int projectId) {
		Query q = entityManager.createQuery("SELECT C FROM ProjectCircumPo C where projectId = :projectId and projectVerId = function('xxeal_project_util_pkg.get_last_ver_id',projectId)", ProjectCircumPo.class);
		q.setParameter("projectId", projectId);
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		List<ProjectCircumPo> circumList = new ArrayList<ProjectCircumPo>();
		
		while (result.hasNext()) {
			circumList.add((ProjectCircumPo)result.next());
		}
		
		return circumList;
	}
	
	public List<ProjectCircumPo> getProjectCircumListByProjectId(int projectId, int verId) {
		Query q = entityManager.createQuery("SELECT C FROM ProjectCircumPo C where projectId = :projectId and projectVerId = :verId", ProjectCircumPo.class);
		q.setParameter("projectId", projectId);
		q.setParameter("verId", verId);
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		List<ProjectCircumPo> circumList = new ArrayList<ProjectCircumPo>();
		
		while (result.hasNext()) {
			circumList.add((ProjectCircumPo)result.next());
		}
		
		return circumList;
	}

	public void deleteProjectCircumPo(ProjectCircumPo circumPo, String updateBy, String roleId) {
		ProjectCircumPo po = entityManager.merge(circumPo);
		entityManager.remove(po);
	}

	public int insert(ProjectCircumPo circumPo, UserPo updateUser) {
		circumPo.setCreatedBy(updateUser.getUserId());
		circumPo.setCreatedRoleId(updateUser.getCurrentRole());
		circumPo.setCreatedDate(new Date());
		circumPo.setUpdatedBy(updateUser.getUserId());
		circumPo.setUpdatedRoleId(updateUser.getCurrentRole());
		circumPo.setUpdatedDate(new Date());

		entityManager.persist(circumPo);
		return circumPo.getCircumstanceId();
	}

	public void update(ProjectCircumPo circumPo, UserPo updateUser) {
		circumPo.setUpdatedBy(updateUser.getUserId());
		circumPo.setUpdatedRoleId(updateUser.getCurrentRole());
		circumPo.setUpdatedDate(new Date());

		entityManager.merge(circumPo);
	}

	
}

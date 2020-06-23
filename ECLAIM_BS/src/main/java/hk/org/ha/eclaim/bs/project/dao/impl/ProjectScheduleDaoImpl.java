package hk.org.ha.eclaim.bs.project.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.project.dao.IProjectScheduleDao;
import hk.org.ha.eclaim.bs.project.po.ProjectSchedulePo;
import hk.org.ha.eclaim.bs.project.po.SessionPatternPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

@Repository
public class ProjectScheduleDaoImpl implements IProjectScheduleDao{

	@PersistenceContext
	private EntityManager entityManager;
	
	public ProjectSchedulePo getProjectSchedulePoByScheduleId(int scheduleId) {
		Query q = entityManager.createQuery("Select S From ProjectSchedulePo S where projectScheduleId = :scheduleId", ProjectSchedulePo.class);
		q.setParameter("scheduleId", scheduleId);
		
		@SuppressWarnings("rawtypes")
		Object result = q.getSingleResult();
		while(result != null) {
			return (ProjectSchedulePo)result;
		}
		return null;
	}

	public List<ProjectSchedulePo> getProjectScheduleListByProjectId(int projectId) {
		Query q = entityManager.createQuery("SELECT S FROM ProjectSchedulePo S where projectId = :projectId and projectVerId = function('xxeal_project_util_pkg.get_last_ver_id',projectId)", ProjectSchedulePo.class);
		q.setParameter("projectId", projectId);
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		List<ProjectSchedulePo> poList = new ArrayList<ProjectSchedulePo>();
		while(result.hasNext()) {
			poList.add((ProjectSchedulePo)result.next());
		}
		return poList;
	}
	
	public List<ProjectSchedulePo> getProjectScheduleListByProjectId(int projectId, int verId) {
		Query q = entityManager.createQuery("SELECT S FROM ProjectSchedulePo S where projectId = :projectId and projectVerId = :verId", ProjectSchedulePo.class);
		q.setParameter("projectId", projectId);
		q.setParameter("verId", verId);
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		List<ProjectSchedulePo> poList = new ArrayList<ProjectSchedulePo>();
		while(result.hasNext()) {
			poList.add((ProjectSchedulePo)result.next());
		}
		return poList;
	}
	

	public void deleteProjectSchedule(int scheduleId) {
		ProjectSchedulePo po = entityManager.find(ProjectSchedulePo.class, scheduleId);
		entityManager.remove(po);
	}

	public int insert(ProjectSchedulePo schedule, UserPo updateUser) {
		schedule.setCreatedBy(updateUser.getUserId());
		schedule.setCreatedRoleId(updateUser.getCurrentRole());
		schedule.setCreatedDate(new Date());
		schedule.setUpdatedBy(updateUser.getUserId());
		schedule.setUpdatedRoleId(updateUser.getCurrentRole());
		schedule.setUpdatedDate(new Date());

		entityManager.persist(schedule);
		return schedule.getProjectScheduleId();
	}

	public void update(ProjectSchedulePo schedule, UserPo updateUser) {
		schedule.setUpdatedBy(updateUser.getUserId());
		schedule.setUpdatedRoleId(updateUser.getCurrentRole());
		schedule.setUpdatedDate(new Date());

		entityManager.merge(schedule);
	}
	
	public List<SessionPatternPo> getPatternList() {
		List<SessionPatternPo> patternList = new ArrayList<SessionPatternPo>();
		Query q = entityManager.createQuery("SELECT S FROM SessionPatternPo S", SessionPatternPo.class);
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		while(result.hasNext()) {
			patternList.add((SessionPatternPo)result.next());
		}
		return patternList;
	}
	
	public String getPatternDesc(String patternCode) {
		List<SessionPatternPo> patternList = new ArrayList<SessionPatternPo>();
		Query q = entityManager.createQuery("SELECT s FROM SessionPatternPo s WHERE s.patternCode = :patternCode", SessionPatternPo.class);
		q.setParameter("patternCode", patternCode);
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		if(result.hasNext()) {
			return ((SessionPatternPo)result.next()).getPatternDesc();
		}
		return "";
	}
	
}

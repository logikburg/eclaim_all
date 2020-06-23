package hk.org.ha.eclaim.bs.project.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import hk.org.ha.eclaim.bs.cs.po.ProgramTypePo;
import hk.org.ha.eclaim.bs.project.dao.IProjectDao;
import hk.org.ha.eclaim.bs.project.po.ProjectPo;
import hk.org.ha.eclaim.bs.project.po.ProjectPublishJobPo;
import hk.org.ha.eclaim.bs.project.po.ProjectPublishPo;
import hk.org.ha.eclaim.bs.project.po.ProjectPublishRankPo;
import hk.org.ha.eclaim.bs.project.po.ProjectRequestPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.core.logger.EClaimLogger;

@Repository
public class ProjectDaoImpl implements IProjectDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private EntityManagerFactory emf;

	public List<ProjectRequestPo> getAllProject() {
		List<ProjectRequestPo> projectList = new ArrayList<ProjectRequestPo>();
		Query q = entityManager.createQuery("SELECT C FROM ProjectRequestPo C", ProjectRequestPo.class);
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		while (result.hasNext()) {
			projectList.add((ProjectRequestPo) result.next());
		}
		return projectList;
	}

	public ProjectPo getProjectByProjectId(int projectUid) {
		Query q = entityManager.createQuery(
				"SELECT C FROM ProjectPo C where projectId = :projectId and projectVerId = function('xxeal_project_util_pkg.get_last_ver_id',projectId)",
				ProjectPo.class);
		q.setParameter("projectId", projectUid);
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		if (result.hasNext()) {
			return (ProjectPo) result.next();
		}

		return null;
	}

	public ProjectPo getProjectByProjectId(int projectUid, int verId) {
		Query q = entityManager.createQuery(
				"SELECT C FROM ProjectPo C where projectId = :projectId and projectVerId = :verId", ProjectPo.class);
		q.setParameter("projectId", projectUid);
		q.setParameter("verId", verId);
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		if (result.hasNext()) {
			return (ProjectPo) result.next();
		}

		return null;
	}

	public void deleteProject(int projectId, String updateBy, String roleId) {
		// TODO Auto-generated method stub

	}

	public int insert(ProjectPo project, UserPo updateUser) {
		project.setCreatedBy(updateUser.getUserId());
		project.setCreatedRoleId(updateUser.getCurrentRole());
		project.setCreatedDate(new Date());
		project.setUpdatedBy(updateUser.getUserId());
		project.setUpdatedRoleId(updateUser.getCurrentRole());
		project.setUpdatedDate(new Date());
		entityManager.persist(project);
		return project.getProjectId();
	}

	public void update(ProjectPo project, UserPo updateUser) {
		project.setUpdatedBy(updateUser.getUserId());
		project.setUpdatedRoleId(updateUser.getCurrentRole());
		project.setUpdatedDate(new Date());

		entityManager.merge(project);
	}

	public List<ProjectRequestPo> getCurrentAllProject(UserPo user) {
		List<ProjectRequestPo> projectList = new ArrayList<ProjectRequestPo>();
		Boolean isHR = false;

		Query q = entityManager.createQuery(
				"SELECT C FROM ProjectRequestPo C where projectOwnerId like :userId or projectPreparerId like :userId or finHospIcId like :userId order by updatedDate desc",
				ProjectRequestPo.class);
		for (int x = 0; x < user.getUserRole().size(); x++) {
			if ("HR_OFFICER".equals(user.getUserRole().get(x).getRole().getRoleId())
					|| "FIN_ADMIN".equals(user.getUserRole().get(x).getRole().getRoleId())
					|| "HR_MANAGER".equals(user.getUserRole().get(x).getRole().getRoleId())) {
				isHR = true;
			}
		}
		q.setParameter("userId", isHR ? "%" : user.getUserId());
		projectList = null; // q.getResultList();

		return projectList != null ? projectList : new ArrayList<ProjectRequestPo>();
	}

	public List<ProjectRequestPo> searchProjectByRequest(Integer projectId, String projectName, String status,
			String ownerId, String preparerId, String userId) {
		String sql = "select u from ProjectRequestPo u ";
		sql += " where 1=1 ";

		if (!"".equals(projectName)) {
			sql += " and upper(u.projectName) like upper(:projectName) ";

		}
		if (projectId != null && projectId != 0) {
			sql += " and u.projectId = :projectId ";
		}
		if (!"".equals(ownerId)) {
			sql += " and u.projectOwnerId = :ownerId ";
		}
		if (!"".equals(preparerId)) {
			sql += " and u.projectPreparerId = :preparerId ";
		}
		if (!"".equals(status)) {
			sql += " and u.projectStatusCode = :status ";
		}
//		if("".equals(roleId)) {
//			sql += " and u.projectOwnerId = :userId or u.projectPreparerId = :userId";
//		}

		sql += " order by u.projectId ";

		List<ProjectRequestPo> resultList = new ArrayList<ProjectRequestPo>();
		Query q = entityManager.createQuery(sql, ProjectRequestPo.class);
//		
		if (!"".equals(projectName)) {
			q.setParameter("projectName", projectName + "%");
		}

		if (projectId != null && projectId != 0) {
			q.setParameter("projectId", projectId);
		}
		if (!"".equals(ownerId)) {
			q.setParameter("ownerId", ownerId);
		}
		if (!"".equals(preparerId)) {
			q.setParameter("preparerId", preparerId);
		}
		if (!"".equals(status)) {
			q.setParameter("status", status);
		}

		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		while (result.hasNext()) {
			resultList.add((ProjectRequestPo) result.next());
		}
		return resultList;
	}

	public boolean isExistingProjectName(String projectId, String projectName) {
		String sql = "SELECT C FROM ProjectPo C where projectName = :projectName";
		if (projectId != null && !"".equals(projectId)) {
			sql = sql.concat(" and projectId != :projectId");
		}
		Query q = entityManager.createQuery(sql, ProjectPo.class);
		q.setParameter("projectName", projectName);
		if (projectId != null && !"".equals(projectId)) {
			q.setParameter("projectId", Integer.parseInt(projectId));
		}
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		if (result.hasNext()) {
			return true;
		}

		return false;
	}

	public List<ProgramTypePo> getAllProgramType() {
		List<ProgramTypePo> typeList = new ArrayList<ProgramTypePo>();
		Query q = entityManager.createQuery("SELECT C FROM ProgramTypePo C", ProgramTypePo.class);
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		while (result.hasNext()) {
			typeList.add((ProgramTypePo) result.next());
		}
		return typeList;
	}

	public String getProgramTypeDesc(String typeCode) {
		Query q = entityManager.createQuery("SELECT C FROM ProgramTypePo C where C.typeCode = :typeCode",
				ProgramTypePo.class);
		q.setParameter("typeCode", typeCode);
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

//		while (result.hasNext()) {
//			return ((ProgramTypePo)result.next()).getTypeDesc();
//		}
		return null;
	}

	public List<ProjectPo> searchProjectByProjectName(String projectName, String deptId, String[] status) {
		String sql = "select u from ProjectPo u ";
//		sql += "left join SS_USER_ROLE ur on u.USER_ID = ur.USER_ID and ur.REC_STATE = 'A' ";
//		sql += "left join SS_USER_ROLE_DATA_ACCESS da on ur.USER_ROLE_UID = da.USER_ROLE_UID  and da.REC_STATE = 'A' ";
		sql += " where 1=1 ";
		sql += " and upper(u.projectName) like upper(:projectName) ";
		if (!StringUtils.isEmpty(deptId)) {
			sql += " and u.departmentId = :deptId ";
		}
		if (status != null && status.length != 0) {
			sql += " and u.projectStatusCode in (:status)";
		}
		sql += " and rownum < 11 ";
		sql += " order by u.projectName ";

		List<ProjectPo> resultList = new ArrayList<ProjectPo>();
		Query q = entityManager.createQuery(sql, ProjectPo.class);
//		
		if (!"".equals(projectName)) {
			q.setParameter("projectName", projectName + "%");
		} else {
			q.setParameter("projectName", "");
		}
		if (!StringUtils.isEmpty(deptId)) {
			q.setParameter("deptId", Integer.parseInt(deptId));
		}
		if (status != null && status.length != 0) {
			q.setParameter("status", String.join(",", status));
		}
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		while (result.hasNext()) {
			resultList.add((ProjectPo) result.next());
		}

		return resultList;
	}

	public int createProjectVer(int projectId, int projectVer, String action, UserPo user) {
		try {
			StoredProcedureQuery storedProcedure = null;

			if ("EXTEND".equals(action)) {
				storedProcedure = entityManager.createStoredProcedureQuery("XXEAL_PROJECT_UTIL_PKG.EXTEND_PROJECT");
			} else if ("UPDATE".equals(action)) {
				storedProcedure = entityManager.createStoredProcedureQuery("XXEAL_PROJECT_UTIL_PKG.COPY_PROJECT");
			} else if ("COPY".equals(action)) {
				storedProcedure = entityManager.createStoredProcedureQuery("XXEAL_PROJECT_UTIL_PKG.COPY_PROJECT");
			} else {
				throw new Exception("Incorrect Action Type");
			}

			storedProcedure.registerStoredProcedureParameter("p_project_id", Integer.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("p_old_ver_id", Integer.class, ParameterMode.IN);
			storedProcedure.setParameter("p_project_id", projectId);
			storedProcedure.setParameter("p_old_ver_id", projectVer);
			storedProcedure.execute();

			return 0;
		} catch (Exception e) {
			EClaimLogger.error("createHCMPosition:" + e.getMessage(), e);
			return 1;
		}
	}

	public List<?> getProjectPublishInvitation(int projectId) {
		Query q = entityManager.createQuery("SELECT C FROM ProjectPublishPo C where C.projectId = :projectId",
				ProjectPublishPo.class);
		q.setParameter("projectId", projectId);
		return q.getResultList();
	}

	public List<?> getProjectInvitationJobGroup(int projectId) {
		Query q = entityManager.createNativeQuery(
				"SELECT JOB_GROUP_ID, LISTAGG(RANK, ', ') WITHIN GROUP (ORDER BY jobgroup.JOB_GROUP_ID) RANK FROM "
						+ "(select jd.*, pj.JOB_DESCRIPTION from XXEAL_PROJECT_JOB pj join XXEAL_PROJECT p on p.project_id = pj.project_id "
						+ "join XXEAL_PRJ_JOB_DETAIL jd on jd.JOB_GROUP_ID = pj.JOB_GROUP_ID "
						+ "where pj.project_id = :projectId) jobgroup GROUP BY jobgroup.JOB_GROUP_ID, JOB_GROUP_ID"); // JOB_GROUP_ID

		q.setParameter("projectId", projectId);

		return q.getResultList();
	}

	public void insertProjectInvitation(ProjectPublishPo pp, List<ProjectPublishJobPo> arrPPJ, List<ProjectPublishRankPo> arrPPR)
			throws RuntimeException {
		
		entityManager = emf.createEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		
		try {
			entityTransaction.begin();

			// preserve chronology order to store PublishProject should be first.
			entityManager.persist(pp);

			// below could be arbitrary
			for(ProjectPublishJobPo ppj : arrPPJ) {
				entityManager.persist(ppj);
			}

			for(ProjectPublishRankPo ppr : arrPPR) {
				entityManager.persist(ppr);
			}

			entityTransaction.commit();
		} catch (RuntimeException e) {
			if (entityTransaction.isActive()) {
				entityTransaction.rollback();
			}
			throw e;
		} 
	}
}

package hk.org.ha.eclaim.bs.project.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.project.dao.IProjectJobDao;
import hk.org.ha.eclaim.bs.project.po.ProjectJobPo;
import hk.org.ha.eclaim.bs.project.po.ProjectJobRankPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

@Repository
public class ProjectJobDaoImpl implements IProjectJobDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	public ProjectJobPo getProjectJobPoByJobGroupId(int jobGroupId) {
		Query q = entityManager.createQuery("Select J From ProjectJobPo J where jobGroupId = :jobGroupId", ProjectJobPo.class);
		q.setParameter("jobGroupId", jobGroupId);
		
		@SuppressWarnings("rawtypes")
		Object result = q.getSingleResult();
		while(result != null) {
			return (ProjectJobPo)result;
		}
		return null;
	}
	
	public List<ProjectJobPo> getProjectJobListByProjectId(int projectId) {
		Query q = entityManager.createQuery("SELECT J FROM ProjectJobPo J where projectId = :projectId and projectVerId = function('xxeal_project_util_pkg.get_last_ver_id',projectId)", ProjectJobPo.class);
		q.setParameter("projectId", projectId);
		@SuppressWarnings("rawtypes")
		//Iterator result = q.getResultList().iterator();
		Iterator result = Collections.emptyListIterator();
		List<ProjectJobPo> jobList = new ArrayList<ProjectJobPo>();
		
		while (result.hasNext()) {
			jobList.add((ProjectJobPo)result.next());
		}
		
		return jobList;
	}
	
	public List<ProjectJobPo> getProjectJobListByProjectId(int projectId, int verId) {
		Query q = entityManager.createQuery("SELECT J FROM ProjectJobPo J where projectId = :projectId and projectVerId = :verId", ProjectJobPo.class);
		q.setParameter("projectId", projectId);
		q.setParameter("verId", verId);
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		List<ProjectJobPo> jobList = new ArrayList<ProjectJobPo>();
		
		while (result.hasNext()) {
			jobList.add((ProjectJobPo)result.next());
		}
		
		return jobList;
	}
	
	public void deleteProjectJob(ProjectJobPo job) {
		ProjectJobPo po = entityManager.merge(job);
		entityManager.remove(po);
		
	}

	public int insert(ProjectJobPo job, UserPo updateUser) {
		job.setCreatedBy(updateUser.getUserId());
		job.setCreatedRoleId(updateUser.getCurrentRole());
		job.setCreatedDate(new Date());
		job.setUpdatedBy(updateUser.getUserId());
		job.setUpdatedRoleId(updateUser.getCurrentRole());
		job.setUpdatedDate(new Date());

		entityManager.persist(job);
		return job.getJobGroupId();
	}

	public void update(ProjectJobPo job, UserPo updateUser) {
		job.setUpdatedBy(updateUser.getUserId());
		job.setUpdatedRoleId(updateUser.getCurrentRole());
		job.setUpdatedDate(new Date());

		entityManager.merge(job);
	}
	
	public void insertJobRank(ProjectJobPo job, ProjectJobRankPo rank, UserPo updateUser) {
		entityManager.persist(rank);
		this.update(job, updateUser);
	}
	
	public void deleteJobRank(ProjectJobRankPo rank) {
		ProjectJobRankPo po = entityManager.merge(rank);
		entityManager.remove(po);
	}
	
	public List<ProjectJobRankPo> getJobRankPoListByJobGropupId(int jobGroupId) {
		List<ProjectJobRankPo> jobRankList = new ArrayList<ProjectJobRankPo>();
		try {
			Query q = entityManager.createQuery("SELECT J FROM ProjectJobRankPo J where jobGroupId = :jobGroupId", ProjectJobRankPo.class);
			q.setParameter("jobGroupId", jobGroupId);
			@SuppressWarnings("rawtypes")
			Iterator result = q.getResultList().iterator();
			
			while (result.hasNext()) {
				jobRankList.add((ProjectJobRankPo)result.next());
			}
			
			return jobRankList;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return jobRankList;
	}
	
	public Map<String, String> getStaffGroupList() {
		String queryStr = "select staff_grp_code, staff_grp_name from v_staff_group";
		
		Map<String, String> staffGrpList = new LinkedHashMap<String, String>();
		try {
			Query query = entityManager.createNativeQuery(queryStr);
			
			@SuppressWarnings("rawtypes")
			Iterator result = query.getResultList().iterator();
			while(result.hasNext()){
				Object[] obj = (Object[])result.next();
				staffGrpList.put(obj[0].toString(), obj[1].toString());
			}
			return staffGrpList;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return staffGrpList;
	}
	
	public Map<String, String> getJobRankList(String staffGrp) {
		String queryStr = "select job_rank_code,job_rank_name from v_job_rank where staff_group = :staffGrp";
//		String queryStr = "select distinct pgd.segment1 "
//				+ "from pay_user_columns_instances_f val, pay_user_columns col, pay_user_tables tab, pay_user_rows_f urow, per_grades pg, per_grade_definitions pgd "
//				+ "where val.user_column_id = col.user_column_id and col.user_table_id = tab.user_table_id "
//				+ "and tab.user_table_name = :tableName and col_user_column_name = :colName"
//				+ "and urow.user_row_id = val.user_row_id and pg.NAME = urow.row_low_range_or_name "
//				+ "and pg.grade_definition_id = pgd.grade_definition_id "
//				+ "and val.VALUE = :staffGrp"
//				+ "and pgd.segment1 in (select v.flex_value from fnd_flex_values_vl v, fnd_flex_value_sets vs "
//				+ "where v.flex_value_set_id = vs.flex_value_set_id and flex_value_set_name = : setName"
//				+ "and atTribute3 = 'Y') order by 1";
		
		Map<String, String> jobRankList = new LinkedHashMap<String, String>();
		try {
			Query query = entityManager.createNativeQuery(queryStr);
//			query.setParameter("setName", "XXPAY_STAFF_GROUP");
//			query.setParameter("colName", "Staff Group");
			query.setParameter("staffGrp", staffGrp);
//			query.setParameter("setName", "XXPER_GENERIC_RANK");
			
			@SuppressWarnings("rawtypes")
			Iterator result = query.getResultList().iterator();
			while(result.hasNext()){
				Object[] obj = (Object[])result.next();
				jobRankList.put(obj[0].toString(), obj[0].toString());
			}
			return jobRankList;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return jobRankList;
	}
	
	public String getHigherRank(String rank1, String rank2, String rank3, String rank4, String rank5) {
		String higherRank = null;
		String queryStr = "select xxeal_project_util_pkg.get_higher_rank(:p_rank1,:p_rank2,:p_rank3,:p_rank4,:p_rank5) from dual";

		try {
			Query query = entityManager.createNativeQuery(queryStr);
			query.setParameter("p_rank1", rank1);
			query.setParameter("p_rank2", rank2);
			query.setParameter("p_rank3", rank3);
			query.setParameter("p_rank4", rank4);
			query.setParameter("p_rank5", rank5);
			
			Object result = query.getSingleResult();
			if(result != null) {
				higherRank = result.toString();
			}
			return higherRank;
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return higherRank;
	}
	public Integer getMaxGradeVal(String higherRank) {
		Integer maxGrade = 1;
		
		try {
			String queryStr = "select xxeal_project_util_pkg.get_grade_val(:higherRank,:date,:status) from dual";
			Query query = entityManager.createNativeQuery(queryStr);
			query.setParameter("higherRank", higherRank);
			query.setParameter("date", new Date());
			query.setParameter("status", "MAX");
			Object result = query.getSingleResult();
			
			if(result != null) {
				maxGrade = Integer.parseInt(result.toString());
			}
			return maxGrade;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return maxGrade;
	}
}

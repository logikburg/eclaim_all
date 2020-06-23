package hk.org.ha.eclaim.bs.project.svc.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hk.org.ha.eclaim.bs.project.dao.IProjectJobDao;
import hk.org.ha.eclaim.bs.project.po.ProjectJobPo;
import hk.org.ha.eclaim.bs.project.po.ProjectJobRankPo;
import hk.org.ha.eclaim.bs.project.svc.IProjectJobSvc;
import hk.org.ha.eclaim.bs.security.po.UserPo;

@Service("projectJobSvc")
public class ProjectJobSvcImpl implements IProjectJobSvc {
	
	@Autowired
	private IProjectJobDao jobDao;
	
	public List<ProjectJobPo> getProjectJobListByProjectId(int projectId) {
		return jobDao.getProjectJobListByProjectId(projectId);
	}
	
	public List<ProjectJobPo> getProjectJobListByProjectId(int projectId, int verId) {
		return jobDao.getProjectJobListByProjectId(projectId,verId);
	}

	@Transactional
	public void deleteProjectJob(ProjectJobPo job, String updateBy, String roleId) {
		List<ProjectJobRankPo> jobRanks = this.getJobRankPoList(job.getJobGroupId());
		for(ProjectJobRankPo jobRank : jobRanks) {
			jobDao.deleteJobRank(jobRank);
		}
		jobDao.deleteProjectJob(job);
	}

	@Transactional
	public int insert(ProjectJobPo jobPo, UserPo updateUser) {
		return jobDao.insert(jobPo, updateUser);
	}

	@Transactional
	public void update(ProjectJobPo jobPo, UserPo updateUser) {
		jobDao.update(jobPo, updateUser);		
	}

	public ProjectJobPo getProjectJobByjobGroupId(int jobGroupId) {
		return jobDao.getProjectJobPoByJobGroupId(jobGroupId);
	}

	@Transactional
	public void insertJobRank(ProjectJobPo job, ProjectJobRankPo rank, UserPo updateUser) {
		jobDao.insertJobRank(job, rank, updateUser);
		
	}
	
	@Transactional
	public void deleteJobRank(ProjectJobRankPo rank) {
		jobDao.deleteJobRank(rank);
	}
	
	public List<ProjectJobRankPo> getJobRankPoList(int jobGroupId) {
		return jobDao.getJobRankPoListByJobGropupId(jobGroupId);
	}
	
	public Map<String, String> getStaffGroupList() {
		return jobDao.getStaffGroupList();
	}

	public Map<String, String> getJobRankList(String staffGrp) {
		return jobDao.getJobRankList(staffGrp);
	}

	public String getHigherRank(String rank1, String rank2) {
		return getHigherRank(rank1, rank2, null, null, null);
	}

	public String getHigherRank(String rank1, String rank2, String rank3) {
		return getHigherRank(rank1, rank2, rank3, null, null);
	}

	public String getHigherRank(String rank1, String rank2, String rank3, String rank4, String rank5) {
		return jobDao.getHigherRank(rank1, rank2, rank3, rank4, rank5);
	}

	public Integer getMaxGradeVal(String higherRank) {
		return jobDao.getMaxGradeVal(higherRank);
	}
	
	
}

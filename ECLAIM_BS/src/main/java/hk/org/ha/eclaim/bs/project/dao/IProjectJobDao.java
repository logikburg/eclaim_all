package hk.org.ha.eclaim.bs.project.dao;

import java.util.List;
import java.util.Map;

import hk.org.ha.eclaim.bs.project.po.ProjectJobPo;
import hk.org.ha.eclaim.bs.project.po.ProjectJobRankPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

public interface IProjectJobDao {

	public ProjectJobPo getProjectJobPoByJobGroupId(int jobGroupId);
	public List<ProjectJobPo> getProjectJobListByProjectId(int projectId);
	public List<ProjectJobPo> getProjectJobListByProjectId(int projectId, int verId);
	public void deleteProjectJob(ProjectJobPo job);
	public int insert(ProjectJobPo job, UserPo updateUser);
	public void update(ProjectJobPo job, UserPo updateUser);
	public void insertJobRank(ProjectJobPo job, ProjectJobRankPo rank, UserPo updateUser);
	public void deleteJobRank(ProjectJobRankPo rank) ;
	public Map<String, String> getStaffGroupList();
	public Map<String, String> getJobRankList(String staffGrp);
	public String getHigherRank(String rank1, String rank2, String rank3, String rank4, String rank5);
	public Integer getMaxGradeVal(String higherRank);
	public List<ProjectJobRankPo> getJobRankPoListByJobGropupId(int jobGroupId);
}

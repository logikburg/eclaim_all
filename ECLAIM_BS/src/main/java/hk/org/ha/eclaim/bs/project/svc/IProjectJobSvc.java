package hk.org.ha.eclaim.bs.project.svc;

import java.util.List;
import java.util.Map;

import hk.org.ha.eclaim.bs.project.po.ProjectJobPo;
import hk.org.ha.eclaim.bs.project.po.ProjectJobRankPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

public interface IProjectJobSvc {

	public ProjectJobPo getProjectJobByjobGroupId(int jobGroupId);
	public List<ProjectJobPo> getProjectJobListByProjectId(int projectId);
	public List<ProjectJobPo> getProjectJobListByProjectId(int projectId, int verId);
	public void deleteProjectJob(ProjectJobPo job, String updateBy, String roleId);
	public int insert(ProjectJobPo jobPo, UserPo updateUser);
	public void update(ProjectJobPo jobPo, UserPo updateUser);
	
	public void insertJobRank(ProjectJobPo job, ProjectJobRankPo rank, UserPo updateUser);
	public void deleteJobRank(ProjectJobRankPo rank);
	public List<ProjectJobRankPo> getJobRankPoList(int jobGroupId);
	
	public Map<String, String> getStaffGroupList();
	public Map<String, String> getJobRankList(String staffGrp);
	public String getHigherRank(String rank1, String rank2);
	public String getHigherRank(String rank1, String rank2, String rank3);
	public String getHigherRank(String rank1, String rank2, String rank3, String rank4, String rank5);
	public Integer getMaxGradeVal(String higherRank);
}

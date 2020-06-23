package hk.org.ha.eclaim.bs.project.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.project.po.ProjectSchedulePo;
import hk.org.ha.eclaim.bs.project.po.SessionPatternPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

public interface IProjectScheduleDao {

	public ProjectSchedulePo getProjectSchedulePoByScheduleId(int scheduleId);
	public List<ProjectSchedulePo> getProjectScheduleListByProjectId(int projectId);
	public List<ProjectSchedulePo> getProjectScheduleListByProjectId(int projectId, int verId);
	public void deleteProjectSchedule(int scheduleId);
	public int insert(ProjectSchedulePo schedule, UserPo updateUser);
	public void update(ProjectSchedulePo schedule, UserPo updateUser);
	
	public List<SessionPatternPo> getPatternList();
	public String getPatternDesc(String patternCode);
}

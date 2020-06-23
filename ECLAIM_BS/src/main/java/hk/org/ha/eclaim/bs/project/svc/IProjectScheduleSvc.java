package hk.org.ha.eclaim.bs.project.svc;

import java.util.List;

import hk.org.ha.eclaim.bs.project.po.ProjectSchedulePo;
import hk.org.ha.eclaim.bs.project.po.SessionPatternPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

public interface IProjectScheduleSvc {

	public ProjectSchedulePo getProjectScheduleByScheduleId(int scheduleId);
	public List<ProjectSchedulePo> getProjectScheduleListByProjectId(int projectId);
	public List<ProjectSchedulePo> getProjectScheduleListByProjectId(int projectId, int verId);
	public void deleteProjectSchedule(int scheduleId);
	public int insert(ProjectSchedulePo schedulePo, UserPo updateUser);
	public void update(ProjectSchedulePo schedulePo, UserPo updateUser);
	
	public List<SessionPatternPo> getPatternList();
	public String getPatternDesc(String patternCode);
	public String getPatternDesc(String[] patternCode);
}

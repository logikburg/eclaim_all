package hk.org.ha.eclaim.bs.project.svc.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hk.org.ha.eclaim.bs.project.dao.IProjectScheduleDao;
import hk.org.ha.eclaim.bs.project.po.ProjectSchedulePo;
import hk.org.ha.eclaim.bs.project.po.SessionPatternPo;
import hk.org.ha.eclaim.bs.project.svc.IProjectScheduleSvc;
import hk.org.ha.eclaim.bs.security.po.UserPo;

@Service("projectScheduleSvc")
public class ProjectScheduleSvcImpl implements IProjectScheduleSvc{

	@Autowired
	IProjectScheduleDao scheduleDao;
	
	public ProjectSchedulePo getProjectScheduleByScheduleId(int scheduleId) {
		return scheduleDao.getProjectSchedulePoByScheduleId(scheduleId);
	}

	public List<ProjectSchedulePo> getProjectScheduleListByProjectId(int projectId) {
		return scheduleDao.getProjectScheduleListByProjectId(projectId);
	}
	
	public List<ProjectSchedulePo> getProjectScheduleListByProjectId(int projectId, int verId) {
		return scheduleDao.getProjectScheduleListByProjectId(projectId,verId);
	}
	
	@Transactional
	public void deleteProjectSchedule(int scheduleId) {
		scheduleDao.deleteProjectSchedule(scheduleId);
		
	}

	@Transactional
	public int insert(ProjectSchedulePo schedulePo, UserPo updateUser) {
		// TODO Auto-generated method stub
		return scheduleDao.insert(schedulePo, updateUser);
	}

	@Transactional
	public void update(ProjectSchedulePo schedulePo, UserPo updateUser) {
		scheduleDao.update(schedulePo, updateUser);
		
	}
	
	public List<SessionPatternPo> getPatternList(){
		return scheduleDao.getPatternList();
	}
	
	public String getPatternDesc(String patternCode) {
		return scheduleDao.getPatternDesc(patternCode);
	}
	
	public String getPatternDesc(String[] patternCode) {
		List<String> patternDescList = new ArrayList<String>();
		for(String code : patternCode) {
			patternDescList.add(this.getPatternDesc(code));
		}
		return String.join(",", patternDescList);
	}
}

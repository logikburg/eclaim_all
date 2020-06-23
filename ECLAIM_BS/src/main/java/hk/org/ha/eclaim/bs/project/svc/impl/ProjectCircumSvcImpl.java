package hk.org.ha.eclaim.bs.project.svc.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hk.org.ha.eclaim.bs.project.dao.IProjectCircumDao;
import hk.org.ha.eclaim.bs.project.po.ProjectCircumPo;
import hk.org.ha.eclaim.bs.project.svc.IProjectCircumSvc;
import hk.org.ha.eclaim.bs.security.po.UserPo;

@Service("projectCircumSvc")
public class ProjectCircumSvcImpl implements IProjectCircumSvc{

	@Autowired
	private IProjectCircumDao projectCircumDao;
	
	public ProjectCircumPo getProjectCircumPoByUid(int circumUid) {
		return projectCircumDao.getProjectCircumPoByUid(circumUid);
	}
	
	public List<ProjectCircumPo> getProjectCircumListByProjectId(int projectId) {
		return projectCircumDao.getProjectCircumListByProjectId(projectId);
	}
	public List<ProjectCircumPo> getProjectCircumListByProjectId(int projectId, int verId) {
		return projectCircumDao.getProjectCircumListByProjectId(projectId,verId);
	}

	@Transactional
	public void deleteProjectCircum(int circumUid, String updateBy, String roleId) {
		// TODO Auto-generated method stub
		System.out.println("Circum UID : "+circumUid);
		ProjectCircumPo circumPo = this.getProjectCircumPoByUid(circumUid);
		projectCircumDao.deleteProjectCircumPo(circumPo, updateBy, roleId);
		
	}

	@Transactional
	public int insert(ProjectCircumPo circumPo, UserPo updateUser) {
		return projectCircumDao.insert(circumPo, updateUser);
	}

	@Transactional
	public void update(ProjectCircumPo circumPo, UserPo updateUser) {
		projectCircumDao.update(circumPo, updateUser);
	}

}

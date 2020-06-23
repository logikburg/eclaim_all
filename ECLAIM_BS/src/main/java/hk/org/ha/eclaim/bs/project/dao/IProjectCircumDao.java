package hk.org.ha.eclaim.bs.project.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.project.po.ProjectCircumPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

public interface IProjectCircumDao {
	
	public ProjectCircumPo getProjectCircumPoByUid(int circumUid);
	public List<ProjectCircumPo> getProjectCircumListByProjectId(int projectId);
	public List<ProjectCircumPo> getProjectCircumListByProjectId(int projectId, int verId);
	public void deleteProjectCircumPo(ProjectCircumPo circumPo, String updateBy, String roleId);
	public int insert(ProjectCircumPo project, UserPo updateUser);
	public void update(ProjectCircumPo project, UserPo updateUser);
}

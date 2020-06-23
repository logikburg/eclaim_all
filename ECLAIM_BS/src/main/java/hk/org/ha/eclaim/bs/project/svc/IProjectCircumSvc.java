package hk.org.ha.eclaim.bs.project.svc;

import java.util.List;

import hk.org.ha.eclaim.bs.project.po.ProjectCircumPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

public interface IProjectCircumSvc {

	public ProjectCircumPo getProjectCircumPoByUid(int circumUid);
	public List<ProjectCircumPo> getProjectCircumListByProjectId(int projectId);
	public List<ProjectCircumPo> getProjectCircumListByProjectId(int projectId,int verId);
	public void deleteProjectCircum(int circumUid, String updateBy, String roleId);
	public int insert(ProjectCircumPo circumPo, UserPo updateUser);
	public void update(ProjectCircumPo circumPo, UserPo updateUser);
}

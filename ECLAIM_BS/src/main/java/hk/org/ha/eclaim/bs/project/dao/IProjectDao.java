package hk.org.ha.eclaim.bs.project.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.project.po.ProjectPo;
import hk.org.ha.eclaim.bs.project.po.ProjectPublishJobPo;
import hk.org.ha.eclaim.bs.project.po.ProjectPublishPo;
import hk.org.ha.eclaim.bs.project.po.ProjectPublishRankPo;
import hk.org.ha.eclaim.bs.project.po.ProjectRequestPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

public interface IProjectDao {

	public List<ProjectRequestPo> getAllProject();
	public ProjectPo getProjectByProjectId(int projectId);
	public ProjectPo getProjectByProjectId(int projectUid, int verId);
	public void deleteProject(int projectId, String updateBy, String roleId);
	public int insert(ProjectPo project, UserPo updateUser);
	public void update(ProjectPo project, UserPo updateUser);
	public List<ProjectPo> searchProjectByProjectName(String projectName, String deptId, String[] status);
	public List<ProjectRequestPo> getCurrentAllProject(UserPo user);
	public List<ProjectRequestPo> searchProjectByRequest(Integer projectId, String projectName, String status, String ownerId, String preparerId, String userId);
	public boolean isExistingProjectName(String projectId, String projectName);
	//public List<ProgramTypePo> getAllProgramType();
	public String getProgramTypeDesc(String typeCode);
	public int createProjectVer(int projectId, int projectVer, String action, UserPo user);
	
	//Project Invitation
	public List<?>  getProjectPublishInvitation(int project);
	public List<?> getProjectInvitationJobGroup(int project);
	public void insertProjectInvitation(ProjectPublishPo pp, List<ProjectPublishJobPo> ppj, List<ProjectPublishRankPo> ppr) throws RuntimeException;
}

package hk.org.ha.eclaim.bs.project.svc;

import java.util.List;

import hk.org.ha.eclaim.bs.cs.po.ProgramTypePo;
import hk.org.ha.eclaim.bs.project.po.ApprovalHistoryPo;
import hk.org.ha.eclaim.bs.project.po.ProjectPo;
import hk.org.ha.eclaim.bs.project.po.ProjectPublishJobPo;
import hk.org.ha.eclaim.bs.project.po.ProjectPublishPo;
import hk.org.ha.eclaim.bs.project.po.ProjectPublishRankPo;
import hk.org.ha.eclaim.bs.project.po.ProjectRequestPo;
import hk.org.ha.eclaim.bs.request.po.RequestEmailPo;
import hk.org.ha.eclaim.bs.request.po.RequestWorkflowHistoryPo;
import hk.org.ha.eclaim.bs.request.po.RequestWorkflowRoutePo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

public interface IProjectSvc {

	public List<ProjectRequestPo> getAllProject();
	public List<ProjectRequestPo> getCurrentProject(UserPo user);
	public List<ProjectRequestPo> searchProjectByRequest(Integer projectId, String projectName, String status, String ownerId, String preparerId, String userId);
	public ProjectPo getProjectByProjectId(int projectId);
	public ProjectPo getProjectVerByProjectId(int projectId,int verId);
	public void deleteProject(int projectId, String updateBy, String roleId);
	public int insert(ProjectPo project, UserPo updateUser);
	public void update(ProjectPo project, UserPo updateUser);
	public Boolean isExistingProjectName(String projectId, String projectName);
	public List<ProjectPo> searchProjectByName(String projectName, String deptId, String[] status);
	public String getActionNameByCode(String actionCode);
	
	public RequestWorkflowRoutePo getRoute(String type, String currentStatus);
	
	public void insertHistory(RequestWorkflowHistoryPo history, UserPo updateUser);
	
	public void insertEmail(RequestEmailPo email, UserPo updateUser);
	
	public void insertApprHist(ApprovalHistoryPo apprHist, UserPo user);
	
	public List<ApprovalHistoryPo> getProjectApprHistPoList(int projectId);
	
	public List<ApprovalHistoryPo> getProjectApprHistPoList(int projectId, int verId);
	
	public List<ProgramTypePo> getAllProgramType();
	
	public String getProgramTypeDesc(String typeCode);
	
	public int processProjectAction(int projectId, int projectVer, String action, UserPo user);
	
	public List<?> getProjectInvitationJobGroup(int projectId);
	public void getProjectPublishInvitation(int projectId);
	public void insertProjectInvitation(ProjectPublishPo pp, List<ProjectPublishJobPo> ppj, List<ProjectPublishRankPo> ppr);
}

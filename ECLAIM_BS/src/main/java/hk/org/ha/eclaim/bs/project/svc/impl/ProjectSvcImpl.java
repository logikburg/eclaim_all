package hk.org.ha.eclaim.bs.project.svc.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hk.org.ha.eclaim.bs.cs.po.ProgramTypePo;
import hk.org.ha.eclaim.bs.project.dao.IApprovalHistoryDao;
import hk.org.ha.eclaim.bs.project.dao.IProjectDao;
import hk.org.ha.eclaim.bs.project.po.ApprovalHistoryPo;
import hk.org.ha.eclaim.bs.project.po.ProjectPo;
import hk.org.ha.eclaim.bs.project.po.ProjectPublishJobPo;
import hk.org.ha.eclaim.bs.project.po.ProjectPublishPo;
import hk.org.ha.eclaim.bs.project.po.ProjectPublishRankPo;
import hk.org.ha.eclaim.bs.project.po.ProjectRequestPo;
import hk.org.ha.eclaim.bs.project.svc.IProjectSvc;
import hk.org.ha.eclaim.bs.request.dao.IRequestEmailDao;
import hk.org.ha.eclaim.bs.request.dao.IRequestWorkflowHistoryDao;
import hk.org.ha.eclaim.bs.request.dao.IRequestWorkflowRouteDao;
import hk.org.ha.eclaim.bs.request.po.RequestEmailPo;
import hk.org.ha.eclaim.bs.request.po.RequestWorkflowHistoryPo;
import hk.org.ha.eclaim.bs.request.po.RequestWorkflowRoutePo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

@Service("projectSvc")
public class ProjectSvcImpl<JobGroupRankVo> implements IProjectSvc {

	@Autowired
	IProjectDao projectDao;

	@Autowired
	IRequestWorkflowRouteDao requestWorkflowRouteDao;

	@Autowired
	IRequestWorkflowHistoryDao requestWorkflowHistoryDao;

	@Autowired
	IRequestEmailDao requestEmailDao;

	@Autowired
	IApprovalHistoryDao apprHistDao;

	public List<ProjectRequestPo> getAllProject() {
		return projectDao.getAllProject();
	}

	public List<ProjectRequestPo> getCurrentProject(UserPo user) {
		return projectDao.getCurrentAllProject(user);
	}

	public List<ProjectRequestPo> searchProjectByRequest(Integer projectId, String projectName, String status,
			String ownerId, String preparerId, String userId) {
		return projectDao.searchProjectByRequest(projectId, projectName, status, ownerId, preparerId, userId);
	}

	// Get last project without verId
	public ProjectPo getProjectByProjectId(int projectId) {
		return projectDao.getProjectByProjectId(projectId);
	}

	// Get project with ver
	public ProjectPo getProjectVerByProjectId(int projectId, int verId) {
		return projectDao.getProjectByProjectId(projectId, verId);
	}

	public List<ApprovalHistoryPo> getProjectApprHistPoList(int projectId) {
		return apprHistDao.getApprovalHistoryListByProjectId(projectId);
	}

	public List<ApprovalHistoryPo> getProjectApprHistPoList(int projectId, int verId) {
		return apprHistDao.getApprovalHistoryListByProjectId(projectId, verId);
	}

	public Boolean isExistingProjectName(String projectId, String projectName) {
		return projectName == null ? false : projectDao.isExistingProjectName(projectId, projectName);
	}

	public List<ProjectPo> searchProjectByName(String projectName, String deptId, String[] status) {
		// return projectDao.searchProjectByProjectName(projectName,deptId, status);
		return null;
	}

	public String getActionNameByCode(String actionCode) {
		return apprHistDao.getActionNameByCode(actionCode);
	}

	@Transactional
	public void deleteProject(int projectId, String updateBy, String roleId) {
		// TODO Auto-generated method stub

	}

	@Transactional
	public int insert(ProjectPo project, UserPo updateUser) {
		return projectDao.insert(project, updateUser);
	}

	@Transactional
	public void update(ProjectPo project, UserPo updateUser) {
		projectDao.update(project, updateUser);
	}

	public RequestWorkflowRoutePo getRoute(String type, String currentStatus) {
		return requestWorkflowRouteDao.getDefaultRoute("NA", type, currentStatus);
	}

	@Transactional
	public void insertHistory(RequestWorkflowHistoryPo history, UserPo updateUser) {
		requestWorkflowHistoryDao.insert(history, updateUser);
	}

	@Transactional
	public void insertEmail(RequestEmailPo email, UserPo updateUser) {
		requestEmailDao.insertEmail(email, updateUser);
	}

	@Transactional
	public void insertApprHist(ApprovalHistoryPo apprHist, UserPo user) {
		apprHistDao.insertApprHistPo(apprHist, user);
	}

	public List<ProgramTypePo> getAllProgramType() {
		// return projectDao.getAllProgramType();
		return null;
	}

	public String getProgramTypeDesc(String typeCode) {
		return typeCode != null ? projectDao.getProgramTypeDesc(typeCode) : null;
	}

	public int processProjectAction(int projectId, int projectVer, String action, UserPo user) {
		return projectDao.createProjectVer(projectId, projectVer, action, user);
	}

	public void getProjectPublishInvitation(int projectId) {
		projectDao.getProjectPublishInvitation(projectId);
	}
	
	public List<?> getProjectInvitationJobGroup(int projectId) {
		return projectDao.getProjectInvitationJobGroup(projectId);
	}

	public void insertProjectInvitation(ProjectPublishPo pp, List<ProjectPublishJobPo> ppj, List<ProjectPublishRankPo> ppr) {
		projectDao.insertProjectInvitation(pp, ppj, ppr);
	}
}

package hk.org.ha.eclaim.controller.home;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import hk.org.ha.eclaim.bs.cs.po.StaffGroupPo;
import hk.org.ha.eclaim.bs.cs.po.StatusPo;
import hk.org.ha.eclaim.bs.cs.svc.ICommonSvc;
import hk.org.ha.eclaim.bs.maintenance.svc.IDocumentSvc;
import hk.org.ha.eclaim.bs.maintenance.svc.INewsSvc;
import hk.org.ha.eclaim.bs.project.po.ProjectPublishJobPo;
import hk.org.ha.eclaim.bs.project.po.ProjectPublishPo;
import hk.org.ha.eclaim.bs.project.po.ProjectPublishRankPo;
import hk.org.ha.eclaim.bs.project.po.ProjectRequestPo;
import hk.org.ha.eclaim.bs.project.svc.IProjectSvc;
import hk.org.ha.eclaim.bs.request.po.RequestPo;
import hk.org.ha.eclaim.bs.request.svc.IRequestSvc;
import hk.org.ha.eclaim.bs.security.po.DataAccessPo;
import hk.org.ha.eclaim.bs.security.po.MPRSFunctionPo;
import hk.org.ha.eclaim.bs.security.po.UserLogonPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.po.UserRolePo;
import hk.org.ha.eclaim.bs.security.svc.ISecuritySvc;
import hk.org.ha.eclaim.core.constant.VersionConstant;
import hk.org.ha.eclaim.core.controller.BaseController;
import hk.org.ha.eclaim.core.helper.StrHelper;
import hk.org.ha.eclaim.core.logger.EClaimLogger;
import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.model.home.HomeWebVo;
import hk.org.ha.eclaim.model.maintenance.RoleVo;
import hk.org.ha.eclaim.model.project.ProjectIncludedInvitationVo;
import hk.org.ha.eclaim.model.project.ProjectInvitationWebVo;
import hk.org.ha.eclaim.model.project.ProjectRequestVo;

@Controller
public class HomeController extends BaseController {

	@Value("${ss.app.environment}")
	private String environment;

	private String swVersion = VersionConstant.APP_VERSION_NO;

	@Value("${bsd.login.url}")
	private String bsdLoginUrl;

	@Autowired
	IRequestSvc requestSvc;

	@Autowired
	ICommonSvc commonSvc;

	@Autowired
	ISecuritySvc securitySvc;

	@Autowired
	INewsSvc newsSvc;

	@Autowired
	IDocumentSvc documentSvc;

	@Autowired
	IProjectSvc projectSvc;

	@RequestMapping(value = "/home/home", method = RequestMethod.GET)
	public ModelAndView home(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String userId = "ABC";// this.getSessionUser(request).getUserId();
		HomeWebVo webVo = new HomeWebVo();

		// Get current news and documents
//		List<NewsPo> currentNews = newsSvc.getCurrentNews();
//		List<DocumentPo> currentDocument = documentSvc.getAllActiveDocuments();
//		webVo.setCurrentNews(currentNews);
//		webVo.setCurrentDocument(currentDocument);

		UserPo user = securitySvc.findUser(userId);
		webVo.setUserName(user.getUserName());
		webVo.setUser(user);

		String currentRoleId = (String) request.getSession().getAttribute("currentRole");
		int userRoleUid = -1;
		if (currentRoleId == null || "".equals(currentRoleId)) {
			boolean haveDefault = false;

			// Check is there default role set
			for (int v = 0; v < user.getUserRole().size(); v++) {
				if ("Y".equals(user.getUserRole().get(v).getDefaultRole())) {
					currentRoleId = user.getUserRole().get(v).getRole().getRoleId();
					request.getSession().setAttribute("currentRole", currentRoleId);
					haveDefault = true;
					break;
				}
			}

			if (!haveDefault) {
				currentRoleId = "ABC"; // user.getUserRole().get(0).getRole().getRoleId();
				request.getSession().setAttribute("currentRole", currentRoleId);
			}
		}

		// Get the list of User Role
		List<UserRolePo> userRoleListPo = securitySvc.findUserRoleByUserId(userId);
		List<RoleVo> userRoleListVo = new ArrayList<RoleVo>();
		for (int i = 0; i < userRoleListPo.size(); i++) {
			RoleVo roleVo = new RoleVo();
			roleVo.setRoleId(userRoleListPo.get(i).getRole().getRoleId());
			roleVo.setRoleName(userRoleListPo.get(i).getRole().getRoleName());
			userRoleListVo.add(roleVo);
			if (currentRoleId.equals(userRoleListPo.get(i).getRole().getRoleId())) {
				webVo.setCurrentRole(userRoleListPo.get(i).getRole().getRoleName());
				request.getSession().setAttribute("currentRoleName", userRoleListPo.get(i).getRole().getRoleName());
				userRoleUid = userRoleListPo.get(i).getUserRoleId();
				// break;
			}
		}

		System.out.println("userRoleUid: " + userRoleUid);
		System.out.println("UserRoleListVo.size: " + userRoleListVo.size());

		request.getSession().setAttribute("userRoleList", userRoleListVo);

		// Get functions for current role
		List<MPRSFunctionPo> funcList = new ArrayList<MPRSFunctionPo>();
//     	funcList.addAll(securitySvc.getFunctionListByRole((String)request.getSession().getAttribute("currentRole")));
		// request.getSession().setAttribute("funcList", funcList);
		System.out.println("funcList.size: " + funcList.size());
		List<String> parentFuncIdList = new ArrayList<String>();
		List<String> funcIdList = new ArrayList<String>();
		for (int i = 0; i < funcList.size(); i++) {
			String parentFuncId = funcList.get(i).getParentFuncId();
			if (parentFuncId != null && !parentFuncIdList.contains(parentFuncId)) {
				parentFuncIdList.add(parentFuncId);
			}
			funcIdList.add(funcList.get(i).getFuncId());
		}
		request.getSession().setAttribute("parentFuncList", parentFuncIdList);
		request.getSession().setAttribute("funcList", funcIdList);

		// Get the Role Data Access
		List<DataAccessPo> dataAccessList = new ArrayList<DataAccessPo>();// securitySvc.getDataAccessByRoleId(userRoleUid);

		List<ProjectRequestPo> myProject = projectSvc.getCurrentProject(user);
		System.out.println("myProject.size():  " + myProject.size());
		List<ProjectRequestVo> projectVoList = new ArrayList<ProjectRequestVo>();
		for (ProjectRequestPo projPo : myProject) {
			ProjectRequestVo vo = new ProjectRequestVo(projPo);
			projectVoList.add(vo);
		}
		webVo.setMyProjectList(projectVoList);

		UserLogonPo lastLogonPo = securitySvc.getLastLogonInfo(userId);
		webVo.setUserLogonPo(lastLogonPo);

		// user.setFunctionList(funcIdList);
		user.setCurrentRole((String) request.getSession().getAttribute("currentRole"));

		List<String> clusterList = new ArrayList<String>();
		String cluster = "";
		for (int i = 0; i < dataAccessList.size(); i++) {
			if (!clusterList.contains(dataAccessList.get(i).getClusterCode())) {
				if (dataAccessList.get(i).getClusterCode() != null) {
					clusterList.add(dataAccessList.get(i).getClusterCode());
				}
			}
		}

		for (int j = 0; j < clusterList.size(); j++) {
			if (j != 0) {
				cluster += ", ";
			}
			cluster += clusterList.get(j);
		}

		webVo.setCurrentCluster(cluster);
		request.getSession().setAttribute("currentUser", user);
		request.getSession().setAttribute("currentUserId", user.getUserId());
		request.getSession().setAttribute("currentUserRoleId", userRoleUid);
		request.getSession().setAttribute("currentUserName", user.getUserName());
		request.getSession().setAttribute("currentCluster", cluster);
		request.getSession().setAttribute("environment", environment);
		request.getSession().setAttribute("swVersion", swVersion);
		request.getSession().setAttribute("bsdLoginUrl", bsdLoginUrl);

//        webVo.setMyProject(myProject);
		System.out.println("myProject.size(): " + myProject.size());

		if (!"".equals(request.getParameter("url")) && request.getParameter("url") != null) {
			response.sendRedirect(request.getParameter("url"));
		}

		return new ModelAndView("common/home", "formBean", webVo);
	}

	// Added for CR0367
	public List<RequestPo> matchStaffGroup(List<RequestPo> requestList) {
		List<StaffGroupPo> staffGroupList = commonSvc.getAllStaffGroup();

		for (int i = 0; i < requestList.size(); i++) {
			String tmpStaffGroup = "";

			for (int j = 0; j < requestList.get(i).getRequestPositionList().size(); j++) {
				if (!tmpStaffGroup.equals(requestList.get(i).getRequestPositionList().get(j).getStaffGroupCode())
						&& requestList.get(i).getRequestPositionList().get(j).getStaffGroupCode() != null) {
					if ("".equals(tmpStaffGroup)) {
						tmpStaffGroup = requestList.get(i).getRequestPositionList().get(j).getStaffGroupCode();
					} else {
						tmpStaffGroup = "";
						break;
					}
				}
			}
			requestList.get(i).setStaffGroup(getStaffGroupAbbr(tmpStaffGroup, staffGroupList));
		}

		return requestList;
	}

	// Added for CR0367
	public String getStaffGroupAbbr(String staffGroupCode, List<StaffGroupPo> staffGroupList) {
		if (staffGroupCode == null) {
			return "";
		}

		// Look up the staff group abbr
		for (int x = 0; x < staffGroupList.size(); x++) {
			if (staffGroupCode.equals(staffGroupList.get(x).getStaffGroupCode())) {
				return staffGroupList.get(x).getStaffGroupAbbr();
			}
		}

		return "";
	}

//	@RequestMapping(value = "/home/invitation", method = RequestMethod.POST)
//	public ModelAndView home(@ModelAttribute("projectId") int projectId, HttpServletRequest request,
//			HttpServletResponse response) throws Exception {
//		
//		ProjectInvitationWebVo webVo = new ProjectInvitationWebVo();
//		ProjectIncludedInvitationVo invitationVo = new ProjectIncludedInvitationVo();
//		
//		List<String> jobGroup = projectSvc.getProjectInvitationJobGroup(projectId);
//		for (String jobs : jobGroup) {
//			invitationVo = new ProjectIncludedInvitationVo();
//			invitationVo.setJobs(jobs);
//			webVo.getIncludedInvitations().add(invitationVo);
//		}
//		
//		// projectSvc.getProjectPublishInvitation(2234);
//
//		return new ModelAndView("project/projectInvitation", "formBean", webVo);
//	}
	
//	@RequestMapping(value = "/home/invitation/publish", method = RequestMethod.POST)
//	public ModelAndView publish(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		
//		ProjectInvitationWebVo webVo = new ProjectInvitationWebVo();
//		Date updateDate = new Date();
//		
//		// Publish Project
//		ProjectPublishPo pp = new ProjectPublishPo();
//		pp.setProjectId(2235);
//		pp.setProjectVerId(1);
//		pp.setTargetApplicant("TargetApplicant");
//		pp.setOtherInfo("OtherInfo");
//		pp.setPublishDate(updateDate);
//		pp.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
//		pp.setCreatedBy("SYSTEM");
//		pp.setCreatedRoleId("SYSTEM");
//		pp.setCreatedDate(updateDate);
//		pp.setUpdatedBy("SYSTEM");
//		pp.setUpdatedRoleId("SYSTEM");
//		pp.setUpdatedDate(updateDate);
//
//		// Publish Project Job
//		ProjectPublishJobPo ppJob = new ProjectPublishJobPo();
//		ppJob.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
//		ppJob.setCreatedBy("SYSTEM");
//		ppJob.setCreatedRoleId("SYSTEM");
//		ppJob.setCreatedDate(updateDate);
//		ppJob.setUpdatedBy("SYSTEM");
//		ppJob.setUpdatedRoleId("SYSTEM");
//		ppJob.setUpdatedDate(updateDate);
//		ppJob.setJobGroupId(101);
//		ppJob.setProjectPublishPo(pp);
//
//		// Publish Project Rank
//		ProjectPublishRankPo ppRank = new ProjectPublishRankPo();
//		ppRank.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
//		ppRank.setCreatedBy("SYSTEM");
//		ppRank.setCreatedRoleId("SYSTEM");
//		ppRank.setCreatedDate(updateDate);
//		ppRank.setUpdatedBy("SYSTEM");
//		ppRank.setUpdatedRoleId("SYSTEM");
//		ppRank.setUpdatedDate(updateDate);
//		ppRank.setPublishJobs(ppJob);
//
//		projectSvc.insertProjectInvitation(pp, ppJob, ppRank);
//		
//		return new ModelAndView("project/projectInvitation", "formBean", webVo);
//	}

	@RequestMapping(value = "/home/home", method = RequestMethod.POST)
	public String switchRole(@ModelAttribute("targetRole") String targetRole, HttpServletRequest request)
			throws Exception {
		System.out.println("HomeController Perform Update");
		boolean isError = true;
		String userId = this.getSessionUser(request).getUserId();
		String switchToRoleName = "";
		targetRole = HtmlUtils.htmlEscape(targetRole);
		EClaimLogger.info("user Id: " + userId + ", targetRole : " + targetRole);
		UserPo user = null;
		List<String> funcIdList = new ArrayList<String>();
		if (StrHelper.isNotEmpty(targetRole)) {
			// Get the user info
			user = securitySvc.findUser(userId);

			if (user.getUserRole() != null) {
				for (int i = 0; i < user.getUserRole().size(); i++) {
					if (targetRole.equals(user.getUserRole().get(i).getRole().getRoleId())) {
						switchToRoleName = user.getUserRole().get(i).getRole().getRoleName();
						// Get function ID for current role
						List<MPRSFunctionPo> funcList = new ArrayList<MPRSFunctionPo>();
						funcList.addAll(securitySvc.getFunctionListByRole(targetRole));
						funcIdList = new ArrayList<String>();
						for (int j = 0; j < funcList.size(); j++) {
							funcIdList.add(funcList.get(j).getFuncId());
						}
						isError = false;
						break;
					}
				}
			}
		} else {
			throw new Exception("Target Role is invalid");
		}

		if (!isError) {
			user.setCurrentRole(targetRole);
			setSessionCurrentRoleId(request, targetRole);
			request.getSession().setAttribute("currentRoleName", switchToRoleName);
			request.getSession().setAttribute("currentUser", user);
			request.getSession().setAttribute("funcList", funcIdList);
		}

		return "redirect:/home/home";
	}

	@RequestMapping(value = "/home/searchProject", method = RequestMethod.POST)
	public ModelAndView searchProject(@ModelAttribute HomeWebVo view, HttpServletRequest request) throws Exception {
		System.out.println("HomeController Perform Update");
		boolean isError = true;
		String userId = this.getSessionUser(request).getUserId();
		EClaimLogger.info("user Id: " + userId);
		UserPo user = securitySvc.findUser(userId);
		int projectId = 0;
		if (!"".equals(view.getProjectId())) {
			projectId = Integer.parseInt(view.getProjectId());
		}
		List<ProjectRequestPo> projectList = projectSvc.searchProjectByRequest(projectId, view.getProjectName(),
				view.getStatusCode(), view.getOwnerId(), view.getPreparerId(), userId);
		List<ProjectRequestVo> projectVoList = new ArrayList<ProjectRequestVo>();
		for (ProjectRequestPo projPo : projectList) {
			ProjectRequestVo vo = new ProjectRequestVo(projPo);
			vo.setProjectStatus(commonSvc.getStatusDesc(projPo.getProjectStatusCode()));
			vo.setDepartmentName(commonSvc.getOrgNameByOrgCode(projPo.getDepartmentId()));
			projectVoList.add(vo);
		}
		view.setMyProjectList(projectVoList);
		return new ModelAndView("common/home", "formBean", view);
	}

	@ModelAttribute("statusList")
	public List<StatusPo> getStatusList() {
		List<StatusPo> list = commonSvc.getAllStatusList();
		return list;
	}

}

package hk.org.ha.eclaim.controller.project;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;

import hk.org.ha.eclaim.bs.cs.po.CircumstancePo;
import hk.org.ha.eclaim.bs.cs.svc.ICommonSvc;
import hk.org.ha.eclaim.bs.project.po.ApprovalHistoryPo;
import hk.org.ha.eclaim.bs.project.po.ProjectCircumPo;
import hk.org.ha.eclaim.bs.project.po.ProjectDocumentPo;
import hk.org.ha.eclaim.bs.project.po.ProjectJobPo;
import hk.org.ha.eclaim.bs.project.po.ProjectJobRankPo;
import hk.org.ha.eclaim.bs.project.po.ProjectPo;
import hk.org.ha.eclaim.bs.project.po.ProjectSchedulePo;
import hk.org.ha.eclaim.bs.project.svc.IProjectCircumSvc;
import hk.org.ha.eclaim.bs.project.svc.IProjectDocumentSvc;
import hk.org.ha.eclaim.bs.project.svc.IProjectJobSvc;
import hk.org.ha.eclaim.bs.project.svc.IProjectScheduleSvc;
import hk.org.ha.eclaim.bs.project.svc.IProjectSvc;
import hk.org.ha.eclaim.bs.request.po.RequestEmailPo;
import hk.org.ha.eclaim.bs.request.po.RequestWorkflowHistoryPo;
import hk.org.ha.eclaim.bs.request.po.RequestWorkflowRoutePo;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.svc.ISecuritySvc;
import hk.org.ha.eclaim.core.controller.BaseController;
import hk.org.ha.eclaim.core.logger.EClaimLogger;
import hk.org.ha.eclaim.model.project.ApprHistVo;
import hk.org.ha.eclaim.model.project.ProjectDocumentVo;
import hk.org.ha.eclaim.model.project.ProjectFinImplVo;
import hk.org.ha.eclaim.model.project.ProjectJobVo;
import hk.org.ha.eclaim.model.project.ProjectReviewVo;
import hk.org.ha.eclaim.model.project.ProjectScheduleVo;

@Controller
public class ProjectReviewController extends BaseController {

	@Autowired
	private IProjectSvc projectSvc;
	
	@Autowired
	private IProjectCircumSvc circumSvc;
	
	@Autowired
	private IProjectJobSvc jobSvc;
	
	@Autowired
	private IProjectScheduleSvc scheduleSvc;
	
	@Autowired
	private IProjectDocumentSvc docSvc; 
	
	@Autowired
	private ICommonSvc commonSvc;
	
	@Autowired
	private ISecuritySvc securitySvc;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
	private SimpleDateFormat dtformat = new SimpleDateFormat("dd/MMM/yyyy hh:mm:ss");

	
	@RequestMapping(value="/project/review", produces= "text/plain;charset=UTF-8", method=RequestMethod.GET)
	public ModelAndView initList(HttpServletRequest request, @RequestParam(required=false) String projectId, String verId) throws Exception {
		// Get the document id
		EClaimLogger.info("ProjectId: " + projectId);
		ProjectReviewVo webVo = new ProjectReviewVo();
		UserPo user = securitySvc.findUser(this.getSessionUser(request).getUserId());
		ProjectPo po = null;
		
		if (projectId == null) {
			webVo.setDisplayMessage("Record update fail.");
			webVo.setUpdateSuccess("N");
			return new ModelAndView("project/projectReview", "formBean", webVo);
		}
		if(StringUtils.isNumeric(verId) && !"".equals(verId)) {
			po = projectSvc.getProjectVerByProjectId(Integer.parseInt(projectId), Integer.parseInt(verId));
		}else {
			po = projectSvc.getProjectByProjectId(Integer.parseInt(projectId));
		}
		webVo = GetProejctReviewVo(po, user);
		
		
		return new ModelAndView("project/projectReview", "formBean", webVo);
	}

	public ProjectReviewVo GetProejctReviewVo(ProjectPo po, UserPo user)
			throws Exception {
		
		ProjectReviewVo webVo = new ProjectReviewVo();
		
		List<ProjectJobPo> jobPoList = jobSvc.getProjectJobListByProjectId(po.getProjectId(), po.getProjectVerId());
		List<ProjectSchedulePo> schedulePoList = scheduleSvc.getProjectScheduleListByProjectId(po.getProjectId(), po.getProjectVerId());
		List<ProjectCircumPo> circumPoList = circumSvc.getProjectCircumListByProjectId(po.getProjectId(), po.getProjectVerId());
		List<ProjectDocumentPo> docList = docSvc.getProjectDocumentByProjectId(po.getProjectId(), po.getProjectVerId());
		List<ApprovalHistoryPo> apprHistPoList = projectSvc.getProjectApprHistPoList(po.getProjectId(), po.getProjectVerId());
		
		webVo.setProjectId(po.getProjectId().toString());
		webVo.setProjectVerId(po.getProjectVerId().toString());
		webVo.setProjectStatus(po.getProjectStatusCode());
		webVo.setRecType(po.getRecType());
		webVo.setProjectStep(po.getProjectStep().toString());
		webVo.setDepartmentName(po.getDepartmentId() != null ? commonSvc.getOrgNameByOrgCode(po.getDepartmentId()) : "");
		webVo.setProjectName(po.getProjectName());
		webVo.setProjectNameC(po.getProjectNameC());
		webVo.setProjectOwner(po.getProjectOwner());
		webVo.setProjectPreparer(po.getProjectPreparer());
		webVo.setProjectPurpose(po.getProjectPurpose());
		webVo.setProgramType(projectSvc.getProgramTypeDesc(po.getProgramType()));
		webVo.setFundingSource(po.getFundingSource());
//			webVo.setSrsDepartment(po.getSrsDepartment());
//			webVo.setSrsHospital(po.getSrsHospital());
		webVo.setStartDate(po.getFromDate() != null ? sdf.format(po.getFromDate()) : null);
		webVo.setEndDate(po.getToDate() != null ? sdf.format(po.getToDate()) : null);
		webVo.setFinIcId(po.getFinHospIcId());
		webVo.setFinIcName(po.getFinHospIc());
		List<ProjectScheduleVo> scheduleList = new ArrayList<ProjectScheduleVo>();
		
		for(ProjectSchedulePo schedulePo : schedulePoList) {
			ProjectScheduleVo scheduleVo = new ProjectScheduleVo();
			scheduleVo.setStartTime(schedulePo.getStartTime());
			scheduleVo.setEndTime(schedulePo.getEndTime());
			scheduleVo.setPatternCode(schedulePo.getPatternCode() != null ? schedulePo.getPatternCode().split(",") : null);
			scheduleVo.setPatternDesc(scheduleVo.getPatternCode() != null ? scheduleSvc.getPatternDesc(scheduleVo.getPatternCode()) : null);
			scheduleVo.setScheduleDate(schedulePo.getScheduleDate() != null ? sdf.format(schedulePo.getScheduleDate()) : null);
			scheduleList.add(scheduleVo);
		}
		webVo.setScheduleList(scheduleList);
		
		List<ProjectFinImplVo> finImplVoList  = new ArrayList<ProjectFinImplVo> ();
		List<ProjectJobVo> jobVoList = new ArrayList<ProjectJobVo>();
		int totHour = 0;
		int totEstImp = 0;
		for(ProjectJobPo jobPo :jobPoList) {
			ProjectJobVo vo = new ProjectJobVo();
			vo.setProjectId(jobPo.getProjectId().toString());
			vo.setProjectVerId(jobPo.getProjectVerId().toString());
			vo.setJobGroupId(jobPo.getJobGroupId().toString());
			vo.setStaffGrp(jobPo.getStaffGroup());
			vo.setCoOrdinator(jobPo.getProjectCoordinator());
			vo.setDescription(jobPo.getJobDescription());
			vo.setTargerApp(jobPo.getTargerApplicant());
			vo.setOtherInfo(jobPo.getOtherInformation());
			vo.setQuota(jobPo.getQuota() == null ? "" : jobPo.getQuota().toString());
			vo.setDuration(jobPo.getDuration() == null ? "" : jobPo.getDuration().toString());
			vo.setSessionDay(jobPo.getSessionPerDay() == null ? "" : jobPo.getSessionPerDay().toString());
			vo.setSessionHour(jobPo.getHoursPerSession().toString());
			vo.setTotalHour(jobPo.getTotalHour().toString());
			vo.setStatus(jobPo.getRecState());
			
			List<ProjectJobRankPo> jobRankPoList = jobSvc.getJobRankPoList(jobPo.getJobGroupId());
			String[] jobRanks = jobRankPoList.stream()
				    .map(jobRank -> jobRank.getRank())
				    .toArray(String[]::new);
			vo.setJobRanks(String.join(",", jobRanks));
			String higherRank = jobSvc.getHigherRank(jobRanks.length > 0 ? jobRanks[0] : null, jobRanks.length > 1 ? jobRanks[1] : null,
					jobRanks.length > 2 ? jobRanks[2] : null, jobRanks.length > 3 ? jobRanks[3] : null,
							jobRanks.length > 4 ? jobRanks[4] : null);
			int maxVal = jobSvc.getMaxGradeVal(higherRank);
			vo.setMaxGradeVal(String.valueOf(maxVal));
			int estImp = Math.round((maxVal * jobPo.getTotalHour())/140);
			vo.setEstImpact(String.valueOf(estImp));
			
			totHour += jobPo.getTotalHour();
			totEstImp += estImp;
			jobVoList.add(vo);
			
			ProjectFinImplVo finImplVo = new ProjectFinImplVo();
			finImplVo.setDepartId(po.getDepartmentId().toString());
			finImplVo.setDepartName(webVo.getDepartmentName());
			finImplVo.setLevel("Entry");
			finImplVo.setWorkHour(jobPo.getTotalHour());
			finImplVo.setJobRankList(jobRanks);
			finImplVo.setProjectId(jobPo.getProjectId().toString());
			finImplVo.setProjectVerId(jobPo.getProjectVerId().toString());
			finImplVo.setJobGroupId(jobPo.getJobGroupId().toString());
			finImplVoList.add(finImplVo);
		}
		webVo.setTotHour(String.valueOf(totHour));
		webVo.setTotEstImp(String.valueOf(totEstImp));
		webVo.setJobList(jobVoList);
		webVo.setFinImplList(finImplVoList);
		
		webVo.setJustifications(po.getJustifications());
		webVo.setTriggerPoint(po.getTriggerPoint());
		webVo.setManPowerShortage(po.getManpowerSituation());
		
		webVo.setUsingOTA(po.getUseOtaFlag());
		webVo.setOtaJustifications(po.getOtaJustifications());
		
		webVo.setqDeliver(po.getqDeliverables());

		List<String> circumList = new ArrayList<String>();
		for(ProjectCircumPo circumPo : circumPoList) {
			CircumstancePo circumstancePo = commonSvc.getCircumPoByCircumId(circumPo.getCircumstanceId());
			circumList.add(circumstancePo.getDescriptionQ());
		}
		webVo.setCircumList(circumList);
		
		
		List<ApprHistVo> apprHistVoList = new ArrayList<ApprHistVo>();
		for(ApprovalHistoryPo apprHistPo : apprHistPoList) {
			UserPo apprUser = securitySvc.findUser(apprHistPo.getActionBy());
			ApprHistVo apprHistVo = new ApprHistVo();
			apprHistVo.setName(apprUser.getUserName());
			apprHistVo.setEmail(apprUser.getEmail());
			apprHistVo.setRemark(apprHistPo.getRemark());
			apprHistVo.setRoleName(apprHistPo.getRole() != null ? apprHistPo.getRole().getRoleName() : apprHistPo.getRoleCode());
			apprHistVo.setActionName(apprHistPo.getAction() != null ? apprHistPo.getAction().getActionName() : projectSvc.getActionNameByCode(apprHistPo.getActionCode()));
			apprHistVo.setActionDate(dtformat.format(apprHistPo.getActionDate()));
			apprHistVoList.add(apprHistVo);
		}
		webVo.setApprHistList(apprHistVoList);
		
		//setup Document List
		List<ProjectDocumentVo> docVoList = new ArrayList<ProjectDocumentVo>();
		for(ProjectDocumentPo docPo : docList) {
			ProjectDocumentVo docVo = new ProjectDocumentVo();
			UserPo uploadUser = securitySvc.findUser(docPo.getCreatedBy());
			docVo.setDocumentId(docPo.getDocumentId());
			docVo.setProjectId(docPo.getProjectId());
			docVo.setProjectVerId(docPo.getProjectVerId());
			docVo.setUploadUserId(docPo.getCreatedBy());
			docVo.setUploadUser(uploadUser != null ? uploadUser.getUserName() : docPo.getCreatedBy());
			docVo.setFileName(docPo.getFileName());
			docVo.setPath(docPo.getPath());
			docVo.setDescription(docPo.getDescription());
			docVo.setUploadDate(docPo.getUpdatedDate());
			docVoList.add(docVo);
		}
		webVo.setDocList(docVoList);
		
		RequestWorkflowRoutePo route = projectSvc.getRoute(po.getRecType(), po.getProjectStatusCode());
		webVo.setActionButton("");
		webVo.setReturnButton("");
		if (route != null) {
			// Check whether user role is in from role
			boolean isCurrentRole = false;
			
			if("OPEN".equals(po.getProjectStatusCode())) {
				isCurrentRole = user.getUserId().equals(po.getProjectOwnerId());
			}else if("DRAFT".equals(po.getProjectStatusCode())) {
				isCurrentRole = user.getUserId().equals(po.getProjectPreparerId());
			}else if("PENDING_HOSP_IC".equals(po.getProjectStatusCode())) {
				isCurrentRole = user.getUserId().equals(po.getFinHospIcId());
			}
			else {
				//isCurrentRole = securitySvc.userHasRole(user, route.getFromRole());
			}
			
			if (isCurrentRole) {
				webVo.setRouteFields(route);
			}
		}
		return webVo;
	}

	@Transactional(rollbackFor={Exception.class})
	@RequestMapping(value="/project/review", method=RequestMethod.POST)
	public ModelAndView performUpdate(@ModelAttribute ProjectReviewVo view, HttpServletRequest request) throws Exception {
		try {
			System.out.println("formAction:" + view.getFormAction());
			String formAction = view.getFormAction();
			
			// Get the user name from cookie
			String userId = this.getSessionUser(request).getUserId();
			UserPo user = securitySvc.findUser(userId);
	        user.setCurrentRole((String)request.getSession().getAttribute("currentRole"));
	        EClaimLogger.debug("Starting Proejct Review Form Submit");
			EClaimLogger.info("view.getProjectId(): " + view.getProjectId());
	
			if (view.getProjectId() == null || "".equals(view.getProjectId())) {
				view.setDisplayMessage("Record did not has Project ID.");
				view.setUpdateSuccess("N");
				return new ModelAndView("project/projectReview", "formBean", view);
			}

			ProjectPo projectPo = projectSvc.getProjectVerByProjectId(Integer.parseInt(view.getProjectId()),Integer.parseInt(view.getProjectVerId()));

			RequestWorkflowRoutePo route = projectSvc.getRoute(projectPo.getRecType(),projectPo.getProjectStatusCode());
			RequestWorkflowHistoryPo history = new RequestWorkflowHistoryPo();

			if("RETURN".equals(formAction)) {
				System.out.println("Update ProjectStatusCode From: " + projectPo.getProjectStatusCode() + " To " + route.getReturnStatus());
			}else {
				System.out.println("Update ProjectStatusCode From: " + projectPo.getProjectStatusCode() + " To " + route.getTargetStatus());
			}
			// Update project status by Get the next status from Route table
			projectPo.setProjectStatusCode("RETURN".equals(formAction) ? route.getReturnStatus() : route.getTargetStatus());
			if("PENDING_HOSP_IC".equals(projectPo.getProjectStatusCode())) {
				projectPo.setFinHospIc(view.getFinIcName());
				projectPo.setFinHospIcId(view.getFinIcId());
				
				
				for(ProjectFinImplVo vo : view.getFinImplList()) {
					ProjectJobPo jobPo = jobSvc.getProjectJobByjobGroupId(Integer.parseInt(vo.getJobGroupId()));
					jobPo.setCoaAnalytic(vo.getAnalyticCOA());
					jobPo.setCoaFund(vo.getFundCOA());
					jobPo.setCoaInst(vo.getInstCOA());
					jobPo.setCoaSection(vo.getSectionCOA());
					jobSvc.update(jobPo, user);
				}
			}
			
			projectSvc.update(projectPo, user);

			// insert the workflow history
			history.setProjectId(projectPo.getProjectId());
			history.setProjectVersionId(projectPo.getProjectVerId());
			history.setActionBy(user.getUserId());
			history.setActionRoleId(route.getFromRole());
			history.setActionTaken("RETURN".equals(formAction) ? "Return" : route.getActionTaken());
			history.setActionByName(user.getUserName());
			history.setActionDate(new Date());
			projectSvc.insertHistory(history, user);

			// Generate Email
			RequestEmailPo email = new RequestEmailPo();
			email.setProjectId(projectPo.getProjectId());
			email.setProjectVersionId(projectPo.getProjectVerId());
			email.setEmailTo(view.getEmailTo() != null ? view.getEmailTo() : "");
			email.setEmailCc(view.getEmailCC() != null ? view.getEmailCC() : "");
			email.setTitle(view.getEmailTitle());
			email.setContent(view.getEmailContent());
			email.setSuppInfo(view.getEmailSuppInfo());
			email.setSentInd("N");
			projectSvc.insertEmail(email, user);

			//Send Email
			commonSvc.performSendEmail(projectPo.getProjectId());

			// Update Approval History
			ApprovalHistoryPo apprHistPo = new ApprovalHistoryPo();
			apprHistPo.setProjectId(projectPo.getProjectId());
			apprHistPo.setProjectVerId(projectPo.getProjectVerId());
			apprHistPo.setActionDate(new Date());
			apprHistPo.setActionCode("RETURN".equals(formAction) ? formAction : route.getActionTaken());
			apprHistPo.setRoleCode(route.getFromRole());
			apprHistPo.setRemark(view.getRemark());
			apprHistPo.setActionBy(user.getUserId());
			apprHistPo.setRecState("A");
			projectSvc.insertApprHist(apprHistPo, user);

			//Upload File
			if (view.getUploadFileId() != null) {
				System.out.println("uploadFileId: " + view.getUploadFileId());
				for (String tmpFileId : view.getUploadFileId()) {
					if (tmpFileId.trim().length() < 1) {
						continue;
					}
					docSvc.uploadFileFromTempFile(Integer.parseInt(tmpFileId), projectPo.getProjectId(),
							projectPo.getProjectVerId(), user);
				}
			}
			//Get next route after updated status code
			view = GetProejctReviewVo(projectPo, user);
			
			view.setDisplayMessage("Record update success.");
			view.setUpdateSuccess("Y");
			
			System.out.println("Next Action: " + view.getActionButton());
			System.out.println("Return Action: " + view.getReturnButton());
			
			return new ModelAndView("project/projectReview", "formBean", view);
		}
		catch (Exception ex) {
			EClaimLogger.error("performUpdate Exception", ex);
			view.setDisplayMessage("Record update fail.");
			view.setUpdateSuccess("N");
			
			return new ModelAndView("project/projectReview", "formBean", view);
		}
	}
	
}

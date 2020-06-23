package hk.org.ha.eclaim.controller.project;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import hk.org.ha.eclaim.bs.project.po.ProjectPublishJobPo;
import hk.org.ha.eclaim.bs.project.po.ProjectPublishPo;
import hk.org.ha.eclaim.bs.project.po.ProjectPublishRankPo;
import hk.org.ha.eclaim.bs.project.svc.IProjectSvc;
import hk.org.ha.eclaim.core.controller.BaseController;
import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.model.project.ProjectIncludedInvitationVo;
import hk.org.ha.eclaim.model.project.ProjectInvitationWebVo;

@Controller
public class PublishController<JobGroupRankVo> extends BaseController {

	@Autowired
	IProjectSvc projectSvc;

	@RequestMapping(value = "/invitation", method = RequestMethod.POST)
	public ModelAndView home(@ModelAttribute("projectId") int projectId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ProjectInvitationWebVo webVo = new ProjectInvitationWebVo();
		ProjectIncludedInvitationVo invitationVo = new ProjectIncludedInvitationVo();

		List<?> jobGroups = projectSvc.getProjectInvitationJobGroup(projectId);

		@SuppressWarnings("unchecked")
		List<Object[]> jobGroupResult = (List<Object[]>)jobGroups;
		for (Object[] o : jobGroupResult) {
			invitationVo = new ProjectIncludedInvitationVo();
			invitationVo.setJobGroupId(((BigDecimal) o[0]).intValueExact());
			invitationVo.setJobs(o[1].toString());
			webVo.getIncludedInvitations().add(invitationVo);
		}

		webVo.setProjectId(projectId);

		return new ModelAndView("project/projectInvitation", "formBean", webVo);
	}

	@RequestMapping(value = "/invitation/publish", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String publish(@RequestBody String publishReq) throws Exception {

		Date updateDate = new Date();
		ObjectMapper objectMapper = new ObjectMapper();
		ProjectInvitationWebVo pubReq = new ProjectInvitationWebVo();
		try {
			pubReq = objectMapper.readValue(publishReq, ProjectInvitationWebVo.class);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

		if (pubReq.getIncludedInvitations().size() < 0) {
			throw new Exception("No job invitation exist in the request");
		}

		// Publish Project
		ProjectPublishPo pp = new ProjectPublishPo();
		pp.setProjectId(pubReq.getProjectId());
		pp.setProjectVerId(1);
		pp.setTargetApplicant(pubReq.getTargetApp());
		pp.setOtherInfo(pubReq.getOthInfo());
		pp.setPublishDate(updateDate);
		pp.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
		pp.setCreatedBy("SYSTEM");
		pp.setCreatedRoleId("SYSTEM");
		pp.setCreatedDate(updateDate);
		pp.setUpdatedBy("SYSTEM");
		pp.setUpdatedRoleId("SYSTEM");
		pp.setUpdatedDate(updateDate);

		List<ProjectPublishJobPo> lstPpJob = new ArrayList<ProjectPublishJobPo>();
		List<ProjectPublishRankPo> lstPpRank = new ArrayList<ProjectPublishRankPo>();
		ProjectPublishRankPo ppRank = new ProjectPublishRankPo();
		ProjectPublishJobPo ppJob = new ProjectPublishJobPo();

		for (ProjectIncludedInvitationVo invitation : pubReq.getIncludedInvitations()) {
			// Publish Project Rank
			ppRank = new ProjectPublishRankPo();
			ppRank.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
			ppRank.setCreatedBy("SYSTEM");
			ppRank.setCreatedRoleId("SYSTEM");
			ppRank.setCreatedDate(updateDate);
			ppRank.setUpdatedBy("SYSTEM");
			ppRank.setUpdatedRoleId("SYSTEM");
			ppRank.setUpdatedDate(updateDate);
			ppRank.setRank(invitation.getJobs());

			// Publish Project Job
			ppJob = new ProjectPublishJobPo();
			ppJob.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
			ppJob.setCreatedBy("SYSTEM");
			ppJob.setCreatedRoleId("SYSTEM");
			ppJob.setCreatedDate(updateDate);
			ppJob.setUpdatedBy("SYSTEM");
			ppJob.setUpdatedRoleId("SYSTEM");
			ppJob.setUpdatedDate(updateDate);
			ppJob.setJobGroupId(invitation.getJobGroupId());

			ppRank.setPublishJobs(ppJob);
			ppJob.setProjectPublishPo(pp);
			
			lstPpJob.add(ppJob);
			lstPpRank.add(ppRank);
		}

		projectSvc.insertProjectInvitation(pp, lstPpJob, lstPpRank);

		return "./home/home";
	}
}

package hk.org.ha.eclaim.controller.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import hk.org.ha.eclaim.bs.cs.svc.ICommonSvc;
import hk.org.ha.eclaim.bs.project.po.ProjectJobPo;
import hk.org.ha.eclaim.bs.project.po.ProjectJobRankPo;
import hk.org.ha.eclaim.bs.project.po.ProjectPo;
import hk.org.ha.eclaim.bs.project.svc.IProjectJobSvc;
import hk.org.ha.eclaim.bs.project.svc.IProjectSvc;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.svc.ISecuritySvc;
import hk.org.ha.eclaim.core.controller.BaseController;
import hk.org.ha.eclaim.core.logger.EClaimLogger;
import hk.org.ha.eclaim.model.maintenance.UserVo;
import hk.org.ha.eclaim.model.project.ProjectJobDetailsVo;
import hk.org.ha.eclaim.model.project.ProjectJobVo;

@Controller
public class ProjectJobDetailsController extends BaseController {
	
	@Autowired
	IProjectSvc projectSvc;
	
	@Autowired
	IProjectJobSvc jobSvc;
	
	@Autowired
	ICommonSvc commonSvc;
	
	@Autowired
	ISecuritySvc securitySvc;

	private static int NextProjectStep = 3;
	
	@RequestMapping(value="/project/jobDetails", method=RequestMethod.GET)
	public ModelAndView initList(HttpServletRequest request,
			@RequestParam(required=false) String projectId, String verId) throws Exception {
		// Get the document id
		EClaimLogger.info("ProjectId: " + projectId);
		ProjectJobDetailsVo webVo = new ProjectJobDetailsVo();
		
		ProjectPo po = null;
		if (projectId == null) {
			po = new ProjectPo();
		}
		else {
			List<ProjectJobPo> jobPoList = new ArrayList<ProjectJobPo>();
			if(StringUtils.isNumeric(verId) && !"".equals(verId)) {
				po = projectSvc.getProjectVerByProjectId(Integer.parseInt(projectId), Integer.parseInt(verId));
				jobPoList = jobSvc.getProjectJobListByProjectId(Integer.parseInt(projectId), Integer.parseInt(verId));
			}else {
				po = projectSvc.getProjectByProjectId(Integer.parseInt(projectId));
				jobPoList = jobSvc.getProjectJobListByProjectId(Integer.parseInt(projectId));
			}
			
			webVo.setProjectId(po.getProjectId().toString());
			webVo.setProjectVerId(po.getProjectVerId().toString());
			webVo.setProjectStatus(po.getProjectStatusCode());
			webVo.setRecType(po.getRecType());
			webVo.setProjectStep(po.getProjectStep().toString());
			
			List<ProjectJobVo> jobVoList = new ArrayList<ProjectJobVo>();
			
			for(ProjectJobPo jobPo :jobPoList) {
				ProjectJobVo vo = new ProjectJobVo();
				vo.setProjectId(jobPo.getProjectId().toString());
				vo.setProjectVerId(jobPo.getProjectVerId().toString());
				vo.setJobGroupId(jobPo.getJobGroupId().toString());
				vo.setStaffGrp(jobPo.getStaffGroup());
				vo.setCoOrdinator(jobPo.getProjectCoordinator());
				vo.setCoOrdinatorId(jobPo.getProjectCoordinatorId());
				vo.setDescription(jobPo.getJobDescription());
				vo.setTargerApp(jobPo.getTargerApplicant());
				vo.setOtherInfo(jobPo.getOtherInformation());
				vo.setQuota(jobPo.getQuota() == null ? "" : jobPo.getQuota().toString());
				vo.setDuration(jobPo.getDuration() == null ? "" : jobPo.getDuration().toString());
				vo.setSessionDay(jobPo.getSessionPerDay() == null ? "" : jobPo.getSessionPerDay().toString());
				vo.setSessionHour(jobPo.getHoursPerSession().toString());
				vo.setTotalHour(jobPo.getTotalHour().toString());
				vo.setStatus("OPEN");
				
				Map<String, String> jobRankList = jobSvc.getJobRankList(jobPo.getStaffGroup());
				vo.setJobRankList(jobRankList);
				
				List<ProjectJobRankPo> jobRankPoList = jobSvc.getJobRankPoList(jobPo.getJobGroupId());
				String[] jobRanks = jobRankPoList.stream()
					    .map(jobRank -> jobRank.getRank())
					    .toArray(String[]::new);
				vo.setJobRank(jobRanks);
				vo.setJobRanks(String.join(",", jobRanks));
				String higherRank = jobSvc.getHigherRank(jobRanks.length > 0 ? jobRanks[0] : null, jobRanks.length > 1 ? jobRanks[1] : null,
						jobRanks.length > 2 ? jobRanks[2] : null, jobRanks.length > 3 ? jobRanks[3] : null,
								jobRanks.length > 4 ? jobRanks[4] : null);
				vo.setMaxGradeVal(jobSvc.getMaxGradeVal(higherRank).toString());
				jobVoList.add(vo);
			}
			if(jobVoList.size() == 0) {
				ProjectJobVo newJobVo = new ProjectJobVo();
				newJobVo.setProjectId(po.getProjectId().toString());
				newJobVo.setProjectVerId(po.getProjectVerId().toString());
				newJobVo.setStatus("NEW");
				jobVoList.add(newJobVo);
			}
			
			webVo.setJobList(jobVoList);
		}
		
		return new ModelAndView("project/projectJobDetails", "formBean", webVo);
	}

	@RequestMapping(value="/project/jobDetails", method=RequestMethod.POST)
	public ModelAndView performUpdate(@ModelAttribute ProjectJobDetailsVo view,
			                          HttpServletRequest request) throws Exception {
		try {
			// Get the user name from cookie
			String userId = this.getSessionUser(request).getUserId();
			UserPo user = securitySvc.findUser(userId);
			
	        user.setCurrentRole((String)request.getSession().getAttribute("currentRole"));
	        EClaimLogger.debug("Starting Proejct JobDetails Form Submit");
			EClaimLogger.info("view.getProjectId(): " + view.getProjectId());
	
			if (view.getProjectId() == null || "".equals(view.getProjectId())) {
				view.setDisplayMessage("Record did not has Project ID.");
				view.setUpdateSuccess("N");
				return new ModelAndView("project/projectJobDetails", "formBean", view);
			}
			else {
				// Check last update date
				ProjectPo projectPo = projectSvc.getProjectVerByProjectId(Integer.parseInt(view.getProjectId()),Integer.parseInt(view.getProjectVerId()));
				if(Integer.parseInt(view.getProjectStep()) < NextProjectStep) {
					projectPo.setProjectStep(NextProjectStep);      
				}
				
				for(ProjectJobVo jobVo : view.getJobList()) {
					if(jobVo.getStaffGrp() == null || jobVo.getStaffGrp().trim().length() < 1) {
						EClaimLogger.error("Job Detail Row without StaffGroup");
						continue;
					}
					if(jobVo.getJobRank() == null || jobVo.getJobRank().length < 1) {
						EClaimLogger.error("Job Detail Row without JobRank");
						continue;
					}
					
						
					if(StringUtils.isBlank(jobVo.getJobGroupId())) {
						ProjectJobPo jobPo = new ProjectJobPo();
						jobPo.setProjectId(projectPo.getProjectId());
						jobPo.setProjectVerId(projectPo.getProjectVerId());
						jobPo.setStaffGroup(jobVo.getStaffGrp());
						jobPo.setProjectCoordinatorId(jobVo.getCoOrdinatorId());
						jobPo.setProjectCoordinator(jobVo.getCoOrdinator());
						jobPo.setQuota(StringUtils.isNotBlank(jobVo.getQuota()) ? Integer.parseInt(jobVo.getQuota()) : null);
						jobPo.setDuration(StringUtils.isNotBlank(jobVo.getDuration()) ? Integer.parseInt(jobVo.getDuration()) : null);
						jobPo.setSessionPerDay(StringUtils.isNotBlank(jobVo.getSessionDay()) ? Integer.parseInt(jobVo.getSessionDay()) : null);
						jobPo.setHoursPerSession(Integer.parseInt(jobVo.getSessionHour()));
						jobPo.setTotalHour(Integer.parseInt(jobVo.getTotalHour()));
						jobPo.setJobDescription(jobVo.getDescription());
						jobPo.setOtherInformation(jobVo.getOtherInfo());
						jobPo.setTargerApplicant(jobVo.getTargerApp());
						jobPo.setRecState("A");
						int jobGrpId = jobSvc.insert(jobPo, user);
						
						for(String jobRank : jobVo.getJobRank()) {
							ProjectJobRankPo jobRankPo = new ProjectJobRankPo();
							jobRankPo.setJobGroupId(jobGrpId);
							jobRankPo.setRank(jobRank);
							jobSvc.insertJobRank(jobPo, jobRankPo, user);
						}
					}else {
						ProjectJobPo jobPo = jobSvc.getProjectJobByjobGroupId(Integer.parseInt(jobVo.getJobGroupId()));
						List<ProjectJobRankPo> jobRankPoList = jobSvc.getJobRankPoList(jobPo.getJobGroupId());
						
						if(jobVo.getStatus().equals("DELETE")) {
							jobSvc.deleteProjectJob(jobPo, user.getUserName(), user.getCurrentRole());
							continue;
						}
						jobPo.setStaffGroup(jobVo.getStaffGrp());
						jobPo.setProjectCoordinatorId(jobVo.getCoOrdinatorId());
						jobPo.setProjectCoordinator(jobVo.getCoOrdinator());
						jobPo.setQuota(StringUtils.isNotBlank(jobVo.getQuota()) ? Integer.parseInt(jobVo.getQuota()) : null);
						jobPo.setDuration(StringUtils.isNotBlank(jobVo.getDuration()) ? Integer.parseInt(jobVo.getDuration()) : null);
						jobPo.setSessionPerDay(StringUtils.isNotBlank(jobVo.getSessionDay()) ? Integer.parseInt(jobVo.getSessionDay()) : null);
						jobPo.setHoursPerSession(Integer.parseInt(jobVo.getSessionHour()));
						jobPo.setTotalHour(Integer.parseInt(jobVo.getTotalHour()));
						jobPo.setJobDescription(jobVo.getDescription());
						jobPo.setOtherInformation(jobVo.getOtherInfo());
						jobPo.setTargerApplicant(jobVo.getTargerApp());
						
						Map<String,String> jobRankMap = new HashMap<String, String>();
						for(String jobRank :jobVo.getJobRank()) {
							if(jobRankPoList.stream().anyMatch(job->job.getRank().equals(jobRank))) {
								jobRankMap.put(jobRank, jobRank);
							}else {
								ProjectJobRankPo jobRankPo = new ProjectJobRankPo();
								jobRankPo.setJobGroupId(jobPo.getJobGroupId());
								jobRankPo.setRank(jobRank);
								jobSvc.insertJobRank(jobPo, jobRankPo, user);
							}
						}
						jobRankPoList.stream().filter(job-> !jobRankMap.containsKey(job.getRank())).forEach(j -> jobSvc.deleteJobRank(j));
						
						jobSvc.update(jobPo, user);
					}
				}
				projectSvc.update(projectPo, user);
			}
	
			view.setDisplayMessage("Record update success.");
			view.setUpdateSuccess("Y");
			
			Map<String, String> parms = new HashMap<String,String>();
			parms.put("projectId", view.getProjectId());
			parms.put("verId", view.getProjectVerId());
			
			return new ModelAndView("redirect:/project/projectCircum",parms);
		}
		catch (Exception ex) {
			EClaimLogger.error("performUpdate Exception", ex);
			view.setDisplayMessage("Record update fail.");
			view.setUpdateSuccess("N");
			
			return new ModelAndView("project/projectJobDetails", "formBean", view);
		}
	}
	
	@RequestMapping(value="/project/getCoOrdinatorList", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> getCoOrdinatorList(@RequestParam String term) {
		List<UserPo> users = null; //securitySvc.searchUserByName(term);
		return users.stream().collect(Collectors.toMap(UserPo :: getUserId, UserPo :: getUserName));
	}
	
	@ModelAttribute("staffGrpList")
	public Map<String, String> getStaffGroupList() {
		Map<String, String> list = jobSvc.getStaffGroupList();
		return list;
	}
	
	@RequestMapping(value="/project/getJobRankList", method = RequestMethod.GET)
	public @ResponseBody Map<String,String> getJobRankList(@RequestParam String staffGrp){
		return jobSvc.getJobRankList(staffGrp);
	}
	
	@RequestMapping(value="/project/getMaxVal", method = RequestMethod.GET)
	public @ResponseBody Integer getMaxGradeVal(@RequestParam(required=false) String rank1, 
			String rank2,String rank3, String rank4, String rank5) {
		String higherRank = jobSvc.getHigherRank(rank1, rank2, rank3, rank4, rank5);
		return jobSvc.getMaxGradeVal(higherRank);
	}
}

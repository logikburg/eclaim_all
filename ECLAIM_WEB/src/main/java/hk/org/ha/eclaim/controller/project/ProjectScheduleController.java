package hk.org.ha.eclaim.controller.project;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import hk.org.ha.eclaim.bs.cs.svc.ICommonSvc;
import hk.org.ha.eclaim.bs.project.po.ProjectPo;
import hk.org.ha.eclaim.bs.project.po.ProjectSchedulePo;
import hk.org.ha.eclaim.bs.project.po.SessionPatternPo;
import hk.org.ha.eclaim.bs.project.svc.IProjectScheduleSvc;
import hk.org.ha.eclaim.bs.project.svc.IProjectSvc;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.svc.ISecuritySvc;
import hk.org.ha.eclaim.core.controller.BaseController;
import hk.org.ha.eclaim.core.logger.EClaimLogger;
import hk.org.ha.eclaim.model.project.ProjectScheduleInfoVo;
import hk.org.ha.eclaim.model.project.ProjectScheduleVo;

@Controller
public class ProjectScheduleController extends BaseController {

	@Autowired
	IProjectSvc projectSvc;
	
	@Autowired
	IProjectScheduleSvc scheduleSvc;
	
	@Autowired
	ICommonSvc commonSvc;
	
	@Autowired
	ISecuritySvc securitySvc;
	
	private static int ProjectStep = 5;
//	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:ii");
	
	@RequestMapping(value="/project/scheduleInfo", method=RequestMethod.GET)
	public ModelAndView initList(HttpServletRequest request,
			@RequestParam(required=false) String projectId) throws Exception {
		// Get the document id
		EClaimLogger.info("ProjectId: " + projectId);
		ProjectScheduleInfoVo webVo = new ProjectScheduleInfoVo();
		
		ProjectPo po = null;
		if (projectId == null) {
			po = new ProjectPo();
		}
		else {
			po = projectSvc.getProjectByProjectId(Integer.parseInt(projectId));
			webVo.setProjectId(po.getProjectId().toString());
			webVo.setProjectVerId(po.getProjectVerId().toString());
			webVo.setProjectStep(po.getProjectStep().toString());
			webVo.setStartDate(po.getFromDate());
			webVo.setEndDate(po.getToDate());
			webVo.setIs7x24("Y".equals(po.getNoScheduleFlag()) ? true : false);
			webVo.setAttendance("SYS");
			List<ProjectScheduleVo> scheduleVoList = new ArrayList<ProjectScheduleVo>();
			
			List<ProjectSchedulePo> schedulePoList = scheduleSvc.getProjectScheduleListByProjectId(Integer.parseInt(projectId));
			for(ProjectSchedulePo schedulePo : schedulePoList) {
				ProjectScheduleVo vo = new ProjectScheduleVo();
				vo.setProjectId(schedulePo.getProjectId().toString());
				vo.setProjectVerId(schedulePo.getProjectVerId().toString());
				vo.setScheduleId(schedulePo.getProjectScheduleId().toString());
//				vo.setPatternCode(schedulePo.getPatternCode());
				vo.setStartTime(schedulePo.getStartTime());
				vo.setEndTime(schedulePo.getEndTime());
				scheduleVoList.add(vo);
			}
			if(scheduleVoList.size() == 0) {
				ProjectScheduleVo newJobVo = new ProjectScheduleVo();
				newJobVo.setProjectId(po.getProjectId().toString());
				newJobVo.setProjectVerId(po.getProjectVerId().toString());
				newJobVo.setStatus("NEW");
				scheduleVoList.add(newJobVo);
			}
			
			webVo.setScheduleList(scheduleVoList);
		}
		return new ModelAndView("project/projectSchedule", "formBean", webVo);
	}

	@RequestMapping(value="/project/scheduleInfo", method=RequestMethod.POST)
	public ModelAndView performUpdate(@ModelAttribute ProjectScheduleInfoVo view,
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
				ProjectPo projectPo = projectSvc.getProjectByProjectId(Integer.parseInt(view.getProjectId()));
				projectPo.setFromDate(view.getStartDate());
				projectPo.setToDate(view.getEndDate());
				projectPo.setNoScheduleFlag(view.getIs7x24() ? "Y" : "N");
				if(Integer.parseInt(view.getProjectStep()) < ProjectStep) {
					projectPo.setProjectStep(ProjectStep);      
				}
				
				if(view.getScheduleList() != null) {
					for(ProjectScheduleVo scheduleVo : view.getScheduleList()) {
						if(scheduleVo.getPatternCode() == null) {
							continue;
						}
						if(scheduleVo.getStartTime() == null || scheduleVo.getEndTime() == null) {
							continue;
						}
						if(StringUtils.isBlank(scheduleVo.getScheduleId())) {
							ProjectSchedulePo schedulePo = new ProjectSchedulePo();
							schedulePo.setProjectId(projectPo.getProjectId());
							schedulePo.setProjectVerId(projectPo.getProjectVerId());
//							schedulePo.setPatternCode(scheduleVo.getPatternCode());
							schedulePo.setStartTime(scheduleVo.getStartTime());
							schedulePo.setEndTime(scheduleVo.getEndTime());
							schedulePo.setRecState("A");
							scheduleSvc.insert(schedulePo, user);
						}else {
							ProjectSchedulePo schedulePo = scheduleSvc.getProjectScheduleByScheduleId(Integer.parseInt(scheduleVo.getScheduleId()));
							
							if(scheduleVo.getStatus().equals("DELETE")) {
								scheduleSvc.deleteProjectSchedule(schedulePo.getProjectScheduleId());
								break;
							}
//							schedulePo.setPatternCode(scheduleVo.getPatternCode());
							schedulePo.setStartTime(scheduleVo.getStartTime());
							schedulePo.setEndTime(scheduleVo.getEndTime());
							scheduleSvc.update(schedulePo, user);
						}
					}
				}
				
				projectSvc.update(projectPo, user);
				
			}
	
			view.setDisplayMessage("Record update success.");
			view.setUpdateSuccess("Y");
//			return new ModelAndView("project/projectSchedule", "formBean", view);
			return new ModelAndView("redirect:/project/quantifiable","projectId", view.getProjectId());
		}
		catch (Exception ex) {
			EClaimLogger.error("performUpdate Exception", ex);
			view.setDisplayMessage("Record update fail.");
			view.setUpdateSuccess("N");
			
			return new ModelAndView("project/projectSchedule", "formBean", view);
		}
	}
	
	@ModelAttribute("patternList")
	public List<SessionPatternPo> getPatternList() {
		List<SessionPatternPo> list = scheduleSvc.getPatternList();
		return list;
	}
	
}

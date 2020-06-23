package hk.org.ha.eclaim.controller.project;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import hk.org.ha.eclaim.bs.cs.po.OrganizationPo;
import hk.org.ha.eclaim.bs.cs.svc.ICommonSvc;
import hk.org.ha.eclaim.bs.project.po.ProgramTypePo;
import hk.org.ha.eclaim.bs.project.po.ProjectPo;
import hk.org.ha.eclaim.bs.project.po.ProjectSchedulePo;
import hk.org.ha.eclaim.bs.project.po.SessionPatternPo;
import hk.org.ha.eclaim.bs.project.svc.IProjectScheduleSvc;
import hk.org.ha.eclaim.bs.project.svc.IProjectSvc;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.svc.ISecuritySvc;
import hk.org.ha.eclaim.core.controller.BaseController;
import hk.org.ha.eclaim.core.logger.EClaimLogger;
import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.model.project.ProjectGeneralInfoVo;
import hk.org.ha.eclaim.model.project.ProjectScheduleVo;

@Controller
public class NewProjectController extends BaseController {

	@Autowired
	IProjectSvc projectSvc;
	
	@Autowired
	ICommonSvc commonSvc;
	
	@Autowired
	ISecuritySvc securitySvc;
	
	@Autowired
	IProjectScheduleSvc scheduleSvc;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	@RequestMapping(value="/project/newProject",produces="text/plain;charset=UTF-8", method=RequestMethod.GET)
	public ModelAndView initList(HttpServletRequest request, 
			@RequestParam(required=false) String projectId, String verId) throws Exception {
		
		EClaimLogger.info("newProject - projectId: " + projectId);
		
		ProjectPo projectPo = null;
		ProjectGeneralInfoVo infoVo = new ProjectGeneralInfoVo();
		List<ProjectSchedulePo> schedulePoList = new ArrayList<ProjectSchedulePo>();
		List<ProjectScheduleVo> scheduleVoList = new ArrayList<ProjectScheduleVo>();
		List<ProjectScheduleVo> scheduleDateList = new ArrayList<ProjectScheduleVo>();
		if (projectId != null && projectId.trim().length() > 0) {
			if(verId != null && verId.trim().length() > 0) {
				projectPo = projectSvc.getProjectVerByProjectId(Integer.parseInt(projectId),Integer.parseInt(verId));
				schedulePoList = scheduleSvc.getProjectScheduleListByProjectId(Integer.parseInt(projectId),Integer.parseInt(verId));
			}else {
				projectPo = projectSvc.getProjectByProjectId(Integer.parseInt(projectId));
				schedulePoList = scheduleSvc.getProjectScheduleListByProjectId(Integer.parseInt(projectId));
			}
			infoVo = convertToVo(projectPo);
			
			for (ProjectSchedulePo schedulePo : schedulePoList) {
				ProjectScheduleVo vo = new ProjectScheduleVo();
				vo.setProjectId(schedulePo.getProjectId().toString());
				vo.setProjectVerId(schedulePo.getProjectVerId().toString());
				vo.setScheduleId(schedulePo.getProjectScheduleId().toString());
				vo.setPatternCode(schedulePo.getPatternCode() != null ? schedulePo.getPatternCode().split(",") : null);
				vo.setStartTime(schedulePo.getStartTime());
				vo.setEndTime(schedulePo.getEndTime());
				vo.setScheduleDate(schedulePo.getScheduleDate() != null ? sdf.format(schedulePo.getScheduleDate()) : null);
				if(schedulePo.getPatternCode() != null) {
					scheduleVoList.add(vo);
				}else {
					scheduleDateList.add(vo);
				}
			}
		} else {
			String userId = this.getSessionUser(request).getUserId();
			UserPo user = securitySvc.findUser(userId);
			infoVo.setPreparer(user.getUserName());
			infoVo.setPreparerId(user.getUserId());
			//infoVo.setDepartmentId(Integer.toString(user.getDepartmentId()));
			infoVo.setProjectId("");
			infoVo.setProjectVerId("0");
			infoVo.setProjectType("SHS");
			infoVo.setProjectStep("1");
			infoVo.setProjectStatus("DRAFT");
			infoVo.setRecType("PROJ_INIT");
		}

		if (scheduleVoList.size() == 0) {
			ProjectScheduleVo newScheduleVo = new ProjectScheduleVo();
			newScheduleVo.setProjectId(infoVo.getProjectId());
			newScheduleVo.setProjectVerId(infoVo.getProjectVerId());
			newScheduleVo.setStatus("NEW");
			scheduleVoList.add(newScheduleVo);
		}
		if (scheduleDateList.size() == 0) {
			ProjectScheduleVo newScheduleVo = new ProjectScheduleVo();
			newScheduleVo.setProjectId(infoVo.getProjectId());
			newScheduleVo.setProjectVerId(infoVo.getProjectVerId());
			newScheduleVo.setStatus("NEW");
			scheduleDateList.add(newScheduleVo);
		}
		infoVo.setScheduleList(scheduleVoList);
		infoVo.setScheduleDateList(scheduleDateList);
		
		return new ModelAndView("project/projectGeneralInfo", "formBean", infoVo);
	}
	@RequestMapping(value="/project/enterProject", method=RequestMethod.GET)
	public ModelAndView enterProject(HttpServletRequest request, 
			@RequestParam(required=false) String projectId, String verId) throws Exception {
		Map<String, String> parms = new HashMap<String,String>();
		parms.put("projectId", projectId);
		parms.put("verId", verId);
		
		if(StringUtils.isBlank(projectId)) {
			return new ModelAndView("redirect:/project/newProject",parms);
		}
		
		ProjectPo projectPo = projectSvc.getProjectByProjectId(Integer.parseInt(projectId));
		switch (projectPo.getProjectStep()) {
		case 4:
			return new ModelAndView("redirect:/project/review", parms);
		case 3:
			return new ModelAndView("redirect:/project/projectCircum", parms);
		case 2:
			return new ModelAndView("redirect:/project/jobDetails", parms);
		default:
			return new ModelAndView("redirect:/project/newProject", parms);
		}
		
	}
	@Transactional
	@RequestMapping(value="/project/newProject", method=RequestMethod.POST)
	public ModelAndView performUpdate(@ModelAttribute ProjectGeneralInfoVo view,
			                          HttpServletRequest request) throws Exception {
		try {
			List<String> mgsList = new ArrayList<String>();
			String success = "Y";
			
			// Get the user name from cookie
			String userId = this.getSessionUser(request).getUserId();
			UserPo user = securitySvc.findUser(userId);
	        user.setCurrentRole((String)request.getSession().getAttribute("currentRole"));
	        String projectId = view.getProjectId();
	        String verId = view.getProjectVerId();
			System.out.println("view.getProjectId(): " + projectId);
			
			//check project name is unique 
			if(projectSvc.isExistingProjectName(view.getProjectId(), view.getProjectName())) {
				success = "N";
				mgsList.add("Uniqueness Project Name");
			}
			
			//
			OrganizationPo orgPo = commonSvc.getOrgPoByOrgCode(Integer.parseInt(view.getDepartmentId()));
			if(orgPo == null || !orgPo.getName().equals(view.getDepartmentName())) {
				success = "N";
				mgsList.add("Incorrect Department");
			}
			
			if("N".equals(success)) {
				view.setDisplayMessages(mgsList);
				throw new Exception("Record update fail.");
			}
			
			//For New Project
			if (view.getProjectId() == null || "".equals(view.getProjectId())) {
				ProjectPo tmpPo = new ProjectPo();
				tmpPo.setDepartmentId(Integer.parseInt(view.getDepartmentId()));
				tmpPo.setClusterId(commonSvc.getClusIdByOrgId(tmpPo.getDepartmentId()));
//				tmpPo.setSrsDepartment(view.getSrsDepartment());
//				tmpPo.setSrsHospital(view.getSrsHospital());
				tmpPo.setProjectName(view.getProjectName());
				tmpPo.setProjectNameC(view.getProjectNameC());
				tmpPo.setProjectOwnerId(view.getOwnerId());
				tmpPo.setProjectOwner(view.getOwner());
				tmpPo.setProjectPreparerId(view.getPreparerId());
				tmpPo.setProjectPreparer(view.getPreparer());
				tmpPo.setProjectPurpose(view.getPurpose());
				tmpPo.setFromDate(view.getStartDate() != null ? sdf.parse(view.getStartDate()) : null);
				tmpPo.setToDate(view.getEndDate() != null ? sdf.parse(view.getEndDate()) : null);
				tmpPo.setFundingSource(view.getFundingSource());
				tmpPo.setProjectType(view.getProjectType());
				//tmpPo.setExtension(0);
				tmpPo.setRecType("PROJ_INIT");
				tmpPo.setProjectStatusCode("DRAFT");
				tmpPo.setProgramType(view.getProgramType());
				tmpPo.setProjectStep(2);
				tmpPo.setProjectVerId(!"".equals(verId) ? Integer.parseInt(verId) : 0);
				tmpPo.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
				// Create new News
				int newProjectId = projectSvc.insert(tmpPo, user);
				
				projectId = String.valueOf(newProjectId);
			}
			else {
				// for update project
				ProjectPo po = projectSvc.getProjectVerByProjectId(Integer.parseInt(projectId),Integer.parseInt(verId));
				po.setProjectName(view.getProjectName());
				po.setProjectNameC(view.getProjectNameC());
				po.setDepartmentId(Integer.parseInt(view.getDepartmentId()));
				po.setProjectOwnerId(view.getOwnerId());
				po.setProjectOwner(view.getOwner());
				po.setProjectPurpose(view.getPurpose());
//				po.setSrsDepartment(view.getSrsDepartment());
//				po.setSrsHospital(view.getSrsHospital());
				po.setFundingSource(view.getFundingSource());
				po.setFromDate(view.getStartDate() != null ? sdf.parse(view.getStartDate()) : null);
				po.setToDate(view.getEndDate() != null ? sdf.parse(view.getEndDate()) : null);
				po.setProgramType(view.getProgramType());
				projectSvc.update(po, user);
			}
			
			if(view.getScheduleList() != null) {
				UpdateProjectSchedulePo(view.getScheduleList(), user, Integer.parseInt(projectId),Integer.parseInt(verId));
			}
			if(view.getScheduleDateList() != null) {
				UpdateProjectSchedulePo(view.getScheduleDateList(), user, Integer.parseInt(projectId),Integer.parseInt(verId));
			}
			Map<String, String> parms = new HashMap<String,String>();
			parms.put("projectId", projectId);
			parms.put("verId", verId);
			
			view.setDisplayMessage("Record update success.");
			view.setUpdateSuccess("Y");
			return new ModelAndView("redirect:/project/jobDetails",parms);
		}
		catch (Exception ex) {
			EClaimLogger.error("performUpdate:" + ex.getMessage(), ex);
			view.setDisplayMessage("Record update fail.");
			view.setUpdateSuccess("N");
			return new ModelAndView("/project/projectGeneralInfo", "formBean", view);
		}
	}

	private void UpdateProjectSchedulePo(List<ProjectScheduleVo> scheduleList, UserPo user, int projectId, int verId) throws ParseException {
		for(ProjectScheduleVo scheduleVo : scheduleList) {
			if( (scheduleVo.getPatternCode() == null || scheduleVo.getPatternCode().length == 0)
					&& StringUtils.isEmpty(scheduleVo.getScheduleDate())) {
				continue;
			}
			if(scheduleVo.getStartTime() == null || scheduleVo.getEndTime() == null) {
				continue;
			}
			ProjectSchedulePo schedulePo = new ProjectSchedulePo();
			if(!StringUtils.isBlank(scheduleVo.getScheduleId())) {
				schedulePo = scheduleSvc.getProjectScheduleByScheduleId(Integer.parseInt(scheduleVo.getScheduleId()));
				if(scheduleVo.getStatus().equals("DELETE")) {
					scheduleSvc.deleteProjectSchedule(schedulePo.getProjectScheduleId());
					break;
				}
			}
			schedulePo.setPatternCode(scheduleVo.getPatternCode() != null ? String.join(",", scheduleVo.getPatternCode()) : null);
			schedulePo.setStartTime(scheduleVo.getStartTime());
			schedulePo.setEndTime(scheduleVo.getEndTime());
			schedulePo.setScheduleDate(StringUtils.isNotEmpty(scheduleVo.getScheduleDate()) ? sdf.parse(scheduleVo.getScheduleDate()) : null);
			schedulePo.setRecState("A");
			
			if(schedulePo.getProjectId() == null) {
				schedulePo.setProjectId(projectId);
				schedulePo.setProjectVerId(verId);
				scheduleSvc.insert(schedulePo, user);
			}else {
				scheduleSvc.update(schedulePo, user);
			}
		}
	}
	
	@ModelAttribute("deptList")
	public Map<String, String> getDeptList() {
		List<OrganizationPo> organList = commonSvc.getAllActiveOrganization();
		
		Map<String, String> displayDeptList = new LinkedHashMap<String, String>();
		for (int i=0; i<organList.size(); i++) {
			displayDeptList.put(organList.get(i).getOrganizationId().toString(), organList.get(i).getName());
		}
		return displayDeptList;
	}
	
	@ModelAttribute("hospitalSrsList")
	public Map<String, String> getHospitalSrsList() {

		Map<String, String> displayhospitalList = new LinkedHashMap<String, String>();
		displayhospitalList.put("A","SRS Hospital A");
		displayhospitalList.put("B","SRS Hospital B");
		displayhospitalList.put("C","SRS Hospital C");
		
		return displayhospitalList;
	}
	
	@ModelAttribute("deptSrsList")
	public Map<String, String> getDeptSrsList() {

		Map<String, String> displayDeptList = new LinkedHashMap<String, String>();
		displayDeptList.put("A","SRS Department A");
		displayDeptList.put("B","SRS Department B");
		displayDeptList.put("C","SRS Department C");
		
		return displayDeptList;
	}
	
	@ModelAttribute("patternList")
	public List<SessionPatternPo> getPatternList() {
		List<SessionPatternPo> list = scheduleSvc.getPatternList();
		return list;
	}
	
	@ModelAttribute("programTypeList")
	public Map<String, String> getProgramTypeListt() {
		List<ProgramTypePo> typeList = new ArrayList<ProgramTypePo>();
		
		Map<String, String> programTypeList = new LinkedHashMap<String, String>();
		programTypeList.put("", "");
		for(ProgramTypePo type : typeList) {
			programTypeList.put(type.getTypeCode(), type.getTypeDesc());
		}
		
		return programTypeList;
	}
	
	@RequestMapping(value="/project/getProjectDepartmentList", method = RequestMethod.GET)
	public @ResponseBody Map<Integer, String> getProjectDepartmentList(@RequestParam String term) {

		List<OrganizationPo> organList = commonSvc.searchOrganizationByName(term);
		
		return organList.stream().collect(Collectors.toMap(OrganizationPo:: getOrganizationId, OrganizationPo :: getName));
	}
	
	@RequestMapping(value="/project/getUserNameList", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> getUserNameList(@RequestParam String term) {
		
		List<UserPo> users = null;
		return users.stream().collect(Collectors.toMap(UserPo :: getUserId, UserPo :: getUserName));
	}
	
	@RequestMapping(value="/project/getProjectNameList", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> getProjectNameList(@RequestParam String name, String deptId, String[] status) {
		List<ProjectPo> projects = projectSvc.searchProjectByName(name,deptId,status);
		return projects.stream().collect(Collectors.toMap(ProjectPo :: getProjectName, ProjectPo :: getProjectName));
	}
		
	@RequestMapping(value="/project/doProjectAction", method = RequestMethod.GET)
	public @ResponseBody int projectAction(HttpServletRequest request, @RequestParam String projectId, String verId, String action) {
		
		String userId = this.getSessionUser(request).getUserId();
		UserPo user;
		try {
			user = securitySvc.findUser(userId);
//			if(!securitySvc.userHasRole(user, "HR_OFFICER")) {
//				return 2;
//			}
			return projectSvc.processProjectAction(Integer.parseInt(projectId), Integer.parseInt(verId), action, user);
		} catch (Exception e) {
			return 1;
		}
	}
	
	private ProjectGeneralInfoVo convertToVo(ProjectPo po) {
		ProjectGeneralInfoVo vo = new ProjectGeneralInfoVo();
		vo.setProjectId(po.getProjectId().toString());
		vo.setProjectVerId(po.getProjectVerId().toString());
		vo.setProjectName(po.getProjectName());
		vo.setProjectStep(po.getProjectStep().toString());
		vo.setProjectStatus(po.getProjectStatusCode());
		vo.setProjectType(po.getProgramType());
		vo.setRecType(po.getRecType());
//		vo.setCommencementDate(po.getOrgiStartDate() != null ? sdf.format(po.getOrgiStartDate()) : null);
//		vo.setOrgiEndDate(po.getOrgiEndDate() != null ? sdf.format(po.getOrgiEndDate()) : null);
		vo.setDepartmentId(po.getDepartmentId().toString());
		vo.setDepartmentName(po.getDepartmentId() != null ? commonSvc.getOrgNameByOrgCode(po.getDepartmentId()): "");
		vo.setProjectNameC(po.getProjectNameC());
		vo.setPurpose(po.getProjectPurpose());
		vo.setOwner(po.getProjectOwner());
		vo.setOwnerId(po.getProjectOwnerId());
		vo.setPreparer(po.getProjectPreparer());
		vo.setPreparerId(po.getProjectPreparerId());
		vo.setFundingSource(po.getFundingSource());
		vo.setStartDate(po.getFromDate() != null ? sdf.format(po.getFromDate()) : null);
		vo.setEndDate(po.getToDate() != null ? sdf.format(po.getToDate()) : null);
		vo.setProgramType(po.getProgramType());
		return vo;
	}
}

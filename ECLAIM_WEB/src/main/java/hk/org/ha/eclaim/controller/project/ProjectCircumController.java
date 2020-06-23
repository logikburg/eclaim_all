package hk.org.ha.eclaim.controller.project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import hk.org.ha.eclaim.bs.cs.po.CircumstancePo;
import hk.org.ha.eclaim.bs.cs.svc.ICommonSvc;
import hk.org.ha.eclaim.bs.project.po.ProjectCircumPo;
import hk.org.ha.eclaim.bs.project.po.ProjectPo;
import hk.org.ha.eclaim.bs.project.svc.IProjectCircumSvc;
import hk.org.ha.eclaim.bs.project.svc.IProjectDocumentSvc;
import hk.org.ha.eclaim.bs.project.svc.IProjectSvc;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.svc.ISecuritySvc;
import hk.org.ha.eclaim.core.controller.BaseController;
import hk.org.ha.eclaim.core.logger.EClaimLogger;
import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.model.project.ProjectCircumVo;

@Controller
public class ProjectCircumController extends BaseController {

	@Autowired
	IProjectSvc projectSvc;
	
	@Autowired
	IProjectCircumSvc projectCircumSvc;
	
	@Autowired
	IProjectDocumentSvc docSvc;
	
	@Autowired
	ICommonSvc commonSvc;
	
	@Autowired
	ISecuritySvc securitySvc;

	private static int NextProjectStep = 4;

	
	@RequestMapping(value="/project/projectCircum", method=RequestMethod.GET)
	public ModelAndView initList(HttpServletRequest request,
			@RequestParam(required=false) String projectId, String verId) throws Exception {
		// Get the document id
		EClaimLogger.info("ProjectId: " + projectId);
		ProjectCircumVo webVo = new ProjectCircumVo();
		
		ProjectPo po = null;
		if (projectId == null) {
			po = new ProjectPo();
		}
		else {
			if(StringUtils.isNumeric(verId) && !"".equals(verId)) {
				po = projectSvc.getProjectVerByProjectId(Integer.parseInt(projectId), Integer.parseInt(verId));
			}else {
				po = projectSvc.getProjectByProjectId(Integer.parseInt(projectId));
			}
			List<ProjectCircumPo> circumList =projectCircumSvc.getProjectCircumListByProjectId(Integer.parseInt(projectId));
			webVo = convertToVo(po,circumList);
		}
		
		return new ModelAndView("project/projectCircum", "formBean", webVo);
	}

	@RequestMapping(value="/project/projectCircum", method=RequestMethod.POST)
	@Transactional(rollbackFor={Exception.class})
	public ModelAndView performUpdate(@ModelAttribute ProjectCircumVo view,
			                          HttpServletRequest request) throws Exception {
		try {
			// Get the user name from cookie
			String userId = this.getSessionUser(request).getUserId();
			UserPo user = securitySvc.findUser(userId);
	        user.setCurrentRole((String)request.getSession().getAttribute("currentRole"));
	        EClaimLogger.debug("Starting Proejct Circum Form Submit");
			EClaimLogger.info("view.getProjectId(): " + view.getProjectId());
	
			if (view.getProjectId() == null || "".equals(view.getProjectId())) {
				view.setDisplayMessage("Record did not has Project ID.");
				view.setUpdateSuccess("N");
				return new ModelAndView("project/projectCircum", "formBean", view);
			}else if ("".equals(view.getProjectVerId()) || !StringUtils.isNumeric(view.getProjectVerId())) {
				view.setDisplayMessage("Record did not has Project Ver.");
				view.setUpdateSuccess("N");
				return new ModelAndView("project/projectCircum", "formBean", view);
			}
			else {
				// Check last update date
				ProjectPo projectPo = projectSvc.getProjectVerByProjectId(Integer.parseInt(view.getProjectId()),Integer.parseInt(view.getProjectVerId()));
				projectPo.setJustifications(view.getJustifications());
				projectPo.setManpowerSituation(view.getManPowerShortage());
				projectPo.setTriggerPoint(view.getTriggerPoint());
				projectPo.setUseOtaFlag(view.getUsingOTA());
				projectPo.setOtaJustifications("Y".equals(view.getUsingOTA()) ? view.getOtaJustifications() : null);
				projectPo.setqDeliverables(view.getqDeliver());
				if(Integer.parseInt(view.getProjectStep()) < NextProjectStep) {
					projectPo.setProjectStep(NextProjectStep);      
				}
				
				List<ProjectCircumPo> circumPoList = projectCircumSvc.getProjectCircumListByProjectId(projectPo.getProjectId(),projectPo.getProjectVerId());
				
				HashMap<String,String> circumIdMap = new HashMap<String,String>();
				for(String circum : view.getCircumIds()) {
					circumIdMap.put(circum, circum);
				}
				
				for(String circum : view.getCircumIds()) {
					if(!circumPoList.stream().anyMatch(po->circum.equals(po.getCircumstanceId().toString()))) {
						ProjectCircumPo circumPo = new ProjectCircumPo();
						circumPo.setCircumstanceId(Integer.parseInt(circum));
						circumPo.setProjectId(projectPo.getProjectId());
						circumPo.setProjectVerId(projectPo.getProjectVerId());
						circumPo.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
						projectCircumSvc.insert(circumPo, user);
					}
				}
				circumPoList.stream().filter(po -> !circumIdMap.containsKey(po.getCircumstanceId().toString())).forEach(p ->  projectCircumSvc.deleteProjectCircum(p.getProjectCircumUid(),"",""));
				
				if (view.getUploadFileId() != null) {
					
					System.out.println("uploadFileId: "+view.getUploadFileId());

					for(String tmpFileId : view.getUploadFileId()) {
						if(tmpFileId.trim().length() < 1) {
							continue;
						}
						docSvc.uploadFileFromTempFile(Integer.parseInt(tmpFileId), projectPo.getProjectId(),projectPo.getProjectVerId(), user);
					}
				}
				projectSvc.update(projectPo, user);
			}
	
			view.setDisplayMessage("Record update success.");
			view.setUpdateSuccess("Y");
			Map<String, String> parms = new HashMap<String,String>();
			parms.put("projectId", view.getProjectId());
			parms.put("verId", view.getProjectVerId());
			
			return new ModelAndView("redirect:/project/review",parms);
		}
		catch (Exception ex) {
			EClaimLogger.error("performUpdate Exception", ex);
			view.setDisplayMessage("Record update fail.");
			view.setUpdateSuccess("N");
			
			return new ModelAndView("project/projectCircum", "formBean", view);
		}
	}
	
	private ProjectCircumVo convertToVo(ProjectPo po,List<ProjectCircumPo> circumList) {
		ProjectCircumVo webVo = new ProjectCircumVo();
		webVo.setProjectId(po.getProjectId().toString());
		webVo.setProjectVerId(po.getProjectVerId().toString());
		webVo.setProjectStatus(po.getProjectStatusCode());
		webVo.setRecType(po.getRecType());
		webVo.setProjectStep(po.getProjectStep().toString());
		webVo.setJustifications(po.getJustifications());
		webVo.setTriggerPoint(po.getTriggerPoint());
		webVo.setManPowerShortage(po.getManpowerSituation());
		webVo.setOtaJustifications(po.getOtaJustifications());
		webVo.setUsingOTA(po.getUseOtaFlag()!= null ? po.getUseOtaFlag() : "N");
		webVo.setqDeliver(po.getqDeliverables());
		String[] ids = circumList.stream()
			    .map(circum -> circum.getCircumstanceId().toString())
			    .toArray(String[]::new);
		
		webVo.setCircumIds(ids);
		return webVo;
	}
	
	@ModelAttribute("circumList")
	public List<CircumstancePo> getCircumList() {
		List<CircumstancePo> list = commonSvc.getAllActiveCircum();
		return list;
	}
	
}

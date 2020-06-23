package hk.org.ha.eclaim.controller.project;

import java.util.ArrayList;
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

import hk.org.ha.eclaim.bs.cs.dao.impl.CircumstanceDaoImpl;
import hk.org.ha.eclaim.bs.cs.po.CircumstancePo;
import hk.org.ha.eclaim.bs.cs.svc.ICommonSvc;
import hk.org.ha.eclaim.bs.project.po.ProjectCircumPo;
import hk.org.ha.eclaim.bs.project.po.ProjectPo;
import hk.org.ha.eclaim.bs.project.po.ProjectSchedulePo;
import hk.org.ha.eclaim.bs.project.svc.IProjectCircumSvc;
import hk.org.ha.eclaim.bs.project.svc.IProjectScheduleSvc;
import hk.org.ha.eclaim.bs.project.svc.IProjectSvc;
import hk.org.ha.eclaim.bs.project.svc.impl.ProjectCircumSvcImpl;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.svc.ISecuritySvc;
import hk.org.ha.eclaim.core.controller.BaseController;
import hk.org.ha.eclaim.core.logger.EClaimLogger;
import hk.org.ha.eclaim.model.project.ProjectCircumVo;
import hk.org.ha.eclaim.model.project.ProjectQuantifiableDetailVo;
import hk.org.ha.eclaim.model.project.ProjectQuantifiableVo;
import hk.org.ha.eclaim.model.project.ProjectScheduleInfoVo;
import hk.org.ha.eclaim.model.project.ProjectScheduleVo;

@Controller
public class ProjectQuantifiableController extends BaseController {

	@Autowired
	IProjectSvc projectSvc;
	
	@Autowired
	IProjectCircumSvc projectCircumSvc;
	
	@Autowired
	ICommonSvc commonSvc;
	
	@Autowired
	ISecuritySvc securitySvc;

	private static int ProjectStep = 6;
	
	@RequestMapping(value="/project/quantifiable", method=RequestMethod.GET)
	public ModelAndView initList(HttpServletRequest request,
			@RequestParam(required=false) String projectId) throws Exception {
		// Get the document id
		EClaimLogger.info("ProjectId: " + projectId);
		ProjectQuantifiableVo webVo = new ProjectQuantifiableVo();
		
		ProjectPo po = null;
		if (projectId == null) {
			po = new ProjectPo();
		}
		else {
			po = projectSvc.getProjectByProjectId(Integer.parseInt(projectId));
			webVo.setProjectId(po.getProjectId().toString());
			webVo.setProjectVerId(po.getProjectVerId().toString());
			webVo.setProjectStep(po.getProjectStep().toString());
			webVo.setqDeliver(po.getqDeliverables());
//			List<ProjectCircumPo> circumPoList = projectCircumSvc.getProjectCircumListByProjectId(po.getProjectId());
//			List<ProjectQuantifiableDetailVo> detailList = new ArrayList<ProjectQuantifiableDetailVo>();
//			for(ProjectCircumPo circumPo : circumPoList) {
//				CircumstancePo circumstancePo = commonSvc.getCircumPoByCircumId(circumPo.getCircumstanceId());
//				ProjectQuantifiableDetailVo vo = new ProjectQuantifiableDetailVo();
//				vo.setProjectCircumUid(circumPo.getProjectCircumUid().toString());
//				vo.setCircumstanceId(circumPo.getCircumstanceId().toString());
//				vo.setShsParm(circumstancePo.getDescriptionQ());
//				vo.setqDeliver(StringUtils.isNotBlank(circumPo.getqDeliverables()) ? circumPo.getqDeliverables() : circumstancePo.getqDeliverableQuide());
//				vo.setExample(circumstancePo.getqDeliverableExampl());
//				detailList.add(vo);
//			}
			
//			webVo.setDetailsList(detailList);
		}
		
		return new ModelAndView("project/projectQuantifiable", "formBean", webVo);
	}

	@RequestMapping(value="/project/quantifiable", method=RequestMethod.POST)
	public ModelAndView performUpdate(@ModelAttribute ProjectQuantifiableVo view,
			                          HttpServletRequest request) throws Exception {
		try {
			// Get the user name from cookie
			String userId = this.getSessionUser(request).getUserId();
			UserPo user = securitySvc.findUser(userId);
	        user.setCurrentRole((String)request.getSession().getAttribute("currentRole"));
	        EClaimLogger.debug("Starting Proejct Quantifiable Form Submit");
			EClaimLogger.info("view.getProjectId(): " + view.getProjectId());
	
			if (view.getProjectId() == null || "".equals(view.getProjectId())) {
				view.setDisplayMessage("Record did not has Project ID.");
				view.setUpdateSuccess("N");
				return new ModelAndView("project/projectQuantifiable", "formBean", view);
			}
			else {
				// Check last update date
				ProjectPo projectPo = projectSvc.getProjectByProjectId(Integer.parseInt(view.getProjectId()));
				if(Integer.parseInt(view.getProjectStep()) < ProjectStep) {
					projectPo.setProjectStep(ProjectStep);      
				}
				projectPo.setqDeliverables(view.getqDeliver());
				projectSvc.update(projectPo, user);
				
//				for(ProjectQuantifiableDetailVo detail : view.getDetailsList()) {
//					ProjectCircumPo circumPo = projectCircumSvc.getProjectCircumPoByUid(Integer.parseInt(detail.getProjectCircumUid()));
//					circumPo.setqDeliverables(detail.getqDeliver());
//					projectCircumSvc.update(circumPo, user);
//				}
			}
			view.setDisplayMessage("Record update success.");
			view.setUpdateSuccess("Y");
			return new ModelAndView("redirect:/project/review","projectId", view.getProjectId());
		}
		catch (Exception ex) {
			EClaimLogger.error("performUpdate Exception", ex);
			view.setDisplayMessage("Record update fail.");
			view.setUpdateSuccess("N");
			
			return new ModelAndView("project/projectQuantifiable", "formBean", view);
		}
	}
	
}

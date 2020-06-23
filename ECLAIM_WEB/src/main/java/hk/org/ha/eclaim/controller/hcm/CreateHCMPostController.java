package hk.org.ha.eclaim.controller.hcm;

import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import hk.org.ha.eclaim.core.controller.BaseController;
import hk.org.ha.eclaim.model.hcm.CreateHCMPostWebVo;
import hk.org.ha.eclaim.model.hcm.HCMRecordGradeVo;
import hk.org.ha.eclaim.bs.cs.svc.ICommonSvc;
import hk.org.ha.eclaim.bs.hcm.po.CreateHCMPostWebPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMGradePo;
import hk.org.ha.eclaim.bs.hcm.po.HCMLocationPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMProbationDurationUnitPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMRecordGradePo;
import hk.org.ha.eclaim.bs.hcm.po.HCMRecordPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMSourceFundingPo;
import hk.org.ha.eclaim.bs.hcm.svc.IHCMSvc;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.svc.ISecuritySvc;
import hk.org.ha.eclaim.core.helper.DateTimeHelper;
import hk.org.ha.eclaim.core.logger.EClaimLogger;

@Controller
public class CreateHCMPostController extends BaseController {
	@Autowired
	IHCMSvc hcmSvc;
	
	@Autowired
	ICommonSvc commonSvc;
	
	@Autowired
	ISecuritySvc securitySvc;

	@RequestMapping(value="/hcm/createHCMPost", method=RequestMethod.GET)
	public ModelAndView init(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("Location CreateHCMPostController.init()");

		// Get the user name from cookie
		String userId = this.getSessionUser(request).getUserId();
		UserPo user = securitySvc.findUser(userId);
        user.setCurrentRole((String)request.getSession().getAttribute("currentRole"));

		CreateHCMPostWebVo vo = new CreateHCMPostWebVo();
		vo.setUserName(user.getUserName());
		vo.setEffectiveFromDate(DateTimeHelper.formatDateToString(new Date()));
		vo.setUserId(userId);
		return new ModelAndView("hcm/createHCMPost", "formBean", vo);
	}

	@RequestMapping(value="/hcm/createHCMPost", method=RequestMethod.POST)
	public ModelAndView performAction(@ModelAttribute("formBean")CreateHCMPostWebVo createPositionWebVo, 
			HttpServletRequest request) throws Exception {
		// Get the user name from cookie
		String userId = this.getSessionUser(request).getUserId();
		int postId = -1;
		
		try {
			System.out.println("repsonsibilityId: " + createPositionWebVo.getRepsonsibilityId());
			postId = hcmSvc.createHCMPosition(convertVoToPo(createPositionWebVo), userId);
			System.out.println("post Id: " + postId);
			
			// Create new UpdatePositionWebVo
			CreateHCMPostWebVo vo = new CreateHCMPostWebVo();
			vo.setUserId(userId);
			vo.setUpdateSuccess("Y");
			vo.setDisplayMessage("HCM Position create success.");
			
			// Get the hcm record 
			HCMRecordPo hcmRecord = hcmSvc.getHCMRecordByPositionId(postId, createPositionWebVo.getEffectiveFromDate());
			vo.setEffectiveFromDate(hcmRecord.getDateEffectiveStr());
			vo.setStartDate(hcmRecord.getEffectiveStartDateStr());
			vo.setProbationDuration(String.valueOf(hcmRecord.getPorbationPeriod()));
			vo.setProbationDurationUnit(hcmRecord.getProbationPeriodUnitCd());
			// vo.setHcmPostOrganization(hcmRecord.getPositionOrganization());
			vo.setHcmOrganization(String.valueOf(hcmRecord.getOrganizationId()));
			vo.setHcmJob(String.valueOf(hcmRecord.getJobId()));
			
			// vo.setDdHcmOrganization(String.valueOf(hcmRecord.getOrganizationId()));
			vo.setDdHcmPostOrganization(hcmRecord.getPositionOrganization());
			vo.setDdHcmJob(String.valueOf(hcmRecord.getJobName()));
			vo.setDdHcmOrganization(hcmRecord.getOrganizationName());
			vo.setDdHcmPostTitle(hcmRecord.getPositionTitle());
			
			vo.setPostName(hcmRecord.getName());
			vo.setFte(String.valueOf(hcmRecord.getFte()));
			vo.setHeadCount(String.valueOf(hcmRecord.getMaxPerson()));
			vo.setHcmPostTitle(hcmRecord.getPositionTitle());
			vo.setHcmPostOrganization(hcmRecord.getPostOrganizationId());
			
			vo.setHcmUnitTeam(hcmRecord.getUnitTeam());
			vo.setHiringStatus(hcmRecord.getAvailabilityStatus());
			vo.setType(hcmRecord.getPositionType());
			vo.setProposedEndDate(hcmRecord.getHiringStatusPropEndDateStr());
			vo.setPositionId(String.valueOf(postId));
			vo.setDdLocation(hcmRecord.getLocationDesc());
			
			vo.setPositionGroup(hcmRecord.getPositionGroup());
			vo.setSrcFunding(hcmRecord.getSrcFunding());
			// vo.setFromCreateHCMPage("Y");
			
			List<HCMRecordGradePo> poList = hcmSvc.getHCMGradeRecordByPositionId(postId);
			List<HCMRecordGradeVo> tmpVoList = new ArrayList<HCMRecordGradeVo>();
			List<HCMGradePo> gradeList = hcmSvc.getAllHCMGrade();
			
			for (int i=0; i<poList.size(); i++) {
				HCMRecordGradeVo tmp = new HCMRecordGradeVo();
				tmp.setGradeId(String.valueOf(poList.get(i).getGradeId()));
				tmp.setPositionId(String.valueOf(poList.get(i).getPositionId()));
				tmp.setDateFrom(poList.get(i).getDateFrom()!=null ? DateTimeHelper.formatDateToString(poList.get(i).getDateFrom()) : "");
				tmp.setDateTo(poList.get(i).getDateTo()!=null ? DateTimeHelper.formatDateToString(poList.get(i).getDateTo()) : "");
				for (int x=0; x<gradeList.size(); x++) {
					if (gradeList.get(x).getGradeId().equals(String.valueOf(poList.get(i).getGradeId()))) {
						tmp.setGradeDesc(gradeList.get(x).getGradeName());
						break;
					}
				}
				tmpVoList.add(tmp);
			}
			System.out.println("xxxxx: " + tmpVoList.size());
			
			vo.setGradeList(tmpVoList);
			
			return new ModelAndView("hcm/createHCMPost", "formBean", vo);
		}
		catch (Exception ex) {
			String errorMsg = doHandleException(ex.getCause());
			EClaimLogger.error("createHCMPost-performAction:" + ex.getMessage(), ex);
			
			List<HCMRecordGradeVo> tmpVoList = new ArrayList<HCMRecordGradeVo>();
			List<HCMGradePo> gradeList = hcmSvc.getAllHCMGrade();
			
			for (int i=0; i<createPositionWebVo.getGrade().size(); i++) {
				HCMRecordGradeVo tmp = new HCMRecordGradeVo();
				tmp.setGradeId(createPositionWebVo.getGrade().size()>0?createPositionWebVo.getGrade().get(i):"");
				tmp.setDateFrom(createPositionWebVo.getGradeDateFrom().size()>0?createPositionWebVo.getGradeDateFrom().get(i):"");
				tmp.setDateTo(createPositionWebVo.getGradeDateTo().size()>0?createPositionWebVo.getGradeDateTo().get(i):"");
				for (int x=0; x<gradeList.size(); x++) {
					if (gradeList.get(x).getGradeId().equals(createPositionWebVo.getGrade().get(i))) {
						tmp.setGradeDesc(gradeList.get(x).getGradeName());
						break;
					}
				}
				tmpVoList.add(tmp);
			}
			createPositionWebVo.setGradeList(tmpVoList);
			createPositionWebVo.setUpdateSuccess("N");
			createPositionWebVo.setDisplayMessage("Error occur:" + errorMsg.replaceAll("\r", " ").replaceAll("\n", " "));
			createPositionWebVo.setUserId(userId);
			return new ModelAndView("hcm/createHCMPost", "formBean", createPositionWebVo);
		}
	}
	
	private String doHandleException(Throwable t) {
		if (t == null) {
			return "Exception";
		}
		if (t.getClass().isAssignableFrom(SQLSyntaxErrorException.class)) {
			String msg = t.getMessage();
			if (msg.contains("ORA")) {
				msg = msg.substring(0, msg.indexOf("ORA", 5));
				return msg;
			}
		}
		else {
			if (t.getCause() != null) {
				return this.doHandleException(t.getCause());
			}
		}
		
		return "";
	}

	/**** Dropdown ****/
	@ModelAttribute("hiringStatusList")
	public Map<String, String> getHiringStatusList() {
		Map<String, String> displayList = new LinkedHashMap<String, String>();
		displayList.put("Active", "Active");
		displayList.put("Proposed", "Proposed");

		return displayList;
	}

	@ModelAttribute("durationUnitList")
	public Map<String, String> getDuringUnitList() {
		List<HCMProbationDurationUnitPo> list = hcmSvc.getAllHCMProbationDurationUnit();

		Map<String, String> displayList = new LinkedHashMap<String, String>();
		for (int i=0; i<list.size(); i++) {
			displayList.put(list.get(i).getCode(), list.get(i).getDesc());
		}
		return displayList;
	}
	
	@ModelAttribute("srcFundingList")
	public Map<String, String> getSourceFundingList() {
		List<HCMSourceFundingPo> list = hcmSvc.getAllHCMSourceFunding();

		Map<String, String> displayList = new LinkedHashMap<String, String>();
		for (int i=0; i<list.size(); i++) {
			displayList.put(list.get(i).getSourceFundingCode(), list.get(i).getSourceFundingDesc());
		}
		return displayList;
	}
	
	@ModelAttribute("gradeList")
	public Map<String, String> getGradeList() {
		List<HCMGradePo> list = hcmSvc.getAllHCMGrade();

		Map<String, String> displayList = new LinkedHashMap<String, String>();
		for (int i=0; i<list.size(); i++) {
			displayList.put(list.get(i).getGradeId(), list.get(i).getGradeName());
		}
		return displayList;
	}
	
	@ModelAttribute("locationList")
	public Map<String, String> getLocationList() {
		List<HCMLocationPo> list = hcmSvc.getAllHCMLocation();

		Map<String, String> displayList = new LinkedHashMap<String, String>();
		for (int i=0; i<list.size(); i++) {
			displayList.put(list.get(i).getLocationId(), list.get(i).getDescription());
		}
		return displayList;
	}
	
	private CreateHCMPostWebPo convertVoToPo(CreateHCMPostWebVo vo) {
		CreateHCMPostWebPo po = new CreateHCMPostWebPo();
		po.setHcmJob(vo.getHcmJob());
		po.setHcmPostTitle(vo.getHcmPostTitle());
		po.setHcmPostOrganization(vo.getHcmPostOrganization());
		po.setHcmOrganization(vo.getHcmOrganization());
		po.setHcmUnitTeam(vo.getHcmUnitTeam());
		po.setPostName(vo.getHiddenPostName());
		
		po.setEffectiveFromDate(vo.getEffectiveFromDate());
		po.setStartDate(vo.getStartDate());
		po.setType(vo.getType());
		
		po.setHiringStatus(vo.getHiringStatus());
		po.setLocation(vo.getLocation());
		po.setFte(vo.getFte());
		po.setHeadCount(vo.getHeadCount());
		po.setProbationDuration(vo.getProbationDuration());
		po.setProbationDurationUnit(vo.getProbationDurationUnit());
		
		po.setUpdateSuccess(vo.getUpdateSuccess());
		po.setDisplayMessage(vo.getDisplayMessage());
		
		po.setUserName(vo.getUserName());
		po.setProposedEndDate(vo.getProposedEndDate());
		
		po.setGrade(vo.getGrade());
		po.setGradeDateFrom(vo.getGradeDateFrom());
		po.setGradeDateTo(vo.getGradeDateTo());		
		
		po.setPositionGroup(vo.getPositionGroup());
		po.setSrcFunding(vo.getSrcFunding());
		
		po.setRepsonsibilityId(vo.getRepsonsibilityId());
		
		return po;
	}
}

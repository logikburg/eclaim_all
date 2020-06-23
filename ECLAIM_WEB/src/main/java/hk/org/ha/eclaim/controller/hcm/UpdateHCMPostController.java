package hk.org.ha.eclaim.controller.hcm;

import java.sql.SQLSyntaxErrorException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import hk.org.ha.eclaim.model.hcm.HCMRecordGradeVo;
import hk.org.ha.eclaim.model.hcm.UpdateHCMPostWebVo;
import hk.org.ha.eclaim.bs.cs.svc.ICommonSvc;
import hk.org.ha.eclaim.bs.hcm.po.HCMGradePo;
import hk.org.ha.eclaim.bs.hcm.po.HCMJobPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMOrganizationPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMPostOrganizationPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMPostTitlePo;
import hk.org.ha.eclaim.bs.hcm.po.HCMProbationDurationUnitPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMRecordGradePo;
import hk.org.ha.eclaim.bs.hcm.po.HCMRecordPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMSourceFundingPo;
import hk.org.ha.eclaim.bs.hcm.po.UpdateHCMPostWebPo;
import hk.org.ha.eclaim.bs.hcm.svc.IHCMSvc;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.svc.ISecuritySvc;
import hk.org.ha.eclaim.core.helper.DateTimeHelper;
import hk.org.ha.eclaim.core.logger.EClaimLogger;

@Controller
public class UpdateHCMPostController extends BaseController {
	@Autowired
	IHCMSvc hcmSvc;

	@Autowired
	ICommonSvc commonSvc;

	@Autowired
	ISecuritySvc securitySvc;

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@RequestMapping(value="/hcm/updateHCMPost", method=RequestMethod.GET)
	public ModelAndView init(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("Location UpdateHCMPostController.init()");

		// Get the user name from cookie
		String userId = this.getSessionUser(request).getUserId();
		UserPo user = securitySvc.findUser(userId);
        user.setCurrentRole((String)request.getSession().getAttribute("currentRole"));

		List<HCMJobPo> hcmJobs = hcmSvc.getAllHCMJob();
		System.out.println("hcmJobs.size(): " + hcmJobs.size());

		List<HCMPostTitlePo> hcmPostTitle = hcmSvc.getAllHCMPostTitle();
		System.out.println("hcmPostTitle.size(): " + hcmPostTitle.size());

		List<HCMPostOrganizationPo> hcmPostOrganization = hcmSvc.getAllHCMPostOrganization();
		System.out.println("hcmPostOrganization.size(): " + hcmPostOrganization.size());

		List<HCMOrganizationPo> hcmOrganization = hcmSvc.getAllHCMOrganization();
		System.out.println("hcmOrganization.size(): " + hcmOrganization.size());

		UpdateHCMPostWebVo vo = new UpdateHCMPostWebVo();
		vo.setHcmEffectiveDate(DateTimeHelper.formatDateToString(new Date()));
		vo.setPositionId(request.getParameter("positionId"));
		vo.setUserName(user.getUserName());
		vo.setUserId(userId);
		
		if (request.getParameter("pId") != null) {
			// Retrieve the content again
			HCMRecordPo hcmRecord = hcmSvc.getHCMRecordByPositionId(Integer.parseInt((String)request.getParameter("pId")), (String)request.getParameter("effectiveDate"));
			vo.setPositionId((String)request.getParameter("pId"));
			vo.setEffectiveFromDate(hcmRecord.getDateEffectiveStr());
			vo.setHiddenEffectiveFromDate(hcmRecord.getDateEffectiveStr());
			vo.setStartDate(hcmRecord.getEffectiveStartDateStr());
			vo.setProbationDuration(String.valueOf(hcmRecord.getPorbationPeriod()));
			vo.setProbationDurationUnit(hcmRecord.getProbationPeriodUnitCd());
			vo.setPostOrganization(hcmRecord.getPositionOrganization());
			vo.setOrganization(String.valueOf(hcmRecord.getOrganizationId()));
			vo.setJob(String.valueOf(hcmRecord.getJobId()));
			vo.setPostName(hcmRecord.getName());
			vo.setFte(String.valueOf(hcmRecord.getFte()));
			vo.setHeadCount(String.valueOf(hcmRecord.getMaxPerson()));
			vo.setPostTitle(hcmRecord.getPositionTitle());
			vo.setPostOrganization(hcmRecord.getPostOrganizationId());
			vo.setUnitTeam(hcmRecord.getUnitTeam());
			vo.setVersionNo(String.valueOf(hcmRecord.getVersionNumber()));
			vo.setHiringStatus(hcmRecord.getAvailabilityStatus());
			vo.setType(hcmRecord.getPositionType());
			vo.setProposedEndDate(hcmRecord.getHiringStatusPropEndDateStr());
			vo.setDdPostOrganization(hcmRecord.getPositionOrganization());
			vo.setDdJob(String.valueOf(hcmRecord.getJobName()));
			vo.setDdOrganization(hcmRecord.getOrganizationName());
			vo.setDdPostTitle(hcmRecord.getPositionTitle());
			vo.setDdLocation(hcmRecord.getLocationDesc());
			vo.setPositionGroup(hcmRecord.getPositionGroup());
			vo.setSrcFunding(hcmRecord.getSrcFunding());
			vo.setLocation(hcmRecord.getLocationId());
			
			List<HCMRecordGradePo> poList = hcmSvc.getHCMGradeRecordByPositionId(Integer.parseInt(vo.getPositionId()));
			List<HCMRecordGradeVo> tmpVoList = new ArrayList<HCMRecordGradeVo>();
			
			List<HCMGradePo> gradeList = hcmSvc.getAllHCMGrade();
			vo.setGradeListAll(hcmSvc.getAllHCMGrade());
			for (int i=0; i<poList.size(); i++) {
				HCMRecordGradeVo tmp = new HCMRecordGradeVo();
				
				tmp.setGradeId(String.valueOf(poList.get(i).getGradeId()));
				tmp.setPositionId(String.valueOf(poList.get(i).getPositionId()));
				tmp.setDateFrom(poList.get(i).getDateFrom()!=null ? DateTimeHelper.formatDateToString(poList.get(i).getDateFrom()) : "");
				tmp.setDateTo(poList.get(i).getDateTo()!=null ? DateTimeHelper.formatDateToString(poList.get(i).getDateTo()) : "");
				tmp.setVersionNumber(String.valueOf(poList.get(i).getVersionNumber()));
				EClaimLogger.info("Version Number: " + String.valueOf(poList.get(i).getVersionNumber()));
				tmp.setGradeUid(String.valueOf(poList.get(i).getGradeUid()));
				EClaimLogger.info("Grade Uid: " + String.valueOf(poList.get(i).getGradeUid()));
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
			
			// Checking whether the case have future record
			boolean lastRecord = false;
			String openEndDate = "31/12/4712";
			if (openEndDate.equals(hcmRecord.getEffectiveEndDateStr())) {
				lastRecord = true;
			}
			else {
				Date tmpDate = hcmRecord.getEffectiveEndDate();
				Calendar cal = Calendar.getInstance();
				cal.setTime(tmpDate);
				cal.add(Calendar.DATE, 1);
				System.out.println("Look for effective date: " + DateTimeHelper.formatDateToString(cal.getTime()));
				HCMRecordPo tmpRecord = hcmSvc.getHCMRecordByPositionId(Integer.parseInt(vo.getPositionId()), DateTimeHelper.formatDateToString(cal.getTime()));
				if (tmpRecord == null) {
					lastRecord = true;
				}
			}
			
			vo.setLastRecord(lastRecord?"Y":"F");
			
			
		}	

		return new ModelAndView("hcm/updateHCMPost", "formBean", vo);
	}

	@RequestMapping(value="/hcm/updateHCMPost", method=RequestMethod.POST)
	public ModelAndView performAction(@ModelAttribute("formBean")UpdateHCMPostWebVo updatePositionWebVo, 
			HttpServletRequest request) throws Exception {
		System.out.println("Location UpdateHCMPostController.performAction()");

		String userId = this.getSessionUser(request).getUserId();
		UserPo user = securitySvc.findUser(userId);
        
		try {
			// Get the user name from cookie
	        user.setCurrentRole((String)request.getSession().getAttribute("currentRole"));
	
			System.out.println("Job: " + updatePositionWebVo.getHcmJob());
			System.out.println("PostOrganization: " + updatePositionWebVo.getHcmPostOrganization());
			System.out.println("PostTitle: " + updatePositionWebVo.getHcmPostTitle());
			System.out.println("Organization: " + updatePositionWebVo.getHcmOrganization());
			System.out.println("Unit/Team: " + updatePositionWebVo.getHcmUnitTeam());
			
			System.out.println("formAction: " + updatePositionWebVo.getFormAction());
			System.out.println("positionId: " + updatePositionWebVo.getPositionId());
			int positionId = Integer.parseInt(updatePositionWebVo.getPositionId());
			int versionNo = Integer.parseInt(updatePositionWebVo.getVersionNo());
			if ("UPDATE".equals(updatePositionWebVo.getFormAction()) ||
					"CORRECTION".equals(updatePositionWebVo.getFormAction()) ||
					"UPDATE_CHANGE_INSERT".equals(updatePositionWebVo.getFormAction()) ||
					"UPDATE_OVERRIDE".equals(updatePositionWebVo.getFormAction())
					) {
				
				// Convert UpdateHCMPostWebVo to UpdateHCMPostWebPo
				UpdateHCMPostWebPo po = this.convertVoToPo(updatePositionWebVo);
				String warningMsg = hcmSvc.updateHCMPosition(po, userId);
				
				System.out.println("updatePositionWebVo.getPositionId(): " + updatePositionWebVo.getPositionId());
				
				// Retrieve the content again
				HCMRecordPo hcmRecord = hcmSvc.getHCMRecordByPositionId(Integer.parseInt(updatePositionWebVo.getPositionId()), updatePositionWebVo.getEffectiveFromDate());
				
				if (!"".equals(updatePositionWebVo.getUpdatedFTE())) {
					hcmSvc.updateFTE(Integer.parseInt(updatePositionWebVo.getPositionId()), 
					         Double.parseDouble(updatePositionWebVo.getUpdatedFTE()), 
					         Integer.parseInt(updatePositionWebVo.getHeadCount()), 
					         updatePositionWebVo.getUpdatedFTEReason(), user);	
				}
				
				updatePositionWebVo = convertToWebVo(hcmRecord);
				
				updatePositionWebVo.setUpdateSuccess("Y");
				String displayMsg = "Record update success!";
				if (warningMsg != null && !"".equals(warningMsg)) {
					displayMsg += " (Warning: " + warningMsg + ")";
				}
				
				updatePositionWebVo.setDisplayMessage(displayMsg);
				System.out.println("WarningMsg: " + warningMsg);
			}
			else if ("UPDATEFTE".equals(updatePositionWebVo.getFormAction())) {
				hcmSvc.updateFTE(Integer.parseInt(updatePositionWebVo.getPositionId()), 
						         Double.parseDouble(updatePositionWebVo.getUpdatedFTE()), 
						         Integer.parseInt(updatePositionWebVo.getHeadCount()), 
						         updatePositionWebVo.getUpdatedFTEReason(), user);
				
				HCMRecordPo hcmRecord = hcmSvc.getHCMRecordByPositionId(Integer.parseInt(updatePositionWebVo.getPositionId()));
				updatePositionWebVo = convertToWebVo(hcmRecord);
				
				updatePositionWebVo.setUpdateSuccess("Y");
				updatePositionWebVo.setDisplayMessage("Record update success!");
			}
			else if ("DEL_NEXT".equals(updatePositionWebVo.getFormAction()) || 
					"DEL_ALL".equals(updatePositionWebVo.getFormAction()) ||
					"DEL_PURGE".equals(updatePositionWebVo.getFormAction())) {
				String postName = updatePositionWebVo.getHiddenPostName();
				
				if ("DEL_NEXT".equals(updatePositionWebVo.getFormAction())) {
					hcmSvc.deleteHCMPosition(updatePositionWebVo.getEffectiveFromDate(), positionId, versionNo, "DELETE_NEXT_CHANGE", updatePositionWebVo.getRepsonsibilityId(), userId);
				}
				
				if ("DEL_ALL".equals(updatePositionWebVo.getFormAction())) {
					hcmSvc.deleteHCMPosition(updatePositionWebVo.getEffectiveFromDate(), positionId, versionNo, "FUTURE_CHANGE", updatePositionWebVo.getRepsonsibilityId(), userId);
				}
				
				if ("DEL_PURGE".equals(updatePositionWebVo.getFormAction())) {
					hcmSvc.deleteHCMPosition(updatePositionWebVo.getEffectiveFromDate(), positionId, versionNo, "ZAP", updatePositionWebVo.getRepsonsibilityId(), userId);
				}
				
				updatePositionWebVo = new UpdateHCMPostWebVo();
				updatePositionWebVo.setUpdateSuccess("Y");
				updatePositionWebVo.setDisplayMessage(postName + " is deleted successfully!"); // Update the message for UT30030
				
				// Added for UT30030
				updatePositionWebVo.setHcmEffectiveDate(DateTimeHelper.formatDateToString(new Date()));
			}
			
			updatePositionWebVo.setUserName(user.getUserName());
			updatePositionWebVo.setUserId(userId);
			return new ModelAndView("hcm/updateHCMPost", "formBean", updatePositionWebVo);
		}
		catch (Exception ex) {
			String errorMsg = doHandleException(ex.getCause());
			EClaimLogger.error("updateHCMPostController-performAction:" + ex.getMessage(), ex);
			
			if ("Exception".equals(errorMsg)) {
				errorMsg = ex.getMessage();
			}
			
			errorMsg = errorMsg.replaceAll("\r", " ").replaceAll("\n", " ");
			
			List<HCMRecordGradeVo> tmpVoList = new ArrayList<HCMRecordGradeVo>();
			List<HCMGradePo> gradeList = hcmSvc.getAllHCMGrade();
			
			for (int i=0; i<updatePositionWebVo.getGrade().size(); i++) {
				HCMRecordGradeVo tmp = new HCMRecordGradeVo();
				tmp.setGradeId(updatePositionWebVo.getGrade().size()>0?updatePositionWebVo.getGrade().get(i):"");
				tmp.setGradeUid(updatePositionWebVo.getGradeUid().size()>0?updatePositionWebVo.getGradeUid().get(i):"");
				tmp.setDateFrom(updatePositionWebVo.getGradeDateFrom().size()>0?updatePositionWebVo.getGradeDateFrom().get(i):"");
				tmp.setDateTo(updatePositionWebVo.getGradeDateTo().size()>0?updatePositionWebVo.getGradeDateTo().get(i):"");
				for (int x=0; x<gradeList.size(); x++) {
					if (gradeList.get(x).getGradeId().equals(updatePositionWebVo.getGrade().get(i))) {
						tmp.setGradeDesc(gradeList.get(x).getGradeName());
						break;
					}
				}
				tmpVoList.add(tmp);
			}
			updatePositionWebVo.setGradeList(tmpVoList);
			updatePositionWebVo.setUpdateSuccess("N");
			updatePositionWebVo.setDisplayMessage(errorMsg);
			updatePositionWebVo.setUserId(userId);
			return new ModelAndView("hcm/updateHCMPost", "formBean", updatePositionWebVo);
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
		displayList.put("Frozen", "Frozen");
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
	
	/*@ModelAttribute("gradeList")
	public Map<String, String> getGradeList() {
		List<HCMGradePo> list = hcmSvc.getAllHCMGrade();

		Map<String, String> displayList = new HashMap<String, String>();
		for (int i=0; i<list.size(); i++) {
			displayList.put(list.get(i).getGradeId(), list.get(i).getGradeName());
		}
		return displayList;
	}
	
	@ModelAttribute("locationList")
	public Map<String, String> getLocationList() {
		List<HCMLocationPo> list = hcmSvc.getAllHCMLocation();

		Map<String, String> displayList = new HashMap<String, String>();
		for (int i=0; i<list.size(); i++) {
			displayList.put(list.get(i).getLocationId(), list.get(i).getDescription());
		}
		return displayList;
	}*/
	
	private UpdateHCMPostWebPo convertVoToPo(UpdateHCMPostWebVo vo) {
		UpdateHCMPostWebPo po = new UpdateHCMPostWebPo();
		po.setHcmJob(vo.getHcmJob());
		po.setHcmPostTitle(vo.getHcmPostTitle());
		po.setHcmPostOrganization(vo.getHcmPostOrganization());
		po.setHcmOrganization(vo.getHcmOrganization());
		po.setHcmUnitTeam(vo.getHcmUnitTeam());
		
		po.setEffectiveFromDate(vo.getEffectiveFromDate());
		po.setStartDate(vo.getStartDate());
		po.setType(vo.getType());
		po.setPostName(vo.getHiddenPostName());
		
		po.setJob(vo.getJob());
		po.setPostTitle(vo.getPostTitle());
		po.setPostOrganization(vo.getPostOrganization());
		po.setOrganization(vo.getOrganization());
		po.setUnitTeam(vo.getUnitTeam());
		
		po.setHiringStatus(vo.getHiringStatus());
		po.setLocation(vo.getLocation());
		if (!"".equals(vo.getUpdatedFTE())) {
			po.setFte(vo.getUpdatedFTE());
		}
		else {
			po.setFte(vo.getFte());
		}
		po.setHeadCount(vo.getHeadCount());
		po.setProbationDuration(vo.getProbationDuration());
		po.setProbationDurationUnit(vo.getProbationDurationUnit());
		
		po.setUpdateSuccess(vo.getUpdateSuccess());
		po.setDisplayMessage(vo.getDisplayMessage());
		po.setFromCreateHCMPage(vo.getFromCreateHCMPage());
		
		po.setUserName(vo.getUserName());
		po.setProposedEndDate(vo.getProposedEndDate());
		System.out.println("vo.getProposedEndDate(): " + vo.getProposedEndDate());
		
		po.setFormAction(vo.getFormAction());
		po.setPositionId(vo.getPositionId());
		
		po.setVersionNo(vo.getVersionNo());
		
		po.setGrade(vo.getGrade());
		po.setGradeDateFrom(vo.getGradeDateFrom());
		po.setGradeDateTo(vo.getGradeDateTo());
		po.setGradeUid(vo.getGradeUid());
		po.setVersionNumber(vo.getVersionNumber());

		po.setPositionGroup(vo.getPositionGroup());
		po.setSrcFunding(vo.getSrcFunding());
		
		po.setLocation(vo.getLocation());
		po.setPositionGroup(vo.getPositionGroup());
		po.setSrcFunding(vo.getSrcFunding());
		
		po.setRepsonsibilityId(vo.getRepsonsibilityId());
		
		return po;
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
	
	private UpdateHCMPostWebVo convertToWebVo(HCMRecordPo hcmRecord) {
		UpdateHCMPostWebVo updatePositionWebVo = new UpdateHCMPostWebVo();
		updatePositionWebVo.setPositionId(String.valueOf(hcmRecord.getPositionId()));
		updatePositionWebVo.setEffectiveFromDate(hcmRecord.getEffectiveStartDateStr());
		updatePositionWebVo.setHiddenEffectiveFromDate(hcmRecord.getEffectiveStartDateStr());
		// updatePositionWebVo.setStartDate(hcmRecord.getEffectiveStartDateStr());
		updatePositionWebVo.setProbationDuration(String.valueOf(hcmRecord.getPorbationPeriod()));
		updatePositionWebVo.setProbationDurationUnit(hcmRecord.getProbationPeriodUnitCd());
		updatePositionWebVo.setPostOrganization(hcmRecord.getPositionOrganization());
		updatePositionWebVo.setOrganization(String.valueOf(hcmRecord.getOrganizationId()));
		updatePositionWebVo.setJob(String.valueOf(hcmRecord.getJobId()));
		updatePositionWebVo.setPostName(hcmRecord.getName());
		updatePositionWebVo.setHiddenPostName(hcmRecord.getName());
		updatePositionWebVo.setFte(String.valueOf(hcmRecord.getFte()));
		updatePositionWebVo.setHeadCount(String.valueOf(hcmRecord.getMaxPerson()));
		updatePositionWebVo.setPostTitle(hcmRecord.getPositionTitle());
		updatePositionWebVo.setPostOrganization(hcmRecord.getPostOrganizationId());
		updatePositionWebVo.setUnitTeam(hcmRecord.getUnitTeam());
		updatePositionWebVo.setVersionNo(String.valueOf(hcmRecord.getVersionNumber()));
		updatePositionWebVo.setHiringStatus(hcmRecord.getAvailabilityStatus());
		updatePositionWebVo.setType(hcmRecord.getPositionType());
		updatePositionWebVo.setProposedEndDate(hcmRecord.getHiringStatusPropEndDateStr());
		updatePositionWebVo.setDdPostOrganization(hcmRecord.getPositionOrganization());
		updatePositionWebVo.setDdJob(String.valueOf(hcmRecord.getJobName()));
		updatePositionWebVo.setDdOrganization(hcmRecord.getOrganizationName());
		updatePositionWebVo.setDdPostTitle(hcmRecord.getPositionTitle());
		updatePositionWebVo.setDdLocation(hcmRecord.getLocationId());
		updatePositionWebVo.setPositionGroup(hcmRecord.getPositionGroup());
		updatePositionWebVo.setSrcFunding(hcmRecord.getSrcFunding());
		updatePositionWebVo.setDdLocation(hcmRecord.getLocationDesc());
		updatePositionWebVo.setStartDate(hcmRecord.getPositionStartDateStr());
		updatePositionWebVo.setEffectiveFromDisplay(hcmRecord.getEffectiveStartDateStr());
		updatePositionWebVo.setEffectiveToDisplay(hcmRecord.getEffectiveEndDateStr());
		updatePositionWebVo.setHiringStatusStartDate(hcmRecord.getHiringStatusStartDateStr());
		
		List<HCMRecordGradePo> poList = hcmSvc.getHCMGradeRecordByPositionId(Integer.parseInt(updatePositionWebVo.getPositionId()));
		List<HCMRecordGradeVo> tmpVoList = new ArrayList<HCMRecordGradeVo>();
		updatePositionWebVo.setGradeListAll(hcmSvc.getAllHCMGrade());
		
		List<HCMGradePo> gradeList = hcmSvc.getAllHCMGrade();
		
		for (int i=0; i<poList.size(); i++) {
			HCMRecordGradeVo tmp = new HCMRecordGradeVo();
//			tmp.setGradeId(poList.get(i).getGradeId());
//			tmp.setPositionId(poList.get(i).getPositionId());
//			tmp.setDateFrom(poList.get(i).getDateFrom());
//			tmp.setDateTo(poList.get(i).getDateTo());
			
			tmp.setGradeId(String.valueOf(poList.get(i).getGradeId()));
			tmp.setPositionId(String.valueOf(poList.get(i).getPositionId()));
			tmp.setDateFrom(poList.get(i).getDateFrom()!=null?DateTimeHelper.formatDateToString(poList.get(i).getDateFrom()):"");
			tmp.setDateTo(poList.get(i).getDateTo()!=null?DateTimeHelper.formatDateToString(poList.get(i).getDateTo()):"");
			tmp.setVersionNumber(String.valueOf(poList.get(i).getVersionNumber()));
			tmp.setGradeUid(String.valueOf(poList.get(i).getGradeUid()));
			for (int x=0; x<gradeList.size(); x++) {
				if (gradeList.get(x).getGradeId().equals(String.valueOf(poList.get(i).getGradeId()))) {
					tmp.setGradeDesc(gradeList.get(x).getGradeName());
					break;
				}
			}
			tmpVoList.add(tmp);
		}
		System.out.println("xxxxx: " + tmpVoList.size());
		
		updatePositionWebVo.setGradeList(tmpVoList);
		
		// Checking whether the case have future record
		boolean lastRecord = false;
		String openEndDate = "31/12/4712";
		if (openEndDate.equals(hcmRecord.getEffectiveEndDateStr())) {
			lastRecord = true;
		}
		else {
			Date tmpDate = hcmRecord.getEffectiveEndDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(tmpDate);
			cal.add(Calendar.DATE, 1);
			System.out.println("Look for effective date: " + DateTimeHelper.formatDateToString(cal.getTime()));
			HCMRecordPo tmpRecord = hcmSvc.getHCMRecordByPositionId(hcmRecord.getPositionId(), DateTimeHelper.formatDateToString(cal.getTime()));
			
			if (tmpRecord == null) {
				lastRecord = true;
			}
		}
		
		updatePositionWebVo.setLastRecord(lastRecord?"Y":"N");
		
		System.out.println("updatePositionWebVo.getLastRecord(): " + updatePositionWebVo.getLastRecord());
		
		updatePositionWebVo.setUpdateSuccess("Y");
		updatePositionWebVo.setDisplayMessage("Record update success!");
		
		return updatePositionWebVo;
	}
}

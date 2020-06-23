package hk.org.ha.eclaim.controller.hcm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hk.org.ha.eclaim.bs.request.po.PostMasterPo;
import hk.org.ha.eclaim.bs.request.svc.IRequestSvc;
import hk.org.ha.eclaim.core.controller.BaseController;
import hk.org.ha.eclaim.model.hcm.HCMResultRespone;
import hk.org.ha.eclaim.model.hcm.HCMSearchRequest;
import hk.org.ha.eclaim.model.hcm.HcmPositionResponse;
import hk.org.ha.eclaim.model.hcm.HcmPositionResponseWrapper;
import hk.org.ha.eclaim.bs.cs.po.InstitutionPo;
import hk.org.ha.eclaim.bs.cs.svc.ICommonSvc;
import hk.org.ha.eclaim.bs.hcm.po.HCMAssignmentPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMGradePo;
import hk.org.ha.eclaim.bs.hcm.po.HCMJobPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMLocationPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMOrganizationPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMPostOrganizationPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMPostTitlePo;
import hk.org.ha.eclaim.bs.hcm.po.HCMRecordGradePo;
import hk.org.ha.eclaim.bs.hcm.po.HCMRecordPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMResponsibilityPo;
import hk.org.ha.eclaim.bs.hcm.svc.IHCMSvc;
import hk.org.ha.eclaim.core.helper.DateTimeHelper;
import hk.org.ha.eclaim.core.helper.StrHelper;
import hk.org.ha.eclaim.core.logger.EClaimLogger;

@Controller
public class CommonHCMPostController extends BaseController {
	
	@Autowired
	IHCMSvc hcmSvc;

	@Autowired
	ICommonSvc commonSvc;
	
	@Autowired
	IRequestSvc requestSvc;
	
	// Search HCM Position For Update HCM Position
	@RequestMapping(value="/api/hcm/searchHcmPositionForHcmFunction")
	public @ResponseBody HcmPositionResponseWrapper searchHcmPositionForHcmFunction(@ModelAttribute HCMSearchRequest hcmSearchRequest, 
																	  HttpServletRequest request) {
		System.out.println("searchHcmPositionForHcmFunction");
		System.out.println("hcmSearchRequest.getHcmEffectiveDate(): " + hcmSearchRequest.getHcmEffectiveDate());
		System.out.println("hcmSearchRequest.getHcmPostTitle(): " + hcmSearchRequest.getHcmPostTitle());
		System.out.println("hcmSearchRequest.getHcmPostOrganization(): " + hcmSearchRequest.getHcmPostOrganization());
		System.out.println("hcmSearchRequest.getHcmUnitTeam(): " + hcmSearchRequest.getHcmUnitTeam());
		System.out.println("hcmSearchRequest.getHcmJob(): " + hcmSearchRequest.getHcmJob());
		System.out.println("hcmSearchRequest.getHcmOrganization(): " + hcmSearchRequest.getHcmOrganization());
		String userId = this.getSessionUser(request).getUserId();
		String roleId = (String)request.getSession().getAttribute("currentRole");
		List<HCMRecordPo> hcmRecordList = hcmSvc.searchHCMRecord(hcmSearchRequest.getHcmEffectiveDate(),
																	hcmSearchRequest.getHcmPostTitle(), 
																	hcmSearchRequest.getHcmPostOrganization(),
																	hcmSearchRequest.getHcmUnitTeam(),
																	hcmSearchRequest.getHcmJob(),
																	hcmSearchRequest.getHcmOrganization(),
																	hcmSearchRequest.getHcmPositionName(),
																	"Y",
																	null,
																	null,
																	userId,
																	roleId);
		HcmPositionResponseWrapper hcmPositionResponseWrapper = new HcmPositionResponseWrapper();
		List<HcmPositionResponse> hcmPositionList = new ArrayList<HcmPositionResponse>();
		System.out.println("hcmRecordList.size()=" + hcmRecordList.size());
		if (hcmRecordList.size() > 0) {
			for (int i=0; i<hcmRecordList.size(); i++) {
				HcmPositionResponse hcmPositionResponse = new HcmPositionResponse();
				hcmPositionResponse.setEffectiveStartDate(DateTimeHelper.formatDateToString(hcmRecordList.get(i).getEffectiveStartDate()));
				hcmPositionResponse.setPositionId(hcmRecordList.get(i).getPositionId());
				hcmPositionResponse.setName(hcmRecordList.get(i).getName());
				hcmPositionResponse.setFte(hcmRecordList.get(i).getFte());
				hcmPositionResponse.setMaxPerson(hcmRecordList.get(i).getMaxPerson());
				hcmPositionResponse.setAvailabilityStatus(hcmRecordList.get(i).getAvailabilityStatus());
				hcmPositionResponse.setPositionType(hcmRecordList.get(i).getPositionType());
				hcmPositionList.add(hcmPositionResponse);
			}
		}
		
		hcmPositionResponseWrapper.setHcmPositionResponse(hcmPositionList);
		hcmPositionResponseWrapper.setError("0");
		hcmPositionResponseWrapper.setErrorMsg("");
		System.out.println("Finish the Ajax Loading");

		return hcmPositionResponseWrapper;
	}
	
	/**** Function for Ajax to get detail of HCM case ****/
	@RequestMapping(value="/hcm/getHCMPostDetail")
	public @ResponseBody HCMResultRespone getHCMPostDetail(@ModelAttribute HCMSearchRequest hcmSearchRequest) {
		System.out.println("Location getHCMPostDetail");
		System.out.println("Post Id: " + hcmSearchRequest.getPostId());

		HCMRecordPo record = hcmSvc.getHCMRecordByPositionId(Integer.parseInt(hcmSearchRequest.getPostId()));
		List<HCMRecordGradePo> gradeList = hcmSvc.getHCMGradeRecordByPositionId(Integer.parseInt(hcmSearchRequest.getPostId()));
		List<HCMGradePo> checkGradeList = hcmSvc.getAllHCMGrade();
		
		for (int i=0; i<gradeList.size(); i++) {
			for (int x=0; x<checkGradeList.size(); x++) {
				if (checkGradeList.get(x).getGradeId().equals(String.valueOf(gradeList.get(i).getGradeId()))) {
					gradeList.get(i).setGradeDesc(checkGradeList.get(x).getGradeName());
					break;
				}
			}
		}
		
		HCMResultRespone response = new HCMResultRespone();
		response.setHcmRecord(record);
		response.setHcmGradeList(gradeList);
		System.out.println("HCM Position ID: " + record.getPositionId());
		// Get the default department, staff group and rank
		PostMasterPo tmpPost = requestSvc.getMPRSDefaultByHCMPositionId(record.getPositionId());
		if (tmpPost.getDeptCode() != null && !"".equals(tmpPost.getDeptCode())) {
			response.setDeptCode(tmpPost.getDeptCode());
		}
		
		if (tmpPost.getStaffGroupCode() != null && !"".equals(tmpPost.getStaffGroupCode())) {
			response.setStaffGroupCode(tmpPost.getStaffGroupCode());
		}
		
		if (tmpPost.getRankCode() != null && !"".equals(tmpPost.getRankCode())) {
			response.setRankCode(tmpPost.getRankCode());
		}

		System.out.println("Finish the Ajax Loading");
		
		// Added for UT30034 - Start
		if (hcmSearchRequest.getMprsPostId() != null && 
				!"".equals(hcmSearchRequest.getMprsPostId())) {

			String employeeId = "";
			String employeeName = "";

			HCMAssignmentPo assignment = hcmSvc.getHCMAssignmentByPostIdEffectiveDate(hcmSearchRequest.getMprsPostId(), DateTimeHelper.formatDateToString(new Date()));
			if (assignment != null) {
				employeeId = assignment.getEmployeeNumber();
				employeeName = assignment.getFullName();
			}
			
			response.setEmployeeId(employeeId);
			response.setEmployeeName(employeeName);
		}
		// Added for UT30034 - End

		return response;
	}
	
	@RequestMapping(value="/api/hcm/getHcmPositionDetailWithEffectiveDate")
	public @ResponseBody HCMResultRespone getHcmPositionDetailWithEffectiveDate(@ModelAttribute HCMSearchRequest hcmSearchRequest) {
		System.out.println("Location getHcmPositionDetailWithEffectiveDate");
		System.out.println("Post ID: " + hcmSearchRequest.getPostId());
		System.out.println("Effective Date: " + hcmSearchRequest.getHcmEffectiveDate());

		HCMRecordPo record = hcmSvc.getHCMRecordByPositionId(Integer.parseInt(hcmSearchRequest.getPostId()), hcmSearchRequest.getHcmEffectiveDate());
		List<HCMRecordGradePo> gradeList = hcmSvc.getHCMGradeRecordByPositionId(Integer.parseInt(hcmSearchRequest.getPostId()));
		List<HCMGradePo> checkGradeList = hcmSvc.getAllHCMGrade();
		
		for (int i=0; i<gradeList.size(); i++) {
			for (int x=0; x<checkGradeList.size(); x++) {
				if (checkGradeList.get(x).getGradeId().equals(String.valueOf(gradeList.get(i).getGradeId()))) {
					gradeList.get(i).setGradeDesc(checkGradeList.get(x).getGradeName());
					break;
				}
			}
		}
		
		HCMResultRespone response = new HCMResultRespone();
		response.setHcmRecord(record);
		response.setHcmGradeList(gradeList);
		
		// Get the default department, staff group and rank
		PostMasterPo tmpPost = requestSvc.getMPRSDefaultByHCMPositionId(record.getPositionId());
		if (tmpPost.getDeptCode() != null && !"".equals(tmpPost.getDeptCode())) {
			response.setDeptCode(tmpPost.getDeptCode());
		}
		
		if (tmpPost.getStaffGroupCode() != null && !"".equals(tmpPost.getStaffGroupCode())) {
			response.setStaffGroupCode(tmpPost.getStaffGroupCode());
		}
		
		if (tmpPost.getRankCode() != null && !"".equals(tmpPost.getRankCode())) {
			response.setRankCode(tmpPost.getRankCode());
		}
		
		// Checking whether the case have future record
		boolean lastRecord = false;
		String openEndDate = "31/12/4712";
		if (openEndDate.equals(record.getEffectiveEndDateStr())) {
			lastRecord = true;
		}
		else {
			Date tmpDate = record.getEffectiveEndDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(tmpDate);
			cal.add(Calendar.DATE, 1);
			System.out.println("Look for effective date: " + DateTimeHelper.formatDateToString(cal.getTime()));
			HCMRecordPo tmpRecord = hcmSvc.getHCMRecordByPositionId(Integer.parseInt(hcmSearchRequest.getPostId()), DateTimeHelper.formatDateToString(cal.getTime()));
			if (tmpRecord == null) {
				lastRecord = true;
			}
		}
		
		response.setLastRecord(lastRecord?"Y":"F");
		System.out.println("Finish the Ajax Loading");

		return response;
	}
	
	/**** Function for Ajax search of HCM case ****/
	@RequestMapping(value="/api/hcm/searchHcmPositionForPostCreation")
	public @ResponseBody HcmPositionResponseWrapper searchHcmPositionForPostCreation(@ModelAttribute HCMSearchRequest hcmSearchRequest, 
																	  HttpServletRequest request) {
		System.out.println("searchHcmPositionForPostCreation");
		System.out.println("hcmSearchRequest.getHcmEffectiveDate(): " + hcmSearchRequest.getHcmEffectiveDate());
		System.out.println("hcmSearchRequest.getHcmPostTitle(): " + hcmSearchRequest.getHcmPostTitle());
		System.out.println("hcmSearchRequest.getHcmPostOrganization(): " + hcmSearchRequest.getHcmPostOrganization());
		System.out.println("hcmSearchRequest.getHcmUnitTeam(): " + hcmSearchRequest.getHcmUnitTeam());
		System.out.println("hcmSearchRequest.getHcmJob(): " + hcmSearchRequest.getHcmJob());
		System.out.println("hcmSearchRequest.getHcmOrganization(): " + hcmSearchRequest.getHcmOrganization());
		System.out.println("hcmSearchRequest.getStaffGroup(): " + hcmSearchRequest.getStaffGroup());
		String userId = this.getSessionUser(request).getUserId();
		String roleId = (String)request.getSession().getAttribute("currentRole");
		
		List<HCMRecordPo> hcmRecordList = hcmSvc.searchHCMRecord(hcmSearchRequest.getHcmEffectiveDate(),
																	hcmSearchRequest.getHcmPostTitle(), 
																	hcmSearchRequest.getHcmPostOrganization(),
																	hcmSearchRequest.getHcmUnitTeam(),
																	hcmSearchRequest.getHcmJob(),
																	hcmSearchRequest.getHcmOrganization(),
																	hcmSearchRequest.getHcmPositionName(),
																	hcmSearchRequest.getAllowSingleIncumbent(),
																	hcmSearchRequest.getRequestType(),
																	hcmSearchRequest.getStaffGroup(),
																	userId,
																	roleId);
		
		HcmPositionResponseWrapper hcmPositionResponseWrapper = new HcmPositionResponseWrapper();
		
		List<HcmPositionResponse> hcmPositionList = new ArrayList<HcmPositionResponse>();
		System.out.println("hcmRecordList.size()=" + hcmRecordList.size());

		if (hcmRecordList.size() > 0) {
			// Get the cluster List
			List<InstitutionPo> instList = commonSvc.getAllInst();
			for (int i=0; i<hcmRecordList.size(); i++) {
				// find the cluster and institute mapping 
				String name = hcmRecordList.get(i).getName();
				if (name.indexOf("/") != -1) {
					name = name.substring(0, name.indexOf("/"));
					name = name.substring(name.indexOf(".")+1);
				}
				else {
					name = name.substring(name.indexOf(".")+1);
					name = name.substring(0, name.indexOf("."));
				}
				
				String clusterCode = "";
				String instCode = "";
				for (int x = 0; x < instList.size(); x++) {
					if (name.equals(instList.get(x).getInstCode())) {
						clusterCode = StrHelper.format(instList.get(x).getClusterCode());
						instCode = StrHelper.format(instList.get(x).getInstCode());
						break;
					}
				}
				HcmPositionResponse hcmPositionResponse = new HcmPositionResponse();
				
				hcmPositionResponse.setEffectiveStartDate(DateTimeHelper.formatDateToString(hcmRecordList.get(i).getEffectiveStartDate()));
				hcmPositionResponse.setPositionId(hcmRecordList.get(i).getPositionId());
				hcmPositionResponse.setName(hcmRecordList.get(i).getName());
				hcmPositionResponse.setFte(hcmRecordList.get(i).getFte());
				hcmPositionResponse.setMaxPerson(hcmRecordList.get(i).getMaxPerson());
				hcmPositionResponse.setAvailabilityStatus(hcmRecordList.get(i).getAvailabilityStatus());
				hcmPositionResponse.setPositionType(hcmRecordList.get(i).getPositionType());
				hcmPositionResponse.setClusterCode(clusterCode);
				hcmPositionResponse.setInstCode(instCode);
				hcmPositionResponse.setDateEffective(hcmRecordList.get(i).getDateEffective());
				hcmPositionList.add(hcmPositionResponse);
			}
		}
		
		hcmPositionResponseWrapper.setHcmPositionResponse(hcmPositionList);
		hcmPositionResponseWrapper.setError("0");
		hcmPositionResponseWrapper.setErrorMsg("");
		System.out.println("Finish the Ajax Loading");

		return hcmPositionResponseWrapper;
	}
	
	@RequestMapping(value="/hcm/getHCMJobList", method = RequestMethod.GET)
	public @ResponseBody List<HCMJobPo> getHCMJobList(@RequestParam String term) {
		List<HCMJobPo> list = hcmSvc.getAllHCMJob(term);
		return list;
	}
	
	@RequestMapping(value="/hcm/getHCMOrganizationList", method = RequestMethod.GET)
	public @ResponseBody List<HCMOrganizationPo> getHCMOrganizationList(@RequestParam String term, 
																		HttpServletRequest request, 
																		HttpServletResponse httpResponse) {
		String userId = this.getSessionUser(request).getUserId();
		String roleId = (String)request.getSession().getAttribute("currentRole");
		
		List<HCMOrganizationPo> list = hcmSvc.getAllHCMOrganization(term, userId, roleId);
		EClaimLogger.info("getHCMOrganizationList: userId=" + userId + ", roleId=" + roleId + ", size=" + list.size());
		return list;
	}
	
	@RequestMapping(value="/hcm/getHCMPostOrganizationList", method = RequestMethod.GET)
	public @ResponseBody List<HCMPostOrganizationPo> getHCMPostOrganizationList(@RequestParam String term, 
																				HttpServletRequest request, 
																				HttpServletResponse httpResponse) {
		String userId = this.getSessionUser(request).getUserId();
		String roleId = (String)request.getSession().getAttribute("currentRole");
		
		List<HCMPostOrganizationPo> list = hcmSvc.getAllHCMPostOrganization(term, userId, roleId);
		return list;
	}
	
	@RequestMapping(value="/hcm/getHCMPostTitleList", method = RequestMethod.GET)
	public @ResponseBody List<HCMPostTitlePo> getHCMPostTitleList(@RequestParam String term) {
		List<HCMPostTitlePo> list = hcmSvc.getAllHCMPostTitle(term);
		return list;
	}
	
	@RequestMapping(value="/hcm/getHCMPostTitleListByStaffGroup", method = RequestMethod.GET)
	public @ResponseBody List<HCMPostTitlePo> getHCMPostTitleListByStaffGroup(@RequestParam String term,
																				@RequestParam String staffGroup) {
		List<HCMPostTitlePo> list = hcmSvc.getAllHCMPostTitleByStaffGroup(term, staffGroup);
		return list;
	}
	
	@RequestMapping(value="/hcm/getHCMLocationList", method = RequestMethod.GET)
	public @ResponseBody List<HCMLocationPo> getHCMLocationList(@RequestParam String term) {
		List<HCMLocationPo> list = hcmSvc.getAllHCMLocation(term);
		return list;
	}
	
	@RequestMapping(value="/hcm/getHCMGradeList", method = RequestMethod.GET)
	public @ResponseBody List<HCMGradePo> getHCMGradeList(@RequestParam String term) {
		List<HCMGradePo> list = hcmSvc.getAllHCMGrade(term);
		return list;
	}
	
	@RequestMapping(value="/hcm/searchResponsibility", method = RequestMethod.POST)
	public @ResponseBody List<HCMResponsibilityPo> searchResponsibility(@RequestParam String userId) {
		System.out.println("searchResponsibility..");
		List<HCMResponsibilityPo> list = hcmSvc.getHCMResponsibilityByUserId(userId);
		return list;
	}
	
	@RequestMapping(value="/hcm/getAssignmentDetail", method = RequestMethod.POST)
	public @ResponseBody HCMAssignmentPo getAssignmentDetail(@RequestParam String assignmentNumber, @RequestParam String effectiveStartDate) {
		System.out.println("getAssignmentDetail..");
		return hcmSvc.getHCMAssignment(assignmentNumber, effectiveStartDate);
	}
	
	@RequestMapping(value="/hcm/isHcmRunning", method = RequestMethod.POST)
	public @ResponseBody String isHcmRunning() {
		System.out.println("isHcmRunning..");
		return hcmSvc.isHCMPayrollRunning()?"Y":"N";
	}
	
	@RequestMapping(value="/hcm/getDefaultLocation", method = RequestMethod.POST)
	public @ResponseBody HCMLocationPo getDefaultLocation(@RequestParam String organizationId) {
		System.out.println("getDefaultLocation..");
		return hcmSvc.getDefaultLocation(Integer.parseInt(organizationId));
	}
	
	@RequestMapping(value="/hcm/deleteValidGrade", method = RequestMethod.POST)
	public @ResponseBody String deleteValidGrade(@RequestParam String gradeUid, @RequestParam String versionNumber, String repsonsibilityId, 
												 HttpServletRequest request) {
		String userId = this.getSessionUser(request).getUserId();
		
		hcmSvc.deleteValidGrade(Integer.parseInt(gradeUid), 
								Integer.parseInt(versionNumber), 
								repsonsibilityId, 
								userId);
		
		return "Y";
	}
	
	// CC176935 validate whether HCM Position is allowed to set headcount to 0
	// If active Post ID are linking to HCM Position, it is not allowed to update headcount to 0
	@RequestMapping(value="/hcm/getZeroHeadcountInd", method = RequestMethod.POST)
	public @ResponseBody String getZeroHeadcountInd(@RequestParam String positionId, @RequestParam String effectiveFromDate, @RequestParam String effectiveToDate) {
		Boolean zeroHeadcountInd = requestSvc.validatePositionZeroHeadcount(positionId, effectiveFromDate, effectiveToDate);
		return zeroHeadcountInd ? "Y" : "N";
	}
}
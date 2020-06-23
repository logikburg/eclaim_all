package hk.org.ha.eclaim.controller.request;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import hk.org.ha.eclaim.bs.request.constant.PostConstant;
import hk.org.ha.eclaim.bs.request.po.PostMasterPo;
import hk.org.ha.eclaim.bs.request.po.RequestAttachmentPo;
import hk.org.ha.eclaim.bs.request.po.RequestFundingPo;
import hk.org.ha.eclaim.bs.request.po.RequestFundingResourcePo;
import hk.org.ha.eclaim.bs.request.po.RequestPo;
import hk.org.ha.eclaim.bs.request.po.RequestPostPo;
import hk.org.ha.eclaim.bs.request.po.RequestTempFilePo;
import hk.org.ha.eclaim.bs.request.po.RequestWorkflowRoutePo;
import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.model.request.ChangeStaffMixWebVo;
import hk.org.ha.eclaim.bs.cs.po.EmailTemplatePo;
import hk.org.ha.eclaim.bs.cs.po.ExternalSupportPo;
import hk.org.ha.eclaim.bs.cs.po.FundingSourcePo;
import hk.org.ha.eclaim.bs.cs.po.FundingSourceSubCatPo;
import hk.org.ha.eclaim.bs.cs.po.MPRSPostStatusPo;
import hk.org.ha.eclaim.bs.cs.po.PostDurationPo;
import hk.org.ha.eclaim.bs.cs.po.ProgramTypePo;
import hk.org.ha.eclaim.bs.cs.po.RankPo;
import hk.org.ha.eclaim.bs.cs.po.RequestStatusPo;
import hk.org.ha.eclaim.bs.cs.po.RequestTypePo;
import hk.org.ha.eclaim.bs.cs.po.SubSpecialtyPo;
import hk.org.ha.eclaim.bs.security.po.DataAccessPo;
import hk.org.ha.eclaim.bs.security.po.RolePo;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.core.helper.DateTimeHelper;
import hk.org.ha.eclaim.core.helper.StrHelper;
import hk.org.ha.eclaim.core.logger.EClaimLogger;

@Controller
public class ChangeStaffMixController extends CommonRequestController {
	
	@Value("${mail.appurl.prefix}")
	private String appUrl;

	protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@RequestMapping(value="/request/changeStaffMix", method=RequestMethod.GET)
	public ModelAndView changeStaffMix(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("Location ChangeOfStaffMixController.changeStaffMix()");
		
		// Get the user name from cookie
		String userId = this.getSessionUser(request).getUserId();
		String currentRole = (String)request.getSession().getAttribute("currentRole");

		if (request.getParameter("rq") != null) {
			EClaimLogger.info("request uid: " + request.getParameter("rq"));

			// Check whether the user have access right for request uid
			int userRoleId = (int) request.getSession().getAttribute("currentUserRoleId");
			List<DataAccessPo> dataAccessList = securitySvc.getDataAccessByRoleId(userRoleId);
			RequestPo requestCase = requestSvc.getRequestByRequestNo(dataAccessList, Integer.parseInt((String)request.getParameter("rq")));
			if (requestCase != null) {
				ChangeStaffMixWebVo vo = convertToWebVo(requestCase, userId, currentRole);

				return new ModelAndView("request/changeStaffMix", "formBean", vo);
			}
			else {
				return new ModelAndView("redirect:/home/home");
			}
		}
		else {
			System.out.println("request uid is null");
			ChangeStaffMixWebVo vo = new ChangeStaffMixWebVo();
			
			if ("HR_OFFICER".equals(currentRole)) {
				vo.setCanEditDetailInfo("Y");
				vo.setUserHaveCreationRight("Y");
			}
			else {
				vo.setCanEditDetailInfo("N");
				vo.setUserHaveCreationRight("N");
			}
	
			if ("FIN_OFFICER".equals(currentRole)) {
				vo.setCanEditFinancialInfo("Y");
			}
			else {
				vo.setCanEditFinancialInfo("N");
			}
			
			if ("Y".equals(vo.getCanEditDetailInfo()) || "Y".equals(vo.getCanEditFinancialInfo())) {
				vo.setUserHaveSaveRight("Y");
			}
			else {
				vo.setUserHaveSaveRight("N");
			}
			
			return new ModelAndView("request/changeStaffMix", "formBean", vo);
		}
	}

	@RequestMapping(value="/request/changeStaffMix", method=RequestMethod.POST)
	public ModelAndView performUpdate(@ModelAttribute("formBean")ChangeStaffMixWebVo changeStaffMixWebVo, 
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		System.out.println("approvalFile: " + changeStaffMixWebVo.getApprovalFile());
		if (changeStaffMixWebVo.getApprovalFile() != null) {
			System.out.println("File name: " + changeStaffMixWebVo.getApprovalFile().getOriginalFilename());
			System.out.println("File size: " + changeStaffMixWebVo.getApprovalFile().getSize());
		}
		String formAction = changeStaffMixWebVo.getFormAction();

		// Get the user name from cookie
		String userId = this.getSessionUser(request).getUserId();
		UserPo user = securitySvc.findUser(userId);
        user.setCurrentRole((String)request.getSession().getAttribute("currentRole"));
		String currentRole = (String)request.getSession().getAttribute("currentRole");

		System.out.println("requestPost:" + changeStaffMixWebVo.getRequestPostNo().size());
		System.out.println("requestNo: " + changeStaffMixWebVo.getRequestNo());
		
		// Concurrent control
		if ("SAVE".equalsIgnoreCase(formAction) ||
				"WITHDRAW".equalsIgnoreCase(formAction) ||
				"SUBMIT".equalsIgnoreCase(formAction) ||
				"APPROVE".equalsIgnoreCase(formAction)) {
			if (changeStaffMixWebVo.getRequestNo() != null && !"".equals(changeStaffMixWebVo.getRequestNo())) {
				RequestPo requestCase = requestSvc.getRequestByRequestNo(Integer.parseInt(changeStaffMixWebVo.getRequestNo()));

				// Check last update date
				String lastUpdateDate = DateTimeHelper.formatDateTimeToString(requestCase.getUpdatedDate());
				System.out.println("=== " + lastUpdateDate + " vs " + changeStaffMixWebVo.getLastUpdateDate());
				if (!lastUpdateDate.equals(changeStaffMixWebVo.getLastUpdateDate())) {
					changeStaffMixWebVo = convertToWebVo(requestCase, userId, currentRole);
					changeStaffMixWebVo.setUpdateSuccess("N");
					changeStaffMixWebVo.setDisplayMessage("Record had been updated by other, content is refreshed.");

					return new ModelAndView("request/changeStaffMix", "formBean", changeStaffMixWebVo);
				}
			}
		}
		
		if ("SAVE".equalsIgnoreCase(formAction)) {
			int rowId1 = 0;
			int rowId2 = 0;
			System.out.println("Location ChangeOfStaffMixController.performUpdate()");
			System.out.println("RequestPoId: " + changeStaffMixWebVo.getRequestId());
			System.out.println("Requester " + changeStaffMixWebVo.getRequester());

			try {
				RequestPo newRequest= null;	
				if (changeStaffMixWebVo.getRequestNo() != null && !"".equals(changeStaffMixWebVo.getRequestNo())) {
					newRequest = requestSvc.getRequestByRequestNo(Integer.parseInt(changeStaffMixWebVo.getRequestNo()));
					
					if (changeStaffMixWebVo.getEffectiveDate() != null && !"".equals(changeStaffMixWebVo.getEffectiveDate())) {
						newRequest.setEffectFrom(sdf.parse(changeStaffMixWebVo.getEffectiveDate()));
					}
					newRequest.setRequestReason(changeStaffMixWebVo.getRequestReason());
					newRequest.setRequester(changeStaffMixWebVo.getRequester());
					
					if (changeStaffMixWebVo.getApprovalDate() != null && !"".equals(changeStaffMixWebVo.getApprovalDate())) {
						newRequest.setApprovalDate(sdf.parse(changeStaffMixWebVo.getApprovalDate()));
					}
					
					newRequest.setApprovalReference(changeStaffMixWebVo.getApprovalReference());
					newRequest.setApprovalRemark(changeStaffMixWebVo.getApprovalRemark());
					
					List<RequestPostPo> requestPositionFromList = new ArrayList<RequestPostPo>();
					List<RequestPostPo> requestPositionToList = new ArrayList<RequestPostPo>();
					
					for (int m=0; m<newRequest.getRequestPositionList().size(); m++) {
						if (MPRSConstant.MPRS_STATE_ACTIVE.equals(newRequest.getRequestPositionList().get(m).getRecState())) {
							if("Y".equals(newRequest.getRequestPositionList().get(m).getFromPostInd())) {
								requestPositionFromList.add(newRequest.getRequestPositionList().get(m));
							}
							else {
								requestPositionToList.add(newRequest.getRequestPositionList().get(m));
							}
						}
					}
					
					System.out.println("requestPositionFromList: " + requestPositionFromList.size());
					System.out.println("requestPositionToList: " + requestPositionToList.size());
					
					for (int i=0; i<changeStaffMixWebVo.getRequestPostNo().size(); i++) {
						System.out.println("Add Request Position!!");
	
						RequestPostPo rp = null;
						
						if (requestPositionFromList.size() > i) {
							rp = requestPositionFromList.get(i);
							System.out.println("Overwrite From: " + rp.getRequestPostId());
						}
						else {
							rp = new RequestPostPo();
						}
						
						PostMasterPo mprsPost = requestSvc.getPostByPostUid(Integer.parseInt(changeStaffMixWebVo.getRequestPostNo().get(i)));
						rp.setPostNo(changeStaffMixWebVo.getRequestPostNo().get(i));
						RankPo rank = new RankPo();
						rank.setRankCode(mprsPost.getRankCode());
						rp.setPostDuration(mprsPost.getPostDuration());
						rp.setPostFTE(String.valueOf(mprsPost.getPostFTE()));
						rp.setRank(rank);
						rp.setPostId(mprsPost.getPostId());
						rp.setClusterCode(mprsPost.getClusterCode());
						rp.setInstCode(mprsPost.getInstCode());
						rp.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
						rp.setFromPostInd("Y");
						rp.setStaffGroupCode(mprsPost.getStaffGroupCode());
						
						if (requestPositionFromList.size() > i) {
							int tmp = 0;
							for (int u=0; u<newRequest.getRequestPositionList().size(); u++) {
								if ("Y".equals(newRequest.getRequestPositionList().get(u).getFromPostInd())) {
									if (tmp == rowId1) {
										newRequest.getRequestPositionList().set(u, rp);
									}
									else {
										tmp ++;
									}
								}
							}
							
							rowId1 ++;
						}
						else {
							newRequest.addRequestPosition(rp);
						}
					}
					
					for (int i=0; i<changeStaffMixWebVo.getRequestCluster().size(); i++) {
						RequestPostPo rp = null;
						RequestFundingResourcePo rfs = null;
						
						if (requestPositionToList.size() > i) {
							rp = requestPositionToList.get(i);
							rfs = rp.getRequestFundingResource();
							
							System.out.println("Overwrite To: " + rp.getRequestPostId());
						}
						else {
							rp = new RequestPostPo();
							rfs = new RequestFundingResourcePo();
							rp.setRequestFundingResource(rfs);
						}
						
						boolean needRecycle = true;
						String tmpProposedPostId = rp.getProposedPostId();
						if (tmpProposedPostId != null) {
							System.out.println("tmpProposedPostId: " + tmpProposedPostId);
							System.out.println("changeStaffMixWebVo: " + changeStaffMixWebVo);
							
							String requestProposedPostId = changeStaffMixWebVo.getRequestProposedPostId().size() != 0 ? changeStaffMixWebVo.getRequestProposedPostId().get(i) : "";
							if (tmpProposedPostId.equals(requestProposedPostId)) {
								needRecycle = false;
							} else {
								System.out.println("Recycle: " + tmpProposedPostId);
								requestSvc.recycleProposedPostId(rp.getStaffGroupCode(), rp.getPostDuration(), rp.getPostFTEType(), tmpProposedPostId, userId, currentRole);
							}
						}
						
						System.out.println("Add RequestPoPosition To!!");
						
						if (changeStaffMixWebVo.getRequestRank().size()!=0) {
							RankPo rank = new RankPo();
							rank.setRankCode(changeStaffMixWebVo.getRequestRank().get(i));
							rp.setRank(rank);
						}
						
						rp.setClusterCode(changeStaffMixWebVo.getRequestCluster().size()!=0?changeStaffMixWebVo.getRequestCluster().get(i):"");
						rp.setInstCode(changeStaffMixWebVo.getRequestInst().size()!=0?changeStaffMixWebVo.getRequestInst().get(i):"");
						rp.setDeptCode(changeStaffMixWebVo.getRequestDept().size()!=0?changeStaffMixWebVo.getRequestDept().get(i):"");
						rp.setStaffGroupCode(changeStaffMixWebVo.getRequestStaffGroup().size()!=0?changeStaffMixWebVo.getRequestStaffGroup().get(i):"");
						rp.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
						rp.setHcmPositionId(changeStaffMixWebVo.getHcmPositionId().size()!=0?changeStaffMixWebVo.getHcmPositionId().get(i):"");
						rp.setHoBuyServiceInd(changeStaffMixWebVo.getRequestHoBuyServiceInd().size()!=0?changeStaffMixWebVo.getRequestHoBuyServiceInd().get(i):"");
						rp.setToPostInd("Y");
						
						System.out.println("--Position Tab Saved --");
						rp.setUnit(changeStaffMixWebVo.getRequestUnit().size()!=0?changeStaffMixWebVo.getRequestUnit().get(i):"");
						rp.setPostTitle(changeStaffMixWebVo.getRequestPostTitle().size()!=0?changeStaffMixWebVo.getRequestPostTitle().get(i):"");
						rp.setPostDuration(changeStaffMixWebVo.getRequestPostDuration().size()!=0?changeStaffMixWebVo.getRequestPostDuration().get(i):"");
						rp.setLimitDurationType(changeStaffMixWebVo.getRequestRdDuration().size()!=0?changeStaffMixWebVo.getRequestRdDuration().get(i):"");
						if (changeStaffMixWebVo.getRequestPostStartDate().size() !=0 ){
							if (!"".equals(changeStaffMixWebVo.getRequestPostStartDate().get(i))){
								rp.setPostStartDate(sdf.parse(changeStaffMixWebVo.getRequestPostStartDate().get(i)));//review
							}
						}
						rp.setLimitDurationNo(changeStaffMixWebVo.getRequestDurationValue().size()!=0?changeStaffMixWebVo.getRequestDurationValue().get(i):null);
						rp.setLimitDurationUnit(changeStaffMixWebVo.getRequestDurationUnit().size()!=0?changeStaffMixWebVo.getRequestDurationUnit().get(i):"");
						if (changeStaffMixWebVo.getRequestPostActualEndDate().size() != 0) {
							if (!"".equals(changeStaffMixWebVo.getRequestPostActualEndDate().get(i))) {
								rp.setLimitDurationEndDate(sdf.parse(changeStaffMixWebVo.getRequestPostActualEndDate().get(i)));
							}
							else {
								rp.setLimitDurationEndDate(null);
							}
						}
						
						// Update the Fixed End Date if selected (Time-Limited) and Duration Period
						if (!"R".equals(rp.getPostDuration()) && "DURATION_PERIOD".equals(rp.getLimitDurationType())) {
							int duration = 0;
							if ("Y".equals(rp.getLimitDurationUnit())) {
								duration = rp.getLimitDurationNo() * 12;
							}
							else if ("M".equals(rp.getLimitDurationUnit())) {
								duration = rp.getLimitDurationNo();
							}
							
							rp.setLimitDurationEndDate(requestSvc.addMonth(rp.getPostStartDate(), duration));
						}
						
						if ("R".equals(rp.getPostDuration())) {
							rp.setLimitDurationEndDate(null);
						}
						
						rp.setPostRemark(changeStaffMixWebVo.getRequestPostRemark().size()!=0?changeStaffMixWebVo.getRequestPostRemark().get(i):"");
						rp.setPostFTEType(changeStaffMixWebVo.getRequestPostFTE().size()!=0?changeStaffMixWebVo.getRequestPostFTE().get(i):"");
						rp.setPostFTE(changeStaffMixWebVo.getRequestPostFTEValue().size()!=0?changeStaffMixWebVo.getRequestPostFTEValue().get(i):"");
						rp.setPostStatus(changeStaffMixWebVo.getRequestPositionStatus().size()!=0?changeStaffMixWebVo.getRequestPositionStatus().get(i):"");
						if (changeStaffMixWebVo.getRequestPositionStartDate().size() != 0) {
							if (!"".equals(changeStaffMixWebVo.getRequestPositionStartDate().get(i))) {
								rp.setPostStatusStartDate(sdf.parse(changeStaffMixWebVo.getRequestPositionStartDate().get(i)));
							}
						}
						if (changeStaffMixWebVo.getRequestPositionEndDate().size() != 0) {
							if (!"".equals(changeStaffMixWebVo.getRequestPositionEndDate().get(i))) {
								rp.setPostStatusEndDate(sdf.parse(changeStaffMixWebVo.getRequestPositionEndDate().get(i)));
							}
						}
						rp.setClusterRef(changeStaffMixWebVo.getRequestClusterRefNo().size()!=0?changeStaffMixWebVo.getRequestClusterRefNo().get(i):"");
						rp.setClusterRemark(changeStaffMixWebVo.getRequestClusterRemark().size()!=0?changeStaffMixWebVo.getRequestClusterRemark().get(i):"");
						rp.setSubSpecialtyCode(changeStaffMixWebVo.getRequestSubSpecialty().size()!=0?changeStaffMixWebVo.getRequestSubSpecialty().get(i):"");
						
						System.out.println("--Funding Details Tab --");
						
						List<String> annualPlanList = new ArrayList<String>();
						List<String> programTypeList = new ArrayList<String>();
						
						if (changeStaffMixWebVo.getRequestPositionToList() != null) {
							if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList() != null) {
								for (int j=0; j<changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().size(); j++) {
									RequestFundingPo fundingPo = null;
									if (j < rp.getRequestFundingList().size()) {
										fundingPo = rp.getRequestFundingList().get(j);
									}
									else {
										fundingPo = new RequestFundingPo();
									}
									
									fundingPo.setFundingSeqNo(j+1);
									
									if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getAnnualPlanInd() != null) {
										fundingPo.setAnnualPlanInd(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getAnnualPlanInd());
									}
									else {
										fundingPo.setAnnualPlanInd("");
									}
									System.out.println("fundingPo.getAnnualPlanInd(): " + fundingPo.getAnnualPlanInd());
									annualPlanList.add(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getAnnualPlanInd());
									
									if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramYear() != null) {
										fundingPo.setProgramYear(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramYear());
									}
									else {
										fundingPo.setProgramYear("");
									}
									
									if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramCode() != null) {
										fundingPo.setProgramCode(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramCode());
									}
									else {
										fundingPo.setProgramCode("");
									}
									
									if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramName() != null) {
										fundingPo.setProgramName(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramName());
									}
									else {
										fundingPo.setProgramName("");
									}
									
									if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramTypeCode() != null) {
										fundingPo.setProgramTypeCode(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramTypeCode());
									}
									else {
										fundingPo.setProgramTypeCode("");
									}
									programTypeList.add(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramTypeCode());
									
									if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcId() != null) {
										fundingPo.setFundSrcId(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcId());
									}
									else {
										fundingPo.setFundSrcId("");
									}
									
									if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcSubCatId() != null) {
										fundingPo.setFundSrcSubCatId(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcSubCatId());
									}
									else {
										fundingPo.setFundSrcSubCatId("");
									}
									
									if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getStartDateStr() != null &&
											!"".equals(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getStartDateStr())) {
										fundingPo.setFundSrcStartDate(sdf.parse(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getStartDateStr()));
									}
									else {
										fundingPo.setFundSrcStartDate(sdf.parse(changeStaffMixWebVo.getRequestPostStartDate().get(i)));
									}
									if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getEndDateStr() != null &&
											!"".equals(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getEndDateStr())) {
										fundingPo.setFundSrcEndDate(sdf.parse(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getEndDateStr()));
									}
									else {
										fundingPo.setFundSrcEndDate(null);
									}
									
									if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcRemark() != null) {
										fundingPo.setFundSrcRemark(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcRemark());
									}
									else {
										fundingPo.setFundSrcRemark("");
									}
									
									if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcFte() != null) {
										fundingPo.setFundSrcFte(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcFte());
									}
									else {
										fundingPo.setFundSrcFte(null);
									}
									
									if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getInst() != null) {
										fundingPo.setInst(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getInst());
									}
									else {
										fundingPo.setInst("");
									}
									
									if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getSection() != null) {
										fundingPo.setSection(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getSection());
									}
									else {
										fundingPo.setSection("");
									}
									
									if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getAnalytical() != null) {
										fundingPo.setAnalytical(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getAnalytical());
									}
									else {
										fundingPo.setAnalytical("");
									}
									
									fundingPo.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
									System.out.println("XXXXXXX: " + fundingPo.getFundSrcRemark());
									if (j >= rp.getRequestFundingList().size()) {
										rp.addRequestFundingList(fundingPo);
									}
								}
							}
						}
						
						// For Request funding inactive
						if (rp.getRequestFundingList().size() > changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().size()) {
							for (int v=changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().size(); v<rp.getRequestFundingList().size(); v++) {
								rp.getRequestFundingList().get(v).setRecState(MPRSConstant.MPRS_STATE_INACTIVE);
							}
						}
						
						String clusterCode = changeStaffMixWebVo.getRequestCluster().size()!=0?changeStaffMixWebVo.getRequestCluster().get(i):"";
						String instCode = changeStaffMixWebVo.getRequestInst().size()!=0?changeStaffMixWebVo.getRequestInst().get(i):"";
						String deptCode = changeStaffMixWebVo.getRequestDept().size()!=0?changeStaffMixWebVo.getRequestDept().get(i):"";
						String rankCode = changeStaffMixWebVo.getRequestRank().size()!=0?changeStaffMixWebVo.getRequestRank().get(i):"";
						String postFte = changeStaffMixWebVo.getRequestPostFTE().size()!=0?changeStaffMixWebVo.getRequestPostFTE().get(i):"";
						String proposedPostId = changeStaffMixWebVo.getRequestProposedPostId().size()!=0?changeStaffMixWebVo.getRequestProposedPostId().get(i):"";
						String staffGroupCode  = changeStaffMixWebVo.getRequestStaffGroup().size()!=0?changeStaffMixWebVo.getRequestStaffGroup().get(i):"";
						String postDuration = changeStaffMixWebVo.getRequestPostDuration().size()!=0?changeStaffMixWebVo.getRequestPostDuration().get(i):"";
						System.out.println("changeStaffMixWebVo.getCluster(): " + clusterCode); 
						System.out.println("changeStaffMixWebVo.getInstitution(): " + instCode); 
						System.out.println("changeStaffMixWebVo.getDepartment(): " +deptCode); 
						System.out.println("changeStaffMixWebVo.getRank(): " + rankCode);
						System.out.println("changeStaffMixWebVo.getPostFTE(): " + postFte);
						System.out.println("changeStaffMixWebVo.getproposedPostId(): " + proposedPostId);
						System.out.println("changeStaffMixWebVo.getpostDuration(): " + postDuration);
						
						boolean haveAnnualPlan = false;
						for (int k=0; k<annualPlanList.size(); k++) {
							if (!"".equals(annualPlanList.get(k)) && annualPlanList.get(k) != null) {
								haveAnnualPlan = true;
								break;
							}
						}
						
						if ("".equals(proposedPostId)) {
							if (!"".equals(clusterCode) && 
									!"".equals(instCode) && 
									!"".equals(deptCode) && 
									!"".equals(rankCode) && 
									haveAnnualPlan &&
									!"".equals(postFte)) {
	
								String postId = requestSvc.generateNewPostId(
										staffGroupCode,
										clusterCode,
										instCode, 
										deptCode,
										rankCode,
										annualPlanList,
										programTypeList,
										postDuration,
										postFte,
										needRecycle,
										tmpProposedPostId);
	
								rp.setProposedPostId(postId);
							}
						}
						else {
							rp.setProposedPostId(proposedPostId);
						}
						
						System.out.println("--Resources Support Tab Saved --");
						rfs.setResourcesSupportFrExt(changeStaffMixWebVo.getRequestResSupFrExt().size()!=0?changeStaffMixWebVo.getRequestResSupFrExt().get(i):"");
						rfs.setResourcesSupportRemark(changeStaffMixWebVo.getRequestResSupRemark().size()!=0?changeStaffMixWebVo.getRequestResSupRemark().get(i):"");
						rp.setRequestFundingResource(rfs);
						
						if (requestPositionToList.size() > i) {
							int tmp = 0;
							for (int u=0; u<newRequest.getRequestPositionList().size(); u++) {
								if ("Y".equals(newRequest.getRequestPositionList().get(u).getToPostInd())) {
									if (tmp == rowId2) {
										newRequest.getRequestPositionList().set(u, rp);
									}
									else {
										tmp ++;
									}
								}
							}
							
							rowId2 ++;
						}
						else {
							newRequest.addRequestPosition(rp);
						}
					}
					
					int aFrom = 0;
					int aTo = 0;
					for (int i=0; i<newRequest.getRequestPositionList().size(); i++) {
						if ("Y".equals(newRequest.getRequestPositionList().get(i).getFromPostInd())) {
							aFrom ++;
							if (aFrom > rowId1) {
								System.out.println("Perform set (From) inactive. " + newRequest.getRequestPositionList().get(i).getRequestPostId());
								newRequest.getRequestPositionList().get(i).setRecState(MPRSConstant.MPRS_STATE_INACTIVE);
							}
						}
						else {
							aTo ++;
							if (aTo > rowId2) {
								System.out.println("Perform set (To) inactive. " + newRequest.getRequestPositionList().get(i).getRequestPostId());
								newRequest.getRequestPositionList().get(i).setRecState(MPRSConstant.MPRS_STATE_INACTIVE);
							}
						}
					}
				}
				else {
					Calendar now = Calendar.getInstance();
					String scurrentYear;
					scurrentYear = String.valueOf(now.get(Calendar.YEAR));	
					
					newRequest= new RequestPo();	
					
					// Get the first cluster and inst of "To Post"
					String tmpClusterCode = changeStaffMixWebVo.getRequestCluster().size()!=0?changeStaffMixWebVo.getRequestCluster().get(0):"";
					String tmpInstCode = changeStaffMixWebVo.getRequestInst().size()!=0?changeStaffMixWebVo.getRequestInst().get(0):"";
					
					String tmpRequestId = requestSvc.generateRequestNo(tmpClusterCode, tmpInstCode, "COM", scurrentYear);
					System.out.println("tmpRequestIdCFM: " + tmpRequestId);
	
					newRequest.setRequestId(tmpRequestId);
					RequestTypePo requestType = new RequestTypePo();
					requestType.setRqTypeCode(MPRSConstant.REQUEST_TYPE_CHANGE_STAFF_MIX);
					newRequest.setRequestType(requestType);
					
					RequestStatusPo requestStatus = new RequestStatusPo();
					requestStatus.setStatusCode(MPRSConstant.REQUEST_STATUS_NEW);
					newRequest.setRequestStatus(requestStatus);
					newRequest.setRequestDate(new Date());
					if (changeStaffMixWebVo.getEffectiveDate() != null && !"".equals(changeStaffMixWebVo.getEffectiveDate())) {
						newRequest.setEffectFrom(sdf.parse(changeStaffMixWebVo.getEffectiveDate()));
					}
					newRequest.setRequestReason(changeStaffMixWebVo.getRequestReason());
					newRequest.setCurrentWFUser(userId);
					newRequest.setCurrentWFGroup(currentRole);
					newRequest.setRequester(changeStaffMixWebVo.getRequester());
					newRequest.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
					
					if (changeStaffMixWebVo.getApprovalDate() != null && !"".equals(changeStaffMixWebVo.getApprovalDate())) {
						newRequest.setApprovalDate(sdf.parse(changeStaffMixWebVo.getApprovalDate()));
					}
					
					newRequest.setApprovalReference(changeStaffMixWebVo.getApprovalReference());
					newRequest.setApprovalRemark(changeStaffMixWebVo.getApprovalRemark());
					// List<RequestPosition> tmpList = new ArrayList<RequestPosition>();
					// newRequest.setRequestPositionList(tmpList);
					// for (int x= 0; x<newRequest.getRequestPositionList().size(); x++) {
					//	newRequest.removeRequestPosition(newRequest.getRequestPositionList().get(0));
					//  }
					for (int i=0; i<changeStaffMixWebVo.getRequestPostNo().size(); i++) {
						// RequestPosition rp = requestSvc.getRequestPositionByPostNo(Integer.parseInt(ChangeStaffMixWebVo.getRequestPostNo().get(i)));
						System.out.println("Add RequestPoPosition!!");
						System.out.println("ChangeStaffMixWebVo.getRequestPostNo().get(i): " + changeStaffMixWebVo.getRequestPostNo().get(i));
						
						RequestPostPo rp = new RequestPostPo();
						PostMasterPo mprsPost = requestSvc.getPostByPostUid(Integer.parseInt(changeStaffMixWebVo.getRequestPostNo().get(i)));
						rp.setPostNo(changeStaffMixWebVo.getRequestPostNo().get(i));
						RankPo rank = new RankPo();
						rank.setRankCode(mprsPost.getRankCode());
						rp.setPostDuration(mprsPost.getPostDuration());
						rp.setPostFTE(String.valueOf(mprsPost.getPostFTE()));
						rp.setRank(rank);
						rp.setPostId(mprsPost.getPostId());
						rp.setClusterCode(mprsPost.getClusterCode());
						rp.setInstCode(mprsPost.getInstCode());
						rp.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
						rp.setFromPostInd("Y");
						rp.setStaffGroupCode(mprsPost.getStaffGroupCode());
						newRequest.addRequestPosition(rp);
					}
					
					for (int i=0; i<changeStaffMixWebVo.getRequestCluster().size(); i++) {
						System.out.println("Add RequestPoPosition To!!");
						
						RequestPostPo rp = new RequestPostPo();
						
						if (changeStaffMixWebVo.getRequestRank().size()!=0) {
							RankPo rank = new RankPo();
							rank.setRankCode(changeStaffMixWebVo.getRequestRank().get(i));
							rp.setRank(rank);
						}
						rp.setPostId("");
						rp.setClusterCode(changeStaffMixWebVo.getRequestCluster().size()!=0?changeStaffMixWebVo.getRequestCluster().get(i):"");
						rp.setInstCode(changeStaffMixWebVo.getRequestInst().size()!=0?changeStaffMixWebVo.getRequestInst().get(i):"");
						rp.setDeptCode(changeStaffMixWebVo.getRequestDept().size()!=0?changeStaffMixWebVo.getRequestDept().get(i):"");
						rp.setStaffGroupCode(changeStaffMixWebVo.getRequestStaffGroup().size()!=0?changeStaffMixWebVo.getRequestStaffGroup().get(i):"");
						rp.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
						rp.setHcmPositionId(changeStaffMixWebVo.getHcmPositionId().size()!=0?changeStaffMixWebVo.getHcmPositionId().get(i):"");
						rp.setHoBuyServiceInd(changeStaffMixWebVo.getRequestHoBuyServiceInd().size()!=0?changeStaffMixWebVo.getRequestHoBuyServiceInd().get(i):"");
						rp.setToPostInd("Y");
						
						System.out.println("--Position Tab Saved --");
						rp.setUnit(changeStaffMixWebVo.getRequestUnit().size()!=0?changeStaffMixWebVo.getRequestUnit().get(i):"");
						rp.setPostTitle(changeStaffMixWebVo.getRequestPostTitle().size()!=0?changeStaffMixWebVo.getRequestPostTitle().get(i):"");
						rp.setPostDuration(changeStaffMixWebVo.getRequestPostDuration().size()!=0?changeStaffMixWebVo.getRequestPostDuration().get(i):"");
						rp.setLimitDurationType(changeStaffMixWebVo.getRequestRdDuration().size()!=0?changeStaffMixWebVo.getRequestRdDuration().get(i):"");
						if (changeStaffMixWebVo.getRequestPostStartDate().size() !=0 ) {
							if (!"".equals(changeStaffMixWebVo.getRequestPostStartDate().get(i))) { 
								rp.setPostStartDate(sdf.parse(changeStaffMixWebVo.getRequestPostStartDate().get(i)));//review
							}
						}
						rp.setLimitDurationNo(changeStaffMixWebVo.getRequestDurationValue().size()!=0?changeStaffMixWebVo.getRequestDurationValue().get(i):null);
						rp.setLimitDurationUnit(changeStaffMixWebVo.getRequestDurationUnit().size()!=0?changeStaffMixWebVo.getRequestDurationUnit().get(i):"");
						if (changeStaffMixWebVo.getRequestPostActualEndDate().size() != 0) {
							if (!"".equals(changeStaffMixWebVo.getRequestPostActualEndDate().get(i))) {
								rp.setLimitDurationEndDate(sdf.parse(changeStaffMixWebVo.getRequestPostActualEndDate().get(i)));
							}
							else {
								rp.setLimitDurationEndDate(null);
							}
						}
						
						// Update the Fixed End Date if selected (Time-Limited) and Duration Period
						if (!"R".equals(rp.getPostDuration()) && "DURATION_PERIOD".equals(rp.getLimitDurationType())) {
							int duration = 0;
							if ("Y".equals(rp.getLimitDurationUnit())) {
								duration = rp.getLimitDurationNo() * 12;
							}
							else if ("M".equals(rp.getLimitDurationUnit())) {
								duration = rp.getLimitDurationNo();
							}
							
							rp.setLimitDurationEndDate(requestSvc.addMonth(rp.getPostStartDate(), duration));
						}
						
						if ("R".equals(rp.getPostDuration())) {
							rp.setLimitDurationEndDate(null);
						}
						
						rp.setPostRemark(changeStaffMixWebVo.getRequestPostRemark().size()!=0?changeStaffMixWebVo.getRequestPostRemark().get(i):"");
						rp.setPostFTEType(changeStaffMixWebVo.getRequestPostFTE().size()!=0?changeStaffMixWebVo.getRequestPostFTE().get(i):"");
						rp.setPostFTE(changeStaffMixWebVo.getRequestPostFTEValue().size()!=0?changeStaffMixWebVo.getRequestPostFTEValue().get(i):"");
						rp.setPostStatus(changeStaffMixWebVo.getRequestPositionStatus().size()!=0?changeStaffMixWebVo.getRequestPositionStatus().get(i):"");
						if (changeStaffMixWebVo.getRequestPositionStartDate().size() != 0) {
							if (!"".equals(changeStaffMixWebVo.getRequestPositionStartDate().get(i))) {
								rp.setPostStatusStartDate(sdf.parse(changeStaffMixWebVo.getRequestPositionStartDate().get(i)));
							}
						}
						if (changeStaffMixWebVo.getRequestPositionEndDate().size() != 0) {
							if (!"".equals(changeStaffMixWebVo.getRequestPositionEndDate().get(i))) {
								rp.setPostStatusEndDate(sdf.parse(changeStaffMixWebVo.getRequestPositionEndDate().get(i)));
							}
						}
						rp.setClusterRef(changeStaffMixWebVo.getRequestClusterRefNo().size()!=0?changeStaffMixWebVo.getRequestClusterRefNo().get(i):"");
						rp.setClusterRemark(changeStaffMixWebVo.getRequestClusterRemark().size()!=0?changeStaffMixWebVo.getRequestClusterRemark().get(i):"");
						rp.setSubSpecialtyCode(changeStaffMixWebVo.getRequestSubSpecialty().size()!=0?changeStaffMixWebVo.getRequestSubSpecialty().get(i):"");
						System.out.println("--Funding Details Tab --");
						List<String> annualPlanList = new ArrayList<String>();
						List<String> programTypeList = new ArrayList<String>();
						
						if (changeStaffMixWebVo.getRequestPositionToList() != null) {
							if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList() != null) {
								for (int j=0; j<changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().size(); j++) {
									RequestFundingPo fundingPo = null;
									if (j < rp.getRequestFundingList().size()) {
										fundingPo = rp.getRequestFundingList().get(j);
									}
									else {
										fundingPo = new RequestFundingPo();
									}
									
									fundingPo.setFundingSeqNo(j+1);
									
									if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getAnnualPlanInd() != null) {
										fundingPo.setAnnualPlanInd(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getAnnualPlanInd());
									}
									else {
										fundingPo.setAnnualPlanInd("");
									}
									
									annualPlanList.add(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getAnnualPlanInd());
									
									if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramYear() != null) {
										fundingPo.setProgramYear(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramYear());
									}
									else {
										fundingPo.setProgramYear("");
									}
									
									if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramCode() != null) {
										fundingPo.setProgramCode(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramCode());
									}
									else {
										fundingPo.setProgramCode("");
									}
									
									if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramName() != null) {
										fundingPo.setProgramName(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramName());
									}
									else {
										fundingPo.setProgramName("");
									}
									
									if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramTypeCode() != null) {
										fundingPo.setProgramTypeCode(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramTypeCode());
									}
									else {
										fundingPo.setProgramTypeCode("");
									}
									
									programTypeList.add(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramTypeCode());
									
									if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcId() != null) {
										fundingPo.setFundSrcId(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcId());
									}
									else {
										fundingPo.setFundSrcId("");
									}
									
									if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcSubCatId() != null) {
										fundingPo.setFundSrcSubCatId(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcSubCatId());
									}
									else {
										fundingPo.setFundSrcSubCatId("");
									}
									
									if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getStartDateStr() != null &&
											!"".equals(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getStartDateStr())) {
										fundingPo.setFundSrcStartDate(sdf.parse(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getStartDateStr()));
									}
									else {
										fundingPo.setFundSrcStartDate(sdf.parse(changeStaffMixWebVo.getRequestPostStartDate().get(i)));
									}
									if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getEndDateStr() != null &&
											!"".equals(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getEndDateStr())) {
										fundingPo.setFundSrcEndDate(sdf.parse(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getEndDateStr()));
									}
									else {
										fundingPo.setFundSrcEndDate(null);
									}
									
									if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcFte() != null) {
										fundingPo.setFundSrcFte(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcFte());
									}
									else {
										fundingPo.setFundSrcFte(null);
									}

									if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcRemark() != null) {
										fundingPo.setFundSrcRemark(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcRemark());
									}
									else {
										fundingPo.setFundSrcRemark("");
									}
									
									if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getInst() != null) {
										fundingPo.setInst(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getInst());
									}
									else {
										fundingPo.setInst("");
									}
									
									if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getSection() != null) {
										fundingPo.setSection(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getSection());
									}
									else {
										fundingPo.setSection("");
									}
									
									if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getAnalytical() != null) {
										fundingPo.setAnalytical(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getAnalytical());
									}
									else {
										fundingPo.setAnalytical("");
									}
									
									fundingPo.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
									if (j >= rp.getRequestFundingList().size()) {
										rp.addRequestFundingList(fundingPo);
									}
								}
							}
						}
						
						// For Request funding inactive
						if (rp.getRequestFundingList().size() > changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().size()) {
							for (int v=changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().size(); v<rp.getRequestFundingList().size(); v++) {
								rp.getRequestFundingList().get(v).setRecState(MPRSConstant.MPRS_STATE_INACTIVE);
							}
						}
												
						
						String clusterCode = changeStaffMixWebVo.getRequestCluster().size()!=0?changeStaffMixWebVo.getRequestCluster().get(i):"";
						String instCode = changeStaffMixWebVo.getRequestInst().size()!=0?changeStaffMixWebVo.getRequestInst().get(i):"";
						String deptCode = changeStaffMixWebVo.getRequestDept().size()!=0?changeStaffMixWebVo.getRequestDept().get(i):"";
						String rankCode = changeStaffMixWebVo.getRequestRank().size()!=0?changeStaffMixWebVo.getRequestRank().get(i):"";
						String postFte = changeStaffMixWebVo.getRequestPostFTE().size()!=0?changeStaffMixWebVo.getRequestPostFTE().get(i):"";
						String proposedPostId = changeStaffMixWebVo.getRequestProposedPostId().size()!=0?changeStaffMixWebVo.getRequestProposedPostId().get(i):"";
						String staffGroupCode  = changeStaffMixWebVo.getRequestStaffGroup().size()!=0?changeStaffMixWebVo.getRequestStaffGroup().get(i):"";
						String postDuration = changeStaffMixWebVo.getRequestPostDuration().size()!=0?changeStaffMixWebVo.getRequestPostDuration().get(i):"";
						System.out.println("changeStaffMixWebVo.getCluster(): " + clusterCode); 
						System.out.println("changeStaffMixWebVo.getInstitution(): " + instCode); 
						System.out.println("changeStaffMixWebVo.getDepartment(): " +deptCode); 
						System.out.println("changeStaffMixWebVo.getRank(): " + rankCode);
						System.out.println("changeStaffMixWebVo.getPostFTE(): " + postFte);
						System.out.println("changeStaffMixWebVo.getproposedPostId(): " + proposedPostId);
						System.out.println("changeStaffMixWebVo.getpostDuration(): " + postDuration);
						
						boolean haveAnnualPlan = false;
						for (int k=0; k<annualPlanList.size(); k++) {
							if (!"".equals(annualPlanList.get(k)) && annualPlanList.get(k) != null) {
								haveAnnualPlan = true;
								break;
							}
						}
						
						if ("".equals(proposedPostId)) {
							if (!"".equals(clusterCode) && 
									!"".equals(instCode) && 
									!"".equals(deptCode) && 
									!"".equals(rankCode) && 
									haveAnnualPlan &&
									!"".equals(postFte)) {
								
								// Newly add a post, generate Post ID
								String postId = requestSvc.generateNewPostId(
										staffGroupCode,
										clusterCode,
										instCode, 
										deptCode,
										rankCode,
										annualPlanList,
										programTypeList,
										postDuration,
										postFte,
										true,
										null);
	
								rp.setProposedPostId(postId);
							}
						}
						else {
							rp.setProposedPostId(proposedPostId);
						}
						
						System.out.println("--Funding Details Tab -3");
						
						System.out.println("--Resources Support Tab Saved 4--");
						RequestFundingResourcePo rfs = new RequestFundingResourcePo();
						rp.setRequestFundingResource(rfs);
						rfs.setResourcesSupportFrExt(changeStaffMixWebVo.getRequestResSupFrExt().size()!=0?changeStaffMixWebVo.getRequestResSupFrExt().get(i):"");
						rfs.setResourcesSupportRemark(changeStaffMixWebVo.getRequestResSupRemark().size()!=0?changeStaffMixWebVo.getRequestResSupRemark().get(i):"");
	
						newRequest.addRequestPosition(rp);
					}
				}
				
				List<RequestAttachmentPo> attachmentList = new ArrayList<RequestAttachmentPo>();
				if (changeStaffMixWebVo.getUploadFileId() != null && !"".equals(changeStaffMixWebVo.getUploadFileId())) {
					for (int i=0; i<changeStaffMixWebVo.getUploadFileId().size(); i++) {
						if (changeStaffMixWebVo.getUploadFileId().get(i) != null && !"".equals(changeStaffMixWebVo.getUploadFileId().get(i))) {
							RequestTempFilePo tempFile = requestSvc.getTempFile(Integer.parseInt(changeStaffMixWebVo.getUploadFileId().get(i)));
	
							RequestAttachmentPo attachment = new RequestAttachmentPo();
							attachment.setFileName(tempFile.getFilename());
							// attachment.setAttachment(tempFile.getContent());
							// newRequest.setAttachment(attachment);
							attachmentList.add(attachment);
						}
					}
				}	
				else {
					newRequest.setAttachment(null);
				}
					
					newRequest.setAttachment(attachmentList);
				
				// ChangeStaffMixWebVo.setRequestId(tmpRequestId);
				changeStaffMixWebVo.setRequestId(newRequest.getRequestId());
				changeStaffMixWebVo.setRequestStatus("N");
				int requestNo = -1;
				if (changeStaffMixWebVo.getRequestNo() != null && !"".equals(changeStaffMixWebVo.getRequestNo())) {
					requestSvc.update(newRequest, changeStaffMixWebVo.getRemoveAttachmentUid(), changeStaffMixWebVo.getUploadFileId(), user);
					requestNo = newRequest.getRequestNo();
				}
				else {
					requestNo = requestSvc.insert(newRequest, changeStaffMixWebVo.getUploadFileId(), true, null, null, user);
				}
	
				// Retrieve again the RequestPo
				RequestPo requestCase = requestSvc.getRequestByRequestNo(requestNo);
				
				changeStaffMixWebVo = convertToWebVo(requestCase, userId, currentRole);
				changeStaffMixWebVo.setDisplayMessage("Request update successful.");
				changeStaffMixWebVo.setUpdateSuccess("Y");
			}
			catch (Exception ex) {
				EClaimLogger.error("performUpdate - update:" + ex.getMessage(), ex);
				String errorMsg = doHandleException(ex.getCause(), ex.getMessage());
				
				if (changeStaffMixWebVo.getRequestNo() != null && !"".equals(changeStaffMixWebVo.getRequestNo())) {
					RequestPo requestCase = requestSvc.getRequestByRequestNo(Integer.parseInt(changeStaffMixWebVo.getRequestNo()));
					changeStaffMixWebVo = convertToWebVo(requestCase, userId, currentRole);
				}
				
				System.out.println("errorMsg: " + errorMsg);
				changeStaffMixWebVo.setUpdateSuccess("N");
				changeStaffMixWebVo.setDisplayMessage(errorMsg);
			}
		}
		else if ("WITHDRAW".equalsIgnoreCase(formAction)) {
			System.out.println("RequestPoNo.: " + changeStaffMixWebVo.getRequestNo());
			int requestNo = Integer.parseInt(changeStaffMixWebVo.getRequestNo());
			String errorMsg = requestSvc.submitWithdraw(requestNo,  MPRSConstant.REQUEST_TYPE_CHANGE_STAFF_MIX, userId, currentRole);

			if ("".equals(errorMsg) || errorMsg == null) {
				// Retrieve again the RequestPo
				RequestPo requestCase = requestSvc.getRequestByRequestNo(requestNo);
				
				changeStaffMixWebVo = convertToWebVo(requestCase, userId, currentRole);
				changeStaffMixWebVo.setUpdateSuccess("Y");
				changeStaffMixWebVo.setDisplayMessage("Request withdraw successful.");
			}
			else {
				System.out.println("errorMsg: " + errorMsg);
				changeStaffMixWebVo.setUpdateSuccess("N");
				changeStaffMixWebVo.setDisplayMessage(errorMsg);
			}
			
			return new ModelAndView("request/changeStaffMix", "formBean", changeStaffMixWebVo);
		}
		else if ("SUBMIT".equalsIgnoreCase(formAction)) {
			if ("FIN_OFFICER".equals(currentRole) || "HR_OFFICER".equals(currentRole)) {
				int rowId1 = 0;
				int rowId2 = 0;
				System.out.println("Location ChangeOfStaffMixController.performUpdate()");
				System.out.println("RequestPoId: " + changeStaffMixWebVo.getRequestId());
				System.out.println("Requester " + changeStaffMixWebVo.getRequester());

				RequestPo newRequest= null;	
				newRequest = requestSvc.getRequestByRequestNo(Integer.parseInt(changeStaffMixWebVo.getRequestNo()));

				if (changeStaffMixWebVo.getEffectiveDate() != null && !"".equals(changeStaffMixWebVo.getEffectiveDate())) {
					newRequest.setEffectFrom(sdf.parse(changeStaffMixWebVo.getEffectiveDate()));
				}
				newRequest.setRequestReason(changeStaffMixWebVo.getRequestReason());
				newRequest.setRequester(changeStaffMixWebVo.getRequester());

				if (changeStaffMixWebVo.getApprovalDate() != null && !"".equals(changeStaffMixWebVo.getApprovalDate())) {
					newRequest.setApprovalDate(sdf.parse(changeStaffMixWebVo.getApprovalDate()));
				}

				newRequest.setApprovalReference(changeStaffMixWebVo.getApprovalReference());
				newRequest.setApprovalRemark(changeStaffMixWebVo.getApprovalRemark());

				List<RequestPostPo> requestPositionFromList = new ArrayList<RequestPostPo>();
				List<RequestPostPo> requestPositionToList = new ArrayList<RequestPostPo>();
				
				for (int m=0; m<newRequest.getRequestPositionList().size(); m++) {
					if (MPRSConstant.MPRS_STATE_ACTIVE.equals(newRequest.getRequestPositionList().get(m).getRecState())) {
						if("Y".equals(newRequest.getRequestPositionList().get(m).getFromPostInd())) {
							requestPositionFromList.add(newRequest.getRequestPositionList().get(m));
						}
						else {
							requestPositionToList.add(newRequest.getRequestPositionList().get(m));
						}
					}
				}
				
				System.out.println("requestPositionFromList: " + requestPositionFromList.size());
				System.out.println("requestPositionToList: " + requestPositionToList.size());
				
				for (int i=0; i<changeStaffMixWebVo.getRequestPostNo().size(); i++) {
					System.out.println("Add Request Position!!");

					RequestPostPo rp = null;
					
					if (requestPositionFromList.size() > i) {
						rp = requestPositionFromList.get(i);
					}
					else {
						rp = new RequestPostPo();
					}
					
					PostMasterPo mprsPost = requestSvc.getPostByPostUid(Integer.parseInt(changeStaffMixWebVo.getRequestPostNo().get(i)));
					rp.setPostNo(changeStaffMixWebVo.getRequestPostNo().get(i));
					RankPo rank = new RankPo();
					rank.setRankCode(mprsPost.getRankCode());
					rp.setPostDuration(mprsPost.getPostDuration());
					rp.setPostFTE(String.valueOf(mprsPost.getPostFTE()));
					rp.setRank(rank);
					rp.setPostId(mprsPost.getPostId());
					rp.setClusterCode(mprsPost.getClusterCode());
					rp.setInstCode(mprsPost.getInstCode());
					rp.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
					rp.setFromPostInd("Y");
					rp.setStaffGroupCode(mprsPost.getStaffGroupCode());
					
					if (requestPositionFromList.size() > i) {
						int tmp = 0;
						for (int u=0; u<newRequest.getRequestPositionList().size(); u++) {
							if ("Y".equals(newRequest.getRequestPositionList().get(u).getFromPostInd())) {
								if (tmp == rowId1) {
									newRequest.getRequestPositionList().set(u, rp);
								}
								else {
									tmp ++;
								}
							}
						}
						
						rowId1 ++;
					}
					else {
						newRequest.addRequestPosition(rp);
					}
				}
				
				for (int i=0; i<changeStaffMixWebVo.getRequestCluster().size(); i++) {
					RequestPostPo rp = null;
					RequestFundingResourcePo rfs = null;
					
					if (requestPositionToList.size() > i) {
						rp = requestPositionToList.get(i);
						System.out.println("Check 1: " + rp.getRequestPostId());
						rfs = rp.getRequestFundingResource();
					}
					else {
						rp = new RequestPostPo();
						rfs = new RequestFundingResourcePo();
						rp.setRequestFundingResource(rfs);
					}
					
					boolean needRecycle = true;
					String tmpProposedPostId = rp.getProposedPostId();
					if (tmpProposedPostId != null) {
						System.out.println("tmpProposedPostId: " + tmpProposedPostId);
						System.out.println("changeStaffMixWebVo: " + changeStaffMixWebVo);
						
						String requestProposedPostId = changeStaffMixWebVo.getRequestProposedPostId().size() != 0 ? changeStaffMixWebVo.getRequestProposedPostId().get(i) : "";
						if (tmpProposedPostId.equals(requestProposedPostId)) {
							needRecycle = false;
						} else {
							System.out.println("Recycle: " + tmpProposedPostId);
							requestSvc.recycleProposedPostId(rp.getStaffGroupCode(), rp.getPostDuration(), rp.getPostFTEType(), tmpProposedPostId, userId, currentRole);
						}
					}
					
					System.out.println("Add RequestPoPosition To!!");
					
					if (changeStaffMixWebVo.getRequestRank().size()!=0) {
						RankPo rank = new RankPo();
						rank.setRankCode(changeStaffMixWebVo.getRequestRank().get(i));
						rp.setRank(rank);
					}
					
					rp.setClusterCode(changeStaffMixWebVo.getRequestCluster().size()!=0?changeStaffMixWebVo.getRequestCluster().get(i):"");
					rp.setInstCode(changeStaffMixWebVo.getRequestInst().size()!=0?changeStaffMixWebVo.getRequestInst().get(i):"");
					rp.setDeptCode(changeStaffMixWebVo.getRequestDept().size()!=0?changeStaffMixWebVo.getRequestDept().get(i):"");
					rp.setStaffGroupCode(changeStaffMixWebVo.getRequestStaffGroup().size()!=0?changeStaffMixWebVo.getRequestStaffGroup().get(i):"");
					rp.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
					rp.setHcmPositionId(changeStaffMixWebVo.getHcmPositionId().size()!=0?changeStaffMixWebVo.getHcmPositionId().get(i):"");
					rp.setHoBuyServiceInd(changeStaffMixWebVo.getRequestHoBuyServiceInd().size()!=0?changeStaffMixWebVo.getRequestHoBuyServiceInd().get(i):"");
					rp.setToPostInd("Y");
					
					System.out.println("--Position Tab Saved --");
					rp.setUnit(changeStaffMixWebVo.getRequestUnit().size()!=0?changeStaffMixWebVo.getRequestUnit().get(i):"");
					rp.setPostTitle(changeStaffMixWebVo.getRequestPostTitle().size()!=0?changeStaffMixWebVo.getRequestPostTitle().get(i):"");
					rp.setPostDuration(changeStaffMixWebVo.getRequestPostDuration().size()!=0?changeStaffMixWebVo.getRequestPostDuration().get(i):"");
					rp.setLimitDurationType(changeStaffMixWebVo.getRequestRdDuration().size()!=0?changeStaffMixWebVo.getRequestRdDuration().get(i):"");
					if (changeStaffMixWebVo.getRequestPostStartDate().size() !=0 ) {
						if (!"".equals(changeStaffMixWebVo.getRequestPostStartDate().get(i))) {
							rp.setPostStartDate(sdf.parse(changeStaffMixWebVo.getRequestPostStartDate().get(i)));//review
						}
					}
					rp.setLimitDurationNo(changeStaffMixWebVo.getRequestDurationValue().size()!=0?changeStaffMixWebVo.getRequestDurationValue().get(i):null);
					rp.setLimitDurationUnit(changeStaffMixWebVo.getRequestDurationUnit().size()!=0?changeStaffMixWebVo.getRequestDurationUnit().get(i):"");
					if (changeStaffMixWebVo.getRequestPostActualEndDate().size() != 0) {
						if (!"".equals(changeStaffMixWebVo.getRequestPostActualEndDate().get(i))) {
							rp.setLimitDurationEndDate(sdf.parse(changeStaffMixWebVo.getRequestPostActualEndDate().get(i)));
						}
						else {
							rp.setLimitDurationEndDate(null);
						}
					}
					
					// Update the Fixed End Date if selected (Time-Limited) and Duration Period
					if (!"R".equals(rp.getPostDuration()) && "DURATION_PERIOD".equals(rp.getLimitDurationType())) {
						int duration = 0;
						if ("Y".equals(rp.getLimitDurationUnit())) {
							duration = rp.getLimitDurationNo() * 12;
						}
						else if ("M".equals(rp.getLimitDurationUnit())) {
							duration = rp.getLimitDurationNo();
						}
						
						rp.setLimitDurationEndDate(requestSvc.addMonth(rp.getPostStartDate(), duration));
					}
					
					if ("R".equals(rp.getPostDuration())) {
						rp.setLimitDurationEndDate(null);
					}
					
					rp.setPostRemark(changeStaffMixWebVo.getRequestPostRemark().size()!=0?changeStaffMixWebVo.getRequestPostRemark().get(i):"");
					rp.setPostFTEType(changeStaffMixWebVo.getRequestPostFTE().size()!=0?changeStaffMixWebVo.getRequestPostFTE().get(i):"");
					rp.setPostFTE(changeStaffMixWebVo.getRequestPostFTEValue().size()!=0?changeStaffMixWebVo.getRequestPostFTEValue().get(i):"");
					rp.setPostStatus(changeStaffMixWebVo.getRequestPositionStatus().size()!=0?changeStaffMixWebVo.getRequestPositionStatus().get(i):"");
					if (changeStaffMixWebVo.getRequestPositionStartDate().size() != 0) {
						if (!"".equals(changeStaffMixWebVo.getRequestPositionStartDate().get(i))) {
							rp.setPostStatusStartDate(sdf.parse(changeStaffMixWebVo.getRequestPositionStartDate().get(i)));
						}
					}
					if (changeStaffMixWebVo.getRequestPositionEndDate().size() != 0) {	
						if (!"".equals(changeStaffMixWebVo.getRequestPositionEndDate().get(i))) {
							rp.setPostStatusEndDate(sdf.parse(changeStaffMixWebVo.getRequestPositionEndDate().get(i)));
						}
					}
					rp.setClusterRef(changeStaffMixWebVo.getRequestClusterRefNo().size()!=0?changeStaffMixWebVo.getRequestClusterRefNo().get(i):"");
					rp.setClusterRemark(changeStaffMixWebVo.getRequestClusterRemark().size()!=0?changeStaffMixWebVo.getRequestClusterRemark().get(i):"");
					rp.setSubSpecialtyCode(changeStaffMixWebVo.getRequestSubSpecialty().size()!=0?changeStaffMixWebVo.getRequestSubSpecialty().get(i):"");
					
					System.out.println("--Funding Details Tab --");
					
					List<String> annualPlanList = new ArrayList<String>();
					List<String> programTypeList = new ArrayList<String>();
					
					if (changeStaffMixWebVo.getRequestPositionToList() != null) {
						if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList() != null) {
							for (int j=0; j<changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().size(); j++) {
								RequestFundingPo fundingPo = null;
								if (j < rp.getRequestFundingList().size()) {
									fundingPo = rp.getRequestFundingList().get(j);
								}
								else {
									fundingPo = new RequestFundingPo();
								}
								
								fundingPo.setFundingSeqNo(j+1);
								
								if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getAnnualPlanInd() != null) {
									fundingPo.setAnnualPlanInd(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getAnnualPlanInd());
								}
								else {
									fundingPo.setAnnualPlanInd("");
								}
								
								annualPlanList.add(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getAnnualPlanInd());
								
								if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramYear() != null) {
									fundingPo.setProgramYear(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramYear());
								}
								else {
									fundingPo.setProgramYear("");
								}
								
								if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramCode() != null) {
									fundingPo.setProgramCode(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramCode());
								}
								else {
									fundingPo.setProgramCode("");
								}
								
								if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramName() != null) {
									fundingPo.setProgramName(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramName());
								}
								else {
									fundingPo.setProgramName("");
								}
								
								if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramTypeCode() != null) {
									fundingPo.setProgramTypeCode(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramTypeCode());
								}
								else {
									fundingPo.setProgramTypeCode("");
								}
								
								programTypeList.add(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramTypeCode());
								
								if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcId() != null) {
									fundingPo.setFundSrcId(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcId());
								}
								else {
									fundingPo.setFundSrcId("");
								}
								
								if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcSubCatId() != null) {
									fundingPo.setFundSrcSubCatId(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcSubCatId());
								}
								else {
									fundingPo.setFundSrcSubCatId("");
								}
								
								if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getStartDateStr() != null &&
										!"".equals(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getStartDateStr())) {
									fundingPo.setFundSrcStartDate(sdf.parse(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getStartDateStr()));
								}
								else {
									fundingPo.setFundSrcStartDate(sdf.parse(changeStaffMixWebVo.getRequestPostStartDate().get(i)));
								}
								if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getEndDateStr() != null &&
										!"".equals(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getEndDateStr())) {
									fundingPo.setFundSrcEndDate(sdf.parse(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getEndDateStr()));
								}
								else {
									fundingPo.setFundSrcEndDate(null);
								}
								
								if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcFte() != null) {
									fundingPo.setFundSrcFte(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcFte());
								}
								else {
									fundingPo.setFundSrcFte(null);
								}

								if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcRemark() != null) {
									fundingPo.setFundSrcRemark(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcRemark());
								}
								else {
									fundingPo.setFundSrcRemark("");
								}
								
								if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getInst() != null) {
									fundingPo.setInst(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getInst());
								}
								else {
									fundingPo.setInst("");
								}
								
								if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getSection() != null) {
									fundingPo.setSection(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getSection());
								}
								else {
									fundingPo.setSection("");
								}
								
								if (changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getAnalytical() != null) {
									fundingPo.setAnalytical(changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getAnalytical());
								}
								else {
									fundingPo.setAnalytical("");
								}
								
								fundingPo.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
								System.out.println("XXXXXXX: " + fundingPo.getFundSrcRemark());
								if (j >= rp.getRequestFundingList().size()) {
									rp.addRequestFundingList(fundingPo);
								}
							}
						}
					}
					
					// For Request funding inactive
					if (rp.getRequestFundingList().size() > changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().size()) {
						for (int v=changeStaffMixWebVo.getRequestPositionToList().get(i).getRequestFundingList().size(); v<rp.getRequestFundingList().size(); v++) {
							rp.getRequestFundingList().get(v).setRecState(MPRSConstant.MPRS_STATE_INACTIVE);
						}
					}
					
					String clusterCode = changeStaffMixWebVo.getRequestCluster().size()!=0?changeStaffMixWebVo.getRequestCluster().get(i):"";
					String instCode = changeStaffMixWebVo.getRequestInst().size()!=0?changeStaffMixWebVo.getRequestInst().get(i):"";
					String deptCode = changeStaffMixWebVo.getRequestDept().size()!=0?changeStaffMixWebVo.getRequestDept().get(i):"";
					String rankCode = changeStaffMixWebVo.getRequestRank().size()!=0?changeStaffMixWebVo.getRequestRank().get(i):"";
					String postFte = changeStaffMixWebVo.getRequestPostFTE().size()!=0?changeStaffMixWebVo.getRequestPostFTE().get(i):"";
					String proposedPostId = changeStaffMixWebVo.getRequestProposedPostId().size()!=0?changeStaffMixWebVo.getRequestProposedPostId().get(i):"";
					String staffGroupCode  = changeStaffMixWebVo.getRequestStaffGroup().size()!=0?changeStaffMixWebVo.getRequestStaffGroup().get(i):"";
					String postDuration = changeStaffMixWebVo.getRequestPostDuration().size()!=0?changeStaffMixWebVo.getRequestPostDuration().get(i):"";
					System.out.println("changeStaffMixWebVo.getCluster(): " + clusterCode); 
					System.out.println("changeStaffMixWebVo.getInstitution(): " + instCode); 
					System.out.println("changeStaffMixWebVo.getDepartment(): " +deptCode); 
					System.out.println("changeStaffMixWebVo.getRank(): " + rankCode);
					System.out.println("changeStaffMixWebVo.getPostFTE(): " + postFte);
					System.out.println("changeStaffMixWebVo.getproposedPostId(): " + proposedPostId);
					System.out.println("changeStaffMixWebVo.getpostDuration(): " + postDuration);
					
					boolean haveAnnualPlan = false;
					for (int k=0; k<annualPlanList.size(); k++) {
						if (!"".equals(annualPlanList.get(k)) && annualPlanList.get(k) != null) {
							haveAnnualPlan = true;
							break;
						}
					}
					
					if ("".equals(proposedPostId)) {
						if (!"".equals(clusterCode) && 
								!"".equals(instCode) && 
								!"".equals(deptCode) && 
								!"".equals(rankCode) && 
								// !"".equals(annualPlanInd) &&
								haveAnnualPlan &&
								!"".equals(postFte)) {
							
							String postId = requestSvc.generateNewPostId(
									staffGroupCode,
									clusterCode,
									instCode, 
									deptCode,
									rankCode,
									annualPlanList,
									programTypeList,
									postDuration,
									postFte,
									needRecycle,
									tmpProposedPostId);

							rp.setProposedPostId(postId);
						}
					}
					
					System.out.println("--Resources Support Tab Saved --");
					rfs.setResourcesSupportFrExt(changeStaffMixWebVo.getRequestResSupFrExt().size()!=0?changeStaffMixWebVo.getRequestResSupFrExt().get(i):"");
					rfs.setResourcesSupportRemark(changeStaffMixWebVo.getRequestResSupRemark().size()!=0?changeStaffMixWebVo.getRequestResSupRemark().get(i):"");
					rp.setRequestFundingResource(rfs);
					
					if (requestPositionToList.size() > i) {
						int tmp = 0;
						for (int u=0; u<newRequest.getRequestPositionList().size(); u++) {
							if ("Y".equals(newRequest.getRequestPositionList().get(u).getToPostInd())) {
								if (tmp == rowId2) {
									newRequest.getRequestPositionList().set(u, rp);
								}
								else {
									tmp ++;
								}
							}
						}
						
						rowId2 ++;
					}
					else {
						newRequest.addRequestPosition(rp);
					}
				}
				
				int aFrom = 0;
				int aTo = 0;
				for (int i=0; i<newRequest.getRequestPositionList().size(); i++) {
					if ("Y".equals(newRequest.getRequestPositionList().get(i).getFromPostInd())) {
						aFrom ++;
						if (aFrom > rowId1) {
							System.out.println("Perform set (From) inactive. " + newRequest.getRequestPositionList().get(i).getRequestPostId());
							newRequest.getRequestPositionList().get(i).setRecState(MPRSConstant.MPRS_STATE_INACTIVE);
						}
					}
					else {
						aTo ++;
						if (aTo > rowId2) {
							System.out.println("Perform set (To) inactive. " + newRequest.getRequestPositionList().get(i).getRequestPostId());
							newRequest.getRequestPositionList().get(i).setRecState(MPRSConstant.MPRS_STATE_INACTIVE);
						}
					}
				}
				
				List<RequestAttachmentPo> attachmentList = new ArrayList<RequestAttachmentPo>();
				if (changeStaffMixWebVo.getUploadFileId() != null && !"".equals(changeStaffMixWebVo.getUploadFileId())) {
					for (int i=0; i<changeStaffMixWebVo.getUploadFileId().size(); i++) {
						if (changeStaffMixWebVo.getUploadFileId().get(i) != null && !"".equals(changeStaffMixWebVo.getUploadFileId().get(i))) {
							RequestTempFilePo tempFile = requestSvc.getTempFile(Integer.parseInt(changeStaffMixWebVo.getUploadFileId().get(i)));

							RequestAttachmentPo attachment = new RequestAttachmentPo();
							attachment.setFileName(tempFile.getFilename());
							// attachment.setAttachment(tempFile.getContent());
							// newRequest.setAttachment(attachment);
							attachmentList.add(attachment);
						}
					}
				}	
				else {
					newRequest.setAttachment(null);
				}
					
					newRequest.setAttachment(attachmentList);
				
				requestSvc.update(newRequest, changeStaffMixWebVo.getRemoveAttachmentUid(), changeStaffMixWebVo.getUploadFileId(), user);
			}
			
			System.out.println("RequestPoNo.: " + changeStaffMixWebVo.getRequestNo());
			int requestNo = Integer.parseInt(changeStaffMixWebVo.getRequestNo());
			
			String emailTo = "";
			String emailCC = "";
			String emailTitle = "";
			String emailContent = "";
			String generateEmail = "N";
			
			if ("Y".equals(changeStaffMixWebVo.getSubmitWithModifiedEmail())) {
				RequestPo tmpCase = requestSvc.getRequestByRequestNo(requestNo);
				String postId = "";
				String postId2 = "";
				for (int i=0; i<tmpCase.getRequestPositionList().size(); i++) {
					if ("Y".equals(tmpCase.getRequestPositionList().get(i).getFromPostInd())) { 
						if (!"".equals(postId)) 
							postId += ", ";
						
						postId += tmpCase.getRequestPositionList().get(i).getPostId();
					}
					else {
						if (!"".equals(postId2)) 
							postId2 += ", ";
						
						if (tmpCase.getRequestPositionList().get(i).getProposedPostId() == null) {
							postId2 += "To be confirmed";
						}
						else {
							postId2 += tmpCase.getRequestPositionList().get(i).getProposedPostId();
						}
					}
					
				}
				
				String submittedBy = user.getUserName();
				String submittedDate = DateTimeHelper.formatDateToString(new Date());
				String suppInfo = StrHelper.format(changeStaffMixWebVo.getEmailSuppInfo());
				
				emailTo = changeStaffMixWebVo.getEmailTo();
				emailCC = changeStaffMixWebVo.getEmailCC();
				emailTitle = changeStaffMixWebVo.getEmailTitle();
				
				String receiver = "";
				if (!"".equals(changeStaffMixWebVo.getNextWFUser())) {
					UserPo requester = securitySvc.findUser(changeStaffMixWebVo.getNextWFUser());
					receiver = requester.getUserName();
				}
				else {
					List<RolePo> userGroupList = securitySvc.getAllRole();
					for (int i=0; i<userGroupList.size(); i++) {
						if (changeStaffMixWebVo.getNextWFGroup().equals(userGroupList.get(i).getRoleId())) {
							receiver = userGroupList.get(i).getRoleName();
							break;
						}
					}
				}
				
				// emailContent = changeStaffMixWebVo.getEmailContent().replaceAll("<Supplementary Information / Comment>", changeStaffMixWebVo.getEmailSuppInfo().replaceAll("\\$", "\\\\\\$"));
				EmailTemplatePo emailTemplate = commonSvc.getTemplateByTemplateId("RQ_CHG_STF_SUBMIT", new String[] {String.valueOf(changeStaffMixWebVo.getRequestId()),
						postId, 
						submittedBy, 
						submittedDate, 
						postId2,																											
						"", 
						"", 
						"", 
						receiver																											
						});

				emailContent = emailTemplate.getTemplateContent();
				emailContent = emailContent.replaceAll("<Supplementary Information / Comment>", suppInfo.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$"));
				String url = appUrl + "/request/changeStaffMix?rq=" + changeStaffMixWebVo.getRequestNo() + "&fromMail=Y";
				emailContent = emailContent.replaceAll("Please click here to review the request", "Please click <a href='" + url + "'>here</a> to review the request");
				generateEmail = "Y";
			}
			
			if ("Y".equals(changeStaffMixWebVo.getSubmitWithEmail())) {
				RequestPo tmpCase = requestSvc.getRequestByRequestNo(requestNo);
				String postId = "";
				String postId2 = "";
				for (int i=0; i<tmpCase.getRequestPositionList().size(); i++) {
					if ("Y".equals(tmpCase.getRequestPositionList().get(i).getFromPostInd())) { 
						if (!"".equals(postId)) 
							postId += ", ";
						
						postId += tmpCase.getRequestPositionList().get(i).getPostId();
					}
					else {
						if (!"".equals(postId2)) 
							postId2 += ", ";
						
						if (tmpCase.getRequestPositionList().get(i).getProposedPostId() == null) {
							postId2 += "To be confirmed";
						}
						else {
							postId2 += tmpCase.getRequestPositionList().get(i).getProposedPostId();
						}
					}
					
				}
				
				String submittedBy = user.getUserName();
				String submittedDate = DateTimeHelper.formatDateToString(new Date());
				String suppInfo = StrHelper.format(changeStaffMixWebVo.getEmailSuppInfo());
				
				String receiver = "";
				if (!"".equals(changeStaffMixWebVo.getNextWFUser())) {
					UserPo requester = securitySvc.findUser(changeStaffMixWebVo.getNextWFUser());
					receiver = requester.getUserName();
				}
				else {
					List<RolePo> userGroupList = securitySvc.getAllRole();
					for (int i=0; i<userGroupList.size(); i++) {
						if (changeStaffMixWebVo.getNextWFGroup().equals(userGroupList.get(i).getRoleId())) {
							receiver = userGroupList.get(i).getRoleName();
							break;
						}
					}
				}
				
				EmailTemplatePo emailTemplate = commonSvc.getTemplateByTemplateId("RQ_CHG_STF_SUBMIT", new String[] {String.valueOf(changeStaffMixWebVo.getRequestId()),
																														postId, 
																														submittedBy, 
																														submittedDate, 
																														postId2,																											
																														"", 
																														"", 
																														"", 
																														receiver																											
																														});
				
				// Set email List
				String emailList = "";
				if (!"".equals(changeStaffMixWebVo.getNextWFUser()) && changeStaffMixWebVo.getNextWFUser() != null) {
					try {
						UserPo tUser = securitySvc.findUser( changeStaffMixWebVo.getNextWFUser());
						emailList = tUser.getEmail();
					} catch (Exception e) {
						EClaimLogger.error("performUpdate - send Email:" + e.getMessage(), e);
					}
				}
				else {
					RequestPo requestCase = requestSvc.getRequestByRequestNo(requestNo);
					String clusterCode = "";
					String instCode = "";
					for (int j=0; j<requestCase.getRequestPositionList().size(); j++) {
						clusterCode = requestCase.getRequestPositionList().get(j).getClusterCode();
						instCode = requestCase.getRequestPositionList().get(j).getInstCode();
						System.out.println("a: Lookup the email list for " + clusterCode + "/" + instCode);
						List<UserPo> userList = securitySvc.getAllUserByGroupAndCluster(clusterCode, instCode, changeStaffMixWebVo.getNextWFGroup());
						
						for (int i=0; i<userList.size(); i++) {
							if (userList.get(i).getEmail() != null) {
								if (!"".equals(emailList)) {
									emailList += ", ";
								}
								
								emailList += userList.get(i).getEmail();
							}
						}
					}
				}
				
				// Get the email CC list
				String ccEmailList = "";
				List<String> ccEmail = new ArrayList<String>();
				
				if ("Y".equals(changeStaffMixWebVo.getReturnCase())) { 
					List<UserPo> ccUserList = securitySvc.getReturnUserFromWorkflowHistoryAll(changeStaffMixWebVo.getRequestNo(), changeStaffMixWebVo.getNextWFGroup());
					for (int i=0; i<ccUserList.size(); i++) {
						if (ccUserList.get(i).getEmail() != null) {
							if (!"".equals(ccEmailList)) 
								ccEmailList += ", ";
							
							ccEmailList += ccUserList.get(i).getEmail();
						}
					}
				}
				
				// If submit to MG team, cc to all related party
				else if ("HO_MG_OFFICER".equals(changeStaffMixWebVo.getNextWFGroup())) {
					List<UserPo> ccUserList = securitySvc.getRelatedUserFromWorkflowHistoryAll(changeStaffMixWebVo.getRequestNo()) ;
					for (int i=0; i<ccUserList.size(); i++) {
						if (ccUserList.get(i).getEmail() != null) {
							if (!ccEmail.contains(ccUserList.get(i).getEmail())) {
								if (!"".equals(ccEmailList)) 
									ccEmailList += ", ";
								
								ccEmailList += ccUserList.get(i).getEmail();
								ccEmail.add(ccUserList.get(i).getEmail());
							}
						}
					}
				}
				
				emailTo = emailList;
				emailCC = ccEmailList;
				emailTo = StrHelper.removeDuplicateEmail(emailTo);
				emailCC = StrHelper.removeDuplicateEmail(emailCC);
				emailTitle = emailTemplate.getTemplateTitle();
				emailContent = emailTemplate.getTemplateContent();
				String si = suppInfo.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$");
				emailContent = emailContent.replaceAll("<Supplementary Information / Comment>", si);
				String url = appUrl + "/request/changeStaffMix?rq=" + changeStaffMixWebVo.getRequestNo() + "&fromMail=Y";
				emailContent = emailContent.replaceAll("Please click here to review the request", "Please click <a href='" + url + "'>here</a> to review the request");
				
				generateEmail = "Y";
			}
			
			System.out.println("emailContent: " + emailContent);
			
			String errorMsg = requestSvc.submitWorkflow(requestNo,  MPRSConstant.REQUEST_TYPE_CHANGE_STAFF_MIX,
					                                    changeStaffMixWebVo.getNextWFGroup(), 
					                                    changeStaffMixWebVo.getNextWFUser(),  
					                                    changeStaffMixWebVo.getReturnCase(),
					                                    generateEmail,
						                                emailTo,
						                                emailCC,
						                                emailTitle,
						                                emailContent,
					                                    userId, currentRole);

			if ("".equals(errorMsg) || errorMsg == null) {
				// Send the email
				commonSvc.performSendEmail(requestNo);
				
				// Retrieve again the RequestPo
				RequestPo requestCase = requestSvc.getRequestByRequestNo(requestNo);

				changeStaffMixWebVo = convertToWebVo(requestCase, userId, currentRole);
				changeStaffMixWebVo.setUpdateSuccess("Y");
				changeStaffMixWebVo.setDisplayMessage("Request submit successful.");
			}
			else {
				System.out.println("errorMsg: " + errorMsg);
				changeStaffMixWebVo.setUpdateSuccess("N");
				changeStaffMixWebVo.setDisplayMessage(errorMsg);
			}

			return new ModelAndView("request/changeStaffMix", "formBean", changeStaffMixWebVo);
		}
		else if ("APPROVE".equalsIgnoreCase(formAction)) {
			System.out.println("RequestPoNo. submit approve: " + changeStaffMixWebVo.getRequestNo());
			try {
				int requestNo = Integer.parseInt(changeStaffMixWebVo.getRequestNo());
				
				String emailTo = "";
				String emailCC = "";
				String emailTitle = "";
				String emailContent = "";
				String generateEmail = "N";
				
				if ("Y".equals(changeStaffMixWebVo.getSubmitWithModifiedEmail())) {
					emailTo = changeStaffMixWebVo.getEmailTo();
					emailCC = changeStaffMixWebVo.getEmailCC();
					emailTitle = changeStaffMixWebVo.getEmailTitle();
					String si = StrHelper.format(changeStaffMixWebVo.getEmailSuppInfo().replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$"));
					emailContent = changeStaffMixWebVo.getEmailContent().replaceAll("<Supplementary Information / Comment>", si);
					String url = appUrl + "/request/changeStaffMix?rq=" + changeStaffMixWebVo.getRequestNo() + "&fromMail=Y";
					emailContent = emailContent.replaceAll("Please click here to review the request", "Please click <a href='" + url + "'>here</a> to review the request");
					generateEmail = "Y";
				}
				
				if ("Y".equals(changeStaffMixWebVo.getSubmitWithEmail())) {
					RequestPo tmpCase = requestSvc.getRequestByRequestNo(requestNo);
					String postId = "";
					String postId2 = "";
					for (int i=0; i<tmpCase.getRequestPositionList().size(); i++) {
						if ("Y".equals(tmpCase.getRequestPositionList().get(i).getFromPostInd())) { 
							if (!"".equals(postId)) 
								postId += ", ";
							
							postId += tmpCase.getRequestPositionList().get(i).getPostId();
						}
						else {
							if (!"".equals(postId2)) 
								postId2 += ", ";
							
							postId2 += tmpCase.getRequestPositionList().get(i).getProposedPostId();
						}
						
					}
					
					String submittedBy = user.getUserName();
					String submittedDate = DateTimeHelper.formatDateToString(new Date());
					String suppInfo = StrHelper.format(changeStaffMixWebVo.getEmailSuppInfo());
					
					String receiver = "ALL";
					
					EmailTemplatePo emailTemplate = commonSvc.getTemplateByTemplateId("RQ_CHG_STF_CONFIRM", new String[] {String.valueOf(changeStaffMixWebVo.getRequestId()),
																															postId, 
																															submittedBy, 
																															submittedDate, 
																															postId2,																											
																															"", 
																															"", 
																															"", 
																															receiver																									
																															});
					
					// Get Requester information
					List<String> ccEmail = new ArrayList<String>();
					String emailList = "";
					emailTo = emailList;
					emailCC = "";
					
					List<UserPo> ccUserList = securitySvc.getRelatedUserFromWorkflowHistoryAll(changeStaffMixWebVo.getRequestNo()) ;
					for (int i=0; i<ccUserList.size(); i++) {
						if (ccUserList.get(i).getEmail() != null) {
							if (!ccEmail.contains(ccUserList.get(i).getEmail())) {
								if (!"".equals(emailTo)) 
									emailTo += ", ";
								
								emailTo += ccUserList.get(i).getEmail();
								ccEmail.add(ccUserList.get(i).getEmail());
							}
						}
					}
					
					String staffGroupCode = "";
					for (int i=0; i<tmpCase.getRequestPositionList().size(); i++) {
						if (MPRSConstant.MPRS_STATE_ACTIVE.equals(tmpCase.getRequestPositionList().get(i).getRecState())) {
							staffGroupCode = tmpCase.getRequestPositionList().get(i).getStaffGroupCode();
						}
					}
					
					// For AH / PHARM, if the status is confirm also CC to HO AH/Pharm Manager
					List<UserPo> approvalCCList = null;
						
					if ("AH".equals(staffGroupCode)) {
						approvalCCList = securitySvc.getAllUserByGroup("HO_AH_MANAGER");
						System.out.println("Check 1: " + approvalCCList.size());
					}
					else if ("PHARM".equals(staffGroupCode)) {
						approvalCCList = securitySvc.getAllUserByGroup("HO_PHARM_MANAGER");
					}

					if (approvalCCList != null) {
						for (int i=0; i<approvalCCList.size(); i++) {
							if (approvalCCList.get(i).getEmail() != null) {
								if (!"".equals(emailCC)) {
									emailCC += ", ";
								}
								emailCC += approvalCCList.get(i).getEmail();
							}
						}		
					}
					
					emailTitle = emailTemplate.getTemplateTitle();
					emailContent = emailTemplate.getTemplateContent();
					String si = suppInfo.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$");
					emailContent = emailContent.replaceAll("<Supplementary Information / Comment>", si);
					String url = appUrl + "/request/changeStaffMix?rq=" + changeStaffMixWebVo.getRequestNo() + "&fromMail=Y";
					emailContent = emailContent.replaceAll("Please click here to review the request", "Please click <a href='" + url + "'>here</a> to review the request");
	
					generateEmail = "Y";
				}
				
				emailTo = StrHelper.removeDuplicateEmail(emailTo);
				emailCC = StrHelper.removeDuplicateEmail(emailCC);
				
				String errorMsg = requestSvc.submitApprove(requestNo, MPRSConstant.REQUEST_TYPE_CHANGE_STAFF_MIX,
														   generateEmail,
						                                   emailTo,
						                                   emailCC,
						                                   emailTitle,
						                                   emailContent,
						                                   userId, currentRole);
	
				if ("".equals(errorMsg) || errorMsg == null) {
					// Send the email
					commonSvc.performSendEmail(requestNo);
					
					// Retrieve again the RequestPo
					RequestPo requestCase = requestSvc.getRequestByRequestNo(requestNo);
	
					changeStaffMixWebVo = convertToWebVo(requestCase, userId, currentRole);
					changeStaffMixWebVo.setUpdateSuccess("Y");
					changeStaffMixWebVo.setDisplayMessage("Request approve successful.");
				}
				else {
					System.out.println("errorMsg: " + errorMsg);
					changeStaffMixWebVo.setUpdateSuccess("N");
					changeStaffMixWebVo.setDisplayMessage(errorMsg);
				}
			}
			catch (Exception ex) {
				EClaimLogger.error("performUpdate - send Email:" + ex.getMessage(), ex);
				String errorMsg = doHandleException(ex.getCause(), ex.getMessage());
				
				// Re-load the case
				RequestPo requestCase = requestSvc.getRequestByRequestNo(Integer.parseInt(changeStaffMixWebVo.getRequestNo()));
				changeStaffMixWebVo = convertToWebVo(requestCase, userId, currentRole);
				
				System.out.println("errorMsg: " + errorMsg);
				changeStaffMixWebVo.setUpdateSuccess("N");
				changeStaffMixWebVo.setDisplayMessage(errorMsg);
			}
			
			return new ModelAndView("request/changeStaffMix", "formBean", changeStaffMixWebVo);
		}
		
		return new ModelAndView("request/changeStaffMix", "formBean", changeStaffMixWebVo);
	}

	@ModelAttribute("FundingSourceList")
	public Map<String, String> getFundingSourceList() {
		List<FundingSourcePo> FundingSourceList = commonSvc.getAllFundingSource();

		Map<String, String> displayFundingSourceList = new LinkedHashMap<String, String>();
		for (int i=0; i<FundingSourceList.size(); i++) {
			displayFundingSourceList.put(FundingSourceList.get(i).getSourceId(), FundingSourceList.get(i).getSourceDesc());
		}
		return displayFundingSourceList;

	}
	
	@ModelAttribute("FundingSourceSubCatList")
	public Map<String, String> getFundingSourceSubCatList() {
		List<FundingSourceSubCatPo> fundingSourceSubCatList = commonSvc.getAllFundingSourceSubCat();

		Map<String, String> displayFundingSourceSubCatList = new LinkedHashMap<String, String>();
		for (int i=0; i<fundingSourceSubCatList.size(); i++) {
			displayFundingSourceSubCatList.put(fundingSourceSubCatList.get(i).getSourceSubCatId(), fundingSourceSubCatList.get(i).getSourceSubCatDesc());
		}
		return displayFundingSourceSubCatList;
	}

	@ModelAttribute("PostDurationList")
	public Map<String, String> getPostDurationList() {
		List<PostDurationPo> PostDurationList = commonSvc.getAllPostDuration();

		Map<String, String> displayPostDurationList = new LinkedHashMap<String, String>();
		for (int i=0; i<PostDurationList.size(); i++) {
			displayPostDurationList.put(PostDurationList.get(i).getPostDurationCode(), PostDurationList.get(i).getPostDurationDesc());
		}
		return displayPostDurationList;

	}

	@ModelAttribute("ExternalSupportList")
	public Map<String, String> getExternalSupportList() {
		List<ExternalSupportPo> ExternalSupportList = commonSvc.getAllExternalSupport();

		Map<String, String> displayExternalSupportList = new LinkedHashMap<String, String>();
		for (int i=0; i<ExternalSupportList.size(); i++) {
			displayExternalSupportList.put(ExternalSupportList.get(i).getSupportId(), ExternalSupportList.get(i).getSupportDesc());
		}
		return displayExternalSupportList;

	}
	
	private ChangeStaffMixWebVo convertToWebVo(RequestPo requestCase, String currentUser, String currentRole) {
		ChangeStaffMixWebVo vo = new ChangeStaffMixWebVo();
		vo.setCurrentLoginUser(currentUser);
		vo.setRequestNo(String.valueOf(requestCase.getRequestNo()));
		vo.setRequestId(requestCase.getRequestId());
		vo.setRequestType(MPRSConstant.REQUEST_TYPE_CHANGE_STAFF_MIX);
		vo.setRequestStatus(requestCase.getRequestStatus().getStatusCode());
		vo.setCurrentWFGroup(requestCase.getCurrentWFGroup());
		vo.setCurrentWFUser(requestCase.getCurrentWFUser());
		vo.setEffectiveDate(DateTimeHelper.formatDateToString(requestCase.getEffectFrom()));
		vo.setRequestReason(requestCase.getRequestReason());
		vo.setRequester(requestCase.getRequester());
		
		if (requestCase.getApprovalDate() != null && !"".equals(requestCase.getApprovalDate())) {
			vo.setApprovalDate(DateTimeHelper.formatDateToString(requestCase.getApprovalDate()));
		}
		vo.setApprovalReference(requestCase.getApprovalReference());
		vo.setApprovalRemark(requestCase.getApprovalRemark());
		
		List<RequestPostPo> requestPositionFromList = new ArrayList<RequestPostPo>();
		List<RequestPostPo> requestPositionToList = new ArrayList<RequestPostPo>();
		for (int i=0; i<requestCase.getRequestPositionList().size(); i++) {
			requestCase.getRequestPositionList().get(i).setPostFTE(StrHelper.formatDecimal(requestCase.getRequestPositionList().get(i).getPostFTE()));
			
			if ("Y".equals(requestCase.getRequestPositionList().get(i).getFromPostInd()) && MPRSConstant.MPRS_STATE_ACTIVE.equals(requestCase.getRequestPositionList().get(i).getRecState())) {
				requestPositionFromList.add(requestCase.getRequestPositionList().get(i));
			}
			
			if ("Y".equals(requestCase.getRequestPositionList().get(i).getToPostInd()) && MPRSConstant.MPRS_STATE_ACTIVE.equals(requestCase.getRequestPositionList().get(i).getRecState())) {
				requestPositionToList.add(requestCase.getRequestPositionList().get(i));
			}
		}
		
		// Added for ST08733
		for (int m=0; m<requestPositionToList.size(); m++) {
			if (requestSvc.isExistingPost(requestPositionToList.get(m).getProposedPostId())) {
				requestPositionToList.get(m).setIsExistingPost("Y");
			}
			else {
				requestPositionToList.get(m).setIsExistingPost("N");
			}
		}
		
		String mgTeamInd = "N";
		String staffGroupCode = "";
		String postDurationCode = "";
		String postFteType = "";
		System.out.println("requestCase.getRequestPositionList().size():" + requestCase.getRequestPositionList().size());
		for (int i=0; i<requestCase.getRequestPositionList().size(); i++) {
			staffGroupCode = requestCase.getRequestPositionList().get(i).getStaffGroupCode();
			postDurationCode = requestCase.getRequestPositionList().get(i).getPostDuration();
			postFteType = requestCase.getRequestPositionList().get(i).getPostFTEType();
			
			System.out.println("Checking: " + staffGroupCode + "/" + postDurationCode + "/" + postFteType);
			if (PostConstant.STAFF_GROUP_MEDICAL.equals(staffGroupCode)) {
				if (PostConstant.POST_DUR_VALUE_RECURRENT.equals(postDurationCode) || 
						(PostConstant.POST_DUR_VALUE_TL_CONTRACT.equals(postDurationCode) && PostConstant.POST_FTE_TYPE_FULL_TIME.equals(postFteType))) {
					mgTeamInd = "Y";
				}
			}
		}
		System.out.println("### Final mgTeamInd:" + mgTeamInd);
		vo.setMgTeamInd(mgTeamInd);

		// Set the button of workflow button
		RequestWorkflowRoutePo route = this.requestSvc.getDefaultRoute(staffGroupCode, MPRSConstant.REQUEST_TYPE_CHANGE_STAFF_MIX, vo.getRequestStatus(), "");
		if (route != null) {
			vo.setSubmitButtonLabel(route.getActionName());
			System.out.println("****** route.getRouteUid()=" + route.getRouteUid().intValue());
		}
		
		vo.setRequestPositionFromList(requestPositionFromList);
		vo.setRequestPositionToList(requestPositionToList);
		
		vo.setUserHaveWithdrawRight("N");
		vo.setUserHaveCreationRight("N");
		
		if (MPRSConstant.REQUEST_STATUS_CONFIRMED.equals(vo.getRequestStatus()) 
				|| MPRSConstant.REQUEST_STATUS_REJECTED.equals(vo.getRequestStatus())) {
			vo.setUserHaveApprovalRight("N");
			vo.setUserHaveSaveRight("N");
			vo.setUserHaveSubmitRight("N");
		}
		else {
			if ((currentUser.equals(requestCase.getCurrentWFUser()) && currentRole.equals(requestCase.getCurrentWFGroup()))
					|| currentRole.equals(requestCase.getCurrentWFGroup())) {
				// Check whether the user have approval right
				if (requestSvc.hasApprovalRight(staffGroupCode, 
												MPRSConstant.REQUEST_TYPE_CHANGE_STAFF_MIX, 
												vo.getRequestStatus(), currentRole, vo.getMgTeamInd())) {
					vo.setUserHaveApprovalRight("Y");
				}
				else {
					vo.setUserHaveApprovalRight("N");
				}

				// vo.setUserHaveSaveRight("Y");
				if ("HR_OFFICER".equals(currentRole)) {
					vo.setUserHaveWithdrawRight("Y");
					vo.setCanEditDetailInfo("Y");
					vo.setUserHaveCreationRight("Y");
				}
				else {
					vo.setCanEditDetailInfo("N");
				}

				if ("FIN_OFFICER".equals(currentRole)) {
					vo.setCanEditFinancialInfo("Y");
				}
				else {
					vo.setCanEditFinancialInfo("N");
				}
				
				vo.setUserHaveSubmitRight("Y");
			}
			else {
				vo.setUserHaveApprovalRight("N");
				vo.setUserHaveSaveRight("N");
				vo.setUserHaveSubmitRight("N");
			}
		}
		
		if ("Y".equals(vo.getCanEditDetailInfo()) || "Y".equals(vo.getCanEditFinancialInfo())) {
			vo.setUserHaveSaveRight("Y");
		}
		else {
			vo.setUserHaveSaveRight("N");
		}
		
		if (requestCase.getAttachment() != null) {
			List<String> attachmentFileName = new ArrayList<String>();
			List<String> attachmentUid = new ArrayList<String>();
			for (int i=0; i<requestCase.getAttachment().size(); i++) {
				attachmentFileName.add(requestCase.getAttachment().get(i).getFileName());
				attachmentUid.add(String.valueOf(requestCase.getAttachment().get(i).getAttachmentUid()));				
			}
			
			vo.setApprovalAttachmentFilename(attachmentFileName);
			vo.setApprovalAttachmentUid(attachmentUid);
		}
		
		// Update the last update date
		vo.setLastUpdateDate(DateTimeHelper.formatDateTimeToString(requestCase.getUpdatedDate()));
		
		return vo;
	}
	
	@ModelAttribute("requestStatusList")
	public Map<String, String> getRequestStatusList() {
		List<RequestStatusPo> requestStatusList = commonSvc.getAllRequestStatus();

		Map<String, String> displayInstList = new LinkedHashMap<String, String>();
		for (int i=0; i<requestStatusList.size(); i++) {
			displayInstList.put(requestStatusList.get(i).getStatusCode(), requestStatusList.get(i).getStatusDesc());
		}
		return displayInstList;
	}
	
	@ModelAttribute("subSpecialtyList")
	public Map<String, String> getSubSpecialtyList() {
		List<SubSpecialtyPo> subSpecialtyList = commonSvc.getAllSubSpecialty();

		Map<String, String> displayInstList = new LinkedHashMap<String, String>();
		for (int i=0; i<subSpecialtyList.size(); i++) {
			displayInstList.put(subSpecialtyList.get(i).getSubSpecialtyCode(), subSpecialtyList.get(i).getSubSpecialtyDesc());
		}
		return displayInstList;
	}
	
	@ModelAttribute("programTypeList")
	public Map<String, String> getProgramTypeList() {
		Map<String, String> displayProgramTypeList = new LinkedHashMap<String, String>();
		List<ProgramTypePo> programTypeList = commonSvc.getAllProgramType();
		for (int i=0; i<programTypeList.size(); i++) {
			displayProgramTypeList.put(programTypeList.get(i).getProgramTypeCode(), programTypeList.get(i).getProgramTypeName());
		}
		
		return displayProgramTypeList;
	}
	
	@ModelAttribute("postStatusList")
	public Map<String, String> getPostStatusList() {
		List<MPRSPostStatusPo> postStatusList = commonSvc.getAllPostStatus();

		Map<String, String> displayPostStatusList = new LinkedHashMap<String, String>();
		for (int i=0; i<postStatusList.size(); i++) {
			if ("CLOSED".equals(postStatusList.get(i).getPostStatusCode())) {
				continue;
			}
			
			displayPostStatusList.put(postStatusList.get(i).getPostStatusCode(), postStatusList.get(i).getPostStatusDesc());
		}
		return displayPostStatusList;
	}
}

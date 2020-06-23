package hk.org.ha.eclaim.controller.request;

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
import hk.org.ha.eclaim.model.request.UpgradeWebVo;
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
public class UpgradeController extends CommonRequestController {
	
	@Value("${mail.appurl.prefix}")
	private String appUrl;

	@RequestMapping(value="/request/upgrade", method=RequestMethod.GET)
	public ModelAndView upgrade(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("Location UpgradeController.upgrade()");
		
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
				EClaimLogger.info("requestCase.getRequestId(): " + requestCase.getRequestId());
				UpgradeWebVo vo = convertToWebVo(requestCase, userId, currentRole);

				return new ModelAndView("request/upgrade", "formBean", vo);
			}
			else {
				return new ModelAndView("redirect:/home/home");
			}
		}
		else {
			System.out.println("request uid is null");
			UpgradeWebVo vo = new UpgradeWebVo();
			
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
			
			return new ModelAndView("request/upgrade", "formBean", vo);
		}
	}

	@RequestMapping(value="/request/upgrade", method=RequestMethod.POST)
	public ModelAndView performUpdate(@ModelAttribute("formBean")UpgradeWebVo upgradeWebVo, 
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		System.out.println("approvalFile: " + upgradeWebVo.getApprovalFile());
		if (upgradeWebVo.getApprovalFile() != null) {
			System.out.println("File name: " + upgradeWebVo.getApprovalFile().getOriginalFilename());
			System.out.println("File size: " + upgradeWebVo.getApprovalFile().getSize());
		}
		String formAction = upgradeWebVo.getFormAction();
		System.out.println("formAction: " + formAction);
		// Get the user name from cookie
		String userId = this.getSessionUser(request).getUserId();
		UserPo user = securitySvc.findUser(userId);
		String currentRole = (String)request.getSession().getAttribute("currentRole");
        user.setCurrentRole(currentRole);

		System.out.println("requestPost:" + upgradeWebVo.getRequestPostNo().size());
		System.out.println("requestNo: " + upgradeWebVo.getRequestNo());
		
		// Concurrent control
		if ("SAVE".equalsIgnoreCase(formAction) ||
				"WITHDRAW".equalsIgnoreCase(formAction) ||
				"SUBMIT".equalsIgnoreCase(formAction) ||
				"APPROVE".equalsIgnoreCase(formAction)) {
			if (upgradeWebVo.getRequestNo() != null && !"".equals(upgradeWebVo.getRequestNo())) {
				RequestPo requestCase = requestSvc.getRequestByRequestNo(Integer.parseInt(upgradeWebVo.getRequestNo()));

				// Check last update date
				String lastUpdateDate = DateTimeHelper.formatDateTimeToString(requestCase.getUpdatedDate());
				System.out.println("=== " + lastUpdateDate + " vs " + upgradeWebVo.getLastUpdateDate());
				if (!lastUpdateDate.equals(upgradeWebVo.getLastUpdateDate())) {
					upgradeWebVo = convertToWebVo(requestCase, userId, currentRole);
					upgradeWebVo.setUpdateSuccess("N");
					upgradeWebVo.setDisplayMessage("Record had been updated by other, content is refreshed.");

					return new ModelAndView("request/upgrade", "formBean", upgradeWebVo);
				}
			}
		}
		
		if ("SAVE".equalsIgnoreCase(formAction)) {
			System.out.println("Location ChangeOfStaffMixController.performUpdate()");
			System.out.println("RequestPoId: " + upgradeWebVo.getRequestId());
			System.out.println("Requester " + upgradeWebVo.getRequester());

			try {
				RequestPo newRequest= null;	
				if (upgradeWebVo.getRequestNo() != null && !"".equals(upgradeWebVo.getRequestNo())) {
					newRequest = requestSvc.getRequestByRequestNo(Integer.parseInt(upgradeWebVo.getRequestNo()));
					
					if (upgradeWebVo.getEffectiveDate() != null && !"".equals(upgradeWebVo.getEffectiveDate())) {
						newRequest.setEffectFrom(sdf.parse(upgradeWebVo.getEffectiveDate()));
					}
					newRequest.setRequestReason(upgradeWebVo.getRequestReason());
					newRequest.setRequester(upgradeWebVo.getRequester());
					
					if (upgradeWebVo.getApprovalDate() != null && !"".equals(upgradeWebVo.getApprovalDate())) {
						newRequest.setApprovalDate(sdf.parse(upgradeWebVo.getApprovalDate()));
					}
					
					newRequest.setApprovalReference(upgradeWebVo.getApprovalReference());
					newRequest.setApprovalRemark(upgradeWebVo.getApprovalRemark());
					
					// Update from post
					for (int i=0; i<upgradeWebVo.getRequestPostNo().size(); i++) {
						for (int m=0; m<newRequest.getRequestPositionList().size(); m++) {
							if ("Y".equals(newRequest.getRequestPositionList().get(m).getFromPostInd())) {
								RequestPostPo rp = newRequest.getRequestPositionList().get(m);
								PostMasterPo mprsPost = requestSvc.getPostByPostUid(Integer.parseInt(upgradeWebVo.getRequestPostNo().get(i)));
								rp.setPostNo(upgradeWebVo.getRequestPostNo().get(i));
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
								newRequest.getRequestPositionList().set(m, rp);
								break;
							}
						}
					}
					
					for (int i=0; i<upgradeWebVo.getRequestCluster().size(); i++) {
						// System.out.println("upgradeWebVo.getRequestPostNoTo(): " + upgradeWebVo.getRequestPostNoTo().get(i));
						
						RequestPostPo rp = null;
						boolean isExisting = false;
						
						// Look up the by requestPostId
						for (int j=0; j<newRequest.getRequestPositionList().size(); j++) {
							if (newRequest.getRequestPositionList().get(j).getRequestPostId() == Integer.parseInt(upgradeWebVo.getRequestPostNoTo().size()==0?"0":upgradeWebVo.getRequestPostNoTo().get(i))) {
								isExisting = true;
								rp = newRequest.getRequestPositionList().get(j);
								break;
							}
						}
						
						if (!isExisting) {
							rp = new RequestPostPo();
							rp.setPostId("");
						} 
						
						boolean needRecycle = true;
						String tmpProposedPostId = rp.getProposedPostId();
						if (tmpProposedPostId != null) {
							
							// Original Proposed Post ID should be stored into CS_POST_ID_SEQ_REUSE,
							// if below fields changes
							if (rp.getClusterCode().equals(upgradeWebVo.getCluster()) && 
								rp.getInstCode().equals(upgradeWebVo.getInstitution()) &&
								rp.getDeptCode().equals(upgradeWebVo.getDepartment()) &&
								rp.getRank().getRankCode().equals(upgradeWebVo.getRank()) &&
								rp.getPostDuration().equals(upgradeWebVo.getPostDuration()) &&
								rp.getPostFTEType().equals(upgradeWebVo.getPostFTE())) {
								needRecycle = false;
							} else {
								System.out.println("Recycle: " + tmpProposedPostId);
								requestSvc.recycleProposedPostId(rp.getStaffGroupCode(), rp.getPostDuration(), rp.getPostFTEType(), tmpProposedPostId, userId, currentRole);
							}
						}
						
						System.out.println("Add RequestPoPosition To!!");
						
						if (upgradeWebVo.getRequestRank().size()!=0) {
							RankPo rank = new RankPo();
							rank.setRankCode(upgradeWebVo.getRequestRank().get(i));
							rp.setRank(rank);
						}
						
						rp.setClusterCode(upgradeWebVo.getRequestCluster().size()!=0?upgradeWebVo.getRequestCluster().get(i):"");
						rp.setInstCode(upgradeWebVo.getRequestInst().size()!=0?upgradeWebVo.getRequestInst().get(i):"");
						rp.setDeptCode(upgradeWebVo.getRequestDept().size()!=0?upgradeWebVo.getRequestDept().get(i):"");
						rp.setStaffGroupCode(upgradeWebVo.getRequestStaffGroup().size()!=0?upgradeWebVo.getRequestStaffGroup().get(i):"");
						rp.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
						rp.setHcmPositionId(upgradeWebVo.getHcmPositionId().size()!=0?upgradeWebVo.getHcmPositionId().get(i):"");
						rp.setHoBuyServiceInd(upgradeWebVo.getRequestHoBuyServiceInd().size()!=0?upgradeWebVo.getRequestHoBuyServiceInd().get(i):"");
						rp.setToPostInd("Y");
						
						System.out.println("--Position Tab Saved --");
						rp.setUnit(upgradeWebVo.getRequestUnit().size()!=0?upgradeWebVo.getRequestUnit().get(i):"");
						rp.setPostTitle(upgradeWebVo.getRequestPostTitle().size()!=0?upgradeWebVo.getRequestPostTitle().get(i):"");
						rp.setPostDuration(upgradeWebVo.getRequestPostDuration().size()!=0?upgradeWebVo.getRequestPostDuration().get(i):"");
						rp.setLimitDurationType(upgradeWebVo.getRequestRdDuration().size()!=0?upgradeWebVo.getRequestRdDuration().get(i):"");
						if (upgradeWebVo.getRequestPostStartDate().size() !=0 ) {
							if (!"".equals(upgradeWebVo.getRequestPostStartDate().get(i))) {
								rp.setPostStartDate(sdf.parse(upgradeWebVo.getRequestPostStartDate().get(i)));//review
							}
						}
						rp.setLimitDurationNo(upgradeWebVo.getRequestDurationValue().size()!=0?upgradeWebVo.getRequestDurationValue().get(i):null);
						rp.setLimitDurationUnit(upgradeWebVo.getRequestDurationUnit().size()!=0?upgradeWebVo.getRequestDurationUnit().get(i):"");
						
						if (upgradeWebVo.getRequestPostActualEndDate().size() != 0) {
							if (!"".equals(upgradeWebVo.getRequestPostActualEndDate().get(i))) {
								rp.setLimitDurationEndDate(sdf.parse(upgradeWebVo.getRequestPostActualEndDate().get(i)));
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
						
						rp.setPostRemark(upgradeWebVo.getRequestPostRemark().size()!=0?upgradeWebVo.getRequestPostRemark().get(i):"");
						rp.setPostFTEType(upgradeWebVo.getRequestPostFTE().size()!=0?upgradeWebVo.getRequestPostFTE().get(i):"");
						rp.setPostFTE(upgradeWebVo.getRequestPostFTEValue().size()!=0?upgradeWebVo.getRequestPostFTEValue().get(i):"");
						rp.setPostStatus(upgradeWebVo.getRequestPositionStatus().size()!=0?upgradeWebVo.getRequestPositionStatus().get(i):"");
						if (upgradeWebVo.getRequestPositionStartDate().size() != 0)
							if (!"".equals(upgradeWebVo.getRequestPositionStartDate().get(i)))
								rp.setPostStatusStartDate(sdf.parse(upgradeWebVo.getRequestPositionStartDate().get(i)));
						if (upgradeWebVo.getRequestPositionEndDate().size() != 0)	
							if (!"".equals(upgradeWebVo.getRequestPositionEndDate().get(i)))
								rp.setPostStatusEndDate(sdf.parse(upgradeWebVo.getRequestPositionEndDate().get(i)));
						rp.setClusterRef(upgradeWebVo.getRequestClusterRefNo().size()!=0?upgradeWebVo.getRequestClusterRefNo().get(i):"");
						rp.setClusterRemark(upgradeWebVo.getRequestClusterRemark().size()!=0?upgradeWebVo.getRequestClusterRemark().get(i):"");
	
						rp.setSubSpecialtyCode(upgradeWebVo.getRequestSubSpecialty().size()!=0?upgradeWebVo.getRequestSubSpecialty().get(i):"");
						
						String clusterCode = upgradeWebVo.getRequestCluster().size()!=0?upgradeWebVo.getRequestCluster().get(i):"";
						String instCode = upgradeWebVo.getRequestInst().size()!=0?upgradeWebVo.getRequestInst().get(i):"";
						String deptCode = upgradeWebVo.getRequestDept().size()!=0?upgradeWebVo.getRequestDept().get(i):"";
						String rankCode = upgradeWebVo.getRequestRank().size()!=0?upgradeWebVo.getRequestRank().get(i):"";
						String postFte = upgradeWebVo.getRequestPostFTE().size()!=0?upgradeWebVo.getRequestPostFTE().get(i):"";
						String proposedPostId = upgradeWebVo.getRequestProposedPostId().size()!=0?upgradeWebVo.getRequestProposedPostId().get(i):"";
						String staffGroupCode  = upgradeWebVo.getRequestStaffGroup().size()!=0?upgradeWebVo.getRequestStaffGroup().get(i):"";
						String postDuration = upgradeWebVo.getRequestPostDuration().size()!=0?upgradeWebVo.getRequestPostDuration().get(i):"";
						System.out.println("upgradeWebVo.getCluster(): " + clusterCode); 
						System.out.println("upgradeWebVo.getInstitution(): " + instCode); 
						System.out.println("upgradeWebVo.getDepartment(): " +deptCode); 
						System.out.println("upgradeWebVo.getRank(): " + rankCode);
						System.out.println("upgradeWebVo.getPostFTE(): " + postFte);
						System.out.println("upgradeWebVo.getproposedPostId(): " + proposedPostId);
						System.out.println("upgradeWebVo.getpostDuration(): " + postDuration);
						
						List<String> annualPlanList = new ArrayList<String>();
						List<String> programTypeList = new ArrayList<String>();
						
						if (upgradeWebVo.getRequestPositionToList() != null) {
							if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList() != null) {
								for (int j=0; j<upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().size(); j++) {
									annualPlanList.add(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getAnnualPlanInd());
									programTypeList.add(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramTypeCode());
								}
							}
						}
						
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
	
						System.out.println("--Funding Details Tab --");
						if (upgradeWebVo.getRequestPositionToList() != null) {
							if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList() != null) {
								for (int j=0; j<upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().size(); j++) {
									RequestFundingPo fundingPo = null;
									if (j < rp.getRequestFundingList().size()) {
										fundingPo = rp.getRequestFundingList().get(j);
									}
									else {
										fundingPo = new RequestFundingPo();
									}
									
									fundingPo.setFundingSeqNo(j+1);
									
									if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getAnnualPlanInd() != null) {
										fundingPo.setAnnualPlanInd(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getAnnualPlanInd());
									}
									else {
										fundingPo.setAnnualPlanInd("");
									}
									
									System.out.println("fundingPo.getAnnualPlanInd(): " + fundingPo.getAnnualPlanInd());
									
									if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramYear() != null) {
										fundingPo.setProgramYear(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramYear());
									}
									else {
										fundingPo.setProgramYear("");
									}
									
									if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramCode() != null) {
										fundingPo.setProgramCode(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramCode());
									}
									else {
										fundingPo.setProgramCode("");
									}
									
									if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramName() != null) {
										fundingPo.setProgramName(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramName());
									}
									else {
										fundingPo.setProgramName("");
									}
									
									if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramTypeCode() != null) {
										fundingPo.setProgramTypeCode(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramTypeCode());
									}
									else {
										fundingPo.setProgramTypeCode("");
									}
									
									if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcId() != null) {
										fundingPo.setFundSrcId(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcId());
									}
									else {
										fundingPo.setFundSrcId("");
									}
									
									if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcSubCatId() != null) {
										fundingPo.setFundSrcSubCatId(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcSubCatId());
									}
									else {
										fundingPo.setFundSrcSubCatId("");
									}
									
									System.out.println("upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getStartDateStr():" + upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getStartDateStr());
									if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getStartDateStr() != null &&
											!"".equals(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getStartDateStr())) {
										fundingPo.setFundSrcStartDate(sdf.parse(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getStartDateStr()));
									}
									else {
										fundingPo.setFundSrcStartDate(sdf.parse(upgradeWebVo.getRequestPostStartDate().get(i)));
									}
									if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getEndDateStr() != null &&
											!"".equals(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getEndDateStr())) {
										fundingPo.setFundSrcEndDate(sdf.parse(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getEndDateStr()));
									}
									else {
										fundingPo.setFundSrcEndDate(null);
									}
									
									if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcFte() != null) {
										fundingPo.setFundSrcFte(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcFte());
									}
									else {
										fundingPo.setFundSrcFte(null);
									}

									if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcRemark() != null) {
										fundingPo.setFundSrcRemark(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcRemark());
									}
									else {
										fundingPo.setFundSrcRemark("");
									}
									
									if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getInst() != null) {
										fundingPo.setInst(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getInst());
									}
									else {
										fundingPo.setInst("");
									}
									
									if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getSection() != null) {
										fundingPo.setSection(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getSection());
									}
									else {
										fundingPo.setSection("");
									}
									
									if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getAnalytical() != null) {
										fundingPo.setAnalytical(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getAnalytical());
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
						if (rp.getRequestFundingList().size() > upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().size()) {
							for (int v=upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().size(); v<rp.getRequestFundingList().size(); v++) {
								rp.getRequestFundingList().get(v).setRecState(MPRSConstant.MPRS_STATE_INACTIVE);
							}
						}
						
						System.out.println("--Resources Support Tab Saved --");
						RequestFundingResourcePo rfs = null;
						if (!isExisting) {
							rfs = new RequestFundingResourcePo();
						}
						else {
							rfs = rp.getRequestFundingResource();
						}
						
						rfs.setResourcesSupportFrExt(upgradeWebVo.getRequestResSupFrExt().size()!=0?upgradeWebVo.getRequestResSupFrExt().get(i):"");
						rfs.setResourcesSupportRemark(upgradeWebVo.getRequestResSupRemark().size()!=0?upgradeWebVo.getRequestResSupRemark().get(i):"");
						rp.setRequestFundingResource(rfs);
						if (!isExisting) {
							newRequest.addRequestPosition(rp);
						}
						else {
							for (int j=0; j<newRequest.getRequestPositionList().size(); j++) {
								if (newRequest.getRequestPositionList().get(j).getRequestPostId() == Integer.parseInt(upgradeWebVo.getRequestPostNoTo().get(i))) {
									newRequest.getRequestPositionList().set(j, rp);
									break;
								}
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
					String tmpClusterCode = upgradeWebVo.getRequestCluster().size()!=0?upgradeWebVo.getRequestCluster().get(0):"";
					String tmpInstCode = upgradeWebVo.getRequestInst().size()!=0?upgradeWebVo.getRequestInst().get(0):"";

					String tmpRequestId = requestSvc.generateRequestNo(tmpClusterCode, tmpInstCode, "U", scurrentYear);
					System.out.println("tmpRequestIdCFM: " + tmpRequestId);
	
					newRequest.setRequestId(tmpRequestId);
					RequestTypePo requestType = new RequestTypePo();
					requestType.setRqTypeCode(MPRSConstant.REQUEST_TYPE_UPGRADE);
					newRequest.setRequestType(requestType);
					
					RequestStatusPo requestStatus = new RequestStatusPo();
					requestStatus.setStatusCode(MPRSConstant.REQUEST_STATUS_NEW);
					newRequest.setRequestStatus(requestStatus);
					newRequest.setRequestDate(new Date());
					if (upgradeWebVo.getEffectiveDate() != null && !"".equals(upgradeWebVo.getEffectiveDate())) {
						newRequest.setEffectFrom(sdf.parse(upgradeWebVo.getEffectiveDate()));
					}
					newRequest.setRequestReason(upgradeWebVo.getRequestReason());
					newRequest.setCurrentWFUser(userId);
					newRequest.setCurrentWFGroup(currentRole);
					newRequest.setRequester(upgradeWebVo.getRequester());
					newRequest.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
					
					if (upgradeWebVo.getApprovalDate() != null && !"".equals(upgradeWebVo.getApprovalDate())) {
						newRequest.setApprovalDate(sdf.parse(upgradeWebVo.getApprovalDate()));
					}
					
					newRequest.setApprovalReference(upgradeWebVo.getApprovalReference());
					newRequest.setApprovalRemark(upgradeWebVo.getApprovalRemark());
					// List<RequestPosition> tmpList = new ArrayList<RequestPosition>();
					// newRequest.setRequestPositionList(tmpList);
					// for (int x= 0; x<newRequest.getRequestPositionList().size(); x++) {
					//	newRequest.removeRequestPosition(newRequest.getRequestPositionList().get(0));
					//  }
					for (int i=0; i<upgradeWebVo.getRequestPostNo().size(); i++) {
						// RequestPosition rp = requestSvc.getRequestPositionByPostNo(Integer.parseInt(upgradeWebVo.getRequestPostNo().get(i)));
						System.out.println("Add RequestPoPosition!!");
						System.out.println("upgradeWebVo.getRequestPostNo().get(i): " + upgradeWebVo.getRequestPostNo().get(i));
						
						RequestPostPo rp = new RequestPostPo();
						PostMasterPo mprsPost = requestSvc.getPostByPostUid(Integer.parseInt(upgradeWebVo.getRequestPostNo().get(i)));
						rp.setPostNo(upgradeWebVo.getRequestPostNo().get(i));
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
					
					for (int i=0; i<upgradeWebVo.getRequestCluster().size(); i++) {
						System.out.println("Add RequestPoPosition To!!");
						
						RequestPostPo rp = new RequestPostPo();
						if (upgradeWebVo.getRequestRank().size()!=0) {
							RankPo rank = new RankPo();
							rank.setRankCode(upgradeWebVo.getRequestRank().get(i));
							rp.setRank(rank);
						}
						rp.setPostId("");
						rp.setClusterCode(upgradeWebVo.getRequestCluster().size()!=0?upgradeWebVo.getRequestCluster().get(i):"");
						rp.setInstCode(upgradeWebVo.getRequestInst().size()!=0?upgradeWebVo.getRequestInst().get(i):"");
						rp.setDeptCode(upgradeWebVo.getRequestDept().size()!=0?upgradeWebVo.getRequestDept().get(i):"");
						rp.setStaffGroupCode(upgradeWebVo.getRequestStaffGroup().size()!=0?upgradeWebVo.getRequestStaffGroup().get(i):"");
						rp.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
						rp.setHcmPositionId(upgradeWebVo.getHcmPositionId().size()!=0?upgradeWebVo.getHcmPositionId().get(i):"");
						rp.setHoBuyServiceInd(upgradeWebVo.getRequestHoBuyServiceInd().size()!=0?upgradeWebVo.getRequestHoBuyServiceInd().get(i):"");
						rp.setToPostInd("Y");
						rp.setSubSpecialtyCode(upgradeWebVo.getRequestSubSpecialty().size()!=0?upgradeWebVo.getRequestSubSpecialty().get(i):"");
						
						System.out.println("--Position Tab Saved --");
						rp.setUnit(upgradeWebVo.getRequestUnit().size()!=0?upgradeWebVo.getRequestUnit().get(i):"");
						rp.setPostTitle(upgradeWebVo.getRequestPostTitle().size()!=0?upgradeWebVo.getRequestPostTitle().get(i):"");
						rp.setPostDuration(upgradeWebVo.getRequestPostDuration().size()!=0?upgradeWebVo.getRequestPostDuration().get(i):"");
						rp.setLimitDurationType(upgradeWebVo.getRequestRdDuration().size()!=0?upgradeWebVo.getRequestRdDuration().get(i):"");
						if (upgradeWebVo.getRequestPostStartDate().size() !=0 )
							if (!"".equals(upgradeWebVo.getRequestPostStartDate().get(i)))
								rp.setPostStartDate(sdf.parse(upgradeWebVo.getRequestPostStartDate().get(i)));//review
						rp.setLimitDurationNo(upgradeWebVo.getRequestDurationValue().size()!=0?upgradeWebVo.getRequestDurationValue().get(i):null);
						rp.setLimitDurationUnit(upgradeWebVo.getRequestDurationUnit().size()!=0?upgradeWebVo.getRequestDurationUnit().get(i):"");
						
						if (upgradeWebVo.getRequestPostActualEndDate().size() != 0) {
							if (!"".equals(upgradeWebVo.getRequestPostActualEndDate().get(i))) {
								rp.setLimitDurationEndDate(sdf.parse(upgradeWebVo.getRequestPostActualEndDate().get(i)));
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
						
						rp.setPostRemark(upgradeWebVo.getRequestPostRemark().size()!=0?upgradeWebVo.getRequestPostRemark().get(i):"");
						rp.setPostFTEType(upgradeWebVo.getRequestPostFTE().size()!=0?upgradeWebVo.getRequestPostFTE().get(i):"");
						rp.setPostFTE(upgradeWebVo.getRequestPostFTEValue().size()!=0?upgradeWebVo.getRequestPostFTEValue().get(i):"");
						rp.setPostStatus(upgradeWebVo.getRequestPositionStatus().size()!=0?upgradeWebVo.getRequestPositionStatus().get(i):"");
						if (upgradeWebVo.getRequestPositionStartDate().size() != 0)
							if (!"".equals(upgradeWebVo.getRequestPositionStartDate().get(i)))
								rp.setPostStatusStartDate(sdf.parse(upgradeWebVo.getRequestPositionStartDate().get(i)));
						
						if (upgradeWebVo.getRequestPositionEndDate().size() != 0)	
							if (!"".equals(upgradeWebVo.getRequestPositionEndDate().get(i)))
								rp.setPostStatusEndDate(sdf.parse(upgradeWebVo.getRequestPositionEndDate().get(i)));
						rp.setClusterRef(upgradeWebVo.getRequestClusterRefNo().size()!=0?upgradeWebVo.getRequestClusterRefNo().get(i):"");
						rp.setClusterRemark(upgradeWebVo.getRequestClusterRemark().size()!=0?upgradeWebVo.getRequestClusterRemark().get(i):"");
						rp.setProposedPostId(upgradeWebVo.getRequestProposedPostId().size()!=0?upgradeWebVo.getRequestProposedPostId().get(i):"");
				
						System.out.println("--Funding Details Tab --");
						if (upgradeWebVo.getRequestPositionToList() != null) {
							if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList() != null) {
								for (int j=0; j<upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().size(); j++) {
									RequestFundingPo fundingPo = null;
									if (j < rp.getRequestFundingList().size()) {
										fundingPo = rp.getRequestFundingList().get(j);
									}
									else {
										fundingPo = new RequestFundingPo();
									}
									
									fundingPo.setFundingSeqNo(j+1);
									
									if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getAnnualPlanInd() != null) {
										fundingPo.setAnnualPlanInd(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getAnnualPlanInd());
									}
									else {
										fundingPo.setAnnualPlanInd("");
									}
									
									if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramYear() != null) {
										fundingPo.setProgramYear(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramYear());
									}
									else {
										fundingPo.setProgramYear("");
									}
									
									if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramCode() != null) {
										fundingPo.setProgramCode(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramCode());
									}
									else {
										fundingPo.setProgramCode("");
									}
									
									if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramName() != null) {
										fundingPo.setProgramName(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramName());
									}
									else {
										fundingPo.setProgramName("");
									}
									
									if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramTypeCode() != null) {
										fundingPo.setProgramTypeCode(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramTypeCode());
									}
									else {
										fundingPo.setProgramTypeCode("");
									}
									
									if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcId() != null) {
										fundingPo.setFundSrcId(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcId());
									}
									else {
										fundingPo.setFundSrcId("");
									}
									
									if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcSubCatId() != null) {
										fundingPo.setFundSrcSubCatId(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcSubCatId());
									}
									else {
										fundingPo.setFundSrcSubCatId("");
									}
									
									if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getStartDateStr() != null &&
											!"".equals(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getStartDateStr())) {
										fundingPo.setFundSrcStartDate(sdf.parse(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getStartDateStr()));
									}
									else {
										fundingPo.setFundSrcStartDate(sdf.parse(upgradeWebVo.getRequestPostStartDate().get(i)));
									}
									if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getEndDateStr() != null &&
											!"".equals(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getEndDateStr())) {
										fundingPo.setFundSrcEndDate(sdf.parse(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getEndDateStr()));
									}
									else {
										fundingPo.setFundSrcEndDate(null);
									}
									
									if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcFte() != null) {
										fundingPo.setFundSrcFte(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcFte());
									}
									else {
										fundingPo.setFundSrcFte(null);
									}
									
									if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcRemark() != null) {
										fundingPo.setFundSrcRemark(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcRemark());
									}
									else {
										fundingPo.setFundSrcRemark("");
									}
									
									if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getInst() != null) {
										fundingPo.setInst(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getInst());
									}
									else {
										fundingPo.setInst("");
									}
									
									if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getSection() != null) {
										fundingPo.setSection(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getSection());
									}
									else {
										fundingPo.setSection("");
									}
									
									if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getAnalytical() != null) {
										fundingPo.setAnalytical(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getAnalytical());
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
						if (rp.getRequestFundingList().size() > upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().size()) {
							for (int v=upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().size(); v<rp.getRequestFundingList().size(); v++) {
								rp.getRequestFundingList().get(v).setRecState(MPRSConstant.MPRS_STATE_INACTIVE);
							}
						}
						
						System.out.println("--Resources Support Tab Saved --");
						RequestFundingResourcePo rfs = new RequestFundingResourcePo();
						rp.setRequestFundingResource(rfs);
						rfs.setResourcesSupportFrExt(upgradeWebVo.getRequestResSupFrExt().size()!=0?upgradeWebVo.getRequestResSupFrExt().get(i):"");
						rfs.setResourcesSupportRemark(upgradeWebVo.getRequestResSupRemark().size()!=0?upgradeWebVo.getRequestResSupRemark().get(i):"");
	
						newRequest.addRequestPosition(rp);
					}
				}
				
				
				
				List<RequestAttachmentPo> attachmentList = new ArrayList<RequestAttachmentPo>();
				if (upgradeWebVo.getUploadFileId() != null && !"".equals(upgradeWebVo.getUploadFileId())) {
					for (int i=0; i<upgradeWebVo.getUploadFileId().size(); i++) {
						if (upgradeWebVo.getUploadFileId().get(i) != null && !"".equals(upgradeWebVo.getUploadFileId().get(i))) {
							RequestTempFilePo tempFile = requestSvc.getTempFile(Integer.parseInt(upgradeWebVo.getUploadFileId().get(i)));
	
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
				
				// upgradeWebVo.setRequestId(tmpRequestId);
				upgradeWebVo.setRequestId(newRequest.getRequestId());
				upgradeWebVo.setRequestStatus("N");
				int requestNo = -1;
				if (upgradeWebVo.getRequestNo() != null && !"".equals(upgradeWebVo.getRequestNo())) {
					requestSvc.update(newRequest, upgradeWebVo.getRemoveAttachmentUid(), upgradeWebVo.getUploadFileId(), user);
					requestNo = newRequest.getRequestNo();
				}
				else {
					requestNo = requestSvc.insert(newRequest, upgradeWebVo.getUploadFileId(), true, null, null, user);
				}
	
				// Retrieve again the RequestPo
				RequestPo requestCase = requestSvc.getRequestByRequestNo(requestNo);
				
				upgradeWebVo = convertToWebVo(requestCase, userId, currentRole);
				upgradeWebVo.setDisplayMessage("Request update successful.");
				upgradeWebVo.setUpdateSuccess("Y");
			}
			catch (Exception ex) {
				EClaimLogger.error("performUpdate - update:" + ex.getMessage(), ex);
				String errorMsg = doHandleException(ex.getCause(), ex.getMessage());
				
				if (upgradeWebVo.getRequestNo() != null && !"".equals(upgradeWebVo.getRequestNo())) {
					RequestPo requestCase = requestSvc.getRequestByRequestNo(Integer.parseInt(upgradeWebVo.getRequestNo()));
					upgradeWebVo = convertToWebVo(requestCase, userId, currentRole);
				}
				
				System.out.println("errorMsg: " + errorMsg);
				upgradeWebVo.setUpdateSuccess("N");
				upgradeWebVo.setDisplayMessage(errorMsg);
			}
		}
		else if ("WITHDRAW".equalsIgnoreCase(formAction)) {
			System.out.println("RequestPoNo.: " + upgradeWebVo.getRequestNo());
			int requestNo = Integer.parseInt(upgradeWebVo.getRequestNo());
			String errorMsg = requestSvc.submitWithdraw(requestNo,  MPRSConstant.REQUEST_TYPE_UPGRADE, userId, currentRole);

			if ("".equals(errorMsg) || errorMsg == null) {
				// Retrieve again the RequestPo
				RequestPo requestCase = requestSvc.getRequestByRequestNo(requestNo);
				
				upgradeWebVo = convertToWebVo(requestCase, userId, currentRole);
				upgradeWebVo.setUpdateSuccess("Y");
				upgradeWebVo.setDisplayMessage("Request withdraw successful.");
			}
			else {
				System.out.println("errorMsg: " + errorMsg);
				upgradeWebVo.setUpdateSuccess("N");
				upgradeWebVo.setDisplayMessage(errorMsg);
			}
			
			return new ModelAndView("request/upgrade", "formBean", upgradeWebVo);
		}
		else if ("SUBMIT".equalsIgnoreCase(formAction)) {
			if ("FIN_OFFICER".equals(currentRole) || "HR_OFFICER".equals(currentRole)) {
				System.out.println("Location ChangeOfStaffMixController.performUpdate()");
				System.out.println("RequestPoId: " + upgradeWebVo.getRequestId());
				System.out.println("Requester " + upgradeWebVo.getRequester());

				RequestPo newRequest= null;	
				newRequest = requestSvc.getRequestByRequestNo(Integer.parseInt(upgradeWebVo.getRequestNo()));

				if (upgradeWebVo.getEffectiveDate() != null && !"".equals(upgradeWebVo.getEffectiveDate())) {
					newRequest.setEffectFrom(sdf.parse(upgradeWebVo.getEffectiveDate()));
				}
				newRequest.setRequestReason(upgradeWebVo.getRequestReason());
				newRequest.setRequester(upgradeWebVo.getRequester());

				if (upgradeWebVo.getApprovalDate() != null && !"".equals(upgradeWebVo.getApprovalDate())) {
					newRequest.setApprovalDate(sdf.parse(upgradeWebVo.getApprovalDate()));
				}

				newRequest.setApprovalReference(upgradeWebVo.getApprovalReference());
				newRequest.setApprovalRemark(upgradeWebVo.getApprovalRemark());

				// Update from post
				for (int i=0; i<upgradeWebVo.getRequestPostNo().size(); i++) {
					for (int m=0; m<newRequest.getRequestPositionList().size(); m++) {
						if ("Y".equals(newRequest.getRequestPositionList().get(m).getFromPostInd())) {
							RequestPostPo rp = newRequest.getRequestPositionList().get(m);
							PostMasterPo mprsPost = requestSvc.getPostByPostUid(Integer.parseInt(upgradeWebVo.getRequestPostNo().get(i)));
							rp.setPostNo(upgradeWebVo.getRequestPostNo().get(i));
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
							newRequest.getRequestPositionList().set(m, rp);
							break;
						}
					}
				}
				
				for (int i=0; i<upgradeWebVo.getRequestCluster().size(); i++) {
					// System.out.println("upgradeWebVo.getRequestPostNoTo(): " + upgradeWebVo.getRequestPostNoTo().get(i));

					RequestPostPo rp = null;
					boolean isExisting = false;

					// Look up the by requestPostId
					for (int j=0; j<newRequest.getRequestPositionList().size(); j++) {
						if (newRequest.getRequestPositionList().get(j).getRequestPostId() == Integer.parseInt(upgradeWebVo.getRequestPostNoTo().get(i))) {
							isExisting = true;
							rp = newRequest.getRequestPositionList().get(j);
							break;
						}
					}

					if (!isExisting) {
						rp = new RequestPostPo();
						rp.setPostId("");
					} 
					
					boolean needRecycle = true;
					String tmpProposedPostId = rp.getProposedPostId();
					if (tmpProposedPostId != null) {
						String proposedPostId = upgradeWebVo.getRequestProposedPostId().size() != 0 ? upgradeWebVo.getRequestProposedPostId().get(i) : "";
						if (tmpProposedPostId.equals(proposedPostId)) {
							needRecycle = false;
						} else {
							System.out.println("Recycle: " + tmpProposedPostId);
							requestSvc.recycleProposedPostId(rp.getStaffGroupCode(), rp.getPostDuration(), rp.getPostFTEType(), tmpProposedPostId, userId, currentRole);
						}
					}

					System.out.println("Add RequestPoPosition To!!");

					if (upgradeWebVo.getRequestRank().size()!=0) {
						RankPo rank = new RankPo();
						rank.setRankCode(upgradeWebVo.getRequestRank().get(i));
						rp.setRank(rank);
					}

					rp.setClusterCode(upgradeWebVo.getRequestCluster().size()!=0?upgradeWebVo.getRequestCluster().get(i):"");
					rp.setInstCode(upgradeWebVo.getRequestInst().size()!=0?upgradeWebVo.getRequestInst().get(i):"");
					rp.setDeptCode(upgradeWebVo.getRequestDept().size()!=0?upgradeWebVo.getRequestDept().get(i):"");
					rp.setStaffGroupCode(upgradeWebVo.getRequestStaffGroup().size()!=0?upgradeWebVo.getRequestStaffGroup().get(i):"");
					rp.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
					rp.setHcmPositionId(upgradeWebVo.getHcmPositionId().size()!=0?upgradeWebVo.getHcmPositionId().get(i):"");
					rp.setHoBuyServiceInd(upgradeWebVo.getRequestHoBuyServiceInd().size()!=0?upgradeWebVo.getRequestHoBuyServiceInd().get(i):"");
					rp.setToPostInd("Y");

					System.out.println("--Position Tab Saved --");
					rp.setUnit(upgradeWebVo.getRequestUnit().size()!=0?upgradeWebVo.getRequestUnit().get(i):"");
					rp.setPostTitle(upgradeWebVo.getRequestPostTitle().size()!=0?upgradeWebVo.getRequestPostTitle().get(i):"");
					rp.setPostDuration(upgradeWebVo.getRequestPostDuration().size()!=0?upgradeWebVo.getRequestPostDuration().get(i):"");
					rp.setLimitDurationType(upgradeWebVo.getRequestRdDuration().size()!=0?upgradeWebVo.getRequestRdDuration().get(i):"");
					if (upgradeWebVo.getRequestPostStartDate().size() !=0 )
						if (!"".equals(upgradeWebVo.getRequestPostStartDate().get(i)))
							rp.setPostStartDate(sdf.parse(upgradeWebVo.getRequestPostStartDate().get(i)));//review
					rp.setLimitDurationNo(upgradeWebVo.getRequestDurationValue().size()!=0?upgradeWebVo.getRequestDurationValue().get(i):null);
					rp.setLimitDurationUnit(upgradeWebVo.getRequestDurationUnit().size()!=0?upgradeWebVo.getRequestDurationUnit().get(i):"");

					if (upgradeWebVo.getRequestPostActualEndDate().size() != 0) {
						if (!"".equals(upgradeWebVo.getRequestPostActualEndDate().get(i))) {
							rp.setLimitDurationEndDate(sdf.parse(upgradeWebVo.getRequestPostActualEndDate().get(i)));
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

					rp.setPostRemark(upgradeWebVo.getRequestPostRemark().size()!=0?upgradeWebVo.getRequestPostRemark().get(i):"");
					rp.setPostFTEType(upgradeWebVo.getRequestPostFTE().size()!=0?upgradeWebVo.getRequestPostFTE().get(i):"");
					rp.setPostFTE(upgradeWebVo.getRequestPostFTEValue().size()!=0?upgradeWebVo.getRequestPostFTEValue().get(i):"");
					rp.setPostStatus(upgradeWebVo.getRequestPositionStatus().size()!=0?upgradeWebVo.getRequestPositionStatus().get(i):"");
					if (upgradeWebVo.getRequestPositionStartDate().size() != 0)
						if (!"".equals(upgradeWebVo.getRequestPositionStartDate().get(i)))
							rp.setPostStatusStartDate(sdf.parse(upgradeWebVo.getRequestPositionStartDate().get(i)));
					if (upgradeWebVo.getRequestPositionEndDate().size() != 0)	
						if (!"".equals(upgradeWebVo.getRequestPositionEndDate().get(i)))
							rp.setPostStatusEndDate(sdf.parse(upgradeWebVo.getRequestPositionEndDate().get(i)));
					rp.setClusterRef(upgradeWebVo.getRequestClusterRefNo().size()!=0?upgradeWebVo.getRequestClusterRefNo().get(i):"");
					rp.setClusterRemark(upgradeWebVo.getRequestClusterRemark().size()!=0?upgradeWebVo.getRequestClusterRemark().get(i):"");

					rp.setSubSpecialtyCode(upgradeWebVo.getRequestSubSpecialty().size()!=0?upgradeWebVo.getRequestSubSpecialty().get(i):"");

					String clusterCode = upgradeWebVo.getRequestCluster().size()!=0?upgradeWebVo.getRequestCluster().get(i):"";
					String instCode = upgradeWebVo.getRequestInst().size()!=0?upgradeWebVo.getRequestInst().get(i):"";
					String deptCode = upgradeWebVo.getRequestDept().size()!=0?upgradeWebVo.getRequestDept().get(i):"";
					String rankCode = upgradeWebVo.getRequestRank().size()!=0?upgradeWebVo.getRequestRank().get(i):"";
					String postFte = upgradeWebVo.getRequestPostFTE().size()!=0?upgradeWebVo.getRequestPostFTE().get(i):"";
					String proposedPostId = upgradeWebVo.getRequestProposedPostId().size()!=0?upgradeWebVo.getRequestProposedPostId().get(i):"";
					String staffGroupCode  = upgradeWebVo.getRequestStaffGroup().size()!=0?upgradeWebVo.getRequestStaffGroup().get(i):"";
					String postDuration = upgradeWebVo.getRequestPostDuration().size()!=0?upgradeWebVo.getRequestPostDuration().get(i):"";
					System.out.println("upgradeWebVo.getCluster(): " + clusterCode); 
					System.out.println("upgradeWebVo.getInstitution(): " + instCode); 
					System.out.println("upgradeWebVo.getDepartment(): " +deptCode); 
					System.out.println("upgradeWebVo.getRank(): " + rankCode);
					System.out.println("upgradeWebVo.getPostFTE(): " + postFte);
					System.out.println("upgradeWebVo.getproposedPostId(): " + proposedPostId);
					System.out.println("upgradeWebVo.getpostDuration(): " + postDuration);
					
					List<String> annualPlanList = new ArrayList<String>();
					List<String> programTypeList = new ArrayList<String>();
					
					if (upgradeWebVo.getRequestPositionToList() != null) {
						if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList() != null) {
							for (int j=0; j<upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().size(); j++) {
								annualPlanList.add(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getAnnualPlanInd());
								programTypeList.add(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramTypeCode());
							}
						}
					}
					
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

					System.out.println("--Funding Details Tab --");
					if (upgradeWebVo.getRequestPositionToList() != null) {
						if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList() != null) {
							for (int j=0; j<upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().size(); j++) {
								RequestFundingPo fundingPo = null;
								if (j < rp.getRequestFundingList().size()) {
									fundingPo = rp.getRequestFundingList().get(j);
								}
								else {
									fundingPo = new RequestFundingPo();
								}
								
								fundingPo.setFundingSeqNo(j+1);
								
								if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getAnnualPlanInd() != null) {
									fundingPo.setAnnualPlanInd(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getAnnualPlanInd());
								}
								else {
									fundingPo.setAnnualPlanInd("");
								}
								
								if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramYear() != null) {
									fundingPo.setProgramYear(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramYear());
								}
								else {
									fundingPo.setProgramYear("");
								}
								
								if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramCode() != null) {
									fundingPo.setProgramCode(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramCode());
								}
								else {
									fundingPo.setProgramCode("");
								}
								
								if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramName() != null) {
									fundingPo.setProgramName(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramName());
								}
								else {
									fundingPo.setProgramName("");
								}
								
								if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramTypeCode() != null) {
									fundingPo.setProgramTypeCode(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getProgramTypeCode());
								}
								else {
									fundingPo.setProgramTypeCode("");
								}
								
								if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcId() != null) {
									fundingPo.setFundSrcId(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcId());
								}
								else {
									fundingPo.setFundSrcId("");
								}
								
								if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcSubCatId() != null) {
									fundingPo.setFundSrcSubCatId(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcSubCatId());
								}
								else {
									fundingPo.setFundSrcSubCatId("");
								}
								
								if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getStartDateStr() != null &&
										!"".equals(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getStartDateStr())) {
									fundingPo.setFundSrcStartDate(sdf.parse(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getStartDateStr()));
								}
								else {
									fundingPo.setFundSrcStartDate(sdf.parse(upgradeWebVo.getRequestPostStartDate().get(i)));
								}
								if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getEndDateStr() != null &&
										!"".equals(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getEndDateStr())) {
									fundingPo.setFundSrcEndDate(sdf.parse(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getEndDateStr()));
								}
								else {
									fundingPo.setFundSrcEndDate(null);
								}
								
								if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcFte() != null) {
									fundingPo.setFundSrcFte(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcFte());
								}
								else {
									fundingPo.setFundSrcFte(null);
								}

								if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcRemark() != null) {
									fundingPo.setFundSrcRemark(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getFundSrcRemark());
								}
								else {
									fundingPo.setFundSrcRemark("");
								}
								
								if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getInst() != null) {
									fundingPo.setInst(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getInst());
								}
								else {
									fundingPo.setInst("");
								}
								
								if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getSection() != null) {
									fundingPo.setSection(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getSection());
								}
								else {
									fundingPo.setSection("");
								}
								
								if (upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getAnalytical() != null) {
									fundingPo.setAnalytical(upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().get(j).getAnalytical());
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
					if (rp.getRequestFundingList().size() > upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().size()) {
						for (int v=upgradeWebVo.getRequestPositionToList().get(i).getRequestFundingList().size(); v<rp.getRequestFundingList().size(); v++) {
							rp.getRequestFundingList().get(v).setRecState(MPRSConstant.MPRS_STATE_INACTIVE);
						}
					}
					
					System.out.println("--Resources Support Tab Saved --");
					RequestFundingResourcePo rfs = null;
					if (!isExisting) {
						rfs = new RequestFundingResourcePo();
					}
					else {
						rfs = rp.getRequestFundingResource();
					}

					rfs.setResourcesSupportFrExt(upgradeWebVo.getRequestResSupFrExt().size()!=0?upgradeWebVo.getRequestResSupFrExt().get(i):"");
					rfs.setResourcesSupportRemark(upgradeWebVo.getRequestResSupRemark().size()!=0?upgradeWebVo.getRequestResSupRemark().get(i):"");
					rp.setRequestFundingResource(rfs);
					if (!isExisting) {
						newRequest.addRequestPosition(rp);
					}
					else {
						for (int j=0; j<newRequest.getRequestPositionList().size(); j++) {
							if (newRequest.getRequestPositionList().get(j).getRequestPostId() == Integer.parseInt(upgradeWebVo.getRequestPostNoTo().get(i))) {
								newRequest.getRequestPositionList().set(j, rp);
								break;
							}
						}
					}
				}
				
				List<RequestAttachmentPo> attachmentList = new ArrayList<RequestAttachmentPo>();
				if (upgradeWebVo.getUploadFileId() != null && !"".equals(upgradeWebVo.getUploadFileId())) {
					for (int i=0; i<upgradeWebVo.getUploadFileId().size(); i++) {
						if (upgradeWebVo.getUploadFileId().get(i) != null && !"".equals(upgradeWebVo.getUploadFileId().get(i))) {
							RequestTempFilePo tempFile = requestSvc.getTempFile(Integer.parseInt(upgradeWebVo.getUploadFileId().get(i)));

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

				requestSvc.update(newRequest, upgradeWebVo.getRemoveAttachmentUid(), upgradeWebVo.getUploadFileId(), user);
			}
			
			System.out.println("RequestPoNo.: " + upgradeWebVo.getRequestNo());
			int requestNo = Integer.parseInt(upgradeWebVo.getRequestNo());
			
			String emailTo = "";
			String emailCC = "";
			String emailTitle = "";
			String emailContent = "";
			String generateEmail = "N";
			
			if ("Y".equals(upgradeWebVo.getSubmitWithModifiedEmail())) {
				emailTo = upgradeWebVo.getEmailTo();
				emailCC = upgradeWebVo.getEmailCC();
				emailTitle = upgradeWebVo.getEmailTitle();
				String si = StrHelper.format(upgradeWebVo.getEmailSuppInfo().replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$"));
				emailContent = upgradeWebVo.getEmailContent().replaceAll("<Supplementary Information / Comment>", si);
				String url = appUrl + "/request/upgrade?rq=" + upgradeWebVo.getRequestNo() + "&fromMail=Y";
				emailContent = emailContent.replaceAll("Please click here to review the request", "Please click <a href='" + url + "'>here</a> to review the request");
				generateEmail = "Y";
			}
			
			if ("Y".equals(upgradeWebVo.getSubmitWithEmail())) {
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

				String receiver = "";
				if (!"".equals(upgradeWebVo.getNextWFUser())) {
					UserPo requester = securitySvc.findUser(upgradeWebVo.getNextWFUser());
					receiver = requester.getUserName();
				}
				else {
					List<RolePo> userGroupList = securitySvc.getAllRole();
					for (int i=0; i<userGroupList.size(); i++) {
						if (upgradeWebVo.getNextWFGroup().equals(userGroupList.get(i).getRoleId())) {
							receiver = userGroupList.get(i).getRoleName();
							break;
						}
					}
				}
				
				EmailTemplatePo emailTemplate = commonSvc.getTemplateByTemplateId("RQ_UPG_SUBMIT", new String[] {String.valueOf(upgradeWebVo.getRequestId()),
																												postId, 
																												submittedBy, 
																												submittedDate, 
																												postId2,																											
																												"", 
																												"", 
																												"", 
																												receiver});
				
				// Set email List
				String emailList = "";
				if (!"".equals(upgradeWebVo.getNextWFUser()) && upgradeWebVo.getNextWFUser() != null) {
					try {
						UserPo tUser = securitySvc.findUser( upgradeWebVo.getNextWFUser());
						emailList = tUser.getEmail();
					} catch (Exception ex) {
						EClaimLogger.error("performUpdate - send Email:" + ex.getMessage(), ex);
					}
				}
				else {
					RequestPo requestCaseTemp = requestSvc.getRequestByRequestNo(requestNo);
					String clusterCode = "";
					String instCode = "";
					for (int j=0; j<requestCaseTemp.getRequestPositionList().size(); j++) {
						clusterCode = requestCaseTemp.getRequestPositionList().get(j).getClusterCode();
						instCode = requestCaseTemp.getRequestPositionList().get(j).getInstCode();
						System.out.println("a: Lookup the email list for " + clusterCode + "/" + instCode);
						List<UserPo> userList = securitySvc.getAllUserByGroupAndCluster(clusterCode, instCode, upgradeWebVo.getNextWFGroup());
						
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
				
				if ("Y".equals(upgradeWebVo.getReturnCase())) { 
					List<UserPo> ccUserList = securitySvc.getReturnUserFromWorkflowHistoryAll(upgradeWebVo.getRequestNo(), upgradeWebVo.getNextWFGroup());
					for (int i=0; i<ccUserList.size(); i++) {
						if (ccUserList.get(i).getEmail() != null) {
							if (!"".equals(ccEmailList)) 
								ccEmailList += ", ";
							
							ccEmailList += ccUserList.get(i).getEmail();
						}
					}
				}
				
				// If submit to MG team, cc to all related party
				else if ("HO_MG_OFFICER".equals(upgradeWebVo.getNextWFGroup())) {
					List<UserPo> ccUserList = securitySvc.getRelatedUserFromWorkflowHistoryAll(upgradeWebVo.getRequestNo()) ;
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
				String url = appUrl + "/request/upgrade?rq=" + upgradeWebVo.getRequestNo() + "&fromMail=Y";
				emailContent = emailContent.replaceAll("Please click here to review the request", "Please click <a href='" + url + "'>here</a> to review the request");
				
				generateEmail = "Y";
			}
			
			System.out.println("emailContent: " + emailContent);
			
			String errorMsg = requestSvc.submitWorkflow(requestNo,  MPRSConstant.REQUEST_TYPE_UPGRADE,
					                                    upgradeWebVo.getNextWFGroup(), 
					                                    upgradeWebVo.getNextWFUser(), 
					                                    upgradeWebVo.getReturnCase(),
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

				upgradeWebVo = convertToWebVo(requestCase, userId, currentRole);
				upgradeWebVo.setUpdateSuccess("Y");
				upgradeWebVo.setDisplayMessage("Request submit successful.");
			}
			else {
				System.out.println("errorMsg: " + errorMsg);
				upgradeWebVo.setUpdateSuccess("N");
				upgradeWebVo.setDisplayMessage(errorMsg);
			}

			return new ModelAndView("request/upgrade", "formBean", upgradeWebVo);
		}
		else if ("APPROVE".equalsIgnoreCase(formAction)) {
			System.out.println("RequestPoNo. submit approve: " + upgradeWebVo.getRequestNo());
			try {
				int requestNo = Integer.parseInt(upgradeWebVo.getRequestNo());
				
				String emailTo = "";
				String emailCC = "";
				String emailTitle = "";
				String emailContent = "";
				String generateEmail = "N";
				
				if ("Y".equals(upgradeWebVo.getSubmitWithModifiedEmail())) {
					emailTo = upgradeWebVo.getEmailTo();
					emailCC = upgradeWebVo.getEmailCC();
					emailTitle = upgradeWebVo.getEmailTitle();
					String url = appUrl + "/request/upgrade?rq=" + upgradeWebVo.getRequestNo() + "&fromMail=Y";
					emailContent = upgradeWebVo.getEmailContent().replaceAll("Please click here to review the request", "Please click <a href='" + url + "'>here</a> to review the request") + "<br/>" + StrHelper.format(upgradeWebVo.getEmailSuppInfo());
					
					generateEmail = "Y";
				}
				
				if ("Y".equals(upgradeWebVo.getSubmitWithEmail())) {
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
					
					String receiver = "ALL";
					EmailTemplatePo emailTemplate = commonSvc.getTemplateByTemplateId("RQ_UPG_CONFIRM", new String[] {String.valueOf(upgradeWebVo.getRequestId()),
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
					
					List<UserPo> ccUserList = securitySvc.getRelatedUserFromWorkflowHistoryAll(upgradeWebVo.getRequestNo()) ;
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
					String url = appUrl + "/request/upgrade?rq=" + upgradeWebVo.getRequestNo() + "&fromMail=Y";
					emailContent = emailContent.replaceAll("Please click here to review the request", "Please click <a href='" + url + "'>here</a> to review the request");
	
					generateEmail = "Y";
				}

				emailTo = StrHelper.removeDuplicateEmail(emailTo);
				emailCC = StrHelper.removeDuplicateEmail(emailCC);
				
				String errorMsg = requestSvc.submitApprove(requestNo, MPRSConstant.REQUEST_TYPE_UPGRADE,
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
	
					upgradeWebVo = convertToWebVo(requestCase, userId, currentRole);
					upgradeWebVo.setUpdateSuccess("Y");
					upgradeWebVo.setDisplayMessage("Request approve successful.");
				}
				else {
					System.out.println("errorMsg: " + errorMsg);
					upgradeWebVo.setUpdateSuccess("N");
					upgradeWebVo.setDisplayMessage(errorMsg);
				}
			}
			catch (Exception ex) {
				EClaimLogger.error("performUpdate - send Email:" + ex.getMessage(), ex);
				String errorMsg = doHandleException(ex.getCause(), ex.getMessage());
				
				// Re-load the case
				RequestPo requestCase = requestSvc.getRequestByRequestNo(Integer.parseInt(upgradeWebVo.getRequestNo()));
				upgradeWebVo = convertToWebVo(requestCase, userId, currentRole);
				
				System.out.println("errorMsg: " + errorMsg);
				upgradeWebVo.setUpdateSuccess("N");
				upgradeWebVo.setDisplayMessage(errorMsg);
			}
			
			return new ModelAndView("request/upgrade", "formBean", upgradeWebVo);
		}
		
		return new ModelAndView("request/upgrade", "formBean", upgradeWebVo);
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
	
	private UpgradeWebVo convertToWebVo(RequestPo requestCase, String currentUser, String currentRole) {
		UpgradeWebVo vo = new UpgradeWebVo();
		vo.setCurrentLoginUser(currentUser);
		vo.setRequestNo(String.valueOf(requestCase.getRequestNo()));
		vo.setRequestId(requestCase.getRequestId());
		vo.setRequestType(MPRSConstant.REQUEST_TYPE_UPGRADE);
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
		
		String staffGroup = "";
		List<RequestPostPo> requestPositionFromList = new ArrayList<RequestPostPo>();
		List<RequestPostPo> requestPositionToList = new ArrayList<RequestPostPo>();
		for (int i=0; i<requestCase.getRequestPositionList().size(); i++) {
			requestCase.getRequestPositionList().get(i).setPostFTE(StrHelper.formatDecimal(requestCase.getRequestPositionList().get(i).getPostFTE()));
			
			if ("Y".equals(requestCase.getRequestPositionList().get(i).getFromPostInd())) {
				requestPositionFromList.add(requestCase.getRequestPositionList().get(i));
			}
			
			if ("Y".equals(requestCase.getRequestPositionList().get(i).getToPostInd())) {
				requestPositionToList.add(requestCase.getRequestPositionList().get(i));
			}
			
			staffGroup = requestCase.getRequestPositionList().get(i).getStaffGroupCode();
		}
		
		vo.setTmpStaffGroup(staffGroup);
		
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
		for (int i=0; i<requestCase.getRequestPositionList().size(); i++) {
			staffGroupCode = requestCase.getRequestPositionList().get(i).getStaffGroupCode();
			postDurationCode = requestCase.getRequestPositionList().get(i).getPostDuration();
			postFteType = requestCase.getRequestPositionList().get(i).getPostFTEType();
			
			System.out.println("Checking: " + staffGroupCode + "/" + postDurationCode + "/" + postFteType);
			if (PostConstant.POST_DUR_VALUE_RECURRENT.equals(postDurationCode) || 
					(PostConstant.POST_DUR_VALUE_TL_CONTRACT.equals(postDurationCode) && PostConstant.POST_FTE_TYPE_FULL_TIME.equals(postFteType))) {
				mgTeamInd = "Y";
			}
		}
		System.out.println("### Final mgTeamInd:" + mgTeamInd);
		vo.setMgTeamInd(mgTeamInd);
		
		// Set the button of workflow button
		RequestWorkflowRoutePo route = this.requestSvc.getDefaultRoute(staffGroupCode, MPRSConstant.REQUEST_TYPE_UPGRADE, vo.getRequestStatus(), mgTeamInd);
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
				if (requestSvc.hasApprovalRight(staffGroupCode, MPRSConstant.REQUEST_TYPE_UPGRADE, vo.getRequestStatus(), currentRole, vo.getMgTeamInd())) {
					vo.setUserHaveApprovalRight("Y");
				}
				else {
					vo.setUserHaveApprovalRight("N");
				}

				if ("HR_OFFICER".equals(currentRole)) {
					vo.setCanEditDetailInfo("Y");
					vo.setUserHaveCreationRight("Y");
					vo.setUserHaveWithdrawRight("Y");
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
		
		// Update the last update date
		vo.setLastUpdateDate(DateTimeHelper.formatDateTimeToString(requestCase.getUpdatedDate()));

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

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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import hk.org.ha.eclaim.bs.request.po.MPRSSearchCriteria;
import hk.org.ha.eclaim.bs.request.po.PostMasterPo;
import hk.org.ha.eclaim.bs.request.po.RequestAttachmentPo;
import hk.org.ha.eclaim.bs.request.po.RequestFundingPo;
import hk.org.ha.eclaim.bs.request.po.RequestPo;
import hk.org.ha.eclaim.bs.request.po.RequestPostPo;
import hk.org.ha.eclaim.bs.request.po.RequestTempFilePo;
import hk.org.ha.eclaim.bs.request.po.RequestWorkflowRoutePo;
import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.model.request.ChangeOfFundingWebVo;
import hk.org.ha.eclaim.model.request.PostResponse;
import hk.org.ha.eclaim.model.request.PostResponseWrapper;
import hk.org.ha.eclaim.bs.cs.po.EmailTemplatePo;
import hk.org.ha.eclaim.bs.cs.po.FundingSourcePo;
import hk.org.ha.eclaim.bs.cs.po.FundingSourceSubCatPo;
import hk.org.ha.eclaim.bs.cs.po.PostDurationPo;
import hk.org.ha.eclaim.bs.cs.po.ProgramTypePo;
import hk.org.ha.eclaim.bs.cs.po.RankPo;
import hk.org.ha.eclaim.bs.cs.po.RequestStatusPo;
import hk.org.ha.eclaim.bs.cs.po.RequestTypePo;
import hk.org.ha.eclaim.bs.security.po.DataAccessPo;
import hk.org.ha.eclaim.bs.security.po.RolePo;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.core.helper.DateTimeHelper;
import hk.org.ha.eclaim.core.helper.StrHelper;
import hk.org.ha.eclaim.core.logger.EClaimLogger;

@Controller
public class ChangeOfFundingController extends CommonRequestController {
	
	@Value("${mail.appurl.prefix}")
	private String appUrl;
	
	@RequestMapping(value="/request/changeOfFunding", method=RequestMethod.GET)
	public ModelAndView viewChangeOfFunding(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("Location ChangeOfFundingController.changingFunding()");

		// Get the user name from cookie
		String userId = this.getSessionUser(request).getUserId();
		UserPo user = securitySvc.findUser(userId);
        user.setCurrentRole((String)request.getSession().getAttribute("currentRole"));
		String currentRole = (String)request.getSession().getAttribute("currentRole");

		if (request.getParameter("rq") != null) {
			EClaimLogger.info("request uid: " + request.getParameter("rq"));

			// Check whether the user have access right for request uid
			int userRoleId = (int) request.getSession().getAttribute("currentUserRoleId");
			List<DataAccessPo> dataAccessList = securitySvc.getDataAccessByRoleId(userRoleId);
			RequestPo requestCase = requestSvc.getRequestByRequestNo(dataAccessList, Integer.parseInt((String)request.getParameter("rq")));
			if (requestCase != null) {
				ChangeOfFundingWebVo vo = convertToVo(requestCase, userId, currentRole);
				vo.setCurrentLoginUser(userId);
				vo.setUserName(user.getUserName());

				return new ModelAndView("request/changeOfFunding", "formBean", vo);
			}
			else {
				return new ModelAndView("redirect:/home/home");
			}
		}
		else {
			EClaimLogger.info("request uid is null");
			ChangeOfFundingWebVo vo = new ChangeOfFundingWebVo();
			
			if ("FIN_OFFICER".equals(currentRole)) {
				vo.setUserHaveSaveRight("Y");
				vo.setCanEditFinancialInfo("Y");
				vo.setUserHaveCreationRight("Y");
			}
			else {
				vo.setCanEditFinancialInfo("N");
				vo.setUserHaveCreationRight("N");
			}
			
			vo.setUserName(user.getUserName());
			return new ModelAndView("request/changeOfFunding", "formBean", vo);
		}
	}


	@RequestMapping(value="/request/changeOfFunding", method=RequestMethod.POST)
	public ModelAndView performUpdate(@ModelAttribute("formBean")ChangeOfFundingWebVo vo, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("Location ChangeOfFundingController.performUpdate()");
		String formAction = vo.getFormAction();

		// Get the user name from cookie
		String userId = this.getSessionUser(request).getUserId();
		UserPo user = securitySvc.findUser(userId);
        user.setCurrentRole((String)request.getSession().getAttribute("currentRole"));
		String currentRole = (String)request.getSession().getAttribute("currentRole");

		System.out.println("requestPost:" + vo.getRequestPostNo().size());
		System.out.println("requestNo: " + vo.getRequestNo());

		Calendar now = Calendar.getInstance();
		String scurrentYear;
		scurrentYear = String.valueOf(now.get(Calendar.YEAR));	
		// String sRequestID = vo.getRequestId();
		System.out.println("RequestID: " + vo.getRequestId());
		
		// Concurrent control
		if ("SAVE".equalsIgnoreCase(formAction) ||
				"WITHDRAW".equalsIgnoreCase(formAction) ||
				"SUBMIT".equalsIgnoreCase(formAction) ||
				"APPROVE".equalsIgnoreCase(formAction)) {
			if (vo.getRequestNo() != null && !"".equals(vo.getRequestNo())) {
				RequestPo requestCase = requestSvc.getRequestByRequestNo(Integer.parseInt(vo.getRequestNo()));

				// Check last update date
				String lastUpdateDate = DateTimeHelper.formatDateTimeToString(requestCase.getUpdatedDate());
				System.out.println("=== " + lastUpdateDate + " vs " + vo.getLastUpdateDate());
				if (!lastUpdateDate.equals(vo.getLastUpdateDate())) {
					vo = convertToVo(requestCase, userId, currentRole);
					vo.setUpdateSuccess("N");
					vo.setDisplayMessage("Record had been updated by other, content is refreshed.");

					return new ModelAndView("request/changeOfFunding", "formBean", vo);
				}
			}
		}

		if ("SAVE".equalsIgnoreCase(formAction)) {
			try { 
				RequestPo newRequest = null;	
	
				if (vo.getRequestNo() != null && !"".equals(vo.getRequestNo())) {
					newRequest = requestSvc.getRequestByRequestNo(Integer.parseInt(vo.getRequestNo()));
				}
				else {
					newRequest = new RequestPo();	
					PostMasterPo mprsPost = requestSvc.getPostByPostUid(Integer.parseInt(vo.getRequestPostNo().get(0)));
					String tmpRequestId = requestSvc.generateRequestNo(mprsPost.getClusterCode(), mprsPost.getInstCode(), "CF", scurrentYear);
					System.out.println("tmpRequestId: " + tmpRequestId);
	
					newRequest.setRequestId(tmpRequestId);
					
					RequestTypePo requestType = new RequestTypePo();
					requestType.setRqTypeCode(MPRSConstant.REQUEST_TYPE_CHANGE_FUNDING);
					newRequest.setRequestType(requestType);
					
					RequestStatusPo requestStatus = new RequestStatusPo();
					requestStatus.setStatusCode(MPRSConstant.REQUEST_STATUS_NEW);
					newRequest.setRequestStatus(requestStatus);
					newRequest.setRequestDate(new Date());
				}
	
				newRequest.setCurrentWFUser(userId);
				newRequest.setCurrentWFGroup(currentRole);
				
				newRequest.setRequestReason(vo.getRequestReason());
				newRequest.setRequester(vo.getRequester());
				
				// Added for UT29939
				newRequest.setEffectFrom(sdf.parse(vo.getEffectiveDate()));
				
				newRequest.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
	
				if (!"".equals(vo.getApprovalDate())) {
					newRequest.setApprovalDate(sdf.parse(vo.getApprovalDate()));
				}
				newRequest.setApprovalReference(vo.getApprovalReference());
				newRequest.setApprovalRemark(vo.getApprovalRemark());
	
				for (int i=0; i<vo.getRequestPostNo().size(); i++) {
					System.out.println("Add Request Position!!");
	
					RequestPostPo rp = null;
					if (newRequest.getRequestPositionList().size() > i) {
						rp = newRequest.getRequestPositionList().get(i);
					}
					else {
						rp = new RequestPostPo();
					}
					
					PostMasterPo mprsPost = requestSvc.getPostByPostUid(Integer.parseInt(vo.getRequestPostNo().get(i)));
					rp.setPostNo(vo.getRequestPostNo().get(i));
					RankPo rank = new RankPo();
					rank.setRankCode(mprsPost.getRankCode());
					rp.setRank(rank);
					rp.setPostId(mprsPost.getPostId());
					rp.setClusterCode(mprsPost.getClusterCode());
					rp.setInstCode(mprsPost.getInstCode());
					rp.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
					rp.setPostDuration(mprsPost.getPostDuration());
					rp.setPostFTE(String.valueOf(mprsPost.getPostFTE()));
					rp.setPostStartDate(mprsPost.getPostStartDate());
					rp.setLimitDurationEndDate(mprsPost.getLimitDurationEndDate());
					rp.setStaffGroupCode(mprsPost.getStaffGroupCode());
					rp.setPostFTEType(mprsPost.getPostFTEType());
					
					// Generate Post ID
					List<String> annualPlanList = new ArrayList<String>();
					List<String> programTypeList = new ArrayList<String>();
					
					if (vo.getRequestPositionList() != null) {
						if (vo.getRequestPositionList().get(i).getRequestFundingList() != null) {
							for (int j=0; j<vo.getRequestPositionList().get(i).getRequestFundingList().size(); j++) {
								annualPlanList.add(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getAnnualPlanInd());
								programTypeList.add(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getProgramTypeCode());
							}
						}
					}
					
					String postId = requestSvc.generateNewPostId(mprsPost.getStaffGroupCode(),
																mprsPost.getClusterCode(),
																mprsPost.getInstCode(), 
																mprsPost.getDeptCode(),
																mprsPost.getRankCode(),
																annualPlanList,
																programTypeList,
																mprsPost.getPostDuration(),
																mprsPost.getPostFTEType(),
																false,
																mprsPost.getPostId());
					
					System.out.println("new Post ID: " + postId);
					rp.setPostId(postId);
					
					//funding info
					if (vo.getRequestPositionList() != null) {
						if (vo.getRequestPositionList().get(i).getRequestFundingList() != null) {
							for (int j=0; j<vo.getRequestPositionList().get(i).getRequestFundingList().size(); j++) {
								RequestFundingPo fundingPo = null;
								if (j < rp.getRequestFundingList().size()) {
									fundingPo = rp.getRequestFundingList().get(j);
								}
								else {
									fundingPo = new RequestFundingPo();
								}
								
								fundingPo.setFundingSeqNo(j+1);
								if (vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getAnnualPlanInd() != null) {
									fundingPo.setAnnualPlanInd(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getAnnualPlanInd());
								}
								else {
									fundingPo.setAnnualPlanInd("");
								}
								
								if (vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getProgramYear() != null) {
									fundingPo.setProgramYear(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getProgramYear());
								}
								else {
									fundingPo.setProgramYear("");
								}
								
								if (vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getProgramCode() != null) {
									fundingPo.setProgramCode(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getProgramCode());
								}
								else {
									fundingPo.setProgramCode("");
								}
								
								if (vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getProgramName() != null) {
									fundingPo.setProgramName(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getProgramName());
								}
								else {
									fundingPo.setProgramName("");
								}
								
								if (vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getProgramTypeCode() != null) {
									fundingPo.setProgramTypeCode(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getProgramTypeCode());
								}
								else {
									fundingPo.setProgramTypeCode("");
								}
								
								if (vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getFundSrcId() != null) {
									fundingPo.setFundSrcId(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getFundSrcId());
								}
								else {
									fundingPo.setFundSrcId("");
								}
								
								if (vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getFundSrcSubCatId() != null) {
									fundingPo.setFundSrcSubCatId(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getFundSrcSubCatId());
								}
								else {
									fundingPo.setFundSrcSubCatId("");
								}

								if (vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getStartDateStr() != null &&
										!"".equals(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getStartDateStr())) {
									fundingPo.setFundSrcStartDate(sdf.parse(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getStartDateStr()));
								}
								else {
									fundingPo.setFundSrcStartDate(null);
								}
								
								if (vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getEndDateStr() != null &&
										!"".equals(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getEndDateStr())) {
									fundingPo.setFundSrcEndDate(sdf.parse(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getEndDateStr()));
								}
								else {
									fundingPo.setFundSrcEndDate(null);
								}
								
								if (vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getFundSrcFte() != null) {
									fundingPo.setFundSrcFte(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getFundSrcFte());
								}
								else {
									fundingPo.setFundSrcFte(null);
								}
								
								if (vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getFundSrcRemark() != null) {
									fundingPo.setFundSrcRemark(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getFundSrcRemark());
								}
								else {
									fundingPo.setFundSrcRemark("");
								}
								
								if (vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getInst() != null) {
									fundingPo.setInst(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getInst());
								}
								else {
									fundingPo.setInst("");
								}
								
								if (vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getSection() != null) {
									fundingPo.setSection(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getSection());
								}
								else {
									fundingPo.setSection("");
								}
								
								if (vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getAnalytical() != null) {
									fundingPo.setAnalytical(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getAnalytical());
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
					if (rp.getRequestFundingList().size() > vo.getRequestPositionList().get(i).getRequestFundingList().size()) {
						for (int v=vo.getRequestPositionList().get(i).getRequestFundingList().size(); v<rp.getRequestFundingList().size(); v++) {
							rp.getRequestFundingList().get(v).setRecState(MPRSConstant.MPRS_STATE_INACTIVE);
						}
					}
					
					if (newRequest.getRequestPositionList().size() > i) {
						newRequest.getRequestPositionList().set(i, rp);
					}
					else {
						newRequest.addRequestPosition(rp);
					}
				}
	
				vo.setRequestId(newRequest.getRequestId());
				vo.setRequestStatus(MPRSConstant.REQUEST_STATUS_NEW);
				
				// Perform update of attachment - Start 
				List<RequestAttachmentPo> attachmentList = new ArrayList<RequestAttachmentPo>();
				if (vo.getUploadFileId() != null && !"".equals(vo.getUploadFileId())) {
					for (int i=0; i<vo.getUploadFileId().size(); i++) {
						if (vo.getUploadFileId().get(i) != null && !"".equals(vo.getUploadFileId().get(i))) {
							RequestTempFilePo tempFile = requestSvc.getTempFile(Integer.parseInt(vo.getUploadFileId().get(i)));
	
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
				// Perform update of attachment - End
				
				int requestNo = -1;
				if (vo.getRequestNo() != null && !"".equals(vo.getRequestNo())) {
					requestSvc.update(newRequest, vo.getRemoveAttachmentUid(), vo.getUploadFileId(), user);
					requestNo = newRequest.getRequestNo();
				}
				else {
					requestNo = requestSvc.insert(newRequest, vo.getUploadFileId(), true, null, null, user);
				}
	
				// Retrieve again the request 
				RequestPo requestCase = requestSvc.getRequestByRequestNo(requestNo);
				
				vo = convertToVo(requestCase, userId, currentRole);
				vo.setDisplayMessage("Request update successful.");
				vo.setUpdateSuccess("Y");
			}
			catch (Exception ex) {
				EClaimLogger.error("performUpdate - update:" + ex.getMessage(), ex);
				String errorMsg = doHandleException(ex.getCause(), ex.getMessage());
				
				if (vo.getRequestNo() != null && !"".equals(vo.getRequestNo())) {
					RequestPo requestCase = requestSvc.getRequestByRequestNo(Integer.parseInt(vo.getRequestNo()));
					vo = convertToVo(requestCase, userId, currentRole);
				}
				
				System.out.println("errorMsg: " + errorMsg);
				vo.setUpdateSuccess("N");
				vo.setDisplayMessage(errorMsg);
			}
		}
		else if ("WITHDRAW".equalsIgnoreCase(formAction)) {
			System.out.println("Request No.: " + vo.getRequestNo());
			int requestNo = Integer.parseInt(vo.getRequestNo());
			String errorMsg = requestSvc.submitWithdraw(requestNo, MPRSConstant.REQUEST_TYPE_CHANGE_FUNDING, userId, currentRole);

			if ("".equals(errorMsg) || errorMsg == null) {
				// Retrieve again the request 
				RequestPo requestCase = requestSvc.getRequestByRequestNo(requestNo);
				vo = convertToVo(requestCase, userId, currentRole);
				vo.setCurrentLoginUser(userId);
				vo.setUserName(user.getUserName());
				vo.setUpdateSuccess("Y");
				vo.setDisplayMessage("Request withdraw successful.");
			}
			else {
				System.out.println("errorMsg: " + errorMsg);
				vo.setUpdateSuccess("N");
				vo.setDisplayMessage(errorMsg);
			}
			
			return new ModelAndView("request/changeOfFunding", "formBean", vo);
		}
		else if ("SUBMIT".equalsIgnoreCase(formAction)) {
			// Perform Save before submit
			RequestPo newRequest = null;	

			if (vo.getRequestNo() != null && !"".equals(vo.getRequestNo())) {
				newRequest = requestSvc.getRequestByRequestNo(Integer.parseInt(vo.getRequestNo()));
			}
			else {
				newRequest = new RequestPo();	
				PostMasterPo mprsPost = requestSvc.getPostByPostUid(Integer.parseInt(vo.getRequestPostNo().get(0)));
				String tmpRequestId = requestSvc.generateRequestNo(mprsPost.getClusterCode(), mprsPost.getInstCode(), "CF", scurrentYear);
				System.out.println("tmpRequestId: " + tmpRequestId);

				newRequest.setRequestId(tmpRequestId);
				RequestTypePo requestType = new RequestTypePo();
				requestType.setRqTypeCode(MPRSConstant.REQUEST_TYPE_CHANGE_FUNDING);
				newRequest.setRequestType(requestType);
				
				RequestStatusPo requestStatus = new RequestStatusPo();
				requestStatus.setStatusCode(MPRSConstant.REQUEST_STATUS_NEW);
				newRequest.setRequestStatus(requestStatus);
				newRequest.setRequestDate(new Date());
			}

			newRequest.setCurrentWFUser(userId);
			newRequest.setCurrentWFGroup(currentRole);
			
			newRequest.setRequestReason(vo.getRequestReason());
			newRequest.setRequester(vo.getRequester());

			// Added for UT29939
			newRequest.setEffectFrom(sdf.parse(vo.getEffectiveDate()));
			
			newRequest.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);

			if (!"".equals(vo.getApprovalDate())) {
				newRequest.setApprovalDate(sdf.parse(vo.getApprovalDate()));
			}
			newRequest.setApprovalReference(vo.getApprovalReference());
			newRequest.setApprovalRemark(vo.getApprovalRemark());

			for (int i=0; i<vo.getRequestPostNo().size(); i++) {
				System.out.println("Add Request Position!!");

				RequestPostPo rp = null;
				if (newRequest.getRequestPositionList().size() > i) {
					rp = newRequest.getRequestPositionList().get(i);
				}
				else {
					rp = new RequestPostPo();
				}
				PostMasterPo mprsPost = requestSvc.getPostByPostUid(Integer.parseInt(vo.getRequestPostNo().get(i)));
				rp.setPostNo(vo.getRequestPostNo().get(i));
				RankPo rank = new RankPo();
				rank.setRankCode(mprsPost.getRankCode());
				rp.setRank(rank);
				rp.setPostId(mprsPost.getPostId());
				rp.setClusterCode(mprsPost.getClusterCode());
				rp.setInstCode(mprsPost.getInstCode());
				rp.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
				rp.setPostDuration(mprsPost.getPostDuration());
				rp.setPostFTE(String.valueOf(mprsPost.getPostFTE()));
				rp.setStaffGroupCode(mprsPost.getStaffGroupCode());
				rp.setPostFTEType(mprsPost.getPostFTEType());
				
				// Generate Post ID
				List<String> annualPlanList = new ArrayList<String>();
				List<String> programTypeList = new ArrayList<String>();
				
				if (vo.getRequestPositionList() != null) {
					if (vo.getRequestPositionList().get(i).getRequestFundingList() != null) {
						for (int j=0; j<vo.getRequestPositionList().get(i).getRequestFundingList().size(); j++) {
							annualPlanList.add(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getAnnualPlanInd());
							programTypeList.add(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getProgramTypeCode());
						}
					}
				}
				
				String postId = requestSvc.generateNewPostId(mprsPost.getStaffGroupCode(),
															mprsPost.getClusterCode(),
															mprsPost.getInstCode(), 
															mprsPost.getDeptCode(),
															mprsPost.getRankCode(),
															annualPlanList,
															programTypeList,
															mprsPost.getPostDuration(),
															mprsPost.getPostFTEType(),
															false,
															mprsPost.getPostId());
				
				System.out.println("new Post ID: " + postId);
				rp.setPostId(postId);
				
				//funding info
				if (vo.getRequestPositionList() != null) {
					if (vo.getRequestPositionList().get(i).getRequestFundingList() != null) {
						for (int j=0; j<vo.getRequestPositionList().get(i).getRequestFundingList().size(); j++) {
							RequestFundingPo fundingPo = null;
							if (j < rp.getRequestFundingList().size()) {
								fundingPo = rp.getRequestFundingList().get(j);
							}
							else {
								fundingPo = new RequestFundingPo();
							}
							
							fundingPo.setFundingSeqNo(j+1);
							if (vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getAnnualPlanInd() != null) {
								fundingPo.setAnnualPlanInd(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getAnnualPlanInd());
							}
							else {
								fundingPo.setAnnualPlanInd("");
							}
							
							if (vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getProgramYear() != null) {
								fundingPo.setProgramYear(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getProgramYear());
							}
							else {
								fundingPo.setProgramYear("");
							}
							
							if (vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getProgramCode() != null) {
								fundingPo.setProgramCode(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getProgramCode());
							}
							else {
								fundingPo.setProgramCode("");
							}
							
							if (vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getProgramName() != null) {
								fundingPo.setProgramName(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getProgramName());
							}
							else {
								fundingPo.setProgramName("");
							}
							
							if (vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getProgramTypeCode() != null) {
								fundingPo.setProgramTypeCode(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getProgramTypeCode());
							}
							else {
								fundingPo.setProgramTypeCode("");
							}
							
							if (vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getFundSrcId() != null) {
								fundingPo.setFundSrcId(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getFundSrcId());
							}
							else {
								fundingPo.setFundSrcId("");
							}

							if (vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getStartDateStr() != null &&
									!"".equals(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getStartDateStr())) {
								fundingPo.setFundSrcStartDate(sdf.parse(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getStartDateStr()));
							}
							else {
								fundingPo.setFundSrcStartDate(null);
							}
							
							if (vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getEndDateStr() != null &&
									!"".equals(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getEndDateStr())) {
								fundingPo.setFundSrcEndDate(sdf.parse(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getEndDateStr()));
							}
							else {
								fundingPo.setFundSrcEndDate(null);
							}
							
							if (vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getFundSrcFte() != null) {
								fundingPo.setFundSrcFte(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getFundSrcFte());
							}
							else {
								fundingPo.setFundSrcFte(null);
							}
							
							if (vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getFundSrcRemark() != null) {
								fundingPo.setFundSrcRemark(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getFundSrcRemark());
							}
							else {
								fundingPo.setFundSrcRemark("");
							}
							
							if (vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getInst() != null) {
								fundingPo.setInst(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getInst());
							}
							else {
								fundingPo.setInst("");
							}
							
							if (vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getSection() != null) {
								fundingPo.setSection(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getSection());
							}
							else {
								fundingPo.setSection("");
							}
							
							if (vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getAnalytical() != null) {
								fundingPo.setAnalytical(vo.getRequestPositionList().get(i).getRequestFundingList().get(j).getAnalytical());
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
				if (rp.getRequestFundingList().size() > vo.getRequestPositionList().get(i).getRequestFundingList().size()) {
					for (int v=vo.getRequestPositionList().get(i).getRequestFundingList().size(); v<rp.getRequestFundingList().size(); v++) {
						rp.getRequestFundingList().get(v).setRecState(MPRSConstant.MPRS_STATE_INACTIVE);
					}
				}

				if (newRequest.getRequestPositionList().size() > i) {
					newRequest.getRequestPositionList().set(i, rp);
				}
				else {
					newRequest.addRequestPosition(rp);
				}
			}

			vo.setRequestId(newRequest.getRequestId());
			vo.setRequestStatus(MPRSConstant.REQUEST_STATUS_NEW);
			
			// Perform update of attachment - Start 
			List<RequestAttachmentPo> attachmentList = new ArrayList<RequestAttachmentPo>();
			if (vo.getUploadFileId() != null && !"".equals(vo.getUploadFileId())) {
				for (int i=0; i<vo.getUploadFileId().size(); i++) {
					if (vo.getUploadFileId().get(i) != null && !"".equals(vo.getUploadFileId().get(i))) {
						RequestTempFilePo tempFile = requestSvc.getTempFile(Integer.parseInt(vo.getUploadFileId().get(i)));

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
			// Perform update of attachment - End
			
			requestSvc.update(newRequest, vo.getRemoveAttachmentUid(), vo.getUploadFileId(), user);
			int requestNo = newRequest.getRequestNo();
			
			String emailTo = "";
			String emailCC = "";
			String emailTitle = "";
			String emailContent = "";
			String generateEmail = "N";
			
			if ("Y".equals(vo.getSubmitWithModifiedEmail())) {
				emailTo = vo.getEmailTo();
				emailCC = vo.getEmailCC();
				emailTitle = vo.getEmailTitle();
				String si = StrHelper.format(vo.getEmailSuppInfo().replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$"));
				emailContent = vo.getEmailContent().replaceAll("<Supplementary Information / Comment>", si);
				String url = appUrl + "/request/changeOfFunding?rq=" + vo.getRequestNo() + "&fromMail=Y";
				emailContent = emailContent.replaceAll("Please click here to review the request", "Please click <a href='" + url + "'>here</a> to review the request");
				generateEmail = "Y";
			}
			
			if ("Y".equals(vo.getSubmitWithEmail())) {
				RequestPo tmpCase = requestSvc.getRequestByRequestNo(requestNo);
				String postId = "";
				for (int i=0; i<tmpCase.getRequestPositionList().size(); i++) {
					if (i != 0) 
						postId += ", ";
					
					postId += tmpCase.getRequestPositionList().get(i).getPostId();
				}
				
				String submittedBy = user.getUserName();
				String submittedDate = DateTimeHelper.formatDateToString(new Date());
				String suppInfo = StrHelper.format(vo.getEmailSuppInfo());
				
				String receiver = "";
				if (!"".equals(vo.getNextWFUser())) {
					UserPo requester = securitySvc.findUser(vo.getNextWFUser());
					receiver = requester.getUserName();
				}
				else {
					List<RolePo> userGroupList = securitySvc.getAllRole();
					for (int i=0; i<userGroupList.size(); i++) {
						if (vo.getNextWFGroup().equals(userGroupList.get(i).getRoleId())) {
							receiver = userGroupList.get(i).getRoleName();
							break;
						}
					}
				}
				
				EmailTemplatePo emailTemplate = commonSvc.getTemplateByTemplateId("RQ_CHG_FUND_SUBMIT", new String[] {String.valueOf(vo.getRequestId()),
																														postId, 
																														submittedBy, 
																														submittedDate, 
																														suppInfo,																											
																														"", 
																														"", 
																														"", 
																														receiver																											
																														});
				
				// Set email List
				String emailList = "";
				if (!"".equals(vo.getNextWFUser()) && vo.getNextWFUser() != null) {
					try {
						UserPo tUser = securitySvc.findUser( vo.getNextWFUser());
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
						List<UserPo> userList = securitySvc.getAllUserByGroupAndCluster(clusterCode, instCode, vo.getNextWFGroup());
						
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
				
				if ("Y".equals(vo.getReturnCase())) { 
					List<UserPo> ccUserList = securitySvc.getReturnUserFromWorkflowHistoryAll(vo.getRequestNo(), vo.getNextWFGroup());
					for (int i=0; i<ccUserList.size(); i++) {
						if (ccUserList.get(i).getEmail() != null) {
							if (!"".equals(ccEmailList)) 
								ccEmailList += ", ";
							
							ccEmailList += ccUserList.get(i).getEmail();
						}
					}
				}
				
				// If submit to MG team, cc to all related party
				else if ("HO_MG_OFFICER".equals(vo.getNextWFGroup())) {
					List<UserPo> ccUserList = securitySvc.getRelatedUserFromWorkflowHistoryAll(vo.getRequestNo()) ;
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
				String url = appUrl + "/request/changeOfFunding?rq=" + vo.getRequestNo() + "&fromMail=Y";
				emailContent = emailContent.replaceAll("Please click here to review the request", "Please click <a href='" + url + "'>here</a> to review the request");

				generateEmail = "Y";
			}
			
			String errorMsg = requestSvc.submitWorkflow(requestNo, MPRSConstant.REQUEST_TYPE_CHANGE_FUNDING,
					                                    vo.getNextWFGroup(), 
					                                    vo.getNextWFUser(), 
					                                    vo.getReturnCase(),
					                                    generateEmail,
						                                emailTo,
						                                emailCC,
						                                emailTitle,
						                                emailContent,
					                                    userId, currentRole);

			if ("".equals(errorMsg) || errorMsg == null) {
				// Send the email
				commonSvc.performSendEmail(requestNo);
				
				// Retrieve again the request 
				RequestPo requestCase = requestSvc.getRequestByRequestNo(requestNo);

				vo = convertToVo(requestCase, userId, currentRole);
				vo.setUpdateSuccess("Y");
				vo.setDisplayMessage("Request submit successful.");
			}
			else {
				System.out.println("errorMsg: " + errorMsg);
				vo.setUpdateSuccess("N");
				vo.setDisplayMessage(errorMsg);
			}
			
			return new ModelAndView("request/changeOfFunding", "formBean", vo);
		}
		else if ("APPROVE".equalsIgnoreCase(formAction)) {
			System.out.println("Request No. submit approve: " + vo.getRequestNo());
			try {
				int requestNo = Integer.parseInt(vo.getRequestNo());
				
				String emailTo = "";
				String emailCC = "";
				String emailTitle = "";
				String emailContent = "";
				String generateEmail = "N";
				
				if ("Y".equals(vo.getSubmitWithModifiedEmail())) {
					emailTo = vo.getEmailTo();
					emailCC = vo.getEmailCC();
					emailTitle = vo.getEmailTitle();
					String si = StrHelper.format(vo.getEmailSuppInfo().replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$"));
					emailContent = vo.getEmailContent().replaceAll("<Supplementary Information / Comment>", si);
					String url = appUrl + "/request/changeOfFunding?rq=" + vo.getRequestNo() + "&fromMail=Y";
					emailContent = emailContent.replaceAll("Please click here to review the request", "Please click <a href='" + url + "'>here</a> to review the request");
					generateEmail = "Y";
				}
				
				if ("Y".equals(vo.getSubmitWithEmail())) {
					RequestPo tmpCase = requestSvc.getRequestByRequestNo(requestNo);
					String postId = "";
					for (int i=0; i<tmpCase.getRequestPositionList().size(); i++) {
						if (i != 0) 
							postId += ", ";
						
						postId += tmpCase.getRequestPositionList().get(i).getPostId();
					}
					
					String submittedBy = user.getUserName();
					String submittedDate = DateTimeHelper.formatDateToString(new Date());
					String suppInfo = StrHelper.format(vo.getEmailSuppInfo());
					
					String receiver = "";
					receiver = "ALL";
					
					EmailTemplatePo emailTemplate = commonSvc.getTemplateByTemplateId("RQ_CHG_FUND_CONFIRM", new String[] {String.valueOf(vo.getRequestId()),
																															postId, 
																															submittedBy, 
																															submittedDate, 
																															suppInfo,																											
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
					
					List<UserPo> ccUserList = securitySvc.getRelatedUserFromWorkflowHistoryAll(vo.getRequestNo()) ;
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
					
					emailTitle = emailTemplate.getTemplateTitle();
					emailContent = emailTemplate.getTemplateContent();
					String url = appUrl + "/request/changeOfFunding?rq=" + vo.getRequestNo() + "&fromMail=Y";
					emailContent = emailContent.replaceAll("Please click here to review the request", "Please click <a href='" + url + "'>here</a> to review the request");
	
					generateEmail = "Y";
				}
				
				emailTo = StrHelper.removeDuplicateEmail(emailTo);
				emailCC = StrHelper.removeDuplicateEmail(emailCC);
				
				String errorMsg = requestSvc.submitApprove(requestNo, MPRSConstant.REQUEST_TYPE_CHANGE_FUNDING,
														   generateEmail,
						                                   emailTo,
						                                   emailCC,
						                                   emailTitle,
						                                   emailContent,
						                                   userId, currentRole);
	
				if ("".equals(errorMsg) || errorMsg == null) {
					// Send the email
					commonSvc.performSendEmail(requestNo);
					
					// Retrieve again the request 
					RequestPo requestCase = requestSvc.getRequestByRequestNo(requestNo);
					vo = convertToVo(requestCase, userId, currentRole);
					
					vo.setUpdateSuccess("Y");
					vo.setDisplayMessage("Request approve successful.");
				}
				else {
					System.out.println("errorMsg: " + errorMsg);
					vo.setUpdateSuccess("N");
					vo.setDisplayMessage(errorMsg);
				}
			}
			catch (Exception ex) {
				EClaimLogger.error("performUpdate - send Email:" + ex.getMessage(), ex);
				String errorMsg = doHandleException(ex.getCause(), ex.getMessage());
				
				// Retrieve again the request 
				RequestPo requestCase = requestSvc.getRequestByRequestNo(Integer.parseInt(vo.getRequestNo()));
				vo = convertToVo(requestCase, userId, currentRole);
				
				System.out.println("errorMsg: " + errorMsg);
				vo.setUpdateSuccess("N");
				vo.setDisplayMessage(errorMsg);
			}
			
			return new ModelAndView("request/changeOfFunding", "formBean", vo);
		}
		
		return new ModelAndView("request/changeOfFunding", "formBean", vo);
	}
	
	// Only serve for Change of Funding request
	@RequestMapping(value="/api/request/changeOfFunding/searchPost", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody PostResponseWrapper searchPost(@ModelAttribute MPRSSearchCriteria mprSearchRequest, 
			HttpServletRequest request) {
		System.out.println("Location ChangeOfFundingController.searchPost");
		String userId = this.getSessionUser(request).getUserId();
		String currentRole = (String)request.getSession().getAttribute("currentRole");
		int userRoleId = (int) request.getSession().getAttribute("currentUserRoleId");
		List<DataAccessPo> dataAccessList = securitySvc.getDataAccessByRoleId(userRoleId);
		System.out.println("userRoleId: " + userRoleId);
		List<PostMasterPo> posts = requestSvc.getMPRSPost(dataAccessList, mprSearchRequest, userId, currentRole);
		PostResponseWrapper response = new PostResponseWrapper();
		List<PostResponse> postResponseList = new ArrayList<PostResponse>();
		if (posts != null && posts.size() > 0) {
			PostMasterPo tmpPost = null;
			List<PostDurationPo> postDurationList = commonSvc.getAllPostDuration();
			List<RankPo> rankList = commonSvc.getAllRank();
			System.out.println("posts.size(): " + posts.size());
			for (int i = 0; i < posts.size(); i++) {
				String postDurationDesc = "";
				String rankDesc = "";
				tmpPost = posts.get(i);
				
				for (int x=0; x<postDurationList.size(); x++) {
					if (postDurationList.get(x).getPostDurationCode().equals(tmpPost.getPostDuration())) {
						postDurationDesc = postDurationList.get(x).getPostDurationDesc(); 
						break;
					}
				}
				
				for (int x=0; x<rankList.size(); x++) {
					if (rankList.get(x).getRankCode().equals(tmpPost.getRankCode())) {
						rankDesc = rankList.get(x).getRankName();
						break;
					}
				}
				
				PostResponse postResponse = new PostResponse();
				postResponse.setPostUid(tmpPost.getPostUid());
				postResponse.setPostId(tmpPost.getPostId());
				postResponse.setClusterCode(tmpPost.getClusterCode());
				postResponse.setInstCode(tmpPost.getInstCode());
				postResponse.setRankCode(tmpPost.getRankCode());
				postResponse.setRankDesc(rankDesc);
				postResponse.setDeptCode(tmpPost.getDeptCode());
				
				postResponse.setPostTitle((tmpPost.getPostTitle() == null) ? "" : tmpPost.getPostTitle());
				postResponse.setPostDuration((tmpPost.getPostDuration() == null) ? "" : tmpPost.getPostDuration());
				postResponse.setPostDurationDesc(postDurationDesc);
				postResponse.setPostFte(tmpPost.getPostFTE());
				postResponse.setPostStartDateStr((tmpPost.getPostStartDate() == null) ? "" : DateTimeHelper.formatDateToString(tmpPost.getPostStartDate()));
				postResponseList.add(postResponse);
			}
		}
		
		response.setPostResponse(postResponseList);
		response.setError("0");
		response.setErrorMsg("");
		System.out.println("Finish the Ajax Loading");

		return response;
	}
	
	private ChangeOfFundingWebVo convertToVo(RequestPo requestCase, String currentUser, String currentRole) {
		ChangeOfFundingWebVo vo = new ChangeOfFundingWebVo();

		vo.setRequestNo(String.valueOf(requestCase.getRequestNo()));
		vo.setRequestId(requestCase.getRequestId());
		vo.setRequestType(MPRSConstant.REQUEST_TYPE_CHANGE_FUNDING);
		String clusterCode = vo.getRequestId().substring(0, vo.getRequestId().indexOf("-")).trim();
		vo.setRequestStatus(requestCase.getRequestStatus().getStatusCode());
		vo.setCurrentWFGroup(requestCase.getCurrentWFGroup());
		vo.setCurrentWFUser(requestCase.getCurrentWFUser());
		vo.setRequestReason(requestCase.getRequestReason());
		vo.setRequester(requestCase.getRequester());
		
		// Added for UT29939
		vo.setEffectiveDate(DateTimeHelper.formatDateToString(requestCase.getEffectFrom()));
		
		if (requestCase.getApprovalDate() != null) {
			vo.setApprovalDate(DateTimeHelper.formatDateToString(requestCase.getApprovalDate()));
		}
		vo.setApprovalReference(requestCase.getApprovalReference());
		vo.setApprovalRemark(requestCase.getApprovalRemark());
		
		List<RequestPostPo> positionList = requestCase.getRequestPositionList();
		List<RequestPostPo> displayList = new ArrayList<RequestPostPo>();
		for (int i=0; i<positionList.size(); i++) {
			if (MPRSConstant.MPRS_STATE_ACTIVE.equals(positionList.get(i).getRecState())) {
				positionList.get(i).setPostFTE(StrHelper.formatDecimal(positionList.get(i).getPostFTE()));
				
				// Look up the annual plan ind
				List<RequestFundingPo> fundingList = positionList.get(i).getRequestFundingList();
				String annualPlanInd = "";
				for (int k=0; k<fundingList.size(); k++) {
					if (k!=0) {
						annualPlanInd += "/";
					}
					
					if (fundingList.get(k).getAnnualPlanInd() == null) {
						annualPlanInd += "";
					}
					else if ("Y".equals(fundingList.get(k).getAnnualPlanInd())) {
						annualPlanInd += "Yes";
					}
					else {
						annualPlanInd += "No";
					}
				}
				
				System.out.println("annualPlanInd : " + annualPlanInd);
				positionList.get(i).setAnnualPlanInd(annualPlanInd);
				
				// Get the MPRS Post
				PostMasterPo mprsPost = requestSvc.getPostByPostUid(Integer.parseInt(positionList.get(i).getPostNo())); 
				positionList.get(i).setOriginalEndDate(mprsPost.getLimitDurationEndDate());
				positionList.get(i).setPostFTE(StrHelper.formatDecimal(positionList.get(i).getPostFTE()));
				
				// Set the original funding information
				for (int j=0; j<mprsPost.getPostFundingList().size(); j++) {
					if (positionList.get(i).getRequestFundingList().size() <= mprsPost.getPostFundingList().size()) {
						positionList.get(i).getRequestFundingList().get(j).setOriAnnualPlanInd(mprsPost.getPostFundingList().get(j).getAnnualPlanInd()==null?"":mprsPost.getPostFundingList().get(j).getAnnualPlanInd());
						positionList.get(i).getRequestFundingList().get(j).setOriProgramYear(mprsPost.getPostFundingList().get(j).getProgramYear()==null?"":mprsPost.getPostFundingList().get(j).getProgramYear());
						positionList.get(i).getRequestFundingList().get(j).setOriProgramCode(mprsPost.getPostFundingList().get(j).getProgramCode()==null?"":mprsPost.getPostFundingList().get(j).getProgramCode());
						positionList.get(i).getRequestFundingList().get(j).setOriProgramName(mprsPost.getPostFundingList().get(j).getProgramName()==null?"":mprsPost.getPostFundingList().get(j).getProgramName());
						positionList.get(i).getRequestFundingList().get(j).setOriProgramTypeCode(mprsPost.getPostFundingList().get(j).getProgramTypeCode()==null?"":mprsPost.getPostFundingList().get(j).getProgramTypeCode());
						positionList.get(i).getRequestFundingList().get(j).setOriFundSrcId(mprsPost.getPostFundingList().get(j).getFundSrcId()==null?"":mprsPost.getPostFundingList().get(j).getFundSrcId());
						positionList.get(i).getRequestFundingList().get(j).setOriFundSrcSubCatId(mprsPost.getPostFundingList().get(j).getFundSrcSubCatId()==null?"":mprsPost.getPostFundingList().get(j).getFundSrcSubCatId());
						positionList.get(i).getRequestFundingList().get(j).setOriStartDateStr(mprsPost.getPostFundingList().get(j).getFundSrcStartDate()==null?"":DateTimeHelper.formatDateToString(mprsPost.getPostFundingList().get(j).getFundSrcStartDate()));
						positionList.get(i).getRequestFundingList().get(j).setOriEndDateStr(mprsPost.getPostFundingList().get(j).getFundSrcEndDate()==null?"":DateTimeHelper.formatDateToString(mprsPost.getPostFundingList().get(j).getFundSrcEndDate()));
						positionList.get(i).getRequestFundingList().get(j).setOriFundSrcFte(mprsPost.getPostFundingList().get(j).getFundSrcFte());
						positionList.get(i).getRequestFundingList().get(j).setOriFundSrcRemark(mprsPost.getPostFundingList().get(j).getFundSrcRemark()==null?"":mprsPost.getPostFundingList().get(j).getFundSrcRemark());
						positionList.get(i).getRequestFundingList().get(j).setOriInst(mprsPost.getPostFundingList().get(j).getInst()==null?"":mprsPost.getPostFundingList().get(j).getInst());
						positionList.get(i).getRequestFundingList().get(j).setOriSection(mprsPost.getPostFundingList().get(j).getSection()==null?"":mprsPost.getPostFundingList().get(j).getSection());
						positionList.get(i).getRequestFundingList().get(j).setOriAnalytical(mprsPost.getPostFundingList().get(j).getAnalytical()==null?"":mprsPost.getPostFundingList().get(j).getAnalytical());
					}
				}
				
				
				displayList.add(positionList.get(i));
			}
		}
		
		vo.setRequestPositionList(displayList);
		
		String staffGroupCode = "";
		for (int v=0; v<vo.getRequestPositionList().size(); v++) {
			System.out.println(vo.getRequestPositionList().get(v).getStaffGroupCode());
			staffGroupCode = vo.getRequestPositionList().get(v).getStaffGroupCode();
		}
		
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
												MPRSConstant.REQUEST_TYPE_CHANGE_FUNDING, 
												vo.getRequestStatus(), currentRole, "")) {
					vo.setUserHaveApprovalRight("Y");
				}
				else {
					vo.setUserHaveApprovalRight("N");
				}
				
				if ("FIN_OFFICER".equals(currentRole)) {
					vo.setUserHaveWithdrawRight("Y");
					vo.setUserHaveSaveRight("Y");
					vo.setCanEditFinancialInfo("Y");
				}
				else {
					vo.setCanEditFinancialInfo("N");
				}

				vo.setUserHaveSaveRight("Y");
				vo.setUserHaveSubmitRight("Y");
			}
			else {
				vo.setUserHaveApprovalRight("N");
				vo.setUserHaveSaveRight("N");
				vo.setUserHaveSubmitRight("N");
			}
		}
		
		if ("FIN_OFFICER".equals(currentRole)) {
			vo.setUserHaveCreationRight("Y");
		}
		else {
			vo.setUserHaveWithdrawRight("N");
			vo.setUserHaveSaveRight("N");
			vo.setUserHaveCreationRight("N");
		}
		
		// Set the button of workflow button
		System.out.println("Cluster Code:" + clusterCode + "/" +MPRSConstant.REQUEST_TYPE_CHANGE_FUNDING + "/" +vo.getRequestStatus());
		RequestWorkflowRoutePo route = this.requestSvc.getDefaultRoute(staffGroupCode, MPRSConstant.REQUEST_TYPE_CHANGE_FUNDING, vo.getRequestStatus(), "");
		if (route != null)
			vo.setSubmitButtonLabel(route.getActionName());
				
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
	
	/****  Dropdown  *****/
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
	
	@ModelAttribute("programTypeList")
	public Map<String, String> getProgramTypeList() {
		Map<String, String> displayProgramTypeList = new LinkedHashMap<String, String>();
		List<ProgramTypePo> programTypeList = commonSvc.getAllProgramType();
		for (int i=0; i<programTypeList.size(); i++) {
			displayProgramTypeList.put(programTypeList.get(i).getProgramTypeCode(), programTypeList.get(i).getProgramTypeName());
		}
		
		return displayProgramTypeList;
	}
}

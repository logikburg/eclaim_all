package hk.org.ha.eclaim.controller.request;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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
import hk.org.ha.eclaim.bs.request.po.RequestPo;
import hk.org.ha.eclaim.bs.request.po.RequestPostPo;
import hk.org.ha.eclaim.bs.request.po.RequestTempFilePo;
import hk.org.ha.eclaim.bs.request.po.RequestWorkflowRoutePo;
import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.model.request.ChangeHCMPostWebVo;
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
import hk.org.ha.eclaim.bs.hcm.po.HCMRecordPo;
import hk.org.ha.eclaim.bs.hcm.svc.IHCMSvc;
import hk.org.ha.eclaim.bs.security.po.DataAccessPo;
import hk.org.ha.eclaim.bs.security.po.RolePo;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.core.helper.DateTimeHelper;
import hk.org.ha.eclaim.core.helper.StrHelper;
import hk.org.ha.eclaim.core.logger.EClaimLogger;

@Controller
public class ChangeHCMPostController extends CommonRequestController {

	@Value("${mail.appurl.prefix}")
	private String appUrl;
	
	@Autowired
	private IHCMSvc hcmSvc;

	@RequestMapping(value="/request/changeHCMPost", method=RequestMethod.GET)
	public ModelAndView viewChangeHCMPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userId = this.getSessionUser(request).getUserId();
		UserPo user = securitySvc.findUser(userId);
		String currentRole = (String)request.getSession().getAttribute("currentRole");
		user.setCurrentRole((String)request.getSession().getAttribute("currentRole"));
		
		if (request.getParameter("rq") != null) {
			EClaimLogger.info("request uid: " + request.getParameter("rq"));
			
			// Check whether the user have access right for request uid
			int userRoleId = (int) request.getSession().getAttribute("currentUserRoleId");
			List<DataAccessPo> dataAccessList = securitySvc.getDataAccessByRoleId(userRoleId);
			RequestPo requestCase = requestSvc.getRequestByRequestNo(dataAccessList, Integer.parseInt((String)request.getParameter("rq")));
			if (requestCase != null) {
				EClaimLogger.info("requestCase.getRequestId(): " + requestCase.getRequestId());
				ChangeHCMPostWebVo vo = convertToVo(requestCase, userId, currentRole);
				return new ModelAndView("request/changeHCMPost", "formBean", vo);
			}
			else {
				return new ModelAndView("redirect:/home/home");
			}
		}
		else {
			System.out.println("request uid is null");
			ChangeHCMPostWebVo vo = new ChangeHCMPostWebVo();
			
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
			
			return new ModelAndView("request/changeHCMPost", "formBean", vo);
		}
	}

	@RequestMapping(value="/request/changeHCMPost", method=RequestMethod.POST)
	public ModelAndView performUpdate(@ModelAttribute("formBean")ChangeHCMPostWebVo vo, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String formAction = vo.getFormAction();
		String userId = this.getSessionUser(request).getUserId();
		UserPo user = securitySvc.findUser(userId);
		String currentRole = (String)request.getSession().getAttribute("currentRole");
        user.setCurrentRole(currentRole);
		
		Calendar now = Calendar.getInstance();
		String scurrentYear;
		scurrentYear = String.valueOf(now.get(Calendar.YEAR));	
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

					return new ModelAndView("request/changeHCMPost", "formBean", vo);
				}
			}
		}

		if ("SAVE".equalsIgnoreCase(formAction)) {
			try {
				RequestPo newRequest = null;	
	
				if (vo.getRequestNo() != null && !"".equals(vo.getRequestNo())) {
					newRequest = requestSvc.getRequestByRequestNo(Integer.parseInt(vo.getRequestNo()));
					
					newRequest.setRequestReason(vo.getRequestReason());
					newRequest.setCurrentWFUser(userId);
					newRequest.setCurrentWFGroup(currentRole);
					newRequest.setRequester(vo.getRequester());
	
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
						// rp.setRankId(mprsPost.getRankId());
						rp.setPostId(mprsPost.getPostId());
						rp.setClusterCode(mprsPost.getClusterCode());
						rp.setInstCode(mprsPost.getInstCode());
						rp.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
						rp.setPostDuration(mprsPost.getPostDuration());
						rp.setPostFTE(String.valueOf(mprsPost.getPostFTE()));
						rp.setOriHcmPositionId(mprsPost.getHcmPositionId());
						rp.setHcmPositionId(vo.getRequestHcmPositionId().size()!=0?vo.getRequestHcmPositionId().get(i):"");
						rp.setStaffGroupCode(mprsPost.getStaffGroupCode());
						
						if (newRequest.getRequestPositionList().size() > i) {
							newRequest.getRequestPositionList().set(i, rp);
						}
						else {
							newRequest.addRequestPosition(rp);
						}
					}
					
					for (int i=vo.getRequestPostNo().size(); i<newRequest.getRequestPositionList().size(); i++) {
						System.out.println("Perform set inactive. " + newRequest.getRequestPositionList().get(i).getRequestPostId());
						newRequest.getRequestPositionList().get(i).setRecState(MPRSConstant.MPRS_STATE_INACTIVE);
					}
				}
				else {
					newRequest = new RequestPo();	
					PostMasterPo tmpPost = requestSvc.getPostByPostUid(Integer.parseInt(vo.getRequestPostNo().get(0)));
					String tmpRequestId = requestSvc.generateRequestNo(tmpPost.getClusterCode(), tmpPost.getInstCode(), "HCM", scurrentYear);
					System.out.println("tmpRequestId: " + tmpRequestId);
	
					newRequest.setRequestId(tmpRequestId);
					RequestTypePo requestType = new RequestTypePo();
					requestType.setRqTypeCode(MPRSConstant.REQUEST_TYPE_CHANGE_HCM_POST);
					newRequest.setRequestType(requestType);
					
					RequestStatusPo requestStatus = new RequestStatusPo();
					requestStatus.setStatusCode(MPRSConstant.REQUEST_STATUS_NEW);
					newRequest.setRequestStatus(requestStatus);
					newRequest.setRequestDate(new Date());
					newRequest.setRequestReason(vo.getRequestReason());
					newRequest.setCurrentWFUser(userId);
					newRequest.setCurrentWFGroup(currentRole);
					newRequest.setRequester(vo.getRequester());
					newRequest.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
					if (!"".equals(vo.getEffectiveDate())) {
						newRequest.setEffectFrom(sdf.parse(vo.getEffectiveDate()));
					}
	
					if (!"".equals(vo.getApprovalDate())) {
						newRequest.setApprovalDate(sdf.parse(vo.getApprovalDate()));
					}
					newRequest.setApprovalReference(vo.getApprovalReference());
					newRequest.setApprovalRemark(vo.getApprovalRemark());
	
					for (int i=0; i<vo.getRequestPostNo().size(); i++) {
						System.out.println("Add Request Position!!");
	
						RequestPostPo rp = new RequestPostPo();
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
						rp.setOriHcmPositionId(mprsPost.getHcmPositionId());
						rp.setHcmPositionId(vo.getRequestHcmPositionId().size()!=0?vo.getRequestHcmPositionId().get(i):"");
						rp.setStaffGroupCode(mprsPost.getStaffGroupCode());
						
						newRequest.addRequestPosition(rp);
					}
	
					vo.setRequestId(newRequest.getRequestId());
					vo.setRequestStatus(MPRSConstant.REQUEST_STATUS_NEW);
				}
	
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
			String errorMsg = requestSvc.submitWithdraw(requestNo, MPRSConstant.REQUEST_TYPE_CHANGE_HCM_POST, userId, currentRole);

			if ("".equals(errorMsg) || errorMsg == null) {
				// Retrieve again the request 
				RequestPo requestCase = requestSvc.getRequestByRequestNo(requestNo);

				vo = convertToVo(requestCase, userId, currentRole);
				vo.setUpdateSuccess("Y");
				vo.setDisplayMessage("Request withdraw successful.");
			}
			else {
				System.out.println("errorMsg: " + errorMsg);
				vo.setUpdateSuccess("N");
				vo.setDisplayMessage(errorMsg);
			}
			
			return new ModelAndView("request/changeHCMPost", "formBean", vo);
		}
		else if ("SUBMIT".equalsIgnoreCase(formAction)) {
			RequestPo newRequest = null;	

			newRequest = requestSvc.getRequestByRequestNo(Integer.parseInt(vo.getRequestNo()));

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
				rp.setOriHcmPositionId(mprsPost.getHcmPositionId());
				rp.setHcmPositionId(vo.getRequestHcmPositionId().size()!=0?vo.getRequestHcmPositionId().get(i):"");
				rp.setStaffGroupCode(mprsPost.getStaffGroupCode());
				
				if (newRequest.getRequestPositionList().size() > i) {
					newRequest.getRequestPositionList().set(i, rp);
				}
				else {
					newRequest.addRequestPosition(rp);
				}
			}
			
			for (int i=vo.getRequestPostNo().size(); i<newRequest.getRequestPositionList().size(); i++) {
				System.out.println("Perform set inactive. " + newRequest.getRequestPositionList().get(i).getRequestPostId());
				newRequest.getRequestPositionList().get(i).setRecState(MPRSConstant.MPRS_STATE_INACTIVE);
			}
			
			List<RequestAttachmentPo> attachmentList = new ArrayList<RequestAttachmentPo>();
			if (vo.getUploadFileId() != null && !"".equals(vo.getUploadFileId())) {
				for (int i=0; i<vo.getUploadFileId().size(); i++) {
					if (vo.getUploadFileId().get(i) != null && !"".equals(vo.getUploadFileId().get(i))) {
						RequestTempFilePo tempFile = requestSvc.getTempFile(Integer.parseInt(vo.getUploadFileId().get(i)));

						RequestAttachmentPo attachment = new RequestAttachmentPo();
						attachment.setFileName(tempFile.getFilename());
						attachmentList.add(attachment);
					}
				}
			}	
			else {
				newRequest.setAttachment(null);
			}

			newRequest.setAttachment(attachmentList);
			
			requestSvc.update(newRequest, vo.getRemoveAttachmentUid(), vo.getUploadFileId(), user);
			int requestNo = newRequest.getRequestNo();

			// Retrieve again the request 
			RequestPo requestCase = requestSvc.getRequestByRequestNo(requestNo);
			System.out.println("Request No.: " + vo.getRequestNo());
			
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
				String url = appUrl + "/request/changeHCMPost?rq=" + vo.getRequestNo() + "&fromMail=Y";
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
				
				EmailTemplatePo emailTemplate = commonSvc.getTemplateByTemplateId("RQ_CHG_HCM_SUBMIT", new String[] {String.valueOf(vo.getRequestId()),
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
				String url = appUrl + "/request/changeHCMPost?rq=" + vo.getRequestNo() + "&fromMail=Y";
				emailContent = emailContent.replaceAll("Please click here to review the request", "Please click <a href='" + url + "'>here</a> to review the request");
				
				generateEmail = "Y";
			}
			
			System.out.println("emailContent: " + emailContent);
			
			String errorMsg = requestSvc.submitWorkflow(requestNo, MPRSConstant.REQUEST_TYPE_CHANGE_HCM_POST,
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
				requestCase = requestSvc.getRequestByRequestNo(requestNo);

				vo = convertToVo(requestCase, userId, currentRole);
				vo.setUpdateSuccess("Y");
				vo.setDisplayMessage("Request submit successful.");
			}
			else {
				System.out.println("errorMsg: " + errorMsg);
				vo.setUpdateSuccess("N");
				vo.setDisplayMessage(errorMsg);
			}
			
//			vo.setUserName(user.getUserName());
//			
//			// Check whether the user have approval right
//			if (requestSvc.hasApprovalRight(vo.getClusterId(), MPRSConstant.REQUEST_TYPE_SUPPLEMENTARY_PROMOTION, vo.getRequestStatus(), currentRole)) {
//				vo.setUserHaveApprovalRight("Y");
//			}
			
			return new ModelAndView("request/changeHCMPost", "formBean", vo);
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
					String url = appUrl + "/request/changeHCMPost?rq=" + vo.getRequestNo() + "&fromMail=Y";
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
					
					String receiver = "ALL";
					
					EmailTemplatePo emailTemplate = commonSvc.getTemplateByTemplateId("RQ_CHG_HCM_CONFIRM", new String[] {String.valueOf(vo.getRequestId()),
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
					String url = appUrl + "/request/changeHCMPost?rq=" + vo.getRequestNo() + "&fromMail=Y";
					emailContent = emailContent.replaceAll("Please click here to review the request", "Please click <a href='" + url + "'>here</a> to review the request");
					
					// Added for ST08746
					String clusterCode = "";
					String instCode = "";
					for (int i=0; i<tmpCase.getRequestPositionList().size(); i++) {
						clusterCode = tmpCase.getRequestPositionList().get(i).getClusterCode();
						instCode = tmpCase.getRequestPositionList().get(i).getInstCode();
						List<UserPo> userList = securitySvc.getAllUserByGroupAndCluster(clusterCode, instCode, "FIN_MANAGER");
							
						for (int j=0; j<userList.size(); j++) {
							if (userList.get(j).getEmail() != null) {
								if (!ccEmail.contains(userList.get(j).getEmail())) {
									if (!"".equals(emailCC)) {
										emailCC += ", ";
									}
									
									emailCC += userList.get(j).getEmail();
								}
							}
						}
					}
					
					generateEmail = "Y";
				}

				emailTo = StrHelper.removeDuplicateEmail(emailTo);
				emailCC = StrHelper.removeDuplicateEmail(emailCC);
				
				String errorMsg = requestSvc.submitApprove(requestNo, MPRSConstant.REQUEST_TYPE_CHANGE_HCM_POST,
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
				EClaimLogger.error("performUpdate:" + ex.getMessage(), ex);
				String errorMsg = doHandleException(ex.getCause(), ex.getMessage());
				
				// Retrieve again the request 
				RequestPo requestCase = requestSvc.getRequestByRequestNo(Integer.parseInt(vo.getRequestNo()));
				vo = convertToVo(requestCase, userId, currentRole);
				
				System.out.println("errorMsg: " + errorMsg);
				vo.setUpdateSuccess("N");
				vo.setDisplayMessage(errorMsg);
			}
			
			return new ModelAndView("request/changeHCMPost", "formBean", vo);
		}
		
		return new ModelAndView("request/changeHCMPost", "formBean", vo);

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
	
	private ChangeHCMPostWebVo convertToVo(RequestPo requestCase, String currentUser, String currentRole) {
		ChangeHCMPostWebVo vo = new ChangeHCMPostWebVo();

		vo.setRequestNo(String.valueOf(requestCase.getRequestNo()));
		vo.setRequestId(requestCase.getRequestId());
		vo.setRequestType(MPRSConstant.REQUEST_TYPE_CHANGE_HCM_POST);
		String clusterCode = vo.getRequestId().substring(0, vo.getRequestId().indexOf("-")).trim();
		vo.setRequestStatus(requestCase.getRequestStatus().getStatusCode());
		vo.setCurrentWFGroup(requestCase.getCurrentWFGroup());
		vo.setCurrentWFUser(requestCase.getCurrentWFUser());
		vo.setRequestReason(requestCase.getRequestReason());
		vo.setRequester(requestCase.getRequester());
		if (requestCase.getApprovalDate() != null) {
			vo.setApprovalDate(DateTimeHelper.formatDateToString(requestCase.getApprovalDate()));
		}
		vo.setApprovalReference(requestCase.getApprovalReference());
		vo.setApprovalRemark(requestCase.getApprovalRemark());
		if (requestCase.getEffectFrom() != null) {
			vo.setEffectiveDate(DateTimeHelper.formatDateToString(requestCase.getEffectFrom()));
		}

		List<RequestPostPo> positionList = requestCase.getRequestPositionList();
		List<RequestPostPo> displayList = new ArrayList<RequestPostPo>();
		
		String staffGroupCode = "";
		
		for (int i=0; i<positionList.size(); i++) {
			if (MPRSConstant.MPRS_STATE_ACTIVE.equals(positionList.get(i).getRecState())) {
				positionList.get(i).setPostFTE(StrHelper.formatDecimal(positionList.get(i).getPostFTE()));
				
				// Look up the original HCM Position Name
				if (!"".equals(positionList.get(i).getOriHcmPositionId())) {
					HCMRecordPo tmp = hcmSvc.getHCMRecordByPositionId(Integer.parseInt(positionList.get(i).getOriHcmPositionId()));
					positionList.get(i).setOriHcmPositionName(tmp.getName());
				}
				
				// Look up the new HCM Position Name
				if (!"".equals(positionList.get(i).getHcmPositionId())) {
					HCMRecordPo tmp = hcmSvc.getHCMRecordByPositionId(Integer.parseInt(positionList.get(i).getHcmPositionId()));
					positionList.get(i).setHcmPositionName(tmp.getName());
				}
				
				staffGroupCode = positionList.get(i).getStaffGroupCode();
				displayList.add(positionList.get(i));
			}
		}
		
		vo.setRequestPositionList(displayList);
		
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
												MPRSConstant.REQUEST_TYPE_CHANGE_HCM_POST, 
												vo.getRequestStatus(), currentRole, "")) {
					vo.setUserHaveApprovalRight("Y");
				}
				else {
					vo.setUserHaveApprovalRight("N");
				}

				vo.setUserHaveSubmitRight("Y");
				
				if ("HR_OFFICER".equals(currentRole)) {
					vo.setUserHaveWithdrawRight("Y");
					vo.setUserHaveSaveRight("Y");
					vo.setCanEditDetailInfo("Y");
				}
				else {
					vo.setCanEditDetailInfo("N");
				}
				
				if ("FIN_OFFICER".equals(currentRole)) {
					vo.setUserHaveSaveRight("Y");
					vo.setCanEditFinancialInfo("Y");
				}
				else {
					vo.setCanEditFinancialInfo("N");
				}
			}
			else {
				vo.setUserHaveApprovalRight("N");
				vo.setUserHaveSaveRight("N");
				vo.setUserHaveSubmitRight("N");
				vo.setCanEditDetailInfo("N");
				vo.setCanEditFinancialInfo("N");
			}
		}
		
		if ("HR_OFFICER".equals(currentRole)) {
			vo.setUserHaveCreationRight("Y");
		}
		else {
			vo.setUserHaveWithdrawRight("N");
			vo.setUserHaveCreationRight("N");
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
		
		// Set the button of workflow button
		System.out.println("Cluster Code:" + clusterCode + "/" +MPRSConstant.REQUEST_TYPE_CHANGE_HCM_POST + "/" +vo.getRequestStatus());
		RequestWorkflowRoutePo route = this.requestSvc.getDefaultRoute(staffGroupCode, MPRSConstant.REQUEST_TYPE_CHANGE_HCM_POST, vo.getRequestStatus(), "");
		if (route != null) {
			vo.setSubmitButtonLabel(route.getActionName());
		}

		return vo;
	}
	
	@RequestMapping(value="/api/request/changeHCMPost/searchPost", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody PostResponseWrapper searchPost(@ModelAttribute MPRSSearchCriteria mprSearchRequest, 
			HttpServletRequest request) {
		String userId = this.getSessionUser(request).getUserId();
		String currentRole = (String)request.getSession().getAttribute("currentRole");
		
		System.out.println("Location changeHCMPostController.searchMPRSPostSuppProm");
		int userRoleId = (int) request.getSession().getAttribute("currentUserRoleId");
		List<DataAccessPo> dataAccessList = securitySvc.getDataAccessByRoleId(userRoleId);
		System.out.println("userRoleId: " + userRoleId);
		
		List<PostMasterPo> posts = requestSvc.getMPRSPostWithoutSupplementPromotion(dataAccessList, mprSearchRequest, userId, currentRole);
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
				
				if (!"".equals(tmpPost.getHcmPositionId())) {
					HCMRecordPo tmp = hcmSvc.getHCMRecordByPositionId(Integer.parseInt(tmpPost.getHcmPositionId()));
					postResponse.setHcmPositionName(tmp.getName());
				}
				
				postResponseList.add(postResponse);
			}
		}
		
		response.setPostResponse(postResponseList);
		response.setError("0");
		response.setErrorMsg("");

		System.out.println("Finish the Ajax Loading");

		return response;
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

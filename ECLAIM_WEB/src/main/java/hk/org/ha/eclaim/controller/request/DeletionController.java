package hk.org.ha.eclaim.controller.request;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import hk.org.ha.eclaim.bs.request.po.RequestPo;
import hk.org.ha.eclaim.bs.request.po.RequestPostPo;
import hk.org.ha.eclaim.bs.request.po.RequestTempFilePo;
import hk.org.ha.eclaim.bs.request.po.RequestWorkflowRoutePo;
import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.model.request.DeletionWebVo;
import hk.org.ha.eclaim.bs.cs.po.EmailTemplatePo;
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
public class DeletionController extends CommonRequestController {
	
	@Value("${mail.appurl.prefix}")
	private String appUrl;

	@RequestMapping(value="/request/deletion", method=RequestMethod.GET)
	public ModelAndView deletion(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("Location DeleteController.deletion()");
		
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
				DeletionWebVo vo = convertWebVo(requestCase, userId, currentRole);
			
				return new ModelAndView("request/deletion", "formBean", vo);
			}
			else {
				return new ModelAndView("redirect:/home/home");
			}
		}
		else {
			System.out.println("request uid is null");
			DeletionWebVo vo = new DeletionWebVo();
			
			if ("HR_OFFICER".equals(currentRole)) {
				vo.setUserHaveCreationRight("Y");
			}
			else {
				vo.setUserHaveCreationRight("N");
			}
			
			return new ModelAndView("request/deletion", "formBean", vo);
		}
	}

	@RequestMapping(value="/request/deletion", method=RequestMethod.POST)
	public ModelAndView performUpdate(@ModelAttribute("formBean")DeletionWebVo deletionWebVo, 
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		System.out.println("approvalFile: " + deletionWebVo.getApprovalFile());
		if (deletionWebVo.getApprovalFile() != null) {
			System.out.println("File name: " + deletionWebVo.getApprovalFile().getOriginalFilename());
			System.out.println("File size: " + deletionWebVo.getApprovalFile().getSize());
		}
		
		String formAction = deletionWebVo.getFormAction();

		// Get the user name from cookie
		String userId = this.getSessionUser(request).getUserId();
		UserPo user = securitySvc.findUser(userId);
        user.setCurrentRole((String)request.getSession().getAttribute("currentRole"));
		String currentRole = (String)request.getSession().getAttribute("currentRole");
		
		// Concurrent control
		if ("SAVE".equalsIgnoreCase(formAction) ||
				"WITHDRAW".equalsIgnoreCase(formAction) ||
				"SUBMIT".equalsIgnoreCase(formAction) ||
				"APPROVE".equalsIgnoreCase(formAction)) {
			if (deletionWebVo.getRequestNo() != null && !"".equals(deletionWebVo.getRequestNo())) {
				RequestPo requestCase = requestSvc.getRequestByRequestNo(Integer.parseInt(deletionWebVo.getRequestNo()));
				
				// Check last update date
				String lastUpdateDate = DateTimeHelper.formatDateTimeToString(requestCase.getUpdatedDate());
				System.out.println("=== " + lastUpdateDate + " vs " + deletionWebVo.getLastUpdateDate());
				if (!lastUpdateDate.equals(deletionWebVo.getLastUpdateDate())) {
					deletionWebVo = convertWebVo(requestCase, userId, currentRole);
					deletionWebVo.setUpdateSuccess("N");
					deletionWebVo.setDisplayMessage("Record had been updated by other, content is refreshed.");
					
					return new ModelAndView("request/deletion", "formBean", deletionWebVo);
				}
			}
		}

		if ("SAVE".equalsIgnoreCase(formAction)) {
			try {
				boolean isExisting = false;
				if (deletionWebVo.getRequestNo() != null && !"".equals(deletionWebVo.getRequestNo())) {
					isExisting = true;
				}
				
				RequestPo newRequest = null;	
				if (isExisting) {
					System.out.println("CP 1");
					newRequest = requestSvc.getRequestByRequestNo(Integer.parseInt(deletionWebVo.getRequestNo()));
				}
				else {
					System.out.println("CP 2");
					Calendar now = Calendar.getInstance();
					String scurrentYear;
					scurrentYear = String.valueOf(now.get(Calendar.YEAR));	
					
					newRequest = new RequestPo();	
					
					// Get the first selected post and use this cluster code + inst code to generate the request id
					PostMasterPo mprsPost = requestSvc.getPostByPostUid(Integer.parseInt(deletionWebVo.getRequestPostNo().get(0)));
					String tmpRequestId = requestSvc.generateRequestNo(mprsPost.getClusterCode(), mprsPost.getInstCode(), "DEL", scurrentYear);
					System.out.println("tmpRequestId: " + tmpRequestId);
	
					newRequest.setRequestId(tmpRequestId);
					RequestTypePo requestType = new RequestTypePo();
					requestType.setRqTypeCode(MPRSConstant.REQUEST_TYPE_DELETION);
					newRequest.setRequestType(requestType);
					
					RequestStatusPo requestStatus = new RequestStatusPo();
					requestStatus.setStatusCode(MPRSConstant.REQUEST_STATUS_NEW);
					newRequest.setRequestStatus(requestStatus);
					newRequest.setRequestDate(new Date());
					newRequest.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
				}
				
				
				newRequest.setEffectFrom(sdf.parse(deletionWebVo.getEffectiveDate()));
				newRequest.setRequestReason(deletionWebVo.getRequestReason());
				newRequest.setCurrentWFUser(userId);
				newRequest.setCurrentWFGroup(currentRole);
				newRequest.setRequester(deletionWebVo.getRequester());
				
				if (!"".equals(deletionWebVo.getApprovalDate())) {
					newRequest.setApprovalDate(sdf.parse(deletionWebVo.getApprovalDate()));
				}
				
				newRequest.setApprovalReference(deletionWebVo.getApprovalReference());
				newRequest.setApprovalRemark(deletionWebVo.getApprovalRemark());
				
				for (int i=0; i<deletionWebVo.getRequestPostNo().size(); i++) {
					System.out.println("Add Request Position!!");
	
					RequestPostPo rp = null;
					if (newRequest.getRequestPositionList().size() > i) {
						rp = newRequest.getRequestPositionList().get(i);
					}
					else {
						rp = new RequestPostPo();
					}
						
					PostMasterPo mprsPost = requestSvc.getPostByPostUid(Integer.parseInt(deletionWebVo.getRequestPostNo().get(i)));
					rp.setPostNo(deletionWebVo.getRequestPostNo().get(i));
					RankPo rank = new RankPo();
					rank.setRankCode(mprsPost.getRankCode());
					rp.setRank(rank);
					rp.setPostId(mprsPost.getPostId());
					rp.setClusterCode(mprsPost.getClusterCode());
					rp.setInstCode(mprsPost.getInstCode());
					rp.setPostDuration(mprsPost.getPostDuration());
					rp.setPostFTEType(mprsPost.getPostFTEType());
					rp.setPostFTE(String.valueOf(mprsPost.getPostFTE()));
					rp.setStaffGroupCode(mprsPost.getStaffGroupCode());
					rp.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
	
					if (newRequest.getRequestPositionList().size() > i) {
						System.out.println("Perform update RequestPosition");
						newRequest.getRequestPositionList().set(i, rp);
					}
					else {
						System.out.println("Perform create RequestPosition");
						newRequest.addRequestPosition(rp);
					}
				}
				
				for (int i=deletionWebVo.getRequestPostNo().size(); i<newRequest.getRequestPositionList().size(); i++) {
					System.out.println("Perform set inactive. " + newRequest.getRequestPositionList().get(i).getRequestPostId());
					newRequest.getRequestPositionList().get(i).setRecState(MPRSConstant.MPRS_STATE_INACTIVE);
				}
				
				List<RequestAttachmentPo> attachmentList = new ArrayList<RequestAttachmentPo>();
				
				if (deletionWebVo.getUploadFileId() != null && !"".equals(deletionWebVo.getUploadFileId())) {
					for (int i=0; i<deletionWebVo.getUploadFileId().size(); i++) {
						System.out.println(i + ": deletionWebVo.getUploadFileId()[i] : " + deletionWebVo.getUploadFileId().get(i));
						if (deletionWebVo.getUploadFileId().get(i) != null && !"".equals(deletionWebVo.getUploadFileId().get(i))) {
							RequestTempFilePo tempFile = requestSvc.getTempFile(Integer.parseInt(deletionWebVo.getUploadFileId().get(i)));
			
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
				// Perform update of attachment - End		
						
				int requestNo = -1;
				if (deletionWebVo.getRequestNo() != null && !"".equals(deletionWebVo.getRequestNo())) {
					requestSvc.update(newRequest, deletionWebVo.getRemoveAttachmentUid(), deletionWebVo.getUploadFileId(), user);
					requestNo = newRequest.getRequestNo();
				}
				else {
					requestNo = requestSvc.insert(newRequest, deletionWebVo.getUploadFileId(), true, null, null, user);
				}
				
				// Retrieve again the request 
				RequestPo requestCase = requestSvc.getRequestByRequestNo(requestNo);
				deletionWebVo = convertWebVo(requestCase, userId, currentRole);
				
				deletionWebVo.setDisplayMessage("Request update successful.");
				deletionWebVo.setUpdateSuccess("Y");
			}
			catch (Exception ex) {
				EClaimLogger.error("performUpdate - update:" + ex.getMessage(), ex);
				String errorMsg = doHandleException(ex.getCause(), ex.getMessage());
				
				if (deletionWebVo.getRequestNo() != null && !"".equals(deletionWebVo.getRequestNo())) {
					RequestPo requestCase = requestSvc.getRequestByRequestNo(Integer.parseInt(deletionWebVo.getRequestNo()));
					deletionWebVo = convertWebVo(requestCase, userId, currentRole);
				}
				
				System.out.println("errorMsg: " + errorMsg);
				deletionWebVo.setUpdateSuccess("N");
				deletionWebVo.setDisplayMessage(errorMsg);
			}
		}
		else if ("WITHDRAW".equalsIgnoreCase(formAction)) {
			System.out.println("Request No.: " + deletionWebVo.getRequestNo());
			int requestNo = Integer.parseInt(deletionWebVo.getRequestNo());
			String errorMsg = requestSvc.submitWithdraw(requestNo,  "DEL", userId, currentRole);

			if ("".equals(errorMsg) || errorMsg == null) {
				// Retrieve again the request 
				RequestPo requestCase = requestSvc.getRequestByRequestNo(Integer.parseInt(deletionWebVo.getRequestNo()));

				deletionWebVo = convertWebVo(requestCase, userId, currentRole);
				deletionWebVo.setUpdateSuccess("Y");
				deletionWebVo.setDisplayMessage("Request withdraw successful.");
			}
			else {
				System.out.println("errorMsg: " + errorMsg);
				deletionWebVo.setUpdateSuccess("N");
				deletionWebVo.setDisplayMessage(errorMsg);
			}
			
			return new ModelAndView("request/deletion", "formBean", deletionWebVo);
		}
		else if ("SUBMIT".equalsIgnoreCase(formAction)) {
			System.out.println("Request No.: " + deletionWebVo.getRequestNo());
			int requestNo = Integer.parseInt(deletionWebVo.getRequestNo());
			
			String emailTo = "";
			String emailCC = "";
			String emailTitle = "";
			String emailContent = "";
			String generateEmail = "N";
			
			if ("Y".equals(deletionWebVo.getSubmitWithModifiedEmail())) {
				emailTo = deletionWebVo.getEmailTo();
				emailCC = deletionWebVo.getEmailCC();
				emailTitle = deletionWebVo.getEmailTitle();
				String si = StrHelper.format(deletionWebVo.getEmailSuppInfo().replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$"));
				emailContent = deletionWebVo.getEmailContent().replaceAll("<Supplementary Information / Comment>", si);
				String url = appUrl + "/request/deletion?rq=" + deletionWebVo.getRequestNo() + "&fromMail=Y";
				emailContent = emailContent.replaceAll("Please click here to review the request", "Please click <a href='" + url + "'>here</a> to review the request");
				generateEmail = "Y";
			}
			
			if ("Y".equals(deletionWebVo.getSubmitWithEmail())) {
				RequestPo tmpCase = requestSvc.getRequestByRequestNo(requestNo);
				String postId = "";
				for (int i=0; i<tmpCase.getRequestPositionList().size(); i++) {
					if (i != 0) {
						postId += ", ";
					}
					
					postId += tmpCase.getRequestPositionList().get(i).getPostId();
				}
				
				String submittedBy = user.getUserName();
				String submittedDate = DateTimeHelper.formatDateToString(new Date());
				String suppInfo = StrHelper.format(deletionWebVo.getEmailSuppInfo());
				
				String receiver = "";
				if (!"".equals(deletionWebVo.getNextWFUser())) {
					UserPo requester = securitySvc.findUser(deletionWebVo.getNextWFUser());
					receiver = requester.getUserName();
				}
				else {
					List<RolePo> userGroupList = securitySvc.getAllRole();
					for (int i=0; i<userGroupList.size(); i++) {
						if (deletionWebVo.getNextWFGroup().equals(userGroupList.get(i).getRoleId())) {
							receiver = userGroupList.get(i).getRoleName();
							break;
						}
					}
				}
				
				EmailTemplatePo emailTemplate = commonSvc.getTemplateByTemplateId("RQ_DEL_SUBMIT", new String[] {String.valueOf(deletionWebVo.getRequestId()),
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
				if (!"".equals(deletionWebVo.getNextWFUser()) && deletionWebVo.getNextWFUser() != null) {
					try {
						UserPo tUser = securitySvc.findUser( deletionWebVo.getNextWFUser());
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
						List<UserPo> userList = securitySvc.getAllUserByGroupAndCluster(clusterCode, instCode, deletionWebVo.getNextWFGroup());
						
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
				
				if ("Y".equals(deletionWebVo.getReturnCase())) { 
					List<UserPo> ccUserList = securitySvc.getReturnUserFromWorkflowHistoryAll(deletionWebVo.getRequestNo(), deletionWebVo.getNextWFGroup());
					for (int i=0; i<ccUserList.size(); i++) {
						if (ccUserList.get(i).getEmail() != null) {
							if (!"".equals(ccEmailList)) {
								ccEmailList += ", ";
							}
							
							ccEmailList += ccUserList.get(i).getEmail();
						}
					}
				}
				
				// If submit to MG team, cc to all related party
				else if ("HO_MG_OFFICER".equals(deletionWebVo.getNextWFGroup())) {
					List<UserPo> ccUserList = securitySvc.getRelatedUserFromWorkflowHistoryAll(deletionWebVo.getRequestNo()) ;
					for (int i=0; i<ccUserList.size(); i++) {
						if (ccUserList.get(i).getEmail() != null) {
							if (!ccEmail.contains(ccUserList.get(i).getEmail())) {
								if (!"".equals(ccEmailList)) {
									ccEmailList += ", ";
								}
								
								ccEmailList += ccUserList.get(i).getEmail();
								ccEmail.add(ccUserList.get(i).getEmail());
							}
						}
					}
					
					RequestPo requestCase = requestSvc.getRequestByRequestNo(requestNo);
					String clusterCode = "";
					String instCode = "";
					for (int i=0; i<requestCase.getRequestPositionList().size(); i++) {
						clusterCode = requestCase.getRequestPositionList().get(i).getClusterCode();
						instCode = requestCase.getRequestPositionList().get(i).getInstCode();
						List<UserPo> userList = securitySvc.getAllUserByGroupAndCluster(clusterCode, instCode, "FIN_MANAGER");
						
						for (int j=0; j<userList.size(); j++) {
							if (userList.get(j).getEmail() != null) {
								if (!ccEmail.contains(userList.get(j).getEmail())) {
									if (!"".equals(ccEmailList)) {
										ccEmailList += ", ";
									}
									
									ccEmailList += userList.get(j).getEmail();
									ccEmail.add(userList.get(j).getEmail());
								}
							}
						}
					}
				}
				
				emailTo = emailList;
				emailCC = ccEmailList;
				emailTo = StrHelper.removeDuplicateEmail(emailTo);
				emailCC = StrHelper.removeDuplicateEmail(emailCC);
				emailTitle = emailTemplate.getTemplateTitle();
				String si = StrHelper.format(deletionWebVo.getEmailSuppInfo().replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$"));
				emailContent = emailTemplate.getTemplateContent().replaceAll("<Supplementary Information / Comment>", si);
				String url = appUrl + "/request/deletion?rq=" + deletionWebVo.getRequestNo() + "&fromMail=Y";
				emailContent = emailContent.replaceAll("Please click here to review the request", "Please click <a href='" + url + "'>here</a> to review the request");
				generateEmail = "Y";
			}
			
			System.out.println("emailContent: " + emailContent);
			
			
			String errorMsg = requestSvc.submitWorkflow(requestNo,  "DEL", 
					                                    deletionWebVo.getNextWFGroup(), 
					                                    deletionWebVo.getNextWFUser(), 
					                                    deletionWebVo.getReturnCase(),
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

				deletionWebVo = convertWebVo(requestCase, userId, currentRole);
				deletionWebVo.setUpdateSuccess("Y");
				deletionWebVo.setDisplayMessage("Request submit successful.");
			}
			else {
				System.out.println("errorMsg: " + errorMsg);
				deletionWebVo.setUpdateSuccess("N");
				deletionWebVo.setDisplayMessage(errorMsg);
			}
			
			return new ModelAndView("request/deletion", "formBean", deletionWebVo);
		}
		else if ("APPROVE".equalsIgnoreCase(formAction)) {
			System.out.println("Request No. submit approve: " + deletionWebVo.getRequestNo());
			try {
				int requestNo = Integer.parseInt(deletionWebVo.getRequestNo());
				
				String emailTo = "";
				String emailCC = "";
				String emailTitle = "";
				String emailContent = "";
				String generateEmail = "N";
				
				if ("Y".equals(deletionWebVo.getSubmitWithModifiedEmail())) {
					emailTo = deletionWebVo.getEmailTo();
					emailCC = deletionWebVo.getEmailCC();
					emailTitle = deletionWebVo.getEmailTitle();
					String si = StrHelper.format(deletionWebVo.getEmailSuppInfo().replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$"));
					emailContent = deletionWebVo.getEmailContent().replaceAll("<Supplementary Information / Comment>", si);
					
					String url = appUrl + "/request/deletion?rq=" + deletionWebVo.getRequestNo() + "&fromMail=Y";
					emailContent = emailContent.replaceAll("Please click here to review the request", "Please click <a href='" + url + "'>here</a> to review the request");
	
					generateEmail = "Y";
				}
				
				if ("Y".equals(deletionWebVo.getSubmitWithEmail())) {
					RequestPo tmpCase = requestSvc.getRequestByRequestNo(requestNo);
					String postId = "";
					for (int i=0; i<tmpCase.getRequestPositionList().size(); i++) {
						if (i != 0) 
							postId += ", ";
						
						postId += tmpCase.getRequestPositionList().get(i).getPostId();
					}
					
					String submittedBy = user.getUserName();
					String submittedDate = DateTimeHelper.formatDateToString(new Date());
					String suppInfo = StrHelper.format(deletionWebVo.getEmailSuppInfo());
					
					String receiver = "ALL";
					
					EmailTemplatePo emailTemplate = commonSvc.getTemplateByTemplateId("RQ_DEL_CONFIRM", new String[] {String.valueOf(deletionWebVo.getRequestId()),
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
					
					List<UserPo> ccUserList = securitySvc.getRelatedUserFromWorkflowHistoryAll(deletionWebVo.getRequestNo()) ;
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
					
					// CC to Financial Manager
					RequestPo requestCase = requestSvc.getRequestByRequestNo(requestNo);
					String clusterCode = "";
					String instCode = "";
					for (int i=0; i<requestCase.getRequestPositionList().size(); i++) {
						clusterCode = requestCase.getRequestPositionList().get(i).getClusterCode();
						instCode = requestCase.getRequestPositionList().get(i).getInstCode();
						List<UserPo> userList = securitySvc.getAllUserByGroupAndCluster(clusterCode, instCode, "FIN_MANAGER");
						
						for (int j=0; j<userList.size(); j++) {
							if (userList.get(j).getEmail() != null) {
								if (!"".equals(emailCC)) {
									emailCC += ", ";
								}

								emailCC += userList.get(j).getEmail();
							}
						}
					}
					
					String staffGroupCode = "";
					for (int i=0; i<requestCase.getRequestPositionList().size(); i++) {
						if (MPRSConstant.MPRS_STATE_ACTIVE.equals(requestCase.getRequestPositionList().get(i).getRecState())) {
							staffGroupCode = requestCase.getRequestPositionList().get(i).getStaffGroupCode();
						}
					}
					
					System.out.println("DD Staff Group Code: " + staffGroupCode); 
					
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
								ccEmail.add(approvalCCList.get(i).getEmail());
							}
						}		
					}
					
					emailTitle = emailTemplate.getTemplateTitle();
					String si = StrHelper.format(deletionWebVo.getEmailSuppInfo().replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$"));
					emailContent = emailTemplate.getTemplateContent().replaceAll("<Supplementary Information / Comment>", si);
					String url = appUrl + "/request/deletion?rq=" + deletionWebVo.getRequestNo() + "&fromMail=Y";
					emailContent = emailContent.replaceAll("Please click here to review the request", "Please click <a href='" + url + "'>here</a> to review the request");
					
					generateEmail = "Y";
				}
				emailTo = StrHelper.removeDuplicateEmail(emailTo);
				emailCC = StrHelper.removeDuplicateEmail(emailCC);
				
				String errorMsg = requestSvc.submitApprove(requestNo, "DEL",
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
	
					deletionWebVo = convertWebVo(requestCase, userId, currentRole);
					deletionWebVo.setUpdateSuccess("Y");
					deletionWebVo.setDisplayMessage("Request approve successful.");
				}
				else {
					deletionWebVo.setUpdateSuccess("N");
					deletionWebVo.setDisplayMessage(errorMsg);
				}
			}
			catch (Exception ex) {
				EClaimLogger.error("performUpdate - send Email:" + ex.getMessage(), ex);
				String errorMsg = doHandleException(ex.getCause(), ex.getMessage());
				
				// Retrieve again the request 
				RequestPo requestCase = requestSvc.getRequestByRequestNo(Integer.parseInt(deletionWebVo.getRequestNo()));
				deletionWebVo = convertWebVo(requestCase, userId, currentRole);
				
				System.out.println("errorMsg: " + errorMsg);
				deletionWebVo.setUpdateSuccess("N");
				deletionWebVo.setDisplayMessage(errorMsg);
			}
			
			return new ModelAndView("request/deletion", "formBean", deletionWebVo);
		}
		
		return new ModelAndView("request/deletion", "formBean", deletionWebVo);
	}
	
	private DeletionWebVo convertWebVo(RequestPo requestCase, String currentUser, String currentRole) {
		DeletionWebVo vo = new DeletionWebVo();
		
		vo.setRequestNo(String.valueOf(requestCase.getRequestNo()));
		
		vo.setRequestId(requestCase.getRequestId());
		vo.setRequestStatus(requestCase.getRequestStatus().getStatusCode());
		vo.setCurrentWFGroup(requestCase.getCurrentWFGroup());
		vo.setCurrentWFUser(requestCase.getCurrentWFUser());
		vo.setEffectiveDate(DateTimeHelper.formatDateToString(requestCase.getEffectFrom()));
		vo.setRequestReason(requestCase.getRequestReason());
		vo.setRequester(requestCase.getRequester());
		
		List<RequestPostPo> positionList = requestCase.getRequestPositionList();
		List<RequestPostPo> displayList = new ArrayList<RequestPostPo>();
		String staffGroupCode = "";
		for (int i=0; i<positionList.size(); i++) {
			if (MPRSConstant.MPRS_STATE_ACTIVE.equals(positionList.get(i).getRecState())) {
				positionList.get(i).setPostFTE(StrHelper.formatDecimal(positionList.get(i).getPostFTE()));
				
				// Look up the annual plan ind
				PostMasterPo po = requestSvc.getPostByPostUid(Integer.parseInt(positionList.get(i).getPostNo()));
				String annualPlanInd = "";
				for (int k=0; k<po.getPostFundingList().size(); k++) {
					if (k!=0) {
						annualPlanInd += "/";
					}
					
					if (po.getPostFundingList().get(k).getAnnualPlanInd() == null) {
						annualPlanInd += "";
					}
					else if ("Y".equals(po.getPostFundingList().get(k).getAnnualPlanInd())) {
						annualPlanInd += "Y";
					}
					else {
						annualPlanInd += "N";
					}
				}
				positionList.get(i).setAnnualPlanInd(annualPlanInd);
				displayList.add(positionList.get(i));
				
				staffGroupCode = positionList.get(i).getStaffGroupCode();
			}
		}
		
		vo.setRequestPositionList(displayList);
		if (requestCase.getApprovalDate() != null) {
			vo.setApprovalDate(DateTimeHelper.formatDateToString(requestCase.getApprovalDate()));
		}
		vo.setApprovalReference(requestCase.getApprovalReference());
		vo.setApprovalRemark(requestCase.getApprovalRemark());
		
		String mgTeamInd = "N";
		String postDurationCode = "";
		String postFteType = "";
		System.out.println("vo.getRequestPositionList().size():" + vo.getRequestPositionList().size());
		for (int i=0; i<vo.getRequestPositionList().size(); i++) {
			staffGroupCode = vo.getRequestPositionList().get(i).getStaffGroupCode();
			postDurationCode = vo.getRequestPositionList().get(i).getPostDuration();
			postFteType = vo.getRequestPositionList().get(i).getPostFTEType();
			
			System.out.println("Checking: " + staffGroupCode + "/" + postDurationCode + "/" + vo.getRequestPositionList().get(i).getPostFTEType());
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
		RequestWorkflowRoutePo route = this.requestSvc.getDefaultRoute(staffGroupCode, MPRSConstant.REQUEST_TYPE_DELETION, vo.getRequestStatus(), mgTeamInd);
		if (route != null) {
			vo.setSubmitButtonLabel(route.getActionName());
			System.out.println("****** route.getRouteUid()=" + route.getRouteUid().intValue());
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
				System.out.println("staff group code");
				
				
				// Check whether the user have approval right
				if (requestSvc.hasApprovalRight(staffGroupCode, MPRSConstant.REQUEST_TYPE_DELETION, vo.getRequestStatus(), currentRole, vo.getMgTeamInd())) {
					vo.setUserHaveApprovalRight("Y");
				}
				else {
					vo.setUserHaveApprovalRight("N");
				}
				
				if ("HR_OFFICER".equals(currentRole)) {
					vo.setUserHaveWithdrawRight("Y");
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
		
		if ("HR_OFFICER".equals(currentRole)) {
			vo.setUserHaveCreationRight("Y");
		}
		else {
			vo.setUserHaveWithdrawRight("N");
			vo.setUserHaveSaveRight("N");
			vo.setUserHaveCreationRight("N");
		}
		
		// Update the last update date
		vo.setLastUpdateDate(DateTimeHelper.formatDateTimeToString(requestCase.getUpdatedDate()));
		vo.setRequestType(MPRSConstant.REQUEST_TYPE_DELETION);
		
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
}

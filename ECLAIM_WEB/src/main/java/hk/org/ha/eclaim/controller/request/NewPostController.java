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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import hk.org.ha.eclaim.bs.request.constant.PostConstant;
import hk.org.ha.eclaim.bs.request.po.RequestAttachmentPo;
import hk.org.ha.eclaim.bs.request.po.RequestFundingPo;
import hk.org.ha.eclaim.bs.request.po.RequestFundingResourcePo;
import hk.org.ha.eclaim.bs.request.po.RequestPo;
import hk.org.ha.eclaim.bs.request.po.RequestPostPo;
import hk.org.ha.eclaim.bs.request.po.RequestTempFilePo;
import hk.org.ha.eclaim.bs.request.po.RequestWorkflowRoutePo;
import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.model.request.NewPostWebVo;
import hk.org.ha.eclaim.model.request.RequestFundingWebVo;
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
import hk.org.ha.eclaim.bs.hcm.po.HCMRecordPo;
import hk.org.ha.eclaim.bs.hcm.svc.IHCMSvc;
import hk.org.ha.eclaim.bs.security.po.DataAccessPo;
import hk.org.ha.eclaim.bs.security.po.RolePo;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.core.helper.DateTimeHelper;
import hk.org.ha.eclaim.core.helper.StrHelper;
import hk.org.ha.eclaim.core.logger.EClaimLogger;

@Controller
public class NewPostController extends CommonRequestController {

	@Value("${mail.appurl.prefix}")
	private String appUrl;
	
	@Autowired
	IHCMSvc hcmSvc;
	
	@RequestMapping(value="/request/newPost", method=RequestMethod.GET)
	public ModelAndView newPosition(@ModelAttribute("formBean")NewPostWebVo createPositionWebVo, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// Get the user name from cookie
		String userId = this.getSessionUser(request).getUserId();
		UserPo user = securitySvc.findUser(userId);
		String currentRole = (String)request.getSession().getAttribute("currentRole");
        user.setCurrentRole(currentRole);
		
		if (request.getParameter("rq") != null) {
			EClaimLogger.info("request uid: " + request.getParameter("rq"));
			
			// Check whether the user have access right for request uid
			int userRoleId = (int) request.getSession().getAttribute("currentUserRoleId");
			List<DataAccessPo> dataAccessList = securitySvc.getDataAccessByRoleId(userRoleId);
			RequestPo requestCase = requestSvc.getRequestByRequestNo(dataAccessList, Integer.parseInt((String)request.getParameter("rq")), true);
			if (requestCase != null) {
				EClaimLogger.info("requestCase.getRequestId(): " + requestCase.getRequestId());
				NewPostWebVo vo = convertToWebVo(requestCase, userId, currentRole);
				return new ModelAndView("request/newPost", "formBean", vo);
			}
			else {
				return new ModelAndView("redirect:/home/home");
			}
		}
		else {
			EClaimLogger.info("request uid is null");
			NewPostWebVo vo = new NewPostWebVo();
			vo.getRequestFundingList().add(new RequestFundingWebVo());
			if ("HR_OFFICER".equals(currentRole)) {
				vo.setCanEditDetailInfo("Y");
				vo.setWithMassSave("N");
				vo.setUserHaveCreationRight("Y");
			}
			else {
				vo.setCanEditDetailInfo("N");
				vo.setWithMassSave("N");
				vo.setUserHaveCreationRight("N");
			}

			if ("FIN_OFFICER".equals(currentRole)) {
				vo.setCanEditFinancialInfo("Y");
			}
			else {
				vo.setCanEditFinancialInfo("N");
			}
			
			EClaimLogger.info("CanEditDetailInfo: " + vo.getCanEditDetailInfo());
			EClaimLogger.info("CanEditFinancialInfo: " + vo.getCanEditFinancialInfo());
			vo.setUserName(user.getUserName());
			return new ModelAndView("request/newPost", "formBean", vo);
		}
	}

	@RequestMapping(value="/request/newPost", method=RequestMethod.POST)
	public ModelAndView performUpdate(@ModelAttribute("formBean")NewPostWebVo createPositionWebVo, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("NewPostController: performUpdate");
		// Get the user name from cookie
		String userId = this.getSessionUser(request).getUserId();
		UserPo user = securitySvc.findUser(userId);
		String currentRole = (String)request.getSession().getAttribute("currentRole");
        user.setCurrentRole(currentRole);

		// int iRequestNo = -1;
		String sRequestID;
		String sCluster;
		String sInstitution;
		String sAction;
		String scurrentYear;
		String tmpRequestId = "";
		Calendar now = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");  

		NewPostWebVo vo = createPositionWebVo;

		sRequestID = createPositionWebVo.getRequestId();
		System.out.println("RequestID: " + createPositionWebVo.getRequestId());

		String formAction = createPositionWebVo.getFormAction();
		System.out.println("formAction: " + formAction);
		
		// Concurrent control
		if ("SAVE".equalsIgnoreCase(formAction) ||
				"WITHDRAW".equalsIgnoreCase(formAction) ||
				"SUBMIT".equalsIgnoreCase(formAction) ||
				"APPROVE".equalsIgnoreCase(formAction)) {
			if (createPositionWebVo.getCurrentRequestNo() != null && !"".equals(createPositionWebVo.getCurrentRequestNo())) {
				RequestPo requestCase = requestSvc.getRequestByRequestNo(Integer.parseInt(createPositionWebVo.getCurrentRequestNo()));

				// Check last update date
				String lastUpdateDate = DateTimeHelper.formatDateTimeToString(requestCase.getUpdatedDate());
				System.out.println("=== " + lastUpdateDate + " vs " + createPositionWebVo.getLastUpdateDate());
				if (!lastUpdateDate.equals(createPositionWebVo.getLastUpdateDate())) {
					createPositionWebVo = convertToWebVo(requestCase, userId, currentRole);
					createPositionWebVo.setUpdateSuccess("N");
					createPositionWebVo.setDisplayMessage("Record had been updated by other, content is refreshed.");

					return new ModelAndView("request/newPost", "formBean", createPositionWebVo);
				}
			}
		}

		if ("SAVE".equalsIgnoreCase(formAction)) {
			try {
				int noOfCopySave = 1;
				if (!"".equals(vo.getNoOfCopySave())) {
					if (vo.getCurrentRequestNo() != null && !"".equals(vo.getCurrentRequestNo())) { 
						noOfCopySave = Integer.parseInt(vo.getNoOfCopySave()) + 1;
					}
					else {
						noOfCopySave = Integer.parseInt(vo.getNoOfCopySave());
					}
				}
				
				List<RequestAttachmentPo> cloneAttachmentList = null;
				Integer sourceRequestUid = null;
	
				System.out.println("noOfCopySave: " + noOfCopySave);
				int requestNo = -1;
				for (int i=0; i<noOfCopySave; i++) {
					System.out.println("Perform xxx " + i);
					if(sRequestID == null || sRequestID.trim().isEmpty() || i > 0) {
						sCluster = createPositionWebVo.getCluster();
						sInstitution = createPositionWebVo.getInstitution();
						sAction = "NEW";
						scurrentYear = String.valueOf(now.get(Calendar.YEAR));	
						tmpRequestId = requestSvc.generateRequestNo(sCluster, sInstitution, sAction, scurrentYear);
						System.out.println("tmpRequestId: " + tmpRequestId);
	
						vo.setRequestId(tmpRequestId);
						vo.setRequestStatus(sAction);
						vo.setCreateBy(userId);
						vo.setUpdateBy(userId);
					} else {
						tmpRequestId = sRequestID;
					}
					RequestPo newRequest = null;
					System.out.println("getRequestNo: " + vo.getCurrentRequestNo());
					if (vo.getCurrentRequestNo() != null && !"".equals(vo.getCurrentRequestNo()) && i == 0) { 
						requestNo = Integer.parseInt(vo.getCurrentRequestNo());
						newRequest = requestSvc.getRequestByRequestNo(Integer.parseInt(vo.getCurrentRequestNo()));
						newRequest.setCurrentWFUser(userId); // Fix for ST08107
						newRequest.setRequester(createPositionWebVo.getRequester());
						
						cloneAttachmentList = newRequest.getAttachment();
						sourceRequestUid = requestNo;
					}
					else {
						newRequest = new RequestPo();	
						RequestStatusPo requestStatus = new RequestStatusPo();
						requestStatus.setStatusCode(MPRSConstant.REQUEST_STATUS_NEW);
						newRequest.setRequestStatus(requestStatus);
						newRequest.setRequestId(tmpRequestId);
						newRequest.setRequestDate(now.getTime());
						newRequest.setRequester(createPositionWebVo.getRequester());
						newRequest.setCurrentWFUser(userId);
					}
					newRequest.setCurrentWFGroup(currentRole);
					RequestTypePo requestType = new RequestTypePo();
					requestType.setRqTypeCode(MPRSConstant.REQUEST_TYPE_NEW_REQUEST);
					newRequest.setRequestType(requestType);
					
					if (!"".equals(createPositionWebVo.getPostStartDate()) && createPositionWebVo.getPostStartDate() != null)
						newRequest.setEffectFrom(sdf.parse(createPositionWebVo.getPostStartDate()));
					if (!"".equals(createPositionWebVo.getApprovalDate()) && createPositionWebVo.getApprovalDate() != null)
						newRequest.setApprovalDate(sdf.parse(createPositionWebVo.getApprovalDate()));
					newRequest.setApprovalReference(createPositionWebVo.getApprovalReference());
					newRequest.setApprovalRemark(createPositionWebVo.getApprovalRemark());
					
					System.out.println("Add Request Position!!");
					boolean needRecycle = true;
					String tmpProposedPostId = null;
					RequestPostPo rp = null;
					if (vo.getCurrentRequestNo() != null && !"".equals(vo.getCurrentRequestNo()) && i == 0) {
						System.out.println("CP 1");
						rp = newRequest.getRequestPositionList().get(0);
						
						tmpProposedPostId = rp.getProposedPostId();
						
						if (tmpProposedPostId != null) {
						
							// Original Proposed Post ID should be stored into CS_POST_ID_SEQ_REUSE,
							// if below fields changes
							if (rp.getClusterCode().equals(createPositionWebVo.getCluster()) && 
								rp.getInstCode().equals(createPositionWebVo.getInstitution()) &&
								rp.getDeptCode().equals(createPositionWebVo.getDepartment()) &&
								rp.getRank().getRankCode().equals(createPositionWebVo.getRank()) &&
								rp.getPostDuration().equals(createPositionWebVo.getPostDuration()) &&
								rp.getPostFTEType().equals(createPositionWebVo.getPostFTE())) {
								needRecycle = false;
							} else {
								System.out.println("Recycle: " + tmpProposedPostId);
								requestSvc.recycleProposedPostId(rp.getStaffGroupCode(), rp.getPostDuration(), rp.getPostFTEType(), tmpProposedPostId, userId, currentRole);
							}
						}
						
						RankPo rank = new RankPo();
						rank.setRankCode(createPositionWebVo.getRank());
						rp.setRank(rank);
						rp.setClusterCode(createPositionWebVo.getCluster());
						rp.setInstCode(createPositionWebVo.getInstitution());
						rp.setStaffGroupCode(createPositionWebVo.getStaffGroup());
						rp.setDeptCode(createPositionWebVo.getDepartment());
						rp.setSubSpecialtyCode(createPositionWebVo.getSubSpecialty());
						
						// For HO Buy Service
						rp.setHoBuyServiceInd(createPositionWebVo.getHoBuyServiceInd());
						
						// For Shortfall Post
						rp.setShortFallInd("N");
						if (!"".equals(createPositionWebVo.getShortFallPostId())) {
							rp.setShortFallPostId(createPositionWebVo.getShortFallPostId());
						}
						else {
							rp.setShortFallPostId("");
						}
					}
					else {
						System.out.println("CP 2");
						rp = new RequestPostPo();
						
						RankPo rank = new RankPo();
						rank.setRankCode(createPositionWebVo.getRank());
						rp.setRank(rank);
						rp.setPostId("");
						rp.setClusterCode(createPositionWebVo.getCluster());
						rp.setInstCode(createPositionWebVo.getInstitution());
						rp.setStaffGroupCode(createPositionWebVo.getStaffGroup());
						rp.setDeptCode(createPositionWebVo.getDepartment());
						rp.setSubSpecialtyCode(createPositionWebVo.getSubSpecialty());
						
						// For HO Buy Service
						rp.setHoBuyServiceInd(createPositionWebVo.getHoBuyServiceInd());
						
						// For Shortfall Post
						rp.setShortFallInd("N");
						if (!"".equals(createPositionWebVo.getShortFallPostId())) {
							rp.setShortFallPostId(createPositionWebVo.getShortFallPostId());
						}
						else {
							rp.setShortFallPostId("");
						}
					}
	
					System.out.println("--Position Tab Saved --");
					rp.setUnit(createPositionWebVo.getUnit());
					rp.setPostTitle(createPositionWebVo.getPostTitle());
					rp.setPostDuration(createPositionWebVo.getPostDuration());
					rp.setLimitDurationType(createPositionWebVo.getLimitDurationType());
					if (!"".equals(createPositionWebVo.getPostStartDate()) && createPositionWebVo.getPostStartDate() != null) {
						rp.setPostStartDate(sdf.parse(createPositionWebVo.getPostStartDate()));//review
					}
					rp.setLimitDurationNo(createPositionWebVo.getLimitDurationNo());
					rp.setLimitDurationUnit(createPositionWebVo.getLimitDurationUnit());
					if (!"".equals(createPositionWebVo.getLimitDurationEndDate()) && createPositionWebVo.getLimitDurationEndDate() != null) {
						rp.setLimitDurationEndDate(sdf.parse(createPositionWebVo.getLimitDurationEndDate()));
					}
					rp.setPostRemark(createPositionWebVo.getPostRemark());
					rp.setPostFTEType(createPositionWebVo.getPostFTE());
					rp.setPostFTE(createPositionWebVo.getPostFTEValue());
					rp.setPostStatus(createPositionWebVo.getPositionStatus());
					if (!"".equals(createPositionWebVo.getPositionStartDate()) && createPositionWebVo.getPositionStartDate() != null) {
						rp.setPostStatusStartDate(sdf.parse(createPositionWebVo.getPositionStartDate()));
					}
					if (!"".equals(createPositionWebVo.getPositionEndDate()) && createPositionWebVo.getPositionEndDate() != null) {
						rp.setPostStatusEndDate(sdf.parse(createPositionWebVo.getPositionEndDate()));
					}
					rp.setClusterRef(createPositionWebVo.getClusterRefNo());
					rp.setClusterRemark(createPositionWebVo.getClusterRemark());
					rp.setHcmPositionId(createPositionWebVo.getHcmPositionId());
					
					// Update the Fixed End Date if selected (Time-Limited) and Duration Period
					if (!"R".equals(createPositionWebVo.getPostDuration()) && "DURATION_PERIOD".equals(createPositionWebVo.getLimitDurationType())) {
						int duration = 0;
						if ("Y".equals(createPositionWebVo.getLimitDurationUnit())) {
							duration = createPositionWebVo.getLimitDurationNo() * 12;
						}
						else if ("M".equals(createPositionWebVo.getLimitDurationUnit())) {
							duration = createPositionWebVo.getLimitDurationNo();
						}
						
						rp.setLimitDurationEndDate(requestSvc.addMonth(sdf.parse(createPositionWebVo.getPostStartDate()), duration));
					}
					
					if ("R".equals(createPositionWebVo.getPostDuration())) {
						rp.setLimitDurationEndDate(null);
					}
	
					System.out.println("--Funding Details Tab --");
					int poSize = rp.getRequestFundingList().size();
					int voSize = createPositionWebVo.getRequestFundingList().size();
					System.out.println("poSize: " + poSize);
					System.out.println("voSize: " + voSize);
					
					List<RequestFundingPo> newFundingList = new ArrayList<RequestFundingPo>();
					List<String> annualPlanList = new ArrayList<String>();
					List<String> programTypeList = new ArrayList<String>();
					
					for (int a=0; a<voSize; a++) {
						RequestFundingWebVo fundVo = createPositionWebVo.getRequestFundingList().get(a);
						
						RequestFundingPo fundPo = null;
						if (a < rp.getRequestFundingList().size()) {
							fundPo = rp.getRequestFundingList().get(a);
						}
						else {
							fundPo = new RequestFundingPo();
						}
						
						fundPo.setFundingSeqNo(a+1);
						fundPo.setAnnualPlanInd(fundVo.getAnnualPlanInd());
						annualPlanList.add(fundVo.getAnnualPlanInd());
						fundPo.setInst(fundVo.getInst());
						fundPo.setSection(fundVo.getSection());
						fundPo.setAnalytical(fundVo.getAnalytical());
						fundPo.setFundSrcId(fundVo.getFundSrcId());
						fundPo.setFundSrcSubCatId(fundVo.getFundSrcSubCatId());
						fundPo.setProgramCode(fundVo.getProgramCode());
						fundPo.setProgramName(fundVo.getProgramName());
						fundPo.setProgramTypeCode(fundVo.getProgramTypeCode());
						programTypeList.add(fundVo.getProgramTypeCode());
						fundPo.setProgramYear(fundVo.getProgramYear());
						fundPo.setFundSrcRemark(fundVo.getFundSrcRemark());
						
						if (!"".equals(fundVo.getFundSrcStartDate())) {
							fundPo.setFundSrcStartDate(sdf.parse(fundVo.getFundSrcStartDate()));
						}
						else {
							// If the start date is empty, change to default Post start date
							fundPo.setFundSrcStartDate(sdf.parse(createPositionWebVo.getPostStartDate()));
						}
						
						if (!"".equals(fundVo.getFundSrcEndDate())) {
							fundPo.setFundSrcEndDate(sdf.parse(fundVo.getFundSrcEndDate()));
						}
						else {
							fundPo.setFundSrcEndDate(null);
						}
						
						if (!"".equals(fundVo.getFundSrcFte())) {
							// fundPo.setFundSrc1stFte(Double.parseDouble(fundVO.getFundSrc1stFte()));
							fundPo.setFundSrcFte(fundVo.getFundSrcFte());
						}
						else {
							fundPo.setFundSrcFte(null);
						}
						
						newFundingList.add(fundPo);
						fundPo.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
						
						if (a >= rp.getRequestFundingList().size()) {
							rp.addRequestFundingList(fundPo);
						}
					}
					
					// For Request funding inactive
					if (rp.getRequestFundingList().size() > createPositionWebVo.getRequestFundingList().size()) {
						for (int v=createPositionWebVo.getRequestFundingList().size(); v<rp.getRequestFundingList().size(); v++) {
							rp.getRequestFundingList().get(v).setRecState(MPRSConstant.MPRS_STATE_INACTIVE);
						}
					}

					// Converted Result
					System.out.println("====================================================");
					for (int g=0; g<rp.getRequestFundingList().size(); g++) {
						System.out.println("xxx: " + rp.getRequestFundingList().get(g).getAnnualPlanInd() + " / " + rp.getRequestFundingList().get(g).getRecState());
					}
					System.out.println("====================================================");
					
	
					System.out.println("--Resources Support Tab Saved --");
					RequestFundingResourcePo rfs = null;
					if (vo.getCurrentRequestNo() != null && !"".equals(vo.getCurrentRequestNo()) && i == 0) {
						rfs = rp.getRequestFundingResource();
					}
					else {
						rfs = new RequestFundingResourcePo();
					}
					rp.setRequestFundingResource(rfs);
					rfs.setResourcesSupportFrExt(createPositionWebVo.getRes_sup_fr_ext());
					rfs.setResourcesSupportRemark(createPositionWebVo.getRes_sup_remark());
	
					System.out.println("--Post Id Assignment Tab Saved --");
					boolean haveAnnualPlan = false;
					for (int k=0; k<annualPlanList.size(); k++) {
						if (!"".equals(annualPlanList.get(k)) && annualPlanList.get(k) != null) {
							haveAnnualPlan = true;
							break;
						}
					}
					
					if ("".equals(createPositionWebVo.getProposed_post_id())) {
						if (!"".equals(createPositionWebVo.getCluster()) && 
								!"".equals(createPositionWebVo.getInstitution()) && 
								!"".equals(createPositionWebVo.getDepartment()) && 
								!"".equals(createPositionWebVo.getRank()) && 
								haveAnnualPlan &&
								!"".equals(createPositionWebVo.getPostFTE())) {
	
							String postId = requestSvc.generateNewPostId(createPositionWebVo.getStaffGroup(),
									createPositionWebVo.getCluster(),
									createPositionWebVo.getInstitution(), 
									createPositionWebVo.getDepartment(),
									createPositionWebVo.getRank(),
									annualPlanList,
									programTypeList,
									createPositionWebVo.getPostDuration(),
									createPositionWebVo.getPostFTE(),
									needRecycle,
									tmpProposedPostId);
							
							rp.setProposedPostId(postId);
						}
					}
	
					newRequest.addRequestPosition(rp);
					System.out.println("XXXXXX=" + vo.getUploadFileId() + "=XXXXXXXX");
					List<RequestAttachmentPo> attachmentList = new ArrayList<RequestAttachmentPo>();
					if (vo.getUploadFileId() != null && !"".equals(vo.getUploadFileId())) {
						for (int q=0; q<vo.getUploadFileId().size(); q++) {
							if (vo.getUploadFileId().get(q) != null && !"".equals(vo.getUploadFileId().get(q))) {
								RequestTempFilePo tempFile = requestSvc.getTempFile(Integer.parseInt(vo.getUploadFileId().get(q)));
	
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
	
					if (vo.getCurrentRequestNo() != null && !"".equals(vo.getCurrentRequestNo()) && i == 0) { 
						System.out.println("perform update");
						requestSvc.update(newRequest, vo.getRemoveAttachmentUid(), vo.getUploadFileId(), user);
					}
					else {
						System.out.println("perform insert");
						requestNo = requestSvc.insert(newRequest, vo.getUploadFileId(), (i==(noOfCopySave-1)), sourceRequestUid, cloneAttachmentList, user);
						vo.setCurrentRequestNo(String.valueOf(requestNo));
					}
				}
	
				RequestPo requestCase = requestSvc.getRequestByRequestNo(requestNo);
	
				vo = convertToWebVo(requestCase, userId, currentRole);
	
				System.out.println("*ACTION SAVE SUCCESS*");
				vo.setDisplayMessage("Record update success.");
				vo.setUpdateSuccess("Y");
			}
			catch (Exception ex) {
				EClaimLogger.error("performUpdate - update:" + ex.getMessage(), ex);
				String errorMsg = doHandleException(ex.getCause(), ex.getMessage());
				
				if (vo.getCurrentRequestNo() != null && !"".equals(vo.getCurrentRequestNo())) {
					RequestPo requestCase = requestSvc.getRequestByRequestNo(Integer.parseInt(vo.getCurrentRequestNo()));
					vo = convertToWebVo(requestCase, userId, currentRole);
				}
				
				System.out.println("errorMsg: " + errorMsg);
				vo.setUpdateSuccess("N");
				vo.setDisplayMessage(errorMsg);
			}

			return new ModelAndView("request/newPost", "formBean", vo);

		} //else other form actions
		else if ("WITHDRAW".equalsIgnoreCase(formAction)) {
			System.out.println("Request No.: " + vo.getCurrentRequestNo());
			int requestNo = Integer.parseInt(vo.getCurrentRequestNo());
			String errorMsg = requestSvc.submitWithdraw(requestNo, "NEW", userId, currentRole);

			if ("".equals(errorMsg) || errorMsg == null) {
				// Retrieve again the request 
				RequestPo requestCase = requestSvc.getRequestByRequestNo(requestNo);

				vo.setRequestNo(String.valueOf(requestCase.getRequestNo()));
				vo.setRequestId(requestCase.getRequestId());
				vo.setRequestStatus(requestCase.getRequestStatus().getStatusCode());
				vo.setCurrentWFGroup(requestCase.getCurrentWFGroup());
				vo.setCurrentWFUser(requestCase.getCurrentWFUser());
				vo.setEffectiveDate(DateTimeHelper.formatDateToString(requestCase.getEffectFrom()));
				vo.setRequester(requestCase.getRequester());
				vo.setRequestPositionList(requestCase.getRequestPositionList());
				if (requestCase.getApprovalDate() != null) {
					vo.setApprovalDate(DateTimeHelper.formatDateToString(requestCase.getApprovalDate()));
				}
				vo.setApprovalReference(requestCase.getApprovalReference());
				vo.setApprovalRemark(requestCase.getApprovalRemark());
				
				requestCase = requestSvc.getRequestByRequestNo(requestNo);
				
				vo = convertToWebVo(requestCase, userId, currentRole);

				vo.setUpdateSuccess("Y");
				vo.setShowDetail("Y");
				vo.setDisplayMessage("Request withdraw successful.");
			}
			else {
				System.out.println("errorMsg: " + errorMsg);
				vo.setShowDetail("Y");
				vo.setUpdateSuccess("N");
				vo.setDisplayMessage(errorMsg);
			}

			return new ModelAndView("request/newPost", "formBean", vo);
		}
		else if ("SUBMIT".equalsIgnoreCase(formAction)) {
			System.out.println("Request No.: " + vo.getCurrentRequestNo());
			int requestNo = Integer.parseInt(vo.getCurrentRequestNo());
			
			// Perform update before submit
			RequestPo newRequest = null;
			requestNo = Integer.parseInt(vo.getCurrentRequestNo());
			newRequest = requestSvc.getRequestByRequestNo(Integer.parseInt(vo.getCurrentRequestNo()));
			
			if (!"".equals(createPositionWebVo.getPostStartDate()) && createPositionWebVo.getPostStartDate() != null) {
				newRequest.setEffectFrom(sdf.parse(createPositionWebVo.getPostStartDate()));
			}
			if (!"".equals(createPositionWebVo.getApprovalDate()) && createPositionWebVo.getApprovalDate() != null) {
				newRequest.setApprovalDate(sdf.parse(createPositionWebVo.getApprovalDate()));
			}
			newRequest.setApprovalReference(createPositionWebVo.getApprovalReference());
			newRequest.setApprovalRemark(createPositionWebVo.getApprovalRemark());
			
			System.out.println("Add Request Position!!");
			RequestPostPo rp = null;
			rp = newRequest.getRequestPositionList().get(0);
			
			String tmpProposedPostId = rp.getProposedPostId();
			boolean needRecycle = true;
			
			if (tmpProposedPostId != null) {
				
				// Original Proposed Post ID should be stored into CS_POST_ID_SEQ_REUSE,
				// if below fields changes
				if (rp.getClusterCode().equals(createPositionWebVo.getCluster()) && 
					rp.getInstCode().equals(createPositionWebVo.getInstitution()) &&
					rp.getDeptCode().equals(createPositionWebVo.getDepartment()) &&
					rp.getRank().getRankCode().equals(createPositionWebVo.getRank()) &&
					rp.getPostDuration().equals(createPositionWebVo.getPostDuration()) &&
					rp.getPostFTEType().equals(createPositionWebVo.getPostFTE())) {
					needRecycle = false;
				}
				System.out.println("needRecycle: " + needRecycle);
				
				if (needRecycle) {
					System.out.println("Recycle: " + tmpProposedPostId);
					requestSvc.recycleProposedPostId(rp.getStaffGroupCode(), rp.getPostDuration(), rp.getPostFTEType(), tmpProposedPostId, userId, currentRole);
				}
			}
			
			RankPo rank = new RankPo();
			rank.setRankCode(createPositionWebVo.getRank());
			rp.setRank(rank);
			rp.setClusterCode(createPositionWebVo.getCluster());
			rp.setInstCode(createPositionWebVo.getInstitution());
			rp.setStaffGroupCode(createPositionWebVo.getStaffGroup());
			rp.setDeptCode(createPositionWebVo.getDepartment());
			rp.setSubSpecialtyCode(createPositionWebVo.getSubSpecialty());
			
			// For HO Buy Service
			rp.setHoBuyServiceInd(createPositionWebVo.getHoBuyServiceInd());

			System.out.println("--Position Tab Saved --");
			rp.setUnit(createPositionWebVo.getUnit());
			rp.setPostTitle(createPositionWebVo.getPostTitle());
			rp.setPostDuration(createPositionWebVo.getPostDuration());
			rp.setLimitDurationType(createPositionWebVo.getLimitDurationType());
			if (!"".equals(createPositionWebVo.getPostStartDate()) && createPositionWebVo.getPostStartDate() != null) {
				rp.setPostStartDate(sdf.parse(createPositionWebVo.getPostStartDate()));//review
			}
			rp.setLimitDurationNo(createPositionWebVo.getLimitDurationNo());
			rp.setLimitDurationUnit(createPositionWebVo.getLimitDurationUnit());
			if (!"".equals(createPositionWebVo.getLimitDurationEndDate()) && createPositionWebVo.getLimitDurationEndDate() != null) {
				rp.setLimitDurationEndDate(sdf.parse(createPositionWebVo.getLimitDurationEndDate()));
			}
			rp.setPostRemark(createPositionWebVo.getPostRemark());
			rp.setPostFTEType(createPositionWebVo.getPostFTE());
			rp.setPostFTE(createPositionWebVo.getPostFTEValue());
			rp.setPostStatus(createPositionWebVo.getPositionStatus());
			if (!"".equals(createPositionWebVo.getPositionStartDate()) && createPositionWebVo.getPositionStartDate() != null) {
				rp.setPostStatusStartDate(sdf.parse(createPositionWebVo.getPositionStartDate()));
			}
			if (!"".equals(createPositionWebVo.getPositionEndDate()) && createPositionWebVo.getPositionEndDate() != null) {
				rp.setPostStatusEndDate(sdf.parse(createPositionWebVo.getPositionEndDate()));
			}
			rp.setClusterRef(createPositionWebVo.getClusterRefNo());
			rp.setClusterRemark(createPositionWebVo.getClusterRemark());
			rp.setHcmPositionId(createPositionWebVo.getHcmPositionId());
			
			// For Shortfall Post
			rp.setShortFallInd("N");
			if (!"".equals(createPositionWebVo.getShortFallPostId())) {
				rp.setShortFallPostId(createPositionWebVo.getShortFallPostId());
			}
			else {
				rp.setShortFallPostId("");
			}
			
			// Update the Fixed End Date if selected (Time-Limited) and Duration Period
			if (!"R".equals(createPositionWebVo.getPostDuration()) && "DURATION_PERIOD".equals(createPositionWebVo.getLimitDurationType())) {
				int duration = 0;
				if ("Y".equals(createPositionWebVo.getLimitDurationUnit())) {
					duration = createPositionWebVo.getLimitDurationNo() * 12;
				}
				else if ("M".equals(createPositionWebVo.getLimitDurationUnit())) {
					duration = createPositionWebVo.getLimitDurationNo();
				}
				
				rp.setLimitDurationEndDate(requestSvc.addMonth(sdf.parse(createPositionWebVo.getPostStartDate()), duration));
			}
			
			if ("R".equals(createPositionWebVo.getPostDuration())) {
				rp.setLimitDurationEndDate(null);
			}

			System.out.println("--Funding Details Tab --");
			int poSize = rp.getRequestFundingList().size();
			int voSize = createPositionWebVo.getRequestFundingList().size();
			System.out.println("poSize: " + poSize);
			System.out.println("voSize: " + voSize);
			
			List<RequestFundingPo> newFundingList = new ArrayList<RequestFundingPo>();
			List<String> annualPlanList = new ArrayList<String>();
			List<String> programTypeList = new ArrayList<String>();
			
			for (int a=0; a<voSize; a++) {
				RequestFundingWebVo fundVo = createPositionWebVo.getRequestFundingList().get(a);
				
				RequestFundingPo fundPo = null;
				if (a < rp.getRequestFundingList().size()) {
					fundPo = rp.getRequestFundingList().get(a);
				}
				else {
					fundPo = new RequestFundingPo();
				}
				
				
				fundPo.setFundingSeqNo(a+1);
				fundPo.setAnnualPlanInd(fundVo.getAnnualPlanInd());
				annualPlanList.add(fundVo.getAnnualPlanInd());
				fundPo.setInst(fundVo.getInst());
				fundPo.setSection(fundVo.getSection());
				fundPo.setAnalytical(fundVo.getAnalytical());
				fundPo.setFundSrcId(fundVo.getFundSrcId());
				fundPo.setFundSrcSubCatId(fundVo.getFundSrcSubCatId());
				fundPo.setProgramCode(fundVo.getProgramCode());
				fundPo.setProgramName(fundVo.getProgramName());
				fundPo.setProgramTypeCode(fundVo.getProgramTypeCode());
				programTypeList.add(fundVo.getProgramTypeCode());
				fundPo.setProgramYear(fundVo.getProgramYear());
				fundPo.setFundSrcRemark(fundVo.getFundSrcRemark());
				
				if (!"".equals(fundVo.getFundSrcStartDate())) {
					fundPo.setFundSrcStartDate(sdf.parse(fundVo.getFundSrcStartDate()));
				}
				else {
					fundPo.setFundSrcStartDate(null);
				}
				
				if (!"".equals(fundVo.getFundSrcEndDate())) {
					fundPo.setFundSrcEndDate(sdf.parse(fundVo.getFundSrcEndDate()));
				}
				else {
					fundPo.setFundSrcEndDate(null);
				}
				
				if (!"".equals(fundVo.getFundSrcFte())) {
					// fundPo.setFundSrc1stFte(Double.parseDouble(fundVO.getFundSrc1stFte()));
					fundPo.setFundSrcFte(fundVo.getFundSrcFte());
				}
				else {
					fundPo.setFundSrcFte(null);
				}
				
				newFundingList.add(fundPo);
				
				fundPo.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
				
				if (a >= rp.getRequestFundingList().size()) {
					rp.addRequestFundingList(fundPo);
				}
			}
			
			// For Request funding inactive
			if (rp.getRequestFundingList().size() > createPositionWebVo.getRequestFundingList().size()) {
				for (int v=createPositionWebVo.getRequestFundingList().size(); v<rp.getRequestFundingList().size(); v++) {
					rp.getRequestFundingList().get(v).setRecState(MPRSConstant.MPRS_STATE_INACTIVE);
				}
			}
			
			// Converted Result
			System.out.println("====================================================");
			for (int g=0; g<rp.getRequestFundingList().size(); g++) {
				System.out.println("xxx: " + rp.getRequestFundingList().get(g).getAnnualPlanInd() + " / " + rp.getRequestFundingList().get(g).getRecState());
			}
			System.out.println("====================================================");

			System.out.println("--Resources Support Tab Saved --");
			RequestFundingResourcePo rfs = rp.getRequestFundingResource();
			rp.setRequestFundingResource(rfs);
			rfs.setResourcesSupportFrExt(createPositionWebVo.getRes_sup_fr_ext());
			rfs.setResourcesSupportRemark(createPositionWebVo.getRes_sup_remark());

			System.out.println("--Post Id Assignment Tab Saved --");
			boolean haveAnnualPlan = false;
			for (int k=0; k<annualPlanList.size(); k++) {
				if (!"".equals(annualPlanList.get(k)) && annualPlanList.get(k) != null) {
					haveAnnualPlan = true;
					break;
				}
			}
			
			if ("".equals(createPositionWebVo.getProposed_post_id())) {
				if (!"".equals(createPositionWebVo.getCluster()) && 
						!"".equals(createPositionWebVo.getInstitution()) && 
						!"".equals(createPositionWebVo.getDepartment()) && 
						!"".equals(createPositionWebVo.getRank()) && 
						haveAnnualPlan &&
						!"".equals(createPositionWebVo.getPostFTE())) {

					String postId = requestSvc.generateNewPostId(createPositionWebVo.getStaffGroup(),
							createPositionWebVo.getCluster(),
							createPositionWebVo.getInstitution(), 
							createPositionWebVo.getDepartment(),
							createPositionWebVo.getRank(),
							annualPlanList,
							programTypeList,
							createPositionWebVo.getPostDuration(),
							createPositionWebVo.getPostFTE(),
							needRecycle,
							tmpProposedPostId);

					rp.setProposedPostId(postId);
				}
			}

			newRequest.addRequestPosition(rp);
			List<RequestAttachmentPo> attachmentList = new ArrayList<RequestAttachmentPo>();
			if (vo.getUploadFileId() != null && !"".equals(vo.getUploadFileId())) {
				for (int q=0; q<vo.getUploadFileId().size(); q++) {
					if (vo.getUploadFileId().get(q) != null && !"".equals(vo.getUploadFileId().get(q))) {
						RequestTempFilePo tempFile = requestSvc.getTempFile(Integer.parseInt(vo.getUploadFileId().get(q)));

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

			System.out.println("perform update");
			requestSvc.update(newRequest, vo.getRemoveAttachmentUid(), vo.getUploadFileId(), user);
			// Perform update before submit - End

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
				String url = appUrl + "/request/newPost?rq=" + requestNo + "&fromMail=Y";
				emailContent = emailContent.replaceAll("Please click here to review the request", "Please click <a href='" + url + "'>here</a> to review the request");
				generateEmail = "Y";
			}

			if ("Y".equals(vo.getSubmitWithEmail())) {
				RequestPo tmpCase = requestSvc.getRequestByRequestNo(requestNo);
				String postId = "";
				postId = tmpCase.getRequestPositionList().get(0).getProposedPostId();
				
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
				
				EmailTemplatePo emailTemplate = commonSvc.getTemplateByTemplateId("RQ_NEW_POST_SUBMIT", new String[] {String.valueOf(tmpCase.getRequestId()),
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
						UserPo tUser = securitySvc.findUser(vo.getNextWFUser());
						emailList = tUser.getEmail();
					} catch (Exception e) {
						EClaimLogger.error("performUpdate - send Email:" + e.getMessage(), e);
					}
				}
				else {
					List<UserPo> userList = securitySvc.getAllUserByGroupAndCluster(rp.getClusterCode(), rp.getInstCode(), vo.getNextWFGroup()) ;
					for (int i=0; i<userList.size(); i++) {
						if (userList.get(i).getEmail() != null) {
							if (!"".equals(emailList)) {
								emailList += ", ";
							}
							emailList += userList.get(i).getEmail();
						}
					}
				}
				EClaimLogger.info("New Post: emailList=" + emailList);
				
				// Get the email CC list
				String ccEmailList = "";
				List<String> ccEmail = new ArrayList<String>();
				
				if ("Y".equals(vo.getReturnCase())) { 
					List<UserPo> ccUserList = securitySvc.getReturnUserFromWorkflowHistoryAll(vo.getCurrentRequestNo(), vo.getNextWFGroup());
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
				else if ("HO_MG_OFFICER".equals(vo.getNextWFGroup())) {
					List<UserPo> ccUserList = securitySvc.getRelatedUserFromWorkflowHistoryAll(vo.getCurrentRequestNo()) ;
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
				}
				
				emailTo = emailList;
				emailCC = ccEmailList;
				emailTo = StrHelper.removeDuplicateEmail(emailTo);
				emailCC = StrHelper.removeDuplicateEmail(emailCC);
				emailTitle = emailTemplate.getTemplateTitle();
				String si = StrHelper.format(vo.getEmailSuppInfo().replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$"));
				emailContent = emailTemplate.getTemplateContent().replaceAll("<Supplementary Information / Comment>", si);
				String url = appUrl + "/request/newPost?rq=" + vo.getCurrentRequestNo() + "&fromMail=Y";
				emailContent = emailContent.replaceAll("Please click here to review the request", "Please click <a href='" + url + "'>here</a> to review the request");
			
				generateEmail = "Y";
			}

			System.out.println("emailContent: " + emailContent);


			String errorMsg = requestSvc.submitWorkflow(requestNo, "NEW",
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
				vo = convertToWebVo(requestCase, userId, currentRole);

				vo.setUpdateSuccess("Y");
				vo.setShowDetail("Y");
				vo.setDisplayMessage("Request submit successful.");
				
				requestCase = requestSvc.getRequestByRequestNo(requestNo);

				vo.setPostID(requestCase.getRequestPositionList().get(0).getPostId());
				vo.setRequestStatus(requestCase.getRequestStatus().getStatusCode());
			}
			else {
				System.out.println("errorMsg: " + errorMsg);
				vo.setUpdateSuccess("N");
				vo.setShowDetail("Y");
				vo.setDisplayMessage(errorMsg);
			}

//			vo.setUserName(user.getUserName());
//			
//			// Check whether the user have approval right
//			if (requestSvc.hasApprovalRight(MPRSConstant.REQUEST_TYPE_NEW_REQUEST, currentRole)) {
//				vo.setUserHaveApprovalRight("Y");
//			}

			return new ModelAndView("request/newPost", "formBean", vo);
		}
		else if ("APPROVE".equalsIgnoreCase(formAction)) {
			System.out.println("Request No. submit approve: " + vo.getCurrentRequestNo());
			try {
				int requestNo = Integer.parseInt(vo.getCurrentRequestNo());
	
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
					String url = appUrl + "/request/newPost?rq=" + vo.getCurrentRequestNo() + "&fromMail=Y";
					emailContent = emailContent.replaceAll("Please click here to review the request", "Please click <a href='" + url + "'>here</a> to review the request");
					generateEmail = "Y";
				}
	
				if ("Y".equals(vo.getSubmitWithEmail())) {
					RequestPo tmpCase = requestSvc.getRequestByRequestNo(requestNo);
					String postId = "";
					postId = tmpCase.getRequestPositionList().get(0).getProposedPostId();
					
					String submittedBy = user.getUserName();
					String submittedDate = DateTimeHelper.formatDateToString(new Date());
					String suppInfo = StrHelper.format(vo.getEmailSuppInfo());
					
					String receiver = "ALL";
					
					EmailTemplatePo emailTemplate = commonSvc.getTemplateByTemplateId("RQ_NEW_POST_CONFIRM", new String[] {String.valueOf(tmpCase.getRequestId()),
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
					
					List<UserPo> ccUserList = securitySvc.getRelatedUserFromWorkflowHistoryAll(vo.getCurrentRequestNo()) ;
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
					
					System.out.println("staffGroupCode: " + staffGroupCode);
					
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
					
					System.out.println("emailCC :" + emailCC);

					emailTitle = emailTemplate.getTemplateTitle();
					String si = StrHelper.format(vo.getEmailSuppInfo().replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$"));
					emailContent = emailTemplate.getTemplateContent().replaceAll("<Supplementary Information / Comment>", si);
					String url = appUrl + "/request/newPost?rq=" + vo.getCurrentRequestNo() + "&fromMail=Y";
					emailContent = emailContent.replaceAll("Please click here to review the request", "Please click <a href='" + url + "'>here</a> to review the request");
				
					generateEmail = "Y";
				}

				emailTo = StrHelper.removeDuplicateEmail(emailTo);
				emailCC = StrHelper.removeDuplicateEmail(emailCC);
	
				String errorMsg = requestSvc.submitApprove(requestNo, "NEW", 
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
					vo = convertToWebVo(requestCase, userId, currentRole);
					vo.setUpdateSuccess("Y");
					vo.setShowDetail("Y");
					vo.setDisplayMessage("Request approve successful.");
				}
				else {
					System.out.println("errorMsg: " + errorMsg);
					vo.setUpdateSuccess("N");
					vo.setShowDetail("Y");
					vo.setDisplayMessage(errorMsg);
				}
			}
			catch (Exception ex) {
				EClaimLogger.error("performUpdate - send Email:" + ex.getMessage(), ex);
				String errorMsg = doHandleException(ex.getCause(), ex.getMessage());
				System.out.println("errorMsg: " + errorMsg);

				// Retrieve again the request 
				RequestPo requestCase = requestSvc.getRequestByRequestNo(Integer.parseInt(vo.getCurrentRequestNo()));
				vo = convertToWebVo(requestCase, userId, currentRole);
				
				vo.setUpdateSuccess("N");
				vo.setDisplayMessage(errorMsg);
			}

			return new ModelAndView("request/newPost", "formBean", vo);
		}

		return new ModelAndView("request/newPost", "formBean", vo);
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
	
	/**** Dropdown ****/
	@ModelAttribute("groupList")
	public Map<String, String> getGroupList() {
		List<RolePo> groupList = securitySvc.getAllRole();

		Map<String, String> displayGroupList = new LinkedHashMap<String, String>();
		for (int i=0; i<groupList.size(); i++) {
			displayGroupList.put(groupList.get(i).getRoleId(), groupList.get(i).getRoleName());
		}
		return displayGroupList;
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
	
	private NewPostWebVo convertToWebVo(RequestPo requestCase, String userId, String currentRole) {
		System.out.println("requestCase.getRequestId(): " + requestCase.getRequestId());
		NewPostWebVo vo = new NewPostWebVo();
		
		System.out.println("aaaxxx: " + vo.getPostDuration() + "..." + vo.getPostFTE());
		vo.setCurrentLoginUser(userId);
		vo.setRequestNo(String.valueOf(requestCase.getRequestNo()));
		vo.setRequestId(requestCase.getRequestId());
		vo.setRequestStatus(requestCase.getRequestStatus().getStatusCode());
		vo.setRequester(requestCase.getRequester());
		vo.setCurrentWFGroup(requestCase.getCurrentWFGroup());
		vo.setCurrentWFUser(requestCase.getCurrentWFUser());
		
		if (requestCase.getEffectFrom()!=null) {
			vo.setPostStartDate(DateTimeHelper.formatDateToString(requestCase.getEffectFrom()));
		}
		if (requestCase.getApprovalDate()!=null) {
			vo.setApprovalDate(DateTimeHelper.formatDateToString(requestCase.getApprovalDate()));
		}
		vo.setRequestPositionList(requestCase.getRequestPositionList());
		if (requestCase.getApprovalDate() != null) {
			vo.setApprovalDate(DateTimeHelper.formatDateToString(requestCase.getApprovalDate()));
		}
		vo.setApprovalReference(requestCase.getApprovalReference());
		vo.setApprovalRemark(requestCase.getApprovalRemark());
		vo.setCurrentRequestNo(String.valueOf(requestCase.getRequestNo()));
		
		vo.setRank(requestCase.getRequestPositionList().get(0).getRank().getRankCode());
		vo.setPostID(requestCase.getRequestPositionList().get(0).getPostId());
		vo.setRequestStatus(requestCase.getRequestStatus().getStatusCode());
		vo.setCluster(requestCase.getRequestPositionList().get(0).getClusterCode());
		vo.setInstitution(requestCase.getRequestPositionList().get(0).getInstCode());
		
		// For HO Buy Service
		vo.setHoBuyServiceInd(requestCase.getRequestPositionList().get(0).getHoBuyServiceInd());
		
		System.out.println("--Position Tab Loaded --");
		vo.setUnit(requestCase.getRequestPositionList().get(0).getUnit());
		vo.setPostTitle(requestCase.getRequestPositionList().get(0).getPostTitle());
		vo.setPostDuration(requestCase.getRequestPositionList().get(0).getPostDuration());
		vo.setLimitDurationType(requestCase.getRequestPositionList().get(0).getLimitDurationType());
		if (requestCase.getRequestPositionList().get(0).getPostStartDate() != null) {
			vo.setPostStartDate(DateTimeHelper.formatDateToString(requestCase.getRequestPositionList().get(0).getPostStartDate()));
		}
		vo.setLimitDurationNo(requestCase.getRequestPositionList().get(0).getLimitDurationNo());
		vo.setLimitDurationUnit(requestCase.getRequestPositionList().get(0).getLimitDurationUnit());
		if (requestCase.getRequestPositionList().get(0).getLimitDurationEndDate() != null) {
			vo.setLimitDurationEndDate(DateTimeHelper.formatDateToString(requestCase.getRequestPositionList().get(0).getLimitDurationEndDate()));
			vo.setPostEndDate(DateTimeHelper.formatDateToString(requestCase.getRequestPositionList().get(0).getLimitDurationEndDate()));
		}
		vo.setPostRemark(requestCase.getRequestPositionList().get(0).getPostRemark());
		vo.setPostFTE(requestCase.getRequestPositionList().get(0).getPostFTEType());
		vo.setPostFTEValue(StrHelper.formatDecimal(requestCase.getRequestPositionList().get(0).getPostFTE()));
		vo.setPositionStatus(requestCase.getRequestPositionList().get(0).getPostStatus());
		if (requestCase.getRequestPositionList().get(0).getPostStatusStartDate() != null) {
			vo.setPositionStartDate(DateTimeHelper.formatDateToString(requestCase.getRequestPositionList().get(0).getPostStatusStartDate()));
		}
		if (requestCase.getRequestPositionList().get(0).getPostStatusEndDate() != null) {
			vo.setPositionEndDate(DateTimeHelper.formatDateToString(requestCase.getRequestPositionList().get(0).getPostStatusEndDate()));
		}
		vo.setClusterRefNo(requestCase.getRequestPositionList().get(0).getClusterRef());
		vo.setClusterRemark(requestCase.getRequestPositionList().get(0).getClusterRemark());
		vo.setSubSpecialty(requestCase.getRequestPositionList().get(0).getSubSpecialtyCode());
		vo.setShortFallPostId(requestCase.getRequestPositionList().get(0).getShortFallPostId());
		
		System.out.println("--Funding Details Loaded --");
		System.out.println("request: " + requestCase.getRequestPositionList().get(0).getRequestFundingList());
		
		if (requestCase.getRequestPositionList().get(0).getRequestFundingList() != null) {
			System.out.println("requestCase.getRequestPositionList().get(0).getRequestFundingList(): " + requestCase.getRequestPositionList().get(0).getRequestFundingList().size());
		}
		else {
			System.out.println("requestCase.getRequestPositionList().get(0).getRequestFundingList(): null");
		}
		
		vo.setRequestFundingList(convertFundingVo(requestCase.getRequestPositionList().get(0).getRequestFundingList()));

		System.out.println("--Resources Support Tab Loaded --");
		vo.setRes_sup_fr_ext(requestCase.getRequestPositionList().get(0).getRequestFundingResource().getResourcesSupportFrExt());
		vo.setRes_sup_remark(requestCase.getRequestPositionList().get(0).getRequestFundingResource().getResourcesSupportRemark());
		
		System.out.println("--Post Id Assignment Tab Loaded --");
		vo.setProposed_post_id(requestCase.getRequestPositionList().get(0).getProposedPostId());
		
		vo.setDepartment(requestCase.getRequestPositionList().get(0).getDeptCode());
		vo.setStaffGroup(requestCase.getRequestPositionList().get(0).getStaffGroupCode());
		vo.setStaffGroupDisplay(requestCase.getRequestPositionList().get(0).getStaffGroupCode());
		
		vo.setHcmPositionId(requestCase.getRequestPositionList().get(0).getHcmPositionId());

		String mgTeamInd = "N";
		String staffGroupCode = vo.getStaffGroup();
		String postDurationCode = vo.getPostDuration();
		String postFteType = vo.getPostFTE();
		
		System.out.println("Checking: " + staffGroupCode + "/" + postDurationCode + "/" + postFteType);
		if (PostConstant.POST_DUR_VALUE_RECURRENT.equals(postDurationCode) || 
				(PostConstant.POST_DUR_VALUE_TL_CONTRACT.equals(postDurationCode) && PostConstant.POST_FTE_TYPE_FULL_TIME.equals(postFteType))) {
			mgTeamInd = "Y";
		}

		vo.setMgTeamInd(mgTeamInd);

		// Get HCM Info
		HCMRecordPo hcmRecordPo = hcmSvc.getHCMRecordByPositionId(Integer.parseInt(requestCase.getRequestPositionList().get(0).getHcmPositionId()));
		System.out.println("hcmRecordPo: HcmPositionId=" + requestCase.getRequestPositionList().get(0).getHcmPositionId());
		if (hcmRecordPo != null) {
			vo.setRelatedHcmJob(hcmRecordPo.getJobName());
			vo.setRelatedHcmPostTitle(hcmRecordPo.getPositionTitle());
			vo.setRelatedHcmPostOrganization(hcmRecordPo.getPositionOrganization());
			vo.setRelatedHcmOrganization(hcmRecordPo.getOrganizationName());
			vo.setRelatedHcmUnitTeam(hcmRecordPo.getUnitTeam());
			vo.setRelatedHcmEffectiveStartDate(hcmRecordPo.getEffectiveStartDateStr());
			vo.setRelatedHcmEffectiveEndDate(hcmRecordPo.getEffectiveStartDateStr());
			vo.setRelatedHcmDateEffective(hcmRecordPo.getDateEffectiveStr());
			vo.setRelatedHcmFTE(String.valueOf(hcmRecordPo.getFte()));
			vo.setRelatedHcmHeadCount(String.valueOf(hcmRecordPo.getMaxPerson()));
			vo.setRelatedHcmPositionName(hcmRecordPo.getName());
			vo.setRelatedHcmHiringStatus(hcmRecordPo.getAvailabilityStatus());
			vo.setRelatedHcmType(hcmRecordPo.getPositionType());
		}
		else {
			vo.setRelatedHcmPositionName("Cannot get HCM related information");
		}
		System.out.println("vo.getMgTeamInd(): " + vo.getMgTeamInd());
		System.out.println("currentRole.equals(requestCase.getCurrentWFGroup()):" + currentRole + "/" + requestCase.getCurrentWFGroup());
		if ((userId.equals(requestCase.getCurrentWFUser()) && currentRole.equals(requestCase.getCurrentWFGroup())) 
				|| currentRole.equals(requestCase.getCurrentWFGroup())) {
			// Check whether the user have approval right
			if (requestSvc.hasApprovalRight(vo.getStaffGroup(), MPRSConstant.REQUEST_TYPE_NEW_REQUEST, vo.getRequestStatus(), currentRole, vo.getMgTeamInd())) {
				vo.setUserHaveApprovalRight("Y");
			}
			else {
				vo.setUserHaveApprovalRight("N");
			}

			vo.setUserHaveSaveRight("Y");
			vo.setUserHaveSubmitRight("Y");
		}
		else {
			vo.setUserHaveApprovalRight("N");
			vo.setUserHaveSaveRight("N");
			vo.setUserHaveSubmitRight("N");
		}
		
		vo.setShowDetail("Y");
		vo.setWithMassSave("N");
		
		// Update the last update date
		vo.setLastUpdateDate(DateTimeHelper.formatDateTimeToString(requestCase.getUpdatedDate()));
		vo.setRequestType(MPRSConstant.REQUEST_TYPE_NEW_REQUEST);
		
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
		System.out.println("Staff Group Code:" + staffGroupCode + "/" +MPRSConstant.REQUEST_TYPE_NEW_REQUEST + "/" +vo.getRequestStatus());
		RequestWorkflowRoutePo route = this.requestSvc.getDefaultRoute(staffGroupCode, MPRSConstant.REQUEST_TYPE_NEW_REQUEST, vo.getRequestStatus(), mgTeamInd);
		if (route != null) {
			vo.setSubmitButtonLabel(route.getActionName());
		}
		
		// Only HO MG Officer can edit the shortfall
		if ("HO_MG_OFFICER".equals(currentRole) || "HO_MG_MANAGER".equals(currentRole)) {
			if ("HO_MG_OFFICER".equals(currentRole)) {
				vo.setCanEditShortFall("Y");
			}
			else {
				vo.setCanEditShortFall("N");
			}
			vo.setShowEditShortFall("Y");
		}
		else {
			vo.setCanEditShortFall("N");
			vo.setShowEditShortFall("N");
		}
		
		if ((userId.equals(requestCase.getCurrentWFUser()) && currentRole.equals(requestCase.getCurrentWFGroup()))
				|| currentRole.equals(requestCase.getCurrentWFGroup())) {
			if ("HR_OFFICER".equals(currentRole)) {
				vo.setCanEditDetailInfo("Y");
				vo.setUserHaveWithdrawRight("Y");
				vo.setWithMassSave("Y");
				vo.setUserHaveCreationRight("Y");
			}
			else {
				vo.setCanEditDetailInfo("N");
				vo.setUserHaveWithdrawRight("N");
				vo.setWithMassSave("N");
				vo.setUserHaveCreationRight("N");
			}
	
			if ("FIN_OFFICER".equals(currentRole)) {
				vo.setCanEditFinancialInfo("Y");
			}
			else {
				vo.setCanEditFinancialInfo("N");
			}
			
			if ("Y".equals(vo.getCanEditDetailInfo()) || "Y".equals(vo.getCanEditFinancialInfo()) || "Y".equals(vo.getCanEditShortFall()) ) {
				vo.setUserHaveSaveRight("Y");
			}
			else {
				vo.setUserHaveSaveRight("N");
			}
		}
		else {
			vo.setUserHaveCreationRight("N");
			vo.setUserHaveSaveRight("N");
		}
		
		vo.setCurrentRole(currentRole);
		
		System.out.println("CanEditDetailInfo: " + vo.getCanEditDetailInfo());
		System.out.println("CanEditFinancialInfo: " + vo.getCanEditFinancialInfo());
				
		return vo;
	}

	private List<RequestFundingWebVo> convertFundingVo(List<RequestFundingPo> requestFundingList) {
		List<RequestFundingWebVo> result = new ArrayList<RequestFundingWebVo>();
		
		if (requestFundingList != null) {
			for (int i=0; i<requestFundingList.size(); i++) {
				RequestFundingWebVo vo = new RequestFundingWebVo();
				vo.setRequestPostFundingUid(requestFundingList.get(i).getRequestPostFundingUid());
				vo.setRequestPostUid(requestFundingList.get(i).getRequestPosition().getRequestPostId());
				vo.setAnnualPlanInd(requestFundingList.get(i).getAnnualPlanInd());
				vo.setInst(requestFundingList.get(i).getInst());
				vo.setSection(requestFundingList.get(i).getSection());
				vo.setAnalytical(requestFundingList.get(i).getAnalytical());
				vo.setProgramYear(requestFundingList.get(i).getProgramYear());
				vo.setProgramCode(requestFundingList.get(i).getProgramCode());
				vo.setProgramName(requestFundingList.get(i).getProgramName());
				vo.setProgramTypeCode(requestFundingList.get(i).getProgramTypeCode());
				vo.setFundSrcId(requestFundingList.get(i).getFundSrcId());
				vo.setFundSrcSubCatId(requestFundingList.get(i).getFundSrcSubCatId());
				vo.setFundSrcFte(requestFundingList.get(i).getFundSrcFte());
				vo.setFundSrcRemark(requestFundingList.get(i).getFundSrcRemark());
				vo.setFundSrcStartDate(requestFundingList.get(i).getFundSrcStartDateStr());
				vo.setFundSrcEndDate(requestFundingList.get(i).getFundSrcEndDateStr());
				result.add(vo);
			}
		}
		
		return result;
	}
	
	@ModelAttribute("shortFallIdList")
	public Map<String, String> getShortFallIdList() {
		List<String> shortFallList = requestSvc.getShortFallList();
		
		Map<String, String> displayList = new LinkedHashMap<String, String>();
		for (int i=0; i<shortFallList.size(); i++) {
			displayList.put(shortFallList.get(i), shortFallList.get(i));
		}
		return displayList;
	}
}
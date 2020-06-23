package hk.org.ha.eclaim.controller.request;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import hk.org.ha.eclaim.bs.request.constant.PostConstant;
import hk.org.ha.eclaim.bs.request.po.JsonResultResponse;
import hk.org.ha.eclaim.bs.request.po.JsonResultResponseWrapper;
import hk.org.ha.eclaim.bs.request.po.MPRSPostFundingResourceSnapPo;
import hk.org.ha.eclaim.bs.request.po.MPRSPostFundingSnapPo;
import hk.org.ha.eclaim.bs.request.po.MPRSPostSnapPo;
import hk.org.ha.eclaim.bs.request.po.MPRSSearchCriteria;
import hk.org.ha.eclaim.bs.request.po.PostMasterPo;
import hk.org.ha.eclaim.bs.request.po.RequestAttachmentPo;
import hk.org.ha.eclaim.bs.request.po.RequestPo;
import hk.org.ha.eclaim.bs.request.po.RequestPostPo;
import hk.org.ha.eclaim.bs.request.po.RequestTempFilePo;
import hk.org.ha.eclaim.bs.request.po.RequestWorkflowRoutePo;
import hk.org.ha.eclaim.bs.request.svc.IRequestSvc;
import hk.org.ha.eclaim.core.controller.BaseController;
import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.model.request.MPRSResultResponse;
import hk.org.ha.eclaim.model.request.PostFundingVo;
import hk.org.ha.eclaim.model.request.PostResponse;
import hk.org.ha.eclaim.model.request.PostResponseWrapper;
import hk.org.ha.eclaim.model.request.RequestPostVo;
import hk.org.ha.eclaim.bs.cs.po.PostDurationPo;
import hk.org.ha.eclaim.bs.cs.po.RankPo;
import hk.org.ha.eclaim.bs.cs.svc.ICommonSvc;
import hk.org.ha.eclaim.bs.hcm.po.HCMAssignmentPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMRecordPo;
import hk.org.ha.eclaim.bs.hcm.svc.IHCMSvc;
import hk.org.ha.eclaim.bs.security.po.DataAccessPo;
import hk.org.ha.eclaim.bs.security.po.RolePo;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.svc.ISecuritySvc;
import hk.org.ha.eclaim.core.helper.DateTimeHelper;
import hk.org.ha.eclaim.core.logger.EClaimLogger;

@Controller
public class RequestController extends BaseController {
	@Autowired
	IRequestSvc requestSvc;
	
	@Autowired
	IHCMSvc hcmSvc;
	
	@Autowired
	ISecuritySvc securitySvc;
	
	@Autowired
	ICommonSvc commonSvc;

	@Value("${attachment.requestTmpDir}")
	private String DOC_PATH;
	
	@Value("${attachment.requestAttachDir}")
	private String APPROVAL_DOC_PATH;
	
	@Value("${attachment.postAttachDir}")
	private String POST_DOC_PATH;
	
	@RequestMapping(value="/api/request/searchVacantPost")
	public @ResponseBody PostResponseWrapper searchVacantPost(@ModelAttribute MPRSSearchCriteria mprSearchRequest, 
																			HttpServletRequest request) {
		System.out.println("Location searchVacantPost");
		System.out.println("Cluster: " + mprSearchRequest.getSearchClusterId());
		
		String userId = this.getSessionUser(request).getUserId();
		String currentRole = (String)request.getSession().getAttribute("currentRole");

		int userRoleId = (int) request.getSession().getAttribute("currentUserRoleId");
		List<DataAccessPo> dataAccessList = securitySvc.getDataAccessByRoleId(userRoleId);
		System.out.println("userRoleId: " + userRoleId);
		
		List<PostMasterPo> posts = requestSvc.getVacancyMPRSPost(dataAccessList, mprSearchRequest, userId, currentRole);
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
				
				String annualPlanInd = "";
				for (int m=0; m<tmpPost.getPostFundingList().size(); m++) {
					if (m != 0) {
						annualPlanInd += "/";
					}
					
					if (tmpPost.getPostFundingList().get(m).getAnnualPlanInd() != null) {
						annualPlanInd += ("Y".equals(tmpPost.getPostFundingList().get(m).getAnnualPlanInd())?"Yes":"No");
					}
					else {
						annualPlanInd += "";
					}
				}
				
				postResponse.setAnnualPlanDesc(annualPlanInd);
				
				// postResponse.setAnnualPlanDesc(("Y".equals(tmpPost.getPostFunding().getAnnualPlanInd())?"Yes":"No"));
				postResponse.setPostDurationDesc(postDurationDesc);
				postResponse.setPostFte(tmpPost.getPostFTE());
				postResponse.setStaffGroupCode(tmpPost.getStaffGroupCode());
				postResponseList.add(postResponse);
				
			}
		}
		
		response.setPostResponse(postResponseList);
		response.setError("0");
		response.setErrorMsg("");
		System.out.println("Finish the Ajax Loading");

		return response;
	} 
	
	// Search Post for Create HCM Approver and Create New Post from Cloning
	@RequestMapping(value="/api/post/searchPost", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody PostResponseWrapper searchPost(@ModelAttribute MPRSSearchCriteria mprSearchRequest, 
																		 HttpServletRequest request) {
		System.out.println("Location searchPostDetailsForHcmApprover");
		System.out.println("Cluster : " + mprSearchRequest.getSearchClusterId());
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
			for (int i=0; i<posts.size(); i++) {
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
				
				String annualPlan = "";
				if (tmpPost.getPostFundingList() != null) {
					for (int q=0; q<tmpPost.getPostFundingList().size(); q++) {
						if (q != 0) {
							annualPlan += "/";
						}
						
						if (tmpPost.getPostFundingList().get(q).getAnnualPlanInd() != null) {
							annualPlan += ("Y".equals(tmpPost.getPostFundingList().get(q).getAnnualPlanInd())?"Yes":"No");
						}
						else {
							annualPlan += "";
						}
					}
				}
				
				postResponse.setAnnualPlanDesc(annualPlan);
				postResponse.setPostDurationDesc(postDurationDesc);
				postResponse.setPostFte(tmpPost.getPostFTE());
				postResponse.setHcmPositionId(tmpPost.getHcmPositionId());
				postResponse.setStaffGroupCode(tmpPost.getStaffGroupCode());
				postResponseList.add(postResponse);
			}
		}
		
		response.setPostResponse(postResponseList);
		response.setError("0");
		response.setErrorMsg("");
		System.out.println("Finish the Ajax Loading");

		return response;
	}
	
	@RequestMapping(value="/api/request/getPostDetails", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody MPRSResultResponse getPostDetails(@ModelAttribute MPRSSearchCriteria mprSearchRequest) throws Exception {
		System.out.println("###### RequestController.getPostDetails - Start ######");
		System.out.println("PostNo: " + mprSearchRequest.getSearchPostNo());
		PostMasterPo po = requestSvc.getPostByPostUid(Integer.parseInt(mprSearchRequest.getSearchPostNo()));
		List<RequestPo> upgradeRequestPo = requestSvc.getExistingRequestByMPRSPostNo(mprSearchRequest.getSearchPostNo(), MPRSConstant.REQUEST_TYPE_UPGRADE);
		List<RequestPo> upgradeRequestPo2 = requestSvc.getUpgradeRequestByPostId(mprSearchRequest.getSearchPostNo());
		System.out.println("upgradeRequestPo2: " + upgradeRequestPo2.size());
		List<RequestPo> extensionRequestPo = requestSvc.getExistingRequestByMPRSPostNo(mprSearchRequest.getSearchPostNo(), MPRSConstant.REQUEST_TYPE_EXTENSION);
		List<RequestPo> deletionRequestPo = requestSvc.getExistingRequestByMPRSPostNo(mprSearchRequest.getSearchPostNo(), MPRSConstant.REQUEST_TYPE_DELETION);
		List<RequestPo> changeFundingRequestPo = requestSvc.getExistingRequestByMPRSPostNo(mprSearchRequest.getSearchPostNo(), MPRSConstant.REQUEST_TYPE_CHANGE_FUNDING);
		List<RequestPo> suppPromotionRequestPo = requestSvc.getExistingRequestByMPRSPostNo(mprSearchRequest.getSearchPostNo(), MPRSConstant.REQUEST_TYPE_SUPPLEMENTARY_PROMOTION);
		List<RequestPo> frozenPostRequestPo = requestSvc.getExistingRequestByMPRSPostNo(mprSearchRequest.getSearchPostNo(), MPRSConstant.REQUEST_TYPE_FROZEN);
		List<RequestPo> changeStaffMixRequestPo = requestSvc.getExistingRequestByMPRSPostNo(mprSearchRequest.getSearchPostNo(), MPRSConstant.REQUEST_TYPE_CHANGE_STAFF_MIX);
		List<RequestPo> changeStaffMixRequestPo2 = requestSvc.getChangeStaffMixRequestByPostId(mprSearchRequest.getSearchPostNo());
		List<RequestPo> fteAdjustmentRequestPo = requestSvc.getExistingRequestByMPRSPostNo(mprSearchRequest.getSearchPostNo(), MPRSConstant.REQUEST_TYPE_FTE_ADJUSTMENT);
		
		List<HCMAssignmentPo> AssignmentPo = hcmSvc.getAllHCMAssignment(po.getPostId());
		
		MPRSResultResponse response = new MPRSResultResponse();
		//temp test info start
		if (po.getPostStartDate() != null) {
			System.out.println("post_start_date" + DateTimeHelper.formatDateToString(po.getPostStartDate()));
		}
		if (po.getLimitDurationEndDate() != null) {
			response.setLimitDurationEndDate("post_duration end_date" + DateTimeHelper.formatDateToString(po.getLimitDurationEndDate()));
		}
		
		//temp test info end
		response.setPostId(po.getPostId());
		
		// Convert the PostMasterPo to response
		response.setUnit(po.getUnit());
		response.setPostTitle(po.getPostTitle());
		response.setPostDuration(po.getPostDuration());
		response.setLimitDurationType(po.getLimitDurationType());
		if (po.getPostStartDate() != null) {
			response.setPostStartDate(DateTimeHelper.formatDateToString(po.getPostStartDate()));
		}
		response.setLimitDurationNo(po.getLimitDurationNo());
		response.setLimitDurationUnit(po.getLimitDurationUnit());
		if (po.getLimitDurationEndDate() != null) {
			response.setLimitDurationEndDate(DateTimeHelper.formatDateToString(po.getLimitDurationEndDate()));
		}
		response.setLimitDurationUnit(po.getLimitDurationUnit());
		response.setLimitDurationType(po.getLimitDurationType());
		response.setPostRemark(po.getPostRemark());
		response.setPostFTE(po.getPostFTEType());
		response.setPostFTEValue(String.valueOf(po.getPostFTE()));
		response.setPositionStatus(po.getPostStatus().getPostStatusCode());
		if (!"".equals(po.getPostStartDate()) && po.getPostStartDate() != null) {
			response.setPostStartDate(DateTimeHelper.formatDateToString(po.getPostStartDate()));
		}
		if (po.getPostStatusStartDate() != null) {
			response.setPositionStartDate(DateTimeHelper.formatDateToString(po.getPostStatusStartDate()));
		}
		if (po.getPostStatusEndDate() != null) {
			response.setPositionEndDate(DateTimeHelper.formatDateToString(po.getPostStatusEndDate()));
		}
		response.setClusterRefNo(po.getClusterRef());
		response.setClusterRemark(po.getClusterRemark());
		response.setSubSpecialty(po.getSubSpecialtyCode());

		// actualStartDate
		if (po.getActualStartDate() != null) {
			response.setPostActualStartDate(DateTimeHelper.formatDateToString(po.getActualStartDate()));
		}
		
		//Resource
		response.setRes_sup_fr_ext(po.getPostFundingResource().getResourceSupportFromExt());
		response.setRes_sup_remark(po.getPostFundingResource().getResourceSupportRemark());

		//History List
		//01, upgrade
		System.out.println("01, upgrade record: " + upgradeRequestPo.size());
		
		List<String> URequestIdList = new ArrayList<String>();
		List<String> URequestEffDateList = new ArrayList<String>();
		List<String> URequestFromToIndList = new ArrayList<String>();
		List<String> URequestPostIDList = new ArrayList<String>();
		List<String> URequestPostIDList2 = new ArrayList<String>();
		List<String> URequestDeptList = new ArrayList<String>();
		List<String> URequestRankList = new ArrayList<String>();
		List<String> URequestPositiontDurationTypeList = new ArrayList<String>();
		List<String> UPositionTypeList = new ArrayList<String>();
		List<String> URequestReason = new ArrayList<String>();
		
		response.setUpgradeListSize(upgradeRequestPo.size() + upgradeRequestPo2.size());
		
		if (upgradeRequestPo.size() != 0) {
			for (int i=0; i<upgradeRequestPo.size(); i++) {
				String postList1 = "";  // From Post
				String postList2 = "";  // To Post
				
				if (upgradeRequestPo.get(i).getRequestId() != null) {
					URequestIdList.add(upgradeRequestPo.get(i).getRequestId());
					System.out.println("  in: requestID" + upgradeRequestPo.get(i).getRequestId());
				}
				else {
					URequestIdList.add("");
				}
				
				if (upgradeRequestPo.get(i).getEffectFrom() != null) {
					URequestEffDateList.add(DateTimeHelper.formatDateToString(upgradeRequestPo.get(i).getEffectFrom()));
				}
				else {
					URequestEffDateList.add("");
				}
				
				if (upgradeRequestPo.get(i).getRequestPositionList() !=null) {
					for (int m=0; m<upgradeRequestPo.get(i).getRequestPositionList().size(); m++) {
						if ("Y".equals(upgradeRequestPo.get(i).getRequestPositionList().get(m).getFromPostInd())) {
							if (!"".equals(postList1)) {
								postList1 = postList1 + ", ";
							}
							
							postList1 = postList1 + upgradeRequestPo.get(i).getRequestPositionList().get(m).getPostId();
						}
						else {
							if (!"".equals(postList2)) {
								postList2 = postList2 + ", ";
							}
							
							postList2 = postList2 + upgradeRequestPo.get(i).getRequestPositionList().get(m).getPostId();
						}
					}
				}
				
				URequestPostIDList.add(postList1);
				URequestPostIDList2.add(postList2);
				
				if (upgradeRequestPo.get(i).getRequestReason() != null) {
					URequestReason.add(upgradeRequestPo.get(i).getRequestReason());
				}
				else {
					URequestReason.add("");
				}
			}
		}
		if (upgradeRequestPo2.size() != 0) {
			for (int i=0; i<upgradeRequestPo2.size(); i++) {
				String postList1 = "";
				String postList2 = "";
				
				if (upgradeRequestPo2.get(i).getRequestId() != null) {
					URequestIdList.add(upgradeRequestPo2.get(i).getRequestId());
					System.out.println("  in: requestID" + upgradeRequestPo2.get(i).getRequestId());
				}
				else {
					URequestIdList.add("");
				}
				
				if (upgradeRequestPo2.get(i).getEffectFrom() != null) {
					URequestEffDateList.add(DateTimeHelper.formatDateToString(upgradeRequestPo2.get(i).getEffectFrom()));
				}
				else {
					URequestEffDateList.add("");
				}
				
				if (upgradeRequestPo2.get(i).getRequestPositionList() !=null) {
					for (int m=0; m<upgradeRequestPo2.get(i).getRequestPositionList().size(); m++) {
						if ("Y".equals(upgradeRequestPo2.get(i).getRequestPositionList().get(m).getFromPostInd())) {
							if (!"".equals(postList1)) {
								postList1 = postList1 + ", ";
							}
							
							postList1 = postList1 + upgradeRequestPo2.get(i).getRequestPositionList().get(m).getPostId();
						}
						else {
							if (!"".equals(postList2)) {
								postList2 = postList2 + ", ";
							}
							
							postList2 = postList2 + upgradeRequestPo2.get(i).getRequestPositionList().get(m).getPostId();
						}
					}
				}
				
				URequestPostIDList.add(postList1);
				URequestPostIDList2.add(postList2);
				
				if (upgradeRequestPo2.get(i).getRequestReason() != null) {
					URequestReason.add(upgradeRequestPo2.get(i).getRequestReason());
				}
				else {
					URequestReason.add("");
				}
			}
		}
		response.setUpgradeHistRequestList_RequestID(URequestIdList);
		response.setUpgradeHistRequestList_RequestEffDate(URequestEffDateList);
		response.setUpgradeHistRequestList_RequestFromToInd(URequestFromToIndList);
		response.setUpgradeHistRequestList_RequestPostID(URequestPostIDList);
		response.setUpgradeHistRequestList_RequestDept(URequestDeptList);
		response.setUpgradeHistRequestList_RequestRank(URequestRankList);
		response.setUpgradeHistRequestList_RequestPositiontDurationType(URequestPositiontDurationTypeList);
		response.setUpgradeHistRequestList_PositionType(UPositionTypeList);
		response.setUpgradeHistRequestList_Reason(URequestReason);

			//02, extension
		System.out.println("02, extension record: " + extensionRequestPo.size());
		if (extensionRequestPo.size() != 0) {
			List<String> ERequestIdList = new ArrayList<String>();
			List<String> ERequestTranDateList = new ArrayList<String>();
			List<String> ERequestOrgStartDateList = new ArrayList<String>();
			List<String> ERequestRevisedStartDateList = new ArrayList<String>();
			List<String> ERequestOrgEndDateList = new ArrayList<String>();
			List<String> ERequestRevisedEndDateList = new ArrayList<String>();

			response.setExtensionListSize(extensionRequestPo.size());
			for (int i=0; i<extensionRequestPo.size(); i++) {
				if (extensionRequestPo.get(i).getRequestId() != null) {
					ERequestIdList.add(extensionRequestPo.get(i).getRequestId());
				}
				else {
					ERequestIdList.add("");
				}
				
				if (extensionRequestPo.get(i).getRequestDate() != null) {
					ERequestTranDateList.add(DateTimeHelper.formatDateToString(extensionRequestPo.get(i).getRequestDate()));
				}
				else {
					ERequestTranDateList.add("");
				}
				
				 
				for (int f=0; f<extensionRequestPo.get(i).getRequestPositionList().size(); f++) {
					if (extensionRequestPo.get(i).getRequestPositionList().get(f).getPostNo().equals(mprSearchRequest.getSearchPostNo())) {
						// Original From RQ_POST.POST_START_DATE
						if (extensionRequestPo.get(i).getRequestPositionList().get(f).getPostStartDate() != null) {
							ERequestOrgStartDateList.add(DateTimeHelper.formatDateToString(extensionRequestPo.get(i).getRequestPositionList().get(f).getPostStartDate()));
						}
						else {
							ERequestOrgStartDateList.add("");
						}
						
						// Original To LIMIT_DURATION_END_DATE
						if (extensionRequestPo.get(i).getRequestPositionList().get(f).getLimitDurationEndDate() != null) {
							ERequestOrgEndDateList.add(DateTimeHelper.formatDateToString(extensionRequestPo.get(i).getRequestPositionList().get(f).getLimitDurationEndDate()));
						}
						else {
							ERequestOrgEndDateList.add("");
						}
					}
				}
				
				// Revised From RQ_MASTER.EFFECTIVE_START_DATE
				if(extensionRequestPo.get(i).getEffectFrom() != null) {
					ERequestRevisedStartDateList.add(DateTimeHelper.formatDateToString(extensionRequestPo.get(i).getEffectFrom()));
				}
				else {
					ERequestRevisedStartDateList.add("");
				}
				
				// Revised To RQ_MASTER.EFFECTIVE_END_DATE
				if (extensionRequestPo.get(i).getEffectTo() != null) {
					ERequestRevisedEndDateList.add(DateTimeHelper.formatDateToString(extensionRequestPo.get(i).getEffectTo()));
				}
				else {
					ERequestRevisedEndDateList.add("");
				}
			}
			response.setExtensionHistRequestList_RequestID(ERequestIdList);
			response.setExtensionHistRequestList_RequestTransDate(ERequestTranDateList);
			response.setExtensionHistRequestList_RequestOrgStartDate(ERequestOrgStartDateList);
			response.setExtensionHistRequestList_RequestRevisedStartDate(ERequestRevisedStartDateList);
			response.setExtensionHistRequestList_RequestOrgEndDate(ERequestOrgEndDateList);
			response.setExtensionHistRequestList_RequestRevisedEndDate(ERequestRevisedEndDateList);
		}
		
		//03, Deletion
		System.out.println("03, Deletion record: " + deletionRequestPo.size());
		if (deletionRequestPo.size() != 0) {
			List<String> DRequestIdList = new ArrayList<String>();
			List<String> DRequestTranDateList = new ArrayList<String>();
			List<String> DRequestEffDateList = new ArrayList<String>();
			List<String> DRequestReasonList = new ArrayList<String>();

			response.setDeletionListSize(deletionRequestPo.size());
			for (int i=0; i<deletionRequestPo.size(); i++) {
				if (deletionRequestPo.get(i).getRequestId() != null) {
					DRequestIdList.add(deletionRequestPo.get(i).getRequestId());
				}
				else {
					DRequestIdList.add("");
				}
				
				if (deletionRequestPo.get(i).getRequestDate() != null) {
					DRequestTranDateList.add(DateTimeHelper.formatDateToString(deletionRequestPo.get(i).getRequestDate()));
				}
				else {
					DRequestTranDateList.add("");
				}
				
				if (deletionRequestPo.get(i).getEffectFrom() != null) {
					DRequestEffDateList.add(DateTimeHelper.formatDateToString(deletionRequestPo.get(i).getEffectFrom()));
				}
				else {
					DRequestEffDateList.add("");
				}
								
				if (deletionRequestPo.get(i).getRequestReason() != null) {
					DRequestReasonList.add(deletionRequestPo.get(i).getRequestReason());
				}
				else {
					DRequestReasonList.add("");
				}
			}
			response.setDeletionHistRequestList_RequestID(DRequestIdList);
			response.setDeletionHistRequestList_RequestEffDate(DRequestEffDateList);
			response.setDeletionHistRequestList_RequestTransDate(DRequestTranDateList);
			response.setDeletionHistRequestList_RequestReason(DRequestReasonList);
			
		}
		
		//04, ChangeFunding
		System.out.println("04, Change Funding: " + changeFundingRequestPo.size());
		if (changeFundingRequestPo.size() != 0) {
			List<String> FRequestIdList = new ArrayList<String>();
			List<String> FRequestTranDateList = new ArrayList<String>();
			List<String> FRequestReasonList = new ArrayList<String>();

			response.setChangeFundingListSize(changeFundingRequestPo.size());
			for (int i=0; i<changeFundingRequestPo.size(); i++) {
				if (changeFundingRequestPo.get(i).getRequestId() != null) {
					FRequestIdList.add(changeFundingRequestPo.get(i).getRequestId());
				}
				else {
					FRequestIdList.add("");
				}
				
				if (changeFundingRequestPo.get(i).getRequestDate() != null) {
					FRequestTranDateList.add(DateTimeHelper.formatDateToString(changeFundingRequestPo.get(i).getRequestDate()));
				}
				else {
					FRequestTranDateList.add("");
				}
				
				if (changeFundingRequestPo.get(i).getRequestReason() != null) {
					FRequestReasonList.add(changeFundingRequestPo.get(i).getRequestReason());
				}
				else {
					FRequestReasonList.add(""); 
				}
			}
			response.setChangeFundHistRequestList_RequestID(FRequestIdList);
			response.setChangeFundHistRequestList_RequestTransDate(FRequestTranDateList);
			response.setChangeFundHistRequestList_RequestReason(FRequestReasonList);
			
		}
		
		//05, Supp Promo
		System.out.println("05, Supp Promo record: " + suppPromotionRequestPo.size());
		if (suppPromotionRequestPo.size() != 0) {
			List<String> SRequestIdList = new ArrayList<String>();
			List<String> SRequestTranDateList = new ArrayList<String>();
			List<String> SRequestEffDateList = new ArrayList<String>();
			List<String> SRequestReasonList = new ArrayList<String>();

			response.setSuppPromotionListSize(suppPromotionRequestPo.size());
			for (int i=0; i<suppPromotionRequestPo.size(); i++) {
				if (suppPromotionRequestPo.get(i).getRequestId() != null) {
					SRequestIdList.add(suppPromotionRequestPo.get(i).getRequestId());
				}
				else {
					SRequestIdList.add("");
				}
				
				if (suppPromotionRequestPo.get(i).getRequestDate() != null) {
					SRequestTranDateList.add(DateTimeHelper.formatDateToString(suppPromotionRequestPo.get(i).getRequestDate()));
				}
				else {
					SRequestTranDateList.add("");
				}
				
				if (suppPromotionRequestPo.get(i).getEffectFrom() != null) {
					SRequestEffDateList.add(DateTimeHelper.formatDateToString(suppPromotionRequestPo.get(i).getEffectFrom()));
				}
				else {
					SRequestEffDateList.add("");
				}
								
				if (suppPromotionRequestPo.get(i).getRequestReason() != null) {
					SRequestReasonList.add(suppPromotionRequestPo.get(i).getRequestReason());
				}
				else {
					SRequestReasonList.add("");
				}
			}
			response.setSuppPromoHistRequestList_RequestID(SRequestIdList);
			response.setSuppPromoHistRequestList_RequestEffDate(SRequestEffDateList);
			response.setSuppPromoHistRequestList_RequestTransDate(SRequestTranDateList);
			response.setSuppPromoHistRequestList_RequestRemark(SRequestReasonList);
			
		}
		//06, Frozen
		System.out.println("06, Frozen record: " + frozenPostRequestPo.size());
		if (frozenPostRequestPo.size() != 0) {
			List<String> FzRequestIdList = new ArrayList<String>();
			List<String> FzRequestTranDateList = new ArrayList<String>();
			List<String> FzRequestEffDateList = new ArrayList<String>();
			List<String> FzRequestFzEndDateList = new ArrayList<String>();
			List<String> FzRequestReasonList = new ArrayList<String>();

			response.setFrozenListSize(frozenPostRequestPo.size());
			for (int i=0; i<frozenPostRequestPo.size(); i++) {
				if (frozenPostRequestPo.get(i).getRequestId() != null) {
					FzRequestIdList.add(frozenPostRequestPo.get(i).getRequestId());
				}
				else {
					FzRequestIdList.add("");
				}
				
				if (frozenPostRequestPo.get(i).getRequestDate() != null) {
					FzRequestTranDateList.add(DateTimeHelper.formatDateToString(frozenPostRequestPo.get(i).getRequestDate()));
				}
				else {
					FzRequestTranDateList.add("");
				}
				
				if (frozenPostRequestPo.get(i).getEffectFrom() != null) {
					FzRequestEffDateList.add(DateTimeHelper.formatDateToString(frozenPostRequestPo.get(i).getEffectFrom()));
				}
				else {
					FzRequestEffDateList.add("");
				}
				
				if (frozenPostRequestPo.get(i).getRequestPositionList().get(0).getPostStatusEndDate() != null) {
					FzRequestFzEndDateList.add(DateTimeHelper.formatDateToString(frozenPostRequestPo.get(i).getRequestPositionList().get(0).getPostStatusEndDate()));
				}
				else {
					FzRequestFzEndDateList.add("");
				}
								
				if (frozenPostRequestPo.get(i).getRequestReason() != null) {
					FzRequestReasonList.add(frozenPostRequestPo.get(i).getRequestReason());
				}
				else {
					FzRequestReasonList.add("");
				}
			}
			response.setFrozenHistRequestList_RequestID(FzRequestIdList);
			response.setFrozenHistRequestList_RequestEffDate(FzRequestEffDateList);
			response.setFrozenHistRequestList_RequestTransDate(FzRequestTranDateList);
			response.setFrozenHistRequestList_RequestFrozenEndDate(FzRequestFzEndDateList);
			response.setFrozenHistRequestList_RequestReason(FzRequestReasonList);
			
		}
		//07, ChangeStaffMix
		System.out.println("07, ChangeStaffMix: " + changeStaffMixRequestPo.size());
		List<String> SmRequestIdList = new ArrayList<String>();
		List<String> SmRequestEffDateList = new ArrayList<String>();
		List<String> SmRequestFromToIndList = new ArrayList<String>();
		List<String> SmRequestPostIDList = new ArrayList<String>();
		List<String> SmRequestPostIDList2 = new ArrayList<String>();
		List<String> SmRequestReasonList = new ArrayList<String>();
		

		response.setChangeStaffMixListSize(changeStaffMixRequestPo.size() + changeStaffMixRequestPo2.size());

		if (changeStaffMixRequestPo.size() != 0) {
			for (int i=0; i<changeStaffMixRequestPo.size(); i++) {
				if (changeStaffMixRequestPo.get(i).getRequestId() != null) {
					SmRequestIdList.add(changeStaffMixRequestPo.get(i).getRequestId());
					System.out.println("  in: requestID" + changeStaffMixRequestPo.get(i).getRequestId());
				}
				else {
					SmRequestIdList.add("");
				}
				
				if (changeStaffMixRequestPo.get(i).getEffectFrom() != null) {
					SmRequestEffDateList.add(DateTimeHelper.formatDateToString(changeStaffMixRequestPo.get(i).getEffectFrom()));
				}
				else {
					SmRequestEffDateList.add("");
				}
				
				String postList1 = "";
				String postList2 = "";
				
				if (changeStaffMixRequestPo.get(i).getRequestPositionList() !=null) {
					System.out.println("changeStaffMixRequestPo.get(i).getRequestPositionList().size(): " + changeStaffMixRequestPo.get(i).getRequestPositionList().size());
					for (int m=0; m<changeStaffMixRequestPo.get(i).getRequestPositionList().size(); m++) {
						if ("Y".equals(changeStaffMixRequestPo.get(i).getRequestPositionList().get(m).getFromPostInd())) {
							if (!"".equals(postList1)) {
								postList1 = postList1 + ", ";
							}
							
							postList1 = postList1 + changeStaffMixRequestPo.get(i).getRequestPositionList().get(m).getPostId();
						}
						else {
							if (!"".equals(postList2)) {
								postList2 = postList2 + ", ";
							}
							
							postList2 = postList2 + changeStaffMixRequestPo.get(i).getRequestPositionList().get(m).getPostId();
						}
					}
				}
				
				SmRequestPostIDList.add(postList1);
				SmRequestPostIDList2.add(postList2);
				
				if (changeStaffMixRequestPo.get(i).getRequestReason() != null) {
					SmRequestReasonList.add(changeStaffMixRequestPo.get(i).getRequestReason());
				}
				else {
					SmRequestReasonList.add("");
				}
				
			}
			
		}
		
		if (changeStaffMixRequestPo2.size() != 0) {
			for (int i=0; i<changeStaffMixRequestPo2.size(); i++) {
				if (changeStaffMixRequestPo2.get(i).getRequestId() != null) {
					SmRequestIdList.add(changeStaffMixRequestPo2.get(i).getRequestId());
					System.out.println("  in: requestID" + changeStaffMixRequestPo2.get(i).getRequestId());
				}
				else {
					SmRequestIdList.add("");
				}

				if (changeStaffMixRequestPo2.get(i).getEffectFrom() != null) {
					SmRequestEffDateList.add(DateTimeHelper.formatDateToString(changeStaffMixRequestPo2.get(i).getEffectFrom()));
				}
				else {
					SmRequestEffDateList.add("");
				}

				String postList1 = "";
				String postList2 = "";
				
				if (changeStaffMixRequestPo2.get(i).getRequestPositionList() !=null) {
					for (int m=0; m<changeStaffMixRequestPo2.get(i).getRequestPositionList().size(); m++) {
						if ("Y".equals(changeStaffMixRequestPo2.get(i).getRequestPositionList().get(m).getFromPostInd())) {
							if (!"".equals(postList1)) {
								postList1 = postList1 + ", ";
							}
							
							postList1 = postList1 + changeStaffMixRequestPo2.get(i).getRequestPositionList().get(m).getPostId();
						}
						else {
							if (!"".equals(postList2)) {
								postList2 = postList2 + ", ";
							}
							
							postList2 = postList2 + changeStaffMixRequestPo2.get(i).getRequestPositionList().get(m).getPostId();
						}
					}
				}
				
				SmRequestPostIDList.add(postList1);
				SmRequestPostIDList2.add(postList2);
				
				if (changeStaffMixRequestPo2.get(i).getRequestReason() != null) {
					SmRequestReasonList.add(changeStaffMixRequestPo2.get(i).getRequestReason());
				}
				else {
					SmRequestReasonList.add("");
				}
			}
		}
		
		response.setStaffMixHistRequestList_RequestID(SmRequestIdList);
		response.setStaffMixHistRequestList_RequestEffDate(SmRequestEffDateList);
		response.setStaffMixHistRequestList_RequestFromToInd(SmRequestFromToIndList);
		response.setStaffMixHistRequestList_RequestPostID(SmRequestPostIDList);
		response.setStaffMixHistRequestList_RequestPostID2(SmRequestPostIDList2);
		response.setStaffMixHistRequestList_Reason(SmRequestReasonList);
		
		// FTE Adjustment
		System.out.println("FTE Adjustment: " + fteAdjustmentRequestPo.size());
		if (fteAdjustmentRequestPo.size() != 0) {
			List<String> fteAdjustRequestIdList = new ArrayList<String>();
			List<String> fteAdjustRequestTranDateList = new ArrayList<String>();
			List<String> fteAdjustRequestReasonList = new ArrayList<String>();

			response.setFteAdjustmentListSize(fteAdjustmentRequestPo.size());
			for (int i=0; i<fteAdjustmentRequestPo.size(); i++) {
				if (fteAdjustmentRequestPo.get(i).getRequestId() != null) {
					fteAdjustRequestIdList.add(fteAdjustmentRequestPo.get(i).getRequestId());
				}
				else {
					fteAdjustRequestIdList.add("");
				}
				
				if (fteAdjustmentRequestPo.get(i).getRequestDate() != null) {
					fteAdjustRequestTranDateList.add(DateTimeHelper.formatDateToString(fteAdjustmentRequestPo.get(i).getRequestDate()));
				}
				else {
					fteAdjustRequestTranDateList.add("");
				}
				
				if (fteAdjustmentRequestPo.get(i).getRequestReason() != null) {
					fteAdjustRequestReasonList.add(fteAdjustmentRequestPo.get(i).getRequestReason());
				}
				else {
					fteAdjustRequestReasonList.add("");
				}
			}
			response.setFteAdjustmentHistRequestList_RequestID(fteAdjustRequestIdList);
			response.setFteAdjustmentHistRequestList_RequestTransDate(fteAdjustRequestTranDateList);
			response.setFteAdjustmentHistRequestList_RequestReason(fteAdjustRequestReasonList);
		}
		
		
		//09, Assignment
		System.out.println("09, Assignment: " + AssignmentPo.size());
		if (AssignmentPo.size() != 0) {
			List<String> assignmentPositionId = new ArrayList<String>();
			List<String> assignmentAssignmentNumber = new ArrayList<String>();
			List<String> assignmentEmpName = new ArrayList<String>();
			List<String> assignmentRank = new ArrayList<String>();
			List<String> assignmentEmpType = new ArrayList<String>();
			List<String> assignmentEmpPositionTitle = new ArrayList<String>();
			List<String> assignmentFTE = new ArrayList<String>();
			List<String> assignmentEffStartDate = new ArrayList<String>();
			List<String> assignmentEffEndDate = new ArrayList<String>();
			List<String> assignmentLeaveReason = new ArrayList<String>();

			response.setAssignmentListSize(AssignmentPo.size());
			for (int i=0; i<AssignmentPo.size(); i++) {
				if (AssignmentPo.get(i).getAssignmentNumber() != null) {
					assignmentAssignmentNumber.add(AssignmentPo.get(i).getAssignmentNumber());
				}
				else {
					assignmentAssignmentNumber.add("");
				}
				
				if (AssignmentPo.get(i).getPositionId() != null) {
					assignmentPositionId.add(String.valueOf(AssignmentPo.get(i).getPositionId()));
				}
				else {
					assignmentPositionId.add("");
				}
				
				if (AssignmentPo.get(i).getFullName() != null) {
					assignmentEmpName.add(AssignmentPo.get(i).getFullName());
				}
				else {
					assignmentEmpName.add("");
				}
				
				if (AssignmentPo.get(i).getRank() != null) {
					assignmentRank.add(AssignmentPo.get(i).getRank());
				}
				else {
					assignmentRank.add("");
				}
				
				if (AssignmentPo.get(i).getEmploymentType() != null) {
					assignmentEmpType.add(AssignmentPo.get(i).getEmploymentType());
				}
				else {
					assignmentEmpType.add("");
				}
				
				if (AssignmentPo.get(i).getPositionTitle() != null) {
					assignmentEmpPositionTitle.add(AssignmentPo.get(i).getPositionTitle());
				}
				else {
					assignmentEmpPositionTitle.add("");
				}
				
				if (AssignmentPo.get(i).getFte() != null) { 
					assignmentFTE.add(String.valueOf(AssignmentPo.get(i).getFte()));
				}
				else {
					assignmentFTE.add("");
				}
				
				if (AssignmentPo.get(i).getEffectiveStartDate() != null) {
					assignmentEffStartDate.add(DateTimeHelper.formatDateToString(AssignmentPo.get(i).getEffectiveStartDate()));
				}
				else {
					assignmentEffStartDate.add("");
				}
				
				if (AssignmentPo.get(i).getEffectiveEndDate() != null) {
					assignmentEffEndDate.add(DateTimeHelper.formatDateToString(AssignmentPo.get(i).getEffectiveEndDate()));
				}
				else {
					assignmentEffEndDate.add("");
				}
				
				if (AssignmentPo.get(i).getReasonOfLeaving() != null) { 
					assignmentLeaveReason.add(String.valueOf(AssignmentPo.get(i).getReasonOfLeaving()));
				}
				else {
					assignmentLeaveReason.add("");
				}
				
			}
			response.setAssignmentHistList_PositionID(assignmentPositionId);
			response.setAssignmentHistList_EmpName(assignmentEmpName);
			response.setAssignmentHistList_Rank(assignmentRank);
			response.setAssignmentHistList_EmpType(assignmentEmpType);
			response.setAssignmentHistList_EmpPositionTitle(assignmentEmpPositionTitle);
			response.setAssignmentHistList_FTE(assignmentFTE);
			response.setAssignmentHistList_StartDate(assignmentEffStartDate);
			response.setAssignmentHistList_EndDate(assignmentEffEndDate);
			response.setAssignmentHistList_LeaveReason(assignmentLeaveReason);
			response.setAssignmentHistList_AssignmentNumber(assignmentAssignmentNumber);
		}
		
		response.setHcmPositionId(po.getHcmPositionId());
		response.setClusterCode(po.getClusterCode());
		response.setInstCode(po.getInstCode());
		response.setDeptCode(po.getDeptCode());
		response.setRankCode(po.getRankCode());
		response.setStaffGroupCode(po.getStaffGroupCode());
		
		HCMRecordPo hcmRecordPo = hcmSvc.getHCMRecordByPositionId(Integer.parseInt(po.getHcmPositionId()));
		if (hcmRecordPo != null) {
			response.setRelatedHcmEffectiveStartDate(hcmRecordPo.getEffectiveStartDateStr());
			response.setRelatedHcmFTE(String.valueOf(hcmRecordPo.getFte()));
			response.setRelatedHcmHeadCount(String.valueOf(hcmRecordPo.getMaxPerson()));
			response.setRelatedHcmPositionName(hcmRecordPo.getName());
			response.setRelatedHcmHiringStatus(hcmRecordPo.getAvailabilityStatus());
			response.setRelatedHcmType(hcmRecordPo.getPositionType());
		}
		
		List<PostFundingVo> fundingList = new ArrayList<PostFundingVo>();
		for (int i=0; i<po.getPostFundingList().size(); i++) {
			PostFundingVo tmpFund = new PostFundingVo();
			tmpFund.setAnnualPlanInd(po.getPostFundingList().get(i).getAnnualPlanInd()==null?"":po.getPostFundingList().get(i).getAnnualPlanInd());
			tmpFund.setProgramYear(po.getPostFundingList().get(i).getProgramYear()==null?"":po.getPostFundingList().get(i).getProgramYear());
			tmpFund.setProgramCode(po.getPostFundingList().get(i).getProgramCode()==null?"":po.getPostFundingList().get(i).getProgramCode());
			tmpFund.setProgramName(po.getPostFundingList().get(i).getProgramName()==null?"":po.getPostFundingList().get(i).getProgramName());
			tmpFund.setProgramTypeCode(po.getPostFundingList().get(i).getProgramTypeCode()==null?"":po.getPostFundingList().get(i).getProgramTypeCode());
			tmpFund.setFundSrcId(po.getPostFundingList().get(i).getFundSrcId()==null?"":po.getPostFundingList().get(i).getFundSrcId());
			tmpFund.setFundSrcSubCatId(po.getPostFundingList().get(i).getFundSrcSubCatId()==null?"":po.getPostFundingList().get(i).getFundSrcSubCatId());
			tmpFund.setFundSrcStartDate(po.getPostFundingList().get(i).getFundSrcStartDate()==null?"":DateTimeHelper.formatDateToString(po.getPostFundingList().get(i).getFundSrcStartDate()));
			tmpFund.setFundSrcEndDate(po.getPostFundingList().get(i).getFundSrcEndDate()==null?"":DateTimeHelper.formatDateToString(po.getPostFundingList().get(i).getFundSrcEndDate()));
			tmpFund.setFundSrcFte(po.getPostFundingList().get(i).getFundSrcFte()==null?"":String.valueOf(po.getPostFundingList().get(i).getFundSrcFte()));
			tmpFund.setFundSrcRemark(po.getPostFundingList().get(i).getFundSrcRemark()==null?"":po.getPostFundingList().get(i).getFundSrcRemark());
			tmpFund.setInst(po.getPostFundingList().get(i).getInst()==null?"":po.getPostFundingList().get(i).getInst());
			tmpFund.setSection(po.getPostFundingList().get(i).getSection()==null?"":po.getPostFundingList().get(i).getSection());
			tmpFund.setAnalytical(po.getPostFundingList().get(i).getAnalytical()==null?"":po.getPostFundingList().get(i).getAnalytical());
			
			fundingList.add(tmpFund);
		}
		
		response.setFundingList(fundingList);
				
		System.out.println("Finish the Ajax Loading");

		return response;
	}
	
	@RequestMapping(value="/api/request/getPostDetailsWithSnap", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody MPRSResultResponse getPostDetailsWithSnap(@ModelAttribute MPRSSearchCriteria mprSearchRequest) throws Exception {
		System.out.println("Location RequestController.getPostDetailsWithSnap");
		System.out.println("PostNo: " + mprSearchRequest.getSearchPostNo());
		MPRSPostSnapPo po = requestSvc.getMPRSPostSnapDetail(Integer.parseInt(mprSearchRequest.getSearchPostSnapUid()));
		List<RequestPo> upgradeRequestPo = requestSvc.getExistingRequestByMPRSPostNo(mprSearchRequest.getSearchPostNo(), MPRSConstant.REQUEST_TYPE_UPGRADE);
		List<RequestPo> upgradeRequestPo2 = requestSvc.getUpgradeRequestByPostId(mprSearchRequest.getSearchPostNo());
		System.out.println("upgradeRequestPo2: " + upgradeRequestPo2.size());
		List<RequestPo> extensionRequestPo = requestSvc.getExistingRequestByMPRSPostNo(mprSearchRequest.getSearchPostNo(), MPRSConstant.REQUEST_TYPE_EXTENSION);
		List<RequestPo> deletionRequestPo = requestSvc.getExistingRequestByMPRSPostNo(mprSearchRequest.getSearchPostNo(), MPRSConstant.REQUEST_TYPE_DELETION);
		List<RequestPo> changeFundingRequestPo = requestSvc.getExistingRequestByMPRSPostNo(mprSearchRequest.getSearchPostNo(), MPRSConstant.REQUEST_TYPE_CHANGE_FUNDING);
		List<RequestPo> suppPromotionRequestPo = requestSvc.getExistingRequestByMPRSPostNo(mprSearchRequest.getSearchPostNo(), MPRSConstant.REQUEST_TYPE_SUPPLEMENTARY_PROMOTION);
		List<RequestPo> frozenPostRequestPo = requestSvc.getExistingRequestByMPRSPostNo(mprSearchRequest.getSearchPostNo(), MPRSConstant.REQUEST_TYPE_FROZEN);
		List<RequestPo> changeStaffMixRequestPo = requestSvc.getExistingRequestByMPRSPostNo(mprSearchRequest.getSearchPostNo(), MPRSConstant.REQUEST_TYPE_CHANGE_STAFF_MIX);
		List<RequestPo> changeStaffMixRequestPo2 = requestSvc.getChangeStaffMixRequestByPostId(mprSearchRequest.getSearchPostNo());
		List<RequestPo> fteAdjustmentRequestPo = requestSvc.getExistingRequestByMPRSPostNo(mprSearchRequest.getSearchPostNo(), MPRSConstant.REQUEST_TYPE_FTE_ADJUSTMENT);
		RequestPo newRequestPo = requestSvc.getNewRequestByPostId(po.getPostId());
		List<HCMAssignmentPo> AssignmentPo = hcmSvc.getAllHCMAssignment(po.getPostId());
		
		// Prepared the Attachment information
		List<RequestAttachmentPo> attachmentList = new ArrayList<RequestAttachmentPo>(); 
		
		List<MPRSPostFundingSnapPo> poFundingList = requestSvc.getMPRSPostFundingSnapDetail(Integer.parseInt(mprSearchRequest.getSearchPostSnapUid()));
		MPRSPostFundingResourceSnapPo poFundingResource = requestSvc.getMPRSPostFundingResourceSnapDetail(Integer.parseInt(mprSearchRequest.getSearchPostSnapUid()));
		MPRSResultResponse response = new MPRSResultResponse();

		//temp test info start
		if (po.getPostStartDate() != null) {
			System.out.println("post_start_date" + DateTimeHelper.formatDateToString(po.getPostStartDate()));
		}
		if (po.getLimitDurationEndDate() != null) {
			response.setLimitDurationEndDate("post_duration end_date" + DateTimeHelper.formatDateToString(po.getLimitDurationEndDate()));
		}
		
		//temp test info end
		
		response.setPostId(po.getPostId());
		
		// Convert the PostMasterPo to response
		response.setUnit(po.getUnit());
		response.setPostTitle(po.getPostTitle());
		response.setPostDuration(po.getPostDuration());
		response.setLimitDurationType(po.getLimitDurationType());
		if (po.getPostStartDate() != null) {
			response.setPostStartDate(DateTimeHelper.formatDateToString(po.getPostStartDate()));
		}
		response.setLimitDurationNo(po.getLimitDurationNo());
		response.setLimitDurationUnit(po.getLimitDurationUnit());
		if (po.getLimitDurationEndDate() != null) {
			response.setLimitDurationEndDate(DateTimeHelper.formatDateToString(po.getLimitDurationEndDate()));
		}
		response.setLimitDurationUnit(po.getLimitDurationUnit());
		response.setLimitDurationType(po.getLimitDurationType());
//		private String limitDurationEndDate;
		response.setPostRemark(po.getPostRemark());
		
		response.setPostFTE(po.getPostFTEType());
		response.setPostFTEValue(String.valueOf(po.getPostFTE()));
		response.setPositionStatus(po.getPostStatus().getPostStatusCode());
		if (!"".equals(po.getPostStartDate()) && po.getPostStartDate() != null) {
			response.setPostStartDate(DateTimeHelper.formatDateToString(po.getPostStartDate()));
		}
		if (po.getPostStatusStartDate() != null) {
			response.setPositionStartDate(DateTimeHelper.formatDateToString(po.getPostStatusStartDate()));
		}
		if (po.getPostStatusEndDate() != null) {
			response.setPositionEndDate(DateTimeHelper.formatDateToString(po.getPostStatusEndDate()));
		}
		response.setClusterRefNo(po.getClusterRef());
		response.setClusterRemark(po.getClusterRemark());

		List<PostFundingVo> fundingList = new ArrayList<PostFundingVo>();
		for (int i=0; i<poFundingList.size(); i++) {
			PostFundingVo tmpFund = new PostFundingVo();
			tmpFund.setAnnualPlanInd(poFundingList.get(i).getAnnualPlanInd()==null?"":poFundingList.get(i).getAnnualPlanInd());
			tmpFund.setProgramYear(poFundingList.get(i).getProgramYear()==null?"":poFundingList.get(i).getProgramYear());
			tmpFund.setProgramCode(poFundingList.get(i).getProgramCode()==null?"":poFundingList.get(i).getProgramCode());
			tmpFund.setProgramName(poFundingList.get(i).getProgramName()==null?"":poFundingList.get(i).getProgramName());
			tmpFund.setProgramTypeCode(poFundingList.get(i).getProgramTypeCode()==null?"":poFundingList.get(i).getProgramTypeCode());
			tmpFund.setFundSrcId(poFundingList.get(i).getFundSrcId()==null?"":poFundingList.get(i).getFundSrcId());
			tmpFund.setFundSrcSubCatId(poFundingList.get(i).getFundSrcSubCatId()==null?"":poFundingList.get(i).getFundSrcSubCatId());
			tmpFund.setFundSrcStartDate(poFundingList.get(i).getFundSrcStartDate()==null?"":DateTimeHelper.formatDateToString(poFundingList.get(i).getFundSrcStartDate()));
			tmpFund.setFundSrcEndDate(poFundingList.get(i).getFundSrcEndDate()==null?"":DateTimeHelper.formatDateToString(poFundingList.get(i).getFundSrcEndDate()));
			tmpFund.setFundSrcFte(poFundingList.get(i).getFundSrcFte()==null?"":String.valueOf(poFundingList.get(i).getFundSrcFte()));
			tmpFund.setFundSrcRemark(poFundingList.get(i).getFundSrcRemark()==null?"":poFundingList.get(i).getFundSrcRemark());
			tmpFund.setInst(poFundingList.get(i).getInst()==null?"":poFundingList.get(i).getInst());
			tmpFund.setSection(poFundingList.get(i).getSection()==null?"":poFundingList.get(i).getSection());
			tmpFund.setAnalytical(poFundingList.get(i).getAnalytical()==null?"":poFundingList.get(i).getAnalytical());
			
			fundingList.add(tmpFund);
		}
		
		response.setFundingList(fundingList);
		
		
		// response.setProgramType(poFunding.getProgramTypeCode());
		// response.setFund_remark(poFunding.getFundSrcRemark());
//		
//		//Resource
		response.setRes_sup_fr_ext(poFundingResource.getResourceSupportFromExt());
		response.setRes_sup_remark(poFundingResource.getResourceSupportRemark());
		
		response.setSubSpecialty(po.getSubSpecialtyCode());
		response.setRankCode(po.getRankCode());
		System.out.println("response.setSubSpecialty: " + response.getSubSpecialty());
		System.out.println("response.getRankId: " + response.getRankCode());
		
		// actualStartDate
		if (po.getActualStartDate() != null) {
			response.setPostActualStartDate(DateTimeHelper.formatDateToString(po.getActualStartDate()));
		}
		
		// Get the HCM Position information
		HCMRecordPo hcmRecordPo = hcmSvc.getHCMRecordByPositionId(Integer.parseInt(po.getHcmPositionId()), mprSearchRequest.getSearchEffectiveDate());
		System.out.println("hcmRecordPo: HcmPositionId=" + po.getHcmPositionId());
		if (hcmRecordPo != null) {
			response.setRelatedHcmEffectiveStartDate(hcmRecordPo.getEffectiveStartDateStr());
			response.setRelatedHcmFTE(String.valueOf(hcmRecordPo.getFte()));
			response.setRelatedHcmHeadCount(String.valueOf(hcmRecordPo.getMaxPerson()));
			response.setRelatedHcmPositionName(hcmRecordPo.getName());
			response.setRelatedHcmHiringStatus(hcmRecordPo.getAvailabilityStatus());
			response.setRelatedHcmType(hcmRecordPo.getPositionType());
		}
		
		System.out.println("newRequestPo.size(): " + newRequestPo);
		if (newRequestPo != null) {
			// Add the attachment
			List<RequestAttachmentPo> attachment = requestSvc.getAttachmentByRequestUid(newRequestPo.getRequestNo());

			if (attachment != null) {
				for (int m=0; m<attachment.size(); m++) {
					attachmentList.add(attachment.get(m));
				}
			}
		}

		//History List
		//01, upgrade
		System.out.println("01, upgrade record: " + upgradeRequestPo.size());
		List<String> URequestIdList = new ArrayList<String>();
		List<String> URequestUidList = new ArrayList<String>();
		List<String> URequestEffDateList = new ArrayList<String>();
		List<String> URequestFromToIndList = new ArrayList<String>();
		List<String> URequestPostIDList = new ArrayList<String>();
		List<String> URequestPostIDList2 = new ArrayList<String>();
		List<String> URequestDeptList = new ArrayList<String>();
		List<String> URequestRankList = new ArrayList<String>();
		List<String> URequestPositiontDurationTypeList = new ArrayList<String>();
		List<String> UPositionTypeList = new ArrayList<String>();
		List<String> URequestReason = new ArrayList<String>();
		
		response.setUpgradeListSize(upgradeRequestPo.size() + upgradeRequestPo2.size());
		
		if (upgradeRequestPo.size() != 0) {
			for (int i=0; i<upgradeRequestPo.size(); i++) {
				URequestUidList.add(String.valueOf(upgradeRequestPo.get(i).getRequestNo()));
				
				if (upgradeRequestPo.get(i).getRequestId() != null) {
					URequestIdList.add(upgradeRequestPo.get(i).getRequestId());
					System.out.println("  in: requestID" + upgradeRequestPo.get(i).getRequestId());
				}
				else {
					URequestIdList.add("");
				}
				
				if (upgradeRequestPo.get(i).getEffectFrom() != null) {
					URequestEffDateList.add(DateTimeHelper.formatDateToString(upgradeRequestPo.get(i).getEffectFrom()));
				}
				else {
					URequestEffDateList.add("");
				}
				
				String postList1 = "";
				String postList2 = "";
				
				if (upgradeRequestPo.get(i).getRequestPositionList() !=null) {
					System.out.println("upgradeRequestPo.get(i).getRequestPositionList().size(): " + upgradeRequestPo.get(i).getRequestPositionList().size());
					for (int m=0; m<upgradeRequestPo.get(i).getRequestPositionList().size(); m++) {
						if ("Y".equals(upgradeRequestPo.get(i).getRequestPositionList().get(m).getFromPostInd())) {
							if (!"".equals(postList1)) {
								postList1 = postList1 + ", ";
							}
							
							postList1 = postList1 + upgradeRequestPo.get(i).getRequestPositionList().get(m).getPostId();
						}
						else {
							if (!"".equals(postList2)) {
								postList2 = postList2 + ", ";
							}
							
							postList2 = postList2 + upgradeRequestPo.get(i).getRequestPositionList().get(m).getPostId();
						}
					}
				}
				
				URequestPostIDList.add(postList1);
				URequestPostIDList2.add(postList2);
				
				if (upgradeRequestPo.get(i).getRequestReason() != null) {
					URequestReason.add(upgradeRequestPo.get(i).getRequestReason());
				}
				else {
					URequestReason.add("");
				}
				
//				if(upgradeRequestPo.get(i).getRequestPositionList().get(0).getFromPostInd() == "Y")
//					URequestFromToIndList.add("From");
//				else if(upgradeRequestPo.get(i).getRequestPositionList().get(0).getToPostInd() == "Y")
//					URequestFromToIndList.add("To");
//				else
//					URequestFromToIndList.add("");
//				
//				if (upgradeRequestPo.get(i).getRequestPositionList().get(0).getPostId() != null) 
//					URequestPostIDList.add(upgradeRequestPo.get(i).getRequestPositionList().get(0).getPostId());
//				else
//					URequestPostIDList.add("");
//				
//				if (upgradeRequestPo.get(i).getRequestPositionList().get(0).getDeptId() != null) 
//					URequestDeptList.add(upgradeRequestPo.get(i).getRequestPositionList().get(0).getDeptId());
//				else
//					URequestDeptList.add("");
//				
//				
//				if (upgradeRequestPo.get(i).getRequestPositionList().get(0).getRank() != null) 
//					URequestRankList.add(upgradeRequestPo.get(i).getRequestPositionList().get(0).getRank().getRankName());
//				else
//					URequestRankList.add("");
//				
//				if (upgradeRequestPo.get(i).getRequestPositionList().get(0).getPostDuration() != null) 
//					URequestPositiontDurationTypeList.add(upgradeRequestPo.get(i).getRequestPositionList().get(0).getPostDuration());
//				else
//					URequestPositiontDurationTypeList.add("");
//				
//				if (upgradeRequestPo.get(i).getRequestPositionList().get(0).getRequestFunding() != null) {
//					if (upgradeRequestPo.get(i).getRequestPositionList().get(0).getRequestFunding().getProgramTypeCode() != null) 
//						UPositionTypeList.add(upgradeRequestPo.get(i).getRequestPositionList().get(0).getRequestFunding().getProgramTypeCode());
//					else
//						UPositionTypeList.add("");
//				}
//				else {
//					UPositionTypeList.add("");
//				}
				
				// Add the attachment
				List<RequestAttachmentPo> attachment = requestSvc.getAttachmentByRequestUid(upgradeRequestPo.get(i).getRequestNo());
				
				if (attachment != null) {
					for (int m=0; m<attachment.size(); m++) {
						attachmentList.add(attachment.get(m));
					}
				}
			}
		}
		
		if (upgradeRequestPo2.size() != 0) {
			for (int i=0; i<upgradeRequestPo2.size(); i++) {
				URequestUidList.add(String.valueOf(upgradeRequestPo2.get(i).getRequestNo()));
				
				if (upgradeRequestPo2.get(i).getRequestId() != null) {
					URequestIdList.add(upgradeRequestPo2.get(i).getRequestId());
					System.out.println("  in: requestID" + upgradeRequestPo2.get(i).getRequestId());
				}
				else {
					URequestIdList.add("");
				}
				
				if (upgradeRequestPo2.get(i).getEffectFrom() != null) {
					URequestEffDateList.add(DateTimeHelper.formatDateToString(upgradeRequestPo2.get(i).getEffectFrom()));
				}
				else {
					URequestEffDateList.add("");
				}
				
				String postList1 = "";
				String postList2 = "";
				
				if (upgradeRequestPo2.get(i).getRequestPositionList() !=null) {
					System.out.println("changeStaffMixRequestPo.get(i).getRequestPositionList().size(): " + upgradeRequestPo2.get(i).getRequestPositionList().size());
					for (int m=0; m<upgradeRequestPo2.get(i).getRequestPositionList().size(); m++) {
						if ("Y".equals(upgradeRequestPo2.get(i).getRequestPositionList().get(m).getFromPostInd())) {
							if (!"".equals(postList1)) {
								postList1 = postList1 + ", ";
							}
							
							postList1 = postList1 + upgradeRequestPo2.get(i).getRequestPositionList().get(m).getPostId();
						}
						else {
							if (!"".equals(postList2)) {
								postList2 = postList2 + ", ";
							}
							
							postList2 = postList2 + upgradeRequestPo2.get(i).getRequestPositionList().get(m).getPostId();
						}
					}
				}
				
				URequestPostIDList.add(postList1);
				URequestPostIDList2.add(postList2);
				
				if (upgradeRequestPo2.get(i).getRequestReason() != null) {
					URequestReason.add(upgradeRequestPo2.get(i).getRequestReason());
				}
				else {
					URequestReason.add("");
				}
				
//				if(upgradeRequestPo2.get(i).getRequestPositionList().get(0).getFromPostInd() == "Y")
//					URequestFromToIndList.add("From");
//				else if(upgradeRequestPo2.get(i).getRequestPositionList().get(0).getToPostInd() == "Y")
//					URequestFromToIndList.add("To");
//				else
//					URequestFromToIndList.add("");
//				
//				if (upgradeRequestPo2.get(i).getRequestPositionList().get(0).getPostId() != null) 
//					URequestPostIDList.add(upgradeRequestPo2.get(i).getRequestPositionList().get(0).getPostId());
//				else
//					URequestPostIDList.add("");
//				
//				if (upgradeRequestPo2.get(i).getRequestPositionList().get(0).getDeptId() != null) 
//					URequestDeptList.add(upgradeRequestPo2.get(i).getRequestPositionList().get(0).getDeptId());
//				else
//					URequestDeptList.add("");
//				
//				
//				if (upgradeRequestPo2.get(i).getRequestPositionList().get(0).getRank() != null) 
//					URequestRankList.add(upgradeRequestPo2.get(i).getRequestPositionList().get(0).getRank().getRankName());
//				else
//					URequestRankList.add("");
//				
//				if (upgradeRequestPo2.get(i).getRequestPositionList().get(0).getPostDuration() != null) 
//					URequestPositiontDurationTypeList.add(upgradeRequestPo2.get(i).getRequestPositionList().get(0).getPostDuration());
//				else
//					URequestPositiontDurationTypeList.add("");
//				
//				if (upgradeRequestPo2.get(i).getRequestPositionList().get(0).getRequestFunding() != null) {
//					if (upgradeRequestPo2.get(i).getRequestPositionList().get(0).getRequestFunding().getProgramTypeCode() != null) 
//						UPositionTypeList.add(upgradeRequestPo2.get(i).getRequestPositionList().get(0).getRequestFunding().getProgramTypeCode());
//					else
//						UPositionTypeList.add("");
//				}
//				else {
//					UPositionTypeList.add("");
//				}
				
				// Add the attachment
				List<RequestAttachmentPo> attachment = requestSvc.getAttachmentByRequestUid(upgradeRequestPo2.get(i).getRequestNo());
				
				if (attachment != null) {
					for (int m=0; m<attachment.size(); m++) {
						attachmentList.add(attachment.get(m));
					}
				}
			}
		}
		response.setUpgradeHistRequestList_RequestUid(URequestUidList);
		response.setUpgradeHistRequestList_RequestID(URequestIdList);
		response.setUpgradeHistRequestList_RequestEffDate(URequestEffDateList);
		response.setUpgradeHistRequestList_RequestFromToInd(URequestFromToIndList);
		response.setUpgradeHistRequestList_RequestPostID(URequestPostIDList);
		response.setUpgradeHistRequestList_RequestDept(URequestDeptList);
		response.setUpgradeHistRequestList_RequestRank(URequestRankList);
		response.setUpgradeHistRequestList_RequestPositiontDurationType(URequestPositiontDurationTypeList);
		response.setUpgradeHistRequestList_PositionType(UPositionTypeList);
		response.setUpgradeHistRequestList_Reason(URequestReason);
		
		//02, extension
		System.out.println("02, extension record: " + extensionRequestPo.size());
		if (extensionRequestPo.size() != 0) {
			List<String> ERequestUidList = new ArrayList<String>();
			List<String> ERequestIdList = new ArrayList<String>();
			List<String> ERequestTranDateList = new ArrayList<String>();
			List<String> ERequestOrgStartDateList = new ArrayList<String>();
			List<String> ERequestRevisedStartDateList = new ArrayList<String>();
			List<String> ERequestOrgEndDateList = new ArrayList<String>();
			List<String> ERequestRevisedEndDateList = new ArrayList<String>();

			response.setExtensionListSize(extensionRequestPo.size());
			for (int i=0; i<extensionRequestPo.size(); i++) {
				ERequestUidList.add(String.valueOf(extensionRequestPo.get(i).getRequestNo()));
				
				if (extensionRequestPo.get(i).getRequestId() != null) {
					ERequestIdList.add(extensionRequestPo.get(i).getRequestId());
				}
				else {
					ERequestIdList.add("");
				}
				
				if (extensionRequestPo.get(i).getRequestDate() != null) {
					ERequestTranDateList.add(DateTimeHelper.formatDateToString(extensionRequestPo.get(i).getRequestDate()));
				}
				else {
					ERequestTranDateList.add("");
				}
				 
				for (int f=0; f<extensionRequestPo.get(i).getRequestPositionList().size(); f++) {
					System.out.println("Compare: " + extensionRequestPo.get(i).getRequestPositionList().get(f).getPostNo() + " vs " + mprSearchRequest.getSearchPostNo());
					if (extensionRequestPo.get(i).getRequestPositionList().get(f).getPostNo().equals(mprSearchRequest.getSearchPostNo())) {
						// Original From RQ_POST.POST_START_DATE
						if (extensionRequestPo.get(i).getRequestPositionList().get(f).getPostStartDate() != null) {
							ERequestOrgStartDateList.add(DateTimeHelper.formatDateToString(extensionRequestPo.get(i).getRequestPositionList().get(f).getPostStartDate()));
						}
						else {
							ERequestOrgStartDateList.add("");
						}
						
						// Original To LIMIT_DURATION_END_DATE
						if (extensionRequestPo.get(i).getRequestPositionList().get(f).getLimitDurationEndDate() != null) {
							ERequestOrgEndDateList.add(DateTimeHelper.formatDateToString(extensionRequestPo.get(i).getRequestPositionList().get(f).getLimitDurationEndDate()));
						}
						else {
							ERequestOrgEndDateList.add("");
						}
					}
				}
				
				// Revised From RQ_MASTER.EFFECTIVE_START_DATE
				if(extensionRequestPo.get(i).getEffectFrom() != null) {
					ERequestRevisedStartDateList.add(DateTimeHelper.formatDateToString(extensionRequestPo.get(i).getEffectFrom()));
				}
				else {
					ERequestRevisedStartDateList.add("");
				}
				
				// Revised To RQ_MASTER.EFFECTIVE_END_DATE
				if (extensionRequestPo.get(i).getEffectTo() != null) {
					ERequestRevisedEndDateList.add(DateTimeHelper.formatDateToString(extensionRequestPo.get(i).getEffectTo()));
				}
				else {
					ERequestRevisedEndDateList.add("");
				}
				
				// Add the attachment
				List<RequestAttachmentPo> attachment = requestSvc.getAttachmentByRequestUid(extensionRequestPo.get(i).getRequestNo());
				
				if (attachment != null) {
					for (int m=0; m<attachment.size(); m++) {
						attachmentList.add(attachment.get(m));
					}
				}
			}
			response.setExtensionHistRequestList_RequestUid(ERequestUidList);
			response.setExtensionHistRequestList_RequestID(ERequestIdList);
			response.setExtensionHistRequestList_RequestTransDate(ERequestTranDateList);
			response.setExtensionHistRequestList_RequestOrgStartDate(ERequestOrgStartDateList);
			response.setExtensionHistRequestList_RequestRevisedStartDate(ERequestRevisedStartDateList);
			response.setExtensionHistRequestList_RequestOrgEndDate(ERequestOrgEndDateList);
			response.setExtensionHistRequestList_RequestRevisedEndDate(ERequestRevisedEndDateList);
			
		}
		
		//03, Deletion
		System.out.println("03, Deletion record: " + deletionRequestPo.size());
		if (deletionRequestPo.size() != 0) {
			List<String> DRequestUidList = new ArrayList<String>();
			List<String> DRequestIdList = new ArrayList<String>();
			List<String> DRequestTranDateList = new ArrayList<String>();
			List<String> DRequestEffDateList = new ArrayList<String>();
			List<String> DRequestReasonList = new ArrayList<String>();

			response.setDeletionListSize(deletionRequestPo.size());
			for (int i=0; i<deletionRequestPo.size(); i++) {
				DRequestUidList.add(String.valueOf(deletionRequestPo.get(i).getRequestNo()));
				
				if (deletionRequestPo.get(i).getRequestId() != null) {
					DRequestIdList.add(deletionRequestPo.get(i).getRequestId());
				}
				else {
					DRequestIdList.add("");
				}
				
				if (deletionRequestPo.get(i).getRequestDate() != null) {
					DRequestTranDateList.add(DateTimeHelper.formatDateToString(deletionRequestPo.get(i).getRequestDate()));
				}
				else {
					DRequestTranDateList.add("");
				}
				
				if (deletionRequestPo.get(i).getEffectFrom() != null) {
					DRequestEffDateList.add(DateTimeHelper.formatDateToString(deletionRequestPo.get(i).getEffectFrom()));
				}
				else {
					DRequestEffDateList.add("");
				}
								
				if (deletionRequestPo.get(i).getRequestReason() != null) {
					DRequestReasonList.add(deletionRequestPo.get(i).getRequestReason());
				}
				else {
					DRequestReasonList.add("");
				}
				
				// Add the attachment
				List<RequestAttachmentPo> attachment = requestSvc.getAttachmentByRequestUid(deletionRequestPo.get(i).getRequestNo());
				
				if (attachment != null) {
					for (int m=0; m<attachment.size(); m++) {
						attachmentList.add(attachment.get(m));
					}
				}
			}
			response.setDeletionHistRequestList_RequestUid(DRequestUidList);
			response.setDeletionHistRequestList_RequestID(DRequestIdList);
			response.setDeletionHistRequestList_RequestEffDate(DRequestEffDateList);
			response.setDeletionHistRequestList_RequestTransDate(DRequestTranDateList);
			response.setDeletionHistRequestList_RequestReason(DRequestReasonList);
			
		}
		
		//04, ChangeFunding
		System.out.println("04, Change Funding: " + changeFundingRequestPo.size());
		if (changeFundingRequestPo.size() != 0) {
			List<String> FRequestIdList = new ArrayList<String>();
			List<String> FRequestTranDateList = new ArrayList<String>();
			List<String> FRequestUidList = new ArrayList<String>();
			List<String> FRequestReasonList = new ArrayList<String>();

			response.setChangeFundingListSize(changeFundingRequestPo.size());
			for (int i=0; i<changeFundingRequestPo.size(); i++) {
				FRequestUidList.add(String.valueOf(changeFundingRequestPo.get(i).getRequestNo()));
				
				if (changeFundingRequestPo.get(i).getRequestId() != null) {
					FRequestIdList.add(changeFundingRequestPo.get(i).getRequestId());
				}
				else {
					FRequestIdList.add("");
				}
				
				if (changeFundingRequestPo.get(i).getRequestDate() != null) {
					FRequestTranDateList.add(DateTimeHelper.formatDateToString(changeFundingRequestPo.get(i).getRequestDate()));
				}
				else {
					FRequestTranDateList.add("");
				}
				
				if (changeFundingRequestPo.get(i).getRequestReason() != null) {
					FRequestReasonList.add(changeFundingRequestPo.get(i).getRequestReason());
				}
				else {
					FRequestReasonList.add("");
				}
				
				// Add the attachment
				List<RequestAttachmentPo> attachment = requestSvc.getAttachmentByRequestUid(changeFundingRequestPo.get(i).getRequestNo());
				
				if (attachment != null) {
					for (int m=0; m<attachment.size(); m++) {
						attachmentList.add(attachment.get(m));
					}
				}
			}
			response.setChangeFundHistRequestList_RequestUid(FRequestUidList);
			response.setChangeFundHistRequestList_RequestID(FRequestIdList);
			response.setChangeFundHistRequestList_RequestTransDate(FRequestTranDateList);
			response.setChangeFundHistRequestList_RequestReason(FRequestReasonList);
			
		}
		
		//05, Supp Promo
		System.out.println("05, Supp Promo record: " + suppPromotionRequestPo.size());
		if (suppPromotionRequestPo.size() != 0) {
			List<String> SRequestUidList = new ArrayList<String>();
			List<String> SRequestIdList = new ArrayList<String>();
			List<String> SRequestTranDateList = new ArrayList<String>();
			List<String> SRequestEffDateList = new ArrayList<String>();
			List<String> SRequestReasonList = new ArrayList<String>();

			response.setSuppPromotionListSize(suppPromotionRequestPo.size());
			for (int i=0; i<suppPromotionRequestPo.size(); i++) {
				SRequestUidList.add(String.valueOf(suppPromotionRequestPo.get(i).getRequestNo()));
				
				if (suppPromotionRequestPo.get(i).getRequestId() != null) {
					SRequestIdList.add(suppPromotionRequestPo.get(i).getRequestId());
				}
				else {
					SRequestIdList.add("");
				}
				
				if (suppPromotionRequestPo.get(i).getRequestDate() != null) {
					SRequestTranDateList.add(DateTimeHelper.formatDateToString(suppPromotionRequestPo.get(i).getRequestDate()));
				}
				else {
					SRequestTranDateList.add("");
				}
				
				if (suppPromotionRequestPo.get(i).getEffectFrom() != null) {
					SRequestEffDateList.add(DateTimeHelper.formatDateToString(suppPromotionRequestPo.get(i).getEffectFrom()));
				}
				else {
					SRequestEffDateList.add("");
				}
								
				if (suppPromotionRequestPo.get(i).getRequestReason() != null) {
					SRequestReasonList.add(suppPromotionRequestPo.get(i).getRequestReason());
				}
				else {
					SRequestReasonList.add("");
				}
				
				// Add the attachment
				List<RequestAttachmentPo> attachment = requestSvc.getAttachmentByRequestUid(suppPromotionRequestPo.get(i).getRequestNo());
				
				if (attachment != null) {
					for (int m=0; m<attachment.size(); m++) {
						attachmentList.add(attachment.get(m));
					}
				}
			}
			response.setSuppPromoHistRequestList_RequestUid(SRequestUidList);
			response.setSuppPromoHistRequestList_RequestID(SRequestIdList);
			response.setSuppPromoHistRequestList_RequestEffDate(SRequestEffDateList);
			response.setSuppPromoHistRequestList_RequestTransDate(SRequestTranDateList);
			response.setSuppPromoHistRequestList_RequestRemark(SRequestReasonList);
			
		}
		//06, Frozen
		System.out.println("06, Frozen record: " + frozenPostRequestPo.size());
		if (frozenPostRequestPo.size() != 0) {
			List<String> FzRequestUidList = new ArrayList<String>();
			List<String> FzRequestIdList = new ArrayList<String>();
			List<String> FzRequestTranDateList = new ArrayList<String>();
			List<String> FzRequestEffDateList = new ArrayList<String>();
			List<String> FzRequestFzEndDateList = new ArrayList<String>();
			List<String> FzRequestReasonList = new ArrayList<String>();

			response.setFrozenListSize(frozenPostRequestPo.size());
			for (int i=0; i<frozenPostRequestPo.size(); i++) {
				FzRequestUidList.add(String.valueOf(frozenPostRequestPo.get(i).getRequestNo()));
				
				if (frozenPostRequestPo.get(i).getRequestId() != null) {
					FzRequestIdList.add(frozenPostRequestPo.get(i).getRequestId());
				}
				else {
					FzRequestIdList.add("");
				}
				
				if (frozenPostRequestPo.get(i).getRequestDate() != null) {
					FzRequestTranDateList.add(DateTimeHelper.formatDateToString(frozenPostRequestPo.get(i).getRequestDate()));
				}
				else {
					FzRequestTranDateList.add("");
				}
				
				if (frozenPostRequestPo.get(i).getEffectFrom() != null) {
					FzRequestEffDateList.add(DateTimeHelper.formatDateToString(frozenPostRequestPo.get(i).getEffectFrom()));
				}
				else {
					FzRequestEffDateList.add("");
				}
				
				if (frozenPostRequestPo.get(i).getRequestPositionList().get(0).getPostStatusEndDate() != null) {
					FzRequestFzEndDateList.add(DateTimeHelper.formatDateToString(frozenPostRequestPo.get(i).getRequestPositionList().get(0).getPostStatusEndDate()));
				}
				else {
					FzRequestFzEndDateList.add("");
				}
								
				if (frozenPostRequestPo.get(i).getRequestReason() != null) {
					FzRequestReasonList.add(frozenPostRequestPo.get(i).getRequestReason());
				}
				else {
					FzRequestReasonList.add("");
				}
				
				// Add the attachment
				List<RequestAttachmentPo> attachment = requestSvc.getAttachmentByRequestUid(frozenPostRequestPo.get(i).getRequestNo());
				
				if (attachment != null) {
					for (int m=0; m<attachment.size(); m++) {
						attachmentList.add(attachment.get(m));
					}
				}
			}
			response.setFrozenHistRequestList_RequestUid(FzRequestUidList);
			response.setFrozenHistRequestList_RequestID(FzRequestIdList);
			response.setFrozenHistRequestList_RequestEffDate(FzRequestEffDateList);
			response.setFrozenHistRequestList_RequestTransDate(FzRequestTranDateList);
			response.setFrozenHistRequestList_RequestFrozenEndDate(FzRequestFzEndDateList);
			response.setFrozenHistRequestList_RequestReason(FzRequestReasonList);
			
		}
		//07, ChangeStaffMix
		System.out.println("07, ChangeStaffMix: " + changeStaffMixRequestPo.size());
		List<String> SmRequestUidList = new ArrayList<String>();
		List<String> SmRequestIdList = new ArrayList<String>();
		List<String> SmRequestEffDateList = new ArrayList<String>();
		List<String> SmRequestPostIDList = new ArrayList<String>();
		List<String> SmRequestPostIDList2 = new ArrayList<String>();
		List<String> SmRequestReasonList = new ArrayList<String>();
		
		response.setChangeStaffMixListSize(changeStaffMixRequestPo.size() + changeStaffMixRequestPo2.size());
		if (changeStaffMixRequestPo.size() != 0) {
			for (int i=0; i<changeStaffMixRequestPo.size(); i++) {
				SmRequestUidList.add(String.valueOf(changeStaffMixRequestPo.get(i).getRequestNo()));
				
				if (changeStaffMixRequestPo.get(i).getRequestId() != null) {
					SmRequestIdList.add(changeStaffMixRequestPo.get(i).getRequestId());
					System.out.println("  in: requestID" + changeStaffMixRequestPo.get(i).getRequestId());
				}
				else {
					SmRequestIdList.add("");
				}
				
				if (changeStaffMixRequestPo.get(i).getEffectFrom() != null) {
					SmRequestEffDateList.add(DateTimeHelper.formatDateToString(changeStaffMixRequestPo.get(i).getEffectFrom()));
				}
				else {
					SmRequestEffDateList.add("");
				}
				

				String postList1 = "";
				String postList2 = "";
				
				if (changeStaffMixRequestPo.get(i).getRequestPositionList() !=null) {
					System.out.println("changeStaffMixRequestPo.get(i).getRequestPositionList().size(): " + changeStaffMixRequestPo.get(i).getRequestPositionList().size());
					for (int m=0; m<changeStaffMixRequestPo.get(i).getRequestPositionList().size(); m++) {
						if ("Y".equals(changeStaffMixRequestPo.get(i).getRequestPositionList().get(m).getFromPostInd())) {
							if (!"".equals(postList1)) {
								postList1 = postList1 + ", ";
							}
							
							postList1 = postList1 + changeStaffMixRequestPo.get(i).getRequestPositionList().get(m).getPostId();
						}
						else {
							if (!"".equals(postList2)) {
								postList2 = postList2 + ", ";
							}
							
							postList2 = postList2 + changeStaffMixRequestPo.get(i).getRequestPositionList().get(m).getPostId();
						}
					}
				}
				
				SmRequestPostIDList.add(postList1);
				SmRequestPostIDList2.add(postList2);
				
				if (changeStaffMixRequestPo.get(i).getRequestReason() != null) {
					SmRequestReasonList.add(changeStaffMixRequestPo.get(i).getRequestReason());
				}
				else {
					SmRequestReasonList.add("");
				}
				
				// Add the attachment
				List<RequestAttachmentPo> attachment = requestSvc.getAttachmentByRequestUid(changeStaffMixRequestPo.get(i).getRequestNo());
				if (attachment != null) {
					for (int m=0; m<attachment.size(); m++) {
						attachmentList.add(attachment.get(m));
					}
				}
				
			}
			
		}
		
		if (changeStaffMixRequestPo2.size() != 0) {
			for (int i=0; i<changeStaffMixRequestPo2.size(); i++) {
				SmRequestUidList.add(String.valueOf(changeStaffMixRequestPo2.get(i).getRequestNo()));
				
				if (changeStaffMixRequestPo2.get(i).getRequestId() != null) {
					SmRequestIdList.add(changeStaffMixRequestPo2.get(i).getRequestId());
					System.out.println("  in: requestID" + changeStaffMixRequestPo2.get(i).getRequestId());
				}
				else {
					SmRequestIdList.add("");
				}
				
				if (changeStaffMixRequestPo2.get(i).getEffectFrom() != null) { 
					SmRequestEffDateList.add(DateTimeHelper.formatDateToString(changeStaffMixRequestPo2.get(i).getEffectFrom()));
				}
				else {
					SmRequestEffDateList.add("");
				}
				
				String postList1 = "";
				String postList2 = "";
				
				if (changeStaffMixRequestPo2.get(i).getRequestPositionList() !=null) {
					System.out.println("changeStaffMixRequestPo.get(i).getRequestPositionList().size(): " + changeStaffMixRequestPo2.get(i).getRequestPositionList().size());
					for (int m=0; m<changeStaffMixRequestPo2.get(i).getRequestPositionList().size(); m++) {
						if ("Y".equals(changeStaffMixRequestPo2.get(i).getRequestPositionList().get(m).getFromPostInd())) {
							if (!"".equals(postList1)) {
								postList1 = postList1 + ", ";
							}
							
							postList1 = postList1 + changeStaffMixRequestPo2.get(i).getRequestPositionList().get(m).getPostId();
						}
						else {
							if (!"".equals(postList2)) {
								postList2 = postList2 + ", ";
							}
							
							postList2 = postList2 + changeStaffMixRequestPo2.get(i).getRequestPositionList().get(m).getPostId();
						}
					}
				}
				
				SmRequestPostIDList.add(postList1);
				SmRequestPostIDList2.add(postList2);
				
				if (changeStaffMixRequestPo2.get(i).getRequestReason() != null) {
					SmRequestReasonList.add(changeStaffMixRequestPo2.get(i).getRequestReason());
				}
				else {
					SmRequestReasonList.add("");
				}
				
				// Add the attachment
				List<RequestAttachmentPo> attachment = requestSvc.getAttachmentByRequestUid(changeStaffMixRequestPo2.get(i).getRequestNo());
				if (attachment != null) {
					for (int m=0; m<attachment.size(); m++) {
						attachmentList.add(attachment.get(m));
					}
				}
				
			}
			
		}
		
		response.setStaffMixHistRequestList_RequestUid(SmRequestUidList);
		response.setStaffMixHistRequestList_RequestID(SmRequestIdList);
		response.setStaffMixHistRequestList_RequestEffDate(SmRequestEffDateList);
		response.setStaffMixHistRequestList_RequestPostID(SmRequestPostIDList);
		response.setStaffMixHistRequestList_RequestPostID2(SmRequestPostIDList2);
		response.setStaffMixHistRequestList_Reason(SmRequestReasonList);
		
		// FTE Adjustment
		System.out.println("FTE Adjustment: " + fteAdjustmentRequestPo.size());
		if (fteAdjustmentRequestPo.size() != 0) {
			List<String> fteAdjustRequestUidList = new ArrayList<String>();
			List<String> fteAdjustRequestIdList = new ArrayList<String>();
			List<String> fteAdjustRequestTranDateList = new ArrayList<String>();
			List<String> fteAdjustRequestReasonList = new ArrayList<String>();

			response.setFteAdjustmentListSize(fteAdjustmentRequestPo.size());
			for (int i=0; i<fteAdjustmentRequestPo.size(); i++) {
				fteAdjustRequestUidList.add(String.valueOf(fteAdjustmentRequestPo.get(i).getRequestNo()));
				
				if (fteAdjustmentRequestPo.get(i).getRequestId() != null) {
					fteAdjustRequestIdList.add(fteAdjustmentRequestPo.get(i).getRequestId());
				}
				else {
					fteAdjustRequestIdList.add("");
				}

				if (fteAdjustmentRequestPo.get(i).getRequestDate() != null) {
					fteAdjustRequestTranDateList.add(DateTimeHelper.formatDateToString(fteAdjustmentRequestPo.get(i).getRequestDate()));
				}
				else {
					fteAdjustRequestTranDateList.add("");
				}

				if (fteAdjustmentRequestPo.get(i).getRequestReason() != null) {
					fteAdjustRequestReasonList.add(fteAdjustmentRequestPo.get(i).getRequestReason());
				}
				else {
					fteAdjustRequestReasonList.add("");
				}
			}
			
			
			response.setFteAdjustmentHistRequestList_RequestUid(fteAdjustRequestUidList);
			response.setFteAdjustmentHistRequestList_RequestID(fteAdjustRequestIdList);
			response.setFteAdjustmentHistRequestList_RequestTransDate(fteAdjustRequestTranDateList);
			response.setFteAdjustmentHistRequestList_RequestReason(fteAdjustRequestReasonList);
		}
		
		//09, Assignment
		System.out.println("09, Assignment: " + AssignmentPo.size());
		if (AssignmentPo.size() != 0) {
			List<String> assignmentPositionId = new ArrayList<String>();
			List<String> assignmentAssignmentNumber = new ArrayList<String>();
			List<String> assignmentEmpName = new ArrayList<String>();
			List<String> assignmentRank = new ArrayList<String>();
			List<String> assignmentEmpType = new ArrayList<String>();
			List<String> assignmentEmpPositionTitle = new ArrayList<String>();
			List<String> assignmentFTE = new ArrayList<String>();
			List<String> assignmentEffStartDate = new ArrayList<String>();
			List<String> assignmentEffEndDate = new ArrayList<String>();
			List<String> assignmentLeaveReason = new ArrayList<String>();

			response.setAssignmentListSize(AssignmentPo.size());
			for (int i=0; i<AssignmentPo.size(); i++) {
				if (AssignmentPo.get(i).getAssignmentNumber() != null) {
					assignmentAssignmentNumber.add(AssignmentPo.get(i).getAssignmentNumber());
				}
				else {
					assignmentAssignmentNumber.add("");
				}
				
				if (AssignmentPo.get(i).getPositionId() != null) {
					assignmentPositionId.add(String.valueOf(AssignmentPo.get(i).getPositionId()));
				}
				else {
					assignmentPositionId.add("");
				}
				
				if (AssignmentPo.get(i).getFullName() != null) {
					assignmentEmpName.add(AssignmentPo.get(i).getFullName());
				}
				else {
					assignmentEmpName.add("");
				}
				
				if (AssignmentPo.get(i).getRank() != null) {
					assignmentRank.add(AssignmentPo.get(i).getRank());
				}
				else {
					assignmentRank.add("");
				}
				
				if (AssignmentPo.get(i).getEmploymentType() != null) {
					assignmentEmpType.add(AssignmentPo.get(i).getEmploymentType());
				}
				else {
					assignmentEmpType.add("");
				}
				
				if (AssignmentPo.get(i).getPositionTitle() != null) {
					assignmentEmpPositionTitle.add(AssignmentPo.get(i).getPositionTitle());
				}
				else {
					assignmentEmpPositionTitle.add("");
				}
				
				if (AssignmentPo.get(i).getFte() != null) {
					assignmentFTE.add(String.valueOf(AssignmentPo.get(i).getFte()));
				}
				else {
					assignmentFTE.add("");
				}
				
				if (AssignmentPo.get(i).getEffectiveStartDate() != null) {
					assignmentEffStartDate.add(DateTimeHelper.formatDateToString(AssignmentPo.get(i).getEffectiveStartDate()));
				}
				else {
					assignmentEffStartDate.add("");
				}
				
				if (AssignmentPo.get(i).getEffectiveEndDate() != null) {
					assignmentEffEndDate.add(DateTimeHelper.formatDateToString(AssignmentPo.get(i).getEffectiveEndDate()));
				}
				else {
					assignmentEffEndDate.add("");
				}
				
				if (AssignmentPo.get(i).getReasonOfLeaving() != null) {
					assignmentLeaveReason.add(String.valueOf(AssignmentPo.get(i).getReasonOfLeaving()));
				}
				else {
					assignmentLeaveReason.add("");
				}
				
			}
			response.setAssignmentHistList_PositionID(assignmentPositionId);
			response.setAssignmentHistList_EmpName(assignmentEmpName);
			response.setAssignmentHistList_Rank(assignmentRank);
			response.setAssignmentHistList_EmpType(assignmentEmpType);
			response.setAssignmentHistList_EmpPositionTitle(assignmentEmpPositionTitle);
			response.setAssignmentHistList_FTE(assignmentFTE);
			response.setAssignmentHistList_StartDate(assignmentEffStartDate);
			response.setAssignmentHistList_EndDate(assignmentEffEndDate);
			response.setAssignmentHistList_LeaveReason(assignmentLeaveReason);
			response.setAssignmentHistList_AssignmentNumber(assignmentAssignmentNumber);
		}
		
		// Get the attachment by Post id
		List<RequestAttachmentPo> postAttachmentList = requestSvc.getAttachmentByPostUid(Integer.parseInt(mprSearchRequest.getSearchPostNo()));
		if (postAttachmentList != null) {
			for (int m=0; m<postAttachmentList.size(); m++) {
				attachmentList.add(postAttachmentList.get(m));
			}
		}
		
		List<String> attachmentNameList = new ArrayList<String>();
		List<String> attachmentUidList = new ArrayList<String>();
		List<String> attachmentCreatedByList = new ArrayList<String>();
		List<String> attachmentCreatedDateList = new ArrayList<String>();
		
		for (int y=0; y<attachmentList.size(); y++) {
			attachmentNameList.add(attachmentList.get(y).getFileName());
			attachmentUidList.add(String.valueOf(attachmentList.get(y).getAttachmentUid()));
			attachmentCreatedDateList.add(DateTimeHelper.formatDateTimeToString(attachmentList.get(y).getCreatedDate()));
			attachmentCreatedByList.add(attachmentList.get(y).getCreatedBy());
		}
		
		System.out.println("attachmentList.size(): " + attachmentList.size());
		
		response.setAttachmentNameList(attachmentNameList);
		response.setAttachmentUidList(attachmentUidList);
		response.setAttachmentCreatedDateList(attachmentCreatedDateList);
		response.setAttachmentCreatedByList(attachmentCreatedByList);
		
		response.setAttachmentListSize(attachmentList.size());
		
		response.setHcmPositionId(po.getHcmPositionId());
		response.setClusterCode(po.getClusterCode());
		response.setInstCode(po.getInstCode());
		response.setDeptCode(po.getDeptCode());
		response.setRankCode(po.getRankCode());
		response.setStaffGroupCode(po.getStaffGroupCode());
				
		System.out.println("Finish the Ajax Loading");

		return response;
	}
	
	@RequestMapping(value="/request/downloadAttachment", method=RequestMethod.GET)
	public void downloadAttachment(HttpServletRequest request,
			HttpServletResponse response, @RequestParam(required=true) String aid) throws Exception {
		EClaimLogger.info("downloadAttachment aid: " + aid);
		RequestAttachmentPo attachment = requestSvc.getRequestAttachment(aid);
		
		// response.setContentType("application/x-pdf");
		response.setHeader("Content-disposition", "attachment; filename=" + attachment.getFileName());
		
		File file = null;
		if (attachment.getPostUid() == null) {
			String rqUidStr = String.valueOf(attachment.getRequestUid());
			while (rqUidStr.length() != 10) {
				rqUidStr = "0" + rqUidStr;
			}
			String filePath = "rq_" + rqUidStr + File.separator + attachment.getFileName();
			
			file = new File(APPROVAL_DOC_PATH + filePath);
		}
		else {
			String postUidStr = String.valueOf(attachment.getPostUid());
			while (postUidStr.length() != 10) {
				postUidStr = "0" + postUidStr;
			}
			String filePath = "post_" + postUidStr + File.separator + attachment.getFileName();
			
			file = new File(POST_DOC_PATH + filePath);
		}
		
		EClaimLogger.info("File: " + file.getAbsolutePath());
		if (file.exists()) {
			FileInputStream in = new FileInputStream(file);
			final OutputStream outStream = response.getOutputStream();
	
			byte[] buf = new byte[1024];
			int count = 0;
			while ((count = in.read(buf)) > 0) {
				outStream.write(buf, 0, count);
			}
	
			outStream.close();
			in.close();
		}
	}
	
	@RequestMapping(value="/request/getNextStep")
	public @ResponseBody MPRSResultResponse getNextStep(@RequestParam("requestNo")String requestNo,
            											@RequestParam("requestType")String requestType,
            											@RequestParam("currentStatus")String currentStatus,
            											@RequestParam("mgTeamInd")String mgTeamInd) {
		EClaimLogger.info("Location RequestController.getNextStep");
		
		RequestPo requestCaseTemp = requestSvc.getRequestByRequestNo(Integer.parseInt(requestNo));
		
		// Get the staffGroupCode
		String staffGroupCode = "";
		for (int m=0; m<requestCaseTemp.getRequestPositionList().size(); m++) {
			staffGroupCode = requestCaseTemp.getRequestPositionList().get(m).getStaffGroupCode();
			if (PostConstant.STAFF_GROUP_MEDICAL.equals(staffGroupCode)) {
				break;
			}
		}
		
		List<RolePo> roleList = null;
		if (mgTeamInd != null && !"".equals(mgTeamInd)) {
//			roleList = requestSvc.getNextGroup(staffGroupCode, requestType, currentStatus, mgTeamInd);
		}
		else {
//			roleList = requestSvc.getNextGroup(staffGroupCode, requestType, currentStatus);
		}
		MPRSResultResponse response = new MPRSResultResponse();
		List<String> userId = new ArrayList<String>();
		List<String> userName = new ArrayList<String>();
		for (int i=0; i<roleList.size(); i++) {
			userId.add(roleList.get(i).getRoleId());
			userName.add(roleList.get(i).getRoleName());
		}
		
		response.setUserId(userId);
		response.setUserName(userName);
		
		// Get next default group
		RequestWorkflowRoutePo defaultRoute = null;
		if (mgTeamInd != null && !"".equals(mgTeamInd)) {
			defaultRoute = requestSvc.getDefaultRoute(staffGroupCode, requestType, currentStatus, mgTeamInd);
		}
		else {
			defaultRoute = requestSvc.getDefaultRoute(staffGroupCode, requestType, currentStatus);
		}
		response.setDefaultRole(defaultRoute.getSubmitToRole());
		response.setNextActionName(defaultRoute.getActionName());
		
		System.out.println("Finish the Ajax Loading");

		return response;
	} 
	
	@RequestMapping(value="/request/getPreviousStep")
	public @ResponseBody MPRSResultResponse getPreviousStep(@RequestParam("requestNo")String requestNo,
            											@RequestParam("requestType")String requestType,
            											@RequestParam("currentStatus")String currentStatus,
            											@RequestParam("mgTeamInd")String mgTeamInd) {
		EClaimLogger.info("Location RequestController.getPreviousStep");
		
		RequestPo requestCaseTemp = requestSvc.getRequestByRequestNo(Integer.parseInt(requestNo));
		
		// Get the staffGroupCode
		String staffGroupCode = "";
		boolean haveMedical = false;
		for (int m=0; m<requestCaseTemp.getRequestPositionList().size(); m++) {
			staffGroupCode = requestCaseTemp.getRequestPositionList().get(m).getStaffGroupCode();
			if ("MEDICAL".equals(staffGroupCode)) {
				haveMedical = true;
			}
		}
		
		if (haveMedical) {
			staffGroupCode = "MEDICAL";
		}
		
		System.out.println("requestController.getNextStep() - staffGroupCode: " + staffGroupCode);
		
		List<RolePo> roleList = null;
		if (mgTeamInd != null && !"".equals(mgTeamInd)) {
			roleList = requestSvc.getPreviousGroup(staffGroupCode, requestType, currentStatus, mgTeamInd);
		}
		else {
			roleList = requestSvc.getPreviousGroup(staffGroupCode, requestType, currentStatus);
		}
		MPRSResultResponse response = new MPRSResultResponse();
		List<String> userId = new ArrayList<String>();
		List<String> userName = new ArrayList<String>();
		for (int i=0; i<roleList.size(); i++) {
			userId.add(roleList.get(i).getRoleId());
			userName.add(roleList.get(i).getRoleName());
		}
		
		response.setUserId(userId);
		response.setUserName(userName);
		
		System.out.println("Finish the Ajax Loading");

		return response;
	} 
	
//	@RequestMapping(value="/common/uploadFile", method = RequestMethod.POST, consumes = "multipart/form-data")
//	public @ResponseBody String doUpload(@RequestParam("approvalFile") MultipartFile multipartFile, 
//												           HttpServletRequest request,
//												           HttpServletResponse response) throws Exception {      
//		String fileName = multipartFile.getOriginalFilename();
//		EClaimLogger.info("### Perform doUpload - Uploaded: filename=" + fileName + ", "+ multipartFile.getSize() + " bytes");
//		
//		String userId = this.getSessionUser(request).getUserId();
//		String currentRole = (String)request.getSession().getAttribute("currentRole");
//		
//		RequestTempFilePo file = new RequestTempFilePo();
//		
//		// file.setContent(multipartFile.getBytes());
//		file.setFilename(fileName);
//		file.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
//		file.setCreatedBy(userId);
//		file.setCreatedRoleId(currentRole);
//		file.setCreatedDate(new Date());
//		file.setUpdatedBy(userId);
//		file.setUpdatedRoleId(currentRole);
//		file.setUpdatedDate(new Date());
//		requestSvc.uploadRequestTempFile(file, multipartFile);
//	    return String.valueOf(file.getTmpFileUid());
//	}
	
	@RequestMapping(value="/common/uploadPostFile", method = RequestMethod.POST, consumes = "multipart/form-data")
	public @ResponseBody String uploadPostFile(@RequestParam("approvalFile") MultipartFile multipartFile,
			                                   	@RequestParam("postUId") String postUid,
												           HttpServletRequest request) throws Exception {      
		EClaimLogger.info("### Perform uploadPostFile - Uploaded: " + multipartFile.getSize() + " bytes");
		if (!multipartFile.isEmpty()) {
			if (multipartFile.getOriginalFilename().contains("\\")
					|| multipartFile.getOriginalFilename().contains("/")) {
				throw new IllegalArgumentException();
			}

			// Get the user name from cookie
			String userId = this.getSessionUser(request).getUserId();
			String currentRole = (String)request.getSession().getAttribute("currentRole");
			
			RequestAttachmentPo file = new RequestAttachmentPo();
			UserPo user = securitySvc.findUser(userId);
			user.setCurrentRole(currentRole);
			file.setFileName(multipartFile.getOriginalFilename());
			file.setPostUid(Integer.parseInt(postUid));
			
			/*String postUidStr = postUid;
			while (postUidStr.length() != 10) {
				postUidStr = "0" + postUidStr;
			}
			String filePath = "post_" + postUidStr + File.separator + file.getFileName();
			file.setFilePath(filePath);*/
			//MprsLogger.info("filePath=" + filePath);
			// Perform file upload
			requestSvc.uploadPostFile(postUid, file, multipartFile, user);
			return String.valueOf(file.getAttachmentUid());
			
		} else {
			throw new Exception("failed to upload because the file was empty.");
		}
	    
	}
	
	@RequestMapping(value="/common/removePostFile", method = RequestMethod.POST)
	public @ResponseBody String removePostFile(@RequestParam("attUid") String attUid,
												           HttpServletRequest request,
												           HttpServletResponse response) throws Exception {      
		System.out.println("Perform removePostFile");
		
		// Get the user name from cookie
		String userId = this.getSessionUser(request).getUserId();
		String currentRole = (String)request.getSession().getAttribute("currentRole");
		
		UserPo user = securitySvc.findUser(userId);
		user.setCurrentRole(currentRole);

		RequestAttachmentPo attachment = requestSvc.getRequestAttachment(attUid);
		requestSvc.removePostFile(attachment, user);

		// Delete the attachment physical
		if (attachment.getPostUid() == null) {
			String rqUidStr = String.valueOf(attachment.getRequestUid());
			while (rqUidStr.length() != 10) {
				rqUidStr = "0" + rqUidStr;
			}
			String filePath = "rq_" + rqUidStr + File.separator + attachment.getFileName();
			
			File file = new File(APPROVAL_DOC_PATH + filePath);
			file.delete();
		}
		else {
			String postUidStr = String.valueOf(attachment.getPostUid());
			while (postUidStr.length() != 10) {
				postUidStr = "0" + postUidStr;
			}
			String filePath = "post_" + postUidStr + File.separator + attachment.getFileName();
			
			File file = new File(POST_DOC_PATH + filePath);
			file.delete();
		}
		
	    return String.valueOf(attachment.getAttachmentUid());
	}
	
//	@RequestMapping(value="/common/getTempFile", method=RequestMethod.GET)
//	public void getTempFile(HttpServletRequest request,
//			HttpServletResponse response, @RequestParam(required=true) String uid) throws Exception {
//		//String aid = request.getParameter("uid");
//		EClaimLogger.info("getTempFile aid: " + uid);
//
//		RequestTempFilePo attachment = requestSvc.getTempFile(Integer.parseInt(uid));
//
//		// response.setContentType("application/x-pdf");
//		response.setHeader("Content-disposition", "attachment; filename=" + attachment.getFilename());
//
//		String docUidStr = uid;
//		while (docUidStr.length() != 10) {
//			docUidStr = "0" + docUidStr;
//		}
//		String filePath = docUidStr + File.separator + attachment.getFilename();
//		
//		File file = new File(DOC_PATH + filePath);
//		FileInputStream in = new FileInputStream(file);
//		final OutputStream outStream = response.getOutputStream();
//
//		byte[] buf = new byte[1024];
//		int count = 0;
//		while ((count = in.read(buf)) > 0) {
//			outStream.write(buf, 0, count);
//		}
//
//		outStream.close();
//		in.close();
//	}
	
	@RequestMapping(value="/request/getRequestPosition", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody RequestPostVo getRequestPosition(@RequestParam("requestPositionUid") String requestPositionUid, 
												           HttpServletRequest request) throws Exception {      
		System.out.println("getRequestPosition");
		RequestPostPo po = requestSvc.getRequestPositionByUid(Integer.parseInt(requestPositionUid));
		RequestPostVo vo = new RequestPostVo();
		
		vo.setRequestPostId(String.valueOf(po.getRequestPostId()));
		vo.setPostNo(po.getPostNo());
		vo.setPostId(po.getPostId());
		vo.setClusterCode(po.getClusterCode());
		vo.setInstCode(po.getInstCode());
		vo.setDeptCode(po.getDeptCode());
		vo.setStaffGroupCode(po.getStaffGroupCode());
		vo.setRankDesc(po.getRank().getRankName());
		vo.setRankCode(po.getRank().getRankCode());
		vo.setProposedPostId(po.getProposedPostId());
		vo.setFromPostInd(po.getFromPostInd());
		vo.setToPostInd(po.getToPostInd());
		vo.setChangeFundingInd(po.getChangeFundingInd());
		vo.setUnit(po.getUnit());
		vo.setPostTitle(po.getPostTitle());
		vo.setPostDuration(po.getPostDuration());
		vo.setLimitDurationType(po.getLimitDurationType());
		vo.setLimitDurationNo(po.getLimitDurationNo());
		vo.setLimitDurationUnit(po.getLimitDurationUnit());
		vo.setLimitDurationEndDate(po.getLimitDurationEndDate());
		vo.setPostRemark(po.getPostRemark());
		vo.setPostStartDate(po.getPostStartDate());
		vo.setPostFTEType(po.getPostFTEType());
		vo.setPostFTE(po.getPostFTE());
		vo.setPostStatus(po.getPostStatus());
		vo.setPostStatusStartDate(po.getPostStatusStartDate());
		vo.setPostStatusEndDate(po.getPostStatusEndDate());
		vo.setClusterRef(po.getClusterRef());		
		vo.setClusterRemark(po.getClusterRemark());
		vo.setSuppPromoInd(po.getSuppPromoInd());
			
		vo.setJuniorPostId(po.getJuniorPostId());
		vo.setHcmPositionId(po.getHcmPositionId());
		vo.setSubSpecialtyCode(po.getSubSpecialtyCode());
		
//		vo.setAnnualPlanInd(po.getRequestFunding().getAnnualPlanInd());
//		vo.setProgramYear(po.getRequestFunding().getProgramYear());
//		vo.setProgramCode(po.getRequestFunding().getProgramCode());
//		vo.setProgramName(po.getRequestFunding().getProgramName());
//		vo.setProgramRemark(po.getRequestFunding().getProgramRemark());
//		vo.setFundSrc1st(po.getRequestFunding().getFundSrcId());
//		vo.setFunSrc1stStartDate(po.getRequestFunding().getFundSrcStartDate());
//		vo.setFundSrc1stEndDate(po.getRequestFunding().getFundSrcEndDate());
//		vo.setFundSrc1stFte(po.getRequestFunding().getFundSrcFte());
//		vo.setFundRemark(po.getRequestFunding().getFundSrcRemark());
//		vo.setProgramTypeCode(po.getRequestFunding().getProgramTypeCode());
		
		vo.setResourcesSupportFrExt(po.getRequestFundingResource().getResourcesSupportFrExt());
		vo.setResourcesSupportRemark(po.getRequestFundingResource().getResourcesSupportRemark());
		
		
		// Get HCM Info
		HCMRecordPo hcmRecordPo = hcmSvc.getHCMRecordByPositionId(Integer.parseInt(po.getHcmPositionId()));
		System.out.println("hcmRecordPo: HcmPositionId=" + po.getHcmPositionId());
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
		
	    return vo;
	}
	
	@RequestMapping(value="/request/getRelatedHCMInfo", method = RequestMethod.POST)
	public @ResponseBody RequestPostVo getRelatedHCMInfo(@RequestParam("hcmPositionId") String hcmPositionId, 
												           HttpServletRequest request) throws Exception {      
		System.out.println("getRelatedHCMInfo");
		RequestPostVo vo = new RequestPostVo();
		HCMRecordPo hcmRecordPo = hcmSvc.getHCMRecordByPositionId(Integer.parseInt(hcmPositionId));
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
			
			// Get the default department, staff group and rank
			PostMasterPo tmpPost = requestSvc.getMPRSDefaultByHCMPositionId(hcmRecordPo.getPositionId());
			if (tmpPost.getDeptCode() != null && !"".equals(tmpPost.getDeptCode())) {
				vo.setDeptCode(tmpPost.getDeptCode());
			}
			
			if (tmpPost.getStaffGroupCode() != null && !"".equals(tmpPost.getStaffGroupCode())) {
				vo.setStaffGroupCode(tmpPost.getStaffGroupCode());
			}
			
			if (tmpPost.getRankCode() != null && !"".equals(tmpPost.getRankCode())) {
				vo.setRankCode(tmpPost.getRankCode());
			}
		}
		
	    return vo;
	}
	
	@RequestMapping(value="/request/deleteAttachment", method = RequestMethod.POST)
	public void deleteAttachment(@RequestParam("attachmentUid") String attachmentUid, 
												 HttpServletRequest request,
												 HttpServletResponse response) throws Exception {      
		EClaimLogger.info("deleteAttachment: " + attachmentUid);
		
		String userId = this.getSessionUser(request).getUserId();
		String currentRole = (String)request.getSession().getAttribute("currentRole");
		UserPo user = securitySvc.findUser(userId);
		user.setCurrentRole(currentRole);
		
		// Look up the attachment information
		RequestAttachmentPo attachment = requestSvc.getRequestAttachment(attachmentUid);
		
		requestSvc.deleteAttachment(Integer.parseInt(attachmentUid), user);
		
		// Delete the attachment physical
		String tmpDestPath = String.valueOf(attachment.getRequestUid());
		while (tmpDestPath.length() != 10) {
			tmpDestPath = "0" + tmpDestPath;
		}
		
		tmpDestPath = "rq_" + tmpDestPath;
		
		String destPath = APPROVAL_DOC_PATH + tmpDestPath + File.separator + attachment.getFileName();
		EClaimLogger.info("Delete attachment: " + destPath);
		File tmpFile = new File(destPath);
		tmpFile.delete();
	}
	
	@RequestMapping(value="/request/validateDeletion", method = RequestMethod.POST)
	public @ResponseBody String validateDeletion(@RequestParam("postUid[]") String[] postUid, 
												 @RequestParam("effectiveDate") String effectiveDate,
												 HttpServletRequest request,
												 HttpServletResponse response) throws Exception {      
		EClaimLogger.info("validateDeletion: " + postUid + "/" + effectiveDate);
		
		String errMsg = "";
		for (int i=0; i<postUid.length; i++) {
			errMsg = requestSvc.validateDeletion(Integer.parseInt(postUid[i]), 
			                                     new SimpleDateFormat("dd/MM/yyyy").parse(effectiveDate));
			System.out.println("errMsg: " + errMsg);
			if (errMsg != null && !"".equals(errMsg)) {
				return errMsg;
			}
		}
		
	    return "";
	}
	
	@RequestMapping(value="/request/validateFrozen", method = RequestMethod.POST)
	public @ResponseBody String validateFrozen(@RequestParam("postUid[]") String[] postUid, 
												 @RequestParam("effectiveDate") String effectiveDate,
												 HttpServletRequest request,
												 HttpServletResponse response) throws Exception {      
		EClaimLogger.info("validateFrozen: " + postUid + "/" + effectiveDate);
		
		String errMsg = "";
		for (int i=0; i<postUid.length; i++) {
			errMsg = requestSvc.validateFrozen(Integer.parseInt(postUid[i]), 
			                                   new SimpleDateFormat("dd/MM/yyyy").parse(effectiveDate));
			System.out.println("errMsg: " + errMsg);
			if (errMsg != null && !"".equals(errMsg)) {
				return errMsg;
			}
		}
		
	    return "";
	}
	
	@RequestMapping(value="/request/validateExtension", method = RequestMethod.POST)
	public @ResponseBody String validateExtension(@RequestParam("postUid[]") String[] postUid, 
												 @RequestParam("endDate") String endDate,
												 @RequestParam("value") String value,
												 @RequestParam("unit") String unit,
												 HttpServletRequest request,
												 HttpServletResponse response) throws Exception {      
		EClaimLogger.info("validateExtension: " + postUid + "/" + endDate);
		
		String errMsg = "";
		for (int i=0; i<postUid.length; i++) {
			Date tmpEndDate = null;
			if (endDate != null && !"".equals(endDate)) {
				tmpEndDate = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
			}
			
			errMsg = requestSvc.validateExtension(Integer.parseInt(postUid[i]), 
			                                     tmpEndDate, value, unit);
			System.out.println("errMsg: " + errMsg);
			if (errMsg != null && !"".equals(errMsg)) {
				return errMsg;
			}
		}
		
	    return "";
	}
	
	@RequestMapping(value="/request/redirect", method=RequestMethod.GET)
	public String redirect(@RequestParam("requestType") String requestType, 
			  			   @RequestParam("rq") String rq,
			 			   HttpServletRequest request,
			 			   HttpServletResponse response) {
		if ("".equals(rq) || rq == null || "".equals(requestType) || requestType == null) {
			return "redirect:/home/home";
		}
		
		try {
			int requestUid = Integer.parseInt(rq);
			
			if (MPRSConstant.REQUEST_TYPE_NEW_REQUEST.equals(requestType)) {
				return "redirect:/request/newPost?rq=" + requestUid;
			}
			else if (MPRSConstant.REQUEST_TYPE_DELETION.equals(requestType)) {
				return "redirect:/request/deletion?rq=" + requestUid;
			}
			else if (MPRSConstant.REQUEST_TYPE_FROZEN.equals(requestType)) {
				return "redirect:/request/frozen?rq=" + requestUid;
			}
			else if (MPRSConstant.REQUEST_TYPE_EXTENSION.equals(requestType)) {
				return "redirect:/request/extension?rq=" + requestUid;
			}
			else if (MPRSConstant.REQUEST_TYPE_UPGRADE.equals(requestType)) {
				return "redirect:/request/upgrade?rq=" + requestUid;
			}
			else if (MPRSConstant.REQUEST_TYPE_CHANGE_STAFF_MIX.equals(requestType)) {
				return "redirect:/request/changeStaffMix?rq=" + requestUid;
			}
			else if (MPRSConstant.REQUEST_TYPE_SUPPLEMENTARY_PROMOTION.equals(requestType)) {
				return "redirect:/request/suppPromotion?rq=" + requestUid;
			}
			else if (MPRSConstant.REQUEST_TYPE_CHANGE_FUNDING.equals(requestType)) {
				return "redirect:/request/changeOfFunding?rq=" + requestUid;
			}
			else {
				return "redirect:/home/home";
			}
		}
		catch (Exception ex) {
			return "redirect:/home/home";
		}
	}
	
	@RequestMapping(value="/api/request/getApprovalInfo", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody MPRSResultResponse getApprovalInfo(@ModelAttribute MPRSSearchCriteria mprSearchRequest) throws Exception {
		System.out.println("###### RequestController.getClusterRemark - Start ######");
		
		MPRSPostSnapPo po = requestSvc.getMPRSPostSnapDetail(Integer.parseInt(mprSearchRequest.getSearchPostSnapUid()));
		
		// Get the New Request
		RequestPo newRequest = requestSvc.getExistingRequestByMPRSPostId(po.getPostId());
		
		MPRSResultResponse response = new MPRSResultResponse();
		if (newRequest != null) {
			response.setRequestApprovalReference(newRequest.getApprovalReference()); 
			response.setRequestApprovalDate(DateTimeHelper.formatDateToString(newRequest.getApprovalDate()));
			response.setRequestApprovalRemark(newRequest.getApprovalRemark());
			System.out.println("XXX:" + DateTimeHelper.formatDateToString(newRequest.getApprovalDate()));
		}
		
		System.out.println("Get request : " + String.valueOf(po.getPostId()));

		return response;
	}
	
	@RequestMapping(value="/api/request/saveClusterRemark", method = RequestMethod.POST)
	public @ResponseBody String saveClusterRemark(@ModelAttribute MPRSSearchCriteria mprSearchRequest,
			@RequestParam("clusterRef") String clusterRef, 
			@RequestParam("clusterRemark") String clusterRemark,
			HttpServletRequest request) throws Exception {
		String userId = this.getSessionUser(request).getUserId();
		String currentRole = (String)request.getSession().getAttribute("currentRole");
		
		UserPo user = securitySvc.findUser(userId);
		user.setCurrentRole(currentRole);
		
		requestSvc.updateClusterRemark(mprSearchRequest.getSearchPostSnapUid(), clusterRef, clusterRemark, user);
		
		System.out.println("Finished Save");
		
		return "";
	}
	
	@RequestMapping(value="/api/request/getClusterRemark", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody MPRSResultResponse getClusterRemark(@ModelAttribute MPRSSearchCriteria mprSearchRequest) throws Exception {
		System.out.println("###### RequestController.getClusterRemark - Start ######");
		
//		MPRSPostSnapPo po = requestSvc.getMPRSPostSnapDetail(Integer.parseInt(mprSearchRequest.getSearchPostSnapUid()));
		
		MPRSResultResponse response = new MPRSResultResponse();
//		response.setClusterRefNo(po.getClusterRef());
//		response.setClusterRemark(po.getClusterRemark());
//		
		return response;
	}
}

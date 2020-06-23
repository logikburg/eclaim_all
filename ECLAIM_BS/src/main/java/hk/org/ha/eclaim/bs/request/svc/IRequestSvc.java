package hk.org.ha.eclaim.bs.request.svc;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import hk.org.ha.eclaim.bs.request.po.EnquiryWebPo;
import hk.org.ha.eclaim.bs.request.po.MPRSPostFundingResourceSnapPo;
import hk.org.ha.eclaim.bs.request.po.MPRSPostFundingSnapPo;
import hk.org.ha.eclaim.bs.request.po.MPRSPostSnapPo;
import hk.org.ha.eclaim.bs.request.po.MPRSSearchCriteria;
import hk.org.ha.eclaim.bs.request.po.PostMasterPo;
import hk.org.ha.eclaim.bs.request.po.RequestAttachmentPo;
import hk.org.ha.eclaim.bs.request.po.RequestEnquiryWebPo;
import hk.org.ha.eclaim.bs.request.po.RequestPo;
import hk.org.ha.eclaim.bs.request.po.RequestPostPo;
import hk.org.ha.eclaim.bs.request.po.RequestSystemFilePo;
import hk.org.ha.eclaim.bs.request.po.RequestTempFilePo;
import hk.org.ha.eclaim.bs.request.po.RequestWorkflowHistoryPo;
import hk.org.ha.eclaim.bs.request.po.RequestWorkflowRoutePo;
import hk.org.ha.eclaim.bs.security.po.DataAccessPo;
import hk.org.ha.eclaim.bs.security.po.RolePo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

public interface IRequestSvc {
	public int insert(RequestPo request, List<String> uploadFileIdList, boolean isLast, Integer sourceRequestUid, List<RequestAttachmentPo> cloneAttachmentList, UserPo user) throws Exception;
	
	public void update(RequestPo request, List<String> deleteAttachmentUid, List<String> uploadFileIdList, UserPo updatedBy) throws Exception;

	public RequestPo getRequestByRequestNo(int requestNo);
	
	public RequestPo getRequestByRequestNo(List<DataAccessPo> dataAccessList, int requestNo);
	
	public RequestPo getRequestByRequestNo(List<DataAccessPo> dataAccessList, int requestNo, boolean fromNewRequest);
	
	public String generateRequestNo(String cluster, String inst, String requestType, String year);
	
	public List<PostMasterPo> getMPRSPost(List<DataAccessPo> dataAccessList, MPRSSearchCriteria vo, String userId, String roleId);
	
	// Added for CR0355
	public List<PostMasterPo> getMPRSPostWithoutSupplementPromotion(List<DataAccessPo> dataAccessList, MPRSSearchCriteria vo, String userId, String roleId);
	
	public List<PostMasterPo> getMPRSResidentPost(List<DataAccessPo> dataAccessList, MPRSSearchCriteria vo, String userId, String roleId);
	
	public List<PostMasterPo> getVacancyMPRSPost(List<DataAccessPo> dataAccessList, MPRSSearchCriteria vo, String userId, String roleId);
	
	public List<PostMasterPo> getMPRSPostTimeLimit(List<DataAccessPo> dataAccessList, MPRSSearchCriteria vo);
	
	public List<PostMasterPo> getMPRSPostTimeLimitAndFTELessThanOne(List<DataAccessPo> dataAccessList, MPRSSearchCriteria vo);
	
	public List<PostMasterPo> getMPRSPost(List<DataAccessPo> dataAccessList, EnquiryWebPo vo, String userId, String roleId);
	
	public List<RequestPo> getMyRequest(List<DataAccessPo> dataAccessList, String userId, String roleId);
	
	public List<RequestPo> getMyTeamRequest(List<DataAccessPo> dataAccessList, String roleId, int userRoleUid);
	
	public List<RequestPo> getMyRecentRequest(String userId, String roleId);
	
	// Function used by function "Deletion"
	public String submitWorkflow(int requestNo, 
			String requestType,
								 String targetWFGroup, 
								 String targetWFUser, 
								 String returnCase,
								 String withEmail,
								 String emailTo,
								 String emailCC,
								 String emailTitle,
								 String emailContent, 
								 String userId, 
						            String roleId);
	
	public String submitWithdraw(int requestNo,
			String requestType, String userId, 
            String roleId);
	
	public String submitApprove(int requestNo,
			String requestType,
								String withEmail,
								String emailTo,
								String emailCC,
								String emailTitle,
								String emailContent, 
								String userId, 
					            String roleId);
	
	public PostMasterPo getPostByPostUid(int postUid);

	public String generateNewPostId(String staffGroup, String cluster, String inst, String dept, String rank,
			List<String> annualPlanInd, List<String> programType, String postDuration, String postFTEType, boolean reGenerateInd,
			String proposedPostId);
	
	public boolean hasApprovalRight(String staffGroupCode, String requestType, String currentStatus, String currentRole);
	
	public boolean hasApprovalRight(String staffGroupCode, String requestType, String currentStatus, String currentRole, String mgReviewInd);

	public List<RequestPo> getRequestByPostNo(String searchPostNo);
	
	public List<RequestPo> getExistingRequestByMPRSPostNo(String searchPostNo, String action);
	
	// Added for UT30027
	public List<RequestPo> searchRequest(List<DataAccessPo> dataAccessList, 
			                             RequestEnquiryWebPo po, 
			                             String userId,
			                             String currentRoleId);
	
	public RequestPo getNewRequestByPostId(String postId);

	public RequestAttachmentPo getRequestAttachment(String aid);
	
	public List<RequestAttachmentPo> getAttachmentByRequestUid(int requestUid);
	
	public List<RequestAttachmentPo> getAttachmentByPostUid(int postUid);
	
	public List<RolePo> getNextGroup(String staffGroupCode, String requestType, String currentStatus);

	public List<RolePo> getNextGroup(String staffGroupCode, String requestType, String currentStatus, String mgReviewInd);
	
	public List<RolePo> getPreviousGroup(String staffGroupCode, String requestType, String currentStatus);

	public List<RolePo> getPreviousGroup(String staffGroupCode, String requestType, String currentStatus, String mgReviewInd);

	public RequestWorkflowRoutePo getDefaultRoute(String staffGroupCode, String requestType, String currentStatus);
	
	public RequestWorkflowRoutePo getDefaultRoute(String staffGroupCode, String requestType, String currentStatus, String mgReviewInd);
	
	public int uploadRequestTempFile(RequestTempFilePo file, MultipartFile multipartFile) throws Exception;
	
	public int uploadPostFile(String postUid, RequestAttachmentPo file, MultipartFile multipartFile, UserPo user) throws Exception;
	
	public RequestSystemFilePo getSystemFile(String code);

	public RequestTempFilePo getTempFile(int uid);

	public RequestPostPo getRequestPositionByUid(int requestPositionUid);

	public PostMasterPo getMPRSDefaultByHCMPositionId(int hcmPositionId);

	public MPRSPostSnapPo getMPRSPostSnapDetail(int postSnapUid);
	
	public List<MPRSPostFundingSnapPo> getMPRSPostFundingSnapDetail(int postSnapUid);
	
	public MPRSPostFundingResourceSnapPo getMPRSPostFundingResourceSnapDetail(int postSnapUid);

	public void recycleProposedPostId(String staffGroup, String postDuration, String postFTEType, String tmpProposedPostId, String createdBy, String roleId);

	public void removePostFile(RequestAttachmentPo attachment, UserPo user);
	
	public List<RequestPo> getUpgradeRequestByPostId(String postId);
	
	public List<RequestPo> getChangeStaffMixRequestByPostId(String postId);
	
	public String validateDeletion(int postUid, Date effectiveDate);
	
	public String validateFrozen(int postUid, Date effectiveDate);

	public void deleteAttachment(int attachmentUid, UserPo user);

	public Date addMonth(Date postStartDate, int duration);
	
	public String validateExtension(int postUid, Date newPostEndDate, String value, String unit);
	
	public boolean validatePositionZeroHeadcount(String hcmPositionId, String effectiveStartDate, String effectiveEndDate);

	// Added for UT30027
	public List<RequestWorkflowHistoryPo> getRequestHistory(int requestUid);
	
	// public int getMPRSByHCMPositionId(String hcmPositionId, String effectiveDate);

	// Added for CC177246
	public void updateClusterRemark(String searchPostSnapUid, String clusterRef, String clusterRemark, UserPo user);

	// Added for UT30064
	public RequestPo getExistingRequestByMPRSPostId(String postId);
	
	// Added for ST08733
	public boolean isExistingPost(String postId);

	public List<String> getShortFallList();
	
}

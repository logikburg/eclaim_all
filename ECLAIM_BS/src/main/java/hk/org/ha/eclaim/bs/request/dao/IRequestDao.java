package hk.org.ha.eclaim.bs.request.dao;

import java.util.Date;
import java.util.List;

import hk.org.ha.eclaim.bs.request.po.RequestAttachmentPo;
import hk.org.ha.eclaim.bs.request.po.RequestEnquiryWebPo;
import hk.org.ha.eclaim.bs.request.po.RequestPo;
import hk.org.ha.eclaim.bs.security.po.DataAccessPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

public interface IRequestDao {
	int insert(RequestPo request, List<RequestAttachmentPo> cloneAttachmentList, UserPo user);
	void update(RequestPo request, UserPo user);
	RequestPo getRequestByRequestNo(int requestNo);
	
	RequestPo getRequestByRequestNo(List<DataAccessPo> dataAccessList, int requestNo, boolean fromNewRequest);
	
	String generateRequestNo(String cluster, String inst, String requestType, String year);
	
	// int generateRequestNo();
	
	// int generateNextPostNo(int requestNo);
	
	List<RequestPo> getMyRequest(List<DataAccessPo> dataAccessList, String userId, String roleId);
	
	List<RequestPo> getMyTeamRequest(List<DataAccessPo> dataAccessList, String roleId, int userRoleUid);
	
	List<RequestPo> getMyRecentRequest(String userId, String roleId);
	
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
	
	public String submitWithdraw(int requestNo, String requestType, String userId, 
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
	
	public String generateNewPostId(String staffGroup, String cluster, String inst, String dept, String rank, String programType, String duration);
	
	public List<RequestPo> getRequestByPostNo(String searchPostNo);
	
	public List<RequestPo> getExistingRequestByMPRSPostNo(String searchPostNo, String action);
	
	public void recycleProposedPostId(String staffGroup, String duration, String tmpProposedPostId, String createdBy, String roleId);
	
	public List<RequestPo> getUpgradeRequestByPostId(String postId);
	
	public List<RequestPo> getChangeStaffMixRequestByPostId(String postId);
	
	public RequestPo getNewRequestByPostId(String postId);
	
	public String validateDeletion(int postUid, Date effectiveDate);
	
	public String validateFrozen(int postUid, Date effectiveDate);
	
	public Date addMonth(Date postStartDate, int duration);
	
	public String validateExtension(int postUid, Date newPostEndDate);
	
	// Added for UT30027
	public List<RequestPo> searchRequest(List<DataAccessPo> dataAccessList, 
										 RequestEnquiryWebPo po, 
										 String userId,
										 String currentRoleId);
	// Added for UT30064
	public RequestPo getExistingRequestByMPRSPostId(String postId);	
}

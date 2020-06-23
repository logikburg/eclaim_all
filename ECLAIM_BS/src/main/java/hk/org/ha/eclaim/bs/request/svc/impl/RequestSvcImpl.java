package hk.org.ha.eclaim.bs.request.svc.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import hk.org.ha.eclaim.bs.request.constant.PostConstant;
import hk.org.ha.eclaim.bs.request.helper.PostHelper;
import hk.org.ha.eclaim.bs.request.po.EnquiryWebPo;
import hk.org.ha.eclaim.bs.request.po.MPRSPostFundingResourceSnapPo;
import hk.org.ha.eclaim.bs.request.po.MPRSPostFundingSnapPo;
import hk.org.ha.eclaim.bs.request.po.MPRSPostSnapPo;
import hk.org.ha.eclaim.bs.request.po.MPRSSearchCriteria;
import hk.org.ha.eclaim.bs.request.po.PostMasterPo;
import hk.org.ha.eclaim.bs.request.po.RequestAttachmentPo;
import hk.org.ha.eclaim.bs.request.po.RequestEnquiryWebPo;
import hk.org.ha.eclaim.bs.request.po.RequestFundingPo;
import hk.org.ha.eclaim.bs.request.po.RequestPo;
import hk.org.ha.eclaim.bs.request.po.RequestPostPo;
import hk.org.ha.eclaim.bs.request.po.RequestSystemFilePo;
import hk.org.ha.eclaim.bs.request.po.RequestTempFilePo;
import hk.org.ha.eclaim.bs.request.po.RequestWorkflowHistoryPo;
import hk.org.ha.eclaim.bs.request.po.RequestWorkflowRoutePo;
import hk.org.ha.eclaim.bs.request.svc.IRequestSvc;
import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.bs.request.dao.IMPRSPostDao;
import hk.org.ha.eclaim.bs.request.dao.IMPRSPostSnapDao;
import hk.org.ha.eclaim.bs.request.dao.IRequestAttachmentDao;
import hk.org.ha.eclaim.bs.request.dao.IRequestDao;
import hk.org.ha.eclaim.bs.request.dao.IRequestPositionDao;
import hk.org.ha.eclaim.bs.request.dao.IRequestSystemFileDao;
import hk.org.ha.eclaim.bs.request.dao.IRequestTempFileDao;
import hk.org.ha.eclaim.bs.request.dao.IRequestWorkflowHistoryDao;
import hk.org.ha.eclaim.bs.request.dao.IRequestWorkflowRouteDao;
import hk.org.ha.eclaim.bs.security.po.DataAccessPo;
import hk.org.ha.eclaim.bs.security.po.RolePo;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.core.logger.EClaimLogger;

@Service("requestSvc")
public class RequestSvcImpl implements IRequestSvc {

	@Value("${attachment.requestTmpDir}")
	private String DOC_PATH;
	
	@Value("${attachment.requestAttachDir}")
	private String APPROVAL_DOC_PATH;
	
	@Value("${attachment.postAttachDir}")
	private String POST_DOC_PATH;
	
	@Autowired (required=true)
	private IRequestDao requestDao;
	
	@Autowired (required=true)
	private IMPRSPostDao mprsPostDao;
	
	@Autowired
	private IRequestAttachmentDao requestAttachmentDao;
	
	@Autowired
	private IRequestWorkflowRouteDao requestWorkflowRouteDao;
	
	@Autowired
	private IRequestTempFileDao requestTempFileDao;
	
	@Autowired
	private IRequestSystemFileDao requestSystemFileDao;
	
	@Autowired
	private IRequestPositionDao requestPositionDao;
	
	@Autowired
	private IMPRSPostSnapDao mprsPostSnapDao;
	
	@Autowired
	private IRequestWorkflowHistoryDao requestWorkflowHistoryDao;

	@Transactional(rollbackFor={Exception.class})
	public int insert(RequestPo request, List<String> uploadFileIdList, boolean isLast, Integer sourceRequestUid, List<RequestAttachmentPo> cloneAttachmentList, UserPo user) throws Exception {
		int result = requestDao.insert(request, cloneAttachmentList, user);
		
		if (uploadFileIdList != null) {
			for (int i=0; i<uploadFileIdList.size(); i++) {
				if ("".equals(uploadFileIdList.get(i)))
					continue;
				
				// Perform upload file
				String tmpSourcePath = uploadFileIdList.get(i);
				while (tmpSourcePath.length() != 10) {
					tmpSourcePath = "0" + tmpSourcePath;
				}
				
				RequestTempFilePo tempFile = this.getTempFile(Integer.parseInt(uploadFileIdList.get(i)));
				tmpSourcePath = tmpSourcePath + File.separator + tempFile.getFilename();
				
				String tmpDestPath = String.valueOf(result);
				while (tmpDestPath.length() != 10) {
					tmpDestPath = "0" + tmpDestPath;
				}
				
				tmpDestPath = "rq_" + tmpDestPath;
				
				// Check is the folder exist
				File tmpPath = new File(APPROVAL_DOC_PATH + tmpDestPath);
				if (!tmpPath.exists()) {
					tmpPath.mkdirs();
				}		
				
				String destPath = APPROVAL_DOC_PATH + tmpDestPath + File.separator + tempFile.getFilename();
				File tmpFile = new File(DOC_PATH + tmpSourcePath);
				
				EClaimLogger.info("Perform name (source): " + tmpFile + ", (destination): " + destPath);
				
				// If last, rename the attachment, otherwise, just copy to the dest path
				if (sourceRequestUid == null && isLast) {
					if (!tmpFile.renameTo(new File(destPath))) {
						throw new Exception("Fail to upload attachment.");
					}
				}
				else {
					InputStream is = null;
					OutputStream os = null;
					
					byte[] buffer = new byte[1024];
					try {
						String sourcePath = "";
						
						if (sourceRequestUid != null) {
							String tmpSrcPath = String.valueOf(sourceRequestUid);
							while (tmpSrcPath.length() != 10) {
								tmpSrcPath = "0" + tmpSrcPath;
							}
							
							tmpSrcPath = "rq_" + tmpSrcPath;
							sourcePath = APPROVAL_DOC_PATH + tmpSrcPath + File.separator + tempFile.getFilename();
						}
						else {
							sourcePath = tmpFile.getAbsolutePath();
						}
						
						is = new FileInputStream(sourcePath);
						os = new FileOutputStream(destPath);
						
						int length;
						while ((length = is.read(buffer)) > 0) {
							os.write(buffer, 0, length);
						}
					}
					catch (Exception ex) {
						throw new Exception("Fail to upload attachment.");
					}
					finally {
						try { is.close(); } catch (Exception ignore) {}
						try { os.close(); } catch (Exception ignore) {}
					}
				}
			}
		}
		
		if (cloneAttachmentList != null) {
			for (int i=0; i<cloneAttachmentList.size(); i++) {
				String tmpSourcePath = String.valueOf(cloneAttachmentList.get(i).getRequestUid());
				while (tmpSourcePath.length() != 10) {
					tmpSourcePath = "0" + tmpSourcePath;
				}
				
				tmpSourcePath = "rq_" + tmpSourcePath;
				tmpSourcePath = APPROVAL_DOC_PATH + tmpSourcePath + File.separator + cloneAttachmentList.get(i).getFileName();
				
				String tmpDestPath = String.valueOf(result);
				while (tmpDestPath.length() != 10) {
					tmpDestPath = "0" + tmpDestPath;
				}
				
				tmpDestPath = "rq_" + tmpDestPath;
				
				// Check is the folder exist
				File tmpPath = new File(APPROVAL_DOC_PATH + tmpDestPath);
				if (!tmpPath.exists()) {
					tmpPath.mkdirs();
				}		
				
				String destPath = APPROVAL_DOC_PATH + tmpDestPath + File.separator + cloneAttachmentList.get(i).getFileName();
				File tmpFile = new File(tmpSourcePath);
				
				EClaimLogger.info("Perform name (source): " + tmpFile + ", (destination): " + destPath);
				
				// If last, rename the attachment, otherwise, just copy to the dest path
				InputStream is = null;
				OutputStream os = null;
					
				byte[] buffer = new byte[1024];
				try {
					is = new FileInputStream(tmpFile.getAbsolutePath());
					os = new FileOutputStream(destPath);
						
					int length;
					while ((length = is.read(buffer)) > 0) {
						os.write(buffer, 0, length);
					}
				}
				catch (Exception ex) {
					throw new Exception("Fail to upload attachment.");
				}
				finally {
					try { is.close(); } catch (Exception ignore) {}
					try { os.close(); } catch (Exception ignore) {}
				}
			}
		}
		
		return result;
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void update(RequestPo request, List<String> deleteAttachmentUid, List<String> uploadFileIdList, UserPo user) throws Exception {
		// Remove the attachment
		if (deleteAttachmentUid != null) {
			for (int i=0; i<deleteAttachmentUid.size(); i++) {
				System.out.println("deleteAttachmentUid: " + deleteAttachmentUid.get(i));
				RequestAttachmentPo attachment = requestAttachmentDao.getAttachmentByAttachmentId(Integer.parseInt(deleteAttachmentUid.get(i)));
				requestAttachmentDao.removeAttachment(attachment, user);
			}
		}

		requestDao.update(request, user);
		
		if (uploadFileIdList != null) {
			for (int i=0; i<uploadFileIdList.size(); i++) {
				if ("".equals(uploadFileIdList.get(i))) {
					continue;
				}
				
				// Perform upload file
				String tmpSourcePath = uploadFileIdList.get(i);
				while (tmpSourcePath.length() != 10) {
					tmpSourcePath = "0" + tmpSourcePath;
				}
				
				RequestTempFilePo tempFile = this.getTempFile(Integer.parseInt(uploadFileIdList.get(i)));
				tmpSourcePath = tmpSourcePath + File.separator + tempFile.getFilename();
				
				String tmpDestPath = String.valueOf(request.getRequestNo());
				while (tmpDestPath.length() != 10) {
					tmpDestPath = "0" + tmpDestPath;
				}
				
				tmpDestPath = "rq_" + tmpDestPath;
				
				// Check is the folder exist
				File tmpPath = new File(APPROVAL_DOC_PATH + tmpDestPath);
				if (!tmpPath.exists()) {
					tmpPath.mkdirs();
				}		
				
				String destPath = APPROVAL_DOC_PATH + tmpDestPath + File.separator + tempFile.getFilename();
				File tmpFile = new File(DOC_PATH + tmpSourcePath);
				
				EClaimLogger.info("perform name (source): " + tmpFile + ", (destination): " + destPath);
				
				if (!tmpFile.renameTo(new File(destPath))) {
					throw new Exception("Fail to upload attachment.");
				}
			}
		}
	}
	
	public RequestPo getRequestByRequestNo(int requestNo) {
		RequestPo requestCase = requestDao.getRequestByRequestNo(requestNo);
		
		// Look up the attachment
		List<RequestAttachmentPo> attachment = requestAttachmentDao.getAttachmentByRequestUid(requestNo);
		System.out.println("get attachment (" + requestNo + ") : " + attachment.size());
		if (attachment != null) {
			requestCase.setAttachment(attachment);
		}
		
		// Remove the Inactive funding related information 
		for (int i=0; i<requestCase.getRequestPositionList().size(); i++) {
			List<RequestFundingPo> fundingList = requestCase.getRequestPositionList().get(i).getRequestFundingList();
			for (int j=fundingList.size()-1; j>=0; j--) {
				if (!MPRSConstant.MPRS_STATE_ACTIVE.equals(fundingList.get(j).getRecState())) {
					fundingList.remove(j);
				}
			}
			
			requestCase.getRequestPositionList().get(i).setRequestFundingList(fundingList);
		}
		
		return requestCase;
	}
	
	public RequestPo getRequestByRequestNo(List<DataAccessPo> dataAccessList, int requestNo) {
		return getRequestByRequestNo(dataAccessList, requestNo, false);
	}
	
	public RequestPo getRequestByRequestNo(List<DataAccessPo> dataAccessList, int requestNo, boolean fromNewRequest) {
		RequestPo requestCase = requestDao.getRequestByRequestNo(dataAccessList, requestNo, fromNewRequest);
		
		if (requestCase != null) {
			// Look up the attachment
			List<RequestAttachmentPo> attachment = requestAttachmentDao.getAttachmentByRequestUid(requestNo);
			if (attachment != null) {
				requestCase.setAttachment(attachment);
			}
		}
		
		// Remove the Inactive funding related information 
		if (requestCase.getRequestPositionList() != null) {
			for (int i=0; i<requestCase.getRequestPositionList().size(); i++) {
				List<RequestFundingPo> fundingList = requestCase.getRequestPositionList().get(i).getRequestFundingList();
				for (int j=fundingList.size()-1; j>=0; j--) {
					if (!MPRSConstant.MPRS_STATE_ACTIVE.equals(fundingList.get(j).getRecState())) {
						fundingList.remove(j);
					}
				}
				
				requestCase.getRequestPositionList().get(i).setRequestFundingList(fundingList);
			}
		}
		
		return requestCase;
	}

	@Transactional
	public String generateRequestNo(String cluster, String inst, String requestType, String year) {
		return requestDao.generateRequestNo(cluster, inst, requestType, year);
	}
	
	public List<PostMasterPo> getMPRSPost(List<DataAccessPo> dataAccessList, MPRSSearchCriteria mprSearchRequest, String userId, String roleId) {
		return mprsPostDao.getMPRSPost(dataAccessList, mprSearchRequest, userId, roleId);
	}
	
	// Added for CR0355
	public List<PostMasterPo> getMPRSPostWithoutSupplementPromotion(List<DataAccessPo> dataAccessList, MPRSSearchCriteria mprSearchRequest, String userId, String roleId) {
		return mprsPostDao.getMPRSPostWithoutSupplementPromotions(dataAccessList, mprSearchRequest, userId, roleId);
	}
	
	public List<PostMasterPo> getVacancyMPRSPost(List<DataAccessPo> dataAccessList, MPRSSearchCriteria mprSearchRequest, String userId, String roleId) {
		return mprsPostDao.getVacancyMPRSPost(dataAccessList, mprSearchRequest, userId, roleId);
	}
	
	public List<PostMasterPo> getMPRSResidentPost(List<DataAccessPo> dataAccessList, MPRSSearchCriteria mprSearchRequest, String userId, String roleId) {
		return mprsPostDao.getMPRSResidentPost(dataAccessList, mprSearchRequest, userId, roleId);
	}

	public List<RequestPo> getMyRequest(List<DataAccessPo> dataAccessList, String userId, String roleId) {
		return requestDao.getMyRequest(dataAccessList, userId, roleId);
	}
	
	public List<RequestPo> getMyRecentRequest(String userId, String roleId) {
		return requestDao.getMyRecentRequest(userId, roleId);
	}
	
	public List<RequestPo> getMyTeamRequest(List<DataAccessPo> dataAccessList, String roleId, int userRoleUid) {
		return requestDao.getMyTeamRequest(dataAccessList, roleId, userRoleUid);
	}

	@Transactional
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
						            String roleId) {
		return requestDao.submitWorkflow(requestNo,
										 requestType,
				                         targetWFGroup, 
				                         targetWFUser,
				                         returnCase,
				                         withEmail,
				                         emailTo,
				                         emailCC,
				                         emailTitle,
				                         emailContent, 
				                         userId, 
								         roleId);
	}

	@Transactional
	public String submitWithdraw(int requestNo, String requestType, String userId, String roleId) {
		String msg = requestDao.submitWithdraw(requestNo, requestType, userId, roleId);
		
		if ("".equals(msg) || msg == null) {
			if (MPRSConstant.REQUEST_TYPE_NEW_REQUEST.equals(requestType)) {
				// Recycle the post ID - For New Request
				RequestPo requestCase = this.getRequestByRequestNo(requestNo);
				for (int i=0; i<requestCase.getRequestPositionList().size(); i++) {
					RequestPostPo post = requestCase.getRequestPositionList().get(i);
					if (post.getProposedPostId() != null && !"".equals(post.getProposedPostId())) {
						this.recycleProposedPostId(post.getStaffGroupCode(), post.getPostDuration(), post.getPostFTEType(), post.getProposedPostId(), userId, roleId);
					}
				}
			} else if (MPRSConstant.REQUEST_TYPE_CHANGE_STAFF_MIX.equals(requestType) || MPRSConstant.REQUEST_TYPE_UPGRADE.equals(requestType)) {
				// Recycle the post ID - For Upgrade / Change of Staff Mix
				RequestPo requestCase = this.getRequestByRequestNo(requestNo);
				for (int i=0; i<requestCase.getRequestPositionList().size(); i++) {
					RequestPostPo post = requestCase.getRequestPositionList().get(i);
					if (!"Y".equals(post.getToPostInd())) {
						continue;
					} else if (post.getProposedPostId() != null && !"".equals(post.getProposedPostId())) {
						this.recycleProposedPostId(post.getStaffGroupCode(), post.getPostDuration(), post.getPostFTEType(), post.getProposedPostId(), userId, roleId);
					}
				}
			}
		}
		
		return msg;
	}

	@Transactional
	public String submitApprove(int requestNo,
								String requestType,
								String withEmail,
								String emailTo,
								String emailCC,
								String emailTitle,
								String emailContent, 
								String userId, 
					            String roleId) {
		return requestDao.submitApprove(requestNo, 
										requestType,
										withEmail,
   						                emailTo,
						                emailCC,
						                emailTitle,
						                emailContent, 
						                userId, 
							            roleId);
	}

	public PostMasterPo getPostByPostUid(int postUid) {
		return mprsPostDao.getPostByPostUid(postUid);
	}

	public List<PostMasterPo> getMPRSPost(List<DataAccessPo> dataAccessList, EnquiryWebPo vo, String userId, String roleId) {
		return mprsPostSnapDao.getMPRSPostSnap(dataAccessList, vo, userId, roleId);
	}
	
	public List<PostMasterPo> getMPRSPostTimeLimit(List<DataAccessPo> dataAccessList, MPRSSearchCriteria vo){ 
		return mprsPostDao.getMPRSPostTimeLimit(dataAccessList, vo);
	}
	
	public List<PostMasterPo> getMPRSPostTimeLimitAndFTELessThanOne(List<DataAccessPo> dataAccessList, MPRSSearchCriteria vo) {
		return mprsPostDao.getMPRSPostTimeLimitAndFTELessThanOne(dataAccessList, vo);
		
	}

	@Transactional
	public String generateNewPostId(String staffGroup, String cluster, String inst, String dept, String rank,
			List<String> annualPlanInd, List<String> programType, String postDuration, String postFTEType, boolean reGenerateInd,
			String proposedPostId) {
		
		String segment5 = PostHelper.getSegment5(annualPlanInd, programType);
		String segment6 = PostHelper.getSegment6(postDuration, postFTEType);

		if (reGenerateInd) {
			System.out.println("Generate new Proposed ID");
			
			// Generate a new Post ID
			return requestDao.generateNewPostId(staffGroup, cluster, inst, dept, rank, segment5, segment6);
		} else {
			System.out.println("Modified existing Proposed ID");
			System.out.println("original proposed post id: " + proposedPostId);
			
			// Get the last sequence no.
			String segment7 = PostHelper.getSegment7(proposedPostId);
			System.out.println("seqTxt: " + segment7);
			
			if ("HCH".equals(cluster)){
				cluster = "KCC";
			}

			if (PostConstant.STAFF_GROUP_MEDICAL.equals(staffGroup)) {
				return cluster + "-" + inst + "-" + dept + "-" + rank + "-" + segment5 + "-" + segment6 + "-" + segment7;
			}
			else {
				return cluster + "-" + inst + "-" + dept + "-" + rank + "-" + segment7;
			}
		}
	}

	public boolean hasApprovalRight(String staffGroupCode, String requestType, String currentStatus, String currentRole) {
		// Get the workflow route
		System.out.println("hasApprovalRight: " + staffGroupCode + "/" + requestType + "/" + currentStatus + "/" + staffGroupCode);
		RequestWorkflowRoutePo route = requestWorkflowRouteDao.getDefaultRoute(staffGroupCode, requestType, currentStatus);
		
		if ("".equals(route.getSubmitToRole()) || route.getSubmitToRole() == null) {
			return true;
		}
		
		return false;
	}
	
	public boolean hasApprovalRight(String staffGroupCode, String requestType, String currentStatus, String currentRole, String mgReviewInd) {
		// Get the workflow route
		RequestWorkflowRoutePo route = requestWorkflowRouteDao.getDefaultRoute(staffGroupCode, requestType, currentStatus, mgReviewInd);
		System.out.println("hasApprovalRight 2: " + staffGroupCode + "/" + requestType + "/" + currentStatus + "/" + mgReviewInd);
		if (route == null) {
			route = requestWorkflowRouteDao.getDefaultRoute(staffGroupCode, requestType, currentStatus);
		}
		EClaimLogger.info("hasApprovalRight (result): " + route.getSubmitToRole());
		if ("".equals(route.getSubmitToRole()) || route.getSubmitToRole() == null) {
			return true;
		}

		return false;
	}

	public List<RequestPo> getRequestByPostNo(String searchPostNo) {
		return requestDao.getRequestByPostNo(searchPostNo);
	}
	
	public List<RequestPo> getExistingRequestByMPRSPostNo(String searchPostNo, String action) {
		return requestDao.getExistingRequestByMPRSPostNo(searchPostNo, action);
	}
	
	// Added for UT30027
	public List<RequestPo> searchRequest(List<DataAccessPo> dataAccessList, 
            RequestEnquiryWebPo po, 
            String userId,
            String currentRoleId) {
		return requestDao.searchRequest(dataAccessList, po, userId, currentRoleId);
	}

	public RequestAttachmentPo getRequestAttachment(String aid) {
		return requestAttachmentDao.getAttachmentByAttachmentId(Integer.parseInt(aid));
	}
	
	public List<RequestAttachmentPo> getAttachmentByRequestUid(int requestUid) {
		return requestAttachmentDao.getAttachmentByRequestUid(requestUid);
	}
	
	public List<RequestAttachmentPo> getAttachmentByPostUid(int postUid) {
		return requestAttachmentDao.getAttachmentByPostUid(postUid);
	}

	public List<RolePo> getNextGroup(String staffGroupCode, String requestType, String currentStatus) {
		return requestWorkflowRouteDao.getNextGroup(staffGroupCode, requestType, currentStatus);
	}
	
	public List<RolePo> getNextGroup(String staffGroupCode, String requestType, String currentStatus, String mgReviewInd) {
		List<RolePo> list = requestWorkflowRouteDao.getNextGroup(staffGroupCode, requestType, currentStatus, mgReviewInd);
		
		if (list.size() == 0) {
			list = requestWorkflowRouteDao.getNextGroup(staffGroupCode, requestType, currentStatus);
		}
		return list;
	}

	public List<RolePo> getPreviousGroup(String staffGroupCode, String requestType, String currentStatus) {
		return requestWorkflowRouteDao.getPreviousGroup(staffGroupCode, requestType, currentStatus);
	}

	public List<RolePo> getPreviousGroup(String staffGroupCode, String requestType, String currentStatus, String mgReviewInd) {
		List<RolePo> list = requestWorkflowRouteDao.getPreviousGroup(staffGroupCode, requestType, currentStatus, mgReviewInd);
		
		if (list.size() == 0) {
			list = requestWorkflowRouteDao.getPreviousGroup(staffGroupCode, requestType, currentStatus);
		}
		return list;
	}

	public RequestWorkflowRoutePo getDefaultRoute(String staffGroupCode, String requestType, String currentStatus) {
		return requestWorkflowRouteDao.getDefaultRoute(staffGroupCode, requestType, currentStatus);
	}
	
	public RequestWorkflowRoutePo getDefaultRoute(String staffGroupCode, String requestType, String currentStatus, String mgReviewInd) {
		EClaimLogger.info("getDefaultRoute- staffGroupCode:" + staffGroupCode + "/requestType:" + requestType + "/currentStatus:" + currentStatus + "/mgReviewInd:" + mgReviewInd);
		RequestWorkflowRoutePo result = requestWorkflowRouteDao.getDefaultRoute(staffGroupCode, requestType, currentStatus, mgReviewInd);
		if (result == null) {
			result = requestWorkflowRouteDao.getDefaultRoute(staffGroupCode, requestType, currentStatus);
		}
		
		return result;
	}

	@Transactional(rollbackFor={Exception.class})
	public int uploadRequestTempFile(RequestTempFilePo file, MultipartFile multipartFile) throws Exception {
		
		String fileName = file.getFilename();
		
		if (fileName != null && !fileName.contains("\\") && !fileName.contains("/")) {
			int result =  requestTempFileDao.insert(file);
			
			String docUidStr = String.valueOf(result);
			while (docUidStr.length() != 10) {
				docUidStr = "0" + docUidStr;
			}
			
			String folderPath = DOC_PATH + docUidStr;
			String filePath = file.getFilename();
			EClaimLogger.info("### uploadRequestTempFile: filePath=" + filePath);
			
			// Create new Document
			File newFile = new File(folderPath, filePath);
			System.out.println("### uploadRequestTempFile: newFile.getPath()=" + newFile.getPath());
			System.out.println("### uploadRequestTempFile: newFile.getAbsolutePath()=" + newFile.getAbsolutePath());
			System.out.println("### uploadRequestTempFile: newFile.getCanonicalPath()=" + newFile.getCanonicalPath());
			
			// Check is the folder exist
			File tmpPath = new File(folderPath);
			if (!tmpPath.exists()) {
				tmpPath.mkdirs();
			}
					
			if (!newFile.exists()) {
				newFile.createNewFile();
			}
			
			OutputStream optStream = null;
			try {
				optStream = new FileOutputStream(newFile);
				optStream.write(multipartFile.getBytes());
				optStream.flush();
			}
			catch (Exception ex) {
				EClaimLogger.error("doUpload:" + ex.getMessage(), ex);
				throw ex;
			}
			finally {
				try { optStream.close(); } catch (Exception ignore) {}
			}
			
			return result;
			
		}
		else {
			throw new IllegalArgumentException();
		}
		
	}
	
	@Transactional(rollbackFor={Exception.class})
	public int uploadPostFile(String postUid, RequestAttachmentPo file, MultipartFile multipartFile, UserPo user) throws Exception  {
		String fileName = file.getFileName();
		
		if (fileName != null && !fileName.contains("\\") && !fileName.contains("/")) {
			
			String postUidStr = postUid;
			while (postUidStr.length() != 10) {
				postUidStr = "0" + postUidStr;
			}
			
			String filePath = "post_" + postUidStr + File.separator + fileName;
			file.setFilePath(filePath);
			int result = requestAttachmentDao.insertAttachment(file, user);
			
			EClaimLogger.info("uploadPostFile=" + postUidStr);
			// Check is the folder exist
			String absoluteFilePath = POST_DOC_PATH + "post_" + postUidStr;
			File tmpPath = new File(absoluteFilePath);
			if (!tmpPath.exists()) {
				tmpPath.mkdirs();
			}

			// Create new Document
			File newFile = new File(absoluteFilePath, fileName);
			if (!newFile.exists()) {
				newFile.createNewFile();
			}
			
			OutputStream optStream = null;
			try {
				optStream = new FileOutputStream(newFile);
				optStream.write(multipartFile.getBytes());
				optStream.flush();
			}
			catch (Exception ex) {
				EClaimLogger.error("Upload Post File:" + ex.getMessage(), ex);
				throw ex;
			}
			finally {
				try { optStream.close(); } catch (Exception ignore) {}
			}
			
			return result;
		}
		else {
			throw new IllegalArgumentException();
		}
		
	}
	
	public RequestSystemFilePo getSystemFile(String code) {
		return requestSystemFileDao.getSystemFile(code);
	}
	
	public RequestTempFilePo getTempFile(int uid) {
		return requestTempFileDao.getTempFile(uid);
	}

	public RequestPostPo getRequestPositionByUid(int requestPositionUid) {
		return requestPositionDao.getRequestPositionByUid(requestPositionUid);
	}
	
	public PostMasterPo getMPRSDefaultByHCMPositionId(int hcmPositionId) {
		return mprsPostDao.getMPRSDefaultByHCMPositionId(hcmPositionId);
	}
	
	public MPRSPostSnapPo getMPRSPostSnapDetail(int postSnapUid) {
		return mprsPostSnapDao.getMPRSPostSnapDetail(postSnapUid);
	}
	
	public List<MPRSPostFundingSnapPo> getMPRSPostFundingSnapDetail(int postSnapUid) {
		return mprsPostSnapDao.getMPRSPostFundingSnapDetail(postSnapUid);
	}
	
	public MPRSPostFundingResourceSnapPo getMPRSPostFundingResourceSnapDetail(int postSnapUid) {
		return mprsPostSnapDao.getMPRSPostFundingResourceSnapDetail(postSnapUid);
	}

	@Transactional
	public void recycleProposedPostId(String staffGroup, String postDuration, String postFTEType, String tmpProposedPostId, String createdBy, String roleId) {
		String segment6 = PostHelper.getSegment6(postDuration, postFTEType);
		requestDao.recycleProposedPostId(staffGroup, segment6, tmpProposedPostId, createdBy, roleId);
	}

	@Transactional
	public void removePostFile(RequestAttachmentPo attachment, UserPo user) {
		requestAttachmentDao.removeAttachment(attachment, user);
	}
	
	public List<RequestPo> getUpgradeRequestByPostId(String postId) {
		return requestDao.getUpgradeRequestByPostId(postId);
	}
	
	public List<RequestPo> getChangeStaffMixRequestByPostId(String postId) {
		return requestDao.getChangeStaffMixRequestByPostId(postId);
	}
	
	public RequestPo getNewRequestByPostId(String postId) {
		return requestDao.getNewRequestByPostId(postId);
	}
	
	@Transactional
	public String validateDeletion(int postUid, Date effectiveDate) {
		return requestDao.validateDeletion(postUid, effectiveDate);
	}
	
	@Transactional
	public String validateFrozen(int postUid, Date effectiveDate) {
		return requestDao.validateFrozen(postUid, effectiveDate);
	}

	@Transactional
	public void deleteAttachment(int attachmentUid, UserPo user) {
		RequestAttachmentPo attachment = requestAttachmentDao.getAttachmentByAttachmentId(attachmentUid);
		requestAttachmentDao.removeAttachment(attachment, user);
	}

	@Transactional
	public Date addMonth(Date postStartDate, int duration) {
		return requestDao.addMonth(postStartDate, duration);
	}
	
	@Transactional
	public String validateExtension(int postUid, Date newPostEndDate, String value, String unit) {
		Date endDate = null;
		if (newPostEndDate != null) {
			endDate = newPostEndDate;
		}
		else {
			int month = 0;
			if ("Y".equals(unit)) {
				month = Integer.parseInt(value) * 12;
			}
			else {
				month = Integer.parseInt(value);
			}
			
			PostMasterPo po = this.getPostByPostUid(postUid);
			endDate = this.addMonth(po.getPostStartDate(), month);
		}
		System.out.println("endDate: " + endDate);
		
		return requestDao.validateExtension(postUid, endDate);
	}
	
	public boolean validatePositionZeroHeadcount(String hcmPositionId, String effectiveStartDate, String effectiveEndDate) {
		int activePostCount = mprsPostSnapDao.getActivePostCountByHcmPositionId(hcmPositionId, effectiveStartDate, effectiveEndDate);
		return activePostCount == 0;
	}

	// ADded for UT30027
	public List<RequestWorkflowHistoryPo> getRequestHistory(int requestUid) {
		return requestWorkflowHistoryDao.getRequestHistory(requestUid);
	}
	
	/*public int getMPRSByHCMPositionId(String hcmPositionId, String effectiveDate) {
		return mprsPostDao.getMPRSByHCMPositionId(hcmPositionId, effectiveDate);
	}*/
	
	// Added for CC177246
	@Transactional
	public void updateClusterRemark(String searchPostSnapUid, String clusterRef, String clusterRemark, UserPo user) {
		MPRSPostSnapPo po = getMPRSPostSnapDetail(Integer.parseInt(searchPostSnapUid));
		int postUid = po.getPostUid();
		
		// Update the POST_MASTER
		mprsPostDao.updateClusterRemark(postUid, clusterRef, clusterRemark, user);
		
		// Update the POST_MASTER_SNAP
		List<MPRSPostSnapPo> snapList = mprsPostSnapDao.getMPRSPostSnapDetailListByPostUid(postUid);
		
		for (int x=0; x<snapList.size(); x++) {
			mprsPostSnapDao.updateClusterRemark(String.valueOf(snapList.get(x).getPostSnapUid()), clusterRef, clusterRemark, user);
		}
	}
	
	// Added for UT30064
	public RequestPo getExistingRequestByMPRSPostId(String postId) {
		return requestDao.getExistingRequestByMPRSPostId(postId);
	}
	
	// Added for ST08733
	public boolean isExistingPost(String postId) {
		return mprsPostSnapDao.isExistingPost(postId);
	}
	
	public List<String> getShortFallList() {
		return mprsPostDao.getShortFallList();
	}
}

package hk.org.ha.eclaim.bs.request.dao.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.request.po.RequestAttachmentPo;
import hk.org.ha.eclaim.bs.request.po.RequestEnquiryWebPo;
import hk.org.ha.eclaim.bs.request.po.RequestFundingPo;
import hk.org.ha.eclaim.bs.request.po.RequestFundingResourcePo;
import hk.org.ha.eclaim.bs.request.po.RequestPo;
import hk.org.ha.eclaim.bs.request.po.RequestPostPo;
import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.bs.report.helper.ReportSubqueryHelper;
import hk.org.ha.eclaim.bs.request.dao.IRequestDao;
import hk.org.ha.eclaim.bs.security.po.DataAccessPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

@Repository
public class RequestDaoImpl implements IRequestDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	public int insert(RequestPo request, List<RequestAttachmentPo> cloneAttachmentList, UserPo user) {
		System.out.println("Perform RequestDaoImpl.insert");
		System.out.println("Current Role Id: " + user.getCurrentRole() );
		Date updateDate = new Date();
		request.setCreatedBy(user.getUserId());
		request.setCreatedRoleId(user.getCurrentRole());
		request.setCreatedDate(updateDate);
		request.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
		
		request.setUpdatedBy(user.getUserId());
		request.setUpdatedRoleId(user.getCurrentRole());
		request.setUpdatedDate(updateDate);
		
		// Update the child
		for (int i=0; i<request.getRequestPositionList().size(); i++) {
			request.getRequestPositionList().get(i).setCreatedBy(user.getUserId());
			request.getRequestPositionList().get(i).setCreatedRoleId(user.getCurrentRole());
			request.getRequestPositionList().get(i).setCreatedDate(updateDate);
			request.getRequestPositionList().get(i).setUpdatedBy(user.getUserId());
			request.getRequestPositionList().get(i).setUpdatedRoleId(user.getCurrentRole());
			request.getRequestPositionList().get(i).setUpdatedDate(updateDate);
			request.getRequestPositionList().get(i).setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
			
			for (int m=0; m<request.getRequestPositionList().get(i).getRequestFundingList().size(); m++) {
				if (request.getRequestPositionList().get(i).getRequestFundingList().get(m) != null) {
					request.getRequestPositionList().get(i).getRequestFundingList().get(m).setCreatedBy(user.getUserId());
					request.getRequestPositionList().get(i).getRequestFundingList().get(m).setCreatedDate(updateDate);
					request.getRequestPositionList().get(i).getRequestFundingList().get(m).setCreatedRoleId(user.getCurrentRole());
					request.getRequestPositionList().get(i).getRequestFundingList().get(m).setUpdatedBy(user.getUserId());
					request.getRequestPositionList().get(i).getRequestFundingList().get(m).setUpdatedRoleId(user.getCurrentRole());
					request.getRequestPositionList().get(i).getRequestFundingList().get(m).setUpdatedDate(updateDate);
					request.getRequestPositionList().get(i).getRequestFundingList().get(m).setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
				}
			}
			
//			if (request.getRequestPositionList().get(i).getRequestFunding() != null) {
//				request.getRequestPositionList().get(i).getRequestFunding().setCreatedBy(user.getUserId());
//				request.getRequestPositionList().get(i).getRequestFunding().setCreatedDate(updateDate);
//				request.getRequestPositionList().get(i).getRequestFunding().setCreatedRoleId(user.getCurrentRole());
//				request.getRequestPositionList().get(i).getRequestFunding().setUpdatedBy(user.getUserId());
//				request.getRequestPositionList().get(i).getRequestFunding().setUpdatedRoleId(user.getCurrentRole());
//				request.getRequestPositionList().get(i).getRequestFunding().setUpdatedDate(updateDate);
//				request.getRequestPositionList().get(i).getRequestFunding().setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
//			}

			if (request.getRequestPositionList().get(i).getRequestFundingResource() != null) {
				request.getRequestPositionList().get(i).getRequestFundingResource().setCreatedBy(user.getUserId());
				request.getRequestPositionList().get(i).getRequestFundingResource().setCreatedRoleId(user.getCurrentRole());
				request.getRequestPositionList().get(i).getRequestFundingResource().setCreatedDate(updateDate);
				request.getRequestPositionList().get(i).getRequestFundingResource().setUpdatedBy(user.getUserId());
				request.getRequestPositionList().get(i).getRequestFundingResource().setUpdatedRoleId(user.getCurrentRole());
				request.getRequestPositionList().get(i).getRequestFundingResource().setUpdatedDate(updateDate);
				request.getRequestPositionList().get(i).getRequestFundingResource().setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
			}
		}
		
		entityManager.persist(request);

		if (request.getAttachment() != null) {
			for (int i=0; i<request.getAttachment().size(); i++) {
				RequestAttachmentPo attachment = new RequestAttachmentPo();
				attachment.setFileName(request.getAttachment().get(i).getFileName());
				attachment.setRequestUid(request.getRequestNo());
				attachment.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
				attachment.setCreatedBy(user.getUserId());
				attachment.setCreatedRoleId(user.getCurrentRole());
				attachment.setCreatedDate(new Date());
				attachment.setUpdatedBy(user.getUserId());
				attachment.setUpdatedRoleId(user.getCurrentRole());
				attachment.setUpdatedDate(new Date());

				String tmpRequestUid = String.valueOf(request.getRequestNo());
				while (tmpRequestUid.length() != 10) {
					tmpRequestUid = "0" + tmpRequestUid;
				}
				String filePath = "rq_" + tmpRequestUid + File.separator + attachment.getFileName();
				attachment.setFilePath(filePath);

				entityManager.persist(attachment);
			}
		}
		
		if (cloneAttachmentList != null) {
			for (int i=0; i<cloneAttachmentList.size(); i++) {
				RequestAttachmentPo attachment = new RequestAttachmentPo();
				attachment.setFileName(cloneAttachmentList.get(i).getFileName());
				attachment.setRequestUid(request.getRequestNo());
				attachment.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
				attachment.setCreatedBy(user.getUserId());
				attachment.setCreatedRoleId(user.getCurrentRole());
				attachment.setCreatedDate(new Date());
				attachment.setUpdatedBy(user.getUserId());
				attachment.setUpdatedRoleId(user.getCurrentRole());
				attachment.setUpdatedDate(new Date());

				String tmpRequestUid = String.valueOf(request.getRequestNo());
				while (tmpRequestUid.length() != 10) {
					tmpRequestUid = "0" + tmpRequestUid;
				}
				String filePath = "rq_" + tmpRequestUid + File.separator + attachment.getFileName();
				attachment.setFilePath(filePath);

				entityManager.persist(attachment);
			}
		}

		return request.getRequestNo();
	}

	public void update(RequestPo request, UserPo user) {
		List<RequestFundingResourcePo> fundingResourceList = new ArrayList<RequestFundingResourcePo>();
		
		// Process the attachment
		if (request.getAttachment() != null) {
			System.out.println("request.getAttachment(): " + request.getAttachment().size());
			for (int i=0; i<request.getAttachment().size(); i++) {
				RequestAttachmentPo attachment = request.getAttachment().get(i);
				attachment.setRequestUid(request.getRequestNo());
				attachment.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
				attachment.setCreatedBy(user.getUserId());
				attachment.setCreatedRoleId(user.getCurrentRole());
				attachment.setCreatedDate(new Date());
				attachment.setUpdatedBy(user.getUserId());
				attachment.setUpdatedRoleId(user.getCurrentRole());
				attachment.setUpdatedDate(new Date());
				
				String tmpRequestUid = String.valueOf(request.getRequestNo());
				while (tmpRequestUid.length() != 10) {
					tmpRequestUid = "0" + tmpRequestUid;
				}
				String filePath = "rq_" + tmpRequestUid + File.separator + attachment.getFileName();
				attachment.setFilePath(filePath);
				
				entityManager.persist(attachment);
			}
		}
		
		Date updateDate = new Date();
		request.setUpdatedBy(user.getUserId());
		request.setUpdatedRoleId(user.getCurrentRole());
		request.setUpdatedDate(updateDate);
		List<RequestFundingPo> fundingList = new ArrayList<RequestFundingPo>();
		
		for (int i = 0; i < request.getRequestPositionList().size(); i++) {
			
			if ("".equals(request.getRequestPositionList().get(i).getCreatedBy())
					|| request.getRequestPositionList().get(i).getCreatedBy() == null) {
				request.getRequestPositionList().get(i).setCreatedBy(user.getUserId());
				request.getRequestPositionList().get(i).setCreatedRoleId(user.getCurrentRole());
				request.getRequestPositionList().get(i).setCreatedDate(updateDate);
				request.getRequestPositionList().get(i).setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
				request.getRequestPositionList().get(i).setUpdatedBy(user.getUserId());
				request.getRequestPositionList().get(i).setUpdatedRoleId(user.getCurrentRole());
				request.getRequestPositionList().get(i).setUpdatedDate(updateDate);
			} 
			else {
				request.getRequestPositionList().get(i).setUpdatedBy(user.getUserId());
				request.getRequestPositionList().get(i).setUpdatedRoleId(user.getCurrentRole());
				request.getRequestPositionList().get(i).setUpdatedDate(updateDate);
			}

			if (request.getRequestPositionList().get(i).getRequestFundingList() != null) {
				for (int m=0; m<request.getRequestPositionList().get(i).getRequestFundingList().size(); m++) {
					if ("".equals(request.getRequestPositionList().get(i).getRequestFundingList().get(m).getCreatedBy())
						|| request.getRequestPositionList().get(i).getRequestFundingList().get(m).getCreatedBy() == null) {
						
						request.getRequestPositionList().get(i).getRequestFundingList().get(m).setCreatedBy(user.getUserId());
						request.getRequestPositionList().get(i).getRequestFundingList().get(m).setCreatedRoleId(user.getCurrentRole());
						request.getRequestPositionList().get(i).getRequestFundingList().get(m).setCreatedDate(updateDate);
						request.getRequestPositionList().get(i).getRequestFundingList().get(m).setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
						request.getRequestPositionList().get(i).getRequestFundingList().get(m).setUpdatedBy(user.getUserId());
						request.getRequestPositionList().get(i).getRequestFundingList().get(m).setUpdatedRoleId(user.getCurrentRole());
						request.getRequestPositionList().get(i).getRequestFundingList().get(m).setUpdatedDate(updateDate);
					} 
					else {
						request.getRequestPositionList().get(i).getRequestFundingList().get(m).setUpdatedBy(user.getUserId());
						request.getRequestPositionList().get(i).getRequestFundingList().get(m).setUpdatedRoleId(user.getCurrentRole());
						request.getRequestPositionList().get(i).getRequestFundingList().get(m).setUpdatedDate(updateDate);
					}
					
					fundingList.add(request.getRequestPositionList().get(i).getRequestFundingList().get(m));
				}
				
				request.getRequestPositionList().get(i).setRequestFundingList(null);
			}

			if (request.getRequestPositionList().get(i).getRequestFundingResource() != null) {
				if ("".equals(request.getRequestPositionList().get(i).getRequestFundingResource().getCreatedBy())
						|| request.getRequestPositionList().get(i).getRequestFundingResource().getCreatedBy() == null) {
					request.getRequestPositionList().get(i).getRequestFundingResource().setCreatedBy(user.getUserId());
					request.getRequestPositionList().get(i).getRequestFundingResource().setCreatedRoleId(user.getCurrentRole());
					request.getRequestPositionList().get(i).getRequestFundingResource().setCreatedDate(updateDate);
					request.getRequestPositionList().get(i).getRequestFundingResource().setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
					request.getRequestPositionList().get(i).getRequestFundingResource().setUpdatedBy(user.getUserId());
					request.getRequestPositionList().get(i).getRequestFundingResource().setUpdatedRoleId(user.getCurrentRole());
					request.getRequestPositionList().get(i).getRequestFundingResource().setUpdatedDate(updateDate);
				} 
				else {
					request.getRequestPositionList().get(i).getRequestFundingResource().setUpdatedBy(user.getUserId());
					request.getRequestPositionList().get(i).getRequestFundingResource().setUpdatedRoleId(user.getCurrentRole());
					request.getRequestPositionList().get(i).getRequestFundingResource().setUpdatedDate(updateDate);
				}
				
				fundingResourceList.add(request.getRequestPositionList().get(i).getRequestFundingResource());
				request.getRequestPositionList().get(i).setRequestFundingResource(null);
			}
			else {
				fundingResourceList.add(null);
			}
		}
		
		System.out.println("Perform Save - Start");
		Session session = entityManager.unwrap(Session.class);
		session.update(request);
		RequestPo tmpRequest = entityManager.find(RequestPo.class, request.getRequestNo());
		
		System.out.println("Perform Save - End");
		
		
		
		for (int m=0; m<tmpRequest.getRequestPositionList().size(); m++) {
		
			RequestPostPo rp1 = entityManager.find(RequestPostPo.class, tmpRequest.getRequestPositionList().get(m).getRequestPostId());
			
			List<RequestFundingPo> currentFundingList = new ArrayList<RequestFundingPo>();
			for (int n=0; n<fundingList.size(); n++) {
				if (fundingList.get(n).getRequestPosition() != null) {
					System.out.println("fundingList.get(n).getRequestPosition().getRequestPostId():" + fundingList.get(n).getRequestPosition().getRequestPostId());
					
					if (fundingList.get(n).getRequestPosition().getRequestPostId() == rp1.getRequestPostId()) {
						currentFundingList.add(fundingList.get(n));
					}
				}
				else {
					currentFundingList.add(fundingList.get(n));
				}
			}
			
			
			List<RequestFundingPo> latestFundingList = this.getRequestFundingPoList(tmpRequest.getRequestPositionList().get(m).getRequestPostId());
			if (latestFundingList.size() > currentFundingList.size()) {
				for (int g=currentFundingList.size(); g<latestFundingList.size(); g++) {
					latestFundingList.get(g).setRecState(MPRSConstant.MPRS_STATE_INACTIVE);
					entityManager.merge(latestFundingList.get(g));
				}
			}
		
			for (int x=0; x<currentFundingList.size(); x++) {
				if (currentFundingList.get(x) != null) {
					// Perform Update
					if (x < latestFundingList.size()) {
						latestFundingList.get(x).setAnnualPlanInd(currentFundingList.get(x).getAnnualPlanInd());
						latestFundingList.get(x).setProgramYear(currentFundingList.get(x).getProgramYear());
						latestFundingList.get(x).setProgramCode(currentFundingList.get(x).getProgramCode());
						latestFundingList.get(x).setProgramName(currentFundingList.get(x).getProgramName());
						latestFundingList.get(x).setProgramTypeCode(currentFundingList.get(x).getProgramTypeCode());
						latestFundingList.get(x).setFundSrcId(currentFundingList.get(x).getFundSrcId());
						latestFundingList.get(x).setFundSrcSubCatId(currentFundingList.get(x).getFundSrcSubCatId());
						latestFundingList.get(x).setFundSrcStartDate(currentFundingList.get(x).getFundSrcStartDate());
						latestFundingList.get(x).setFundSrcEndDate(currentFundingList.get(x).getFundSrcEndDate());
						latestFundingList.get(x).setFundSrcFte(currentFundingList.get(x).getFundSrcFte());
						latestFundingList.get(x).setFundSrcRemark(currentFundingList.get(x).getFundSrcRemark());
						latestFundingList.get(x).setCreatedBy(currentFundingList.get(x).getCreatedBy());
						latestFundingList.get(x).setCreatedRoleId(currentFundingList.get(x).getCreatedRoleId());
						latestFundingList.get(x).setCreatedDate(currentFundingList.get(x).getCreatedDate());
						latestFundingList.get(x).setUpdatedBy(currentFundingList.get(x).getUpdatedBy());
						latestFundingList.get(x).setUpdatedRoleId(currentFundingList.get(x).getUpdatedRoleId());
						latestFundingList.get(x).setUpdatedDate(currentFundingList.get(x).getUpdatedDate());
						latestFundingList.get(x).setInst(currentFundingList.get(x).getInst());
						latestFundingList.get(x).setSection(currentFundingList.get(x).getSection());
						latestFundingList.get(x).setAnalytical(currentFundingList.get(x).getAnalytical());
						latestFundingList.get(x).setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
						entityManager.merge(latestFundingList.get(x));
					}
					// Perform Insert
					else {
						rp1.addRequestFundingList(currentFundingList.get(x));
						entityManager.merge(currentFundingList.get(x));
					}
				}
			}
		}
		
		for (int y=0; y<fundingResourceList.size(); y++) {
			if (fundingResourceList.get(y) != null) {
				System.out.println("tmpRequest.getRequestPositionList().get(y).getRequestPostId(): " + tmpRequest.getRequestPositionList().get(y).getRequestPostId());
				RequestPostPo rp = entityManager.find(RequestPostPo.class, tmpRequest.getRequestPositionList().get(y).getRequestPostId());
				RequestFundingResourcePo rfr = entityManager.find(RequestFundingResourcePo.class, tmpRequest.getRequestPositionList().get(y).getRequestPostId());
				
				if (rfr != null) {
					rp.setRequestFundingResource(fundingResourceList.get(y));
					entityManager.merge(fundingResourceList.get(y));
				}
				else {
					rp.setRequestFundingResource(fundingResourceList.get(y));
					RequestFundingResourcePo fundingResource = new RequestFundingResourcePo();
					fundingResource.setResourcesSupportFrExt(fundingResourceList.get(y).getResourcesSupportFrExt());
					fundingResource.setResourcesSupportRemark(fundingResourceList.get(y).getResourcesSupportRemark());
					fundingResource.setCreatedBy(fundingResourceList.get(y).getCreatedBy());
					fundingResource.setCreatedRoleId(fundingResourceList.get(y).getCreatedRoleId());
					fundingResource.setCreatedDate(fundingResourceList.get(y).getCreatedDate());
					fundingResource.setUpdatedBy(fundingResourceList.get(y).getUpdatedBy());
					fundingResource.setUpdatedRoleId(fundingResourceList.get(y).getUpdatedRoleId());
					fundingResource.setUpdatedDate(fundingResourceList.get(y).getUpdatedDate());
					fundingResource.setRecState(fundingResourceList.get(y).getRecState());
					rp.setRequestFundingResource(fundingResource);
					entityManager.persist(fundingResource);
				}
				
				entityManager.merge(rp);
			}
		}
	}

	private List<RequestFundingPo> getRequestFundingPoList(int requestPostId) {
		List<RequestFundingPo> resultList = new ArrayList<RequestFundingPo>();
		String sql = "select c.* from RQ_FUNDING c where c.REQUEST_POST_UID = :requestPostId ";
		
		Query q = entityManager.createNativeQuery(sql, RequestFundingPo.class);
		q.setParameter("requestPostId", requestPostId);
		
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			RequestFundingPo c = (RequestFundingPo)result.next();
			
			resultList.add(c);
		}
		
		return resultList;
	}

	public RequestPo getRequestByRequestNo(int requestNo) {
		String sql = "";
		sql = "select distinct c.* from RQ_MASTER c, RQ_POST pm where c.request_uid = pm.request_uid and c.request_uid = :requestUid  ";
		
		Query q = entityManager.createNativeQuery(sql, RequestPo.class);
		q.setParameter("requestUid", requestNo);
		
		System.out.println("sql: " + sql);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		if (result.hasNext()) {
			return (RequestPo)result.next();
		}
		
		return null;
	}
	
	public RequestPo getRequestByRequestNo(List<DataAccessPo> dataAccessList, int requestNo, boolean fromNewRequest) {
		String sql = "";
		if (fromNewRequest) {
			sql = "select distinct c.* from RQ_MASTER c, RQ_POST pm where c.request_uid = pm.request_uid and c.request_uid = :requestUid  ";
			sql += " and c.REC_STATE = :recState and pm.REC_STATE = :recState ";
		}
		else {
			sql = "select distinct c.* from RQ_MASTER c, RQ_POST p, POST_MASTER pm where c.request_uid = p.request_uid and c.request_uid = :requestUid and p.POST_UID = pm.POST_UID ";
			sql += " and c.REC_STATE = :recState and p.REC_STATE = :recState and pm.REC_STATE = :recState ";
		}
		
		// Append the Data Access
		String sqlDataAccess = ReportSubqueryHelper.getDataAccessScopeQuery(dataAccessList, "pm");
		if(!sqlDataAccess.isEmpty()) {
			sql += " and ( ";
			sql += sqlDataAccess;
			sql += " ) ";
		}
		
		Query q = entityManager.createNativeQuery(sql, RequestPo.class);
		q.setParameter("requestUid", requestNo);
		
		for (int i = 0; i < dataAccessList.size(); i++) {
			DataAccessPo d = dataAccessList.get(i);
			if (d.getClusterCode() != null) {
				q.setParameter("cluster_code" + i, d.getClusterCode());
			}
			if (d.getInstCode() != null) {
				q.setParameter("inst_code" + i, d.getInstCode());
			}
			if (d.getDeptCode() != null) {
				q.setParameter("dept_code" + i, d.getDeptCode());
			}
			if (d.getStaffGroupCode() != null) {
				q.setParameter("staff_group_code" + i, d.getStaffGroupCode());
			}
		}
		
		q.setParameter("recState", MPRSConstant.MPRS_STATE_ACTIVE);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		if (result.hasNext()) {
			return (RequestPo)result.next();
		}
		
		return null;
	}

	public String generateRequestNo(String cluster, String inst, String requestType, String year) {
		System.out.println("generateRequestNo:" + cluster + "/"  + inst+ "/"  + requestType + "/" + year);
		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.GENERATE_REQUEST_ID");
		storedProcedure.registerStoredProcedureParameter("in_cluster_id", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_inst_id", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_request_type", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_year", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("out_request_id", String.class, ParameterMode.OUT);
		storedProcedure.setParameter("in_cluster_id", cluster);
		storedProcedure.setParameter("in_inst_id", inst);
		storedProcedure.setParameter("in_request_type", requestType);
		storedProcedure.setParameter("in_year", year);
		storedProcedure.execute();
		String result = (String)storedProcedure.getOutputParameterValue("out_request_id");
		
		return result;
	}

	public List<RequestPo> getMyRequest(List<DataAccessPo> dataAccessList, String userId, String roleId) {
		List<RequestPo> resultList = new ArrayList<RequestPo>();
		
		String sql = "select distinct m.* from RQ_MASTER m, RQ_POST p where m.REQUEST_UID = p.REQUEST_UID ";
		sql += " and ((m.CURRENT_WF_GROUP = :roleId and m.CURRENT_USER = :userId) or (m.CURRENT_WF_GROUP = :roleId and m.CURRENT_USER is null)) ";
		sql += " and m.rec_state = :recState and p.rec_state = :recState ";
		
		String sqlDataAccess = ReportSubqueryHelper.getDataAccessScopeQuery(dataAccessList, "p");
		if(!sqlDataAccess.isEmpty()) {
			sql += " and ( ";
			sql += sqlDataAccess;
			sql += " ) ";
		}
		sql += " order by m.REQUEST_ID desc";
		System.out.println("sql: " + sql);
		
		
		Query q = entityManager.createNativeQuery(sql, RequestPo.class);
		q.setParameter("userId", userId);
		q.setParameter("roleId", roleId);
		q.setParameter("recState", MPRSConstant.MPRS_STATE_ACTIVE);
		
		for (int i = 0; i < dataAccessList.size(); i++) {
			DataAccessPo d = dataAccessList.get(i);
			if (d.getClusterCode() != null) {
				q.setParameter("cluster_code" + i, d.getClusterCode());
			}
			if (d.getInstCode() != null) {
				q.setParameter("inst_code" + i, d.getInstCode());
			}
			if (d.getDeptCode() != null) {
				q.setParameter("dept_code" + i, d.getDeptCode());
			}
			if (d.getStaffGroupCode() != null) {
				q.setParameter("staff_group_code" + i, d.getStaffGroupCode());
			}
		}
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			RequestPo c = (RequestPo)result.next();
			
			resultList.add(c);
		}
		
		return resultList;
	}
	
	public List<RequestPo> getMyTeamRequest(List<DataAccessPo> dataAccessList, String roleId, int userRoleUid) {
		List<RequestPo> resultList = new ArrayList<RequestPo>();
		
		String sql = "select distinct m.* from RQ_MASTER m, RQ_POST p where m.REQUEST_UID = p.REQUEST_UID "; 
		
		sql += " and m.CURRENT_WF_GROUP = :roleId ";
		sql += " and m.CURRENT_USER <> (select user_id from SS_USER_ROLE where USER_ROLE_UID = :userRoleUid and rec_state = :recState) ";
		sql += " and m.rec_state = :recState and p.rec_state = :recState ";
		String sqlDataAccess = ReportSubqueryHelper.getDataAccessScopeQuery(dataAccessList, "p");
		if(!sqlDataAccess.isEmpty()) {
			sql += " and ( ";
			sql += sqlDataAccess;
			sql += " ) ";
		}
		
		sql += " order by m.REQUEST_ID ";
		System.out.println("sql: " + sql);
		
		
		Query q = entityManager.createNativeQuery(sql, RequestPo.class);
		q.setParameter("roleId", roleId);
		q.setParameter("userRoleUid", userRoleUid);
		q.setParameter("recState", MPRSConstant.MPRS_STATE_ACTIVE);
		
		for (int i = 0; i < dataAccessList.size(); i++) {
			DataAccessPo d = dataAccessList.get(i);
			if (d.getClusterCode() != null) {
				q.setParameter("cluster_code" + i, d.getClusterCode());
			}
			if (d.getInstCode() != null) {
				q.setParameter("inst_code" + i, d.getInstCode());
			}
			if (d.getDeptCode() != null) {
				q.setParameter("dept_code" + i, d.getDeptCode());
			}
			if (d.getStaffGroupCode() != null) {
				q.setParameter("staff_group_code" + i, d.getStaffGroupCode());
			}
		}
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			RequestPo c = (RequestPo)result.next();
			
			resultList.add(c);
		}
		
		return resultList;
	}
	
	public List<RequestPo> getMyRecentRequest(String userId, String roleId) {
		List<RequestPo> resultList = new ArrayList<RequestPo>();
//		String sql = " select * from RQ_MASTER C "; 
//		sql += " where REQUEST_UID in (select REQUEST_UID from XXEC_WORKFLOW_HIST where ACTION_BY = :userId  "; 
//		sql += " and ACTION_ROLE_ID = :roleId and REC_STATE = 'A' )  ";
//		sql += " and ((not (((C.CURRENT_WF_GROUP = :roleId and C.CURRENT_USER = :userId ) or (C.CURRENT_WF_GROUP = :roleId and C.CURRENT_USER is null)))  ) ";
//		sql += " or (C.CURRENT_WF_GROUP is null and C.CURRENT_USER is null ) ";
//		sql += " ) ";

//		Query q = entityManager.createNativeQuery(sql, RequestPo.class);
//		q.setParameter("userId", userId);
//		q.setParameter("roleId", roleId);
//		
//		@SuppressWarnings("rawtypes")
//		Iterator result = q.getResultList().iterator();
//		
//		while (result.hasNext()) {
//			RequestPo c = (RequestPo)result.next();
//			
//			resultList.add(c);
//		}
		
		return resultList;
	}

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
		String errMsg = "";
		
		StoredProcedureQuery storedProcedure = null;
		
		if ("NEW".equals(requestType)) {
			storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_new_post");
		}
		else if ("DEL".equals(requestType)) {
			storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_deletion");
		}
		else if (MPRSConstant.REQUEST_TYPE_CHANGE_FUNDING.equals(requestType)) {
			storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_change_funding");
		}
		else if (MPRSConstant.REQUEST_TYPE_EXTENSION.equals(requestType)) {
			storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_extension");
		}
		else if (MPRSConstant.REQUEST_TYPE_SUPPLEMENTARY_PROMOTION.equals(requestType)) {
			storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_supplementary_promotion");
		}
		else if (MPRSConstant.REQUEST_TYPE_UPGRADE.equals(requestType)) {
			storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_upgrade");
		}
		else if (MPRSConstant.REQUEST_TYPE_CHANGE_STAFF_MIX.equals(requestType)) {
			storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_change_staff_mix");
		}
		else if (MPRSConstant.REQUEST_TYPE_FROZEN.equals(requestType)) {
			storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_frozen");
		}
		else if (MPRSConstant.REQUEST_TYPE_CHANGE_HCM_POST.equals(requestType)) {
			storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_change_hcm_post");
		}
		else if (MPRSConstant.REQUEST_TYPE_FTE_ADJUSTMENT.equals(requestType)) {
			storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_fte_adjustment");
		}
		
		storedProcedure.registerStoredProcedureParameter("in_request_uid", Integer.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_submit_to_group", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_submit_to_user", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_action", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_with_email", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_email_to", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_email_cc", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_email_title", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_email_content", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_user_id", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_role_id", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("out_err_msg", String.class, ParameterMode.OUT);
		System.out.println("requestNo:" + requestNo);
		storedProcedure.setParameter("in_request_uid", requestNo);
		System.out.println("targetWFGroup:" + targetWFGroup);
		storedProcedure.setParameter("in_submit_to_group", targetWFGroup);
		System.out.println("targetWFUser:" + targetWFUser);
		storedProcedure.setParameter("in_submit_to_user", targetWFUser);
		
		if ("Y".equals(returnCase)) {
			storedProcedure.setParameter("in_action", "RETURN");
		}
		else {
			storedProcedure.setParameter("in_action", "SUBMIT");
		}
		System.out.println("withEmail:" + withEmail);
		storedProcedure.setParameter("in_with_email", withEmail);
		System.out.println("emailTo:" + emailTo);
		storedProcedure.setParameter("in_email_to", emailTo);
		System.out.println("emailCC:" + emailCC);
		storedProcedure.setParameter("in_email_cc", emailCC);
		System.out.println("emailTitle:" + emailTitle);
		storedProcedure.setParameter("in_email_title", emailTitle);
		System.out.println("emailContent:" + emailContent);
		storedProcedure.setParameter("in_email_content", emailContent);
		System.out.println("userId:" + userId);
		storedProcedure.setParameter("in_user_id", userId);
		System.out.println("roleId:" + roleId);
		storedProcedure.setParameter("in_role_id", roleId);
		storedProcedure.execute();
		errMsg = (String)storedProcedure.getOutputParameterValue("out_err_msg");
		
		return errMsg;
	}

	public String submitWithdraw(int requestNo, String requestType, String userId, 
            String roleId) {
		String errMsg = "";
		
		StoredProcedureQuery storedProcedure = null;
		
		if ("NEW".equals(requestType)) {
			storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_new_post");
		}
		else if ("DEL".equals(requestType)) {
			storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_deletion");
		}
		else if (MPRSConstant.REQUEST_TYPE_CHANGE_FUNDING.equals(requestType)) {
			storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_change_funding");
		}
		else if (MPRSConstant.REQUEST_TYPE_EXTENSION.equals(requestType)) {
			storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_extension");
		}
		else if (MPRSConstant.REQUEST_TYPE_SUPPLEMENTARY_PROMOTION.equals(requestType)) {
			storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_supplementary_promotion");
		}
		else if (MPRSConstant.REQUEST_TYPE_UPGRADE.equals(requestType)) {
			storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_upgrade");
		}
		else if (MPRSConstant.REQUEST_TYPE_CHANGE_STAFF_MIX.equals(requestType)) {
			storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_change_staff_mix");
		}
		else if (MPRSConstant.REQUEST_TYPE_FROZEN.equals(requestType)) {
			storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_frozen");
		}
		else if (MPRSConstant.REQUEST_TYPE_CHANGE_HCM_POST.equals(requestType)) {
			storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_change_hcm_post");
		}
		else if (MPRSConstant.REQUEST_TYPE_FTE_ADJUSTMENT.equals(requestType)) {
			storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_fte_adjustment");
		}
		
		storedProcedure.registerStoredProcedureParameter("in_request_uid", Integer.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_submit_to_group", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_submit_to_user", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_action", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_with_email", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_email_to", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_email_cc", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_email_title", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_email_content", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_user_id", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_role_id", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("out_err_msg", String.class, ParameterMode.OUT);
		storedProcedure.setParameter("in_request_uid", requestNo);
		storedProcedure.setParameter("in_submit_to_group", "");
		storedProcedure.setParameter("in_submit_to_user", "");
		storedProcedure.setParameter("in_action", "WITHDRAW");
		storedProcedure.setParameter("in_with_email", "N");
		storedProcedure.setParameter("in_email_to", "");
		storedProcedure.setParameter("in_email_cc", "");
		storedProcedure.setParameter("in_email_title", "");
		storedProcedure.setParameter("in_email_content", "");
		storedProcedure.setParameter("in_user_id", userId);
		storedProcedure.setParameter("in_role_id", roleId);
		storedProcedure.execute();
		errMsg = (String)storedProcedure.getOutputParameterValue("out_err_msg");
		
		return errMsg;
	}

	public String submitApprove(int requestNo,  
								String requestType,
								String withEmail,
					            String emailTo,
					            String emailCC,
					            String emailTitle,
					            String emailContent,
					            String userId, 
					            String roleId) {
		String errMsg = "";
		
		StoredProcedureQuery storedProcedure = null;
		
		if ("NEW".equals(requestType)) {
			storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_new_post");
		}
		else if ("DEL".equals(requestType)) {
			storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_deletion");
		}
		else if (MPRSConstant.REQUEST_TYPE_CHANGE_FUNDING.equals(requestType)) {
			storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_change_funding");
		}
		else if (MPRSConstant.REQUEST_TYPE_EXTENSION.equals(requestType)) {
			storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_extension");
		}
		else if (MPRSConstant.REQUEST_TYPE_SUPPLEMENTARY_PROMOTION.equals(requestType)) {
			storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_supplementary_promotion");
		}
		else if (MPRSConstant.REQUEST_TYPE_UPGRADE.equals(requestType)) {
			storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_upgrade");
		}
		else if (MPRSConstant.REQUEST_TYPE_CHANGE_STAFF_MIX.equals(requestType)) {
			storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_change_staff_mix");
		}
		else if (MPRSConstant.REQUEST_TYPE_FROZEN.equals(requestType)) {
			storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_frozen");
		}
		else if (MPRSConstant.REQUEST_TYPE_CHANGE_HCM_POST.equals(requestType)) {
			storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_change_hcm_post");
		}
		else if (MPRSConstant.REQUEST_TYPE_FTE_ADJUSTMENT.equals(requestType)) {
			storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_fte_adjustment");
		}
		
		storedProcedure.registerStoredProcedureParameter("in_request_uid", Integer.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_submit_to_group", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_submit_to_user", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_action", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_with_email", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_email_to", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_email_cc", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_email_title", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_email_content", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_user_id", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_role_id", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("out_err_msg", String.class, ParameterMode.OUT);
		storedProcedure.setParameter("in_request_uid", requestNo);
		storedProcedure.setParameter("in_submit_to_group", "");
		storedProcedure.setParameter("in_submit_to_user", "");
		storedProcedure.setParameter("in_action", "APPROVE");
		storedProcedure.setParameter("in_with_email", withEmail);
		storedProcedure.setParameter("in_email_to", emailTo);
		storedProcedure.setParameter("in_email_cc", emailCC);
		storedProcedure.setParameter("in_email_title", emailTitle);
		storedProcedure.setParameter("in_email_content", emailContent);
		storedProcedure.setParameter("in_user_id", userId);
		storedProcedure.setParameter("in_role_id", roleId);
		storedProcedure.execute();
		errMsg = (String)storedProcedure.getOutputParameterValue("out_err_msg");
		
		return errMsg;
	}
	
	public String createSubmitWorkflow(int requestNo, 
            String targetWFGroup, 
            String targetWFUser, 
            String withEmail,
            String emailTo,
            String emailCC,
            String emailTitle,
            String emailContent,
            String userId) {
		String errMsg = "";
		
		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_deletion");
		storedProcedure.registerStoredProcedureParameter("in_request_uid", Integer.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_submit_to_group", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_submit_to_user", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_action", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_with_email", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_email_to", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_email_cc", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_email_title", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_email_content", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_user_id", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("out_err_msg", String.class, ParameterMode.OUT);
		storedProcedure.setParameter("in_request_uid", requestNo);
		storedProcedure.setParameter("in_submit_to_group", targetWFGroup);
		storedProcedure.setParameter("in_submit_to_user", targetWFUser);
		storedProcedure.setParameter("in_action", "SUBMIT");
		storedProcedure.setParameter("in_with_email", withEmail);
		storedProcedure.setParameter("in_email_to", emailTo);
		storedProcedure.setParameter("in_email_cc", emailCC);
		storedProcedure.setParameter("in_email_title", emailTitle);
		storedProcedure.setParameter("in_email_content", emailContent);
		storedProcedure.setParameter("in_user_id", userId);
		storedProcedure.execute();
		errMsg = (String)storedProcedure.getOutputParameterValue("out_err_msg");
		
		return errMsg;
		}
		
		public String createSubmitWithdraw(int requestNo, String userId) {
		String errMsg = "";
		
		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_deletion");
		storedProcedure.registerStoredProcedureParameter("in_request_uid", Integer.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_submit_to_group", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_submit_to_user", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_action", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_with_email", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_email_to", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_email_cc", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_email_title", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_email_content", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_user_id", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("out_err_msg", String.class, ParameterMode.OUT);
		storedProcedure.setParameter("in_request_uid", requestNo);
		storedProcedure.setParameter("in_submit_to_group", "");
		storedProcedure.setParameter("in_submit_to_user", "");
		storedProcedure.setParameter("in_action", "WITHDRAW");
		storedProcedure.setParameter("in_with_email", "N");
		storedProcedure.setParameter("in_email_to", "");
		storedProcedure.setParameter("in_email_cc", "");
		storedProcedure.setParameter("in_email_title", "");
		storedProcedure.setParameter("in_email_content", "");
		storedProcedure.setParameter("in_user_id", userId);
		storedProcedure.execute();
		errMsg = (String)storedProcedure.getOutputParameterValue("out_err_msg");
		
		return errMsg;
		}
		
		public String createSubmitApprove(int requestNo,  
					String withEmail,
		           String emailTo,
		           String emailCC,
		           String emailTitle,
		           String emailContent,
		           String userId) {
		String errMsg = "";
		
		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.submit_deletion");
		storedProcedure.registerStoredProcedureParameter("in_request_uid", Integer.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_submit_to_group", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_submit_to_user", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_action", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_with_email", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_email_to", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_email_cc", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_email_title", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_email_content", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_user_id", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("out_err_msg", String.class, ParameterMode.OUT);
		storedProcedure.setParameter("in_request_uid", requestNo);
		storedProcedure.setParameter("in_submit_to_group", "");
		storedProcedure.setParameter("in_submit_to_user", "");
		storedProcedure.setParameter("in_action", "APPROVE");
		storedProcedure.setParameter("in_with_email", withEmail);
		storedProcedure.setParameter("in_email_to", emailTo);
		storedProcedure.setParameter("in_email_cc", emailCC);
		storedProcedure.setParameter("in_email_title", emailTitle);
		storedProcedure.setParameter("in_email_content", emailContent);
		storedProcedure.setParameter("in_user_id", userId);
		storedProcedure.execute();
		errMsg = (String)storedProcedure.getOutputParameterValue("out_err_msg");
		
		return errMsg;
	}

	public String generateNewPostId(String staffGroup, String cluster, String inst, String dept, String rank, String programType, String duration) {
		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.generate_proposed_post_id");
		storedProcedure.registerStoredProcedureParameter("in_staff_group_id", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_cluster_id", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_inst_id", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("IN_DEPT_ID", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("IN_RANK", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("IN_PROGRAM_TYPE", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("IN_DURATION", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("out_request_id", String.class, ParameterMode.OUT);
		storedProcedure.setParameter("in_staff_group_id", staffGroup);
		storedProcedure.setParameter("in_cluster_id", cluster);
		storedProcedure.setParameter("in_inst_id", inst);
		storedProcedure.setParameter("IN_DEPT_ID", dept);
		storedProcedure.setParameter("IN_RANK", rank);
		storedProcedure.setParameter("IN_PROGRAM_TYPE", programType);
		storedProcedure.setParameter("IN_DURATION", duration);
		storedProcedure.execute();
		String result = (String)storedProcedure.getOutputParameterValue("out_request_id");
		
		return result;
	}
	
	public List<RequestPo> getRequestByPostNo(String searchPostNo) {
		List<RequestPo> resultList = new ArrayList<RequestPo>();
		
		Query q = entityManager.createQuery("SELECT C FROM RequestPo C INNER JOIN C.requestPositionList r where r.postNo = :postNo and C.requestStatus.statusCode <> :withdrawStatus and C.requestStatus.statusCode <> :confirmStatus and C.requestType.rqTypeCode <> 'NEW' order by C.requestNo desc", RequestPo.class);
		q.setParameter("postNo", searchPostNo);
		q.setParameter("withdrawStatus", "WITHDRAW");
		q.setParameter("confirmStatus", "CONFIRMED");
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			RequestPo c = (RequestPo)result.next();
			
			resultList.add(c);
		}
		
		return resultList;
	}
	
	public List<RequestPo> getExistingRequestByMPRSPostNo(String searchPostNo, String action) {
		List<RequestPo> resultList = new ArrayList<RequestPo>();
		
		Query q = entityManager.createQuery("SELECT C FROM RequestPo C INNER JOIN C.requestPositionList r where r.postNo = :postNo  and C.requestType.rqTypeCode = :requestType and C.requestStatus.statusCode = :confirmStatus order by C.requestId desc", RequestPo.class);
		q.setParameter("postNo", searchPostNo);
		q.setParameter("requestType", action);
		q.setParameter("confirmStatus", "CONFIRMED");  // ST08152: does not show WITHDRAW type request
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			RequestPo c = (RequestPo)result.next();
			
			resultList.add(c);
		}
		
		return resultList;
	}
	
	public List<RequestPo> getUpgradeRequestByPostId(String postUId) {
		String sql = "select m.* from RQ_POST p, RQ_MASTER m "; 
		sql += " where p.REQUEST_UID = m.REQUEST_UID ";
		sql += "   and p.POST_ID = (select POST_ID from POST_MASTER where post_uid = :postId)  ";
		sql += " and p.TO_POST_IND = 'Y' ";
		sql += " and m.REQUEST_TYPE = 'UPGRADE' ";
		sql += " and m.REQUEST_STATUS = 'CONFIRMED' ";
		sql += " order by m.request_id desc ";
		
		List<RequestPo> resultList = new ArrayList<RequestPo>();

		Query q = entityManager.createNativeQuery(sql, RequestPo.class);
		q.setParameter("postId", postUId);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			RequestPo c = (RequestPo)result.next();
			
			resultList.add(c);
		}
		
		return resultList;
	}
	
	public List<RequestPo> getChangeStaffMixRequestByPostId(String postUId) {
		String sql = "select m.* from RQ_POST p, RQ_MASTER m "; 
		sql += " where p.REQUEST_UID = m.REQUEST_UID ";
		sql += "   and p.POST_ID = (select POST_ID from POST_MASTER where post_uid = :postId)  ";
		sql += " and p.TO_POST_IND = 'Y' ";
		sql += " and m.REQUEST_TYPE = 'CHG_STAFF' ";
		sql += " and m.REQUEST_STATUS = 'CONFIRMED' ";
		sql += " order by m.request_id desc ";
		
		List<RequestPo> resultList = new ArrayList<RequestPo>();

		Query q = entityManager.createNativeQuery(sql, RequestPo.class);
		q.setParameter("postId", postUId);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			RequestPo c = (RequestPo)result.next();
			
			resultList.add(c);
		}
		
		return resultList;
	}

	public void recycleProposedPostId(String staffGroup, String duration, String tmpProposedPostId, String createdBy, String roleId) {
		System.out.println("recycleProposedPostId(String staffGroup, String duration, String tmpProposedPostId: " + staffGroup + "/" + duration + "/" + tmpProposedPostId);
		
		String seqNoStr = tmpProposedPostId.substring(tmpProposedPostId.lastIndexOf("-")+1);
		
		String prefix = tmpProposedPostId.substring(0, tmpProposedPostId.lastIndexOf("-"));
		prefix = prefix.substring(0, prefix.lastIndexOf("-"));
		prefix = prefix.substring(0, prefix.lastIndexOf("-"));
		
		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.recycle_proposed_post_id");
		storedProcedure.registerStoredProcedureParameter("in_staff_group_id", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_prefix", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_seq_no", Integer.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("IN_DURATION", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_created_by", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_role_id", String.class, ParameterMode.IN);
		storedProcedure.setParameter("in_staff_group_id", staffGroup);
		storedProcedure.setParameter("in_prefix", prefix);
		storedProcedure.setParameter("in_seq_no", Integer.parseInt(seqNoStr));
		storedProcedure.setParameter("IN_DURATION", duration);
		storedProcedure.setParameter("in_created_by", createdBy);
		storedProcedure.setParameter("in_role_id", roleId);
		storedProcedure.execute();
	}

	public RequestPo getNewRequestByPostId(String postId) {
		Query q = entityManager.createQuery("SELECT C FROM RequestPo C INNER JOIN C.requestPositionList r where r.postId = :postId and C.requestType.rqTypeCode = :requestType order by C.requestId desc", RequestPo.class);
		q.setParameter("postId", postId);
		q.setParameter("requestType", MPRSConstant.REQUEST_TYPE_NEW_REQUEST);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		if (result.hasNext()) {
			return (RequestPo)result.next();
		}
		
		return null;
	}

	public String validateDeletion(int postUid, Date effectiveDate) {
		StoredProcedureQuery storedProcedure = entityManager
				.createStoredProcedureQuery("REQUEST_WF_PKG.DELETION_VALIDATION");
		storedProcedure.registerStoredProcedureParameter("in_post_uid", Integer.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_effective_date", Date.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("out_err_msg", String.class, ParameterMode.OUT);
		storedProcedure.setParameter("in_post_uid", postUid);
		System.out.println("postUid:" + postUid);
		storedProcedure.setParameter("in_effective_date", effectiveDate);
		System.out.println("effectiveDate:" + effectiveDate);
		storedProcedure.execute();
		String result = (String) storedProcedure.getOutputParameterValue("out_err_msg");

		return result;
	}

	public String validateFrozen(int postUid, Date effectiveDate) {
		StoredProcedureQuery storedProcedure = entityManager
				.createStoredProcedureQuery("REQUEST_WF_PKG.FROZEN_VALIDATION");
		storedProcedure.registerStoredProcedureParameter("in_post_uid", Integer.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_effective_date", Date.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("out_err_msg", String.class, ParameterMode.OUT);
		storedProcedure.setParameter("in_post_uid", postUid);
		System.out.println("postUid:" + postUid);
		storedProcedure.setParameter("in_effective_date", effectiveDate);
		System.out.println("effectiveDate:" + effectiveDate);
		storedProcedure.execute();
		String result = (String) storedProcedure.getOutputParameterValue("out_err_msg");

		return result;
	}

	public Date addMonth(Date postStartDate, int duration) {
		Query q = entityManager.createNativeQuery("SELECT BATCH_JOB_PKG.MPRS_ADD_MONTHS(:inDate, :monthAdd) FROM dual");
		q.setParameter("inDate", postStartDate);
		q.setParameter("monthAdd", duration);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		if (result.hasNext()) {
			return (Date)result.next();
		}
		
		return null;
	}
	
	public String validateExtension(int postUid, Date newPostEndDate) {
		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("REQUEST_WF_PKG.VALIDATE_EXTENSION");
		storedProcedure.registerStoredProcedureParameter("in_post_uid", Integer.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("in_newPostEndDate", Date.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("out_err_msg", String.class, ParameterMode.OUT);
		storedProcedure.setParameter("in_post_uid", postUid);
		System.out.println("postUid:" + postUid);
		storedProcedure.setParameter("in_newPostEndDate", newPostEndDate);
		System.out.println("newPostEndDate:" + newPostEndDate);
		storedProcedure.execute();
		String result = (String) storedProcedure.getOutputParameterValue("out_err_msg");

		return result;
	}

	// Added for UT30027
	public List<RequestPo> searchRequest(List<DataAccessPo> dataAccessList, 
										 RequestEnquiryWebPo po, 
										 String userId,
										 String currentRoleId) {
		String sql = "select distinct m.* from RQ_POST p, RQ_MASTER m "; 
		sql += " where p.REQUEST_UID = m.REQUEST_UID ";
		sql += " and m.request_id not like 'DC-%' ";
		
		if (po.getRequestId() != null && !"".equals(po.getRequestId())) {
			sql += " and upper(m.request_id) like '%' || upper(:requestId) || '%' ";
		}
		
		if (po.getCreatedDateFrom() != null && !"".equals(po.getCreatedDateFrom())) {
			sql += " and trunc(m.CREATED_DATE) >=  trunc(to_date(:createdDateFrom, 'dd/mm/yyyy')) ";
		}
		
		if (po.getCreatedDateTo() != null && !"".equals(po.getCreatedDateTo())) {
			sql += " and trunc(m.CREATED_DATE) <=  trunc(to_date(:createdDateTo, 'dd/mm/yyyy')) ";
		}
		
		if (po.getUpdatedDateFrom() != null && !"".equals(po.getUpdatedDateFrom())) {
			sql += " and trunc(m.UPDATED_DATE) >=  trunc(to_date(:updatedDateFrom, 'dd/mm/yyyy')) ";
		}
		
		if (po.getUpdatedDateTo() != null && !"".equals(po.getUpdatedDateTo())) {
			sql += " and trunc(m.UPDATED_DATE) <=  trunc(to_date(:updatedDateTo, 'dd/mm/yyyy')) ";
		}
		
		// Append the Data Access
		String sqlDataAccess = ReportSubqueryHelper.getDataAccessScopeQuery(dataAccessList, "p");
		if(!sqlDataAccess.isEmpty()) {
			sql += " and ( ";
			sql += sqlDataAccess;
			sql += " ) ";
		}
		
		sql += " order by m.request_id desc ";
		
		List<RequestPo> resultList = new ArrayList<RequestPo>();

		Query q = entityManager.createNativeQuery(sql, RequestPo.class);
		
		if (po.getRequestId() != null && !"".equals(po.getRequestId())) {
			q.setParameter("requestId", po.getRequestId());
		}
		
		if (po.getCreatedDateFrom() != null && !"".equals(po.getCreatedDateFrom())) {
			q.setParameter("createdDateFrom", po.getCreatedDateFrom());
		}
		
		if (po.getCreatedDateTo() != null && !"".equals(po.getCreatedDateTo())) {
			q.setParameter("createdDateTo", po.getCreatedDateTo());
		}
		
		if (po.getUpdatedDateFrom() != null && !"".equals(po.getUpdatedDateFrom())) {
			q.setParameter("updatedDateFrom", po.getUpdatedDateFrom());
		}
		
		if (po.getUpdatedDateTo() != null && !"".equals(po.getUpdatedDateTo())) {
			q.setParameter("updatedDateTo", po.getUpdatedDateTo());
		}
		
		for (int i = 0; i < dataAccessList.size(); i++) {
			DataAccessPo d = dataAccessList.get(i);
			if (d.getClusterCode() != null) {
				q.setParameter("cluster_code" + i, d.getClusterCode());
			}
			if (d.getInstCode() != null) {
				q.setParameter("inst_code" + i, d.getInstCode());
			}
			if (d.getDeptCode() != null) {
				q.setParameter("dept_code" + i, d.getDeptCode());
			}
			if (d.getStaffGroupCode() != null) {
				q.setParameter("staff_group_code" + i, d.getStaffGroupCode());
			}
		}
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			RequestPo c = (RequestPo)result.next();
			
			resultList.add(c);
		}
		
		return resultList;
	}
	
	// Added for UT30064
	public RequestPo getExistingRequestByMPRSPostId(String postId) {
		RequestPo r = null;
		
		String sql = "SELECT C FROM RequestPo C INNER JOIN C.requestPositionList r where r.postId = :postId  ";
		sql += " and (C.requestType.rqTypeCode = :requestTypeNew ";
		sql += " or (C.requestType.rqTypeCode = :requestTypeUpgrade and r.toPostInd = 'Y')";
		sql += " or (C.requestType.rqTypeCode = :requestTypeChangeStaffMix and r.toPostInd = 'Y')";
		sql += " ) ";
		sql += " and C.requestStatus.statusCode = :confirmStatus order by C.requestId desc";
		
		Query q = entityManager.createQuery(sql, RequestPo.class);
		q.setParameter("postId", postId);
		q.setParameter("requestTypeNew", MPRSConstant.REQUEST_TYPE_NEW_REQUEST);
		q.setParameter("requestTypeUpgrade", MPRSConstant.REQUEST_TYPE_UPGRADE);
		q.setParameter("requestTypeChangeStaffMix", MPRSConstant.REQUEST_TYPE_CHANGE_STAFF_MIX);
		q.setParameter("confirmStatus", "CONFIRMED");  // ST08152: does not show WITHDRAW type request
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		if (result.hasNext()) {
			r = (RequestPo)result.next();
		}
		
		return r;		
	}
}
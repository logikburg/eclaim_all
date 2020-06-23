package hk.org.ha.eclaim.bs.request.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.request.po.RequestWorkflowRoutePo;
import hk.org.ha.eclaim.bs.request.dao.IRequestWorkflowRouteDao;
import hk.org.ha.eclaim.bs.security.po.RolePo;
import hk.org.ha.eclaim.core.logger.EClaimLogger;

@Repository
public class RequestWorkflowRouteDaoImpl implements IRequestWorkflowRouteDao {

	@PersistenceContext
	private EntityManager entityManager;

	public List<RolePo> getNextGroup(String staffGroupCode, String requestType, String currentStatus) {
		List<RolePo> resultList = new ArrayList<RolePo>();
		String sql = "SELECT C.* FROM SS_ROLE C where C.role_id in (select submitToRole from RequestWorkflowRoutePo where staffGroupCode = :staffGroupCode and request_type = :requestType and current_status = :currentStatus)";
		
		Query q = entityManager.createNativeQuery(sql, RolePo.class);
		q.setParameter("staffGroupCode", staffGroupCode);
		q.setParameter("requestType", requestType);
		q.setParameter("currentStatus", currentStatus);

		@SuppressWarnings("rawtypes")
		Iterator result2 = q.getResultList().iterator();

		while (result2.hasNext()) {
			RolePo c = (RolePo)result2.next();
			resultList.add(c);
		}

		return resultList;
	}
	
	public List<RolePo> getNextGroup(String staffGroupCode, String requestType, String currentStatus, String mgReviewInd) {
		List<RolePo> resultList = new ArrayList<RolePo>();
		String sql = "SELECT C.* FROM SS_ROLE C where C.role_id in (select submitToRole from RequestWorkflowRoutePo where staffGroupCode = :staffGroupCode and request_type = :requestType and current_status = :currentStatus and MG_REVIEW_IND = :mgReviewInd)";
		
		Query q = entityManager.createNativeQuery(sql, RolePo.class);
		q.setParameter("staffGroupCode", staffGroupCode);
		q.setParameter("requestType", requestType);
		q.setParameter("currentStatus", currentStatus);
		q.setParameter("mgReviewInd", mgReviewInd);

		@SuppressWarnings("rawtypes")
		Iterator result2 = q.getResultList().iterator();

		while (result2.hasNext()) {
			RolePo c = (RolePo)result2.next();
			resultList.add(c);
		}

		return resultList;
	}

	public List<RolePo> getPreviousGroup(String staffGroupCode, String requestType, String currentStatus) {
		// Get the next group first
		RequestWorkflowRoutePo workflowRoute = null;
		String sql = "SELECT C from RequestWorkflowRoutePo C where staffGroupCode = :staffGroupCode and requestType = :requestType and currentStatus = :currentStatus";
		
		Query q = entityManager.createQuery(sql, RequestWorkflowRoutePo.class);
		q.setParameter("staffGroupCode", staffGroupCode);
		q.setParameter("requestType", requestType);
		q.setParameter("currentStatus", currentStatus);
		
		System.out.println("sql: " + sql);
		System.out.println("requestType: " + requestType);
		System.out.println("staffGroupCode: " + staffGroupCode);
		System.out.println("currentStatus: " + currentStatus);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		if (result.hasNext()) {
			workflowRoute = (RequestWorkflowRoutePo)result.next();
		}

		// Get the Role mapped
		String[] tmpGroup = workflowRoute.getReturnToRole().split(",");
		List<RolePo> resultList = new ArrayList<RolePo>();
		
		for (int i=0; i<tmpGroup.length; i++) {
			EClaimLogger.debug("Process: " + tmpGroup[i]);

			q = entityManager.createNativeQuery("SELECT C.* FROM SS_ROLE C where C.role_id = :roleId", RolePo.class);
			q.setParameter("roleId", tmpGroup[i]);

			@SuppressWarnings("rawtypes")
			Iterator result2 = q.getResultList().iterator();

			while (result2.hasNext()) {
				RolePo c = (RolePo)result2.next();
				resultList.add(c);
			}

		}

		return resultList;
	}

	public List<RolePo> getPreviousGroup(String staffGroupCode, String requestType, String currentStatus, String mgReviewInd) {
		// Get the next group first
		RequestWorkflowRoutePo workflowRoute = null;
		
		String sql = "SELECT C from RequestWorkflowRoutePo C where staffGroupCode = :staffGroupCode and requestType = :requestType and currentStatus = :currentStatus and mgReviewInd = :mgReviewInd ";

		Query q = entityManager.createQuery(sql, RequestWorkflowRoutePo.class);
		q.setParameter("staffGroupCode", staffGroupCode);
		q.setParameter("requestType", requestType);
		q.setParameter("currentStatus", currentStatus);
		q.setParameter("mgReviewInd", mgReviewInd);

		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		if (result.hasNext()) {
			workflowRoute = (RequestWorkflowRoutePo)result.next();
		}
		
		if (workflowRoute == null) {
			q = entityManager.createQuery("SELECT C from RequestWorkflowRoutePo C where C.staffGroupCode = :staffGroupCode and requestType = :requestType and currentStatus = :currentStatus", RequestWorkflowRoutePo.class);
			q.setParameter("staffGroupCode", staffGroupCode);
			q.setParameter("requestType", requestType);
			q.setParameter("currentStatus", currentStatus);

			result = q.getResultList().iterator();

			if (result.hasNext()) {
				workflowRoute = (RequestWorkflowRoutePo)result.next();
			}
		}

		// Get the Role mapped
		List<RolePo> resultList = new ArrayList<RolePo>();
		String[] tmpGroup = workflowRoute.getReturnToRole().split(",");
		for (int i=0; i<tmpGroup.length; i++) {
			EClaimLogger.info("Process: " + tmpGroup[i]);

			q = entityManager.createNativeQuery("SELECT C.* FROM SS_ROLE C where C.role_id = :roleId", RolePo.class);
			q.setParameter("roleId", tmpGroup[i]);

			@SuppressWarnings("rawtypes")
			Iterator result2 = q.getResultList().iterator();

			while (result2.hasNext()) {
				RolePo c = (RolePo)result2.next();
				resultList.add(c);
			}
			
		}

		return resultList;
	}
	
	public RequestWorkflowRoutePo getDefaultRoute(String staffGroupCode, String requestType, String currentStatus) {
		String sql = "SELECT C from RequestWorkflowRoutePo C where C.staffGroupCode = :staffGroupCode and requestType = :requestType and currentStatus = :currentStatus";
		
		Query q = entityManager.createQuery(sql, RequestWorkflowRoutePo.class);
		q.setParameter("staffGroupCode", staffGroupCode);
		q.setParameter("requestType", requestType);
		q.setParameter("currentStatus", currentStatus);

		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		if (result.hasNext()) {
			return (RequestWorkflowRoutePo)result.next();
		}

		return null;
	}
	
	public RequestWorkflowRoutePo getDefaultRoute(String staffGroupCode, String requestType, String currentStatus, String mgReviewInd) {
		String sql = "SELECT C from RequestWorkflowRoutePo C where C.staffGroupCode = :staffGroupCode and requestType = :requestType and currentStatus = :currentStatus and mgReviewInd = :mgReviewInd";
		
		Query q = entityManager.createQuery(sql, RequestWorkflowRoutePo.class);
		q.setParameter("staffGroupCode", staffGroupCode);
		q.setParameter("requestType", requestType);
		q.setParameter("currentStatus", currentStatus);
		q.setParameter("mgReviewInd", mgReviewInd);

		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		if (result.hasNext()) {
			return (RequestWorkflowRoutePo)result.next();
		}

		return null;
	}
}

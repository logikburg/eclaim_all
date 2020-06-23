
package hk.org.ha.eclaim.bs.project.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.project.dao.IApprovalHistoryDao;
import hk.org.ha.eclaim.bs.project.po.ApprovalHistoryPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

@Repository
public class ApprovalHistoryDaoImpl implements IApprovalHistoryDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	public ApprovalHistoryPo getApprovalHistoryPoByUid(int apprHistUid) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ApprovalHistoryPo> getApprovalHistoryListByProjectId(int projectId) {
		Query q = entityManager.createQuery("SELECT po FROM ApprovalHistoryPo po where projectId = :projectId and projectVerId = function('xxeal_project_util_pkg.get_last_ver_id',projectId)", ApprovalHistoryPo.class);
		q.setParameter("projectId", projectId);
		
		List<ApprovalHistoryPo> apprHistList = new ArrayList<ApprovalHistoryPo>();
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while(result.hasNext()) {
			apprHistList.add((ApprovalHistoryPo)result.next());
		}
		
		return apprHistList;
	}
	
	public List<ApprovalHistoryPo> getApprovalHistoryListByProjectId(int projectId, int verId) {
		Query q = entityManager.createQuery("SELECT po FROM ApprovalHistoryPo po where projectId = :projectId and projectVerId = :verId", ApprovalHistoryPo.class);
		q.setParameter("projectId", projectId);
		q.setParameter("verId", verId);
		List<ApprovalHistoryPo> apprHistList = new ArrayList<ApprovalHistoryPo>();
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while(result.hasNext()) {
			apprHistList.add((ApprovalHistoryPo)result.next());
		}
		
		return apprHistList;
	}

	public void deleteApprHistPo(int apprHistUid) {
		// TODO Auto-generated method stub
		
	}
	
	public String getActionNameByCode(String actionCode) {
		Query q = entityManager.createQuery("SELECT actionName FROM ApprovalActionPo where actionCode = :actionCode");
		q.setParameter("actionCode", actionCode);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			return (String)result.next();
		}
		return null;
	}
	
	public int insertApprHistPo(ApprovalHistoryPo apprHist, UserPo updateUser) {
		apprHist.setCreatedBy(updateUser.getUserId());
		apprHist.setCreatedRoleId(updateUser.getCurrentRole());
		apprHist.setCreatedDate(new Date());
		apprHist.setUpdatedBy(updateUser.getUserId());
		apprHist.setUpdatedRoleId(updateUser.getCurrentRole());
		apprHist.setUpdatedDate(new Date());

		entityManager.persist(apprHist);
		return apprHist.getApprHistUid();
	}

	public void updateApprHistPo(ApprovalHistoryPo apprHist, UserPo updateUser) {
		apprHist.setUpdatedBy(updateUser.getUserId());
		apprHist.setUpdatedRoleId(updateUser.getCurrentRole());
		apprHist.setUpdatedDate(new Date());
		
		entityManager.merge(apprHist);
	}

}

package hk.org.ha.eclaim.bs.payment.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.payment.dao.IPaymentJobDao;
import hk.org.ha.eclaim.bs.payment.po.PaymentJobPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

@Repository
public class PaymentJobDaoImpl implements IPaymentJobDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private EntityManagerFactory emf;

	public String getBatchJobListByString(Integer batcdId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<PaymentJobPo> getBatchJobList(Integer batchId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void insert(Integer batchId, HashMap<String, Integer> rankList, UserPo user) {
		if (rankList != null && rankList.size() > 0) {
			for (Entry<String, Integer> temp : rankList.entrySet()) {
				PaymentJobPo paymentJobPo = new PaymentJobPo();
				paymentJobPo.setBatchId(batchId);
				paymentJobPo.setRank(temp.getKey());
				paymentJobPo.setCreatedBy(user.getUserId());
				paymentJobPo.setCreatedRoleId(user.getCurrentRole());
				paymentJobPo.setCreatedDate(new Date());
				paymentJobPo.setUpdatedBy(user.getUserId());
				paymentJobPo.setUpdatedRoleId(user.getCurrentRole());
				paymentJobPo.setUpdatedDate(new Date());
				entityManager.persist(paymentJobPo);
			}
		}
	}

	public void delete(List<PaymentJobPo> rankList) {
		if (rankList != null && rankList.size() > 0) {
			for (PaymentJobPo rank : rankList) {
				PaymentJobPo jobPo = entityManager.merge(rank);
				entityManager.remove(jobPo);
			}
		}
		
	}

	
}

package hk.org.ha.eclaim.bs.payment.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.payment.dao.IPaymentDetailDao;
import hk.org.ha.eclaim.bs.payment.po.PaymentBatchPo;
import hk.org.ha.eclaim.bs.payment.po.PaymentDetailPo;
import hk.org.ha.eclaim.core.logger.EClaimLogger;

@Repository
public class PaymentDetailDaoImpl implements IPaymentDetailDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private EntityManagerFactory emf;

	public List<PaymentDetailPo> getAllPaymentDetails() {

		List<PaymentDetailPo> paymentDetailList = new ArrayList<PaymentDetailPo>();
		entityManager = emf.createEntityManager();
		Query q = entityManager.createQuery("FROM PaymentDetailPo", PaymentBatchPo.class);

		Iterator<?> result = q.getResultList().iterator();

		while (result.hasNext()) {
			paymentDetailList.add((PaymentDetailPo) result.next());
		}
		return paymentDetailList;
	}

	protected List<PaymentDetailPo> getAllPaymentDetails(int pageNo, int maxResults) {

		List<PaymentDetailPo> paymentDetailList = new ArrayList<PaymentDetailPo>();
		entityManager = emf.createEntityManager();
		Query q = entityManager.createQuery("FROM PaymentDetailPo", PaymentBatchPo.class);
		q.setFirstResult(pageNo * maxResults);
		q.setMaxResults(maxResults);

		Iterator<?> result = q.getResultList().iterator();

		while (result.hasNext()) {
			paymentDetailList.add((PaymentDetailPo) result.next());
		}
		return paymentDetailList;
	}

	public List<PaymentDetailPo> getPaymentDetailByBatchId(int batchId) {
		entityManager = emf.createEntityManager();
		Query q = entityManager.createQuery("SELECT P FROM PaymentDetailPo P where batchId = :batchId",
				PaymentDetailPo.class);
		q.setParameter("batchId", batchId);

		List<PaymentDetailPo> payDetail = q.getResultList();

		return payDetail;
	}

	public int insert(PaymentDetailPo paymentDetailPo) {
		entityManager = emf.createEntityManager();
		entityManager.persist(paymentDetailPo);
		return paymentDetailPo.getBatchDetailId();
	}

	public List<PaymentDetailPo> insertAll(List<PaymentDetailPo> lstPaymentDetail) {

		int detailSze = lstPaymentDetail.size();
		int btchSze = 10;
		int cn = 0;
		List<PaymentDetailPo> retPo = new ArrayList<PaymentDetailPo>();
		entityManager = emf.createEntityManager();

		EntityTransaction entityTransaction = entityManager.getTransaction();

		try {
			entityTransaction.begin();

			for (; cn < detailSze; cn++) {
				PaymentDetailPo po = (PaymentDetailPo) lstPaymentDetail.get(cn);
				if (cn > 0 && cn % btchSze == 0) {
					entityTransaction.commit();
					entityTransaction.begin();

					entityManager.clear();
				}

				entityManager.persist(po);
				EClaimLogger.info(po.toString());
			}

			entityTransaction.commit();
		} catch (RuntimeException e) {
			if (entityTransaction.isActive()) {
				entityTransaction.rollback();
			}
		} finally {
			entityManager.close();
		}
		return retPo;
	}

	public void update(PaymentDetailPo po) {
		entityManager = emf.createEntityManager();
		entityManager.merge(po);
	}

	public void delete(PaymentDetailPo pdPo) {
		PaymentDetailPo detailPo = entityManager.merge(pdPo);
		entityManager.remove(detailPo);
	}
}

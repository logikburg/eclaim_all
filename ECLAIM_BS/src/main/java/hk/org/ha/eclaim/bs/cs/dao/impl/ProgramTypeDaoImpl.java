package hk.org.ha.eclaim.bs.cs.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.bs.cs.dao.IProgramTypeDao;
import hk.org.ha.eclaim.bs.cs.po.ProgramTypePo;

@Repository
public class ProgramTypeDaoImpl implements IProgramTypeDao {

	@PersistenceContext
	private EntityManager entityManager;

	public List<ProgramTypePo> getAllProgramType() {
		List<ProgramTypePo> resultList = new ArrayList<ProgramTypePo>();

		Query q = entityManager.createQuery("SELECT C FROM ProgramTypePo C where C.recState = :state order by C.programTypeName", ProgramTypePo.class);
		q.setParameter("state", MPRSConstant.MPRS_STATE_ACTIVE);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		while (result.hasNext()) {
			ProgramTypePo c = (ProgramTypePo)result.next();
			resultList.add(c);
		}

		return resultList;
	}

	// Added for CC176525
	public List<ProgramTypePo> getProgramTypeListByPostInfo(String postDuration, String postFteType, String annaulPlanInd) {
		System.out.println("###postDuration="+postDuration);
		System.out.println("###postFteType="+postFteType);
		System.out.println("###annaulPlanInd="+annaulPlanInd);
		List<ProgramTypePo> resultList = new ArrayList<ProgramTypePo>();

		String sql = "SELECT P.* FROM CS_POST_PROGRAM_TYPE C INNER JOIN CS_PROGRAM_TYPE P ON C.PROGRAM_TYPE_CODE = P.PROGRAM_TYPE_CODE WHERE C.POST_DURATION = :postDuration AND C.POST_FTE_TYPE = :postFteType AND C.ANNUAL_PLAN_IND = :annualPlanInd AND C.REC_STATE = :state order by P.PROGRAM_TYPE_NAME ";
		
		Query q = entityManager.createNativeQuery(sql, ProgramTypePo.class);
		q.setParameter("postDuration", postDuration);
		q.setParameter("postFteType", postFteType);
		q.setParameter("annualPlanInd", annaulPlanInd);
		q.setParameter("state", MPRSConstant.MPRS_STATE_ACTIVE);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			ProgramTypePo c = (ProgramTypePo)result.next();
			resultList.add(c);
		}
		
		return resultList;
	}
	
}

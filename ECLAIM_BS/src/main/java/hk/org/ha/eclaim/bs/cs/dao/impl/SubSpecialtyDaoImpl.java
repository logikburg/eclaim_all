package hk.org.ha.eclaim.bs.cs.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.request.constant.PostConstant;
import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.bs.cs.dao.ISubSpecialtyDao;
import hk.org.ha.eclaim.bs.cs.po.SubSpecialtyPo;

@Repository
public class SubSpecialtyDaoImpl implements ISubSpecialtyDao {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public List<SubSpecialtyPo> getAllSubSpecialty() {
		List<SubSpecialtyPo> resultList = new ArrayList<SubSpecialtyPo>();

		Query q = entityManager.createQuery("SELECT C FROM SubSpecialtyPo C where C.recState = :recState order by C.subSpecialtyDesc", SubSpecialtyPo.class);
		q.setParameter("recState", MPRSConstant.MPRS_STATE_ACTIVE);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		while (result.hasNext()) {
			SubSpecialtyPo c = (SubSpecialtyPo)result.next();
			resultList.add(c);
		}

		return resultList;
	}
	
	public List<SubSpecialtyPo> getAllSubSpecialtyByStaffGroup(String staffGroupCode, String deptCode){
		List<SubSpecialtyPo> resultList = new ArrayList<SubSpecialtyPo>();

		String sql = "SELECT C FROM SubSpecialtyPo C WHERE C.recState = :recState ";
		if (staffGroupCode != null && !"".equals(staffGroupCode)) {
			sql += " and C.staffGroupCode = :staffGroupCode ";
		}
		
		if (!PostConstant.STAFF_GROUP_MEDICAL.equals(staffGroupCode)) {
			if (deptCode != null && !"".equals(deptCode)) {
				sql += " and C.deptCode = :deptCode ";
			}
		}
		
		sql += " order by C.subSpecialtyDesc";
		
		System.out.println(sql);
		
		Query q = entityManager.createQuery(sql, SubSpecialtyPo.class);
		q.setParameter("recState", MPRSConstant.MPRS_STATE_ACTIVE);
		if (staffGroupCode != null && !"".equals(staffGroupCode)) {
			q.setParameter("staffGroupCode", staffGroupCode);
			System.out.println("staffGroupCode: " + staffGroupCode);
		}
		
		if (!PostConstant.STAFF_GROUP_MEDICAL.equals(staffGroupCode)) {
			if (deptCode != null && !"".equals(deptCode)) {
				q.setParameter("deptCode", deptCode);
				System.out.println("deptCode: " + deptCode);
			}
		}

		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		while (result.hasNext()) {
			SubSpecialtyPo c = (SubSpecialtyPo)result.next();
			resultList.add(c);
		}

		return resultList;
	}
}

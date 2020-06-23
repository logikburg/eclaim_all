package hk.org.ha.eclaim.bs.cs.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.cs.dao.IDeptDao;
import hk.org.ha.eclaim.bs.cs.po.DepartmentPo;

@Repository
public class DeptDaoImpl implements IDeptDao{
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public List<DepartmentPo> getAllDept() {
		List<DepartmentPo> resultList = new ArrayList<DepartmentPo>();
		
		Query q = entityManager.createQuery("SELECT D FROM DepartmentPo D order by D.deptName", DepartmentPo.class);
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			DepartmentPo c = (DepartmentPo)result.next();
			
			resultList.add(c);
		}
		
		return resultList;
	}
	
	public List<DepartmentPo> getAllDeptByStaffGroup(String staffGroupCode) {
		List<DepartmentPo> resultList = new ArrayList<DepartmentPo>();
		
		String sql = "SELECT D FROM DepartmentPo D where 1=1 ";
		
		if (staffGroupCode != null && !"".equals(staffGroupCode)) {
			sql += " AND D.staffGroupCode = :staffGroupCode ";
		}
		
		sql += " order by D.deptName ";

		Query q = entityManager.createQuery(sql, DepartmentPo.class);

		if (staffGroupCode != null && !"".equals(staffGroupCode)) {
			q.setParameter("staffGroupCode", staffGroupCode);
		}
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			DepartmentPo c = (DepartmentPo)result.next();
			
			resultList.add(c);
		}
		
		return resultList;
	}

	public DepartmentPo getDeptNameByDeptCode(String deptCode) {
		DepartmentPo c = new DepartmentPo();
		Query q = entityManager.createQuery("SELECT C FROM DepartmentPo C where deptCode = :deptCode", DepartmentPo.class);
		q.setParameter("deptCode", deptCode);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		if (result.hasNext()) {
			c =(DepartmentPo)result.next();
		}
		return c;
	}

}

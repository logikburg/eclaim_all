package hk.org.ha.eclaim.bs.cs.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.cs.dao.IStaffGroupDao;
import hk.org.ha.eclaim.bs.cs.po.StaffGroupPo;

@Repository
public class StaffGroupDaoImpl implements IStaffGroupDao{

	@PersistenceContext
	private EntityManager entityManager;
	
	public List<StaffGroupPo> getAllStaffGroup() {
		List<StaffGroupPo> resultList = new ArrayList<StaffGroupPo>();

		Query q = entityManager.createQuery("SELECT C FROM StaffGroupPo C order by C.staffGroupName ", StaffGroupPo.class);

		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		while (result.hasNext()) {
			StaffGroupPo c = (StaffGroupPo)result.next();
			resultList.add(c);
		}

		return resultList;
	}

	public StaffGroupPo getStaffGroupNameByStaffGroupCode(String staffGroupCode) {
		StaffGroupPo c = new StaffGroupPo();
		Query q = entityManager.createQuery("SELECT C FROM StaffGroupPo C where C.staffGroupCode = :staffGroupCode", StaffGroupPo.class);
		q.setParameter("staffGroupCode", staffGroupCode);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		if (result.hasNext()) {
			c =(StaffGroupPo)result.next();
		}
		return c;
	}

}

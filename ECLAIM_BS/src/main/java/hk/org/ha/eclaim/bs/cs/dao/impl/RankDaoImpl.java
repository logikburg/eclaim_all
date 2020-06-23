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
import hk.org.ha.eclaim.bs.cs.dao.IRankDao;
import hk.org.ha.eclaim.bs.cs.po.RankPo;

@Repository
public class RankDaoImpl implements IRankDao{
	@PersistenceContext
	private EntityManager entityManager;

	public List<RankPo> getAllRank() {
		List<RankPo> resultList = new ArrayList<RankPo>();

		Query q = entityManager.createQuery("SELECT C FROM RankPo C order by C.seqNo", RankPo.class);

		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		while (result.hasNext()) {
			RankPo c = (RankPo)result.next();
			resultList.add(c);
		}

		return resultList;
	}
	
	public List<RankPo> getAllRank(String staffGroup, String fromRank) {
		List<RankPo> resultList = new ArrayList<RankPo>();

		String sql = "select * from CS_RANK ";
		sql += " where (STAFF_GROUP_CODE, RANK_CODE) in ";
		if (!"".equals(staffGroup)) {
			if (!"".equals(fromRank)) {
				sql += " (select m.STAFF_GROUP_CODE, m.RANK_TO from CS_UPGRADE_RANK_MAP m, CS_RANK r where m.STAFF_GROUP_CODE = r.STAFF_GROUP_CODE and m.STAFF_GROUP_CODE = :staffGroupCode and m.RANK_FROM = r.rank_code and r.rank_name = :fromRank and m.REC_STATE = :recState)   ";
			}
			else {
				sql += " (select m.STAFF_GROUP_CODE, m.RANK_TO from CS_UPGRADE_RANK_MAP m, CS_RANK r where m.STAFF_GROUP_CODE = r.STAFF_GROUP_CODE and m.STAFF_GROUP_CODE = :staffGroupCode and m.RANK_FROM = r.rank_code and m.REC_STATE = :recState)   ";
			}
		}
		else {
			if (!"".equals(fromRank)) {
				sql += " (select m.STAFF_GROUP_CODE, m.RANK_TO from CS_UPGRADE_RANK_MAP m, CS_RANK r where m.STAFF_GROUP_CODE = r.STAFF_GROUP_CODE and m.RANK_FROM = r.rank_code and r.rank_name = :fromRank and m.REC_STATE = :recState)   ";
			}
			else {
				sql += " (select m.STAFF_GROUP_CODE, m.RANK_TO from CS_UPGRADE_RANK_MAP m, CS_RANK r where m.STAFF_GROUP_CODE = r.STAFF_GROUP_CODE and m.RANK_FROM = r.rank_code and m.REC_STATE = :recState)   ";
			}
		}
		
		sql += " and REC_STATE = :recState ";
		sql += " order by SEQ_NO ";
		Query q = entityManager.createNativeQuery(sql, RankPo.class);

		q.setParameter("recState", MPRSConstant.MPRS_STATE_ACTIVE);
		q.setParameter("fromRank", fromRank);
		
		if (!"".equals(staffGroup)) {
			if (!"".equals(fromRank)) {
				q.setParameter("staffGroupCode", staffGroup);
				q.setParameter("fromRank", fromRank);
			}
			else {
				q.setParameter("staffGroupCode", staffGroup);
			}
		}
		else {
			if (!"".equals(fromRank)) {
				q.setParameter("fromRank", fromRank);
			}
			else {
			}
		}
		
		
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		while (result.hasNext()) {
			RankPo c = (RankPo)result.next();
			resultList.add(c);
		}

		return resultList;
	}
	
	public List<RankPo> getAllRankByStaffGroup(String staffGroupCode, String deptCode) {
		List<RankPo> resultList = new ArrayList<RankPo>();

		String sql = "SELECT C FROM RankPo C where 1=1 ";
		if (staffGroupCode != null && !"".equals(staffGroupCode)) {
			sql += " and C.staffGroupCode = :staffGroupCode ";
		}
		
		if (!PostConstant.STAFF_GROUP_MEDICAL.equals(staffGroupCode)) {
			if (deptCode != null && !"".equals(deptCode)) {
				sql += " and C.deptCode = :deptCode ";
			}
		}
		sql += " order by C.seqNo";
		Query q = entityManager.createQuery(sql, RankPo.class);
		
		if (staffGroupCode != null && !"".equals(staffGroupCode)) {
			q.setParameter("staffGroupCode", staffGroupCode);
		}
		
		if (!PostConstant.STAFF_GROUP_MEDICAL.equals(staffGroupCode)) {
			if (deptCode != null && !"".equals(deptCode)) {
				q.setParameter("deptCode", deptCode);
			}
		}
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		while (result.hasNext()) {
			RankPo c = (RankPo)result.next();
			resultList.add(c);
		}

		return resultList;
	}
	
	public List<RankPo> getAllRankByStaffGroup(String staffGroupCode, String deptCode, String fromRank) {
		List<RankPo> resultList = new ArrayList<RankPo>();

		String sql = "SELECT C.* FROM CS_RANK C where 1=1 ";
		if (staffGroupCode != null && !"".equals(staffGroupCode)) {
			sql += " and C.STAFF_GROUP_CODE = :staffGroupCode ";
		}
		
		if (!PostConstant.STAFF_GROUP_MEDICAL.equals(staffGroupCode)) {
			if (deptCode != null && !"".equals(deptCode)) {
				sql += " and C.DEPT_CODE = :deptCode ";
			}
		}
		
		if (fromRank != null && !"".equals(fromRank)) {
			sql += " and (STAFF_GROUP_CODE, RANK_CODE) in ";
			sql += " (select m.STAFF_GROUP_CODE, m.RANK_TO from CS_UPGRADE_RANK_MAP m, CS_RANK r where m.STAFF_GROUP_CODE = r.STAFF_GROUP_CODE and m.STAFF_GROUP_CODE = :staffGroupCode and m.RANK_FROM = r.rank_code and r.rank_name = :fromRank and m.REC_STATE = :recState)   ";
		}
		
		sql += " order by C.SEQ_NO";
		Query q = entityManager.createNativeQuery(sql, RankPo.class);
		
		if (staffGroupCode != null && !"".equals(staffGroupCode)) {
			q.setParameter("staffGroupCode", staffGroupCode);
		}
		
		if (!PostConstant.STAFF_GROUP_MEDICAL.equals(staffGroupCode)) {
			if (deptCode != null && !"".equals(deptCode)) {
				q.setParameter("deptCode", deptCode);
			}
		}
		
		if (fromRank != null && !"".equals(fromRank)) {
			if (staffGroupCode == null || "".equals(staffGroupCode)) {
				q.setParameter("staffGroupCode", "");
			}
			
			q.setParameter("fromRank", fromRank);
			q.setParameter("recState", MPRSConstant.MPRS_STATE_ACTIVE);
		}
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		while (result.hasNext()) {
			RankPo c = (RankPo)result.next();
			resultList.add(c);
		}

		return resultList;
	}
	

	public RankPo getRankNameByRankCode(String rankCode) {
		RankPo c = new RankPo();
		Query q = entityManager.createQuery("SELECT C FROM RankPo C where rankCode = :rankCode order by C.seqNo ", RankPo.class);
		q.setParameter("rankCode", rankCode);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		if (result.hasNext()) {
			c =(RankPo)result.next();
		}
		return c;
	}
	
	
}

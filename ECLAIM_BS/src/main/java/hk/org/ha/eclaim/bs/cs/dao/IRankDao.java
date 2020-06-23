package hk.org.ha.eclaim.bs.cs.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.cs.po.RankPo;

public interface IRankDao {
	public List<RankPo> getAllRank();
	public List<RankPo> getAllRank(String staffGroup, String fromRank);
	public List<RankPo> getAllRankByStaffGroup(String staffGroupCode, String deptCode);
	public List<RankPo> getAllRankByStaffGroup(String staffGroupCode, String deptCode, String rankCode);
	public RankPo getRankNameByRankCode(String rankCode);
}

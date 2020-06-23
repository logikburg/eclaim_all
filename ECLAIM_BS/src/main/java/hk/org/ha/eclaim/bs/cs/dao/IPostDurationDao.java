package hk.org.ha.eclaim.bs.cs.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.cs.po.PostDurationPo;;

public interface IPostDurationDao {
	List<PostDurationPo> getAllPostDuration();
	
	public PostDurationPo getDuartionDescByDurationCode(String durationCode);

}

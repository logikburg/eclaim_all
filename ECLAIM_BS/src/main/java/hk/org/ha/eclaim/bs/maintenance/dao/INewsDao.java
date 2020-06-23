package hk.org.ha.eclaim.bs.maintenance.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.maintenance.po.NewsPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

public interface INewsDao {
	public List<NewsPo> getAllNews();
	public NewsPo getNewsByNewsUid(int newsUid);
	public void deleteNews(int newsUid, String updateBy, String roleId);
	public int insert(NewsPo news, UserPo updateUser);
	public void update(NewsPo news, UserPo updateUser);
	public List<NewsPo> getCurrentAllNews();
}

package hk.org.ha.eclaim.bs.maintenance.svc;

import java.util.List;

import hk.org.ha.eclaim.bs.maintenance.po.NewsPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

public interface INewsSvc {
	public List<NewsPo> getAllNews();
	public List<NewsPo> getCurrentNews();
	public NewsPo getNewsByNewsUid(int newsUid);
	public void deleteNews(int newsUid, String updateBy, String roleId);
	public int insert(NewsPo news, UserPo updateUser);
	public void update(NewsPo news, UserPo updateUser);
}

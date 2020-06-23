package hk.org.ha.eclaim.bs.maintenance.svc.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hk.org.ha.eclaim.bs.maintenance.dao.INewsDao;
import hk.org.ha.eclaim.bs.maintenance.po.NewsPo;
import hk.org.ha.eclaim.bs.maintenance.svc.INewsSvc;
import hk.org.ha.eclaim.bs.security.po.UserPo;

@Service("newsSvc")
public class NewsSvcImpl implements INewsSvc {

	@Autowired
	INewsDao newsDao;
	
	public List<NewsPo> getAllNews() {
		return newsDao.getAllNews();
	}
	
	public List<NewsPo> getCurrentNews() {
		return newsDao.getCurrentAllNews();
	}

	public NewsPo getNewsByNewsUid(int newsUid) {
		return newsDao.getNewsByNewsUid(newsUid);
	}

	@Transactional
	public void deleteNews(int newsUid, String updateBy, String roleId) {
		newsDao.deleteNews(newsUid, updateBy, roleId);
	}

	@Transactional
	public int insert(NewsPo news, UserPo updateUser) {
		return newsDao.insert(news, updateUser);
	}

	@Transactional
	public void update(NewsPo news, UserPo updateUser) {
		newsDao.update(news, updateUser);
	}
}

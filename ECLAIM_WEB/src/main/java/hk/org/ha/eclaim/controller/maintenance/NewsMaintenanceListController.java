package hk.org.ha.eclaim.controller.maintenance;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import hk.org.ha.eclaim.bs.maintenance.po.NewsPo;
import hk.org.ha.eclaim.bs.maintenance.svc.INewsSvc;
import hk.org.ha.eclaim.core.controller.BaseController;
import hk.org.ha.eclaim.model.maintenance.NewsMaintenanceListWebVo;
import hk.org.ha.eclaim.model.maintenance.NewsWebVo;
import hk.org.ha.eclaim.bs.cs.svc.ICommonSvc;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.svc.ISecuritySvc;

@Controller
public class NewsMaintenanceListController extends BaseController {

	@Autowired
	INewsSvc newsSvc;
	
	@Autowired
	ICommonSvc commonSvc;

	@Autowired
	ISecuritySvc securitySvc;

	@RequestMapping(value="/maintenance/newsMaintenance", method=RequestMethod.GET)
	public ModelAndView initList(HttpServletRequest request) throws Exception {
		// Get All news information
		List<NewsPo> allNews = newsSvc.getAllNews();
		List<NewsWebVo> allNewsVo = new ArrayList<NewsWebVo>();
		NewsWebVo newsVo = null;
		for (int i=0; i<allNews.size(); i++) {
			newsVo = new NewsWebVo();
			newsVo.setNewsUid(allNews.get(i).getNewsUid());
			newsVo.setNewsDesc(allNews.get(i).getNewsDesc());
			newsVo.setEffectiveFrom(allNews.get(i).getEffectiveFrom());
			newsVo.setEffectiveTo(allNews.get(i).getEffectiveTo());
			allNewsVo.add(newsVo);
		}
		
		NewsMaintenanceListWebVo webVo = new NewsMaintenanceListWebVo();
		webVo.setAllNews(allNewsVo);
		return new ModelAndView("maintenance/newsMaintenanceList", "formBean", webVo);
	}

	@RequestMapping(value="/maintenance/newsMaintenance", method=RequestMethod.POST)
	public ModelAndView performUpdate(@ModelAttribute NewsMaintenanceListWebVo view, HttpServletRequest request) throws Exception {
		System.out.println("NewsMaintenanceListController.performUpdate()");
		// Get the user name from cookie
		String userId = this.getSessionUser(request).getUserId();
		UserPo user = securitySvc.findUser(userId);
        user.setCurrentRole((String)request.getSession().getAttribute("currentRole"));

		// Get the delete news Id
		String deleteNewsUid = view.getDeleteNewsUid();

		// Perform Delete
		newsSvc.deleteNews(Integer.parseInt(deleteNewsUid), userId, (String)request.getSession().getAttribute("currentRole"));

		// Get All news information
		List<NewsPo> allNews = newsSvc.getAllNews();

		NewsMaintenanceListWebVo webVo = new NewsMaintenanceListWebVo();
		List<NewsWebVo> allNewsVo = new ArrayList<NewsWebVo>();
		NewsWebVo newsVo = null;
		for (int i=0; i<allNews.size(); i++) {
			newsVo = new NewsWebVo();
			newsVo.setNewsUid(allNews.get(i).getNewsUid());
			newsVo.setNewsDesc(allNews.get(i).getNewsDesc());
			newsVo.setEffectiveFrom(allNews.get(i).getEffectiveFrom());
			newsVo.setEffectiveTo(allNews.get(i).getEffectiveTo());
			allNewsVo.add(newsVo);
		}
		
		webVo.setAllNews(allNewsVo);
		webVo.setUpdateSuccess("Y");
		webVo.setUpdateSuccess("Record delete success.");
		webVo.setUserName(user.getUserName());
		return new ModelAndView("maintenance/newsMaintenanceList", "formBean", webVo);
	}
}

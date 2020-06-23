package hk.org.ha.eclaim.controller.maintenance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import hk.org.ha.eclaim.bs.maintenance.po.NewsPo;
import hk.org.ha.eclaim.bs.maintenance.svc.INewsSvc;
import hk.org.ha.eclaim.core.controller.BaseController;
import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.model.maintenance.NewsMaintenanceDetailWebVo;
import hk.org.ha.eclaim.model.maintenance.NewsMaintenanceListWebVo;
import hk.org.ha.eclaim.model.maintenance.NewsWebVo;
import hk.org.ha.eclaim.bs.cs.svc.ICommonSvc;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.svc.ISecuritySvc;
import hk.org.ha.eclaim.core.helper.DateTimeHelper;
import hk.org.ha.eclaim.core.logger.EClaimLogger;

@Controller
public class NewsMaintenanceDetailController extends BaseController {

	@Autowired
	INewsSvc newsSvc;
	
	@Autowired
	ICommonSvc commonSvc;
	
	@Autowired
	ISecuritySvc securitySvc;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@RequestMapping(value="/maintenance/newsMaintenanceDetail", method=RequestMethod.GET)
	public ModelAndView initList(HttpServletRequest request,
			@RequestParam(required=false) String newsUid) throws Exception {
		
		EClaimLogger.info("newsMaintenanceDetail - newsUid: " + newsUid);
		NewsMaintenanceDetailWebVo webVo = new NewsMaintenanceDetailWebVo();

		NewsPo news = null;
		if (newsUid == null) {
			news = new NewsPo();
		}
		else {
			news = newsSvc.getNewsByNewsUid(Integer.parseInt(newsUid));
			webVo = convertToVo(news);
		}

		// webVo.setNews(news);
		return new ModelAndView("maintenance/newsMaintenanceDetail", "formBean", webVo);
	}

	@RequestMapping(value="/maintenance/newsMaintenanceDetail", method=RequestMethod.POST)
	public ModelAndView performUpdate(@ModelAttribute NewsMaintenanceDetailWebVo view,
			                          HttpServletRequest request) throws Exception {
		try {
			// Get the user name from cookie
			String userId = this.getSessionUser(request).getUserId();
			UserPo user = securitySvc.findUser(userId);
	        user.setCurrentRole((String)request.getSession().getAttribute("currentRole"));
	
			System.out.println("view.getNewsUid(): " + view.getNewsUid());
			System.out.println("view.getHiddenNewsUid(): " + view.getHiddenNewsUid());
			System.out.println("view.getEffectiveFrom(): " + view.getEffectiveFrom());
	
			if ("".equals(view.getHiddenNewsUid())) {
				NewsPo tmpNews = new NewsPo();
				tmpNews.setEffectiveFrom(sdf.parse(view.getEffectiveFrom()));
				tmpNews.setEffectiveTo(sdf.parse(view.getEffectiveTo()));
				tmpNews.setNewsDesc(view.getNewsDesc());
				tmpNews.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
				
				// Create new News
				newsSvc.insert(tmpNews, user);
			}
			else {
				// Check last update date
				NewsPo t = newsSvc.getNewsByNewsUid(Integer.parseInt(view.getHiddenNewsUid()));
				String lastUpdateDate = DateTimeHelper.formatDateTimeToString(t.getUpdatedDate());
				if (!lastUpdateDate.equals(view.getLastUpdatedDate())) {
					view.setUpdateSuccess("N");
					view.setDisplayMessage("Record had been updated by other, please refresh the content.");
						
					return new ModelAndView("maintenance/newsMaintenanceDetail", "formBean", view);
				}
				
				// Update exist News
				NewsPo tmpNews = newsSvc.getNewsByNewsUid(Integer.parseInt(view.getHiddenNewsUid()));
				tmpNews.setEffectiveFrom(sdf.parse(view.getEffectiveFrom()));
				tmpNews.setEffectiveTo(sdf.parse(view.getEffectiveTo()));
				tmpNews.setNewsDesc(view.getNewsDesc());
	
				newsSvc.update(tmpNews, user);
			}
	
			// Retrieve the news information again
			// NewsPo news = newsSvc.getNewsByNewsUid(newsUid);
			// NewsMaintenanceDetailWebVo webVo = convertToVo(news);
	
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
			webVo.setDisplayMessage("Record update success.");
			webVo.setUpdateSuccess("Y");
			return new ModelAndView("maintenance/newsMaintenanceList", "formBean", webVo);
		}
		catch (Exception ex) {
			EClaimLogger.error("performUpdate:" + ex.getMessage(), ex);
			view.setDisplayMessage("Record update fail.");
			view.setUpdateSuccess("N");
			
			return new ModelAndView("maintenance/newsMaintenanceDetail", "formBean", view);
		}
	}
	
	private NewsMaintenanceDetailWebVo convertToVo(NewsPo news) {
		NewsMaintenanceDetailWebVo webVo = new NewsMaintenanceDetailWebVo();
		webVo.setNewsUid(news.getNewsUid().toString());
		webVo.setNewsDesc(news.getNewsDesc());
		webVo.setEffectiveFrom(DateTimeHelper.formatDateToString(news.getEffectiveFrom()));
		webVo.setEffectiveTo(DateTimeHelper.formatDateToString(news.getEffectiveTo()));
		webVo.setLastUpdatedDate(DateTimeHelper.formatDateTimeToString(news.getUpdatedDate()));
		webVo.setHiddenNewsUid(news.getNewsUid().toString());
		
		return webVo;
	}
}

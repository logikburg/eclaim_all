package hk.org.ha.eclaim.controller.request;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import hk.org.ha.eclaim.bs.request.po.RequestEnquiryWebPo;
import hk.org.ha.eclaim.bs.request.po.RequestPo;
import hk.org.ha.eclaim.bs.request.po.RequestWorkflowHistoryPo;
import hk.org.ha.eclaim.bs.request.svc.IRequestSvc;
import hk.org.ha.eclaim.core.controller.BaseController;
import hk.org.ha.eclaim.model.request.RequestEnquiryWebVo;
import hk.org.ha.eclaim.bs.cs.svc.ICommonSvc;
import hk.org.ha.eclaim.bs.hcm.svc.IHCMSvc;
import hk.org.ha.eclaim.bs.security.po.DataAccessPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.svc.ISecuritySvc;

@Controller
public class RequestEnquiryController extends BaseController {
	@Autowired
	ICommonSvc commonSvc; 

	@Autowired
	ISecuritySvc securitySvc;
	
	@Autowired
	IRequestSvc requestSvc;
	
	@Autowired
	IHCMSvc hcmSvc;
	
	private int maxRecordShown = 150;

	@RequestMapping(value="/request/requestEnquiry", method=RequestMethod.GET)
	public ModelAndView init(HttpServletRequest request) throws Exception {
		RequestEnquiryWebVo vo = new RequestEnquiryWebVo();
		String userId = this.getSessionUser(request).getUserId();
		UserPo user = securitySvc.findUser(userId);
        user.setCurrentRole((String)request.getSession().getAttribute("currentRole"));
		vo.setUserName(user.getUserName());

		return new ModelAndView("request/requestEnquiry", "formBean", vo);
	}

	@RequestMapping(value="/request/requestEnquiry", method=RequestMethod.POST)
	public ModelAndView performSearch(@ModelAttribute("formBean")RequestEnquiryWebVo vo, 
									  HttpServletRequest request) throws Exception {
		String userId = this.getSessionUser(request).getUserId();
        String currentRoleId = (String)request.getSession().getAttribute("currentRole");
		
		// Convert EnquiryWebVo to EnquiryWebPo
        RequestEnquiryWebPo po = new RequestEnquiryWebPo();
        po.setCreatedDateFrom(vo.getCreatedDateFrom());
        po.setCreatedDateTo(vo.getCreatedDateTo());
        po.setUpdatedDateFrom(vo.getUpdatedDateFrom());
        po.setUpdatedDateTo(vo.getUpdatedDateTo());
		po.setRequestId(vo.getRequestId());
		
		int userRoleId = (int) request.getSession().getAttribute("currentUserRoleId");
		List<DataAccessPo> dataAccessList = securitySvc.getDataAccessByRoleId(userRoleId);
		System.out.println("userRoleId: " + userRoleId);
		
		List<RequestPo> list = requestSvc.searchRequest(dataAccessList, po, userId, currentRoleId);
		List<RequestPo> resultList = new ArrayList<RequestPo>();
		for (int i=0; i<list.size(); i++) {
			if (i >= maxRecordShown) {
				break;
			}
			
			resultList.add(list.get(i));
		}
		
		vo.setSearchResultList(resultList);
		vo.setHaveResult("Y");
		vo.setTotalRecordCount(String.valueOf(list.size()));
		if (list.size() > maxRecordShown) {
			vo.setShowRecordTrimmedMsg("Y");
		}
		else {
			vo.setShowRecordTrimmedMsg("N");
		}
		
		return new ModelAndView("request/requestEnquiry", "formBean", vo);
	}

	
	@RequestMapping(value="/request/getRequestHistory", method=RequestMethod.POST)
	public @ResponseBody RequestEnquiryWebVo getRequestHistory(@RequestParam("requestUid") String requestUid) {
//		List<RequestWorkflowHistoryPo> resultList = requestSvc.getRequestHistory(Integer.parseInt(requestUid));
		
		RequestEnquiryWebVo result = new RequestEnquiryWebVo();
//		result.setHistoryList(resultList);
		
		return result;
	}
}
package hk.org.ha.eclaim.controller.maintenance;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import hk.org.ha.eclaim.core.controller.BaseController;
import hk.org.ha.eclaim.model.maintenance.EditProfileWebVo;
import hk.org.ha.eclaim.bs.cs.svc.ICommonSvc;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.svc.ISecuritySvc;

@Controller
public class EditProfileController extends BaseController {
	
	@Autowired
	ISecuritySvc securitySvc;
	
	@Autowired
	ICommonSvc commonSvc;
	
	@RequestMapping(value="/maintenance/editProfile", method=RequestMethod.GET)
    public ModelAndView viewProfile(HttpServletRequest request) throws Exception {
		String userId = this.getSessionUser(request).getUserId();
		System.out.println("user Id: " + userId);
		UserPo user = securitySvc.findUser(userId);
        user.setCurrentRole((String)request.getSession().getAttribute("currentRole"));
		EditProfileWebVo webVo = new EditProfileWebVo();
		webVo.setUserId(user.getUserId());
		webVo.setUserName(user.getUserName());
		webVo.setEmail(user.getEmail());
		webVo.setPhone(user.getPhoneNo());
		
        return new ModelAndView("maintenance/editProfile", "formBean", webVo);
    }
	
	@RequestMapping(value="/maintenance/editProfile", method=RequestMethod.POST)
    public ModelAndView performUpdate(@ModelAttribute("formBean")EditProfileWebVo webVo,
    		                          HttpServletRequest request) throws Exception {
		System.out.println("Perform Update");
		String userId = this.getSessionUser(request).getUserId();
		String roleId = (String)request.getSession().getAttribute("currentRole");
		UserPo user = securitySvc.findUser(webVo.getUserId());
		user.setUserName(webVo.getUserName());
		user.setPhoneNo(webVo.getPhone());
		user.setEmail(webVo.getEmail());
		user.setUpdatedBy(userId);
		user.setUpdatedRoleId(roleId);
		user.setUpdatedDate(new Date());
		securitySvc.updateUser(user);
		
        request.getSession().setAttribute("currentUserName", webVo.getUserName());
		
		webVo.setUpdateSuccess("Y");
		webVo.setDisplayMessage("Profile update success.");
		return new ModelAndView("maintenance/editProfile", "formBean", webVo);
	}
}

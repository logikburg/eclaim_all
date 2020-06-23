package hk.org.ha.eclaim.controller.maintenance;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import hk.org.ha.eclaim.core.controller.BaseController;
import hk.org.ha.eclaim.model.security.SwitchRoleWebVo;
import hk.org.ha.eclaim.bs.cs.svc.ICommonSvc;
import hk.org.ha.eclaim.bs.security.po.MPRSFunctionPo;
import hk.org.ha.eclaim.bs.security.po.RolePo;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.svc.ISecuritySvc;
import hk.org.ha.eclaim.core.helper.StrHelper;
import hk.org.ha.eclaim.core.logger.EClaimLogger;

@Controller
public class SwitchRoleController extends BaseController {
	@Autowired
	ISecuritySvc securitySvc;

	@Autowired
	ICommonSvc commonSvc;

	@RequestMapping(value="/maintenance/switchRole", method=RequestMethod.GET)
	public ModelAndView initList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userId = this.getSessionUser(request).getUserId();
		EClaimLogger.debug("user Id: " + userId);
		// Get the user info
		UserPo user = securitySvc.findUser(userId);

		SwitchRoleWebVo webVo = new SwitchRoleWebVo();
		if (user.getUserRole() != null) {
			for (int i=0; i<user.getUserRole().size(); i++) {
				RolePo rolePo = user.getUserRole().get(i).getRole();
				rolePo.setDefaultRole(user.getUserRole().get(i).getDefaultRole());
				
				webVo.addRoleList(rolePo);
			}
		}
		EClaimLogger.info("currentRole: " + (String)request.getSession().getAttribute("currentRole"));
		webVo.setCurrentRole((String)request.getSession().getAttribute("currentRole"));

		return new ModelAndView("maintenance/switchRole", "formBean", webVo);
	}

	@RequestMapping(value="/maintenance/switchRole", method=RequestMethod.POST)
	public ModelAndView performUpdate(@ModelAttribute("formBean")SwitchRoleWebVo webVo,
			HttpServletRequest request, HttpServletResponse reponse) throws Exception {
		System.out.println("SwitchController Perform Update");
		boolean isError = true;
		String userId = this.getSessionUser(request).getUserId();
		UserPo user = securitySvc.findUser(userId);
		String currentRole = (String)request.getSession().getAttribute("currentRole");
		user.setCurrentRole(currentRole);
		
		String switchToRoleId = webVo.getSelectedRoleId();
		String switchToRoleName = "";
		List<String> funcIdList = new ArrayList<String>();
		
		if ("setDefault".equals(webVo.getFormAction())) {
			securitySvc.updateDefaultRole(userId, webVo.getSelectedRoleId(), user);
			
			user = securitySvc.findUser(userId);
			webVo = new SwitchRoleWebVo();
			if (user.getUserRole() != null) {
				for (int i=0; i<user.getUserRole().size(); i++) {
					RolePo rolePo = user.getUserRole().get(i).getRole();
					rolePo.setDefaultRole(user.getUserRole().get(i).getDefaultRole());
					
					webVo.addRoleList(rolePo);
				}
			}
			EClaimLogger.info("currentRole: " + (String)request.getSession().getAttribute("currentRole"));
			webVo.setCurrentRole((String)request.getSession().getAttribute("currentRole"));
			
			webVo.setUpdateSuccess("Y");
			webVo.setDisplayMessage("Default role update success.");
		}
		else {
			System.out.println("switchToRoleId="+switchToRoleId);
			if (StrHelper.isNotEmpty(switchToRoleId)) {
				System.out.println("user Id: " + userId);
				// Get the user info
				user = securitySvc.findUser(userId);
				if (user.getUserRole() != null) {
					for (int i=0; i<user.getUserRole().size(); i++) {
						webVo.addRoleList(user.getUserRole().get(i).getRole());
						if (switchToRoleId.equals(user.getUserRole().get(i).getRole().getRoleId())) {
							switchToRoleName = user.getUserRole().get(i).getRole().getRoleName();
							// Get function ID for current role
					     	List<MPRSFunctionPo> funcList = new ArrayList<MPRSFunctionPo>();
					     	funcList.addAll(securitySvc.getFunctionListByRole(switchToRoleId));
					     	funcIdList = new ArrayList<String>();
					        for (int j=0; j<funcList.size(); j++) {
					        	funcIdList.add(funcList.get(j).getFuncId());
					        }
							isError = false;
						}
					}
				}
			}
			
			if (!isError) {
	        	webVo.setCurrentRole(switchToRoleId);
		        user.setCurrentRole(switchToRoleId);
	        	request.getSession().setAttribute("currentRole", switchToRoleId);
				request.getSession().setAttribute("currentRoleName", switchToRoleName);
				request.getSession().setAttribute("currentUser", user);
				request.getSession().setAttribute("funcList", funcIdList);
				webVo.setUpdateSuccess("Y");
				webVo.setDisplayMessage("User role switch success.");
			}
			else {
				webVo.setCurrentRole((String)request.getSession().getAttribute("currentRole"));
				webVo.setUpdateSuccess("N");
				webVo.setDisplayMessage("Fail to switch role due to invalid role.");
			}
		}
		
		
		return new ModelAndView("maintenance/switchRole", "formBean", webVo);
	}
}

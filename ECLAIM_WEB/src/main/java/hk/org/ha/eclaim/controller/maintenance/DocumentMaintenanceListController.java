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

import hk.org.ha.eclaim.bs.maintenance.po.DocumentPo;
import hk.org.ha.eclaim.bs.maintenance.svc.IDocumentSvc;
import hk.org.ha.eclaim.core.controller.BaseController;
import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.model.maintenance.DocumentMaintenanceListWebVo;
import hk.org.ha.eclaim.model.maintenance.DocumentWebVo;
import hk.org.ha.eclaim.bs.cs.svc.ICommonSvc;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.svc.ISecuritySvc;

@Controller
public class DocumentMaintenanceListController extends BaseController {

	@Autowired
	IDocumentSvc documentSvc;
	
	@Autowired
	ICommonSvc commonSvc;

	@Autowired
	ISecuritySvc securitySvc;

	@RequestMapping(value="/maintenance/documentMaintenance", method=RequestMethod.GET)
	public ModelAndView initList(HttpServletRequest request) throws Exception {
		// Get All document information
		List<DocumentPo> allDocument = documentSvc.getAllActiveDocuments();
		List<DocumentWebVo> allDocumentVo = new ArrayList<DocumentWebVo>();
		DocumentWebVo documentVo = null;
		for (int i=0; i<allDocument.size(); i++) {
			documentVo = new DocumentWebVo();
			documentVo.setDocumentUid(allDocument.get(i).getDocumentUid());
			documentVo.setDocumentDesc(allDocument.get(i).getDocumentName());
			documentVo.setDocumentType("U".equals(allDocument.get(i).getDocumentType())?"URL":"Attachment");
			documentVo.setRecState(MPRSConstant.MPRS_STATE_ACTIVE.equals(allDocument.get(i).getRecState())?"Active":"Inactive");
			allDocumentVo.add(documentVo);
		}
		
		DocumentMaintenanceListWebVo webVo = new DocumentMaintenanceListWebVo();
		webVo.setAllDocument(allDocumentVo);
		return new ModelAndView("maintenance/documentMaintenanceList", "formBean", webVo);
	}

	@RequestMapping(value="/maintenance/documentMaintenance", method=RequestMethod.POST)
	public ModelAndView performUpdate(@ModelAttribute DocumentMaintenanceListWebVo view, HttpServletRequest request) throws Exception {
		System.out.println("DocumentMaintenanceListController.performUpdate()");
		// Get the user name from cookie
		String userId = this.getSessionUser(request).getUserId();
		UserPo user = securitySvc.findUser(userId);
        user.setCurrentRole((String)request.getSession().getAttribute("currentRole"));

		// Get the delete document Id
		String deleteDocumentId = view.getDeleteDocumentUid();

		// Perform Delete
		documentSvc.deleteDocument(Integer.parseInt(deleteDocumentId), user);

		// Get All document information
		List<DocumentPo> allDocument = documentSvc.getAllActiveDocuments();

		DocumentMaintenanceListWebVo webVo = new DocumentMaintenanceListWebVo();
		List<DocumentWebVo> allDocumentVo = new ArrayList<DocumentWebVo>();
		DocumentWebVo documentVo = null;
		for (int i=0; i<allDocument.size(); i++) {
			documentVo = new DocumentWebVo();
			documentVo.setDocumentUid(allDocument.get(i).getDocumentUid());
			documentVo.setDocumentDesc(allDocument.get(i).getDocumentName());
			documentVo.setDocumentType("U".equals(allDocument.get(i).getDocumentType())?"URL":"Attachment");
			documentVo.setRecState("A".equals(allDocument.get(i).getRecState())?"Active":"Inactive");
			allDocumentVo.add(documentVo);
		}
		
		webVo.setAllDocument(allDocumentVo);
		webVo.setUpdateSuccess("Y");
		webVo.setUpdateSuccess("Record delete success.");
		webVo.setUserName(user.getUserName());
		return new ModelAndView("maintenance/documentMaintenanceList", "formBean", webVo);
	}
}

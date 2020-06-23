package hk.org.ha.eclaim.controller.maintenance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import hk.org.ha.eclaim.bs.maintenance.po.DocumentPo;
import hk.org.ha.eclaim.bs.maintenance.svc.IDocumentSvc;
import hk.org.ha.eclaim.core.controller.BaseController;
import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.model.maintenance.DocumentMaintenanceDetailWebVo;
import hk.org.ha.eclaim.model.maintenance.DocumentMaintenanceListWebVo;
import hk.org.ha.eclaim.model.maintenance.DocumentWebVo;
import hk.org.ha.eclaim.bs.cs.svc.ICommonSvc;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.svc.ISecuritySvc;
import hk.org.ha.eclaim.core.helper.DateTimeHelper;
import hk.org.ha.eclaim.core.logger.EClaimLogger;

@Controller
public class DocumentMaintenanceDetailController extends BaseController {

	@Autowired
	IDocumentSvc documentSvc;
	
	@Autowired
	ICommonSvc commonSvc;
	
	@Autowired
	ISecuritySvc securitySvc;

	@Value("${attachment.docMainDir}")
	private String DOC_PATH;

	@RequestMapping(value="/maintenance/documentMaintenanceDetail", method=RequestMethod.GET)
	public ModelAndView initList(HttpServletRequest request,
			@RequestParam(required=false) String id) throws Exception {
		// Get the document id
		EClaimLogger.info("documentUid: " + id);
		DocumentMaintenanceDetailWebVo webVo = new DocumentMaintenanceDetailWebVo();

		DocumentPo document = null;
		if (id == null) {
			document = new DocumentPo();
		}
		else {
			document = documentSvc.getDocumentByDocumentUid(Integer.parseInt(id));
			webVo = convertToVo(document);
		}
		
		return new ModelAndView("maintenance/documentMaintenanceDetail", "formBean", webVo);
	}

	@RequestMapping(value="/maintenance/documentMaintenanceDetail", method=RequestMethod.POST)
	public ModelAndView performUpdate(@ModelAttribute DocumentMaintenanceDetailWebVo view,
			                          HttpServletRequest request) throws Exception {
		try {
			// Get the user name from cookie
			String userId = this.getSessionUser(request).getUserId();
			UserPo user = securitySvc.findUser(userId);
	        user.setCurrentRole((String)request.getSession().getAttribute("currentRole"));
			EClaimLogger.info("view.getDocumentId(): " + view.getDocumentUid());
	
			if ("".equals(view.getDocumentUid())) {
				DocumentPo tmpDocument = new DocumentPo();
				tmpDocument.setDocumentName(view.getDocumentDesc());
				tmpDocument.setDocumentType(view.getDocumentType());
				tmpDocument.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
				
				if ("U".equals(tmpDocument.getDocumentType())) {
					tmpDocument.setDocumentUrl(view.getDocumentUrl());
				}
				else if ("A".equals(tmpDocument.getDocumentType())) {
					tmpDocument.setDocumentFileName(view.getDocumentFile().getOriginalFilename());
				} 
				
				// Create new Document
				int documentUid = documentSvc.insert(tmpDocument, user);
				
				if ("A".equals(tmpDocument.getDocumentType())) {
					String docUidStr = String.valueOf(documentUid);
					while (docUidStr.length() != 10) {
						docUidStr = "0" + docUidStr;
					}
					String filePath = docUidStr + File.separator + tmpDocument.getDocumentFileName();
					
					// Check is the folder exist
					File tmpPath = new File(DOC_PATH + docUidStr);
					if (!tmpPath.exists()) {
						tmpPath.mkdirs();
					}
					
					File newFile = new File(DOC_PATH + filePath);
					if (!newFile.exists()) {
						newFile.createNewFile();
					}
					
					System.out.println("Do upload: " + newFile.getAbsolutePath());
					
					OutputStream optStream = null;
					try {
						optStream = new FileOutputStream(newFile);
						optStream.write(view.getDocumentFile().getBytes());
						optStream.flush();
					}
					catch (Exception ex) {
						throw ex;
					}
					finally {
						try { 
							optStream.close(); 
						} catch (Exception ignore) {
							throw ignore;
						}
					}
				}
			}
			else {
				// Check last update date
				DocumentPo documentPo = documentSvc.getDocumentByDocumentUid(Integer.parseInt(view.getDocumentUid()));
				String lastUpdateDate = DateTimeHelper.formatDateTimeToString(documentPo.getUpdatedDate());
				if (!lastUpdateDate.equals(view.getLastUpdatedDate())) {
					view.setUpdateSuccess("N");
					view.setDisplayMessage("Record had been updated by other, please refresh the content.");
						
					return new ModelAndView("maintenance/documentMaintenanceDetail", "formBean", view);
				}
				
				// Update exist Document
				DocumentPo tmpDocument = documentSvc.getDocumentByDocumentUid(Integer.parseInt(view.getDocumentUid()));
				tmpDocument.setDocumentName(view.getDocumentDesc());
				tmpDocument.setDocumentType(view.getDocumentType());
				
				if ("U".equals(tmpDocument.getDocumentType())) {
					tmpDocument.setDocumentUrl(view.getDocumentUrl());
				}
				else if ("A".equals(tmpDocument.getDocumentType())) {
					if (!"".equals(view.getDocumentFile().getOriginalFilename())) {
						tmpDocument.setDocumentFileName(view.getDocumentFile().getOriginalFilename());
					}
				} 
				
				documentSvc.update(tmpDocument, user);
				
				// Create new Document
				int id = tmpDocument.getDocumentUid();
				
				if ("A".equals(tmpDocument.getDocumentType()) && !"".equals(view.getDocumentFile().getOriginalFilename())) {
					String docUidStr = String.valueOf(id);
					while (docUidStr.length() != 10) {
						docUidStr = "0" + docUidStr;
					}
					String filePath = docUidStr + File.separator + tmpDocument.getDocumentFileName();
					
					// Check is the folder exist
					File tmpPath = new File(DOC_PATH + docUidStr);
					if (!tmpPath.exists()) {
						tmpPath.mkdirs();
					}
					
					File newFile = new File(DOC_PATH + filePath);
					if (!newFile.exists()) {
						newFile.createNewFile();
					}
					
					OutputStream optStream = null;
					try {
						optStream = new FileOutputStream(newFile);
						optStream.write(view.getDocumentFile().getBytes());
						optStream.flush();
					}
					catch (Exception ex) {
						throw ex;
					}
					finally {
						try { 
							optStream.close(); 
						} catch (Exception ignore) {
							throw ignore;
						}
					}
				}
			}
	
			// Get All document information
			List<DocumentPo> allDocument = documentSvc.getAllActiveDocuments();
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
			
			DocumentMaintenanceListWebVo webVo = new DocumentMaintenanceListWebVo();
			webVo.setAllDocument(allDocumentVo);
			webVo.setDisplayMessage("Record update success.");
			webVo.setUpdateSuccess("Y");
			return new ModelAndView("maintenance/documentMaintenanceList", "formBean", webVo);
		}
		catch (Exception ex) {
			EClaimLogger.error("performUpdate Exception", ex);
			view.setDisplayMessage("Record update fail.");
			view.setUpdateSuccess("N");
			
			return new ModelAndView("maintenance/documentMaintenanceDetail", "formBean", view);
		}
	}
	
	private DocumentMaintenanceDetailWebVo convertToVo(DocumentPo document) {
		DocumentMaintenanceDetailWebVo webVo = new DocumentMaintenanceDetailWebVo();
		webVo.setDocumentUid(String.valueOf(document.getDocumentUid()));
		webVo.setDocumentDesc(document.getDocumentName());
		webVo.setDocumentType(document.getDocumentType());
		webVo.setDocumentUrl(document.getDocumentUrl());
		webVo.setDocumentFileName(document.getDocumentFileName());
		webVo.setRecState(document.getRecState());
		webVo.setLastUpdatedDate(DateTimeHelper.formatDateTimeToString(document.getUpdatedDate()));
		
		return webVo;
	}
	
	@RequestMapping(value="/maintenance/downloadDocument", method=RequestMethod.GET)
	public void downloadDocument(HttpServletRequest request,
			HttpServletResponse response, @RequestParam(required=true) String did) throws Exception {
		EClaimLogger.info("downloadDocument - did: " + did);
		DocumentPo documentPo = documentSvc.getDocumentByDocumentUid(Integer.parseInt(did));
		if (documentPo != null) {
			String contentDisposition = String.format("attachment; filename=%s", documentPo.getDocumentFileName());
			response.setHeader("Content-disposition", contentDisposition);
			
			String docUidStr = did;
			while (docUidStr.length() != 10) {
				docUidStr = "0" + docUidStr;
			}
			String filePath = docUidStr + File.separator + documentPo.getDocumentFileName();
			
			File file = new File(DOC_PATH + filePath);
			FileInputStream in = new FileInputStream(file);
			final OutputStream outStream = response.getOutputStream();
			
			byte[] buf = new byte[1024];
			int count = 0;
			while ((count = in.read(buf)) > 0) {
				outStream.write(buf, 0, count);
			}

			outStream.close();
			in.close();
		}
		
	}
}

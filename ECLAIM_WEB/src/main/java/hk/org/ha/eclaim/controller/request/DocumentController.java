package hk.org.ha.eclaim.controller.request;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import hk.org.ha.eclaim.bs.project.po.ProjectDocumentPo;
import hk.org.ha.eclaim.bs.project.svc.IProjectDocumentSvc;
import hk.org.ha.eclaim.bs.request.po.RequestSystemFilePo;
import hk.org.ha.eclaim.bs.request.po.RequestTempFilePo;
import hk.org.ha.eclaim.bs.request.svc.IRequestSvc;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.svc.ISecuritySvc;
import hk.org.ha.eclaim.core.controller.BaseController;
import hk.org.ha.eclaim.core.logger.EClaimLogger;
import hk.org.ha.eclaim.core.po.MPRSConstant;

@Controller
public class DocumentController extends BaseController{
	
	@Autowired
	private IRequestSvc requestSvc;
	
	@Autowired
	private ISecuritySvc securitySvc;
	
	@Autowired
	private IProjectDocumentSvc docSvc;
	
	@Value("${attachment.requestTmpDir}")
	private String DOC_PATH;
	
	@Value("${attachment.requestSysDir}")
	private String SYS_DOC_PATH;
	
	@Value("${attachment.requestAttachDir}")
	private String APPROVAL_DOC_PATH;
	
	@Value("${attachment.postAttachDir}")
	private String POST_DOC_PATH;
	
	
	
	@RequestMapping(value="/common/uploadFile", method = RequestMethod.POST, consumes = "multipart/form-data")
	public @ResponseBody String doUpload(@RequestParam("approvalFile") MultipartFile multipartFile,  
														   @RequestParam("desc") String description,
												           HttpServletRequest request,
												           HttpServletResponse response) throws Exception {      
		String fileName = multipartFile.getOriginalFilename();
		EClaimLogger.info("### Perform doUpload - Uploaded: filename=" + fileName + ", "+ multipartFile.getSize() + " bytes");
		
		String userId = this.getSessionUser(request).getUserId();
		String currentRole = (String)request.getSession().getAttribute("currentRole");
		
		RequestTempFilePo file = new RequestTempFilePo();
		
		// file.setContent(multipartFile.getBytes());
		file.setFilename(fileName);
		file.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
		file.setDescription(description);
		file.setCreatedBy(userId);
		file.setCreatedRoleId(currentRole);
		file.setCreatedDate(new Date());
		file.setUpdatedBy(userId);
		file.setUpdatedRoleId(currentRole);
		file.setUpdatedDate(new Date());
		requestSvc.uploadRequestTempFile(file, multipartFile);
	    return String.valueOf(file.getTmpFileUid());
	}
	
	@RequestMapping(value="/common/getSysFile", method=RequestMethod.GET)
	public void getSystemFile(HttpServletResponse response, @RequestParam(required=true) String code) throws Exception {
		//String aid = request.getParameter("uid");
		EClaimLogger.info("get System File code: " + code);

		RequestSystemFilePo attachment = requestSvc.getSystemFile(code);

		// response.setContentType("application/x-pdf");
		response.setHeader("Content-disposition", "attachment; filename=" + attachment.getFilename());

//		String docUidStr = code;
//		while (docUidStr.length() != 10) {
//			docUidStr = "0" + docUidStr;
//		}
//		String filePath = docUidStr + File.separator + attachment.getFilename();
		
		File file = new File(SYS_DOC_PATH + attachment.getFilename());
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
	
	@RequestMapping(value="/common/getTempFile", method=RequestMethod.GET)
	public void getTempFile(HttpServletRequest request,
			HttpServletResponse response, @RequestParam(required=true) String uid) throws Exception {
		//String aid = request.getParameter("uid");
		EClaimLogger.info("getTempFile aid: " + uid);

		RequestTempFilePo attachment = requestSvc.getTempFile(Integer.parseInt(uid));

		// response.setContentType("application/x-pdf");
		response.setHeader("Content-disposition", "attachment; filename=" + attachment.getFilename());

		String docUidStr = uid;
		while (docUidStr.length() != 10) {
			docUidStr = "0" + docUidStr;
		}
		String filePath = docUidStr + File.separator + attachment.getFilename();
		
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
	
	@RequestMapping(value="/common/downloadAttachment", method=RequestMethod.GET)
	public void downloadAttachment(HttpServletRequest request,
			HttpServletResponse response, @RequestParam(required=true) String aid) throws Exception {
		
		EClaimLogger.info("downloadAttachment aid: " + aid);
		ProjectDocumentPo attachment = docSvc.getProjectDocumentPo(Integer.parseInt(aid));
		
		// response.setContentType("application/x-pdf");
		response.setHeader("Content-disposition", "attachment; filename=" + attachment.getFileName());
		
		File file = null;
		if (attachment.getDocumentId() != null) {
			String filepath  = attachment.getPath();
			String fileName = attachment.getFileName();
			
			file = new File(filepath + fileName);
		}
		else {
			throw new Exception("No Attachment record!");
		}
		
		EClaimLogger.info("Download File From: " + file.getAbsolutePath());
		if (file.exists()) {
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
	
	@RequestMapping(value="/common/deleteAttachment", method = RequestMethod.POST)
	public void deleteAttachment(@RequestParam("attachmentUid") String attachmentUid, 
												 HttpServletRequest request,
												 HttpServletResponse response) throws Exception {      
		EClaimLogger.info("deleteAttachment: " + attachmentUid);
		
		String userId = this.getSessionUser(request).getUserId();
		String currentRole = (String)request.getSession().getAttribute("currentRole");
		UserPo user = securitySvc.findUser(userId);
		user.setCurrentRole(currentRole);
		
		// Look up the attachment information
		ProjectDocumentPo attachment = docSvc.getProjectDocumentPo(Integer.parseInt(attachmentUid));
		
		if(!attachment.getUpdatedBy().equals(user.getUserName())) {
			throw new Exception("No ");
		}
		docSvc.deleteProjectDocument(attachment.getDocumentId());
		
		// Delete the attachment physical
		String destPath = attachment.getPath() + attachment.getFileName();
		EClaimLogger.info("Delete attachment: " + destPath);
		File tmpFile = new File(destPath);
		if(tmpFile.exists()) {
			tmpFile.delete();
		}
	}
}

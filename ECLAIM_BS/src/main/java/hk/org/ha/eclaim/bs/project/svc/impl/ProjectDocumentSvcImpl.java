package hk.org.ha.eclaim.bs.project.svc.impl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hk.org.ha.eclaim.bs.project.dao.IProjectDocumentDao;
import hk.org.ha.eclaim.bs.project.po.ProjectDocumentPo;
import hk.org.ha.eclaim.bs.project.svc.IProjectDocumentSvc;
import hk.org.ha.eclaim.bs.request.dao.IRequestTempFileDao;
import hk.org.ha.eclaim.bs.request.po.RequestTempFilePo;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.core.po.MPRSConstant;

@Service("projectDocumentSvc")
public class ProjectDocumentSvcImpl implements IProjectDocumentSvc{

	@Autowired
	private IProjectDocumentDao docDao;
	
	@Autowired
	private IRequestTempFileDao requestTempFileDao;
	
	@Value("${attachment.docMainDir}")
	private String DOC_PATH;
	
	@Value("${attachment.requestTmpDir}")
	private String TMP_DOC_PATH;
	
	public ProjectDocumentPo getProjectDocumentPo(int uid) {
		return docDao.getProjectDocumentPo(uid);
	}
	
	public List<ProjectDocumentPo> getProjectDocumentByProjectId(int projectId) {
		return docDao.getProjectDocumentListByProject(projectId);
	}
	
	public List<ProjectDocumentPo> getProjectDocumentByProjectId(int projectId, int verId) {
		return docDao.getProjectDocumentListByProject(projectId, verId);
	}
	
	@Transactional
	public void deleteProjectDocument(int docId) {
		docDao.deleteProject(docId);
	}

	@Transactional
	public int insert(ProjectDocumentPo docPo, UserPo updateUser) {
		return docDao.insert(docPo, updateUser);
	}

	@Transactional
	public void update(ProjectDocumentPo docPo, UserPo updateUser) {
		docDao.update(docPo, updateUser);
	}
	private String getDirName(int id) {
		String tmpSourcePath = String.valueOf(id);
		while (tmpSourcePath.length() != 10) {
			tmpSourcePath = "0" + tmpSourcePath;
		}
		return tmpSourcePath;
	}
	
	public void uploadFileFromTempFile(int tmpFileId, int projectId, int projectVer, UserPo user) throws Exception {
		
		RequestTempFilePo tempFile = requestTempFileDao.getTempFile(tmpFileId);

		// Perform temp file
		String tmpSourcePath = getDirName(tmpFileId);
		
		File tmpFile = new File(TMP_DOC_PATH + tmpSourcePath + File.separator + tempFile.getFilename());
		System.out.println("Temp File: " + tmpFile.getAbsolutePath());
		
		
		String fileName = tempFile.getFilename();
		String docUidStr = getDirName(projectId);
		
		String filePath = DOC_PATH + docUidStr + File.separator;

		// Check is the folder exist
		File tmpPath = new File(DOC_PATH + docUidStr);
		if (!tmpPath.exists()) {
			tmpPath.mkdirs();
		}

		File newFile = new File(filePath+ fileName);
		if (newFile.exists()) {
			newFile.delete();
		}
		
		
		System.out.println("Do upload: " + newFile.getAbsolutePath());
		if (!tmpFile.renameTo(newFile)) {
			throw new Exception("Fail to upload attachment.");
		}
		
		ProjectDocumentPo docPo = new ProjectDocumentPo();
		docPo.setProjectId(projectId);
		docPo.setProjectVerId(projectVer);
		docPo.setDescription(tempFile.getDescription());
		docPo.setPath(filePath);
		docPo.setFileName(fileName);
		docPo.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
		this.insert(docPo, user);
	}
}

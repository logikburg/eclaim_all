package hk.org.ha.eclaim.bs.project.svc;

import java.util.List;

import hk.org.ha.eclaim.bs.project.po.ProjectDocumentPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

public interface IProjectDocumentSvc {

	public ProjectDocumentPo getProjectDocumentPo(int uid);
	public List<ProjectDocumentPo> getProjectDocumentByProjectId(int projectId);
	public List<ProjectDocumentPo> getProjectDocumentByProjectId(int projectId, int verId);
	public void deleteProjectDocument(int docId);
	public int insert(ProjectDocumentPo docPo, UserPo updateUser);
	public void update(ProjectDocumentPo docPo, UserPo updateUser);
	public void uploadFileFromTempFile(int tmpFileId, int projectId, int projectVer, UserPo user) throws Exception ;
}

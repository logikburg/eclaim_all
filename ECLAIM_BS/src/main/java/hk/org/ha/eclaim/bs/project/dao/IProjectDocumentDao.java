package hk.org.ha.eclaim.bs.project.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.project.po.ProjectDocumentPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

public interface IProjectDocumentDao {

	public ProjectDocumentPo getProjectDocumentPo(int uid);
	public List<ProjectDocumentPo> getProjectDocumentListByProject(int projectId);
	public List<ProjectDocumentPo> getProjectDocumentListByProject(int projectId, int verId);
	public void deleteProject(int docId);
	public int insert(ProjectDocumentPo project, UserPo updateUser);
	public void update(ProjectDocumentPo project, UserPo updateUser);
}

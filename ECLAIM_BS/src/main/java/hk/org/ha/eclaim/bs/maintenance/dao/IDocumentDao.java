package hk.org.ha.eclaim.bs.maintenance.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.maintenance.po.DocumentPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

public interface IDocumentDao {
	public List<DocumentPo> getAllActiveDocuments();
	public DocumentPo getDocumentByDocumentUid(int documentId);
	public void deleteDocument(int documentUid, UserPo updateUser);
	public int insert(DocumentPo news, UserPo updateUser);
	public void update(DocumentPo news, UserPo updateUser);
}

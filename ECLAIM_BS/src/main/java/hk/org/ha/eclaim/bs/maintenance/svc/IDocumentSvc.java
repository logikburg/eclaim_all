package hk.org.ha.eclaim.bs.maintenance.svc;

import java.util.List;

import hk.org.ha.eclaim.bs.maintenance.po.DocumentPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

public interface IDocumentSvc {
	public List<DocumentPo> getAllActiveDocuments();
	public DocumentPo getDocumentByDocumentUid(int documentUid);
	public void deleteDocument(int documentUid, UserPo updateUser);
	public int insert(DocumentPo doc, UserPo updateUser);
	public void update(DocumentPo doc, UserPo updateUser);
}

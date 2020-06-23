package hk.org.ha.eclaim.bs.maintenance.svc.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hk.org.ha.eclaim.bs.maintenance.dao.IDocumentDao;
import hk.org.ha.eclaim.bs.maintenance.po.DocumentPo;
import hk.org.ha.eclaim.bs.maintenance.svc.IDocumentSvc;
import hk.org.ha.eclaim.bs.security.po.UserPo;

@Service("documentSvc")
public class DocumentSvcImpl implements IDocumentSvc {

	@Autowired
	IDocumentDao documentDao;
	
	public List<DocumentPo> getAllActiveDocuments() {
		return documentDao.getAllActiveDocuments();
	}
	
	public DocumentPo getDocumentByDocumentUid(int documentUid) {
		return documentDao.getDocumentByDocumentUid(documentUid);
	}

	@Transactional
	public void deleteDocument(int documentUid, UserPo updateUser) {
		documentDao.deleteDocument(documentUid, updateUser);
	}

	@Transactional
	public int insert(DocumentPo document, UserPo updateUser) {
		return documentDao.insert(document, updateUser);
	}

	@Transactional
	public void update(DocumentPo document, UserPo updateUser) {
		documentDao.update(document, updateUser);
	}
}

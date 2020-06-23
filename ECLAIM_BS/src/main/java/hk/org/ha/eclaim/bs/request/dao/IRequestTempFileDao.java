package hk.org.ha.eclaim.bs.request.dao;

import hk.org.ha.eclaim.bs.request.po.RequestTempFilePo;

public interface IRequestTempFileDao {
	int insert(RequestTempFilePo file);
	
	RequestTempFilePo getTempFile(int uid);
}

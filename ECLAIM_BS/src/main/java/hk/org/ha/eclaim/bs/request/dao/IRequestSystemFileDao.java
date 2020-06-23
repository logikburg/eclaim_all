package hk.org.ha.eclaim.bs.request.dao;

import hk.org.ha.eclaim.bs.request.po.RequestSystemFilePo;

public interface IRequestSystemFileDao {
	
	public RequestSystemFilePo getSystemFile(String code);
}

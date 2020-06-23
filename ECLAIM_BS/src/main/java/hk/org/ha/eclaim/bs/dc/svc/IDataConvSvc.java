package hk.org.ha.eclaim.bs.dc.svc;

import java.io.File;

public interface IDataConvSvc {
	
	public void initDataSource() throws Exception;
	public void importDcTemplateFile(File dcPostDataFile) throws Exception;
	public void proceedDataConv() throws Exception;
		
}

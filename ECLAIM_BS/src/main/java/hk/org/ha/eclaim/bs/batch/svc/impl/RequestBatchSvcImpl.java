package hk.org.ha.eclaim.bs.batch.svc.impl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hk.org.ha.eclaim.bs.batch.dao.IRequestBatchDao;
import hk.org.ha.eclaim.bs.batch.po.BatchLogPo;
import hk.org.ha.eclaim.bs.batch.svc.IRequestBatchSvc;
import hk.org.ha.eclaim.core.helper.DateTimeHelper;
import hk.org.ha.eclaim.core.logger.EClaimLogger;

@Service("requestBatchSvc")
public class RequestBatchSvcImpl implements IRequestBatchSvc {
	
	@Autowired
	private IRequestBatchDao requestBatchDao;
	
	@Transactional
	public int updateEndDate() {
		return requestBatchDao.updateEndDate(); // update_limit_duration_end_date
	}
	
	@Transactional
	public int syncPost() {
		return requestBatchDao.syncPost(); // process_daily_status_change
	}
	
	@Transactional
	public int supplePromotion() {
		return requestBatchDao.supplePromotion(); // update_supp_prom
	}
	
	@Transactional
	public int getRptBatchUserAccess(String reportBatchUserAccessDir) {
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		Date lastUpdatedDate = cal.getTime();
		Date lastUpdateDtFrom = cal.getTime();
		
		FileWriter writer = null;
		File userAccessFile = null;
		File lastUpdatedDateFile = null;
		try {
		
			// get the last update date from file
			String lastUpdatedDateFilepath = reportBatchUserAccessDir + File.separator + "Report_Batch_User_Access_Last_Update_Date.txt";
			lastUpdatedDateFile = new File(lastUpdatedDateFilepath);
			System.out.println("#### Last Update Date Filepath=" + lastUpdatedDateFilepath);
			if (!lastUpdatedDateFile.exists()) {
				System.out.println("#### Last Update Date file does not exist ####");
				lastUpdatedDateFile.createNewFile();
			} else {
				System.out.println("#### Last Update Date file exists ####");
				lastUpdateDtFrom = new SimpleDateFormat("dd/MM/yyyy").parse(new String(Files.readAllBytes(lastUpdatedDateFile.toPath())));
			}
			String lastUpdateDtFromStr = DateTimeHelper.formatDateToString(lastUpdateDtFrom);
			System.out.println("#### Last Updated Date From=" + lastUpdateDtFromStr);
			
		    // query user access
			List<String> rptBatchAccessList = requestBatchDao.getRptBatchUserAccess(lastUpdateDtFrom); // REPORT_BATCH_ACCESS
			if (rptBatchAccessList == null) {
				return 1;
			}
			
			// user access filepath
			String userAccessFilepath = reportBatchUserAccessDir + File.separator + DateTimeHelper.formatDateToStringForFilename(lastUpdatedDate) + "_Report_Batch_User_Access.txt";
			System.out.println("#### User Access Filepath=" + userAccessFilepath);
			userAccessFile = new File(userAccessFilepath);
			if (!userAccessFile.exists()) {
				userAccessFile.createNewFile();
			}
			
			// write to user access file
			writer = new FileWriter(userAccessFile); 
			for(String str: rptBatchAccessList) {
			  writer.write(str + "\n");
			  System.out.println("#### User Access=" + str);
			}
			writer.close();
			
			// write to last update date file
			cal.add(Calendar.DATE, 1);
			writer = new FileWriter(lastUpdatedDateFile); 
			writer.write(DateTimeHelper.formatDateToString(cal.getTime()));
			
			writer.close();
		} catch (Exception e) {
			EClaimLogger.error("getRptBatchUserAccess:" + e.getMessage(), e);
			return 1;
		} finally {
			try {
				writer.close();
				writer = null;
				userAccessFile = null;
				lastUpdatedDateFile = null;
			} catch (Exception ignore) {}
		}
		
		return 0;
	}
	
	@Transactional
	public BatchLogPo createBatchLog(String batchName) {
		return requestBatchDao.createBatchLog(batchName);
	}
	
	@Transactional
	public int updateBatchLog(BatchLogPo batchLogPo, String batchResult) {
		return requestBatchDao.updateBatchLog(batchLogPo, batchResult);
	}
	
	@Transactional
	public int createBatchLogDtl(Integer batchUid, String postId, String batchDtlResult) {
		return requestBatchDao.createBatchLogDtl(batchUid, postId, batchDtlResult);
	}
}

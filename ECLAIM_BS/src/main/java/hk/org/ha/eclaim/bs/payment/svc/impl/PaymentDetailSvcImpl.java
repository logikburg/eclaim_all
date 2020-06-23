package hk.org.ha.eclaim.bs.payment.svc.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hk.org.ha.eclaim.bs.payment.constant.PaymentXlsConstant;
import hk.org.ha.eclaim.bs.payment.dao.IPaymentDetailDao;
import hk.org.ha.eclaim.bs.payment.helper.PaymentXlsHelper;
import hk.org.ha.eclaim.bs.payment.po.PaymentBatchPo;
import hk.org.ha.eclaim.bs.payment.po.PaymentBatchTo;
import hk.org.ha.eclaim.bs.payment.po.PaymentDetailPo;
import hk.org.ha.eclaim.bs.payment.po.PaymentDetailVo;
import hk.org.ha.eclaim.bs.payment.svc.IPaymentBatchSvc;
import hk.org.ha.eclaim.bs.payment.svc.IPaymentDetailSvc;
import hk.org.ha.eclaim.core.logger.EClaimLogger;

@Service("paymentDetailSvc")
public class PaymentDetailSvcImpl implements IPaymentDetailSvc {

	@Autowired
	IPaymentDetailDao paymentDetailDao;
	
	@Autowired
	IPaymentBatchSvc paymentBatchSvc;

	FileOutputStream out = null;

	public List<PaymentDetailPo> getAllPaymentBatch() {
		return paymentDetailDao.getAllPaymentDetails();
	}

	public List<PaymentBatchPo> getAllDetail() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<PaymentDetailPo> getDetailByBatchDetailId(int batchDetailId) {
		return paymentDetailDao.getPaymentDetailByBatchId(batchDetailId);
	}

	public int insert() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int insertAll(List<PaymentDetailPo> lstPaymentDetail) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void update(PaymentDetailPo paymentDetailPo) {
		paymentDetailDao.update(paymentDetailPo);
	}

	// validate payment detail sheet
	public List<PaymentDetailPo> prepareBatchFromXlsFile(String fn) {
		FileInputStream in = null;
		Workbook xswb = null;
		Row row = null;
		Cell cel = null;
		Calendar c1 = Calendar.getInstance();
		List<PaymentDetailPo> lstPo = new ArrayList<PaymentDetailPo>();
		PaymentDetailPo po = null;
		PaymentDetailPo poTemp = null;

		String earnedYYYYMM = null;
		int yr = 0;
		int mnth = 0;
		int date = 0;
		int wrkHours = 0;

		try {
			in = new FileInputStream(fn);
			xswb = new XSSFWorkbook(in);
			Iterator<Row> rowIt = xswb.getSheetAt(0).iterator();
			while (rowIt.hasNext()) {
				row = rowIt.next();
				if (row.getRowNum() < 7)
					continue;

				po = new PaymentDetailPo();

				po.setBatchId(111);
				po.setEmpNo("Employee123");

				// Always pending due to require approve
				po.setStatus("PENDING");

				int val = 0;
				String strVal = "";

				Iterator<Cell> celIt = row.cellIterator();
				while (celIt.hasNext()) {
					cel = celIt.next();

					if (cel.getColumnIndex() == PaymentXlsConstant.COLUMN_INDEX_ASSIGN_NO) {
						strVal = cel.getStringCellValue();
						po.setAsgNo(strVal);
					} else if (cel.getColumnIndex() == PaymentXlsConstant.COLUMN_INDEX_NAME) {
						strVal = cel.getStringCellValue();
					} else if (cel.getColumnIndex() == PaymentXlsConstant.COLUMN_INDEX_WORK_LOCATION) {
						strVal = cel.getStringCellValue();
					} else if (cel.getColumnIndex() == PaymentXlsConstant.COLUMN_INDEX_JOB) {
						strVal = cel.getStringCellValue();
						po.setJob(strVal);
					} else if (cel.getColumnIndex() == PaymentXlsConstant.COLUMN_INDEX_HOUR_TYPE) {
						strVal = cel.getStringCellValue();
						po.setHourType(strVal);
					} else if (cel.getColumnIndex() == PaymentXlsConstant.COLUMN_INDEX_COA_ANALYSTIC) {
						val = (int) Math.round(cel.getNumericCellValue());
						po.setCoaAnalystic(Integer.toString(val));
					} else if (cel.getColumnIndex() == PaymentXlsConstant.COLUMN_INDEX_COA_FUND) {
						val = (int) Math.round(cel.getNumericCellValue());
						po.setCoaFund(Integer.toString(val));
					} else if (cel.getColumnIndex() == PaymentXlsConstant.COLUMN_INDEX_COA_INST) {
						val = (int) Math.round(cel.getNumericCellValue());
						po.setCoaInst(Integer.toString(val));
					} else if (cel.getColumnIndex() == PaymentXlsConstant.COLUMN_INDEX_COA_SECTION) {
						val = (int) Math.round(cel.getNumericCellValue());
						po.setCoaSection(Integer.toString(val));
					} else if (cel.getColumnIndex() == PaymentXlsConstant.COLUMN_INDEX_COA_TYPE) {
						val = (int) Math.round(cel.getNumericCellValue());
						po.setCoaType(Integer.toString(val));
					} else if (cel.getColumnIndex() == PaymentXlsConstant.COLUMN_INDEX_EARNED_MONTH) {
						cel = row.getCell(PaymentXlsConstant.COLUMN_INDEX_EARNED_MONTH);
						earnedYYYYMM = cel.getStringCellValue();
						yr = Integer.parseInt(earnedYYYYMM.substring(0, 4));
						mnth = Integer.parseInt(earnedYYYYMM.substring(4, 6)) - 1;
					}

					// cells greater than 10 are belong to days
					else if (cel.getColumnIndex() > PaymentXlsConstant.COLUMN_INDEX_EARNED_MONTH
							&& cel.getColumnIndex() < PaymentXlsConstant.COLUMN_INDEX_TOTAL_HOURS) {
						date = cel.getColumnIndex() - PaymentXlsConstant.COLUMN_INDEX_EARNED_MONTH;
						c1.set(yr, mnth, date);
						po.setDutyDate(c1.getTime());
						wrkHours = (int) Math.round(cel.getNumericCellValue());
						po.setTotalHour(wrkHours);
						lstPo.add(po);

						// clone to insert same object have different work hour on different date.
//						poTemp = (PaymentDetailPo) SerializationUtils.clone(po);
						po = poTemp;
					}
					EClaimLogger.info(cel.getCellTypeEnum().toString());
					EClaimLogger.info(cel.getAddress().toString());
				}
			}
			paymentDetailDao.insertAll(lstPo);

			xswb.close();
			in.close();
		} catch (IOException e) {
			EClaimLogger.error(e.getMessage());
			return null;
		} finally {

		}
		EClaimLogger.info("no error, prepared successfully");

		return lstPo;
	}

	// validate payment detail sheet and insert to the database
	@Transactional
	public PaymentDetailVo prepareBatchFromXls(PaymentBatchPo batchPo, String fn) throws Exception {

		PaymentDetailVo paymentDetailVo = new PaymentDetailVo();
		FileInputStream in = new FileInputStream(fn);
		List<PaymentDetailPo> lstPo = new ArrayList<PaymentDetailPo>();
		paymentDetailVo = PaymentXlsHelper.prepareBatchRecordsFromXls(batchPo, in);
		
		if(paymentDetailVo.getErrorMsg().size() == 0){
			// Delete all existing line records
			paymentBatchSvc.deleteDetail(batchPo);
			// Insert new records
			paymentDetailDao.insertAll(paymentDetailVo.getLstBatchRecords());	
		}

		return paymentDetailVo;
	}

	public void prepareXlsForOutStream(OutputStream out, PaymentBatchPo batchPo) throws Exception {

		Workbook wb = PaymentXlsHelper.generateXlsTemplate(batchPo);
		wb.write(out);
		wb.close();

		EClaimLogger.info("excel template published");
	}
	
	public void prepareXlsForOutStreamWithDetails(OutputStream out, PaymentBatchPo batchPo, List<PaymentBatchTo> tmpBatch) throws Exception {

		Workbook wb = PaymentXlsHelper.generateXlsTemplateWithDataWithDetails(batchPo, tmpBatch);
		wb.write(out);
		wb.close();

		EClaimLogger.info("excel template published");
	}

}
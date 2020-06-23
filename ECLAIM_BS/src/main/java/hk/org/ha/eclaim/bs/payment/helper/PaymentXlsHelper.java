package hk.org.ha.eclaim.bs.payment.helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang.SerializationUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import hk.org.ha.eclaim.bs.payment.constant.PaymentXlsConstant;
import hk.org.ha.eclaim.bs.payment.po.PaymentBatchPo;
import hk.org.ha.eclaim.bs.payment.po.PaymentBatchTo;
import hk.org.ha.eclaim.bs.payment.po.PaymentDetailPo;
import hk.org.ha.eclaim.bs.payment.po.PaymentDetailVo;
import hk.org.ha.eclaim.bs.payment.svc.IPaymentDetailSvc;
import hk.org.ha.eclaim.core.logger.EClaimLogger;

public class PaymentXlsHelper {
	
	@Autowired
	static
	IPaymentDetailSvc paymentDetailSvc;

	private static final String[] titles_1st = { "Assignment \nNo.", "Name", "Work \nLocation", "Job", "Hour \nType",
			"COA", "Reason", "Earned \nMonth", "Date / No. of hours worked", "Total" };

	private static final String[] titles_2nd = { "", "", "", "", "", "Inst", "Fund", "Section", "Analystic", "Type", "", "",
			"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
			"20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "" };

	private static Object[][] user_data = {};
//			{ "", "Mr A", "YK", "", "", "", "", "", "", "", "", "", "", 5.0, 8.0, 10.0, 5.0, 5.0, 7.0, 6.0 },
//			{ "", "Mr B", "GB", "", "", "", "", "", "", "", "", "", "", 4.0, 3.0, 1.0, 3.5, null, null, 4.0 }, };

	public static PaymentDetailVo prepareBatchRecordsFromXls(PaymentBatchPo batchPo, FileInputStream in) throws Exception {
		Workbook xswb = null;
		Row row = null;
		Cell cel = null;
		Calendar c1 = Calendar.getInstance();
		List<PaymentDetailPo> lstBatchRecords = new ArrayList<PaymentDetailPo>();
		PaymentDetailPo po = null;
		PaymentDetailPo poTemp = null;
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		String earnedYYYYMM = null;
		int yr = 0;
		int mnth = 0;
		int date = 0;
		int wrkHours = 0;
		
		String xlsYr = "";
		String xlsMth = "";
		String xlsDate = "";
		
		Integer line = 0;
		List<String> errorList = new ArrayList<String>();
		List<String> warningMsg = new ArrayList<String>();
		
		PaymentDetailVo paymentDetailVo = new PaymentDetailVo(); 
		
		try {
			xswb = new XSSFWorkbook(in);
			
			if (xswb.getSheetAt(0).getLastRowNum() < 6) {
				errorList.add("Empty Record in Excel file, please check");
				paymentDetailVo.setErrorMsg(errorList);
				return paymentDetailVo;
			}
			
			Iterator<Row> rowIt = xswb.getSheetAt(0).iterator();
			while (rowIt.hasNext()) {
				Boolean isValid = true;
				line = line + 1;
				row = rowIt.next();
				if (row.getRowNum() < 6)
					continue;

				// TODO @@Troy 20190124 Setup Details Po
				po = new PaymentDetailPo();
				po.setBatchId(batchPo.getBatchId());
				po.setEmpNo("Employee123");
				po.setStatus("U");

				int val = 0;
				String strVal = "";

				Iterator<Cell> celIt = row.cellIterator();
				while (celIt.hasNext()) {
					cel = celIt.next();
					if (cel.getColumnIndex() == PaymentXlsConstant.COLUMN_INDEX_ASSIGN_NO) {
						if (cel.getCellTypeEnum().equals(CellType.BLANK)) {
							errorList.add("Line " + (line + 1) + " Assignment number cannot be empty.");
							isValid = false;
						} else if (cel.getCellTypeEnum().equals(CellType.STRING)) {
							errorList.add("Line " + (line + 1) + " Assignment number should be numeric.");
							isValid = false;
						} else {
							strVal = Integer.toString((int) Math.round(cel.getNumericCellValue()));
							po.setAsgNo(strVal);
						}
					} else if (cel.getColumnIndex() == PaymentXlsConstant.COLUMN_INDEX_NAME) {
						if (cel.getCellTypeEnum().equals(CellType.BLANK)) {
							warningMsg.add("Line " + (line + 1) + " Name is Empty.");
						} else {
							// TODO compare with DB name
							strVal = cel.getStringCellValue();
						}
						
					} else if (cel.getColumnIndex() == PaymentXlsConstant.COLUMN_INDEX_WORK_LOCATION) {
						if (cel.getCellTypeEnum().equals(CellType.STRING)) {
							strVal = cel.getStringCellValue();
						} else if (cel.getCellTypeEnum().equals(CellType.NUMERIC)){
							strVal = String.valueOf(cel.getNumericCellValue());
						}
					} else if (cel.getColumnIndex() == PaymentXlsConstant.COLUMN_INDEX_JOB) {
						if (cel.getCellTypeEnum().equals(CellType.BLANK)) {
							errorList.add("Line " + (line + 1) + " Job cannot be empty.");
							isValid = false;
						} else if (1 != 1){
							// TODO compare with batch Job
						} else {
							if (cel.getCellTypeEnum().equals(CellType.STRING)) {
								strVal = cel.getStringCellValue();
							} else if (cel.getCellTypeEnum().equals(CellType.NUMERIC)){
								strVal = String.valueOf(cel.getNumericCellValue());
							}
							po.setJob(strVal);
						}						
					} else if (cel.getColumnIndex() == PaymentXlsConstant.COLUMN_INDEX_HOUR_TYPE) {
						//TODO
						strVal = cel.getStringCellValue();
						po.setHourType(strVal);
					} else if (cel.getColumnIndex() == PaymentXlsConstant.COLUMN_INDEX_COA_ANALYSTIC) {
						if (!cel.getCellTypeEnum().equals(CellType.BLANK)) {
							if (cel.getCellTypeEnum().equals(CellType.STRING)) {
								strVal = cel.getStringCellValue();
							} else if (cel.getCellTypeEnum().equals(CellType.NUMERIC)) {
								strVal = Integer.toString((int) Math.round(cel.getNumericCellValue()));
							}
							if (!StringUtils.isEmpty(strVal)) {
								if (strVal.length() > 3) {
									errorList.add("Line " + (line + 1) + " COA Inst " + strVal + " can't longer than 3");
									isValid = false;
								} else {
									po.setCoaAnalystic(strVal);
								}
							}
						}
					} else if (cel.getColumnIndex() == PaymentXlsConstant.COLUMN_INDEX_COA_FUND) {
						if (!cel.getCellTypeEnum().equals(CellType.BLANK)) {
							if (cel.getCellTypeEnum().equals(CellType.STRING)) {
								strVal = cel.getStringCellValue();
							} else if (cel.getCellTypeEnum().equals(CellType.NUMERIC)) {
								strVal = Integer.toString((int) Math.round(cel.getNumericCellValue()));
							}
							if (!StringUtils.isEmpty(strVal)) {
								if (strVal.length() > 2) {
									errorList.add("Line " + (line + 1) + " COA Fund " + strVal + " can't longer than 2");
									isValid = false;
								} else {
									po.setCoaFund(strVal);
								}
							}
						}
					} else if (cel.getColumnIndex() == PaymentXlsConstant.COLUMN_INDEX_COA_INST) {
						if (!cel.getCellTypeEnum().equals(CellType.BLANK)) {
							if (cel.getCellTypeEnum().equals(CellType.STRING)) {
								strVal = cel.getStringCellValue();
							} else if (cel.getCellTypeEnum().equals(CellType.NUMERIC)) {
								strVal = Integer.toString((int) Math.round(cel.getNumericCellValue()));
							}
							if (!StringUtils.isEmpty(strVal)) {
								if (strVal.length() > 7) {
									errorList.add("Line " + (line + 1) + " COA Inst " + strVal + " can't longer than 7");
									isValid = false;
								} else {
									po.setCoaInst(strVal);
								}
							}
						}
					} else if (cel.getColumnIndex() == PaymentXlsConstant.COLUMN_INDEX_COA_SECTION) {
						if (!cel.getCellTypeEnum().equals(CellType.BLANK)) {
							if (cel.getCellTypeEnum().equals(CellType.STRING)) {
								strVal = cel.getStringCellValue();
							} else if (cel.getCellTypeEnum().equals(CellType.NUMERIC)) {
								strVal = Integer.toString((int) Math.round(cel.getNumericCellValue()));
							}
							if (!StringUtils.isEmpty(strVal)) {
								if (strVal.length() > 5) {
									errorList.add("Line " + (line + 1) + " COA Section " + strVal + " can't longer than 5");
									isValid = false;
								} else {
									po.setCoaSection(strVal);
								}
							}
						}
					} else if (cel.getColumnIndex() == PaymentXlsConstant.COLUMN_INDEX_COA_TYPE) {
						if (!cel.getCellTypeEnum().equals(CellType.BLANK)) {
							if (cel.getCellTypeEnum().equals(CellType.STRING)) {
								strVal = cel.getStringCellValue();
							} else if (cel.getCellTypeEnum().equals(CellType.NUMERIC)) {
								strVal = Integer.toString((int) Math.round(cel.getNumericCellValue()));
							}
							if (!StringUtils.isEmpty(strVal)) {
								if (strVal.length() > 2) {
									errorList.add("Line " + (line + 1) + " COA Section " + strVal + " can't longer than 2");
									isValid = false;
								} else {
									po.setCoaType(strVal);
								}
							}
						}
					} else if (cel.getColumnIndex() == PaymentXlsConstant.COLUMN_INDEX_REASON) {
						if (cel.getCellTypeEnum().equals(CellType.BLANK)) {
							errorList.add("Line " + (line + 1) + " Reason cannot be empty.");
							isValid = false;
						} else {
							if (cel.getCellTypeEnum().equals(CellType.STRING)) {
								strVal = cel.getStringCellValue();
							}
							if (cel.getCellTypeEnum().equals(CellType.NUMERIC)) {
								strVal = String.valueOf(cel.getNumericCellValue());
							}
							po.setReason(strVal);
						}
					} else if (cel.getColumnIndex() == PaymentXlsConstant.COLUMN_INDEX_EARNED_MONTH) {
						if (cel.getCellTypeEnum().equals(CellType.BLANK)) {
							errorList.add("Line " + (line + 1) + " Earned Month cannot be empty.");
							isValid = false;
						} else {
							if (cel.getCellTypeEnum().equals(CellType.STRING)) {
								strVal = cel.getStringCellValue();
							} else if (cel.getCellTypeEnum().equals(CellType.NUMERIC)) {
								strVal = String.valueOf((int)cel.getNumericCellValue());
							}
							Boolean validEarnedMonth = true;
							try 
							{
							  Integer.parseInt(strVal);
							}
							catch(Exception e) {
								errorList.add("Line " + (line + 1) + " Earned Month " + strVal + " must be numeric");
								isValid = false;
								validEarnedMonth = false;
							}
							if (validEarnedMonth) {
								if (strVal.length() > 6 || strVal.length() < 4) {
									errorList.add("Line " + (line + 1) + " Earned Month " + strVal + " Invalid Format");
									isValid = false;
									validEarnedMonth = false;
								}
							}
							if (validEarnedMonth) {
								if (strVal.length() == 5) {
									mnth = Integer.parseInt(strVal.substring(0, 1));
									yr = Integer.parseInt(strVal.substring(1, 5));
									xlsYr = strVal.substring(1, 5);
									xlsMth = strVal.substring(0, 1);
								} else if (strVal.length() == 6) {
									mnth = Integer.parseInt(strVal.substring(0, 2));
									yr = Integer.parseInt(strVal.substring(2, 6));
									xlsYr = strVal.substring(2, 6);
									xlsMth = strVal.substring(0, 2);
								}
								if (mnth < 0 || mnth > 12) {
									errorList.add("Line " + (line + 1) + " Earned Month " + strVal + " must within 1 to 12");
									isValid = false;
									validEarnedMonth = false;
								}
							}
						}
					}

					// cells greater than 10 are belong to days
					else if (cel.getColumnIndex() > PaymentXlsConstant.COLUMN_INDEX_EARNED_MONTH
							&& cel.getColumnIndex() < PaymentXlsConstant.COLUMN_INDEX_TOTAL_HOURS) {
						if (!cel.getCellTypeEnum().equals(CellType.BLANK) && !cel.getCellTypeEnum().equals(CellType.NUMERIC)) {
							errorList.add("Line " + (line + 1) + " Work Hours Value must be numberic");
						} else {
							wrkHours = (int) Math.round(cel.getNumericCellValue());
							if (wrkHours < 0 || wrkHours > 24) {
								errorList.add("Line " + (line + 1) + " Work Hours " + wrkHours +" must within 1 to 12");
								isValid = false;
							} else {
								date = cel.getColumnIndex() - PaymentXlsConstant.COLUMN_INDEX_EARNED_MONTH;
								xlsDate = String.valueOf(date);
								if (xlsDate.length() < 2) {
									xlsDate = "0" + xlsDate;
								}
							
								//po.setDutyDate(c1.getTime());
								po.setDutyDate(simpleDateFormat.parse(xlsYr+xlsMth+xlsDate));
								wrkHours = (int) Math.round(cel.getNumericCellValue());
								po.setTotalHour(wrkHours);
								lstBatchRecords.add(po);

								// clone to insert same object have different work hour on different date.
								poTemp = (PaymentDetailPo) SerializationUtils.clone(po);
								po = poTemp;
							}
						}
					}
					EClaimLogger.info(cel.getCellTypeEnum().toString());
					EClaimLogger.info(cel.getAddress().toString());
					
					if(isValid == false) {
						break;
					}
				}

				if(isValid == false) {
					lstBatchRecords = new ArrayList<PaymentDetailPo>();
					break;
				}
			}

		} catch (Exception e) {
			EClaimLogger.error(e.getMessage());
			return null;
		} finally {
			xswb.close();
			in.close();
		}

		paymentDetailVo.setErrorMsg(errorList);
		paymentDetailVo.setLstBatchRecords(lstBatchRecords);

		return paymentDetailVo;
	}

	public static Workbook generateXlsTemplate(PaymentBatchPo batchPo) throws Exception {

		Workbook wb = new XSSFWorkbook();

		Sheet sheet = wb.createSheet("Timesheet");
		PrintSetup printSetup = sheet.getPrintSetup();
		printSetup.setLandscape(true);
		sheet.setFitToPage(true);
		sheet.setHorizontallyCenter(true);

		// title row
		Row titleRow = sheet.createRow(0);
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue("Project");
		sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$B$1"));

		Cell valueCell = titleRow.createCell(2);
		valueCell.setCellValue(batchPo.getProjectName());

		// Payment Type
		titleCell = titleRow.createCell(11);
		titleCell.setCellValue("Payment Type");
		sheet.addMergedRegion(CellRangeAddress.valueOf("$L$1:$M$1"));

		valueCell = titleRow.createCell(13);
		valueCell.setCellValue(batchPo.getPaymentType());

		// Department Type
		titleRow = sheet.createRow(1);
		titleCell = titleRow.createCell(0);
		titleCell.setCellValue("Department");
		sheet.addMergedRegion(CellRangeAddress.valueOf("$A$2:$B$2"));

		valueCell = titleRow.createCell(2);
		valueCell.setCellValue(batchPo.getDepartmentName());

		// Work Location
		titleCell = titleRow.createCell(11);
		titleCell.setCellValue("Work Location");
		sheet.addMergedRegion(CellRangeAddress.valueOf("$L$2:$M$2"));

		// Pay Month
		titleRow = sheet.createRow(2);
		titleCell = titleRow.createCell(0);
		titleCell.setCellValue("Pay Month");
		sheet.addMergedRegion(CellRangeAddress.valueOf("$A$3:$B$3"));

		valueCell = titleRow.createCell(2);
		Calendar cald = Calendar.getInstance();
		cald.setTime(batchPo.getPayMonth());
		String payMonth = (cald.get(Calendar.MONTH) + 1) + "/" + cald.get(Calendar.YEAR);
		valueCell.setCellValue(payMonth);

		Row headerRow = sheet.createRow(5);
		Cell headerCell;
		for (int i = 0; i < titles_2nd.length; i++) {
			headerCell = headerRow.createCell(i);
			headerCell.setCellValue(titles_2nd[i]);
		}

		// header row
		int _hRow = 4;
		headerRow = sheet.createRow(_hRow);
		int mergeCellIndexSt = 0;
		int mergeCellIndexEd = 0;
		int titleIndx = -1;
		for (int i = 0; i < titles_2nd.length; i++) {
			headerCell = headerRow.createCell(i);

			if (mergeCellIndexSt <= mergeCellIndexEd && StringUtils.hasLength(titles_2nd[i])) {
				mergeCellIndexSt = i;
			} else if (mergeCellIndexSt > mergeCellIndexEd && StringUtils.isEmpty(titles_2nd[i + 1])) {
				mergeCellIndexEd = i;
			} else {
				if (mergeCellIndexSt > mergeCellIndexEd) {
					continue;
				}
				mergeCellIndexSt = mergeCellIndexEd = i;
			}
			if (mergeCellIndexEd - mergeCellIndexSt > 0) {
				sheet.addMergedRegion(new CellRangeAddress(4, 4, mergeCellIndexSt, mergeCellIndexEd));
				headerCell = headerRow.getCell(mergeCellIndexSt);
				titleIndx = titleIndx + 1;
				headerCell.setCellValue(titles_1st[titleIndx]);
			} else if (mergeCellIndexSt > mergeCellIndexEd) {
				// skip this step to merge with merge index end.
			} else {
				titleIndx = titleIndx + 1;
				headerCell.setCellValue(titles_1st[titleIndx]);
				sheet.addMergedRegion(new CellRangeAddress(4, 5, mergeCellIndexSt, mergeCellIndexSt));
			}
		}

		final int rn = 6;
		int rownum = rn;
		for (int i = 0; i < user_data.length; i++) {
			Row row = sheet.createRow(rownum++);
			for (int j = 0; j < titles_2nd.length; j++) {
				Cell cell = row.createCell(j);
				if (j == titles_2nd.length - 1) {
					// cell contains sum over work hours, e.g. SUM(L6:U6)
					String ref = "L" + rownum + ":U" + rownum;
					cell.setCellFormula("SUM(" + ref + ")");

				} else {

				}
			}
		}

		sheet.setColumnWidth(0, 10 * 256);

		return wb;
	}

	
	public static Workbook generateXlsTemplateWithDataWithDetails(PaymentBatchPo batchPo, List<PaymentBatchTo> tmpBatch) throws Exception {

		Workbook wb = new XSSFWorkbook();

		Sheet sheet = wb.createSheet("Timesheet");
		PrintSetup printSetup = sheet.getPrintSetup();
		printSetup.setLandscape(true);
		sheet.setFitToPage(true);
		sheet.setHorizontallyCenter(true);

		// title row
		Row titleRow = sheet.createRow(0);
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue("Project");
		sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$B$1"));

		Cell valueCell = titleRow.createCell(2);
		valueCell.setCellValue(batchPo.getProjectName());

		// Payment Type
		titleCell = titleRow.createCell(11);
		titleCell.setCellValue("Payment Type");
		sheet.addMergedRegion(CellRangeAddress.valueOf("$L$1:$M$1"));

		valueCell = titleRow.createCell(13);
		valueCell.setCellValue(batchPo.getPaymentType());

		// Department Type
		titleRow = sheet.createRow(1);
		titleCell = titleRow.createCell(0);
		titleCell.setCellValue("Department");
		sheet.addMergedRegion(CellRangeAddress.valueOf("$A$2:$B$2"));

		valueCell = titleRow.createCell(2);
		valueCell.setCellValue(batchPo.getDepartmentName());

		// Work Location
		titleCell = titleRow.createCell(11);
		titleCell.setCellValue("Work Location");
		sheet.addMergedRegion(CellRangeAddress.valueOf("$L$2:$M$2"));

		// Pay Month
		titleRow = sheet.createRow(2);
		titleCell = titleRow.createCell(0);
		titleCell.setCellValue("Pay Month");
		sheet.addMergedRegion(CellRangeAddress.valueOf("$A$3:$B$3"));

		valueCell = titleRow.createCell(2);
		Calendar cald = Calendar.getInstance();
		cald.setTime(batchPo.getPayMonth());
		String payMonth = (cald.get(Calendar.MONTH) + 1) + "/" + cald.get(Calendar.YEAR);
		valueCell.setCellValue(payMonth);

		Row headerRow = sheet.createRow(5);
		Cell headerCell;
		for (int i = 0; i < titles_2nd.length; i++) {
			headerCell = headerRow.createCell(i);
			headerCell.setCellValue(titles_2nd[i]);
		}

		// header row
		int _hRow = 4;
		headerRow = sheet.createRow(_hRow);
		int mergeCellIndexSt = 0;
		int mergeCellIndexEd = 0;
		int titleIndx = -1;
		for (int i = 0; i < titles_2nd.length; i++) {
			headerCell = headerRow.createCell(i);

			if (mergeCellIndexSt <= mergeCellIndexEd && StringUtils.hasLength(titles_2nd[i])) {
				mergeCellIndexSt = i;
			} else if (mergeCellIndexSt > mergeCellIndexEd && StringUtils.isEmpty(titles_2nd[i + 1])) {
				mergeCellIndexEd = i;
			} else {
				if (mergeCellIndexSt > mergeCellIndexEd) {
					continue;
				}
				mergeCellIndexSt = mergeCellIndexEd = i;
			}
			if (mergeCellIndexEd - mergeCellIndexSt > 0) {
				sheet.addMergedRegion(new CellRangeAddress(4, 4, mergeCellIndexSt, mergeCellIndexEd));
				headerCell = headerRow.getCell(mergeCellIndexSt);
				titleIndx = titleIndx + 1;
				headerCell.setCellValue(titles_1st[titleIndx]);
			} else if (mergeCellIndexSt > mergeCellIndexEd) {
				// skip this step to merge with merge index end.
			} else {
				titleIndx = titleIndx + 1;
				headerCell.setCellValue(titles_1st[titleIndx]);
				sheet.addMergedRegion(new CellRangeAddress(4, 5, mergeCellIndexSt, mergeCellIndexSt));
			}
		}

		
		Integer rownum = 6;
		if (tmpBatch != null && tmpBatch.size() > 0) {	
			for (PaymentBatchTo temp : tmpBatch) {
				// Create Row
				Row detailRow = sheet.createRow(rownum);
				// Set Details
				// 1. Asg No
				Cell detailCell = detailRow.createCell(0);
				detailCell.setCellValue(temp.getAsgNo());
				// 2. Name
				detailCell = detailRow.createCell(1);
				detailCell.setCellValue(temp.getEmpName());
				// 3. Work Location
				detailCell = detailRow.createCell(2);
				detailCell.setCellValue(temp.getWorkLocation());
				// 4. Job
				detailCell = detailRow.createCell(3);
				detailCell.setCellValue(temp.getJob());
				// 5. Hour Type
				detailCell = detailRow.createCell(4);
				detailCell.setCellValue(temp.getHourType());
				// 6. COA INST
				detailCell = detailRow.createCell(5);
				detailCell.setCellValue(temp.getCoaInst());
				// 7. COA FUND
				detailCell = detailRow.createCell(6);
				detailCell.setCellValue(temp.getCoaFund());
				// 8. COA Section
				detailCell = detailRow.createCell(7);
				detailCell.setCellValue(temp.getCoaSection());
				// 9. COA Analysis
				detailCell = detailRow.createCell(8);
				detailCell.setCellValue(temp.getCoaAnalytic());
				// 10. COA Type
				detailCell = detailRow.createCell(9);
				detailCell.setCellValue(temp.getCoaType());
				// 11. Reason
				detailCell = detailRow.createCell(10);
				detailCell.setCellValue(temp.getReason()); 
				// 12. Earned Month
				detailCell = detailRow.createCell(11);
				detailCell.setCellValue(temp.getEarnedMonth());
				// 13. Work Hour
				for (Entry<Integer, Integer> key : temp.getDutyDate().entrySet()) {
					detailCell = detailRow.createCell(key.getKey() + 11);
					detailCell.setCellValue(key.getValue());
				}
				rownum = rownum + 1;
			}

		}


		sheet.setColumnWidth(0, 10 * 256);

		return wb;
	}
}

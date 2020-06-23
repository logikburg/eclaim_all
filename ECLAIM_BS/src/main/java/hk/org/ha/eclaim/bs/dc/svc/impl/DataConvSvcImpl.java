package hk.org.ha.eclaim.bs.dc.svc.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
//import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import hk.org.ha.eclaim.bs.dc.dao.IDataConvDao;
import hk.org.ha.eclaim.bs.dc.dao.IDcPostDataSourceDao;
import hk.org.ha.eclaim.bs.dc.po.DcPostDataSourcePo;
import hk.org.ha.eclaim.bs.dc.svc.IDataConvSvc;
import hk.org.ha.eclaim.core.logger.EClaimLogger;

@Service("dcSvc")
public class DataConvSvcImpl implements IDataConvSvc {

	// DC Template worksheet name
	private final static String DC_TEMPLATE_SHEETNAME = "MPR DC";

	// Common record fields
	private Date DATE_DATACONV;

	@Autowired(required = true)
	private IDataConvDao dataConvDao;

	@Autowired(required = true)
	private IDcPostDataSourceDao dcPostDataSourceDao;

	@Transactional
	public void initDataSource() throws Exception {
		// init tables and sequence for DC
		dataConvDao.initDataSource();
	}
	
	@Transactional
	public void proceedDataConv() throws Exception {
		dataConvDao.proceedDataConv();
	}

	@Transactional
	public void importDcTemplateFile(File dcPostDataFile) throws Exception {
		FileInputStream fis = null;
		XSSFWorkbook dcPostDataWorkbook = null;
		
		DATE_DATACONV = new Date();

		// read from DC template
		try {
			EClaimLogger.info("DC Template - " + dcPostDataFile);
			fis = new FileInputStream(dcPostDataFile);

			// Finds the workbook instance for XLSX file
			dcPostDataWorkbook = new XSSFWorkbook(fis);
			// Return worksheet from the XLSX workbook
			XSSFSheet dcPostDataSheet = dcPostDataWorkbook.getSheet(DC_TEMPLATE_SHEETNAME);

			// Import MPR DC Worksheet
			importDcWorksheet(dcPostDataFile, dcPostDataSheet);

		} catch (Exception e) {
			EClaimLogger.error("importDcTemplateFile:" + e.getMessage(), e);
		} finally {
			try {
				dcPostDataWorkbook.close();
				fis.close();
			} catch (IOException e) {
				EClaimLogger.error("importDcTemplateFile IOException:" + e.getMessage(), e);
			}

			// force JVM to release memory
			dcPostDataWorkbook = null;
			fis = null;
		}
	}

	private void importDcWorksheet(File dcPostDataFile, Sheet dcPostDataSheet) throws Exception {
		// Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = dcPostDataSheet.iterator();

		// Traversing over each row of XLSX file
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			// Skip heading rows
			if (row.getRowNum() < 5) {
				continue;
			}

			// read post records from DC template
			EClaimLogger.info(dcPostDataFile + ", " + row.getRowNum());
			// importPost(row);
			importPostDataSource(dcPostDataFile.getParentFile().getName(), row);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void importPostDataSource(String staffGroupCode, Row row) throws Exception {

		// Post Data
		String sourcePostId = getStringCellValue(row.getCell(0));
		String hcmPositionName = getStringCellValue(row.getCell(1));
		String clusterCode = getStringCellValue(row.getCell(2));
		String instCode = getStringCellValue(row.getCell(3));
		String deptName = getStringCellValue(row.getCell(4));
		String subSpecialtyDesc = getStringCellValue(row.getCell(5));
		String unit = getStringCellValue(row.getCell(6));
		String rankCode = getStringCellValue(row.getCell(7));
		String postTitle = getStringCellValue(row.getCell(8));
		Date approvalDate = getDateCellValue(row.getCell(9));
		String approvalRef = getStringCellValue(row.getCell(10));
		String approvalRemark = getStringCellValue(row.getCell(11));
		String postDurationDesc = getStringCellValue(row.getCell(12));
		Date postStartDate = getDateCellValue(row.getCell(13));
		String limitDurationNo = getStringCellValue(row.getCell(14));
		String limitDurationUnitDesc = getStringCellValue(row.getCell(15));
		Date limitDurationEndDate = getDateCellValue(row.getCell(16));
		String employeeNumber = getStringCellValue(row.getCell(17));
		String employeeName = getStringCellValue(row.getCell(18));
		String employeeRank = getStringCellValue(row.getCell(19));
		Double postFTE = getNumericCellValue(row.getCell(20));
		Double strengthFTE = getNumericCellValue(row.getCell(21));
		Double vacancyFTE = getNumericCellValue(row.getCell(22));
		String clusterRef = getStringCellValue(row.getCell(23));
		String clusterRemark = getStringCellValue(row.getCell(24));
		String postRemark = getStringCellValue(row.getCell(25));
		String postStatusDesc = getStringCellValue(row.getCell(26));
		Date postStatusStartDate = getDateCellValue(row.getCell(27));
		Date postStatusEndDate = getDateCellValue(row.getCell(28));
		String heldAgainstInd = getStringCellValue(row.getCell(29));
		String heldAgainstPostId = getStringCellValue(row.getCell(30));
		String upgradeTLInd = getStringCellValue(row.getCell(31));
		String juniorPostId = getStringCellValue(row.getCell(32));
		String hoBuyServiceInd = getStringCellValue(row.getCell(33));

		// Funding Source 1
		String fundSrc1stAnnualPlanInd = getStringCellValue(row.getCell(34));
		String fundSrc1stProgramYear = getStringCellValue(row.getCell(35));
		String fundSrc1stProgramCode = getStringCellValue(row.getCell(36));
		String fundSrc1stProgramName = getStringCellValue(row.getCell(37));
		String fundSrc1stProgramType = getStringCellValue(row.getCell(38));
		String fundSrc1stDesc = getStringCellValue(row.getCell(39));
		String fundSrc1stSubCatDesc = getStringCellValue(row.getCell(40));
		Date fundSrc1stStartDate = getDateCellValue(row.getCell(41));
		Date fundSrc1stEndDate = getDateCellValue(row.getCell(42));
		Double fundSrc1stFte = getNumericCellValue(row.getCell(43));
		String fundSrc1stInst = getStringCellValue(row.getCell(44));
		String fundSrc1stSection = getStringCellValue(row.getCell(45));
		String fundSrc1stAnalytical = getStringCellValue(row.getCell(46));
		String fundSrc1stRemark = getStringCellValue(row.getCell(47));
		// Funding Source 2
		String fundSrc2ndAnnualPlanInd = getStringCellValue(row.getCell(48));
		String fundSrc2ndProgramYear = getStringCellValue(row.getCell(49));
		String fundSrc2ndProgramCode = getStringCellValue(row.getCell(50));
		String fundSrc2ndProgramName = getStringCellValue(row.getCell(51));
		String fundSrc2ndProgramType = getStringCellValue(row.getCell(52));
		String fundSrc2ndDesc = getStringCellValue(row.getCell(53));
		String fundSrc2ndSubCatDesc = getStringCellValue(row.getCell(54));
		Date fundSrc2ndStartDate = getDateCellValue(row.getCell(55));
		Date fundSrc2ndEndDate = getDateCellValue(row.getCell(56));
		Double fundSrc2ndFte = getNumericCellValue(row.getCell(57));
		String fundSrc2ndInst = getStringCellValue(row.getCell(58));
		String fundSrc2ndSection = getStringCellValue(row.getCell(59));
		String fundSrc2ndAnalytical = getStringCellValue(row.getCell(60));
		String fundSrc2ndRemark = getStringCellValue(row.getCell(61));
		// Funding Source 3
		String fundSrc3rdAnnualPlanInd = getStringCellValue(row.getCell(62));
		String fundSrc3rdProgramYear = getStringCellValue(row.getCell(63));
		String fundSrc3rdProgramCode = getStringCellValue(row.getCell(64));
		String fundSrc3rdProgramName = getStringCellValue(row.getCell(65));
		String fundSrc3rdProgramType = getStringCellValue(row.getCell(66));
		String fundSrc3rdDesc = getStringCellValue(row.getCell(67));
		String fundSrc3rdSubCatDesc = getStringCellValue(row.getCell(68));
		Date fundSrc3rdStartDate = getDateCellValue(row.getCell(69));
		Date fundSrc3rdEndDate = getDateCellValue(row.getCell(70));
		Double fundSrc3rdFte = getNumericCellValue(row.getCell(71));
		String fundSrc3rdInst = getStringCellValue(row.getCell(72));
		String fundSrc3rdSection = getStringCellValue(row.getCell(73));
		String fundSrc3rdAnalytical = getStringCellValue(row.getCell(74));
		String fundSrc3rdRemark = getStringCellValue(row.getCell(75));
		
		String resourcesSupportFrExt = getStringCellValue(row.getCell(76));
		String resourcesSupportRemark = getStringCellValue(row.getCell(77));
		
		// Escape rows of all blank fields
		StringBuilder str = new StringBuilder();
		str.append(Objects.toString(sourcePostId, ""))
		   .append(Objects.toString(hcmPositionName, ""))
		   .append(Objects.toString(clusterCode, ""))
		   .append(Objects.toString(instCode, ""))
		   .append(Objects.toString(deptName, ""))
		   .append(Objects.toString(subSpecialtyDesc, ""))
		   .append(Objects.toString(unit, ""))
		   .append(Objects.toString(rankCode, ""))
		   .append(Objects.toString(postTitle, ""))
		   .append(Objects.toString(approvalDate, ""))
		   .append(Objects.toString(approvalRef, ""))
		   .append(Objects.toString(approvalRemark, ""))
		   .append(Objects.toString(postDurationDesc, ""))
		   .append(Objects.toString(postStartDate, ""))
		   .append(Objects.toString(limitDurationNo, ""))
		   .append(Objects.toString(limitDurationUnitDesc, ""))
		   .append(Objects.toString(limitDurationEndDate, ""))
		   .append(Objects.toString(employeeNumber, ""))
		   .append(Objects.toString(employeeName, ""))
		   .append(Objects.toString(employeeRank, ""))
		   .append(Objects.toString(postFTE, ""))
		   .append(Objects.toString(strengthFTE, ""))
		   .append(Objects.toString(vacancyFTE, ""))
		   .append(Objects.toString(clusterRef, ""))
		   .append(Objects.toString(clusterRemark, ""))
		   .append(Objects.toString(postRemark, ""))
		   .append(Objects.toString(postStatusDesc, ""))
		   .append(Objects.toString(postStatusStartDate, ""))
		   .append(Objects.toString(postStatusEndDate, ""))
		   .append(Objects.toString(heldAgainstInd, ""))
		   .append(Objects.toString(heldAgainstPostId, ""))
		   .append(Objects.toString(upgradeTLInd, ""))
		   .append(Objects.toString(juniorPostId, ""))
		   .append(Objects.toString(hoBuyServiceInd, ""))
		   .append(Objects.toString(fundSrc1stAnnualPlanInd, ""))
		   .append(Objects.toString(fundSrc1stProgramYear, ""))
		   .append(Objects.toString(fundSrc1stProgramCode, ""))
		   .append(Objects.toString(fundSrc1stProgramName, ""))
		   .append(Objects.toString(fundSrc1stProgramType, ""))
		   .append(Objects.toString(fundSrc1stDesc, ""))
		   .append(Objects.toString(fundSrc1stSubCatDesc, ""))
		   .append(Objects.toString(fundSrc1stStartDate, ""))
		   .append(Objects.toString(fundSrc1stEndDate, ""))
		   .append(Objects.toString(fundSrc1stFte, ""))
		   .append(Objects.toString(fundSrc1stInst, ""))
		   .append(Objects.toString(fundSrc1stSection, ""))
		   .append(Objects.toString(fundSrc1stAnalytical, ""))
		   .append(Objects.toString(fundSrc1stRemark, ""))
		   .append(Objects.toString(fundSrc2ndAnnualPlanInd, ""))
		   .append(Objects.toString(fundSrc2ndProgramYear, ""))
		   .append(Objects.toString(fundSrc2ndProgramCode, ""))
		   .append(Objects.toString(fundSrc2ndProgramName, ""))
		   .append(Objects.toString(fundSrc2ndProgramType, ""))
		   .append(Objects.toString(fundSrc2ndDesc, ""))
		   .append(Objects.toString(fundSrc2ndSubCatDesc, ""))
		   .append(Objects.toString(fundSrc2ndStartDate, ""))
		   .append(Objects.toString(fundSrc2ndEndDate, ""))
		   .append(Objects.toString(fundSrc2ndFte, ""))
		   .append(Objects.toString(fundSrc2ndInst, ""))
		   .append(Objects.toString(fundSrc2ndSection, ""))
		   .append(Objects.toString(fundSrc2ndAnalytical, ""))
		   .append(Objects.toString(fundSrc2ndRemark, ""))
		   .append(Objects.toString(fundSrc3rdAnnualPlanInd, ""))
		   .append(Objects.toString(fundSrc3rdProgramYear, ""))
		   .append(Objects.toString(fundSrc3rdProgramCode, ""))
		   .append(Objects.toString(fundSrc3rdProgramName, ""))
		   .append(Objects.toString(fundSrc3rdProgramType, ""))
		   .append(Objects.toString(fundSrc3rdDesc, ""))
		   .append(Objects.toString(fundSrc3rdSubCatDesc, ""))
		   .append(Objects.toString(fundSrc3rdStartDate, ""))
		   .append(Objects.toString(fundSrc3rdEndDate, ""))
		   .append(Objects.toString(fundSrc3rdFte, ""))
		   .append(Objects.toString(fundSrc3rdInst, ""))
		   .append(Objects.toString(fundSrc3rdSection, ""))
		   .append(Objects.toString(fundSrc3rdAnalytical, ""))
		   .append(Objects.toString(fundSrc3rdRemark, ""))
		   .append(Objects.toString(resourcesSupportFrExt, ""))
		   .append(Objects.toString(resourcesSupportRemark, ""))
		   ;
		
		if (str.toString().trim().length() == 0) {
			System.out.println("Skip empty row");
			return;
		}
		
		// Pre-cal number of Funding Source
		Integer fundSrcCnt = new Integer(1);
			
		str = new StringBuilder();
		str.append(Objects.toString(fundSrc2ndAnnualPlanInd, ""))
		   .append(Objects.toString(fundSrc2ndProgramYear, ""))
		   .append(Objects.toString(fundSrc2ndProgramCode, ""))
		   .append(Objects.toString(fundSrc2ndProgramName, ""))
		   .append(Objects.toString(fundSrc2ndProgramType, ""))
		   .append(Objects.toString(fundSrc2ndDesc, ""))
		   .append(Objects.toString(fundSrc2ndSubCatDesc, ""))
		   .append(Objects.toString(fundSrc2ndStartDate, ""))
		   .append(Objects.toString(fundSrc2ndEndDate, ""))
		   .append(Objects.toString(fundSrc2ndFte, ""))
		   .append(Objects.toString(fundSrc2ndInst, ""))
		   .append(Objects.toString(fundSrc2ndSection, ""))
		   .append(Objects.toString(fundSrc2ndAnalytical, ""))
		   .append(Objects.toString(fundSrc2ndRemark, ""));
			
		if (str.toString().trim().length() > 0) {
			fundSrcCnt++;
		}
		
		str = new StringBuilder();
		str.append(Objects.toString(fundSrc3rdAnnualPlanInd, ""))
		   .append(Objects.toString(fundSrc3rdProgramYear, ""))
		   .append(Objects.toString(fundSrc3rdProgramCode, ""))
		   .append(Objects.toString(fundSrc3rdProgramName, ""))
		   .append(Objects.toString(fundSrc3rdProgramType, ""))
		   .append(Objects.toString(fundSrc3rdDesc, ""))
		   .append(Objects.toString(fundSrc3rdSubCatDesc, ""))
		   .append(Objects.toString(fundSrc3rdStartDate, ""))
		   .append(Objects.toString(fundSrc3rdEndDate, ""))
		   .append(Objects.toString(fundSrc3rdFte, ""))
		   .append(Objects.toString(fundSrc3rdInst, ""))
		   .append(Objects.toString(fundSrc3rdSection, ""))
		   .append(Objects.toString(fundSrc3rdAnalytical, ""))
		   .append(Objects.toString(fundSrc3rdRemark, ""));
			
		if (str.toString().trim().length() > 0) {
			fundSrcCnt++;
		}

		// Convert to code value
		int rowNum = row.getRowNum();
		String postDurationCode = getPostDurationCode(postDurationDesc, rowNum);
		String limitDurationUnit = getDurationPeriodUnitCode(limitDurationUnitDesc, rowNum);
		
		// Store Post Data Source to DB
		DcPostDataSourcePo dcPostDataSourcePo = new DcPostDataSourcePo();
		dcPostDataSourcePo.setDcDate(DATE_DATACONV);
		dcPostDataSourcePo.setSourcePostId(sourcePostId);
		dcPostDataSourcePo.setHcmPositionName(hcmPositionName);
		dcPostDataSourcePo.setClusterCode(clusterCode);
		dcPostDataSourcePo.setInstCode(instCode);
		dcPostDataSourcePo.setDeptName(deptName);
		dcPostDataSourcePo.setSubSpecialtyDesc(subSpecialtyDesc);
		dcPostDataSourcePo.setUnit(unit);
		dcPostDataSourcePo.setRankCode(rankCode);
		dcPostDataSourcePo.setPostTitle(postTitle);
		dcPostDataSourcePo.setApprovalDate(approvalDate);
		dcPostDataSourcePo.setApprovalRef(approvalRef);
		dcPostDataSourcePo.setApprovalRemark(approvalRemark);
		dcPostDataSourcePo.setPostDurationDesc(postDurationDesc);
		dcPostDataSourcePo.setPostStartDate(postStartDate);
		dcPostDataSourcePo.setLimitDurationNo(limitDurationNo);
		dcPostDataSourcePo.setLimitDurationUnitDesc(limitDurationUnitDesc);
		dcPostDataSourcePo.setLimitDurationEndDate(limitDurationEndDate);
		dcPostDataSourcePo.setEmployeeNumber(employeeNumber);
		dcPostDataSourcePo.setEmployeeName(employeeName);
		dcPostDataSourcePo.setEmployeeRank(employeeRank);
		dcPostDataSourcePo.setPostFTE(postFTE);
		dcPostDataSourcePo.setStrengthFTE(strengthFTE);
		dcPostDataSourcePo.setVacancyFTE(vacancyFTE);
		dcPostDataSourcePo.setClusterRef(clusterRef);
		dcPostDataSourcePo.setClusterRemark(clusterRemark);
		dcPostDataSourcePo.setPostRemark(postRemark);
		dcPostDataSourcePo.setPostStatusDesc(postStatusDesc);
		dcPostDataSourcePo.setPostStatusStartDate(postStatusStartDate);
		dcPostDataSourcePo.setPostStatusEndDate(postStatusEndDate);
		dcPostDataSourcePo.setHeldAgainstInd(heldAgainstInd);
		dcPostDataSourcePo.setHeldAgainstPostId(heldAgainstPostId);
		dcPostDataSourcePo.setUpgradeTLInd(upgradeTLInd);
		dcPostDataSourcePo.setJuniorPostId(juniorPostId);
		dcPostDataSourcePo.setHoBuyServiceInd(hoBuyServiceInd);
		dcPostDataSourcePo.setResourcesSupportFrExtDesc(resourcesSupportFrExt);
		dcPostDataSourcePo.setResourcesSupportRemark(resourcesSupportRemark);
		// Funding Source 1
		dcPostDataSourcePo.setFundSrc1stAnnualPlanInd(fundSrc1stAnnualPlanInd);
		dcPostDataSourcePo.setFundSrc1stProgramYear(fundSrc1stProgramYear);
		dcPostDataSourcePo.setFundSrc1stProgramCode(fundSrc1stProgramCode);
		dcPostDataSourcePo.setFundSrc1stProgramName(fundSrc1stProgramName);
		dcPostDataSourcePo.setFundSrc1stProgramType(fundSrc1stProgramType);
		dcPostDataSourcePo.setFundSrc1stDesc(fundSrc1stDesc);
		dcPostDataSourcePo.setFundSrc1stSubCatDesc(fundSrc1stSubCatDesc);
		dcPostDataSourcePo.setFundSrc1stStartDate(fundSrc1stStartDate);
		dcPostDataSourcePo.setFundSrc1stEndDate(fundSrc1stEndDate);
		dcPostDataSourcePo.setFundSrc1stFte(fundSrc1stFte);
		dcPostDataSourcePo.setFundSrc1stInst(fundSrc1stInst);
		dcPostDataSourcePo.setFundSrc1stSection(fundSrc1stSection);
		dcPostDataSourcePo.setFundSrc1stAnalytical(fundSrc1stAnalytical);
		dcPostDataSourcePo.setFundSrc1stRemark(fundSrc1stRemark);
		// Funding Source 2
		dcPostDataSourcePo.setFundSrc2ndAnnualPlanInd(fundSrc2ndAnnualPlanInd);
		dcPostDataSourcePo.setFundSrc2ndProgramYear(fundSrc2ndProgramYear);
		dcPostDataSourcePo.setFundSrc2ndProgramCode(fundSrc2ndProgramCode);
		dcPostDataSourcePo.setFundSrc2ndProgramName(fundSrc2ndProgramName);
		dcPostDataSourcePo.setFundSrc2ndProgramType(fundSrc2ndProgramType);
		dcPostDataSourcePo.setFundSrc2ndDesc(fundSrc2ndDesc);
		dcPostDataSourcePo.setFundSrc2ndSubCatDesc(fundSrc2ndSubCatDesc);
		dcPostDataSourcePo.setFundSrc2ndStartDate(fundSrc2ndStartDate);
		dcPostDataSourcePo.setFundSrc2ndEndDate(fundSrc2ndEndDate);
		dcPostDataSourcePo.setFundSrc2ndFte(fundSrc2ndFte);
		dcPostDataSourcePo.setFundSrc2ndInst(fundSrc2ndInst);
		dcPostDataSourcePo.setFundSrc2ndSection(fundSrc2ndSection);
		dcPostDataSourcePo.setFundSrc2ndAnalytical(fundSrc2ndAnalytical);
		dcPostDataSourcePo.setFundSrc2ndRemark(fundSrc2ndRemark);
		// Funding Source 3
		dcPostDataSourcePo.setFundSrc3rdAnnualPlanInd(fundSrc3rdAnnualPlanInd);
		dcPostDataSourcePo.setFundSrc3rdProgramYear(fundSrc3rdProgramYear);
		dcPostDataSourcePo.setFundSrc3rdProgramCode(fundSrc3rdProgramCode);
		dcPostDataSourcePo.setFundSrc3rdProgramName(fundSrc3rdProgramName);
		dcPostDataSourcePo.setFundSrc3rdProgramType(fundSrc3rdProgramType);
		dcPostDataSourcePo.setFundSrc3rdDesc(fundSrc3rdDesc);
		dcPostDataSourcePo.setFundSrc3rdSubCatDesc(fundSrc3rdSubCatDesc);
		dcPostDataSourcePo.setFundSrc3rdStartDate(fundSrc3rdStartDate);
		dcPostDataSourcePo.setFundSrc3rdEndDate(fundSrc3rdEndDate);
		dcPostDataSourcePo.setFundSrc3rdFte(fundSrc3rdFte);
		dcPostDataSourcePo.setFundSrc3rdInst(fundSrc3rdInst);
		dcPostDataSourcePo.setFundSrc3rdSection(fundSrc3rdSection);
		dcPostDataSourcePo.setFundSrc3rdAnalytical(fundSrc3rdAnalytical);
		dcPostDataSourcePo.setFundSrc3rdRemark(fundSrc3rdRemark);
		
		dcPostDataSourcePo.setStaffGroupCode(staffGroupCode);
		dcPostDataSourcePo.setPostDurationCode(postDurationCode);
		dcPostDataSourcePo.setLimitDurationUnit(limitDurationUnit);
		dcPostDataSourcePo.setFundSrcCnt(fundSrcCnt);
		dcPostDataSourceDao.insert(dcPostDataSourcePo);
	}

	private String getPostDurationCode(String postDurationDesc, int rowNum) {
		if (postDurationDesc != null) {

			postDurationDesc = postDurationDesc.toUpperCase();
			if ("RECURRENT".equals(postDurationDesc)) {
				return "R";
			} else if ("TIME LIMITED - CONTRACT".equals(postDurationDesc)) {
				return "TLC";
			} else if ("TIME LIMITED - TEMPORARY".equals(postDurationDesc)) {
				return "TLT";
			} else {
				return null;
			}
		}
		return null;
	}

	private String getDurationPeriodUnitCode(String durationPeriodUnitDesc, int rowNum) throws Exception {
		if (durationPeriodUnitDesc != null) {

			durationPeriodUnitDesc = durationPeriodUnitDesc.toUpperCase();
			if ("MONTH".equals(durationPeriodUnitDesc)) {
				return "M";
			} else if ("YEAR".equals(durationPeriodUnitDesc)) {
				return "Y";
			} else {
				return null;
			}
		}
		return null;
	}

	private String getStringCellValue(Cell cell) {
		String str = null;

		if (cell != null) {
			if (cell.getCellTypeEnum() == CellType.STRING || cell.getCellTypeEnum() == CellType.FORMULA) {
				str = cell.getStringCellValue();
				if (str.isEmpty()) {
					str = null;
				}
			} else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
				str = String.format("%.0f", cell.getNumericCellValue());
				if (str.isEmpty()) {
					str = null;
				}
			}
		}

		return str;
	}

	private Double getNumericCellValue(Cell cell) throws Exception {
		Double dbl = null;

		if (cell != null) {
			if (cell.getCellTypeEnum() == CellType.NUMERIC || cell.getCellTypeEnum() == CellType.FORMULA) {
				dbl = cell.getNumericCellValue();
				
				if (dbl.toString().length() <= 4) {
					return dbl;
				} else {
					System.out.println(dbl.toString());
					
					BigDecimal bd = new BigDecimal(dbl);
				    bd = bd.setScale(2, RoundingMode.HALF_UP);
				    return bd.doubleValue();
				}
			}
		}

		return dbl;
	}

	private Date getDateCellValue(Cell cell) throws Exception {
		Date date = null;

		if (cell != null) {
			if (cell.getCellTypeEnum() == CellType.NUMERIC || cell.getCellTypeEnum() == CellType.FORMULA) {
				date = cell.getDateCellValue();
			}
		}

		return date;
	}
}

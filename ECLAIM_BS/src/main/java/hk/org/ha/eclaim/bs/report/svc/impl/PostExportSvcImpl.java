package hk.org.ha.eclaim.bs.report.svc.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.idyl.winzipaes.AesZipFileEncrypter;
import de.idyl.winzipaes.impl.AESEncrypter;
import de.idyl.winzipaes.impl.AESEncrypterBC;
import hk.org.ha.eclaim.bs.report.dao.IPostExportDao;
import hk.org.ha.eclaim.bs.report.svc.IPostExportSvc;
import hk.org.ha.eclaim.bs.request.po.MPRSPostFundingSnapPo;
import hk.org.ha.eclaim.bs.cs.dao.IFundingSourceDao;
import hk.org.ha.eclaim.bs.cs.dao.IFundingSourceSubCatDao;
import hk.org.ha.eclaim.bs.cs.dao.IProgramTypeDao;
import hk.org.ha.eclaim.bs.cs.po.FundingSourcePo;
import hk.org.ha.eclaim.bs.cs.po.FundingSourceSubCatPo;
import hk.org.ha.eclaim.bs.cs.po.ProgramTypePo;
import hk.org.ha.eclaim.bs.report.po.PostExportPo;
import hk.org.ha.eclaim.bs.report.po.Report1Po;
import hk.org.ha.eclaim.bs.report.po.Report2Po;
import hk.org.ha.eclaim.bs.report.po.Report3Po;
import hk.org.ha.eclaim.bs.report.po.Report4Po;
import hk.org.ha.eclaim.bs.report.po.Report5ClusterPo;
import hk.org.ha.eclaim.bs.report.po.Report5Po;
import hk.org.ha.eclaim.bs.report.po.Report5RankClusterPo;
import hk.org.ha.eclaim.bs.report.po.Report5RankPo;
import hk.org.ha.eclaim.bs.report.po.Report6Po;
import hk.org.ha.eclaim.bs.report.po.Report7Po;
import hk.org.ha.eclaim.bs.report.po.ReportHeldAgainstListPo;
import hk.org.ha.eclaim.bs.security.po.DataAccessPo;
import hk.org.ha.eclaim.core.helper.DateTimeHelper;

@Service("postExportSvc")
public class PostExportSvcImpl implements IPostExportSvc {
	
	private static final String YES_IND = "Y";
	private static final String POST_RANK_AC_SUPP_PROMO = "AC (Supplementary Promotion)";

	@Autowired(required = true)
	private IPostExportDao postExportDao;
	
	@Autowired
	private IFundingSourceDao fundingSourceDao;
	
	@Autowired
	private IFundingSourceSubCatDao fundingSourceSubCatDao;
	
	@Autowired
	private IProgramTypeDao programTypeDao;
	

	public byte[] exportPost(List<DataAccessPo> dataAccessList, Date asAtDate, String clusterCode, String instCode,
			String deptCode, String staffGroupName, String rankName, String password) throws Exception {
		// prepare sql condition
		Map<String, String> condCriteria = new HashMap<String, String>();
		
		// setup filename
		StringBuilder filename = new StringBuilder();
		if (clusterCode != null && !clusterCode.isEmpty()) {
			filename.append(clusterCode).append("_");
			condCriteria.put("cluster_code", clusterCode);
		}
		if (instCode != null && !instCode.isEmpty()) {
			filename.append(instCode).append("_");
			condCriteria.put("inst_code", instCode);
		}
		if (deptCode != null && !deptCode.isEmpty()) {
			filename.append(deptCode).append("_");
			condCriteria.put("dept_code", deptCode);
		}
		if (staffGroupName != null && !staffGroupName.isEmpty()) {
			filename.append(staffGroupName).append("_");
			condCriteria.put("staff_group_code", staffGroupName);
		}
		if (rankName != null && !rankName.isEmpty()) {
			filename.append(rankName).append("_");
			condCriteria.put("rank_code", rankName);
		}
		filename.append("data_export").append(".xlsx");
		
		// get resultSet
		List<PostExportPo> exportList = postExportDao.getDataExportContent(dataAccessList, asAtDate, condCriteria);
		
		int maxNumberFundingSrc = postExportDao.getMaxNumberFundingSrc(dataAccessList, asAtDate, condCriteria);
		
		List<List<MPRSPostFundingSnapPo>> wholeFundingList = new ArrayList<List<MPRSPostFundingSnapPo>>();
		
		List<FundingSourcePo> fundingSourceList = fundingSourceDao.getAllFundingSource();
		List<FundingSourceSubCatPo> fundingSourceSubCatList = fundingSourceSubCatDao.getAllFundingSourceSubCat();
		List<ProgramTypePo> programTypeList = programTypeDao.getAllProgramType();
		
		for (int i=0; i<maxNumberFundingSrc; i++) {
			List<MPRSPostFundingSnapPo> fundingList = postExportDao.getPostFundingSnap(dataAccessList, asAtDate, condCriteria, (i+1)); 
			
			for (int m=0; m<fundingList.size(); m++) {
				String fundSrcId = fundingList.get(m).getFundSrcId();
				String fundSrcSubCatId = fundingList.get(m).getFundSrcSubCatId();
				String programTypeCode = fundingList.get(m).getProgramTypeCode();
				
				if (fundSrcId != null && !"".equals(fundSrcId)) {
					for (int a=0; a<fundingSourceList.size(); a++) {
						if (fundSrcId.equals(fundingSourceList.get(a).getSourceId())) {
							fundingList.get(m).setFundSrcDesc(fundingSourceList.get(a).getSourceDesc());
							break;
						}
					}
				}
				
				if (fundSrcSubCatId != null && !"".equals(fundSrcSubCatId)) {
					for (int a=0; a<fundingSourceSubCatList.size(); a++) {
						if (fundSrcSubCatId.equals(fundingSourceSubCatList.get(a).getSourceSubCatId())) {
							fundingList.get(m).setFundSrcSubCatDesc(fundingSourceSubCatList.get(a).getSourceSubCatDesc());
							break;
						}
					}
				}

				if (programTypeCode != null && !"".equals(programTypeCode)) {
					for (int a=0; a<programTypeList.size(); a++) {
						if (programTypeCode.equals(programTypeList.get(a).getProgramTypeCode())) {
							fundingList.get(m).setProgramTypeDesc(programTypeList.get(a).getProgramTypeName());
							break;
						}
					}
				}
			}
			
			wholeFundingList.add(fundingList);
		}
		
		// Set ByteArrayOutputStream to store POI data
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		// Finds the workbook instance for XLSX file
		XSSFWorkbook myWorkBook = new XSSFWorkbook();

		// Return first sheet from the XLSX workbook
		XSSFSheet mySheet = myWorkBook.createSheet("MPRS_export");

		// get the last row number to append new data
		int rownum = mySheet.getLastRowNum();
		// Creating header row
		Row headerRow = mySheet.createRow(rownum++);
		setHeaderRow(headerRow, maxNumberFundingSrc);

		XSSFCellStyle cs = myWorkBook.createCellStyle();
		XSSFDataFormat df = myWorkBook.createDataFormat();
		cs.setDataFormat(df.getFormat("yyyy-mm-dd"));

		// Set to Iterate and add rows into XLSX file
		for (PostExportPo post : exportList) {
			// Creating data row
			Row row = mySheet.createRow(rownum++);
			setRow(row, cs, asAtDate, post, wholeFundingList);
		}
		
		myWorkBook.write(outputStream);
		myWorkBook.close();
		
		// zip the XLSX
		return generateZip(filename.toString(), new ByteArrayInputStream(outputStream.toByteArray()), password);
	}
	
	public byte[] exportPostByPostId(List<DataAccessPo> dataAccessList, String postId, String password) throws Exception {
		// setup filename
		StringBuilder filename = new StringBuilder();
		if (postId != null && !postId.isEmpty()) {
			filename.append(postId).append("_");
		}

		filename.append("data_export").append(".xlsx");
		
		// get resultSet
		List<PostExportPo> exportList = postExportDao.getDataExportContentByPostId(dataAccessList, postId);
		
		int maxNumberFundingSrc = postExportDao.getMaxNumberFundingSrcByPostId(dataAccessList, postId);
		
		List<List<MPRSPostFundingSnapPo>> wholeFundingList = new ArrayList<List<MPRSPostFundingSnapPo>>();
		
		List<FundingSourcePo> fundingSourceList = fundingSourceDao.getAllFundingSource();
		List<FundingSourceSubCatPo> fundingSourceSubCatList = fundingSourceSubCatDao.getAllFundingSourceSubCat();
		List<ProgramTypePo> programTypeList = programTypeDao.getAllProgramType();
		
		for (int i=0; i<maxNumberFundingSrc; i++) {
			List<MPRSPostFundingSnapPo> fundingList = postExportDao.getPostFundingSnapByPostId(dataAccessList, postId, (i+1)); 
			
			for (int m=0; m<fundingList.size(); m++) {
				String fundSrcId = fundingList.get(m).getFundSrcId();
				String fundSrcSubCatId = fundingList.get(m).getFundSrcSubCatId();
				String programTypeCode = fundingList.get(m).getProgramTypeCode();
				
				if (fundSrcId != null && !"".equals(fundSrcId)) {
					for (int a=0; a<fundingSourceList.size(); a++) {
						if (fundSrcId.equals(fundingSourceList.get(a).getSourceId())) {
							fundingList.get(m).setFundSrcDesc(fundingSourceList.get(a).getSourceDesc());
							break;
						}
					}
				}
				
				if (fundSrcSubCatId != null && !"".equals(fundSrcSubCatId)) {
					for (int a=0; a<fundingSourceSubCatList.size(); a++) {
						if (fundSrcSubCatId.equals(fundingSourceSubCatList.get(a).getSourceSubCatId())) {
							fundingList.get(m).setFundSrcSubCatDesc(fundingSourceSubCatList.get(a).getSourceSubCatDesc());
							break;
						}
					}
				}
				
				if (programTypeCode != null && !"".equals(programTypeCode)) {
					for (int a=0; a<programTypeList.size(); a++) {
						if (programTypeCode.equals(programTypeList.get(a).getProgramTypeCode())) {
							fundingList.get(m).setProgramTypeDesc(programTypeList.get(a).getProgramTypeName());
							break;
						}
					}
				}
			}
			
			wholeFundingList.add(fundingList);
		}
		
		// Set ByteArrayOutputStream to store POI data
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		// Finds the workbook instance for XLSX file
		XSSFWorkbook myWorkBook = new XSSFWorkbook();

		// Return first sheet from the XLSX workbook
		XSSFSheet mySheet = myWorkBook.createSheet("MPRS_export");

		// get the last row number to append new data
		int rownum = mySheet.getLastRowNum();
		// Creating header row
		Row headerRow = mySheet.createRow(rownum++);
		setHeaderRowByPost(headerRow, maxNumberFundingSrc);

		XSSFCellStyle cs = myWorkBook.createCellStyle();
		XSSFDataFormat df = myWorkBook.createDataFormat();
		cs.setDataFormat(df.getFormat("yyyy-mm-dd"));

		// Set to Iterate and add rows into XLSX file
		for (PostExportPo post : exportList) {
			// Creating data row
			Row row = mySheet.createRow(rownum++);
			setRowByPost(row, cs, new Date(), post, wholeFundingList);
		}
		
		myWorkBook.write(outputStream);
		myWorkBook.close();
		
		// zip the XLSX
		return generateZip(filename.toString(), new ByteArrayInputStream(outputStream.toByteArray()), password);
	}
	
	private void setRow(Row row, XSSFCellStyle cs, Date asAtDate, PostExportPo post, List<List<MPRSPostFundingSnapPo>> wholeFundingList) throws Exception {
		int cellnum = 0;
		
		// Snapshot Date
		Cell cell = row.createCell(cellnum++);
		cell.setCellValue(DateTimeHelper.formatDateToString(asAtDate));
				
		// Post ID
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getPostId());

		// HCM Position
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getHcmPositionName());
		
		// Staff Group
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getStaffGroupName());
		
		// Cluster
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getClusterCode());

		// Hospital
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getInstCode());

		// Department
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getDeptCode());
		
		// Sub-specialty
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getSubSpecialtyDesc());

		// Unit
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getUnit());

		// Post Rank
		cell = row.createCell(cellnum++);
		if (YES_IND.equals(post.getSuppPromoInd())) {
			cell.setCellValue(POST_RANK_AC_SUPP_PROMO);
		} else {
			cell.setCellValue(post.getRankName());
		}
		
		// Post Title
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getPostTitle());

		// Approval Date
		cell = row.createCell(cellnum++);
		if (post.getApprovalDate() != null) {
			cell.setCellValue(post.getApprovalDate());
			cell.setCellStyle(cs);
		}
		
		// Approval Reference
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getApprovalRef());
		
		// Approval Remarks
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getApprovalRemark());

		// Post Duration
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getPostDuration());

		// Post Start Date
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getPostStartDate());
		cell.setCellStyle(cs);
		
		// Duration Period
		cell = row.createCell(cellnum++);
		if (post.getLimitDurationNo() != null) {
			cell.setCellValue(post.getLimitDurationNo());
		}

		// Duration Period Unit
		cell = row.createCell(cellnum++);
		if (post.getLimitDurationUnit() != null) {
			cell.setCellValue(post.getLimitDurationUnit());
		}
		
		// Actual Start Date
		cell = row.createCell(cellnum++);
		if (post.getActualStartDate() != null) {
			cell.setCellValue(post.getActualStartDate());
			cell.setCellStyle(cs);
		}
		
		// Actual End Date
		cell = row.createCell(cellnum++);
		if (post.getActualEndDate() != null) {
			cell.setCellValue(post.getActualEndDate());
			cell.setCellStyle(cs);
		}
		
		// Post End Date
		cell = row.createCell(cellnum++);
		if (post.getFixedEndDate() != null) {
			cell.setCellValue(post.getFixedEndDate());
			cell.setCellStyle(cs);
		}
		
		// Employee Number
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getEmployeeNumber());
		
		// Employee Name
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getEmployeeName());
		
		// Employee Rank
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getEmployeeRank());
		
		// Employee Category
		cell = row.createCell(cellnum++);
		if (post.getEmployeeCategory() != null) {
			cell.setCellValue(post.getEmployeeCategory());
		}
		
		// FTE Post
		cell = row.createCell(cellnum++);
		if (post.getPostFTE() != null) {
			cell.setCellValue(post.getPostFTE());
		}

		// FTE Strength
		cell = row.createCell(cellnum++);
		if (post.getStrengthFTE() != null) {
			cell.setCellValue(post.getStrengthFTE());
		}
		
		// FTE Vacancy
		cell = row.createCell(cellnum++);
		if (post.getVacancyFTE() != null) {
			cell.setCellValue(post.getVacancyFTE());
		}

		// Cluster Reference Number
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getClusterRef());
		
		// Remarks for Cluster Reference Information
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getClusterRemark());
		
		// Remarks for Post Information
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getPostRemark());
		
		// Position Status
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getPostStatusDesc());
		
		// Position Status Start Date
		cell = row.createCell(cellnum++);
		if (post.getPostStatusStartDate() != null) {
			cell.setCellValue(post.getPostStatusStartDate());
			cell.setCellStyle(cs);
		}
		
		// Position Status End Date
		cell = row.createCell(cellnum++);
		if (post.getPostStatusEndDate() != null) {
			cell.setCellValue(post.getPostStatusEndDate());
			cell.setCellStyle(cs);
		}
		
		// Is Supplementary Promotion?
//		cell = row.createCell(cellnum++);
//		if (post.getSuppPromoInd() != null) {
//			cell.setCellValue(post.getSuppPromoInd());
//		}
		
		// Is Upgrade to Time-Limited Senior Post?
		// Junior Post ID to be Upgraded
		if (post.getJuniorPostId() == null) {
			row.createCell(cellnum++);
			row.createCell(cellnum++);
		} else {
			row.createCell(cellnum++).setCellValue("Y");
			row.createCell(cellnum++).setCellValue(post.getJuniorPostId());
		}
		
		
		for (int i=0; i<wholeFundingList.size(); i++) {
			boolean matchFound = false;
			List<MPRSPostFundingSnapPo> tmp = wholeFundingList.get(i);
			for (int j=0; j<tmp.size(); j++) {
				MPRSPostFundingSnapPo currentFundingSnap = tmp.get(j);
				if (currentFundingSnap.getPostSnapUid().intValue() == post.getPostSnapUid().intValue()) {
					// Annual Plan
					cell = row.createCell(cellnum++);
					cell.setCellValue(currentFundingSnap.getAnnualPlanInd());
					
					
					// HO Program Year
					cell = row.createCell(cellnum++);
					cell.setCellValue(currentFundingSnap.getProgramYear());
					
					// HO Program Code
					cell = row.createCell(cellnum++);
					cell.setCellValue(currentFundingSnap.getProgramCode());
					
					// HO Program Name
					cell = row.createCell(cellnum++);
					cell.setCellValue(currentFundingSnap.getProgramName());
					
					// Program Type
					cell = row.createCell(cellnum++);
					cell.setCellValue(currentFundingSnap.getProgramTypeDesc());
					
					// Funding Source 
					cell = row.createCell(cellnum++);
					cell.setCellValue(currentFundingSnap.getFundSrcDesc());
					
					// Funding Source - Sub-category of Funding Source
					cell = row.createCell(cellnum++);
					cell.setCellValue(currentFundingSnap.getFundSrcSubCatDesc());
					
					// Funding Start Date
					cell = row.createCell(cellnum++);
					if (currentFundingSnap.getFundSrcStartDate() != null) {
						cell.setCellValue(currentFundingSnap.getFundSrcStartDate());
						cell.setCellStyle(cs);
					}
					
					// Funding End Date
					cell = row.createCell(cellnum++);
					if (currentFundingSnap.getFundSrcEndDate() != null) {
						cell.setCellValue(currentFundingSnap.getFundSrcEndDate());
						cell.setCellStyle(cs);
					}
					
					// Funding Source FTE
					cell = row.createCell(cellnum++);
					if (currentFundingSnap.getFundSrcFte() != null) {
						cell.setCellValue(currentFundingSnap.getFundSrcFte());
					}
					
					// institute
					cell = row.createCell(cellnum++);
					cell.setCellValue(currentFundingSnap.getInst());
					
					// Section
					cell = row.createCell(cellnum++);
					cell.setCellValue(currentFundingSnap.getSection());
					
					// Analystic
					cell = row.createCell(cellnum++);
					cell.setCellValue(currentFundingSnap.getAnalytical());
					
					// Funding Source Remark
					cell = row.createCell(cellnum++);
					cell.setCellValue(currentFundingSnap.getFundSrcRemark());
					
					matchFound = true;
					break;
				}
			}
			
			if (!matchFound) {
				// Annual Plan
				cell = row.createCell(cellnum++);
				
				// HO Program Year
				cell = row.createCell(cellnum++);
				
				// HO Program Code
				cell = row.createCell(cellnum++);
				
				// HO Program Name
				cell = row.createCell(cellnum++);
				
				// Program Type
				cell = row.createCell(cellnum++);
				
				// Funding Source 
				cell = row.createCell(cellnum++);
				
				// Funding Source - Sub-category of Funding Source
				cell = row.createCell(cellnum++);
				
				// Funding Start Date
				cell = row.createCell(cellnum++);
				
				// Funding End Date
				cell = row.createCell(cellnum++);
				
				// Funding Source FTE
				cell = row.createCell(cellnum++);
				
				// institute
				cell = row.createCell(cellnum++);
				
				// Section
				cell = row.createCell(cellnum++);
				
				// Analystic
				cell = row.createCell(cellnum++);
				
				// Funding Source Remark
				cell = row.createCell(cellnum++);
			}
		}
		
		// External Resources Support
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getResourcesSupportFrExt());
		
		// Remarks for External Resources Information
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getResourcesSupportRemark());
		
		// Payzone
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getPayzone());
	}
	
	private void setRowByPost(Row row, XSSFCellStyle cs, Date asAtDate, PostExportPo post, List<List<MPRSPostFundingSnapPo>> wholeFundingList) throws Exception {
		int cellnum = 0;
		
		// Snapshot Date
		Cell cell = row.createCell(cellnum++);
		cell.setCellValue(DateTimeHelper.formatDateToString(post.getEffectiveStartDate()));
				
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getRequestType());
		
		// Position Status
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getPostStatusDesc());

		// Post ID
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getPostId());

		// HCM Position
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getHcmPositionName());
		
		// Staff Group
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getStaffGroupName());
		
		// Cluster
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getClusterCode());

		// Hospital
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getInstCode());

		// Department
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getDeptCode());

		// Sub-specialty
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getSubSpecialtyDesc());

		// Unit
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getUnit());

		// Post Rank
		cell = row.createCell(cellnum++);
		if (YES_IND.equals(post.getSuppPromoInd())) {
			cell.setCellValue(POST_RANK_AC_SUPP_PROMO);
		} else {
			cell.setCellValue(post.getRankName());
		}
		
		// Post Title
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getPostTitle());

		// Approval Date
		cell = row.createCell(cellnum++);
		if (post.getApprovalDate() != null) {
			cell.setCellValue(post.getApprovalDate());
			cell.setCellStyle(cs);
		}
		
		// Approval Reference
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getApprovalRef());
		
		// Approval Remarks
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getApprovalRemark());

		// Post Duration
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getPostDuration());

		// Post Start Date
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getPostStartDate());
		cell.setCellStyle(cs);
		
		// Duration Period
		cell = row.createCell(cellnum++);
		if (post.getLimitDurationNo() != null) {
			cell.setCellValue(post.getLimitDurationNo());
		}

		// Duration Period Unit
		cell = row.createCell(cellnum++);
		if (post.getLimitDurationUnit() != null) {
			cell.setCellValue(post.getLimitDurationUnit());
		}
		
		// Actual Start Date
		cell = row.createCell(cellnum++);
		if (post.getActualStartDate() != null) {
			cell.setCellValue(post.getActualStartDate());
			cell.setCellStyle(cs);
		}
		
		// Actual End Date
		cell = row.createCell(cellnum++);
		if (post.getActualEndDate() != null) {
			cell.setCellValue(post.getActualEndDate());
			cell.setCellStyle(cs);
		}
		
		// Post End Date
		cell = row.createCell(cellnum++);
		if (post.getFixedEndDate() != null) {
			cell.setCellValue(post.getFixedEndDate());
			cell.setCellStyle(cs);
		}
		
		// Employee Number
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getEmployeeNumber());
		
		// Employee Name
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getEmployeeName());
		
		// Employee Rank
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getEmployeeRank());
		
		// Employee Category
		cell = row.createCell(cellnum++);
		if (post.getEmployeeCategory() != null) {
			cell.setCellValue(post.getEmployeeCategory());
		}
		
		// FTE Post
		cell = row.createCell(cellnum++);
		if (post.getPostFTE() != null) {
			cell.setCellValue(post.getPostFTE());
		}

		// FTE Strength
		cell = row.createCell(cellnum++);
		if (post.getStrengthFTE() != null) {
			cell.setCellValue(post.getStrengthFTE());
		}
		else {
			cell.setCellValue(0);
		}
		
		// FTE Vacancy
		cell = row.createCell(cellnum++);
		if (post.getVacancyFTE() != null) {
			cell.setCellValue(post.getVacancyFTE());
		}
		else {
			cell.setCellValue(0);
		}

		// Cluster Reference Number
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getClusterRef());
		
		// Remarks for Cluster Reference Information
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getClusterRemark());
		
		// Remarks for Post Information
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getPostRemark());
		
		// Position Status Start Date
		cell = row.createCell(cellnum++);
		if (post.getPostStatusStartDate() != null) {
			cell.setCellValue(post.getPostStatusStartDate());
			cell.setCellStyle(cs);
		}
		
		// Position Status End Date
		cell = row.createCell(cellnum++);
		if (post.getPostStatusEndDate() != null) {
			cell.setCellValue(post.getPostStatusEndDate());
			cell.setCellStyle(cs);
		}
		
		// Is Supplementary Promotion?
//		cell = row.createCell(cellnum++);
//		if (post.getSuppPromoInd() != null) {
//			cell.setCellValue(post.getSuppPromoInd());
//		}
		
		// Is Upgrade to Time-Limited Senior Post?
		// Junior Post ID to be Upgraded
		if (post.getJuniorPostId() == null) {
			row.createCell(cellnum++);
			row.createCell(cellnum++);
		} else {
			row.createCell(cellnum++).setCellValue("Y");
			row.createCell(cellnum++).setCellValue(post.getJuniorPostId());
		}

		// Funding
		for (int i=0; i<wholeFundingList.size(); i++) {
			boolean matchFound = false;
			List<MPRSPostFundingSnapPo> tmp = wholeFundingList.get(i);
			for (int j=0; j<tmp.size(); j++) {
				MPRSPostFundingSnapPo currentFundingSnap = tmp.get(j);
				if (currentFundingSnap.getPostSnapUid().intValue() == post.getPostSnapUid().intValue()) {
					// Annual Plan
					cell = row.createCell(cellnum++);
					cell.setCellValue(currentFundingSnap.getAnnualPlanInd());
					
					// HO Program Year
					cell = row.createCell(cellnum++);
					cell.setCellValue(currentFundingSnap.getProgramYear());
					
					// HO Program Code
					cell = row.createCell(cellnum++);
					cell.setCellValue(currentFundingSnap.getProgramCode());
					
					// HO Program Name
					cell = row.createCell(cellnum++);
					cell.setCellValue(currentFundingSnap.getProgramName());
					
					// Program Type
					cell = row.createCell(cellnum++);
					cell.setCellValue(currentFundingSnap.getProgramTypeDesc());
					
					// Funding Source 
					cell = row.createCell(cellnum++);
					cell.setCellValue(currentFundingSnap.getFundSrcDesc());
					
					// Funding Source - Sub-category of Funding Source
					cell = row.createCell(cellnum++);
					cell.setCellValue(currentFundingSnap.getFundSrcSubCatDesc());
					
					// Funding Start Date
					cell = row.createCell(cellnum++);
					if (currentFundingSnap.getFundSrcStartDate() != null) {
						cell.setCellValue(currentFundingSnap.getFundSrcStartDate());
						cell.setCellStyle(cs);
					}
					
					// Funding End Date
					cell = row.createCell(cellnum++);
					if (currentFundingSnap.getFundSrcEndDate() != null) {
						cell.setCellValue(currentFundingSnap.getFundSrcEndDate());
						cell.setCellStyle(cs);
					}
					
					// Funding Source FTE
					cell = row.createCell(cellnum++);
					if (currentFundingSnap.getFundSrcFte() != null) {
						cell.setCellValue(currentFundingSnap.getFundSrcFte());
					}
					
					// institute
					cell = row.createCell(cellnum++);
					cell.setCellValue(currentFundingSnap.getInst());
					
					// Section
					cell = row.createCell(cellnum++);
					cell.setCellValue(currentFundingSnap.getSection());
					
					// Analystic
					cell = row.createCell(cellnum++);
					cell.setCellValue(currentFundingSnap.getAnalytical());
					
					// Funding Source Remark
					cell = row.createCell(cellnum++);
					cell.setCellValue(currentFundingSnap.getFundSrcRemark());
					
					matchFound = true;
					break;
				}
			}
			
			if (!matchFound) {
				// Annual Plan
				cell = row.createCell(cellnum++);
				
				// HO Program Year
				cell = row.createCell(cellnum++);
				
				// HO Program Code
				cell = row.createCell(cellnum++);
				
				// HO Program Name
				cell = row.createCell(cellnum++);
				
				// Program Type
				cell = row.createCell(cellnum++);
				
				// Funding Source 
				cell = row.createCell(cellnum++);
				
				// Funding Source - Sub-category of Funding Source
				cell = row.createCell(cellnum++);
				
				// Funding Start Date
				cell = row.createCell(cellnum++);
				
				// Funding End Date
				cell = row.createCell(cellnum++);
				
				// Funding Source FTE
				cell = row.createCell(cellnum++);
				
				// institute
				cell = row.createCell(cellnum++);
				
				// Section
				cell = row.createCell(cellnum++);
				
				// Analystic
				cell = row.createCell(cellnum++);
				
				// Funding Source Remark
				cell = row.createCell(cellnum++);
			}
		}
		
		// External Resources Support
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getResourcesSupportFrExt());
		
		// Remarks for External Resources Information
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getResourcesSupportRemark());
		
		// Payzone
		cell = row.createCell(cellnum++);
		cell.setCellValue(post.getPayzone());
	}
	
	private void setHeaderRow(Row row, int maxNumberFundingSrc) throws Exception {
		int cellnum = 0;
		row.createCell(cellnum++).setCellValue("As At Date");

		// Position
		row.createCell(cellnum++).setCellValue("Post ID");
		row.createCell(cellnum++).setCellValue("HCM Position");
		row.createCell(cellnum++).setCellValue("Staff Group");
		row.createCell(cellnum++).setCellValue("Cluster");
		row.createCell(cellnum++).setCellValue("Hospital");
		row.createCell(cellnum++).setCellValue("Department");
		row.createCell(cellnum++).setCellValue("Sub-specialty");
		row.createCell(cellnum++).setCellValue("Unit");
		row.createCell(cellnum++).setCellValue("Post Rank");
		row.createCell(cellnum++).setCellValue("Post Title");
		row.createCell(cellnum++).setCellValue("Approval Date");
		row.createCell(cellnum++).setCellValue("Approval Reference");
		row.createCell(cellnum++).setCellValue("Approval Remarks");
		row.createCell(cellnum++).setCellValue("Post Duration");
		row.createCell(cellnum++).setCellValue("Post Start Date");
		row.createCell(cellnum++).setCellValue("Duration Period");
		row.createCell(cellnum++).setCellValue("Duration Period Unit");
		row.createCell(cellnum++).setCellValue("Actual Start Date");
		row.createCell(cellnum++).setCellValue("Actual End Date");
		row.createCell(cellnum++).setCellValue("Post End Date");
		row.createCell(cellnum++).setCellValue("Employee Number");
		row.createCell(cellnum++).setCellValue("Employee Name");
		row.createCell(cellnum++).setCellValue("Employee Rank");
		row.createCell(cellnum++).setCellValue("Employee Category");
		row.createCell(cellnum++).setCellValue("FTE Post");
		row.createCell(cellnum++).setCellValue("FTE Strength");
		row.createCell(cellnum++).setCellValue("FTE Vacancy");
		row.createCell(cellnum++).setCellValue("Cluster Reference Number");
		row.createCell(cellnum++).setCellValue("Remarks for Cluster Reference Information");
		row.createCell(cellnum++).setCellValue("Remarks for Post Information");
		row.createCell(cellnum++).setCellValue("Post Status");
		row.createCell(cellnum++).setCellValue("Post Status Start Date");
		row.createCell(cellnum++).setCellValue("Post Status End Date");
//		row.createCell(cellnum++).setCellValue("Is Supplementary Promotion?");
		row.createCell(cellnum++).setCellValue("Is Upgrade to Time-Limited Senior Post?");
		row.createCell(cellnum++).setCellValue("Junior Post ID to be Upgraded");

		// Funding
		for (int i=0; i<maxNumberFundingSrc; i++) {
			String prefix = "Funding Source " + (i+1) + " - ";
			row.createCell(cellnum++).setCellValue(prefix + "Supported by HO Annual Plan?");
			row.createCell(cellnum++).setCellValue(prefix + "HO Program Year");
			row.createCell(cellnum++).setCellValue(prefix + "HO Program Code");
			row.createCell(cellnum++).setCellValue(prefix + "HO Program Name");
			row.createCell(cellnum++).setCellValue(prefix + "Program Type");
			row.createCell(cellnum++).setCellValue(prefix + "Funding Source");
			row.createCell(cellnum++).setCellValue(prefix + "Sub-category of Funding Source");
			row.createCell(cellnum++).setCellValue(prefix + "Funding Start Date");
			row.createCell(cellnum++).setCellValue(prefix + "Funding End Date");
			row.createCell(cellnum++).setCellValue(prefix + "FTE");
			row.createCell(cellnum++).setCellValue(prefix + "Cost Code - Inst");
			row.createCell(cellnum++).setCellValue(prefix + "Cost Code - Section");
			row.createCell(cellnum++).setCellValue(prefix + "Cost Code - Analytical");
			row.createCell(cellnum++).setCellValue(prefix + "Funding Remarks");
		}
		
		row.createCell(cellnum++).setCellValue("External Resources Support");
		row.createCell(cellnum++).setCellValue("Remarks for External Resources Information");
		
		row.createCell(cellnum++).setCellValue("Payzone");
	}
	
	private void setHeaderRowByPost(Row row, int maxNumberFundingSrc) throws Exception {
		int cellnum = 0;
		row.createCell(cellnum++).setCellValue("Effective Date");
		row.createCell(cellnum++).setCellValue("Request Type");
		row.createCell(cellnum++).setCellValue("Post Status");
		row.createCell(cellnum++).setCellValue("Post ID");
		row.createCell(cellnum++).setCellValue("HCM Position");
		row.createCell(cellnum++).setCellValue("Staff Group");
		row.createCell(cellnum++).setCellValue("Cluster");
		row.createCell(cellnum++).setCellValue("Hospital");
		row.createCell(cellnum++).setCellValue("Department");
		row.createCell(cellnum++).setCellValue("Sub-specialty");
		row.createCell(cellnum++).setCellValue("Unit");
		row.createCell(cellnum++).setCellValue("Post Rank");
		row.createCell(cellnum++).setCellValue("Post Title");
		row.createCell(cellnum++).setCellValue("Approval Date");
		row.createCell(cellnum++).setCellValue("Approval Reference");
		row.createCell(cellnum++).setCellValue("Approval Remarks");
		row.createCell(cellnum++).setCellValue("Post Duration");
		row.createCell(cellnum++).setCellValue("Post Start Date");
		row.createCell(cellnum++).setCellValue("Duration Period");
		row.createCell(cellnum++).setCellValue("Duration Period Unit");
		row.createCell(cellnum++).setCellValue("Actual Start Date");
		row.createCell(cellnum++).setCellValue("Actual End Date");
		row.createCell(cellnum++).setCellValue("Post End Date");
		row.createCell(cellnum++).setCellValue("Employee Number");
		row.createCell(cellnum++).setCellValue("Employee Name");
		row.createCell(cellnum++).setCellValue("Employee Rank");
		row.createCell(cellnum++).setCellValue("Employee Category");
		row.createCell(cellnum++).setCellValue("FTE Post");
		row.createCell(cellnum++).setCellValue("FTE Strength");
		row.createCell(cellnum++).setCellValue("FTE Vacancy");
		row.createCell(cellnum++).setCellValue("Cluster Reference Number");
		row.createCell(cellnum++).setCellValue("Remarks for Cluster Reference Information");
		row.createCell(cellnum++).setCellValue("Remarks for Post Information");
		row.createCell(cellnum++).setCellValue("Post Status Start Date");
		row.createCell(cellnum++).setCellValue("Post Status End Date");
		row.createCell(cellnum++).setCellValue("Is Upgrade to Time-Limited Senior Post?");
		row.createCell(cellnum++).setCellValue("Junior Post ID to be Upgraded");
		
		for (int i=0; i<maxNumberFundingSrc; i++) {
			String prefix = "Funding Source " + (i+1) + " - ";
			row.createCell(cellnum++).setCellValue(prefix + "Supported by HO Annual Plan?");
			row.createCell(cellnum++).setCellValue(prefix + "HO Program Year");
			row.createCell(cellnum++).setCellValue(prefix + "HO Program Code");
			row.createCell(cellnum++).setCellValue(prefix + "HO Program Name");
			row.createCell(cellnum++).setCellValue(prefix + "Program Type");
			row.createCell(cellnum++).setCellValue(prefix + "Funding Source");
			row.createCell(cellnum++).setCellValue(prefix + "Sub-category of Funding Source");
			row.createCell(cellnum++).setCellValue(prefix + "Funding Start Date");
			row.createCell(cellnum++).setCellValue(prefix + "Funding End Date");
			row.createCell(cellnum++).setCellValue(prefix + "FTE");
			row.createCell(cellnum++).setCellValue(prefix + "Cost Code - Inst");
			row.createCell(cellnum++).setCellValue(prefix + "Cost Code - Section");
			row.createCell(cellnum++).setCellValue(prefix + "Cost Code - Analytical");
			row.createCell(cellnum++).setCellValue(prefix + "Funding Remarks");
		}
		
		row.createCell(cellnum++).setCellValue("External Resources Support");
		row.createCell(cellnum++).setCellValue("Remarks for External Resources Information");
		
		row.createCell(cellnum++).setCellValue("Payzone");
	}
	
	private byte[] generateZip(String filename, InputStream inputStream, String password) throws Exception {
		
		ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
		
		AESEncrypter aesEncrypter = new AESEncrypterBC();
		aesEncrypter.init(password, 0);
		
		AesZipFileEncrypter enc = new AesZipFileEncrypter(outputstream, aesEncrypter);
		enc.add(filename, inputStream, password);
		enc.close();
		
		return outputstream.toByteArray();
	}

	/**** Generate Excel for report 1 - Start ****/
	public byte[] exportESVbyHospRank(List<Report1Po> listOfRequest, 
			                          List<ReportHeldAgainstListPo> listOfHeldAgainst,
			                          String asAt, String clusterCode, String hospital, String rank, String staffGroupName) throws Exception {
		// Set ByteArrayOutputStream to store POI data
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		// Finds the workbook instance for XLSX file
		XSSFWorkbook myWorkBook = new XSSFWorkbook();

		// Return first sheet from the XLSX workbook
		XSSFSheet mySheet = myWorkBook.createSheet("ESVbyHospRank");
		
		mySheet.setColumnWidth(0, 4000);
		mySheet.setColumnWidth(1, 4000);
		mySheet.setColumnWidth(2, 6400);
		mySheet.setColumnWidth(3, 4000);
		mySheet.setColumnWidth(4, 4000);
		mySheet.setColumnWidth(5, 4000);
		mySheet.setColumnWidth(6, 4000);
		mySheet.setColumnWidth(7, 4000);
		mySheet.setColumnWidth(8, 4000);

		// get the last row number to append new data
		int rownum = mySheet.getLastRowNum();
		
		XSSFCellStyle csBold = myWorkBook.createCellStyle();
		XSSFFont font = myWorkBook.createFont();
		font.setBold(true);
		csBold.setFont(font);
		
		XSSFCellStyle csNumber = myWorkBook.createCellStyle();
		XSSFDataFormat df = myWorkBook.createDataFormat();
		csNumber.setDataFormat(df.getFormat("#,##0.00"));

		// Set to Iterate and add rows into XLSX file
		boolean printHeader = true;
		
		double total1 = 0;
		double total2 = 0;
		double total3 = 0;
		double total4 = 0;
		double total5 = 0;
		double total6 = 0;
		
		// Print the report header
		Row titleRow = mySheet.createRow(rownum++);
		Cell tmp = titleRow.createCell(0);
		tmp.setCellValue("HOSPITAL AUTHORITY");
		
		String[] currentDatetime = DateTimeHelper.formatDateTimeToString(new Date()).split(" ");
		
		tmp = titleRow.createCell(7);
		tmp.setCellValue("Date:");
		
		tmp = titleRow.createCell(8);
		tmp.setCellValue(currentDatetime[0]);
		
		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("MANPOWER POSITION REGISTRY SYSTEM - ESV By Hospital & Ranks");
		
		tmp = titleRow.createCell(7);
		tmp.setCellValue("Time:");
		
		tmp = titleRow.createCell(8);
		tmp.setCellValue(currentDatetime[1]);
		
		// Empty Row
		rownum++;

		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Report Criteria:");
		
		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Date As At:");
		
		tmp = titleRow.createCell(1);
		tmp.setCellValue(asAt);
		
		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Cluster:");
		
		tmp = titleRow.createCell(1);
		tmp.setCellValue(clusterCode);
		
		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Institution:");
		
		tmp = titleRow.createCell(1);
		tmp.setCellValue(hospital);
		
		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Staff Group:");
		
		tmp = titleRow.createCell(1);
		tmp.setCellValue(staffGroupName);
		
		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Rank:");
		
		tmp = titleRow.createCell(1);
		tmp.setCellValue(rank);
		
		// Empty Row
		rownum++;
		
		// Creating header row
		Row headerRow = mySheet.createRow(rownum++);
		
		double grandTotal1 = 0;
		double grandTotal2 = 0;
		double grandTotal3 = 0;
		double grandTotal4 = 0;
		double grandTotal5 = 0;
		double grandTotal6 = 0;
				
		// for (Report1Po post : listOfRequest) {
		for (int i=0; i<listOfRequest.size(); i++) {
			Report1Po post = listOfRequest.get(i);
			
			if (printHeader) {
				setReport1HeaderRow(headerRow, csBold);
				printHeader = false;
			}
			// Creating data row
			Row row = mySheet.createRow(rownum++);
			setRowReport1(row, csNumber, post);
			
			total1 = total1 + post.getLastYrFTE();
			total2 = total2 + post.getCurrYrFTE();
			total3 = total3 + post.getTotalFTE();
			total4 = total4 + post.getStrengthFTE();
			total5 = total5 + post.getVacanciesFTE();
			
			grandTotal1 = grandTotal1 + post.getLastYrFTE();
			grandTotal2 = grandTotal2 + post.getCurrYrFTE();
			grandTotal3 = grandTotal3 + post.getTotalFTE();
			grandTotal4 = grandTotal4 + post.getStrengthFTE();
			grandTotal5 = grandTotal5 + post.getVacanciesFTE();
			
			if (i != listOfRequest.size() -1) {
				if (!post.getHospital().equals(listOfRequest.get(i+1).getHospital())) {
					row = mySheet.createRow(rownum++);
					total6 = total4/total3*100;
					setReport1TotalRow(row, csBold, csNumber, post.getHospital(), total1, total2, total3, total4, total5, total6);
					
					if (!post.getClusterCode().equals(listOfRequest.get(i+1).getClusterCode())) {
						Row grandRow = mySheet.createRow(rownum++);
						grandTotal6 = grandTotal4/grandTotal3*100;
						setReport1GrandTotalRow(grandRow, csBold, csNumber, post.getClusterCode(), grandTotal1, grandTotal2, grandTotal3, grandTotal4, grandTotal5, grandTotal6);
						
						grandTotal1 = 0;
						grandTotal2 = 0;
						grandTotal3 = 0;
						grandTotal4 = 0;
						grandTotal5 = 0;
						grandTotal6 = 0;
					}
					
					rownum++;
					printHeader = true;
					headerRow = mySheet.createRow(rownum++);
					total1 = 0;
					total2 = 0;
					total3 = 0;
					total4 = 0;
					total5 = 0;
					total6 = 0;
				}
			}
			else if (i == listOfRequest.size()-1) {
				row = mySheet.createRow(rownum++);
				total6 = total4/total3*100;
				setReport1TotalRow(row, csBold, csNumber, post.getHospital(), total1, total2, total3, total4, total5, total6);
				
				total1 = 0;
				total2 = 0;
				total3 = 0;
				total4 = 0;
				total5 = 0;
				total6 = 0;
				
				Row grandRow = mySheet.createRow(rownum++);
				grandTotal6 = grandTotal4/grandTotal3*100;
				setReport1GrandTotalRow(grandRow, csBold, csNumber, post.getClusterCode(), grandTotal1, grandTotal2, grandTotal3, grandTotal4, grandTotal5, grandTotal6);
			}
		}
		
		if (listOfHeldAgainst.size() > 0) {
			rownum++;
			rownum++;
			rownum++;
			
			// Creating header row
			headerRow = mySheet.createRow(rownum++);
			setReport1HeaderRowHeldAgainst(headerRow, csBold);
	
			// Set to Iterate and add rows into XLSX file
			for (ReportHeldAgainstListPo post : listOfHeldAgainst) {
				// Creating data row
				Row row = mySheet.createRow(rownum++);
				setRowReport1HeldAgainst(row, csNumber, post);
			}
		}
		
		myWorkBook.write(outputStream);
		myWorkBook.close();
		
		return outputStream.toByteArray();
	}
	
	private void setReport1HeaderRow(Row row, XSSFCellStyle styleFont) throws Exception {
		int cellnum = 0;
		Cell tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Cluster");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Hospital");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Post Rank");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Last Year FTE");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("This Year Add'l FTE");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Post FTE");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Strength FTE");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Vacancies FTE");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Occupancy Rate %");
	}
	
	private void setReport1HeaderRowHeldAgainst(Row row, XSSFCellStyle styleFont) throws Exception {
		int cellnum = 0;
		Cell tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Cluster");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Hospital");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Post ID");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employee ID");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employee Name");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Generic Rank");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employment Type");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employee Category");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Strength FTE");
	}
	
	private void setReport1TotalRow(Row row, XSSFCellStyle styleFont, XSSFCellStyle csNumber, String hospital, double total1, double total2, double total3, double total4, double total5, double total6) {
		// Add the total row
		
		int cellnum = 0;
		
		// Cluster
		Cell cell = row.createCell(cellnum++);
		cell.setCellValue("");
				
		// Hospital
		cell = row.createCell(cellnum++);
		cell.setCellValue("");
		
		// Post Rank
		cell = row.createCell(cellnum++);
		cell.setCellStyle(styleFont);
		cell.setCellValue(hospital + " Total");
		
		// Last Year FTE
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total1);
		
		// This Year FTE
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total2);
		
		// Post FTE
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total3);
		
		// Strength FTE
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total4);
		
		// Vacancies FTE
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total5);
		
		// Occupancy Rate
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total6);
	}
	
	private void setReport1GrandTotalRow(Row row, XSSFCellStyle styleFont, XSSFCellStyle csNumber, String clusterCode, double total1, double total2, double total3, double total4, double total5, double total6) {
		// Add the total row
		
		int cellnum = 0;
		
		// Cluster
		Cell cell = row.createCell(cellnum++);
		cell.setCellValue("");
				
		// Hospital
		cell = row.createCell(cellnum++);
		cell.setCellValue("");
		
		// Post Rank
		cell = row.createCell(cellnum++);
		cell.setCellStyle(styleFont);
		cell.setCellValue(clusterCode + " Total");
		
		// Last Year FTE
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total1);
		
		// This Year FTE
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total2);
		
		// Post FTE
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total3);
		
		// Strength FTE
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total4);
		
		// Vacancies FTE
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total5);
		
		// Occupancy Rate
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total6);
	}
	
	private void setRowReport1(Row row, XSSFCellStyle csNumber, Report1Po po) {
		int cellnum = 0;
		
		// Cluster
		Cell cell = row.createCell(cellnum++);
		cell.setCellValue(po.getClusterCode());
				
		// Hospital
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getHospital());
		
		// Post Rank
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getPostTitle());
		
		// Last Year FTE
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getLastYrFTE());
		
		// This Year FTE
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getCurrYrFTE());
		
		// Post FTE
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getTotalFTE());
		
		// Strength FTE
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getStrengthFTE());
		
		// Vacancies FTE
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getVacanciesFTE());
		
		// Occupancy Rate
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getOccRate());
	}
	
	private void setRowReport1HeldAgainst(Row row, XSSFCellStyle csNumber, ReportHeldAgainstListPo po) {
		int cellnum = 0;
		
		Cell cell = row.createCell(cellnum++);
		cell.setCellValue(po.getClusterCode());
				
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getInstCode());
		
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getPostId());
		
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getEmployeeId());
		
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getEmployeeName());
		
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getRank());
		
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getEmploymentType());
		
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getEmployeeCategory());
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getStrengthFTE());
	}
	/**** Generate Excel for report 1 - End ****/

	/**** Generate Excel for report 2 - Start ****/
	public byte[] exportESVbyDeptRank(List<Report2Po> listOfRequest, 
									  List<ReportHeldAgainstListPo> listOfHeldAgainst,
									  String dateAsAt, 
									  String clusterCode,
									  String deptName,
									  String staffGroupName,
									  String rankName) throws Exception {
		// Set ByteArrayOutputStream to store POI data
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		// Finds the workbook instance for XLSX file
		XSSFWorkbook myWorkBook = new XSSFWorkbook();

		// Return first sheet from the XLSX workbook
		XSSFSheet mySheet = myWorkBook.createSheet("ESVbyDeptRank");
		
		mySheet.setColumnWidth(0, 4000);
		mySheet.setColumnWidth(1, 4000);
		mySheet.setColumnWidth(2, 4000);
		mySheet.setColumnWidth(3, 6400);
		mySheet.setColumnWidth(4, 4000);
		mySheet.setColumnWidth(5, 4000);
		mySheet.setColumnWidth(6, 4000);
		mySheet.setColumnWidth(7, 4000);

		// get the last row number to append new data
		int rownum = mySheet.getLastRowNum();

		XSSFCellStyle csBold = myWorkBook.createCellStyle();
		XSSFFont font = myWorkBook.createFont();
		font.setBold(true);
		csBold.setFont(font);

		XSSFCellStyle csNumber = myWorkBook.createCellStyle();
		XSSFDataFormat df = myWorkBook.createDataFormat();
		csNumber.setDataFormat(df.getFormat("#,##0.00"));

		// Set to Iterate and add rows into XLSX file
		boolean printHeader = true;

		double total1 = 0;
		double total2 = 0;
		double total3 = 0;
		double total4 = 0;

		double fTotal1 = 0;
		double fTotal2 = 0;
		double fTotal3 = 0;
		double fTotal4 = 0;
		
		double grandTotal1 = 0;
		double grandTotal2 = 0;
		double grandTotal3 = 0;
		double grandTotal4 = 0;


		int counter = 0;

		// Print the report header
		Row titleRow = mySheet.createRow(rownum++);
		Cell tmp = titleRow.createCell(0);
		tmp.setCellValue("HOSPITAL AUTHORITY");

		String[] currentDatetime = DateTimeHelper.formatDateTimeToString(new Date()).split(" ");

		tmp = titleRow.createCell(7);
		tmp.setCellValue("Date:");

		tmp = titleRow.createCell(8);
		tmp.setCellValue(currentDatetime[0]);

		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("MANPOWER POSITION REGISTRY SYSTEM - ESV By Departments & Ranks");

		tmp = titleRow.createCell(7);
		tmp.setCellValue("Time:");

		tmp = titleRow.createCell(8);
		tmp.setCellValue(currentDatetime[1]);

		// Empty Row
		rownum++;

		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Report Criteria:");

		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Date As At:");

		tmp = titleRow.createCell(1);
		tmp.setCellValue(dateAsAt);
		
		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Cluster:");

		tmp = titleRow.createCell(1);
		tmp.setCellValue(clusterCode);
		
		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Staff Group:");
		
		tmp = titleRow.createCell(1);
		tmp.setCellValue(staffGroupName);

		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Department:");

		tmp = titleRow.createCell(1);
		tmp.setCellValue(deptName);
		
		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Rank:");
		
		tmp = titleRow.createCell(1);
		tmp.setCellValue(rankName);

		// Empty Row
		rownum++;

		// Creating header row
		Row headerRow = mySheet.createRow(rownum++);

		// for (Report1Po post : listOfRequest) {
		for (int i=0; i<listOfRequest.size(); i++) {
			Report2Po post = listOfRequest.get(i);

			if (printHeader) {
				setReport2HeaderRow(headerRow, csBold);
				printHeader = false;
			}
			// Creating data row
			Row row = mySheet.createRow(rownum++);
			setRowReport2(row, csNumber, post);

			total1 = total1 + post.getTotalFTE();
			total2 = total2 + post.getStrengthFTE();
			total3 = total3 + post.getVacanciesFTE();
			total4 = total4 + post.getOccRate();

			fTotal1 = fTotal1 + post.getTotalFTE();
			fTotal2 = fTotal2 + post.getStrengthFTE();
			fTotal3 = fTotal3 + post.getVacanciesFTE();
			
			grandTotal1 = grandTotal1 + post.getTotalFTE();
			grandTotal2 = grandTotal2 + post.getStrengthFTE();
			grandTotal3 = grandTotal3 + post.getVacanciesFTE();

			counter++;

			if (i != listOfRequest.size() -1) {
				if (!(post.getHospital().equals(listOfRequest.get(i+1).getHospital()) && post.getDeptName().equals(listOfRequest.get(i+1).getDeptName()))) {
					row = mySheet.createRow(rownum++);
					total4 = total4/counter;
					setReport2TotalRow(row, csBold, csNumber, post.getHospital(), post.getDeptName(), total1, total2, total3, total4);

					if (!post.getHospital().equals(listOfRequest.get(i+1).getHospital())) {
						row = mySheet.createRow(rownum++);
						fTotal4 = fTotal2/fTotal1*100;
						setReport2TotalRow(row, csBold, csNumber, post.getHospital(), null, fTotal1, fTotal2, fTotal3, fTotal4);

						if (!post.getClusterCode().equals(listOfRequest.get(i+1).getClusterCode())) {
							row = mySheet.createRow(rownum++);
							grandTotal4 = grandTotal2/grandTotal1*100;
							setReport2GrandTotalRow(row, csBold, csNumber, post.getClusterCode(), grandTotal1, grandTotal2, grandTotal3, grandTotal4);

							rownum++;
							printHeader = true;
							headerRow = mySheet.createRow(rownum++);
							
							grandTotal1 = 0;
							grandTotal2 = 0;
							grandTotal3 = 0;
							grandTotal4 = 0;
						}
						
						rownum++;
						printHeader = true;
						headerRow = mySheet.createRow(rownum++);
						
						fTotal1 = 0;
						fTotal2 = 0;
						fTotal3 = 0;
						fTotal4 = 0;
					}
					
					// rownum++;
					// printHeader = true;
					// headerRow = mySheet.createRow(rownum++);
					total1 = 0;
					total2 = 0;
					total3 = 0;
					total4 = 0;
					counter = 0;
				}
			}
			else if (i == listOfRequest.size()-1) {
				row = mySheet.createRow(rownum++);
				total4 = total4/counter;
				setReport2TotalRow(row, csBold, csNumber, post.getHospital(), post.getDeptName(), total1, total2, total3, total4);

				total1 = 0;
				total2 = 0;
				total3 = 0;
				total4 = 0;
				counter = 0;

				row = mySheet.createRow(rownum++);
				fTotal4 = fTotal2/fTotal1*100;
				setReport2TotalRow(row, csBold, csNumber, post.getHospital(), null, fTotal1, fTotal2, fTotal3, fTotal4);
				
				row = mySheet.createRow(rownum++);
				grandTotal4 = grandTotal2/grandTotal1*100;
				setReport2GrandTotalRow(row, csBold, csNumber, post.getClusterCode(), grandTotal1, grandTotal2, grandTotal3, grandTotal4);

				grandTotal1 = 0;
				grandTotal2 = 0;
				grandTotal3 = 0;
				grandTotal4 = 0;
			}
		}

		if (listOfHeldAgainst.size() > 0) {
			rownum++;
			rownum++;
			rownum++;

			// Creating header row
			headerRow = mySheet.createRow(rownum++);
			setReport2HeaderRowHeldAgainst(headerRow, csBold);

			// Set to Iterate and add rows into XLSX file
			for (ReportHeldAgainstListPo post : listOfHeldAgainst) {
				// Creating data row
				Row row = mySheet.createRow(rownum++);
				setRowReport2HeldAgainst(row, csNumber, post);
			}
		}

		myWorkBook.write(outputStream);
		myWorkBook.close();

		return outputStream.toByteArray();
	}
	
	private void setReport2HeaderRow(Row row, XSSFCellStyle styleFont) throws Exception {
		int cellnum = 0;
		Cell tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Cluster");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Hospital");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Department");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Post Rank");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Post FTE");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Strength FTE");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Vacancies FTE");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Occupancy Rate %");
	}
	
	private void setReport2HeaderRowHeldAgainst(Row row, XSSFCellStyle styleFont) throws Exception {
		int cellnum = 0;
		Cell tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Cluster");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Hospital");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Department");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Post ID");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employee ID");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employee Name");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Generic Rank");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employment Type");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employee Category");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Strength FTE");
	}
	
	private void setReport2TotalRow(Row row, XSSFCellStyle styleFont, XSSFCellStyle csNumber, String hospital, String deptName, double total1, double total2, double total3, double total4) {
		// Add the total row
		
		int cellnum = 3;
		
		Cell cell = row.createCell(cellnum++);
		cell.setCellStyle(styleFont);
		
		if (deptName != null) {
			cell.setCellValue(hospital + " " + deptName + " Subtotal");
		}
		else {
			cell.setCellValue(hospital + " Total");
		}
		
		// Post FTE
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total1);
		
		// Strength FTE
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total2);
		
		// Vacancies FTE
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total3);
		
		// Occupancy Rate
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total4);
	}
	
	private void setReport2GrandTotalRow(Row row, XSSFCellStyle styleFont, XSSFCellStyle csNumber, String cluster, double total1, double total2, double total3, double total4) {
		// Add the total row
		
		int cellnum = 3;
		
		Cell cell = row.createCell(cellnum++);
		cell.setCellStyle(styleFont);
		cell.setCellValue(cluster + " Total");
		
		// Post FTE
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total1);
		
		// Strength FTE
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total2);
		
		// Vacancies FTE
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total3);
		
		// Occupancy Rate
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total4);
	}
	
	private void setRowReport2(Row row, XSSFCellStyle csNumber, Report2Po po) {
		int cellnum = 0;
		
		// Cluster
		Cell cell = row.createCell(cellnum++);
		cell.setCellValue(po.getClusterCode());
				
		// Hospital
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getHospital());
		
		// Department
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getDeptName());
		
		// Post Rank
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getPostTitle());
		
		// Post FTE
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getTotalFTE());
		
		// Strength FTE
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getStrengthFTE());
		
		// Vacancies FTE
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getVacanciesFTE());
		
		// Occupancy Rate
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getOccRate());
	}
	
	private void setRowReport2HeldAgainst(Row row, XSSFCellStyle csNumber, ReportHeldAgainstListPo po) {
		int cellnum = 0;
		
		Cell cell = row.createCell(cellnum++);
		cell.setCellValue(po.getClusterCode());
				
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getInstCode());
		
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getDeptName());
		
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getPostId());
		
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getEmployeeId());
		
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getEmployeeName());
		
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getRank());
		
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getEmploymentType());
		
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getEmployeeCategory());
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getStrengthFTE());
	}
	/**** Generate Excel for report 2 - End ****/

	/**** Generate Excel for report 3 - Start ****/
	public byte[] exportESVDetail(List<Report3Po> listOfRequest, 
			                      List<ReportHeldAgainstListPo> listOfHeldAgainst,
								  String dateAsAt, String clusterCode, String staffGroupName, String rankName) throws Exception {
		
		// Set ByteArrayOutputStream to store POI data
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		// Finds the workbook instance for XLSX file
		XSSFWorkbook myWorkBook = new XSSFWorkbook();

		// Return first sheet from the XLSX workbook
		XSSFSheet mySheet = myWorkBook.createSheet("ESVDetail");
		mySheet.setColumnWidth(0, 4000);
		mySheet.setColumnWidth(1, 4000);
		mySheet.setColumnWidth(2, 6400);
		mySheet.setColumnWidth(3, 4000);
		mySheet.setColumnWidth(4, 4000);
		mySheet.setColumnWidth(5, 6400);
		mySheet.setColumnWidth(6, 4000);
		mySheet.setColumnWidth(7, 4000);
		mySheet.setColumnWidth(8, 4000);
		mySheet.setColumnWidth(9, 4000);
		mySheet.setColumnWidth(10, 4000);
		mySheet.setColumnWidth(11, 4000);
		mySheet.setColumnWidth(12, 4000);
		mySheet.setColumnWidth(13, 4000);
		
		// get the last row number to append new data
		int rownum = mySheet.getLastRowNum();

		XSSFCellStyle csBold = myWorkBook.createCellStyle();
		XSSFFont font = myWorkBook.createFont();
		font.setBold(true);
		csBold.setFont(font);

		XSSFCellStyle csNumber = myWorkBook.createCellStyle();
		XSSFDataFormat dfNumber = myWorkBook.createDataFormat();
		csNumber.setDataFormat(dfNumber.getFormat("#,##0.00"));
		
		XSSFCellStyle csDate = myWorkBook.createCellStyle();
		XSSFDataFormat dfDate = myWorkBook.createDataFormat();
		csDate.setDataFormat(dfDate.getFormat("yyyy-mm-dd"));

		// Set to Iterate and add rows into XLSX file
		boolean printHeader = true;

		double total1 = 0;
		double total2 = 0;
		double total3 = 0;
		
		double grandTotal1 = 0;
		double grandTotal2 = 0;
		double grandTotal3 = 0;

		// Print the report header
		Row titleRow = mySheet.createRow(rownum++);
		Cell tmp = titleRow.createCell(0);
		tmp.setCellValue("HOSPITAL AUTHORITY");

		String[] currentDatetime = DateTimeHelper.formatDateTimeToString(new Date()).split(" ");

		tmp = titleRow.createCell(7);
		tmp.setCellValue("Date:");

		tmp = titleRow.createCell(8);
		tmp.setCellValue(currentDatetime[0]);

		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("MANPOWER POSITION REGISTRY SYSTEM - ESV Details");

		tmp = titleRow.createCell(7);
		tmp.setCellValue("Time:");

		tmp = titleRow.createCell(8);
		tmp.setCellValue(currentDatetime[1]);

		// Empty Row
		rownum++;

		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Report Criteria:");

		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Date As At:");

		tmp = titleRow.createCell(1);
		tmp.setCellValue(dateAsAt);
		
		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Cluster:");

		tmp = titleRow.createCell(1);
		tmp.setCellValue(clusterCode);
		
		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Staff Group:");
		
		tmp = titleRow.createCell(1);
		tmp.setCellValue(staffGroupName);
		

		// Empty Row
		rownum++;

		// Creating header row
		Row headerRow = mySheet.createRow(rownum++);

		for (int i=0; i<listOfRequest.size(); i++) {
			Report3Po post = listOfRequest.get(i);

			if (printHeader) {
				setReport3HeaderRow(headerRow, csBold);
				printHeader = false;
			}
			// Creating data row
			Row row = mySheet.createRow(rownum++);
			setRowReport3(row, csDate, csNumber, post);

			total1 = total1 + post.getTotalFTE();
			total2 = total2 + post.getStrengthFTE();
			total3 = total3 + post.getVacanciesFTE();
			
			grandTotal1 = grandTotal1 + post.getTotalFTE();
			grandTotal2 = grandTotal2 + post.getStrengthFTE();
			grandTotal3 = grandTotal3 + post.getVacanciesFTE();

			if (i != listOfRequest.size() -1) {
				if (!post.getPostTitle().equals(listOfRequest.get(i+1).getPostTitle())) {
					row = mySheet.createRow(rownum++);
					setReport3TotalRow(row, csBold, csNumber, post.getPostTitle(), total1, total2, total3);

					if (!post.getClusterCode().equals(listOfRequest.get(i+1).getClusterCode())) {
						row = mySheet.createRow(rownum++);
						setReport3GrandTotalRow(row, csBold, csNumber, post.getClusterCode(), grandTotal1, grandTotal2, grandTotal3);

						rownum++;
						printHeader = true;
						headerRow = mySheet.createRow(rownum++);
						grandTotal1 = 0;
						grandTotal2 = 0;
						grandTotal3 = 0;
					}
					
					rownum++;
					printHeader = true;
					headerRow = mySheet.createRow(rownum++);
					total1 = 0;
					total2 = 0;
					total3 = 0;
				}
			}
			else if (i == listOfRequest.size()-1) {
				row = mySheet.createRow(rownum++);
				setReport3TotalRow(row, csBold, csNumber, post.getPostTitle(), total1, total2, total3);

				total1 = 0;
				total2 = 0;
				total3 = 0;
				
				row = mySheet.createRow(rownum++);
				setReport3GrandTotalRow(row, csBold, csNumber, post.getClusterCode(), grandTotal1, grandTotal2, grandTotal3);

				rownum++;
				printHeader = true;
				headerRow = mySheet.createRow(rownum++);
				grandTotal1 = 0;
				grandTotal2 = 0;
				grandTotal3 = 0;
			}
		}

		if (listOfHeldAgainst.size() > 0) {
			rownum++;
			rownum++;
			rownum++;

			// Creating header row
			headerRow = mySheet.createRow(rownum++);
			setReport3HeaderRowHeldAgainst(headerRow, csBold);

			// Set to Iterate and add rows into XLSX file
			for (ReportHeldAgainstListPo post : listOfHeldAgainst) {
				// Creating data row
				Row row = mySheet.createRow(rownum++);
				setRowReport3HeldAgainst(row, csNumber, post);
			}
		}

		myWorkBook.write(outputStream);
		myWorkBook.close();

		return outputStream.toByteArray();
	}

	private void setReport3HeaderRow(Row row, XSSFCellStyle styleFont) throws Exception {
		int cellnum = 0;
		Cell tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Cluster");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Hospital");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Post Rank");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Post Duration");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Post End Date");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Post ID");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employee ID");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employee Name");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Generic Rank");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employment Type");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employee Category");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Post FTE");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Strength FTE");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Vacancies FTE");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Remark");
	}

	private void setReport3HeaderRowHeldAgainst(Row row, XSSFCellStyle styleFont) throws Exception {
		int cellnum = 0;
		Cell tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Cluster");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Hospital");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Department");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Post ID");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employee ID");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employee Name");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Generic Rank");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employment Type");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employee Category");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Strength FTE");
	}

	private void setReport3TotalRow(Row row, XSSFCellStyle styleFont, XSSFCellStyle csNumber, String postTtitle, double total1, double total2, double total3) {
		// Add the total row

		int cellnum = 10;

		// Cluster
		Cell cell = row.createCell(cellnum++);
		cell.setCellStyle(styleFont);
		cell.setCellValue(postTtitle + " Subtotal");

		// Post FTE
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total1);

		// Strength FTE
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total2);

		// Vacancies FTE
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total3);
	}
	
	private void setReport3GrandTotalRow(Row row, XSSFCellStyle styleFont, XSSFCellStyle csNumber, String clusterCode, double total1, double total2, double total3) {
		// Add the total row

		int cellnum = 10;

		// Cluster
		Cell cell = row.createCell(cellnum++);
		cell.setCellStyle(styleFont);
		cell.setCellValue(clusterCode + " Total");

		// Post FTE
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total1);

		// Strength FTE
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total2);

		// Vacancies FTE
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total3);
	}

	private void setRowReport3(Row row, XSSFCellStyle csDate, XSSFCellStyle csNumber, Report3Po po) {
		int cellnum = 0;

		Cell cell = row.createCell(cellnum++);
		cell.setCellValue(po.getClusterCode());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getInstCode());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getPostTitle());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getPostDurationType());

		cell = row.createCell(cellnum++);
		cell.setCellStyle(csDate);
		cell.setCellValue(po.getPostEndDate());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getPostId());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getEmployeeId());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getEmployeeName());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getRank());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getEmploymentType());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getEmployeeCategory());

		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getTotalFTE());

		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getStrengthFTE());

		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getVacanciesFTE());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getPostRemark());
	}

	private void setRowReport3HeldAgainst(Row row, XSSFCellStyle csNumber, ReportHeldAgainstListPo po) {
		int cellnum = 0;

		Cell cell = row.createCell(cellnum++);
		cell.setCellValue(po.getClusterCode());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getInstCode());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getDeptName());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getPostId());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getEmployeeId());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getEmployeeName());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getRank());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getEmploymentType());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getEmployeeCategory());

		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getStrengthFTE());
	}
	/**** Generate Excel for report 3 - End ****/

	/**** Generate Excel for report 4 - Start ****/
	public byte[] exportReviewTimeLtdPost(List<Report4Po> listReminder, 
										  List<Report4Po> listOutstanding,
										  String dateAsAt, String clusterCode, String staffGroupName, String rankName) throws Exception {
		// Set ByteArrayOutputStream to store POI data
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		// Finds the workbook instance for XLSX file
		XSSFWorkbook myWorkBook = new XSSFWorkbook();

		// Return first sheet from the XLSX workbook
		XSSFSheet mySheet = myWorkBook.createSheet("ReviewTimeLtdPost");
		mySheet.setColumnWidth(0, 4000);
		mySheet.setColumnWidth(1, 4000);
		mySheet.setColumnWidth(2, 6400);
		mySheet.setColumnWidth(3, 4000);
		mySheet.setColumnWidth(4, 4000);
		mySheet.setColumnWidth(5, 6400);
		mySheet.setColumnWidth(6, 4000);
		mySheet.setColumnWidth(7, 4000);
		mySheet.setColumnWidth(8, 4000);
		mySheet.setColumnWidth(9, 4000);
		mySheet.setColumnWidth(10, 4000);
		mySheet.setColumnWidth(11, 4000);
		mySheet.setColumnWidth(12, 4000);
		mySheet.setColumnWidth(13, 4000);
		mySheet.setColumnWidth(14, 4000);

		// get the last row number to append new data
		int rownum = mySheet.getLastRowNum();

		XSSFCellStyle csBold = myWorkBook.createCellStyle();
		XSSFFont font = myWorkBook.createFont();
		font.setBold(true);
		csBold.setFont(font);

		XSSFCellStyle cs = myWorkBook.createCellStyle();
		XSSFDataFormat df = myWorkBook.createDataFormat();
		cs.setDataFormat(df.getFormat("yyyy-mm-dd"));

		// Set to Iterate and add rows into XLSX file
		boolean printHeader = true;

		double total1 = 0;
		double total2 = 0;
		double total3 = 0;

		// Print the report header
		Row titleRow = mySheet.createRow(rownum++);
		Cell tmp = titleRow.createCell(0);
		tmp.setCellValue("HOSPITAL AUTHORITY");

		String[] currentDatetime = DateTimeHelper.formatDateTimeToString(new Date()).split(" ");

		tmp = titleRow.createCell(7);
		tmp.setCellValue("Date:");

		tmp = titleRow.createCell(8);
		tmp.setCellValue(currentDatetime[0]);
				
		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("MANPOWER POSITION REGISTRY SYSTEM - Review of Time-Limited Posts that are close to effective date");

		tmp = titleRow.createCell(7);
		tmp.setCellValue("Time:");

		tmp = titleRow.createCell(8);
		tmp.setCellValue(currentDatetime[1]);

		// Empty Row
		rownum++;

		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Report Criteria:");

		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Date As At:");

		tmp = titleRow.createCell(1);
		tmp.setCellValue(dateAsAt);
		
		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Cluster:");
		
		tmp = titleRow.createCell(1);
		tmp.setCellValue(clusterCode);
		
		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Staff Group:");
		
		tmp = titleRow.createCell(1);
		tmp.setCellValue(staffGroupName);
		
		// Empty Row
		rownum++;
				
		/**** Time-Limited Posts that are close to effective date ****/
		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellStyle(csBold);
		tmp.setCellValue("Time-Limited Posts that are close to effective date");

		// Creating header row
		Row headerRow = mySheet.createRow(rownum++);
		
		if (listReminder.size() == 0) {
			Cell tmpNoDateFound = headerRow.createCell(0);
			tmpNoDateFound.setCellValue("No Data Found");
		}
		else {	
			// for (Report1Po post : listOfRequest) {
			for (int i=0; i<listReminder.size(); i++) {
				Report4Po post = listReminder.get(i);
	
				if (printHeader) {
					setReport4TimeLimitedPost(headerRow, csBold);
					printHeader = false;
				}
				
				// Creating data row
				Row row = mySheet.createRow(rownum++);
				setRowReport4(row, cs, post);
						
				total1 = total1 + post.getTotalFTE();
				total2 = total2 + post.getStrengthFTE();
				total3 = total3 + post.getVacanciesFTE();
	
				if (i != listReminder.size() -1) {
					if (!(post.getClusterCode().equals(listReminder.get(i+1).getClusterCode())
							&& post.getInstCode().equals(listReminder.get(i+1).getInstCode())
							&& post.getPostTitle().equals(listReminder.get(i+1).getPostTitle()))
							) {
						row = mySheet.createRow(rownum++);
						setReport4TotalRow(row, csBold, post.getPostTitle(), total1, total2, total3);
	
						rownum++;
						printHeader = true;
						headerRow = mySheet.createRow(rownum++);
						total1 = 0;
						total2 = 0;
						total3 = 0;
					}
				}
				else if (i == listReminder.size()-1) {
					row = mySheet.createRow(rownum++);
					setReport4TotalRow(row, csBold, post.getPostTitle(), total1, total2, total3);
	
					total1 = 0;
					total2 = 0;
					total3 = 0;
				}
			}
		}
				
		/**** Outstanding Time-Limited Posts ****/
		rownum++;
		rownum++;
		total1 = 0;
		total2 = 0;
		total3 = 0;
		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellStyle(csBold);
		tmp.setCellValue("Outstanding Time-Limited Posts");
		
		// Creating header row
		headerRow = mySheet.createRow(rownum++);
		
		if (listOutstanding.size() == 0) {
			Cell tmpNoDateFound = headerRow.createCell(0);
			tmpNoDateFound.setCellValue("No Data Found");
		}
		else {				
			for (int i=0; i<listOutstanding.size(); i++) {
				Report4Po post = listOutstanding.get(i);
	
				if (printHeader) {
					setReport4Outstanding(headerRow, csBold);
					printHeader = false;
				}
	
				// Creating data row
				Row row = mySheet.createRow(rownum++);
				setRowReport4(row, cs, post);
	
				total1 = total1 + post.getTotalFTE();
				total2 = total2 + post.getStrengthFTE();
				total3 = total3 + post.getVacanciesFTE();
	
				if (i != listOutstanding.size() -1) {
					if (!(post.getClusterCode().equals(listOutstanding.get(i+1).getClusterCode())
							&& post.getInstCode().equals(listOutstanding.get(i+1).getInstCode())
							&& post.getPostTitle().equals(listOutstanding.get(i+1).getPostTitle()))
							) {
						row = mySheet.createRow(rownum++);
						setReport4TotalRow(row, csBold, post.getPostTitle(), total1, total2, total3);
	
						rownum++;
						printHeader = true;
						headerRow = mySheet.createRow(rownum++);
						total1 = 0;
						total2 = 0;
						total3 = 0;
					}
				}
				else if (i == listOutstanding.size()-1) {
					row = mySheet.createRow(rownum++);
					setReport4TotalRow(row, csBold, post.getPostTitle(), total1, total2, total3);
	
					total1 = 0;
					total2 = 0;
					total3 = 0;
				}
			}
		}
				
		myWorkBook.write(outputStream);
		myWorkBook.close();

		return outputStream.toByteArray();
	}
	
	private void setReport4TimeLimitedPost(Row row, XSSFCellStyle styleFont) throws Exception {
		int cellnum = 0;
		Cell tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Cluster");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Hospital");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Post Rank");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Post Duration");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Post End Date");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Post ID");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employee ID");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employee Name");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Generic Rank");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employment Type");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Contract End Date");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Post FTE");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Strength FTE");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Vacancies FTE");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Remark");
	}
	
	private void setReport4Outstanding(Row row, XSSFCellStyle styleFont) throws Exception {
		int cellnum = 0;
		Cell tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Cluster");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Hospital");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Post Rank");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Post Duration");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Post End Date");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Post ID");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employee ID");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employee Name");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Generic Rank");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employment Type");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Contract End Date");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Post FTE");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Strength FTE");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Vacancies FTE");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Remark");
	}
	
	private void setRowReport4(Row row, XSSFCellStyle cs, Report4Po po) {
		int cellnum = 0;
		
		Cell cell = row.createCell(cellnum++);
		cell.setCellValue(po.getClusterCode());
				
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getInstCode());
		
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getPostTitle());
		
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getPostDurationType());
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(cs);
		cell.setCellValue(po.getPostEndDate());
		
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getPostId());
		
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getEmployeeId());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getEmployeeName());
		
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getRank());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getEmploymentCategory());

		cell = row.createCell(cellnum++);
		cell.setCellStyle(cs);
		cell.setCellValue(po.getContractEndDate());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getTotalFTE());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getStrengthFTE());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getVacanciesFTE());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getPostRemark());
	}

	private void setReport4TotalRow(Row row, XSSFCellStyle styleFont, String postTitle, double total1, double total2, double total3) {
		// Add the total row
		int cellnum = 10;
		
		// Post Rank
		Cell cell = row.createCell(cellnum++);
		cell.setCellStyle(styleFont);
		cell.setCellValue(postTitle + " Subtotal");
		
		// Post FTE
		cell = row.createCell(cellnum++);
		cell.setCellValue(total1);
		
		// Strength FTE
		cell = row.createCell(cellnum++);
		cell.setCellValue(total2);
		
		// Vacancies FTE
		cell = row.createCell(cellnum++);
		cell.setCellValue(total3);
	}
	/**** Generate Excel for report 4 - End ****/

	/**** Generate Excel for report 5 - Start ****/
	public byte[] exportNoOfVacanciesAfterOffset(List<Report5Po> listOfRequest,
												 List<Report5ClusterPo> listOfRequestCluster, 
												 List<ReportHeldAgainstListPo> listOfHeldAgainst,
												 String dateAsAt, String clusterCode, String staffGroupName, String rankName) throws Exception {
		// Set ByteArrayOutputStream to store POI data
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		// Finds the workbook instance for XLSX file
		XSSFWorkbook myWorkBook = new XSSFWorkbook();

		// Return first sheet from the XLSX workbook
		XSSFSheet mySheet = myWorkBook.createSheet("ReviewTimeLtdPost");

		// get the last row number to append new data
		int rownum = mySheet.getLastRowNum();

		XSSFCellStyle csBold = myWorkBook.createCellStyle();
		XSSFFont font = myWorkBook.createFont();
		font.setBold(true);
		csBold.setWrapText(true);
		csBold.setFont(font);

		XSSFCellStyle csDate = myWorkBook.createCellStyle();
		XSSFDataFormat dfDate = myWorkBook.createDataFormat();
		csDate.setDataFormat(dfDate.getFormat("yyyy-mm-dd"));
		
		XSSFCellStyle csNumber = myWorkBook.createCellStyle();
		XSSFDataFormat dfNumber = myWorkBook.createDataFormat();
		csNumber.setDataFormat(dfNumber.getFormat("#,##0.00"));

		// Set to Iterate and add rows into XLSX file
		boolean printHeader = true;

		double total1 = 0;
		double total2 = 0;
		double total3 = 0;

		// Print the report header
		Row titleRow = mySheet.createRow(rownum++);
		Cell tmp = titleRow.createCell(0);
		tmp.setCellValue("HOSPITAL AUTHORITY");

		String[] currentDatetime = DateTimeHelper.formatDateTimeToString(new Date()).split(" ");

		tmp = titleRow.createCell(7);
		tmp.setCellValue("Date:");

		tmp = titleRow.createCell(8);
		tmp.setCellValue(currentDatetime[0]);

		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("MANPOWER POSITION REGISTRY SYSTEM - No. of Vacancies & No of Vacancies after offsetting the Contract Part-time & Temporary Staff");

		tmp = titleRow.createCell(7);
		tmp.setCellValue("Time:");

		tmp = titleRow.createCell(8);
		tmp.setCellValue(currentDatetime[1]);

		// Empty Row
		rownum++;

		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Report Criteria:");

		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Date As At:");

		tmp = titleRow.createCell(1);
		tmp.setCellValue(dateAsAt);
		
		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Cluster:");
		
		tmp = titleRow.createCell(1);
		tmp.setCellValue(clusterCode);
		
		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Staff Group:");
		
		tmp = titleRow.createCell(1);
		tmp.setCellValue(staffGroupName);
		
		// Empty Row
		rownum++;

		/**** By Hospital ****/
		// Creating header row
		Row headerRow = mySheet.createRow(rownum++);

		for (int i=0; i<listOfRequest.size(); i++) {
			Report5Po post = listOfRequest.get(i);

			if (printHeader) {
				Cell headerCell = headerRow.createCell(0);
				headerCell.setCellStyle(csBold);
				headerCell.setCellValue(post.getInstName());
								
				headerRow = mySheet.createRow(rownum++);
				setReport5Header(headerRow, csBold, dateAsAt);
				printHeader = false;
			}

			// Creating data row
			Row row = mySheet.createRow(rownum++);
			setRowReport5(row, csBold, csNumber, post);

			total1 = total1 + post.getVacanciesFTE();
			total2 = total2 + post.getStrengthFTE();
			total3 = total3 + post.getDeficiency();

			if (i != listOfRequest.size() -1) {
				if (!post.getInstCode().equals(listOfRequest.get(i+1).getInstCode())) {
					row = mySheet.createRow(rownum++);
					setReport5TotalRow(row, csBold, csNumber, total1, total2, total3);

					rownum++;
					printHeader = true;
					headerRow = mySheet.createRow(rownum++);
					total1 = 0;
					total2 = 0;
					total3 = 0;
				}
			}
			else if (i == listOfRequest.size()-1) {
				row = mySheet.createRow(rownum++);
				setReport5TotalRow(row, csBold, csNumber, total1, total2, total3);

				total1 = 0;
				total2 = 0;
				total3 = 0;
			}
		}

		/**** By Cluster ****/
		rownum++;
		rownum++;
		total1 = 0;
		total2 = 0;
		total3 = 0;
		printHeader = true;

		// Creating header row
		headerRow = mySheet.createRow(rownum++);
		mySheet.setColumnWidth(0, 6400);
		mySheet.setColumnWidth(1, 6400);
		mySheet.setColumnWidth(2, 6400);
		mySheet.setColumnWidth(3, 6400);
		mySheet.setColumnWidth(4, 6400);
		mySheet.setColumnWidth(5, 6400);

		for (int i=0; i<listOfRequestCluster.size(); i++) {
			Report5ClusterPo post = listOfRequestCluster.get(i);

			if (printHeader) {
				Cell headerCell = headerRow.createCell(0);
				headerCell.setCellStyle(csBold);
				headerCell.setCellValue(post.getClusterName());
								
				headerRow = mySheet.createRow(rownum++);
				setReport5Header(headerRow, csBold, dateAsAt);
				printHeader = false;
			}

			// Creating data row
			Row row = mySheet.createRow(rownum++);
			setRowReport5Cluster(row, csBold, csNumber, post);

			total1 = total1 + post.getTotalVacanciesFte();
			total2 = total2 + post.getTotalStrengthfte();
			total3 = total3 + post.getTotalDeficiency();

			if (i != listOfRequestCluster.size() -1) {
				if (!post.getClusterCode().equals(listOfRequestCluster.get(i+1).getClusterCode())) {
					row = mySheet.createRow(rownum++);
					setReport5TotalRow(row, csBold, csNumber, total1, total2, total3);

					rownum++;
					printHeader = true;
					headerRow = mySheet.createRow(rownum++);
					total1 = 0;
					total2 = 0;
					total3 = 0;
				}
			}
			else if (i == listOfRequestCluster.size()-1) {
				row = mySheet.createRow(rownum++);
				setReport5TotalRow(row, csBold, csNumber, total1, total2, total3);

				total1 = 0;
				total2 = 0;
				total3 = 0;
			}
		}

		if (listOfHeldAgainst.size() > 0) {
			rownum++;
			rownum++;
			rownum++;

			// Creating header row
			headerRow = mySheet.createRow(rownum++);
			setReport5HeaderRowHeldAgainst(headerRow, csBold);

			// Set to Iterate and add rows into XLSX file
			for (ReportHeldAgainstListPo post : listOfHeldAgainst) {
				// Creating data row
				Row row = mySheet.createRow(rownum++);
				setRowReport5HeldAgainst(row, csNumber, post);
			}
		}

		myWorkBook.write(outputStream);
		myWorkBook.close();

		return outputStream.toByteArray();
	}
	
	public byte[] exportNoOfVacanciesAfterOffsetByRank(List<Report5RankPo> listOfRequest,
			   List<Report5RankClusterPo> listOfRequestCluster, 
			   List<ReportHeldAgainstListPo> listOfHeldAgainst,
			   String dateAsAt, String clusterCode, String staffGroupName, String rankName) throws Exception {
		// Set ByteArrayOutputStream to store POI data
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		// Finds the workbook instance for XLSX file
		XSSFWorkbook myWorkBook = new XSSFWorkbook();

		// Return first sheet from the XLSX workbook
		XSSFSheet mySheet = myWorkBook.createSheet("ReviewTimeLtdPost");

		// get the last row number to append new data
		int rownum = mySheet.getLastRowNum();

		XSSFCellStyle csBold = myWorkBook.createCellStyle();
		XSSFFont font = myWorkBook.createFont();
		font.setBold(true);
		csBold.setWrapText(true);
		csBold.setFont(font);

		XSSFCellStyle csDate = myWorkBook.createCellStyle();
		XSSFDataFormat dfDate = myWorkBook.createDataFormat();
		csDate.setDataFormat(dfDate.getFormat("yyyy-mm-dd"));
		
		XSSFCellStyle csNumber = myWorkBook.createCellStyle();
		XSSFDataFormat dfNumber = myWorkBook.createDataFormat();
		csNumber.setDataFormat(dfNumber.getFormat("#,##0.00"));

		// Set to Iterate and add rows into XLSX file
		boolean printHeader = true;

		double total1 = 0;
		double total2 = 0;
		double total3 = 0;
		
		double subTotal1 = 0;
		double subTotal2 = 0;
		double subTotal3 = 0;

		// Print the report header
		Row titleRow = mySheet.createRow(rownum++);
		Cell tmp = titleRow.createCell(0);
		tmp.setCellValue("HOSPITAL AUTHORITY");

		String[] currentDatetime = DateTimeHelper.formatDateTimeToString(new Date()).split(" ");

		tmp = titleRow.createCell(7);
		tmp.setCellValue("Date:");

		tmp = titleRow.createCell(8);
		tmp.setCellValue(currentDatetime[0]);

		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("MANPOWER POSITION REGISTRY SYSTEM - No. of Vacancies & No of Vacancies after offsetting the Contract Part-time & Temporary Staff");

		tmp = titleRow.createCell(7);
		tmp.setCellValue("Time:");

		tmp = titleRow.createCell(8);
		tmp.setCellValue(currentDatetime[1]);

		// Empty Row
		rownum++;

		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Report Criteria:");

		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Date As At:");

		tmp = titleRow.createCell(1);
		tmp.setCellValue(dateAsAt);
		
		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Cluster:");
		
		tmp = titleRow.createCell(1);
		tmp.setCellValue(clusterCode);

		
		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Staff Group:");
		
		tmp = titleRow.createCell(1);
		tmp.setCellValue(staffGroupName);
		
		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Rank:");
		
		tmp = titleRow.createCell(1);
		tmp.setCellValue(rankName);

		// Empty Row
		rownum++;

		/**** By Hospital ****/
		// Creating header row
		Row headerRow = mySheet.createRow(rownum++);

		for (int i=0; i<listOfRequest.size(); i++) {
			Report5RankPo post = listOfRequest.get(i);

			if (printHeader) {
				Cell headerCell = headerRow.createCell(0);
				headerCell.setCellStyle(csBold);
				headerCell.setCellValue(post.getInstName());
								
				headerRow = mySheet.createRow(rownum++);
				setReport5ByRankHeader(headerRow, csBold, dateAsAt);
				printHeader = false;
			}

			// Creating data row
			Row row = mySheet.createRow(rownum++);
			setRowReport5ByRank(row, csBold, csNumber, post);

			total1 = total1 + post.getVacanciesFTE();
			total2 = total2 + post.getStrengthFTE();
			total3 = total3 + post.getDeficiency();
			

			subTotal1 = subTotal1 + post.getVacanciesFTE();
			subTotal2 = subTotal2 + post.getStrengthFTE();
			subTotal3 = subTotal3 + post.getDeficiency();

			if (i != listOfRequest.size() -1) {
				if (!post.getDeptName().equals(listOfRequest.get(i+1).getDeptName()) || !post.getInstCode().equals(listOfRequest.get(i+1).getInstCode())) {
					row = mySheet.createRow(rownum++);
					setReport5ByRankSubTotalRow(row, csBold, csNumber, post.getDeptName(), subTotal1, subTotal2, subTotal3);

					subTotal1 = 0;
					subTotal2 = 0;
					subTotal3 = 0;
				}
				
				if (!post.getInstCode().equals(listOfRequest.get(i+1).getInstCode())) {
					row = mySheet.createRow(rownum++);
					setReport5ByRankTotalRow(row, csBold, csNumber, total1, total2, total3);

					rownum++;
					printHeader = true;
					headerRow = mySheet.createRow(rownum++);
					total1 = 0;
					total2 = 0;
					total3 = 0;
				}
			}
			else if (i == listOfRequest.size()-1) {
				row = mySheet.createRow(rownum++);
				setReport5ByRankSubTotalRow(row, csBold, csNumber, post.getDeptName(), subTotal1, subTotal2, subTotal3);

				row = mySheet.createRow(rownum++);
				setReport5ByRankTotalRow(row, csBold, csNumber, total1, total2, total3);

				total1 = 0;
				total2 = 0;
				total3 = 0;
				
				subTotal1 = 0;
				subTotal2 = 0;
				subTotal3 = 0;
			}
		}

		/**** By Cluster ****/
		rownum++;
		rownum++;
		total1 = 0;
		total2 = 0;
		total3 = 0;
		printHeader = true;

		// Creating header row
		headerRow = mySheet.createRow(rownum++);
		mySheet.setColumnWidth(0, 6400);
		mySheet.setColumnWidth(1, 6400);
		mySheet.setColumnWidth(2, 6400);
		mySheet.setColumnWidth(3, 6400);

		for (int i=0; i<listOfRequestCluster.size(); i++) {
			Report5RankClusterPo post = listOfRequestCluster.get(i);

			if (printHeader) {
				Cell headerCell = headerRow.createCell(0);
				headerCell.setCellStyle(csBold);
				headerCell.setCellValue(post.getClusterName());
								
				headerRow = mySheet.createRow(rownum++);
				setReport5ByRankHeader(headerRow, csBold, dateAsAt);
				printHeader = false;
			}

			// Creating data row
			Row row = mySheet.createRow(rownum++);
			setRowReport5ByRankCluster(row, csBold, csNumber, post);

			total1 = total1 + post.getTotalVacanciesFte();
			total2 = total2 + post.getTotalStrengthfte();
			total3 = total3 + post.getTotalDeficiency();

			if (i != listOfRequestCluster.size() -1) {
				if (!post.getClusterCode().equals(listOfRequestCluster.get(i+1).getClusterCode())) {
					row = mySheet.createRow(rownum++);
					setReport5ByRankTotalRow(row, csBold, csNumber, total1, total2, total3);

					rownum++;
					printHeader = true;
					headerRow = mySheet.createRow(rownum++);
					total1 = 0;
					total2 = 0;
					total3 = 0;
				}
			}
			else if (i == listOfRequestCluster.size()-1) {
				row = mySheet.createRow(rownum++);
				setReport5ByRankTotalRow(row, csBold, csNumber, total1, total2, total3);

				total1 = 0;
				total2 = 0;
				total3 = 0;
			}
		}

		if (listOfHeldAgainst.size() > 0) {
			rownum++;
			rownum++;
			rownum++;

			// Creating header row
			headerRow = mySheet.createRow(rownum++);
			setReport5HeaderRowHeldAgainst(headerRow, csBold);

			// Set to Iterate and add rows into XLSX file
			for (ReportHeldAgainstListPo post : listOfHeldAgainst) {
				// Creating data row
				Row row = mySheet.createRow(rownum++);
				setRowReport5HeldAgainst(row, csNumber, post);
			}
		}

		myWorkBook.write(outputStream);
		myWorkBook.close();

		return outputStream.toByteArray();
	}
	
	private void setReport5Header(Row row, XSSFCellStyle styleFont, String asAtDate) throws Exception {
		int cellnum = 0;
		Cell tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Grade");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Vacancies as at " + asAtDate + " (excl Temp Post)");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("CPT & TEMP Staff as at " + asAtDate + " (FTE)");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Vacancies after Offset the Temp Post");
	}
	
	private void setReport5ByRankHeader(Row row, XSSFCellStyle styleFont, String asAtDate) throws Exception {
		int cellnum = 0;
		Cell tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Grade");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Department");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Rank");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Vacancies as at " + asAtDate + " (excl Temp Post)");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("CPT & TEMP Staff as at " + asAtDate + " (FTE)");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Vacancies after Offset the Temp Post");
	}
	
	private void setRowReport5(Row row, XSSFCellStyle csBold, XSSFCellStyle csNumber, Report5Po po) {
		int cellnum = 0;
		
		Cell cell = row.createCell(cellnum++);
		cell.setCellStyle(csBold);
		cell.setCellValue(po.getStaffGroupName());
				
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getVacanciesFTE());
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getStrengthFTE());
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getDeficiency());
	}		
	
	private void setRowReport5ByRank(Row row, XSSFCellStyle csBold, XSSFCellStyle csNumber, Report5RankPo po) {
		int cellnum = 0;
		
		Cell cell = row.createCell(cellnum++);
		cell.setCellStyle(csBold);
		cell.setCellValue(po.getStaffGroupName());
				
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getDeptName());
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getRankName());
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getVacanciesFTE());
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getStrengthFTE());
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getDeficiency());
	}		
	
	private void setRowReport5Cluster(Row row, XSSFCellStyle csBold, XSSFCellStyle csNumber, Report5ClusterPo po) {
		int cellnum = 0;
		
		Cell cell = row.createCell(cellnum++);
		cell.setCellStyle(csBold);
		cell.setCellValue(po.getStaffGroupName());
				
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getTotalVacanciesFte());
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getTotalStrengthfte());
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getTotalDeficiency());
	}	
	
	private void setRowReport5ByRankCluster(Row row, XSSFCellStyle csBold, XSSFCellStyle csNumber, Report5RankClusterPo po) {
		int cellnum = 0;
		
		Cell cell = row.createCell(cellnum++);
		cell.setCellStyle(csBold);
		cell.setCellValue(po.getStaffGroupName());
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csBold);
		cell.setCellValue(po.getDeptName());
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csBold);
		cell.setCellValue(po.getRankName());
				
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getTotalVacanciesFte());
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getTotalStrengthfte());
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getTotalDeficiency());
	}		
	
	private void setReport5TotalRow(Row row, XSSFCellStyle csBold, XSSFCellStyle csNumber, double total1, double total2, double total3) {
		int cellnum = 0;
		
		Cell cell = row.createCell(cellnum);
		cell.setCellStyle(csBold);
		cell.setCellValue("Total");
		
		cellnum++;
				
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total1);
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total2);
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total3);
	}		
	
	private void setReport5ByRankSubTotalRow(Row row, XSSFCellStyle csBold, XSSFCellStyle csNumber, String deptName, double total1, double total2, double total3) {
		int cellnum = 0;
		
		Cell cell = row.createCell(cellnum++);
		cell = row.createCell(cellnum++);

		cell.setCellStyle(csNumber);
		cell.setCellStyle(csBold);
		cell.setCellValue(deptName + " Sub-Total");
				
		cellnum++;
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total1);
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total2);
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total3);
	}		
	
	private void setReport5ByRankTotalRow(Row row, XSSFCellStyle csBold, XSSFCellStyle csNumber, double total1, double total2, double total3) {
		int cellnum = 0;
		
		Cell cell = row.createCell(cellnum++);
		cell.setCellStyle(csBold);
		cell.setCellValue("Total");
		
		cellnum++;
		cellnum++;
				
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total1);
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total2);
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(total3);
	}		
	
	private void setReport5HeaderRowHeldAgainst(Row row, XSSFCellStyle styleFont) throws Exception {
		int cellnum = 0;
		Cell tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Cluster");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Hospital");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Post ID");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employee ID");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employee Name");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Generic Rank");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employment Type");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employee Category");

		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Strength FTE");
	}

	private void setRowReport5HeldAgainst(Row row, XSSFCellStyle csNumber, ReportHeldAgainstListPo po) {
		int cellnum = 0;

		Cell cell = row.createCell(cellnum++);
		cell.setCellValue(po.getClusterCode());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getInstCode());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getPostId());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getEmployeeId());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getEmployeeName());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getRank());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getEmploymentType());

		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getEmployeeCategory());

		cell = row.createCell(cellnum++);
		cell.setCellStyle(csNumber);
		cell.setCellValue(po.getStrengthFTE());
	}
	/**** Generate Excel for report 5 - End ****/

	/**** Generate Excel for report 6 - Start ****/
	public byte[] exportClosedPostOccupied(List<Report6Po> listOfRequest, String dateAsAt, String clusterCode, String staffGroupName, String rankName) throws Exception {
		// Set ByteArrayOutputStream to store POI data
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		// Finds the workbook instance for XLSX file
		XSSFWorkbook myWorkBook = new XSSFWorkbook();

		// Return first sheet from the XLSX workbook
		XSSFSheet mySheet = myWorkBook.createSheet("ClosedPostOccupied");
		mySheet.setColumnWidth(0, 6400);
		mySheet.setColumnWidth(1, 4000);
		mySheet.setColumnWidth(2, 4000);
		mySheet.setColumnWidth(3, 4000);
		mySheet.setColumnWidth(4, 4000);
		mySheet.setColumnWidth(5, 4000);
		mySheet.setColumnWidth(6, 4000);

		// get the last row number to append new data
		int rownum = mySheet.getLastRowNum();

		XSSFCellStyle csBold = myWorkBook.createCellStyle();
		XSSFFont font = myWorkBook.createFont();
		font.setBold(true);
		csBold.setFont(font);

		XSSFCellStyle cs = myWorkBook.createCellStyle();
		XSSFDataFormat df = myWorkBook.createDataFormat();
		cs.setDataFormat(df.getFormat("yyyy-mm-dd"));

		// Set to Iterate and add rows into XLSX file
		boolean printHeader = true;
				
		// Print the report header
		Row titleRow = mySheet.createRow(rownum++);
		Cell tmp = titleRow.createCell(0);
		tmp.setCellValue("HOSPITAL AUTHORITY");

		String[] currentDatetime = DateTimeHelper.formatDateTimeToString(new Date()).split(" ");

		tmp = titleRow.createCell(7);
		tmp.setCellValue("Date:");

		tmp = titleRow.createCell(8);
		tmp.setCellValue(currentDatetime[0]);

		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("MANPOWER POSITION REGISTRY SYSTEM - Closed and Frozen Post being occupied");

		tmp = titleRow.createCell(7);
		tmp.setCellValue("Time:");

		tmp = titleRow.createCell(8);
		tmp.setCellValue(currentDatetime[1]);

		// Empty Row
		rownum++;

		// Report Criteria
		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Report Criteria:");

		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Date As At:");
		
		tmp = titleRow.createCell(1);
		tmp.setCellValue(dateAsAt);
		
		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Cluster:");
		
		tmp = titleRow.createCell(1);
		tmp.setCellValue(clusterCode);
		
		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Staff Group:");
		
		tmp = titleRow.createCell(1);
		tmp.setCellValue(staffGroupName);
		
		// Empty Row
		rownum++;

		// Creating header row
		Row headerRow = mySheet.createRow(rownum++);
						
		for (int i=0; i<listOfRequest.size(); i++) {
			Report6Po post = listOfRequest.get(i);
					
			if (printHeader) {
				setReport6HeaderRow(headerRow, csBold);
				printHeader = false;
			}
			
			// Creating data row
			Row row = mySheet.createRow(rownum++);
			setRowReport6(row, cs, post);
					
		}				
		
		myWorkBook.write(outputStream);
		myWorkBook.close();
				
		return outputStream.toByteArray();
	}
	
	private void setReport6HeaderRow(Row row, XSSFCellStyle styleFont) throws Exception {
		int cellnum = 0;
		Cell tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Post ID");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Post Status");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Post End Date");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employee ID");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employee Name");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Generic Rank");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employment Type");
	}
	
	private void setRowReport6(Row row, XSSFCellStyle cs, Report6Po po) {
		int cellnum = 0;
		
		Cell cell = row.createCell(cellnum++);
		cell.setCellValue(po.getPostId());
				
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getPostStatusDesc());
		
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getPostEndDate());
		
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getEmployeeId());
		
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getEmployeeName());
		
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getRank());
		
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getEmploymentCategory());
	}
	/**** Generate Excel for report 6 - End ****/

	/**** Generate Excel for report 7 - Start ****/
	public byte[] exportDiscrepanciesOnHCMPost(List<Report7Po> listOfRequest, String dateAsAt, String clusterCode, String staffGroupName, String rankName) throws Exception {
		// Set ByteArrayOutputStream to store POI data
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		// Finds the workbook instance for XLSX file
		XSSFWorkbook myWorkBook = new XSSFWorkbook();

		// Return first sheet from the XLSX workbook
		XSSFSheet mySheet = myWorkBook.createSheet("DiscrepanciesOnHCMPost");	
		mySheet.setColumnWidth(0, 6400);
		mySheet.setColumnWidth(1, 6400);
		mySheet.setColumnWidth(2, 4000);
		mySheet.setColumnWidth(3, 4000);
		mySheet.setColumnWidth(4, 4000);
		mySheet.setColumnWidth(5, 6400);

		// get the last row number to append new data
		int rownum = mySheet.getLastRowNum();

		XSSFCellStyle csBold = myWorkBook.createCellStyle();
		XSSFFont font = myWorkBook.createFont();
		font.setBold(true);
		csBold.setFont(font);

		XSSFCellStyle cs = myWorkBook.createCellStyle();
		XSSFDataFormat df = myWorkBook.createDataFormat();
		cs.setDataFormat(df.getFormat("yyyy-mm-dd"));

		// Set to Iterate and add rows into XLSX file
		boolean printHeader = true;

		// Print the report header
		Row titleRow = mySheet.createRow(rownum++);
		Cell tmp = titleRow.createCell(0);
		tmp.setCellValue("HOSPITAL AUTHORITY");

		String[] currentDatetime = DateTimeHelper.formatDateTimeToString(new Date()).split(" ");

		tmp = titleRow.createCell(7);
		tmp.setCellValue("Date:");

		tmp = titleRow.createCell(8);
		tmp.setCellValue(currentDatetime[0]);

		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("MANPOWER POSITION REGISTRY SYSTEM - Discrepancies on HCM Position of Post ID and Employee");

		tmp = titleRow.createCell(7);
		tmp.setCellValue("Time:");

		tmp = titleRow.createCell(8);
		tmp.setCellValue(currentDatetime[1]);

		// Empty Row
		rownum++;

		// Report Criteria
		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Report Criteria:");

		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Date As At:");

		tmp = titleRow.createCell(1);
		tmp.setCellValue(dateAsAt);
		
		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Cluster:");
		
		tmp = titleRow.createCell(1);
		tmp.setCellValue(clusterCode);
		
		titleRow = mySheet.createRow(rownum++);
		tmp = titleRow.createCell(0);
		tmp.setCellValue("Staff Group:");
		
		tmp = titleRow.createCell(1);
		tmp.setCellValue(staffGroupName);

		// Empty Row
		rownum++;

		// Creating header row
		Row headerRow = mySheet.createRow(rownum++);

		for (int i=0; i<listOfRequest.size(); i++) {
			Report7Po post = listOfRequest.get(i);

			if (printHeader) {
				setReport7HeaderRow(headerRow, csBold);
				printHeader = false;
			}

			// Creating data row
			Row row = mySheet.createRow(rownum++);
			setRowReport7(row, cs, post);

		}				

		myWorkBook.write(outputStream);
		myWorkBook.close();

		return outputStream.toByteArray();
	}

	
	private void setReport7HeaderRow(Row row, XSSFCellStyle styleFont) throws Exception {
		int cellnum = 0;
		Cell tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Post ID");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("HCM Position of Post ID");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employee ID");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Employee Name");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("Generic Rank");
		
		tmp = row.createCell(cellnum++);
		tmp.setCellStyle(styleFont);
		tmp.setCellValue("HCM Position of Employee");
	}
	
	private void setRowReport7(Row row, XSSFCellStyle cs, Report7Po po) {
		int cellnum = 0;
		
		Cell cell = row.createCell(cellnum++);
		cell.setCellValue(po.getPostId());
				
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getPostHcmPositionName());
		
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getEmployeeId());
		
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getEmployeeName());
		
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getRank());
		
		cell = row.createCell(cellnum++);
		cell.setCellValue(po.getHcmHcmPositionName());
	}
	/**** Generate Excel for report 7 - End ****/
}

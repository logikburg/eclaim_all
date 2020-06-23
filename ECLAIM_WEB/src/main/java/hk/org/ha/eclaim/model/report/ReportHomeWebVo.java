package hk.org.ha.eclaim.model.report;

import java.util.List;

import hk.org.ha.eclaim.bs.report.constant.ReportConstant;

public class ReportHomeWebVo {
	private String userName;
	private List<String> funcList;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<String> getFuncList() {
		return funcList;
	}

	public void setFuncList(List<String> funcList) {
		this.funcList = funcList;
	}
	
	public String getReportESVByHospRank() {
		if (funcList != null) {
			return funcList.contains(ReportConstant.REPORT_01_ESV_HOSP_RANK) ? "Y" : "N";
		}
		
		return "N";
	}
	
	public String getReportESVByDeptRank() {
		if (funcList != null) {
			return funcList.contains(ReportConstant.REPORT_02_ESV_DEPT_RANK) ? "Y" : "N";
		}
		
		return "N";
	}
	
	public String getReportESVDetail() {
		if (funcList != null) {
			return funcList.contains(ReportConstant.REPORT_03_ESV_DTL) ? "Y" : "N";
		}
		
		return "N";
	}
	
	public String getReportReviewTimeLtdPost() {
		if (funcList != null) {
			return funcList.contains(ReportConstant.REPORT_04_TIME_LIMITED) ? "Y" : "N";
		}
		
		return "N";
	}
	
	public String getReportNoOfVacanciesAfterOffset() {
		if (funcList != null) {
			return funcList.contains(ReportConstant.REPORT_05_VACANCIES) ? "Y" : "N";
		}
		
		return "N";
	}
	
	public String getClosedPostOccupied() {
		if (funcList != null) {
			return funcList.contains(ReportConstant.REPORT_06_CLOSED_OCCUPIED) ? "Y" : "N";
		}
		
		return "N";
	}
	
	public String getDiscrepanciesOnHCMPost() {
		if (funcList != null) {
			return funcList.contains(ReportConstant.REPORT_07_HCM_POS_DISCREP) ? "Y" : "N";
		}
		
		return "N";
	}
	
	public String getDataExport() {
		if (funcList != null) {
			return funcList.contains(ReportConstant.REPORT_DATA_EXPORT) ? "Y" : "N";
		}
		
		return "N";
	}
	
	public String getTransExport() {
		if (funcList != null) {
			return funcList.contains(ReportConstant.REPORT_TRANS_EXPORT) ? "Y" : "N";
		}
		
		return "N";
	}
}

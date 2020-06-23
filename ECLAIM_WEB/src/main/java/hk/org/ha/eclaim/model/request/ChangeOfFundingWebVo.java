package hk.org.ha.eclaim.model.request;

import java.util.ArrayList;
import java.util.List;

import hk.org.ha.eclaim.bs.request.po.RequestPostPo;

public class ChangeOfFundingWebVo extends CommonWebVo {
	private String withMassSave = "N";
	
	private String userName;

	private String requestNo;
	private String requestId;
	private String requester;
	private String requestStatus;
	private String requestReason;
	private String effectiveDate;	// Added for UT29939

	private List<RequestPostPo> requestPositionList = new ArrayList<RequestPostPo>();
	private String lastUpdateDate;

	//Funding
	private String annualPlanInd;
	private String programYear;
	private String programTypeCode;
	private String programName;
	private String fundSrcId;
	private String fundSrcSubCatId;
	private String fundSrcStartDate;
	private String fundSrcEndDate;
	private String fundSrcFte;
	private String fundSrcRemark;
	private String inst;
	private String section;
	private String analytical;
	private String programCode;

	private String updateSuccess;
	private String displayMessage;
	private String formAction;
	private String programType;

	private List<String> requestPostNo = new ArrayList<String>();
	private List<String> requestAnnualPlanInd = new ArrayList<String>();
	private List<String> requestProgramYear = new ArrayList<String>();
	private List<String> requestProgramCode = new ArrayList<String>();
	private List<String> requestProgramName = new ArrayList<String>();
	private List<String> requestProgramRemark = new ArrayList<String>();
	private List<String> requestProgramType = new ArrayList<String>();
	private List<String> requestFundSrc1st = new ArrayList<String>();
	private List<String> requestFundSrc1stStartDate = new ArrayList<String>();
	private List<String> requestFundSrc1stEndDate = new ArrayList<String>();
	private List<String> requestFundSrc1stFTE = new ArrayList<String>();
	private List<String> requestFundRemark = new ArrayList<String>();
		
	private String canEditDetailInfo;
	private String canEditFinancialInfo;
	
	private String hoBuyServiceInd;	
	
	public String getRequestNo() {
		return requestNo;
	}
	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getRequester() {
		return requester;
	}
	public void setRequester(String requester) {
		this.requester = requester;
	}
	public String getRequestStatus() {
		return requestStatus;
	}
	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}
	public String getRequestReason() {
		return requestReason;
	}
	public void setRequestReason(String requestReason) {
		this.requestReason = requestReason;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUpdateSuccess() {
		return updateSuccess;
	}
	public void setUpdateSuccess(String updateSuccess) {
		this.updateSuccess = updateSuccess;
	}
	public String getDisplayMessage() {
		return displayMessage;
	}
	public void setDisplayMessage(String displayMessage) {
		this.displayMessage = displayMessage;
	}
	public String getFormAction() {
		return formAction;
	}
	public void setFormAction(String formAction) {
		this.formAction = formAction;
	}
	public List<RequestPostPo> getRequestPositionList() {
		return requestPositionList;
	}
	public void setRequestPositionList(List<RequestPostPo> requestPositionList) {
		this.requestPositionList = requestPositionList;
	}
	public String getWithMassSave() {
		return withMassSave;
	}
	public void setWithMassSave(String withMassSave) {
		this.withMassSave = withMassSave;
	}
	public List<String> getRequestPostNo() {
		return requestPostNo;
	}
	public void setRequestPostNo(List<String> requestPostNo) {
		this.requestPostNo = requestPostNo;
	}
	public List<String> getRequestAnnualPlanInd() {
		return requestAnnualPlanInd;
	}
	public void setRequestAnnualPlanInd(List<String> requestAnnualPlanInd) {
		this.requestAnnualPlanInd = requestAnnualPlanInd;
	}
	public List<String> getRequestProgramYear() {
		return requestProgramYear;
	}
	public void setRequestProgramYear(List<String> requestProgramYear) {
		this.requestProgramYear = requestProgramYear;
	}
	public List<String> getRequestProgramCode() {
		return requestProgramCode;
	}
	public void setRequestProgramCode(List<String> requestProgramCode) {
		this.requestProgramCode = requestProgramCode;
	}
	public List<String> getRequestProgramName() {
		return requestProgramName;
	}
	public void setRequestProgramName(List<String> requestProgramName) {
		this.requestProgramName = requestProgramName;
	}
	public List<String> getRequestProgramRemark() {
		return requestProgramRemark;
	}
	public void setRequestProgramRemark(List<String> requestProgramRemark) {
		this.requestProgramRemark = requestProgramRemark;
	}
	public List<String> getRequestFundSrc1st() {
		return requestFundSrc1st;
	}
	public void setRequestFundSrc1st(List<String> requestFundSrc1st) {
		this.requestFundSrc1st = requestFundSrc1st;
	}
	public List<String> getRequestFundSrc1stStartDate() {
		return requestFundSrc1stStartDate;
	}
	public void setRequestFundSrc1stStartDate(List<String> requestFundSrc1stStartDate) {
		this.requestFundSrc1stStartDate = requestFundSrc1stStartDate;
	}
	public List<String> getRequestFundSrc1stEndDate() {
		return requestFundSrc1stEndDate;
	}
	public void setRequestFundSrc1stEndDate(List<String> requestFundSrc1stEndDate) {
		this.requestFundSrc1stEndDate = requestFundSrc1stEndDate;
	}
	public List<String> getRequestFundRemark() {
		return requestFundRemark;
	}
	public void setRequestFundRemark(List<String> requestFundRemark) {
		this.requestFundRemark = requestFundRemark;
	}
	public String getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public String getCanEditDetailInfo() {
		return canEditDetailInfo;
	}
	public void setCanEditDetailInfo(String canEditDetailInfo) {
		this.canEditDetailInfo = canEditDetailInfo;
	}
	public String getCanEditFinancialInfo() {
		return canEditFinancialInfo;
	}
	public void setCanEditFinancialInfo(String canEditFinancialInfo) {
		this.canEditFinancialInfo = canEditFinancialInfo;
	}
	public List<String> getRequestFundSrc1stFTE() {
		return requestFundSrc1stFTE;
	}
	public void setRequestFundSrc1stFTE(List<String> requestFundSrc1stFTE) {
		this.requestFundSrc1stFTE = requestFundSrc1stFTE;
	}
	public String getProgramType() {
		return programType;
	}
	public void setProgramType(String programType) {
		this.programType = programType;
	}
	public List<String> getRequestProgramType() {
		return requestProgramType;
	}
	public void setRequestProgramType(List<String> requestProgramType) {
		this.requestProgramType = requestProgramType;
	}
	public String getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public String getHoBuyServiceInd() {
		return hoBuyServiceInd;
	}
	public void setHoBuyServiceInd(String hoBuyServiceInd) {
		this.hoBuyServiceInd = hoBuyServiceInd;
	}
	public String getAnnualPlanInd() {
		return annualPlanInd;
	}
	public void setAnnualPlanInd(String annualPlanInd) {
		this.annualPlanInd = annualPlanInd;
	}
	public String getProgramYear() {
		return programYear;
	}
	public void setProgramYear(String programYear) {
		this.programYear = programYear;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public String getFundSrcId() {
		return fundSrcId;
	}
	public void setFundSrcId(String fundSrcId) {
		this.fundSrcId = fundSrcId;
	}
	public String getFundSrcSubCatId() {
		return fundSrcSubCatId;
	}
	public void setFundSrcSubCatId(String fundSrcSubCatId) {
		this.fundSrcSubCatId = fundSrcSubCatId;
	}
	public String getFundSrcStartDate() {
		return fundSrcStartDate;
	}
	public void setFundSrcStartDate(String fundSrcStartDate) {
		this.fundSrcStartDate = fundSrcStartDate;
	}
	public String getFundSrcEndDate() {
		return fundSrcEndDate;
	}
	public void setFundSrcEndDate(String fundSrcEndDate) {
		this.fundSrcEndDate = fundSrcEndDate;
	}
	public String getFundSrcFte() {
		return fundSrcFte;
	}
	public void setFundSrcFte(String fundSrcFte) {
		this.fundSrcFte = fundSrcFte;
	}
	public String getFundSrcRemark() {
		return fundSrcRemark;
	}
	public void setFundSrcRemark(String fundSrcRemark) {
		this.fundSrcRemark = fundSrcRemark;
	}
	public String getInst() {
		return inst;
	}
	public void setInst(String inst) {
		this.inst = inst;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getAnalytical() {
		return analytical;
	}
	public void setAnalytical(String analytical) {
		this.analytical = analytical;
	}
	public String getProgramTypeCode() {
		return programTypeCode;
	}
	public void setProgramTypeCode(String programTypeCode) {
		this.programTypeCode = programTypeCode;
	}
	public String getProgramCode() {
		return programCode;
	}
	public void setProgramCode(String programCode) {
		this.programCode = programCode;
	}
}

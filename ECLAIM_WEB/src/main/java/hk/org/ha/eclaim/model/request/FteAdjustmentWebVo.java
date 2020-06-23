package hk.org.ha.eclaim.model.request;

import java.util.ArrayList;
import java.util.List;

import hk.org.ha.eclaim.bs.request.po.RequestPostPo;

public class FteAdjustmentWebVo extends CommonWebVo{
	private String withMassSave = "N";
	
	private String userName;

	private String requestNo;
	private String requestId;
	private String requester;
	private String requestStatus;
	private String requestReason;
	private String effectiveDate;
	
	private String requestDurationValue;
	private String requestDurationUnit;
	private String requestDurationType;

	private List<RequestPostPo> requestPositionList = new ArrayList<RequestPostPo>();
	private String lastUpdateDate;
	
	private String requestStartDate;
	private String requestEndDate;

	//Funding
	private String annualPlanInd;
	private String programYear;
	private String programCode;
	private String programName;
	//private String programRemark;
	private String fundSrcId;
	private String fundSrcSubCatId;
	private String fundSrcStartDate;
	private String fundSrcEndDate;
	private String fundSrcFte;
	private String fundSrcRemark;
	private String inst;
	private String section;
	private String analytical;
	private String programTypeCode;

	private String updateSuccess;
	private String displayMessage;
	private String formAction;
	private String programType;

	private List<String> requestPostNo = new ArrayList<String>();
	private List<String> requestNewFTE = new ArrayList<String>();
	
	private String canEditDetailInfo;
	private String canEditFinancialInfo;
	
	private String hoBuyServiceInd;
	private String lastUpdatedByLoginUser;
	
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
	public String getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public String getRequestStartDate() {
		return requestStartDate;
	}
	public void setRequestStartDate(String requestStartDate) {
		this.requestStartDate = requestStartDate;
	}
	public String getRequestEndDate() {
		return requestEndDate;
	}
	public void setRequestEndDate(String requestEndDate) {
		this.requestEndDate = requestEndDate;
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
	public String getProgramType() {
		return programType;
	}
	public void setProgramType(String programType) {
		this.programType = programType;
	}
	public String getRequestDurationValue() {
		return requestDurationValue;
	}
	public void setRequestDurationValue(String requestDurationValue) {
		this.requestDurationValue = requestDurationValue;
	}
	public String getRequestDurationUnit() {
		return requestDurationUnit;
	}
	public void setRequestDurationUnit(String requestDurationUnit) {
		this.requestDurationUnit = requestDurationUnit;
	}
	public String getRequestDurationType() {
		return requestDurationType;
	}
	public void setRequestDurationType(String requestDurationType) {
		this.requestDurationType = requestDurationType;
	}
	public List<String> getRequestNewFTE() {
		return requestNewFTE;
	}
	public void setRequestNewFTE(List<String> requestNewFTE) {
		this.requestNewFTE = requestNewFTE;
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
	public String getProgramCode() {
		return programCode;
	}
	public void setProgramCode(String programCode) {
		this.programCode = programCode;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	/*public String getProgramRemark() {
		return programRemark;
	}
	public void setProgramRemark(String programRemark) {
		this.programRemark = programRemark;
	}*/
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
	public String getHoBuyServiceInd() {
		return hoBuyServiceInd;
	}
	public void setHoBuyServiceInd(String hoBuyServiceInd) {
		this.hoBuyServiceInd = hoBuyServiceInd;
	}
	public String getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public String getLastUpdatedByLoginUser() {
		return lastUpdatedByLoginUser;
	}
	public void setLastUpdatedByLoginUser(String lastUpdatedByLoginUser) {
		this.lastUpdatedByLoginUser = lastUpdatedByLoginUser;
	}
}

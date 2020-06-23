package hk.org.ha.eclaim.model.hcm;

public class HCMRecordGradeVo {
	private String gradeUid;
	private String gradeId;
	private String gradeDesc;
	private String positionId;
	private String dateFrom;
	private String dateTo;
	private String versionNumber;
	
	public String getGradeUid() {
		return gradeUid;
	}
	
	public void setGradeUid(String gradeUid) {
		this.gradeUid = gradeUid;
	}

	public String getGradeId() {
		return gradeId;
	}
	
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getGradeDesc() {
		return gradeDesc;
	}

	public void setGradeDesc(String gradeDesc) {
		this.gradeDesc = gradeDesc;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}
}

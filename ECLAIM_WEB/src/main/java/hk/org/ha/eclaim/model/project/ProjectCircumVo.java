package hk.org.ha.eclaim.model.project;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class ProjectCircumVo extends ProjectBaseVo{
	
	private String justifications;
	private String triggerPoint;
	private String manPowerShortage;
	private String qDeliver;
	private String usingOTA;
	private String otaJustifications;
	private String[] circumIds;
	private MultipartFile documentFile;
	private String documentFileName;
	
	private List<String> uploadFileId;
	
	public String getJustifications() {
		return justifications;
	}
	public void setJustifications(String justifications) {
		this.justifications = justifications;
	}
	public String getTriggerPoint() {
		return triggerPoint;
	}
	public void setTriggerPoint(String triggerPoint) {
		this.triggerPoint = triggerPoint;
	}
	public String getManPowerShortage() {
		return manPowerShortage;
	}
	public void setManPowerShortage(String manPowerShortage) {
		this.manPowerShortage = manPowerShortage;
	}
	public String[] getCircumIds() {
		return circumIds;
	}
	public void setCircumIds(String[] circumIds) {
		this.circumIds = circumIds;
	}
	public MultipartFile getDocumentFile() {
		return documentFile;
	}
	public void setDocumentFile(MultipartFile documentFile) {
		this.documentFile = documentFile;
	}
	public String getDocumentFileName() {
		return documentFileName;
	}
	public void setDocumentFileName(String documentFileName) {
		this.documentFileName = documentFileName;
	}
	public String getCircumIdinString() {
		return circumIds != null ? Arrays.toString(circumIds) : "";
	}
	public List<String> getUploadFileId() {
		return uploadFileId;
	}
	public void setUploadFileId(List<String> uploadFileId) {
		this.uploadFileId = uploadFileId;
	}
	public String getqDeliver() {
		return qDeliver;
	}
	public void setqDeliver(String qDeliver) {
		this.qDeliver = qDeliver;
	}
	public String getUsingOTA() {
		return usingOTA;
	}
	public void setUsingOTA(String usingOTA) {
		this.usingOTA = usingOTA;
	}
	public String getOtaJustifications() {
		return otaJustifications;
	}
	public void setOtaJustifications(String otaJustifications) {
		this.otaJustifications = otaJustifications;
	}
}

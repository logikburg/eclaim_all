package hk.org.ha.eclaim.model.project;

import java.util.List;

public class ProjectJobDetailsVo extends ProjectBaseVo{

	private List<ProjectJobVo> jobList;
	private String usingOTA;
	private String otaJustifications;
	
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
	public List<ProjectJobVo> getJobList() {
		return jobList;
	}
	public void setJobList(List<ProjectJobVo> jobList) {
		this.jobList = jobList;
	}
	
}

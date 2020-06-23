package hk.org.ha.eclaim.model.maintenance;

import java.util.Date;

public class NewsWebVo {
	private int newsUid;
	private String newsDesc;
	private Date effectiveFrom;
	private Date effectiveTo;
	
	public int getNewsUid() {
		return newsUid;
	}

	public void setNewsUid(int newsUid) {
		this.newsUid = newsUid;
	}

	public String getNewsDesc() {
		return newsDesc;
	}

	public void setNewsDesc(String newsDesc) {
		this.newsDesc = newsDesc;
	}

	public Date getEffectiveFrom() {
		return effectiveFrom;
	}

	public void setEffectiveFrom(Date effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public Date getEffectiveTo() {
		return effectiveTo;
	}

	public void setEffectiveTo(Date effectiveTo) {
		this.effectiveTo = effectiveTo;
	}
}

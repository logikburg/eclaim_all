package hk.org.ha.eclaim.bs.maintenance.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="CS_NEWS")
public class NewsPo extends AbstractBasePo {
	
	private static final long serialVersionUID = 6442791703209677764L;

	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CS_NEWS_UID_SEQ")
	@SequenceGenerator(name="CS_NEWS_UID_SEQ", sequenceName="CS_NEWS_UID_SEQ", allocationSize=1)
	@Id
	@Column(name="NEWS_UID")
	private Integer newsUid;
	
	@Column(name="NEWS_DESC")
	private String newsDesc;
	
	@Temporal(TemporalType.DATE)
	@Column(name="EFFECTIVE_START_DATE")
	private Date effectiveFrom;
	
	@Temporal(TemporalType.DATE)
	@Column(name="EFFECTIVE_END_DATE")
	private Date effectiveTo;
	
	public Integer getNewsUid() {
		return newsUid;
	}

	public void setNewsUid(Integer newsUid) {
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

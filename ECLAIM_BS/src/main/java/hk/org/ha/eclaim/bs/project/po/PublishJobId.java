package hk.org.ha.eclaim.bs.project.po;

import java.io.Serializable;
import java.util.Objects;

public class PublishJobId implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
 
    private Integer publishId;
    
    private Integer jobGroupId;
 
    public PublishJobId() {
    }
 
    public PublishJobId(Integer publishId, Integer jobGroupId) {
    	this.publishId = publishId;
        this.jobGroupId = jobGroupId;
    }
    
    public Integer getPublishId() {
		return publishId;
	}

	public void setPublishId(Integer publishId) {
		this.publishId = publishId;
	}

	public Integer getJobGroupId() {
		return jobGroupId;
	}

	public void setJobGroupId(Integer jobGroupId) {
		this.jobGroupId = jobGroupId;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PublishJobId)) return false;
        PublishJobId that = (PublishJobId) o;
        return Objects.equals(getPublishId(), that.getPublishId()) &&
                Objects.equals(getJobGroupId(), that.getJobGroupId());
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(getPublishId(), getJobGroupId());
    }
}
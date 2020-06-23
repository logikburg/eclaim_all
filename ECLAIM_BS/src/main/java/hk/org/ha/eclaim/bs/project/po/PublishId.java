package hk.org.ha.eclaim.bs.project.po;

import java.io.Serializable;
import java.util.Objects;

public class PublishId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer projectId;

	private Integer projectVerId;

	private Integer publishId;

	public PublishId() {
	}

	public PublishId(Integer projectId, Integer projectVerId, Integer publishId) {
		this.projectId = projectId;
		this.projectVerId = projectVerId;
		this.publishId = publishId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public Integer getProjectVerId() {
		return projectVerId;
	}

	public Integer getPublishId() {
		return publishId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof PublishId))
			return false;
		PublishId that = (PublishId) o;
		return Objects.equals(getProjectId(), that.getProjectId())
				&& Objects.equals(getProjectVerId(), that.getProjectVerId())
				&& Objects.equals(getPublishId(), that.getPublishId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getProjectId(), getProjectVerId(), getPublishId());
	}
}
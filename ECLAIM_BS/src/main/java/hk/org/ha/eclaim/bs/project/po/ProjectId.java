package hk.org.ha.eclaim.bs.project.po;

import java.io.Serializable;
import java.util.Objects;

public class ProjectId implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private Integer projectId;
 
    private Integer projectVerId;
 
    public ProjectId() {
    }
 
    public ProjectId(Integer projectId, Integer projectVerId) {
        this.projectId = projectId;
        this.projectVerId = projectVerId;
    }
 
    public Integer getProjectId() {
        return projectId;
    }
 
    public Integer getProjectVerId() {
        return projectVerId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectId)) return false;
        ProjectId that = (ProjectId) o;
        return Objects.equals(getProjectId(), that.getProjectId()) &&
                Objects.equals(getProjectVerId(), that.getProjectVerId());
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(getProjectId(), getProjectVerId());
    }
}
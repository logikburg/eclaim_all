package hk.org.ha.eclaim.bs.request.po;

import java.io.Serializable;

public class PK_RequestPosition implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int requestNo;
    private String postId;

    public PK_RequestPosition() {}

    public PK_RequestPosition(int requestNo, String postId) {
        this.requestNo = requestNo;
        this.postId = postId;
    }

    @Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null) {
			return false;
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		PK_RequestPosition other = (PK_RequestPosition) obj;
		
		if (requestNo != other.requestNo) {
			return false;
		}
		
		if (postId != other.postId) {
			return false;
		}
		return true;
	}
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((postId == null) ? 0 : postId.hashCode());
		result = prime * result + requestNo;
		return result;
	}
    
}

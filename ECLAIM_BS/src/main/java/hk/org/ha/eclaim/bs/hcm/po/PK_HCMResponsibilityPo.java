package hk.org.ha.eclaim.bs.hcm.po;

import java.io.Serializable;

public class PK_HCMResponsibilityPo implements Serializable{

	private static final long serialVersionUID = 1L;

	private String userId;
	private Integer responsibilityId;
	
	public PK_HCMResponsibilityPo() {}
	
	 public PK_HCMResponsibilityPo(String userId, Integer responsibilityId) {
	        this.userId = userId;
	        this.responsibilityId = responsibilityId;
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
			
			PK_HCMResponsibilityPo other = (PK_HCMResponsibilityPo) obj;
			
			if (userId != other.userId) {
				return false;
			}
			
			if (responsibilityId != other.responsibilityId) {
				return false;
			}
			return true;
		}
	    
	    @Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((userId == null) ? 0 : userId.hashCode())+ ((responsibilityId == null) ? 0 : responsibilityId.hashCode());
			result = prime * result + prime;
			return result;
		}
}

package hk.org.ha.eclaim.bs.report.po;

import java.io.Serializable;

public class PK_Report4 implements Serializable{

	private static final long serialVersionUID = 1L;

	private String postTitle;
	private String postId;
	private String employeeName;
	
	
	public PK_Report4() {}
	
	 public PK_Report4(String postTitle, String postId, String employeeName) {
	        this.postTitle = postTitle;
	        this.postId = postId;
	        this.employeeName = employeeName;
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
			
			PK_Report4 other = (PK_Report4) obj;
			
			if (postTitle != other.postTitle) {
				return false;
			}
			
			if (postId != other.postId) {
				return false;
			}
			
			if (employeeName != other.employeeName) {
				return false;
			}
			return true;
		}
	    
	    @Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((postTitle == null) ? 0 : postTitle.hashCode());
			result = prime * result + prime;
			return result;
		}
}

package hk.org.ha.eclaim.bs.report.po;

import java.io.Serializable;

public class PK_Report2 implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String clusterCode;
	private String hospital;
	private String deptName;
	private String postTitle;
	
	public PK_Report2() {}
	
	public PK_Report2(String clusterCode, String hospital, String deptName, String postTitle) {
		this.clusterCode = clusterCode;
		this.hospital = hospital;
		this.deptName = deptName;
		this.postTitle = postTitle;
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

		PK_Report2 other = (PK_Report2) obj;
		
		if (clusterCode != other.clusterCode) {
			return false;
		}

		if (hospital != other.hospital) {
			return false;
		}

		if (deptName != other.deptName) {
			return false;
		}

		if (postTitle != other.postTitle) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((clusterCode == null) ? 0 : clusterCode.hashCode())
				+ ((hospital == null) ? 0 : hospital.hashCode())
				+ ((deptName == null) ? 0 : deptName.hashCode())
				+ ((postTitle == null) ? 0 : postTitle.hashCode());
		result = prime * result + prime;
		return result;
	}

}

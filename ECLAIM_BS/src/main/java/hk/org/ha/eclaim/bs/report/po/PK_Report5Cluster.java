package hk.org.ha.eclaim.bs.report.po;

import java.io.Serializable;

public class PK_Report5Cluster implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String clusterCode;
    private String staffGroupCode;

    public PK_Report5Cluster() {}

    public PK_Report5Cluster(String clusterCode, String staffGroupCode) {
        this.clusterCode = clusterCode;
        this.staffGroupCode = staffGroupCode;
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
		
		PK_Report5Cluster other = (PK_Report5Cluster) obj;
		
		if (clusterCode != other.clusterCode) {
			return false;
		}
		
		if (staffGroupCode != other.staffGroupCode) {
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
				+ ((staffGroupCode == null) ? 0 : staffGroupCode.hashCode());
		result = prime * result + prime;
		return result;
	}
    
}

package hk.org.ha.eclaim.bs.report.po;

import java.io.Serializable;

public class PK_Report5InstRank implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	private String instCode;
	private String clusterCode;
    private String staffGroupCode;
    private String deptCode;
	private String rankCode;

    public PK_Report5InstRank() {}

    public PK_Report5InstRank(String instCode, String clusterCode, String staffGroupCode, String deptCode, String rankCode) {
        this.instCode = instCode;
        this.clusterCode = clusterCode;
        this.staffGroupCode = staffGroupCode;
        this.deptCode = deptCode;
        this.rankCode = rankCode;
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
		
		PK_Report5InstRank other = (PK_Report5InstRank) obj;
		
		if (instCode != other.instCode) {
			return false;
		}
		
		if (clusterCode != other.clusterCode) {
			return false;
		}
		
		if (staffGroupCode != other.staffGroupCode) {
			return false;
		}
		
		if (deptCode != other.deptCode) {
			return false;
		}
		
		if (rankCode != other.rankCode) {
			return false;
		}
		return true;
	}
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((instCode == null) ? 0 : instCode.hashCode())
				+ ((clusterCode == null) ? 0 : clusterCode.hashCode())
				+ ((deptCode == null) ? 0 : deptCode.hashCode())
				+ ((rankCode == null) ? 0 : rankCode.hashCode());
		result = prime * result + prime;
		return result;
	}
}

package hk.org.ha.eclaim.bs.report.po;

import java.io.Serializable;

public class PK_Report5Inst implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	private String instCode;
	private String clusterCode;
    private String staffGroupCode;

    public PK_Report5Inst() {}

    public PK_Report5Inst(String instCode, String clusterCode, String staffGroupCode) {
        this.instCode = instCode;
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
		
		PK_Report5Inst other = (PK_Report5Inst) obj;
		
		if (instCode != other.instCode) {
			return false;
		}
		
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
				+ ((instCode == null) ? 0 : instCode.hashCode());
		result = prime * result + prime;
		return result;
	}
}

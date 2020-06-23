package hk.org.ha.eclaim.bs.hcm.po;

import java.io.Serializable;
import java.util.Date;

public class PK_HCMAssignmentPo implements Serializable{

	private static final long serialVersionUID = 1L;
	private String assignmentNumber;
	private Date effectiveStartDate;

	public PK_HCMAssignmentPo() {}

	public PK_HCMAssignmentPo(String assignmentNumber, Date effectiveStartDate) {
		this.setAssignmentNumber(assignmentNumber);
		this.setEffectiveStartDate(effectiveStartDate);
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

		PK_HCMAssignmentPo other = (PK_HCMAssignmentPo) obj;

		if (assignmentNumber != other.assignmentNumber) {
			return false;
		}

		if (effectiveStartDate != other.effectiveStartDate) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((effectiveStartDate == null) ? 0 : effectiveStartDate.hashCode());
		result = prime * result + prime;
		return result;
	}

	public String getAssignmentNumber() {
		return assignmentNumber;
	}

	public void setAssignmentNumber(String assignmentNumber) {
		this.assignmentNumber = assignmentNumber;
	}

	public Date getEffectiveStartDate() {
		return effectiveStartDate;
	}

	public void setEffectiveStartDate(Date effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}
}

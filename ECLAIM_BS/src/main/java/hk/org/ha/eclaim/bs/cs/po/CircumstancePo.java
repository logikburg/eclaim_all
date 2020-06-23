package hk.org.ha.eclaim.bs.cs.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="XXEAL_SHS_CIRCUMSTANCE_M")
public class CircumstancePo extends AbstractBasePo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1915109261331658170L;
	
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="XXEAL_SHS_CIRCUM_M_ID_SEQ")
	@SequenceGenerator(name="XXEAL_SHS_CIRCUM_M_ID_SEQ", sequenceName="XXEAL_SHS_CIRCUM_M_ID_SEQ", allocationSize=1)
	@Id
	@Column(name="CIRCUMSTANCE_ID")
	private Integer circumstanceId;
	
	@Column(name="CIRCUMSTANCE_CODE")
	private String circumstanceCode;
	
	@Column(name="DISPLAY_SEQ")
	private Integer displaySeq;
	
	@Column(name="DESCRIPTIONQ")
	private String descriptionQ;
	
	@Column(name="MANPOWER_SHORTAGE")
	private String manpowerShortage;
	
	@Column(name="Q_DELIVERABLE_QUIDE")
	private String qDeliverableQuide;
	
	@Column(name="Q_DELIVERABLE_EXAMPL")
	private String qDeliverableExampl;
	
	@Temporal(TemporalType.DATE)
	@Column(name="START_DATE")
	private Date startDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="END_DATE")
	private Date endDate;
	
	
	public Integer getCircumstanceId() {
		return circumstanceId;
	}
	public void setCircumstanceId(Integer circumstanceId) {
		this.circumstanceId = circumstanceId;
	}
	public String getCircumstanceCode() {
		return circumstanceCode;
	}
	public void setCircumstanceCode(String circumstanceCode) {
		this.circumstanceCode = circumstanceCode;
	}
	public Integer getDisplaySeq() {
		return displaySeq;
	}
	public void setDisplaySeq(Integer displaySeq) {
		this.displaySeq = displaySeq;
	}
	public String getDescriptionQ() {
		return descriptionQ;
	}
	public void setDescriptionQ(String descriptionQ) {
		this.descriptionQ = descriptionQ;
	}
	public String getManpowerShortage() {
		return manpowerShortage;
	}
	public void setManpowerShortage(String manpowerShortage) {
		this.manpowerShortage = manpowerShortage;
	}
	public String getqDeliverableQuide() {
		return qDeliverableQuide;
	}
	public void setqDeliverableQuide(String qDeliverableQuide) {
		this.qDeliverableQuide = qDeliverableQuide;
	}
	public String getqDeliverableExampl() {
		return qDeliverableExampl;
	}
	public void setqDeliverableExampl(String qDeliverableExampl) {
		this.qDeliverableExampl = qDeliverableExampl;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
}

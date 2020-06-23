package hk.org.ha.eclaim.bs.cs.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.cs.po.CircumstancePo;

public interface ICircumstanceDao {
	
	public List<CircumstancePo> getAllCircum();
	
	public List<CircumstancePo> getAllActiveCircum();
	
	public CircumstancePo getCircumPoByCircumId(Integer circumId);
	
	public CircumstancePo getCircumPoByCircumCode(String circumCode);
}

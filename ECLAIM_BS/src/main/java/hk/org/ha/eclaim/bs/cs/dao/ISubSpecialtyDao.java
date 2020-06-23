package hk.org.ha.eclaim.bs.cs.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.cs.po.SubSpecialtyPo;

public interface ISubSpecialtyDao {

	public List<SubSpecialtyPo> getAllSubSpecialty();

	public List<SubSpecialtyPo> getAllSubSpecialtyByStaffGroup(String staffGroupCode, String deptCode);

}
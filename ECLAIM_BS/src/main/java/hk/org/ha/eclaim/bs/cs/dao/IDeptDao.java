package hk.org.ha.eclaim.bs.cs.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.cs.po.DepartmentPo;

public interface IDeptDao {
	List<DepartmentPo> getAllDept();
	
	List<DepartmentPo> getAllDeptByStaffGroup(String staffGroupCode);
	
	DepartmentPo getDeptNameByDeptCode(String deptCode);
	
}

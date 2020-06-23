package hk.org.ha.eclaim.bs.cs.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.cs.po.StaffGroupPo;

public interface IStaffGroupDao {
	List<StaffGroupPo> getAllStaffGroup();

	StaffGroupPo getStaffGroupNameByStaffGroupCode(String staffGroupCode);

}

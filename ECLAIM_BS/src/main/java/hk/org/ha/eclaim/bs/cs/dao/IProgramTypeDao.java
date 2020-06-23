package hk.org.ha.eclaim.bs.cs.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.cs.po.ProgramTypePo;

public interface IProgramTypeDao {
	List<ProgramTypePo> getAllProgramType();
	
	// Added for CC176525
	public List<ProgramTypePo> getProgramTypeListByPostInfo(String postDuration, String postFteType, String annaulPlanInd);
	
}

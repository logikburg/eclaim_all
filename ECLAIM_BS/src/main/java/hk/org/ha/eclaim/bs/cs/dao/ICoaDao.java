package hk.org.ha.eclaim.bs.cs.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.cs.po.CoaPo;

public interface ICoaDao {
	List<CoaPo> getAllCOA(String coaName);
}

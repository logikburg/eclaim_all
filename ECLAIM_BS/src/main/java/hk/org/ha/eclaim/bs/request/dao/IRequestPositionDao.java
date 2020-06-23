package hk.org.ha.eclaim.bs.request.dao;

import hk.org.ha.eclaim.bs.request.po.RequestPostPo;

public interface IRequestPositionDao {
	RequestPostPo getRequestPositionByUid(int requestPositionUid);
}

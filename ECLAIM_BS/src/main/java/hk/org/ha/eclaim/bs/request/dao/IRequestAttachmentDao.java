package hk.org.ha.eclaim.bs.request.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.request.po.RequestAttachmentPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

public interface IRequestAttachmentDao {
	int insertAttachment(RequestAttachmentPo attachment, UserPo user);

	List<RequestAttachmentPo> getAttachmentByRequestUid(int requestUid);
	
	List<RequestAttachmentPo> getAttachmentByPostUid(int postUid);

	RequestAttachmentPo getAttachmentByAttachmentId(int parseInt);

	void removeAttachment(RequestAttachmentPo attachment, UserPo user);
}

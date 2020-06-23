package hk.org.ha.eclaim.bs.cs.dao;

import hk.org.ha.eclaim.bs.cs.po.EmailTemplatePo;

public interface IEmailTemplateDao {
	public EmailTemplatePo getTemplateByTemplateId(String id, String[] para);
}

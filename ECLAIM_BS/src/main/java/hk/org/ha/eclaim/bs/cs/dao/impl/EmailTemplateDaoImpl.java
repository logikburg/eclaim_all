package hk.org.ha.eclaim.bs.cs.dao.impl;

import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.cs.dao.IEmailTemplateDao;
import hk.org.ha.eclaim.bs.cs.po.EmailTemplatePo;

@Repository
public class EmailTemplateDaoImpl implements IEmailTemplateDao {
	@PersistenceContext
	private EntityManager entityManager;

	public EmailTemplatePo getTemplateByTemplateId(String id, String[] para) {
		Query q = entityManager.createQuery("SELECT C FROM EmailTemplatePo C where templateId = :templateId", EmailTemplatePo.class);
		q.setParameter("templateId", id);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		if (result.hasNext()) {
			EmailTemplatePo tmp = (EmailTemplatePo)result.next();
			
			if (para != null) {
				for (int i = 0; i < para.length; i++) {
					String v = para[i];
					if (v == null)
						v = "";
					
					v = v.replaceAll("\\$", "\\\\\\$");
					
					tmp.setTemplateTitle(tmp.getTemplateTitle().replaceAll("<para" + (i+1) + ">", v));
					tmp.setTemplateContent(tmp.getTemplateContent().replaceAll("<para" + (i+1) + ">", v));
				}
			}
			
			return tmp;
		}
		
		return null;
	}
}
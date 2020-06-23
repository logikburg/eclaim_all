package hk.org.ha.eclaim.core.dao.jpa.listener;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

public class EntityChangeListener {
	
	@PrePersist
	@PreUpdate
	public void setTime(AbstractBasePo po){
		Date currentDate = new Date();
		
		if (po.getCreatedDate()==null){
			po.setCreatedDate(currentDate);
		}
		
		po.setUpdatedDate(currentDate);
		
	}
}

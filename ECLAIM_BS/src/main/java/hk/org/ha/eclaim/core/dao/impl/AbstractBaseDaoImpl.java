package hk.org.ha.eclaim.core.dao.impl;

import java.lang.reflect.ParameterizedType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import hk.org.ha.eclaim.core.po.AbstractBasePo;
import hk.org.ha.eclaim.core.dao.IBaseDao;


public abstract class AbstractBaseDaoImpl<P extends AbstractBasePo> implements IBaseDao<P>  {

	@PersistenceContext
	protected EntityManager entityManager;
	
	private Class<P> poClass;
	
	@SuppressWarnings("unchecked")
	public AbstractBaseDaoImpl() {
		ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
		this.poClass = (Class<P>) type.getActualTypeArguments()[0];
	}
	
	protected P load(Object id) {
		return load(poClass, id);
	}
	
	protected P load(Class<P> clazz, Object id) {
		P po = (P)entityManager.find(clazz, id);
		return po;
	}
	
	public void create(P po) {
		entityManager.persist(po);
	}
	
	public P update(P po) {
		P newPo = (P)entityManager.merge(po);
		return newPo;
	}
	
}

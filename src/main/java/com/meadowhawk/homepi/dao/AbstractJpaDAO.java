package com.meadowhawk.homepi.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractJpaDAO<T> {
	protected Class<T> clazz;
	@PersistenceContext
	EntityManager entityManager;

	public void setClazz(Class<T> clazzToSet) {
		this.clazz = clazzToSet;
	}

	@Transactional(readOnly = true)
	public T findOne(Long id) {
		return entityManager.find(clazz, id);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<T> findAll() {
		return entityManager.createQuery("from " + clazz.getName()).getResultList();
	}

	@Transactional
	public void save(T entity) {
		entityManager.persist(entity);
	}

	@Transactional
	public void update(T entity) {
		entityManager.merge(entity);
	}

	@Transactional
	public void delete(T entity) {
		entityManager.remove(entityManager.merge(entity));
	}

	@Transactional
	public void deleteById(Long entityId) {
		final T entity = findOne( entityId );
    if(entity != null){
    	delete( entity );
    }
	}
}

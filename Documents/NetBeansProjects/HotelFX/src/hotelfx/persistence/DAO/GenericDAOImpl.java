/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.DAO;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;

/**
 *
 * @author ANDRI
 * @param <T>
 * @param <ID>
 */
public abstract class GenericDAOImpl<T, ID extends Serializable>
        implements GenericDAO<T, ID> {

    private final Class<T> persistentClass;
    private Session session;

    public GenericDAOImpl() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    
    public void setSession(Session s) {
        this.session = s;
    }

    protected Session getSession() {
        if (session == null) {
            throw new IllegalStateException("Session has not been set on DAO before usage");
        }
        return session;
    }

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T findById(ID id, boolean lock) {
        T entity;
        if (lock) {
            entity = (T) getSession().load(getPersistentClass(), id, LockMode.PESSIMISTIC_WRITE);
        } else {
            entity = (T) getSession().load(getPersistentClass(), id);
        }

        return entity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAll() {
        return findByCriteria();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findByExample(T exampleInstance, String[] excludeProperty) {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        Example example = Example.create(exampleInstance);
        for (String exclude : excludeProperty) {
            example.excludeProperty(exclude);
        }
        crit.add(example);
        return crit.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T makePersistent(T entity) {
        getSession().saveOrUpdate(entity);
        return entity;
    }

    @Override
    public void makeTransient(T entity) {
        getSession().delete(entity);
    }

    public void flush() {
        getSession().flush();
    }

    public void clear() {
        getSession().clear();
    }

    /**
     * Use this inside subclasses as a convenience method.
     * @param criterion
     * @return 
     */
    @SuppressWarnings("unchecked")
    protected List<T> findByCriteria(Criterion... criterion) {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        for (Criterion c : criterion) {
            crit.add(c);
        }
        return crit.list();
    }

}

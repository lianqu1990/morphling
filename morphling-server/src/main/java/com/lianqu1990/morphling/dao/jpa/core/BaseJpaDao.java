package com.lianqu1990.morphling.dao.jpa.core;


import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author hanchao
 * @date 2017/5/25 20:43
 */
public class BaseJpaDao extends BaseDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    protected Session getSession() {
        return em.unwrap(Session.class);
    }
}

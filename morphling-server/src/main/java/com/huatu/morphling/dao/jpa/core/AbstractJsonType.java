package com.huatu.morphling.dao.jpa.core;

import com.alibaba.fastjson.JSON;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * @author hanchao
 * @date 2017/10/21 14:15
 */
public abstract class AbstractJsonType implements UserType,Serializable {
    private static final long serialVersionUID = 1L;


    @Override
    public int[] sqlTypes() {
        return new int[] {getSqlType()};
    }

    @Override
    public Class returnedClass() {
        return getJavaType();
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return Objects.equals(x,y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        String json = rs.getString(names[0]);
        if(json == null || json.trim().length() == 0) {
            return null;
        }
        return JSON.parseObject(json,getJavaType());
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, getSqlType());
            return;
        }
        st.setObject(index, JSON.toJSONString(value), getSqlType());
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        if(value == null){
            return null;
        }
        return JSON.parseObject(JSON.toJSONString(value),value.getClass());
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }


    public abstract int getSqlType();
    public abstract Class getJavaType();
}

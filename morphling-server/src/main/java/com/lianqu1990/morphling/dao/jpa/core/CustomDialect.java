package com.lianqu1990.morphling.dao.jpa.core;

import org.hibernate.dialect.MySQLDialect;
import org.hibernate.type.LongType;

import java.sql.Types;

/**
 * 自定义方言，bigint和po中的long无法转换
 * @author Han
 */
public class CustomDialect extends MySQLDialect {
    public CustomDialect() {  
        super();  
        registerHibernateType(Types.BIGINT, LongType.INSTANCE.getName());
    }  
} 

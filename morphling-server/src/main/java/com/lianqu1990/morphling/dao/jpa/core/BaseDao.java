package com.lianqu1990.morphling.dao.jpa.core;

import com.lianqu1990.common.bean.page.Pager;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.hibernate.transform.Transformers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author hanchao
 * @date 2017/4/27 16:07
 */
public abstract class BaseDao {

    protected abstract Session getSession();

    /**
     * 分页查询
     * @param sql
     * @param clazz
     * @param pager
     * @param params
     * @param <T>
     * @return
     */
    public <T> Pager<T> pagerTransformBySql(String sql, Class<T> clazz, Pager<T> pager, Map<String,Object> params){
        SQLQuery sqlQuery = buildTransformSQLQuery(sql,clazz,params);
        sqlQuery.setFirstResult(pager.getFirstResult());
        sqlQuery.setMaxResults(pager.getOnePageSize());
        pager.setData(find(sqlQuery));
        pager.setTotalResults(queryCount(sql,params));
        return pager;
    }

    /**
     * 分页查询
     * @param sql
     * @param clazz
     * @param pager
     * @param params
     * @param <T>
     * @return
     */
    public <T> Pager<T> pagerTransformBySql(String sql,Class<T> clazz,Pager<T> pager,Object ... params){
        SQLQuery sqlQuery = buildTransformSQLQuery(sql,clazz,params);
        sqlQuery.setFirstResult(pager.getFirstResult());
        sqlQuery.setMaxResults(pager.getOnePageSize());
        pager.setData(find(sqlQuery));
        pager.setTotalResults(queryCount(sql,params));
        return pager;
    }


    /**
     * 分页查询
     * @param sql
     * @param clazz
     * @param pager
     * @param params
     * @param <T>
     * @return
     */
    public <T> Pager<T> pagerEntityBySql(String sql, Class<T> clazz, Pager<T> pager, Map<String,Object> params){
        SQLQuery sqlQuery = buildEntitySQLQuery(sql,clazz,params);
        sqlQuery.setFirstResult(pager.getFirstResult());
        sqlQuery.setMaxResults(pager.getOnePageSize());
        pager.setData(find(sqlQuery));
        pager.setTotalResults(queryCount(sql,params));
        return pager;
    }

    /**
     * 分页查询
     * @param sql
     * @param clazz
     * @param pager
     * @param params
     * @param <T>
     * @return
     */
    public <T> Pager<T> pagerEntityBySql(String sql,Class<T> clazz,Pager<T> pager,Object ... params){
        SQLQuery sqlQuery = buildEntitySQLQuery(sql,clazz,params);
        sqlQuery.setFirstResult(pager.getFirstResult());
        sqlQuery.setMaxResults(pager.getOnePageSize());
        pager.setData(find(sqlQuery));
        pager.setTotalResults(queryCount(sql,params));
        return pager;
    }

    /**
     * 查找不被hibernate管理的实体
     * @param sql
     * @param clazz
     * @param params
     * @return
     */
    public <T> List<T> findToTransformerBySql(String sql,Class<T> clazz, Map<String,Object> params) {
        SQLQuery sqlQuery = buildTransformSQLQuery(sql,clazz,params);
        return sqlQuery.list();
    }

    /**
     * 查找不被hibernate管理的实体
     * @param sql
     * @param clazz
     * @param params
     * @return
     */
    public <T> List<T> findToTransformerBySql(String sql,Class<T> clazz, Object ...params) {
        SQLQuery sqlQuery = buildTransformSQLQuery(sql,clazz,params);
        return sqlQuery.list();
    }

    /**
     * 查找不被hibernate管理的实体，getunique可能抛异常情况下使用
     * @param sql
     * @param clazz
     * @param params
     * @return
     */
    public <T> T findOneToTransformerBySql(String sql,Class<T> clazz, Map<String,Object> params) {
        SQLQuery sqlQuery = buildTransformSQLQuery(sql,clazz,params);
        return findOne(sqlQuery);
    }

    /**
     * 查找不被hibernate管理的实体，getunique可能抛异常情况下使用
     * @param sql
     * @param clazz
     * @param params
     * @return
     */
    public <T> T findOneToTransformerBySql(String sql,Class<T> clazz, Object ...params) {
        SQLQuery sqlQuery = buildTransformSQLQuery(sql,clazz,params);
        return findOne(sqlQuery);
    }

    /**
     * 查找不被hibernate管理的实体
     * @param sql
     * @param clazz
     * @param params
     * @return
     */
    public <T> T getToTransformerBySql(String sql,Class<T> clazz, Map<String,Object> params) {
        SQLQuery sqlQuery = buildTransformSQLQuery(sql,clazz,params);
        return get(sqlQuery);
    }

    /**
     * 查找不被hibernate管理的实体
     * @param sql
     * @param clazz
     * @param params
     * @return
     */
    public <T> T getToTransformerBySql(String sql,Class<T> clazz, Object ...params) {
        SQLQuery sqlQuery = buildTransformSQLQuery(sql,clazz,params);
        return get(sqlQuery);
    }


    /**
     * 查找sqlquery默认返回的，已擦除类型，注意使用安全
     * @param sql
     * @param params
     * @return
     */
    public <T> List<T> findBySql(String sql, Map<String,Object> params) {
        SQLQuery sqlQuery = createSQLQuery(sql);
        appendParams(sqlQuery,params);
        return sqlQuery.list();
    }

    /**
     * 查找sqlquery默认返回的，已擦除类型，注意使用安全
     * @param sql
     * @param params
     * @return
     */
    public <T> List<T> findBySql(String sql, Object ...params) {
        SQLQuery sqlQuery = createSQLQuery(sql);
        appendParams(sqlQuery,params);
        return sqlQuery.list();
    }

    /**
     * 查找sqlquery默认返回的，已擦除类型，注意使用安全
     * @param sql
     * @param params
     * @return
     */
    public <T> T findOneBySql(String sql, Map<String,Object> params) {
        SQLQuery sqlQuery = createSQLQuery(sql);
        appendParams(sqlQuery,params);
        return findOne(sqlQuery);
    }

    /**
     * 查找sqlquery默认返回的，已擦除类型，注意使用安全
     * @param sql
     * @param params
     * @return
     */
    public <T> T findOneBySql(String sql, Object ...params) {
        SQLQuery sqlQuery = createSQLQuery(sql);
        appendParams(sqlQuery,params);
        return findOne(sqlQuery);
    }

    /**
     * 查找sqlquery默认返回的，已擦除类型，注意使用安全
     * @param sql
     * @param params
     * @return
     */
    public <T> T getBySql(String sql, Map<String,Object> params) {
        SQLQuery sqlQuery = createSQLQuery(sql);
        appendParams(sqlQuery,params);
        return get(sqlQuery);
    }

    /**
     * 查找sqlquery默认返回的，已擦除类型，注意使用安全
     * @param sql
     * @param params
     * @return
     */
    public <T> T getBySql(String sql, Object ...params) {
        SQLQuery sqlQuery = createSQLQuery(sql);
        appendParams(sqlQuery,params);
        return get(sqlQuery);
    }


    //-------------------
    /**
     * 查找sqlquery默认返回的，已擦除类型，注意使用安全
     * @param sql
     * @param params
     * @return
     */
    public <T> List<T> findToEntityBySql(String sql,Class<T> entity, Map<String,Object> params) {
        SQLQuery sqlQuery = buildEntitySQLQuery(sql,entity);
        appendParams(sqlQuery,params);
        return sqlQuery.list();
    }

    /**
     * 查找sqlquery默认返回的，已擦除类型，注意使用安全
     * @param sql
     * @param params
     * @return
     */
    public <T> List<T> findToEntityBySql(String sql,Class<T> entity, Object ...params) {
        SQLQuery sqlQuery = buildEntitySQLQuery(sql,entity);
        appendParams(sqlQuery,params);
        return sqlQuery.list();
    }

    /**
     * 查找sqlquery默认返回的，已擦除类型，注意使用安全
     * @param sql
     * @param params
     * @return
     */
    public <T> T findOneToEntityBySql(String sql,Class<T> entity, Map<String,Object> params) {
        SQLQuery sqlQuery = buildEntitySQLQuery(sql,entity);
        appendParams(sqlQuery,params);
        return findOne(sqlQuery);
    }

    /**
     * 查找sqlquery默认返回的，已擦除类型，注意使用安全
     * @param sql
     * @param params
     * @return
     */
    public <T> T findOneToEntityBySql(String sql,Class<T> entity, Object ...params) {
        SQLQuery sqlQuery = buildEntitySQLQuery(sql,entity);
        appendParams(sqlQuery,params);
        return findOne(sqlQuery);
    }

    /**
     * 查找sqlquery默认返回的，已擦除类型，注意使用安全
     * @param sql
     * @param params
     * @return
     */
    public <T> T getEntityBySql(String sql, Class<T> entity,Map<String,Object> params) {
        SQLQuery sqlQuery = buildEntitySQLQuery(sql,entity);
        appendParams(sqlQuery,params);
        return get(sqlQuery);
    }

    /**
     * 查找sqlquery默认返回的，已擦除类型，注意使用安全
     * @param sql
     * @param params
     * @return
     */
    public <T> T getEntityBySql(String sql,Class<T> entity, Object ...params) {
        SQLQuery sqlQuery = buildEntitySQLQuery(sql,entity);
        appendParams(sqlQuery,params);
        return get(sqlQuery);
    }



    /**
     * 批处理一条sql
     *
     * @param sql
     * @param params
     * @return
     */
    public int excuteBatchSqlUpdate(final String sql, final List<Object[]> params) {
        getSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                PreparedStatement statement = connection.prepareStatement(sql);
                int index = 0;
                for (Object[] objs : params) {
                    for (int i = 0; i < objs.length; i++) {
                        statement.setObject(i+1, objs[i]);
                    }
                    statement.addBatch();
                    index++;
                    if(index%100 == 0){
                        statement.executeBatch();
                        statement.clearBatch();
                    }
                }
                statement.executeBatch();
            }
        });
        return 1;
    }


    // ---------------- base methods -------------------
    public <T> T get(SQLQuery sqlQuery){
        return (T) sqlQuery.uniqueResult();
    }

    public <T> T findOne(SQLQuery sqlQuery){
        List<T> result = sqlQuery.list();
        if(result == null || result.size() == 0){
            return null;
        }else{
            return result.get(0);
        }
    }

    public <T> List<T> find(SQLQuery sqlQuery){
        return sqlQuery.list();
    }


    /**
     * 创建完整sqlquery
     * @param sql
     * @param clazz
     * @param params
     * @return
     */
    private SQLQuery buildEntitySQLQuery(String sql, Class<?> clazz, Object ...params){
        SQLQuery sqlQuery = createEntitySQLQuery(sql,clazz);
        appendParams(sqlQuery, params);
        return sqlQuery;
    }


    /**
     * 创建完整sqlquery
     * @param sql
     * @param clazz
     * @param params
     * @return
     */
    private SQLQuery buildEntitySQLQuery(String sql, Class<?> clazz, Map<String,Object> params){
        SQLQuery sqlQuery = createEntitySQLQuery(sql, clazz);
        appendParams(sqlQuery,params);
        return sqlQuery;
    }

    /**
     * 创建完整sqlquery
     * @param sql
     * @param clazz
     * @param params
     * @return
     */
    private SQLQuery buildTransformSQLQuery(String sql, Class<?> clazz, Object ...params){
        SQLQuery sqlQuery = createTransformSQLQuery(sql,clazz);
        appendParams(sqlQuery, params);
        return sqlQuery;
    }


    /**
     * 创建完整sqlquery
     * @param sql
     * @param clazz
     * @param params
     * @return
     */
    private SQLQuery buildTransformSQLQuery(String sql, Class<?> clazz, Map<String,Object> params){
        SQLQuery sqlQuery = createTransformSQLQuery(sql, clazz);
        appendParams(sqlQuery,params);
        return sqlQuery;
    }

    /**
     * 创建sqlQuery
     * @param sql
     * @return
     */
    private SQLQuery createSQLQuery(String sql){
        return getSession().createSQLQuery(sql);
    }

    /**
     * 创建查询基本对象的sqlQuery
     * @param sql
     * @param clazz
     * @return
     */
    private SQLQuery createEntitySQLQuery(String sql, Class<?> clazz) {
        SQLQuery sqlQuery = createSQLQuery(sql);
        sqlQuery.addEntity(clazz);
        return sqlQuery;
    }

    /**
     * 创建查询基本对象的sqlQuery
     * @param sql
     * @param clazz
     * @return
     */
    private SQLQuery createTransformSQLQuery(String sql, Class<?> clazz) {
        SQLQuery sqlQuery = createSQLQuery(sql);
        if(clazz == null || clazz == Map.class){
            sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        }else{
            sqlQuery.setResultTransformer(Transformers.aliasToBean(clazz));
        }
        return sqlQuery;
    }


    /**
     * 装配参数
     * @param sqlQuery
     * @param params
     */
    private void appendParams(SQLQuery sqlQuery, Object... params) {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                sqlQuery.setParameter(i, params[i]);
            }
        }
    }

    /**
     * 装配参数
     * @param sqlQuery
     * @param params
     */
    private void appendParams(SQLQuery sqlQuery, Map<String,Object> params) {
        if (params != null && !params.isEmpty()) {
            params.forEach((k,v) -> sqlQuery.setParameter(k,v) );
        }
    }


    /**
     * 查询总数
     * @param sql
     * @param params
     * @return
     */
    public long queryCount(String sql,Object ...params){
        sql = "select count(1) from ("+sql+")t";
        SQLQuery sqlQuery = createSQLQuery(sql);
        appendParams(sqlQuery,params);
        return get(sqlQuery);
    }

    /**
     * 查询总数
     * @param sql
     * @param params
     * @return
     */
    public long queryCount(String sql,Map<String,Object> params){
        sql = "select count(1) from ("+sql+")t";
        SQLQuery sqlQuery = createSQLQuery(sql);
        appendParams(sqlQuery,params);
        return get(sqlQuery);
    }
}

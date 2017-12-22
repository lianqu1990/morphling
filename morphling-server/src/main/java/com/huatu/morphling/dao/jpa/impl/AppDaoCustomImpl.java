package com.huatu.morphling.dao.jpa.impl;

import com.google.common.collect.Lists;
import com.huatu.common.bean.page.Pager;
import com.huatu.morphling.common.bean.AppFilter;
import com.huatu.morphling.common.bean.AppInstanceVO;
import com.huatu.morphling.common.bean.UserAppVO;
import com.huatu.morphling.dao.jpa.api.AppDaoCustom;
import com.huatu.morphling.dao.jpa.core.BaseJpaDao;
import com.huatu.morphling.dao.jpa.entity.App;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author hanchao
 * @date 2017/12/5 13:20
 */
public class AppDaoCustomImpl extends BaseJpaDao implements AppDaoCustom {
    @Override
    public AppInstanceVO findByInstanceId(int id) {
        String sql = "select a.port as port,a.context_path as contextPath,ac.id as id,c.host_address as host,c.name as clientName,a.name as appName,ac.status as status from client c,app a,app_instance ac where ac.app_id=a.id and ac.client_id=c.id and ac.id=? and c.state=1 and ac.state=1";
        return findOneToTransformerBySql(sql,AppInstanceVO.class,id);
    }

    @Override
    public List<AppInstanceVO> findInstances(List<Integer> instanceIds) {
        String id = StringUtils.join(instanceIds.toArray(),",");
        String sql = "select a.port as port,c.port as clientPort,a.context_path as contextPath,ac.id as id,c.host_address as host,c.name as clientName,a.name as appName,ac.status as status from client c,app a,app_instance ac where ac.app_id=a.id and ac.client_id=c.id and ac.id in ("+id+") and c.state=1 and ac.state=1";
        return findToTransformerBySql(sql,AppInstanceVO.class);
    }

    @Override
    public List<AppInstanceVO> findInstances(int appId) {
        String sql = "select a.port as port,a.context_path as contextPath,ac.current_version as currentVersion,ac.id as id,c.host_address as host,c.name as clientName,a.name as appName,ac.status as status from client c,app a,app_instance ac where ac.app_id=a.id and ac.client_id=c.id and ac.app_id=? and c.state=1 and ac.state=1";
        return findToTransformerBySql(sql,AppInstanceVO.class,appId);
    }

    @Override
    public List<UserAppVO> findUserApps(int userId) {
        String sql = "select ua.id as id,a.env as env,a.name as appName,a.description as appDes from app a,user_app ua where ua.app_id=a.id and a.state=1 and ua.state=1 and ua.user_id=?";
        return findToTransformerBySql(sql,UserAppVO.class,userId);
    }

    @Override
    public Pager<App> findExcludeByFilter(int userId, AppFilter filter, Pager pager) {
        List<Object> params = Lists.newArrayList();
        StringBuilder sql = new StringBuilder("select a.* from app a where a.state = 1 and a.id not in (select ua.app_id from user_app ua where ua.user_id=? and ua.state=1)");
        params.add(userId);
        if(StringUtils.isNotBlank(filter.getName())){
            sql.append(" and a.name=?");
            params.add(filter.getName());
        }
        return pagerEntityBySql(sql.toString(),App.class,pager,params.stream().toArray(Object[]::new));
    }
}

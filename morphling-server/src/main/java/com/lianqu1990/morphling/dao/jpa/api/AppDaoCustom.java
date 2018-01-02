package com.lianqu1990.morphling.dao.jpa.api;

import com.lianqu1990.common.bean.page.Pager;
import com.lianqu1990.morphling.common.bean.AppFilter;
import com.lianqu1990.morphling.common.bean.AppInstanceVO;
import com.lianqu1990.morphling.common.bean.UserAppVO;
import com.lianqu1990.morphling.dao.jpa.entity.App;

import java.util.List;

/**
 * @author hanchao
 * @date 2017/12/5 13:19
 */
public interface AppDaoCustom {
    AppInstanceVO findByInstanceId(int id);

    List<AppInstanceVO> findInstances(List<Integer> instanceIds);

    List<AppInstanceVO> findInstances(int appId);

    List<UserAppVO> findUserApps(int userId);

    Pager<App> findExcludeByFilter(int userId, AppFilter filter, Pager pager);
}

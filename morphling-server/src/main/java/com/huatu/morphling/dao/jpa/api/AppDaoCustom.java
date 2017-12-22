package com.huatu.morphling.dao.jpa.api;

import com.huatu.common.bean.page.Pager;
import com.huatu.morphling.common.bean.AppFilter;
import com.huatu.morphling.common.bean.AppInstanceVO;
import com.huatu.morphling.common.bean.UserAppVO;
import com.huatu.morphling.dao.jpa.entity.App;

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

package com.huatu.morphling.dao.jpa.api;

import com.huatu.morphling.dao.jpa.entity.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author hanchao
 * @date 2017/12/4 17:27
 */
public interface AppDao extends JpaRepository<App,Integer>,JpaSpecificationExecutor<App>,AppDaoCustom{
    List<App> findByEnv(String env);

    @Query(value = "select a.* from app a,user_app ua where ua.user_id=:userId and ua.app_id=a.id and ua.state=1 and a.state=1 and a.env=:env",nativeQuery = true)
    List<App> findUserApps(@Param("userId") int userId,@Param("env") String env);
}

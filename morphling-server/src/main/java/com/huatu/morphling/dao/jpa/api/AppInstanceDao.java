package com.huatu.morphling.dao.jpa.api;

import com.huatu.morphling.dao.jpa.entity.AppInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author hanchao
 * @date 2017/12/5 15:08
 */
public interface AppInstanceDao extends JpaRepository<AppInstance,Integer> {
    @Query("update AppInstance set state=0 where id=:id")
    @Modifying
    void deleteLogic(@Param("id") int id);
}

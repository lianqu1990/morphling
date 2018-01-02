package com.lianqu1990.morphling.dao.jpa.api;

import com.lianqu1990.morphling.dao.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author hanchao
 * @date 2017/10/23 21:45
 */
public interface UserDao extends JpaRepository<User,Integer>,JpaSpecificationExecutor<User> {
    User findByUsername(String username);

    @Query("update User set state=0 where id=:id")
    @Modifying
    void deleteLogic(@Param("id")int id);
}

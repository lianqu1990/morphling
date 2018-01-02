package com.lianqu1990.morphling.dao.jpa.api;

import com.lianqu1990.morphling.dao.jpa.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * @author hanchao
 * @date 2017/11/6 10:22
 */
public interface UserRoleDao extends JpaRepository<UserRole,Integer> {
    @Query("update UserRole set state = 0 where userId=:userId and roleId in (:roleIds)")
    @Modifying
    void deleteLogic(@Param("userId")int userId, @Param("roleIds")Set<Integer> roleIds);
}

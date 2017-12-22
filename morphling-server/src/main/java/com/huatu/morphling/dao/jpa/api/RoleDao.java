package com.huatu.morphling.dao.jpa.api;

import com.huatu.morphling.dao.jpa.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author hanchao
 * @date 2017/11/6 10:23
 */
public interface RoleDao extends JpaRepository<Role,Integer> {
    @Query(value = "select r from Role r,UserRole ur where ur.userId=? and ur.roleId=r.id")
    List<Role> findByUserId(int userid);

    @Query(value = "select r from Role r,MenuRole mr where r.id=mr.roleId and mr.menuId=:menuId")
    List<Role> findByMenu(@Param("menuId") int menuId);
}

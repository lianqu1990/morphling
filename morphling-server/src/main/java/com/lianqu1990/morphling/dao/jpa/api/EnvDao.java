package com.lianqu1990.morphling.dao.jpa.api;

import com.lianqu1990.morphling.dao.jpa.entity.Env;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author hanchao
 * @date 2017/10/21 14:23
 */
public interface EnvDao extends JpaRepository<Env,Integer> {
    @Query(value = "select e from Env e,EnvRole re where re.envKey=e.key and re.roleId in (:roleIds)")
    List<Env> findByRole(@Param("roleIds") List<Integer> roleIdList);
}

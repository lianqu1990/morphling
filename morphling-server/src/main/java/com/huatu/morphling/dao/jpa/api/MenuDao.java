package com.huatu.morphling.dao.jpa.api;

import com.huatu.morphling.dao.jpa.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author hanchao
 * @date 2017/10/22 19:12
 */
public interface MenuDao extends JpaRepository<Menu,Integer> {
    @Override
    @Query("from Menu order by ordered")
    List<Menu> findAll();

    @Query("select m from Menu m,MenuRole mr where mr.menuId=m.id and mr.roleId in (:roleIds) order by m.ordered")
    List<Menu> findByRole(@Param("roleIds") List<Integer> roleId);
}

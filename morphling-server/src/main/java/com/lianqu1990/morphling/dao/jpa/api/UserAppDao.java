package com.lianqu1990.morphling.dao.jpa.api;

import com.lianqu1990.morphling.dao.jpa.entity.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author hanchao
 * @date 2017/12/5 18:29
 */
public interface UserAppDao extends JpaRepository<UserApp,Integer> {
}

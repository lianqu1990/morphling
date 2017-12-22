package com.huatu.morphling.dao.jpa.api;

import com.huatu.morphling.dao.jpa.entity.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author hanchao
 * @date 2017/12/5 18:29
 */
public interface UserAppDao extends JpaRepository<UserApp,Integer> {
}

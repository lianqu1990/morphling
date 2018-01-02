package com.lianqu1990.morphling.dao.jpa.api;

import com.lianqu1990.morphling.dao.jpa.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author hanchao
 * @date 2017/11/8 17:21
 */
public interface ClientDao extends JpaRepository<Client,Integer> {
    List<Client> findByEnv(String env);

    @Query(value = "select c.* from client c where c.id not in (select ai.client_id from app_instance ai where ai.app_id=:appId and ai.state=1) and c.env=:env",nativeQuery = true)
    List<Client> findExcludeByApp(@Param("appId") int appId,@Param("env") String env);
}

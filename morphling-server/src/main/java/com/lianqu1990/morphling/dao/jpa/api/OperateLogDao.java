package com.lianqu1990.morphling.dao.jpa.api;

import com.lianqu1990.morphling.dao.jpa.entity.OperateLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author hanchao
 * @date 2017/11/10 18:13
 */
public interface OperateLogDao extends JpaRepository<OperateLog,Integer> {
    @Query(value = "select * from operate_log where type in :type and service_id=:serviceId order by id desc limit :start,10 ",nativeQuery = true)
    List<OperateLog> findByTypeAndServiceId(@Param("type") List<Byte> type, @Param("serviceId") int serviceId,@Param("start")int start);
}

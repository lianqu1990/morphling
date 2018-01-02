package com.lianqu1990.morphling.service.local;

import com.lianqu1990.morphling.dao.jpa.api.OperateLogDao;
import com.lianqu1990.morphling.dao.jpa.entity.OperateLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hanchao
 * @date 2017/11/10 18:14
 */
@Service
public class OperateLogService extends BaseService<OperateLog,Integer> {
    @Autowired
    private OperateLogDao operateLogDao;
    @Override
    public JpaRepository<OperateLog, Integer> getDefaultDao() {
        return operateLogDao;
    }

    public List<OperateLog> findByTypeAndServiceId(List<Byte> type, int serviceId,int start){
        return operateLogDao.findByTypeAndServiceId(type,serviceId,start);
    }
}

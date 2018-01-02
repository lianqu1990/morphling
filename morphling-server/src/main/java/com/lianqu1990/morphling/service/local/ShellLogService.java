package com.lianqu1990.morphling.service.local;

import com.lianqu1990.morphling.dao.jpa.api.ShellLogDao;
import com.lianqu1990.morphling.dao.jpa.entity.ShellLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * @author hanchao
 * @date 2017/11/23 13:24
 */
@Service
public class ShellLogService extends BaseService<ShellLog,Long> {
    @Autowired
    private ShellLogDao shellLogDao;
    @Override
    public JpaRepository<ShellLog, Long> getDefaultDao() {
        return shellLogDao;
    }
}

package com.huatu.morphling.service.local;

import com.huatu.morphling.dao.jpa.api.AppInstanceDao;
import com.huatu.morphling.dao.jpa.entity.AppInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * @author hanchao
 * @date 2017/12/5 15:07
 */
@Service
public class AppInstanceService extends BaseService<AppInstance,Integer> {
    @Autowired
    private AppInstanceDao appInstanceDao;
    @Override
    public JpaRepository<AppInstance, Integer> getDefaultDao() {
        return appInstanceDao;
    }


    public void deleteLogic(int id){
        appInstanceDao.deleteLogic(id);
    }
}

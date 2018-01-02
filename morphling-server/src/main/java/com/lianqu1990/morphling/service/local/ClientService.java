package com.lianqu1990.morphling.service.local;

import com.lianqu1990.morphling.dao.jpa.api.ClientDao;
import com.lianqu1990.morphling.dao.jpa.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hanchao
 * @date 2017/11/8 17:21
 */
@Service
public class ClientService extends BaseService<Client,Integer> {
    @Autowired
    private ClientDao clientDao;

    public List<Client> findByEnv(String env){
        return clientDao.findByEnv(env);
    }
    @Override
    public JpaRepository<Client, Integer> getDefaultDao() {
        return clientDao;
    }
}

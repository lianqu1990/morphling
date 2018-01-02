package com.lianqu1990.morphling.service.local;

import com.lianqu1990.morphling.dao.jpa.api.EnvDao;
import com.lianqu1990.morphling.dao.jpa.entity.Env;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author hanchao
 * @date 2017/10/22 19:11
 */
@Service
public class EnvService extends BaseService<Env,Integer> {
    @Autowired
    private EnvDao defaultDao;

    private static Map<String,Env> envs;

    @PostConstruct
    public void init(){
        envs = findAll().stream().collect(Collectors.toMap(Env::getKey, Function.identity()));
    }

    /**
     * 无深拷贝，注意不能修改
     * @return
     */
    public Map<String, Env> getEnvs() {
        return envs;
    }


    @Override
    public JpaRepository<Env, Integer> getDefaultDao() {
        return defaultDao;
    }

    public Env.RedisDataSource getRedisDataSource(String env,int cluster){
        Map<Integer, Env.RedisDataSource> redises = envs.get(env).getProperties().getRedises();
        return redises.get(cluster);
    }

    public String getConfigPortablUrl(String env){
        return envs.get(env).getProperties().getConfigPortal();
    }
}

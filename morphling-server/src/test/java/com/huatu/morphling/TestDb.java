package com.huatu.morphling;

import com.huatu.morphling.dao.jpa.api.EnvDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author hanchao
 * @date 2017/10/21 14:26
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class TestDb {
    @Autowired
    private EnvDao envDao;
    @Test
    public void testConn(){
        System.out.println(envDao.findAll());
    }

}

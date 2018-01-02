package com.lianqu1990.morphling;

import com.lianqu1990.morphling.dao.jpa.api.EnvDao;
import org.junit.Ignore;
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
    @Ignore
    public void testConn(){
        System.out.println(envDao.findAll());
    }

}

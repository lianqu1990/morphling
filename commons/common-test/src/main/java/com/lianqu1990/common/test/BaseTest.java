package com.lianqu1990.common.test;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
/**
 * @author hanchao
 * @date 2017/9/23 10:07
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class BaseTest {
    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }
}

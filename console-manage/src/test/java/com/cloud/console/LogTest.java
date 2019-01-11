package com.cloud.console;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Frank on 2019-01-11.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
@Slf4j(topic = "manage")
public class LogTest {

    Logger logger= LoggerFactory.getLogger("manage");

    @Test
    public void test1(){
        log.info("info11111111111111111111111111111-222222222222222222222");
    }

    @Test
    public void test2(){
        logger.info("info11111111111111111111111111111-222222222222222222222");
    }
}

package com.dxy.console;

import com.dxy.console.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by Frank on 2017/8/7.
 * 应用启动加载树形菜单到redis
 */
@Component
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    IndexService indexService;

    @Override
    public void run(String... strings) throws Exception {
        redisTemplate.opsForValue().set("menus", indexService.builderMenus());
    }
}

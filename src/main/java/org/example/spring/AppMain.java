package org.example.spring;

import org.example.spring.annotation.ComponentScan;
import org.example.spring.component.MonsterDao;
import org.example.spring.component.MonsterService;
import org.example.spring.ioc.MySpringApplicationContext;
import org.example.spring.ioc.MySpringConfig;

public class AppMain {
    public static void main(String[] args) {
        MySpringApplicationContext ioc = new MySpringApplicationContext(MySpringConfig.class);
        MonsterDao monsterDao = (MonsterDao) ioc.getBean("monsterDao01");
        System.out.println(monsterDao);

        MonsterService monsterService = (MonsterService) ioc.getBean("monsterService");
        System.out.println(monsterService);
    }
}
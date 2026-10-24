package org.example.spring.component;

import org.example.spring.annotation.Autowired;
import org.example.spring.annotation.Component;
import org.example.spring.annotation.Scope;
import org.example.spring.processor.InitializingBean;

// @Component("monsterService")
@Component
@Scope("prototype")
public class MonsterService implements InitializingBean {
    @Autowired
    private MonsterDao monsterDao;

    public void save() {
        System.out.println("MonsterService save...");
        monsterDao.save();
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("MonsterService 初始化方法调用，具体业务由程序员来搞定.......");
    }
}

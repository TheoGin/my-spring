package org.example.spring.component;

import org.example.spring.annotation.Autowired;
import org.example.spring.annotation.Component;
import org.example.spring.annotation.Scope;

// @Component("monsterService")
@Component
@Scope("prototype")
public class MonsterService {
    @Autowired
    private MonsterDao monsterDao;

    public void save() {
        System.out.println("MonsterService save...");
        monsterDao.save();
    }
}

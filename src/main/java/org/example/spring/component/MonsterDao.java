package org.example.spring.component;

import org.example.spring.annotation.Component;

// @Component("monsterDao01")
@Component("monsterDao")
public class MonsterDao {
    public void save() {
        System.out.println("MonsterDao save...");
    }
}

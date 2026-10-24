package org.example.spring.component;

import org.example.spring.annotation.Component;
import org.example.spring.processor.InitializingBean;

@Component
public class Car implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Car 初始化方法调用...");
    }
}

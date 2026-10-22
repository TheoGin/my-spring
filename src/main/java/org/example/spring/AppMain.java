package org.example.spring;

import org.example.spring.annotation.ComponentScan;
import org.example.spring.ioc.MySpringApplicationContext;
import org.example.spring.ioc.MySpringConfig;

public class AppMain {
    public static void main(String[] args) {
        MySpringApplicationContext ioc = new MySpringApplicationContext(MySpringConfig.class);
    }
}
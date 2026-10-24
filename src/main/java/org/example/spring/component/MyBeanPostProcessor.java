package org.example.spring.component;

import org.example.spring.annotation.Component;
import org.example.spring.processor.BeanPostProcessor;

@Component
public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        System.out.println("MyBeanPostProcessor postProcessBeforeInitialization().. bean=" + bean + " beanName=" + beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("MyBeanPostProcessor postProcessAfterInitialization().. bean=" + bean + " beanName=" + beanName);
        return bean;
    }
}

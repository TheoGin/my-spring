package org.example.spring.ioc;

import org.apache.commons.lang.StringUtils;
import org.example.spring.annotation.Autowired;
import org.example.spring.annotation.Component;
import org.example.spring.annotation.ComponentScan;
import org.example.spring.annotation.Scope;
import org.example.spring.processor.BeanPostProcessor;
import org.example.spring.processor.InitializingBean;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

public class MySpringApplicationContext {

    private Class configClass;
    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();
    private ArrayList<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public MySpringApplicationContext(Class configClass) {
        this.configClass = configClass;
        scanBeanByConfigClass();
        // System.out.println("beanDefinitionMap: " + beanDefinitionMap);

        Enumeration<String> keys = beanDefinitionMap.keys();
        while (keys.hasMoreElements()) {
            String beanId = keys.nextElement();
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanId);
            if ("singleton".equals(beanDefinition.getScope())) {
                Object bean = createBean(beanDefinition, beanId);
                singletonObjects.put(beanId, bean);
            }
        }
        // System.out.println("singletonObjects: " + singletonObjects);
    }

    private void scanBeanByConfigClass() {
        ComponentScan componentScanAnnotation = (ComponentScan) this.configClass.getDeclaredAnnotation(ComponentScan.class);
        String scanPackage = componentScanAnnotation.value();
        System.out.println("扫描：" + scanPackage);

        ClassLoader classLoader = this.configClass.getClassLoader();
        scanPackage = scanPackage.replace(".", "/");
        URL url = classLoader.getResource(scanPackage);

        File file = new File(url.getFile());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.getName().endsWith(".class")) {
                    String className = f.getName().replace(".class", "");

                    BeanDefinition beanDefinition = new BeanDefinition();
                    String fullClassName = scanPackage.replace("/", ".") + "." + className;
                    try {
                        Class<?> clazz = classLoader.loadClass(fullClassName);
                        beanDefinition.setClazz(clazz);
                        if (clazz.isAnnotationPresent(Component.class)) {

                            // System.out.println(className + "是一个bean");
                            Component componentAnnotation = clazz.getAnnotation(Component.class);
                            if (!"".equals(componentAnnotation.value())) {
                                className = componentAnnotation.value();
                            } else {
                                className = StringUtils.uncapitalize(className);
                            }

                            if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                                beanPostProcessorList.add((BeanPostProcessor) createBean(beanDefinition, className));
                                continue;
                            }

                            if (clazz.isAnnotationPresent(Scope.class)) {
                                Scope scopeAnnotation = clazz.getAnnotation(Scope.class);
                                beanDefinition.setScope(scopeAnnotation.value());
                            } else {
                                beanDefinition.setScope("singleton");
                            }

                            beanDefinitionMap.put(className, beanDefinition);
                        } else {
                            System.out.println(className + "不是一个bean");
                        }
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public Object createBean(BeanDefinition beanDefinition, String beanId) {
        Class clazz = beanDefinition.getClazz();
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Autowired autowiredAnnotation = field.getAnnotation(Autowired.class);
                    if (autowiredAnnotation.required()) {
                        // Exception in thread "main" java.lang.RuntimeException: java.lang.IllegalAccessException: Class org.example.spring.ioc.MySpringApplicationContext can not access a member of class org.example.spring.component.MonsterService with modifiers "private"
                        field.setAccessible(true);
                        field.set(instance, getBean(field.getName()));
                    }
                }
            }

            System.out.println("=================创建了实例：" + instance + "==================");
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessBeforeInitialization(instance, beanId);
            }

            if (instance instanceof InitializingBean) {
                try {
                    ((InitializingBean)instance).afterPropertiesSet();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessAfterInitialization(instance, beanId);
            }

            return instance;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        //如果没有创建成功，返回null
        // return null;
    }

    public Object getBean(String beanId) {
        if (singletonObjects.containsKey(beanId)) {
            return singletonObjects.get(beanId);
        }

        if (beanDefinitionMap.containsKey(beanId)) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanId);
            if ("prototype".equals(beanDefinition.getScope())) {
                return createBean(beanDefinition, beanId);
            }
        }

        // Exception in thread "main" org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named 'MonsterDao' available
        throw new RuntimeException("No bean named '" + beanId + "' available");
    }
}

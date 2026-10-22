package org.example.spring.ioc;

import org.example.spring.annotation.Component;
import org.example.spring.annotation.ComponentScan;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;

public class MySpringApplicationContext {

    private Class configClass;

    public MySpringApplicationContext(Class configClass) {
        this.configClass = configClass;
        ComponentScan componentScan = (ComponentScan) this.configClass.getDeclaredAnnotation(ComponentScan.class);
        String scanPackage = componentScan.value();
        System.out.println("扫描：" + scanPackage);

        ClassLoader classLoader = configClass.getClassLoader();
        scanPackage = scanPackage.replace(".", "/");
        URL url = classLoader.getResource(scanPackage);


        File file = new File(url.getFile());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.getName().endsWith(".class")) {
                    String className = f.getName().replace(".class", "");
                    String fullClassName = scanPackage.replace("/", ".") + "." + className;
                    try {
                        Class<?> clazz = classLoader.loadClass(fullClassName);
                        if (clazz.isAnnotationPresent(Component.class)) {
                            System.out.println(className + "是一个bean");
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
}

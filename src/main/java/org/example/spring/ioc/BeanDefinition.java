package org.example.spring.ioc;

public class BeanDefinition {
    private String classFullPath;
    private String scope;

    public String getClassFullPath() {
        return classFullPath;
    }

    public void setClassFullPath(String classFullPath) {
        this.classFullPath = classFullPath;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "BeanDefinition{" +
                "classFullPath='" + classFullPath + '\'' +
                ", scope='" + scope + '\'' +
                '}';
    }
}

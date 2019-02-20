package cn.edu.just.spring;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;

/**
 * @Author: Gorilla
 * @Date: Created in 11:46 2019/2/20
 * @QQ: 904878659
 */
public class MyComponentScanner {

    private LinkedHashSet<Class> classes = new LinkedHashSet<>();

    /**
     * 查找指定包及其子包下包含特殊注解的class
     *
     * @param basePackage
     */
    public void scan(String basePackage) {
        String packageName = basePackage.replaceAll("[.]", "/");

        try {
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(packageName);

            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();

                if ("file".equals(url.getProtocol())) {

                    String path = url.getPath();

                    // 递归查找.class类型
                    findAndAddClassInPackage(basePackage, path);
                }
            }
        } catch (IOException e) {
            // 写入log

            throw new RuntimeException("加载资源出现异常", e);
        }
    }

    private void findAndAddClassInPackage(String basePackage, String path) {
        File file = new File(path);
        if ()

    }

    /**
     * 通过反射创建实例并放入到容器中
     *
     * @param container
     */
    public void createBeanWithAnnotation(HashMap container) {

    }

    /**
     * 注入依赖
     *
     * @param container
     */
    public void injectDept(HashMap container) {

    }
}

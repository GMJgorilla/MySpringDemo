package cn.edu.just.spring;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
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

        // 将包名中的"."替换成"/"
        String packageName = basePackage.replaceAll("[.]", "/");

        // 加载这个目录下的资源
        try {
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(packageName);

            while (resources.hasMoreElements()) {
                //获取资源(jar、图片、class文件、目录)
                URL url = resources.nextElement();

                // 判断文件是否是file协议，即是文件或文件夹
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

    /**
     * 递归查找.class文件并将查找到的.class保存起来
     *
     * @param basePackage
     * @param path
     */
    private void findAndAddClassInPackage(String basePackage, String path) {
        File file = new File(path);
        // 判断扫描的根目录是否存在
        if (!file.exists() || !file.isDirectory()) {
            return;
        }

        // 把这个目录下的.class和文件夹都列出来
        File[] filteredFiles = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".class") || pathname.isDirectory();
            }
        });

        for (File filteredFile : filteredFiles) {
            // 判断是否是文件夹，是就递归调用
            if (filteredFile.isDirectory()) {
                // cn.edu.just.springMVC + . + controller
                findAndAddClassInPackage(basePackage + "." + filteredFile.getName(), filteredFile.getPath());
            } else {
                // 获取文件名字，包含扩展名 UserController.class 并截取类名
                String fileName = filteredFile.getName();
                String className = fileName.substring(0, fileName.indexOf("."));

                // 将包名和类名拼接
                String packageAndClassName = basePackage + "." + className;

                //使用类加载器加载
                try {
                    Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(packageAndClassName);

                    classes.add(clazz);
                } catch (ClassNotFoundException e) {
                    new RuntimeException("类加载出现异常");
                }
            }
        }
    }

    /**
     * 通过反射创建实例并放入到容器中
     *
     * @param container
     */
    public void createBeanWithAnnotation(HashMap<String, Object> container) {

        // 循环事先查找好的classes
        for (Class clazz : classes) {

            // 判断是类上否有MyComponent注解
            if (clazz.isAnnotationPresent(MyComponent.class)) {
                try {
                    Object obj = clazz.newInstance();

                    // 获取类名 并将首字母小写，因为一般注解都是使用类名的首字母小写来默认当做注解的值
                    String simpleName = clazz.getSimpleName();
                    String beanId = lowerFirstChar(simpleName);

                    // 将创建好的实例bean加入到容器(HashMap)
                    container.put(beanId, obj);
                } catch (Exception e) {
                    throw new RuntimeException("反射创建类的实例异常", e);
                }
            }
        }
    }

    /**
     * 将类名的首字母小写，一般类名大写 在ASCII码中 大写与小写相差32
     *
     * @param simpleName
     * @return
     */
    private String lowerFirstChar(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return new String(chars);
    }

    /**
     * 注入依赖
     *
     * @param container
     */
    public void injectDept(HashMap<String, Object> container) {
        for (Class clazz : classes) {
            // 获取私有的属性
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                // 判断注解中的name值
                if (field.isAnnotationPresent(MyResource.class)) {

                    MyResource annotation = field.getAnnotation(MyResource.class);

                    // 获取注解中的name值
                    String refBeanId = annotation.value();

                    // 到容器中根据refBeanId找到依赖的bean实例
                    Object refBean = container.get(refBeanId);

                    // 获取当前bean的ID
                    String currentBeanId = lowerFirstChar(clazz.getSimpleName());

                    // 取出当前的bean实例
                    Object currentBean = container.get(currentBeanId);

                    field.setAccessible(true);

                    try {
                        // 依赖注入
                        field.set(currentBean, refBean);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

package cn.edu.just.spring;

import java.util.HashMap;

/**
 * @Author: Gorilla
 * @Date: Created in 11:20 2019/2/20
 * @QQ: 904878659
 */
public class MyApplicationContext {

    private HashMap container = new HashMap<String, Object>();

    /**
     *
     * @param basePackage
     * 在MyApplicationContext的构造器中实现了以下功能
     *
     * 1.到指定的包下查找class
     * 2.在所有的class中，扫描指定包下包含特殊注解的类
     * 3.通过反射实例化
     * 4.将实例化的bean放入到容器中(HashMap)
     * 5.注入依赖
     */
    public MyApplicationContext(String basePackage) {

        // 组件的扫描器
        MyComponentScanner scanner = new MyComponentScanner();

        // 指定路径进行扫描
        scanner.scan(basePackage);

        // 通过反射创建实例,并放入容器中
        scanner.createBeanWithAnnotation(container);

        // 依赖注入
        scanner.injectDept(container);

    }

    public Object getBean(String beanId) {
        return container.get(beanId);
    }
}

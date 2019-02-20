package cn.edu.just.test;

import cn.edu.just.spring.MyApplicationContext;

/**
 * @Author: Gorilla
 * @Date: Created in 11:15 2019/2/20
 * @QQ: 904878659
 */
public class MySpringTest {

    public static void main(String[] args) {

        // 创建一个容器
        // 指定MyApplicationContext以后扫描的包路径(basePackge)
        String basePackge = "cn.edu.just.springMVC";

        // 如果以后我定义的类上包含特殊的注解MyComponent，就交给MySpring容器管理
        MyApplicationContext context = new MyApplicationContext(basePackge);

        // 到容器中根据ID获取bean的实例
        Object obj = context.getBean("userController");

        System.out.println(obj);
    }
}

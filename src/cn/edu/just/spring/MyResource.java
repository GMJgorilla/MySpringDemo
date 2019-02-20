package cn.edu.just.spring;

import java.lang.annotation.*;

/**
 * @Author: Gorilla
 * @Date: Created in 17:59 2019/2/20
 * @QQ: 904878659
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyResource {

    public String value() default "";
}

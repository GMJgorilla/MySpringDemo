package cn.edu.just.springMVC.service.impl;

import cn.edu.just.spring.MyComponent;
import cn.edu.just.springMVC.service.UserService;

/**
 * @Author: Gorilla
 * @Date: Created in 19:15 2019/2/20
 * @QQ: 904878659
 */
@MyComponent
public class UserServiceImpl implements UserService {
    @Override
    public String test(String name) {
        return "helle" + name;
    }
}

package cn.edu.just.springMVC.controller;

import cn.edu.just.spring.MyComponent;
import cn.edu.just.spring.MyResource;
import cn.edu.just.springMVC.service.UserService;

/**
 * @Author: Gorilla
 * @Date: Created in 11:18 2019/2/20
 * @QQ: 904878659
 */

@MyComponent
public class UserController {

    @MyResource("userServiceImpl")
    private UserService userService;

    public UserService getUserService() {
        return userService;
    }
}

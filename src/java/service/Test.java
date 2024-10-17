package service;

import Spring.MyApplicationContext;

/**
 * @FileName Test
 * @Description
 * @Author fahrtwind
 * @date 2024-10-16
 **/


public class Test {
    public static void main(String[] args) {
        MyApplicationContext ac = new MyApplicationContext(AppConfig.class);

        UserService userService = (UserService) ac.getBean("userService");
    }
}

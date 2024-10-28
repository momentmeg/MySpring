package service;

import Spring.*;

/**
 * @FileName UserService
 * @Description
 * @Author fahrtwind
 * @date 2024-10-16
 **/

@Component
public class UserService implements UserInterface {

    @Autowired
    private OrderService orderService;

    //一个回调属性，用于获取beanName
    private String beanName;


    @Override
    public void test() {
        System.out.println("userService");
    }

}

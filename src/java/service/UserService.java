package service;

import Spring.Autowired;
import Spring.BeanNameAware;
import Spring.Component;
import Spring.Scope;

/**
 * @FileName UserService
 * @Description
 * @Author fahrtwind
 * @date 2024-10-16
 **/

@Component
public class UserService implements BeanNameAware {

    @Autowired
    private OrderService orderService;

    //一个回调属性，用于获取beanName
    private String beanName;


    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}

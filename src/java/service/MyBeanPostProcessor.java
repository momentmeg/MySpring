package service;

import Spring.BeanPostProcessor;
import Spring.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @FileName MyBeanPostProcessor
 * @Description
 * @Author fahrtwind
 * @date 2024-10-23 day3
 **/

@Component
public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(String beanName, Object bean) {
        if (beanName.equals("userService")) {
            System.out.println("Before---userService");
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(String beanName, Object bean) {

        if ("userService".equals(beanName)) {
            Object proxyInstance = Proxy.newProxyInstance(MyBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(),
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            System.out.println("切面逻辑");
                            //执行本来对象的方法
                            return method.invoke(bean,args);
                        }
                    });
            return proxyInstance;
        }
        return bean;
    }
}

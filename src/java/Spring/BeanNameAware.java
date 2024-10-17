package Spring;

/**
 * beanName的一个回调，用来在bean中获取beanName
 */
public interface BeanNameAware {

    void setBeanName(String beanName);

}

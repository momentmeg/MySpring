package Spring;

/**
 * @FileName BeanDefinition
 * @Description Bean对象的定义
 * @Author fahrtwind
 * @date 2024-10-17
 **/


public class BeanDefinition {

    private Class beanClass;
    private String Scope;

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    public String getScope() {
        return Scope;
    }

    public void setScope(String scope) {
        Scope = scope;
    }
}

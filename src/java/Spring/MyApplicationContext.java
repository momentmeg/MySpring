package Spring;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @FileName MyApplicationContext
 * @Description
 * @Author fahrtwind
 * @date 2024-10-16
 **/


public class MyApplicationContext {

    /**
     * 配置类参数
     */
    private Class configClass;

    //存储bean的名字和类型
    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    //单例池
    private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();


    public MyApplicationContext(Class configClass) {
        this.configClass = configClass;

        // 判断是否存在ComponentScan注解，存在返回true，否则返回false
        if (configClass.isAnnotationPresent(ComponentScan.class)) {

            //获取注解的信息
            ComponentScan annotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);

            //拿到注解的value值 扫描路径 service
            String path = annotation.value();  //此时只是一个包名
            path = path.replace(".", "/");

            //获取类的绝对路径
            ClassLoader classLoader = MyApplicationContext.class.getClassLoader();
            //根据路径的路径的一部分，返回该类的绝对路径。编译后的.class文件路径
            URL resource = classLoader.getResource(path); //G:/Programs/MySpring/MySpring/out/production/MySpring/service

            //获取路径下的文件
            File file = new File(resource.getFile());
            if (file.isDirectory()) {
                File[] files = file.listFiles(); //拿到所有文件

                //找出.class文件
                for (File f : files) {
                    //获取该行代码获取文件 f 的绝对路径
                    String fileName = f.getAbsolutePath();

                    //判断类上是否有Component注解
                    if (fileName.endsWith(".class")) {
                        //fileName去掉前缀，得到类名
                        String pre = classLoader.getResource("").getPath().substring(1);
                        pre = pre.replace("/", "\\");
                        String replace = fileName.replace(pre, "").replace("\\", ".");
                        String className = replace.substring(0, replace.indexOf(".class"));

                        try {
                            Class clazz = classLoader.loadClass(className);
                            //判断类上是否有Component注解
                            if (clazz.isAnnotationPresent(Component.class)) {
                                //获取Component的值（beanName），这里是自定义的值
                                Component component = (Component) clazz.getAnnotation(Component.class);
                                String beanName = component.value();

                                //beanName，没有定义的情况
                                if("".equals(beanName)){
                                    //Introspector 是java.beans包下的类，decapitalize会让第一个字母小写
                                    beanName = Introspector.decapitalize(clazz.getSimpleName());
                                }

                                //判断bean对象的单例，多例
                                BeanDefinition beanDefinition = new BeanDefinition();
                                beanDefinition.setBeanClass(clazz);

                                //是否有Scope注解
                                if (clazz.isAnnotationPresent(Scope.class)) {
                                    Scope ScopeAnnotation = (Scope) clazz.getAnnotation(Scope.class);
                                    beanDefinition.setScope(ScopeAnnotation.value());

                                } else {
                                    //没有则默认单例
                                    beanDefinition.setScope("singleton");
                                }

                                beanDefinitionMap.put(beanName, beanDefinition);

                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

        }


        //知道是否为单例后，就可以直接创建单例bean对象
        for (String beanName : beanDefinitionMap.keySet()) {

            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);

            if (beanDefinition.getScope().equals("singleton")) {

                Object bean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName,bean);
            }

        }
    }








    /**
     * 创建单例bean方法
     */
    private Object createBean(String beanName,BeanDefinition beanDefinition)
    {
        //获取bean的类
        Class clazz = beanDefinition.getBeanClass();

        try {
            //通过反射得到对象
            Object instance = clazz.getConstructor().newInstance();

            //属性填充，@Autowired依赖注入的的实现
            for (Field f : clazz.getDeclaredFields()) {
                //是否有Autowired注解
                if(f.isAnnotationPresent(Autowired.class)){
                    //打开权限
                    f.setAccessible(true);
                    //为该属性赋上新值
                    f.set(instance,getBean(f.getName()));
                }
            }

            //BeanNameAware的回调
            //判断instance是否实现了BeanNameAware的接口
            if(instance instanceof BeanNameAware)
            {
                ((BeanNameAware) instance).setBeanName(beanName);
            }



            return instance;

        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

//        return null;
    }





    /**
     * 获取bean对象.
     *
     * @param beanName
     * @return
     */
    public Object getBean(String beanName) {

        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);

        //如果为空，beanName有误或者，没有这个bean
        if (beanDefinition == null) {
            throw new NullPointerException();
        } else { //否则创建bean对象
            String Scope = beanDefinition.getScope();

            if (Scope.equals("singleton")) { //如果是单例直接在单例池里拿
                Object bean = singletonObjects.get(beanName);

                if(bean == null) // 如果bean为null，说明有这个bean但没在单例池中，于是再创建
                {
                    bean = createBean(beanName,beanDefinition);
                    singletonObjects.put(beanName,bean);
                }

                return bean;

            } else {
                return createBean(beanName,beanDefinition);
            }
        }


    }


}

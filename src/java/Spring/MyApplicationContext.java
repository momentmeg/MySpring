package Spring;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;

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


    public MyApplicationContext(Class configClass) {
        this.configClass = configClass;

        if (configClass.isAnnotationPresent(ComponentScan.class)) { // 判断是否存在ComponentScan注解，存在返回true，否则返回false

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
                    if(fileName.endsWith(".class")){
                        //fileName去掉前缀，得到类名
                        String pre = classLoader.getResource("").getPath().substring(1);
                        pre = pre.replace("/", "\\");
                        String replace = fileName.replace(pre, "").replace("\\",".");
                        String className = replace.substring(0,replace.indexOf(".class"));
                        System.out.println(className);

                        try{
                            Class clazz = classLoader.loadClass(className);
                            //判断类上是否有Component注解
                            if(clazz.isAnnotationPresent(Component.class)){
                                //拿到注解的信息
                                Component annotation1 = (Component) clazz.getAnnotation(Component.class);
                                //拿到注解的value值
                                String value = annotation1.value();
                                //将类名与beanName进行绑定
                                //beanName与bean的实例进行绑定
                           }
                        }catch (ClassNotFoundException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }

            }

        }

    }

    /**
     * 获取bean对象.
     *
     * @param beanName
     * @return
     */
    public Object getBean(String beanName) {
        return null;
    }


}

package Spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) //生效时间：运行时生效
@Target(ElementType.TYPE) //只作用在类上
public @interface Scope {

    //Bean对象的单例，多例  prototype singleton
    String value() default "";
}

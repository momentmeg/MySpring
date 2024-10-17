package Spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) //生效时间：运行时生效
@Target(ElementType.FIELD) //只作用在字段上
public @interface Autowired {


}

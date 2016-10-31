package Annotation;

import javax.annotation.Generated;
import java.lang.annotation.*;

/**
 * Created by Administrator on 16-10-25.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
     String value() default "";
}

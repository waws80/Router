package pw.androidthanatos.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Modules
 *
 * @author liuxiongfei
 *         2017/10/27
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Modules {

    String[] value();
}

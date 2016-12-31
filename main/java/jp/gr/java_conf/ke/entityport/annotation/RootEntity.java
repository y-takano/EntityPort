package jp.gr.java_conf.ke.entityport.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RootEntity {

	Lifecycle lifecycle() default Lifecycle.PERSISTENT;

	boolean restoration() default true;

	String classVersion() default "1.0.0";
}

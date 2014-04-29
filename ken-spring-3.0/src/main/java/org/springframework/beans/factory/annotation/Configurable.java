package org.springframework.beans.factory.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
@Documented
@Inherited
public @interface Configurable
{
  String value() default "";
  
  Autowire autowire() default Autowire.NO;
  
  boolean dependencyCheck() default false;
  
  boolean preConstruction() default false;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.annotation.Configurable
 * JD-Core Version:    0.7.0.1
 */
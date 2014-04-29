package org.springframework.jmx.export.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ManagedResource
{
  String value() default "";
  
  String objectName() default "";
  
  String description() default "";
  
  int currencyTimeLimit() default -1;
  
  boolean log() default false;
  
  String logFile() default "";
  
  String persistPolicy() default "";
  
  int persistPeriod() default -1;
  
  String persistName() default "";
  
  String persistLocation() default "";
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.annotation.ManagedResource
 * JD-Core Version:    0.7.0.1
 */
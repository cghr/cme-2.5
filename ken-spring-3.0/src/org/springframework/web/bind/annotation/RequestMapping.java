package org.springframework.web.bind.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface RequestMapping
{
  String[] value() default {};
  
  RequestMethod[] method() default {};
  
  String[] params() default {};
  
  String[] headers() default {};
  
  String[] consumes() default {};
  
  String[] produces() default {};
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.bind.annotation.RequestMapping
 * JD-Core Version:    0.7.0.1
 */
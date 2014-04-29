package org.springframework.context.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({AspectJAutoProxyConfigurationSelector.class})
@Documented
public @interface EnableAspectJAutoProxy
{
  boolean proxyTargetClass() default false;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.EnableAspectJAutoProxy
 * JD-Core Version:    0.7.0.1
 */
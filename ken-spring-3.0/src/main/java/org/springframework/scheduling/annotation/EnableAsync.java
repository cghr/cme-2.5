package org.springframework.scheduling.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({AsyncConfigurationSelector.class})
@Documented
public @interface EnableAsync
{
  Class<? extends Annotation> annotation() default Annotation.class;
  
  boolean proxyTargetClass() default false;
  
  AdviceMode mode() default AdviceMode.PROXY;
  
  int order() default 2147483647;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.annotation.EnableAsync
 * JD-Core Version:    0.7.0.1
 */
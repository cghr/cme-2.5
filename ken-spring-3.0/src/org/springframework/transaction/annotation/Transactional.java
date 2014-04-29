package org.springframework.transaction.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Transactional
{
  String value() default "";
  
  Propagation propagation() default Propagation.REQUIRED;
  
  Isolation isolation() default Isolation.DEFAULT;
  
  int timeout() default -1;
  
  boolean readOnly() default false;
  
  Class<? extends Throwable>[] rollbackFor() default {};
  
  String[] rollbackForClassName() default {};
  
  Class<? extends Throwable>[] noRollbackFor() default {};
  
  String[] noRollbackForClassName() default {};
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.annotation.Transactional
 * JD-Core Version:    0.7.0.1
 */
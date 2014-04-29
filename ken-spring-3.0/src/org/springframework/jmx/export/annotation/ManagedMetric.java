package org.springframework.jmx.export.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.jmx.support.MetricType;

@Target({java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ManagedMetric
{
  String category() default "";
  
  int currencyTimeLimit() default -1;
  
  String description() default "";
  
  String displayName() default "";
  
  MetricType metricType() default MetricType.GAUGE;
  
  int persistPeriod() default -1;
  
  String persistPolicy() default "";
  
  String unit() default "";
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.annotation.ManagedMetric
 * JD-Core Version:    0.7.0.1
 */
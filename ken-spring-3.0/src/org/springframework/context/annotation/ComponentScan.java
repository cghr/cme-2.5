package org.springframework.context.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.beans.factory.support.BeanNameGenerator;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
@Documented
public @interface ComponentScan
{
  String[] value() default {};
  
  String[] basePackages() default {};
  
  Class<?>[] basePackageClasses() default {};
  
  Class<? extends BeanNameGenerator> nameGenerator() default AnnotationBeanNameGenerator.class;
  
  Class<? extends ScopeMetadataResolver> scopeResolver() default AnnotationScopeMetadataResolver.class;
  
  ScopedProxyMode scopedProxy() default ScopedProxyMode.DEFAULT;
  
  String resourcePattern() default "**/*.class";
  
  boolean useDefaultFilters() default true;
  
  Filter[] includeFilters() default {};
  
  Filter[] excludeFilters() default {};
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Filter
  {
    FilterType type() default FilterType.ANNOTATION;
    
    Class<?> value();
  }
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.ComponentScan
 * JD-Core Version:    0.7.0.1
 */
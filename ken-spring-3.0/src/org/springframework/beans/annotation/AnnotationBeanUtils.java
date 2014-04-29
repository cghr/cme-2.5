/*  1:   */ package org.springframework.beans.annotation;
/*  2:   */ 
/*  3:   */ import java.lang.annotation.Annotation;
/*  4:   */ import java.lang.reflect.Method;
/*  5:   */ import java.util.Arrays;
/*  6:   */ import java.util.Collection;
/*  7:   */ import java.util.HashSet;
/*  8:   */ import java.util.Set;
/*  9:   */ import org.springframework.beans.BeanWrapper;
/* 10:   */ import org.springframework.beans.PropertyAccessorFactory;
/* 11:   */ import org.springframework.util.ReflectionUtils;
/* 12:   */ 
/* 13:   */ public abstract class AnnotationBeanUtils
/* 14:   */ {
/* 15:   */   public static void copyPropertiesToBean(Annotation ann, Object bean, String... excludedProperties)
/* 16:   */   {
/* 17:44 */     Set<String> excluded = new HashSet((Collection)Arrays.asList(excludedProperties));
/* 18:45 */     Method[] annotationProperties = ann.annotationType().getDeclaredMethods();
/* 19:46 */     BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(bean);
/* 20:47 */     for (Method annotationProperty : annotationProperties)
/* 21:   */     {
/* 22:48 */       String propertyName = annotationProperty.getName();
/* 23:49 */       if ((!excluded.contains(propertyName)) && (bw.isWritableProperty(propertyName)))
/* 24:   */       {
/* 25:50 */         Object value = ReflectionUtils.invokeMethod(annotationProperty, ann);
/* 26:51 */         bw.setPropertyValue(propertyName, value);
/* 27:   */       }
/* 28:   */     }
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.annotation.AnnotationBeanUtils
 * JD-Core Version:    0.7.0.1
 */
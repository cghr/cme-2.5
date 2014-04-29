/*  1:   */ package org.springframework.context.annotation;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.Method;
/*  4:   */ import org.springframework.core.annotation.AnnotationUtils;
/*  5:   */ 
/*  6:   */ class BeanAnnotationHelper
/*  7:   */ {
/*  8:   */   public static boolean isBeanAnnotated(Method method)
/*  9:   */   {
/* 10:35 */     return AnnotationUtils.findAnnotation(method, Bean.class) != null;
/* 11:   */   }
/* 12:   */   
/* 13:   */   public static String determineBeanNameFor(Method beanMethod)
/* 14:   */   {
/* 15:40 */     String beanName = beanMethod.getName();
/* 16:   */     
/* 17:   */ 
/* 18:43 */     Bean bean = (Bean)AnnotationUtils.findAnnotation(beanMethod, Bean.class);
/* 19:44 */     if ((bean != null) && (bean.name().length > 0)) {
/* 20:45 */       beanName = bean.name()[0];
/* 21:   */     }
/* 22:48 */     return beanName;
/* 23:   */   }
/* 24:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.BeanAnnotationHelper
 * JD-Core Version:    0.7.0.1
 */
/*  1:   */ package org.springframework.web.servlet.mvc.support;
/*  2:   */ 
/*  3:   */ import org.springframework.core.annotation.AnnotationUtils;
/*  4:   */ import org.springframework.stereotype.Controller;
/*  5:   */ 
/*  6:   */ class AnnotationControllerTypePredicate
/*  7:   */   extends ControllerTypePredicate
/*  8:   */ {
/*  9:   */   public boolean isControllerType(Class beanClass)
/* 10:   */   {
/* 11:34 */     return (super.isControllerType(beanClass)) || (AnnotationUtils.findAnnotation(beanClass, Controller.class) != null);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public boolean isMultiActionControllerType(Class beanClass)
/* 15:   */   {
/* 16:40 */     return (super.isMultiActionControllerType(beanClass)) || (AnnotationUtils.findAnnotation(beanClass, Controller.class) != null);
/* 17:   */   }
/* 18:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.support.AnnotationControllerTypePredicate
 * JD-Core Version:    0.7.0.1
 */
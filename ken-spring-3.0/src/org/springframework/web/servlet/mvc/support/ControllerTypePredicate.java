/*  1:   */ package org.springframework.web.servlet.mvc.support;
/*  2:   */ 
/*  3:   */ import org.springframework.web.servlet.mvc.Controller;
/*  4:   */ import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
/*  5:   */ 
/*  6:   */ class ControllerTypePredicate
/*  7:   */ {
/*  8:   */   public boolean isControllerType(Class beanClass)
/*  9:   */   {
/* 10:31 */     return Controller.class.isAssignableFrom(beanClass);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public boolean isMultiActionControllerType(Class beanClass)
/* 14:   */   {
/* 15:35 */     return MultiActionController.class.isAssignableFrom(beanClass);
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.support.ControllerTypePredicate
 * JD-Core Version:    0.7.0.1
 */
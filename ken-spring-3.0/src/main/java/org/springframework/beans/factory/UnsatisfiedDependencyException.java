/*  1:   */ package org.springframework.beans.factory;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.BeansException;
/*  4:   */ import org.springframework.util.ClassUtils;
/*  5:   */ 
/*  6:   */ public class UnsatisfiedDependencyException
/*  7:   */   extends BeanCreationException
/*  8:   */ {
/*  9:   */   public UnsatisfiedDependencyException(String resourceDescription, String beanName, String propertyName, String msg)
/* 10:   */   {
/* 11:44 */     super(resourceDescription, beanName, "Unsatisfied dependency expressed through bean property '" + propertyName + "'" + (
/* 12:45 */       msg != null ? ": " + msg : ""));
/* 13:   */   }
/* 14:   */   
/* 15:   */   public UnsatisfiedDependencyException(String resourceDescription, String beanName, String propertyName, BeansException ex)
/* 16:   */   {
/* 17:58 */     this(resourceDescription, beanName, propertyName, ex != null ? ": " + ex.getMessage() : "");
/* 18:59 */     initCause(ex);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public UnsatisfiedDependencyException(String resourceDescription, String beanName, int ctorArgIndex, Class ctorArgType, String msg)
/* 22:   */   {
/* 23:74 */     super(resourceDescription, beanName, "Unsatisfied dependency expressed through constructor argument with index " + 
/* 24:75 */       ctorArgIndex + " of type [" + ClassUtils.getQualifiedName(ctorArgType) + "]" + (
/* 25:76 */       msg != null ? ": " + msg : ""));
/* 26:   */   }
/* 27:   */   
/* 28:   */   public UnsatisfiedDependencyException(String resourceDescription, String beanName, int ctorArgIndex, Class ctorArgType, BeansException ex)
/* 29:   */   {
/* 30:90 */     this(resourceDescription, beanName, ctorArgIndex, ctorArgType, ex != null ? ": " + ex.getMessage() : "");
/* 31:91 */     initCause(ex);
/* 32:   */   }
/* 33:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.UnsatisfiedDependencyException
 * JD-Core Version:    0.7.0.1
 */
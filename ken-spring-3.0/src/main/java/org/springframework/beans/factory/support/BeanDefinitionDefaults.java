/*  1:   */ package org.springframework.beans.factory.support;
/*  2:   */ 
/*  3:   */ import org.springframework.util.StringUtils;
/*  4:   */ 
/*  5:   */ public class BeanDefinitionDefaults
/*  6:   */ {
/*  7:   */   private boolean lazyInit;
/*  8:31 */   private int dependencyCheck = 0;
/*  9:33 */   private int autowireMode = 0;
/* 10:   */   private String initMethodName;
/* 11:   */   private String destroyMethodName;
/* 12:   */   
/* 13:   */   public void setLazyInit(boolean lazyInit)
/* 14:   */   {
/* 15:41 */     this.lazyInit = lazyInit;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public boolean isLazyInit()
/* 19:   */   {
/* 20:45 */     return this.lazyInit;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public void setDependencyCheck(int dependencyCheck)
/* 24:   */   {
/* 25:49 */     this.dependencyCheck = dependencyCheck;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public int getDependencyCheck()
/* 29:   */   {
/* 30:53 */     return this.dependencyCheck;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public void setAutowireMode(int autowireMode)
/* 34:   */   {
/* 35:57 */     this.autowireMode = autowireMode;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public int getAutowireMode()
/* 39:   */   {
/* 40:61 */     return this.autowireMode;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public void setInitMethodName(String initMethodName)
/* 44:   */   {
/* 45:65 */     this.initMethodName = (StringUtils.hasText(initMethodName) ? initMethodName : null);
/* 46:   */   }
/* 47:   */   
/* 48:   */   public String getInitMethodName()
/* 49:   */   {
/* 50:69 */     return this.initMethodName;
/* 51:   */   }
/* 52:   */   
/* 53:   */   public void setDestroyMethodName(String destroyMethodName)
/* 54:   */   {
/* 55:73 */     this.destroyMethodName = (StringUtils.hasText(destroyMethodName) ? destroyMethodName : null);
/* 56:   */   }
/* 57:   */   
/* 58:   */   public String getDestroyMethodName()
/* 59:   */   {
/* 60:77 */     return this.destroyMethodName;
/* 61:   */   }
/* 62:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.BeanDefinitionDefaults
 * JD-Core Version:    0.7.0.1
 */
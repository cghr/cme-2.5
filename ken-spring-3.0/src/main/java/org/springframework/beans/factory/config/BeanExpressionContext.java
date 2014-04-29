/*  1:   */ package org.springframework.beans.factory.config;
/*  2:   */ 
/*  3:   */ import org.springframework.util.Assert;
/*  4:   */ 
/*  5:   */ public class BeanExpressionContext
/*  6:   */ {
/*  7:   */   private final ConfigurableBeanFactory beanFactory;
/*  8:   */   private final Scope scope;
/*  9:   */   
/* 10:   */   public BeanExpressionContext(ConfigurableBeanFactory beanFactory, Scope scope)
/* 11:   */   {
/* 12:35 */     Assert.notNull(beanFactory, "BeanFactory must not be null");
/* 13:36 */     this.beanFactory = beanFactory;
/* 14:37 */     this.scope = scope;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public final ConfigurableBeanFactory getBeanFactory()
/* 18:   */   {
/* 19:41 */     return this.beanFactory;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public final Scope getScope()
/* 23:   */   {
/* 24:45 */     return this.scope;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public boolean containsObject(String key)
/* 28:   */   {
/* 29:51 */     return (this.beanFactory.containsBean(key)) || ((this.scope != null) && (this.scope.resolveContextualObject(key) != null));
/* 30:   */   }
/* 31:   */   
/* 32:   */   public Object getObject(String key)
/* 33:   */   {
/* 34:55 */     if (this.beanFactory.containsBean(key)) {
/* 35:56 */       return this.beanFactory.getBean(key);
/* 36:   */     }
/* 37:58 */     if (this.scope != null) {
/* 38:59 */       return this.scope.resolveContextualObject(key);
/* 39:   */     }
/* 40:62 */     return null;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public boolean equals(Object other)
/* 44:   */   {
/* 45:69 */     if (this == other) {
/* 46:70 */       return true;
/* 47:   */     }
/* 48:72 */     if (!(other instanceof BeanExpressionContext)) {
/* 49:73 */       return false;
/* 50:   */     }
/* 51:75 */     BeanExpressionContext otherContext = (BeanExpressionContext)other;
/* 52:76 */     return (this.beanFactory == otherContext.beanFactory) && (this.scope == otherContext.scope);
/* 53:   */   }
/* 54:   */   
/* 55:   */   public int hashCode()
/* 56:   */   {
/* 57:81 */     return this.beanFactory.hashCode();
/* 58:   */   }
/* 59:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.BeanExpressionContext
 * JD-Core Version:    0.7.0.1
 */
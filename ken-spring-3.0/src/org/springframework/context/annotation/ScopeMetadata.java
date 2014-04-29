/*  1:   */ package org.springframework.context.annotation;
/*  2:   */ 
/*  3:   */ import org.springframework.util.Assert;
/*  4:   */ 
/*  5:   */ public class ScopeMetadata
/*  6:   */ {
/*  7:37 */   private String scopeName = "singleton";
/*  8:39 */   private ScopedProxyMode scopedProxyMode = ScopedProxyMode.NO;
/*  9:   */   
/* 10:   */   public void setScopeName(String scopeName)
/* 11:   */   {
/* 12:46 */     Assert.notNull(scopeName, "'scopeName' must not be null");
/* 13:47 */     this.scopeName = scopeName;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public String getScopeName()
/* 17:   */   {
/* 18:54 */     return this.scopeName;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public void setScopedProxyMode(ScopedProxyMode scopedProxyMode)
/* 22:   */   {
/* 23:61 */     Assert.notNull(scopedProxyMode, "'scopedProxyMode' must not be null");
/* 24:62 */     this.scopedProxyMode = scopedProxyMode;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public ScopedProxyMode getScopedProxyMode()
/* 28:   */   {
/* 29:69 */     return this.scopedProxyMode;
/* 30:   */   }
/* 31:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.ScopeMetadata
 * JD-Core Version:    0.7.0.1
 */
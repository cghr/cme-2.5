/*  1:   */ package org.springframework.cache.interceptor;
/*  2:   */ 
/*  3:   */ public class CacheEvictOperation
/*  4:   */   extends CacheOperation
/*  5:   */ {
/*  6:27 */   private boolean cacheWide = false;
/*  7:   */   
/*  8:   */   public void setCacheWide(boolean cacheWide)
/*  9:   */   {
/* 10:30 */     this.cacheWide = cacheWide;
/* 11:   */   }
/* 12:   */   
/* 13:   */   public boolean isCacheWide()
/* 14:   */   {
/* 15:34 */     return this.cacheWide;
/* 16:   */   }
/* 17:   */   
/* 18:   */   protected StringBuilder getOperationDescription()
/* 19:   */   {
/* 20:39 */     StringBuilder sb = super.getOperationDescription();
/* 21:40 */     sb.append(",");
/* 22:41 */     sb.append(this.cacheWide);
/* 23:42 */     return sb;
/* 24:   */   }
/* 25:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.interceptor.CacheEvictOperation
 * JD-Core Version:    0.7.0.1
 */
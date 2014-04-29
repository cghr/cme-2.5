/*  1:   */ package org.springframework.cache.interceptor;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.Method;
/*  4:   */ 
/*  5:   */ public class DefaultKeyGenerator
/*  6:   */   implements KeyGenerator
/*  7:   */ {
/*  8:   */   public Object extract(Object target, Method method, Object... params)
/*  9:   */   {
/* 10:34 */     if (params.length == 1) {
/* 11:35 */       return params[0] == null ? Integer.valueOf(53) : params[0];
/* 12:   */     }
/* 13:37 */     if (params.length == 0) {
/* 14:38 */       return Integer.valueOf(0);
/* 15:   */     }
/* 16:40 */     int hashCode = 17;
/* 17:41 */     for (Object object : params) {
/* 18:42 */       hashCode = 31 * hashCode + (object == null ? 53 : object.hashCode());
/* 19:   */     }
/* 20:44 */     return Integer.valueOf(hashCode);
/* 21:   */   }
/* 22:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.interceptor.DefaultKeyGenerator
 * JD-Core Version:    0.7.0.1
 */
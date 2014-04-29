/*  1:   */ package org.springframework.web.bind.support;
/*  2:   */ 
/*  3:   */ import org.springframework.core.MethodParameter;
/*  4:   */ import org.springframework.web.context.request.NativeWebRequest;
/*  5:   */ 
/*  6:   */ public abstract interface WebArgumentResolver
/*  7:   */ {
/*  8:51 */   public static final Object UNRESOLVED = new Object();
/*  9:   */   
/* 10:   */   public abstract Object resolveArgument(MethodParameter paramMethodParameter, NativeWebRequest paramNativeWebRequest)
/* 11:   */     throws Exception;
/* 12:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.bind.support.WebArgumentResolver
 * JD-Core Version:    0.7.0.1
 */
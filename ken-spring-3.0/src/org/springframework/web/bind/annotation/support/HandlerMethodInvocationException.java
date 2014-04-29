/*  1:   */ package org.springframework.web.bind.annotation.support;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.Method;
/*  4:   */ import org.springframework.core.NestedRuntimeException;
/*  5:   */ 
/*  6:   */ public class HandlerMethodInvocationException
/*  7:   */   extends NestedRuntimeException
/*  8:   */ {
/*  9:   */   public HandlerMethodInvocationException(Method handlerMethod, Throwable cause)
/* 10:   */   {
/* 11:38 */     super("Failed to invoke handler method [" + handlerMethod + "]", cause);
/* 12:   */   }
/* 13:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.bind.annotation.support.HandlerMethodInvocationException
 * JD-Core Version:    0.7.0.1
 */
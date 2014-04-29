/*  1:   */ package org.springframework.core.serializer.support;
/*  2:   */ 
/*  3:   */ import org.springframework.core.NestedRuntimeException;
/*  4:   */ 
/*  5:   */ public class SerializationFailedException
/*  6:   */   extends NestedRuntimeException
/*  7:   */ {
/*  8:   */   public SerializationFailedException(String message)
/*  9:   */   {
/* 10:38 */     super(message);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public SerializationFailedException(String message, Throwable cause)
/* 14:   */   {
/* 15:48 */     super(message, cause);
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.serializer.support.SerializationFailedException
 * JD-Core Version:    0.7.0.1
 */
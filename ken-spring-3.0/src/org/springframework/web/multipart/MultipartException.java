/*  1:   */ package org.springframework.web.multipart;
/*  2:   */ 
/*  3:   */ import org.springframework.core.NestedRuntimeException;
/*  4:   */ 
/*  5:   */ public class MultipartException
/*  6:   */   extends NestedRuntimeException
/*  7:   */ {
/*  8:   */   public MultipartException(String msg)
/*  9:   */   {
/* 10:37 */     super(msg);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public MultipartException(String msg, Throwable cause)
/* 14:   */   {
/* 15:46 */     super(msg, cause);
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.multipart.MultipartException
 * JD-Core Version:    0.7.0.1
 */
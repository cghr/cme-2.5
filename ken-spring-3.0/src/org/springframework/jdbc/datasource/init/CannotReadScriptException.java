/*  1:   */ package org.springframework.jdbc.datasource.init;
/*  2:   */ 
/*  3:   */ import org.springframework.core.io.support.EncodedResource;
/*  4:   */ 
/*  5:   */ public class CannotReadScriptException
/*  6:   */   extends RuntimeException
/*  7:   */ {
/*  8:   */   public CannotReadScriptException(EncodedResource resource, Throwable cause)
/*  9:   */   {
/* 10:36 */     super("Cannot read SQL script from " + resource, cause);
/* 11:   */   }
/* 12:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.init.CannotReadScriptException
 * JD-Core Version:    0.7.0.1
 */
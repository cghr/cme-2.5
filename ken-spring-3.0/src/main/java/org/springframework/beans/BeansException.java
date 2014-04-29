/*  1:   */ package org.springframework.beans;
/*  2:   */ 
/*  3:   */ import org.springframework.core.NestedRuntimeException;
/*  4:   */ import org.springframework.util.ObjectUtils;
/*  5:   */ 
/*  6:   */ public abstract class BeansException
/*  7:   */   extends NestedRuntimeException
/*  8:   */ {
/*  9:   */   public BeansException(String msg)
/* 10:   */   {
/* 11:39 */     super(msg);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public BeansException(String msg, Throwable cause)
/* 15:   */   {
/* 16:49 */     super(msg, cause);
/* 17:   */   }
/* 18:   */   
/* 19:   */   public boolean equals(Object other)
/* 20:   */   {
/* 21:55 */     if (this == other) {
/* 22:56 */       return true;
/* 23:   */     }
/* 24:58 */     if (!(other instanceof BeansException)) {
/* 25:59 */       return false;
/* 26:   */     }
/* 27:61 */     BeansException otherBe = (BeansException)other;
/* 28:   */     
/* 29:63 */     return (getMessage().equals(otherBe.getMessage())) && (ObjectUtils.nullSafeEquals(getCause(), otherBe.getCause()));
/* 30:   */   }
/* 31:   */   
/* 32:   */   public int hashCode()
/* 33:   */   {
/* 34:68 */     return getMessage().hashCode();
/* 35:   */   }
/* 36:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.BeansException
 * JD-Core Version:    0.7.0.1
 */
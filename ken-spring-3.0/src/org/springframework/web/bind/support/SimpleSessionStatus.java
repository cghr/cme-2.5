/*  1:   */ package org.springframework.web.bind.support;
/*  2:   */ 
/*  3:   */ public class SimpleSessionStatus
/*  4:   */   implements SessionStatus
/*  5:   */ {
/*  6:28 */   private boolean complete = false;
/*  7:   */   
/*  8:   */   public void setComplete()
/*  9:   */   {
/* 10:32 */     this.complete = true;
/* 11:   */   }
/* 12:   */   
/* 13:   */   public boolean isComplete()
/* 14:   */   {
/* 15:36 */     return this.complete;
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.bind.support.SimpleSessionStatus
 * JD-Core Version:    0.7.0.1
 */
/*  1:   */ package org.springframework.beans.factory.parsing;
/*  2:   */ 
/*  3:   */ import org.springframework.util.Assert;
/*  4:   */ 
/*  5:   */ public class ConstructorArgumentEntry
/*  6:   */   implements ParseState.Entry
/*  7:   */ {
/*  8:   */   private final int index;
/*  9:   */   
/* 10:   */   public ConstructorArgumentEntry()
/* 11:   */   {
/* 12:39 */     this.index = -1;
/* 13:   */   }
/* 14:   */   
/* 15:   */   public ConstructorArgumentEntry(int index)
/* 16:   */   {
/* 17:50 */     Assert.isTrue(index >= 0, "Constructor argument index must be greater than or equal to zero");
/* 18:51 */     this.index = index;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public String toString()
/* 22:   */   {
/* 23:57 */     return "Constructor-arg" + (this.index >= 0 ? " #" + this.index : "");
/* 24:   */   }
/* 25:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.ConstructorArgumentEntry
 * JD-Core Version:    0.7.0.1
 */
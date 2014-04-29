/*  1:   */ package org.springframework.beans.factory.parsing;
/*  2:   */ 
/*  3:   */ import org.springframework.util.StringUtils;
/*  4:   */ 
/*  5:   */ public class QualifierEntry
/*  6:   */   implements ParseState.Entry
/*  7:   */ {
/*  8:   */   private String typeName;
/*  9:   */   
/* 10:   */   public QualifierEntry(String typeName)
/* 11:   */   {
/* 12:33 */     if (!StringUtils.hasText(typeName)) {
/* 13:34 */       throw new IllegalArgumentException("Invalid qualifier type '" + typeName + "'.");
/* 14:   */     }
/* 15:36 */     this.typeName = typeName;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public String toString()
/* 19:   */   {
/* 20:41 */     return "Qualifier '" + this.typeName + "'";
/* 21:   */   }
/* 22:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.QualifierEntry
 * JD-Core Version:    0.7.0.1
 */
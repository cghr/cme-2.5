/*  1:   */ package org.springframework.beans.factory.parsing;
/*  2:   */ 
/*  3:   */ import org.springframework.util.StringUtils;
/*  4:   */ 
/*  5:   */ public class PropertyEntry
/*  6:   */   implements ParseState.Entry
/*  7:   */ {
/*  8:   */   private final String name;
/*  9:   */   
/* 10:   */   public PropertyEntry(String name)
/* 11:   */   {
/* 12:39 */     if (!StringUtils.hasText(name)) {
/* 13:40 */       throw new IllegalArgumentException("Invalid property name '" + name + "'.");
/* 14:   */     }
/* 15:42 */     this.name = name;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public String toString()
/* 19:   */   {
/* 20:48 */     return "Property '" + this.name + "'";
/* 21:   */   }
/* 22:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.PropertyEntry
 * JD-Core Version:    0.7.0.1
 */
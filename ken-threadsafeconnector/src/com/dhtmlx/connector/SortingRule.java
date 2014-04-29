/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ public class SortingRule
/*  4:   */ {
/*  5:   */   public String name;
/*  6:   */   public String direction;
/*  7:   */   
/*  8:   */   public SortingRule(String name, String direction)
/*  9:   */   {
/* 10:24 */     if (direction.equals("")) {
/* 11:25 */       direction = "ASC";
/* 12:   */     } else {
/* 13:27 */       direction = direction.toLowerCase().equals("asc") ? "ASC" : "DESC";
/* 14:   */     }
/* 15:29 */     this.name = name;
/* 16:30 */     this.direction = direction;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public String to_sql()
/* 20:   */   {
/* 21:39 */     return this.name + " " + this.direction;
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.SortingRule
 * JD-Core Version:    0.7.0.1
 */
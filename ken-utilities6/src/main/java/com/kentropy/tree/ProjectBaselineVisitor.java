/*  1:   */ package com.kentropy.tree;
/*  2:   */ 
/*  3:   */ import java.util.Map;
/*  4:   */ import java.util.Map.Entry;
/*  5:   */ 
/*  6:   */ public class ProjectBaselineVisitor
/*  7:   */   implements TreeVisitor
/*  8:   */ {
/*  9:   */   Map<String, Object> map;
/* 10:   */   
/* 11:   */   public ProjectBaselineVisitor(Map<String, Object> map)
/* 12:   */   {
/* 13:12 */     this.map = map;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public static void main(String[] args) {}
/* 17:   */   
/* 18:   */   public void doTask(DBNode dbn)
/* 19:   */   {
/* 20:25 */     for (Map.Entry<String, Object> e : this.map.entrySet()) {
/* 21:27 */       dbn.data.put((String)e.getKey(), e.getValue());
/* 22:   */     }
/* 23:   */   }
/* 24:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-utilities6\ken-utilities6.jar
 * Qualified Name:     com.kentropy.tree.ProjectBaselineVisitor
 * JD-Core Version:    0.7.0.1
 */
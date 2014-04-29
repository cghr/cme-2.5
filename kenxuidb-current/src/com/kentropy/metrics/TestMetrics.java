/*  1:   */ package com.kentropy.metrics;
/*  2:   */ 
/*  3:   */ import net.xoetrope.optional.data.sql.DatabaseTableModel;
/*  4:   */ import net.xoetrope.xui.data.XModel;
/*  5:   */ 
/*  6:   */ public class TestMetrics
/*  7:   */ {
/*  8:   */   public static void main(String[] args) {}
/*  9:   */   
/* 10:   */   public static void getMetric(String name, String constraints)
/* 11:   */   {
/* 12:14 */     DatabaseTableModel dtm = new DatabaseTableModel();
/* 13:15 */     dtm.setupTable("metrics", "*", " name='" + name + "'", "test", true);
/* 14:16 */     dtm.retrieve();
/* 15:17 */     if (dtm.getNumChildren() > 0) {
/* 16:19 */       String str = dtm.get(0).get("query").toString();
/* 17:   */     }
/* 18:   */   }
/* 19:   */   
/* 20:   */   public static void executeMetricQry(String query, String constraints)
/* 21:   */   {
/* 22:25 */     throw new Error("Unresolved compilation problems: \n\targ1 cannot be resolved to a variable\n\targ2 cannot be resolved to a variable\n\tSyntax error, insert \";\" to complete Statement\n");
/* 23:   */   }
/* 24:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.metrics.TestMetrics
 * JD-Core Version:    0.7.0.1
 */
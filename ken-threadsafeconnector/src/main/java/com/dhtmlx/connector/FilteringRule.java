/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ public class FilteringRule
/*  4:   */ {
/*  5:   */   public String name;
/*  6:   */   public String operation;
/*  7:   */   public String value;
/*  8:   */   public String sql;
/*  9:   */   
/* 10:   */   public FilteringRule(String sql)
/* 11:   */   {
/* 12:31 */     this.sql = sql;
/* 13:   */   }
/* 14:   */   
/* 15:   */   public FilteringRule(String name, String value)
/* 16:   */   {
/* 17:41 */     this.name = name;
/* 18:42 */     this.value = value;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public FilteringRule(String name, String value, String operation)
/* 22:   */   {
/* 23:53 */     this.name = name;
/* 24:54 */     this.value = value;
/* 25:55 */     this.operation = operation;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public String to_sql(DBDataWrapper db)
/* 29:   */   {
/* 30:66 */     if ((this.sql != null) && (!this.sql.equals(""))) {
/* 31:66 */       return this.sql;
/* 32:   */     }
/* 33:67 */     if ((this.operation != null) && (!this.operation.equals(""))) {
/* 34:67 */       return this.name + " " + this.operation + " '" + db.escape(this.value) + "'";
/* 35:   */     }
/* 36:68 */     return this.name + " LIKE '%" + db.escape(this.value) + "%'";
/* 37:   */   }
/* 38:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.FilteringRule
 * JD-Core Version:    0.7.0.1
 */
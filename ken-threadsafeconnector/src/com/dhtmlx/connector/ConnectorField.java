/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ public class ConnectorField
/*  4:   */ {
/*  5:   */   public String name;
/*  6:   */   public String db_name;
/*  7:   */   
/*  8:   */   public ConnectorField()
/*  9:   */   {
/* 10:25 */     this("", "");
/* 11:   */   }
/* 12:   */   
/* 13:   */   public ConnectorField(String db_name)
/* 14:   */   {
/* 15:34 */     this(db_name, db_name);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public ConnectorField(String db_name, String name)
/* 19:   */   {
/* 20:44 */     this.name = name;
/* 21:45 */     this.db_name = db_name;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public boolean isEmpty()
/* 25:   */   {
/* 26:54 */     return this.db_name.equals("");
/* 27:   */   }
/* 28:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.ConnectorField
 * JD-Core Version:    0.7.0.1
 */
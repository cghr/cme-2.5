/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ import java.sql.Connection;
/*  4:   */ 
/*  5:   */ public class SelectOptionsConnector
/*  6:   */   extends BaseConnector
/*  7:   */ {
/*  8:   */   public SelectOptionsConnector(Connection db)
/*  9:   */   {
/* 10:20 */     this(db, DBType.Custom);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public SelectOptionsConnector(Connection db, DBType db_type)
/* 14:   */   {
/* 15:30 */     this(db, db_type, new SelectOptionsFactory());
/* 16:   */   }
/* 17:   */   
/* 18:   */   public SelectOptionsConnector(Connection db, DBType db_type, BaseFactory a_factory)
/* 19:   */   {
/* 20:41 */     super(db, db_type, a_factory);
/* 21:   */   }
/* 22:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.SelectOptionsConnector
 * JD-Core Version:    0.7.0.1
 */
/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ import java.sql.Connection;
/*  4:   */ 
/*  5:   */ public class KeyGridConnector
/*  6:   */   extends GridConnector
/*  7:   */ {
/*  8:   */   public KeyGridConnector(Connection db)
/*  9:   */   {
/* 10:12 */     this(db, DBType.Custom);
/* 11:   */     
/* 12:14 */     this.event.attach(new KeyGridBehavior(this));
/* 13:   */   }
/* 14:   */   
/* 15:   */   public KeyGridConnector(Connection db, DBType db_type)
/* 16:   */   {
/* 17:24 */     this(db, db_type, new KeyGridFactory());
/* 18:   */   }
/* 19:   */   
/* 20:   */   public KeyGridConnector(Connection db, DBType db_type, BaseFactory a_factory)
/* 21:   */   {
/* 22:35 */     super(db, db_type, a_factory);
/* 23:   */   }
/* 24:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.KeyGridConnector
 * JD-Core Version:    0.7.0.1
 */
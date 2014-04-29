/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ import java.sql.Connection;
/*  4:   */ 
/*  5:   */ public class OptionsConnector
/*  6:   */   extends BaseConnector
/*  7:   */ {
/*  8:16 */   protected boolean init_flag = false;
/*  9:   */   
/* 10:   */   public OptionsConnector(Connection db)
/* 11:   */   {
/* 12:24 */     this(db, DBType.Custom);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public OptionsConnector(Connection db, DBType db_type)
/* 16:   */   {
/* 17:34 */     this(db, db_type, new BaseFactory());
/* 18:   */   }
/* 19:   */   
/* 20:   */   public OptionsConnector(Connection db, DBType db_type, BaseFactory a_factory)
/* 21:   */   {
/* 22:45 */     super(db, db_type, a_factory);
/* 23:   */   }
/* 24:   */   
/* 25:   */   public String render()
/* 26:   */   {
/* 27:53 */     if (!this.init_flag)
/* 28:   */     {
/* 29:54 */       this.init_flag = true;
/* 30:55 */       return "";
/* 31:   */     }
/* 32:58 */     return super.render();
/* 33:   */   }
/* 34:   */   
/* 35:   */   protected void output_as_xml(String data) {}
/* 36:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.OptionsConnector
 * JD-Core Version:    0.7.0.1
 */
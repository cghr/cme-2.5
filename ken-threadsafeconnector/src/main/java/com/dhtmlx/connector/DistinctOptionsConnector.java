/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ import java.sql.Connection;
/*  4:   */ 
/*  5:   */ public class DistinctOptionsConnector
/*  6:   */   extends OptionsConnector
/*  7:   */ {
/*  8:   */   public DistinctOptionsConnector(Connection db)
/*  9:   */   {
/* 10:22 */     this(db, DBType.Custom);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public DistinctOptionsConnector(Connection db, DBType db_type)
/* 14:   */   {
/* 15:32 */     this(db, db_type, new BaseFactory());
/* 16:   */   }
/* 17:   */   
/* 18:   */   public DistinctOptionsConnector(Connection db, DBType db_type, BaseFactory a_factory)
/* 19:   */   {
/* 20:43 */     super(db, db_type, a_factory);
/* 21:   */   }
/* 22:   */   
/* 23:   */   public String render()
/* 24:   */   {
/* 25:51 */     if (!this.init_flag)
/* 26:   */     {
/* 27:52 */       this.init_flag = true;
/* 28:53 */       return "";
/* 29:   */     }
/* 30:   */     try
/* 31:   */     {
/* 32:56 */       ConnectorResultSet res = this.sql.get_variants(this.request);
/* 33:57 */       return render_set(res);
/* 34:   */     }
/* 35:   */     catch (ConnectorOperationException e) {}
/* 36:59 */     return "";
/* 37:   */   }
/* 38:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.DistinctOptionsConnector
 * JD-Core Version:    0.7.0.1
 */
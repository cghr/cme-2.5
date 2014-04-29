/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ import java.sql.Connection;
/*  4:   */ import javax.servlet.http.HttpServletRequest;
/*  5:   */ 
/*  6:   */ public class FormConnector
/*  7:   */   extends BaseConnector
/*  8:   */ {
/*  9:   */   public FormConnector(Connection db)
/* 10:   */   {
/* 11:12 */     this(db, DBType.Custom);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public FormConnector(Connection db, DBType db_type)
/* 15:   */   {
/* 16:22 */     this(db, db_type, new FormFactory());
/* 17:   */   }
/* 18:   */   
/* 19:   */   public FormConnector(Connection db, DBType db_type, BaseFactory a_factory)
/* 20:   */   {
/* 21:33 */     super(db, db_type, a_factory);
/* 22:   */   }
/* 23:   */   
/* 24:   */   protected void parse_request()
/* 25:   */   {
/* 26:39 */     super.parse_request();
/* 27:   */     
/* 28:41 */     String id = this.http_request.getParameter("id");
/* 29:42 */     if ((id == null) || (id.equals(""))) {
/* 30:43 */       LogManager.getInstance().log("id parameter was missed");
/* 31:   */     } else {
/* 32:45 */       this.request.set_filter(this.config.id.name, id, "=");
/* 33:   */     }
/* 34:   */   }
/* 35:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.FormConnector
 * JD-Core Version:    0.7.0.1
 */
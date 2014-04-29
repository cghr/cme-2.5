/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ import java.sql.Connection;
/*  4:   */ import java.util.ArrayList;
/*  5:   */ import javax.servlet.http.HttpServletRequest;
/*  6:   */ 
/*  7:   */ public class ComboConnector
/*  8:   */   extends BaseConnector
/*  9:   */ {
/* 10:   */   public ComboConnector(Connection db)
/* 11:   */   {
/* 12:21 */     this(db, DBType.Custom);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public ComboConnector(Connection db, DBType db_type)
/* 16:   */   {
/* 17:31 */     this(db, db_type, new ComboFactory());
/* 18:   */   }
/* 19:   */   
/* 20:   */   public ComboConnector(Connection db, DBType db_type, BaseFactory a_factory)
/* 21:   */   {
/* 22:42 */     super(db, db_type, a_factory);
/* 23:   */   }
/* 24:   */   
/* 25:   */   protected void parse_request()
/* 26:   */   {
/* 27:50 */     super.parse_request();
/* 28:   */     
/* 29:52 */     String pos = this.http_request.getParameter("pos");
/* 30:53 */     if ((pos != null) && (this.dynloading)) {
/* 31:54 */       this.request.set_limit(pos, Integer.toString(this.dynloading_size));
/* 32:   */     }
/* 33:56 */     String mask = this.http_request.getParameter("mask");
/* 34:57 */     if (mask != null) {
/* 35:58 */       this.request.set_filter(((ConnectorField)this.config.text.get(0)).name, mask + "%", "LIKE");
/* 36:   */     }
/* 37:   */   }
/* 38:   */   
/* 39:   */   protected String xml_start()
/* 40:   */   {
/* 41:66 */     String pos = this.request.get_start();
/* 42:67 */     if ((pos != null) && (!pos.equals("")) && (!pos.equals("0"))) {
/* 43:68 */       return "<complete add='true'>";
/* 44:   */     }
/* 45:70 */     return "<complete>";
/* 46:   */   }
/* 47:   */   
/* 48:   */   protected String xml_end()
/* 49:   */   {
/* 50:78 */     return "</complete>";
/* 51:   */   }
/* 52:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.ComboConnector
 * JD-Core Version:    0.7.0.1
 */
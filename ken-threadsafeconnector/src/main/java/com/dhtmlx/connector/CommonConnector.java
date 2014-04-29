/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ import java.sql.Connection;
/*  4:   */ import javax.servlet.http.HttpServletRequest;
/*  5:   */ 
/*  6:   */ public class CommonConnector
/*  7:   */   extends BaseConnector
/*  8:   */ {
/*  9: 6 */   private boolean is_simple_protocol_used = false;
/* 10:   */   
/* 11:   */   public boolean isSimpleProtocolUsed()
/* 12:   */   {
/* 13: 8 */     return this.is_simple_protocol_used;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public CommonConnector(Connection db)
/* 17:   */   {
/* 18:16 */     this(db, DBType.Custom);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public CommonConnector(Connection db, DBType db_type)
/* 22:   */   {
/* 23:26 */     this(db, db_type, new CommonFactory());
/* 24:   */   }
/* 25:   */   
/* 26:   */   public CommonConnector(Connection db, DBType db_type, BaseFactory a_factory)
/* 27:   */   {
/* 28:37 */     super(db, db_type, a_factory);
/* 29:   */   }
/* 30:   */   
/* 31:   */   protected void parse_request()
/* 32:   */   {
/* 33:42 */     String action = this.http_request.getParameter("action");
/* 34:43 */     String id = this.http_request.getParameter("id");
/* 35:44 */     Boolean is_get_action = Boolean.valueOf(false);
/* 36:46 */     if (action != null)
/* 37:   */     {
/* 38:47 */       String[] nameValuePairs = this.http_request.getQueryString().split("&");
/* 39:48 */       for (String nameValuePair : nameValuePairs) {
/* 40:49 */         if (nameValuePair.startsWith("action="))
/* 41:   */         {
/* 42:50 */           is_get_action = Boolean.valueOf(true);
/* 43:51 */           break;
/* 44:   */         }
/* 45:   */       }
/* 46:   */     }
/* 47:56 */     if (is_get_action.booleanValue())
/* 48:   */     {
/* 49:57 */       if ((action.equals("get")) && (id != null))
/* 50:   */       {
/* 51:58 */         this.request.set_filter(this.config.id.name, id);
/* 52:   */       }
/* 53:59 */       else if (((!action.equals("update")) && (!action.equals("delete"))) || ((id != null) || (action.equals("insert"))))
/* 54:   */       {
/* 55:60 */         this.editing = true;
/* 56:61 */         this.is_simple_protocol_used = true;
/* 57:   */       }
/* 58:   */       else
/* 59:   */       {
/* 60:63 */         super.parse_request();
/* 61:   */       }
/* 62:   */     }
/* 63:   */     else {
/* 64:66 */       super.parse_request();
/* 65:   */     }
/* 66:   */   }
/* 67:   */   
/* 68:   */   public String getRecord(String getNewId)
/* 69:   */     throws ConnectorOperationException
/* 70:   */   {
/* 71:72 */     DataRequest source = new DataRequest(this.request);
/* 72:73 */     source.set_filter(this.config.id.name, getNewId, "=");
/* 73:   */     
/* 74:75 */     ConnectorResultSet res = this.sql.select(source);
/* 75:76 */     return render_set(res);
/* 76:   */   }
/* 77:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.CommonConnector
 * JD-Core Version:    0.7.0.1
 */
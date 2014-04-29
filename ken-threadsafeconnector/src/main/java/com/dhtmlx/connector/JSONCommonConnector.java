/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.Writer;
/*  5:   */ import java.sql.Connection;
/*  6:   */ import java.util.HashMap;
/*  7:   */ import javax.servlet.http.HttpServletResponse;
/*  8:   */ import org.json.simple.JSONArray;
/*  9:   */ 
/* 10:   */ public class JSONCommonConnector
/* 11:   */   extends CommonConnector
/* 12:   */ {
/* 13:   */   public JSONCommonConnector(Connection db)
/* 14:   */   {
/* 15:16 */     this(db, DBType.Custom);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public JSONCommonConnector(Connection db, DBType db_type)
/* 19:   */   {
/* 20:26 */     this(db, db_type, new JSONCommonFactory());
/* 21:   */   }
/* 22:   */   
/* 23:   */   public JSONCommonConnector(Connection db, DBType db_type, BaseFactory a_factory)
/* 24:   */   {
/* 25:37 */     super(db, db_type, a_factory);
/* 26:   */   }
/* 27:   */   
/* 28:   */   protected String render_set(ConnectorResultSet result)
/* 29:   */     throws ConnectorOperationException
/* 30:   */   {
/* 31:43 */     JSONArray output = new JSONArray();
/* 32:44 */     int index = 0;
/* 33:   */     HashMap<String, String> values;
/* 34:46 */     while ((values = result.get_next()) != null)
/* 35:   */     {
/* 36:   */       HashMap<String, String> values;
/* 37:47 */       JSONCommonDataItem data = (JSONCommonDataItem)this.cfactory.createDataItem(values, this.config, index);
/* 38:48 */       if (data.get_id() == null) {
/* 39:49 */         data.set_id(uuid());
/* 40:   */       }
/* 41:51 */       this.event.trigger().beforeRender(data);
/* 42:52 */       data.to_json(output);
/* 43:53 */       index++;
/* 44:   */     }
/* 45:55 */     return output.toString();
/* 46:   */   }
/* 47:   */   
/* 48:   */   protected String output_as_xml(ConnectorResultSet result)
/* 49:   */     throws ConnectorOperationException
/* 50:   */   {
/* 51:59 */     ConnectorOutputWriter out = new ConnectorOutputWriter(xml_start(), render_set(result) + xml_end());
/* 52:   */     
/* 53:61 */     output_as_xml(out.toString());
/* 54:62 */     return out.toString();
/* 55:   */   }
/* 56:   */   
/* 57:   */   protected String xml_start()
/* 58:   */   {
/* 59:65 */     return "";
/* 60:   */   }
/* 61:   */   
/* 62:   */   protected String xml_end()
/* 63:   */   {
/* 64:66 */     return "";
/* 65:   */   }
/* 66:   */   
/* 67:   */   public void output_as_xml(String data)
/* 68:   */   {
/* 69:69 */     this.http_response.reset();
/* 70:70 */     this.http_response.addHeader("Content-type", "text/javascript;charset=" + this.encoding);
/* 71:   */     try
/* 72:   */     {
/* 73:73 */       Writer out = this.http_response.getWriter();
/* 74:74 */       out.write(data);
/* 75:75 */       out.close();
/* 76:76 */       this.http_response.flushBuffer();
/* 77:   */     }
/* 78:   */     catch (IOException e)
/* 79:   */     {
/* 80:78 */       LogManager.getInstance().log("Error during data outputing");
/* 81:79 */       LogManager.getInstance().log(e.getMessage());
/* 82:   */     }
/* 83:81 */     end_run();
/* 84:   */   }
/* 85:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.JSONCommonConnector
 * JD-Core Version:    0.7.0.1
 */
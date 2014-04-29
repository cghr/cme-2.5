/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.Enumeration;
/*  5:   */ import java.util.HashMap;
/*  6:   */ import javax.servlet.http.HttpServletRequest;
/*  7:   */ 
/*  8:   */ public class CommonDataProcessor
/*  9:   */   extends DataProcessor
/* 10:   */ {
/* 11:   */   public CommonDataProcessor(BaseConnector connector, DataConfig config, DataRequest request, BaseFactory cfactory)
/* 12:   */   {
/* 13:11 */     super(connector, config, request, cfactory);
/* 14:   */   }
/* 15:   */   
/* 16:   */   protected String get_list_of_ids()
/* 17:   */   {
/* 18:16 */     if (((CommonConnector)this.connector).isSimpleProtocolUsed())
/* 19:   */     {
/* 20:17 */       String id = this.connector.http_request.getParameter("id");
/* 21:18 */       if ((id == null) && (this.connector.http_request.getParameter("action").equals("insert"))) {
/* 22:19 */         id = "dummy_insert_id";
/* 23:   */       }
/* 24:20 */       return id;
/* 25:   */     }
/* 26:22 */     return super.get_list_of_ids();
/* 27:   */   }
/* 28:   */   
/* 29:   */   protected HashMap<String, HashMap<String, String>> get_post_values(String[] ids)
/* 30:   */   {
/* 31:29 */     if (((CommonConnector)this.connector).isSimpleProtocolUsed())
/* 32:   */     {
/* 33:30 */       HashMap<String, HashMap<String, String>> data = new HashMap();
/* 34:31 */       HashMap<String, String> record = new HashMap();
/* 35:32 */       Enumeration names = this.connector.http_request.getParameterNames();
/* 36:33 */       while (names.hasMoreElements())
/* 37:   */       {
/* 38:34 */         String name = (String)names.nextElement();
/* 39:35 */         record.put(name, this.connector.http_request.getParameter(name));
/* 40:   */       }
/* 41:37 */       for (int i = 0; i < ids.length; i++) {
/* 42:38 */         data.put(ids[i], record);
/* 43:   */       }
/* 44:39 */       return data;
/* 45:   */     }
/* 46:41 */     return super.get_post_values(ids);
/* 47:   */   }
/* 48:   */   
/* 49:   */   protected DataAction get_data_action(String status, String id, HashMap<String, String> itemData)
/* 50:   */   {
/* 51:47 */     if (((CommonConnector)this.connector).isSimpleProtocolUsed()) {
/* 52:48 */       return new CommonDataAction(status, id, itemData);
/* 53:   */     }
/* 54:50 */     return super.get_data_action(status, id, itemData);
/* 55:   */   }
/* 56:   */   
/* 57:   */   protected String output_as_xml(ArrayList<DataAction> result)
/* 58:   */   {
/* 59:56 */     if (((CommonConnector)this.connector).isSimpleProtocolUsed())
/* 60:   */     {
/* 61:57 */       StringBuffer out = new StringBuffer();
/* 62:58 */       for (int i = 0; i < result.size(); i++) {
/* 63:59 */         out.append(((DataAction)result.get(i)).to_xml());
/* 64:   */       }
/* 65:60 */       return out.toString();
/* 66:   */     }
/* 67:62 */     return super.output_as_xml(result);
/* 68:   */   }
/* 69:   */   
/* 70:   */   protected String get_status(HashMap<String, String> itemData)
/* 71:   */   {
/* 72:68 */     if (((CommonConnector)this.connector).isSimpleProtocolUsed()) {
/* 73:69 */       return this.connector.http_request.getParameter("action");
/* 74:   */     }
/* 75:71 */     return super.get_status(itemData);
/* 76:   */   }
/* 77:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.CommonDataProcessor
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package com.dhtmlx.connector;
/*   2:    */ 
/*   3:    */ import java.sql.Connection;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import javax.servlet.http.HttpServletRequest;
/*   6:    */ 
/*   7:    */ public class TreeGridConnector
/*   8:    */   extends GridConnector
/*   9:    */ {
/*  10:    */   public TreeGridConnector(Connection db)
/*  11:    */   {
/*  12: 20 */     this(db, DBType.Custom);
/*  13:    */   }
/*  14:    */   
/*  15:    */   public TreeGridConnector(Connection db, DBType db_type)
/*  16:    */   {
/*  17: 30 */     this(db, db_type, new TreeGridFactory());
/*  18:    */   }
/*  19:    */   
/*  20:    */   public TreeGridConnector(Connection db, DBType db_type, BaseFactory a_factory)
/*  21:    */   {
/*  22: 41 */     super(db, db_type, a_factory);
/*  23: 42 */     this.event.attach(new TreeGridBehavior(this.config));
/*  24:    */   }
/*  25:    */   
/*  26:    */   protected void parse_request()
/*  27:    */   {
/*  28: 50 */     super.parse_request();
/*  29:    */     
/*  30: 52 */     String id = this.http_request.getParameter("id");
/*  31: 53 */     if (id != null) {
/*  32: 54 */       this.request.set_relation(id);
/*  33:    */     } else {
/*  34: 56 */       this.request.set_relation("0");
/*  35:    */     }
/*  36: 58 */     this.request.set_limit("", "");
/*  37:    */   }
/*  38:    */   
/*  39:    */   protected String render_set(ConnectorResultSet result)
/*  40:    */     throws ConnectorOperationException
/*  41:    */   {
/*  42: 68 */     StringBuffer output = new StringBuffer();
/*  43: 69 */     int index = 0;
/*  44:    */     HashMap<String, String> values;
/*  45: 71 */     while ((values = result.get_next()) != null)
/*  46:    */     {
/*  47:    */       HashMap<String, String> values;
/*  48: 72 */       TreeGridDataItem data = (TreeGridDataItem)this.cfactory.createDataItem(values, this.config, index);
/*  49: 73 */       if (data.get_id() == null) {
/*  50: 74 */         data.set_id(uuid());
/*  51:    */       }
/*  52: 76 */       this.event.trigger().beforeRender(data);
/*  53: 78 */       if ((data.has_kids() == -1) && (this.dynloading)) {
/*  54: 79 */         data.set_kids(1);
/*  55:    */       }
/*  56: 81 */       data.to_xml_start(output);
/*  57: 83 */       if ((data.has_kids() == -1) || ((data.has_kids() != 0) && (!this.dynloading)))
/*  58:    */       {
/*  59: 84 */         DataRequest sub_request = new DataRequest(this.request);
/*  60: 85 */         sub_request.set_relation(data.get_id());
/*  61: 86 */         output.append(render_set(this.sql.select(sub_request)));
/*  62:    */       }
/*  63: 89 */       data.to_xml_end(output);
/*  64: 90 */       index++;
/*  65:    */     }
/*  66: 92 */     return output.toString();
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected String xml_start()
/*  70:    */   {
/*  71:100 */     return "<rows parent='" + this.request.get_relation() + "'>";
/*  72:    */   }
/*  73:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.TreeGridConnector
 * JD-Core Version:    0.7.0.1
 */
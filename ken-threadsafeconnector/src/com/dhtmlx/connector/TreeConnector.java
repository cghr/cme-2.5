/*   1:    */ package com.dhtmlx.connector;
/*   2:    */ 
/*   3:    */ import java.sql.Connection;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import javax.servlet.http.HttpServletRequest;
/*   6:    */ 
/*   7:    */ public class TreeConnector
/*   8:    */   extends BaseConnector
/*   9:    */ {
/*  10:    */   public TreeConnector(Connection db)
/*  11:    */   {
/*  12: 21 */     this(db, DBType.Custom);
/*  13:    */   }
/*  14:    */   
/*  15:    */   public TreeConnector(Connection db, DBType db_type)
/*  16:    */   {
/*  17: 31 */     this(db, db_type, new TreeFactory());
/*  18:    */   }
/*  19:    */   
/*  20:    */   public TreeConnector(Connection db, DBType db_type, BaseFactory a_factory)
/*  21:    */   {
/*  22: 42 */     super(db, db_type, a_factory);
/*  23: 43 */     this.event.attach(new TreeGridBehavior(this.config));
/*  24:    */   }
/*  25:    */   
/*  26:    */   protected void parse_request()
/*  27:    */   {
/*  28: 51 */     super.parse_request();
/*  29:    */     
/*  30: 53 */     String id = this.http_request.getParameter("id");
/*  31: 54 */     if (id != null) {
/*  32: 55 */       this.request.set_relation(id);
/*  33:    */     } else {
/*  34: 57 */       this.request.set_relation("0");
/*  35:    */     }
/*  36: 59 */     this.request.set_limit("", "");
/*  37:    */   }
/*  38:    */   
/*  39:    */   protected String render_set(ConnectorResultSet result)
/*  40:    */     throws ConnectorOperationException
/*  41:    */   {
/*  42: 69 */     StringBuffer output = new StringBuffer();
/*  43: 70 */     int index = 0;
/*  44:    */     HashMap<String, String> values;
/*  45: 72 */     while ((values = result.get_next()) != null)
/*  46:    */     {
/*  47:    */       HashMap<String, String> values;
/*  48: 73 */       TreeDataItem data = (TreeDataItem)this.cfactory.createDataItem(values, this.config, index);
/*  49: 74 */       if (data.get_id() == null) {
/*  50: 75 */         data.set_id(uuid());
/*  51:    */       }
/*  52: 77 */       this.event.trigger().beforeRender(data);
/*  53: 79 */       if ((data.has_kids() == -1) && (this.dynloading)) {
/*  54: 80 */         data.set_kids(1);
/*  55:    */       }
/*  56: 82 */       data.to_xml_start(output);
/*  57: 84 */       if ((data.has_kids() == -1) || ((data.has_kids() != 0) && (!this.dynloading)))
/*  58:    */       {
/*  59: 85 */         DataRequest sub_request = new DataRequest(this.request);
/*  60: 86 */         sub_request.set_relation(data.get_id());
/*  61: 87 */         output.append(render_set(this.sql.select(sub_request)));
/*  62:    */       }
/*  63: 90 */       data.to_xml_end(output);
/*  64: 91 */       index++;
/*  65:    */     }
/*  66: 93 */     return output.toString();
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected String xml_start()
/*  70:    */   {
/*  71:101 */     return "<tree id='" + this.request.get_relation() + "'>";
/*  72:    */   }
/*  73:    */   
/*  74:    */   protected String xml_end()
/*  75:    */   {
/*  76:109 */     return "</tree>";
/*  77:    */   }
/*  78:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.TreeConnector
 * JD-Core Version:    0.7.0.1
 */
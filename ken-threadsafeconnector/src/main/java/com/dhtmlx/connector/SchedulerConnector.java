/*   1:    */ package com.dhtmlx.connector;
/*   2:    */ 
/*   3:    */ import java.sql.Connection;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import java.util.Set;
/*   8:    */ import javax.servlet.http.HttpServletRequest;
/*   9:    */ 
/*  10:    */ public class SchedulerConnector
/*  11:    */   extends BaseConnector
/*  12:    */ {
/*  13: 16 */   protected StringBuffer extra_output = new StringBuffer();
/*  14: 19 */   protected HashMap<String, BaseConnector> options = new HashMap();
/*  15:    */   
/*  16:    */   public SchedulerConnector(Connection db)
/*  17:    */   {
/*  18: 27 */     this(db, DBType.Custom);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public SchedulerConnector(Connection db, DBType db_type)
/*  22:    */   {
/*  23: 37 */     this(db, db_type, new SchedulerFactory());
/*  24:    */   }
/*  25:    */   
/*  26:    */   public SchedulerConnector(Connection db, DBType db_type, BaseFactory a_factory)
/*  27:    */   {
/*  28: 48 */     super(db, db_type, a_factory);
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected void parse_request()
/*  32:    */   {
/*  33: 56 */     super.parse_request();
/*  34:    */     
/*  35: 58 */     String to = this.http_request.getParameter("to");
/*  36: 59 */     String from = this.http_request.getParameter("from");
/*  37: 60 */     if ((to != null) && (!to.equals(""))) {
/*  38: 61 */       this.request.set_filter(((ConnectorField)this.config.text.get(0)).name, to, "<");
/*  39:    */     }
/*  40: 62 */     if ((from != null) && (!from.equals(""))) {
/*  41: 63 */       this.request.set_filter(((ConnectorField)this.config.text.get(1)).name, from, ">");
/*  42:    */     }
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected String xml_end()
/*  46:    */   {
/*  47: 68 */     fill_collections();
/*  48: 69 */     return this.extra_output.toString() + "</data>";
/*  49:    */   }
/*  50:    */   
/*  51:    */   protected void fill_collections()
/*  52:    */   {
/*  53: 80 */     Iterator<String> key = this.options.keySet().iterator();
/*  54: 81 */     while (key.hasNext())
/*  55:    */     {
/*  56: 82 */       String name = (String)key.next();
/*  57: 83 */       BaseConnector option_connector = (BaseConnector)this.options.get(name);
/*  58: 84 */       this.extra_output.append("<coll_options for='" + name + "'>");
/*  59: 85 */       this.extra_output.append(option_connector.render());
/*  60: 86 */       this.extra_output.append("</coll_options>");
/*  61:    */     }
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void set_options(String name, BaseConnector connector)
/*  65:    */   {
/*  66: 97 */     this.options.put(name, connector);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void set_options(String name, Iterable object)
/*  70:    */   {
/*  71:108 */     Iterator it = object.iterator();
/*  72:109 */     StringBuffer data = new StringBuffer();
/*  73:111 */     while (it.hasNext())
/*  74:    */     {
/*  75:112 */       String value = it.next().toString();
/*  76:113 */       data.append("<item value='" + value + "' label='" + value + "' />");
/*  77:    */     }
/*  78:115 */     set_options(name, new DummyStringConnector(data.toString()));
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void set_options(String name, HashMap object)
/*  82:    */   {
/*  83:126 */     Iterator it = object.keySet().iterator();
/*  84:127 */     StringBuffer data = new StringBuffer();
/*  85:129 */     while (it.hasNext())
/*  86:    */     {
/*  87:130 */       Object value = it.next();
/*  88:131 */       Object label = object.get(value).toString();
/*  89:132 */       data.append("<item value='" + value.toString() + "' label='" + label.toString() + "' />");
/*  90:    */     }
/*  91:134 */     set_options(name, new DummyStringConnector(data.toString()));
/*  92:    */   }
/*  93:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.SchedulerConnector
 * JD-Core Version:    0.7.0.1
 */
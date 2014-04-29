/*   1:    */ package com.dhtmlx.connector;
/*   2:    */ 
/*   3:    */ import java.sql.Connection;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import java.util.Set;
/*   8:    */ import javax.servlet.http.HttpServletRequest;
/*   9:    */ 
/*  10:    */ public class GridConnector
/*  11:    */   extends BaseConnector
/*  12:    */ {
/*  13: 16 */   protected StringBuffer extra_output = new StringBuffer();
/*  14: 19 */   protected HashMap<String, BaseConnector> options = new HashMap();
/*  15:    */   private GridConfiguration gridConfig;
/*  16:    */   
/*  17:    */   public GridConnector(Connection db)
/*  18:    */   {
/*  19: 29 */     this(db, DBType.Custom);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public GridConnector(Connection db, DBType db_type)
/*  23:    */   {
/*  24: 39 */     this(db, db_type, new GridFactory());
/*  25:    */   }
/*  26:    */   
/*  27:    */   public GridConnector(Connection db, DBType db_type, BaseFactory a_factory)
/*  28:    */   {
/*  29: 50 */     super(db, db_type, a_factory);
/*  30:    */   }
/*  31:    */   
/*  32:    */   protected void parse_request()
/*  33:    */   {
/*  34: 58 */     super.parse_request();
/*  35:    */     
/*  36: 60 */     String colls = this.http_request.getParameter("dhx_colls");
/*  37: 62 */     if (colls != null) {
/*  38:    */       try
/*  39:    */       {
/*  40: 64 */         fill_collections(colls.split(","));
/*  41:    */       }
/*  42:    */       catch (ConnectorConfigException e)
/*  43:    */       {
/*  44: 67 */         e.printStackTrace();
/*  45:    */       }
/*  46:    */     }
/*  47: 71 */     String pos = this.http_request.getParameter("posStart");
/*  48: 72 */     String count = this.http_request.getParameter("count");
/*  49: 74 */     if ((pos != null) || (count != null)) {
/*  50: 75 */       this.request.set_limit(pos, count);
/*  51:    */     }
/*  52:    */   }
/*  53:    */   
/*  54:    */   protected void fill_collections(String[] columns)
/*  55:    */     throws ConnectorConfigException
/*  56:    */   {
/*  57: 86 */     for (int i = 0; i < columns.length; i++)
/*  58:    */     {
/*  59: 87 */       String name = resolve_parameter(columns[i]);
/*  60: 88 */       String data = "";
/*  61: 89 */       BaseConnector option_connector = (BaseConnector)this.options.get(name);
/*  62: 90 */       if (option_connector == null)
/*  63:    */       {
/*  64: 91 */         option_connector = new DistinctOptionsConnector(get_connection(), this.db_type);
/*  65: 92 */         DataConfig c = new DataConfig(this.config);
/*  66: 93 */         DataRequest r = new DataRequest(this.request);
/*  67: 94 */         c.minimize(name);
/*  68: 95 */         option_connector.render_connector(c, r);
/*  69:    */       }
/*  70: 98 */       data = option_connector.render();
/*  71:    */       
/*  72:100 */       this.extra_output.append("<coll_options for='" + columns[i] + "'>");
/*  73:101 */       this.extra_output.append(data);
/*  74:102 */       this.extra_output.append("</coll_options>");
/*  75:    */     }
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void set_options(String name, BaseConnector connector)
/*  79:    */   {
/*  80:113 */     this.options.put(name, connector);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void set_options(String name, Iterable object)
/*  84:    */   {
/*  85:124 */     Iterator it = object.iterator();
/*  86:125 */     StringBuffer data = new StringBuffer();
/*  87:127 */     while (it.hasNext())
/*  88:    */     {
/*  89:128 */       String value = it.next().toString();
/*  90:129 */       data.append("<item value='" + value + "' label='" + value + "' />");
/*  91:    */     }
/*  92:131 */     set_options(name, new DummyStringConnector(data.toString()));
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void set_options(String name, HashMap object)
/*  96:    */   {
/*  97:142 */     Iterator it = object.keySet().iterator();
/*  98:143 */     StringBuffer data = new StringBuffer();
/*  99:145 */     while (it.hasNext())
/* 100:    */     {
/* 101:146 */       Object value = it.next();
/* 102:147 */       Object label = object.get(value).toString();
/* 103:148 */       data.append("<item value='" + value.toString() + "' label='" + label.toString() + "' />");
/* 104:    */     }
/* 105:150 */     set_options(name, new DummyStringConnector(data.toString()));
/* 106:    */   }
/* 107:    */   
/* 108:    */   protected String resolve_parameter(String name)
/* 109:    */   {
/* 110:    */     try
/* 111:    */     {
/* 112:160 */       int index = Integer.parseInt(name);
/* 113:161 */       return ((ConnectorField)this.config.text.get(index)).db_name;
/* 114:    */     }
/* 115:    */     catch (NumberFormatException e) {}
/* 116:163 */     return super.resolve_parameter(name);
/* 117:    */   }
/* 118:    */   
/* 119:    */   protected String xml_end()
/* 120:    */   {
/* 121:172 */     return this.extra_output.toString() + "</rows>";
/* 122:    */   }
/* 123:    */   
/* 124:    */   protected String xml_start()
/* 125:    */   {
/* 126:    */     String response;
/* 127:181 */     if (this.dynloading)
/* 128:    */     {
/* 129:182 */       String pos = this.request.get_start();
/* 130:    */       String response;
/* 131:    */       try
/* 132:    */       {
/* 133:    */         String response;
/* 134:184 */         if ((pos != null) && (!pos.equals("")) && (!pos.equals("0"))) {
/* 135:185 */           response = "<rows pos='" + pos + "'>";
/* 136:    */         } else {
/* 137:187 */           response = "<rows total_count='" + this.sql.get_size(this.request) + "'>";
/* 138:    */         }
/* 139:    */       }
/* 140:    */       catch (ConnectorOperationException e)
/* 141:    */       {
/* 142:    */         String response;
/* 143:190 */         response = "<rows>";
/* 144:    */       }
/* 145:    */     }
/* 146:    */     else
/* 147:    */     {
/* 148:194 */       response = "<rows>";
/* 149:    */     }
/* 150:195 */     if ((isFirstLoading()) && (this.gridConfig != null)) {
/* 151:196 */       return response + this.gridConfig.toXML();
/* 152:    */     }
/* 153:198 */     return response;
/* 154:    */   }
/* 155:    */   
/* 156:    */   private boolean isFirstLoading()
/* 157:    */   {
/* 158:203 */     if (this.http_request.getParameter("dhx_no_header") != null) {
/* 159:204 */       return false;
/* 160:    */     }
/* 161:205 */     if (this.http_request.getParameter("posStart") != null) {
/* 162:206 */       return false;
/* 163:    */     }
/* 164:207 */     if (this.http_request.getParameter("editing") != null) {
/* 165:208 */       return false;
/* 166:    */     }
/* 167:209 */     return true;
/* 168:    */   }
/* 169:    */   
/* 170:    */   public void setConfiguration(GridConfiguration config)
/* 171:    */   {
/* 172:213 */     this.gridConfig = config;
/* 173:    */   }
/* 174:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.GridConnector
 * JD-Core Version:    0.7.0.1
 */
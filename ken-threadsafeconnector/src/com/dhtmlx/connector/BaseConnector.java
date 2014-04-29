/*   1:    */ package com.dhtmlx.connector;
/*   2:    */ 
/*   3:    */ import java.io.FileWriter;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.sql.Connection;
/*   6:    */ import java.util.HashMap;
/*   7:    */ import java.util.Iterator;
/*   8:    */ import java.util.Map;
/*   9:    */ import java.util.Map.Entry;
/*  10:    */ import java.util.Set;
/*  11:    */ import javax.servlet.http.HttpServletRequest;
/*  12:    */ import javax.servlet.http.HttpServletResponse;
/*  13:    */ 
/*  14:    */ public class BaseConnector
/*  15:    */ {
/*  16:    */   protected static HttpServletRequest global_http_request;
/*  17:    */   protected static HttpServletResponse global_http_response;
/*  18:    */   protected HttpServletRequest http_request;
/*  19:    */   protected HttpServletResponse http_response;
/*  20:    */   public HashMap<String, String> incoming_data;
/*  21:    */   protected DataConfig config;
/*  22:    */   protected DataRequest request;
/*  23:    */   protected BaseFactory cfactory;
/*  24:    */   protected boolean dynloading;
/*  25:    */   protected int dynloading_size;
/*  26:    */   protected String encoding;
/*  27:    */   protected boolean editing;
/*  28:    */   private Connection db;
/*  29:    */   private int id_seed;
/*  30:    */   protected DBType db_type;
/*  31:    */   private long exec_time;
/*  32:    */   public AccessManager access;
/*  33:    */   public DataWrapper sql;
/*  34:    */   public ConnectorBehavior event;
/*  35:    */   
/*  36:    */   private DataWrapper resolve_data_wrapper(DBType db_type, BaseFactory a_factory)
/*  37:    */   {
/*  38: 91 */     this.db_type = db_type;
/*  39: 92 */     switch (db_type)
/*  40:    */     {
/*  41:    */     case PostgreSQL: 
/*  42: 94 */       return new PGSQLDBDataWrapper();
/*  43:    */     case MySQL: 
/*  44: 96 */       return new OracleDBDataWrapper();
/*  45:    */     case Oracle: 
/*  46: 98 */       return new MSSQLDBDataWrapper();
/*  47:    */     case SQL: 
/*  48:100 */       return a_factory.createDataWrapper();
/*  49:    */     }
/*  50:102 */     return new MySQLDBDataWrapper();
/*  51:    */   }
/*  52:    */   
/*  53:    */   public BaseConnector(Connection db)
/*  54:    */   {
/*  55:112 */     this(db, DBType.Custom);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public BaseConnector(Connection db, DBType db_type)
/*  59:    */   {
/*  60:122 */     this(db, db_type, new BaseFactory());
/*  61:    */   }
/*  62:    */   
/*  63:    */   public BaseConnector(Connection db, DBType db_type, BaseFactory a_factory)
/*  64:    */   {
/*  65:133 */     init_self(db, resolve_data_wrapper(db_type, a_factory), a_factory);
/*  66:    */   }
/*  67:    */   
/*  68:    */   private void init_self(Connection db, DataWrapper dw, BaseFactory a_factory)
/*  69:    */   {
/*  70:144 */     servlet(global_http_request, global_http_response);
/*  71:    */     
/*  72:146 */     this.exec_time = System.currentTimeMillis();
/*  73:147 */     this.encoding = "utf-8";
/*  74:    */     
/*  75:149 */     this.config = new DataConfig();
/*  76:150 */     this.request = new DataRequest();
/*  77:151 */     this.event = new ConnectorBehavior();
/*  78:152 */     this.access = new AccessManager();
/*  79:153 */     this.cfactory = a_factory;
/*  80:    */     
/*  81:155 */     this.sql = dw;
/*  82:156 */     this.sql.init(db, this.config);
/*  83:    */     
/*  84:158 */     this.db = db;
/*  85:    */   }
/*  86:    */   
/*  87:    */   protected Connection get_connection()
/*  88:    */   {
/*  89:167 */     return this.db;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void render_table(String table, String id, String fields)
/*  93:    */   {
/*  94:178 */     render_table(table, id, fields, "", "");
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void render_table(String table, String id, String fields, String extra)
/*  98:    */   {
/*  99:190 */     render_table(table, id, fields, extra, "");
/* 100:    */   }
/* 101:    */   
/* 102:    */   public void render_table(String table, String id, String fields, String extra, String relation_id)
/* 103:    */   {
/* 104:203 */     this.config.init(id, fields, extra, relation_id);
/* 105:    */     try
/* 106:    */     {
/* 107:205 */       this.request.set_source(table);
/* 108:    */     }
/* 109:    */     catch (ConnectorConfigException e)
/* 110:    */     {
/* 111:207 */       LogManager.getInstance().log("Error during render_table execution");
/* 112:208 */       LogManager.getInstance().log(e.getMessage());
/* 113:    */     }
/* 114:210 */     render();
/* 115:    */   }
/* 116:    */   
/* 117:    */   protected String uuid()
/* 118:    */   {
/* 119:219 */     return System.currentTimeMillis() + "_" + Integer.toString(this.id_seed++);
/* 120:    */   }
/* 121:    */   
/* 122:    */   public void render_sql(String sql, String id, String fields)
/* 123:    */   {
/* 124:230 */     render_sql(sql, id, fields, "", "");
/* 125:    */   }
/* 126:    */   
/* 127:    */   public void render_sql(String sql, String id, String fields, String extra)
/* 128:    */   {
/* 129:242 */     render_sql(sql, id, fields, extra, "");
/* 130:    */   }
/* 131:    */   
/* 132:    */   public void render_sql(String sql, String id, String fields, String extra, String relation_id)
/* 133:    */   {
/* 134:255 */     this.config.init(id, fields, extra, relation_id);
/* 135:    */     try
/* 136:    */     {
/* 137:257 */       this.request.parse_sql(sql);
/* 138:    */     }
/* 139:    */     catch (ConnectorConfigException e)
/* 140:    */     {
/* 141:259 */       LogManager.getInstance().log("Error during render_sql execution");
/* 142:260 */       LogManager.getInstance().log(e.getMessage());
/* 143:    */     }
/* 144:262 */     render();
/* 145:    */   }
/* 146:    */   
/* 147:    */   public String render_complex_sql(String sql, String id, String fields, String extra)
/* 148:    */   {
/* 149:275 */     return render_complex_sql(sql, id, fields, extra, "");
/* 150:    */   }
/* 151:    */   
/* 152:    */   public String render_complex_sql(String sql, String id, String fields)
/* 153:    */   {
/* 154:287 */     return render_complex_sql(sql, id, fields, "", "");
/* 155:    */   }
/* 156:    */   
/* 157:    */   public String render_complex_sql(String sql, String id, String fields, String extra, String relation_id)
/* 158:    */   {
/* 159:301 */     this.config.init(id, fields, extra, relation_id);
/* 160:    */     try
/* 161:    */     {
/* 162:303 */       this.request.parse_sql(sql, Boolean.valueOf(true));
/* 163:    */     }
/* 164:    */     catch (ConnectorConfigException e)
/* 165:    */     {
/* 166:305 */       LogManager.getInstance().log("Error during render_sql execution");
/* 167:306 */       LogManager.getInstance().log(e.getMessage());
/* 168:    */     }
/* 169:308 */     return render();
/* 170:    */   }
/* 171:    */   
/* 172:    */   public String render_connector(DataConfig config, DataRequest request)
/* 173:    */   {
/* 174:321 */     this.config.copy(config);
/* 175:322 */     this.request.copy(request);
/* 176:323 */     return render();
/* 177:    */   }
/* 178:    */   
/* 179:    */   public String render()
/* 180:    */   {
/* 181:334 */     parse_request();
/* 182:335 */     if (this.editing)
/* 183:    */     {
/* 184:336 */       DataProcessor dp = this.cfactory.createDataProcessor(this, this.config, this.request, this.cfactory);
/* 185:    */       String result;
/* 186:    */       try
/* 187:    */       {
/* 188:339 */         result = dp.process();
/* 189:    */       }
/* 190:    */       catch (ConnectorOperationException e)
/* 191:    */       {
/* 192:    */         String result;
/* 193:341 */         LogManager.getInstance().log("Error during data processing");
/* 194:342 */         LogManager.getInstance().log(e.getMessage());
/* 195:343 */         result = "<data>Operation error</data>";
/* 196:    */       }
/* 197:    */       catch (ConnectorConfigException e)
/* 198:    */       {
/* 199:    */         String result;
/* 200:345 */         LogManager.getInstance().log("Error during configuration parsing");
/* 201:346 */         LogManager.getInstance().log(e.getMessage());
/* 202:347 */         result = "<data>Configuration error</data>";
/* 203:    */       }
/* 204:349 */       output_as_xml(result);
/* 205:350 */       return result;
/* 206:    */     }
/* 207:352 */     this.event.trigger().beforeSort(this.request.get_sort_by());
/* 208:353 */     this.event.trigger().beforeFilter(this.request.get_filters());
/* 209:    */     try
/* 210:    */     {
/* 211:355 */       return output_as_xml(this.sql.select(this.request));
/* 212:    */     }
/* 213:    */     catch (ConnectorOperationException e)
/* 214:    */     {
/* 215:357 */       e.printStackTrace();
/* 216:358 */       LogManager.getInstance().log("Error during data selecting");
/* 217:359 */       LogManager.getInstance().log(e.getMessage());
/* 218:    */     }
/* 219:360 */     return "";
/* 220:    */   }
/* 221:    */   
/* 222:    */   protected String safe_field_name(String str)
/* 223:    */   {
/* 224:373 */     return str.split("[ \n\t;]+")[0];
/* 225:    */   }
/* 226:    */   
/* 227:    */   public void servlet(HttpServletRequest request, HttpServletResponse response)
/* 228:    */   {
/* 229:383 */     this.http_request = request;
/* 230:384 */     this.http_response = response;
/* 231:    */   }
/* 232:    */   
/* 233:    */   protected void parse_request()
/* 234:    */   {
/* 235:396 */     if (this.dynloading) {
/* 236:397 */       this.request.set_limit(0, this.dynloading_size);
/* 237:    */     }
/* 238:399 */     if (this.http_request.getParameter("editing") != null) {
/* 239:400 */       this.editing = true;
/* 240:    */     }
/* 241:402 */     if (this.http_request.getParameter("ids") != null) {
/* 242:403 */       this.editing = true;
/* 243:    */     }
/* 244:406 */     HashMap<String, String> data = new HashMap();
/* 245:    */     
/* 246:408 */     Map map = this.http_request.getParameterMap();
/* 247:409 */     Iterator it = map.entrySet().iterator();
/* 248:410 */     while (it.hasNext())
/* 249:    */     {
/* 250:411 */       Map.Entry pair = (Map.Entry)it.next();
/* 251:412 */       String key = pair.getKey().toString();
/* 252:413 */       int index = key.indexOf("[");
/* 253:414 */       if (index != -1)
/* 254:    */       {
/* 255:415 */         String subkey = key.substring(index + 1, key.length() - 1);
/* 256:416 */         String key_name = key.substring(0, index);
/* 257:417 */         if (key_name.equals("dhx_filter")) {
/* 258:418 */           this.request.set_filter(resolve_parameter(safe_field_name(subkey)), this.http_request.getParameter(key));
/* 259:419 */         } else if (key_name.equals("dhx_sort")) {
/* 260:420 */           this.request.set_sort(resolve_parameter(safe_field_name(subkey)), this.http_request.getParameter(key));
/* 261:    */         }
/* 262:    */       }
/* 263:    */       else
/* 264:    */       {
/* 265:422 */         data.put(key, this.http_request.getParameter(key));
/* 266:    */       }
/* 267:    */     }
/* 268:425 */     this.incoming_data = data;
/* 269:    */   }
/* 270:    */   
/* 271:    */   protected String resolve_parameter(String name)
/* 272:    */   {
/* 273:438 */     return name;
/* 274:    */   }
/* 275:    */   
/* 276:    */   protected String render_set(ConnectorResultSet result)
/* 277:    */     throws ConnectorOperationException
/* 278:    */   {
/* 279:451 */     StringBuffer output = new StringBuffer();
/* 280:452 */     int index = 0;
/* 281:    */     HashMap<String, String> values;
/* 282:454 */     while ((values = result.get_next()) != null)
/* 283:    */     {
/* 284:    */       HashMap<String, String> values;
/* 285:455 */       DataItem data = this.cfactory.createDataItem(values, this.config, index);
/* 286:456 */       if (data.get_id() == null) {
/* 287:457 */         data.set_id(uuid());
/* 288:    */       }
/* 289:459 */       this.event.trigger().beforeRender(data);
/* 290:460 */       data.to_xml(output);
/* 291:461 */       index++;
/* 292:    */     }
/* 293:463 */     return output.toString();
/* 294:    */   }
/* 295:    */   
/* 296:    */   protected void output_as_xml(ConnectorOutputWriter data)
/* 297:    */   {
/* 298:472 */     data.output(this.http_response, this.encoding);
/* 299:473 */     end_run();
/* 300:    */   }
/* 301:    */   
/* 302:    */   protected void output_as_xml(String xml)
/* 303:    */   {
/* 304:477 */     ConnectorOutputWriter data = new ConnectorOutputWriter(xml, "");
/* 305:478 */     output_as_xml(data);
/* 306:    */   }
/* 307:    */   
/* 308:    */   protected String output_as_xml(ConnectorResultSet result)
/* 309:    */     throws ConnectorOperationException
/* 310:    */   {
/* 311:491 */     String start = "<?xml version='1.0' encoding='" + this.encoding + "' ?>";
/* 312:492 */     ConnectorOutputWriter out = new ConnectorOutputWriter(start + xml_start(), render_set(result) + xml_end());
/* 313:    */     
/* 314:494 */     this.event.trigger().beforeOutput(out, this.http_request, this.http_response);
/* 315:    */     
/* 316:    */ 
/* 317:497 */     output_as_xml(out);
/* 318:498 */     return out.toString();
/* 319:    */   }
/* 320:    */   
/* 321:    */   protected void end_run()
/* 322:    */   {
/* 323:507 */     LogManager.getInstance().log("Done in", Long.toString(System.currentTimeMillis() - this.exec_time) + "ms");
/* 324:508 */     LogManager.getInstance().close();
/* 325:    */   }
/* 326:    */   
/* 327:    */   public void set_encoding(String name)
/* 328:    */   {
/* 329:517 */     this.encoding = name;
/* 330:    */   }
/* 331:    */   
/* 332:    */   public void dynamic_loading(int size)
/* 333:    */   {
/* 334:526 */     this.dynloading = (size != 0);
/* 335:527 */     this.dynloading_size = size;
/* 336:    */   }
/* 337:    */   
/* 338:    */   public void dynamic_loading(boolean state)
/* 339:    */   {
/* 340:538 */     this.dynloading = state;
/* 341:539 */     this.dynloading_size = (state ? 30 : 0);
/* 342:    */   }
/* 343:    */   
/* 344:    */   public void enable_log(String path, boolean output_to_client)
/* 345:    */   {
/* 346:550 */     FileWriter log = null;
/* 347:    */     try
/* 348:    */     {
/* 349:552 */       log = new FileWriter(path, true);
/* 350:    */     }
/* 351:    */     catch (IOException e)
/* 352:    */     {
/* 353:554 */       e.printStackTrace();
/* 354:    */     }
/* 355:556 */     LogManager.getInstance().enable_log(log, output_to_client);
/* 356:    */   }
/* 357:    */   
/* 358:    */   public boolean is_select_mode()
/* 359:    */   {
/* 360:565 */     parse_request();
/* 361:566 */     return !this.editing;
/* 362:    */   }
/* 363:    */   
/* 364:    */   protected String xml_start()
/* 365:    */   {
/* 366:575 */     return "<data>";
/* 367:    */   }
/* 368:    */   
/* 369:    */   protected String xml_end()
/* 370:    */   {
/* 371:584 */     return "</data>";
/* 372:    */   }
/* 373:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.BaseConnector
 * JD-Core Version:    0.7.0.1
 */
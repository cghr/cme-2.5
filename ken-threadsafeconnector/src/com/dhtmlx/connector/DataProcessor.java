/*   1:    */ package com.dhtmlx.connector;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import java.util.Set;
/*   7:    */ 
/*   8:    */ public class DataProcessor
/*   9:    */ {
/*  10:    */   protected BaseConnector connector;
/*  11:    */   protected DataConfig config;
/*  12:    */   protected DataRequest request;
/*  13:    */   protected BaseFactory cfactory;
/*  14:    */   
/*  15:    */   public DataProcessor(BaseConnector connector, DataConfig config, DataRequest request, BaseFactory cfactory)
/*  16:    */   {
/*  17: 40 */     this.connector = connector;
/*  18: 41 */     this.config = config;
/*  19: 42 */     this.request = request;
/*  20: 43 */     this.cfactory = cfactory;
/*  21:    */   }
/*  22:    */   
/*  23:    */   protected String name_data(String name)
/*  24:    */   {
/*  25: 54 */     return name;
/*  26:    */   }
/*  27:    */   
/*  28:    */   protected HashMap<String, HashMap<String, String>> get_post_values(String[] ids)
/*  29:    */   {
/*  30: 65 */     HashMap<String, HashMap<String, String>> data = new HashMap();
/*  31: 66 */     for (int i = 0; i < ids.length; i++)
/*  32:    */     {
/*  33: 67 */       data.put(ids[i], new HashMap());
/*  34: 68 */       if (!this.config.id.name.equals("")) {
/*  35: 69 */         ((HashMap)data.get(ids[i])).put(this.config.id.name, ids[i]);
/*  36:    */       }
/*  37:    */     }
/*  38: 72 */     Iterator<String> it = this.connector.incoming_data.keySet().iterator();
/*  39: 73 */     while (it.hasNext())
/*  40:    */     {
/*  41: 74 */       String key = (String)it.next();
/*  42: 75 */       if (key.indexOf("_") != -1) {
/*  43: 77 */         for (int i = 0; i < ids.length; i++) {
/*  44: 78 */           if (key.indexOf(ids[i]) == 0)
/*  45:    */           {
/*  46: 79 */             String field = key.replace(ids[i] + "_", "");
/*  47: 80 */             ((HashMap)data.get(ids[i])).put(name_data(field), (String)this.connector.incoming_data.get(key));
/*  48: 81 */             break;
/*  49:    */           }
/*  50:    */         }
/*  51:    */       }
/*  52:    */     }
/*  53: 88 */     return data;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public String process()
/*  57:    */     throws ConnectorOperationException, ConnectorConfigException
/*  58:    */   {
/*  59:103 */     ArrayList<DataAction> result = new ArrayList();
/*  60:    */     
/*  61:105 */     String ids = get_list_of_ids();
/*  62:106 */     if (ids == null) {
/*  63:107 */       throw new ConnectorOperationException("Incorrect incoming data, ID of incoming records not recognized");
/*  64:    */     }
/*  65:109 */     String[] id_keys = ids.split(",");
/*  66:110 */     HashMap<String, HashMap<String, String>> data = get_post_values(id_keys);
/*  67:111 */     boolean failed = false;
/*  68:    */     try
/*  69:    */     {
/*  70:114 */       if (this.connector.sql.is_global_transaction()) {
/*  71:115 */         this.connector.sql.begin_transaction();
/*  72:    */       }
/*  73:117 */       for (int i = 0; i < id_keys.length; i++)
/*  74:    */       {
/*  75:118 */         String id = id_keys[i];
/*  76:119 */         HashMap<String, String> item_data = (HashMap)data.get(id);
/*  77:120 */         String status = get_status(item_data);
/*  78:    */         
/*  79:122 */         DataAction action = get_data_action(status, id, item_data);
/*  80:123 */         result.add(action);
/*  81:124 */         inner_process(action);
/*  82:    */       }
/*  83:    */     }
/*  84:    */     catch (ConnectorOperationException e)
/*  85:    */     {
/*  86:127 */       this.connector.event.trigger().afterDBError((DataAction)result.get(result.size() - 1), e);
/*  87:128 */       failed = true;
/*  88:    */     }
/*  89:131 */     if (this.connector.sql.is_global_transaction())
/*  90:    */     {
/*  91:132 */       if (!failed) {
/*  92:133 */         for (int i = 0; i < result.size(); i++)
/*  93:    */         {
/*  94:134 */           String result_status = ((DataAction)result.get(i)).get_status();
/*  95:135 */           if ((result_status.equals("error")) || (result_status.equals("invalid")))
/*  96:    */           {
/*  97:136 */             failed = true;
/*  98:137 */             break;
/*  99:    */           }
/* 100:    */         }
/* 101:    */       }
/* 102:140 */       if (failed)
/* 103:    */       {
/* 104:141 */         for (int i = 0; i < result.size(); i++) {
/* 105:142 */           ((DataAction)result.get(i)).error();
/* 106:    */         }
/* 107:143 */         this.connector.sql.rollback_transaction();
/* 108:    */       }
/* 109:    */       else
/* 110:    */       {
/* 111:145 */         this.connector.sql.commit_transaction();
/* 112:    */       }
/* 113:    */     }
/* 114:149 */     return output_as_xml(result);
/* 115:    */   }
/* 116:    */   
/* 117:    */   protected DataAction get_data_action(String status, String id, HashMap<String, String> itemData)
/* 118:    */   {
/* 119:154 */     return new DataAction(status, id, itemData);
/* 120:    */   }
/* 121:    */   
/* 122:    */   protected String get_status(HashMap<String, String> itemData)
/* 123:    */   {
/* 124:158 */     return (String)itemData.get("!nativeeditor_status");
/* 125:    */   }
/* 126:    */   
/* 127:    */   protected String get_list_of_ids()
/* 128:    */   {
/* 129:162 */     return (String)this.connector.incoming_data.get("ids");
/* 130:    */   }
/* 131:    */   
/* 132:    */   protected OperationType status_to_mode(String status)
/* 133:    */     throws ConnectorOperationException
/* 134:    */   {
/* 135:175 */     if ((status.equals("updated")) || (status.equals("update"))) {
/* 136:175 */       return OperationType.Update;
/* 137:    */     }
/* 138:176 */     if ((status.equals("inserted")) || (status.equals("insert"))) {
/* 139:176 */       return OperationType.Insert;
/* 140:    */     }
/* 141:177 */     if ((status.equals("deleted")) || (status.equals("delete"))) {
/* 142:177 */       return OperationType.Delete;
/* 143:    */     }
/* 144:179 */     throw new ConnectorOperationException("Unknown action type: " + status);
/* 145:    */   }
/* 146:    */   
/* 147:    */   protected String output_as_xml(ArrayList<DataAction> result)
/* 148:    */   {
/* 149:191 */     StringBuffer out = new StringBuffer();
/* 150:192 */     out.append("<data>");
/* 151:193 */     for (int i = 0; i < result.size(); i++) {
/* 152:194 */       out.append(((DataAction)result.get(i)).to_xml());
/* 153:    */     }
/* 154:195 */     out.append("</data>");
/* 155:    */     
/* 156:197 */     return out.toString();
/* 157:    */   }
/* 158:    */   
/* 159:    */   private void inner_process(DataAction action)
/* 160:    */     throws ConnectorConfigException, ConnectorOperationException
/* 161:    */   {
/* 162:209 */     if (this.connector.sql.is_record_transaction()) {
/* 163:210 */       this.connector.sql.begin_transaction();
/* 164:    */     }
/* 165:    */     try
/* 166:    */     {
/* 167:213 */       OperationType mode = status_to_mode(action.get_status());
/* 168:214 */       if (!this.connector.access.check(mode))
/* 169:    */       {
/* 170:215 */         action.error();
/* 171:    */       }
/* 172:    */       else
/* 173:    */       {
/* 174:217 */         this.connector.event.trigger().beforeProcessing(action);
/* 175:218 */         if (!action.is_ready()) {
/* 176:219 */           check_exts(action, mode);
/* 177:    */         }
/* 178:220 */         this.connector.event.trigger().afterProcessing(action);
/* 179:    */       }
/* 180:    */     }
/* 181:    */     catch (ConnectorConfigException e)
/* 182:    */     {
/* 183:223 */       action.set_status("error");
/* 184:224 */       throw e;
/* 185:    */     }
/* 186:    */     catch (ConnectorOperationException e)
/* 187:    */     {
/* 188:226 */       action.set_status("error");
/* 189:227 */       throw e;
/* 190:    */     }
/* 191:230 */     if (this.connector.sql.is_record_transaction()) {
/* 192:231 */       if ((action.get_status().equals("invalid")) || (action.get_status().equals("error"))) {
/* 193:232 */         this.connector.sql.rollback_transaction();
/* 194:    */       } else {
/* 195:234 */         this.connector.sql.commit_transaction();
/* 196:    */       }
/* 197:    */     }
/* 198:    */   }
/* 199:    */   
/* 200:    */   private void check_exts(DataAction action, OperationType mode)
/* 201:    */     throws ConnectorConfigException, ConnectorOperationException
/* 202:    */   {
/* 203:248 */     switch (mode)
/* 204:    */     {
/* 205:    */     case Update: 
/* 206:250 */       this.connector.event.trigger().beforeDelete(action);
/* 207:251 */       break;
/* 208:    */     case Insert: 
/* 209:253 */       this.connector.event.trigger().beforeInsert(action);
/* 210:254 */       break;
/* 211:    */     case Read: 
/* 212:256 */       this.connector.event.trigger().beforeUpdate(action);
/* 213:    */     }
/* 214:260 */     if (!action.is_ready())
/* 215:    */     {
/* 216:261 */       String sql = this.connector.sql.get_sql(mode, action.get_data());
/* 217:262 */       if ((sql != null) && (!sql.equals("")))
/* 218:    */       {
/* 219:263 */         ((DBDataWrapper)this.connector.sql).query(sql);
/* 220:264 */         if (mode.equals(OperationType.Insert)) {
/* 221:265 */           action.success(((DBDataWrapper)this.connector.sql).get_new_id(null));
/* 222:    */         }
/* 223:    */       }
/* 224:    */       else
/* 225:    */       {
/* 226:267 */         action.sync_config(this.config);
/* 227:268 */         switch (mode)
/* 228:    */         {
/* 229:    */         case Update: 
/* 230:270 */           this.connector.sql.delete(action, this.request);
/* 231:271 */           break;
/* 232:    */         case Insert: 
/* 233:273 */           this.connector.sql.insert(action, this.request);
/* 234:274 */           break;
/* 235:    */         case Read: 
/* 236:276 */           this.connector.sql.update(action, this.request);
/* 237:    */         }
/* 238:    */       }
/* 239:    */     }
/* 240:282 */     switch (mode)
/* 241:    */     {
/* 242:    */     case Update: 
/* 243:284 */       this.connector.event.trigger().afterDelete(action);
/* 244:285 */       break;
/* 245:    */     case Insert: 
/* 246:287 */       this.connector.event.trigger().afterInsert(action);
/* 247:288 */       break;
/* 248:    */     case Read: 
/* 249:290 */       this.connector.event.trigger().afterUpdate(action);
/* 250:    */     }
/* 251:    */   }
/* 252:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.DataProcessor
 * JD-Core Version:    0.7.0.1
 */
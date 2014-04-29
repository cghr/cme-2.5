/*   1:    */ package com.kentropy.tree;
/*   2:    */ 
/*   3:    */ import com.kentropy.util.SpringApplicationContext;
/*   4:    */ import com.kentropy.util.SpringUtils;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.text.MessageFormat;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ import java.util.HashMap;
/*   9:    */ import java.util.Hashtable;
/*  10:    */ import java.util.List;
/*  11:    */ import java.util.Map;
/*  12:    */ import java.util.Map.Entry;
/*  13:    */ import java.util.Vector;
/*  14:    */ import org.springframework.context.support.ClassPathXmlApplicationContext;
/*  15:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*  16:    */ 
/*  17:    */ public class DBNode
/*  18:    */ {
/*  19: 26 */   public boolean distinct = false;
/*  20: 27 */   public String path = "";
/*  21:    */   
/*  22:    */   public void visit(TreeVisitor vis)
/*  23:    */   {
/*  24: 30 */     vis.doTask(this);
/*  25: 31 */     for (DBNode dbn : this.children) {
/*  26: 33 */       dbn.visit(vis);
/*  27:    */     }
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void visit1(TreeVisitor vis)
/*  31:    */   {
/*  32: 38 */     vis.doTask(this);
/*  33:    */   }
/*  34:    */   
/*  35: 42 */   public static int id = 1;
/*  36: 43 */   List<Map<String, String>> tree = null;
/*  37: 44 */   String table = "";
/*  38: 45 */   String keyField = "";
/*  39: 46 */   String key = null;
/*  40: 47 */   String parentKey = null;
/*  41: 48 */   String parentKeyField = "";
/*  42: 49 */   Map<String, Object> data = null;
/*  43:    */   
/*  44:    */   public Map getData()
/*  45:    */   {
/*  46: 52 */     JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/*  47: 53 */     String sql = " select * from " + this.table + " where " + this.keyField + "='" + this.key + "'";
/*  48:    */     
/*  49: 55 */     this.data = jt.queryForMap(sql);
/*  50:    */     
/*  51: 57 */     return this.data;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void deleteData()
/*  55:    */   {
/*  56: 60 */     JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/*  57: 61 */     String qry = "delete from " + this.table + " where id='" + this.key + "'";
/*  58: 62 */     jt.execute(qry);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void deleteTree()
/*  62:    */   {
/*  63: 67 */     for (DBNode dbn : this.children) {
/*  64: 69 */       dbn.deleteData();
/*  65:    */     }
/*  66: 71 */     deleteData();
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void updateData(String version)
/*  70:    */   {
/*  71: 75 */     JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/*  72: 76 */     String sql = "update " + this.table + " set version='" + version + "' where id='" + this.key + "'";
/*  73:    */     
/*  74: 78 */     jt.update(sql);
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void updateTree(String version)
/*  78:    */   {
/*  79: 81 */     updateData(version);
/*  80: 82 */     for (DBNode dbn : this.children) {
/*  81: 83 */       dbn.updateTree(version);
/*  82:    */     }
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void saveData()
/*  86:    */   {
/*  87: 88 */     JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/*  88: 89 */     String sql = "insert into " + this.table + " ({0}) VALUES ({1}) ";
/*  89:    */     
/*  90:    */ 
/*  91: 92 */     StringBuffer fields = new StringBuffer();
/*  92: 93 */     StringBuffer values = new StringBuffer();
/*  93: 94 */     MessageFormat mf = new MessageFormat(sql);
/*  94: 95 */     for (Map.Entry<String, Object> e : this.data.entrySet())
/*  95:    */     {
/*  96: 97 */       fields.append((String)e.getKey() + ",");
/*  97: 98 */       if (e.getValue() == null) {
/*  98:100 */         values.append(e.getValue() + ",");
/*  99:    */       } else {
/* 100:103 */         values.append("'" + e.getValue() + "',");
/* 101:    */       }
/* 102:    */     }
/* 103:105 */     fields.delete(fields.length() - 1, fields.length());
/* 104:106 */     values.delete(values.length() - 1, values.length());
/* 105:107 */     String[] obj = { fields.toString(), values.toString() };
/* 106:108 */     sql = MessageFormat.format(sql, obj);
/* 107:    */     
/* 108:110 */     jt.update(sql);
/* 109:    */   }
/* 110:    */   
/* 111:    */   public void saveTree()
/* 112:    */   {
/* 113:114 */     saveData();
/* 114:115 */     for (DBNode dbn : this.children) {
/* 115:117 */       dbn.saveTree();
/* 116:    */     }
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void setData(Map<String, Object> map)
/* 120:    */   {
/* 121:122 */     this.data = map;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public void copy(DBNode d2)
/* 125:    */   {
/* 126:126 */     if (this.tree.size() > 1)
/* 127:    */     {
/* 128:128 */       d2.setData(getData());
/* 129:129 */       for (DBNode dbn : this.children)
/* 130:    */       {
/* 131:131 */         DBNode dbNode = new DBNode(d2.tree.subList(1, d2.tree.size()), dbn.key, this.key);
/* 132:132 */         dbn.copy(dbNode);
/* 133:133 */         d2.children.add(dbNode);
/* 134:    */       }
/* 135:    */     }
/* 136:    */   }
/* 137:    */   
/* 138:    */   public void construct()
/* 139:    */   {
/* 140:141 */     queryChildren();
/* 141:    */   }
/* 142:    */   
/* 143:    */   public void printTree()
/* 144:    */   {
/* 145:146 */     System.out.println(this.parentKey + "-" + this.table + "-" + this.key + " ---" + this.data);
/* 146:147 */     if (this.tree.size() > 1) {
/* 147:149 */       for (DBNode dbn : this.children) {
/* 148:151 */         dbn.printTree();
/* 149:    */       }
/* 150:    */     }
/* 151:    */   }
/* 152:    */   
/* 153:    */   public void printTree1(StringBuffer sb)
/* 154:    */   {
/* 155:160 */     if (this.tree.size() > 1)
/* 156:    */     {
/* 157:162 */       if (!this.keyField.equals("all1")) {
/* 158:163 */         sb.append(this.path + (this.path.equals("") ? " " : " and   ") + this.keyField + "='" + this.key + "' \r\n");
/* 159:    */       }
/* 160:165 */       for (DBNode dbn : this.children) {
/* 161:167 */         dbn.printTree1(sb);
/* 162:    */       }
/* 163:    */     }
/* 164:    */     else
/* 165:    */     {
/* 166:172 */       sb.append(this.path + (this.path.equals("") ? " " : " and   ") + this.keyField + "='" + this.key + "' \r\n");
/* 167:    */     }
/* 168:    */   }
/* 169:    */   
/* 170:    */   public int getTotalLeaves()
/* 171:    */   {
/* 172:178 */     if (this.children.size() > 0)
/* 173:    */     {
/* 174:180 */       int count = 0;
/* 175:181 */       for (DBNode n : this.children) {
/* 176:183 */         count += n.getTotalLeaves();
/* 177:    */       }
/* 178:186 */       if (((DBNode)this.children.get(0)).children.size() == 0) {
/* 179:187 */         count++;
/* 180:    */       }
/* 181:189 */       return count;
/* 182:    */     }
/* 183:193 */     return 1;
/* 184:    */   }
/* 185:    */   
/* 186:    */   public int getMaxDepth(int depth)
/* 187:    */   {
/* 188:200 */     if (this.children.size() > 0)
/* 189:    */     {
/* 190:202 */       int count = 0;
/* 191:203 */       int maxDepth = depth;
/* 192:204 */       for (DBNode n : this.children)
/* 193:    */       {
/* 194:207 */         int tmpDepth = n.getMaxDepth(depth + 1);
/* 195:208 */         if (tmpDepth > maxDepth) {
/* 196:209 */           maxDepth = tmpDepth;
/* 197:    */         }
/* 198:    */       }
/* 199:213 */       return maxDepth;
/* 200:    */     }
/* 201:217 */     return depth;
/* 202:    */   }
/* 203:    */   
/* 204:    */   private void queryChildren()
/* 205:    */   {
/* 206:223 */     if (this.tree.size() > 1)
/* 207:    */     {
/* 208:225 */       String table1 = (String)((Map)this.tree.get(1)).get("table");
/* 209:226 */       String parentKeyField1 = (String)((Map)this.tree.get(1)).get("parentKeyField");
/* 210:227 */       String keyField = (String)((Map)this.tree.get(1)).get("keyField");
/* 211:228 */       String where = (String)((Map)this.tree.get(1)).get("where");
/* 212:    */       
/* 213:    */ 
/* 214:231 */       String qry = "select " + (this.distinct ? "distinct " : "") + keyField + "  from " + table1 + " where " + parentKeyField1 + "='" + this.key + "' " + (where == null ? "" : new StringBuilder(" and ").append(where).toString());
/* 215:232 */       System.out.println("Query " + qry);
/* 216:    */       
/* 217:234 */       JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/* 218:235 */       List<Map<String, Object>> values = jt.queryForList(qry);
/* 219:236 */       for (Map map : values)
/* 220:    */       {
/* 221:238 */         Object val = map.get(keyField);
/* 222:    */         
/* 223:    */ 
/* 224:241 */         DBNode dbn1 = new DBNode(this.tree.subList(1, this.tree.size()), val.toString(), this.key);
/* 225:    */         
/* 226:243 */         dbn1.distinct = this.distinct;
/* 227:244 */         if (this.parentKey != null) {
/* 228:246 */           dbn1.path = (this.path + (this.path.length() == 0 ? "" : " and  ") + this.keyField + "='" + this.key + "' ");
/* 229:    */         }
/* 230:249 */         this.children.add(dbn1);
/* 231:250 */         dbn1.construct();
/* 232:    */       }
/* 233:    */     }
/* 234:    */   }
/* 235:    */   
/* 236:    */   public DBNode(List<Map<String, String>> subList, String key1, String parentKey2)
/* 237:    */   {
/* 238:302 */     this.tree = subList;
/* 239:303 */     this.table = ((String)((Map)subList.get(0)).get("table"));
/* 240:304 */     this.keyField = ((String)((Map)subList.get(0)).get("keyField"));
/* 241:305 */     this.parentKeyField = ((String)((Map)subList.get(0)).get("parentKeyField"));
/* 242:306 */     this.key = key1;
/* 243:307 */     this.parentKey = parentKey2;
/* 244:    */   }
/* 245:    */   
/* 246:313 */   public Vector<DBNode> children = new Vector();
/* 247:314 */   Hashtable<String, String> attributes = new Hashtable();
/* 248:    */   String name;
/* 249:    */   
/* 250:    */   public void addChild(DBNode c)
/* 251:    */   {
/* 252:321 */     this.children.add(c);
/* 253:    */   }
/* 254:    */   
/* 255:    */   public DBNode getDBNode(String name)
/* 256:    */   {
/* 257:326 */     for (int i = 0; i < this.children.size(); i++) {
/* 258:329 */       if (((DBNode)this.children.get(i)).name.equals(name)) {
/* 259:331 */         return (DBNode)this.children.get(i);
/* 260:    */       }
/* 261:    */     }
/* 262:334 */     return null;
/* 263:    */   }
/* 264:    */   
/* 265:    */   public static Map createTableDef(String table, String keyField, String parentKeyField, String where)
/* 266:    */   {
/* 267:494 */     HashMap<String, String> m = new HashMap();
/* 268:495 */     m.put("table", table);
/* 269:496 */     m.put("keyField", keyField);
/* 270:497 */     m.put("parentKeyField", parentKeyField);
/* 271:498 */     m.put("where", where);
/* 272:    */     
/* 273:500 */     return m;
/* 274:    */   }
/* 275:    */   
/* 276:    */   public static List<Map<String, String>> getTree()
/* 277:    */   {
/* 278:505 */     List<Map<String, String>> tree = new ArrayList();
/* 279:506 */     tree.add(createTableDef("project", "id", null, "true"));
/* 280:507 */     tree.add(createTableDef("deliverable", "id", "project_id", "true"));
/* 281:508 */     tree.add(createTableDef("activity", "id", "deliverable_id", "true"));
/* 282:509 */     tree.add(createTableDef("activity_cost", "id", "activity_id", "true"));
/* 283:510 */     tree.add(createTableDef("activity_cost_resource", "id", "activity_cost_id", "true"));
/* 284:511 */     return tree;
/* 285:    */   }
/* 286:    */   
/* 287:    */   public static List<Map<String, String>> getBaseLine()
/* 288:    */   {
/* 289:516 */     List<Map<String, String>> tree2 = new ArrayList();
/* 290:517 */     String key = "true";
/* 291:518 */     tree2.add(createTableDef("project_base", "id", null, key));
/* 292:519 */     tree2.add(createTableDef("deliverable_base", "id", "project_id", key));
/* 293:520 */     tree2.add(createTableDef("activity_base", "id", "deliverable_id", key));
/* 294:521 */     tree2.add(createTableDef("activity_cost_base", "id", "activity_id", key));
/* 295:522 */     tree2.add(createTableDef("activity_cost_resource_base", "id", "activity_cost_id", key));
/* 296:523 */     return tree2;
/* 297:    */   }
/* 298:    */   
/* 299:    */   public static List<Map<String, String>> getDimTree(String[] colDims, String table, String where)
/* 300:    */   {
/* 301:527 */     List<Map<String, String>> tree2 = new ArrayList();
/* 302:529 */     for (int i = 0; i < colDims.length; i++) {
/* 303:531 */       if (i > 0) {
/* 304:533 */         tree2.add(createTableDef(table, colDims[i], colDims[(i - 1)], where));
/* 305:    */       } else {
/* 306:536 */         tree2.add(createTableDef(table, colDims[i], null, where));
/* 307:    */       }
/* 308:    */     }
/* 309:540 */     return tree2;
/* 310:    */   }
/* 311:    */   
/* 312:    */   public static void main(String[] args)
/* 313:    */     throws Exception
/* 314:    */   {
/* 315:548 */     new SpringApplicationContext().setApplicationContext(new ClassPathXmlApplicationContext("appContext.xml"));
/* 316:549 */     String[] colDims = { "period", "sector_name", "country_name" };
/* 317:550 */     String table = "project_details_workorder ";
/* 318:551 */     String where = "true";
/* 319:552 */     DBNode d1 = new DBNode(getDimTree(colDims, table, where), "2013-04", null);
/* 320:553 */     d1.distinct = true;
/* 321:554 */     d1.construct();
/* 322:555 */     StringBuffer sb = new StringBuffer();
/* 323:556 */     d1.printTree1(sb);
/* 324:557 */     System.out.println(sb + "-----------");
/* 325:    */   }
/* 326:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-utilities6\ken-utilities6.jar
 * Qualified Name:     com.kentropy.tree.DBNode
 * JD-Core Version:    0.7.0.1
 */
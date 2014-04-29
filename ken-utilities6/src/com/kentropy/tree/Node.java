/*   1:    */ package com.kentropy.tree;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ import java.sql.Connection;
/*   5:    */ import java.sql.DriverManager;
/*   6:    */ import java.sql.ResultSet;
/*   7:    */ import java.sql.Statement;
/*   8:    */ import java.util.Enumeration;
/*   9:    */ import java.util.Hashtable;
/*  10:    */ import java.util.Iterator;
/*  11:    */ import java.util.Vector;
/*  12:    */ 
/*  13:    */ public class Node
/*  14:    */ {
/*  15: 13 */   public static int id = 1;
/*  16: 17 */   String tag = "data";
/*  17: 18 */   String image = "books_cat.gif";
/*  18:    */   
/*  19:    */   public String getTag()
/*  20:    */   {
/*  21: 20 */     return this.tag;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void setTag(String tag)
/*  25:    */   {
/*  26: 24 */     this.tag = tag;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public String getText()
/*  30:    */   {
/*  31: 29 */     return this.text;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setText(String text)
/*  35:    */   {
/*  36: 33 */     this.text = text;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public String getName()
/*  40:    */   {
/*  41: 37 */     return this.name;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setName(String name)
/*  45:    */   {
/*  46: 41 */     this.name = name;
/*  47:    */   }
/*  48:    */   
/*  49: 44 */   String text = "";
/*  50: 45 */   int nodeId = 0;
/*  51:    */   
/*  52:    */   public Node(String tag, String name, String image2)
/*  53:    */   {
/*  54: 48 */     this.tag = tag;
/*  55: 49 */     this.name = name;
/*  56: 50 */     this.image = image2;
/*  57: 51 */     this.nodeId = id;
/*  58: 52 */     id += 1;
/*  59:    */   }
/*  60:    */   
/*  61: 55 */   public Vector<Node> children = new Vector();
/*  62: 56 */   Hashtable<String, String> attributes = new Hashtable();
/*  63:    */   String name;
/*  64:    */   
/*  65:    */   public void addChild(Node c)
/*  66:    */   {
/*  67: 61 */     this.children.add(c);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public Node getNode(String name)
/*  71:    */   {
/*  72: 66 */     for (int i = 0; i < this.children.size(); i++) {
/*  73: 69 */       if (((Node)this.children.get(i)).name.equals(name)) {
/*  74: 71 */         return (Node)this.children.get(i);
/*  75:    */       }
/*  76:    */     }
/*  77: 74 */     return null;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public Node getOrCreateNode(String name, String tag, String image)
/*  81:    */   {
/*  82: 81 */     if (getNode(name) == null)
/*  83:    */     {
/*  84: 84 */       Node node = new Node(tag, name, image);
/*  85:    */       
/*  86: 86 */       addChild(node);
/*  87:    */     }
/*  88: 88 */     return getNode(name);
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void addAttribute(String name, String value)
/*  92:    */   {
/*  93: 92 */     this.attributes.put(name, value);
/*  94:    */   }
/*  95:    */   
/*  96:    */   public String getAttribute(String name)
/*  97:    */   {
/*  98: 97 */     return (String)this.attributes.get(name);
/*  99:    */   }
/* 100:    */   
/* 101:    */   public String toString()
/* 102:    */   {
/* 103:101 */     String img = "image='" + this.image + "'";
/* 104:102 */     String str = "<" + this.tag + " " + img + " ";
/* 105:103 */     if (this.name != null) {
/* 106:104 */       str = str + " id='" + this.nodeId + "' ";
/* 107:    */     }
/* 108:105 */     Enumeration<String> e = this.attributes.keys();
/* 109:    */     String value;
/* 110:106 */     while (e.hasMoreElements())
/* 111:    */     {
/* 112:108 */       String key = (String)e.nextElement();
/* 113:109 */       value = getAttribute(key);
/* 114:110 */       str = str + key + "='" + value + "' ";
/* 115:    */     }
/* 116:112 */     str = str + ">";
/* 117:114 */     for (Node n : this.children) {
/* 118:116 */       str = str + "\r\n" + n.toString();
/* 119:    */     }
/* 120:118 */     str = str + "\r\n" + this.text;
/* 121:119 */     str = str + "\r\n</" + this.tag + ">";
/* 122:    */     
/* 123:121 */     return str;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public static void testNode(Vector<String> headers, Vector<String> images, Vector<String> aggregations, Vector<Vector<String>> values, int numFacts, Node root)
/* 127:    */   {
/* 128:129 */     int rows1 = 0;
/* 129:    */     int factIdx;
/* 130:130 */     for (Iterator localIterator = values.iterator(); localIterator.hasNext(); factIdx.hasNext())
/* 131:    */     {
/* 132:130 */       Vector<String> row = (Vector)localIterator.next();
/* 133:131 */       int i = 0;
/* 134:132 */       Node parent = root;
/* 135:133 */       rows1++;
/* 136:134 */       System.out.println("Size " + row.size() + " " + headers.size());
/* 137:135 */       Node n1 = parent.getOrCreateNode("heading", "cell", "folder.gif");
/* 138:136 */       for (int k = row.size() - numFacts; k < row.size(); k++)
/* 139:    */       {
/* 140:138 */         n1 = parent.getOrCreateNode("test_" + k, "cell", "");
/* 141:139 */         factIdx = k - (row.size() - numFacts);
/* 142:141 */         if (((String)aggregations.get(factIdx)).trim().equals("SUM"))
/* 143:    */         {
/* 144:143 */           int fact = Integer.parseInt((String)row.get(k));
/* 145:144 */           String cur = n1.getText();
/* 146:146 */           if (cur.equals("")) {
/* 147:147 */             n1.setText(fact);
/* 148:    */           } else {
/* 149:150 */             n1.setText(Integer.parseInt(cur) + fact);
/* 150:    */           }
/* 151:    */         }
/* 152:    */       }
/* 153:161 */       factIdx = row.iterator(); continue;String col = (String)factIdx.next();
/* 154:163 */       if (i < row.size() - numFacts)
/* 155:    */       {
/* 156:174 */         parent = parent.getOrCreateNode(col, "row", "");
/* 157:175 */         String image = i == row.size() - numFacts - 1 ? "leaf.gif" : "folder.gif";
/* 158:176 */         n1 = parent.getOrCreateNode("heading", "cell", image);
/* 159:177 */         n1.setText(col);
/* 160:178 */         for (int k = row.size() - numFacts; k < row.size(); k++)
/* 161:    */         {
/* 162:180 */           n1 = parent.getOrCreateNode("test_" + k, "cell", "");
/* 163:    */           
/* 164:182 */           String cur = n1.getText();
/* 165:183 */           int factIdx = k - (row.size() - numFacts);
/* 166:185 */           if ((((String)aggregations.get(factIdx)).trim().equals("SUM")) || (i == row.size() - numFacts - 1))
/* 167:    */           {
/* 168:187 */             String fact = (String)row.get(k);
/* 169:189 */             if (cur.equals(""))
/* 170:    */             {
/* 171:190 */               n1.setText(fact);
/* 172:    */             }
/* 173:193 */             else if (((String)aggregations.get(factIdx)).trim().equals("SUM"))
/* 174:    */             {
/* 175:195 */               int fact1 = Integer.parseInt((String)row.get(k));
/* 176:196 */               n1.setText(Integer.parseInt(cur) + fact1);
/* 177:    */             }
/* 178:    */           }
/* 179:    */         }
/* 180:    */       }
/* 181:207 */       i++;
/* 182:    */     }
/* 183:    */   }
/* 184:    */   
/* 185:    */   public String getImage()
/* 186:    */   {
/* 187:215 */     return this.image;
/* 188:    */   }
/* 189:    */   
/* 190:    */   public void setImage(String image)
/* 191:    */   {
/* 192:219 */     this.image = image;
/* 193:    */   }
/* 194:    */   
/* 195:    */   public static Connection getConnection()
/* 196:    */     throws Exception
/* 197:    */   {
/* 198:223 */     String driverClassName = "com.mysql.jdbc.Driver";
/* 199:224 */     String connectionUrl = "jdbc:mysql://localhost:3306/kenproj";
/* 200:225 */     String dbUser = "root";
/* 201:226 */     String dbPwd = "password";
/* 202:227 */     Class.forName(driverClassName);
/* 203:228 */     Connection conn = null;
/* 204:229 */     conn = DriverManager.getConnection(connectionUrl, dbUser, dbPwd);
/* 205:230 */     return conn;
/* 206:    */   }
/* 207:    */   
/* 208:    */   public static void transpose(Node n)
/* 209:    */   {
/* 210:235 */     Node n2 = new Node("rows", "test", "folder.gif");
/* 211:    */     Iterator localIterator2;
/* 212:237 */     for (Iterator localIterator1 = n.children.iterator(); localIterator1.hasNext(); localIterator2.hasNext())
/* 213:    */     {
/* 214:237 */       Node top1 = (Node)localIterator1.next();
/* 215:    */       
/* 216:239 */       localIterator2 = top1.children.iterator(); continue;Node n1 = (Node)localIterator2.next();
/* 217:241 */       if (n1.tag.equals("row"))
/* 218:    */       {
/* 219:243 */         Node n0 = n2.getNode(n1.name);
/* 220:244 */         if (n0 == null) {
/* 221:246 */           n2.addChild(n1);
/* 222:    */         } else {
/* 223:250 */           n0.addChild((Node)n1.children.get(1));
/* 224:    */         }
/* 225:    */       }
/* 226:    */     }
/* 227:255 */     System.out.println(n2);
/* 228:    */   }
/* 229:    */   
/* 230:    */   public static void main(String[] args)
/* 231:    */     throws Exception
/* 232:    */   {
/* 233:261 */     Node n = new Node("row", "root", "");
/* 234:    */     
/* 235:263 */     Vector<String> headers = new Vector();
/* 236:264 */     headers.add("project");
/* 237:265 */     headers.add("resource_type");
/* 238:266 */     headers.add("deliverable");
/* 239:267 */     headers.add("activity");
/* 240:268 */     headers.add("internal_number");
/* 241:    */     
/* 242:    */ 
/* 243:    */ 
/* 244:    */ 
/* 245:273 */     Vector<String> images = new Vector();
/* 246:274 */     images.add("book_cat.gif");
/* 247:275 */     images.add("book_cat.gif");
/* 248:276 */     images.add("book_cat.gif");
/* 249:277 */     images.add("book_cat.gif");
/* 250:    */     
/* 251:279 */     Vector<String> aggregations = new Vector();
/* 252:    */     
/* 253:    */ 
/* 254:282 */     aggregations.add("SUM");
/* 255:    */     
/* 256:    */ 
/* 257:    */ 
/* 258:    */ 
/* 259:    */ 
/* 260:288 */     String qry = "SELECT project,resource_type,deliverable,activity,int_number,int_days,SUM(int_days*int_number) mandays FROM estimation WHERE resource_type IS NOT NULL  GROUP BY project,resource_type,deliverable,activity ";
/* 261:    */     
/* 262:    */ 
/* 263:    */ 
/* 264:292 */     Connection con = getConnection();
/* 265:293 */     Statement st = con.createStatement();
/* 266:294 */     ResultSet rs = st.executeQuery(qry);
/* 267:    */     
/* 268:    */ 
/* 269:297 */     Vector<Vector<String>> values = new Vector();
/* 270:298 */     while (rs.next())
/* 271:    */     {
/* 272:299 */       Vector<String> row1 = new Vector();
/* 273:300 */       row1.add(rs.getString("project"));
/* 274:301 */       row1.add(rs.getString("resource_type"));
/* 275:302 */       row1.add(rs.getString("deliverable"));
/* 276:303 */       row1.add(rs.getString("activity"));
/* 277:304 */       row1.add(rs.getString("int_number"));
/* 278:    */       
/* 279:    */ 
/* 280:307 */       values.add(row1);
/* 281:    */     }
/* 282:326 */     int numFacts = 1;
/* 283:327 */     testNode(headers, images, aggregations, values, numFacts, n);
/* 284:328 */     System.out.println("--------------------");
/* 285:329 */     transpose(n);
/* 286:330 */     System.out.println("--------------------");
/* 287:    */   }
/* 288:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-utilities6\ken-utilities6.jar
 * Qualified Name:     com.kentropy.tree.Node
 * JD-Core Version:    0.7.0.1
 */
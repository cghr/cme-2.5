/*   1:    */ package com.kentropy.services;
/*   2:    */ 
/*   3:    */ import de.schlund.pfixxml.util.MD5Utils;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.io.OutputStream;
/*   7:    */ import java.io.PrintStream;
/*   8:    */ import java.io.StringReader;
/*   9:    */ import java.io.StringWriter;
/*  10:    */ import java.net.HttpURLConnection;
/*  11:    */ import java.net.MalformedURLException;
/*  12:    */ import java.net.URL;
/*  13:    */ import net.xoetrope.data.XDataSource;
/*  14:    */ import net.xoetrope.xml.XmlElement;
/*  15:    */ import net.xoetrope.xml.XmlSource;
/*  16:    */ import net.xoetrope.xui.data.XBaseModel;
/*  17:    */ import net.xoetrope.xui.data.XModel;
/*  18:    */ 
/*  19:    */ public class TransactionClient
/*  20:    */ {
/*  21: 29 */   public String SERVER_URL = "http://54.243.251.166/cme/";
/*  22:    */   
/*  23:    */   public XModel parse(String xmlStr)
/*  24:    */   {
/*  25: 37 */     StringReader sr = new StringReader(xmlStr);
/*  26: 38 */     System.out.println(xmlStr);
/*  27: 39 */     XmlElement xe = XmlSource.read(sr);
/*  28:    */     
/*  29: 41 */     XDataSource xd = new XDataSource();
/*  30: 42 */     XModel xm = new XBaseModel();
/*  31: 43 */     xd.loadTable(xe, xm);
/*  32: 44 */     return xm;
/*  33:    */   }
/*  34:    */   
/*  35:    */   XModel saveKeyvalueTr(String table, String key, String value)
/*  36:    */     throws Exception
/*  37:    */   {
/*  38: 47 */     XModel test = new XBaseModel();
/*  39:    */     
/*  40: 49 */     test.set("@op", "saveKeyvalue");
/*  41: 50 */     test.set("@table", table);
/*  42: 51 */     test.set("@key", key);
/*  43: 52 */     test.set(value);
/*  44: 53 */     return test;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void saveDataTr(String table, String where, XModel dataM, boolean changelog)
/*  48:    */   {
/*  49: 57 */     dataM.set("@op", "saveData");
/*  50: 58 */     dataM.set("@table", table);
/*  51:    */     
/*  52: 60 */     dataM.set("@where", where);
/*  53: 61 */     dataM.set("@changelog", Boolean.valueOf(changelog));
/*  54:    */   }
/*  55:    */   
/*  56:    */   public XModel getDataTr(String table, String fields, String where)
/*  57:    */     throws Exception
/*  58:    */   {
/*  59: 66 */     XModel test = new XBaseModel();
/*  60: 67 */     test.set("@op", "getData");
/*  61: 68 */     test.set("@table", table);
/*  62: 69 */     test.set("@fields", fields);
/*  63: 70 */     test.set("@where", where);
/*  64: 71 */     return test;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public XModel getKeyvaluesTr(String table, String parentpath)
/*  68:    */     throws Exception
/*  69:    */   {
/*  70: 75 */     XModel test1 = new XBaseModel();
/*  71: 76 */     test1.set("@op", "getKeyvalues");
/*  72: 77 */     test1.set("@table", "keyvalue");
/*  73: 78 */     test1.set("@parentpath", parentpath);
/*  74: 79 */     test1.set("@where", " member !='' and status is null and assignedto='385'");
/*  75: 80 */     return test1;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public XModel getRefreshProcessesTr(String where)
/*  79:    */     throws Exception
/*  80:    */   {
/*  81: 85 */     XModel test = new XBaseModel();
/*  82: 86 */     test.set("@op", "refreshProcesses");
/*  83:    */     
/*  84:    */ 
/*  85: 89 */     test.set("@where", where);
/*  86: 90 */     return test;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public XModel getCreateProcessTr(String pid, String stateMachineClassName)
/*  90:    */     throws Exception
/*  91:    */   {
/*  92: 94 */     XModel test = new XBaseModel();
/*  93: 95 */     test.set("@op", "createProcess");
/*  94:    */     
/*  95:    */ 
/*  96: 98 */     test.set("@pid", pid);
/*  97: 99 */     test.set("@stateMachineClassName", stateMachineClassName);
/*  98:100 */     return test;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public boolean authenticate(String user, String password, String sessionId, boolean md5, String[] creds)
/* 102:    */     throws MalformedURLException, IOException
/* 103:    */   {
/* 104:104 */     XModel xm = new XBaseModel();
/* 105:105 */     XModel test1 = new XBaseModel();
/* 106:106 */     test1.set("@op", "getData");
/* 107:107 */     test1.set("@table", "accounts a left join physician b on a.username=b.username");
/* 108:108 */     test1.set("@fields", "a.username,password,a.status,roles,b.id ,name");
/* 109:109 */     test1.set("@where", " a.username='" + user + "'");
/* 110:110 */     test1.setId("authenticate");
/* 111:111 */     xm.append(test1);
/* 112:112 */     XModel outM = execute(xm);
/* 113:    */     
/* 114:114 */     String result = outM.get(0).get("@result").toString();
/* 115:115 */     if (result.equals("success"))
/* 116:    */     {
/* 117:117 */       String pass1 = outM.get(0).get(0).get("password/@value").toString();
/* 118:118 */       System.out.println("Pass1 " + pass1);
/* 119:119 */       if (md5) {
/* 120:121 */         pass1 = MD5Utils.hex_md5(pass1 + sessionId);
/* 121:    */       }
/* 122:123 */       System.out.println("Pass1 " + pass1 + " Password " + password);
/* 123:124 */       if (pass1.equals(password))
/* 124:    */       {
/* 125:126 */         System.out.println("OK");
/* 126:    */         try
/* 127:    */         {
/* 128:128 */           creds[0] = user;
/* 129:129 */           creds[1] = outM.get(0).get(0).get("roles/@value").toString();
/* 130:130 */           creds[2] = ((String)outM.get(0).get(0).get("id/@value"));
/* 131:131 */           creds[3] = ((String)outM.get(0).get(0).get("name/@value"));
/* 132:    */         }
/* 133:    */         catch (Exception e)
/* 134:    */         {
/* 135:135 */           e.printStackTrace();
/* 136:    */         }
/* 137:137 */         return true;
/* 138:    */       }
/* 139:    */     }
/* 140:143 */     return false;
/* 141:    */   }
/* 142:    */   
/* 143:    */   public void join(XModel src, XModel dest)
/* 144:    */   {
/* 145:149 */     for (int i = 0; i < dest.getNumChildren(); i++) {
/* 146:151 */       src.append(dest.get(i));
/* 147:    */     }
/* 148:    */   }
/* 149:    */   
/* 150:    */   public XModel execute1(XModel xm, int bsize)
/* 151:    */     throws MalformedURLException, IOException
/* 152:    */   {
/* 153:159 */     XModel out1 = new XBaseModel();
/* 154:160 */     int bsize1 = Math.min(bsize, xm.getNumChildren());
/* 155:161 */     int i = 0;
/* 156:162 */     while (i < xm.getNumChildren())
/* 157:    */     {
/* 158:164 */       bsize1 = Math.min(bsize, xm.getNumChildren() - i);
/* 159:165 */       XModel xm1 = new XBaseModel();
/* 160:166 */       for (int j = 0; j < bsize1; j++) {
/* 161:168 */         xm1.append(xm.get(i + j));
/* 162:    */       }
/* 163:171 */       XModel out2 = execute(xm1);
/* 164:172 */       join(out1, out2);
/* 165:    */       
/* 166:    */ 
/* 167:    */ 
/* 168:176 */       i += bsize1;
/* 169:    */     }
/* 170:178 */     return out1;
/* 171:    */   }
/* 172:    */   
/* 173:    */   public XModel execute(XModel xm)
/* 174:    */     throws MalformedURLException, IOException
/* 175:    */   {
/* 176:183 */     StringWriter sw = new StringWriter();
/* 177:    */     
/* 178:    */ 
/* 179:    */ 
/* 180:    */ 
/* 181:    */ 
/* 182:    */ 
/* 183:190 */     XDataSource.outputModel(sw, xm);
/* 184:191 */     HttpURLConnection con = (HttpURLConnection)new URL(this.SERVER_URL + "integration/TransService").openConnection();
/* 185:192 */     con.setDoInput(true);
/* 186:    */     
/* 187:194 */     con.setRequestMethod("POST");
/* 188:195 */     con.setDoOutput(true);
/* 189:196 */     con.connect();
/* 190:    */     
/* 191:198 */     OutputStream out1 = con.getOutputStream();
/* 192:    */     
/* 193:200 */     System.out.println(sw);
/* 194:    */     
/* 195:202 */     byte[] b1 = sw.toString().getBytes();
/* 196:    */     
/* 197:    */ 
/* 198:    */ 
/* 199:206 */     out1.write(b1);
/* 200:    */     
/* 201:    */ 
/* 202:209 */     out1.close();
/* 203:    */     
/* 204:211 */     con.getInputStream();
/* 205:212 */     byte[] b = new byte[2048];
/* 206:213 */     int count = con.getInputStream().read(b);
/* 207:214 */     StringWriter sw1 = new StringWriter();
/* 208:215 */     while (count > 0)
/* 209:    */     {
/* 210:217 */       sw1.write(new String(b, 0, count));
/* 211:    */       
/* 212:219 */       count = con.getInputStream().read(b);
/* 213:    */     }
/* 214:221 */     System.out.println(" --" + sw1.toString());
/* 215:222 */     return parse(sw1.toString());
/* 216:    */   }
/* 217:    */   
/* 218:    */   public static void main(String[] args)
/* 219:    */     throws Exception
/* 220:    */   {
/* 221:232 */     System.out.println(MD5Utils.hex_md5("5f4dcc3b5aa765d61d8327deb882cf9982D555A1FFC50101C316BCD46A2F37D3  "));
/* 222:233 */     TransactionClient cl = new TransactionClient();
/* 223:    */     
/* 224:    */ 
/* 225:236 */     XModel xm = new XBaseModel();
/* 226:237 */     XModel test1 = cl.saveKeyvalueTr("keyvalue", "/test/tt", " Test data");
/* 227:238 */     test1.setId("testsave1");
/* 228:239 */     xm.append(test1);
/* 229:240 */     XModel test = (XModel)xm.get("1");
/* 230:241 */     test.set("@op", "saveKeyvalue");
/* 231:242 */     test.set("@table", "keyvalue");
/* 232:243 */     test.set("@key", "/test/tt");
/* 233:244 */     test.set("TEst data");
/* 234:245 */     XModel test2 = new XBaseModel();
/* 235:246 */     String table = "physician  ";
/* 236:247 */     String fields = "*";
/* 237:248 */     String where = " true limit 1 ";
/* 238:249 */     test2 = cl.getDataTr(table, fields, where);
/* 239:250 */     test2.setId("testget1");
/* 240:251 */     xm.append(test2);
/* 241:    */     
/* 242:    */ 
/* 243:254 */     cl.execute(xm);
/* 244:    */   }
/* 245:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-services2\ken-services2.jar
 * Qualified Name:     com.kentropy.services.TransactionClient
 * JD-Core Version:    0.7.0.1
 */
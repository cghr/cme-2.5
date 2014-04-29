/*   1:    */ package com.kentropy.services;
/*   2:    */ 
/*   3:    */ import com.kentropy.db.TestXUIDB;
/*   4:    */ import com.kentropy.process.Process;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.PrintStream;
/*   7:    */ import java.io.PrintWriter;
/*   8:    */ import java.io.StringReader;
/*   9:    */ import java.io.StringWriter;
/*  10:    */ import javax.servlet.ServletException;
/*  11:    */ import javax.servlet.ServletInputStream;
/*  12:    */ import javax.servlet.http.HttpServlet;
/*  13:    */ import javax.servlet.http.HttpServletRequest;
/*  14:    */ import javax.servlet.http.HttpServletResponse;
/*  15:    */ import net.xoetrope.data.XDataSource;
/*  16:    */ import net.xoetrope.xml.XmlElement;
/*  17:    */ import net.xoetrope.xml.XmlSource;
/*  18:    */ import net.xoetrope.xui.data.XBaseModel;
/*  19:    */ import net.xoetrope.xui.data.XModel;
/*  20:    */ 
/*  21:    */ public class TransactionService
/*  22:    */   extends HttpServlet
/*  23:    */ {
/*  24:    */   public String getClientIpAddr(HttpServletRequest request)
/*  25:    */   {
/*  26: 30 */     String ip = request.getHeader("X-Forwarded-For");
/*  27: 31 */     if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
/*  28: 32 */       ip = request.getHeader("Proxy-Client-IP");
/*  29:    */     }
/*  30: 34 */     if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
/*  31: 35 */       ip = request.getHeader("WL-Proxy-Client-IP");
/*  32:    */     }
/*  33: 37 */     if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
/*  34: 38 */       ip = request.getHeader("HTTP_CLIENT_IP");
/*  35:    */     }
/*  36: 40 */     if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
/*  37: 41 */       ip = request.getHeader("HTTP_X_FORWARDED_FOR");
/*  38:    */     }
/*  39: 43 */     if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
/*  40: 44 */       ip = request.getRemoteAddr();
/*  41:    */     }
/*  42: 46 */     return ip;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public boolean checkAllowedHost(String host)
/*  46:    */   {
/*  47: 52 */     String[] allowedHosts = TestXUIDB.getInstance().getProperty("allowed_hosts").split(",");
/*  48: 53 */     for (int i = 0; i < allowedHosts.length; i++) {
/*  49: 55 */       if (allowedHosts[i].equals(host)) {
/*  50: 56 */         return true;
/*  51:    */       }
/*  52:    */     }
/*  53: 59 */     return false;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public XModel parse(String xmlStr)
/*  57:    */   {
/*  58: 68 */     StringReader sr = new StringReader(xmlStr);
/*  59: 69 */     System.out.println(xmlStr);
/*  60: 70 */     XmlElement xe = XmlSource.read(sr);
/*  61: 71 */     System.out.println(xe);
/*  62: 72 */     XDataSource xd = new XDataSource();
/*  63: 73 */     XModel xm = new XBaseModel();
/*  64: 74 */     xd.loadTable(xe, xm);
/*  65: 75 */     return xm;
/*  66:    */   }
/*  67:    */   
/*  68:    */   protected void service(HttpServletRequest request, HttpServletResponse response)
/*  69:    */     throws ServletException, IOException, SecurityException
/*  70:    */   {
/*  71: 83 */     new IntegrationAuth().authenticate(request);
/*  72:    */     
/*  73: 85 */     byte[] b = new byte[10480];
/*  74: 86 */     int count = request.getInputStream().read(b);
/*  75: 87 */     System.out.println(" Count=" + count);
/*  76: 88 */     if (count > 0)
/*  77:    */     {
/*  78: 90 */       String s = new String(b, 0, count);
/*  79: 91 */       System.out.println(" S=" + s);
/*  80: 92 */       response.setContentType("text/xml");
/*  81: 93 */       response.getWriter().println("<?xml version='1.0'?>");
/*  82: 94 */       response.getWriter().println(runTransaction(s));
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   public String runTransaction(String transaction)
/*  87:    */   {
/*  88:104 */     XModel xm = parse(transaction);
/*  89:105 */     XModel outM = new XBaseModel();
/*  90:109 */     for (int i = 0; i < xm.getNumChildren(); i++)
/*  91:    */     {
/*  92:111 */       System.out.println(xm.get(i).getId());
/*  93:112 */       String op = xm.get(i).get("@op").toString();
/*  94:113 */       System.out.println(op);
/*  95:114 */       if (op.equals("saveData")) {
/*  96:    */         try
/*  97:    */         {
/*  98:117 */           saveData(xm.get(i));
/*  99:118 */           ((XModel)outM.get(xm.get(i).getId())).set("@result", "success");
/* 100:    */         }
/* 101:    */         catch (Exception e)
/* 102:    */         {
/* 103:121 */           ((XModel)outM.get(xm.get(i).getId())).set("@result", "failure");
/* 104:122 */           e.printStackTrace();
/* 105:123 */           ((XModel)outM.get(xm.get(i).getId())).set("@exception", e);
/* 106:    */         }
/* 107:    */       }
/* 108:127 */       if (op.equals("saveKeyvalue")) {
/* 109:    */         try
/* 110:    */         {
/* 111:130 */           saveKeyvalue(xm.get(i));
/* 112:131 */           ((XModel)outM.get(xm.get(i).getId())).set("@result", "success");
/* 113:    */         }
/* 114:    */         catch (Exception e)
/* 115:    */         {
/* 116:134 */           e.printStackTrace();
/* 117:135 */           ((XModel)outM.get(xm.get(i).getId())).set("@result", "failure");
/* 118:136 */           ((XModel)outM.get(xm.get(i).getId())).set("@exception", e);
/* 119:    */         }
/* 120:    */       }
/* 121:139 */       if (op.equals("getData")) {
/* 122:    */         try
/* 123:    */         {
/* 124:142 */           getData(xm.get(i), (XModel)outM.get(xm.get(i).getId()));
/* 125:143 */           ((XModel)outM.get(xm.get(i).getId())).set("@result", "success");
/* 126:    */         }
/* 127:    */         catch (Exception e)
/* 128:    */         {
/* 129:146 */           e.printStackTrace();
/* 130:147 */           ((XModel)outM.get(xm.get(i).getId())).set("@result", "failure");
/* 131:148 */           ((XModel)outM.get(xm.get(i).getId())).set("@exception", e);
/* 132:    */         }
/* 133:    */       }
/* 134:152 */       if (op.equals("getKeyvalues")) {
/* 135:    */         try
/* 136:    */         {
/* 137:155 */           getKeyvalues(xm.get(i), (XModel)outM.get(xm.get(i).getId()));
/* 138:156 */           ((XModel)outM.get(xm.get(i).getId())).set("@result", "success");
/* 139:    */         }
/* 140:    */         catch (Exception e)
/* 141:    */         {
/* 142:159 */           e.printStackTrace();
/* 143:160 */           ((XModel)outM.get(xm.get(i).getId())).set("@result", "failure");
/* 144:161 */           ((XModel)outM.get(xm.get(i).getId())).set("@exception", e);
/* 145:    */         }
/* 146:    */       }
/* 147:164 */       if (op.equals("runTasks")) {
/* 148:    */         try
/* 149:    */         {
/* 150:168 */           getKeyvalues(xm.get(i), (XModel)outM.get(xm.get(i).getId()));
/* 151:169 */           ((XModel)outM.get(xm.get(i).getId())).set("@result", "success");
/* 152:    */         }
/* 153:    */         catch (Exception e)
/* 154:    */         {
/* 155:172 */           e.printStackTrace();
/* 156:173 */           ((XModel)outM.get(xm.get(i).getId())).set("@result", "failure");
/* 157:174 */           ((XModel)outM.get(xm.get(i).getId())).set("@exception", e);
/* 158:    */         }
/* 159:    */       }
/* 160:177 */       if (op.equals("refreshProcesses")) {
/* 161:    */         try
/* 162:    */         {
/* 163:181 */           refreshProcesses(xm.get(i), (XModel)outM.get(xm.get(i).getId()));
/* 164:182 */           ((XModel)outM.get(xm.get(i).getId())).set("@result", "success");
/* 165:    */         }
/* 166:    */         catch (Exception e)
/* 167:    */         {
/* 168:185 */           e.printStackTrace();
/* 169:186 */           ((XModel)outM.get(xm.get(i).getId())).set("@result", "failure");
/* 170:187 */           ((XModel)outM.get(xm.get(i).getId())).set("@exception", e);
/* 171:    */         }
/* 172:    */       }
/* 173:190 */       if (op.equals("createProcess")) {
/* 174:    */         try
/* 175:    */         {
/* 176:194 */           createProcess(xm.get(i), (XModel)outM.get(xm.get(i).getId()));
/* 177:195 */           ((XModel)outM.get(xm.get(i).getId())).set("@result", "success");
/* 178:    */         }
/* 179:    */         catch (Exception e)
/* 180:    */         {
/* 181:198 */           e.printStackTrace();
/* 182:199 */           ((XModel)outM.get(xm.get(i).getId())).set("@result", "failure");
/* 183:200 */           ((XModel)outM.get(xm.get(i).getId())).set("@exception", e);
/* 184:    */         }
/* 185:    */       }
/* 186:    */     }
/* 187:204 */     StringWriter sw = new StringWriter();
/* 188:    */     
/* 189:206 */     XDataSource.outputModel(sw, outM);
/* 190:207 */     return sw.toString();
/* 191:    */   }
/* 192:    */   
/* 193:    */   private void createProcess(XModel xm, XModel outM)
/* 194:    */     throws Exception
/* 195:    */   {
/* 196:211 */     String pid = xm.get("@pid").toString();
/* 197:212 */     String stateMachineClassName = xm.get("@stateMachineClassName").toString();
/* 198:213 */     Process.createProcess(pid, stateMachineClassName);
/* 199:    */   }
/* 200:    */   
/* 201:    */   private void refreshProcesses(XModel xm, XModel outM)
/* 202:    */     throws Exception
/* 203:    */   {
/* 204:219 */     String where = xm.get("@where").toString();
/* 205:220 */     Process.refreshProcessStatus(where);
/* 206:    */   }
/* 207:    */   
/* 208:    */   private void saveKeyvalue(XModel xm)
/* 209:    */     throws Exception
/* 210:    */   {
/* 211:227 */     String table = xm.get("@table").toString();
/* 212:228 */     String value = xm.get().toString();
/* 213:229 */     String key = xm.get("@key").toString();
/* 214:230 */     TestXUIDB.getInstance().saveKeyValue(table, key, value);
/* 215:    */   }
/* 216:    */   
/* 217:    */   public void saveData(XModel xm)
/* 218:    */     throws Exception
/* 219:    */   {
/* 220:237 */     String table = xm.get("@table").toString();
/* 221:238 */     String where = xm.get("@where").toString();
/* 222:239 */     String changelog = xm.get("@changelog").toString();
/* 223:240 */     if (changelog.equals("true")) {
/* 224:242 */       TestXUIDB.getInstance().saveDataM1(table, where, xm);
/* 225:    */     } else {
/* 226:247 */       TestXUIDB.getInstance().saveDataM2(table, where, xm);
/* 227:    */     }
/* 228:    */   }
/* 229:    */   
/* 230:    */   public void getData(XModel xm, XModel outM)
/* 231:    */     throws Exception
/* 232:    */   {
/* 233:256 */     String table = xm.get("@table").toString();
/* 234:257 */     String where = xm.get("@where").toString();
/* 235:258 */     String fields = xm.get("@fields").toString();
/* 236:    */     
/* 237:    */ 
/* 238:261 */     TestXUIDB.getInstance().getData(table, fields, where, outM);
/* 239:    */   }
/* 240:    */   
/* 241:    */   public void getKeyvalues(XModel xm, XModel outM)
/* 242:    */     throws Exception
/* 243:    */   {
/* 244:269 */     String table = xm.get("@table").toString();
/* 245:270 */     String where = xm.get("@where").toString();
/* 246:271 */     String parentPath = xm.get("@parentpath").toString();
/* 247:    */     
/* 248:    */ 
/* 249:274 */     TestXUIDB.getInstance().getKeyValues(outM, table, parentPath);
/* 250:    */   }
/* 251:    */   
/* 252:    */   public static void main(String[] args)
/* 253:    */   {
/* 254:281 */     String trans = "<?xml version='1.0'?>\r\n<data>\r\n<data id='1' op='saveData' where='' table='' changelog='true'>\r\n\t\t<data id='' value=''/>\t\r\n</data>\r\n\t\t<data  id='2' op='saveDataM2' where='' table=''>\r\n\t\t<data id='' value=''/>\r\n\t\t</data>\r\n\t\t<data  id='3' op='saveKeyvalue' table='keyvalue'>\r\n\t\t<data id='key' value='/test/tt'/>\r\n\t\t<data id='value' value='test135'/>\r\n\t\t</data>\r\n\t\t</data>";
/* 255:    */     
/* 256:    */ 
/* 257:    */ 
/* 258:    */ 
/* 259:    */ 
/* 260:    */ 
/* 261:    */ 
/* 262:    */ 
/* 263:    */ 
/* 264:    */ 
/* 265:    */ 
/* 266:293 */     new TransactionService().runTransaction(trans);
/* 267:    */   }
/* 268:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-services2\ken-services2.jar
 * Qualified Name:     com.kentropy.services.TransactionService
 * JD-Core Version:    0.7.0.1
 */
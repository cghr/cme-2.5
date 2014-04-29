/*    1:     */ package com.kentropy.services;
/*    2:     */ 
/*    3:     */ import de.schlund.pfixxml.util.MD5Utils;
/*    4:     */ import java.io.FileOutputStream;
/*    5:     */ import java.io.IOException;
/*    6:     */ import java.io.InputStream;
/*    7:     */ import java.io.OutputStream;
/*    8:     */ import java.io.PrintStream;
/*    9:     */ import java.io.StringWriter;
/*   10:     */ import java.net.HttpURLConnection;
/*   11:     */ import java.net.MalformedURLException;
/*   12:     */ import java.net.URL;
/*   13:     */ import java.util.Map;
/*   14:     */ import java.util.Set;
/*   15:     */ import java.util.Vector;
/*   16:     */ import net.xoetrope.data.XDataSource;
/*   17:     */ import net.xoetrope.xui.data.XBaseModel;
/*   18:     */ import net.xoetrope.xui.data.XModel;
/*   19:     */ 
/*   20:     */ public class IntegrationService
/*   21:     */ {
/*   22:     */   public String authenticate(String username, String password, String appURL)
/*   23:     */     throws Exception
/*   24:     */   {
/*   25:  38 */     System.out.println("Before  Client Authentication");
/*   26:  39 */     URL url1 = new URL(appURL + "?action=getSession");
/*   27:  40 */     System.out.println("appURL" + url1);
/*   28:  41 */     HttpURLConnection urlConnection = (HttpURLConnection)url1.openConnection();
/*   29:  42 */     Map map = urlConnection.getHeaderFields();
/*   30:  43 */     Set<String> set = map.keySet();
/*   31:  44 */     for (String key : set) {
/*   32:  45 */       System.out.println("Key:" + key + " Value" + urlConnection.getHeaderField(key));
/*   33:     */     }
/*   34:  48 */     String session = urlConnection.getHeaderField("Set-Cookie");
/*   35:     */     
/*   36:     */ 
/*   37:     */ 
/*   38:     */ 
/*   39:     */ 
/*   40:  54 */     System.out.println("SessionID:" + session);
/*   41:     */     
/*   42:  56 */     String sessionId = session.split(";")[0].split("=")[1].trim();
/*   43:  57 */     System.out.println("SessionID" + sessionId);
/*   44:  58 */     String secret = null;
/*   45:     */     
/*   46:     */ 
/*   47:  61 */     String secretPass = MD5Utils.hex_md5(password);
/*   48:  62 */     System.out.println("SecretPass" + secretPass);
/*   49:  63 */     String md5Secret = MD5Utils.hex_md5(secretPass + sessionId);
/*   50:  64 */     secret = md5Secret;
/*   51:     */     
/*   52:     */ 
/*   53:  67 */     System.out.println("secret " + secret);
/*   54:     */     
/*   55:  69 */     url1 = new URL(appURL);
/*   56:  70 */     System.out.println("appURL" + url1);
/*   57:  71 */     urlConnection = (HttpURLConnection)url1.openConnection();
/*   58:  72 */     urlConnection.setRequestProperty("Cookie", session + ";");
/*   59:     */     
/*   60:  74 */     urlConnection.setRequestMethod("POST");
/*   61:  75 */     urlConnection.setDoOutput(true);
/*   62:  76 */     urlConnection.getOutputStream().write(("username=" + username + "&password=" + secret).getBytes());
/*   63:  77 */     urlConnection.getOutputStream().close();
/*   64:  78 */     urlConnection.getHeaderFields();
/*   65:     */     
/*   66:     */ 
/*   67:  81 */     System.out.println("Response:" + urlConnection.getResponseCode());
/*   68:  82 */     if (urlConnection.getResponseCode() != 200) {
/*   69:  83 */       throw new Exception("Not authenticated");
/*   70:     */     }
/*   71:  85 */     return session;
/*   72:     */   }
/*   73:     */   
/*   74:     */   public void downloadFile(String url, String username, String password, String appURL, String file)
/*   75:     */     throws Exception
/*   76:     */   {
/*   77:  97 */     String session = authenticate(username, password, appURL);
/*   78:  98 */     URL url1 = new URL(url);
/*   79:  99 */     System.out.println("URL" + url1);
/*   80: 100 */     HttpURLConnection urlConnection = (HttpURLConnection)url1.openConnection();
/*   81: 101 */     urlConnection.setRequestProperty("Cookie", session + ";");
/*   82: 102 */     InputStream in = urlConnection.getInputStream();
/*   83: 103 */     byte[] b = new byte[2048];
/*   84: 104 */     FileOutputStream fout = new FileOutputStream(file);
/*   85: 105 */     int count = in.read(b);
/*   86: 106 */     while (count > 0)
/*   87:     */     {
/*   88: 108 */       fout.write(b, 0, count);
/*   89: 109 */       count = in.read(b);
/*   90: 110 */       System.out.println(count);
/*   91:     */     }
/*   92: 113 */     fout.close();
/*   93: 114 */     in.close();
/*   94:     */   }
/*   95:     */   
/*   96:     */   public XModel createUser(String source, String username, String md5Pass, String role)
/*   97:     */     throws MalformedURLException, IOException
/*   98:     */   {
/*   99: 122 */     TransactionClient cl = new TransactionClient();
/*  100: 123 */     cl.SERVER_URL = source;
/*  101: 124 */     String table = "accounts";
/*  102:     */     
/*  103:     */ 
/*  104: 127 */     XModel xm = new XBaseModel();
/*  105: 128 */     xm.set("@source", source);
/*  106: 129 */     xm.set("@table", table);
/*  107:     */     
/*  108:     */ 
/*  109:     */ 
/*  110:     */ 
/*  111:     */ 
/*  112: 135 */     XModel trans = new XBaseModel();
/*  113: 136 */     ((XModel)trans.get("username")).set(username);
/*  114: 137 */     ((XModel)trans.get("password")).set(md5Pass);
/*  115: 138 */     ((XModel)trans.get("roles")).set(role);
/*  116: 139 */     ((XModel)trans.get("status")).set("active");
/*  117: 140 */     trans.setId("saveUser");
/*  118:     */     
/*  119: 142 */     cl.saveDataTr(table, "username='" + username + "'", trans, false);
/*  120: 143 */     xm.append(trans);
/*  121: 144 */     XModel out = cl.execute(xm);
/*  122: 145 */     return out;
/*  123:     */   }
/*  124:     */   
/*  125:     */   public String join(Vector<String> v)
/*  126:     */   {
/*  127: 151 */     String s1 = "";
/*  128: 152 */     for (String s : v) {
/*  129: 154 */       s1 = s1 + "'" + s + "',";
/*  130:     */     }
/*  131: 156 */     if (!s1.equals("")) {
/*  132: 157 */       s1 = s1.substring(0, s1.length() - 1);
/*  133:     */     }
/*  134: 159 */     return s1;
/*  135:     */   }
/*  136:     */   
/*  137:     */   public String getTimeConstraint(String lastSyncTime, String curSyncTime, String timeField)
/*  138:     */   {
/*  139: 164 */     return timeField + ">= '" + lastSyncTime + "' and " + timeField + " < '" + curSyncTime + "'";
/*  140:     */   }
/*  141:     */   
/*  142:     */   public XModel readTable(String source, String table, String fields, String where)
/*  143:     */     throws Exception
/*  144:     */   {
/*  145: 170 */     TransactionClient cl = new TransactionClient();
/*  146: 171 */     cl.SERVER_URL = source;
/*  147:     */     
/*  148:     */ 
/*  149: 174 */     XModel xm = new XBaseModel();
/*  150: 175 */     xm.set("@source", source);
/*  151: 176 */     xm.set("@table", table);
/*  152:     */     
/*  153: 178 */     xm.append(cl.getDataTr(table, fields, where));
/*  154:     */     
/*  155: 180 */     XModel out = cl.execute(xm);
/*  156: 181 */     return out;
/*  157:     */   }
/*  158:     */   
/*  159:     */   public XModel readProcesses(String source, Vector processes)
/*  160:     */     throws Exception
/*  161:     */   {
/*  162: 188 */     TransactionClient cl = new TransactionClient();
/*  163: 189 */     cl.SERVER_URL = source;
/*  164:     */     
/*  165:     */ 
/*  166: 192 */     XModel xm = new XBaseModel();
/*  167: 193 */     xm.set("@source", source);
/*  168: 194 */     xm.set("@table", "process");
/*  169: 196 */     for (Object p : processes) {
/*  170: 198 */       xm.append(cl.getDataTr("process", "*", "pid='" + p + "'"));
/*  171:     */     }
/*  172: 201 */     XModel out = cl.execute1(xm, 20);
/*  173: 202 */     return out;
/*  174:     */   }
/*  175:     */   
/*  176:     */   public XModel readTable(String source, String table, String changesTable, String keyField1, String keyField2, String join, String timeField, String lastSyncTime, String curSyncTime, String constraint, XModel out1)
/*  177:     */     throws Exception
/*  178:     */   {
/*  179: 209 */     String where = timeField + ">= '" + lastSyncTime + "' and " + timeField + " < '" + curSyncTime + "' and " + keyField1 + " is not null";
/*  180: 210 */     System.out.println(source);
/*  181:     */     
/*  182: 212 */     TransactionClient cl = new TransactionClient();
/*  183: 213 */     cl.SERVER_URL = source;
/*  184:     */     
/*  185:     */ 
/*  186: 216 */     XModel xm = new XBaseModel();
/*  187: 217 */     xm.set("@source", source);
/*  188: 218 */     xm.set("@table", table);
/*  189:     */     
/*  190: 220 */     xm.append(cl.getDataTr(changesTable, "DISTINCT " + keyField1, where));
/*  191:     */     
/*  192: 222 */     XModel out = cl.execute(xm);
/*  193: 223 */     XModel xm1 = new XBaseModel();
/*  194: 225 */     for (int i = 0; i < out.getNumChildren(); i++) {
/*  195: 228 */       for (int j = 0; j < out.get(i).getNumChildren(); j++)
/*  196:     */       {
/*  197: 232 */         String where1 = join.replaceAll("<k1>", keyField2).replaceAll("<k2>", new StringBuilder().append(out.get(i).get(j).get(new StringBuilder(String.valueOf(keyField1)).append("/@value").toString())).toString()) + (constraint == null ? "" : new StringBuilder(" and ").append(constraint).toString());
/*  198:     */         
/*  199: 234 */         String key = "";
/*  200: 235 */         if (keyField2.indexOf("concat") >= 0) {
/*  201: 236 */           key = key + keyField2 + " ," + table + ".";
/*  202:     */         }
/*  203: 238 */         xm1.append(cl.getDataTr(table, key + "*", where1));
/*  204: 239 */         System.out.println(out.get(i).get(j).get(keyField1 + "/@value"));
/*  205:     */       }
/*  206:     */     }
/*  207: 244 */     out1 = cl.execute1(xm1, 20);
/*  208: 245 */     return out1;
/*  209:     */   }
/*  210:     */   
/*  211:     */   public XModel readTable2(String source, String table, String changesTable, String keyField1, String keyField2, String destKeyField, String join, String timeField, String lastSyncTime, String curSyncTime, String constraint, XModel out1)
/*  212:     */     throws Exception
/*  213:     */   {
/*  214: 253 */     String where = timeField + ">= '" + lastSyncTime + "' and " + timeField + " < '" + curSyncTime + "'";
/*  215: 254 */     System.out.println(source);
/*  216:     */     
/*  217: 256 */     TransactionClient cl = new TransactionClient();
/*  218: 257 */     cl.SERVER_URL = source;
/*  219:     */     
/*  220:     */ 
/*  221: 260 */     XModel xm = new XBaseModel();
/*  222: 261 */     xm.set("@source", source);
/*  223: 262 */     xm.set("@table", table);
/*  224:     */     
/*  225: 264 */     xm.append(cl.getDataTr(changesTable, "DISTINCT " + keyField1, where));
/*  226:     */     
/*  227: 266 */     XModel out = cl.execute(xm);
/*  228: 267 */     XModel xm1 = new XBaseModel();
/*  229: 269 */     for (int i = 0; i < out.getNumChildren(); i++) {
/*  230: 272 */       for (int j = 0; j < out.get(i).getNumChildren(); j++)
/*  231:     */       {
/*  232: 275 */         String where1 = join.replaceAll("<k1>", keyField2).replaceAll("<k2>", new StringBuilder().append(out.get(i).get(j).get(new StringBuilder(String.valueOf(keyField1)).append("/@value").toString())).toString()) + (constraint == null ? "" : new StringBuilder(" and ").append(constraint).toString());
/*  233:     */         
/*  234: 277 */         String key = "";
/*  235: 278 */         if (keyField2.indexOf("concat") >= 0) {
/*  236: 279 */           key = key + keyField2 + " " + destKeyField + "," + table + ".";
/*  237:     */         }
/*  238: 280 */         xm1.append(cl.getDataTr(table, key + "*", where1));
/*  239: 281 */         System.out.println(out.get(i).get(j).get(keyField1 + "/@value"));
/*  240:     */       }
/*  241:     */     }
/*  242: 286 */     out1 = cl.execute1(xm1, 20);
/*  243: 287 */     return out1;
/*  244:     */   }
/*  245:     */   
/*  246:     */   public XModel trim(XModel xm)
/*  247:     */   {
/*  248: 294 */     XModel xm1 = new XBaseModel();
/*  249: 296 */     for (int i = 0; i < xm.getNumChildren(); i++) {
/*  250: 299 */       if (xm.get(i).get() != null) {
/*  251: 300 */         xm1.append(xm.get(i));
/*  252:     */       }
/*  253:     */     }
/*  254: 301 */     return xm1;
/*  255:     */   }
/*  256:     */   
/*  257:     */   public void refreshProcesses(String destination, String matchField, XModel xm, String stateMachineClassName)
/*  258:     */     throws Exception
/*  259:     */   {
/*  260: 308 */     TransactionClient cl = new TransactionClient();
/*  261: 309 */     XModel xmWrite = new XBaseModel();
/*  262: 310 */     cl.SERVER_URL = destination;
/*  263: 311 */     for (int i = 0; i < xm.getNumChildren(); i++) {
/*  264: 313 */       if (xm.get(i).getNumChildren() != 0) {
/*  265: 316 */         for (int j = 0; j < xm.get(i).getNumChildren(); j++)
/*  266:     */         {
/*  267: 318 */           XModel xm1 = trim(xm.get(i).get(j));
/*  268: 319 */           xm1.setId((i + 1) * (j + 1) - 1);
/*  269: 320 */           String pid = (String)xm1.get(matchField + "/@value");
/*  270: 321 */           if (pid != null)
/*  271:     */           {
/*  272: 323 */             xm1 = cl.getCreateProcessTr(pid, stateMachineClassName);
/*  273: 324 */             xmWrite.append(xm1);
/*  274: 325 */             if (xmWrite.getNumChildren() > 10)
/*  275:     */             {
/*  276: 327 */               StringWriter sw = new StringWriter();
/*  277: 328 */               XDataSource.outputModel(sw, xmWrite);
/*  278: 329 */               System.out.println(" Test >>>>>" + sw + " " + xmWrite.getNumChildren());
/*  279:     */               
/*  280: 331 */               cl.execute(xmWrite);
/*  281: 332 */               xmWrite = new XBaseModel();
/*  282:     */             }
/*  283:     */           }
/*  284:     */         }
/*  285:     */       }
/*  286:     */     }
/*  287: 337 */     StringWriter sw = new StringWriter();
/*  288: 338 */     XDataSource.outputModel(sw, xmWrite);
/*  289: 339 */     System.out.println(" Test >>>>>" + sw + " " + xmWrite.getNumChildren());
/*  290:     */     
/*  291: 341 */     cl.execute(xmWrite);
/*  292:     */   }
/*  293:     */   
/*  294:     */   public void write(String destination, String table, String matchField, XModel xm)
/*  295:     */     throws Exception
/*  296:     */   {
/*  297: 350 */     TransactionClient cl = new TransactionClient();
/*  298:     */     
/*  299: 352 */     cl.SERVER_URL = destination;
/*  300:     */     
/*  301: 354 */     System.out.println(xm.getId() + " " + xm.getNumChildren());
/*  302: 355 */     XModel xmWrite = new XBaseModel();
/*  303: 356 */     XModel xmlProcessWrite = new XBaseModel();
/*  304: 357 */     for (int i = 0; i < xm.getNumChildren(); i++)
/*  305:     */     {
/*  306: 359 */       System.out.println(xm.get(i).getId() + " " + xm.get(i).getNumChildren());
/*  307: 360 */       if (xm.get(i).getNumChildren() != 0) {
/*  308: 363 */         for (int j = 0; j < xm.get(i).getNumChildren(); j++)
/*  309:     */         {
/*  310: 365 */           XModel xm1 = trim(xm.get(i).get(j));
/*  311: 366 */           xm1.setId((i + 1) * (j + 1) - 1);
/*  312:     */           
/*  313: 368 */           cl.saveDataTr(table, matchField + "='" + xm1.get(new StringBuilder().append(matchField).append("/@value").toString()) + "'", xm1, false);
/*  314:     */           
/*  315: 370 */           xmWrite.append(xm1);
/*  316: 372 */           if (xm.get(i).get(j).get("@refreshprocess") != null)
/*  317:     */           {
/*  318: 374 */             System.out.println("refresh process is " + xm.get(i).get(j).get("@refreshprocess"));
/*  319:     */             
/*  320: 376 */             xm1 = new TransactionClient().getRefreshProcessesTr(" pid ='" + xm.get(i).get(j).get("@refreshprocess") + "'");
/*  321: 377 */             xmlProcessWrite.append(xm1);
/*  322:     */           }
/*  323: 380 */           if (xmWrite.getNumChildren() > 0)
/*  324:     */           {
/*  325: 382 */             StringWriter sw = new StringWriter();
/*  326: 383 */             XDataSource.outputModel(sw, xmWrite);
/*  327: 384 */             System.out.println(" Test >>>>>" + sw + " " + xmWrite.getNumChildren());
/*  328:     */             
/*  329: 386 */             cl.execute(xmWrite);
/*  330: 387 */             xmWrite = new XBaseModel();
/*  331:     */           }
/*  332:     */         }
/*  333:     */       }
/*  334:     */     }
/*  335: 392 */     StringWriter sw = new StringWriter();
/*  336: 393 */     XDataSource.outputModel(sw, xmWrite);
/*  337: 394 */     System.out.println(" Test >>>>>" + sw + " " + xmWrite.getNumChildren());
/*  338:     */     
/*  339: 396 */     cl.execute(xmWrite);
/*  340:     */     
/*  341: 398 */     StringWriter sw1 = new StringWriter();
/*  342: 399 */     XDataSource.outputModel(sw1, xmlProcessWrite);
/*  343: 400 */     System.out.println(" Test Process >>>>>" + sw1 + " " + xmlProcessWrite.getNumChildren());
/*  344:     */     
/*  345: 402 */     cl.execute(xmlProcessWrite);
/*  346:     */   }
/*  347:     */   
/*  348:     */   public void write1(String destination, String table, String[] keyFields, XModel xm)
/*  349:     */     throws Exception
/*  350:     */   {
/*  351: 408 */     TransactionClient cl = new TransactionClient();
/*  352:     */     
/*  353: 410 */     cl.SERVER_URL = destination;
/*  354:     */     
/*  355: 412 */     System.out.println(xm.getId() + " " + xm.getNumChildren());
/*  356: 413 */     XModel xmWrite = new XBaseModel();
/*  357: 414 */     XModel xmlProcessWrite = new XBaseModel();
/*  358: 415 */     for (int i = 0; i < xm.getNumChildren(); i++)
/*  359:     */     {
/*  360: 417 */       System.out.println(xm.get(i).getId() + " " + xm.get(i).getNumChildren());
/*  361: 418 */       if (xm.get(i).getNumChildren() != 0) {
/*  362: 421 */         for (int j = 0; j < xm.get(i).getNumChildren(); j++)
/*  363:     */         {
/*  364: 423 */           XModel xm1 = trim(xm.get(i).get(j));
/*  365: 424 */           xm1.setId((i + 1) * (j + 1) - 1);
/*  366: 425 */           String where = "";
/*  367: 426 */           for (int k = 0; k < keyFields.length; k++)
/*  368:     */           {
/*  369: 428 */             String term = keyFields[k] + "='" + xm1.get(new StringBuilder().append(keyFields[k]).append("/@value").toString()) + "'";
/*  370: 429 */             if (k == 0) {
/*  371: 430 */               where = where + term;
/*  372:     */             } else {
/*  373: 432 */               where = where + " AND " + term;
/*  374:     */             }
/*  375:     */           }
/*  376: 435 */           System.out.println(" WHere is " + where);
/*  377: 436 */           cl.saveDataTr(table, where, xm1, false);
/*  378:     */           
/*  379: 438 */           xmWrite.append(xm1);
/*  380: 440 */           if (xm.get(i).get(j).get("@refreshprocess") != null)
/*  381:     */           {
/*  382: 442 */             System.out.println("refresh process is " + xm.get(i).get(j).get("@refreshprocess"));
/*  383:     */             
/*  384: 444 */             xm1 = new TransactionClient().getRefreshProcessesTr(" pid ='" + xm.get(i).get(j).get("@refreshprocess") + "'");
/*  385: 445 */             xmlProcessWrite.append(xm1);
/*  386:     */           }
/*  387: 448 */           if (xmWrite.getNumChildren() > 0)
/*  388:     */           {
/*  389: 450 */             StringWriter sw = new StringWriter();
/*  390: 451 */             XDataSource.outputModel(sw, xmWrite);
/*  391: 452 */             System.out.println(" Test >>>>>" + sw + " " + xmWrite.getNumChildren());
/*  392:     */             
/*  393: 454 */             cl.execute(xmWrite);
/*  394: 455 */             xmWrite = new XBaseModel();
/*  395:     */           }
/*  396:     */         }
/*  397:     */       }
/*  398:     */     }
/*  399: 460 */     StringWriter sw = new StringWriter();
/*  400: 461 */     XDataSource.outputModel(sw, xmWrite);
/*  401: 462 */     System.out.println(" Test >>>>>" + sw + " " + xmWrite.getNumChildren());
/*  402:     */     
/*  403: 464 */     cl.execute(xmWrite);
/*  404:     */     
/*  405: 466 */     StringWriter sw1 = new StringWriter();
/*  406: 467 */     XDataSource.outputModel(sw1, xmlProcessWrite);
/*  407: 468 */     System.out.println(" Test Process >>>>>" + sw1 + " " + xmlProcessWrite.getNumChildren());
/*  408:     */     
/*  409: 470 */     cl.execute1(xmlProcessWrite, 20);
/*  410:     */   }
/*  411:     */   
/*  412:     */   public void transfer(String source, String destination, String table, String changesTable, String keyField1, String keyField2, String join, String matchField, String timeField, String lastSyncTime, String constraint, XModel out1)
/*  413:     */     throws Exception
/*  414:     */   {}
/*  415:     */   
/*  416:     */   public void setRefreshProcess(XModel xm, String pidFld)
/*  417:     */   {
/*  418: 531 */     for (int i = 0; i < xm.getNumChildren(); i++) {
/*  419: 534 */       for (int j = 0; j < xm.get(i).getNumChildren(); j++) {
/*  420: 536 */         xm.get(i).get(j).set("@refreshprocess", xm.get(i).get(j).get(pidFld + "/@value"));
/*  421:     */       }
/*  422:     */     }
/*  423:     */   }
/*  424:     */   
/*  425:     */   public Vector<String> getRefreshProcess(XModel xm, String pidFld)
/*  426:     */   {
/*  427: 543 */     Vector<String> v1 = new Vector();
/*  428: 544 */     for (int i = 0; i < xm.getNumChildren(); i++) {
/*  429: 547 */       for (int j = 0; j < xm.get(i).getNumChildren(); j++)
/*  430:     */       {
/*  431: 550 */         XModel valM = (XModel)xm.get(i).get(j).get(pidFld);
/*  432: 551 */         if (valM.get() != null) {
/*  433: 552 */           v1.add(valM.get().toString());
/*  434:     */         }
/*  435:     */       }
/*  436:     */     }
/*  437: 557 */     return v1;
/*  438:     */   }
/*  439:     */   
/*  440:     */   public XModel readTasks1(String serverURL, String status, String keyvalType, String lastSyncTime, String curSyncTime)
/*  441:     */     throws Exception
/*  442:     */   {
/*  443: 566 */     XModel xm = new XBaseModel();
/*  444:     */     
/*  445: 568 */     TransactionClient cl = new TransactionClient();
/*  446: 569 */     cl.SERVER_URL = serverURL;
/*  447: 570 */     XModel tr = cl.getDataTr("tasks", "Distinct member ", "dateassigned > '" + lastSyncTime + "' and " + status);
/*  448: 571 */     XModel trans = new XBaseModel();
/*  449: 572 */     xm.append(tr);
/*  450: 573 */     xm = cl.execute(xm);
/*  451: 574 */     trans = new XBaseModel();
/*  452: 575 */     XModel out = new XBaseModel();
/*  453: 576 */     for (int i = 0; i < xm.getNumChildren(); i++)
/*  454:     */     {
/*  455: 577 */       int count = 0;
/*  456: 578 */       for (int j = 0; j < xm.get(i).getNumChildren(); j++)
/*  457:     */       {
/*  458: 580 */         XModel taskM = xm.get(i).get(j);
/*  459: 581 */         String member = (String)((XModel)taskM.get("member")).get();
/*  460: 582 */         System.out.println(member);
/*  461: 583 */         XModel tr1 = cl.getDataTr("keyvalue", "*", "key1 like '/" + keyvalType + "/" + member + "%'");
/*  462:     */         
/*  463: 585 */         trans.append(tr1);
/*  464: 586 */         count++;
/*  465: 588 */         if (count == 10)
/*  466:     */         {
/*  467: 590 */           XModel out1 = cl.execute(trans);
/*  468: 591 */           trans = new XBaseModel();
/*  469: 592 */           copyData(out1, out);
/*  470: 593 */           count = 0;
/*  471:     */         }
/*  472:     */       }
/*  473:     */     }
/*  474: 598 */     XModel out1 = cl.execute(trans);
/*  475: 599 */     copyData(out1, out);
/*  476: 600 */     return out1;
/*  477:     */   }
/*  478:     */   
/*  479:     */   public void copyData(XModel out, XModel xm)
/*  480:     */   {
/*  481: 604 */     for (int i = 0; i < xm.getNumChildren(); i++)
/*  482:     */     {
/*  483: 605 */       int count = 0;
/*  484: 606 */       for (int j = 0; j < xm.get(i).getNumChildren(); j++)
/*  485:     */       {
/*  486: 608 */         XModel taskM = xm.get(i).get(j);
/*  487: 609 */         xm.append(taskM);
/*  488:     */       }
/*  489:     */     }
/*  490:     */   }
/*  491:     */   
/*  492:     */   public void readTime(String serverURL)
/*  493:     */     throws Exception
/*  494:     */   {
/*  495: 615 */     TransactionClient tc = new TransactionClient();
/*  496: 616 */     tc.SERVER_URL = serverURL;
/*  497: 617 */     XModel xm = new XBaseModel();
/*  498: 618 */     xm.append(tc.getDataTr("configuration", "NOW()", null));
/*  499: 619 */     XModel out = tc.execute(xm);
/*  500: 620 */     System.out.println(out.get(0).get(0).get());
/*  501:     */   }
/*  502:     */   
/*  503:     */   public void readNewPhy(String serverURL, String lastSyncTime, String curSyncTime, XModel container)
/*  504:     */     throws Exception
/*  505:     */   {
/*  506: 625 */     String datetable = "physician";
/*  507: 626 */     String dateFld = "created_time";
/*  508:     */     
/*  509:     */ 
/*  510:     */ 
/*  511:     */ 
/*  512: 631 */     XModel xm = new IntegrationService().readTable(serverURL, "physician", datetable, "id", "id", "<k1> =<k2>", dateFld, lastSyncTime, curSyncTime, null, new XBaseModel());
/*  513:     */     
/*  514:     */ 
/*  515: 634 */     xm.set("@matchfield", "id");
/*  516: 635 */     xm.setId("physician");
/*  517: 636 */     container.append(xm);
/*  518:     */   }
/*  519:     */   
/*  520:     */   public void readAccts(String serverURL, String lastSyncTime, String curSyncTime, XModel container)
/*  521:     */     throws Exception
/*  522:     */   {
/*  523: 640 */     String datetable = "accounts_changes";
/*  524: 641 */     String dateFld = "date1";
/*  525:     */     
/*  526:     */ 
/*  527:     */ 
/*  528:     */ 
/*  529: 646 */     XModel xm = new IntegrationService().readTable(serverURL, "accounts", datetable, "username", "username", "<k1> ='<k2>'", dateFld, lastSyncTime, curSyncTime, null, new XBaseModel());
/*  530:     */     
/*  531:     */ 
/*  532: 649 */     xm.set("@matchfield", "username");
/*  533: 650 */     xm.setId("accounts");
/*  534: 651 */     container.append(xm);
/*  535:     */   }
/*  536:     */   
/*  537:     */   public void readCancellations(String serverURL, String lastSyncTime, String curSyncTime, XModel container)
/*  538:     */     throws Exception
/*  539:     */   {
/*  540: 657 */     String datetable = "cancellation";
/*  541: 658 */     String dateFld = "time1";
/*  542:     */     
/*  543:     */ 
/*  544:     */ 
/*  545: 662 */     XModel xm = new IntegrationService().readTable(serverURL, "cancellation", "*", getTimeConstraint(lastSyncTime, curSyncTime, "time1"));
/*  546:     */     
/*  547:     */ 
/*  548:     */ 
/*  549: 666 */     xm.set("@matchfield", "physician1,uniqno");
/*  550: 667 */     xm.setId("cancellation");
/*  551: 668 */     container.append(xm);
/*  552:     */   }
/*  553:     */   
/*  554:     */   public void readUserlog(String serverURL, String lastSyncTime, String curSyncTime, XModel container)
/*  555:     */     throws Exception
/*  556:     */   {
/*  557: 674 */     String datetable = "userlog";
/*  558: 675 */     String dateFld = "login";
/*  559:     */     
/*  560:     */ 
/*  561:     */ 
/*  562: 679 */     XModel xm = new IntegrationService().readTable(serverURL, "userlog", "*", getTimeConstraint(lastSyncTime, curSyncTime, "login"));
/*  563:     */     
/*  564:     */ 
/*  565:     */ 
/*  566: 683 */     xm.set("@matchfield", "username,login");
/*  567: 684 */     xm.setId("userlog");
/*  568: 685 */     container.append(xm);
/*  569:     */   }
/*  570:     */   
/*  571:     */   public void readCMERecords(String serverURL, String lastSyncTime, String curSyncTime, XModel container)
/*  572:     */   {
/*  573:     */     try
/*  574:     */     {
/*  575: 692 */       XModel xm = new IntegrationService().readTable(serverURL, "cme_records", "process", "pid", "uniqno", "<k1>='<k2>'", "startTime", lastSyncTime, curSyncTime, null, new XBaseModel());
/*  576: 693 */       xm.set("@matchfield", "uniqno");
/*  577: 694 */       xm.setId("cme_records");
/*  578: 695 */       container.append(xm);
/*  579:     */     }
/*  580:     */     catch (Exception e)
/*  581:     */     {
/*  582: 698 */       e.printStackTrace();
/*  583:     */     }
/*  584:     */   }
/*  585:     */   
/*  586:     */   public void readReports(String serverURL, String lastSyncTime, String curSyncTime, XModel container)
/*  587:     */     throws Exception
/*  588:     */   {
/*  589: 707 */     XModel xm = new IntegrationService().readTable(serverURL, "report", "process", "pid", "uniqno", "<k1>='<k2>'", "startTime", lastSyncTime, curSyncTime, null, new XBaseModel());
/*  590: 708 */     xm.set("@matchfield", "uniqno");
/*  591: 709 */     xm.setId("report");
/*  592: 710 */     container.append(xm);
/*  593:     */   }
/*  594:     */   
/*  595:     */   public void readVAKeyValues(String serverURL, String lastSyncTime, String curSyncTime, XModel container)
/*  596:     */   {
/*  597: 718 */     String datetable = "tasks";
/*  598: 719 */     String dateFld = "starttime";
/*  599:     */     try
/*  600:     */     {
/*  601: 722 */       XModel xm = new IntegrationService().readTable(serverURL, "keyvalue", datetable, "member", "key1", "<k1> like '/va/<k2>%'", dateFld, lastSyncTime, curSyncTime, null, new XBaseModel());
/*  602:     */       
/*  603: 724 */       xm.set("@matchfield", "key1");
/*  604: 725 */       xm.setId("keyvalue");
/*  605: 726 */       container.append(xm);
/*  606:     */     }
/*  607:     */     catch (Exception e)
/*  608:     */     {
/*  609: 729 */       e.printStackTrace();
/*  610:     */     }
/*  611:     */   }
/*  612:     */   
/*  613:     */   public void readTasksAssigned(String serverURL, String lastSyncTime, String curSyncTime, XModel container)
/*  614:     */     throws Exception
/*  615:     */   {
/*  616: 736 */     String datetable = "tasks";
/*  617: 737 */     String dateFld = "starttime";
/*  618:     */     
/*  619:     */ 
/*  620:     */ 
/*  621: 741 */     XModel xm = new IntegrationService().readTable(serverURL, "tasks", "*", getTimeConstraint(lastSyncTime, curSyncTime, "starttime") + " and (status is null || status='0')");
/*  622: 742 */     xm.set("@matchfield", "id");
/*  623: 743 */     xm.setId("tasks");
/*  624:     */     
/*  625:     */ 
/*  626:     */ 
/*  627:     */ 
/*  628:     */ 
/*  629: 749 */     container.append(xm);
/*  630:     */   }
/*  631:     */   
/*  632:     */   public void readTaskAndDataAssigned(String serverURL, String lastSyncTime, String curSyncTime, XModel container)
/*  633:     */     throws Exception
/*  634:     */   {
/*  635: 755 */     Vector<String> processes = new Vector();
/*  636: 756 */     String datetable = "tasks";
/*  637: 757 */     String dateFld = "starttime";
/*  638: 758 */     readCMERecords(serverURL, lastSyncTime, curSyncTime, container);
/*  639:     */     
/*  640:     */ 
/*  641:     */ 
/*  642:     */ 
/*  643:     */ 
/*  644:     */ 
/*  645:     */ 
/*  646:     */ 
/*  647:     */ 
/*  648:     */ 
/*  649: 769 */     readReports(serverURL, lastSyncTime, curSyncTime, container);
/*  650:     */     
/*  651: 771 */     XModel xm = new IntegrationService().readTable(serverURL, "keyvalue", datetable, "member", "key1", "<k1> like '/va/<k2>%'", dateFld, lastSyncTime, curSyncTime, null, new XBaseModel());
/*  652:     */     
/*  653:     */ 
/*  654: 774 */     xm.set("@matchfield", "key1");
/*  655: 775 */     xm.setId("keyvalue");
/*  656: 776 */     container.append(xm);
/*  657:     */     
/*  658:     */ 
/*  659:     */ 
/*  660:     */ 
/*  661:     */ 
/*  662:     */ 
/*  663:     */ 
/*  664:     */ 
/*  665:     */ 
/*  666: 786 */     readTasksAssigned(serverURL, lastSyncTime, curSyncTime, container);
/*  667:     */     
/*  668:     */ 
/*  669:     */ 
/*  670:     */ 
/*  671: 791 */     container.append(xm);
/*  672: 792 */     xm = new IntegrationService().readTable(serverURL, "process", datetable, "member", "pid", "<k1>='<k2>'", dateFld, lastSyncTime, curSyncTime, null, new XBaseModel());
/*  673: 793 */     xm.set("@matchfield", "pid");
/*  674: 794 */     xm.setId("process");
/*  675: 795 */     container.append(xm);
/*  676:     */   }
/*  677:     */   
/*  678:     */   public void readBillable(String serverURL, String lastSyncTime, String curSyncTime, XModel container, Vector<String> processes)
/*  679:     */     throws Exception
/*  680:     */   {
/*  681: 808 */     String tb = "(select concat(report_id,'-',physician_id) bid, billables.* from  billables) bl";
/*  682: 809 */     String datetable = tb;
/*  683: 810 */     String dateFld = "date_of_billable";
/*  684:     */     
/*  685:     */ 
/*  686:     */ 
/*  687:     */ 
/*  688:     */ 
/*  689:     */ 
/*  690:     */ 
/*  691:     */ 
/*  692:     */ 
/*  693:     */ 
/*  694:     */ 
/*  695:     */ 
/*  696: 823 */     XModel xm = new IntegrationService().readTable(serverURL, "billables", "*", getTimeConstraint(lastSyncTime, curSyncTime, "date_of_billable"));
/*  697:     */     
/*  698:     */ 
/*  699:     */ 
/*  700:     */ 
/*  701:     */ 
/*  702:     */ 
/*  703:     */ 
/*  704:     */ 
/*  705:     */ 
/*  706:     */ 
/*  707:     */ 
/*  708:     */ 
/*  709:     */ 
/*  710: 837 */     xm.set("@matchfield", "report_id,physician_id");
/*  711: 838 */     xm.setId("billables");
/*  712: 839 */     container.append(xm);
/*  713:     */   }
/*  714:     */   
/*  715:     */   public void readCMEReport(String serverURL, String lastSyncTime, String curSyncTime, XModel container, Vector<String> processes)
/*  716:     */     throws Exception
/*  717:     */   {
/*  718: 845 */     String tb = "(select concat(uniqno,'-',physician) cid, cme-report.* from  cme_report) bl";
/*  719: 846 */     String datetable = "report";
/*  720: 847 */     String dateFld = "time1";
/*  721:     */     
/*  722:     */ 
/*  723:     */ 
/*  724: 851 */     XModel xm = new IntegrationService().readTable(serverURL, "cme_report", "*", getTimeConstraint(lastSyncTime, curSyncTime, "time1"));
/*  725:     */     
/*  726:     */ 
/*  727:     */ 
/*  728: 855 */     xm.set("@matchfield", "physician,uniqno");
/*  729: 856 */     xm.setId("cme_report");
/*  730: 857 */     container.append(xm);
/*  731: 858 */     xm = new IntegrationService().readTable(serverURL, "report", "*", getTimeConstraint(lastSyncTime, curSyncTime, "time1"));
/*  732: 859 */     xm.set("@matchfield", "uniqno");
/*  733: 860 */     xm.setId("report");
/*  734: 861 */     container.append(xm);
/*  735:     */   }
/*  736:     */   
/*  737:     */   public void readCancellation(String serverURL, String lastSyncTime, String curSyncTime, XModel container, Vector<String> processes)
/*  738:     */     throws Exception
/*  739:     */   {
/*  740: 871 */     String tb = "(select concat(uniqno,'-',physician) cid, cme-report.* from  cme_report) bl";
/*  741: 872 */     String datetable = tb;
/*  742: 873 */     String dateFld = "time1";
/*  743:     */     
/*  744:     */ 
/*  745:     */ 
/*  746: 877 */     XModel xm = new IntegrationService().readTable(serverURL, "cancellation", "*", getTimeConstraint(lastSyncTime, curSyncTime, "time1"));
/*  747:     */     
/*  748:     */ 
/*  749:     */ 
/*  750: 881 */     xm.set("@matchfield", "physician1,uniqno");
/*  751: 882 */     xm.setId("cancellation");
/*  752: 883 */     container.append(xm);
/*  753:     */   }
/*  754:     */   
/*  755:     */   public void readTaskAndDataCompleted(String serverURL, String lastSyncTime, String curSyncTime, XModel container, Vector<String> processes)
/*  756:     */     throws Exception
/*  757:     */   {
/*  758: 892 */     String datetable = "tasks";
/*  759: 893 */     String dateFld = "endtime";
/*  760:     */     
/*  761:     */ 
/*  762:     */ 
/*  763:     */ 
/*  764:     */ 
/*  765:     */ 
/*  766:     */ 
/*  767:     */ 
/*  768:     */ 
/*  769:     */ 
/*  770:     */ 
/*  771:     */ 
/*  772: 906 */     XModel xm = new IntegrationService().readTable(serverURL, "keyvalue", datetable, "member", "key1", "<k1> like '/cme/<k2>%'", dateFld, lastSyncTime, curSyncTime, null, new XBaseModel());
/*  773:     */     
/*  774:     */ 
/*  775: 909 */     xm.set("@matchfield", "key1");
/*  776: 910 */     xm.setId("keyvalue");
/*  777: 911 */     container.append(xm);
/*  778: 912 */     xm = new IntegrationService().readTable(serverURL, "tasks", datetable, "id", "id", "<k1>='<k2>' ", dateFld, lastSyncTime, curSyncTime, null, new XBaseModel());
/*  779: 913 */     xm.set("@matchfield", "id");
/*  780: 914 */     xm.setId("tasks");
/*  781: 915 */     setRefreshProcess(xm, "member");
/*  782: 916 */     container.append(xm);
/*  783: 917 */     XModel p1 = new XBaseModel();
/*  784: 918 */     processes.addAll(getRefreshProcess(xm, "member"));
/*  785:     */   }
/*  786:     */   
/*  787:     */   public void readProcessesCompleted(String serverURL, String lastSyncTime, String curSyncTime, XModel container, Vector<String> processes)
/*  788:     */     throws Exception
/*  789:     */   {
/*  790: 925 */     String datetable = "tasks";
/*  791: 926 */     String dateFld = "endtime";
/*  792: 927 */     XModel xm = new XBaseModel();
/*  793:     */     
/*  794: 929 */     xm = new IntegrationService().readTable(serverURL, "process", datetable, "member", "pid", "<k1>='<k2>'", dateFld, lastSyncTime, curSyncTime, null, new XBaseModel());
/*  795:     */     
/*  796:     */ 
/*  797:     */ 
/*  798:     */ 
/*  799:     */ 
/*  800: 935 */     processes.addAll(getRefreshProcess(xm, "pid"));
/*  801:     */   }
/*  802:     */   
/*  803:     */   public void readProcessesNew(String serverURL, String lastSyncTime, String curSyncTime, XModel container, Vector<String> processes)
/*  804:     */     throws Exception
/*  805:     */   {
/*  806: 942 */     String datetable = "process";
/*  807: 943 */     String dateFld = "starttime";
/*  808: 944 */     XModel xm = new XBaseModel();
/*  809:     */     
/*  810: 946 */     xm = new IntegrationService().readTable(serverURL, "process", datetable, "pid", "pid", "<k1>='<k2>'", dateFld, lastSyncTime, curSyncTime, null, new XBaseModel());
/*  811:     */     
/*  812:     */ 
/*  813:     */ 
/*  814:     */ 
/*  815:     */ 
/*  816: 952 */     processes.addAll(getRefreshProcess(xm, "pid"));
/*  817:     */   }
/*  818:     */   
/*  819:     */   public void readCMEReport1(String serverURL, String lastSyncTime, String curSyncTime, XModel container)
/*  820:     */     throws Exception
/*  821:     */   {}
/*  822:     */   
/*  823:     */   public void readData(String serverURL, String lastSyncTime, String curSyncTime, XModel container)
/*  824:     */     throws Exception
/*  825:     */   {
/*  826: 984 */     String datetable = "cme_report";
/*  827: 985 */     String dateFld = "time1";
/*  828:     */     
/*  829: 987 */     XModel xm = new IntegrationService().readTable(serverURL, "cme_report", datetable, "crid", "crid", "<k1> = '<k2>'", dateFld, lastSyncTime, curSyncTime, null, new XBaseModel());
/*  830: 988 */     xm.set("@matchfield", "crid");
/*  831: 989 */     xm.setId("cme_report");
/*  832: 990 */     container.append(xm);
/*  833:     */     
/*  834: 992 */     datetable = "report";
/*  835: 993 */     dateFld = "time1";
/*  836:     */     
/*  837: 995 */     xm = new IntegrationService().readTable(serverURL, "report", datetable, "uniqno", "uniqno", "<k1> = '<k2>'", dateFld, lastSyncTime, curSyncTime, null, new XBaseModel());
/*  838: 996 */     xm.set("@matchfield", "uniqno");
/*  839: 997 */     xm.setId("report");
/*  840: 998 */     container.append(xm);
/*  841:     */   }
/*  842:     */   
/*  843:     */   public void writeTaskAndData(String serverURL, XModel container)
/*  844:     */     throws Exception
/*  845:     */   {
/*  846:1006 */     for (int i = 0; i < container.getNumChildren(); i++)
/*  847:     */     {
/*  848:1008 */       XModel xm = container.get(i);
/*  849:     */       
/*  850:     */ 
/*  851:     */ 
/*  852:     */ 
/*  853:     */ 
/*  854:     */ 
/*  855:     */ 
/*  856:1016 */       System.out.println(xm.getId() + " " + xm.get("@matchfield") + " " + xm.getNumChildren());
/*  857:1018 */       if (xm.getNumChildren() > 0)
/*  858:     */       {
/*  859:1021 */         String[] keyFs = xm.get("@matchfield").toString().split(",");
/*  860:1022 */         new IntegrationService().write1(serverURL, xm.getId(), keyFs, xm);
/*  861:     */       }
/*  862:     */     }
/*  863:     */   }
/*  864:     */   
/*  865:     */   public static void main(String[] args)
/*  866:     */     throws Exception
/*  867:     */   {
/*  868:1040 */     System.out.println(" Accounts >>>>>");
/*  869:1041 */     XModel xm = new XBaseModel();
/*  870:     */     
/*  871:     */ 
/*  872:1044 */     String image = "06300065_01_05_0_blank.png";
/*  873:     */     
/*  874:     */ 
/*  875:     */ 
/*  876:     */ 
/*  877:1049 */     new IntegrationService().readCancellation("http://192.168.1.102:8080/cme/", "2014-02-01", "2014-03-01", xm, null);
/*  878:     */     
/*  879:     */ 
/*  880:     */ 
/*  881:     */ 
/*  882:1054 */     new IntegrationService().writeTaskAndData("http://192.168.1.102:8080/cme2-test/", xm);
/*  883:     */     
/*  884:     */ 
/*  885:     */ 
/*  886:     */ 
/*  887:1059 */     System.out.println(" Tasks >>>>>");
/*  888:     */     
/*  889:1061 */     System.out.println(" Process >>>>>");
/*  890:     */   }
/*  891:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-services2\ken-services2.jar
 * Qualified Name:     com.kentropy.services.IntegrationService
 * JD-Core Version:    0.7.0.1
 */
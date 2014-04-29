/*   1:    */ package com.kentropy.transfer;
/*   2:    */ 
/*   3:    */ import com.kentropy.db.TestXUIDB;
/*   4:    */ import com.kentropy.process.Process;
/*   5:    */ import java.io.File;
/*   6:    */ import java.io.IOException;
/*   7:    */ import java.io.PrintStream;
/*   8:    */ import java.io.PrintWriter;
/*   9:    */ import java.net.URLDecoder;
/*  10:    */ import java.util.Vector;
/*  11:    */ import javax.servlet.ServletContext;
/*  12:    */ import javax.servlet.ServletException;
/*  13:    */ import javax.servlet.http.Cookie;
/*  14:    */ import javax.servlet.http.HttpServletRequest;
/*  15:    */ import javax.servlet.http.HttpServletResponse;
/*  16:    */ import javax.servlet.http.HttpSession;
/*  17:    */ import net.xoetrope.xui.data.XBaseModel;
/*  18:    */ import net.xoetrope.xui.data.XModel;
/*  19:    */ import org.springframework.web.servlet.ModelAndView;
/*  20:    */ import org.springframework.web.servlet.mvc.Controller;
/*  21:    */ import sun.misc.BASE64Decoder;
/*  22:    */ 
/*  23:    */ public class TransferController
/*  24:    */   implements Controller
/*  25:    */ {
/*  26:    */   public static void main(String[] args)
/*  27:    */     throws Exception
/*  28:    */   {
/*  29: 43 */     File f = new File("d:\\tmp\\test.zip");
/*  30: 44 */     f.createNewFile();
/*  31:    */   }
/*  32:    */   
/*  33:    */   protected void service(HttpServletRequest request, HttpServletResponse response)
/*  34:    */     throws ServletException, IOException
/*  35:    */   {
/*  36:    */     try
/*  37:    */     {
/*  38: 87 */       String username = (String)request.getSession().getAttribute("username");
/*  39: 88 */       String password = (String)request.getSession().getAttribute("password");
/*  40: 89 */       if (username == null) {
/*  41: 90 */         throw new Exception("Not Logged In");
/*  42:    */       }
/*  43: 92 */       String action = request.getParameter("action");
/*  44:    */       
/*  45: 94 */       System.out.println("in" + request.getContentType() + " " + 
/*  46: 95 */         request.getMethod() + " " + request.getContentLength());
/*  47: 96 */       String path = request.getSession().getServletContext().getRealPath("/mbox");
/*  48: 97 */       if (action != null)
/*  49:    */       {
/*  50: 98 */         if (action.equals("getSession"))
/*  51:    */         {
/*  52:100 */           HttpSession session = request.getSession();
/*  53:    */           
/*  54:102 */           PrintWriter out = response.getWriter();
/*  55:103 */           System.out.println("Session ID: " + session.getId());out.println("<br>");
/*  56:    */         }
/*  57:106 */         if (action.equals("getSalt"))
/*  58:    */         {
/*  59:108 */           HttpSession session = request.getSession();
/*  60:    */           
/*  61:110 */           PrintWriter out = response.getWriter();
/*  62:111 */           System.out.println("Session ID: " + session.getId());out.println("<br>");
/*  63:112 */           Double salt = Double.valueOf(Math.random());
/*  64:113 */           System.out.println("SALT:" + salt);
/*  65:114 */           session.setAttribute("salt", salt);
/*  66:115 */           Cookie cookie = new Cookie("salt", salt);
/*  67:116 */           response.addCookie(cookie);
/*  68:    */         }
/*  69:119 */         if (action.equals("authenticate1"))
/*  70:    */         {
/*  71:121 */           HttpSession session = request.getSession();
/*  72:    */           
/*  73:123 */           PrintWriter out = response.getWriter();
/*  74:124 */           System.out.println("Session ID: " + session.getId());out.println("<br>");
/*  75:125 */           String salt = session.getAttribute("salt").toString();
/*  76:126 */           String cookieSalt = request.getHeader("Cookie");
/*  77:127 */           System.out.println("SALT1:" + salt);
/*  78:128 */           System.out.println("COOKIE:" + cookieSalt);
/*  79:    */         }
/*  80:134 */         if (action.equals("upload"))
/*  81:    */         {
/*  82:135 */           Server2 server = new Server2(request.getInputStream(), 
/*  83:136 */             response.getOutputStream());
/*  84:137 */           server.path = path;
/*  85:138 */           server.run();
/*  86:    */         }
/*  87:140 */         if (action.equals("download"))
/*  88:    */         {
/*  89:141 */           Server server = new Server(request.getInputStream(), 
/*  90:142 */             response.getOutputStream());
/*  91:143 */           server.path = path;
/*  92:144 */           server.run();
/*  93:    */         }
/*  94:146 */         if (action.equals("authenticate"))
/*  95:    */         {
/*  96:150 */           String credentials = request.getParameter("credentials");
/*  97:151 */           if ((credentials != null) && (credentials != ""))
/*  98:    */           {
/*  99:152 */             BASE64Decoder dec = new BASE64Decoder();
/* 100:153 */             byte[] b = dec.decodeBuffer(credentials);
/* 101:154 */             String[] credsArray = new String(b).split(":");
/* 102:    */             
/* 103:156 */             username = URLDecoder.decode(credsArray[0], "utf-8");
/* 104:157 */             password = URLDecoder.decode(credsArray[1], "utf-8");
/* 105:158 */             System.out.println("credentials:: username=" + username + " password:" + password);
/* 106:    */           }
/* 107:    */           else
/* 108:    */           {
/* 109:161 */             username = request.getParameter("username");
/* 110:162 */             password = request.getParameter("password");
/* 111:    */           }
/* 112:164 */           if (TestXUIDB.getInstance().isPhysician(username, password))
/* 113:    */           {
/* 114:165 */             XModel xm = new XBaseModel();
/* 115:166 */             TestXUIDB.getInstance().getPhysicianDetails(
/* 116:167 */               "username='" + username + "'", xm);
/* 117:    */             
/* 118:169 */             response.setContentType("text/xml");
/* 119:170 */             response.getWriter()
/* 120:171 */               .println(
/* 121:172 */               "<?xml version=\"1.0\" ?>\n<Datasets><data id=\"teamdata\"><data id=\"users\"><data id=\"" + 
/* 122:    */               
/* 123:    */ 
/* 124:    */ 
/* 125:176 */               username.toString().replace('@', '_') + 
/* 126:177 */               "\" code=\"" + 
/* 127:178 */               ((XModel)xm.get(0).get("id"))
/* 128:179 */               .get() + 
/* 129:180 */               "\" fullname=\"" + 
/* 130:181 */               ((XModel)xm.get(0).get(
/* 131:182 */               "name")).get() + 
/* 132:183 */               "\" password=\"" + 
/* 133:184 */               password + 
/* 134:185 */               "\" role=\"physician\" gender=\"1\">" + 
/* 135:186 */               "</data> " + 
/* 136:187 */               "</data>" + 
/* 137:188 */               "<data id=\"teams\">" + 
/* 138:189 */               "<data id=\"" + 
/* 139:190 */               ((XModel)xm.get(0).get("id"))
/* 140:191 */               .get() + 
/* 141:192 */               "\" name=\"team5\">" + 
/* 142:193 */               "<data value=\"" + 
/* 143:194 */               username + 
/* 144:195 */               "\" id=\"" + 
/* 145:196 */               ((XModel)xm.get(0).get("id"))
/* 146:197 */               .get() + 
/* 147:198 */               "\">" + 
/* 148:199 */               "</data>" + 
/* 149:200 */               "</data>" + 
/* 150:201 */               "</data>" + 
/* 151:202 */               "</data>" + 
/* 152:203 */               "</Datasets>");
/* 153:    */           }
/* 154:    */           else
/* 155:    */           {
/* 156:207 */             response.sendError(403, "Not Authenticated");
/* 157:    */           }
/* 158:    */         }
/* 159:214 */         if (action.equals("getTeamData"))
/* 160:    */         {
/* 161:215 */           XModel xm = new XBaseModel();
/* 162:216 */           TestXUIDB.getInstance().getPhysicianDetails(
/* 163:217 */             "username='" + username + "'", xm);
/* 164:    */           
/* 165:219 */           response.setContentType("text/xml");
/* 166:220 */           response.getWriter()
/* 167:221 */             .println(
/* 168:222 */             "<?xml version=\"1.0\" ?>\n<Datasets><data id=\"teamdata\"><data id=\"users\"><data id=\"" + 
/* 169:    */             
/* 170:    */ 
/* 171:    */ 
/* 172:226 */             username.toString().replace('@', '_') + 
/* 173:227 */             "\" code=\"" + 
/* 174:228 */             ((XModel)xm.get(0).get("id"))
/* 175:229 */             .get() + 
/* 176:230 */             "\" fullname=\"" + 
/* 177:231 */             ((XModel)xm.get(0).get(
/* 178:232 */             "name")).get() + 
/* 179:233 */             "\" password=\"" + 
/* 180:234 */             password + 
/* 181:235 */             "\" role=\"physician\" gender=\"1\">" + 
/* 182:236 */             "</data> " + 
/* 183:237 */             "</data>" + 
/* 184:238 */             "<data id=\"teams\">" + 
/* 185:239 */             "<data id=\"" + 
/* 186:240 */             ((XModel)xm.get(0).get("id"))
/* 187:241 */             .get() + 
/* 188:242 */             "\" name=\"team5\">" + 
/* 189:243 */             "<data value=\"" + 
/* 190:244 */             username + 
/* 191:245 */             "\" id=\"" + 
/* 192:246 */             ((XModel)xm.get(0).get("id"))
/* 193:247 */             .get() + 
/* 194:248 */             "\">" + 
/* 195:249 */             "</data>" + 
/* 196:250 */             "</data>" + 
/* 197:251 */             "</data>" + 
/* 198:252 */             "</data>" + 
/* 199:253 */             "</Datasets>");
/* 200:    */         }
/* 201:257 */         if (action.equals("process"))
/* 202:    */         {
/* 203:258 */           String uniqno = request.getParameter("uniqno");
/* 204:259 */           System.out.println("Processing pid=" + uniqno);
/* 205:    */           
/* 206:261 */           Process.createProcess(uniqno);
/* 207:262 */           Process.processTransitions();
/* 208:    */           
/* 209:264 */           PrintWriter out = response.getWriter();
/* 210:265 */           out.println("<html><body>Successful</body></html>");
/* 211:    */         }
/* 212:269 */         if ((action.equals("addphysician")) || (action.equals("updatephysician")))
/* 213:    */         {
/* 214:270 */           PrintWriter out = response.getWriter();
/* 215:    */           
/* 216:272 */           String id = request.getParameter("physician");
/* 217:    */           
/* 218:274 */           String status = request.getParameter("status");
/* 219:275 */           String name = request.getParameter("name");
/* 220:    */           
/* 221:    */ 
/* 222:278 */           String[] languagesArray = request
/* 223:279 */             .getParameterValues("lang1");
/* 224:280 */           String languages = "";
/* 225:281 */           String otherLanguages = request.getParameter("lang_other");
/* 226:282 */           if (otherLanguages.length() > 0) {
/* 227:283 */             languages = languages + "," + otherLanguages;
/* 228:    */           }
/* 229:285 */           String coder = request.getParameter("coder");
/* 230:286 */           coder = coder == null ? "" : coder;
/* 231:287 */           String adjudicator = request.getParameter("adjudicator");
/* 232:288 */           adjudicator = adjudicator == null ? "" : adjudicator;
/* 233:    */           
/* 234:290 */           boolean usernameExists = TestXUIDB.getInstance().physicianExists(username);
/* 235:291 */           boolean passwordLengthValidation = password.length() > 0;
/* 236:292 */           boolean usernameLengthValidation = username.length() > 0;
/* 237:293 */           boolean firstnameLengthValidation = name.length() > 0;
/* 238:294 */           boolean coderAdjudicatorSelected = (coder.equals("1")) || (adjudicator.equals("1"));
/* 239:295 */           boolean languagesSelected = true;
/* 240:    */           try
/* 241:    */           {
/* 242:298 */             for (int i = 0; i < languagesArray.length; i++) {
/* 243:299 */               languages = languages + (i == 0 ? "" : ",") + languagesArray[i];
/* 244:    */             }
/* 245:301 */             languagesSelected = languagesArray.length > 0;
/* 246:    */           }
/* 247:    */           catch (Exception e)
/* 248:    */           {
/* 249:303 */             languagesSelected = false;
/* 250:    */           }
/* 251:306 */           if ((!action.equals("updatephysician")) && (usernameExists))
/* 252:    */           {
/* 253:307 */             printMessage(out, "Username Already Taken", "addphysician.jsp");
/* 254:    */           }
/* 255:308 */           else if (!usernameLengthValidation)
/* 256:    */           {
/* 257:309 */             printMessage(out, "Please Enter Username", "addphysician.jsp");
/* 258:    */           }
/* 259:310 */           else if (!passwordLengthValidation)
/* 260:    */           {
/* 261:311 */             printMessage(out, "Please Enter Password", "addphysician.jsp");
/* 262:    */           }
/* 263:312 */           else if (!firstnameLengthValidation)
/* 264:    */           {
/* 265:313 */             printMessage(out, "Please Enter Your Name", "addphysician.jsp");
/* 266:    */           }
/* 267:314 */           else if (!languagesSelected)
/* 268:    */           {
/* 269:315 */             printMessage(out, "Please Select At least one Language", "addphysician.jsp");
/* 270:    */           }
/* 271:316 */           else if (!coderAdjudicatorSelected)
/* 272:    */           {
/* 273:317 */             printMessage(out, "Please Select Coder/Adjudicator", "addphysician.jsp");
/* 274:    */           }
/* 275:    */           else
/* 276:    */           {
/* 277:319 */             TestXUIDB.getInstance().createPhysician(username, name, languages, coder, adjudicator, id, status);
/* 278:320 */             TestXUIDB.getInstance().createAccount(username, password, "physician");
/* 279:321 */             if (action.equals("addphysician")) {
/* 280:322 */               printMessage(out, "Physician Added ", "addphysician.jsp");
/* 281:323 */             } else if (action.equals("updatephysician")) {
/* 282:324 */               printMessage(out, "Physician Updated ", "addphysician.jsp?action=" + action + "&physician=" + id);
/* 283:    */             } else {
/* 284:326 */               printMessage(out, "No Message Specified", "#");
/* 285:    */             }
/* 286:    */           }
/* 287:    */         }
/* 288:331 */         if (action.equals("adduser"))
/* 289:    */         {
/* 290:333 */           PrintWriter out = response.getWriter();
/* 291:    */           
/* 292:    */ 
/* 293:336 */           String[] rolesArray = request.getParameterValues("roles");
/* 294:337 */           String roles = "";
/* 295:338 */           for (int i = 0; i < rolesArray.length; i++) {
/* 296:339 */             roles = roles + (i == 0 ? "" : ",") + rolesArray[i];
/* 297:    */           }
/* 298:342 */           boolean usernameExists = TestXUIDB.getInstance().userExists(username);
/* 299:343 */           boolean passwordLengthValidation = password.length() > 0;
/* 300:344 */           boolean usernameLengthValidation = username.length() > 0;
/* 301:345 */           boolean rolesSelected = rolesArray.length > 0;
/* 302:347 */           if (usernameExists)
/* 303:    */           {
/* 304:348 */             printMessage(out, "Username Already Taken", "adduser.jsp");
/* 305:    */           }
/* 306:349 */           else if (!usernameLengthValidation)
/* 307:    */           {
/* 308:350 */             printMessage(out, "Please Enter Username", "adduser.jsp");
/* 309:    */           }
/* 310:351 */           else if (!passwordLengthValidation)
/* 311:    */           {
/* 312:352 */             printMessage(out, "Please Enter Password", "adduser.jsp");
/* 313:    */           }
/* 314:353 */           else if (!rolesSelected)
/* 315:    */           {
/* 316:354 */             printMessage(out, "Please Select Atleast one Role", "adduser.jsp");
/* 317:    */           }
/* 318:    */           else
/* 319:    */           {
/* 320:357 */             TestXUIDB.getInstance().createAccount(username, password, roles);
/* 321:358 */             printMessage(out, "User Successfully Created", "adduser.jsp");
/* 322:    */           }
/* 323:    */         }
/* 324:    */       }
/* 325:    */     }
/* 326:    */     catch (Exception e)
/* 327:    */     {
/* 328:366 */       e.printStackTrace();
/* 329:    */     }
/* 330:    */   }
/* 331:    */   
/* 332:    */   private void printMessage(PrintWriter out, String message, String url)
/* 333:    */   {
/* 334:382 */     out.println("<html><body>");
/* 335:383 */     out.println("<h2>" + message + "</h2>");
/* 336:384 */     out.println("<a href=\"" + url + "\" align=\"right\">Back</a>");
/* 337:385 */     out.println("</body></html>");
/* 338:    */   }
/* 339:    */   
/* 340:    */   /**
/* 341:    */    * @deprecated
/* 342:    */    */
/* 343:    */   public synchronized void createPhyLogs(String user, String id)
/* 344:    */   {
/* 345:396 */     Vector v = new Vector();
/* 346:397 */     v.add("username");
/* 347:    */     try
/* 348:    */     {
/* 349:399 */       String bookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 350:400 */       TestXUIDB.getInstance().createChangeLog("physician", 
/* 351:401 */         "where username='Prabhat Jha'", v);
/* 352:402 */       TestXUIDB.getInstance().sendServerLogs("admin", id, bookmark, 
/* 353:403 */         "999999");
/* 354:    */     }
/* 355:    */     catch (Exception e)
/* 356:    */     {
/* 357:406 */       e.printStackTrace();
/* 358:    */     }
/* 359:    */   }
/* 360:    */   
/* 361:    */   public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
/* 362:    */     throws Exception
/* 363:    */   {
/* 364:414 */     service(request, response);
/* 365:415 */     return null;
/* 366:    */   }
/* 367:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-transfer\ken-transfer.jar
 * Qualified Name:     com.kentropy.transfer.TransferController
 * JD-Core Version:    0.7.0.1
 */
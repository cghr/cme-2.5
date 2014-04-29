/*   1:    */ package com.kentropy.flow;
/*   2:    */ 
/*   3:    */ import com.kentropy.db.TestXUIDB;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ import net.xoetrope.xui.XProject;
/*   6:    */ import net.xoetrope.xui.XProjectManager;
/*   7:    */ import net.xoetrope.xui.data.XBaseModel;
/*   8:    */ import net.xoetrope.xui.data.XModel;
/*   9:    */ 
/*  10:    */ public class QuestionFlowManager
/*  11:    */ {
/*  12: 12 */   public boolean testmode = false;
/*  13: 13 */   public XModel flowModel = new XBaseModel();
/*  14: 14 */   public XModel flowParams = new XBaseModel();
/*  15: 15 */   public XModel dataModel = new XBaseModel();
/*  16: 16 */   public int current = -1;
/*  17: 17 */   QuestionFlowManager qfm = null;
/*  18: 19 */   public String inlineflow = null;
/*  19:    */   public XModel context;
/*  20:    */   public String currentContextType;
/*  21:    */   
/*  22:    */   public void setFlowModel(XModel qfModel)
/*  23:    */   {
/*  24: 25 */     this.flowModel = qfModel;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public XModel getFlowParams()
/*  28:    */   {
/*  29: 30 */     return this.flowParams;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void setFlowParams(XModel flowParams)
/*  33:    */   {
/*  34: 35 */     this.flowParams = flowParams;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void clearValues(XModel qModel)
/*  38:    */   {
/*  39: 39 */     String fieldStr = (String)qModel.get("@fields");
/*  40: 41 */     if (fieldStr == null) {
/*  41: 42 */       return;
/*  42:    */     }
/*  43: 44 */     String[] fields = fieldStr.split(",");
/*  44: 45 */     XModel dataM = new XBaseModel();
/*  45: 46 */     for (int i = 0; i < fields.length; i++)
/*  46:    */     {
/*  47: 48 */       ((XModel)dataM.get(fields[i])).set("");
/*  48: 49 */       System.out.println(fields[i]);
/*  49:    */     }
/*  50:    */     try
/*  51:    */     {
/*  52: 54 */       if (!this.testmode) {
/*  53: 55 */         TestXUIDB.getInstance().saveEnumData(this.context, this.currentContextType, dataM);
/*  54:    */       }
/*  55:    */     }
/*  56:    */     catch (Exception e)
/*  57:    */     {
/*  58: 58 */       e.printStackTrace();
/*  59:    */     }
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void goTo(int pos)
/*  63:    */   {
/*  64: 64 */     this.current = -1;
/*  65: 65 */     this.qfm = null;
/*  66: 66 */     System.out.println(" Curent pos " + pos + " " + this.current);
/*  67: 67 */     for (int i = 0; i <= pos; i++) {
/*  68: 69 */       nextQuestion();
/*  69:    */     }
/*  70:    */   }
/*  71:    */   
/*  72:    */   public XModel nextQuestion()
/*  73:    */   {
/*  74: 75 */     if (this.current != -1)
/*  75:    */     {
/*  76: 77 */       XModel qModel = this.flowModel.get(this.current);
/*  77: 78 */       if ((qModel != null) && (qModel.get("@type").equals("inlineflow")))
/*  78:    */       {
/*  79: 80 */         this.inlineflow = ((String)qModel.get("@flow"));
/*  80: 81 */         XModel n = this.qfm.nextQuestion();
/*  81: 82 */         if (n != null) {
/*  82: 84 */           return n;
/*  83:    */         }
/*  84: 87 */         this.qfm = null;
/*  85:    */       }
/*  86:    */     }
/*  87: 92 */     if (this.current >= this.flowModel.getNumChildren()) {
/*  88: 93 */       return null;
/*  89:    */     }
/*  90: 94 */     XModel qM = null;
/*  91: 95 */     boolean flag = false;
/*  92: 96 */     if (this.current >= 0)
/*  93:    */     {
/*  94: 98 */       String skip = getSkip(this.flowModel.get(this.current));
/*  95: 99 */       System.out.println("Skip " + skip);
/*  96:100 */       if (skip != null) {
/*  97:102 */         for (int i = this.current + 1; i < this.flowModel.getNumChildren(); i++)
/*  98:    */         {
/*  99:104 */           qM = this.flowModel.get(i);
/* 100:106 */           if (qM.getId().equals(skip))
/* 101:    */           {
/* 102:108 */             this.current = i;
/* 103:109 */             return qM;
/* 104:    */           }
/* 105:112 */           clearValues(qM);
/* 106:    */         }
/* 107:    */       }
/* 108:    */     }
/* 109:117 */     for (int i = this.current + 1; i < this.flowModel.getNumChildren(); i++)
/* 110:    */     {
/* 111:119 */       qM = this.flowModel.get(i);
/* 112:120 */       if (checkCondition(qM))
/* 113:    */       {
/* 114:122 */         if ((qM.get("@type") != null) && (qM.get("@type").equals("inlineflow")))
/* 115:    */         {
/* 116:122 */           XModel rootM = XProjectManager.getCurrentProject().getModel();
/* 117:123 */           XModel qM1 = (XModel)rootM.get("flows/" + qM.get("@flow"));
/* 118:124 */           this.qfm = new QuestionFlowManager();
/* 119:125 */           this.qfm.setFlowModel(qM1);
/* 120:126 */           this.qfm.dataModel = this.dataModel;
/* 121:127 */           this.qfm.setFlowParams(this.flowParams);
/* 122:128 */           this.qfm.context = this.context;
/* 123:129 */           this.qfm.currentContextType = this.currentContextType;
/* 124:    */           
/* 125:131 */           qM = this.qfm.nextQuestion();
/* 126:132 */           if (qM != null) {}
/* 127:    */         }
/* 128:    */         else
/* 129:    */         {
/* 130:132 */           flag = true;
/* 131:133 */           this.current = i;
/* 132:134 */           break;
/* 133:    */         }
/* 134:    */       }
/* 135:    */       else {
/* 136:137 */         clearValues(qM);
/* 137:    */       }
/* 138:    */     }
/* 139:142 */     System.out.println("--" + this.current + " " + this.flowModel.getNumChildren());
/* 140:143 */     return flag ? qM : null;
/* 141:    */   }
/* 142:    */   
/* 143:    */   public boolean checkCondition(XModel qModel)
/* 144:    */   {
/* 145:148 */     String exp = (String)qModel.get("@condition");
/* 146:149 */     if (exp != null)
/* 147:    */     {
/* 148:151 */       String[] exp1 = exp.split(";");
/* 149:152 */       boolean flg = true;
/* 150:153 */       for (int i = 0; i < exp1.length; i++)
/* 151:    */       {
/* 152:155 */         System.out.println(" Evaluating expression " + exp1[i]);
/* 153:156 */         flg &= evaluate0(exp1[i]).equals("true");
/* 154:    */       }
/* 155:158 */       return flg;
/* 156:    */     }
/* 157:161 */     return true;
/* 158:    */   }
/* 159:    */   
/* 160:    */   public boolean crossCheck(XModel qModel)
/* 161:    */   {
/* 162:166 */     String exp = (String)qModel.get("@crosscheck");
/* 163:167 */     if (this.qfm != null) {
/* 164:168 */       return this.qfm.crossCheck(qModel);
/* 165:    */     }
/* 166:169 */     if (exp != null)
/* 167:    */     {
/* 168:171 */       String[] exp1 = exp.split(";");
/* 169:172 */       boolean flg = true;
/* 170:173 */       for (int i = 0; i < exp1.length; i++)
/* 171:    */       {
/* 172:175 */         System.out.println(" Evaluating expression " + exp1[i]);
/* 173:176 */         flg &= evaluate0(exp1[i]).equals("true");
/* 174:    */       }
/* 175:178 */       return flg;
/* 176:    */     }
/* 177:181 */     return true;
/* 178:    */   }
/* 179:    */   
/* 180:    */   public String getSkip(XModel qModel)
/* 181:    */   {
/* 182:    */     try
/* 183:    */     {
/* 184:188 */       System.out.println(" ID is " + qModel.getId());
/* 185:189 */       XModel dataM = (XModel)this.dataModel.get(qModel.getId());
/* 186:190 */       System.out.println(" ID is " + dataM);
/* 187:191 */       String value = (String)dataM.get();
/* 188:192 */       System.out.println(" ID is " + value);
/* 189:193 */       if (qModel.get("@type").toString().equals("choice"))
/* 190:    */       {
/* 191:195 */         XModel options = (XModel)qModel.get("options");
/* 192:196 */         for (int i = 0; i < options.getNumChildren(); i++) {
/* 193:198 */           if (value.equals(options.get(i).get()))
/* 194:    */           {
/* 195:200 */             Object skip = options.get(i).get("@skip");
/* 196:201 */             if (skip != null) {
/* 197:202 */               return skip.toString();
/* 198:    */             }
/* 199:    */           }
/* 200:    */         }
/* 201:    */       }
/* 202:    */     }
/* 203:    */     catch (Exception e)
/* 204:    */     {
/* 205:209 */       e.printStackTrace();
/* 206:    */     }
/* 207:211 */     return null;
/* 208:    */   }
/* 209:    */   
/* 210:    */   public Comparable evaluate00(String token)
/* 211:    */   {
/* 212:216 */     System.out.println(" HELP " + token);
/* 213:217 */     if (token.startsWith("^"))
/* 214:    */     {
/* 215:219 */       System.out.println(" HELP1 " + token.substring(1));
/* 216:220 */       String tok = token.substring(1);
/* 217:221 */       String[] tokens = tok.split(":");
/* 218:222 */       Object[] conds = new Comparable[tokens.length];
/* 219:223 */       for (int i = 0; i < tokens.length; i++) {
/* 220:224 */         conds[i] = evaluate00(tokens[i]);
/* 221:    */       }
/* 222:    */       try
/* 223:    */       {
/* 224:226 */         Comparable tok1 = mathops(conds[0], conds[1], conds[2]);
/* 225:227 */         System.out.println(" MATH HELP " + tok1);
/* 226:228 */         return tok1;
/* 227:    */       }
/* 228:    */       catch (Exception e)
/* 229:    */       {
/* 230:232 */         return null;
/* 231:    */       }
/* 232:    */     }
/* 233:236 */     if (token.startsWith("$"))
/* 234:    */     {
/* 235:238 */       String qno = token.substring(1);
/* 236:239 */       System.out.println("qModel  is " + qno + " flowModel " + this.flowModel.getNumChildren());
/* 237:240 */       XModel qModel = (XModel)this.flowModel.get(qno);
/* 238:241 */       System.out.println("qModel  is " + qModel);
/* 239:242 */       String type = (String)qModel.get("@type");
/* 240:243 */       System.out.println("qModel  is " + qno + " type " + type);
/* 241:244 */       String value = (String)((XModel)this.dataModel.get(qno)).get();
/* 242:245 */       System.out.println("qModel  is " + qno + " type " + type + " value " + value);
/* 243:246 */       if (value != null)
/* 244:    */       {
/* 245:248 */         if (type.equals("age")) {
/* 246:250 */           return new Age(value.replace(' ', '_'));
/* 247:    */         }
/* 248:253 */         return value.replace(' ', '_');
/* 249:    */       }
/* 250:    */     }
/* 251:257 */     else if (token.startsWith("%"))
/* 252:    */     {
/* 253:259 */       String qno = token.substring(1);
/* 254:260 */       XModel paramModel = (XModel)this.flowParams.get(qno);
/* 255:261 */       String type = (String)paramModel.get("@type");
/* 256:262 */       String value = (String)paramModel.get();
/* 257:263 */       System.out.println("flow param  is " + qno + " type " + type + " value " + value);
/* 258:265 */       if (value != null)
/* 259:    */       {
/* 260:267 */         if ((type != null) && (type.equals("age"))) {
/* 261:269 */           return new Age(value.replace(' ', '_'));
/* 262:    */         }
/* 263:272 */         return value.replace(' ', '_');
/* 264:    */       }
/* 265:    */     }
/* 266:    */     else
/* 267:    */     {
/* 268:278 */       return token;
/* 269:    */     }
/* 270:280 */     return null;
/* 271:    */   }
/* 272:    */   
/* 273:    */   public String evaluate0(String exp)
/* 274:    */   {
/* 275:284 */     if (exp == null) {
/* 276:285 */       return "true";
/* 277:    */     }
/* 278:287 */     String[] tokens = exp.split(" ");
/* 279:288 */     Comparable[] condition = new Comparable[3];
/* 280:290 */     for (int i = 0; i < tokens.length; i++) {
/* 281:292 */       condition[i] = evaluate00(tokens[i]);
/* 282:    */     }
/* 283:295 */     String ret = evaluate1(condition[0], condition[1], condition[2]);
/* 284:296 */     System.out.println(ret);
/* 285:297 */     if (!ret.equals("true")) {
/* 286:297 */       return "false";
/* 287:    */     }
/* 288:299 */     return "true";
/* 289:    */   }
/* 290:    */   
/* 291:    */   public String evaluate(String exp)
/* 292:    */   {
/* 293:304 */     if (exp == null) {
/* 294:305 */       return "true";
/* 295:    */     }
/* 296:306 */     for (int i = 0; i <= this.current; i++)
/* 297:    */     {
/* 298:308 */       String field = (String)this.flowModel.get(i).get("@qno");
/* 299:309 */       System.out.println(field);
/* 300:310 */       if (field != null)
/* 301:    */       {
/* 302:312 */         String value = ((XModel)this.dataModel.get(field)).get().toString();
/* 303:313 */         System.out.println("Value=" + value + " " + this.flowModel.get(i).getId());
/* 304:314 */         String value1 = value.replaceAll(" ", "_");
/* 305:315 */         System.out.println("Value1=" + value1);
/* 306:316 */         exp = exp.replace("$" + this.flowModel.get(i).getId(), value1);
/* 307:317 */         System.out.println(">>>" + exp);
/* 308:    */       }
/* 309:    */     }
/* 310:320 */     System.out.println("Out");
/* 311:321 */     String[] exp1 = exp.split(";");
/* 312:322 */     System.out.println("No of conditions " + exp1.length);
/* 313:323 */     boolean flg = true;
/* 314:324 */     for (int i = 0; i < exp1.length; i++)
/* 315:    */     {
/* 316:326 */       String[] exp2 = exp1[i].split(" ");
/* 317:327 */       String ret = evaluate1(exp2[0], exp2[1], exp2[2]);
/* 318:328 */       System.out.println(ret);
/* 319:329 */       if (!ret.equals("true")) {
/* 320:329 */         return "false";
/* 321:    */       }
/* 322:    */     }
/* 323:331 */     return "true";
/* 324:    */   }
/* 325:    */   
/* 326:    */   public String evaluate1(Comparable left, Comparable op, Comparable right)
/* 327:    */   {
/* 328:342 */     throw new Error("Unresolved compilation problems: \n\tType mismatch: cannot convert from boolean to String\n\tType mismatch: cannot convert from boolean to String\n\tType mismatch: cannot convert from boolean to String\n\tType mismatch: cannot convert from boolean to String\n\tType mismatch: cannot convert from boolean to String\n\tType mismatch: cannot convert from boolean to String\n");
/* 329:    */   }
/* 330:    */   
/* 331:    */   public Comparable mathops(Object left, Object op, Object right)
/* 332:    */     throws Exception
/* 333:    */   {
/* 334:371 */     System.out.println(" HELP 2 " + left + " " + right + " " + op);
/* 335:372 */     if (left == null) {
/* 336:373 */       return null;
/* 337:    */     }
/* 338:374 */     NumberValue leftNum = new NumberValue(left);
/* 339:    */     
/* 340:376 */     NumberValue rightNum = new NumberValue(right);
/* 341:378 */     if (op.equals("add"))
/* 342:    */     {
/* 343:380 */       leftNum.add(rightNum);
/* 344:381 */       return leftNum.getValue();
/* 345:    */     }
/* 346:384 */     return "";
/* 347:    */   }
/* 348:    */   
/* 349:    */   public void testFlow(XModel testModel, XModel flowModel, XModel context, String contextType)
/* 350:    */   {
/* 351:389 */     this.flowModel = flowModel;
/* 352:390 */     this.context = context;
/* 353:391 */     this.currentContextType = contextType;
/* 354:392 */     this.dataModel = testModel;
/* 355:    */   }
/* 356:    */   
/* 357:    */   public static void main(String[] args)
/* 358:    */   {
/* 359:397 */     QuestionFlowManager qfm = new QuestionFlowManager();
/* 360:398 */     XModel data = new XBaseModel();
/* 361:399 */     XModel flow = new XBaseModel();
/* 362:400 */     ((XModel)data.get("1")).set("15");
/* 363:401 */     ((XModel)flow.get("1")).set("@type", "text");
/* 364:    */     
/* 365:403 */     data.set("@test1", "20");
/* 366:404 */     ((XModel)flow.get("1")).set("@field", "test1");
/* 367:405 */     ((XModel)flow.get("2")).set("@type", "text");
/* 368:406 */     ((XModel)flow.get("2")).set("19");
/* 369:407 */     ((XModel)flow.get("2")).set("@condition", "$1 lt 19");
/* 370:408 */     ((XModel)flow.get("3")).set("19");
/* 371:409 */     ((XModel)flow.get("3")).set("@type", "text");
/* 372:410 */     ((XModel)flow.get("3")).set("@condition", "^$1:add:21 lt 10");
/* 373:411 */     qfm.setFlowModel(flow);
/* 374:412 */     qfm.dataModel = data;
/* 375:413 */     System.out.println(qfm.nextQuestion().getId());
/* 376:414 */     System.out.println(qfm.nextQuestion().getId());
/* 377:415 */     System.out.println(qfm.nextQuestion().getId());
/* 378:    */   }
/* 379:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.flow.QuestionFlowManager
 * JD-Core Version:    0.7.0.1
 */
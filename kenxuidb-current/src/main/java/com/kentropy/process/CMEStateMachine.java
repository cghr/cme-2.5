/*   1:    */ package com.kentropy.process;
/*   2:    */ 
/*   3:    */ import com.kentropy.agents.cme.AssignmentAgent;
/*   4:    */ import com.kentropy.agents.cme.CodingMonitoringAgent;
/*   5:    */ import com.kentropy.agents.cme.CompletionAgent;
/*   6:    */ import com.kentropy.agents.cme.MatchingAgent;
/*   7:    */ import com.kentropy.agents.cme.ReMatchingAgent;
/*   8:    */ import com.kentropy.db.TestXUIDB;
/*   9:    */ import java.io.InputStream;
/*  10:    */ import java.util.Observable;
/*  11:    */ import java.util.StringTokenizer;
/*  12:    */ import java.util.Vector;
/*  13:    */ import org.apache.log4j.Logger;
/*  14:    */ 
/*  15:    */ public class CMEStateMachine
/*  16:    */   extends Observable
/*  17:    */   implements StateMachine
/*  18:    */ {
/*  19: 20 */   Logger logger = Logger.getLogger(getClass().getName());
/*  20: 21 */   public Vector agents = new Vector();
/*  21: 22 */   public String pid = "0";
/*  22: 24 */   public String[] states = { "assignment", "coding", "coding1", "matching", 
/*  23: 25 */     "reconc", "reconc1", "rematching", "adjudication", "complete" };
/*  24: 27 */   public int[][] transitions = { { 0, 2 }, { 2, 3 }, { 3, 4 }, { 4, 5 }, 
/*  25: 28 */     { 4, 9 } };
/*  26: 30 */   public String currentState = "started";
/*  27: 32 */   public boolean foundvadata = false;
/*  28: 34 */   public boolean foundimage = false;
/*  29: 36 */   public String assignedfirst = " ";
/*  30: 38 */   public String assignedsecond = " ";
/*  31: 40 */   public String codingCompleted = " ";
/*  32: 41 */   public String reconcCompleted = " ";
/*  33: 42 */   public String adjudicator = " ";
/*  34: 43 */   public String adjCompleted = " ";
/*  35: 44 */   public String codingUnassigned = " ";
/*  36: 45 */   public boolean reassignCoding = false;
/*  37: 46 */   public String adjUnassigned = " ";
/*  38: 47 */   public boolean reassignAdj = false;
/*  39: 48 */   public boolean cancelled = false;
/*  40: 50 */   public boolean sentimage = false;
/*  41: 52 */   public boolean sentvadata = false;
/*  42: 54 */   public boolean matchingResult = false;
/*  43: 56 */   public boolean sentcmecodingdata = false;
/*  44: 58 */   public boolean sentcmereconciliationdata = false;
/*  45: 60 */   public boolean reMatchingResult = false;
/*  46: 62 */   public int codingTasks = 0;
/*  47:    */   
/*  48:    */   public String getCurrentState()
/*  49:    */   {
/*  50: 66 */     return this.currentState;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void rollback()
/*  54:    */   {
/*  55: 70 */     if (this.currentState.equals("rematching")) {
/*  56: 71 */       this.currentState = "reconc1";
/*  57: 73 */     } else if (this.currentState.equals("complete"))
/*  58:    */     {
/*  59: 75 */       if ((!this.matchingResult) && (!this.reMatchingResult)) {
/*  60: 76 */         this.currentState = "adjudication";
/*  61: 78 */       } else if (!this.matchingResult) {
/*  62: 79 */         this.currentState = "rematching";
/*  63:    */       } else {
/*  64: 81 */         this.currentState = "matching";
/*  65:    */       }
/*  66:    */     }
/*  67: 84 */     else if (this.currentState.equals("cancelled")) {
/*  68: 85 */       this.currentState = "reassignment";
/*  69:    */     }
/*  70:    */   }
/*  71:    */   
/*  72:    */   public String transition()
/*  73:    */   {
/*  74:    */     try
/*  75:    */     {
/*  76: 92 */       if (this.currentState.equals("started"))
/*  77:    */       {
/*  78: 94 */         this.currentState = "assignment";
/*  79:    */         
/*  80: 96 */         return "InProcess";
/*  81:    */       }
/*  82: 99 */       if (this.currentState.equals("assignment"))
/*  83:    */       {
/*  84:101 */         if (!this.assignedsecond.trim().equals(""))
/*  85:    */         {
/*  86:103 */           this.currentState = "coding";
/*  87:    */           
/*  88:105 */           return "InProcess";
/*  89:    */         }
/*  90:108 */         this.currentState = "assignment";
/*  91:    */         
/*  92:110 */         return "InProcess";
/*  93:    */       }
/*  94:113 */       if (this.currentState.equals("reassignment"))
/*  95:    */       {
/*  96:115 */         if (!this.reassignCoding)
/*  97:    */         {
/*  98:117 */           if (!this.codingCompleted.trim().equals(""))
/*  99:    */           {
/* 100:119 */             if (this.codingCompleted.split(":").length == 1)
/* 101:    */             {
/* 102:121 */               this.currentState = "coding1";
/* 103:    */               
/* 104:123 */               return "InProcess";
/* 105:    */             }
/* 106:126 */             if (this.codingCompleted.split(":").length == 2) {
/* 107:128 */               return "InProcess";
/* 108:    */             }
/* 109:    */           }
/* 110:    */           else
/* 111:    */           {
/* 112:133 */             this.currentState = "coding";
/* 113:    */             
/* 114:135 */             return "InProcess";
/* 115:    */           }
/* 116:    */         }
/* 117:    */         else {
/* 118:139 */           return "InProcess";
/* 119:    */         }
/* 120:    */       }
/* 121:143 */       else if (this.currentState.equals("coding"))
/* 122:    */       {
/* 123:145 */         if ((!this.codingCompleted.trim().equals("")) && (this.codingCompleted.split(":").length == 1))
/* 124:    */         {
/* 125:147 */           this.currentState = "coding1";
/* 126:    */           
/* 127:149 */           return "InProcess";
/* 128:    */         }
/* 129:152 */         if (this.codingCompleted.split(":").length == 2)
/* 130:    */         {
/* 131:154 */           this.currentState = "matching";
/* 132:    */           
/* 133:156 */           return "InProcess";
/* 134:    */         }
/* 135:159 */         if (this.reassignCoding)
/* 136:    */         {
/* 137:160 */           this.currentState = "reassignment";
/* 138:    */           
/* 139:162 */           return "InProcess";
/* 140:    */         }
/* 141:    */       }
/* 142:166 */       else if (this.currentState.equals("coding1"))
/* 143:    */       {
/* 144:168 */         if (this.codingCompleted.split(":").length == 2)
/* 145:    */         {
/* 146:170 */           this.currentState = "matching";
/* 147:    */           
/* 148:172 */           return "InProcess";
/* 149:    */         }
/* 150:175 */         if (this.reassignCoding)
/* 151:    */         {
/* 152:177 */           this.currentState = "reassignment";
/* 153:    */           
/* 154:179 */           return "InProcess";
/* 155:    */         }
/* 156:    */       }
/* 157:183 */       else if (this.currentState.equals("matching"))
/* 158:    */       {
/* 159:185 */         if (this.cancelled)
/* 160:    */         {
/* 161:187 */           this.currentState = "cancelled";
/* 162:188 */           return "Terminated";
/* 163:    */         }
/* 164:191 */         if (this.matchingResult)
/* 165:    */         {
/* 166:193 */           this.currentState = "complete";
/* 167:    */           
/* 168:195 */           return "Completed";
/* 169:    */         }
/* 170:198 */         this.currentState = "reconc";
/* 171:    */       }
/* 172:200 */       else if (this.currentState.equals("reconc"))
/* 173:    */       {
/* 174:202 */         if ((!this.reconcCompleted.trim().equals("")) && (this.reconcCompleted.split(":").length == 1))
/* 175:    */         {
/* 176:204 */           this.currentState = "reconc1";
/* 177:    */           
/* 178:206 */           return "InProcess";
/* 179:    */         }
/* 180:209 */         if (this.reconcCompleted.split(":").length == 2)
/* 181:    */         {
/* 182:211 */           this.currentState = "rematching";
/* 183:    */           
/* 184:213 */           return "InProcess";
/* 185:    */         }
/* 186:    */       }
/* 187:217 */       else if (this.currentState.equals("reconc1"))
/* 188:    */       {
/* 189:219 */         if (this.reconcCompleted.split(":").length == 2)
/* 190:    */         {
/* 191:221 */           this.currentState = "rematching";
/* 192:    */           
/* 193:223 */           return "InProcess";
/* 194:    */         }
/* 195:    */       }
/* 196:    */       else
/* 197:    */       {
/* 198:228 */         if (this.currentState.equals("rematching"))
/* 199:    */         {
/* 200:230 */           this.logger.info("Rematching " + this.reMatchingResult);
/* 201:231 */           if (this.reMatchingResult)
/* 202:    */           {
/* 203:233 */             this.currentState = "complete";
/* 204:    */             
/* 205:235 */             return "Completed";
/* 206:    */           }
/* 207:238 */           this.currentState = "adjassignment";
/* 208:    */           
/* 209:240 */           return "InProcess";
/* 210:    */         }
/* 211:243 */         if (this.currentState.equals("adjassignment"))
/* 212:    */         {
/* 213:245 */           if (!this.adjudicator.trim().equals(""))
/* 214:    */           {
/* 215:247 */             this.currentState = "adjudication";
/* 216:248 */             return "InProcess";
/* 217:    */           }
/* 218:251 */           return "InProcess";
/* 219:    */         }
/* 220:253 */         if (this.currentState.equals("adjreassignment"))
/* 221:    */         {
/* 222:255 */           if (!this.adjudicator.trim().equals(""))
/* 223:    */           {
/* 224:257 */             if (this.adjUnassigned.trim().length() == 0)
/* 225:    */             {
/* 226:259 */               this.currentState = "adjudication";
/* 227:260 */               return "InProcess";
/* 228:    */             }
/* 229:263 */             return "InProcess";
/* 230:    */           }
/* 231:266 */           return "InProcess";
/* 232:    */         }
/* 233:269 */         if (this.currentState.equals("adjudication"))
/* 234:    */         {
/* 235:271 */           if (!this.adjCompleted.trim().equals(""))
/* 236:    */           {
/* 237:273 */             this.currentState = "complete";
/* 238:    */             
/* 239:275 */             return "InProcess";
/* 240:    */           }
/* 241:278 */           if (!this.adjUnassigned.trim().equals(""))
/* 242:    */           {
/* 243:280 */             this.currentState = "adjreassignment";
/* 244:281 */             return "InProcess";
/* 245:    */           }
/* 246:    */         }
/* 247:    */         else
/* 248:    */         {
/* 249:287 */           if (this.currentState.equals("cancelled")) {
/* 250:288 */             return "Terminated";
/* 251:    */           }
/* 252:291 */           this.currentState = getNextState();
/* 253:292 */           if (this.currentState.equals("complete")) {
/* 254:294 */             return "Completed";
/* 255:    */           }
/* 256:    */         }
/* 257:    */       }
/* 258:    */     }
/* 259:    */     catch (Exception e)
/* 260:    */     {
/* 261:303 */       e.printStackTrace();
/* 262:    */     }
/* 263:305 */     return "InProcess";
/* 264:    */   }
/* 265:    */   
/* 266:    */   public String getNextState()
/* 267:    */   {
/* 268:310 */     for (int i = 0; i < this.states.length - 1; i++) {
/* 269:311 */       if (this.states[i].equals(this.currentState)) {
/* 270:312 */         return this.states[(i + 1)];
/* 271:    */       }
/* 272:    */     }
/* 273:314 */     return this.states[(this.states.length - 1)];
/* 274:    */   }
/* 275:    */   
/* 276:    */   public static void main(String[] args)
/* 277:    */     throws Exception
/* 278:    */   {
/* 279:320 */     CMEStateMachine cmestat = new CMEStateMachine();
/* 280:321 */     cmestat.currentState = "assignment";
/* 281:322 */     Logger logger = Logger.getLogger(CMEStateMachine.class);
/* 282:323 */     for (int i = 0; i < 9; i++)
/* 283:    */     {
/* 284:324 */       cmestat.transition();
/* 285:325 */       logger.info(cmestat.currentState);
/* 286:    */     }
/* 287:328 */     cmestat.reassignCoding = true;
/* 288:329 */     for (int i = 0; i < 9; i++)
/* 289:    */     {
/* 290:330 */       cmestat.transition();
/* 291:331 */       logger.info(cmestat.currentState);
/* 292:    */     }
/* 293:334 */     cmestat.codingCompleted = "20";
/* 294:    */     
/* 295:336 */     cmestat.reassignCoding = false;
/* 296:337 */     for (int i = 0; i < 9; i++)
/* 297:    */     {
/* 298:338 */       cmestat.transition();
/* 299:339 */       logger.info(cmestat.currentState);
/* 300:    */     }
/* 301:341 */     System.in.read();
/* 302:342 */     cmestat.reconcCompleted = "20";
/* 303:343 */     for (int i = 0; i < 9; i++)
/* 304:    */     {
/* 305:344 */       cmestat.transition();
/* 306:345 */       logger.info(cmestat.currentState);
/* 307:    */     }
/* 308:347 */     cmestat.reconcCompleted = "20:21";
/* 309:348 */     cmestat.transition();
/* 310:349 */     logger.info(cmestat.currentState);
/* 311:350 */     cmestat.reMatchingResult = false;
/* 312:351 */     for (int i = 0; i < 9; i++)
/* 313:    */     {
/* 314:352 */       cmestat.transition();
/* 315:353 */       logger.info(cmestat.currentState);
/* 316:    */     }
/* 317:356 */     for (int i = 0; i < 9; i++)
/* 318:    */     {
/* 319:357 */       cmestat.transition();
/* 320:358 */       logger.info(cmestat.currentState);
/* 321:    */     }
/* 322:360 */     cmestat.adjudicator = "20";
/* 323:361 */     for (int i = 0; i < 9; i++)
/* 324:    */     {
/* 325:362 */       cmestat.transition();
/* 326:363 */       logger.info(cmestat.currentState);
/* 327:    */     }
/* 328:366 */     for (int i = 0; i < 9; i++)
/* 329:    */     {
/* 330:367 */       cmestat.transition();
/* 331:368 */       logger.info(cmestat.currentState);
/* 332:    */     }
/* 333:370 */     cmestat.adjCompleted = "25";
/* 334:371 */     for (int i = 0; i < 9; i++)
/* 335:    */     {
/* 336:372 */       cmestat.transition();
/* 337:373 */       logger.info(cmestat.currentState);
/* 338:    */     }
/* 339:    */   }
/* 340:    */   
/* 341:    */   public void onTaskStatusUpdate(String task, String status)
/* 342:    */   {
/* 343:392 */     throw new Error("Unresolved compilation problem: \n\tType mismatch: cannot convert from Object to String\n");
/* 344:    */   }
/* 345:    */   
/* 346:    */   public void deserialize(String s)
/* 347:    */   {
/* 348:462 */     StringTokenizer st = new StringTokenizer(s, ",");
/* 349:    */     
/* 350:464 */     this.pid = st.nextToken();
/* 351:465 */     this.currentState = st.nextToken();
/* 352:466 */     this.logger.info(this.currentState);
/* 353:467 */     this.foundvadata = Boolean.parseBoolean(st.nextToken());
/* 354:468 */     this.foundimage = Boolean.parseBoolean(st.nextToken());
/* 355:469 */     this.assignedfirst = st.nextToken();
/* 356:470 */     this.assignedsecond = st.nextToken();
/* 357:471 */     this.adjudicator = st.nextToken();
/* 358:472 */     this.sentimage = Boolean.parseBoolean(st.nextToken());
/* 359:473 */     this.sentvadata = Boolean.parseBoolean(st.nextToken());
/* 360:474 */     this.matchingResult = Boolean.parseBoolean(st.nextToken());
/* 361:475 */     this.reMatchingResult = Boolean.parseBoolean(st.nextToken());
/* 362:476 */     this.codingCompleted = st.nextToken();
/* 363:477 */     this.reconcCompleted = st.nextToken();
/* 364:478 */     this.adjCompleted = st.nextToken();
/* 365:479 */     this.codingUnassigned = st.nextToken();
/* 366:480 */     this.reassignCoding = Boolean.parseBoolean(st.nextToken());
/* 367:481 */     this.adjUnassigned = st.nextToken();
/* 368:482 */     this.reassignAdj = Boolean.parseBoolean(st.nextToken());
/* 369:483 */     if (st.hasMoreTokens()) {
/* 370:484 */       this.cancelled = Boolean.parseBoolean(st.nextToken());
/* 371:    */     }
/* 372:    */   }
/* 373:    */   
/* 374:    */   public String toString()
/* 375:    */   {
/* 376:489 */     return 
/* 377:    */     
/* 378:    */ 
/* 379:    */ 
/* 380:    */ 
/* 381:    */ 
/* 382:    */ 
/* 383:496 */       this.pid + "," + this.currentState + "," + this.foundvadata + "," + this.foundimage + "," + this.assignedfirst + "," + this.assignedsecond + "," + this.adjudicator + "," + this.sentimage + "," + this.sentvadata + "," + this.matchingResult + "," + this.reMatchingResult + "," + this.codingCompleted + "," + this.reconcCompleted + "," + this.adjCompleted + "," + this.codingUnassigned + "," + this.reassignCoding + "," + this.adjUnassigned + "," + this.reassignAdj + "," + this.cancelled;
/* 384:    */   }
/* 385:    */   
/* 386:    */   public void setPid(String pid)
/* 387:    */   {
/* 388:501 */     this.pid = pid;
/* 389:    */   }
/* 390:    */   
/* 391:    */   public void init()
/* 392:    */   {
/* 393:506 */     this.agents.add(new AssignmentAgent());
/* 394:507 */     this.agents.add(new CodingMonitoringAgent());
/* 395:508 */     this.agents.add(new MatchingAgent());
/* 396:509 */     this.agents.add(new ReMatchingAgent());
/* 397:510 */     this.agents.add(new CompletionAgent());
/* 398:    */   }
/* 399:    */   
/* 400:    */   public void runAgents(Process p)
/* 401:    */   {
/* 402:515 */     for (int i = 0; i < this.agents.size(); i++)
/* 403:    */     {
/* 404:516 */       Agent a = (Agent)this.agents.get(i);
/* 405:517 */       a.stateChange(p);
/* 406:518 */       TestXUIDB.getInstance().saveProcess(p);
/* 407:    */     }
/* 408:    */   }
/* 409:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.process.CMEStateMachine
 * JD-Core Version:    0.7.0.1
 */
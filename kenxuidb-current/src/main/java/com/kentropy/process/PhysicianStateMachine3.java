/*   1:    */ package com.kentropy.process;
/*   2:    */ 
/*   3:    */ import com.kentropy.agents.resource.physician.PhysicianMonitoringAgent3;
/*   4:    */ import com.kentropy.db.TestXUIDB;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.io.PrintStream;
/*   7:    */ import java.text.ParseException;
/*   8:    */ import java.text.SimpleDateFormat;
/*   9:    */ import java.util.Calendar;
/*  10:    */ import java.util.Date;
/*  11:    */ import java.util.StringTokenizer;
/*  12:    */ import java.util.Vector;
/*  13:    */ 
/*  14:    */ public class PhysicianStateMachine3
/*  15:    */   implements StateMachine
/*  16:    */ {
/*  17: 17 */   String pid = "";
/*  18: 18 */   public String currentState = " ";
/*  19: 20 */   public String stdate = " ";
/*  20: 21 */   public String eddate = " ";
/*  21: 22 */   String act_stdate = " ";
/*  22: 23 */   String act_eddate = " ";
/*  23: 24 */   public Vector<PhysicianStateMachine3> steps = new Vector();
/*  24: 25 */   public int duration = 0;
/*  25: 26 */   public int currentStep = -1;
/*  26: 27 */   int noOfSteps = 0;
/*  27: 28 */   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/*  28: 30 */   Vector agents = new Vector();
/*  29:    */   
/*  30:    */   PhysicianStateMachine3(String pid1, int duration1, Vector stps)
/*  31:    */   {
/*  32: 34 */     this.duration = duration1;
/*  33: 35 */     this.steps = stps;
/*  34:    */     
/*  35: 37 */     setPid(pid1);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public PhysicianStateMachine3 getCurStep()
/*  39:    */   {
/*  40: 42 */     if ((this.currentStep != -1) && (this.steps.size() > this.currentStep)) {
/*  41: 43 */       return ((PhysicianStateMachine3)this.steps.get(this.currentStep)).getCurStep();
/*  42:    */     }
/*  43: 45 */     return this;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void calculatePlan(String stdate1)
/*  47:    */     throws Exception
/*  48:    */   {
/*  49: 49 */     this.stdate = stdate1;
/*  50: 50 */     Calendar cal = Calendar.getInstance();
/*  51: 51 */     cal.setTime(this.sdf.parse(this.stdate));
/*  52: 53 */     for (int i = 0; i < this.steps.size(); i++)
/*  53:    */     {
/*  54: 55 */       PhysicianStateMachine3 psm = (PhysicianStateMachine3)this.steps.get(i);
/*  55: 56 */       String cur_stdate = this.sdf.format(cal.getTime());
/*  56:    */       
/*  57: 58 */       psm.calculatePlan(cur_stdate);
/*  58:    */       
/*  59: 60 */       this.duration += psm.duration;
/*  60: 61 */       cal.add(5, psm.duration);
/*  61:    */     }
/*  62: 64 */     cal = Calendar.getInstance();
/*  63: 65 */     cal.setTime(this.sdf.parse(this.stdate));
/*  64: 66 */     cal.add(5, this.duration);
/*  65: 67 */     this.eddate = this.sdf.format(cal.getTime());
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void extendPlan(int days)
/*  69:    */     throws Exception
/*  70:    */   {
/*  71: 73 */     Calendar cal = Calendar.getInstance();
/*  72:    */     
/*  73: 75 */     getCurStep().duration += days;
/*  74: 76 */     this.duration += days;
/*  75: 77 */     cal.setTime(this.sdf.parse(getCurStep().stdate));
/*  76: 78 */     for (int i = this.currentStep; i < this.steps.size(); i++)
/*  77:    */     {
/*  78: 80 */       PhysicianStateMachine3 psm = (PhysicianStateMachine3)this.steps.get(i);
/*  79: 81 */       String cur_stdate = this.sdf.format(cal.getTime());
/*  80:    */       
/*  81: 83 */       psm.calculatePlan(cur_stdate);
/*  82:    */       
/*  83: 85 */       cal.add(5, psm.duration);
/*  84:    */     }
/*  85: 88 */     cal = Calendar.getInstance();
/*  86: 89 */     cal.setTime(this.sdf.parse(this.stdate));
/*  87: 90 */     cal.add(5, this.duration);
/*  88: 91 */     this.eddate = this.sdf.format(cal.getTime());
/*  89:    */   }
/*  90:    */   
/*  91:    */   PhysicianStateMachine3(int duration1)
/*  92:    */   {
/*  93: 96 */     this.duration = duration1;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public PhysicianStateMachine3() {}
/*  97:    */   
/*  98:    */   public static PhysicianStateMachine3 createInstance(String pid, String dt)
/*  99:    */     throws Exception
/* 100:    */   {
/* 101:106 */     PhysicianStateMachine3 psm1 = new PhysicianStateMachine3(20);
/* 102:107 */     PhysicianStateMachine3 psm2 = new PhysicianStateMachine3(30);
/* 103:108 */     PhysicianStateMachine3 psm3 = new PhysicianStateMachine3(15);
/* 104:109 */     Vector steps = new Vector();
/* 105:110 */     steps.add(psm1);
/* 106:111 */     steps.add(psm2);
/* 107:112 */     steps.add(psm3);
/* 108:113 */     PhysicianStateMachine3 psm = new PhysicianStateMachine3(pid, 0, steps);
/* 109:114 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* 110:115 */     psm.calculatePlan(dt);
/* 111:116 */     System.out.println(psm);
/* 112:117 */     return psm;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public static void main(String[] args)
/* 116:    */     throws Exception
/* 117:    */   {
/* 118:123 */     Process p = TestXUIDB.getInstance().getProcess("808");
/* 119:124 */     PhysicianStateMachine3 ps5 = (PhysicianStateMachine3)p.states;
/* 120:125 */     ps5.extendPlan(10);
/* 121:126 */     TestXUIDB.getInstance().saveProcess(p);
/* 122:127 */     System.in.read();
/* 123:    */     
/* 124:129 */     PhysicianStateMachine3 psm1 = new PhysicianStateMachine3(33);
/* 125:130 */     PhysicianStateMachine3 psm2 = new PhysicianStateMachine3(33);
/* 126:131 */     PhysicianStateMachine3 psm3 = new PhysicianStateMachine3(33);
/* 127:132 */     Vector steps = new Vector();
/* 128:133 */     steps.add(psm1);
/* 129:134 */     steps.add(psm2);
/* 130:135 */     steps.add(psm3);
/* 131:    */     
/* 132:137 */     PhysicianStateMachine3 psm = new PhysicianStateMachine3("12", 0, steps);
/* 133:138 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* 134:139 */     psm.calculatePlan(sdf.format(new Date()));
/* 135:140 */     System.out.println(" " + psm.currentStep + " ");
/* 136:141 */     System.out.println(" " + psm.toString() + " ");
/* 137:142 */     psm.currentState = "inprocess";
/* 138:143 */     psm.transition();
/* 139:    */     
/* 140:145 */     psm.getCurStep().currentState = "inprocess";
/* 141:146 */     System.out.println(" " + psm.currentStep + " ");
/* 142:147 */     System.out.println(" " + psm.toString() + " ");
/* 143:    */     
/* 144:149 */     psm.transition();
/* 145:    */     
/* 146:151 */     psm.getCurStep().currentState = "complete";
/* 147:152 */     System.out.println(" " + psm.currentStep + " ");
/* 148:153 */     System.out.println(" " + psm.toString() + " ");
/* 149:    */     
/* 150:155 */     psm.transition();
/* 151:    */     
/* 152:157 */     psm.getCurStep().currentState = "inprocess";
/* 153:158 */     System.out.println(" " + psm.currentStep + " ");
/* 154:159 */     System.out.println(" " + psm.toString() + " ");
/* 155:160 */     psm.transition();
/* 156:    */     
/* 157:162 */     System.out.println(" " + psm.currentStep + " ");
/* 158:163 */     System.out.println(" " + psm.toString() + " ");
/* 159:    */   }
/* 160:    */   
/* 161:    */   public String getCurrentState()
/* 162:    */   {
/* 163:168 */     return this.currentState;
/* 164:    */   }
/* 165:    */   
/* 166:    */   public String transition()
/* 167:    */   {
/* 168:173 */     if (this.currentState.equals(" "))
/* 169:    */     {
/* 170:175 */       Calendar cal = Calendar.getInstance();
/* 171:176 */       this.act_stdate = this.sdf.format(cal.getTime());
/* 172:    */       
/* 173:178 */       cal.add(5, this.duration);
/* 174:    */       
/* 175:180 */       this.currentState = "started";
/* 176:181 */       return "InProcess";
/* 177:    */     }
/* 178:184 */     if (this.currentState.equals("inprocess"))
/* 179:    */     {
/* 180:186 */       if (this.currentStep == -1) {
/* 181:188 */         this.currentStep = 0;
/* 182:    */       }
/* 183:191 */       if (this.currentStep < this.steps.size())
/* 184:    */       {
/* 185:193 */         PhysicianStateMachine3 pst = (PhysicianStateMachine3)this.steps.get(this.currentStep);
/* 186:194 */         pst.transition();
/* 187:196 */         if (pst.currentState.equals("complete")) {
/* 188:198 */           if (this.currentStep < this.steps.size() - 1)
/* 189:    */           {
/* 190:200 */             this.currentStep += 1;
/* 191:201 */             ((PhysicianStateMachine3)this.steps.get(this.currentStep)).transition();
/* 192:    */           }
/* 193:    */           else
/* 194:    */           {
/* 195:205 */             this.currentState = "complete";
/* 196:206 */             return "Complete";
/* 197:    */           }
/* 198:    */         }
/* 199:    */       }
/* 200:213 */       if ((this.steps.size() > 0) && (this.currentStep >= this.steps.size()))
/* 201:    */       {
/* 202:215 */         this.currentState = "complete";
/* 203:216 */         return "Complete";
/* 204:    */       }
/* 205:    */       try
/* 206:    */       {
/* 207:221 */         Date test = this.sdf.parse(this.eddate);
/* 208:222 */         if (test.before(new Date()))
/* 209:    */         {
/* 210:224 */           this.currentState = "delayed";
/* 211:225 */           return "InProcess";
/* 212:    */         }
/* 213:228 */         if (test.equals(new Date()))
/* 214:    */         {
/* 215:230 */           this.currentState = "due";
/* 216:231 */           return "InProcess";
/* 217:    */         }
/* 218:234 */         if (test.after(new Date())) {
/* 219:    */           break label329;
/* 220:    */         }
/* 221:236 */         this.currentState = "inprocess";
/* 222:237 */         return "InProcess";
/* 223:    */       }
/* 224:    */       catch (ParseException e)
/* 225:    */       {
/* 226:242 */         e.printStackTrace();
/* 227:    */       }
/* 228:    */     }
/* 229:    */     else
/* 230:    */     {
/* 231:248 */       if (this.currentState.equals("complete")) {
/* 232:250 */         return "Complete";
/* 233:    */       }
/* 234:253 */       if (this.currentState.equals("terminated")) {
/* 235:255 */         return "Terminated";
/* 236:    */       }
/* 237:    */     }
/* 238:    */     label329:
/* 239:260 */     return "InProcess";
/* 240:    */   }
/* 241:    */   
/* 242:    */   public void onTaskStatusUpdate(String task, String status) {}
/* 243:    */   
/* 244:    */   public void deserialize(String s)
/* 245:    */   {
/* 246:269 */     StringTokenizer st = new StringTokenizer(s, ",");
/* 247:270 */     deserialize(st);
/* 248:    */   }
/* 249:    */   
/* 250:    */   public void deserialize(StringTokenizer st)
/* 251:    */   {
/* 252:275 */     this.pid = st.nextToken();
/* 253:276 */     this.currentState = st.nextToken();
/* 254:277 */     this.stdate = st.nextToken();
/* 255:278 */     this.eddate = st.nextToken();
/* 256:279 */     this.act_stdate = st.nextToken();
/* 257:280 */     this.act_eddate = st.nextToken();
/* 258:281 */     this.currentStep = Integer.parseInt(st.nextToken());
/* 259:282 */     this.duration = Integer.parseInt(st.nextToken());
/* 260:283 */     this.noOfSteps = Integer.parseInt(st.nextToken());
/* 261:284 */     for (int i = 0; i < this.noOfSteps; i++)
/* 262:    */     {
/* 263:286 */       PhysicianStateMachine3 psm = new PhysicianStateMachine3();
/* 264:287 */       psm.deserialize(st);
/* 265:288 */       this.steps.add(psm);
/* 266:    */     }
/* 267:    */   }
/* 268:    */   
/* 269:    */   public String toString()
/* 270:    */   {
/* 271:294 */     String str = this.pid + "," + this.currentState + "," + this.stdate + "," + this.eddate + "," + this.act_stdate + "," + this.act_eddate + "," + this.currentStep + "," + this.duration + "," + this.steps.size();
/* 272:296 */     for (int i = 0; i < this.steps.size(); i++)
/* 273:    */     {
/* 274:298 */       PhysicianStateMachine3 psm = (PhysicianStateMachine3)this.steps.get(i);
/* 275:299 */       str = str + "," + psm.toString();
/* 276:    */     }
/* 277:302 */     return str;
/* 278:    */   }
/* 279:    */   
/* 280:    */   public void setPid(String pid)
/* 281:    */   {
/* 282:307 */     this.pid = pid;
/* 283:308 */     for (int i = 0; i < this.steps.size(); i++) {
/* 284:310 */       ((PhysicianStateMachine3)this.steps.get(i)).setPid(pid);
/* 285:    */     }
/* 286:    */   }
/* 287:    */   
/* 288:    */   public void init()
/* 289:    */   {
/* 290:316 */     this.agents.add(new PhysicianMonitoringAgent3());
/* 291:    */   }
/* 292:    */   
/* 293:    */   public void runAgents(Process p)
/* 294:    */   {
/* 295:321 */     for (int i = 0; i < this.agents.size(); i++) {
/* 296:323 */       ((Agent)this.agents.get(i)).stateChange(p);
/* 297:    */     }
/* 298:    */   }
/* 299:    */   
/* 300:    */   public void rollback() {}
/* 301:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.process.PhysicianStateMachine3
 * JD-Core Version:    0.7.0.1
 */
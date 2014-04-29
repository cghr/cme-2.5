/*   1:    */ package com.kentropy.process.qa;
/*   2:    */ 
/*   3:    */ import com.kentropy.agents.AgentUtils;
/*   4:    */ import com.kentropy.agents.cme.CompletionAgent;
/*   5:    */ import com.kentropy.db.TestXUIDB;
/*   6:    */ import com.kentropy.process.CMEStateMachine;
/*   7:    */ import com.kentropy.process.Process;
/*   8:    */ import java.io.PrintStream;
/*   9:    */ import java.text.SimpleDateFormat;
/*  10:    */ import net.xoetrope.xui.data.XBaseModel;
/*  11:    */ import net.xoetrope.xui.data.XModel;
/*  12:    */ 
/*  13:    */ public class CMEProcessQA
/*  14:    */   implements ProcessQA
/*  15:    */ {
/*  16:    */   String pid;
/*  17:    */   Process p;
/*  18: 18 */   String billableMissing = "";
/*  19: 19 */   String completionMissing = "";
/*  20:    */   
/*  21:    */   public void setProcess(String pid)
/*  22:    */     throws Exception
/*  23:    */   {
/*  24: 22 */     this.pid = pid;
/*  25: 23 */     this.p = TestXUIDB.getInstance().getProcess(pid);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void runCorrection(String pid)
/*  29:    */   {
/*  30:    */     try
/*  31:    */     {
/*  32: 28 */       setProcess(pid);
/*  33: 29 */       correctBillable();
/*  34: 30 */       correctCompletion();
/*  35:    */     }
/*  36:    */     catch (Exception e)
/*  37:    */     {
/*  38: 34 */       e.printStackTrace();
/*  39:    */     }
/*  40:    */   }
/*  41:    */   
/*  42:    */   public boolean checkProcess(String pid)
/*  43:    */   {
/*  44:    */     try
/*  45:    */     {
/*  46: 41 */       this.billableMissing = "";
/*  47: 42 */       this.completionMissing = "";
/*  48: 43 */       setProcess(pid);
/*  49: 44 */       if (checkCompletion())
/*  50:    */       {
/*  51: 46 */         if (checkBillable()) {
/*  52: 47 */           return true;
/*  53:    */         }
/*  54:    */       }
/*  55:    */       else {
/*  56: 50 */         return false;
/*  57:    */       }
/*  58:    */     }
/*  59:    */     catch (Exception e)
/*  60:    */     {
/*  61: 53 */       e.printStackTrace();
/*  62:    */     }
/*  63: 55 */     return false;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public boolean checkBillable()
/*  67:    */   {
/*  68: 60 */     XModel xm = new XBaseModel();
/*  69:    */     
/*  70: 62 */     CMEStateMachine sm = (CMEStateMachine)this.p.states;
/*  71: 63 */     if (sm.currentState.equals("complete"))
/*  72:    */     {
/*  73: 65 */       String phy = "'" + sm.assignedfirst + "','" + sm.assignedsecond + "'";
/*  74: 66 */       TestXUIDB.getInstance().getData("billables", "physician_id", " report_id='" + this.pid + "' ", xm);
/*  75:    */       
/*  76: 68 */       boolean firstFound = false;
/*  77: 69 */       boolean secondFound = false;
/*  78: 70 */       boolean adjFound = sm.adjudicator.trim().length() <= 0;
/*  79: 72 */       for (int i = 0; i < xm.getNumChildren(); i++)
/*  80:    */       {
/*  81: 74 */         System.out.println(">>>Billable" + xm.get(i).get(0).get() + " " + sm.assignedfirst + " " + sm.assignedsecond + " " + sm.adjudicator);
/*  82: 75 */         if (xm.get(i).get(0).get().equals(sm.assignedfirst)) {
/*  83: 77 */           firstFound = true;
/*  84: 79 */         } else if (xm.get(i).get(0).get().equals(sm.assignedsecond)) {
/*  85: 81 */           secondFound = true;
/*  86: 83 */         } else if (sm.adjudicator.trim().length() > 0) {
/*  87: 85 */           if (xm.get(i).get(0).get().equals(sm.adjudicator)) {
/*  88: 87 */             adjFound = true;
/*  89:    */           }
/*  90:    */         }
/*  91:    */       }
/*  92: 94 */       if (!firstFound) {
/*  93: 95 */         this.billableMissing = (sm.assignedfirst + ",");
/*  94:    */       }
/*  95: 96 */       if (!secondFound) {
/*  96: 97 */         this.billableMissing = ((this.billableMissing.length() > 0 ? "," : "") + sm.assignedsecond);
/*  97:    */       }
/*  98: 98 */       if (!adjFound) {
/*  99: 99 */         this.billableMissing = ((this.billableMissing.length() > 0 ? "," : "") + sm.adjudicator);
/* 100:    */       }
/* 101:102 */       return (firstFound) && (secondFound) && (adjFound);
/* 102:    */     }
/* 103:105 */     return true;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public boolean checkCompletion()
/* 107:    */   {
/* 108:110 */     XModel xm = new XBaseModel();
/* 109:    */     
/* 110:112 */     CMEStateMachine sm = (CMEStateMachine)this.p.states;
/* 111:113 */     if (sm.currentState.equals("complete"))
/* 112:    */     {
/* 113:115 */       String phy = "'" + sm.assignedfirst + "','" + sm.assignedsecond + "'";
/* 114:116 */       TestXUIDB.getInstance().getData("cme_report", "physician", " uniqno='" + this.pid + "' ", xm);
/* 115:    */       
/* 116:118 */       boolean firstFound = false;
/* 117:119 */       boolean secondFound = false;
/* 118:120 */       boolean adjFound = sm.adjudicator.trim().length() <= 0;
/* 119:    */       
/* 120:122 */       System.out.println(">>>Completion " + this.pid + " " + xm.getNumChildren());
/* 121:123 */       for (int i = 0; i < xm.getNumChildren(); i++)
/* 122:    */       {
/* 123:125 */         System.out.println(">>>Completion " + xm.get(i).get(0).get() + " " + sm.assignedfirst + " " + sm.assignedsecond + " " + sm.adjudicator);
/* 124:126 */         if (xm.get(i).get(0).get().equals(sm.assignedfirst)) {
/* 125:128 */           firstFound = true;
/* 126:130 */         } else if (xm.get(i).get(0).get().equals(sm.assignedsecond)) {
/* 127:132 */           secondFound = true;
/* 128:134 */         } else if (sm.adjudicator.trim().length() > 0) {
/* 129:136 */           if (xm.get(i).get(0).get().equals(sm.adjudicator)) {
/* 130:138 */             adjFound = true;
/* 131:    */           }
/* 132:    */         }
/* 133:    */       }
/* 134:145 */       if (!firstFound) {
/* 135:146 */         this.completionMissing = (sm.assignedfirst + ",");
/* 136:    */       }
/* 137:147 */       if (!secondFound) {
/* 138:148 */         this.completionMissing = ((this.completionMissing.length() > 0 ? "," : "") + sm.assignedsecond);
/* 139:    */       }
/* 140:149 */       if (!adjFound) {
/* 141:150 */         this.completionMissing = ((this.completionMissing.length() > 0 ? "," : "") + sm.adjudicator);
/* 142:    */       }
/* 143:153 */       return (firstFound) && (secondFound) && (adjFound);
/* 144:    */     }
/* 145:156 */     return true;
/* 146:    */   }
/* 147:    */   
/* 148:    */   public void correctBillable()
/* 149:    */     throws Exception
/* 150:    */   {
/* 151:162 */     String[] test = this.billableMissing.split(",");
/* 152:163 */     CMEStateMachine sm = (CMEStateMachine)this.p.states;
/* 153:164 */     for (int i = 0; i < test.length; i++)
/* 154:    */     {
/* 155:166 */       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* 156:167 */       XModel xm = new XBaseModel();
/* 157:168 */       xm.set("report_id", this.pid);
/* 158:169 */       xm.set("physician_id", test[i]);
/* 159:170 */       xm.set("date_of_billable", sdf.format(sdf));
/* 160:171 */       String role = "coding";
/* 161:172 */       if (test.equals(sm.adjudicator)) {
/* 162:174 */         role = "adjudication";
/* 163:    */       }
/* 164:176 */       xm.set("date_of_billable", "role");
/* 165:    */     }
/* 166:179 */     AgentUtils.transform("coderpayment", new CompletionAgent(), this.p);
/* 167:    */   }
/* 168:    */   
/* 169:    */   public void correctCompletion()
/* 170:    */     throws Exception
/* 171:    */   {
/* 172:185 */     AgentUtils.transform("completion", new CompletionAgent(), this.p);
/* 173:    */   }
/* 174:    */   
/* 175:    */   public static void runQA(String[] args)
/* 176:    */   {
/* 177:    */     try
/* 178:    */     {
/* 179:191 */       XModel xm = new XBaseModel();
/* 180:192 */       TestXUIDB.getInstance().getData("process a left join process_qa b on a.pid=b.pid", "a.pid", "(b.status is null or b.status!='true')and stateMachine like '%compl%' and stateMachineClass='com.kentropy.process.CMEStateMachine'", xm);
/* 181:193 */       for (int i = 0; i < xm.getNumChildren(); i++)
/* 182:    */       {
/* 183:195 */         System.out.println(">>>>" + xm.get(i).get(0).get());
/* 184:196 */         String pid = xm.get(i).get(0).get().toString();
/* 185:197 */         CMEProcessQA qa = new CMEProcessQA();
/* 186:198 */         boolean tt = qa.checkProcess(pid);
/* 187:199 */         XModel qaM = new XBaseModel();
/* 188:200 */         qaM.set("pid", pid);
/* 189:201 */         qaM.set("billablemissing", qa.billableMissing);
/* 190:202 */         qaM.set("completionmissing", qa.completionMissing);
/* 191:203 */         qaM.set("status", Boolean.valueOf(tt));
/* 192:204 */         TestXUIDB.getInstance().saveData("process_qa", "pid='" + pid + "'", qaM);
/* 193:205 */         System.out.println(">>>>>" + tt);
/* 194:    */       }
/* 195:    */     }
/* 196:    */     catch (Exception e)
/* 197:    */     {
/* 198:210 */       e.printStackTrace();
/* 199:    */     }
/* 200:    */   }
/* 201:    */   
/* 202:    */   public static void runCorrection(String[] args)
/* 203:    */   {
/* 204:    */     try
/* 205:    */     {
/* 206:218 */       XModel xm = new XBaseModel();
/* 207:219 */       TestXUIDB.getInstance().getData("process_qa", "*", "status ='false' and correctionstatus is null  ", xm);
/* 208:220 */       for (int i = 0; i < xm.getNumChildren(); i++)
/* 209:    */       {
/* 210:222 */         System.out.println(">>>>" + xm.get(i).get(0).get());
/* 211:223 */         String pid = xm.get(i).get(0).get().toString();
/* 212:224 */         CMEProcessQA qa = new CMEProcessQA();
/* 213:225 */         qa.billableMissing = xm.get(i).get(1).get().toString();
/* 214:226 */         qa.completionMissing = xm.get(i).get(2).get().toString();
/* 215:227 */         qa.runCorrection(pid);
/* 216:    */         
/* 217:229 */         boolean tt = qa.checkProcess(pid);
/* 218:230 */         XModel qaM = new XBaseModel();
/* 219:231 */         qaM.set("pid", pid);
/* 220:232 */         qaM.set("billablemissing", qa.billableMissing);
/* 221:233 */         qaM.set("completionmissing", qa.completionMissing);
/* 222:234 */         qaM.set("finalstatus", Boolean.valueOf(tt));
/* 223:235 */         qaM.set("correctionstatus", Boolean.valueOf(true));
/* 224:236 */         TestXUIDB.getInstance().saveData("process_qa", "pid='" + pid + "'", qaM);
/* 225:    */       }
/* 226:    */     }
/* 227:    */     catch (Exception e)
/* 228:    */     {
/* 229:242 */       e.printStackTrace();
/* 230:    */     }
/* 231:    */   }
/* 232:    */   
/* 233:    */   public static void runReCorrection(String[] args)
/* 234:    */   {
/* 235:    */     try
/* 236:    */     {
/* 237:250 */       XModel xm = new XBaseModel();
/* 238:251 */       TestXUIDB.getInstance().getData("process_qa", "*", " finalstatus  ='false'  ", xm);
/* 239:252 */       for (int i = 0; i < xm.getNumChildren(); i++)
/* 240:    */       {
/* 241:254 */         System.out.println(">>>>" + xm.get(i).get(0).get());
/* 242:255 */         String pid = xm.get(i).get(0).get().toString();
/* 243:256 */         CMEProcessQA qa = new CMEProcessQA();
/* 244:257 */         qa.billableMissing = xm.get(i).get(1).get().toString();
/* 245:258 */         qa.completionMissing = xm.get(i).get(2).get().toString();
/* 246:259 */         qa.runCorrection(pid);
/* 247:    */         
/* 248:261 */         boolean tt = qa.checkProcess(pid);
/* 249:262 */         XModel qaM = new XBaseModel();
/* 250:263 */         qaM.set("pid", pid);
/* 251:264 */         qaM.set("billablemissing", qa.billableMissing);
/* 252:265 */         qaM.set("completionmissing", qa.completionMissing);
/* 253:266 */         qaM.set("finalstatus", Boolean.valueOf(tt));
/* 254:267 */         qaM.set("correctionstatus", Boolean.valueOf(true));
/* 255:268 */         TestXUIDB.getInstance().saveData("process_qa", "pid='" + pid + "'", qaM);
/* 256:    */       }
/* 257:    */     }
/* 258:    */     catch (Exception e)
/* 259:    */     {
/* 260:274 */       e.printStackTrace();
/* 261:    */     }
/* 262:    */   }
/* 263:    */   
/* 264:    */   public static void main(String[] args)
/* 265:    */     throws Exception
/* 266:    */   {}
/* 267:    */   
/* 268:    */   public boolean correctProcess(String pid)
/* 269:    */   {
/* 270:285 */     return false;
/* 271:    */   }
/* 272:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.process.qa.CMEProcessQA
 * JD-Core Version:    0.7.0.1
 */
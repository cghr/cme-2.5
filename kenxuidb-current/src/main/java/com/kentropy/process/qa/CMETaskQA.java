/*   1:    */ package com.kentropy.process.qa;
/*   2:    */ 
/*   3:    */ import com.kentropy.db.TestXUIDB;
/*   4:    */ import com.kentropy.process.Process;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.text.SimpleDateFormat;
/*   7:    */ import java.util.Date;
/*   8:    */ import net.xoetrope.xui.data.XBaseModel;
/*   9:    */ import net.xoetrope.xui.data.XModel;
/*  10:    */ 
/*  11:    */ public class CMETaskQA
/*  12:    */ {
/*  13:    */   String member;
/*  14:    */   String task;
/*  15: 15 */   String assignedto = "";
/*  16:    */   Process p;
/*  17: 17 */   String billableMissing = "";
/*  18: 18 */   String completionMissing = "";
/*  19: 19 */   public boolean endTimeStatus = false;
/*  20: 20 */   public boolean startTimeStatus = false;
/*  21:    */   
/*  22:    */   public void runCorrection(String task, String member, String assignedto)
/*  23:    */   {
/*  24:    */     try
/*  25:    */     {
/*  26: 26 */       this.task = task;
/*  27: 27 */       this.member = member;
/*  28: 28 */       this.assignedto = assignedto;
/*  29:    */     }
/*  30:    */     catch (Exception e)
/*  31:    */     {
/*  32: 32 */       e.printStackTrace();
/*  33:    */     }
/*  34:    */   }
/*  35:    */   
/*  36:    */   public boolean checkProcess(String task, String member, String assignedto)
/*  37:    */   {
/*  38:    */     try
/*  39:    */     {
/*  40: 39 */       this.task = task;
/*  41: 40 */       this.member = member;
/*  42: 41 */       this.assignedto = assignedto;
/*  43: 42 */       if (checkTime()) {
/*  44: 44 */         return true;
/*  45:    */       }
/*  46:    */     }
/*  47:    */     catch (Exception e)
/*  48:    */     {
/*  49: 48 */       e.printStackTrace();
/*  50:    */     }
/*  51: 50 */     return false;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public boolean correctProcess(String task, String member, String assignedto)
/*  55:    */   {
/*  56:    */     try
/*  57:    */     {
/*  58: 56 */       this.task = task;
/*  59: 57 */       this.member = member;
/*  60: 58 */       this.assignedto = assignedto;
/*  61: 59 */       correctTime();
/*  62:    */     }
/*  63:    */     catch (Exception e)
/*  64:    */     {
/*  65: 62 */       e.printStackTrace();
/*  66:    */     }
/*  67: 64 */     return false;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void correctTime()
/*  71:    */     throws Exception
/*  72:    */   {
/*  73: 70 */     XModel xm = new XBaseModel();
/*  74: 71 */     TestXUIDB.getInstance().getData("tasks", "dateassigned,startTime,endTime,dueDate", "task='" + this.task + "' and member='" + this.member + "' and assignedto='" + this.assignedto + "'", xm);
/*  75: 72 */     XModel taskM = xm.get(0);
/*  76: 73 */     XModel x2 = new XBaseModel();
/*  77: 74 */     x2.set("starttime", taskM.get(0).get());
/*  78: 75 */     x2.set("endtime", taskM.get(3).get());
/*  79: 76 */     System.out.println(" start time =" + taskM.get(0).get());
/*  80: 77 */     System.out.println(" end time =" + taskM.get(3).get());
/*  81: 78 */     TestXUIDB.getInstance().saveDataM1("tasks", "task='" + this.task + "' and member='" + this.member + "' and assignedto='" + this.assignedto + "'", x2);
/*  82:    */   }
/*  83:    */   
/*  84:    */   public boolean checkTime()
/*  85:    */   {
/*  86: 83 */     XModel xm = new XBaseModel();
/*  87:    */     try
/*  88:    */     {
/*  89: 86 */       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
/*  90: 87 */       SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
/*  91:    */       
/*  92: 89 */       TestXUIDB.getInstance().getData("tasks", "dateassigned,startTime,endTime,dueDate", "task='" + this.task + "' and member='" + this.member + "' and assignedto='" + this.assignedto + "'", xm);
/*  93:    */       
/*  94: 91 */       this.endTimeStatus = true;
/*  95: 92 */       this.startTimeStatus = true;
/*  96: 93 */       for (int i = 0; i < xm.getNumChildren(); i++)
/*  97:    */       {
/*  98: 95 */         Date dateassigned = sdf1.parse(xm.get(i).get(0).get().toString());
/*  99: 96 */         Date startTime = sdf1.parse(xm.get(i).get(1).get().toString());
/* 100: 97 */         Date endTime = sdf1.parse(xm.get(i).get(2).get().toString());
/* 101: 98 */         Date dueDate = sdf1.parse(xm.get(i).get(3).get().toString());
/* 102:100 */         if ((endTime.before(dateassigned)) || (endTime.after(new Date()))) {
/* 103:102 */           this.endTimeStatus = false;
/* 104:    */         }
/* 105:104 */         if ((startTime.before(dateassigned)) || (startTime.after(new Date())) || (startTime.after(endTime))) {
/* 106:106 */           this.startTimeStatus = false;
/* 107:    */         }
/* 108:    */       }
/* 109:111 */       return (this.endTimeStatus) && (this.startTimeStatus);
/* 110:    */     }
/* 111:    */     catch (Exception e)
/* 112:    */     {
/* 113:115 */       e.printStackTrace();
/* 114:    */     }
/* 115:118 */     return false;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public void correctBillable()
/* 119:    */     throws Exception
/* 120:    */   {}
/* 121:    */   
/* 122:    */   public void correctCompletion()
/* 123:    */     throws Exception
/* 124:    */   {}
/* 125:    */   
/* 126:    */   public static void runQA(String[] args)
/* 127:    */   {
/* 128:    */     try
/* 129:    */     {
/* 130:135 */       XModel xm = new XBaseModel();
/* 131:136 */       TestXUIDB.getInstance().getData("tasks a left join tasks_qa b on a.task=b.task and a.member=b.member and a.assignedto=b.assignedto ", "a.task,a.member,a.assignedto", "(b.status is null )and a.status='1' and a.task like '%task0/%'", xm);
/* 132:137 */       for (int i = 0; i < xm.getNumChildren(); i++)
/* 133:    */       {
/* 134:139 */         System.out.println(">>>>" + xm.get(i).get(0).get());
/* 135:140 */         String task = xm.get(i).get(0).get().toString();
/* 136:141 */         String member = xm.get(i).get(1).get().toString();
/* 137:142 */         String assignedto = xm.get(i).get(2).get().toString();
/* 138:143 */         CMETaskQA qa = new CMETaskQA();
/* 139:144 */         boolean tt = qa.checkProcess(task, member, assignedto);
/* 140:145 */         XModel qaM = new XBaseModel();
/* 141:146 */         qaM.set("task", task);
/* 142:147 */         qaM.set("member", member);
/* 143:148 */         qaM.set("assignedto", assignedto);
/* 144:149 */         qaM.set("endTimeStatus", Boolean.valueOf(qa.endTimeStatus));
/* 145:150 */         qaM.set("startTimeStatus", Boolean.valueOf(qa.startTimeStatus));
/* 146:151 */         qaM.set("status", Boolean.valueOf(tt));
/* 147:152 */         TestXUIDB.getInstance().saveData("tasks_qa", "task='" + task + "' and member='" + member + "' and assignedto='" + assignedto + "'", qaM);
/* 148:153 */         System.out.println(">>>>>" + tt);
/* 149:    */       }
/* 150:    */     }
/* 151:    */     catch (Exception e)
/* 152:    */     {
/* 153:158 */       e.printStackTrace();
/* 154:    */     }
/* 155:    */   }
/* 156:    */   
/* 157:    */   public static void runCorrection(String[] args)
/* 158:    */   {
/* 159:    */     try
/* 160:    */     {
/* 161:166 */       XModel xm = new XBaseModel();
/* 162:167 */       TestXUIDB.getInstance().getData(" tasks_qa", "task,member,assignedto", "correctionstatus is null and status='false'", xm);
/* 163:168 */       for (int i = 0; i < xm.getNumChildren(); i++)
/* 164:    */       {
/* 165:170 */         CMETaskQA qa = new CMETaskQA();
/* 166:171 */         XModel rowM = xm.get(i);
/* 167:172 */         String task = (String)rowM.get(0).get();
/* 168:173 */         String member = (String)rowM.get(1).get();
/* 169:174 */         String assignedto = (String)rowM.get(2).get();
/* 170:175 */         XModel xm1 = new XBaseModel();
/* 171:176 */         qa.correctProcess(task, member, assignedto);
/* 172:    */         
/* 173:178 */         boolean tt = qa.checkProcess(task, member, assignedto);
/* 174:179 */         XModel qaM = new XBaseModel();
/* 175:180 */         qaM.set("task", task);
/* 176:181 */         qaM.set("endTimeStatus", Boolean.valueOf(qa.endTimeStatus));
/* 177:182 */         qaM.set("startTimeStatus", Boolean.valueOf(qa.startTimeStatus));
/* 178:183 */         qaM.set("finalstatus", Boolean.valueOf(tt));
/* 179:184 */         qaM.set("correctionstatus", Boolean.valueOf(true));
/* 180:185 */         TestXUIDB.getInstance().saveData("tasks_qa", "task='" + task + "' and member='" + member + "' and assignedto='" + assignedto + "'", qaM);
/* 181:186 */         System.out.println(">>>>>" + tt);
/* 182:    */       }
/* 183:    */     }
/* 184:    */     catch (Exception e)
/* 185:    */     {
/* 186:191 */       e.printStackTrace();
/* 187:    */     }
/* 188:    */   }
/* 189:    */   
/* 190:    */   public static void runReCorrection(String[] args)
/* 191:    */   {
/* 192:    */     try
/* 193:    */     {
/* 194:199 */       XModel xm = new XBaseModel();
/* 195:200 */       TestXUIDB.getInstance().getData("tasks_qa", "*", " finalstatus  ='false'  ", xm);
/* 196:201 */       for (int i = 0; i < xm.getNumChildren(); i++)
/* 197:    */       {
/* 198:203 */         CMETaskQA qa = new CMETaskQA();
/* 199:204 */         XModel rowM = xm.get(0);
/* 200:205 */         String task = (String)rowM.get(0).get();
/* 201:206 */         String member = (String)rowM.get(1).get();
/* 202:207 */         String assignedto = (String)rowM.get(2).get();
/* 203:208 */         XModel xm1 = new XBaseModel();
/* 204:209 */         qa.correctProcess(task, member, assignedto);
/* 205:    */         
/* 206:211 */         boolean tt = qa.checkProcess(task, member, assignedto);
/* 207:212 */         XModel qaM = new XBaseModel();
/* 208:213 */         qaM.set("task", task);
/* 209:214 */         qaM.set("endTimeStatus", Boolean.valueOf(qa.endTimeStatus));
/* 210:215 */         qaM.set("startTimeStatus", Boolean.valueOf(qa.startTimeStatus));
/* 211:216 */         qaM.set("finalstatus", Boolean.valueOf(tt));
/* 212:217 */         qaM.set("correctionstatus", Boolean.valueOf(true));
/* 213:218 */         TestXUIDB.getInstance().saveData("tasks_qa", "task='" + task + " and member='" + member + "' and assignedto='" + assignedto + "'", qaM);
/* 214:219 */         System.out.println(">>>>>" + tt);
/* 215:    */       }
/* 216:    */     }
/* 217:    */     catch (Exception e)
/* 218:    */     {
/* 219:225 */       e.printStackTrace();
/* 220:    */     }
/* 221:    */   }
/* 222:    */   
/* 223:    */   public static void main(String[] args)
/* 224:    */     throws Exception
/* 225:    */   {
/* 226:232 */     runQA(args);
/* 227:233 */     runCorrection(args);
/* 228:    */   }
/* 229:    */   
/* 230:    */   public boolean correctProcess(String pid)
/* 231:    */   {
/* 232:238 */     return false;
/* 233:    */   }
/* 234:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.process.qa.CMETaskQA
 * JD-Core Version:    0.7.0.1
 */
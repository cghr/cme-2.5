/*   1:    */ package com.kentropy.agents.resource.physician;
/*   2:    */ 
/*   3:    */ import com.kentropy.db.TestXUIDB;
/*   4:    */ import com.kentropy.process.Agent;
/*   5:    */ import com.kentropy.process.PhysicianStateMachine;
/*   6:    */ import com.kentropy.process.Process;
/*   7:    */ import com.kentropy.process.notification.ProcessNotification;
/*   8:    */ import java.io.PrintStream;
/*   9:    */ import java.text.SimpleDateFormat;
/*  10:    */ import java.util.Date;
/*  11:    */ import net.xoetrope.xui.data.XBaseModel;
/*  12:    */ import net.xoetrope.xui.data.XModel;
/*  13:    */ 
/*  14:    */ public class PhysicianMonitoringAgent
/*  15:    */   implements Agent
/*  16:    */ {
/*  17: 17 */   PhysicianStateMachine sm = null;
/*  18:    */   
/*  19:    */   public void setPhysicianStatus(String phy, String status)
/*  20:    */     throws Exception
/*  21:    */   {
/*  22: 20 */     XModel dataM = new XBaseModel();
/*  23: 21 */     ((XModel)dataM.get("status")).set(status);
/*  24:    */     
/*  25: 23 */     TestXUIDB.getInstance().saveDataM1("physician", "id='" + phy + "'", dataM);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void stateChange(Process p)
/*  29:    */   {
/*  30: 28 */     this.sm = ((PhysicianStateMachine)p.states);
/*  31:    */     
/*  32: 30 */     String dt = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
/*  33: 32 */     if (this.sm.getCurrentState().equals("new"))
/*  34:    */     {
/*  35: 34 */       ProcessNotification pn = new ProcessNotification();
/*  36: 35 */       pn.queue(p.pid, "physician", "induction-welcome", "induction-welcome", "init");
/*  37: 36 */       this.sm.currentState = "active";
/*  38:    */       
/*  39: 38 */       Process.transition(p.pid);
/*  40:    */     }
/*  41: 41 */     if (this.sm.getCurrentState().equals("active"))
/*  42:    */     {
/*  43: 43 */       XModel xm = new XBaseModel();
/*  44: 44 */       System.out.println("away_date='" + dt + "' and  physician ='" + p.pid + "'");
/*  45: 45 */       TestXUIDB.getInstance().getData("physician_away", "count(*)", "away_date='" + dt + "' and  physician ='" + p.pid + "'", xm);
/*  46: 46 */       int count = Integer.parseInt(xm.get(0).get(0).get().toString());
/*  47: 47 */       if (count > 0)
/*  48:    */       {
/*  49: 49 */         this.sm.isOnVacation = true;
/*  50:    */         try
/*  51:    */         {
/*  52: 52 */           setPhysicianStatus(p.pid, "unavailable");
/*  53: 53 */           Process.transition(p.pid);
/*  54:    */         }
/*  55:    */         catch (Exception e)
/*  56:    */         {
/*  57: 56 */           e.printStackTrace();
/*  58:    */         }
/*  59:    */       }
/*  60:    */       else
/*  61:    */       {
/*  62: 62 */         xm = new XBaseModel();
/*  63: 63 */         System.out.println("status !=1 and dueDate < '" + dt + "' ");
/*  64: 64 */         TestXUIDB.getInstance().getData("tasks", "count(*)", "(status is null) and dueDate < '" + dt + "' and  assignedto ='" + p.pid + "'", xm);
/*  65: 65 */         System.out.println(" Select count(*) from tasks where ( status is null) and dueDate < '" + dt + "' and  assignedto ='" + p.pid + "'");
/*  66: 66 */         count = Integer.parseInt(xm.get(0).get(0).get().toString());
/*  67: 68 */         if (count > 0)
/*  68:    */         {
/*  69: 70 */           this.sm.isTempStopped = true;
/*  70:    */           try
/*  71:    */           {
/*  72: 72 */             setPhysicianStatus(p.pid, "tempstopped");
/*  73: 73 */             Process.transition(p.pid);
/*  74:    */           }
/*  75:    */           catch (Exception e)
/*  76:    */           {
/*  77: 76 */             e.printStackTrace();
/*  78:    */           }
/*  79:    */         }
/*  80:    */       }
/*  81:    */     }
/*  82: 85 */     if (this.sm.getCurrentState().equals("tempstopped"))
/*  83:    */     {
/*  84: 87 */       XModel xm = new XBaseModel();
/*  85: 88 */       TestXUIDB.getInstance().getData("tasks", "count(*)", "( status is null) and dueDate < '" + dt + "' and  assignedto ='" + p.pid + "'", xm);
/*  86: 89 */       int count = Integer.parseInt(xm.get(0).get(0).get().toString());
/*  87: 90 */       if (count == 0)
/*  88:    */       {
/*  89: 92 */         this.sm.isTempStopped = false;
/*  90:    */         try
/*  91:    */         {
/*  92: 94 */           setPhysicianStatus(p.pid, "active");
/*  93: 95 */           Process.transition(p.pid);
/*  94:    */         }
/*  95:    */         catch (Exception e)
/*  96:    */         {
/*  97: 98 */           e.printStackTrace();
/*  98:    */         }
/*  99:    */       }
/* 100:    */       else
/* 101:    */       {
/* 102:104 */         System.out.println("away_date='" + dt + "' and  physician ='" + p.pid + "'");
/* 103:105 */         TestXUIDB.getInstance().getData("physician_away", "count(*)", "away_date='" + dt + "' and  physician ='" + p.pid + "'", xm);
/* 104:106 */         int count1 = Integer.parseInt(xm.get(0).get(0).get().toString());
/* 105:107 */         if (count1 > 0)
/* 106:    */         {
/* 107:109 */           this.sm.isOnVacation = true;
/* 108:110 */           this.sm.isTempStopped = false;
/* 109:    */           try
/* 110:    */           {
/* 111:113 */             setPhysicianStatus(p.pid, "unavailable");
/* 112:114 */             Process.transition(p.pid);
/* 113:    */           }
/* 114:    */           catch (Exception e)
/* 115:    */           {
/* 116:117 */             e.printStackTrace();
/* 117:    */           }
/* 118:    */         }
/* 119:    */       }
/* 120:    */     }
/* 121:126 */     if (this.sm.getCurrentState().equals("unavailable"))
/* 122:    */     {
/* 123:128 */       XModel xm = new XBaseModel();
/* 124:129 */       TestXUIDB.getInstance().getData("physician_away", "count(*)", "away_date='" + dt + "' and  physician ='" + p.pid + "'", xm);
/* 125:130 */       int count = Integer.parseInt(xm.get(0).get(0).get().toString());
/* 126:131 */       if (count == 0)
/* 127:    */       {
/* 128:133 */         this.sm.isOnVacation = false;
/* 129:    */         try
/* 130:    */         {
/* 131:135 */           setPhysicianStatus(p.pid, "active");
/* 132:136 */           Process.transition(p.pid);
/* 133:    */         }
/* 134:    */         catch (Exception e)
/* 135:    */         {
/* 136:139 */           e.printStackTrace();
/* 137:    */         }
/* 138:    */       }
/* 139:    */     }
/* 140:    */   }
/* 141:    */   
/* 142:    */   public void batchExecute() {}
/* 143:    */   
/* 144:    */   public void getVacationStatus()
/* 145:    */   {
/* 146:151 */     this.sm.isOnVacation = false;
/* 147:    */   }
/* 148:    */   
/* 149:    */   public void getTempStopStatus()
/* 150:    */   {
/* 151:155 */     this.sm.isTempStopped = false;
/* 152:    */   }
/* 153:    */   
/* 154:    */   public void getStopStatus()
/* 155:    */   {
/* 156:159 */     this.sm.isStopped = false;
/* 157:    */   }
/* 158:    */   
/* 159:    */   public void getNoOfTasks()
/* 160:    */   {
/* 161:163 */     this.sm.noOfTasks = 0;
/* 162:    */   }
/* 163:    */   
/* 164:    */   public void getNoOfCompletedTasks()
/* 165:    */   {
/* 166:167 */     this.sm.noOfCompletedTasks = 0;
/* 167:    */   }
/* 168:    */   
/* 169:    */   public void getPerformanceStatus()
/* 170:    */   {
/* 171:171 */     this.sm.performing = true;
/* 172:    */   }
/* 173:    */   
/* 174:    */   public static void main(String[] args)
/* 175:    */   {
/* 176:    */     try
/* 177:    */     {
/* 178:178 */       Process p = Process.createProcess("1", "com.kentropy.process.PhysicianStateMachine");
/* 179:    */       
/* 180:180 */       Process.processTransitions();
/* 181:181 */       Process.processTransitions();
/* 182:    */     }
/* 183:    */     catch (Exception e)
/* 184:    */     {
/* 185:184 */       e.printStackTrace();
/* 186:    */     }
/* 187:    */   }
/* 188:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.agents.resource.physician.PhysicianMonitoringAgent
 * JD-Core Version:    0.7.0.1
 */
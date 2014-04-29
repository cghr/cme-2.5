/*   1:    */ package com.kentropy.process;
/*   2:    */ 
/*   3:    */ import com.kentropy.agents.resource.physician.PhysicianMonitoringAgent;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ import java.util.StringTokenizer;
/*   6:    */ import java.util.Vector;
/*   7:    */ 
/*   8:    */ public class PhysicianStateMachine
/*   9:    */   implements StateMachine
/*  10:    */ {
/*  11: 11 */   String pid = "";
/*  12: 13 */   public String currentState = "active";
/*  13: 14 */   public boolean performing = true;
/*  14: 15 */   public int noOfTasks = 0;
/*  15: 16 */   public int noOfCompletedTasks = 0;
/*  16: 17 */   public boolean isOnVacation = false;
/*  17: 18 */   public boolean isTempStopped = false;
/*  18: 19 */   public boolean isStopped = false;
/*  19: 21 */   Vector agents = new Vector();
/*  20:    */   
/*  21:    */   public static void main(String[] args) {}
/*  22:    */   
/*  23:    */   public String getCurrentState()
/*  24:    */   {
/*  25: 29 */     return this.currentState;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public String transition()
/*  29:    */   {
/*  30: 34 */     if (this.currentState.equals("new"))
/*  31:    */     {
/*  32: 36 */       if (this.noOfTasks > 0) {
/*  33: 38 */         this.currentState = "assigned";
/*  34:    */       }
/*  35: 40 */       return "InProcess";
/*  36:    */     }
/*  37: 42 */     if (this.currentState.equals("assigned"))
/*  38:    */     {
/*  39: 44 */       if (this.noOfCompletedTasks > 0) {
/*  40: 46 */         this.currentState = "active";
/*  41:    */       }
/*  42: 48 */       return "InProcess";
/*  43:    */     }
/*  44: 51 */     if (this.currentState.equals("active"))
/*  45:    */     {
/*  46: 53 */       System.out.println("In Active");
/*  47: 54 */       if (this.isOnVacation) {
/*  48: 56 */         this.currentState = "unavailable";
/*  49: 58 */       } else if (this.isTempStopped) {
/*  50: 60 */         this.currentState = "tempstopped";
/*  51:    */       }
/*  52: 62 */       if (this.isStopped) {
/*  53: 64 */         this.currentState = "stopped";
/*  54:    */       }
/*  55: 66 */       return "InProcess";
/*  56:    */     }
/*  57: 69 */     if (this.currentState.equals("unavailable"))
/*  58:    */     {
/*  59: 71 */       if (!this.isOnVacation)
/*  60:    */       {
/*  61: 73 */         if (this.isTempStopped) {
/*  62: 75 */           this.currentState = "tempstopped";
/*  63:    */         }
/*  64: 77 */         if (this.isStopped) {
/*  65: 79 */           this.currentState = "stopped";
/*  66:    */         } else {
/*  67: 82 */           this.currentState = "active";
/*  68:    */         }
/*  69:    */       }
/*  70: 87 */       return "InProcess";
/*  71:    */     }
/*  72: 90 */     if (this.currentState.equals("tempstopped"))
/*  73:    */     {
/*  74: 92 */       System.out.println("In Temp stopped " + this.isTempStopped);
/*  75: 93 */       if (!this.isTempStopped) {
/*  76: 95 */         if (this.isStopped) {
/*  77: 97 */           this.currentState = "stopped";
/*  78:    */         } else {
/*  79:100 */           this.currentState = "active";
/*  80:    */         }
/*  81:    */       }
/*  82:105 */       return "InProcess";
/*  83:    */     }
/*  84:107 */     return "InProcess";
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void onTaskStatusUpdate(String task, String status) {}
/*  88:    */   
/*  89:    */   public void deserialize(String s)
/*  90:    */   {
/*  91:116 */     StringTokenizer st = new StringTokenizer(s, ",");
/*  92:117 */     this.pid = st.nextToken();
/*  93:118 */     this.currentState = st.nextToken();
/*  94:119 */     this.performing = Boolean.parseBoolean(st.nextToken());
/*  95:120 */     this.noOfTasks = Integer.parseInt(st.nextToken());
/*  96:121 */     this.noOfCompletedTasks = Integer.parseInt(st.nextToken());
/*  97:122 */     this.isOnVacation = Boolean.parseBoolean(st.nextToken());
/*  98:123 */     this.isTempStopped = Boolean.parseBoolean(st.nextToken());
/*  99:124 */     this.isStopped = Boolean.parseBoolean(st.nextToken());
/* 100:    */   }
/* 101:    */   
/* 102:    */   public String toString()
/* 103:    */   {
/* 104:129 */     return this.pid + "," + this.currentState + "," + this.performing + "," + this.noOfTasks + "," + this.noOfCompletedTasks + "," + this.isOnVacation + "," + this.isTempStopped + "," + this.isStopped;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public void setPid(String pid)
/* 108:    */   {
/* 109:133 */     this.pid = pid;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public void init()
/* 113:    */   {
/* 114:138 */     this.agents.add(new PhysicianMonitoringAgent());
/* 115:    */   }
/* 116:    */   
/* 117:    */   public void runAgents(Process p)
/* 118:    */   {
/* 119:143 */     for (int i = 0; i < this.agents.size(); i++) {
/* 120:145 */       ((Agent)this.agents.get(i)).stateChange(p);
/* 121:    */     }
/* 122:    */   }
/* 123:    */   
/* 124:    */   public void rollback() {}
/* 125:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.process.PhysicianStateMachine
 * JD-Core Version:    0.7.0.1
 */
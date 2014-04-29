/*   1:    */ package com.kentropy.process;
/*   2:    */ 
/*   3:    */ import com.kentropy.agents.resource.physician.PhysicianMonitoringAgent2;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ import java.util.StringTokenizer;
/*   6:    */ import java.util.Vector;
/*   7:    */ 
/*   8:    */ public class PhysicianStateMachine2
/*   9:    */   implements StateMachine
/*  10:    */ {
/*  11: 11 */   String pid = "";
/*  12: 12 */   String currentState = "phase1";
/*  13: 14 */   public boolean p1Selected = false;
/*  14: 15 */   public boolean p2AssignmentCompleted = false;
/*  15: 16 */   public int p2ReportCount = 0;
/*  16: 17 */   public boolean p2Selected = false;
/*  17: 18 */   public boolean p2Assignment1Completed = false;
/*  18:    */   public boolean p1AssignmentCompleted;
/*  19: 20 */   Vector agents = new Vector();
/*  20:    */   
/*  21:    */   public static void main(String[] args) {}
/*  22:    */   
/*  23:    */   public String getCurrentState()
/*  24:    */   {
/*  25: 28 */     return this.currentState;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public String transition()
/*  29:    */   {
/*  30: 33 */     if (this.currentState.equals("phase1_assignment"))
/*  31:    */     {
/*  32: 35 */       if (this.p1AssignmentCompleted)
/*  33:    */       {
/*  34: 37 */         this.currentState = "phase1";
/*  35: 38 */         return "InProcess";
/*  36:    */       }
/*  37: 41 */       return "InProcess";
/*  38:    */     }
/*  39: 44 */     if (this.currentState.equals("phase1"))
/*  40:    */     {
/*  41: 46 */       if (this.p1Selected) {
/*  42: 48 */         this.currentState = "phase2assignment1";
/*  43:    */       }
/*  44: 50 */       return "InProcess";
/*  45:    */     }
/*  46: 53 */     if (this.currentState.equals("phase2assignment"))
/*  47:    */     {
/*  48: 55 */       if (this.p2AssignmentCompleted) {
/*  49: 57 */         this.currentState = "phase2assignment1";
/*  50:    */       }
/*  51:    */     }
/*  52: 61 */     else if (this.currentState.equals("phase2assignment1"))
/*  53:    */     {
/*  54: 63 */       if (this.p2Assignment1Completed) {
/*  55: 65 */         this.currentState = "phase2";
/*  56:    */       }
/*  57:    */     }
/*  58: 69 */     else if (this.currentState.equals("phase2"))
/*  59:    */     {
/*  60: 71 */       System.out.println("In Active");
/*  61: 72 */       if (this.p2Selected)
/*  62:    */       {
/*  63: 74 */         this.currentState = "accepted";
/*  64: 75 */         return "Complete";
/*  65:    */       }
/*  66: 78 */       return "InProcess";
/*  67:    */     }
/*  68: 81 */     return "InProcess";
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void onTaskStatusUpdate(String task, String status) {}
/*  72:    */   
/*  73:    */   public void deserialize(String s)
/*  74:    */   {
/*  75: 90 */     StringTokenizer st = new StringTokenizer(s, ",");
/*  76: 91 */     this.pid = st.nextToken();
/*  77: 92 */     this.currentState = st.nextToken();
/*  78:    */     
/*  79: 94 */     this.p1Selected = Boolean.parseBoolean(st.nextToken());
/*  80: 95 */     this.p2AssignmentCompleted = Boolean.parseBoolean(st.nextToken());
/*  81: 96 */     this.p2Assignment1Completed = Boolean.parseBoolean(st.nextToken());
/*  82: 97 */     this.p2ReportCount = Integer.parseInt(st.nextToken());
/*  83: 98 */     this.p2Selected = Boolean.parseBoolean(st.nextToken());
/*  84:    */   }
/*  85:    */   
/*  86:    */   public String toString()
/*  87:    */   {
/*  88:103 */     return this.pid + "," + this.currentState + "," + this.p1Selected + "," + this.p2AssignmentCompleted + "," + this.p2Assignment1Completed + "," + this.p2ReportCount + "," + this.p2Selected;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void setPid(String pid)
/*  92:    */   {
/*  93:107 */     this.pid = pid;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public void init()
/*  97:    */   {
/*  98:112 */     this.agents.add(new PhysicianMonitoringAgent2());
/*  99:    */   }
/* 100:    */   
/* 101:    */   public void runAgents(Process p)
/* 102:    */   {
/* 103:117 */     for (int i = 0; i < this.agents.size(); i++) {
/* 104:119 */       ((Agent)this.agents.get(i)).stateChange(p);
/* 105:    */     }
/* 106:    */   }
/* 107:    */   
/* 108:    */   public void rollback() {}
/* 109:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.process.PhysicianStateMachine2
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package com.kentropy.process;
/*   2:    */ 
/*   3:    */ import com.kentropy.agents.cme.AssignmentAgent2;
/*   4:    */ import com.kentropy.agents.cme.CompletionAgent2;
/*   5:    */ import com.kentropy.db.TestXUIDB;
/*   6:    */ import java.io.PrintStream;
/*   7:    */ import java.util.Observable;
/*   8:    */ import java.util.StringTokenizer;
/*   9:    */ import java.util.Vector;
/*  10:    */ 
/*  11:    */ public class CMEStateMachine3
/*  12:    */   extends Observable
/*  13:    */   implements StateMachine
/*  14:    */ {
/*  15: 14 */   public String pid = "0";
/*  16: 16 */   public String[] states = { "assignment", "coding", "complete" };
/*  17: 18 */   public int[][] transitions = { { 0, 2 }, { 2, 3 }, { 3, 4 }, { 4, 5 }, { 4, 9 } };
/*  18: 20 */   public String currentState = "started";
/*  19: 22 */   public boolean foundvadata = false;
/*  20: 24 */   public boolean foundimage = false;
/*  21: 26 */   public Vector assigned = new Vector();
/*  22: 27 */   public String assignedfirst = " ";
/*  23: 29 */   public String assignedsecond = " ";
/*  24: 31 */   public String adjudicator = " ";
/*  25: 33 */   public boolean sentimage = false;
/*  26: 35 */   public boolean sentvadata = false;
/*  27: 37 */   public boolean matchingResult = false;
/*  28: 39 */   public boolean sentcmecodingdata = false;
/*  29: 41 */   public boolean sentcmereconciliationdata = false;
/*  30: 43 */   public boolean reMatchingResult = false;
/*  31: 45 */   public int codingTasks = 0;
/*  32: 47 */   public Vector agents = new Vector();
/*  33:    */   
/*  34:    */   public String getCurrentState()
/*  35:    */   {
/*  36: 51 */     return this.currentState;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public String transition()
/*  40:    */   {
/*  41:    */     try
/*  42:    */     {
/*  43: 57 */       if (this.currentState.equals("started"))
/*  44:    */       {
/*  45: 59 */         this.currentState = "assignment";
/*  46:    */         
/*  47: 61 */         return "InProcess";
/*  48:    */       }
/*  49: 64 */       if (this.currentState.equals("assignment"))
/*  50:    */       {
/*  51: 66 */         this.currentState = "coding";
/*  52: 67 */         return "InProcess";
/*  53:    */       }
/*  54: 70 */       if (this.currentState.equals("complete")) {
/*  55: 72 */         return "Completed";
/*  56:    */       }
/*  57:    */     }
/*  58:    */     catch (Exception e)
/*  59:    */     {
/*  60: 78 */       e.printStackTrace();
/*  61:    */     }
/*  62: 80 */     return "InProcess";
/*  63:    */   }
/*  64:    */   
/*  65:    */   public String getNextState()
/*  66:    */   {
/*  67: 85 */     for (int i = 0; i < this.states.length - 1; i++) {
/*  68: 87 */       if (this.states[i].equals(this.currentState)) {
/*  69: 88 */         return this.states[(i + 1)];
/*  70:    */       }
/*  71:    */     }
/*  72: 90 */     return this.states[(this.states.length - 1)];
/*  73:    */   }
/*  74:    */   
/*  75:    */   public static void main(String[] args)
/*  76:    */   {
/*  77: 95 */     CMEStateMachine2 cmestat = new CMEStateMachine2();
/*  78: 96 */     cmestat.reMatchingResult = true;
/*  79: 97 */     for (int i = 0; i < 9; i++)
/*  80:    */     {
/*  81: 99 */       cmestat.transition();
/*  82:100 */       System.out.println(cmestat.currentState);
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void onTaskStatusUpdate(String task, String status)
/*  87:    */   {
/*  88:106 */     String pre = "taskdefinitions/cme_taskdefinition/task0/";
/*  89:107 */     if (task.equals(pre + "task0"))
/*  90:    */     {
/*  91:109 */       System.out.println(">>Current " + this.currentState);
/*  92:110 */       if ((this.currentState.equals("coding")) || (this.currentState.equals("coding1"))) {
/*  93:112 */         Process.transition(this.pid);
/*  94:    */       }
/*  95:    */     }
/*  96:116 */     if (task.equals(pre + "task1"))
/*  97:    */     {
/*  98:118 */       System.out.println(">>Current " + this.currentState);
/*  99:119 */       if ((this.currentState.equals("reconc")) || (this.currentState.equals("reconc1"))) {
/* 100:121 */         Process.transition(this.pid);
/* 101:    */       }
/* 102:    */     }
/* 103:124 */     if (task.equals(pre + "task2"))
/* 104:    */     {
/* 105:126 */       System.out.println(">>Current " + this.currentState);
/* 106:127 */       if (this.currentState.equals("adjudication")) {
/* 107:129 */         Process.transition(this.pid);
/* 108:    */       }
/* 109:    */     }
/* 110:    */   }
/* 111:    */   
/* 112:    */   public void deserialize(String s)
/* 113:    */   {
/* 114:136 */     StringTokenizer st = new StringTokenizer(s, ",");
/* 115:    */     
/* 116:138 */     this.pid = st.nextToken();
/* 117:    */     
/* 118:140 */     this.currentState = st.nextToken();
/* 119:141 */     System.out.println(this.currentState);
/* 120:142 */     this.foundvadata = Boolean.parseBoolean(st.nextToken());
/* 121:143 */     this.foundimage = Boolean.parseBoolean(st.nextToken());
/* 122:144 */     String ass = st.nextToken();
/* 123:145 */     String[] tt = ass.split(":");
/* 124:146 */     for (int i = 0; i < tt.length; i++) {
/* 125:148 */       this.assigned.add(tt[i]);
/* 126:    */     }
/* 127:150 */     if (st.hasMoreTokens()) {
/* 128:151 */       this.adjudicator = st.nextToken();
/* 129:    */     }
/* 130:    */   }
/* 131:    */   
/* 132:    */   public String toString()
/* 133:    */   {
/* 134:156 */     String ass = "";
/* 135:157 */     for (int i = 0; i < this.assigned.size(); i++) {
/* 136:159 */       ass = ass + (i == 0 ? "" : ":") + this.assigned.get(i);
/* 137:    */     }
/* 138:162 */     return this.pid + "," + this.currentState + "," + this.foundvadata + "," + this.foundimage + "," + ass + "," + this.adjudicator + "," + this.sentimage;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public void setPid(String pid)
/* 142:    */   {
/* 143:167 */     this.pid = pid;
/* 144:    */   }
/* 145:    */   
/* 146:    */   public void init()
/* 147:    */   {
/* 148:172 */     this.agents.add(new AssignmentAgent2());
/* 149:173 */     this.agents.add(new CompletionAgent2());
/* 150:    */   }
/* 151:    */   
/* 152:    */   public void runAgents(Process p)
/* 153:    */   {
/* 154:178 */     for (int i = 0; i < this.agents.size(); i++)
/* 155:    */     {
/* 156:179 */       Agent a = (Agent)this.agents.get(i);
/* 157:180 */       a.stateChange(p);
/* 158:181 */       TestXUIDB.getInstance().saveProcess(p);
/* 159:    */     }
/* 160:    */   }
/* 161:    */   
/* 162:    */   public void rollback() {}
/* 163:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.process.CMEStateMachine3
 * JD-Core Version:    0.7.0.1
 */
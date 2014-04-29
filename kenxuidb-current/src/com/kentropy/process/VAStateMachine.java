/*   1:    */ package com.kentropy.process;
/*   2:    */ 
/*   3:    */ import com.kentropy.agents.va.ImportAgent;
/*   4:    */ import com.kentropy.agents.va.PushToCMEAgent;
/*   5:    */ import com.kentropy.db.TestXUIDB;
/*   6:    */ import java.io.PrintStream;
/*   7:    */ import java.util.Observable;
/*   8:    */ import java.util.StringTokenizer;
/*   9:    */ import java.util.Vector;
/*  10:    */ 
/*  11:    */ public class VAStateMachine
/*  12:    */   extends Observable
/*  13:    */   implements StateMachine
/*  14:    */ {
/*  15: 14 */   public Vector agents = new Vector();
/*  16: 15 */   public String pid = "0";
/*  17: 16 */   public String domain = " ";
/*  18: 18 */   public String[] states = { "import", "pushtocme", "complete" };
/*  19: 20 */   public int[][] transitions = { { 0, 1 } };
/*  20: 22 */   public String currentState = "started";
/*  21: 24 */   public boolean foundvadata = false;
/*  22: 26 */   public boolean foundnarrative = false;
/*  23: 27 */   public boolean foundrespondentcod = false;
/*  24: 28 */   public boolean vacomplete = false;
/*  25: 30 */   public String surveyor = " ";
/*  26: 32 */   public boolean sentimage = false;
/*  27: 34 */   public boolean sentvadata = false;
/*  28: 36 */   public boolean matchingResult = false;
/*  29: 38 */   public boolean sentcmecodingdata = false;
/*  30: 40 */   public boolean sentcmereconciliationdata = false;
/*  31: 42 */   public boolean pushedToCME = false;
/*  32: 44 */   public int codingTasks = 0;
/*  33:    */   
/*  34:    */   public String getCurrentState()
/*  35:    */   {
/*  36: 48 */     return this.currentState;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public String transition()
/*  40:    */   {
/*  41:    */     try
/*  42:    */     {
/*  43: 54 */       if (this.currentState.equals("started"))
/*  44:    */       {
/*  45: 56 */         this.currentState = "import";
/*  46:    */         
/*  47: 58 */         return "InProcess";
/*  48:    */       }
/*  49: 61 */       if (this.currentState.equals("import"))
/*  50:    */       {
/*  51: 62 */         if (this.vacomplete)
/*  52:    */         {
/*  53: 64 */           this.currentState = "pushtocme";
/*  54:    */           
/*  55: 66 */           return "InProcess";
/*  56:    */         }
/*  57: 68 */         return "InProcess";
/*  58:    */       }
/*  59: 71 */       if (this.currentState.equals("pushtocme")) {
/*  60: 73 */         if (this.pushedToCME)
/*  61:    */         {
/*  62: 75 */           this.currentState = "complete";
/*  63:    */           
/*  64: 77 */           return "Completed";
/*  65:    */         }
/*  66:    */       }
/*  67:    */     }
/*  68:    */     catch (Exception e)
/*  69:    */     {
/*  70: 85 */       e.printStackTrace();
/*  71:    */     }
/*  72: 87 */     return "InProcess";
/*  73:    */   }
/*  74:    */   
/*  75:    */   public String getNextState()
/*  76:    */   {
/*  77: 92 */     for (int i = 0; i < this.states.length - 1; i++) {
/*  78: 94 */       if (this.states[i].equals(this.currentState)) {
/*  79: 95 */         return this.states[(i + 1)];
/*  80:    */       }
/*  81:    */     }
/*  82: 97 */     return this.states[(this.states.length - 1)];
/*  83:    */   }
/*  84:    */   
/*  85:    */   public static void main(String[] args)
/*  86:    */   {
/*  87:102 */     CMEStateMachine cmestat = new CMEStateMachine();
/*  88:103 */     cmestat.reMatchingResult = true;
/*  89:104 */     for (int i = 0; i < 9; i++)
/*  90:    */     {
/*  91:106 */       cmestat.transition();
/*  92:107 */       System.out.println(cmestat.currentState);
/*  93:    */     }
/*  94:    */   }
/*  95:    */   
/*  96:    */   public void onTaskStatusUpdate(String task, String status)
/*  97:    */   {
/*  98:113 */     String pre = "taskdefinitions/cme_taskdefinition/task0/";
/*  99:114 */     if (status.equals("5"))
/* 100:    */     {
/* 101:116 */       Process.transition(this.pid);
/* 102:117 */       this.currentState = ("reassign_" + this.currentState);
/* 103:118 */       return;
/* 104:    */     }
/* 105:121 */     if (task.equals(pre + "task0"))
/* 106:    */     {
/* 107:123 */       System.out.println(">>Current " + this.currentState);
/* 108:124 */       if ((this.currentState.equals("coding")) || (this.currentState.equals("coding1"))) {
/* 109:126 */         Process.transition(this.pid);
/* 110:    */       }
/* 111:    */     }
/* 112:130 */     if (task.equals(pre + "task1"))
/* 113:    */     {
/* 114:132 */       System.out.println(">>Current " + this.currentState);
/* 115:133 */       if ((this.currentState.equals("reconc")) || (this.currentState.equals("reconc1"))) {
/* 116:135 */         Process.transition(this.pid);
/* 117:    */       }
/* 118:    */     }
/* 119:138 */     if (task.equals(pre + "task2"))
/* 120:    */     {
/* 121:140 */       System.out.println(">>Current " + this.currentState);
/* 122:141 */       if (this.currentState.equals("adjudication")) {
/* 123:143 */         Process.transition(this.pid);
/* 124:    */       }
/* 125:    */     }
/* 126:    */   }
/* 127:    */   
/* 128:    */   public void deserialize(String s)
/* 129:    */   {
/* 130:150 */     StringTokenizer st = new StringTokenizer(s, ",");
/* 131:    */     
/* 132:152 */     this.pid = st.nextToken();
/* 133:    */     
/* 134:154 */     this.currentState = st.nextToken();
/* 135:155 */     this.domain = st.nextToken();
/* 136:156 */     System.out.println(this.currentState);
/* 137:157 */     this.foundvadata = Boolean.parseBoolean(st.nextToken());
/* 138:158 */     this.foundnarrative = Boolean.parseBoolean(st.nextToken());
/* 139:159 */     this.foundrespondentcod = Boolean.parseBoolean(st.nextToken());
/* 140:160 */     this.vacomplete = Boolean.parseBoolean(st.nextToken());
/* 141:161 */     this.surveyor = st.nextToken();
/* 142:    */     
/* 143:163 */     this.pushedToCME = Boolean.parseBoolean(st.nextToken());
/* 144:    */   }
/* 145:    */   
/* 146:    */   public String toString()
/* 147:    */   {
/* 148:168 */     return this.pid + "," + this.currentState + "," + this.domain + "," + this.foundvadata + "," + this.foundnarrative + "," + this.foundrespondentcod + "," + this.vacomplete + "," + this.surveyor + "," + this.pushedToCME;
/* 149:    */   }
/* 150:    */   
/* 151:    */   public void setPid(String pid)
/* 152:    */   {
/* 153:173 */     this.pid = pid;
/* 154:    */   }
/* 155:    */   
/* 156:    */   public void init()
/* 157:    */   {
/* 158:178 */     this.agents.add(new ImportAgent());
/* 159:179 */     this.agents.add(new PushToCMEAgent());
/* 160:    */   }
/* 161:    */   
/* 162:    */   public void runAgents(Process p)
/* 163:    */   {
/* 164:184 */     for (int i = 0; i < this.agents.size(); i++)
/* 165:    */     {
/* 166:186 */       Agent a = (Agent)this.agents.get(i);
/* 167:187 */       a.stateChange(p);
/* 168:188 */       TestXUIDB.getInstance().saveProcess(p);
/* 169:    */     }
/* 170:    */   }
/* 171:    */   
/* 172:    */   public void rollback() {}
/* 173:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.process.VAStateMachine
 * JD-Core Version:    0.7.0.1
 */
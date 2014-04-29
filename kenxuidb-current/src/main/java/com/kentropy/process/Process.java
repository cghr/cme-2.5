/*   1:    */ package com.kentropy.process;
/*   2:    */ 
/*   3:    */ import com.kentropy.db.TestXUIDB;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.util.Date;
/*   6:    */ import java.util.Hashtable;
/*   7:    */ import java.util.Vector;
/*   8:    */ import net.xoetrope.xui.data.XBaseModel;
/*   9:    */ import net.xoetrope.xui.data.XModel;
/*  10:    */ import org.apache.log4j.Logger;
/*  11:    */ 
/*  12:    */ public class Process
/*  13:    */ {
/*  14: 14 */   static Logger logger = Logger.getLogger(Process.class);
/*  15: 16 */   public String pid = "0";
/*  16: 17 */   public Date startTime = null;
/*  17: 18 */   public Date endTime = null;
/*  18: 19 */   public String status = "NotStarted";
/*  19: 20 */   public String stateMachineClass = "com.kentropy.process.CMEStateMachine";
/*  20: 21 */   public static Vector agents = new Vector();
/*  21: 23 */   public static Hashtable agentsHt = new Hashtable();
/*  22: 24 */   public static Vector transitionQueue = new Vector();
/*  23: 25 */   public static Vector processInstances = new Vector();
/*  24: 26 */   public StateMachine states = null;
/*  25: 28 */   public static Hashtable ht = new Hashtable();
/*  26:    */   
/*  27:    */   public static void main(String[] args)
/*  28:    */     throws Exception
/*  29:    */   {
/*  30: 33 */     String[] reports = { "01100009_01_01", "01100009_01_02", "01100009_01_03", "01100012_01_02", 
/*  31: 34 */       "01100013_01_01", "01100015_01_02", 
/*  32: 35 */       "03300118_01_01", "03300118_01_03", "03300118_01_04", "03300119_01_01", "03300119_01_02", "03300119_01_03", 
/*  33: 36 */       "03300184_01_01", "03300184_01_02", "03300184_01_03", "03300184_01_04" };
/*  34: 37 */     String[] reports1 = { "01100009_01_01" };
/*  35: 39 */     for (int i = 0; i < reports1.length; i++)
/*  36:    */     {
/*  37: 41 */       createProcess("/va/" + reports[i], "com.kentropy.process.VAStateMachine");
/*  38:    */       
/*  39: 43 */       processTransitions();
/*  40:    */       
/*  41: 45 */       processTransitions();
/*  42: 46 */       processTransitions();
/*  43:    */     }
/*  44: 48 */     System.in.read();
/*  45: 49 */     for (int i = 0; i < reports.length; i++)
/*  46:    */     {
/*  47: 51 */       createProcess(reports[i], "com.kentropy.process.CMEStateMachine");
/*  48: 52 */       processTransitions();
/*  49: 53 */       processTransitions();
/*  50:    */     }
/*  51: 56 */     logger.info("Status:" + getStatus("01100009_01_01"));
/*  52:    */     
/*  53: 58 */     System.in.read();
/*  54:    */     
/*  55: 60 */     taskStatusUpdate("01100009_01_01", "task0", "2");
/*  56:    */     
/*  57: 62 */     taskStatusUpdate("01100009_01_01", "task0", "2");
/*  58:    */     
/*  59: 64 */     logger.info("Status:" + getStatus("01100009_01_01"));
/*  60:    */     
/*  61: 66 */     logger.info("Status:" + getStatus("01100009_01_01"));
/*  62:    */   }
/*  63:    */   
/*  64:    */   public static void addAgent(Agent agent)
/*  65:    */   {
/*  66: 71 */     agents.add(agent);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public static void addAgent(String stateMachine, Agent agent)
/*  70:    */   {
/*  71: 75 */     Vector v = (Vector)agentsHt.get(stateMachine);
/*  72: 77 */     if (v == null) {
/*  73: 78 */       v = new Vector();
/*  74:    */     }
/*  75: 80 */     v.add(agent);
/*  76: 81 */     agentsHt.put(stateMachine, v);
/*  77:    */   }
/*  78:    */   
/*  79:    */   public static synchronized Process createProcess(String pid)
/*  80:    */     throws Exception
/*  81:    */   {
/*  82: 87 */     Process p = new Process();
/*  83:    */     
/*  84: 89 */     p.pid = pid;
/*  85: 90 */     p.startTime = new Date();
/*  86: 91 */     p.states = createStateMachine(p.stateMachineClass);
/*  87: 92 */     logger.info("PID " + p.pid + " " + pid);
/*  88: 93 */     p.states.setPid(pid);
/*  89: 94 */     TestXUIDB.getInstance().saveProcess(p);
/*  90:    */     
/*  91: 96 */     transition(p.pid);
/*  92:    */     
/*  93: 98 */     return p;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public static synchronized void refreshProcessStatus(String where)
/*  97:    */     throws Exception
/*  98:    */   {
/*  99:103 */     XModel xm = new XBaseModel();
/* 100:104 */     TestXUIDB.getInstance().getData("process", "pid", where, xm);
/* 101:105 */     logger.info(" No of children " + xm.getNumChildren());
/* 102:106 */     for (int i = 0; i < xm.getNumChildren(); i++)
/* 103:    */     {
/* 104:108 */       XModel rowM = xm.get(i);
/* 105:109 */       String pid = rowM.get(0).get().toString();
/* 106:110 */       transition(pid);
/* 107:111 */       processTransitions();
/* 108:    */     }
/* 109:    */   }
/* 110:    */   
/* 111:    */   public static synchronized Process createProcess(String pid, String stateMachineClassName)
/* 112:    */     throws Exception
/* 113:    */   {
/* 114:118 */     Process p = null;
/* 115:119 */     logger.info("Creating process " + pid + " " + stateMachineClassName);
/* 116:120 */     Process p1 = TestXUIDB.getInstance().getProcess(pid);
/* 117:121 */     if (p1 == null)
/* 118:    */     {
/* 119:123 */       p = new Process();
/* 120:124 */       p.pid = pid;
/* 121:125 */       p.startTime = new Date();
/* 122:126 */       p.endTime = new Date();
/* 123:127 */       p.states = createStateMachine(stateMachineClassName);
/* 124:128 */       logger.info("PID " + p.pid + " " + pid);
/* 125:129 */       p.states.setPid(pid);
/* 126:130 */       p.stateMachineClass = stateMachineClassName;
/* 127:131 */       TestXUIDB.getInstance().saveProcess(p, stateMachineClassName);
/* 128:    */       
/* 129:133 */       transition(p.pid);
/* 130:    */     }
/* 131:    */     else
/* 132:    */     {
/* 133:136 */       logger.info("process already created");
/* 134:    */     }
/* 135:138 */     return p;
/* 136:    */   }
/* 137:    */   
/* 138:    */   public static synchronized Process createProcess(String pid, StateMachine sm)
/* 139:    */     throws Exception
/* 140:    */   {
/* 141:143 */     Process p = null;
/* 142:144 */     logger.info("Creating process " + pid + " " + sm.getClass().getName());
/* 143:145 */     Process p1 = TestXUIDB.getInstance().getProcess(pid);
/* 144:146 */     if (p1 == null)
/* 145:    */     {
/* 146:148 */       p = new Process();
/* 147:149 */       p.pid = pid;
/* 148:150 */       p.startTime = new Date();
/* 149:151 */       p.endTime = new Date();
/* 150:152 */       p.states = sm;
/* 151:153 */       logger.info("PID " + p.pid + " " + pid);
/* 152:154 */       p.states.setPid(pid);
/* 153:155 */       p.stateMachineClass = sm.getClass().getName();
/* 154:156 */       TestXUIDB.getInstance().saveProcess(p, sm.getClass().getName());
/* 155:    */       
/* 156:158 */       transition(p.pid);
/* 157:    */     }
/* 158:    */     else
/* 159:    */     {
/* 160:161 */       logger.info("process already created");
/* 161:    */     }
/* 162:163 */     return p;
/* 163:    */   }
/* 164:    */   
/* 165:    */   public static void updateStatus(String pid, String status1)
/* 166:    */   {
/* 167:168 */     logger.info(pid);
/* 168:    */     try
/* 169:    */     {
/* 170:171 */       TestXUIDB.getInstance();Process p = TestXUIDB.getInstance().getProcess(pid);
/* 171:172 */       p.status = status1;
/* 172:173 */       TestXUIDB.getInstance();TestXUIDB.getInstance().saveProcess(p);
/* 173:    */     }
/* 174:    */     catch (Exception e)
/* 175:    */     {
/* 176:177 */       e.printStackTrace();
/* 177:    */     }
/* 178:    */   }
/* 179:    */   
/* 180:    */   public static StateMachine createStateMachine(String stateMachineClassName)
/* 181:    */     throws Exception
/* 182:    */   {
/* 183:184 */     StateMachine sm = null;
/* 184:185 */     Class clz = null;
/* 185:186 */     clz = Class.forName(stateMachineClassName);
/* 186:    */     
/* 187:188 */     sm = (StateMachine)clz.newInstance();
/* 188:    */     
/* 189:190 */     sm.init();
/* 190:    */     
/* 191:192 */     return sm;
/* 192:    */   }
/* 193:    */   
/* 194:    */   public static String getStatus(String pid)
/* 195:    */     throws Exception
/* 196:    */   {
/* 197:197 */     return TestXUIDB.getInstance().getProcess(pid).status;
/* 198:    */   }
/* 199:    */   
/* 200:    */   public static synchronized void taskStatusUpdate(String pid, String task, String taskStatus)
/* 201:    */     throws Exception
/* 202:    */   {
/* 203:201 */     if ((pid != "") && (!pid.equals("")))
/* 204:    */     {
/* 205:203 */       Process p = TestXUIDB.getInstance().getProcess(pid);
/* 206:204 */       logger.info(" task  is " + task);
/* 207:205 */       p.states.onTaskStatusUpdate(task, taskStatus);
/* 208:206 */       TestXUIDB.getInstance().saveProcess(p, p.stateMachineClass);
/* 209:207 */       processTransitions();
/* 210:    */     }
/* 211:    */   }
/* 212:    */   
/* 213:    */   public static Vector getProcesses(String status)
/* 214:    */   {
/* 215:213 */     return null;
/* 216:    */   }
/* 217:    */   
/* 218:    */   public static synchronized void transition(String pid)
/* 219:    */   {
/* 220:218 */     TestXUIDB.getInstance().saveTransition("0", pid, 0);
/* 221:    */   }
/* 222:    */   
/* 223:    */   public static void rollBack(String pid, String state)
/* 224:    */     throws Exception
/* 225:    */   {
/* 226:224 */     Process p = TestXUIDB.getInstance().getProcess(pid);
/* 227:225 */     p.states.rollback();
/* 228:    */   }
/* 229:    */   
/* 230:    */   public static synchronized void processTransitions()
/* 231:    */     throws Exception
/* 232:    */   {
/* 233:231 */     TestXUIDB db = TestXUIDB.getInstance();
/* 234:    */     for (;;)
/* 235:    */     {
/* 236:234 */       String[] ret = db.getNextTransition();
/* 237:235 */       logger.info(" RET " + ret);
/* 238:236 */       if (ret == null)
/* 239:    */       {
/* 240:238 */         db.clearPool();
/* 241:239 */         return;
/* 242:    */       }
/* 243:241 */       String pid = ret[1];
/* 244:242 */       logger.info("PID..." + pid);
/* 245:    */       
/* 246:244 */       Process p = db.getProcess(pid);
/* 247:245 */       p.status = p.states.transition();
/* 248:246 */       logger.info("state " + p.states.getCurrentState());
/* 249:247 */       runAgents(p, p.stateMachineClass);
/* 250:248 */       db.saveProcess(p);
/* 251:    */       
/* 252:250 */       db.saveTransition(ret[0], pid, 1);
/* 253:    */       
/* 254:252 */       System.gc();
/* 255:    */     }
/* 256:    */   }
/* 257:    */   
/* 258:    */   public static void run()
/* 259:    */     throws Exception
/* 260:    */   {
/* 261:    */     for (;;)
/* 262:    */     {
/* 263:261 */       processTransitions();
/* 264:262 */       Thread.currentThread();Thread.sleep(100L);
/* 265:    */     }
/* 266:    */   }
/* 267:    */   
/* 268:    */   public static void runAgents(Process p)
/* 269:    */   {
/* 270:268 */     for (int i = 0; i < agents.size(); i++)
/* 271:    */     {
/* 272:270 */       Agent a = (Agent)agents.get(i);
/* 273:271 */       a.stateChange(p);
/* 274:272 */       TestXUIDB.getInstance().saveProcess(p);
/* 275:    */     }
/* 276:    */   }
/* 277:    */   
/* 278:    */   public static void runAgents(Process p, String stateMachine)
/* 279:    */   {
/* 280:278 */     logger.info("Running agents");
/* 281:279 */     p.states.runAgents(p);
/* 282:    */   }
/* 283:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.process.Process
 * JD-Core Version:    0.7.0.1
 */